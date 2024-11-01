package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.math.BigDecimal

interface IProductoRepository {

    @GET("api/producto/GetProductosVentaPedido")
    fun GetProductosVenta(
        @Query("tipoConsulta") tipoMov: String,
        @Query("codeCia") nombreCia: String,
        @Query("tipoBusqueda") tipoBusqueda: Int,
        @Query("idCategoria") idCategoria: Int,
        @Query("descripcion") descripcion: String
    ): Call<List<mProduct>>



    @GET("api/producto/GenerarCodigoNumericoProducto")
    fun GenerarCodigoNumericoProducto(
        @Query("tipoConsulta") tipoMov: String,
        @Query("codeCia") nombreCia: String
    ): Call<ResponseCodProducto>






    @GET("api/producto/GetProductosConfig")
    fun GetProductosConfig(
        @Query("codeCia") codeCia: String, @Query("tipoConsulta") codeTipo: String,
        @Query("param") param: String
    ): Call<List<mProduct>>

    @GET("api/producto/ObtenerPreciosAdiccionales")
    fun ObtenerPreciosAdiccionales(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("idProducto") idProducto: Int
    ): Call<List<AdditionalPriceProduct>>


    @GET("api/producto/BuscarProductoCodigoBarra")
    fun BuscarProductoCodigoBarra(
        @Query("codeCia") codeCia: String,
        @Query("codeTipo") codeTipo: String,
        @Query("codigoBarra") codigoBarra: String,
        @Query("idCabeceraPedido") idCabeceraPedido: Int,
        @Query("idTerminal") idTerminal: Int,
        @Query("idUsuario") idUsuario: Int
    ): Call<ProductoEnVenta>

    @POST("api/producto/AgregarComboRapidoDetallePedido")
    fun AgregarComboRapidoDetallePedido(@Body sol: SolicitudEnvio<ComboRapidoSol>): Call<RespuestaProductoVenta>


    @GET("api/producto/ObtenerPackProductos")
    fun GetContenidoPack(  @Query("tipoConsulta") tipoMov: String,
                           @Query("codeCia") nombreCia: String,
                           @Query("idProducto")idProducto:Int):Call<ResultPack>

    @POST("api/producto/AgregaComboPedido")
    fun GuardarPackDetallePedido(@Body sol: SolicitudEnvio<PackElemento>):Call<PackElemento>



    @GET("api/producto/CantidadMaximaPedidoWeb")
    fun CantidadMaximaPedidoWeb(
        @Query("ciaCode") codeCia: String,
        @Query("tipoConsulta") codeTipo: String
    ): Call<BigDecimal>






}