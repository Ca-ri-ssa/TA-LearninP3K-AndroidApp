package com.carissac.learninp3k.view.course

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.remote.response.CourseStatusResponseItem
import com.carissac.learninp3k.databinding.ItemCourseBinding

class CourseStatusAdapter: ListAdapter<CourseStatusResponseItem, CourseStatusAdapter.CourseStatusViewHolder>(CourseStatusDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseStatusViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseStatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseStatusViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CourseIntroActivity::class.java)
            intent.putExtra("course_id", currentItem.courseId)
            holder.itemView.context.startActivity(intent)
        }
    }

    class CourseStatusViewHolder (private val binding: ItemCourseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(courseStatus: CourseStatusResponseItem) {
            binding.apply {
                tvItemCourseTitle.text = courseStatus.courseName
                tvItemCourseDesc.text = courseStatus.courseIntro
                 Glide.with(itemView)
                     .load(courseStatus.courseThumbnail)
                     .centerCrop()
                     .into(ivItemCourse)
            }
        }
    }

    class CourseStatusDiffCallback : DiffUtil.ItemCallback<CourseStatusResponseItem>() {
        override fun areItemsTheSame(oldItem: CourseStatusResponseItem, newItem: CourseStatusResponseItem): Boolean {
            return oldItem.courseId == newItem.courseId
        }

        override fun areContentsTheSame(oldItem: CourseStatusResponseItem, newItem: CourseStatusResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}