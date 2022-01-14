package com.example.cyberchat1.activity

import android.annotation.SuppressLint

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cyberchat1.R

import com.example.cyberchat1.adapters.ChatAdapter

import com.example.cyberchat1.model.MessagesModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

import java.text.SimpleDateFormat

import java.util.*

private const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity() {

    private lateinit var  textMessage : EditText
    private lateinit var   recyclerViewMessages : RecyclerView
    lateinit var db: FirebaseDatabase
    private lateinit var    contactPhoneChat :TextView
    private lateinit var contactUID : String
    private lateinit var contactStatus:String
    private lateinit var chatAdapter : ChatAdapter

    private val MessagesList = mutableListOf<MessagesModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_chat)


        val contactNameChat : TextView = findViewById(R.id.contactNameChat)
        val sendMessageButton : FloatingActionButton =findViewById(R.id.sendMessageButton)
        val userProfilePhoto : ImageView = findViewById(R.id.userProfilePhoto)
        val contactStatusChat : TextView = findViewById(R.id.contactStatusChat)

        contactPhoneChat= findViewById(R.id.contactPhoneChat)
        recyclerViewMessages= findViewById(R.id.recyclerViewMessages)



        textMessage = findViewById(R.id.textMessage)

        chatAdapter = ChatAdapter(MessagesList,this)

        // assign to chatAdapter
        recyclerViewMessages.adapter = chatAdapter



        // Initialize Realtime Database
        db = Firebase.database

        // get the parameter from selected contact
        val extras = intent.extras
        if(extras !=null)
        {
            //set contact name and phone number for top bar
            contactNameChat.text = extras.getString("ContactName")
            contactPhoneChat.text =extras.getString("PhoneNumber")?.replace("\\s".toRegex(),"")
            contactUID = extras.getString("uid").toString()
            contactStatus = extras.getString("status").toString()

        }
        //--


        val fileName = contactUID+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("UsersProfilePhoto/$fileName")
        refStorage.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> {
            try {
                Glide.with(this).load(it).into(userProfilePhoto)
            }
            catch (e:Exception)
            {
                Log.d(TAG,e.toString())
            }

        })

        contactStatusChat.text = contactStatus



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


        val messageSender : String  = MainActivity.CurrentPhoneNumber.replace("\\s".toRegex(),"")
        val messageReceiver : String =  contactPhoneChat.text.replace("\\s".toRegex(),"")
        // format for time and date
        val simpleTimeFormat = SimpleDateFormat("HH:mm:ss")
        val simpleDateFormat = SimpleDateFormat("MM-dd-yyyy")
        // init time and date
        val currentTime: String = simpleTimeFormat.format(Date())
        val currentDate: String = simpleDateFormat.format((Date()))

        // generate unique key for each message
        val messageID : String = db.getReference("messages").push().key.toString()



        // send to firebase++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        db.getReference("messages").child(messageID).child("from").setValue(messageSender)
        db.getReference("messages").child(messageID).child("fromUID").setValue(MainActivity.auth.currentUser?.uid.toString())
        db.getReference("messages").child(messageID).child("to").setValue(messageReceiver)
        db.getReference("messages").child(messageID).child("toUID").setValue(contactUID)
        db.getReference("messages").child(messageID).child("message").setValue(textMessage.text.toString())
        db.getReference("messages").child(messageID).child("date").setValue(currentDate)
        db.getReference("messages").child(messageID).child("time").setValue(currentTime)
        db.getReference("messages").child(messageID).child("status").setValue("sent")

        Log.d(TAG,"you push to firebase new message with ID ${messageID}")
        // end end to firebase+++++++++++++++++++++++++++++++++++++++++++++++++++++++

        // clear the message text box
        textMessage.setText("")




    }

    private fun retrieveMessage()
    {

        /*
        * Retrieve Messages from massages where sub child ("from") == Current phone Number and ("to") == selected phone number
        *  */
        var messageOldID:String =""
        val retrieveFrom =  db.reference.child("messages").addChildEventListener(
           object: ChildEventListener{
               @SuppressLint("NotifyDataSetChanged")
               override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                   Log.d(TAG,"the new Message saved in firebase with ID ${snapshot.key.toString()}")


                   if( (snapshot.child("fromUID").value.toString() == MainActivity.auth.currentUser?.uid.toString() && snapshot.child("toUID").value.toString() == contactUID ) || (snapshot.child("toUID").value.toString() == MainActivity.auth.currentUser?.uid.toString() && snapshot.child("fromUID").value.toString() == contactUID))
                   {
                       // if the Message from me to my selected contact or if message from my selected contact to me retrieve this message
                       Log.d(TAG,"there is a Message found below")
                       Log.d(TAG,snapshot.toString())
                                                                      //from                                 from UID                                   message                                           to                                            to UID                                message key
                       MessagesList.add(MessagesModel(snapshot.child("from").value.toString(),snapshot.child("fromUID").value.toString(),snapshot.child("message").value.toString(),snapshot.child("to").value.toString(),snapshot.child("toUID").value.toString(),snapshot.key.toString(),snapshot.child("time").value.toString(),snapshot.child("date").value.toString(),"fileName",snapshot.child("status").value.toString()))

                       chatAdapter.notifyDataSetChanged()
                       recyclerViewMessages.smoothScrollToPosition(chatAdapter.itemCount)

                       messageOldID = previousChildName.toString()

                   }
                   else
                   {
                       Log.d(TAG,"There is no messages between you and your selected contact")
                   }


               }

               @SuppressLint("NotifyDataSetChanged")
               override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                   Log.d(TAG,"onChildChanged action start---")

                   if( (snapshot.child("fromUID").value.toString() == MainActivity.auth.currentUser?.uid.toString() && snapshot.child("toUID").value.toString() == contactUID ) || (snapshot.child("toUID").value.toString() == MainActivity.auth.currentUser?.uid.toString() && snapshot.child("fromUID").value.toString() == contactUID))
                   {

                       if(snapshot.child("message").value.toString() !=null && snapshot.child("message").value.toString() !="null")
                       {
                           val messageid = snapshot.key.toString()

                           if(messageid != messageOldID)
                           {
                               Log.d(TAG,"new ID = $messageid  old ID = $messageOldID")

                               Log.d(TAG,"meesage ID = $messageid and $previousChildName")
                               // if the Message from me to my selected contact or if message from my selected contact to me retrieve this message
                               Log.d(TAG,"there is a Message found below by onChildChanged")
                               Log.d(TAG,snapshot.toString())

                               MessagesList.add(MessagesModel(snapshot.child("from").value.toString(),snapshot.child("fromUID").value.toString(),snapshot.child("message").value.toString(),snapshot.child("to").value.toString(),snapshot.child("toUID").value.toString(),snapshot.key.toString(),snapshot.child("time").value.toString(),snapshot.child("date").value.toString(),"fileName",snapshot.child("status").value.toString()))

                               chatAdapter.notifyDataSetChanged()
                               recyclerViewMessages.smoothScrollToPosition(chatAdapter.itemCount)
                           }



                           messageOldID = messageid

                       }




                   }



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




           } //end ChildEventListener
       )







    }



}



