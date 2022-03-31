package com.example.adiblarapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adiblarapp.fragments.HomeFragment
import com.example.adiblarapp.fragments.SavedFragment
import com.example.adiblarapp.fragments.SettingFragment

class BasicPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SavedFragment()
            2 -> SettingFragment()
            else -> HomeFragment()
        }
    }


}