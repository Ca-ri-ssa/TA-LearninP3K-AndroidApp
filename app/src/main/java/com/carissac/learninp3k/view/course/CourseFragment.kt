package com.carissac.learninp3k.view.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.carissac.learninp3k.R
import com.carissac.learninp3k.databinding.FragmentCourseBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CourseFragment : Fragment() {
    private lateinit var binding : FragmentCourseBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_tab_1,
            R.string.title_tab_2,
            R.string.title_tab_3
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coursePagerAdapter = CoursePagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = coursePagerAdapter
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}