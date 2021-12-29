package com.example.cyberchat1.fragment

import android.annotation.SuppressLint
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
import android.content.ContentValues.TAG
import android.provider.UserDictionary
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextSwitcher
import com.example.cyberchat1.activity.MainActivity
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider


class SelectContactToStartChat : Fragment() {

    private val contactList = mutableListOf<ContactsModel>()

    private lateinit var  contactListRecyclerView :RecyclerView

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

        contactListRecyclerView = view.findViewById(R.id.contactListRecyclerView)

        val searchContactEditText : EditText = view.findViewById(R.id.searchContactEditText)

        // call for first time without filter
        getContactList("")


        // search action ---------------------------------------------------------------------
        searchContactEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG,"you Enter the following ${searchContactEditText.text}")

                // call get contact list with search
                getContactList(searchContactEditText.text.toString())
            }
        })
        //--------------------------------------------------------------------------------------








    }

    @SuppressLint("NotifyDataSetChanged")
    fun getContactList(searchWord:String?)
    {
        // clear the list
        contactList.clear()


        Log.d(TAG,"you call getContactList function with search $searchWord")

        // Get All contacts -----------------------------------------------
        //val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        // CONTENT_FILTER_URI
        val selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";
        val selectionArguments = arrayOf("%${searchWord}%")
        val contactsCursor = requireActivity().contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            selection ,
            selectionArguments,
            null)


        // check if you get all contact
        if (contactsCursor != null && contactsCursor.count > 0)
        {
            // get index for column name
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

            // get index for column phone number



            val phoneNoIndex = contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)




            contactsCursor?.use {                      // loop through the cursor
                while (it.moveToNext())
                {
                    // loop for all contact last

                    val contactName = it.getString(nameIndex)


                    val contactPhoneNo = it.getString(phoneNoIndex)

                    if (contactName != null)
                    {

                        //before you add to list check if the phone no as register in firebase or not

                        //if( PhoneAuthProvider.PHONE_SIGN_IN_METHOD)

                        contactList.add(ContactsModel( contactName, null,  contactPhoneNo,null, null))


                    }


                }
            }

            contactsCursor.close()
        }

        //-------------------------------------------------------------------------






        contactListRecyclerView.adapter = ContactListAdapter(contactList,requireContext())


        ContactListAdapter(contactList,requireContext()).notifyDataSetChanged()




    } // end get contact list function



}