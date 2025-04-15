package com.carissac.learninp3k.view.quiz

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carissac.learninp3k.data.remote.response.QuizResponse
import com.carissac.learninp3k.databinding.ItemQuizQuestionBinding

@SuppressLint("SetTextI18n")
class QuizAdapter(
    private val onAnswerSelected: (Int, String) -> Unit
) : ListAdapter<QuizResponse, QuizAdapter.QuizViewHolder>(QuizDiffCallback()) {

    private val selectedAnswers = mutableMapOf<Int, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem.questionId?.let {
            holder.bind(currentItem, position, selectedAnswers[it] ?: "")
        }
    }

    inner class QuizViewHolder(private val binding: ItemQuizQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: QuizResponse, position: Int, selectedAnswer: String) {
            binding.apply {
                tvNumQuestion.text = "${position + 1}."
                tvQuestion.text = quiz.questionContent
                rbOption1.text = quiz.option1
                rbOption2.text = quiz.option2
                rbOption3.text = quiz.option3
                rbOption4.text = quiz.option4
                cdFeedback.visibility = View.GONE

                radioGroupAnswer.setOnCheckedChangeListener(null)
                radioGroupAnswer.clearCheck()

                when (selectedAnswer) {
                    quiz.option1 -> rbOption1.isChecked = true
                    quiz.option2 -> rbOption2.isChecked = true
                    quiz.option3 -> rbOption3.isChecked = true
                    quiz.option4 -> rbOption4.isChecked = true
                }

                radioGroupAnswer.setOnCheckedChangeListener { _, checkedId ->
                    val answer = when (checkedId) {
                        rbOption1.id -> quiz.option1
                        rbOption2.id -> quiz.option2
                        rbOption3.id -> quiz.option3
                        rbOption4.id -> quiz.option4
                        else -> null
                    }

                    quiz.questionId?.let { questionId ->
                        answer?.let {
                            selectedAnswers[questionId] = it
                            onAnswerSelected(questionId, it)
                        }
                    }
                }
            }
        }
    }

    class QuizDiffCallback : DiffUtil.ItemCallback<QuizResponse>() {
        override fun areItemsTheSame(oldItem: QuizResponse, newItem: QuizResponse): Boolean {
            return oldItem.questionId == newItem.questionId
        }

        override fun areContentsTheSame(oldItem: QuizResponse, newItem: QuizResponse): Boolean {
            return oldItem == newItem
        }
    }
}
