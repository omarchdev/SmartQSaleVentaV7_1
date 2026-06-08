package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

data class MensajeError(
    @SerializedName("cMensaje") val cMensaje: String,
    @SerializedName("lcritico") val lcritico: Boolean
)
