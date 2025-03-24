package com.carissac.learninp3k.view.quiz

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.AllAttemptResponse
import com.carissac.learninp3k.data.remote.response.AttemptResponse
import com.carissac.learninp3k.data.remote.response.SubmitAttemptRequest
import com.carissac.learninp3k.data.remote.response.TakeAttemptResponse
import com.carissac.learninp3k.data.repository.AttemptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: AttemptRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading

    val allAttemptResult: LiveData<Result<AllAttemptResponse>> = repository.allAttemptResult

    val submitQuizResult: LiveData<Result<TakeAttemptResponse>> = repository.submitQuizResult

    val detailAttemptResult: LiveData<Result<AttemptResponse>> = repository.detailAttemptResult

    private val _isQuizSubmitted = MutableLiveData(false)
    val isQuizSubmitted: LiveData<Boolean> = _isQuizSubmitted

    companion object {
        private const val QUIZ_DURATION = 5 * 60 * 1000L
        private const val INTERVAL = 1000L
    }

    private var countDownTimer: CountDownTimer? = null

    private val _remainingTime = MutableStateFlow(QUIZ_DURATION / 1000)
    val remainingTime: StateFlow<Long> = _remainingTime.asStateFlow()

    private val _isTimerFinished = MutableLiveData(false)
    val isTimerFinished: LiveData<Boolean> = _isTimerFinished

    fun startTimer(startTime: Long) {
        val elapsedTime = System.currentTimeMillis() - startTime
        val remaining = (QUIZ_DURATION - elapsedTime).coerceAtLeast(0L)

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(remaining, INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                _remainingTime.value = 0
                _isTimerFinished.postValue(true)
            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }

    fun getAllAttemptHistory(id: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if (token.isNotEmpty()) {
                repository.getAllAttemptSession(token, id)
            }
        }
    }

    fun takeAttempt(id: Int, submitAttemptRequest: SubmitAttemptRequest) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if (token.isNotEmpty()) {
                repository.takeAttempt(token, id, submitAttemptRequest)
            }
        }
    }

    fun resetSubmitQuizState() {
        repository.resetSubmitQuizResult()
    }

    fun getDetailAttempt(id: Int, sessionId: Int) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if (token.isNotEmpty()) {
                repository.getDetailAttempt(token, id, sessionId)
            }
        }
    }
}