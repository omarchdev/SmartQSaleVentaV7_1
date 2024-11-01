package com.omarchdev.smartqsale.smartqsaleventas.Network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

class NetworkUtil {

    val context:Context?
    val manager: ConnectivityManager?
    constructor(context:Context?){
        this.context=context
        manager=context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }

    fun statusConnection():Boolean{
        return manager?.activeNetworkInfo?.isConnected != null && manager!!.activeNetworkInfo!!.isConnected
   }


    fun nameConnection():String{
        var nombre=""

        if(manager!=null)
            if(manager.activeNetworkInfo!=null)
                nombre= manager.activeNetworkInfo!!.extraInfo


        return nombre
    }

    private fun typeConnection():String{

        var tipo=""
        if(manager?.activeNetworkInfo!=null)
        when(manager?.activeNetworkInfo!!.type){
            ConnectivityManager.TYPE_WIFI->tipo="WIFI"
            ConnectivityManager.TYPE_MOBILE->tipo="RED DE DATOS"
        }
        return tipo

    }


    fun VerificarConexion(){
        var resultado=false
        var nombre=""
        var estado=""
        var tipo=""
        if(manager!=null){
            if(statusConnection()){
                estado="Conectado"
                resultado=true
            }else{
                estado="Desconectado"
                resultado=false
            }

            tipo=typeConnection()
            if(manager.activeNetworkInfo!=null)
            nombre= manager.activeNetworkInfo!!.extraInfo
        }
        Log.e("Estado Red","Estado -> $estado    , Nombre -> $nombre      , Tipo -> $tipo   ")

    }

}