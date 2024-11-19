package com.carissac.learninp3k.view.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carissac.learninp3k.databinding.FragmentHomeBinding
import com.carissac.learninp3k.view.challenge.WeeklyChallengeActivity

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabChallenge.setOnClickListener {
            val intent = Intent(requireContext(), WeeklyChallengeActivity::class.java)
            startActivity(intent)
        }
    }
}