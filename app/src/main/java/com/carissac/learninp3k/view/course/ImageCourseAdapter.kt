package com.carissac.learninp3k.view.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissac.learninp3k.R
import com.carissac.learninp3k.data.remote.response.ImgCourseResponseItem
import com.carissac.learninp3k.databinding.ItemImgCourseBinding

class ImageCourseAdapter: ListAdapter<ImgCourseResponseItem, ImageCourseAdapter.ImgCourseViewHolder>(ImgCourseDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgCourseViewHolder {
        val binding = ItemImgCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImgCourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImgCourseViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class ImgCourseViewHolder (private val binding: ItemImgCourseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(imgCourse: ImgCourseResponseItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(imgCourse.imgCourseUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .centerCrop()
                    .into(ivImgCourse)
            }
        }
    }

    class ImgCourseDiffCallback : DiffUtil.ItemCallback<ImgCourseResponseItem>() {
        override fun areItemsTheSame(oldItem: ImgCourseResponseItem, newItem: ImgCourseResponseItem): Boolean {
            return oldItem.imgCourseId == newItem.imgCourseId
        }

        override fun areContentsTheSame(oldItem: ImgCourseResponseItem, newItem: ImgCourseResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}