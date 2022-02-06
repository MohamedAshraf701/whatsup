package com.example.finalproject.adaptor

import android.content.Context

import com.example.finalproject.models.Userstatusm
import com.example.finalproject.adaptor.Topstatusadeptor.topstatusviewholder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.bumptech.glide.Glide
import omari.hamza.storyview.model.MyStory
import com.example.finalproject.models.Statusm
import omari.hamza.storyview.StoryView
import com.example.finalproject.activitys.chatmain
import com.example.finalproject.databinding.ItemstatusBinding
import omari.hamza.storyview.callback.StoryClickListeners
import java.util.ArrayList

class Topstatusadeptor(var context: Context, var userstatuses: ArrayList<Userstatusm>) :
    RecyclerView.Adapter<topstatusviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): topstatusviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemstatus, parent, false)
        return topstatusviewholder(view)
    }

    override fun onBindViewHolder(holder: topstatusviewholder, position: Int) {
        val userstatusm = userstatuses[position]
        Glide.with(context).load(userstatusm.profileimg).into(holder.binding.imview)
        holder.binding.textView9.text = userstatusm.lastupdate
        holder.binding.textView13.text = userstatusm.name
        holder.binding.circularStatusView.setPortionsCount(userstatusm.statuses!!.size)
        holder.binding.cr1.setOnClickListener { showst(userstatusm) }
        holder.binding.circularStatusView.setOnClickListener { showst(userstatusm) }
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
        var binding: ItemstatusBinding

        init {
            binding = ItemstatusBinding.bind(itemView)
        }
    }
}