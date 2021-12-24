package com.example.cyberchat1.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.R
import com.example.cyberchat1.activity.MainActivity


class ChatListFragment : Fragment()
{


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?  ): View?
    {


        // Inflate the layout for this fragment
        return inflater.inflate(com.example.cyberchat1.R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectContactNewChatBtn : FloatingActionButton = view.findViewById(com.example.cyberchat1.R.id.selectContactNewChatBtn)


        selectContactNewChatBtn.setOnClickListener()
        {
            Log.d(TAG,"you clicked on start new chat")
            // go to selectContactNewChatFragment to start new chat

           MainActivity.navController.navigate(com.example.cyberchat1.R.id.action_MainFragment_to_selectContactToStartChat)



        }

    }







}