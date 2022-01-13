package com.example.cyberchat1.adapters


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cyberchat1.R
import com.example.cyberchat1.activity.ChatActivity
import com.example.cyberchat1.activity.MainActivity
import com.example.cyberchat1.model.ContactsModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage

class ContactListAdapter(private val contacts : List<ContactsModel>, val context:Context) :RecyclerView.Adapter<ContactListAdapter.MyViewHolder>() {



    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val contactName : TextView = view.findViewById(R.id.contactNameTxt)
        val contactPhone : TextView = view.findViewById(R.id.userPhoneTxt)
        val userProfilePhoto : ImageView = view.findViewById(R.id.userProfilePhoto)
        val contactLayoutContainer : View = view.findViewById(R.id.contactLayoutContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_view_holder, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = contacts[position]
        holder.contactName.text = contact.uname
        holder.contactPhone.text = contact.phoneNumber




        holder.contactLayoutContainer.setOnClickListener{
            Log.d(TAG,"you Clicked on   ${holder.contactName.text}")

            //go to chat activity and send contact name and mobile
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("ContactName", holder.contactName.text.toString())
            intent.putExtra("PhoneNumber", holder.contactPhone.text .toString())
            intent.putExtra("uid",contact.uid.toString())
            context.startActivity(intent)
        }

        val fileName = contact.uid+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("UsersProfilePhoto/$fileName")
        refStorage.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> {
            Glide.with(context).load(it).into(holder.userProfilePhoto)
        })





    }

    override fun getItemCount(): Int {

       return contacts.size
    }

}