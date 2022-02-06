@file:Suppress("DEPRECATION")

package com.example.finalproject.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detailacti.*
import java.text.SimpleDateFormat

@Suppress("ClassName")
class detailacti : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailacti)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        val username = intent.getStringExtra("name")
        val number = intent.getStringExtra("number")
        val status = intent.getStringExtra("status")
        usernametxtd.text = username
        statxtd.text = status
        phonetxtd.text = number
        button2.setOnClickListener {
        FirebaseDatabase.getInstance().reference.child(FirebaseAuth.getInstance().uid.toString()).setValue(null)
            FirebaseAuth.getInstance().currentUser!!.delete()
        intent= Intent(this,phonenumactivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
        toolbar7.setNavigationOnClickListener { finish() }
    }
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onStop() {
        super.onStop()
        val simpleDateFormat = SimpleDateFormat("hh:mma")
        val cal = Calendar.getInstance()
        val date = "Lastseen:" + simpleDateFormat.format(cal.time)
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().uid!!).child("lastseen").setValue(date)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            FirebaseDatabase.getInstance().reference.child("Users").child(
                FirebaseAuth.getInstance().uid!!
            ).child("lastseen").setValue("Online")
        }, 1000)
    }
}