package com.example.cyberchat1.permission

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cyberchat1.activity.MainActivity

fun MainActivity.CAMERA() {

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
                AppCompatActivity.CONTEXT_INCLUDE_CODE
            )



            // REQUEST_CODE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    } else {
        // Permission has already been granted
    }
}