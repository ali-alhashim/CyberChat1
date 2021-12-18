package com.example.cyberchat1

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.TimeUnit
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker
import javax.xml.datatype.DatatypeConstants.SECONDS
import kotlin.time.DurationUnit


class RegisterFragment : Fragment() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    private var SMSCODE :String=""
    private var MOBILNUMBER : String = ""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?  ): View?
    {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    private fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        /*
        -- Executing tasks: [signingreport] in project --Valid until: Friday, October 20, 2051
        you run : signingreport from right corner by gradle you will have fingerprint of your application
        SHA1: B3:A9:97:F1:1D:38:1D:16:57:D4:15:94:3B:92:D5:0B:F0:BF:73:DB
        SHA-256: 49:ED:61:DB:41:E3:49:2F:02:08:0F:C6:79:77:6D:BB:5B:F0:6B:A7:14:F7:79:93:E5:48:F6:E1:AF:F8:28:E9

         */

        //SafetyNet  ------------------------------Credentials Key = AIzaSyBLRo0G7-n830__uYCQqS2NWWp1y2_M5OM
        val nonce =byteArrayOfInts(0xA1, 0x2E, 0x38, 0xD4, 0x89, 0xC3)
        SafetyNet.getClient(requireActivity()).attest(nonce, "AIzaSyBLRo0G7-n830__uYCQqS2NWWp1y2_M5OM")
            .addOnSuccessListener(requireActivity()) {
                // Indicates communication with the service was successful.
                // Use response.getJwsResult() to get the result data.
            }
            .addOnFailureListener(requireActivity()) { e ->
                // An error occurred while communicating with the service.
                if (e is ApiException) {
                    // An error with the Google Play services API contains some
                    // additional details.
                    val apiException = e as ApiException

                    // You can retrieve the status code using the
                    // apiException.statusCode property.
                } else {
                    // A different, unknown type of error occurred.
                    Log.d(TAG, "Error: " + e.message)
                }
            }
        // end SafetyNet ------------------------




        val sendBtn : Button = view.findViewById(R.id.sendBtn)
        val registerBtn :Button = view.findViewById(R.id.registerBtn)
        val userPhoneNumber:EditText = view.findViewById(R.id.editTextPhone)
        val countryCode : CountryCodePicker = view.findViewById(R.id.country_code_picker)
        val smsCode :EditText = view.findViewById(R.id.editTextSMScode)
        val progressBar : ProgressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = ProgressBar.INVISIBLE

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]


        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.d(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(verificationId: String,token: PhoneAuthProvider.ForceResendingToken)
             {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                 progressBar.visibility = ProgressBar.INVISIBLE

            }


        } //end call back



//-------------------- sendBtn Action

        sendBtn.setOnClickListener {
            Log.d(TAG,"You Clicked on send button")


            // send user phone number to firebase
            Log.d(TAG, "your Mobile number with country code is :"+countryCode.fullNumberWithPlus +userPhoneNumber.text.toString())

            //function to send full mobile number to firebase
            startPhoneNumberVerification(countryCode.fullNumberWithPlus +userPhoneNumber.text.toString())

            MOBILNUMBER =countryCode.fullNumberWithPlus +userPhoneNumber.text.toString()

            progressBar.visibility = ProgressBar.VISIBLE

        }
//------------------end send button action


        //--------- register button action
        registerBtn.setOnClickListener {
            Log.d(TAG,"you clicked on register Button")

            SMSCODE = smsCode.text.toString()

            Log.d(TAG,"SMS CODE sent : $SMSCODE")

            verifyPhoneNumberWithCode(storedVerificationId,SMSCODE)
        }
        //----------- end button action


    } // end onViewCreated


    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        Log.d(TAG,"you credential = $credential")
        // [END verify_with_code]

        //test--
        signInWithPhoneAuthCredential(credential)
        Log.d(TAG,"login -------------------->>>>>>>>>")
    }


    private fun startPhoneNumberVerification(phoneNumber: String) {

        Log.d(TAG,"you call PhoneAuthProvider for: $phoneNumber")

        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }


    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    // now in firebase there is Identifier and User UID we need to save this in local storage Share Pre.

                    val MySharedPreferences : SharedPreferences = requireActivity().getSharedPreferences("CyberChatSharedPreferences", 0)


                    //get current user uid
                    var userID = auth.currentUser?.uid

                    //save the data
                    MySharedPreferences.edit().putString("PhoneNumber",MOBILNUMBER).commit()
                    MySharedPreferences.edit().putString("UserUID",userID).commit()




                    Log.d(TAG,"you saved your Mobile number and user UID in CyberChatSharedPreferences with $MOBILNUMBER and user UID : $userID")


                    val user = task.result?.user
                    //go to chat list
                    MainActivity().navController.navigate(R.id.action_splashFragment_to_chatListFragment)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid

                        Snackbar.make(this.requireView(), "The verification code entered was invalid", Snackbar.LENGTH_LONG).show()

                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]


    private fun updateUI(user: FirebaseUser? = auth.currentUser) {

    }


}//end class