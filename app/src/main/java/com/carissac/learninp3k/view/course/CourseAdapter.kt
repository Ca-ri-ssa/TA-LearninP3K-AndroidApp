package com.carissac.learninp3k.view.course

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.remote.response.CourseResponseItem
import com.carissac.learninp3k.databinding.ItemCourseBinding

class CourseAdapter: ListAdapter<CourseResponseItem, CourseAdapter.CourseViewHolder>(CourseDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CourseIntroActivity::class.java)
            intent.putExtra("course_id", currentItem.courseId)
            holder.itemView.context.startActivity(intent)
        }
    }

    class CourseViewHolder (private val binding: ItemCourseBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseResponseItem) {
            binding.apply {
                tvItemCourseTitle.text = course.courseName
                tvItemCourseDesc.text = course.courseIntro
                Glide.with(itemView)
                    .load(course.courseThumbnail)
                    .centerCrop()
                    .into(ivItemCourse)
            }
        }
    }

    class CourseDiffCallback : DiffUtil.ItemCallback<CourseResponseItem>() {
        override fun areItemsTheSame(oldItem: CourseResponseItem, newItem: CourseResponseItem): Boolean {
            return oldItem.courseId == newItem.courseId
        }

        override fun areContentsTheSame(oldItem: CourseResponseItem, newItem: CourseResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}