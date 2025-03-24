package com.carissac.learninp3k.view.challenge

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.remote.response.TakeWeeklyChallengeResponse
import com.carissac.learninp3k.databinding.ActivityWeeklyChallengeResultBinding

@Suppress("DEPRECATION")
class WeeklyChallengeResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeeklyChallengeResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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

    companion object {
        const val CHALLENGE_RESULT_CORRECT = "challenge_result_correct"
        const val CHALLENGE_RESULT_INCORRECT = "challenge_result_incorrect"
    }
}