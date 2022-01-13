package com.example.cyberchat1.fragment

import android.content.ContentResolver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import com.example.cyberchat1.R
import com.example.cyberchat1.activity.MainActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File


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
        val updateProfileButton : Button = view.findViewById(R.id.updateProfileButton)


        val profileUserName : EditText = view.findViewById(R.id.profileUserName)
        val profileUserEmail : EditText = view.findViewById(R.id.profileUserEmail)
        val profilePhoneNumber : EditText = view.findViewById(R.id.profilePhoneNumber)


        val fileName = MainActivity.auth.currentUser?.uid+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("UsersProfilePhoto/$fileName")
        refStorage.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> {
            Glide.with(this).load(it).into(user_profile_image)
        })


        // get user profile
        val user = MainActivity.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            profileUserName.setText(it.displayName)
            profileUserEmail.setText(it.email)
            profilePhoneNumber.setText(it.phoneNumber)
            val photoUrl = it.photoUrl


        }








         //update user profile
        updateProfileButton.setOnClickListener{



            //-----------

            val profileUpdates = userProfileChangeRequest {
                displayName = profileUserName.text.toString()

                //photoUri = Uri.parse(imageUri.toString())
            }

            MainActivity.auth.currentUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }


            MainActivity.auth.currentUser!!.updateEmail(profileUserEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User email address updated.")
                    }
                }

            //-----------


            Log.d(TAG, "update user profile")
           // updateProfile()

        }



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

    private fun updateProfile()
    {
        // upload profile photo to firebase
        if(imageUri !=null)
        {
            val fileName = MainActivity.auth.currentUser?.uid+".jpg"

            val refStorage = FirebaseStorage.getInstance().reference.child("UsersProfilePhoto/$fileName")

            refStorage.putFile(imageUri).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
                    taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    Toast.makeText(requireContext(),"Your Profile updated Successfully",Toast.LENGTH_LONG).show()
                }
            })
                ?.addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(requireContext(),e.message,Toast.LENGTH_LONG).show()
                })

        }
    }





}