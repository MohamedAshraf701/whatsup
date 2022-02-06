@file:Suppress("ClassName")

package com.example.finalproject.adaptor
import android.content.Context
import com.example.finalproject.models.Contuser
import com.example.finalproject.adaptor.contusersAdaptor.contuserviewholder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.finalproject.R
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.activitys.personchat
import com.example.finalproject.databinding.RowofcontBinding
import java.util.ArrayList

class contusersAdaptor(var context: Context, private var contusers: ArrayList<Contuser>) :
    RecyclerView.Adapter<contuserviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contuserviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.rowofcont, parent, false)
        return contuserviewholder(view)
    }

    override fun onBindViewHolder(holder: contuserviewholder, position: Int) {
        val user = contusers[position]
        holder.binding.username.text = user.username
        Glide.with(context).load(user.profilepicture).placeholder(R.drawable.man)
            .into(holder.binding.imview)
        holder.binding.numbertxt.text = user.phonenumber
        holder.itemView.setOnClickListener {
            val intent = Intent(context, personchat::class.java)
            intent.putExtra("name", user.username)
            intent.putExtra("uid", user.uid)
            intent.putExtra("uri", user.profilepicture)
            intent.putExtra("lastseen", user.lastseen)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return contusers.size
    }

    inner class contuserviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowofcontBinding = RowofcontBinding.bind(itemView)

    }
}