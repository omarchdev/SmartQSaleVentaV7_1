package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.util.Log
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ConnectionValid {

    var connectionStatus:ConnectionStatus?=null

    interface ConnectionStatus{
        fun ConnectionSlow()
        fun ConnectionBreak()
    }


    fun ValidConnection():HashMap<Int,Boolean>{

        val map=HashMap<Int,Boolean>()
        val startTime=System.nanoTime()
        var response=BdConnectionSql.getSinglentonInstance().VerificaConexionBd()
        val endTime=System.nanoTime()
        val secondsTask=(endTime-startTime)/1000000000
        var isConnect=false
        var isOptimun=false
        var message=""
        var mostrarMensaje=true
        if(response==200 || response==201){
            isConnect=true
            message="Su conexión a internet es muy lenta en estos momentos.Por favor verifique su conexión."
            if(secondsTask<2){
                isOptimun=true
                message=""
            }
            if(response==201){
                mostrarMensaje=false
            }
        }else{
            isConnect=false
            isOptimun=false
            message="Su sesión ha expirado en estos momentos.Se reiniciará la apliación en estos momentos."
        }
        map.put(1,isConnect)
        map.put(2,isOptimun)
   //     map.put(3,message)
        map.put(3,mostrarMensaje)
        val rtime=TimeUnit.SECONDS.convert((endTime-startTime), TimeUnit.NANOSECONDS)
        Log.i("INFOTEST_R_TIME->",rtime.toString())
        Log.i("INFOTEST_INITTIME->",startTime.toString())
        Log.i("INFOTEST_ENDTTIME->",endTime.toString())
        Log.i("INFOTEST_NANO_TIME->",(endTime-startTime).toString())
        Log.i("INFOTEST_CODERESPONSE->",response.toString())
        Log.i("INFOTEST_Tiempo->",secondsTask.toString())
        Log.i("INFOTEST_Conexion ->",isConnect.toString())
        Log.i("INFOTEST_Es optimo ->",isOptimun.toString())
        Log.i("INFOTEST_Mensaje  ->",message)
        return map
    }

    fun ValidConnectionTaks(){

        GlobalScope.launch {
            val response= ValidConnection()
            val isConnect= response[1]
            val isOptimun= response[2]
            val mostrarMensaje=response[3]
            if(isConnect!!){
                if(!isOptimun!!){
                    connectionStatus?.ConnectionSlow()
                }
            }else {
                connectionStatus?.ConnectionBreak()
            }
        }

    }

}