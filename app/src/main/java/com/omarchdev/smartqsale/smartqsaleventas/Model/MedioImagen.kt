package com.omarchdev.smartqsale.smartqsaleventas.Model

import android.graphics.Bitmap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.BitmapToBase64
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImageBase64

class MedioImagen {

    @SerializedName("id_medio")
    @Expose(serialize = true)
    var IdMedio=0
    @SerializedName("num_item")
    @Expose(serialize = true)
    var INumItem=0
    @SerializedName("imgDataBase64")
    var ImageDataArray:String=""
    @Expose(serialize = false)
    var bitmapImage: Bitmap?=null
    @SerializedName("key")
    @Expose(serialize = true)
    var key=""

    fun ConvertBitmapToImageDataArray(){

        if(ImageDataArray.equals("")){
            this.ImageDataArray=  bitmapImage!!.BitmapToBase64()
        }


    }

    fun ImageBitmap():Bitmap{

        if(bitmapImage==null){
            return ImageDataArray.ImageBase64()
        }else{
            return bitmapImage!!
        }

    }
}
