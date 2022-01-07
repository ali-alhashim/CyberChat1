package com.example.cyberchat1.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.cyberchat1.R



class ProfileFragment : Fragment() {

    lateinit var user_profile_image : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         user_profile_image  = view.findViewById(R.id.user_profile_image)

        user_profile_image.setOnClickListener{
            Log.d(TAG,"open Camera")

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            requireActivity().startActivityForResult(cameraIntent, 1888)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1888) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            user_profile_image.setImageBitmap(photo)
        }
    }


    }