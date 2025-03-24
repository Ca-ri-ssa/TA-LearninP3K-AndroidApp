package com.carissac.learninp3k.view.quiz

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.data.remote.response.Answer
import com.carissac.learninp3k.data.remote.response.SubmitAttemptRequest
import com.carissac.learninp3k.databinding.ActivityQuizBinding
import com.carissac.learninp3k.view.course.CourseViewModel
import com.carissac.learninp3k.view.course.CourseViewModelFactory
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var quizAdapter: QuizAdapter
    private var courseId: Int = -1
    private val selectedAnswers = mutableMapOf<Int, String>()

    private val quizViewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(Injection.provideAttemptRepository(this))
    }

    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(Injection.provideCourseRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.quiz) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        setupRecyclerView()

        courseId = intent.getIntExtra(COURSE_ID, -1)
        val startTime = intent.getLongExtra(START_TIMER, System.currentTimeMillis())

        observeQuestions()

        quizViewModel.startTimer(startTime)
        observeTimer()

        if(courseId != -1) {
            courseViewModel.getCourseQuiz(courseId)
        }

        binding.btnQuizFinish.setOnClickListener {
            submitQuiz()
        }

        observeQuizResult()
    }

    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter { questionId, answer ->
            selectedAnswers[questionId] = answer
        }

        binding.rvQuiz.apply {
            layoutManager = LinearLayoutManager(this@QuizActivity)
            adapter = quizAdapter
        }
    }

    private fun observeQuestions() {
        courseViewModel.listCourseQuizResult.observe(this) { result ->
            result.onSuccess { response ->
                quizAdapter.submitList(response)
            }.onFailure {
                showToast("Gagal memuat semua data soal, silahkan coba kembali")
            }
        }

        courseViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun observeTimer() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                quizViewModel.remainingTime.collect { timeLeft ->
                    val minute = timeLeft / 60
                    val seconds = timeLeft % 60
                    binding.tvTimer.text = String.format("%02d:%02d", minute, seconds)
                }
            }
        }

        quizViewModel.isTimerFinished.observe(this) { isFinished ->
            if (isFinished) {
                showToast("Waktu habis! Anda gagal menyelesaikan kuis.")

                val intent = Intent(this, QuizIntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun submitQuiz() {
        val answers = selectedAnswers.map { (questionId, userAnswer) ->
            Answer(questionId = questionId, userAnswer = userAnswer)
        }

        if (selectedAnswers.size < quizAdapter.itemCount) {
            showToast("Harap jawab semua pertanyaan sebelum menyelesaikan kuis.")
            binding.btnQuizFinish.isEnabled = false
            return
        } else {
            binding.btnQuizFinish.isEnabled = true
        }

        val request = SubmitAttemptRequest(answers)
        quizViewModel.takeAttempt(courseId, request)
    }

    private fun observeQuizResult() {
        quizViewModel.submitQuizResult.observe(this) { result ->
            result?.onSuccess { response ->
                showToast("Kuis telah dikirim")

                val intent = Intent(this, QuizResultActivity::class.java)
                intent.putExtra(QUIZ_RESULT, response)
                startActivity(intent)
                finish()
            }?.onFailure {
                showToast("Gagal mengirimkan hasil kuis")
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
        const val START_TIMER = "start_time"
        const val QUIZ_RESULT = "quiz_result"
    }
}