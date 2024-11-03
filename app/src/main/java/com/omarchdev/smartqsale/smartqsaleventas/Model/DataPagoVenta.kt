package com.omarchdev.smartqsale.smartqsaleventas.Model

import java.math.BigDecimal

/*
BigDecimal CantidadCambio, int TipoDocPago,
                          boolean generaDoc, int idTipoAtencion, BigDecimal montoPromocion
                , String obs, BigDecimal montoDetraccion, BigDecimal porcentajeDetraccion,
                          String cuentaDetraccion, boolean usaDetraccion)
*/
data class DataPagoVenta(
    val CantidadCambio: BigDecimal,
    val TipoDocPago: Int,
    val generaDoc: Boolean,
    val idTipoAtencion: Int,
    val montoPromocion: BigDecimal,
    val obs: String,
    val montoDetraccion: BigDecimal,
    val porcentajeDetraccion: BigDecimal,
    val cuentaDetraccion: String,
    val usaDetraccion: Boolean
)
