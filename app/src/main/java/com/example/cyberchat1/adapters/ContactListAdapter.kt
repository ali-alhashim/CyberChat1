package com.example.cyberchat1.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberchat1.R
import com.example.cyberchat1.model.ContactsModel

class ContactListAdapter(val contacts : List<ContactsModel>) :RecyclerView.Adapter<ContactListAdapter.MyViewHolder>() {



    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val contactName : TextView = view.findViewById(R.id.contactNameTxt)
        val contactPhone : TextView = view.findViewById(R.id.userPhoneTxt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_view_holder, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = contacts[position]
        holder.contactName.text = contact.uname
        holder.contactPhone.text = contact.phoneNumber
    }

    override fun getItemCount(): Int {

       return contacts.size
    }

}