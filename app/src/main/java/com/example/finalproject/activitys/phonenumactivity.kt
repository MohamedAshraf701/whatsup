@file:Suppress("DEPRECATION", "ClassName")

package com.example.finalproject.activitys
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import android.widget.Toast
import com.example.finalproject.databinding.ActivityPhonenumactivityBinding
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class phonenumactivity : AppCompatActivity() {
    var binding: ActivityPhonenumactivityBinding? = null
    private var code: String? = null
    var phonenumbers: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhonenumactivityBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        val firebaseAuth = FirebaseAuth.getInstance()
        code = binding!!.contrycode.defaultCountryCodeWithPlus
        binding!!.contrycode.setOnCountryChangeListener {
            code = binding!!.contrycode.selectedCountryCodeWithPlus
        }
        binding!!.sendbtn.setOnClickListener {
            val check = binding!!.phonetxt.text.toString()
            if (check.isNotEmpty() && check.length > 9) {
                phonenumbers = code + check
                binding!!.progressBar.visibility = View.VISIBLE
                val options = PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(
                    phonenumbers!!
                ).setTimeout(60L, TimeUnit.SECONDS).setActivity(this@phonenumactivity)
                    .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                            //how auto fetch otp or code
                        }

                        override fun onVerificationFailed(e: FirebaseException) {}
                        override fun onCodeSent(
                            s: String,
                            forceResendingToken: ForceResendingToken
                        ) {
                            super.onCodeSent(s, forceResendingToken)
                            Toast.makeText(applicationContext, "OTP sent", Toast.LENGTH_SHORT)
                                .show()
                            binding!!.progressBar.visibility = View.INVISIBLE
                            val intent = Intent(this@phonenumactivity, otpactivity::class.java)
                            intent.putExtra("otp", s)
                            intent.putExtra("phoneno", phonenumbers)
                            startActivity(intent)
                        }
                    }).build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }
    }
}