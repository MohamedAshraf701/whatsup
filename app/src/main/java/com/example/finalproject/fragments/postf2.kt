@file:Suppress("ClassName")

package com.example.finalproject.fragments

import android.annotation.SuppressLint
import com.example.finalproject.adaptor.userpostadaptor
import com.example.finalproject.models.userposts
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import java.util.ArrayList

class postf2 : Fragment() {
    var topstatusadeptor: userpostadaptor? = null
    var userposts: ArrayList<userposts>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_postf2, container, false)
        userposts = ArrayList()
        topstatusadeptor = userpostadaptor(container!!.context, userposts!!)
        val rv: RecyclerView = view.findViewById(R.id.rv7)
        rv.adapter = topstatusadeptor
        FirebaseDatabase.getInstance().reference.child("posts")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userposts!!.clear()
                        for (snapshot1 in snapshot.children) {
                            val statusm = userposts()
                            statusm.name = snapshot1.child("name").getValue(String::class.java)
                            statusm.post = snapshot1.child("post").getValue(String::class.java)
                            statusm.time = snapshot1.child("time").getValue(String::class.java)
                            statusm.uid = snapshot1.child("uid").getValue(String::class.java)
                            statusm.key = snapshot1.key.toString()
                            if (FirebaseAuth.getInstance().uid == statusm.uid) {
                                userposts!!.add(statusm)
                            }
                        }
                        topstatusadeptor!!.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return view
    }
}