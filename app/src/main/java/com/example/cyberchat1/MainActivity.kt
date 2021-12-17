package com.example.cyberchat1

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
        // connect to firebase lookup for saved token with mobile number in Share preferences
        //-----------------------------------------------------------------------------------------



        val MySharedPreferences : SharedPreferences = this.getSharedPreferences("CyberChatSharedPreferences", MODE_PRIVATE)

        val token_preferences:String = MySharedPreferences.getString("deviceToken",null).toString()
        val mobileNumber_preferences:String = MySharedPreferences.getString("deviceNumber",null).toString()


        Log.d(TAG,"this first check before if condition for token : " +token_preferences)
        Log.d(TAG,"this first check before if condition for Mobile Number : " +mobileNumber_preferences)

        // check if null go to register fragment

        if(token_preferences =="null")
        {
            Log.d(TAG,"open fragment register")

            navController.navigate(R.id.action_splashFragment_to_registerFragment)






        }
        else
        {
            Log.d(TAG,"Already  register check if correct open chat list if not login again")
            // check if the credential is correct



            //go to chat list
            navController.navigate(R.id.action_splashFragment_to_chatListFragment)
        }





    }

    // function to enable user go back
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}