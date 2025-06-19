package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class ConfiguracionEmpresa {

    @SerializedName("valorIgv")
    var valorIgv: Double = 0.0
    @SerializedName("idDivisaSunat")
    var idDivisaSunat: Int = 0
    @SerializedName("simboloDivisa")
    var simboloDivisa: String? = null
    @SerializedName("tieneAreaDespacho")
    var tieneAreaDespacho = false
    @SerializedName("nombreConCategoria")
    var nombreConCategoria = false
    @SerializedName("precioConIgv")
    var precioConIgv = false
    @SerializedName("bUsa_Facturacion")
    var bUsa_Facturacion = false
    @SerializedName("pieNotaVenta")
    var pieNotaVenta: String? = null
    @SerializedName("pieFactura")
    var pieFactura: String? = null
    @SerializedName("pieBoleta")
    var pieBoleta: String? = null
    @SerializedName("zonasAtencion")
    var ZonasAtencion = false
    @SerializedName("idTipoZonaServicio")
    var idTipoZonaServicio = 0
    @SerializedName("cTipoZonaServicio")
    var cTipoZonaServicio: String? = null
    @SerializedName("visibleNumDocumento")
    var visibleNumDocumento = false
    @SerializedName("visibleBusquedaAvanzadaCliente")
    var visibleBusquedaAvanzadaCliente = false
    @SerializedName("obtenerControlClientes")
    var ObtenerControlClientes = false
    @SerializedName("pagoUnico")
    var pagoUnico = false
    @SerializedName("usaPromocion")
    var usaPromocion = false
    @SerializedName("bUsaTipoAtencion")
    var bUsaTipoAtencion = false
    @SerializedName("codeFacturacion")
    var codeFacturacion: String? = null
    @SerializedName("cabeceraPieTicketAdicional")
    var CabeceraPieTicketAdicional = false
    @SerializedName("contenidoPieTicketAdicional")
    var contenidoPieTicketAdicional: String? = null
    @SerializedName("bPieTicketAdicional")
    var bPieTicketAdicional = false
    @SerializedName("linkTicket")
    var linkTicket: String? = null
    @SerializedName("bCategoriaImpresion")
    var bCategoriaImpresion = false
    @SerializedName("bVentaCredito")
    var bVentaCredito = false
    @SerializedName("idDocumentoPagoDefecto")
    var idDocumentoPagoDefecto = 0
    @SerializedName("bUsaFechaEntrega")
    var bUsaFechaEntrega = false
    @SerializedName("bImprimePagosPrecuenta")
    var bImprimePagosPrecuenta = false
    @SerializedName("bImprimePrecuentaAutomatica")
    var bImprimePrecuentaAutomatica = false
    @SerializedName("cMensajePrecuenta")
    var cMensajePrecuenta: String? = null
    @SerializedName("bCategoriaDefecto")
    var bCategoriaDefecto = false
    @SerializedName("idCategoriaDefecto")
    var idCategoriaDefecto = 0
    @SerializedName("cLinkBaseWeb")
    var cLinkBaseWeb: String? = null
    @SerializedName("cLinkAddPedidoNuevo")
    var cLinkAddPedidoNuevo: String? = null
    @SerializedName("cLinkAddPedidoExistente")
    var cLinkAddPedidoExistente: String? = null
    @SerializedName("cLinkAddPedidoConsulta")
    var cLinkAddPedidoConsulta: String? = null
    @SerializedName("iTiempoLecturaPedido")
    var iTiempoLecturaPedido = 0
    @SerializedName("bUsaAdelantoPagoPedido")
    var bUsaAdelantoPagoPedido = false
    @SerializedName("cTipoPantallaPedido")
    var cTipoPantallaPedido: String? = null
    @SerializedName("cConfiguracionPantallaPedido")
    var cConfiguracionPantallaPedido: String? = null
    @SerializedName("bVisibleBtnCambioPantalla")
    var bVisibleBtnCambioPantalla = false
    @SerializedName("bUsaAforo")
    var bUsaAforo = false
    @SerializedName("cCodigo_detraccion_default")
    var cCodigo_detraccion_default: String? = null
    @SerializedName("bUsaDetraccion")
    var bUsaDetraccion = false
    @SerializedName("nMonto_minimo_uso_detraccion")
    var nMonto_minimo_uso_detraccion = 0.0

}