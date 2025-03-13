package com.carissac.learninp3k.view.news

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.remote.response.NewsResponseItem
import com.carissac.learninp3k.databinding.ItemHomeNewsBinding
import com.carissac.learninp3k.view.utils.formatDate

@RequiresApi(Build.VERSION_CODES.O)
class NewsAdapter: ListAdapter<NewsResponseItem, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemHomeNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailNewsActivity::class.java)
            intent.putExtra("news_id", currentItem.newsId)
            holder.itemView.context.startActivity(intent)
        }
    }

    class NewsViewHolder(private val binding: ItemHomeNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsResponseItem) {
            binding.apply {
                tvHomeNewsTitle.text = news.newsTitle
                tvHomeNewsDesc.text = news.newsContent
                tvHomeNewsDate.text = formatDate(news.publishedAt ?: "")

                Glide.with(itemView)
                    .load(news.newsImg)
                    .centerCrop()
                    .into(ivHomeNews)
            }
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsResponseItem>() {
        override fun areItemsTheSame(oldItem: NewsResponseItem, newItem: NewsResponseItem): Boolean {
            return oldItem.newsId == newItem.newsId
        }

        override fun areContentsTheSame(oldItem: NewsResponseItem, newItem: NewsResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}