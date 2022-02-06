@file:Suppress("ClassName", "DEPRECATION")

package com.example.finalproject.activitys

import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.models.Contuser
import com.example.finalproject.adaptor.contusersAdaptor
import android.os.Bundle
import android.provider.ContactsContract
import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Build
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import com.example.finalproject.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_contactacti.*
import java.text.SimpleDateFormat
import java.util.ArrayList

class contactacti : AppCompatActivity() {
    lateinit var contusers: ArrayList<Contuser>
    var contusersAdaptor: contusersAdaptor? = null
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactacti)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        toolbar4.setNavigationOnClickListener { finish() }
        var i = 0
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val phonebook = arrayOfNulls<String>(phones!!.count)
        while (phones.moveToNext()) {
            // @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") val phoneNumber =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val pn = phoneNumber.replace(" ".toRegex(), "")
            phonebook[i] = pn
            i++
        }
        contusers = ArrayList()
        contusersAdaptor = contusersAdaptor(this, contusers)
        rv1.adapter = contusersAdaptor
        FirebaseDatabase.getInstance().reference.child("Users")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        contusers.clear()
                        for (snapshot1 in snapshot.children) {
                            val user = snapshot1.getValue(Contuser::class.java)
                            if (user!!.uid != FirebaseAuth.getInstance().uid) {
                                for (s in phonebook) {
                                    if (s == user.phonenumber) {
                                        contusers.add(user)
                                        break
                                    }
                                }
                            }
                        }
                        contusersAdaptor!!.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
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
        finish()
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