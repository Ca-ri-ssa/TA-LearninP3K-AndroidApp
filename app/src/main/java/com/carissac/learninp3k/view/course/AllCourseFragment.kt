package com.carissac.learninp3k.view.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.FragmentAllCourseBinding

class AllCourseFragment : Fragment() {
    private lateinit var binding : FragmentAllCourseBinding
    private lateinit var courseAdapter: CourseAdapter

    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(Injection.provideCourseRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseAdapter = CourseAdapter()
        binding.rvAllCourse.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }

        courseViewModel.allCourseResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                binding.tvAllCourse.text = response.totalCourse?.toString() ?: "0"
                courseAdapter.submitList(response.courses)
            }.onFailure {
                showToast("Gagal memuat semua kelas")
            }
        }

        courseViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        courseViewModel.getAllCourse()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}