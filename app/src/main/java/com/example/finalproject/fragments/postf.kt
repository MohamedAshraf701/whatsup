package com.example.finalproject.fragments
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.example.finalproject.adaptor.userpostadaptor
import com.example.finalproject.models.userposts
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.content.Intent
import android.icu.util.Calendar
import androidx.annotation.RequiresApi
import android.os.Build
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.HashMap

class postf : Fragment() {

    var firebaseDatabase: FirebaseDatabase? = null
    var userpostadaptor: userpostadaptor? = null
    var userposts: ArrayList<userposts>? = null
    var dialog: ProgressDialog? = null
    lateinit var usersarray: Array<String?>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_postf, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        userposts = ArrayList()
        userpostadaptor = userpostadaptor(container!!.context, userposts!!)
        val rv: RecyclerView = view.findViewById(R.id.rv2)
        rv.setAdapter(userpostadaptor)
        val userid = FirebaseAuth.getInstance().uid
        FirebaseDatabase.getInstance().reference.child("Users").child(userid!!).child("chatid")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usersarray = arrayOfNulls(snapshot.childrenCount.toInt() + 1)
                    usersarray[0] = userid
                    var a = 1
                    for (snapshot1 in snapshot.children) {
                        usersarray[a] = snapshot1.key
                        a++
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        firebaseDatabase!!.reference.child("posts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userposts!!.clear()
                        for (snapshot1 in snapshot.children) {
                            val statusm = userposts()
                            statusm.name = snapshot1.child("name").getValue(String::class.java)
                            statusm.post = snapshot1.child("post").getValue(String::class.java)
                            statusm.time = snapshot1.child("time").getValue(String::class.java)
                            statusm.uid = snapshot1.child("uid").getValue(String::class.java)
                            statusm.key = snapshot1.key.toString()
                            for (s in usersarray) {
                                if (s == statusm.uid) {
                                    userposts!!.add(statusm)
                                    break
                                }
                            }
                        }
                        userpostadaptor!!.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        val cim: FloatingActionButton = view.findViewById(R.id.floatingActionButton2)
        dialog = ProgressDialog(activity)
        dialog!!.setMessage("Uploading.....")
        dialog!!.setCancelable(false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        cim.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 70)
        })
        return view
    }

    var simpleDateFormat1: SimpleDateFormat? = null
    var simpleDateFormat3: SimpleDateFormat? = null
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.data != null) {
            dialog!!.show()
            val storage = FirebaseStorage.getInstance()
            simpleDateFormat1 = SimpleDateFormat("hh:mm a")
            var calendar: Calendar = Calendar.getInstance()
            var daat1: String = simpleDateFormat1!!.format(calendar.getTime())
            val reference = storage.reference.child("post").child(daat1)
            reference.putFile(data.data!!).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener { uri ->
                        val obj = HashMap<String, Any?>()
                        val imguri2 = uri.toString()
                        obj["post"] = imguri2
                        obj["uid"] = FirebaseAuth.getInstance().uid
                        simpleDateFormat3 = SimpleDateFormat("hh:mm a")
                        calendar = Calendar.getInstance()
                        val daat3: String= simpleDateFormat3!!.format(calendar.getTime())
                        obj["time"] = daat3
                        val key = firebaseDatabase!!.reference.push().key
                        obj["key"] = key
                        firebaseDatabase!!.reference.child("posts").child(key!!).setValue(obj)
                            .addOnCompleteListener {
                                Toast.makeText(
                                    activity,
                                    "uploded",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        dialog!!.dismiss()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        userpostadaptor!!.notifyDataSetChanged()
    }
}