@file:Suppress("MemberVisibilityCanBePrivate", "ClassName")

package com.example.finalproject.adaptor
import android.content.Context
import com.example.finalproject.models.Userstatusm
import com.example.finalproject.adaptor.Topstatusadeptor.topstatusviewholder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.bumptech.glide.Glide
import com.devlomi.circularstatusview.CircularStatusView
import omari.hamza.storyview.model.MyStory
import omari.hamza.storyview.StoryView
import com.example.finalproject.activitys.chatmain
import omari.hamza.storyview.callback.StoryClickListeners
import java.util.ArrayList

class Topstatusadeptor(val context: Context, private var userstatuses: ArrayList<Userstatusm>) :
    RecyclerView.Adapter<topstatusviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): topstatusviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemstatus, parent, false)
        return topstatusviewholder(view)
    }

    override fun onBindViewHolder(holder: topstatusviewholder, position: Int) {
        val userstatusm = userstatuses[position]
        Glide.with(context).load(userstatusm.profileimg).into(holder.imview)
        holder.textView9.text = userstatusm.lastupdate
        holder.textView13.text = userstatusm.name
        holder.cir.setPortionsCount(userstatusm.statuses!!.size)
        holder.cr1.setOnClickListener { showst(userstatusm) }
        holder.cir.setOnClickListener { showst(userstatusm) }
    }

    fun showst(userstatusm: Userstatusm) {
        val myStories = ArrayList<MyStory>()
        for (status in userstatusm.statuses!!) {
            myStories.add(MyStory(status.imgurl))
        }
        val builder = StoryView.Builder((context as chatmain).supportFragmentManager)
        builder.setStoriesList(myStories)
        builder.setStoryDuration(5000)
        builder.setTitleText(userstatusm.name)
        builder.setSubtitleText("")
        builder.setTitleLogoUrl(userstatusm.profileimg)
        builder.setStoryClickListeners(object : StoryClickListeners {
            override fun onDescriptionClickListener(position: Int) {}
            override fun onTitleIconClickListener(position: Int) {}
        })
        builder.build()
        builder.show()
    }

    override fun getItemCount(): Int {
        return userstatuses.size
    }

    inner class topstatusviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imview: ImageView =itemView.findViewById(R.id.imview3)
        val cir:CircularStatusView =itemView.findViewById(R.id.circular_status_view)
        val textView13: TextView =itemView.findViewById(R.id.textView13)
        val textView9: TextView =itemView.findViewById(R.id.textView9)
        val cr1:CardView =itemView.findViewById(R.id.cr1)
    }
}