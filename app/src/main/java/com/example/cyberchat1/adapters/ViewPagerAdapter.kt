package com.example.cyberchat1.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cyberchat1.fragment.CallsFragment
import com.example.cyberchat1.fragment.ChatListFragment
import com.example.cyberchat1.fragment.StatusFragment

@Suppress("DEPRECATION")
internal class ViewPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ChatListFragment()
            }
            1 -> {
                StatusFragment()
            }
            2 -> {
                CallsFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}