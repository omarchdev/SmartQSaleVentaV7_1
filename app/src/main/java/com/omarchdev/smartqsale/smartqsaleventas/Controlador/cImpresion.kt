package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.content.Context
import android.os.Build
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.fortMoneda
import com.omarchdev.smartqsale.smartqsaleventas.Bluetooth.BluetoothConnection
import com.omarchdev.smartqsale.smartqsaleventas.Bluetooth.WifiConnection
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.bg
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.PrintOptions
import com.omarchdev.smartqsale.smartqsaleventas.UsbConnect.UsbController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal


class cImpresion(context: Context) {
    val context = context
    private val dbHelper = DbHelper(context)
    internal var btConnection: BluetoothConnection =
        BluetoothConnection.getSinglentonInstance(context)

    //List<mPagosEnVenta> pagosEnVentaList;
    fun ImpresionPedido(pedido: Pedido): String {
        val productos = pedido.listProducto
        val cabeceraPedido = pedido.cabeceraPedido
        val pagos = pedido.pagosEnPedido
        val entregaPedido = pedido.entregaPedidoInfo
        var textoCuerpo = ""
        val constructorFactura = ConstructorFactura()
        val docVenta = DocVenta()
        var r = ""

        if (pedido.cabeceraPedido.zonaServicio.idZona != 0) {
            docVenta.imageQrZonaServicio = pedido.cabeceraPedido.GetQrCabeceraZonaServicio()
        }

        docVenta.bEstadoEntrega = cabeceraPedido.isbEntregado()
        if (cabeceraPedido.observacion.trim().isNotEmpty()) {
            docVenta.observacion = "Observacion :   " + cabeceraPedido.observacion.replace("\\n","\n")

        }
        if (cabeceraPedido.identificadorPedido.toString().trim().isNotEmpty()) {
            docVenta.identificador =
                "Identificador :  " + cabeceraPedido.identificadorPedido.toString().trim()
        } else {
            docVenta.identificador = ""
        }
        docVenta.total = completarEspaciosI(
            18, "TOTAL  " +
                    "${Constantes.SimboloMoneda.moneda}"
        ) + completarEspaciosI(
            12,
            "${cabeceraPedido.totalNeto.fortMoneda}"
        )
        docVenta.nroPedido = cabeceraPedido.GetIndentificadorUnicoSimple()
        docVenta.fechaEmision = "Fecha del pedido: " + "\n" + cabeceraPedido.fechaReserva
        if (Constantes.ConfigTienda.bUsaFechaEntrega) {
            docVenta.fechaEntrega =
                "Fecha de Entrega: " + "\n" + cabeceraPedido.fechaEntrega.replace("-", "/")

        }

        when (cabeceraPedido.getcTipoPedido().trim()) {

            "01" -> docVenta.tipoDoc = "${Constantes.Tienda.nombreTienda}\nPre cuenta"
            "02" -> docVenta.tipoDoc =
                "${Constantes.Tienda.nombreTienda}\n${cabeceraPedido.GetIdentificadorUnicoPedido()}"

        }


        docVenta.pagosVenta = TextoPagosVenta(pagos)
        if (cabeceraPedido.vendedor != null) {
            if (cabeceraPedido.vendedor.idVendedor != 0) {
                docVenta.nombreVendedor =
                    "Vendedor: " + cabeceraPedido.vendedor.primerNombre.replaceSpecialChar
            } else {
                docVenta.nombreVendedor = "Vendedor: -"
            }
        } else {
            docVenta.nombreVendedor = "Vendedor: -"
        }

        if (pedido.idEntregaPedido != 0) {
            docVenta.datosEntrega = "\n\n---Datos de entrega---\n"
            docVenta.datosEntrega =
                "${docVenta.datosEntrega}Nro Pedido : ${entregaPedido.cNumeroPedido}\n"
            docVenta.datosEntrega =
                "${docVenta.datosEntrega}Cliente :\n${entregaPedido.clienteEntrega.getcName().removerTilde()} ${entregaPedido.clienteEntrega.getcApellidoPaterno().removerTilde()}\n"
            docVenta.datosEntrega =
                "${docVenta.datosEntrega}Email :\n${entregaPedido.clienteEntrega.cemail2.removerTilde()}\n"
            docVenta.datosEntrega =
                "${docVenta.datosEntrega}Nro Celular : ${entregaPedido.clienteEntrega.getcNumberPhone()}\n"
            docVenta.datosEntrega =
                "${docVenta.datosEntrega}Metodo de pago :\n${entregaPedido.medioPagoEntrega.cDescripcionMedioPago.removerTilde()}\n"
            docVenta.datosEntrega =
                "${docVenta.datosEntrega}Direccion :\n${entregaPedido.tipoEntregaPedido.getDireccion().removerTilde()}\n"
            docVenta.datosEntrega =
                "${docVenta.datosEntrega}Tipo entrega :\n${entregaPedido.tipoEntregaPedido.descripcionTipoEntrega.removerTilde()}\n"
            docVenta.datosEntrega =
                "${docVenta.datosEntrega}Hora de entrega :\n${entregaPedido.tiempoEntregaPedido.cDescripcionEntrega.removerTilde()}\n\n\n"
        }
        if (cabeceraPedido.cliente != null) {
            if (cabeceraPedido.cliente.razonSocial.trim().length > 0) {
                if (cabeceraPedido.cliente.numeroRuc.trim().isEmpty()) {
                    docVenta.nombreReceptor =
                        "Cliente: " + cabeceraPedido.cliente.razonSocial.trim().replaceSpecialChar

                } else {
                    docVenta.nombreReceptor =
                        "Cliente: " + cabeceraPedido.cliente.razonSocial.trim().replaceSpecialChar + "\nNumero Documento : " + cabeceraPedido.cliente.numeroRuc
                }
            } else {
                docVenta.nombreReceptor = "Cliente: " + "-"
            }
        } else {
            docVenta.nombreReceptor = "Cliente: " + "-"
        }


        if (cabeceraPedido.identificadorPedido != null) {
            if (cabeceraPedido.identificadorPedido.trim().length > 0) {
                docVenta.identificador =
                    "Identificador: " + cabeceraPedido.identificadorPedido.replace(
                        "\\n",
                        "\n"
                    ).replaceSpecialChar
            } else {
                docVenta.identificador = " "
            }
        }
        textoCuerpo =
            constructorFactura.generarListadoItems53mmPedidoPreCuenta(productos).replaceSpecialChar
        docVenta.productos = textoCuerpo
        docVenta.cabecerasTicket = completarEspacios(10, "Desc") +
                completarEspacios(5, "Cant") +
                completarEspacios(16, "P.U") +
                completarEspacios(5, "P.T")


        when (getMedioImpresion()) {
            "PDF" -> {
            }
            "Bluetooth" -> {
                 GlobalScope.launch  {
                    try {
                        var address = dbHelper.AddressBT()
                        if (!address.equals("N")) {
                            btConnection.selectDevice(address)
                            btConnection.openBT()
                            var printOptions = PrintOptions(
                                context, btConnection.outputStream,
                                btConnection.mmInputStream
                            )
                            printOptions.ImprimirPedidoPrecuenta(docVenta)
                            printOptions.ImpresionAdicionalPedido(docVenta)
                            //     printOptions.CerrarConexion()
                            r = "B"
                        } else {
                            r = "N"
                        }
                    } catch (e: Exception) {
                        e.toString()
                    }

                }

            }
            "Impresora en Red" -> {
                //  textoCuerpo = constructorFactura.generarListadoItems80mm(productos).replaceSpecialChar
                var impresora = dbHelper.ObtenerImpresoraRed()
                if (!impresora.IP.equals("") && impresora.puerto != 0) {
                     GlobalScope.launch  {
                        var wifiConnection = WifiConnection()
                        if (wifiConnection.ConnectDevice(impresora.IP, impresora.puerto)) {
                            var printOptions = PrintOptions(
                                context,
                                wifiConnection.mmOutputStream,
                                wifiConnection.mmInputStream
                            )
                            printOptions.ImprimirPedidoPrecuenta(docVenta)
                            printOptions.ImpresionAdicionalPedido(docVenta)

                            //    printOptions.ImprimirFactura(docVenta)
                            printOptions.CerrarConexion()
                            wifiConnection.CloseConnection()

                            r = "R"
                        } else {
                            r = "ER"
                        }
                    }
                } else {

                }

            }
            "USB" -> {
                var impresora = dbHelper.ObtenerImpresoraRed()
                if (!impresora.IP.equals("") && impresora.puerto != 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        var printOptions = PrintOptions(context)

                        val usbController = UsbController(context)
                        val usbDevice =
                            usbController.searchDevice(impresora.IP.toInt(), impresora.puerto)

                        if (usbDevice != null) {
                            val listPrint = printOptions.ImprimirPedidoPrecuentaPrinterJob(docVenta)
                            listPrint.addAll(printOptions.ImpresionAdicionalPedidoPrintJob(docVenta))
                            usbController.imprimeEnDispositivo(ArrayList(listPrint), usbDevice)

                        }
                    }


                } else {

                }
            }
            "N" -> {
                r = "N"
            }
            "Ninguno" -> {
                r = "N"
            }
            else -> {
                r = "N"
            }
        }



        return ""
    }

    fun TextoPagosVenta(pagos: List<mPagosEnVenta>): String {
        var texto = ""

        pagos.forEach {

            texto = texto + completarEspaciosI(15, it.tipoPago) +
                    completarEspaciosI(
                        12,
                        Constantes.DivisaPorDefecto.SimboloDivisa + " " + it.cantidadPagada.fortMoneda
                    ) + "\n"
        }

        return texto
    }

    fun ImpresionDocVenta(
        productos: List<ProductoEnVenta>,
        cabeceraVenta: mCabeceraVenta,
        tipo: Int
    ): String {
        var r = ""
        val docVenta = DocVenta()
        var textoCuerpo = ""
        val constructorFactura = ConstructorFactura()

        when (tipo) {

            Constantes.TipoDocumentoPago.FACTURA -> {
                docVenta.pieDoc = Constantes.PieImpresion.pieFactura
                docVenta.docReceptor = cabeceraVenta.cliente.numeroRuc
            }
            Constantes.TipoDocumentoPago.BOLETA -> {
                docVenta.pieDoc = Constantes.PieImpresion.pieBoleta
                docVenta.docReceptor = cabeceraVenta.cliente.numeroRuc
            }
            Constantes.TipoDocumentoPago.NOTAVENTA -> docVenta.pieDoc =
                Constantes.PieImpresion.pieNotaVenta

        }
        docVenta.importeLetra = cabeceraVenta.totalPagado.fortMoneda

        if (cabeceraVenta.identificador.length > 0) {
            docVenta.identificador =
                "Identificador: \n" + cabeceraVenta.identificador.replaceSpecialChar
        } else {
            docVenta.identificador = ""

        }

        if (cabeceraVenta.observacion.trim().isNotEmpty()) {

            docVenta.observacion = "Observacion : " + cabeceraVenta.observacion.trim()

        }

        docVenta.tipoDoc = ObtenerNombreTipoDoc(tipo).replaceSpecialChar
        if (cabeceraVenta.numSerie != null) {
            if (cabeceraVenta.numSerie.length > 0) {
                var s = ""
                /*if(docVenta.tipoDoc.contains("Factura")){
                    s=docVenta.tipoDoc.substring(0,16)+"\n"+docVenta.tipoDoc.substring(17)
                }else if(docVenta.tipoDoc.contains("Boleta")){

                    s=docVenta.tipoDoc.substring(0,16)+"\n"+docVenta.tipoDoc.substring(16)
                }*/
                docVenta.mensajeRepre =
                    "Representación impresa de\n${docVenta.tipoDoc}".toUpperCase()

            }
        }
        if (cabeceraVenta.vendedor.primerNombre.length > 0) {
            docVenta.nombreVendedor =
                "Vendedor:\n" + cabeceraVenta.vendedor.primerNombre.replaceSpecialChar
        } else {
            docVenta.nombreVendedor = ""
        }
        docVenta.cabecerasTicket = "Descripcion" + "\n   " +
                completarEspacios(8, "Cant") +
                completarEspacios(16, "P.U") +
                completarEspacios(5, "P.T")


        if (cabeceraVenta.emisor.length > 0) {
            docVenta.nombreEmisor = cabeceraVenta.emisor.replaceSpecialChar
        }
        if (ObtenerNombreTienda(Constantes.Tienda.idTienda).length > 0) {
            docVenta.nombreTienda = ObtenerNombreTienda(Constantes.Tienda.idTienda)
        }
        if (cabeceraVenta.nombreCiudad != null) {
            if (cabeceraVenta.nombreCiudad.length > 0) {
                docVenta.direccionEmisor = cabeceraVenta.nombreCiudad
            }
        }
        if (cabeceraVenta.rucEmisor != null) {
            if (cabeceraVenta.rucEmisor.length > 0) {
                docVenta.rucEmisor = "RUC " + cabeceraVenta.rucEmisor

            }
        }
        if (cabeceraVenta.enlaceNf != null) {
            if (cabeceraVenta.enlaceNf.length > 0) {
                docVenta.enlaceNf = cabeceraVenta.enlaceNf
            }
        }
        if (cabeceraVenta.codigoHash != null) {
            if (cabeceraVenta.codigoHash.length > 0) {
                docVenta.codigoHash = cabeceraVenta.codigoHash
            }
        }


        if (cabeceraVenta.numCorrelativo != null) {
            if (cabeceraVenta.numCorrelativo > 0) {
                docVenta.correlativo =
                    CompletarCaracteresIzquierda("0", 8, cabeceraVenta.numCorrelativo.toString())
            }
        }
        if (cabeceraVenta.cliente.getcDireccion() != null) {
            if (cabeceraVenta.cliente.getcDireccion().length > 0) {
                docVenta.direccion = cabeceraVenta.cliente.getcDireccion()
            }
        }

        if (cabeceraVenta.cliente.razonSocial.length > 0) {
            if (docVenta.docReceptor.length > 0) {
                docVenta.nombreReceptor = "Cliente:\n" + docVenta.docReceptor + "\n" +
                        cabeceraVenta.cliente.razonSocial.replaceSpecialChar + "\n"
            } else {
                docVenta.nombreReceptor =
                    "Cliente:\n" + cabeceraVenta.cliente.razonSocial.replaceSpecialChar + "\n"
            }
        } else {
            docVenta.nombreReceptor = ""
        }
        /*
        if(cabeceraVenta.getcValorTotal()!=null) {
            if (cabeceraVenta.getcValorTotal().length > 0) {
                docVenta.importeLetra = cabeceraVenta.getcValorTotal()
            }
        }*/
        if (cabeceraVenta.fechaVenta != null) {
            docVenta.fechaEmision = "Fecha emision \n${cabeceraVenta.fechaVenta}"
        }
        if (cabeceraVenta.numSerie != null)
            docVenta.serie = cabeceraVenta.numSerie

        if (cabeceraVenta.numCorrelativo != null)
            docVenta.correlativo = cabeceraVenta.numCorrelativo.toString()
        docVenta.productos = textoCuerpo
        /*
        if(cabeceraVenta.getcValorTotal()!=null){
            if(cabeceraVenta.getcValorTotal().length>0){
                docVenta.importeLetra=cabeceraVenta.getcValorTotal()
            }
        }*/
        when (getMedioImpresion()) {
            "PDF" -> {
            }
            "Bluetooth" -> {
                try {
                    var address = dbHelper.AddressBT()
                    if (!address.equals("N")) {
                        textoCuerpo =
                            constructorFactura.generarListadoItems53mm(productos).replaceSpecialChar
                        docVenta.productos = textoCuerpo
                        if (cabeceraVenta.numSerie != null) {
                            if (cabeceraVenta.numSerie.length > 0) {
                                docVenta.totalGravada = completarEspaciosI(
                                    18, "GRAVADA  " +
                                            "${Constantes.SimboloMoneda.moneda}"
                                ) +
                                        completarEspaciosI(
                                            12,
                                            cabeceraVenta.totalGravado.fortMoneda
                                        )
                                docVenta.totalIgv = completarEspaciosI(
                                    18,
                                    "IGV  ${Constantes.SimboloMoneda.moneda}"
                                ) +
                                        completarEspaciosI(12, cabeceraVenta.totalIgv.fortMoneda)
                            }
                        }
                        if (cabeceraVenta.descuentoGlobal.toInt() > 0) {
                            docVenta.totalDescuento = completarEspaciosI(
                                18, "DESCUENTO  " +
                                        "${Constantes.SimboloMoneda.moneda}"
                            ) +
                                    completarEspaciosI(
                                        12,
                                        "${cabeceraVenta.descuentoGlobal.fortMoneda}"
                                    )
                        }
                        docVenta.total = completarEspaciosI(
                            18, "TOTAL  " +
                                    "${Constantes.SimboloMoneda.moneda}"
                        ) + completarEspaciosI(
                            12,
                            "${cabeceraVenta.totalPagado.fortMoneda}"
                        )
                        docVenta.totalCambio = completarEspaciosI(
                            18, "VUELTO  " +
                                    "${Constantes.SimboloMoneda.moneda}"
                        ) + completarEspaciosI(
                            12,
                            cabeceraVenta.totalCambio.multiply(-1.bg).fortMoneda
                        )
                         GlobalScope.launch  {
                            btConnection.selectDevice(address)
                            btConnection.openBT()
                            var printOptions = PrintOptions(
                                context, btConnection.outputStream,
                                btConnection.mmInputStream
                            )
                            printOptions.ImprimirFactura(docVenta)
                            //    printOptions.PrinterBitmap(bitmap)
                        }
                        r = "B"
                    } else {
                        r = "N"
                        var n = 0.bg
                        n.fortMoneda
                    }
                } catch (e: Exception) {
                    e.toString()
                }
            }
            "Impresora en Red" -> {
                textoCuerpo =
                    constructorFactura.generarListadoItems53mm(productos).replaceSpecialChar
                var impresionRed = cImpresionRed()
                var impresora = dbHelper.ObtenerImpresoraRed()
                if (cabeceraVenta.numSerie != null) {
                    if (cabeceraVenta.numSerie.length > 0) {
                        docVenta.totalGravada = completarEspaciosI(
                            18, "GRAVADA  " +
                                    "${Constantes.SimboloMoneda.moneda}"
                        ) +
                                completarEspaciosI(12, cabeceraVenta.totalGravado.fortMoneda)
                        docVenta.totalIgv =
                            completarEspaciosI(18, "IGV  ${Constantes.SimboloMoneda.moneda}") +
                                    completarEspaciosI(12, cabeceraVenta.totalIgv.fortMoneda)
                    }
                }
                if (cabeceraVenta.descuentoGlobal.toInt() > 0) {
                    docVenta.totalDescuento = completarEspaciosI(
                        18, "DESCUENTO  " +
                                "${Constantes.SimboloMoneda.moneda}"
                    ) +
                            completarEspaciosI(12, "${cabeceraVenta.descuentoGlobal.fortMoneda}")
                }
                docVenta.total = completarEspaciosI(
                    18, "TOTAL  " +
                            "${Constantes.SimboloMoneda.moneda}"
                ) + completarEspaciosI(
                    12,
                    "${cabeceraVenta.totalPagado.fortMoneda}"
                )
                docVenta.totalCambio = completarEspaciosI(
                    18, "VUELTO  " +
                            "${Constantes.SimboloMoneda.moneda}"
                ) + completarEspaciosI(12, cabeceraVenta.totalCambio.fortMoneda)
                docVenta.productos = textoCuerpo
                if (!impresora.IP.equals("") && impresora.puerto != 0) {
                     GlobalScope.launch  {
                        var wifiConnection = WifiConnection()
                        if (wifiConnection.ConnectDevice(impresora.IP, impresora.puerto)) {
                            var printOptions = PrintOptions(
                                context,
                                wifiConnection.mmOutputStream,
                                wifiConnection.mmInputStream
                            )
                            printOptions.ImprimirFactura(docVenta)
                            printOptions.CerrarConexion()
                            wifiConnection.CloseConnection()
                            var c = BdConnectionSql.getSinglentonInstance()
                            c.Demo()
                            r = "R"
                        } else {
                            r = "ER"
                        }
                    }
                } else {

                }
            }
            "USB" -> {

                Toast.makeText(context, "IMPRESORA USB", Toast.LENGTH_SHORT).show()
                textoCuerpo =
                    constructorFactura.generarListadoItems53mm(productos).replaceSpecialChar
                var impresora = dbHelper.ObtenerImpresoraRed()
                if (cabeceraVenta.numSerie != null) {
                    if (cabeceraVenta.numSerie.length > 0) {
                        docVenta.totalGravada = completarEspaciosI(
                            18, "GRAVADA  " +
                                    "${Constantes.SimboloMoneda.moneda}"
                        ) +
                                completarEspaciosI(12, cabeceraVenta.totalGravado.fortMoneda)
                        docVenta.totalIgv =
                            completarEspaciosI(18, "IGV  ${Constantes.SimboloMoneda.moneda}") +
                                    completarEspaciosI(12, cabeceraVenta.totalIgv.fortMoneda)
                    }
                }
                if (cabeceraVenta.descuentoGlobal.toInt() > 0) {
                    docVenta.totalDescuento = completarEspaciosI(
                        18, "DESCUENTO  " +
                                "${Constantes.SimboloMoneda.moneda}"
                    ) +
                            completarEspaciosI(12, "${cabeceraVenta.descuentoGlobal.fortMoneda}")
                }
                docVenta.total = completarEspaciosI(
                    18, "TOTAL  " +
                            "${Constantes.SimboloMoneda.moneda}"
                ) + completarEspaciosI(
                    12,
                    "${cabeceraVenta.totalPagado.fortMoneda}"
                )
                docVenta.totalCambio = completarEspaciosI(
                    18, "VUELTO  " +
                            "${Constantes.SimboloMoneda.moneda}"
                ) + completarEspaciosI(12, cabeceraVenta.totalCambio.fortMoneda)
                docVenta.productos = textoCuerpo
                if (!impresora.IP.equals("") && impresora.puerto != 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        var usbController = UsbController(context)
                        var usbDevice =
                            usbController.searchDevice(impresora.IP.toInt(), impresora.puerto)
                        var printOptions = PrintOptions(context)
                        var listPrint = printOptions.GenerarPrintJobs(docVenta)

                        if (usbDevice != null) {

                            usbController.imprimeEnDispositivo(ArrayList(listPrint), usbDevice!!)

                        } else {
                            Toast.makeText(
                                context,
                                "NO SE UBICO IMPRESORA REGISTRADA IMPRESORA USB",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }

                } else {
                    Toast.makeText(context, "No ubica IMPRESORA USB", Toast.LENGTH_SHORT).show()
                }

            }
            "N" -> {
                r = "N"
            }
            "Ninguno" -> {
                r = "N"
            }
            else -> {
                r = "N"
            }
        }


        return r
    }


    private fun getMedioImpresion(): String {

        return dbHelper.MetodoImpresioSeleccionada()
    }


}

private fun String.removerTilde(): String {
    return this.replace("á","a").replace("é","e").replace("í","i").replace("ó","o").replace("ú","u")
}

val BigDecimal.AplicarIgv: BigDecimal
    get() {
        return this.multiply(Constantes.IGV.valorIgv)
    }


val String.replaceSpecialChar: String
    get() {

        return this.replace("ñ", "n").replace("á", "a").replace("é", "e").replace("í", "i")
            .replace("ó", "o").replace("ú", "u").replace("Ú", "U").replace("Á", "A")
            .replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ñ", "N")
            .replace("ë", "e").replace("ü", "u").replace("Ü", "U")

    }


fun CompletarCaracteresIzquierda(caracter: String, clen: Int, cadOriginal: String): String {

    var c = ""

    var len = cadOriginal.length
    var count = len - 1
    while (len < clen) {
        c = caracter + cadOriginal
        len++
    }
    return c
}

fun CompletarCaracteresDerecha(caracter: String, clen: Int, cadOriginal: String): String {

    var c = ""

    for (len in cadOriginal.length until clen) {
        val sb = StringBuilder()
        sb.append(cadOriginal)
        sb.append(caracter)
        c = sb.toString()
    }
    return c
}

fun ObtenerNombreTipoDoc(id: Int): String {
    var nombre = ""
    Constantes.TiposDocPago.listaTipoDocPago.forEach {
        if (it.idDoc.equals(id)) {
            nombre = it.cDescripcion
        }
    }
    return nombre
}

////////////////////////***//////////////////////////////////////
///////////////////////*****/////////////////////////////////////
//////////////////////**//**/////////////////////////////////////
/////////////////////**///**/////////////////////////////////////
////////////////////**////**/////////////////////////////////////
///////////////////**/////**///**////////////////////////////////
//////////////////**//////**////***//////////////////////////////
/////////////////*******************/////////////////////////////
////////////////********************/////////////////////////////
///////////////**/////////**/////**//////////////////////////////
//////////////**//////////**///**////////////////////////////////
/////////////**///////////**/////////////////////////////////////
////////////**////////////**/////////////////////////////////////