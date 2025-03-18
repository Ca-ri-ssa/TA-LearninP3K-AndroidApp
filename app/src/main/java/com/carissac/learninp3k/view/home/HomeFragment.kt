package com.carissac.learninp3k.view.home

import android.content.Intent
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
import com.bumptech.glide.Glide
import com.carissac.learninp3k.data.di.Injection
import com.carissac.learninp3k.databinding.FragmentHomeBinding
import com.carissac.learninp3k.view.challenge.WeeklyChallengeActivity
import com.carissac.learninp3k.view.course.CourseIntroActivity
import com.carissac.learninp3k.view.course.CourseViewModel
import com.carissac.learninp3k.view.course.CourseViewModelFactory
import com.carissac.learninp3k.view.news.NewsAdapter
import com.carissac.learninp3k.view.news.NewsViewModel
import com.carissac.learninp3k.view.news.NewsViewModelFactory
import com.carissac.learninp3k.view.profile.ProfileViewModel
import com.carissac.learninp3k.view.profile.ProfileViewModelFactory
import retrofit2.HttpException
import kotlin.getValue

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var courseAdapter: HomeCourseAdapter
    private lateinit var newsAdapter: NewsAdapter

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(Injection.provideUserRepository(requireContext()))
    }

    private val courseViewModel: CourseViewModel by viewModels {
        CourseViewModelFactory(Injection.provideCourseRepository(requireContext()))
    }

    private val newsViewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(Injection.provideNewsRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        courseAdapter = HomeCourseAdapter()
        binding.rvCourseRec.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = courseAdapter
        }

        newsAdapter = NewsAdapter()
        binding.rvNewsRec.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }

        observerNearestCourse()
        observerAllCourse()
        observerAllNews()

        courseViewModel.getNearestCourse()
        courseViewModel.getAllCourse()
        newsViewModel.getAllNews()
    }

    private fun observerNearestCourse() {
        courseViewModel.nearestCourseResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                binding.tvContinueCourseTitle.text = response.courseName
                Glide.with(requireContext())
                    .load(response.courseThumbnail)
                    .centerCrop()
                    .into(binding.ivContinueCourse)

                binding.cdContinueCourse.setOnClickListener {
                    val intent = Intent(requireContext(), CourseIntroActivity::class.java)
                    intent.putExtra("course_id", response.courseId)
                    startActivity(intent)
                }

                binding.cdContinueCourse.visibility = View.VISIBLE
                binding.cdNoCourseInprogress.visibility = View.INVISIBLE
            }.onFailure { error ->
                if(error is HttpException && error.code() == 404) {
                    binding.cdNoCourseInprogress.visibility = View.VISIBLE
                    binding.cdContinueCourse.visibility = View.INVISIBLE
                } else {
                    showToast("Gagal memuat kursus status \"Sedang Belajar\"")
                }
            }
        }

        courseViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun observerAllCourse() {
        courseViewModel.allCourseResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                response.courses?.let { courses ->
                    courseAdapter.submitList(courses.take(5))
                } ?: showToast("Daftar kursus kosong")
            }.onFailure {
                showToast("Gagal memuat semua rekomendasi kelas")
            }
        }

        courseViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun observerAllNews() {
        newsViewModel.listNewsResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                response?.let {
                    newsAdapter.submitList(it.take(5))
                } ?: showToast("Daftar berita kosong")
            }.onFailure {
                showToast("Gagal memuat semua rekomendasi berita")
            }
        }

        newsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
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