package com.example.finalproject.adaptor

import android.app.AlertDialog
import android.content.Context
import com.example.finalproject.models.Statusm
import com.example.finalproject.adaptor.statusadeptor.statusviewholder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.finalproject.R
import com.bumptech.glide.Glide
import android.content.DialogInterface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.databinding.StoreeditBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class statusadeptor(var context: Context, var statuses: ArrayList<Statusm>) :
    RecyclerView.Adapter<statusviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): statusviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.storeedit, parent, false)
        return statusviewholder(view)
    }

    override fun onBindViewHolder(holder: statusviewholder, position: Int) {
        val statusm = statuses[position]
        Glide.with(context).load(statusm.imgurl).into(holder.binding.Storyimg)
        holder.binding.timeofstory.text = statusm.timestamp
        holder.binding.delbtnstory.setOnClickListener {
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
        var binding: StoreeditBinding

        init {
            binding = StoreeditBinding.bind(itemView)
        }
    }
}