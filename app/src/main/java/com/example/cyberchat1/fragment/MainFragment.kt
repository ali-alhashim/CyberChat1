package com.example.cyberchat1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.cyberchat1.activity.MainActivity
import com.example.cyberchat1.R
import com.example.cyberchat1.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class MainFragment : Fragment()   {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        val tabLayout : TabLayout = view.findViewById(R.id.tabLayout)
        val viewPager : ViewPager = view.findViewById(R.id.viewPager)

        val adapter = ViewPagerAdapter(requireContext(),requireActivity().supportFragmentManager, tabLayout.tabCount)


        viewPager.adapter = adapter




        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }






    }






