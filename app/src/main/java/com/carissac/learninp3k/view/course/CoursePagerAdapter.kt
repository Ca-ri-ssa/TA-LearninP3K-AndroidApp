package com.carissac.learninp3k.view.course

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CoursePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllCourseFragment()
            1 -> ContinueCourseFragment()
            2 -> FinishedCourseFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}