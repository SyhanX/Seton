package com.example.seton.common.domain.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream

class StorageManager {
    companion object {
        fun saveToInternalStorage(
            context: Context,
            bitmapImage: Bitmap,
            imageFileName: String
        ): String {
            context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use {
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, it)
            }
            return context.filesDir.absolutePath
        }

        fun getImageFromInternalStorage(context: Context, imageFileName: String): Bitmap? {
            val directory = context.filesDir
            val file = File(directory, imageFileName)
            return BitmapFactory.decodeStream(FileInputStream(file))
        }

        fun deleteImageFromInternalStorage(context: Context, imageFileName: String): Boolean {
            val directory = context.filesDir
            val file = File(directory, imageFileName)
            return file.delete()
        }

        fun deleteEverythingFromAppDirectory(context: Context) {
            val directory = context.filesDir
            for (file: File in directory.listFiles()!!) {
                if(file.name != "profileInstalled") {
                    file.delete()
                }
            }
        }

    }
}