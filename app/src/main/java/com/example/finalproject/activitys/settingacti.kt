@file:Suppress("ClassName", "DEPRECATION")
package com.example.finalproject.activitys
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import com.example.finalproject.R
import kotlinx.android.synthetic.main.activity_settingacti.*
import java.text.SimpleDateFormat

class settingacti : AppCompatActivity() {
    var username: String? = null
    var propic: String? = null
    var status: String? = null
    var phoneno: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settingacti)
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        username = snapshot.child("username").getValue(String::class.java)
                        status = snapshot.child("status").getValue(String::class.java)
                        propic = snapshot.child("profilepicture").getValue(String::class.java)
                        phoneno = snapshot.child("phonenumber").getValue(String::class.java)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        cardView9.setOnClickListener {
            val intent = Intent(this@settingacti, editprofileacti::class.java)
            intent.putExtra("name", username)
            intent.putExtra("propic", propic)
            intent.putExtra("status", status)
            startActivity(intent)
            finish()
        }
            cardView8.setOnClickListener {
            val intent = Intent(this@settingacti, detailacti::class.java)
            intent.putExtra("name", username)
            intent.putExtra("number", phoneno)
            intent.putExtra("status", status)
            startActivity(intent)
        }
        cardView10.setOnClickListener {
            val intent = Intent(this@settingacti, aboutus::class.java)
            startActivity(intent)
        }
        toolbar6.setNavigationOnClickListener { finish() }
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