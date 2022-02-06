@file:Suppress("DEPRECATION", "ClassName")
package com.example.finalproject.activitys
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.os.Build
import com.example.finalproject.R
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import android.content.Intent
import android.icu.util.Calendar
import android.os.Handler
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_editprofileacti.*
import java.text.SimpleDateFormat

class editprofileacti : AppCompatActivity() {
    @Suppress("NAME_SHADOWING")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofileacti)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
       toolbar3.setNavigationOnClickListener { finish() }
       visiblestat.setOnClickListener {
            if (editstatus2.isEnabled) {
                editstatus2.isEnabled = false
                editstatus2.setTextColor(getColor(R.color.text1))
            } else {
               editstatus2.isEnabled = true
               editstatus2.setTextColor(getColor(R.color.white))
            }
        }
        visiname.setOnClickListener {
            if (editname2.isEnabled) {
                editname2.isEnabled = false
                editname2.setTextColor(getColor(R.color.text1))
            } else {
               editname2.isEnabled = true
               editname2.setTextColor(getColor(R.color.white))
            }
        }
        val username = intent.getStringExtra("name")
        val propic = intent.getStringExtra("propic")
        val status = intent.getStringExtra("status")
       editname2.setText(username)
       editstatus2.setText(status)
        Glide.with(applicationContext).load(propic).placeholder(R.drawable.man)
            .into(userpropic3)
        savbtn.setOnClickListener {
            val name = editname2.text.toString()
            val status = editstatus2.text.toString()
            when {
                name == "" -> {
                    editname2.error = "Please Enter your Username"
                }
                status == "" -> {
                    editstatus2.error = "Please Enter your Status"
                }
                else -> {
                    FirebaseDatabase.getInstance().reference.child("Users")
                        .child(FirebaseAuth.getInstance().uid!!).child("username").setValue(name)
                    FirebaseDatabase.getInstance().reference.child("Users")
                        .child(FirebaseAuth.getInstance().uid!!).child("status").setValue(status)
                    editname2.isEnabled = false
                    editstatus2.isEnabled = false
                }
            }
            Toast.makeText(this@editprofileacti, "Saved Info", Toast.LENGTH_LONG).show()
        }
        cr2.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                .start(this@editprofileacti)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
               userpropic3.setImageURI(result.uri)
                val pic = result.uri
                val storage = FirebaseStorage.getInstance()
                val ref = storage.reference.child("profilepictures").child(
                    FirebaseAuth.getInstance().uid!!
                ).child("image")
                ref.putFile(pic).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        ref.downloadUrl.addOnSuccessListener { uri ->
                            FirebaseDatabase.getInstance().reference.child("Users").child(
                                FirebaseAuth.getInstance().uid!!
                            ).child("profilepicture").setValue(uri.toString())
                        }
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this,error.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onStop() {
        super.onStop()
        val simpleDateFormat = SimpleDateFormat("hh:mma")
        val cal = Calendar.getInstance()
        val date = "Lastseen:" + simpleDateFormat.format(cal.time)
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().uid!!).child("lastseen").setValue(date)
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            FirebaseDatabase.getInstance().reference.child("Users").child(
                FirebaseAuth.getInstance().uid!!
            ).child("lastseen").setValue("Online")
        }, 1000)
    }
}