package com.carissac.learninp3k.view.profile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.remote.response.AvatarResponseItem
import com.carissac.learninp3k.databinding.ItemAvatarBinding

class ChooseAvatarAdapter: ListAdapter<AvatarResponseItem, ChooseAvatarAdapter.AvatarViewHolder>(AvatarDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = ItemAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, holder.itemView.context)
    }

    class AvatarViewHolder(private val binding: ItemAvatarBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(avatar: AvatarResponseItem, context: Context) {
            binding.tvAvatarName.text = avatar.avatarName
            Glide.with(itemView)
                .load(avatar.avatarImg)
                .centerCrop()
                .into(binding.ivAvatar)

            binding.btnDetailAvatar.setOnClickListener {
                val intent = Intent(context, DetailAvatarActivity::class.java)
                intent.putExtra("avatar_id", avatar.avatarId)
                context.startActivity(intent)
            }

            binding.btnChooseAvatar.setOnClickListener {
                val intent = Intent(context, EditProfileActivity::class.java)
                intent.putExtra("avatar_id", avatar.avatarId)
                intent.putExtra("avatar_img", avatar.avatarImg)
                context.startActivity(intent)
            }
        }
    }

    class AvatarDiffCallback : DiffUtil.ItemCallback<AvatarResponseItem>() {
        override fun areItemsTheSame(oldItem: AvatarResponseItem, newItem: AvatarResponseItem): Boolean {
            return oldItem.avatarId == newItem.avatarId
        }

        override fun areContentsTheSame(oldItem: AvatarResponseItem, newItem: AvatarResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}