@file:Suppress("ClassName", "RedundantSamConstructor", "UNUSED_ANONYMOUS_PARAMETER")

package com.example.finalproject.adaptor

import android.app.AlertDialog
import android.content.Context

import com.example.finalproject.models.userposts
import com.example.finalproject.adaptor.userpostadaptor.postviewholder
import com.google.firebase.auth.FirebaseAuth
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.finalproject.R
import android.content.DialogInterface
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseError
import java.util.ArrayList

class userpostadaptor(val context: Context, private val userpostes: ArrayList<userposts>) :
    RecyclerView.Adapter<postviewholder>() {
    val uid = FirebaseAuth.getInstance().uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.postofusers, parent, false)
        return postviewholder(view)
    }

    override fun onBindViewHolder(holder: postviewholder, position: Int) {
        val up = userpostes[position]
        if ((up.uid == FirebaseAuth.getInstance().uid)) {
            holder.delbtn.visibility =
                View.VISIBLE
        } else {
            holder.delbtn.visibility = View.INVISIBLE
        }
        holder.delbtn.setOnClickListener{
                val builder = AlertDialog.Builder(
                    context
                )
                builder.setMessage("Do you want to delete this post ?")
                builder.setTitle("DELETE !")
                builder.setCancelable(false)
                builder.setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        FirebaseDatabase.getInstance().reference.child("posts").child((up.key)!!)
                            .setValue(null)
                    })
                builder.setNegativeButton("No"
                ) { dialog, which -> dialog.cancel() }
            val alertDialog = builder.create()
                alertDialog.show()
            }

        FirebaseDatabase.getInstance().reference.child("Users").child((up.uid)!!)
            .child("profilepicture").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Glide.with(context).load(snapshot.getValue(String::class.java))
                    .placeholder(R.drawable.man).into(holder.imageview3)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        FirebaseDatabase.getInstance().reference.child("Users").child((up.uid)!!).child("username")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    up.name = snapshot.getValue(String::class.java)
                    holder.textview10.text = up.name
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        val key = up.key
        FirebaseDatabase.getInstance().reference.child("posts").child((key)!!).child("like")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var a = 0
                    for (snapshot1: DataSnapshot in snapshot.children) {
                        if ((snapshot1.getValue(String::class.java) == "true")) {
                            a++
                            if ((snapshot1.key == FirebaseAuth.getInstance().uid)) {
                                holder.imageview4.visibility = View.INVISIBLE
                                holder.imageview5.visibility = View.VISIBLE
                            }
                        } else if ((snapshot1.getValue(String::class.java) == "false") && (snapshot1.key == FirebaseAuth.getInstance().uid)) {
                            holder.imageview4.visibility = View.VISIBLE
                            holder.imageview5.visibility = View.INVISIBLE
                        }
                    }
                    val b = a.toString()
                    holder.likecount2.text = b
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        holder.timetxt.text = up.time
        Glide.with(context).load(up.post).placeholder(R.drawable.man)
            .into(holder.imageview2)
        holder.imageview4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                holder.imageview4.visibility = View.INVISIBLE
                holder.imageview5.visibility = View.VISIBLE
                FirebaseDatabase.getInstance().reference.child("posts").child((key)).child("like")
                    .child(
                        (uid)!!
                    ).setValue("true")
            }
        })
        holder.imageview5.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                holder.imageview5.visibility = View.INVISIBLE
                holder.imageview4.visibility = View.VISIBLE
                FirebaseDatabase.getInstance().reference.child("posts").child((key)).child("like")
                    .child(
                        (uid)!!
                    ).setValue("false")
            }
        })
    }

    override fun getItemCount(): Int {
        return userpostes.size
    }

    inner class postviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageview3: ImageView =itemView.findViewById(R.id.imageView3)
        val imageview2: ImageView =itemView.findViewById(R.id.imageView2)
        val imageview4: ImageView =itemView.findViewById(R.id.imageView4)
        val imageview5: ImageView =itemView.findViewById(R.id.imageView5)
        val likecount2: TextView =itemView.findViewById(R.id.likescount2)
        val timetxt: TextView =itemView.findViewById(R.id.timetxt)
        val textview10: TextView =itemView.findViewById(R.id.textView10)
        val delbtn:ImageButton =itemView.findViewById(R.id.delbtn)
    }
}