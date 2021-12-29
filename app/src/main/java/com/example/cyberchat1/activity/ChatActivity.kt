package com.example.cyberchat1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberchat1.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_chat)


        val contactNameChat : TextView = findViewById(R.id.contactNameChat)
        val contactPhoneChat :TextView = findViewById(R.id.contactPhoneChat)
        val recyclerViewMessages : RecyclerView = findViewById(R.id.recyclerViewMessages)
        val sendMessageButton : FloatingActionButton =findViewById(R.id.sendMessageButton)
        val textMessage : EditText = findViewById(R.id.textMessage)


        val extras = intent.extras

        if(extras !=null)
        {
            //set contact name and phone number for top bar
            contactNameChat.text = extras.getString("ContactName")
            contactPhoneChat.text =extras.getString("PhoneNumber")
        }

        sendMessageButton.setOnClickListener{
            Log.d("TAG","you click on send button with message ${textMessage.text} ")

            /*
            * Now we need current phone number
            * to phone number
            * message text
            * */

            //last clear the message text box
            textMessage.setText(" ")
        }
    }
}