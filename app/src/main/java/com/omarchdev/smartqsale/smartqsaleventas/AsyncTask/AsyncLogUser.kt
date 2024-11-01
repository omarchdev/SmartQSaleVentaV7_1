package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.os.Handler
import android.os.Looper
import com.omarchdev.smartqsale.smartqsaleventas.API.ApiConn
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.SesionUsuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AsyncLogUser() {


    var resultLogPrincipal:ResultLogPrincipal?=null
    var resultFistLogin:ResultFirstLogin?=null
    val sesionUsuario=SesionUsuario()
    val apiConn= ApiConn()

    interface ResultFirstLogin{

        fun LogExitoFirst()
        fun LogErrorFirst(mensaje:String)

    }
    interface ResultLogPrincipal{

        fun LogExito()
        fun LogError(mensaje:String)
    }

     var resultLogPin:ResultLogPin?=null
     interface ResultLogPin{

         fun PinLogExito()
         fun PinLogExitoAccesoDirecto()
         fun PinLogError(mensaje:String)

     }

    interface IStarConnection{

        fun IngresarApp()
        fun ErrorConexion(mensaje:String)
        fun Actualizar(link:String,name:String,extend:String,mensaje:String)

    }

    var iStarConnection:IStarConnection?=null
    // Primera funcion para crear las conexiones
    fun StartSERVICEAPP(version:Int,packageName: String){

        apiConn.GetConnInit()
        apiConn.interfaceApiConnStart=object:ApiConn.InterfaceApiConnStart{
            override fun ReceivedStringConn(connString: String) {
                Constantes.UrlConnectionCreate.urlConnection=connString.replace(" ","")
                iStarConnection?.IngresarApp()
            }

            override fun ConnInitError() {
                iStarConnection?.ErrorConexion("Existe un inconveniente al conectar la apliación.Intente en otro momento")
            }

        }
        /*
         GlobalScope.launch  {
            var resultConnection= BdConnectionSql.getSinglentonInstance().StarApp(version,packageName)
           launch(Dispatchers.Main){
                when( resultConnection.codeResult)
                {
                     200->{
                        Constantes.bdConstantesCreate.url =resultConnection.host
                        Constantes.bdConstantesCreate.db = resultConnection.base
                        Constantes.bdConstantesCreate.user = resultConnection.user
                        Constantes.bdConstantesCreate.password = resultConnection.connp
                        Constantes.UrlConnectionCreate.urlConnection = "jdbc:jtds:sqlserver://" + Constantes.bdConstantesCreate.url +
                                ";databaseName=" + Constantes.bdConstantesCreate.db + ";user=" + Constantes.bdConstantesCreate.user +
                                ";password=" + Constantes.bdConstantesCreate.password + ";"
                            iStarConnection?.IngresarApp()
                    }
                    100->{
                        iStarConnection?.Actualizar(resultConnection.link,resultConnection.name,resultConnection.extend,resultConnection.messageResult)
                    }
                    99->{
                        iStarConnection?.ErrorConexion(resultConnection.messageResult)
                    }
                }
            }
        }
        */
    }


    fun FirstLogUser(user:String,pass:String){


         GlobalScope.launch {

        }

    }


    private val bd=BdConnectionSql.getSinglentonInstance()
    fun LogEmpresa(user:String,pass:String){

         GlobalScope.launch  {
            var logUser= bd.LoginEmpresaConnection(user,pass)
               if(logUser.codeResult==200){

                    Constantes.Licencia.idLicencia=logUser.idLincencia
                    Constantes.Empresa.idEmpresa=logUser.idCompany
                    Constantes.Empresa.nombreTienda=logUser.NombreComercial
                    Constantes.Empresa.NumRuc=logUser.NumRuc
                    Constantes.Empresa.Razon_Social=logUser.RazonSocial
                    Constantes.Empresa.cDireccion=logUser.direccion
                    Constantes.Tienda.NumTiendas=logUser.numTiendas
                    Constantes.bdConstantes.url = logUser.host
                    Constantes.bdConstantes.user = logUser.userDbName
                    Constantes.bdConstantes.db =logUser.dbName
                    Constantes.bdConstantes.password =logUser.passDbName
                    Constantes.UrlConnection.urlConnection = "jdbc:jtds:sqlserver://" + Constantes.bdConstantes.url +
                            ";databaseName=" + Constantes.bdConstantes.db + ";user=" + Constantes.bdConstantes.user + ";password=" + Constantes.bdConstantes.password + ";"

                    bd.ConectarInstanciaPrincipal()
                    bd.isSeConectoVentas=true
               }
           launch(Dispatchers.Main){
                if(logUser.codeResult==200){
                    resultLogPrincipal?.LogExito()
                }else{
                    resultLogPrincipal?.LogError(logUser.mensajeResult)
                }
            }
        }


    }

    fun PrimerLog(email:String,pass: String,pin:String,imei:String,marca:String,modelo:String,versionAndroid:String,idUser:Int=0,permitirReingreso:Boolean=false){

         GlobalScope.launch  {
            var logUser = bd.LoginEmpresaConnection(email, pass)
            if (logUser.codeResult == 200) {

                Constantes.Licencia.idLicencia = logUser.idLincencia
                Constantes.Empresa.idEmpresa = logUser.idCompany
                Constantes.Empresa.nombreTienda = logUser.NombreComercial
                Constantes.Empresa.NumRuc = logUser.NumRuc
                Constantes.Empresa.Razon_Social = logUser.RazonSocial
                Constantes.Empresa.cDireccion = logUser.direccion
                Constantes.Tienda.NumTiendas = logUser.numTiendas
                Constantes.bdConstantes.url = logUser.host
                Constantes.bdConstantes.user = logUser.userDbName
                Constantes.bdConstantes.db = logUser.dbName
                Constantes.bdConstantes.password = logUser.passDbName
                Constantes.UrlConnection.urlConnection = "jdbc:jtds:sqlserver://" + Constantes.bdConstantes.url +
                        ";databaseName=" + Constantes.bdConstantes.db + ";user=" + Constantes.bdConstantes.user + ";password=" + Constantes.bdConstantes.password + ";"

                bd.ConectarInstanciaPrincipal()
                bd.isSeConectoVentas = true
                var r = bd.LogPinUserV2(pin, imei, marca, modelo, versionAndroid, idUser, permitirReingreso)

               launch(Dispatchers.Main){
                    if(r.code==100){
                            resultFistLogin?.LogExitoFirst()

                    }else{
                            resultFistLogin?.LogErrorFirst("Error al ingresar")
                    }
                }
            }
        }

    }

    fun LogPin(pin:String,imei:String,marca:String,modelo:String,versionAndroid:String,idUser:Int=0,permitirReingreso:Boolean=false){
        val handler = Handler(Looper.getMainLooper())

        GlobalScope.launch {

            var r=bd.LogPinUserV2(pin,imei,marca,modelo,versionAndroid,idUser,permitirReingreso)
           launch(Dispatchers.Main){

                   if(r.code==100){
                       if(!r.accesoDirecto){
                           resultLogPin?.PinLogExito()
                       }else{
                           resultLogPin?.PinLogExitoAccesoDirecto()
                       }
                   }else{
                       resultLogPin?.PinLogError(r.messageResult)
                   }


            }
        }
    }

}
