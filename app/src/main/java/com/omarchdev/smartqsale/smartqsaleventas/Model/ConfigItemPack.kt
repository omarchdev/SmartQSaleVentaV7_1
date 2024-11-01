package com.omarchdev.smartqsale.smartqsaleventas.Model

import java.math.BigDecimal

data class ConfigItemPack(
        val nombreArticulo: String,
        val precioVenta: BigDecimal,
        val cantidad: BigDecimal,
        val idProducto: Int,
        val idTipoModificaPack: Int,
        val montoModifica:BigDecimal,
        val metodo: Byte)