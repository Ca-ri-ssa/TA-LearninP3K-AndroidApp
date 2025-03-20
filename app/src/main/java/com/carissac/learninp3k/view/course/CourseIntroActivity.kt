package com.carissac.learninp3k.view.course

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityCourseIntroBinding

class CourseIntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseIntroBinding

    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(Injection.provideCourseRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCourseIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.courseIntro) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        val courseId = intent.getIntExtra(COURSE_ID, -1)
        if(courseId != -1) {
            courseViewModel.getIntroCourse(courseId)
        }

        observeCourseIntro()
        observeEnrollCourse()

        binding.btnLearnCourse.setOnClickListener {
            val intent = Intent(this, DetailCourseActivity::class.java)
            intent.putExtra(COURSE_ID, courseId)
            startActivity(intent)
        }

        binding.btnCourseEnroll.setOnClickListener {
            courseViewModel.enrollCourse(courseId)
        }
    }

    @SuppressLint("ResourceType")
    private fun observeCourseIntro() {
        courseViewModel.introCourseResult.observe(this) { result ->
            result.onSuccess { response ->
                Glide.with(this@CourseIntroActivity)
                    .load(response.courseImg)
                    .placeholder(R.drawable.img_placeholder)
                    .centerCrop()
                    .into(binding.ivCourse)

                binding.tvTitleCourse.text = response.courseName
                binding.tvCourseIntroDesc.text = response.courseIntro
                binding.tvCourseGoalDesc.text = response.courseGoal

                if(response.isEnrolled == true) {
                    binding.btnLearnCourse.visibility = View.VISIBLE
                    binding.btnCourseEnroll.visibility = View.INVISIBLE
                    binding.cdCourseScore.visibility = View.VISIBLE

                    val score = response.courseScore ?: 0
                    val styleScore = when {
                        score > 65 -> R.style.ScoreColorType_GoodPass
                        score >= 50 -> R.style.ScoreColorType_Pass
                        else -> R.style.ScoreColorType_NotPass
                    }

                    val typedArray = obtainStyledAttributes(styleScore, intArrayOf(
                        android.R.attr.background,
                        android.R.attr.textColor
                    ))
                    val backgroundColor = typedArray.getColor(0, android.graphics.Color.TRANSPARENT)
                    typedArray.recycle()

                    binding.tvCourseScoreNum.text = score.toString()
                    binding.tvCourseScore.setTextAppearance(styleScore)
                    binding.tvCourseScoreNum.setTextAppearance(styleScore)
                    binding.tvCourseScore.setBackgroundColor(backgroundColor)
                    binding.tvCourseScoreNum.setBackgroundColor(backgroundColor)
                    binding.cdCourseScore.setCardBackgroundColor(backgroundColor)
                } else {
                    binding.btnLearnCourse.visibility = View.INVISIBLE
                    binding.btnCourseEnroll.visibility = View.VISIBLE
                    binding.cdCourseScore.visibility = View.GONE
                }
            }.onFailure {
                showToast("Gagal memuat intro kelas")
            }
        }

        courseViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun observeEnrollCourse() {
        courseViewModel.enrollCourseResult.observe(this) { result ->
            result.onSuccess { response ->
                showToast("Kelas berhasil terdaftar")
                response.data?.courseId?.let { courseId->
                    courseViewModel.getIntroCourse(courseId)
                }
            }.onFailure {
                showToast("Kelas gagal didaftar, silahkan coba kembali")
            }
        }

        courseViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val COURSE_ID = "course_id"
    }
}