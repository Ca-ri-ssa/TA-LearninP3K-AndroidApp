package com.carissac.learninp3k.view.challenge

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
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityWeeklyChallengeBinding
import com.carissac.learninp3k.view.utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
class WeeklyChallengeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeeklyChallengeBinding
    private var challengeId: Int = -1
    private var correctAnswer: String = ""

    private val weeklyChallengeViewModel: WeeklyChallengeViewModel by viewModels {
        WeeklyChallengeViewModelFactory(Injection.provideLeaderboardRepository(this))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWeeklyChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.weeklyChallenge) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        weeklyChallengeViewModel.getWeeklyChallenge()
        observeWeeklyChallenge()

        binding.btnFinishChallenge.setOnClickListener {
            if(challengeId == -1) {
                showToast("Challenge tidak ditemukan")
                return@setOnClickListener
            }

            val selectedOptionId = binding.radioGroupAnswer.checkedRadioButtonId

            if(selectedOptionId == -1) {
                showToast("Anda belum memilih jawaban")
                return@setOnClickListener
            }

            val userAnswer = when(selectedOptionId) {
                R.id.radio_btn_1 -> binding.radioBtn1.text.toString()
                R.id.radio_btn_2 -> binding.radioBtn2.text.toString()
                R.id.radio_btn_3 -> binding.radioBtn3.text.toString()
                R.id.radio_btn_4 -> binding.radioBtn4.text.toString()
                else -> ""
            }

            weeklyChallengeViewModel.takeWeeklyChallenge(challengeId, userAnswer)
        }
        observeTakeWeeklyChallenge()
    }

    private fun observeWeeklyChallenge() {
        weeklyChallengeViewModel.weeklyChallengeResult.observe(this) { result ->
            result.onSuccess { response ->
                val challenge = response.challenge
                correctAnswer = challenge?.challengeAnswer.toString()

                if(challenge != null) {
                    challengeId = challenge.challengeId!!

                    binding.apply {
                        errorMsg.visibility = View.GONE

                        cdChallengePeriod.visibility = View.VISIBLE
                        tvTitleWeeklyChallenge.visibility = View.VISIBLE
                        cdIvChallenge.visibility = View.VISIBLE
                        tvScenarioDesc.visibility = View.VISIBLE
                        radioGroupAnswer.visibility = View.VISIBLE
                        btnFinishChallenge.visibility = View.VISIBLE

                        Glide.with(this@WeeklyChallengeActivity)
                            .load(challenge.challengeImg)
                            .placeholder(R.drawable.img_placeholder)
                            .centerCrop()
                            .into(ivChallenge)

                        val startChallengeDate = formatDate(challenge.startDate ?: "")
                        val endChallengeDate = formatDate(challenge.endDate ?: "")

                        tvChallengePeriod.text = "$startChallengeDate - $endChallengeDate"
                        tvTitleWeeklyChallenge.text = challenge.challengeName
                        tvScenarioDesc.text = challenge.challengeQuestion
                        radioBtn1.text = challenge.option1
                        radioBtn2.text = challenge.option2
                        radioBtn3.text = challenge.option3
                        radioBtn4.text = challenge.option4
                    }
                }
            }.onFailure { error ->
                binding.tvErrorMsg.text = error.message
                binding.errorMsg.visibility = View.VISIBLE
                binding.cdChallengePeriod.visibility = View.GONE
                binding.tvTitleWeeklyChallenge.visibility = View.GONE
                binding.cdIvChallenge.visibility = View.GONE
                binding.tvScenarioDesc.visibility = View.GONE
                binding.radioGroupAnswer.visibility = View.GONE
                binding.btnFinishChallenge.visibility = View.GONE
            }
        }

        weeklyChallengeViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun observeTakeWeeklyChallenge() {
        weeklyChallengeViewModel.takeWeeklyChallengeResult.observe(this) { result ->
            result.onSuccess { response ->

                if(challengeId == -1) {
                    showToast("State lama")
                    return@onSuccess
                }

                val intent = Intent(this, WeeklyChallengeResultActivity::class.java)
                intent.putExtra(CHALLENGE_RESULT_INCORRECT, correctAnswer)
                intent.putExtra(CHALLENGE_RESULT_CORRECT, response)
                startActivity(intent)

                challengeId = -1
                weeklyChallengeViewModel.resetTakeWeeklyChallengeState()
                finish()
            }.onFailure {
                showToast("Gagal mengambil data hasil Weekly Challenge minggu ini")
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
        const val CHALLENGE_RESULT_CORRECT = "challenge_result_correct"
        const val CHALLENGE_RESULT_INCORRECT = "challenge_result_incorrect"
    }
}