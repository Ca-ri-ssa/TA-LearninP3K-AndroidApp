package com.carissac.learninp3k.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carissac.learninp3k.databinding.FragmentClassBinding

class ClassFragment : Fragment() {
    private lateinit var binding : FragmentClassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassBinding.inflate(inflater, container, false)
        return binding.root
    }
}