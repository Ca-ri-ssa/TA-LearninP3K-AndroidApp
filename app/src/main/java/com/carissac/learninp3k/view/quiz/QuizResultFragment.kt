package com.carissac.learninp3k.view.quiz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.remote.response.TakeAttemptResponse
import com.carissac.learninp3k.databinding.FragmentQuizResultBinding

@Suppress("DEPRECATION")
class QuizResultFragment : Fragment() {
    private lateinit var binding: FragmentQuizResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizResult: TakeAttemptResponse? = arguments?.getParcelable(QUIZ_RESULT)

        quizResult.let { result ->
            val score = result?.score ?: 0
            val (styleColor, message) = when {
                score > 65 -> R.style.TextColorSuccess to "Selamat!"
                score >= 50 -> R.style.TextColorSecondary to "Tinggal sedikit lagi!"
                else -> R.style.TextColorError to "Tetap semangat! dan coba lagi"
            }

            binding.tvQuizScoreNum.text = result?.score?.toString()
            binding.tvQuizPoint.setTextAppearance(styleColor)
            binding.tvQuizMsg.text = message
            binding.tvQuizPoint.text = "+${result?.pointsEarned}pts"

            if(!result?.badgeName.isNullOrEmpty()) {
                binding.badgeResult.visibility = View.VISIBLE

                binding.tvBadgeName.text = result.badgeName
                Glide.with(requireContext())
                    .load(result.badgeImg)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.ivBadgeResult)
            } else {
                binding.badgeResult.visibility = View.GONE
            }
        }

        binding.btnFinishQuizResult.setOnClickListener {
            val intent = Intent(requireContext(), QuizIntroActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    }

    companion object {
        private const val QUIZ_RESULT = "quiz_result"

        fun newInstance(result: TakeAttemptResponse) = QuizResultFragment().apply {
            arguments = Bundle().apply {
                putParcelable(QUIZ_RESULT, result)
            }
        }
    }
}