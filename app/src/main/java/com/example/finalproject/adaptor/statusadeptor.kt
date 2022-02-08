@file:Suppress("ClassName", "UNUSED_ANONYMOUS_PARAMETER")

package com.example.finalproject.adaptor

import android.app.AlertDialog
import android.content.Context
import com.example.finalproject.models.Statusm
import com.example.finalproject.adaptor.statusadeptor.statusviewholder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.finalproject.R
import com.bumptech.glide.Glide
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class statusadeptor(var context: Context, private var statuses: ArrayList<Statusm>) :
    RecyclerView.Adapter<statusviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): statusviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.storeedit, parent, false)
        return statusviewholder(view)
    }

    override fun onBindViewHolder(holder: statusviewholder, position: Int) {
        val statusm = statuses[position]
        Glide.with(context).load(statusm.imgurl).into(holder.storyimg)
        holder.timeofstory.text = statusm.timestamp
        holder.delbtnstory.setOnClickListener {
            val builder = AlertDialog.Builder(
                context
            )
            builder.setMessage("Do you want to delete this Story?")
            builder.setTitle("DELETE !")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { dialog, which ->
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(FirebaseAuth.getInstance().uid!!).child("statuses").child(
                    statusm.key!!
                ).setValue(null)
            }
            builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return statuses.size
    }

    inner class statusviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storyimg: ImageView =itemView.findViewById(R.id.Storyimg)
        val delbtnstory: ImageView =itemView.findViewById(R.id.delbtnstory)
        val timeofstory: TextView =itemView.findViewById(R.id.timeofstory)
    }
}