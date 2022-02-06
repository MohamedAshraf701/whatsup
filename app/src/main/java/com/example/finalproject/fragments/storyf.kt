package com.example.finalproject.fragments

import com.example.finalproject.adaptor.statusadeptor
import com.example.finalproject.models.Statusm
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.ArrayList

class storyf : Fragment() {
    var statusadept: statusadeptor? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_storyf, container, false)
        val rv: RecyclerView = view.findViewById(R.id.rv6)
        rv.layoutManager = GridLayoutManager(activity, 2)
        var statusms: ArrayList<Statusm> = ArrayList()
        statusadept = statusadeptor(container!!.context, statusms)
        rv.adapter = statusadept
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("statuses").exists()) {
                        statusms!!.clear()
                        val stat = ArrayList<Statusm>()
                        for (snapshot2 in snapshot.child("statuses").children) {
                            val samplestatus = snapshot2.getValue(Statusm::class.java)
                            statusms!!.add(samplestatus!!)
                        }
                        statusadept!!.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return view
    }
}