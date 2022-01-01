package com.example.cyberchat1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberchat1.R
import com.example.cyberchat1.activity.MainActivity
import com.example.cyberchat1.model.MessagesModel

// class name       parameter list type          inherit RecyclerAdapter
class ChatAdapter(val messages:List<MessagesModel>) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
            val messageTextView : TextView = view.findViewById(R.id.messageTextView)
            val messengerTextViewNameAndNumber : TextView = view.findViewById(R.id.messengerTextViewNameAndNumber)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.MyViewHolder {
                                                                         // layout for holder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messages_view_holder, parent, false)

        return MyViewHolder(itemView)
    }



    override fun getItemCount(): Int {
         return messages.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



        holder.messageTextView.text = messages[position].message
        holder.messengerTextViewNameAndNumber.text = messages[position].from + " " + messages[position].date + " " + messages[position].time

        if(messages[position].from == MainActivity.CurrentPhoneNumber.replace("\\s".toRegex(),""))
        {
            holder.messageTextView.setBackgroundResource(R.drawable.rounded_message_green)
        }
        else
        {
           holder.messageTextView.setBackgroundResource(R.drawable.rounded_message_bule)
        }


    }
}