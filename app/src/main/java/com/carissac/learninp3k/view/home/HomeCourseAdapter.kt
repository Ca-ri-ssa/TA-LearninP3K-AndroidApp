package com.carissac.learninp3k.view.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.remote.response.CourseResponseItem
import com.carissac.learninp3k.databinding.ItemHomeCourseBinding
import com.carissac.learninp3k.view.course.CourseIntroActivity

class HomeCourseAdapter: ListAdapter<CourseResponseItem, HomeCourseAdapter.CourseViewHolder>(CourseDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemHomeCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class CourseViewHolder(private val binding: ItemHomeCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseResponseItem) {
            binding.apply {
                tvHomeCourseTitle.text = course.courseName
                tvHomeCourseDesc.text = course.courseIntro
                Glide.with(itemView)
                    .load(course.courseThumbnail)
                    .centerCrop()
                    .into(ivHomeCourse)
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