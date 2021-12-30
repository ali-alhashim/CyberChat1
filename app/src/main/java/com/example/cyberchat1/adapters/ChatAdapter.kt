package com.example.cyberchat1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberchat1.R

// class name       parameter list type          inherit RecyclerAdapter
class ChatAdapter(val messages:List<String>) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
    {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.MyViewHolder {
                                                                         // layout for holder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.messages_view_holder, parent, false)

        return MyViewHolder(itemView)
    }



    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}