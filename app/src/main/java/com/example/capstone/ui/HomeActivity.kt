package com.example.capstone.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Fetch Data
        val intent = intent
        val email = intent.getStringExtra("Email")
        val name = intent.getStringExtra("Name")

        Log.d("Email", email.toString())
        Log.d("Name", name.toString())

        //Default Fragment as Home
        loadFragment(EventFragment())

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.itemIconTintList = null
        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> {
                    loadFragment(EventFragment())
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

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}