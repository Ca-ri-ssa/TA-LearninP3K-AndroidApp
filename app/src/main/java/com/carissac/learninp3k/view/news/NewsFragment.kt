package com.carissac.learninp3k.view.news

import android.os.Bundle
import android.util.Log
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            searchViewNews.setupWithSearchBar(searchBarNews)

//            searchViewNews.post {
//                searchMotivation.visibility = View.VISIBLE
//                Log.d("NewsFragment", "Forced visibility change in post block")
//            }
//
//            searchViewNews.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
//                override fun onViewAttachedToWindow(view: View) {
//                    searchMotivation.visibility = View.VISIBLE
//                    Log.d("NewsFragment", "SearchMotivation set to VISIBLE")
//                }
//
//                override fun onViewDetachedFromWindow(view: View) {
//                    searchMotivation.visibility = View.GONE
//                    Log.d("NewsFragment", "SearchMotivation set to GONE")
//                }
//            })

            searchViewNews
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBarNews.setText(searchViewNews.text)
                    searchViewNews.hide()
                    Toast.makeText(requireContext(), searchViewNews.text, Toast.LENGTH_SHORT).show()
                    false
                }
        }
    }
}