package com.example.capstone.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.databinding.ActivitySplashBinding
import com.example.capstone.viewModel.SplashActivityViewModel
import com.example.capstone.viewModel.SplashActivityViewModelFactory

class SplashActivity : AppCompatActivity() {


    private lateinit var activitySplashBinding: ActivitySplashBinding

    private lateinit var splashActivityViewModel: SplashActivityViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

        val splashActivityViewModelFactory = SplashActivityViewModelFactory()
        val splashActivityViewModel = ViewModelProvider(this, splashActivityViewModelFactory).get(SplashActivityViewModel::class.java)

        splashActivityViewModel.timer()

        splashActivityViewModel.navigateToLoginScreen.observe(this) { loginNavigation ->
            if (loginNavigation) {
                //Not registered
                val intent = Intent(this, LoginActivity::class.java)
                //intent.putExtra("Email",email)
                startActivity(intent)
            } else {
                //Registered
                val intent = Intent(this, HomeActivity::class.java)
                //intent.putExtra("Email",email)
                startActivity(intent)

            }
        }

    }
}