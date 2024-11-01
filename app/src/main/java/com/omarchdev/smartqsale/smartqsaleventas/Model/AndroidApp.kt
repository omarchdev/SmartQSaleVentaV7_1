package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

data class AndroidApp ( @SerializedName("packageNameApp")
    var versionName:String="",
    @SerializedName("codeVerApp")
    var versionCode:Int=0


)