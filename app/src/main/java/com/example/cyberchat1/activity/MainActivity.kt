package com.example.cyberchat1.activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.cyberchat1.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    companion object
    {
        lateinit var auth : FirebaseAuth
        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavController

        lateinit var CurrentPhoneNumber : String
    }







    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         Log.d(TAG,"MainActivity Start ----")

        // Initialize Firebase Auth

        auth = FirebaseAuth.getInstance()





        //---navController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        //---end navController



        //--------------------Request Permission for external storage
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    CONTEXT_INCLUDE_CODE)



                // REQUEST_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        //----------------------------




        //--------------------Request Permission for contact
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),
                    CONTEXT_INCLUDE_CODE)



                // REQUEST_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        //----------------------------



        //--------------------Request Permission for CAMERA
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
                    CONTEXT_INCLUDE_CODE)



                // REQUEST_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        //----------------------------




        //-----------------------------------------------------------------------------------------
        // first check if this device is already registered
        // if the device is registered open chat list fragment
        // else open register fragment
        // connect to firebase lookup for saved UserUID with mobile number in Share preferences
        //-----------------------------------------------------------------------------------------



        val MySharedPreferences : SharedPreferences = this.getSharedPreferences("CyberChatSharedPreferences", 0)

        val UserUID_preferences:String = MySharedPreferences.getString("UserUID","null").toString()
        val mobileNumber_preferences:String = MySharedPreferences.getString("PhoneNumber","null").toString()

        val User_Credential : String = MySharedPreferences.getString("User_Credential", "null").toString()


        Log.d(TAG,"this first check before if condition for UserUID : $UserUID_preferences")

        Log.d(TAG,"this first check before if condition for Mobile Number : $mobileNumber_preferences" )

        // check if null go to register fragment

        if(UserUID_preferences =="null")
        {
            Log.d(TAG,"open fragment register")

            navController.navigate(R.id.action_MainFragment_to_registerFragment)






        }
        else
        {
            Log.d(TAG,"Already  register check if correct open chat list if not login again")


            // check if the credential is correct




            Log.d(TAG,"you saved Mobile Number is : $mobileNumber_preferences")
            Log.d(TAG,"you saved User UID is : $UserUID_preferences")
            Log.d(TAG, "Your Credential is : $User_Credential")

            //val myCredential:AuthCredential =  User_Credential as AuthCredential

           // auth.signInWithCredential(myCredential)

            // set after you check he is really the owner of this mobile number
            CurrentPhoneNumber = mobileNumber_preferences

            //go to chat list

            //show chat list from firebase

        }





    }//end onCreate fun










    // function to enable user go back
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    // to add Main Menu to this Activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    // function to handle selected item from menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    Toast.makeText(this@MainActivity,"You have been signed out.", Toast.LENGTH_LONG  ).show()

                    // Close activity
                    finish()
                }
        }
        else if(item.itemId == R.id.menu_profile)
        {
            //go to profile fragment

            //Log.d(TAG,supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.id.toString())


            navController.navigate(R.id.action_MainFragment_to_profileFragment)


        }
        return true
    }


}