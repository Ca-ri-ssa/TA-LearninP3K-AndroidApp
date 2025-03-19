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
import com.carissac.learninp3k.databinding.FragmentFinishedCourseBinding
import kotlin.getValue

class FinishedCourseFragment : Fragment() {
    private lateinit var binding : FragmentFinishedCourseBinding

    private lateinit var courseAdapter: CourseStatusAdapter

    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(Injection.provideCourseRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinishedCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseAdapter = CourseStatusAdapter()
        binding.rvFinishedCourse.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }

        courseViewModel.completedCourseResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                if(!response.courses.isNullOrEmpty()) {
                    binding.ivNoFinishedCourse.visibility = View.GONE
                    binding.tvNoFinishedCourse.visibility = View.GONE
                    binding.tvFinishedCourse.text = response.totalCourses?.toString() ?: "0"
                    binding.rvFinishedCourse.visibility = View.VISIBLE
                    courseAdapter.submitList(response.courses)
                } else {
                    binding.ivNoFinishedCourse.visibility = View.VISIBLE
                    binding.ivNoFinishedCourse.visibility = View.VISIBLE
                    binding.rvFinishedCourse.visibility = View.GONE
                }
            }.onFailure {
                showToast("Gagal memuat data kelas")
            }
        }

        courseViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        courseViewModel.getCourseByCompletedStatus()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("SameParameterValue")
    private fun showToast(msg: String?) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
//        courseViewModel.getCourseByCompletedStatus()
    }
}