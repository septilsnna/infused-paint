package com.mobcom.paintly

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri

internal object ImageHelper {
    fun loadSizeLimitedBitmapFromUri(
        imageUri: Uri?,
        contentResolver: ContentResolver,
        imageMaxSideLength: Int
    ): Bitmap? {
        return try {
            var imageInputStream = contentResolver.openInputStream(imageUri!!)
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val outPadding = Rect()
            BitmapFactory.decodeStream(imageInputStream, outPadding, options)
            var maxSideLength = Math.max(options.outWidth, options.outHeight)
            options.inSampleSize = 1
            options.inSampleSize = calculateSampleSize(maxSideLength, imageMaxSideLength)
            options.inJustDecodeBounds = false
            imageInputStream!!.close()
            imageInputStream = contentResolver.openInputStream(imageUri)
            var bitmap = BitmapFactory.decodeStream(imageInputStream, outPadding, options)
            maxSideLength = Math.max(bitmap!!.width, bitmap.height)
            val ratio = imageMaxSideLength / maxSideLength.toDouble()
            if (ratio < 1) {
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    (bitmap.width * ratio).toInt(),
                    (bitmap.height * ratio).toInt(),
                    false
                )
            }
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateSampleSize(maxSideLength: Int, expectedMaxImageSideLength: Int): Int {
        var maxSideLength = maxSideLength
        var inSampleSize = 1
        while (maxSideLength > 2 * expectedMaxImageSideLength) {
            maxSideLength /= 2
            inSampleSize *= 2
        }
        return inSampleSize
    }
}
