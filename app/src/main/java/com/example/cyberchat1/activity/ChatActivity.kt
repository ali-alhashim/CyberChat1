package com.example.cyberchat1.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberchat1.R
import com.example.cyberchat1.adapters.ChatAdapter

import com.example.cyberchat1.model.MessagesModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import java.text.SimpleDateFormat

import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var  textMessage : EditText
    private lateinit var   recyclerViewMessages : RecyclerView
    lateinit var db: FirebaseDatabase
    private lateinit var    contactPhoneChat :TextView

    private val MessagesList = mutableListOf<MessagesModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_chat)


        val contactNameChat : TextView = findViewById(R.id.contactNameChat)
        val sendMessageButton : FloatingActionButton =findViewById(R.id.sendMessageButton)

        contactPhoneChat= findViewById(R.id.contactPhoneChat)
        recyclerViewMessages= findViewById(R.id.recyclerViewMessages)



        textMessage = findViewById(R.id.textMessage)

        // assign to chatAdapter
        recyclerViewMessages.adapter = ChatAdapter(MessagesList)

        // Initialize Realtime Database
        db = Firebase.database

        // get the parameter from selected contact
        val extras = intent.extras
        if(extras !=null)
        {
            //set contact name and phone number for top bar
            contactNameChat.text = extras.getString("ContactName")
            contactPhoneChat.text =extras.getString("PhoneNumber")?.replace("\\s".toRegex(),"")


        }
        //--

        Log.d(TAG,"you called retrieve Message")
        retrieveMessage()


        // send button action ----
        sendMessageButton.setOnClickListener{
            Log.d("TAG","you click on send button with message ${textMessage.text} ")
            // call function for send message
            sendMessage()




        }
        // --
    }

    @SuppressLint("NotifyDataSetChanged", "SimpleDateFormat")
    private fun sendMessage()
    {


        val messageSender : String  = MainActivity.CurrentPhoneNumber
        val messageReceiver : String =  contactPhoneChat.text.toString()
        // format for time and date
        val simpleTimeFormat = SimpleDateFormat("HH:mm:ss")
        val simpleDateFormat = SimpleDateFormat("MM-dd-yyyy")
        // init time and date
        val currentTime: String = simpleTimeFormat.format(Date())
        val currentDate: String = simpleDateFormat.format((Date()))

        // generate unique key for each message
        val messageID : String = db.getReference("messages").push().key.toString()

        // add message to message list
        MessagesList.add(MessagesModel(messageSender,textMessage.text.toString(),messageReceiver,messageID,currentTime,currentDate,"URL for File","online"))


        // send to firebase++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        db.getReference("messages").child(messageID).child("from").setValue(messageSender)
        db.getReference("messages").child(messageID).child("to").setValue(messageReceiver)
        db.getReference("messages").child(messageID).child("message").setValue(textMessage.text.toString())
        db.getReference("messages").child(messageID).child("date").setValue(currentDate)
        db.getReference("messages").child(messageID).child("time").setValue(currentTime)
        db.getReference("messages").child(messageID).child("status").setValue("sent")

        Log.d(TAG,"you push to firebase new message with ID ${messageID}")
        // end end to firebase+++++++++++++++++++++++++++++++++++++++++++++++++++++++

        // clear the message text box
        textMessage.setText("")

        // scroll To Position for  recyclerViewMessages

        // messages count
        val messagesCount = ChatAdapter(MessagesList).itemCount


        // update recycler view with the new items
        ChatAdapter(MessagesList).notifyDataSetChanged()

        recyclerViewMessages.smoothScrollToPosition(messagesCount)

    }

    private fun retrieveMessage()
    {
        //MessagesList.clear()
        /*
        * Retrieve Messages from massages where sub child ("from") == Current phone Number and ("to") == selected phone number
        *  */

       db.reference.child("messages").addChildEventListener(
           object: ChildEventListener{
               override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                   Log.d(TAG,"the new Message saved in firebase with ID ${snapshot.key.toString()}")


                   if( (snapshot.child("from").value.toString().replace("\\s".toRegex(),"") == MainActivity.CurrentPhoneNumber.replace("\\s".toRegex(),"") && snapshot.child("to").value.toString().replace("\\s".toRegex(),"") ==contactPhoneChat.text.toString().replace("\\s".toRegex(),"")) || (snapshot.child("to").value.toString().replace("\\s".toRegex(),"") == MainActivity.CurrentPhoneNumber.replace("\\s".toRegex(),"") && snapshot.child("from").value.toString().replace("\\s".toRegex(),"") == contactPhoneChat.text.toString().replace("\\s".toRegex(),"")))
                   {
                       // if the Message from me to my selected contact or if message from my selected contact to me retrieve this message
                       Log.d(TAG,"there is a Message found below")
                       Log.d(TAG,snapshot.toString())

                       MessagesList.add(MessagesModel(snapshot.child("from").value.toString(),snapshot.child("message").value.toString(),snapshot.child("to").value.toString(),snapshot.key.toString(),snapshot.child("time").value.toString(),snapshot.child("date").value.toString(),"URL for File","online"))

                       ChatAdapter(MessagesList).notifyDataSetChanged()
                       recyclerViewMessages.smoothScrollToPosition(ChatAdapter(MessagesList).itemCount)
                   }
                   else
                   {
                       Log.d(TAG,"There is no messages between you and your selected contact")
                   }
               }

               override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                   Log.d(TAG,"function on child changed called ")

               }

               override fun onChildRemoved(snapshot: DataSnapshot) {
                   Log.d(TAG,"function on child removed called ")
               }

               override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                   Log.d(TAG,"function on child moved called ")
               }

               override fun onCancelled(error: DatabaseError) {
                   Log.d(TAG,"function on child cancelled called ")
               }
           }
       )


    }
}