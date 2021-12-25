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


        // Get All contacts -----------------------------------------------
        //val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        val contactsCursor = requireActivity().contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

        // check if you get all contact
        if (contactsCursor != null && contactsCursor.count > 0)
        {
            // get index for column name
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

            // get index for column phone number
           // val phoneNoIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)


            contactsCursor?.use {                      // loop through the cursor
                while (it.moveToNext())
                {
                    // loop for all contact last

                    val contactName = it.getString(nameIndex)
                   // val contactPhoneNo = it.getString(phoneNoIndex)

                    if (contactName != null /*&& contactPhoneNo != null*/)
                    {
                        contactList.add(ContactsModel( contactName, null,  /*contactPhoneNo*/null, null, null))


                    }


                }
            }

            contactsCursor.close()
        }

        //-------------------------------------------------------------------------






        contactListRecyclerView.adapter = ContactListAdapter(contactList)

    }



}