@file:Suppress("ClassName")

package com.example.finalproject.fragments
import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.example.finalproject.adaptor.UsersAdaptor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.finalproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.activitys.contactacti
import com.example.finalproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.ArrayList

class chatf : Fragment() {
    var firebaseDatabase: FirebaseDatabase? = null
    lateinit var usersarray: Array<String?>
    val userid = FirebaseAuth.getInstance().uid
    private lateinit var con:Context
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        con= container!!.context
        val view = inflater.inflate(R.layout.fragment_chatf, container, false)
        val flotbtn: FloatingActionButton = view.findViewById(R.id.floatingActionButton)
        flotbtn.setOnClickListener {
            val intent = Intent(activity, contactacti::class.java)
            startActivity(intent)
        }
        FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("chatid")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usersarray = arrayOfNulls(snapshot.childrenCount.toInt())
                    for ((a, snapshot1) in snapshot.children.withIndex()) {
                        usersarray[a] = snapshot1.key
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        firebaseDatabase = FirebaseDatabase.getInstance()
        val users:ArrayList<User> = ArrayList()
        val usersAdaptor= UsersAdaptor(con, users)
        firebaseDatabase!!.reference.child("Users")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        users.clear()
                        for (snapshot1 in snapshot.children) {
                            val user = snapshot1.getValue(
                                User::class.java
                            )
                            if (user!!.uid != userid) {
                                for (i in usersarray.indices) {
                                    if (user.uid == usersarray[i]) users.add(user)
                                }
                            }
                        }
                        usersAdaptor.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        val rvv:RecyclerView = view.findViewById(R.id.rv443)
        rvv.adapter=usersAdaptor

        return view
    }

}