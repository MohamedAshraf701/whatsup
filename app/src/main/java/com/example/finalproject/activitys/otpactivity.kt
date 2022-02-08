@file:Suppress("ClassName")

package com.example.finalproject.activitys

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.finalproject.R
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.android.synthetic.main.activity_otpactivity.*

class otpactivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null
    private var otp: String? = null
    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_otpactivity)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        firebaseAuth = FirebaseAuth.getInstance()
        phtxt.text = "Verify " + intent.getStringExtra("phoneno")
        otp = intent.getStringExtra("otp")
        changeno.setOnClickListener {
            val intent = Intent(this@otpactivity, phonenumactivity::class.java)
            startActivity(intent)
            finish()
        }
        donebtn.setOnClickListener {
            val eotp = pinview.text.toString()
            if (eotp.isEmpty()) {
                Toast.makeText(applicationContext, "please enter otp", Toast.LENGTH_SHORT).show()
            } else {
                progressBar2.visibility = View.VISIBLE
                val credential = PhoneAuthProvider.getCredential(otp!!, eotp)
                signinwithphone(credential)
            }
        }
    }

    private fun signinwithphone(credential: PhoneAuthCredential) {
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressBar2.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Wel-Come", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@otpactivity, setproacti::class.java)
                startActivity(intent)
                finish()
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    progressBar2.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}