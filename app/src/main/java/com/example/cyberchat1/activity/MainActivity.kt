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
import com.example.cyberchat1.permission.CAMERA
import com.example.cyberchat1.permission.CONTACTS
import com.example.cyberchat1.permission.STORAGE
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object
    {
        lateinit var auth : FirebaseAuth
        lateinit var db : FirebaseDatabase
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


        // Initialize Realtime Database
        db = Firebase.database


        //---navController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        //---end navController



        //--------------------Request Permission for external storage
        STORAGE()
        //----------------------------

        //--------------------Request Permission for contact
        CONTACTS()
        //----------------------------

        //--------------------Request Permission for CAMERA
        CAMERA()
        //----------------------------



        // run Sign in Launcher
        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { res ->
            this.onSignInResult(res)
        }
        if(auth.currentUser == null) {
            // Start sign in/sign up activity

            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                // AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.TwitterBuilder().build())

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.cyberchat1logo) // Set logo drawable
                // .setTheme(R.style.MySuperAppTheme) // Set theme
                .build()
            signInLauncher.launch(signInIntent)


        }
        else
        {
            // User is already signed in. Therefore, open chat Main Fragment


            CurrentPhoneNumber = auth.currentUser!!.phoneNumber.toString()

            // change the user status from offline to online
            db.getReference("users").child(auth.currentUser?.uid.toString()).child("status").setValue("online")
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


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(this,   "Welcome you Successfully signed in" + (FirebaseAuth.getInstance().getCurrentUser()?.getDisplayName()),Toast.LENGTH_LONG).show();

            RegisterNewUser(user?.uid.toString(),user?.phoneNumber.toString())

            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    fun RegisterNewUser(uid:String, phoneNumber:String)
    {
        val simpleDateFormat = SimpleDateFormat("MM-dd-yyyy")
        val currentDate: String = simpleDateFormat.format((Date()))
        db.getReference("users").child(uid).child("phoneNumber").setValue(phoneNumber)
        db.getReference("users").child(uid).child("RegisteredDate").setValue(currentDate)
        db.getReference("users").child(uid).child("status").setValue("online")
    }

    override fun onDestroy() {
        super.onDestroy()
        db.getReference("users").child(auth.currentUser?.uid.toString()).child("status").setValue("offline")
    }


}