package com.carissac.learninp3k.view.news

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.ActivityDetailNewsBinding
import com.carissac.learninp3k.view.utils.formatDate

class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(Injection.provideNewsRepository(this))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.detailNews) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                bottomMargin = systemBars.bottom
                rightMargin = systemBars.right
            }

            WindowInsetsCompat.CONSUMED
        }

        val newsId = intent.getIntExtra(NEWS_ID, -1)
        if(newsId != -1) {
            newsViewModel.getNewsDetail(newsId)
        }

        newsViewModel.newsResult.observe(this) { result ->
            result.onSuccess { news ->
                binding.apply {
                    tvTitleNews.text = news.newsTitle
                    tvNewsContent.text = news.newsContent
                    tvNewsDate.text = formatDate(news.publishedAt ?: "")
                    tvWriterNews.text = news.newsWriter

                    Glide.with(this@DetailNewsActivity)
                        .load(news.newsImg)
                        .centerCrop()
                        .into(ivDetailNews)
                }
            }.onFailure {
                showToast("Gagal mengambil data berita")
            }
        }

        newsViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val NEWS_ID = "news_id"
    }
}