package com.example.mapdiary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mapdiary.fragment.chat.ChatFragment
import com.example.mapdiary.fragment.home.CommunityFragment
import com.example.mapdiary.fragment.setting.SettingFragment
import com.example.mapdiary.fragment.map.MapFragment

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CommunityFragment()
            1 -> MapFragment()
            2 -> ChatFragment()
            else -> SettingFragment()
        }
    }
}