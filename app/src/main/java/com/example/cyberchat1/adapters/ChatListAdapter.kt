package com.example.cyberchat1.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cyberchat1.R
import com.example.cyberchat1.activity.ChatActivity
import com.example.cyberchat1.activity.MainActivity
import com.example.cyberchat1.model.ContactsModel
import com.example.cyberchat1.model.MessagesModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage

// class name       parameter list type          inherit RecyclerAdapter
class ChatListAdapter(val chats:List<ContactsModel>, val fragment: Fragment) : RecyclerView.Adapter<ChatListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameTxt: TextView = view.findViewById(R.id.userNameTxt)
        val userStatusTxt: TextView = view.findViewById(R.id.userStatusTxt)
        val lastMessageTime : TextView = view.findViewById(R.id.lastMessageTime)
        val userProfilePhoto : ImageView = view.findViewById(R.id.userProfilePhoto)
        val contactLayout : View = view.findViewById(R.id.contactLayout)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListAdapter.MyViewHolder {
        // layout for holder
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_view_holder, parent, false)

        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

       holder.userNameTxt.text = chats[position].uname
       holder.userStatusTxt.text = chats[position].status
       //holder.lastMessageTime.text = chats[position].
        val fileName =  chats[position].uid+".jpg"
        val imageUri =  FirebaseStorage.getInstance().reference.child("UsersProfilePhoto/$fileName")

        imageUri.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> {
            Glide.with(fragment).load(it).into(holder.userProfilePhoto)
        })

        holder.contactLayout.setOnClickListener{
            //go to chat activity and send contact name and mobile
            val intent = Intent(fragment.context, ChatActivity::class.java)
            intent.putExtra("ContactName", chats[position].uname)
            intent.putExtra("PhoneNumber",chats[position].phoneNumber)
            intent.putExtra("uid", chats[position].uid)
            intent.putExtra("status",chats[position].status.toString())
            fragment.context?.startActivity(intent)
        }



    }
}