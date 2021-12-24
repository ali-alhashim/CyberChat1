package com.example.cyberchat1.fragment

import android.os.Bundle

import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.cyberchat1.R
import com.example.cyberchat1.adapters.ContactListAdapter
import com.example.cyberchat1.model.ContactsModel
import android.content.ContentResolver





class SelectContactToStartChat : Fragment() {

    private val contactList = mutableListOf<ContactsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_contact_to_start_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactListRecyclerView :RecyclerView = view.findViewById(R.id.contactListRecyclerView)

        //val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)



        contactList.add(ContactsModel("ali",null,"0547078933",null,null))
        contactList.add(ContactsModel("Naji",null,"0560999557",null,null))



        contactListRecyclerView.adapter = ContactListAdapter(contactList)

    }



}