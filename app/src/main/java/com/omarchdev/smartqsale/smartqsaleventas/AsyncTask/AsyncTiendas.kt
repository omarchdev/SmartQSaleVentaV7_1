package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.content.Context
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTienda
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncTiendas{


    var listenerRecuperarTiendas:ListenerRecuperarTiendas?=null
    var listenerConfigTienda:ListenerConfigTienda?=null
    var controladorProcesoCargar:ControladorProcesoCargar?=null

    interface ListenerRecuperarTiendas{

        fun TiendasRecuperadas(Tiendas:List<mTienda>?)
        fun ErrorTiendas()

    }

    interface ListenerConfigTienda{

        fun IngresarTienda()

    }
    fun ObtenerConfigTienda(idTienda:Int,context: Context){
    /*    controladorProcesoCargar= ControladorProcesoCargar(context)
        controladorProcesoCargar?.IniciarDialogCarga("Ingresando a tienda")*/
         GlobalScope.launch  {
            var respuesta=BdConnectionSql.getSinglentonInstance().ObtenerConfigTiendaAdmin1(idTienda)
           launch(Dispatchers.Main){
            //    controladorProcesoCargar?.FinalizarDialogCarga()
                if(respuesta==100.toByte()){
                    listenerConfigTienda?.IngresarTienda()
                }

            }
        }

    }

    fun ObtenerTiendas(listenerRecuperarTiendas: ListenerRecuperarTiendas?){

        var Tiendas:List<mTienda>?=Constantes.Tiendas.tiendaList
         GlobalScope.launch  {
           launch(Dispatchers.Main){
                if(Tiendas!=null){
                    listenerRecuperarTiendas?.TiendasRecuperadas(Tiendas)
                }else{
                    listenerRecuperarTiendas?.ErrorTiendas()
                }

            }

        }

    }

    interface ListenerTiendaResult{

        fun ResultadoTienda(tienda: mTienda)

    }

    var listenerTiendaResult:ListenerTiendaResult?=null
    fun ObtenerTiendaId(id:Int){
        controladorProcesoCargar?.IniciarDialogCarga("Obteniendo tienda")
         GlobalScope.launch  {
            var tienda=BdConnectionSql.getSinglentonInstance().ObtenerTiendaId(id)
           launch(Dispatchers.Main){
                controladorProcesoCargar?.FinalizarDialogCarga()
                listenerTiendaResult?.ResultadoTienda(tienda)
            }
        }

    }
    interface ListenerGuardarTienda{

        fun ErrorGuardar()
        fun ExitoGuardar()
        fun ActualizarExito()

    }
    var listenerGuardarTienda:ListenerGuardarTienda?=null
    fun AgregarTienda(tienda:mTienda){
        controladorProcesoCargar?.IniciarDialogCarga("Creando nueva tienda")
         GlobalScope.launch  {
             var tienda=BdConnectionSql.getSinglentonInstance().GuardarTienda(tienda)
           launch(Dispatchers.Main){
                controladorProcesoCargar?.FinalizarDialogCarga()
                if(tienda.idTienda==99){
                    listenerGuardarTienda?.ErrorGuardar()
                }else{
                    listenerGuardarTienda?.ExitoGuardar()
                }
            }

        }

    }

    fun ActualizarTienda(m:mTienda){
        controladorProcesoCargar?.IniciarDialogCarga("Actualizando datos de la tienda")
         GlobalScope.launch  {
            var tienda=BdConnectionSql.getSinglentonInstance().ActualizarTienda(m)

           launch(Dispatchers.Main){
                controladorProcesoCargar?.FinalizarDialogCarga()
                if(tienda.idTienda!=-99){
                    listenerGuardarTienda?.ActualizarExito()
                }else{
                    listenerGuardarTienda?.ErrorGuardar()
                }
            }
        }

    }
}