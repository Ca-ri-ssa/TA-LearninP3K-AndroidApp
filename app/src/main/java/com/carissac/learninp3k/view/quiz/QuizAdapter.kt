package com.carissac.learninp3k.view.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carissac.learninp3k.data.remote.response.QuizResponse
import com.carissac.learninp3k.databinding.ItemQuizQuestionBinding

class QuizAdapter: ListAdapter<QuizResponse, QuizAdapter.QuizViewHolder>(QuizDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, position)
    }

    class QuizViewHolder(private val binding: ItemQuizQuestionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: QuizResponse, position: Int) {
            binding.apply {
                tvNumQuestion.text = position.toString()
                tvQuestion.text = quiz.questionContent
                rbOption1.text = quiz.option1
                rbOption2.text = quiz.option2
                rbOption3.text = quiz.option3
                rbOption4.text = quiz.option4
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