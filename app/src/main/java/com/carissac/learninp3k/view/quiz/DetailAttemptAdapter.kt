package com.carissac.learninp3k.view.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.remote.response.AttemptDetailResponse
import com.carissac.learninp3k.databinding.ItemQuizQuestionBinding

class DetailAttemptAdapter: ListAdapter<AttemptDetailResponse, DetailAttemptAdapter.DetailAttemptViewHolder>(DetailAttemptHistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAttemptViewHolder {
        val binding =
            ItemQuizQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailAttemptViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailAttemptViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, position)
    }

    class DetailAttemptViewHolder(private val binding: ItemQuizQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(attempt: AttemptDetailResponse, position: Int) {
            binding.apply {
                tvNumQuestion.text = (position + 1).toString()
                tvQuestion.text = attempt.questionContent

                val radioButtons = listOf(rbOption1, rbOption2, rbOption3, rbOption4)
                val options = listOf(attempt.option1, attempt.option2, attempt.option3, attempt.option4)

                radioGroupAnswer.clearCheck()
                radioButtons.forEach { rb ->
                    rb.setOnCheckedChangeListener(null)
                    rb.isChecked = false
                    rb.isEnabled = false
                    rb.setTextAppearance(R.style.NormalTextStyle)
                }

                options.forEachIndexed { index, option ->
                    if(index < radioButtons.size) {
                        radioButtons[index].text = option ?: ""
                    }
                }

                val selectedIndex = options.indexOf(attempt.userAnswer)
                val correctIndex = options.indexOf(attempt.correctAnswer)

                if(selectedIndex != -1) {
                    radioButtons[selectedIndex].isChecked = true
                    if(attempt.isCorrect == true) {
                        radioButtons[selectedIndex].setTextAppearance(R.style.TextColorSuccess_bold)
                    } else {
                        radioButtons[selectedIndex].setTextAppearance(R.style.TextColorError_bold)
                    }
                }

                if (correctIndex != -1 && attempt.isCorrect == false) {
                    radioButtons[correctIndex].setTextAppearance(R.style.TextColorSuccess_bold)
                }

                if (attempt.isCorrect == false) {
                    cdFeedback.visibility = View.VISIBLE
                    tvFeedback.text = attempt.feedback
                } else {
                    cdFeedback.visibility = View.GONE
                }
            }
        }
    }

    class DetailAttemptHistoryDiffCallback : DiffUtil.ItemCallback<AttemptDetailResponse>() {
        override fun areItemsTheSame(oldItem: AttemptDetailResponse, newItem: AttemptDetailResponse): Boolean {
            return oldItem.attemptId == newItem.attemptId
        }

        override fun areContentsTheSame(oldItem: AttemptDetailResponse, newItem: AttemptDetailResponse): Boolean {
            return oldItem == newItem
        }
    }
}