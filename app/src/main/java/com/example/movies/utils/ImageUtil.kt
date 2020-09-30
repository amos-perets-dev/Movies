package com.example.movies.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Base64
import com.example.movies.movie_app.MovieApplication
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

class ImageUtil {

    fun blurBitmapFromBitmap(image: Bitmap, levelBlur: Float = 17.5f): Bitmap? {
        val BITMAP_SCALE = 0.2f
        val BLUR_RADIUS = levelBlur
        val width = (image.width * BITMAP_SCALE).roundToInt()
        val height = (image.height * BITMAP_SCALE).roundToInt()
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val rs = RenderScript.create(MovieApplication.context)
        val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        theIntrinsic.setRadius(BLUR_RADIUS)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }

    private fun createByteArrayFromBitmap(bitmap: Bitmap): ByteArray {
        val streamGuestOrComputer = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, streamGuestOrComputer)
        return streamGuestOrComputer.toByteArray()
    }

    fun encodeBase64Image(image: Bitmap): String? = Base64.encodeToString(createByteArrayFromBitmap(image), Base64.DEFAULT)

    fun decodeBase64(input: String?): Bitmap? {
        val decodedBytes = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}