package com.example.cyberchat1.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cyberchat1.R
import com.example.cyberchat1.activity.MainActivity
import com.example.cyberchat1.model.MessagesModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

private const val TAG = "ChatAdapter"
// class name       parameter list type          inherit RecyclerAdapter
class ChatAdapter(private val messages:List<MessagesModel>,val context: Context) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
            val messageTextView : TextView = view.findViewById(R.id.messageTextView)
            val messengerTextViewNameAndNumber : TextView = view.findViewById(R.id.messengerTextViewNameAndNumber)
            val messengerImageView : ImageView =view.findViewById(R.id.messengerImageView)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.MyViewHolder {
                                                                         // layout for holder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messages_view_holder, parent, false)

        return MyViewHolder(itemView)
    }



    override fun getItemCount(): Int {
         return messages.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



        holder.messageTextView.text = messages[position].message
        holder.messengerTextViewNameAndNumber.text = messages[position].status + " " + messages[position].date + " " + messages[position].time

        val fileName = messages[position].fromUID+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("UsersProfilePhoto/$fileName")
        refStorage.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> {

            try {
                Glide.with(context).load(it).into(holder.messengerImageView)
            }
            catch (e : Exception)
            {
                 Log.d(TAG,e.toString())
            }

        })

        Log.d(TAG,"message from "+messages[position].fromUID +" To "+ messages[position].toUID)

        if(messages[position].fromUID == MainActivity.auth.currentUser?.uid.toString())
        {
            holder.messageTextView.setBackgroundResource(R.drawable.rounded_message_green)
        }
        else
        {
           holder.messageTextView.setBackgroundResource(R.drawable.rounded_message_bule)
        }


    }
}