package com.example.finalproject.adaptor

import java.util.ArrayList
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.finalproject.R
import com.example.finalproject.adaptor.messagesadatpot.sentviewholder
import com.example.finalproject.adaptor.messagesadatpot.recviewholder
import com.google.firebase.auth.FirebaseAuth
import android.annotation.SuppressLint
import android.content.Context
import com.github.pgreze.reactions.ReactionsConfig
import com.github.pgreze.reactions.ReactionsConfigBuilder
import com.github.pgreze.reactions.ReactionPopup
import com.google.firebase.database.FirebaseDatabase
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.models.Message

class messagesadatpot(
    var context: Context,
    var messages: ArrayList<Message>,
    var senderroom: String,
    var recroom: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val itemsent = 1
    val itemrec = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == itemsent) {
            val view = LayoutInflater.from(context).inflate(R.layout.itemsent, parent, false)
            sentviewholder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.itemrec, parent, false)
            recviewholder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (FirebaseAuth.getInstance().uid == message.senderid) itemsent else itemrec
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val reactions = intArrayOf(
            R.drawable.ic_fb_like,
            R.drawable.ic_fb_love,
            R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_wow,
            R.drawable.ic_fb_sad,
            R.drawable.ic_fb_angry
        )
        val config = ReactionsConfigBuilder(context)
            .withReactions(reactions)
            .build()
        val popup = ReactionPopup(context, config, { pos: Int? ->
            if (holder.javaClass == sentviewholder::class.java) {
                val sentviewholder = holder as sentviewholder
                sentviewholder.reaction.setImageResource(reactions[pos!!])
                sentviewholder.reaction.visibility = View.VISIBLE
            } else {
                val recviewholder = holder as recviewholder
                recviewholder.reaction.setImageResource(reactions[pos!!])
                recviewholder.reaction.visibility = View.VISIBLE
            }
            message.feeling = pos
            FirebaseDatabase.getInstance().reference.child("chats").child(senderroom)
                .child("message").child(
                message.msgid!!
            ).setValue(message)
            FirebaseDatabase.getInstance().reference.child("chats").child(recroom).child("message")
                .child(
                    message.msgid!!
                ).setValue(message)
            true // true is closing popup, false is requesting a new selection
        })
        if (holder.javaClass == sentviewholder::class.java) {
            val sentviewholder = holder as sentviewholder
            sentviewholder.sentmsg.text = message.msg
            sentviewholder.timeofmsgs.text = message.timestamp
            if (message.feeling >= 0) {
                sentviewholder.reaction.setImageResource(reactions[message.feeling])
                sentviewholder.reaction.visibility = View.VISIBLE
            } else sentviewholder.reaction.visibility = View.GONE
            sentviewholder.sentmsg.setOnTouchListener { v, event ->
                popup.onTouch(v, event)
                false
            }
        } else {
            val recviewholder = holder as recviewholder
            recviewholder.sentmsg.text = message.msg
            recviewholder.timeofmsgr.text = message.timestamp
            if (message.feeling >= 0) {
                recviewholder.reaction.setImageResource(reactions[message.feeling])
                recviewholder.reaction.visibility = View.VISIBLE
            } else recviewholder.reaction.visibility = View.GONE
            recviewholder.sentmsg.setOnTouchListener { v, event ->
                popup.onTouch(v, event)
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class sentviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var reaction: ImageView
        var sentmsg: TextView
        var timeofmsgs: TextView

        init {
            reaction = itemView.findViewById(R.id.reactions)
            sentmsg = itemView.findViewById(R.id.sentmsgs)
            timeofmsgs = itemView.findViewById(R.id.timeofmsgs)
        }
    }

    inner class recviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var reaction: ImageView
        var sentmsg: TextView
        var timeofmsgr: TextView

        init {
            reaction = itemView.findViewById(R.id.reaction)
            sentmsg = itemView.findViewById(R.id.sentmsg)
            timeofmsgr = itemView.findViewById(R.id.timeofmsgr)
        }
    }
}