package com.example.cyberchat1

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar





class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //--------------------Request Permission
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


        //---navController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        //---end navController

        //-----------------------------------------------------------------------------------------
        // first check if this device is already registered
        // if the device is registered open chat list fragment
        // else open register fragment
        // connect to firebase lookup for saved UserUID with mobile number in Share preferences
        //-----------------------------------------------------------------------------------------



        val MySharedPreferences : SharedPreferences = this.getSharedPreferences("CyberChatSharedPreferences", 0)

        val UserUID_preferences:String = MySharedPreferences.getString("UserUID","null").toString()
        val mobileNumber_preferences:String = MySharedPreferences.getString("PhoneNumber","null").toString()


        Log.d(TAG,"this first check before if condition for UserUID : " +UserUID_preferences)
        Log.d(TAG,"this first check before if condition for Mobile Number : " +mobileNumber_preferences)

        // check if null go to register fragment

        if(UserUID_preferences =="null")
        {
            Log.d(TAG,"open fragment register")

            navController.navigate(R.id.action_splashFragment_to_registerFragment)






        }
        else
        {
            Log.d(TAG,"Already  register check if correct open chat list if not login again")
            // check if the credential is correct

            Log.d(TAG,"you saved Mobile Number is : $mobileNumber_preferences")
            Log.d(TAG,"you saved User UID is : $UserUID_preferences")
            //go to chat list
            navController.navigate(R.id.action_splashFragment_to_chatListFragment)
        }





    }//end onCreate fun










    // function to enable user go back
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}