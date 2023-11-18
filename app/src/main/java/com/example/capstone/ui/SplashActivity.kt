package com.example.capstone.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.R
import com.example.capstone.utils.Constant.Companion.LOGIN_CREDENTIAL

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val preferences: SharedPreferences =
            this.getSharedPreferences(LOGIN_CREDENTIAL, Context.MODE_PRIVATE)

        val timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                if (preferences.getString("email", "") == "") {
                    //Not registered
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    //intent.putExtra("Email",email)
                    startActivity(intent)
                } else {
                    //Registered
                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                    //intent.putExtra("Email",email)
                    startActivity(intent)

                }
            }
        }
        timer.start()

    }
}