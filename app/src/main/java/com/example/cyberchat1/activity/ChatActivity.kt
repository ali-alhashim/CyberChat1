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
import com.example.cyberchat1.model.MessagesModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var  textMessage : EditText
    private lateinit var   recyclerViewMessages : RecyclerView
    private lateinit var    contactPhoneChat :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_chat)


        val contactNameChat : TextView = findViewById(R.id.contactNameChat)
        val sendMessageButton : FloatingActionButton =findViewById(R.id.sendMessageButton)

        contactPhoneChat= findViewById(R.id.contactPhoneChat)
        recyclerViewMessages= findViewById(R.id.recyclerViewMessages)
        textMessage = findViewById(R.id.textMessage)


        // get the parameter from selected contact
        val extras = intent.extras
        if(extras !=null)
        {
            //set contact name and phone number for top bar
            contactNameChat.text = extras.getString("ContactName")
            contactPhoneChat.text =extras.getString("PhoneNumber")
        }
        //--

        // send button action ----
        sendMessageButton.setOnClickListener{
            Log.d("TAG","you click on send button with message ${textMessage.text} ")

            // call function for send message

            sendMessage()
        }
        // --
    }

    private fun sendMessage()
    {


        val messageSender : String  = MainActivity.CurrentPhoneNumber
        val messageReceiver : String =  contactPhoneChat.text.toString()
        // format for time and date
        val simpleTimeFormat = SimpleDateFormat("HH:mm:ss")
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
        // init time and date
        val currentTime: String = simpleTimeFormat.format(Date())
        val currentDate: String = simpleDateFormat.format((Date()))

        // init message model
        val messageModel = MessagesModel(messageSender,textMessage.text.toString(),messageReceiver,"unique key ",currentTime,currentDate,"URL for File","online")
    }
}