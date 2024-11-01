package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.util.Base64
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.IGV.valorIgv
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.TokenFactura.*
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.bg
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultadoComprobante
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDocVenta
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit

//Controlador para envio de facturacion electronica con FACTURA ACTIVA .

class FacturaActivaController{

    //ObTENER PRIMER TOKEN PARA AUTORIZAR ELLA GENERACION DE FACTURACION
    fun ObtenerToken():JSONObject?{

        var jsonRpta:JSONObject?=JSONObject()
        var envio = JSONObject()
        envio.put("grant_type","client_credentials")
        var pre="/oauth2/token"
        var url1 = "$rutaApi$pre"
        var tok = "$tokenEnvio:$tokenP2".replace("\n","").replace("\r","")
        var token1_64 = Base64.encodeToString(tok.toByteArray(), Base64.DEFAULT).replace("\n","")
        val client = OkHttpClient().newBuilder().connectTimeout(15,TimeUnit.SECONDS).build()
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, envio.toString().toByteArray())
        val request = Request.Builder()
                .url(url1)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic $token1_64")
                .addHeader("User-Agent", "Client Application")
                .addHeader("cache-control", "no-cache")
                .build()
        val response = client.newCall(request).execute()
        var linea = ""
        linea = response.body()!!.string()
        if ((linea) != null) {
            val pasearRptaJsonToken = JSONParser()
            jsonRpta = pasearRptaJsonToken.parse(linea) as org.json.simple.JSONObject
        }else{
            jsonRpta=null
        }
        return jsonRpta
    }

    //Emision de comprobrante -- BOLETA , FACTURA ,
    fun EmitirComprobanteElectronicoV2(docVenta:mDocVenta):ResultadoComprobante{

        var r=ResultadoComprobante()
        try {
            val json_rspta:JSONObject?=ObtenerToken()
            if (json_rspta != null) {
                // val pasearRptaJsonToken = JSONParser()
                // val json_rspta = pasearRptaJsonToken.parse(linea) as org.json.simple.JSONObject
                if (json_rspta.get("errors") != null) {
                    r.enviado=false
                    r.recibido=false
                    r.codeSuccess=0
                    r.estadoRespuesta="ERROR"
                    r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Debe comunicarse con FACTURA ACTIVA."
                } else {
                    val jsonDocElectronico = org.json.simple.JSONObject()
                    var accesToken = ""
                    accesToken = json_rspta.get("access_token").toString()
                    val url2 = rutaApi + "/emission/documents"
                    var jsonDocumento = org.json.simple.JSONObject()
                    jsonDocumento.put("serie", docVenta.cabeceraVenta.numSerie)
                    jsonDocumento.put("correlativo", docVenta.cabeceraVenta.numCorrelativo)
                    jsonDocumento.put("nombreEmisor", Constantes.Empresa.Razon_Social)
                    jsonDocumento.put("nombreComercialEmisor", Constantes.Empresa.Razon_Social)
                    jsonDocumento.put("tipoDocEmisor", "6")
                    jsonDocumento.put("direccionOrigen", Constantes.Empresa.cDireccion)
                    jsonDocumento.put("numDocEmisor", Constantes.Empresa.NumRuc)
                    jsonDocumento.put("tipoDocReceptor", if(docVenta.cabeceraVenta.cliente.idTipoDocSunat.toString()=="8"){"-"}
                    else{docVenta.cabeceraVenta.cliente.idTipoDocSunat.toString()})
                    jsonDocumento.put("nombreReceptor", docVenta.cabeceraVenta.cliente.razonSocial)
                    jsonDocumento.put("numDocReceptor", docVenta.cabeceraVenta.cliente.numeroRuc)
                    jsonDocumento.put("tipoMoneda", "PEN")
                    jsonDocumento.put("mntNeto", docVenta.cabeceraVenta.totalGravado.setScale(2, RoundingMode.HALF_UP))
                    jsonDocumento.put("mntTotalIgv", docVenta.cabeceraVenta.totalIgv.setScale(2, RoundingMode.HALF_UP))
                    jsonDocumento.put("mntTotal", docVenta.cabeceraVenta.totalPagado.setScale(2, RoundingMode.HALF_UP))
                  //  jsonDocumento.put("mntTotalGrat", 0.bg)
                    jsonDocumento.put("tipoFormatoRepresentacionImpresa", "GENERAL")
                    var jsonImpuesto = org.json.simple.JSONObject()
                    jsonImpuesto.put("codImpuesto", "1000")
                    jsonImpuesto.put("montoImpuesto", docVenta.cabeceraVenta.totalIgv.setScale(2, RoundingMode.HALF_UP))
                    jsonImpuesto.put("tasaImpuesto", BigDecimal(0.18).setScale(2, RoundingMode.HALF_EVEN))
                    val jsonArrayDetalle = JSONArray()
                    docVenta.productoEnVenta.forEach {
                        val jsonDetalle = org.json.simple.JSONObject()
                        jsonDetalle.put("codAfectacionIgv","10")
                        jsonDetalle.put("cantidadItem", it.cantidad.toBigDecimal().setScale(3, RoundingMode.HALF_UP))
                        jsonDetalle.put("unidadMedidaItem", it.codUnidSunat.trim())
                        jsonDetalle.put("codItem", it.codigoProducto.trim())
                        jsonDetalle.put("nombreItem", it.productName.trim())
                        jsonDetalle.put("montoIgv",it.montoIgv.setScale(2, RoundingMode.HALF_UP))
                        jsonDetalle.put("precioItem", it.precioOriginal.setScale(2, RoundingMode.HALF_UP))
                        jsonDetalle.put("precioItemSinIgv", it.valorUnitario.setScale(2, RoundingMode.HALF_UP))
                        // jsonDetalle.put("precioItemSinIgv",3.bg)
                        jsonDetalle.put("montoItem", it.precioNeto.setScale(2, RoundingMode.HALF_UP))
                        if (it.montoDescuento.toFloat() > 0) {
                          jsonDetalle.put("descuentoMonto", it.montoDescuento.setScale(2, RoundingMode.HALF_UP))
                        }
                        jsonDetalle.put("tasaIgv", valorIgv.divide(100.bg,2, RoundingMode.HALF_UP))
                        jsonDetalle.put("idOperacion", docVenta.cabeceraVenta.idVenta.toString())
                        jsonArrayDetalle.add(jsonDetalle)
                    }
                    var jsonDescuento = org.json.simple.JSONObject()
                    jsonDescuento.put("mntDescuentoGlobal", 0.bg.setScale(2, RoundingMode.HALF_UP))
                    jsonDescuento.put("mntTotalDescuentos", docVenta.cabeceraVenta.descuentoGlobal.setScale(2, RoundingMode.HALF_UP))
                    val jsonArrayImpuesto = JSONArray()
                    jsonArrayImpuesto.add(jsonImpuesto)
                    jsonDocElectronico.put("correoReceptor", docVenta.cabeceraVenta.cliente.getcEmail())
                    jsonDocElectronico.put("tipoDocumento", docVenta.cabeceraVenta.codDocPago)
                    if(docVenta.esNota) {
                        var jsonArrayReferencia = JSONArray()
                        var jsonReferencia = org.json.simple.JSONObject()
                        jsonDocumento.put("sustento", "Error en los datos ingresados")
                        jsonDocumento.put("tipoMotivoNotaModificatoria","01")

                        jsonReferencia.put("tipoDocumentoRef", docVenta.tipoDocReferenciaChar)
                        jsonReferencia.put("serieRef", docVenta.serieReferencia)
                        jsonReferencia.put("correlativoRef",docVenta.correlativoReferencia)
                        jsonReferencia.put("fechaEmisionRef", docVenta.fechaReferencia)
                        jsonArrayReferencia.add(jsonReferencia)
                        jsonDocElectronico.put("referencia", jsonArrayReferencia)
                    }
                    jsonDocElectronico.put("fechaEmision", docVenta.cabeceraVenta.fechaV2)
                    jsonDocElectronico.put("idTransaccion", docVenta.cabeceraVenta.idVenta.toString())
                    jsonDocElectronico.put("documento", jsonDocumento)
                    jsonDocElectronico.put("descuento", jsonDescuento)
                    jsonDocElectronico.put("detalle", jsonArrayDetalle)
                    jsonDocElectronico.put("impuesto", jsonArrayImpuesto)
                    var clientResult=OkHttpClient().newBuilder().connectTimeout(15,TimeUnit.SECONDS).build()
                    val mediaT = MediaType.parse("application/json")
                    val body = RequestBody.create(mediaT, jsonDocElectronico.toString().toByteArray())
                    val request2 = Request.Builder()
                            .url(url2).post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("User-Agent", "Client Application")
                            .addHeader("Authorization","Bearer $accesToken")
                            .build()


                    val response2 = clientResult.newCall(request2).execute()
                    var linea = response2.body()!!.string()
                    r.enviado=true
                    r.recibido=false
                    r.codeSuccess=0
                    r.estadoRespuesta="ERROR"
                    r.mensaje="Se envio el documento pero no se recibio una respuesta"
                    if (linea != null) {
                        val parsearRpta = JSONParser()
                        val json_rspta = parsearRpta.parse(linea) as org.json.simple.JSONObject
                        if(json_rspta.get("data")!=null){
                            var json_data=  json_rspta.get("data") as JSONObject
                            var code="200"
                            var message="OK"
                            r.estadoRespuesta="OK"
                            r.codeSuccess=200
                            r.mensaje=""
                            r.recibido=true
                        }else if(json_rspta.get("errors")!=null){
                            r.recibido=true
                            var json_error =json_rspta.get("errors") as JSONArray
                            if(json_error.get(0)!=null){
                                var error1=json_error.get(0) as JSONObject
                                var code=error1.get("status")
                                code.toString()
                                var detail=error1.get("detail")
                                detail.toString()
                                r.codeSuccess=code.toString().toInt()
                                r.estadoRespuesta="Error"
                                r.mensaje=detail.toString()
                            }
                        }
                    }


                }
            }
            else{
                r.enviado=false
                r.recibido=false
                r.codeSuccess=0
                r.estadoRespuesta="ERROR"
                r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Verifique su conexión a internet ."
            }
        } catch (ex1: UnsupportedEncodingException) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.message)
            ex1.toString()
            r.recibido=false
            r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Verifique su conexión a internet ."
            r.estadoRespuesta="ERROR"
            //rptaSunat=null
        } catch (ex2: IOException) {
            System.err.println("Error IOException: " + ex2.message)
            ex2.toString()
            r.recibido=false
            r.estadoRespuesta="ERROR"
            r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Verifique su conexión a internet ."
            //rptaSunat=null
        } catch (ex3: Exception) {
            System.err.println("Exepction: " + ex3.message)
            ex3.toString()
            r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Verifique su conexión a internet ."
            r.recibido=false
            r.estadoRespuesta="ERROR"
            // rptaSunat=null
        }
        return r

    }
    //Emision de comprobrante -- BOLETA Y FACTURA
    fun EmitirComprobanteElectronico(docVenta:mDocVenta):ResultadoComprobante{

        var r=ResultadoComprobante()
        try {
        /*  var envio = JSONObject()
            envio.put("grant_type","client_credentials")
            var pre="/oauth2/token"
            var url1 = "$rutaApi$pre"
            var tok = "$tokenEnvio:$tokenP2".replace("\n","").replace("\r","")
            var token1_64 = Base64.encodeToString(tok.toByteArray(), Base64.DEFAULT).replace("\n","")
            val client = OkHttpClient().newBuilder().connectTimeout(15,TimeUnit.SECONDS).build()
            val mediaType = MediaType.parse("application/json")
            val body = RequestBody.create(mediaType, envio.toString().toByteArray())
            val request = Request.Builder()
                    .url(url1)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Basic $token1_64")
                    .addHeader("User-Agent", "Client Application")
                    .addHeader("cache-control", "no-cache")
                    .build()
            val response = client.newCall(request).execute()
            var linea = ""
            linea = response.body()!!.string()*/
            val json_rspta:JSONObject?=ObtenerToken()
            if (json_rspta != null) {
               // val pasearRptaJsonToken = JSONParser()
               // val json_rspta = pasearRptaJsonToken.parse(linea) as org.json.simple.JSONObject
                if (json_rspta.get("errors") != null) {
                    r.enviado=false
                    r.recibido=false
                    r.codeSuccess=0
                    r.estadoRespuesta="ERROR"
                    r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Debe comunicarse con FACTURA ACTIVA."
                } else {
                    val jsonDocElectronico = org.json.simple.JSONObject()
                    var accesToken = ""
                    accesToken = json_rspta.get("access_token").toString()
                    val url2 = rutaApi + "/emission/documents"
                    var jsonDocumento = org.json.simple.JSONObject()
                    jsonDocumento.put("serie", docVenta.cabeceraVenta.numSerie)
                    jsonDocumento.put("correlativo", docVenta.cabeceraVenta.numCorrelativo)
                    jsonDocumento.put("nombreEmisor", Constantes.Empresa.Razon_Social)
                    jsonDocumento.put("nombreComercialEmisor", Constantes.Empresa.Razon_Social)
                    jsonDocumento.put("tipoDocEmisor", "6")
                    jsonDocumento.put("direccionOrigen", Constantes.Empresa.cDireccion)
                    jsonDocumento.put("numDocEmisor", Constantes.Empresa.NumRuc)
                    jsonDocumento.put("tipoDocReceptor", if(docVenta.cabeceraVenta.cliente.idTipoDocSunat.toString()=="8"){"-"}
                    else{docVenta.cabeceraVenta.cliente.idTipoDocSunat.toString()})
                    jsonDocumento.put("nombreReceptor", docVenta.cabeceraVenta.cliente.razonSocial)
                    jsonDocumento.put("numDocReceptor", docVenta.cabeceraVenta.cliente.numeroRuc)
                    jsonDocumento.put("tipoMoneda", "PEN")
                    jsonDocumento.put("mntNeto", docVenta.cabeceraVenta.totalGravado.setScale(2, RoundingMode.HALF_UP))
                    jsonDocumento.put("mntTotalIgv", docVenta.cabeceraVenta.totalIgv.setScale(2, RoundingMode.HALF_UP))
                    jsonDocumento.put("mntTotal", docVenta.cabeceraVenta.totalPagado.setScale(2, RoundingMode.HALF_UP))
                    jsonDocumento.put("mntTotalGrat", 0.bg)
                    jsonDocumento.put("tipoFormatoRepresentacionImpresa", "GENERAL")
                    var jsonImpuesto = org.json.simple.JSONObject()
                    jsonImpuesto.put("codImpuesto", "1000")
                    jsonImpuesto.put("montoImpuesto", docVenta.cabeceraVenta.totalIgv.setScale(2, RoundingMode.HALF_UP))
                    jsonImpuesto.put("tasaImpuesto", BigDecimal(0.18).setScale(2, RoundingMode.HALF_EVEN))
                    val jsonArrayDetalle = JSONArray()
                    docVenta.productoEnVenta.forEach {
                        val jsonDetalle = org.json.simple.JSONObject()
                        jsonDetalle.put("codAfectacionIgv","10")
                        jsonDetalle.put("cantidadItem", it.cantidad.toBigDecimal().setScale(3, RoundingMode.HALF_UP))
                        jsonDetalle.put("unidadMedidaItem", it.codUnidSunat.trim())
                        jsonDetalle.put("codItem", it.codigoProducto.trim())
                        jsonDetalle.put("nombreItem", it.productName.trim())
                        jsonDetalle.put("montoIgv",it.montoIgv.setScale(2,RoundingMode.HALF_UP))
                        jsonDetalle.put("precioItem", it.precioOriginal.multiply(BigDecimal(1.18)).setScale(2, RoundingMode.HALF_UP))
                        jsonDetalle.put("precioItemSinIgv", it.precioOriginal.setScale(2, RoundingMode.HALF_UP))
                       // jsonDetalle.put("precioItemSinIgv",3.bg)
                        jsonDetalle.put("montoItem", it.precioVentaFinal.divide(BigDecimal(1.18),2, RoundingMode.HALF_UP))
                        if (it.montoDescuento.toFloat() > 0) {
                            jsonDetalle.put("descuentoMonto", it.montoDescuento.setScale(2, RoundingMode.HALF_UP))
                        }
                        jsonDetalle.put("tasaIgv", valorIgv.divide(100.bg,2, RoundingMode.HALF_UP))
                        jsonDetalle.put("idOperacion", docVenta.cabeceraVenta.idVenta.toString())
                        jsonArrayDetalle.add(jsonDetalle)
                    }
                    var jsonDescuento = org.json.simple.JSONObject()
                    jsonDescuento.put("mntDescuentoGlobal", 0.bg.setScale(2, RoundingMode.HALF_UP))
                    jsonDescuento.put("mntTotalDescuentos", docVenta.cabeceraVenta.descuentoGlobal)
                    val jsonArrayImpuesto = JSONArray()
                    jsonArrayImpuesto.add(jsonImpuesto)
                    jsonDocElectronico.put("correoReceptor", docVenta.cabeceraVenta.cliente.getcEmail())
                    jsonDocElectronico.put("tipoDocumento", docVenta.cabeceraVenta.codDocPago)
                    if(docVenta.esNota) {
                        var jsonArrayReferencia = JSONArray()
                        var jsonReferencia = org.json.simple.JSONObject()
                        jsonDocumento.put("sustento", "Error en los datos ingresados")
                        jsonDocumento.put("tipoMotivoNotaModificatoria","01")

                        jsonReferencia.put("tipoDocumentoRef", docVenta.tipoDocReferenciaChar)
                        jsonReferencia.put("serieRef", docVenta.serieReferencia)
                        jsonReferencia.put("correlativoRef",docVenta.correlativoReferencia)
                        jsonReferencia.put("fechaEmisionRef", docVenta.fechaReferencia)
                        jsonArrayReferencia.add(jsonReferencia)
                        jsonDocElectronico.put("referencia", jsonArrayReferencia)
                    }
                    jsonDocElectronico.put("fechaEmision", docVenta.cabeceraVenta.fechaV2)
                    jsonDocElectronico.put("idTransaccion", docVenta.cabeceraVenta.idVenta.toString())
                    jsonDocElectronico.put("documento", jsonDocumento)
                    jsonDocElectronico.put("descuento", jsonDescuento)
                    jsonDocElectronico.put("detalle", jsonArrayDetalle)
                    jsonDocElectronico.put("impuesto", jsonArrayImpuesto)
                    var clientResult=OkHttpClient().newBuilder().connectTimeout(15,TimeUnit.SECONDS).build()
                    val mediaT = MediaType.parse("application/json")
                    val body = RequestBody.create(mediaT, jsonDocElectronico.toString().toByteArray())
                    val request2 = Request.Builder()
                            .url(url2).post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("User-Agent", "Client Application")
                            .addHeader("Authorization","Bearer $accesToken")
                            .build()


                    val response2 = clientResult.newCall(request2).execute()
                    var linea = response2.body()!!.string()
                    r.enviado=true
                    r.recibido=false
                    r.codeSuccess=0
                    r.estadoRespuesta="ERROR"
                    r.mensaje="Se envio el documento pero no se recibio una respuesta"
                    if (linea != null) {
                        val parsearRpta = JSONParser()
                        val json_rspta = parsearRpta.parse(linea) as org.json.simple.JSONObject
                            if(json_rspta.get("data")!=null){
                                var json_data=  json_rspta.get("data") as JSONObject
                                var code="200"
                                var message="OK"
                                r.estadoRespuesta="OK"
                                r.codeSuccess=200
                                r.mensaje=""
                                r.recibido=true
                            }else if(json_rspta.get("errors")!=null){
                                r.recibido=true
                                 var json_error =json_rspta.get("errors") as JSONArray
                                  if(json_error.get(0)!=null){
                                      var error1=json_error.get(0) as JSONObject
                                      var code=error1.get("status")
                                      code.toString()
                                      var detail=error1.get("detail")
                                      detail.toString()
                                      r.codeSuccess=code.toString().toInt()
                                      r.estadoRespuesta="Error"
                                      r.mensaje=detail.toString()
                                  }
                            }
                    }

                }
            }
            else{
                r.enviado=false
                r.recibido=false
                r.codeSuccess=0
                r.estadoRespuesta="ERROR"
                r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Verifique su conexión a internet ."
            }
        } catch (ex1: UnsupportedEncodingException) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.message)
            ex1.toString()
            r.recibido=false
            r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Verifique su conexión a internet ."
            r.estadoRespuesta="ERROR"
            //rptaSunat=null
        } catch (ex2: IOException) {
            System.err.println("Error IOException: " + ex2.message)
            ex2.toString()
            r.recibido=false
            r.estadoRespuesta="ERROR"
            r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Verifique su conexión a internet ."
            //rptaSunat=null
        } catch (ex3: Exception) {
            System.err.println("Exepction: " + ex3.message)
            ex3.toString()
            r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Verifique su conexión a internet ."
             r.recibido=false
            r.estadoRespuesta="ERROR"
           // rptaSunat=null
        }
        return r

    }

    //Emitir baja a documento -- DOCUMENTO BAJA CPE
    fun ComunicacionBajaDocumento(docVenta: mDocVenta,motivo:String):ResultadoComprobante{
        // /emission/summaries
        var jsonToken:JSONObject?=ObtenerToken()
        var r=ResultadoComprobante()
        if(jsonToken!=null){
            var accesToken = ""
            accesToken = jsonToken.get("access_token").toString()
            if (jsonToken.get("errors") != null) {
                r.enviado=false
                r.recibido=false
                r.codeSuccess=0
                r.estadoRespuesta="ERROR"
                r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Debe comunicarse con FACTURA ACTIVA."
            }else{
                val jsonResumen=JSONObject()
                val jsonDetalle=JSONObject()
                val jsonDocumento=JSONObject()
                jsonDocumento.put("tipoResumen","RA")
                jsonDocumento.put("fechaGeneracion",docVenta.cabeceraVenta.fechaEmision)
                jsonDocumento.put("idTransaccion",docVenta.idResult.toString())
                //// RESUMEN
                  jsonResumen.put("id",docVenta.idResult)
                  jsonResumen.put("tipoDocEmisor","6")
                  jsonResumen.put("numDocEmisor",Constantes.Empresa.NumRuc)
                  jsonResumen.put("nombreEmisor",Constantes.Empresa.Razon_Social)
                  jsonResumen.put("fechaReferente",docVenta.fechaReferencia)
                  jsonResumen.put("tipoFormatoRepresentacionImpresa","GENERAL")
                jsonDocumento.put("resumen",jsonResumen)
                //// DETALLE
                val jsonArrayDetalle=JSONArray()
                  jsonDetalle.put("serie",docVenta.cabeceraVenta.numSerie)
                  jsonDetalle.put("correlativo",docVenta.cabeceraVenta.numCorrelativo)
                  jsonDetalle.put("tipoDocumento",docVenta.cabeceraVenta.tipoDocumento)
                  jsonDetalle.put("motivo",motivo)
                jsonArrayDetalle.add(jsonDetalle)
                jsonDocumento.put("detalle",jsonArrayDetalle)

                val url = rutaApi + "/emission/summaries"

                var clientResult=OkHttpClient().newBuilder().connectTimeout(15,TimeUnit.SECONDS).build()
                val mediaT = MediaType.parse("application/json")
                val body = RequestBody.create(mediaT, jsonDocumento.toString().toByteArray())
                val request2 = Request.Builder()
                        .url(url).post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("User-Agent", "Client Application")
                        .addHeader("Authorization","Bearer $accesToken")
                        .build()

                val response2 = clientResult.newCall(request2).execute()
                r.enviado=true
                var linea = response2.body()!!.string()
                if (linea != null) {
                    val parsearRpta = JSONParser()
                    val json_rspta = parsearRpta.parse(linea) as org.json.simple.JSONObject
                    if(json_rspta.get("data")!=null){
                        var json_data=  json_rspta.get("data") as JSONObject
                        var code="200"
                        var message="OK"
                        r.estadoRespuesta="OK"
                        r.codeSuccess=200
                        r.mensaje=""
                        r.recibido=true

                    }else{
                        r.recibido=true
                        var json_error =json_rspta.get("errors") as JSONArray
                        if(json_error.get(0)!=null){
                            var error1=json_error.get(0) as JSONObject
                            var code=error1.get("status")
                            code.toString()
                            var detail=error1.get("detail")
                            detail.toString()
                            r.codeSuccess=code.toString().toInt()
                            r.estadoRespuesta="Error"
                            r.mensaje=detail.toString()
                        }
                    }

                }

            }
        }else{
            r.enviado=false
            r.recibido=false
            r.codeSuccess=0
            r.estadoRespuesta="ERROR"
            r.mensaje="No se obtuvo el acceso para generar el documento eléctronico.Debe comunicarse con FACTURA ACTIVA."

        }

        return r
    }

}