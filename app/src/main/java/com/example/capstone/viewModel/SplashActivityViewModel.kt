package com.example.capstone.viewModel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.ui.HomeActivity
import com.example.capstone.ui.LoginActivity
import com.example.capstone.utils.Constant

class SplashActivityViewModel() : ViewModel() {

    val preferences: SharedPreferences =
        getSharedPreferences(Constant.LOGIN_CREDENTIAL, Context.MODE_PRIVATE)

    private val _navigateToLoginScreen = MutableLiveData<Boolean>()
    val navigateToLoginScreen: LiveData<Boolean>
        get() = _navigateToLoginScreen

    fun timer(){
        val timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                _navigateToLoginScreen.value = preferences.getString("email", "") == ""
            }
        }
        timer.start()

    }


}