@file:Suppress("ClassName")

package com.example.finalproject.activitys

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.example.finalproject.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class otpactivity : AppCompatActivity() {
    var binding: ActivityOtpactivityBinding? = null
    var firebaseAuth: FirebaseAuth? = null
    private var otp: String? = null
    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        firebaseAuth = FirebaseAuth.getInstance()
        binding!!.phtxt.text = "Verify " + intent.getStringExtra("phoneno")
        otp = intent.getStringExtra("otp")
        binding!!.changeno.setOnClickListener {
            val intent = Intent(this@otpactivity, phonenumactivity::class.java)
            startActivity(intent)
            finish()
        }
        binding!!.donebtn.setOnClickListener {
            val eotp = binding!!.pinview.text.toString()
            if (eotp.isEmpty()) {
                Toast.makeText(applicationContext, "please enter otp", Toast.LENGTH_SHORT).show()
            } else {
                binding!!.progressBar2.visibility = View.VISIBLE
                val credential = PhoneAuthProvider.getCredential(otp!!, eotp)
                signinwithphone(credential)
            }
        }
    }

    private fun signinwithphone(credential: PhoneAuthCredential) {
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding!!.progressBar2.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Wel-Come", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@otpactivity, setproacti::class.java)
                startActivity(intent)
                finish()
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    binding!!.progressBar2.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}