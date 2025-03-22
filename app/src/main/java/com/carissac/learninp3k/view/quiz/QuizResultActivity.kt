package com.carissac.learninp3k.view.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.remote.response.TakeAttemptResponse
import com.carissac.learninp3k.databinding.ActivityQuizResultBinding

@Suppress("DEPRECATION")
class QuizResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.activityQuizResult) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        val quizResult = intent.getParcelableExtra<TakeAttemptResponse>(QUIZ_RESULT)

        if (quizResult != null) {
            val score = quizResult.score ?: 0
            val (styleColor, message) = when {
                score > 65 -> R.style.TextColorSuccess to "Selamat!"
                score >= 50 -> R.style.TextColorSecondary to "Ayo, Anda pasti bisa!"
                else -> R.style.TextColorError to "Tetap semangat dan coba lagi!"
            }

            binding.tvQuizScoreNum.text = score.toString()
            binding.tvQuizPoint.setTextAppearance(styleColor)
            binding.tvQuizMsg.text = message
            binding.tvQuizPoint.text = "+${quizResult.pointsEarned ?: 0}pts"

            if (!quizResult.badgeName.isNullOrEmpty()) {
                binding.badgeResult.visibility = View.VISIBLE
                binding.tvBadgeName.text = quizResult.badgeName

                Glide.with(this)
                    .load(quizResult.badgeImg)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.ivBadgeResult)
            } else {
                binding.badgeResult.visibility = View.GONE
            }
        } else {
            Toast.makeText(this, "Data hasil kuis tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnFinishQuizResult.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val QUIZ_RESULT = "quiz_result"
    }
}