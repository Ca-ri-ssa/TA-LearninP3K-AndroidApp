package com.carissac.learninp3k.view.quiz

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.remote.response.AllAttemptResponseItem
import com.carissac.learninp3k.databinding.ItemAttemptHistoryBinding
import com.carissac.learninp3k.view.utils.formatDateTime

@RequiresApi(Build.VERSION_CODES.O)
class AttemptHistoryAdapter(private val courseId: Int): ListAdapter<AllAttemptResponseItem, AttemptHistoryAdapter.AttemptHistoryViewHolder>(AllAttemptHistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttemptHistoryViewHolder {
        val binding =
            ItemAttemptHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttemptHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttemptHistoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailAttemptActivity::class.java)
            intent.putExtra("attempt_session_id", currentItem.attemptSessionId)
            intent.putExtra("course_id", courseId)
            holder.itemView.context.startActivity(intent)
        }
    }

    class AttemptHistoryViewHolder(private val binding: ItemAttemptHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(attempt: AllAttemptResponseItem) {
            binding.tvTimeAttempt.text = formatDateTime(attempt.createdAt ?: "")
            val score = attempt.attemptScore ?: 0
            val styleScore = when {
                score > 65 -> R.style.TextColorSuccess
                score >= 50 -> R.style.TextColorSecondary
                else -> R.style.TextColorError
            }

            binding.tvScoreAttempt.text = attempt.attemptScore?.toString()
            binding.tvScoreAttempt.setTextAppearance(styleScore)
        }
    }

    class AllAttemptHistoryDiffCallback : DiffUtil.ItemCallback<AllAttemptResponseItem>() {
        override fun areItemsTheSame(oldItem: AllAttemptResponseItem, newItem: AllAttemptResponseItem): Boolean {
            return oldItem.attemptSessionId == newItem.attemptSessionId
        }

        override fun areContentsTheSame(oldItem: AllAttemptResponseItem, newItem: AllAttemptResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}