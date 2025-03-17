package com.carissac.learninp3k.view.profile

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.remote.response.UserBadgeResponseItem
import com.carissac.learninp3k.databinding.ItemProfileBadgeBinding
import com.carissac.learninp3k.view.utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
class BadgeAdapter(
    private val onItemClick: (Int) -> Unit
): ListAdapter<UserBadgeResponseItem, BadgeAdapter.BadgeViewHolder>(BadgeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val binding = ItemProfileBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BadgeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            currentItem.badgeId?.let { id ->
                onItemClick(id)
            }
        }
    }

    class BadgeViewHolder(private val binding: ItemProfileBadgeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(badge: UserBadgeResponseItem) {
            binding.tvTitleBadge.text = badge.badgeName
            binding.tvDateBadge.text = formatDate(badge.createdAt ?: "")

            Glide.with(itemView)
                .load(badge.badgeImg)
                .centerCrop()
                .into(binding.ivProfileBadge)
        }
    }

    class BadgeDiffCallback : DiffUtil.ItemCallback<UserBadgeResponseItem>() {
        override fun areItemsTheSame(oldItem: UserBadgeResponseItem, newItem: UserBadgeResponseItem): Boolean {
            return oldItem.badgeId == newItem.badgeId
        }

        override fun areContentsTheSame(oldItem: UserBadgeResponseItem, newItem: UserBadgeResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}