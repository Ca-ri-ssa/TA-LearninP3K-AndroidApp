package com.carissac.learninp3k.view.challenge

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.remote.response.TakeWeeklyChallengeResponse
import com.carissac.learninp3k.databinding.ActivityWeeklyChallengeResultBinding
import com.carissac.learninp3k.view.setting.SettingViewModel
import com.carissac.learninp3k.view.setting.SettingViewModelFactory
import com.carissac.learninp3k.view.setting.ThemePreference
import com.carissac.learninp3k.view.setting.dataStore
import kotlin.getValue

@Suppress("DEPRECATION")
class WeeklyChallengeResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeeklyChallengeResultBinding

    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(ThemePreference.getInstance(application.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        checkTheme()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityWeeklyChallengeResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.weeklyChallengeResult) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        val challengeCorrectAnswer = intent.getStringExtra(CHALLENGE_RESULT_INCORRECT)
        val challengeResult = intent.getParcelableExtra<TakeWeeklyChallengeResponse>(CHALLENGE_RESULT_CORRECT)

        binding.tvResultPoint.text = "+${challengeResult?.earnedPoint}pts"

        val score = challengeResult?.score
        binding.tvQuizScore.text = score

        if(score == "Benar") {
            binding.tvQuizScore.setTextAppearance(R.style.TextColorSuccess)
            binding.tvResultMsg.text = "Selamat!"

            binding.cdWeeklyChallengeAnswer.visibility = View.GONE

            Glide.with(this)
                .load(challengeResult.badgeImg)
                .placeholder(R.drawable.img_placeholder)
                .centerCrop()
                .into(binding.ivBadgeResult)
            binding.tvBadgeName.text = challengeResult.badgeName
        } else {
            binding.tvQuizScore.setTextAppearance(R.style.TextColorError)
            binding.tvResultMsg.text = "Anda tidak lulus, tetap semangat!"

            binding.cdWeeklyChallengeAnswer.visibility = View.VISIBLE
            binding.tvChallengeAnswer.text = challengeCorrectAnswer

            binding.badgeResult.visibility = View.GONE
        }

        binding.btnFinishQuizResult.setOnClickListener {
            finish()
        }
    }

    private fun checkTheme() {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            val currentMode = AppCompatDelegate.getDefaultNightMode()
            val newMode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

            if (currentMode != newMode) {
                AppCompatDelegate.setDefaultNightMode(newMode)
            }
        }
    }

    companion object {
        const val CHALLENGE_RESULT_CORRECT = "challenge_result_correct"
        const val CHALLENGE_RESULT_INCORRECT = "challenge_result_incorrect"
    }
}