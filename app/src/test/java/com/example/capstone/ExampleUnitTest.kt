package com.example.capstone

import androidx.test.core.app.ActivityScenario
import com.example.capstone.ui.EventFragment
import com.example.capstone.ui.SplashActivity
import org.junit.Assert.assertEquals
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
/*
    @Before
    fun setup() {
        // Launch the activity
        ActivityScenario.launch(SplashActivity::class.java)
    }

    @Test
    fun testHelloTextDisplayed() {
        // Check if the TextView with ID textViewHello has the correct text
        onView(withId(R.id.textView))
            .check(matches(withText("The Explorer")))

    }*/
}