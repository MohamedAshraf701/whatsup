package com.example.finalproject.fragments
import com.example.finalproject.adaptor.Topstatusadeptor
import com.example.finalproject.models.Userstatusm
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.finalproject.R
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseError
import com.devlomi.circularstatusview.CircularStatusView
import com.example.finalproject.models.Statusm
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import omari.hamza.storyview.model.MyStory
import omari.hamza.storyview.StoryView
import com.example.finalproject.activitys.chatmain
import omari.hamza.storyview.callback.StoryClickListeners
import androidx.annotation.RequiresApi
import android.os.Build
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.ArrayList

class statusf() : Fragment() {
    var topstatusadeptor: Topstatusadeptor? = null
    var userstatusms: ArrayList<Userstatusm>? = null
    lateinit var usersarray: Array<String?>
    var dialog: ProgressDialog? = null
    var username: String? = null
    var imguri1: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statusf, container, false)
        val imv: CircleImageView = view.findViewById(R.id.imview2)
        val tv = view.findViewById<TextView>(R.id.textView132)
        FirebaseDatabase.getInstance().reference.child("Users")
            .child((FirebaseAuth.getInstance().uid)!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        username = snapshot.child("username").getValue(String::class.java)
                        imguri1 = snapshot.child("profilepicture").getValue(String::class.java)
                        tv.text = username
                        Glide.with((activity)!!).load(imguri1).placeholder(R.drawable.man).into(imv)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        dialog = ProgressDialog(activity)
        dialog!!.setMessage("Uploading.....")
        dialog!!.setCancelable(false)
        val rv: RecyclerView= view.findViewById(R.id.rv5)
        val userid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().reference.child("Users").child((userid)!!).child("chatid")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usersarray = arrayOfNulls((snapshot.childrenCount.toInt()))
                    var a = 0
                    for (snapshot1: DataSnapshot in snapshot.children) {
                        usersarray[a] = snapshot1.key
                        a++
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        val cs: CircularStatusView = view.findViewById(R.id.circular_status_view2)
        val tv1 = view.findViewById<TextView>(R.id.textView92)
        userstatusms = ArrayList()
        topstatusadeptor = Topstatusadeptor((activity)!!, userstatusms!!)
        rv.setAdapter(topstatusadeptor)
        FirebaseDatabase.getInstance().reference.child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userstatusms!!.clear()
                        for (snapshot1: DataSnapshot in snapshot.children) {
                            if ((snapshot1.key == userid) && snapshot1.child("statuses").exists()) {
                                val stat = ArrayList<Statusm?>()
                                var ll: String = ""
                                for (snapshot2: DataSnapshot in snapshot1.child("statuses").children) {
                                    val samplestatus = snapshot2.getValue(Statusm::class.java)
                                    ll = samplestatus!!.timestamp.toString()
                                    stat.add(samplestatus)
                                    cs.setPortionsCount(stat.size)
                                }
                                tv1.text = ll
                                if (ll != "") tv1.visibility = View.VISIBLE
                                cs.setOnClickListener { showst(stat) }
                            }
                            for (sa: String? in usersarray) {
                                if ((snapshot1.key == sa)) {
                                    if (snapshot1.child("statuses").exists()) {
                                        val statusm = Userstatusm()
                                        statusm.name = snapshot1.child("username").getValue(
                                            String::class.java
                                        )
                                        statusm.profileimg =
                                            snapshot1.child("profilepicture").getValue(
                                                String::class.java
                                            )
                                        val stat = ArrayList<Statusm>()
                                        for (snapshot2: DataSnapshot in snapshot1.child("statuses").children) {
                                            val samplestatus = snapshot2.getValue(
                                                Statusm::class.java
                                            )
                                            stat.add(samplestatus!!)
                                        }
                                        statusm.statuses=stat
                                        val s = stat[stat.size - 1]
                                        statusm.lastupdate = s!!.timestamp
                                        userstatusms!!.add(statusm)
                                    }
                                    topstatusadeptor!!.notifyDataSetChanged()
                                    break
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        val im: FloatingActionButton
        im = view.findViewById(R.id.floatingActionButton3)
        im.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 75)
        }
        return view
    }

    fun showst(stat: ArrayList<Statusm?>) {
        val myStories = ArrayList<MyStory>()
        for (status: Statusm? in stat) {
            myStories.add(MyStory(status!!.imgurl))
        }
        val builder = StoryView.Builder((context as chatmain?)!!.supportFragmentManager)
        builder.setStoriesList(myStories)
        builder.setStoryDuration(5000)
        builder.setTitleText(username)
        builder.setSubtitleText("")
        builder.setTitleLogoUrl(imguri1)
        builder.setStoryClickListeners(object : StoryClickListeners {
            override fun onDescriptionClickListener(position: Int) {}
            override fun onTitleIconClickListener(position: Int) {}
        })
        builder.build()
        builder.show()
    }


    var simpleDateFormat1: SimpleDateFormat? = null
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 75) {
            if (data != null && data.data != null) {
                dialog!!.show()
                val storage = FirebaseStorage.getInstance()
                simpleDateFormat1 = SimpleDateFormat("hh:mm a")
                var calendar: Calendar= Calendar.getInstance()
                var daat1: String= simpleDateFormat1!!.format(calendar.getTime())
                val reference = storage.reference.child("status").child(daat1)
                reference.putFile(data.data!!)
                    .addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot?> {
                        override fun onComplete(task: Task<UploadTask.TaskSnapshot?>) {
                            if (task.isSuccessful) {
                                reference.downloadUrl.addOnSuccessListener(object :
                                    OnSuccessListener<Uri> {
                                    override fun onSuccess(uri: Uri) {
                                        val imguri2 = uri.toString()
                                        val randomkey =
                                            FirebaseDatabase.getInstance().reference.push().key
                                        val statusm = Statusm(imguri2, daat1, randomkey)
                                        FirebaseDatabase.getInstance().reference.child("Users")
                                            .child(
                                                (FirebaseAuth.getInstance().uid)!!
                                            ).child("statuses").child((randomkey)!!)
                                            .setValue(statusm).addOnCompleteListener(
                                            OnCompleteListener { dialog!!.dismiss() })
                                    }
                                })
                            }
                        }
                    })
            }
        }
    }
}