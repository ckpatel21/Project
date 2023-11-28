package com.example.capstone

import android.location.Address
import android.location.Geocoder
import com.example.capstone.ui.MapDialogFragment
import com.example.capstone.utils.Helper
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class UnitTest {

    @Test
    fun nullCheck(){
        val dummyData = ""
        val result = Helper.nullCheck(dummyData)
        assertEquals(result,false)
    }


}