package com.carissac.learninp3k.view.leaderboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.remote.response.LeaderboardResponse
import com.carissac.learninp3k.databinding.ItemUserLeaderboardBinding

class LeaderboardAdapter(private val userId: Int): ListAdapter<LeaderboardResponse, LeaderboardAdapter.LeaderboardViewHolder>(LeaderboardDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = ItemUserLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, position, userId)
    }

    class LeaderboardViewHolder (private val binding: ItemUserLeaderboardBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceType")
        fun bind(leaderboard: LeaderboardResponse, position: Int, userId: Int) {
            binding.apply {
                tvUsernameRank.text = leaderboard.userName
                tvTotalPointUserRank.text = leaderboard.totalPoint?.toString() ?: "0"
                tvNumRank.text = (position + 4).toString()

                Glide.with(itemView)
                    .load(leaderboard.avatarImg)
                    .centerCrop()
                    .into(ivUserRank)


                if(leaderboard.userId == userId) {
                    tvUsernameRank.text = "Anda"
                    tvUsernameRank.setTextAppearance(R.style.LeaderboardStyleHighlightedText)
                    tvTotalPointUserRank.setTextAppearance(R.style.LeaderboardStyleHighlightedText)
                }
            }
        }
    }

    class LeaderboardDiffCallback : DiffUtil.ItemCallback<LeaderboardResponse>() {
        override fun areItemsTheSame(oldItem: LeaderboardResponse, newItem: LeaderboardResponse): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: LeaderboardResponse, newItem: LeaderboardResponse): Boolean {
            return oldItem == newItem
        }
    }
}