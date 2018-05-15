package com.sumera.argallery.tools

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import com.sumera.argallery.injection.ApplicationContext
import io.reactivex.Completable
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnityImageSaver @Inject constructor(
        @ApplicationContext private val context: Context
) {

    fun saveImage(url: String): Completable {
        return Completable.fromCallable {
            val bitmap = Glide.with(context).asBitmap().load(url).submit().get()
            saveImageToInternalStorage(bitmap, context)
        }
    }

    private fun saveImageToInternalStorage(image: Bitmap, context: Context): Boolean {
        try {
            val fos = FileOutputStream(File(context.filesDir, "test.png"))
            image.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            return true
        } catch (e: Exception) {
            Timber.e(e)
        }
        return false
    }
}