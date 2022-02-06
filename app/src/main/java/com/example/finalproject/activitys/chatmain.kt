@file:Suppress("DEPRECATION")

package com.example.finalproject.activitys
import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.adaptor.pageadeptor
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chatmain.*
import java.text.SimpleDateFormat

@Suppress("ClassName", "UNUSED_EXPRESSION")
class chatmain : AppCompatActivity() {
    var pageadeptor: pageadeptor? = null
    private var imguri:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatmain)
        val b: Bundle = intent.extras!!
        imguri=b.get("imguri").toString()
        usernametxtmain.text = b.get("uname").toString()
        statustxt.text = b.get("stat").toString()
        Glide.with(applicationContext).load(imguri)
            .placeholder(R.drawable.man).into(userpropic)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        pageadeptor = pageadeptor(supportFragmentManager, tablay1.tabCount)
        viewpager.adapter = pageadeptor
        tablay1.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.currentItem = tab.position
                if (tab.position == 0 || tab.position == 1 || tab.position == 2) pageadeptor!!.notifyDataSetChanged()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewpager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tablay1))
    }
    fun profilemenuclicked(item: MenuItem?) {
        item
        val intent = Intent(this@chatmain, profileacti::class.java)
        intent.putExtra("name", usernametxtmain.text.toString())
        intent.putExtra("propic",imguri )
        intent.putExtra("status", statustxt.text.toString())
        startActivity(intent)
    }
    fun settingmenuclicked(item: MenuItem?) {
        item
        val intent = Intent(this@chatmain, settingacti::class.java)
        startActivity(intent)
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


