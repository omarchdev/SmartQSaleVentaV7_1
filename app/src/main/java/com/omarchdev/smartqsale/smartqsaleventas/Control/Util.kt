package com.omarchdev.smartqsale.smartqsaleventas.Control

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun GenerarFecha(): Int {
    val fecha = Date()
    val dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
    return dateFormat.format(fecha).toString().toInt()
}