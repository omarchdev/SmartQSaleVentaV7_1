package com.omarchdev.smartqsale.smartqsaleventas.API
import com.omarchdev.smartqsale.smartqsaleventas.Model.ConnectionString
import retrofit2.Call;
import retrofit2.http.GET

interface InterfaceCallApi {

    @GET("api/startApp/GetDataInit")
    fun ReceivedConnInit():Call<ConnectionString>

}