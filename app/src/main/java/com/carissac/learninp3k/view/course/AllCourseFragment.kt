package com.carissac.learninp3k.view.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carissac.learninp3k.databinding.FragmentAllCourseBinding

class AllCourseFragment : Fragment() {
    private lateinit var binding : FragmentAllCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllCourseBinding.inflate(inflater, container, false)
        return binding.root
    }
}