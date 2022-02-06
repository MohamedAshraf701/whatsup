package com.example.finalproject.activitys
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import com.example.finalproject.R
import android.os.CountDownTimer
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var count = 0
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this@MainActivity, phonenumactivity::class.java)
            startActivity(intent)
            finish()
        } else {
        val an1 = AnimationUtils.loadAnimation(this@MainActivity, R.anim.zooming)
        val an2 = AnimationUtils.loadAnimation(this@MainActivity, R.anim.zooming2)
        imageView.startAnimation(an1)
        textView.visibility = View.VISIBLE
        textView.startAnimation(an2)
        an2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val anitext =textView2.text.toString()
                textView2.text = ""
                textView2.visibility = View.VISIBLE

                object : CountDownTimer((anitext.length * 100).toLong(), 100) {
                    @SuppressLint("SetTextI18n")
                    override fun onTick(l: Long) {
                        textView2.text =textView2.text.toString() + anitext[count]
                        count++
                    }
                    override fun onFinish() {
                        val anitext1 =textView4.text.toString()
                        textView4.text = ""
                        textView4.visibility = View.VISIBLE
                        count = 0
                        object : CountDownTimer((anitext1.length * 100).toLong(), 100) {
                            @SuppressLint("SetTextI18n")
                            override fun onTick(l: Long) {
                                textView4.text =textView4.text.toString() + anitext1[count]
                                count++
                            }
                            override fun onFinish() {
                               getdet()
                            }
                        }.start()
                    }
                }.start()
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        }
    }
    fun getdet()
    {
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().uid!!).get().addOnCompleteListener{ task_ ->
                if(task_.isSuccessful) {
                    val intent = Intent(this@MainActivity, chatmain::class.java)
                    intent.putExtra(
                        "uname",
                        task_.result!!.child("username").getValue(String::class.java)
                    )
                    intent.putExtra(
                        "stat",
                        task_.result!!.child("status").getValue(String::class.java)
                    )
                    intent.putExtra(
                        "imguri",
                        task_.result!!.child("profilepicture").getValue(String::class.java)
                    )
                    startActivity(intent)
                    finish()
                }else
                {
                    Toast.makeText(this@MainActivity,"Connection Lose",Toast.LENGTH_SHORT).show()
                    getdet()
                }
            }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}