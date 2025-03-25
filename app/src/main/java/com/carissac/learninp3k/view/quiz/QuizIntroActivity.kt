package com.carissac.learninp3k.view.quiz

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityQuizIntroBinding

class QuizIntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizIntroBinding
    private lateinit var attemptHistoryAdapter: AttemptHistoryAdapter
    private var courseId: Int = -1

    private val quizViewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(Injection.provideAttemptRepository(this))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuizIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.quizIntro) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        courseId = intent.getIntExtra(COURSE_ID, -1)
        if(courseId != -1) {
            quizViewModel.getAllAttemptHistory(courseId)
        }

        attemptHistoryAdapter = AttemptHistoryAdapter(courseId)
        binding.rvAttemptHistory.apply {
            layoutManager = LinearLayoutManager(this@QuizIntroActivity)
            adapter = attemptHistoryAdapter
        }

        binding.btnStartQuiz.setOnClickListener {
            quizViewModel.resetSubmitQuizState()

            val currentItem = System.currentTimeMillis()
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("start_time", currentItem)
            intent.putExtra(COURSE_ID, courseId)
            startActivity(intent)
        }

        observeSubmitQuizState()
        observeAllAttemptHistory()
    }

    private fun observeSubmitQuizState() {
        quizViewModel.isQuizSubmitted.observe(this) { isSubmitted ->
            if(isSubmitted) {
                binding.btnStartQuiz.isEnabled = false
            } else {
                binding.btnStartQuiz.isEnabled = true
            }
        }
    }

    private fun observeAllAttemptHistory() {
        quizViewModel.allAttemptResult.observe(this) { result ->
            result.onSuccess { response ->
                val sortedAttempts = response.attempts
                if(!sortedAttempts.isNullOrEmpty()) {
                    val newestAttempt = sortedAttempts
                        .filter { it.createdAt?.isNotBlank() == true }
                        .sortedByDescending { it.createdAt }
                        .take(3)

                    attemptHistoryAdapter.submitList(newestAttempt)
                    binding.tvAttemptHistory.visibility = View.VISIBLE
                } else {
                    binding.tvAttemptHistory.visibility = View.GONE
                    binding.rvAttemptHistory.visibility = View.GONE
                }
            }.onFailure {
                showToast("Gagal memuat data riwayat percobaan")
            }
        }

        quizViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    override fun onResume() {
        super.onResume()
        if(courseId != -1) {
            quizViewModel.getAllAttemptHistory(courseId)
        }
        quizViewModel.resetSubmitQuizState()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String?) {
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