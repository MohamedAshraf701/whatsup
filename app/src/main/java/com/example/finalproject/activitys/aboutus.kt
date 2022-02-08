package com.example.finalproject.activitys

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_aboutus.*
import java.text.SimpleDateFormat

@Suppress("DEPRECATION", "ClassName")
class aboutus : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_aboutus)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        toolbar8.setNavigationOnClickListener { finish() }
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