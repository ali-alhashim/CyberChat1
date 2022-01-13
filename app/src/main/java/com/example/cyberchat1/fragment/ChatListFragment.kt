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
import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberchat1.activity.MainActivity
import com.example.cyberchat1.adapters.ChatListAdapter
import com.example.cyberchat1.model.ContactsModel
import com.example.cyberchat1.model.MessagesModel
import com.google.firebase.database.*


class ChatListFragment : Fragment()
{
    private lateinit var chatListRecyclerView : RecyclerView
    private lateinit var chatListAdapter : ChatListAdapter
    private val chatList = mutableListOf<ContactsModel>()
    lateinit var db: FirebaseDatabase
    lateinit var snapshotObj: DataSnapshot
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


        chatListRecyclerView = view.findViewById(com.example.cyberchat1.R.id.chatListRecyclerView)


        chatListAdapter = ChatListAdapter(chatList,this)
        chatListRecyclerView.adapter = chatListAdapter



        FirebaseDatabase.getInstance().reference.child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshotObj = snapshot

                for(chat in snapshotObj.children)
                {




                    chatList.add(ContactsModel(chat.child("to").value.toString(),chat.child("toUID").value.toString(),chat.child("to").value.toString(),chat.child("toUID").value.toString(),"online"))
                    chatListAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })












        // clear list
        chatList.clear()


    }








}