package com.example.capstone

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.capstone.ui.SplashActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SplashActivityTest {
    @Before
    fun setup() {
        // Launch the activity
        ActivityScenario.launch(SplashActivity::class.java)
    }
    @Test
    fun testImageViewDisplayed() {
        // Check if the ImageView is displayed
        onView(withId(R.id.imageView2))
            .check(matches(isDisplayed()))
    }
}