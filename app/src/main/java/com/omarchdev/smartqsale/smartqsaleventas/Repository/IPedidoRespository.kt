package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import retrofit2.Call
import retrofit2.http.*

interface IPedidoRespository {

    @POST("api/pedido/AnularPedido")
    fun AnularPedido(@Body solicitudEnvio: SolicitudEnvio<Int>): Call<ProcessResult<Int>>

    @POST("api/pedido/PermitirVentaPedido")
    fun PermitirPedidoVenta(@Body solicitudEnvio: SolicitudEnvio<PedidoVentaPermite>): Call<ProcessResult<Int>>

    @POST("api/pedido/GuardarPagoTemporal")
    fun GuardarPagoTemporal(@Body solicitudEnvio: SolicitudEnvio<PagoVentaTemp>): Call<ProcessResult<Boolean>>

    @POST("api/pedido/GetPagosTemporalesPedido")
    fun GetPagosTemporalesPedido(@Body solicitudEnvio: SolicitudEnvio<Int>): Call<List<mPagosEnVenta>>

    @POST("api/pedido/GenerarVenta")
    fun GenerarVenta(@Body solicitudEnvio: SolicitudEnvio<VentaGeneracion>): Call<mRespuestaVenta>
    @POST("api/pedido/GenerarVentaV2")
    fun GenerarVentaV2(@Body solicitudEnvio: SolicitudEnvio<VentaGeneracion>): Call<mRespuestaVenta>
    @GET("api/Pedido/GetEstadoPedidoActivo")
    fun GetEstadoPedido(
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoBusqueda: String,
        @Query("idPedido") idPedido: Int
    ): Call<Boolean>


    @GET("api/Pedido/GetEstadoFlujoPagoPedido")
    fun GetEstadoFlujoPagoPedido(
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoBusqueda: String,
        @Query("idPedido") idPedido: Int
    ): Call<List<EstadoPagoEnPedido>>


    @GET("api/Pedido/GetEstadoFlujoPedido")
    fun GetEstadoFlujoPedido(
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoBusqueda: String,
        @Query("idPedido") idPedido: Int
    ): Call<List<EstadoEntregaPedidoEnUso>>


    @GET("api/Pedido/GetDatoEntregaPedido")
    fun GetDatoEntregaPedido(
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoBusqueda: String,
        @Query("idPedido") idPedido: Int
    ): Call<EntregaPedidoInfo>



    @GET("api/Pedido/GetDetallePedidoId")
    fun GetDetallePedidoId(
        @Header("codeCia") codeCia: String,
        @Header("tipoBusqueda") tipoBusqueda: String,
        @Query("idPedido") idPedido: Int
    ): Call<List<ProductoEnVenta>>





    @GET("api/Pedido/GetPedidoId")
    fun GetPedidoId(
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoBusqueda: String,
        @Query("idPedido") idPedido: Int,
        @Query("idTipoZona") idTipoZona: Int,
        @Query("bPagado") pagado: Boolean
    ): Call<Pedido>



    @POST("api/Pedido/GuardarProductoVariante")
    fun GuardarProductoVariante(@Body solicitudVariante: SolicitudEnvio<VarianteBusqueda>): Call<ProductoEnVenta>


    @POST("api/Pedido/CambioPedidoActualPorReservado")
    fun CambioPedidoActualPorReservado(@Body solicitudCambioEstadoPedido: SolicitudEnvio<PedidoCambioEstado>): Call<Boolean>


    @POST("api/Pedido/EliminarProductoPack")
    fun EliminarProductoPack(@Body solicitudCambioEstadoPedido: SolicitudEnvio<ProductoEnVenta>): Call<ProductoEnVenta>


    @GET("api/pedido/GetPedidosEnReservaV2")
    fun GetPedidosReserva(
        @Query("codeCia") codeCia: String,
        @Query("tipoBusqueda") tipoBusqueda: String,

        @Query("fechaInicio") fechaInicio: Int,
        @Query("fechaFin") fechaFin: Int,

        @Query("idTipoZona") idTipoZona: Int,
        @Query("tipoEstado") tipoEstado: String,
        @Query("nroPedido") nroPedido: String,
        @Query("codeCliente") codeCliente: Int
    ): Call<List<mCabeceraPedido>>

    @DELETE("api/pedido/ActItemDetallePedido")
    fun EliminaDetallePedido(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("idCabeceraPedido") idCabeceraPedido: Int,
        @Query("idDetallePedido") idDetallePedido: Int
    ): Call<Int>

    @GET("api/pedido/VerificaStockDisponible")
    fun VerificaStockDisponible(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("idCabeceraPedido") idCabeceraPedido: Int
    ): Call<List<ProductoEnVenta>>

    @GET("api/pedido/GetAforoDisponible")
    fun GetAforoDisponible(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String
    ): Call<Int>

    @GET("api/pedido/GetResumenPedidoReserva")
    fun GetResumenPedidoReserva(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String
    ): Call<List<ResumenPedido>>

    @POST("api/Pedido/GuardarProductoTiempoPedido")
    fun GuardarProductoTiempoPedido(@Body sol: SolicitudEnvio<ProductoTiempo>): Call<ResultProcessData<ProductoEnVenta>>

    @GET("api/Pedido/GetIdZonaServicioReservado")
    fun GetIdZonaServicioReservado(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("descripcion") descripcion: String
    ): Call<Int>


    @POST("api/ZonaServicio/RegistrarZonaServicioPedido")
    fun RegistrarZonaServicioPedido(@Body sol: SolicitudEnvio<mCabeceraPedido>): Call<ResZonaServicio>

    //   SolicitudEnvio<SolModificadorProductoDetallePedido>

    @POST("api/Pedido/GuardarProductoModificadorDetallePedido")
    fun GuardarProductoModificadorDetallePedido(@Body sol: SolicitudEnvio<SolModificadorProductoDetallePedido>): Call<ProductoEnVenta>


    @POST("api/Pedido/EditarProductoTiempoPedido")
    fun EditarProductoTiempoPedido(@Body sol: SolicitudEnvio<ProductoTiempoSol>): Call<ProductoEnVenta>

    @POST("api/Pedido/ModificarEstadoPedido")
    fun ModificarEstadoPedido(@Body sol: SolicitudEnvio<InfoGuardadoPedido>): Call<ResultProcessData<List<ProductoEnVenta>>>


    @POST("api/Pedido/IngresaProductoPedido")
    fun IngresaProductoPedido(@Body solicitudVariante: SolicitudEnvio<ProductoEnVenta>): Call<ProductoEnVenta>


    @PUT("api/Pedido/GuardarDescuentoPedido")
    fun GuardarDescuentoPedido(@Body sol: SolicitudEnvio<DescuentoSol>): Call<Byte>

    @PUT("api/Pedido/GuardarVendedorCabeceraPedido")
    fun GuardarVendedorCabeceraPedido(@Body sol: SolicitudEnvio<VendedorSol>): Call<Byte>


    @PUT("api/Pedido/GuardarClienteCabeceraPedido")
    fun GuardarClienteCabeceraPedido(@Body sol: SolicitudEnvio<ClientePedido>): Call<Byte>



    @PUT("api/Pedido/EditarCantidadProducto")
    fun EditarCantidadProducto(@Body solicitudVariante: SolicitudEnvio<ProductoEnVenta>,@Header("idCabeceraPedido") idCabeceraPedido:Int): Call<ProductoEnVenta>





    @GET("api/Pedido/ObtenerIdentificadorPedido")
    fun ObtenerIdentificadorPedido(
        @Query("codeCia") codeCia: String,
        @Query("tipoConsulta") codeTipo: String,
        @Header("idPedido") idPedido: Int
    ): Call<List<String>>






    @POST("api/Pedido/ActualizarEstadoImpresionPedido")
    fun ActualizarEstadoImpresionPedido(
        @Body lista:List<ConfirmacionImpresion>,
        @Header("codeCia") codeCia: String,
        @Header("tipoConsulta") codeTipo: String,
        @Header("idCabeceraPedido") idCabeceraPedido: Int
    ): Call<Byte>






}