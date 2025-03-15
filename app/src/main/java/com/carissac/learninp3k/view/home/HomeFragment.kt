package com.carissac.learninp3k.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.FragmentHomeBinding
import com.carissac.learninp3k.view.challenge.WeeklyChallengeActivity
import com.carissac.learninp3k.view.profile.ProfileViewModel
import com.carissac.learninp3k.view.profile.ProfileViewModelFactory
import kotlin.getValue

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(Injection.provideUserRepository(requireContext()))
    }

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

        profileViewModel.userName.observe(viewLifecycleOwner) { username ->
            if(username != null) {
                binding.tvHomeUsername.text = username
            }
        }
    }
}