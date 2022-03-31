package com.example.adiblarapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adiblarapp.fragments.PagerFragment


class PagerAdapter(fm:FragmentManager, lifecycle: Lifecycle, val list: List<String>): FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return PagerFragment.newInstance(list[position])
    }


}