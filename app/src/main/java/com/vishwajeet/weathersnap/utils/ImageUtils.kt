package com.vishwajeet.weathersnap.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageUtils {

    fun compressImage(context : Context, imageUri: Uri) : File?{
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val compressedFile = File(context.cacheDir, "${UUID.randomUUID()}.jpg")

        return try {
            val out = FileOutputStream(compressedFile)

            originalBitmap.compress(Bitmap.CompressFormat.JPEG,70, out)
            out.flush()
            out.close()
            compressedFile

        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    fun getFileSize(file: File): Long = file.length()

    fun deleteFile(file: File){
        if (file.exists()) file.delete()
    }
}