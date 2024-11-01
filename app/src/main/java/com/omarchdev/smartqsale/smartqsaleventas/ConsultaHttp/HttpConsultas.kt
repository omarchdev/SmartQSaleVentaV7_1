package com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp

import com.omarchdev.smartqsale.smartqsaleventas.API.ApiConsultaDocumento
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.*
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import kotlin.jvm.internal.Intrinsics






public class HttpConsultas{


    var  listenerResultadoBusquedaCliente:ListenerResultadoBusquedaCliente?=null
    var listenerResultadoConsultaNum:ListenerResultadoConsultaNum?=null

    val apiConsultaDocumento= ApiConsultaDocumento()
    interface ListenerResultadoBusquedaCliente{

        fun DatosClienteResultadoSunat(cliente:mCustomer?)
        fun ErrorConsultaCliente(mensaje:String)
        fun ClienteNoHabilitado(nombre:String)

    }
    interface ListenerResultadoConsultaNum{
        fun ResultadoNumero(num:String)
        fun ErrorConsulta(mensaje:String)

    }
    fun ObtenerDatosClienteNumRuc(numRuc:String=""){
        var tipo=""
        when(numRuc.length){
            8->tipo="DNI_1"
            11->tipo="RUC_1"

        }
        if(tipo!=""){
            apiConsultaDocumento.ConsultaPersonaPorTipoDocumento(numRuc,tipo)
            apiConsultaDocumento.irConsultaDocumento=object:ApiConsultaDocumento.IRConsultaDocumento{
                override fun ResultadoConsultaDocumento(customer: mCustomer) {
                    if(customer.estadoDomicilio.equals("HABIDO") && customer.estadoContribuyente.equals("ACTIVO")) {
                        listenerResultadoBusquedaCliente?.DatosClienteResultadoSunat(customer)
                    }
                    else{
                        listenerResultadoBusquedaCliente?.ClienteNoHabilitado(customer?.razonSocial)
                    }
                }

            }
        }

        /*  GlobalScope.launch  {

            var tipo=""
            when(numRuc.length){
                8->tipo="DNI_1"
                11->tipo="RUC_1"

            }
            var cliente:mCustomer?=ConsultaEntidad(numRuc,tipo)
           launch(Dispatchers.Main){
                if(cliente?.getiId()==-99) {

                    listenerResultadoBusquedaCliente?.ErrorConsultaCliente(cliente!!.getcName())
                }else if(cliente?.getiId()==0){
                    if(cliente.estadoDomicilio.equals("HABIDO") && cliente.estadoContribuyente.equals("ACTIVO")) {
                        listenerResultadoBusquedaCliente?.DatosClienteResultadoSunat(cliente)
                    }
                    else{
                        listenerResultadoBusquedaCliente?.ClienteNoHabilitado(cliente?.razonSocial)
                    }
                }
            }
        }
*/
    }

    private fun ConsultaEntidad(numDoc:String,tipo:String):mCustomer?{
        var cliente=mCustomer()
        try {
        var envio = JSONObject()
        envio.put("numDoc",numDoc)
        envio.put("tipoDoc",tipo)
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, envio.toString().toByteArray())
        val request = Request.Builder()
                .url("http://omarchssq-001-site6.etempurl.com/consultaDocumento.php")
                .post(body)
                .addHeader("Content-Type", "application/json")
                 .build()
        val client = OkHttpClient().newBuilder().connectTimeout(15, TimeUnit.SECONDS).build()
        val response = client.newCall(request).execute()

        var linea = ""
        linea = response.body()!!.string()

        if ((linea) != null) {
            val pasearRptaJsonToken = JSONParser()
            val json_rspta = pasearRptaJsonToken.parse(linea) as org.json.simple.JSONObject

            if (json_rspta.get("errors") != null) {
                cliente.setiId(-99)
            }else{
                cliente.setiId(0)
                cliente.estadoDomicilio=json_rspta.get("EstadoDomicilio").toString()
                cliente.estadoContribuyente=json_rspta.get("Estado").toString()
                cliente.razonSocial=json_rspta.get("Denominacion").toString()
                cliente.setcDireccion(json_rspta.get("Direccion").toString())

            }

        }
        } catch (ex1: UnsupportedEncodingException) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.message)
            ex1.toString()
            cliente.setiId(-99)

            //rptaSunat=null
        } catch (ex2: IOException) {
            System.err.println("Error IOException: " + ex2.message)
            ex2.toString()
            cliente.setiId(-99)

            //rptaSunat=null
        } catch (ex3: Exception) {
            System.err.println("Exepction: " + ex3.message)
            ex3.toString()
            cliente.setiId(-99)

            // rptaSunat=null
        }

        return cliente

    }

    private fun ConsultaClienteSunat(numRuc:String=""): mCustomer? {
        var cliente:mCustomer?= mCustomer()
        val route="http://omarch140992-001-site1.htempurl.com/api_consulta_smartqsale/consulta-ruc-json.php"
         val RUTA = "https://ruc.com.pe/api/v1/ruc"
        val Token="f0bcb987-35cf-40d9-a967-ff7b47c462d7-1f48e8c1-2967-47e6-bb49-9b243b4c8b52"
        try {
            val clienteConsulta = DefaultHttpClient()
            val post = HttpPost(route)
            post.addHeader("Content-Type", "application/json") // Cabecera del Content-Type
            val objectoCabecera = org.json.simple.JSONObject()
            objectoCabecera.put("numRuc",numRuc)
            //  objectoCabecera.put("token", Token)
            //  objectoCabecera.put("ruc", numRuc)
            val parametros = StringEntity(objectoCabecera.toString(), "UTF-8")
            post.entity = parametros
            val response = clienteConsulta.execute(post)
            val rd = BufferedReader(InputStreamReader(response.entity.content))
            var linea = ""
            linea = rd.readLine()
            if (linea != null) {
                val parsearRsptaJson = JSONParser()
                val json_rspta = parsearRsptaJson.parse(linea) as org.json.simple.JSONObject
                if (json_rspta.get("error") != null) {

                      cliente?.setiId(-99)
                    cliente?.setcName(json_rspta.get("error").toString())
                }
                else {

                   val parsearRsptaDetalleOK = JSONParser()

                    val json_rspta_ok = parsearRsptaDetalleOK.parse(json_rspta.toString()) as org.json.simple.JSONObject

                    System.out.println(json_rspta_ok.get("success"))
                    System.out.println(json_rspta_ok.get("ruc"))
                    System.out.println(json_rspta_ok.get("nombre_o_razon_social"))
                    System.out.println(json_rspta_ok.get("estado_del_contribuyente"))
                    System.out.println(json_rspta_ok.get("condicion_de_domicilio"))
                    System.out.println(json_rspta_ok.get("ubigeo"))
                    System.out.println(json_rspta_ok.get("tipo_de_via"))
                    System.out.println(json_rspta_ok.get("nombre_de_via"))
                    System.out.println(json_rspta_ok.get("codigo_de_zona"))
                    System.out.println(json_rspta_ok.get("tipo_de_zona"))
                    System.out.println(json_rspta_ok.get("numero"))
                    System.out.println(json_rspta_ok.get("lote"))
                    System.out.println(json_rspta_ok.get("dpto"))
                    System.out.println(json_rspta_ok.get("manzana"))
                    System.out.println(json_rspta_ok.get("kilometro"))
                    System.out.println(json_rspta_ok.get("departamento"))
                    System.out.println(json_rspta_ok.get("provincia"))
                    System.out.println(json_rspta_ok.get("distrito"))
                    System.out.println(json_rspta_ok.get("direccion"))
                    System.out.println(json_rspta_ok.get("direccion_completa"))
                    System.out.println(json_rspta_ok.get("ultima_actualizacion"))

                    cliente?.setiId(0)
                    cliente?.razonSocial=json_rspta_ok.get("nombre_o_razon_social").toString()
                    cliente?.setcDireccion(json_rspta_ok.get("direccion_completa").toString())
                    cliente?.isSuccess=json_rspta_ok.get("success").toString().toBoolean()
                    cliente?.estadoDomicilio=json_rspta_ok.get("condicion_de_domicilio").toString()
                    cliente?.estadoContribuyente=json_rspta_ok.get("estado_del_contribuyente").toString()
                }
            } else {

            }

        }catch (ex1:UnsupportedEncodingException) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.message)
            cliente?.setiId(-99)
        } catch (ex2: IOException) {
            System.err.println("Error IOException: " + ex2.message)
            cliente?.setiId(-99)
        } catch (ex3: Exception) {
            System.err.println("Exepction: " + ex3.message)
            cliente?.setiId(-99)

        }
        return cliente
    }


    fun GenerarDocumentoElectronicoNubefact(documentoVenta:mDocVenta): ResultadoComprobante {
       var rptaSunat: RptaSunat?=RptaSunat()
       var resultadoComprobante=ResultadoComprobante()

        //  TOKEN para enviar documentos
        //val TOKEN = "ecc87ba57a234b85b8a9fa0ad7a66f8e8a15f7ad38a7455ca2d478a7449cb42b"
        try {
            /*
#########################################################
#### PASO 2: GENERAR EL ARCHIVO PARA ENVIAR A NUBEFACT ####
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
# - MANUAL para archivo JSON en el link: https://goo.gl/WHMmSb
# - MANUAL para archivo TXT en el link: https://goo.gl/Lz7hAq
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/   val RUTA = Constantes.TokenFactura.rutaApi.trim()

            val cliente = DefaultHttpClient()
            val post = HttpPost(RUTA)
            post.addHeader("Authorization", "Token token=${Constantes.TokenFactura.tokenEnvio.trim()}") // Cabecera del token
            post.addHeader("Content-Type", "application/json") // Cabecera del Content-Type


            val objetoCabecera = org.json.simple.JSONObject() // Instancear el  segundario
            objetoCabecera.put("operacion","generar_comprobante")//Obligatorio
            objetoCabecera.put("tipo_de_comprobante",documentoVenta.cabeceraVenta.idComprobantePagoSunat)//Obligatorio
            objetoCabecera.put("serie",documentoVenta.cabeceraVenta.numSerie)//Obligatorio
            objetoCabecera.put("numero",documentoVenta.cabeceraVenta.numCorrelativo)//Obligatorio
            objetoCabecera.put("sunat_transaction","1")//Obligatorio
            if(documentoVenta.cabeceraVenta.cliente.idTipoDocSunat==8){
                objetoCabecera.put("cliente_tipo_de_documento","-")//Obligatorio
                objetoCabecera.put("cliente_numero_de_documento","-")//Obligatorio
                objetoCabecera.put("cliente_denominacion","Varios")//Obligatorio

            }else{
                objetoCabecera.put("cliente_tipo_de_documento",documentoVenta.cabeceraVenta.cliente.idTipoDocSunat)//Obligatorio
                objetoCabecera.put("cliente_numero_de_documento",documentoVenta.cabeceraVenta.cliente.numeroRuc)//Obligatorio
                objetoCabecera.put("cliente_denominacion",documentoVenta.cabeceraVenta.cliente.razonSocial.trim())//Obligatorio

            }
           objetoCabecera.put("cliente_direccion",documentoVenta.cabeceraVenta.cliente.getcDireccion().trim())//Obligatorio si es factura o boleta
            objetoCabecera.put("cliente_email",documentoVenta.cabeceraVenta.cliente.getcEmail().trim())//Opcional
            objetoCabecera.put("cliente_email_1","")//Opcional
            objetoCabecera.put("cliente_email_2","")//Opcional
            objetoCabecera.put("fecha_de_emision", documentoVenta.cabeceraVenta.fechaEmision)//Obligatorio
            objetoCabecera.put("fecha_de_vencimiento","")//Opcional
            objetoCabecera.put("moneda", Constantes.DivisaPorDefecto.idDivisaSunat.toString())//Obligatorio
            objetoCabecera.put("tipo_de_cambio","")//Opcional
            objetoCabecera.put("porcentaje_de_igv","${String.format("%.2f",Constantes.IGV.valorIgv)}")//Obligatorio
            objetoCabecera.put("descuento_global",documentoVenta.cabeceraVenta.descuentoGlobal.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("total_descuento",documentoVenta.cabeceraVenta.totalDescuento.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("total_anticipo",documentoVenta.cabeceraVenta.totalAnticipo.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("total_gravada",documentoVenta.cabeceraVenta.totalGravado.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("total_inafecta",documentoVenta.cabeceraVenta.totalInafecta.setScale(2,BigDecimal.ROUND_HALF_EVEN))
          //  objetoCabecera.put("total_inafecta",0.1)
            objetoCabecera.put("total_exonerada",documentoVenta.cabeceraVenta.totalExonerada.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("total_igv",documentoVenta.cabeceraVenta.totalIgv.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("total_gratuita",documentoVenta.cabeceraVenta.totalGratuita.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("total_otros_cargos",documentoVenta.cabeceraVenta.totalOtrosCargos.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("total",documentoVenta.cabeceraVenta.totalPagado.setScale(2,BigDecimal.ROUND_HALF_EVEN))
            objetoCabecera.put("percepcion_tipo","")
          //  objetoCabecera.put("total_impuestos_bolsas",0.1)

            objetoCabecera.put("percepcion_base_imponible","")
            objetoCabecera.put("total_percepcion","")
            objetoCabecera.put("total_incluido_percepcion","")
            objetoCabecera.put("detraccion", "false")
            objetoCabecera.put("observaciones", "")

            if(documentoVenta.esNota) {
                objetoCabecera.put("documento_que_se_modifica_tipo", documentoVenta.tipoDocReferenciaNum)
                objetoCabecera.put("documento_que_se_modifica_serie", documentoVenta.serieReferencia)
                objetoCabecera.put("documento_que_se_modifica_numero", documentoVenta.correlativoReferencia)
                objetoCabecera.put("tipo_de_nota_de_credito", 1)
                objetoCabecera.put("tipo_de_nota_de_debito", "")
            }else{
  /*
                objetoCabecera.put("documento_que_se_modifica_tipo", "")
                objetoCabecera.put("documento_que_se_modifica_serie", "")
                objetoCabecera.put("documento_que_se_modifica_numero", "")
                objetoCabecera.put("tipo_de_nota_de_credito", "")
                objetoCabecera.put("tipo_de_nota_de_debito", "")
*/
            }

            objetoCabecera.put("codigo_unico",Constantes.Empresa.idEmpresa.toString()+"-"+ documentoVenta.cabeceraVenta.idVenta.toString()+documentoVenta.cabeceraVenta.numSerie+documentoVenta.cabeceraVenta.numCorrelativo)
            objetoCabecera.put("enviar_automaticamente_a_la_sunat", "false")
            objetoCabecera.put("enviar_automaticamente_al_cliente", "true")

            objetoCabecera.put("condiciones_de_pago", "")
            objetoCabecera.put("medio_de_pago", "")
            objetoCabecera.put("placa_vehiculo", "")
            objetoCabecera.put("orden_compra_servicio", "")
            objetoCabecera.put("tabla_personalizada_codigo", "")
            objetoCabecera.put("formato_de_pdf", "")
            var lista = org.json.simple.JSONArray()
            var detalle_linea_1:JSONObject = org.json.simple.JSONObject()
            documentoVenta.productoEnVenta.forEach {
                detalle_linea_1=org.json.simple.JSONObject()
                detalle_linea_1.put("unidad_de_medida", "${it.codUnidSunat.trim()}")
                detalle_linea_1.put("codigo","${it.codigoProducto}")
                detalle_linea_1.put("descripcion", "${it.productName} ${it.descripcionVariante}".trim())
                detalle_linea_1.put("cantidad", BigDecimal(it.cantidad.toDouble()).setScale(2,BigDecimal.ROUND_HALF_EVEN))
                detalle_linea_1.put("valor_unitario", it.valorUnitario)
                detalle_linea_1.put("precio_unitario", it.precioOriginal)
                detalle_linea_1.put("descuento", it.montoDescuento)
                detalle_linea_1.put("subtotal", it.precioNeto)
                detalle_linea_1.put("tipo_de_igv", "1")
                detalle_linea_1.put("igv", it.montoIgv)
                detalle_linea_1.put("total", it.precioVentaFinal)
                detalle_linea_1.put("anticipo_regularizacion", "false")
                detalle_linea_1.put("anticipo_serie", "")
                detalle_linea_1.put("anticipo_documento_numero", "")
                lista.add(detalle_linea_1)
            }
            /*
            detalle_linea_1=org.json.simple.JSONObject()
            detalle_linea_1.put("unidad_de_medida", "ZZ")
            detalle_linea_1.put("codigo","001")
            detalle_linea_1.put("descripcion", "BOLSA".trim())
            detalle_linea_1.put("cantidad",1)
            detalle_linea_1.put("valor_unitario",0.10)
            detalle_linea_1.put("precio_unitario", 0.10)
            detalle_linea_1.put("descuento", 0)
            detalle_linea_1.put("subtotal", 0.10)
            detalle_linea_1.put("tipo_de_igv", "9")
            detalle_linea_1.put("igv", 0)
            detalle_linea_1.put("total",0.10)
            detalle_linea_1.put("impuesto_bolsas", 0.10)
            detalle_linea_1.put("anticipo_regularizacion", "false")
            detalle_linea_1.put("anticipo_serie", "")
            detalle_linea_1.put("anticipo_documento_numero", "")
            lista.add(detalle_linea_1)*/
            objetoCabecera.put("items", lista)
            /*
#########################################################
#### PASO 3: ENVIAR EL ARCHIVO A NUBEFACT ####
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
# SI ESTÃS TRABAJANDO CON ARCHIVO JSON
# - Debes enviar en el HEADER de tu solicitud la siguiente lo siguiente:
# Authorization = Token token="8d19d8c7c1f6402687720eab85cd57a54f5a7a3fa163476bbcf381ee2b5e0c69"
# Content-Type = application/json
# - Adjuntar en el CUERPO o BODY el archivo JSON o TXT
# SI ESTÃS TRABAJANDO CON ARCHIVO TXT
# - Debes enviar en el HEADER de tu solicitud la siguiente lo siguiente:
# Authorization = Token token="8d19d8c7c1f6402687720eab85cd57a54f5a7a3fa163476bbcf381ee2b5e0c69"
# Content-Type = text/plain
# - Adjuntar en el CUERPO o BODY el archivo JSON o TXT
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
            resultadoComprobante.enviado=true
            val parametros = StringEntity(objetoCabecera.toString(), "UTF-8")
            post.setEntity(parametros)
            val response = cliente.execute(post)
            resultadoComprobante.enviado=true
            resultadoComprobante.recibido=false
            resultadoComprobante.codeSuccess=0
            resultadoComprobante.estadoRespuesta="ERROR"
            resultadoComprobante.mensaje="Se envio el documento pero no se recibio una respuesta"
            val rd = BufferedReader(InputStreamReader(response.getEntity().getContent()))
            var linea = ""
             linea= rd.readLine()
                 if ((linea ) != null) {

                val parsearRsptaJson = JSONParser()
                val json_rspta = parsearRsptaJson.parse(linea) as org.json.simple.JSONObject

                /*
#########################################################
#### PASO 4: LEER RESPUESTA DE NUBEFACT ####
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
# RecibirÃ¡s una respuesta de NUBEFACT inmediatamente lo cual se debe leer, verificando que no haya errores.
# Debes guardar en la base de datos la respuesta que te devolveremos.
# EscrÃ­benos a soporte@nubefact.com o llÃ¡manos al telÃ©fono: 01 468 3535 (opciÃ³n 2) o celular (WhatsApp) 955 598762
# Puedes imprimir el PDF que nosotros generamos como tambiÃ©n generar tu propia representaciÃ³n impresa previa coordinaciÃ³n con nosotros.
# La impresiÃ³n del documento seguirÃ¡ haciÃ©ndose desde tu sistema. Enviaremos el documento por email a tu cliente si asÃ­ lo indicas en el archivo JSON o TXT.
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
*/
                if (json_rspta.get("errors") != null) {
                    rptaSunat?.mensajeError=json_rspta.get("errors").toString()
                    resultadoComprobante.codeSuccess=json_rspta.get("codigo").toString().toInt()
                    resultadoComprobante.mensaje=json_rspta.get("errors").toString()
                    resultadoComprobante.estadoRespuesta="Error"
                    resultadoComprobante.recibido=true
                }
                else {
                    val parsearRsptaDetalleOK = JSONParser()
                    val json_rspta_ok = parsearRsptaDetalleOK.parse(json_rspta.get("invoice").toString()) as org.json.simple.JSONObject
                    rptaSunat?.tipoComprobante=json_rspta_ok.get("tipo_de_comprobante").toString().toInt()
                    rptaSunat?.serie=json_rspta_ok.get("serie").toString()
                    rptaSunat?.numero=json_rspta_ok.get("numero").toString().toInt()
                    rptaSunat?.enlace=json_rspta_ok.get("enlace").toString()
                    rptaSunat?.aceptada_sunat=json_rspta_ok.get("aceptada_por_sunat").toString().toBoolean()
                    rptaSunat?.sunat_descripcion=json_rspta_ok.get("sunat_description").toString()
                    rptaSunat?.sunat_nota=json_rspta_ok.get("sunat_note")?.toString()
                    rptaSunat?.sunat_responsecode=json_rspta_ok.get("sunat_responsecode")?.toString()
                    rptaSunat?.sunat_soap_error=json_rspta_ok.get("sunat_soap_error")?.toString()
                    rptaSunat?.pdf_zip_base64=json_rspta_ok.get("pdf_zip_base64")?.toString()
                    rptaSunat?.xml_zip_base64=json_rspta_ok.get("xml_zip_base64")?.toString()
                    rptaSunat?.cdr_zip_base64=json_rspta_ok.get("cdr_zip_base64")?.toString()
                    rptaSunat?.cadena_codigo_qr=json_rspta_ok.get("cadena_para_codigo_qr").toString()
                    rptaSunat?.codigo_hash=json_rspta_ok.get("codigo_hash").toString()
                    resultadoComprobante.estadoRespuesta="OK"
                    resultadoComprobante.rptaSunat=rptaSunat!!
                    resultadoComprobante.codeSuccess=200
                    resultadoComprobante.estadoRespuesta="OK"
                    resultadoComprobante.mensaje=""
                    resultadoComprobante.recibido=true

                }
            }
        } catch (ex1: UnsupportedEncodingException) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.message)
            resultadoComprobante.enviado=true
            resultadoComprobante.recibido=false
            resultadoComprobante.codeSuccess=0
            resultadoComprobante.mensaje="No se recibio respuesta UnsupportedEncodingException ${ex1.message}"
            resultadoComprobante.estadoRespuesta="ERROR"
        } catch (ex2: IOException) {
            System.err.println("Error IOException: " + ex2.message)
            resultadoComprobante.enviado=true
            resultadoComprobante.recibido=false
            resultadoComprobante.codeSuccess=0
            resultadoComprobante.mensaje="No se recibio respuesta Error IOException: ${ex2.message}"
            resultadoComprobante.estadoRespuesta="ERROR"
            rptaSunat=null
        } catch (ex3: Exception) {
            System.err.println("Exepction: " + ex3.message)
            resultadoComprobante.enviado=true
            resultadoComprobante.recibido=false
            resultadoComprobante.codeSuccess=0
            resultadoComprobante.mensaje="No se recibio respuesta Exepction ${ex3.message}"
            resultadoComprobante.estadoRespuesta="ERROR"
            rptaSunat=null
        }
   //     var s=doc.getElementById()
        return resultadoComprobante
    }

    fun AnularDocumento(docVenta: mDocVenta,motivo:String):ResultadoComprobante{
        var resultadoComprobante=ResultadoComprobante()
        val jsonAnulacion=JSONObject()
        jsonAnulacion.put("operacion","generar_anulacion")
        jsonAnulacion.put("tipo_de_comprobante",docVenta.cabeceraVenta.idTipoDocPago)
        jsonAnulacion.put("serie",docVenta.cabeceraVenta.numSerie)
        jsonAnulacion.put("numero",docVenta.cabeceraVenta.numCorrelativo)
        jsonAnulacion.put("motivo",motivo)
        try {
            val client = OkHttpClient().newBuilder().connectTimeout(15, TimeUnit.SECONDS).build()
            val mediaType = MediaType.parse("application/json")
            val body = RequestBody.create(mediaType, jsonAnulacion.toString().toByteArray())
            val RUTA = Constantes.TokenFactura.rutaApi.trim()
            val request = Request.Builder()
                    .url(RUTA)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Token token=${Constantes.TokenFactura.tokenEnvio.trim()}")
                    .build()
            /*
        *  .addHeader("User-Agent", "Client Application")
                .addHeader("cache-control", "no-cache")
        * */
            val response = client.newCall(request).execute()
            resultadoComprobante.enviado=true
            resultadoComprobante.recibido=false
            resultadoComprobante.codeSuccess=0
            resultadoComprobante.mensaje="No se recibio respuesta"
            resultadoComprobante.estadoRespuesta="ERROR"
            var linea = ""
            linea = response.body()!!.string()
            if (linea != null) {
                val pasearRptaJson = JSONParser()
                var jsonRpta=pasearRptaJson.parse(linea) as org.json.simple.JSONObject
                if(jsonRpta.get("errors")==null){

                    var rptaSunat=RptaSunat()

                    rptaSunat.enlace=jsonRpta["enlace"].toString()
                    resultadoComprobante.mensaje=""
                    resultadoComprobante.estadoRespuesta=""
                    resultadoComprobante.codeSuccess=200
                    resultadoComprobante.estadoRespuesta="OK"
                    resultadoComprobante.enviado=true
                    resultadoComprobante.recibido=true
                    resultadoComprobante.urlPdf=if(jsonRpta["enlace"]!=null){jsonRpta["enlace"].toString()}else{""}
                    resultadoComprobante.rptaSunat=rptaSunat
                }else{
                    resultadoComprobante.enviado=true
                    resultadoComprobante.recibido=true
                    resultadoComprobante.estadoRespuesta="ERROR"
                    resultadoComprobante.mensaje=jsonRpta.get("errors").toString()
                    resultadoComprobante.codeSuccess=jsonRpta.get("codigo").toString().toInt()
                }
            } else {
                resultadoComprobante.enviado=true
                resultadoComprobante.recibido=true
                resultadoComprobante.estadoRespuesta="ERROR"
                resultadoComprobante.mensaje="Resultado JSON NULL"
                resultadoComprobante.codeSuccess=0
            }
        } catch (ex1: UnsupportedEncodingException) {
            System.err.println("Error UnsupportedEncodingException: " + ex1.message)
            resultadoComprobante.enviado=true
            resultadoComprobante.recibido=false
            resultadoComprobante.codeSuccess=0
            resultadoComprobante.mensaje="No se recibio respuesta UnsupportedEncodingException: ${ex1.message}"
            resultadoComprobante.estadoRespuesta="ERROR"
            //rptaSunat=null
        } catch (ex2: IOException) {
            System.err.println("Error IOException: " + ex2.message)
            resultadoComprobante.enviado=true
            resultadoComprobante.recibido=false
            resultadoComprobante.codeSuccess=0
            resultadoComprobante.mensaje="No se recibio respuesta  Error IOException:${ex2.message}"
            resultadoComprobante.estadoRespuesta="ERROR"
           // rptaSunat=null
        } catch (ex3: Exception) {
            System.err.println("Exepction: " + ex3.message)
            resultadoComprobante.enviado=true
            resultadoComprobante.recibido=false
            resultadoComprobante.codeSuccess=0
            resultadoComprobante.mensaje="No se recibio respuesta Exepction: ${ex3.message}"
            resultadoComprobante.estadoRespuesta="ERROR"
            //rptaSunat=null
        }

        return resultadoComprobante
    }


    fun ConsultaServicio(){

         GlobalScope.launch  {
            var r=BdConnectionSql.getSinglentonInstance().GetNumeroSoporte()
            r.resultado.length
           launch(Dispatchers.Main){
                r.resultado.length
                if(r.estado){
                    listenerResultadoConsultaNum?.ResultadoNumero(r.resultado)
                }else{
                    listenerResultadoConsultaNum?.ErrorConsulta(r.resultado)
                }
            }
         }

    }

    private fun consultaNumServicioTecnico():mNumServicioConsulta{
        var respuesta=mNumServicioConsulta()
        val route="http://omarchssq-001-site3.etempurl.com/api_consulta_smartqsale/consulta-num-servicio-tecnico.php"
        val numConsulta = DefaultHttpClient()
        val post = HttpPost(route)
        post.addHeader("Content-Type", "application/json") // Cabecera del Content-Type
        var linea = ""
        val response = numConsulta.execute(post)

        val rd = BufferedReader(InputStreamReader(response.entity.content))

        linea = rd.readLine()

        if(linea!=null){
            val parsearRsptaJson = JSONParser()
            val json_rspta = parsearRsptaJson.parse(linea) as org.json.simple.JSONObject
            if (json_rspta.get("success") != null) {
                                respuesta.estado=json_rspta.get("success").toString().toBoolean()
                    respuesta.resultado=json_rspta.get("numtelefono").toString()
                    respuesta.resultado.length
            }
        }else{
            respuesta.estado=false
            respuesta.resultado="Error al encontrar el número de servicio técnico"
        }
        return respuesta
    }


    private fun ConsultarDocumento(numDoc: String):mCustomer{

        var r=mCustomer()
        val cliente = DefaultHttpClient()
        var tipoDoc = ""
        val length = numDoc.length
        if (length == 8) {
            tipoDoc = "DNI_1"
        } else if (length == 11) {
            tipoDoc = "RUC_1"
        }
        var post=HttpPost(Constantes.Links.links.filter { it->it.codeUrl=="CODC1" }.first().url)
        post.addHeader("Content-Type", "application/json")
        val jsonEnviar = JSONObject()
        jsonEnviar["numDoc"] = numDoc
        jsonEnviar["tipoDoc"] = tipoDoc
        post.entity = StringEntity(jsonEnviar.toString(), "UTF-8")
        val rd = BufferedReader(InputStreamReader(cliente.execute(post).entity.content))
        var linea = ""
        var readLine = rd.readLine()
        Intrinsics.checkExpressionValueIsNotNull(readLine, "rd.readLine()")
        linea = readLine
        if (linea != null) {

            var json_rspta: JSONObject? = JSONParser().parse(linea)  as org.json.simple.JSONObject
            if (json_rspta != null) {
                json_rspta = json_rspta
                if (json_rspta["errors"] == null) {
                    if (json_rspta["Denominacion"] != null) {
                        r.razonSocial=json_rspta.get("Denominacion").toString()
                    }
                    if (json_rspta["Direccion"] != null) {
                        r.setcDireccion(json_rspta.get("Direccion").toString())
                    }
                    if (json_rspta["Estado"] != null) {
                        r.estadoContribuyente=json_rspta.get("Estado").toString()
                    }
                    if (json_rspta["EstadoDomicilio"] != null) {
                        r.estadoDomicilio=json_rspta.get("EstadoDomicilio").toString()
                    }
                }
            }
        }

        return r
    }


}




val String.bgS: BigDecimal
    get() {
        return this.toBigDecimal()
    }

val Int.bg: BigDecimal
    get() {
        return BigDecimal(this)
    }

