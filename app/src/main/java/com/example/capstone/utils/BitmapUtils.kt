package com.example.capstone.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import java.io.ByteArrayOutputStream

class BitmapUtils {
    companion object {
        fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
            // Convert the bitmap to a byte array
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, 100, outputStream)
            val bitmapData: ByteArray = outputStream.toByteArray()

            // Create a temporary in-memory Uri
            val uriString = "data:image/jpeg;base64," + android.util.Base64.encodeToString(bitmapData, android.util.Base64.NO_WRAP)
            return Uri.parse(uriString)
        }
    }
}