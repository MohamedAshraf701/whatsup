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
import com.example.finalproject.databinding.ActivityProfileactiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat

class profileacti : AppCompatActivity() {
    var binding: ActivityProfileactiBinding? = null
    var pn: pageadeptor2? = null
    var username: String? = null
    private var propic: String? = null
    var status: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileactiBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        username = intent.getStringExtra("name")
        propic = intent.getStringExtra("propic")
        status = intent.getStringExtra("status")
        binding!!.username2.text = username
        binding!!.status2.text = status
        Glide.with(applicationContext).load(propic).placeholder(R.drawable.man)
            .into(binding!!.userpropic2)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        binding!!.toolbar2.setNavigationOnClickListener { finish() }
        pn = pageadeptor2(supportFragmentManager, binding!!.tb2.tabCount)
        binding!!.viewpager1.adapter = pn
        binding!!.tb2.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.viewpager1.currentItem = tab.position
                if (tab.position == 0 || tab.position == 1) pn!!.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding!!.viewpager1.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding!!.tb2))
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