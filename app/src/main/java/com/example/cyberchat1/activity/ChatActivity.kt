package com.example.cyberchat1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.cyberchat1.R
import org.w3c.dom.Text

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val contactNameChat : TextView = findViewById(R.id.contactNameChat)
        val contactPhoneChat :TextView = findViewById(R.id.contactPhoneChat)

        val extras = intent.extras

        if(extras !=null)
        {
            contactNameChat.text = extras.getString("ContactName")
            contactPhoneChat.text =extras.getString("PhoneNumber")
        }
    }
}