package com.example.finalproject.adaptor
import android.content.Context
import com.example.finalproject.adaptor.UsersAdaptor.userviewholder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.activitys.personchat
import com.example.finalproject.databinding.RowofuserBinding
import com.example.finalproject.models.User
import kotlinx.android.synthetic.main.activity_personchat.*
import java.util.ArrayList

class UsersAdaptor(var context: Context, var users: ArrayList<User>) :
    RecyclerView.Adapter<userviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.rowofuser, parent, false)
        return userviewholder(view)
    }

    override fun onBindViewHolder(holder: userviewholder, position: Int) {
        val user = users[position]
        val senderroom = FirebaseAuth.getInstance().uid + user.uid
        FirebaseDatabase.getInstance().reference.child("chats").child(senderroom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        holder.binding.lastmsg.text = snapshot.child("lastmessage").getValue(
                            String::class.java
                        )
                        holder.binding.lasttime.text = snapshot.child("lastmsgtime").getValue(
                            String::class.java
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        holder.binding.username.text = user.username
        Glide.with(context).load(user.profilepicture).placeholder(R.drawable.man)
            .into(holder.binding.imview)
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(user.uid.toString()).child("lastseen").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var asd = snapshot.getValue(String::class.java)
                    if(asd=="Online")
                        holder.binding.status.text=asd
                    else
                        holder.binding.status.text=""
                }
                override fun onCancelled(error: DatabaseError) {}
            })
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
        return users.size
    }

    inner class userviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowofuserBinding

        init {
            binding = RowofuserBinding.bind(itemView)
        }
    }
}