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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityQuizIntroBinding
import com.carissac.learninp3k.view.utils.formatTimer
import kotlinx.coroutines.launch

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
            observeCooldown()
        }

        attemptHistoryAdapter = AttemptHistoryAdapter(courseId)
        binding.rvAttemptHistory.apply {
            layoutManager = LinearLayoutManager(this@QuizIntroActivity)
            adapter = attemptHistoryAdapter
        }

        binding.btnStartQuiz.setOnClickListener {
            val currentItem = System.currentTimeMillis()
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("start_time", currentItem)
            intent.putExtra(COURSE_ID, courseId)
            startActivity(intent)
        }

        observeAllAttemptHistory()
    }

    private fun observeAllAttemptHistory() {
        quizViewModel.allAttemptResult.observe(this) { result ->
            result.onSuccess { response ->
                val sortedAttempts = response
                    .filter { it.createdAt?.isNotBlank() == true }
                    .sortedByDescending { it.createdAt }
                    .take(3)

                attemptHistoryAdapter.submitList(sortedAttempts)
            }.onFailure {
                showToast("Gagal memuat data riwayat percobaan")
            }
        }

        quizViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun observeCooldown() {
        lifecycleScope.launch {
            quizViewModel.isCooldownActive(courseId).collect { isActive ->
                if (isActive) {
                    binding.btnStartQuiz.isEnabled = false
                    binding.timerMessage.visibility = View.VISIBLE

                    quizViewModel.getCooldownTime(courseId)

                    quizViewModel.cooldownTime.collect { remainingTime ->
                        if (remainingTime > 0) {
                            binding.tvTimerNum.text = formatTimer(remainingTime)
                        } else {
                            binding.btnStartQuiz.isEnabled = true
                            binding.timerMessage.visibility = View.GONE
                        }
                    }
                } else {
                    binding.btnStartQuiz.isEnabled = true
                    binding.timerMessage.visibility = View.GONE
                }
            }
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
    }
}