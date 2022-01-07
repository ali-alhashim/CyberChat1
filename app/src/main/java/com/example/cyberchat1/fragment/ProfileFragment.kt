package com.example.cyberchat1.fragment

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.cyberchat1.R
import com.example.cyberchat1.activity.MainActivity


class ProfileFragment : Fragment() {

    private lateinit var user_profile_image : ImageView

    private lateinit var imageUri: Uri

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


            takePicture()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.d(TAG,"there is activity result from Profile Fragment")

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1888) {

            Log.d(TAG,"there is activity result with Request Code 1888")

            val photo: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)

            user_profile_image.setImageBitmap(photo)
        }
    }

    private fun takePicture() {

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Profile Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Cyber Chat profile photo")

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, 1888)
    }


    }