package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class SolicitudEnvio<T>(@SerializedName("code_cia") val codeCia: String,
                             @SerializedName("tipo_movimiento") val tipoMov: String,
                             @SerializedName("data") val data: T,
                             @SerializedName("id_terminal") var idTerminal: Int = 0,
                             @SerializedName("id_usuario") var idUsuario: Int = 0)


data class PedidoVentaPermite(@SerializedName("idCabeceraPedido") val idCabeceraPedido: Int,
                              @SerializedName("validaPagos") val validaPagos: Boolean)

data class PagoTemporalSolicitud(@SerializedName("idCabeceraPedido") val idCabeceraPedido: Int,
                                 @SerializedName("idMetodoPago") val IdMetodoPago: Int,
                                 @SerializedName("codigoPago") val codigoPago: String,
                                 @SerializedName("nombreMetodoPago") val NombreMetodoPago: String,
                                 @SerializedName("cantidadPagada") val CantidadPagada: BigDecimal)

data class VentaGeneracion(@SerializedName("idCabeceraPedido") val idCabeceraPedido: Int,
                           @SerializedName("cambio") val cambio: BigDecimal,
                           @SerializedName("idTipoDoc") val idTipoDoc: Int,
                           @SerializedName("generaFac") val generaFac: Boolean,
                           @SerializedName("idTipoAtencion") val idTipoAtencion: Int,
                           @SerializedName("montoPromocion") val montoPromocion: BigDecimal,
                           @SerializedName("observacion") val observacion: String,
                           @SerializedName("dataKey") val DataKey: Int,
                           @SerializedName("montoDetraccion") val montoDetraccion: BigDecimal,
                           @SerializedName("porcentajeDetraccion") val porcentajeDetraccion: BigDecimal,
                           @SerializedName("numeroCuenta") val numeroCuentaDetraccion: String,
                           @SerializedName("usaDetraccion") val usaDetraccion: Boolean,

)