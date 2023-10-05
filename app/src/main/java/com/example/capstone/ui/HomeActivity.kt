package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult


class HomeActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Fetch Data
        val intent = intent
        val email = intent.getStringExtra("Email")
        val name = intent.getStringExtra("Name")

        Log.d("Email",email.toString())
        Log.d("Name",name.toString())

        loadFragment(MapsFragment())
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.itemIconTintList = null
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.map -> {
                    loadFragment(MapsFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.place -> {
                    loadFragment(AddPlaceFragment())
                    true
                }
                R.id.event -> {
                    loadFragment(AddEventFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}