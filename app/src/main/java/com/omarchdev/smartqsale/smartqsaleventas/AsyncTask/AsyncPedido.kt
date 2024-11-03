package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.*
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPagoRepository
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal


class AsyncPedido(context: Context) {


    var context = context
    private val controladorVentas = ControladorVentas()
    var pedidoReserva: PedidoReserva? = null
    private val controladorProcesoCargar = ControladorProcesoCargar(context)
    var resultadoBusquedaProductoCodigoBarra: ResultadoBusquedaProductoCodigoBarra? = null
    var resultAforo: ResultAforo? = null

    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iPedidoRespository = retro.create(IPedidoRespository::class.java)
    var iPagoRepository=retro.create(IPagoRepository::class.java)

    interface PedidoReserva {
        fun ExitoReservar()
        fun ErrorReservar(mensaje: String)
    }

    interface ResultAforo {
        fun GetResultAforoDisponible(aforo: Int)
    }

    //API_AV

    /**
     * Consulta en api
     * **/
    fun ConsultaAforoDisponible() {

        if (Constantes.ConfigTienda.bUsaAforo) {
             GlobalScope.launch  {
                val aforo =
                    iPedidoRespository.GetAforoDisponible(codeCia, BASECONN.TIPO_CONSULTA).execute()
                        .body()
                // val aforo = BdConnectionSql.getSinglentonInstance().GetAforoDisponible()
               launch(Dispatchers.Main){
                    if (aforo != null)
                        resultAforo?.GetResultAforoDisponible(aforo)
                }
            }
        } else {
            resultAforo?.GetResultAforoDisponible(0)
        }

    }

    public interface ResultadoBusquedaProductoCodigoBarra {
        fun ExisteProducto(productoEnVenta: ProductoEnVenta)
        fun ErrorBusquedaProducto(str: String)
    }

    var listenerEliminarPedido: ListenerEliminarPedido? = null

    interface ListenerEliminarPedido {

        fun EliminarPedidoRespuesta()
        fun ErrorEliminar()
        fun PedidoNoDisponible()
    }


    interface ListenerObtenerPedidos {
        fun PedidosResultado(result: List<mCabeceraPedido>?)
        fun ErrorPedidos()

    }

    private fun TextoCabeceraPedido(cabeceraPedido: mCabeceraPedido): String {
        var texto: String = ""
        texto = "              \n\n" + cabeceraPedido.numPedido + "\n"
        texto = texto + " Fecha Pedido : " + cabeceraPedido.fechaReserva + "\n"
        texto = texto + " Atendido por: " + cabeceraPedido.nombreVendedor + "\n"
        texto = texto + " Cliente: " + cabeceraPedido.denominacionCliente + "\n"
        texto = texto + " Identificador: " + cabeceraPedido.identificadorPedido + "\n"
        texto = texto + " Observacion: " + cabeceraPedido.observacion + "\n"
        return texto
    }

    interface TimeResult {
        fun TimeResultOk(time: String)
    }

    fun GetTiempoAsync(timeResult: TimeResult) {

         GlobalScope.launch  {
            val t = BdConnectionSql.getSinglentonInstance().ObtenerHoraActual()
           launch(Dispatchers.Main){
                timeResult.TimeResultOk(t.fecha + " " + t.hora)
            }
        }

    }


    interface ResultGeneraPedido {
        fun pedidoGenerado()
    }

    fun GeneraPedidoPlaca(idPedido: Int, nroPlaca: String, resultGeneraPedido: ResultGeneraPedido) {


         GlobalScope.launch  {
            BdConnectionSql.getSinglentonInstance().GeneraPromocion(idPedido, nroPlaca);
           launch(Dispatchers.Main){
                resultGeneraPedido.pedidoGenerado()

            }
        }

    }

    fun GuardarPedido(
        idCabeceraPedido: Int, productos: List<ProductoEnVenta>,
        cabeceraPedido: mCabeceraPedido, info: InfoGuardadoPedido
    ) {
        controladorProcesoCargar?.IniciarDialogCarga("Reservando Pedido")
         GlobalScope.launch  {

            info.idCabeceraPedido = idCabeceraPedido
            val solicitudEnvio = SolicitudEnvio(
                codeCia,
                Constantes.TipoMovimientoApi.DEFAULT,
                info,
                Constantes.Terminal.idTerminal,
                Constantes.Usuario.idUsuario
            )
            var json = Gson().toJson(solicitudEnvio)
            var r1 = json

            val lista = iPedidoRespository.ModificarEstadoPedido(solicitudEnvio).execute().body()

            if (lista!!.codeResult == 200) {
                if (lista.data!!.isNotEmpty() && info.reservaPedido) {
                    if (lista.data!!.get(0).idDetallePedido != -95) {
                        try {
                            val li = iPedidoRespository.ObtenerIdentificadorPedido(
                                codeCia,
                                TIPO_CONSULTA,
                                idCabeceraPedido,
                            ).execute().body()
                            if (li != null) {
                                cabeceraPedido.numPedido = li.get(0)
                                cabeceraPedido.fechaReserva = li.get(1)
                                cabeceraPedido.denominacionCliente = li.get(2)
                                cabeceraPedido.nombreVendedor = li.get(3)
                                cabeceraPedido.identificadorPedido = li.get(4)
                                cabeceraPedido.observacion = li.get(5)
                            }

                        } catch (ex: Exception) {
                            ex.toString()
                        }
                        /*
                        var li = BdConnectionSql.getSinglentonInstance()
                            .ObtenerIdentificadorPedido(idCabeceraPedido)*/


                        val ca = TextoCabeceraPedido(cabeceraPedido)
                        val listado = obtenerTextoPedidos(ArrayList(lista.data!!))
                        val impresionRed = cImpresionRed()

                        val confirmaciones = ArrayList<ConfirmacionImpresion>()
                        val confirmacionImpresion = ConfirmacionImpresion()
                        listado.forEach {

                            confirmacionImpresion.imprimio = impresionRed.ImpresionPedido(
                                it.impresoraRed.IP,
                                it.impresoraRed.puerto,
                                it.cadenaPedido,
                                ca
                            )
                            confirmacionImpresion.idArea = it.idArea
                            confirmaciones.add(confirmacionImpresion)
                        }
                        try {
                            val result = iPedidoRespository.ActualizarEstadoImpresionPedido(
                                confirmaciones, codeCia,
                                TIPO_CONSULTA, idCabeceraPedido
                            ).execute().body()
                        }catch (ex:Exception){
                            Log.e("ErrAct",ex.toString())
                        }

                        /*
                        BdConnectionSql.getSinglentonInstance()
                            .ActualizarEstadoImpresionPedido(confirmaciones, idCabeceraPedido)*/
                        confirmaciones.clear()
                        ArrayList(lista.data!!).clear()
                        listado.clear()
                    }
                }
               launch(Dispatchers.Main){

                    if (Constantes.ConfigTienda.bImprimePrecuentaAutomatica && info.imprimi && info.reservaPedido) {
                        var pedido: Pedido
                        var listpagosventa: ArrayList<mPagosEnVenta>
                         GlobalScope.launch  {

                            pedido= iPedidoRespository.GetPedidoId(codeCia, TIPO_CONSULTA,idCabeceraPedido, Constantes.ConfigTienda.idTipoZonaServicio,false).execute().body()!!

                            /*
                            pedido = BdConnectionSql.getSinglentonInstance()
                                .ObtenerPedidoId(idCabeceraPedido, false)*/
                            listpagosventa= ArrayList(iPagoRepository.GetPagosPedido(codeCia, TIPO_CONSULTA,idCabeceraPedido).execute().body()!!)
                            /*listpagosventa = ArrayList(
                                BdConnectionSql.getSinglentonInstance()
                                    .getPagosRealizadosDetallePedido(idCabeceraPedido)
                            )*/
                            pedido.pagosEnPedido = listpagosventa
                           launch(Dispatchers.Main){
                                val impresion = cImpresion(context)
                                impresion.ImpresionPedido(pedido)
                                controladorProcesoCargar.FinalizarDialogCarga()
                                pedidoReserva?.ExitoReservar()
                            }
                        }
                    } else {
                        controladorProcesoCargar.FinalizarDialogCarga()
                        pedidoReserva?.ExitoReservar()
                    }


                }
            } else {

               launch(Dispatchers.Main){
                    controladorProcesoCargar.FinalizarDialogCarga()
                    pedidoReserva?.ErrorReservar(lista.messageResult)

                }

            }


        }

    }

    /*
    @GET("api/Pedido/GetBienesServiciosDetraccion")
    fun GetBienesServiciosDetraccion(
        @Query("tipoConsulta") tipoConsulta: String,
        @Query("codeCia") codeCia: String
    ): Call<List<BienServicioDetraccion>>
    */
    interface IResultBienesServiciosDetraccion {
        fun ResultBienesServiciosDetraccion(result: List<BienServicioDetraccion>)
    }
    fun GetBienesServiciosDetraccion( iResultBienesServiciosDetraccion: IResultBienesServiciosDetraccion) {
         GlobalScope.launch  {
             try {
                 val result = iPedidoRespository.GetBienesServiciosDetraccion(BASECONN.TIPO_CONSULTA, codeCia).execute().body()
                 launch(Dispatchers.Main){
                     if (result != null) {
                         iResultBienesServiciosDetraccion.ResultBienesServiciosDetraccion(result)
                     }
                 }
             }catch (ex:Exception){
                 launch(Dispatchers.Main){
                         iResultBienesServiciosDetraccion.ResultBienesServiciosDetraccion(ArrayList())

                 }
             }

        }
    }


    /*
        @POST("api/Pedido/CalculoDetraccion")
    fun CalculoDetraccion(@Body sol: SolicitudEnvio<BigDecimal>): Call<DetraccionCalculo>

    * */
    interface IResultCalculoDetraccion {
        fun ResultCalculoDetraccion(result: DetraccionCalculo)
    }
    fun CalculoDetraccion(monto: BigDecimal,codigo_detraccion:String, iResultCalculoDetraccion: IResultCalculoDetraccion) {
         GlobalScope.launch  {

           val detraccionInput=InputDetraccionCalculo(monto,codigo_detraccion)
           val solicitudEnvioJson = Gson().toJson(SolicitudEnvio(codeCia, TIPO_CONSULTA, detraccionInput))
           val str=solicitudEnvioJson
           val result = iPedidoRespository.CalculoDetraccion(SolicitudEnvio(codeCia, TIPO_CONSULTA,detraccionInput)).execute().body()
           launch(Dispatchers.Main){
                if (result != null) {
                    iResultCalculoDetraccion.ResultCalculoDetraccion(result)
                }
            }
        }
    }



    interface IResultZonaServicioPedido {
        fun PedidoEncontrado(idPedido: Int)
        fun PedidoError()
    }

    fun GetIdPedidoZonaServicio(
        descripcionZona: String,
        iResultZonaServicioPedido: IResultZonaServicioPedido
    ) {

         GlobalScope.launch  {
            val idPedido = iPedidoRespository.GetIdZonaServicioReservado(
                codeCia,
                BASECONN.TIPO_CONSULTA, descripcionZona
            ).execute().body()
           launch(Dispatchers.Main){

                if (idPedido != null) {

                    if (idPedido > 0) {
                        iResultZonaServicioPedido.PedidoEncontrado(idPedido)
                    } else {
                        iResultZonaServicioPedido.PedidoError()
                    }
                } else {
                    iResultZonaServicioPedido.PedidoError()
                }

            }
        }

    }


    private fun obtenerTextoPedidos(listaProducto: ArrayList<ProductoEnVenta>): ArrayList<PedidoImpresora> {
        var cadena = ""
        var lista = ArrayList<PedidoImpresora>()
        var tempIdArea = 0
        tempIdArea = listaProducto.get(0).areaProduccion.idArea
        var i = 0
        var len = listaProducto.size
        var pedidoImpresora: PedidoImpresora
        var impresoraRed = mImpresora()
        impresoraRed.idArea = listaProducto.get(0).areaProduccion.idArea
        impresoraRed.IP = listaProducto.get(0).areaProduccion.impresora.IP
        impresoraRed.puerto = listaProducto.get(0).areaProduccion.impresora.puerto
        impresoraRed.tipoImpresora = listaProducto.get(0).areaProduccion.impresora.tipoImpresora
        impresoraRed.macAddress = listaProducto.get(0).areaProduccion.impresora.macAddress
        while (i < len) {

            if (tempIdArea == listaProducto.get(i).areaProduccion.idArea) {
                var itemPedido = ItemPedido()
                if (listaProducto.get(i).isEsDetallePack) {
                    itemPedido.descripcion = listaProducto.get(i).descripcionCombo +
                            " " + listaProducto.get(i).productName
                    if (listaProducto.get(i).descripcionVariante.length > 0) {
                        itemPedido.descripcion = itemPedido.descripcion + "/" +
                                listaProducto.get(i).descripcionVariante
                    }
                    if (listaProducto.get(i).descripcionModificador.length > 0) {
                        itemPedido.descripcion =
                            itemPedido.descripcion + "\n" + listaProducto.get(i).descripcionModificador
                    }
                    if (listaProducto.get(i).observacionProducto.length > 0) {
                        itemPedido.descripcion =
                            itemPedido.descripcion + "\n" + listaProducto.get(i).observacionProducto
                    }
                } else {
                    itemPedido.descripcion = listaProducto.get(i).productName
                    if (listaProducto.get(i).descripcionVariante.length > 0) {
                        itemPedido.descripcion = itemPedido.descripcion + "/" +
                                listaProducto.get(i).descripcionVariante
                    }
                    if (listaProducto.get(i).descripcionModificador.length > 0) {
                        itemPedido.descripcion =
                            itemPedido.descripcion + " " + listaProducto.get(i).descripcionModificador
                    }
                    if (listaProducto.get(i).observacionProducto.length > 0) {
                        itemPedido.descripcion =
                            itemPedido.descripcion + "\n" + listaProducto.get(i).observacionProducto
                    }
                }

                itemPedido.cantidad = listaProducto.get(i).cantidad.st
                cadena = cadena + itemPedido.cadenaPedido()
                cadena.length
                i++
            } else {
                pedidoImpresora = PedidoImpresora()
                pedidoImpresora.idArea = tempIdArea
                pedidoImpresora.cadenaPedido = cadena
                pedidoImpresora.impresoraRed = impresoraRed
                lista.add(pedidoImpresora)
                tempIdArea = listaProducto.get(i).areaProduccion.idArea
                cadena = ""
                impresoraRed = mImpresora()
                impresoraRed.IP = listaProducto.get(i).areaProduccion.impresora.IP
                impresoraRed.puerto = listaProducto.get(i).areaProduccion.impresora.puerto
                impresoraRed.tipoImpresora =
                    listaProducto.get(i).areaProduccion.impresora.tipoImpresora
                impresoraRed.macAddress = listaProducto.get(i).areaProduccion.impresora.macAddress
            }
        }
        pedidoImpresora = PedidoImpresora()
        pedidoImpresora.cadenaPedido = cadena
        pedidoImpresora.impresoraRed = impresoraRed
        lista.add(pedidoImpresora)

        return lista

    }

    fun AnulacionPedidoAreasProduccion(idCabeceraPedido: Int, cabeceraPedido: mCabeceraPedido) {
        var li =
            BdConnectionSql.getSinglentonInstance().ObtenerIdentificadorPedido(idCabeceraPedido)
        cabeceraPedido.numPedido = li.get(0)
        cabeceraPedido.fechaReserva = li.get(1)
        cabeceraPedido.denominacionCliente = li.get(2)
        cabeceraPedido.nombreVendedor = li.get(3)
        cabeceraPedido.identificadorPedido = li.get(4)
        cabeceraPedido.observacion = li.get(5)

        if (!cabeceraPedido.numPedido.equals("")) {

            var listaP = BdConnectionSql.getSinglentonInstance()
                .ObtenerAreasImpresionProductosPedidoAnulacion(idCabeceraPedido)
            if (listaP.size > 0) {

                var ca = TextoCabeceraPedidoAnulacion(cabeceraPedido)
                var listado = obtenerTextoPedidos(ArrayList(listaP))
                val impresionRed = cImpresionRed()
                listado.size
                listado.forEach {
                    if (it.impresoraRed.tipoImpresora.equals("R")) {
                        impresionRed.ImpresionPedido(
                            it.impresoraRed.IP,
                            it.impresoraRed.puerto,
                            it.cadenaPedido,
                            ca
                        )
                    }
                }
                listado.clear()
            }
        }
    }

    private fun TextoCabeceraPedidoAnulacion(cabeceraPedido: mCabeceraPedido): String {
        var texto: String = ""
        texto = "         Anulación Pedido\n"
        texto = texto + "                 " + cabeceraPedido.numPedido + "\n"

        texto = texto + " Fecha Anulacion : " + cabeceraPedido.fechaReserva + "\n"
        texto = texto + " Atendido por: " + cabeceraPedido.nombreVendedor + "\n"
        texto = texto + " Cliente: " + cabeceraPedido.denominacionCliente + "\n"
        texto = texto + " Identificador: " + cabeceraPedido.identificadorPedido + "\n"
        texto = texto + " Observacion: " + cabeceraPedido.observacion + "\n"
        return texto
    }

    fun EliminarPedido(idCabeceraPedido: Int) {

        controladorProcesoCargar.IniciarDialogCarga("Eliminando Pedido")
         GlobalScope.launch  {


            var b =
                iPedidoRespository.GetEstadoPedido(codeCia, "2", idCabeceraPedido).execute().body()
            var r = 0

            if (b == null) {
                b = false
            }
            if (b) {
                val sol = SolicitudEnvio<Int>(
                    codeCia,
                    "2",
                    idCabeceraPedido,
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario
                )
                val result = iPedidoRespository.AnularPedido(sol).execute().body()
                r = result?.codeResult ?: 0
            }
           launch(Dispatchers.Main){
                controladorProcesoCargar.FinalizarDialogCarga()
                if (b) {
                    if (r > 0) {
                        listenerEliminarPedido?.EliminarPedidoRespuesta()
                    } else {
                        listenerEliminarPedido?.ErrorEliminar()
                    }
                } else {
                    listenerEliminarPedido?.PedidoNoDisponible()
                }
            }
        }
    }

    fun BuscarProductoCodigoBarra(
        barCode: String,
        idCabeceraPedido: Int,
        idDetallePedido: Int,
        idProducto: Int,
        MetodoGuardar: String
    ) {

         GlobalScope.launch  {
            var r = BdConnectionSql.getSinglentonInstance()
                .AgregarProductoCodigoBarra(
                    barCode,
                    idCabeceraPedido,
                    idDetallePedido, idProducto, MetodoGuardar
                )
           launch(Dispatchers.Main){
                if (r.codeRespuesta == 100) {
                    resultadoBusquedaProductoCodigoBarra?.ExisteProducto(r.productoEnVenta)
                } else if (r.codeRespuesta == 99) {
                    resultadoBusquedaProductoCodigoBarra?.ErrorBusquedaProducto(r.mensaje)
                }
            }
        }

    }

    fun obtenerTextoPedidoOrden(listaProducto: ArrayList<ProductoEnVenta>): ArrayList<PedidoImpresora> {
        var cadenaMod: String? = ""
        var tempCombo = 0
        var cadena = ""
        var lista = ArrayList<PedidoImpresora>()
        val obj = listaProducto[0]
        val tempIdArea = obj.areaProduccion.idArea
        var i = 0
        val len = listaProducto.size
        var impresoraRed = mImpresora()
        val obj2 = listaProducto[0]
        impresoraRed.idArea = obj2.areaProduccion.idArea
        val obj3 = listaProducto[0]
        impresoraRed.IP = obj3.areaProduccion.impresora.IP
        val obj4 = listaProducto[0]
        impresoraRed.puerto = obj4.areaProduccion.impresora.puerto
        val obj5 = listaProducto[0]
        impresoraRed.tipoImpresora = obj5.areaProduccion.impresora.tipoImpresora
        val obj6 = listaProducto[0]
        impresoraRed.macAddress = obj6.areaProduccion.impresora.macAddress
        val obj7 = listaProducto[0]
        var tempIdArea2 = obj7.areaProduccion.idArea
        while (i < len) {
            val obj8 = listaProducto[i]
            if (tempIdArea2 == obj8.areaProduccion.idArea) {
                val obj9 = listaProducto[i]
                if (obj9.isEsDetallePack) {
                    if (tempCombo == 0) {
                        val obj10 = listaProducto[i]
                        tempCombo = obj10.idProductoPadre
                    }
                    val obj11 = listaProducto[i]
                    if (tempCombo == obj11.idProductoPadre) {
                        val sb = StringBuilder()
                        sb.append(cadena)
                        var str = " "
                        var obj12 = listaProducto[i]
                        var accessfUnidadesDec = obj12.cantidad.toInt().toString()
                        sb.append(CompletarCaracteresDerecha(str, 3, accessfUnidadesDec))
                        val obj13 = listaProducto[i]
                        val codigoProducto = obj13.codigoProducto
                        sb.append(CompletarCaracteresDerecha(" ", 10, codigoProducto))
                        cadena = sb.toString()
                        val obj14 = listaProducto[i]
                        val descripcionModificador = obj14.descripcionModificador
                        if (descripcionModificador == null) {
                            throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
                        } else if (descripcionModificador.trim().length > 0) {
                            val sb2 = StringBuilder()
                            sb2.append(cadenaMod)
                            sb2.append("\n")
                            val obj15 = listaProducto[i]
                            sb2.append(obj15.codigoProducto)
                            sb2.append(" ->")
                            val obj16 = listaProducto[i]
                            sb2.append(obj16.descripcionModificador)
                            cadenaMod = sb2.toString()
                        }
                    } else if (cadenaMod != null) {
                        if (cadenaMod.trim().length > 0) {
                            val sb3 = StringBuilder()
                            sb3.append(cadena)
                            sb3.append("\n")
                            sb3.append(cadenaMod)
                            cadena = sb3.toString()
                        }
                        i--
                        val sb4 = StringBuilder()
                        sb4.append(cadena)
                        sb4.append("\n")
                        cadena = sb4.toString()
                        cadenaMod = ""
                        tempCombo = 0
                    } else {
                        throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
                    }
                } else {
                    val sb5 = StringBuilder()
                    sb5.append(cadena)
                    sb5.append("\n")
                    val str2 = " "
                    val obj17 = listaProducto[i]
                    val accessfUnidadesDec2 = obj17.cantidad.toInt().toString()

                    sb5.append(CompletarCaracteresDerecha(str2, 3, accessfUnidadesDec2))
                    val obj18 = listaProducto[i]
                    sb5.append(obj18.codigoProducto)
                    cadena = sb5.toString()
                    val obj19 = listaProducto[i]
                    val descripcionVariante = obj19.descripcionVariante
                    if (descripcionVariante != null) {
                        if (descripcionVariante.trim().length > 0) {
                            val sb6 = StringBuilder()
                            sb6.append(cadena)
                            sb6.append("\n")
                            val obj20 = listaProducto[i]
                            val descripcionVariante2 = obj20.descripcionVariante
                            if (descripcionVariante2 != null) {
                                sb6.append(descripcionVariante2.trim())
                                cadena = sb6.toString()
                            } else {
                                throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
                            }
                        }
                        val obj21 = listaProducto[i]
                        val descripcionModificador2 = obj21.descripcionModificador
                        if (descripcionModificador2 != null) {
                            if (descripcionModificador2.trim().length > 0) {
                                val sb7 = StringBuilder()
                                sb7.append(cadena)
                                sb7.append("\n")
                                val obj22 = listaProducto[i]
                                val descripcionModificador3 = obj22.descripcionModificador
                                if (descripcionModificador3 != null) {
                                    sb7.append(descripcionModificador3.trim())
                                    cadena = sb7.toString()
                                } else {
                                    throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
                                }
                            }
                            val obj23 = listaProducto[i]
                            val observacionProducto = obj23.observacionProducto
                            if (observacionProducto == null) {
                                throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
                            } else if (observacionProducto.trim().length > 0) {
                                val sb8 = StringBuilder()
                                sb8.append(cadena)
                                sb8.append("\n")
                                val obj24 = listaProducto[i]
                                val observacionProducto2 = obj24.observacionProducto
                                if (observacionProducto2 != null) {
                                    sb8.append(observacionProducto2.trim())
                                    cadena = sb8.toString()
                                } else {
                                    throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
                                }
                            }
                        } else {
                            throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
                        }
                    } else {
                        throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
                    }
                }
                i++
            } else {
                var pedidoImpresora = PedidoImpresora()
                pedidoImpresora.idArea = tempIdArea2
                pedidoImpresora.cadenaPedido = cadena
                pedidoImpresora.impresoraRed = impresoraRed
                lista.add(pedidoImpresora)
                var obj25 = listaProducto[i]
                tempIdArea2 = obj25.areaProduccion.idArea
                cadena = ""
                impresoraRed = mImpresora()
                var obj26 = listaProducto[i]
                impresoraRed.IP = obj26.areaProduccion.impresora.IP
                var obj27 = listaProducto[i]
                impresoraRed.puerto = obj27.areaProduccion.impresora.puerto
                var obj28 = listaProducto[i]
                impresoraRed.tipoImpresora = obj28.areaProduccion.impresora.tipoImpresora
                val obj29 = listaProducto[i]
                impresoraRed.macAddress = obj29.areaProduccion.impresora.macAddress
            }
        }
        val pedidoImpresora2 = PedidoImpresora()
        if (cadenaMod != null) {
            if (cadenaMod.trim().length > 0) {
                val sb9 = StringBuilder()
                sb9.append(cadena)
                sb9.append("\n")
                sb9.append(cadenaMod)
                cadena = sb9.toString()
            }
            val sb10 = StringBuilder()
            sb10.append(cadena)
            sb10.append("\n")
            pedidoImpresora2.cadenaPedido = sb10.toString()
            pedidoImpresora2.impresoraRed = impresoraRed
            lista.add(pedidoImpresora2)
            return lista
        }
        throw TypeCastException("null cannot be cast to non-null type kotlin.CharSequence")
    }

    interface IUpdateInfo {

        fun ResultUpdateOk(mensaje: String)
        fun ResultError(mensaje: String)
    }

    fun ActualizaPedidoProductoUnico(pedido: Pedido, iUpdateInfo: IUpdateInfo) {

         GlobalScope.launch  {
            val result =
                BdConnectionSql.getSinglentonInstance().ActualizarPedidoProductoUnico(pedido)
           launch(Dispatchers.Main){

                if (result.codeResult == 200) {
                    iUpdateInfo.ResultUpdateOk(result.messageResult)
                } else {
                    iUpdateInfo.ResultError(result.messageResult)
                }
            }
        }

    }

}

private val Float.st: String
    get() {

        return String.format("%.1f", this)
    }