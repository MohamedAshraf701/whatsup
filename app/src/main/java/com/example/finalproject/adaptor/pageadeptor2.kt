package com.example.finalproject.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.finalproject.fragments.postf2
import com.example.finalproject.fragments.storyf

class pageadeptor2(fm: FragmentManager, private var count: Int) : FragmentPagerAdapter(fm, count) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> postf2()
            1 -> storyf()
            else -> postf2()
        }
    }

    override fun getCount(): Int {
        return count
    }
}