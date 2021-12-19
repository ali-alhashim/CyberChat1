package com.example.cyberchat1

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout


class MainFragment : Fragment()  {
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

        val fragmentContainerView: FragmentContainerView = view.findViewById(R.id.fragmentContainerView)
        val navController = NavController(fragmentContainerView.context)


        val tabLayout : TabLayout = view.findViewById(R.id.tabLayout)





        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(tab.position ==0)
                {
                   Log.d(TAG,"you clicked on chat list")
                   // navController.navigate(R.id.action_MainFragment_to_chatListFragment)
                }
                else if(tab.position ==1)
                {
                    Log.d(TAG,"you clicked on Status tap")
                   // navController.navigate(R.id.action_MainFragment_to_statusFragment)
                }
                else if(tab.position ==2)
                {
                    Log.d(TAG,"you clicked on calls tap")
                   // navController.navigate(R.id.action_MainFragment_to_callsFragment)
                }

            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })






    }


}


