@file:Suppress("DEPRECATION", "ClassName")
package com.example.finalproject.activitys
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.adaptor.messagesadatpot
import com.google.firebase.database.FirebaseDatabase
import androidx.annotation.RequiresApi
import android.os.Build
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.models.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.activity_personchat.*
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.HashMap

class personchat : AppCompatActivity() {

    var adaptor: messagesadatpot? = null
    var messages: ArrayList<Message?>? = null
    private var senderroom: String? = null
    private var recroom: String? = null
    val database: FirebaseDatabase= FirebaseDatabase.getInstance()
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personchat)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        val uri = intent.getStringExtra("uri")
        Glide.with(applicationContext).load(uri).placeholder(R.drawable.man)
            .into(imageView8)
        toolbar5.setNavigationOnClickListener { finish() }
        val username = intent.getStringExtra("name")
        usernametxt2.text=username
        val recid = intent.getStringExtra("uid")
        val senid = FirebaseAuth.getInstance().uid
        senderroom = senid + recid
        recroom = recid + senid
        database.reference.child("chats").child(senderroom!!).child("message")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages!!.clear()
                    for (dataSnapshot in snapshot.children) {
                        val massege = dataSnapshot.getValue(
                            Message::class.java
                        )
                        massege!!.msgid = dataSnapshot.key
                        messages!!.add(massege)
                    }
                    adaptor!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        messages = ArrayList()
        adaptor = messagesadatpot(this, messages, senderroom, recroom)
        rv3.layoutManager = LinearLayoutManager(this)
        rv3.adapter = adaptor
        rv3.scrollToPosition(messages!!.size)
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(recid.toString()).child("lastseen").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    statustxt2.text = snapshot.getValue(String::class.java)
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        sndbtn.setOnClickListener {
            val msgbox = msgtxt.text.toString()
            val calendar = Calendar.getInstance()
            msgtxt.setText("")
            val randomkey = database.reference.push().key
            @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("hh:mm a")
            @Suppress("UsePropertyAccessSyntax") val message = Message(msgbox, senid, simpleDateFormat.format(calendar.getTime()))
            val lastmsg = HashMap<String, Any>()
            lastmsg["lastmessage"] = message.msg.toString()
            lastmsg["lastmsgtime"] = message.timestamp.toString()
            database.reference.child("chats").child(senderroom!!).updateChildren(lastmsg)
            database.reference.child("chats").child(recroom!!).updateChildren(lastmsg)
            database.reference.child("chats").child(senderroom!!).child("message").child(
                randomkey!!
            ).setValue(message).addOnSuccessListener {
                database.reference.child("chats").child(recroom!!).child("message").child(
                    randomkey
                ).setValue(message).addOnSuccessListener {
                    database.reference.child("Users").child(FirebaseAuth.getInstance().uid!!)
                        .child("chatid").child(
                        recid!!
                    ).setValue("true")
                    database.reference.child("Users").child(recid).child("chatid").child(
                        FirebaseAuth.getInstance().uid!!
                    ).setValue("true")
                }
            }
        }
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