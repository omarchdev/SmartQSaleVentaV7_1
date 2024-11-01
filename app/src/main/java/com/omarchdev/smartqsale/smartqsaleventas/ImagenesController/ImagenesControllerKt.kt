package com.omarchdev.smartqsale.smartqsaleventas.ImagenesController

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


fun String.ImageBase64(): Bitmap {

    val imageBytes = Base64.decode(this, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return decodedImage
}


fun Bitmap.BitmapToBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
    return encoded.replace(System.getProperty("line.separator").toString(), "")
}