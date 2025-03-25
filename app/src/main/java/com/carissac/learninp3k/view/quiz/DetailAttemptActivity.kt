package com.carissac.learninp3k.view.quiz

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityDetailAttemptBinding
import com.carissac.learninp3k.databinding.ActivityQuizBinding
import com.carissac.learninp3k.view.quiz.QuizActivity
import kotlin.getValue

class DetailAttemptActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAttemptBinding
    private lateinit var detailAttemptAdapter: DetailAttemptAdapter
    private var courseId: Int = -1
    private var sessionId: Int = -1

    private val quizViewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(Injection.provideAttemptRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailAttemptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.detailAttempt) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        detailAttemptAdapter = DetailAttemptAdapter()
        binding.rvAttemptDetail.apply {
            layoutManager = LinearLayoutManager(this@DetailAttemptActivity)
            adapter = detailAttemptAdapter
        }

        courseId = intent.getIntExtra(COURSE_ID, -1)
        sessionId = intent.getIntExtra(SESSION_ID, -1)
        if(courseId != -1 && sessionId != -1) {
            quizViewModel.getDetailAttempt(courseId, sessionId)
        }

        observeDetailAttempt()
    }

    private fun observeDetailAttempt() {
        quizViewModel.detailAttemptResult.observe(this) { result ->
            result.onSuccess { response ->
                val score = response.attemptScore ?: 0
                val styleScore = when {
                    score > 65 -> R.style.ScoreColorType_GoodPass
                    score >= 50 -> R.style.ScoreColorType_Pass
                    else -> R.style.ScoreColorType_NotPass
                }

                val typedArray = obtainStyledAttributes(
                    styleScore, intArrayOf(
                        android.R.attr.background,
                        android.R.attr.textColor
                    )
                )
                val backgroundColor =
                    typedArray.getColor(0, android.graphics.Color.TRANSPARENT)
                typedArray.recycle()

                binding.tvAttemptScoreNum.text = score.toString()
                binding.tvAttemptScore.setTextAppearance(styleScore)
                binding.tvAttemptScoreNum.setTextAppearance(styleScore)
                binding.tvAttemptScore.setBackgroundColor(backgroundColor)
                binding.tvAttemptScoreNum.setBackgroundColor(backgroundColor)
                binding.cdAttemptScore.setCardBackgroundColor(backgroundColor)

                detailAttemptAdapter.submitList(response.attempts)
            }.onFailure {
                showToast("Gagal mengambil data detail percobaan")
            }
        }

        quizViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
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
        const val SESSION_ID = "attempt_session_id"
    }
}