package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import java.math.BigDecimal


fun montoDecimalTexto(num: BigDecimal):String{

    return String.format("%.2f",num)
}

fun montoDecimalPrecioSimbolo(num:BigDecimal):String{

    return Constantes.DivisaPorDefecto.SimboloDivisa+""+String.format("%.2f",num)
}