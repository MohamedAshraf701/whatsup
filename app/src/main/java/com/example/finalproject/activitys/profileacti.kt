@file:Suppress("ClassName", "DEPRECATION")

package com.example.finalproject.activitys

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.adaptor.pageadeptor2
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_profileacti.*
import java.text.SimpleDateFormat

class profileacti : AppCompatActivity() {
    var pn: pageadeptor2? = null
    var username: String? = null
    private var propic: String? = null
    var status: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileacti)
        username = intent.getStringExtra("name")
        propic = intent.getStringExtra("propic")
        status = intent.getStringExtra("status")
        username2.text = username
        status2.text = status
        Glide.with(applicationContext).load(propic).placeholder(R.drawable.man)
            .into(userpropic2)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        toolbar2.setNavigationOnClickListener { finish() }
        pn = pageadeptor2(supportFragmentManager, tb2.tabCount)
        viewpager1.adapter = pn
        tb2.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager1.currentItem = tab.position
                if (tab.position == 0 || tab.position == 1) pn!!.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewpager1.addOnPageChangeListener(TabLayoutOnPageChangeListener(tb2))
    }

    @Suppress("UNUSED_EXPRESSION")
    fun editproclicked(item: MenuItem?) {
        item
        val intent = Intent(this@profileacti, editprofileacti::class.java)
        intent.putExtra("name", username)
        intent.putExtra("propic", propic)
        intent.putExtra("status", status)
        startActivity(intent)
        finish()
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