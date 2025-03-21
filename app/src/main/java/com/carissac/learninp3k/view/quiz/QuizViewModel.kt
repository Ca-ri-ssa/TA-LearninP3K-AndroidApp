package com.carissac.learninp3k.view.quiz

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carissac.learninp3k.data.remote.response.AllAttemptResponseItem
import com.carissac.learninp3k.data.remote.response.SubmitAttemptRequest
import com.carissac.learninp3k.data.remote.response.TakeAttemptResponse
import com.carissac.learninp3k.data.repository.AttemptRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: AttemptRepository): ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading

    val allAttemptResult: LiveData<Result<List<AllAttemptResponseItem>>> = repository.allAttemptResult
    val submitQuizResult: LiveData<Result<TakeAttemptResponse>> = repository.submitQuizResult

    companion object {
        private const val QUIZ_DURATION = 5 * 60 * 1000L
        private const val INTERVAL = 1000L
        private const val COOLDOWN_PERIOD = 5 * 60 * 1000L
    }

    private var countDownTimer: CountDownTimer? = null

    private val _remainingTime = MutableStateFlow(QUIZ_DURATION / 1000)
    val remainingTime: StateFlow<Long> = _remainingTime.asStateFlow()

    private val _isTimerFinished = MutableLiveData(false)
    val isTimerFinished: LiveData<Boolean> = _isTimerFinished

    private val _cooldownTime = MutableStateFlow(0L)
    val cooldownTime: StateFlow<Long> = _cooldownTime.asStateFlow()

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
            if(token.isNotEmpty()) {
                repository.getAllAttemptSession(token, id)
            }
        }
    }

    fun takeAttempt(id:Int, submitAttemptRequest: SubmitAttemptRequest) {
        viewModelScope.launch {
            val token = repository.getUserSession().first() ?: ""
            if(token.isNotEmpty()) {
                repository.takeAttempt(token, id, submitAttemptRequest)
            }
        }
    }

    fun saveCooldown(courseId: Int, cooldownTime: Long) {
        viewModelScope.launch {
            repository.saveCooldown(courseId, cooldownTime)
        }
    }

    fun getCooldownTime(courseId: Int) {
        viewModelScope.launch {
            repository.getCooldownTime(courseId).collect { cooldownEndTime ->
                val cooldown = cooldownEndTime ?: 0L
                val currentTime = System.currentTimeMillis()
                if (cooldown > currentTime) {
                    _cooldownTime.value = (cooldown - currentTime) / 1000
                    startCooldownTimer()
                } else {
                    _cooldownTime.value = 0
                }
            }
        }
    }

    private fun startCooldownTimer() {
        viewModelScope.launch {
            while (_cooldownTime.value > 0) {
                delay(1000)
                _cooldownTime.value -= 1
            }
        }
    }

    fun isCooldownActive(courseId: Int): Flow<Boolean> {
        return repository.isCooldownActive(courseId)
    }

    fun clearCooldown(courseId: Int) {
        viewModelScope.launch {
            repository.clearCooldown(courseId)
        }
    }

}