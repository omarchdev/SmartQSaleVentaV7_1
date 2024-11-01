package com.omarchdev.smartqsale.smartqsaleventas.Repository

import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import retrofit2.Call
import retrofit2.http.*

interface ICategoriaRepository {

    @GET("api/Categoria/categoriaVentasLista")
    fun GetCategoriaVentas(
        @Query("TipoMovimientoPedido") tipoMov: String,
        @Query("nombreCia") nombreCia: String
    ): Call<List<mCategoriaProductos>>




    @GET("api/Categoria/categoriaVentasLista")
    fun GetCategoriasPrecio(
        @Query("id")id:Int,
        @Query("tipoConsulta") tipoMov: String,
        @Query("codeCia") nombreCia: String
    ): Call<List<CategoriaPack>>






    @PUT("api/Categoria/EditarCategoria")
    fun EditarCategoria(@Body cat: SolicitudEnvio<mCategoriaProductos>): Call<Int>

    @PUT("api/Categoria/ActualizaCategoriaDefecto")
    fun ActualizaCategoriaDefecto(@Body cat: SolicitudEnvio<CategoriaDefault>): Call<ResultProcces>


    @POST("api/Categoria/AgregarCategoria")
    fun AgregarCategoria(@Body cat: SolicitudEnvio<String>): Call<Byte>


    @DELETE("api/Categoria/EliminarCategoria")
    fun EliminarCategoria(
        @Query("tipoMovimientoPedido") tipoMov: String,
        @Query("nombreCia") nombreCia: String,
        @Query("idCategoria") idCategoria: Int,
        @Query("idTerminal") idTerminal: Int,
        @Query("idUsuario") idUsuario: Int,
        ): Call<RepuestaEliminar>


}