package com.carissac.learninp3k.view.course

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityDetailCourseBinding
import com.carissac.learninp3k.view.quiz.QuizIntroActivity
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch

class DetailCourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCourseBinding
    private lateinit var imgCourseAdapter: ImageCourseAdapter
    private lateinit var youtubePlayerView: YouTubePlayerView
    private var intentCourseId: Int = -1
    private var detailCourseId: Int = -1

    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(Injection.provideCourseRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.detailCourse) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        imgCourseAdapter = ImageCourseAdapter()

        binding.rvImgCourse.apply {
            layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
            adapter = imgCourseAdapter
            setHasFixedSize(true)
        }

        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvImgCourse)

        intentCourseId = intent.getIntExtra(COURSE_ID, -1)
        if(intentCourseId != -1) {
            courseViewModel.getDetailCourse(intentCourseId)
        }

        observeDetailCourse()
    }

    private fun observeDetailCourse() {
        lifecycleScope.launch {
            courseViewModel.detailCourseResult.collect { result ->
                result?.onSuccess { response ->
                    binding.tvTitleCourse.text = response.courseName
                    binding.tvCourseContent.text = response.courseContent

                    detailCourseId = response.courseId ?: -1
                    setUpCourseVid(response.courseVid ?: "")

                    if (response.vidSource == "Smart Emergency") {
                        binding.tvVidSourceCreator.visibility = View.VISIBLE
                        binding.tvVidSourceCreator.text = "Sumber: ${response.vidSource}"
                    } else {
                        binding.tvVidSourceCreator.visibility = View.GONE
                    }

                    if (!response.imgCourseHeading.isNullOrEmpty() && !response.imgCourse.isNullOrEmpty()) {
                        binding.tvImgCourseTitle.text = response.imgCourseHeading
                        binding.tvImgCourseTitle.visibility = View.VISIBLE
                        binding.rvImgCourse.visibility = View.VISIBLE
                        binding.rvImgCourse.post {
                            imgCourseAdapter.submitList(response.imgCourse)
                        }
                    } else {
                        binding.tvImgCourseTitle.visibility = View.GONE
                        binding.rvImgCourse.visibility = View.GONE
                        imgCourseAdapter.submitList(emptyList())
                    }
                }?.onFailure {
                    showToast("Gagal memuat data detail kelas")
                }
            }
        }

        courseViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setUpCourseVid(courseVid: String) {
        youtubePlayerView = binding.vidDetailCourse

        lifecycle.addObserver(youtubePlayerView)

        val options = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1)
            .build()

        youtubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(courseVid, 0f)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_course_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_quiz -> {
                val intent = Intent(this, QuizIntroActivity::class.java)
                intent.putExtra(COURSE_ID, detailCourseId)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView.release()

        courseViewModel.getDetailCourse(detailCourseId)
    }

    companion object {
        const val COURSE_ID = "course_id"
    }
}