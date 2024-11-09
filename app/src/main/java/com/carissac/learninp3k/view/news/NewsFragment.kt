package com.carissac.learninp3k.view.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.carissac.learninp3k.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {
    private lateinit var binding : FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        with(binding) {
            searchViewNews.setupWithSearchBar(searchBarNews)
            searchViewNews
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBarNews.setText(searchViewNews.text)
                    searchViewNews.hide()
                    Toast.makeText(requireContext(), searchViewNews.text, Toast.LENGTH_SHORT).show()
                    false
                }
        }

        return binding.root
    }
}