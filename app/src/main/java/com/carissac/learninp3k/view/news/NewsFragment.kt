package com.carissac.learninp3k.view.news

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {
    private lateinit var binding : FragmentNewsBinding
    private lateinit var newsAdapterSearch: NewsAdapter
    private lateinit var newsAdapter: NewsAdapter

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(Injection.provideNewsRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsAdapter()
        newsAdapterSearch = NewsAdapter()

        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
        binding.rvNewsSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapterSearch
        }

        setupSearchView()
        observeSearchResult()
        observeAllNews()

        newsViewModel.getAllNews()
    }

    private fun observeAllNews() {
        newsViewModel.listNewsResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { allNews ->
                if(allNews.isNotEmpty()) {
                    newsAdapter.submitList(allNews)
                }
            }.onFailure {
                showToast("Gagal memuat semua berita")
            }
        }

        newsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupSearchView() {
        with(binding) {
            searchViewNews.setupWithSearchBar(searchBarNews)

            searchViewNews
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val query = searchViewNews.text.toString().trim()
                    if(query.isNotEmpty()) {
                        newsViewModel.searchNews(query)
                        searchBarNews.setText(query)
                    }
                    false
                }
        }
    }

    private fun observeSearchResult() {
        newsViewModel.searchNewsResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val query = binding.searchBarNews.text.toString()

                if(!response.news.isNullOrEmpty()) {
                    binding.tvTotalSearch.text = "Total: ${response.totalResult ?: 0}"
                    binding.tvTotalSearch.visibility = View.VISIBLE

                    binding.rvNewsSearch.visibility = View.VISIBLE
                    newsAdapterSearch.submitList(response.news)

                    binding.ivSearchMotivation.visibility = View.INVISIBLE
                    binding.tvSearchMotivation.visibility = View.INVISIBLE
                } else {
                    binding.tvTotalSearch.visibility = View.GONE
                    binding.rvNewsSearch.visibility = View.GONE
                    binding.ivSearchMotivation.visibility = View.VISIBLE
                    binding.tvSearchMotivation.visibility = View.VISIBLE
                    binding.tvSearchMotivation.text = "Tidak ada berita \"${query}\", Coba cari berita lainnya"
                }
            }
        }

        newsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarSearch.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}