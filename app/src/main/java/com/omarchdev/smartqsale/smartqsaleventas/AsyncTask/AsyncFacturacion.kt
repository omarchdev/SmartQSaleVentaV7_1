package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.HttpConsultas
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.FacturaActivaController
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultadoComprobante
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDocVenta
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRespuestaVenta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncFacturacion{



    fun envioFacturActiva(id:Int){

         GlobalScope.launch {
          var r= mRespuestaVenta()
           r.cabeceraVenta= BdConnectionSql.getSinglentonInstance().GetCabeceraVenta(id)
            r.cabeceraVenta.idVenta=id
          val lista = BdConnectionSql.getSinglentonInstance().ObtenerDetalleVenta(r.cabeceraVenta.getIdVenta())
            val httpConsultas= HttpConsultas()
       //   r.cabeceraVenta.idComprobantePagoSunat=1
/*
           var result=  httpConsultas.GenerarDocumentoElectronicoNubefact(mDocVenta(r.cabeceraVenta,lista))
            BdConnectionSql.getSinglentonInstance().ActualizarCabeceraVentaIdFacturacionElectronica(id, result)*/
            val  facturaActivaController = FacturaActivaController()
            var doc=mDocVenta(r.cabeceraVenta, lista)

          var result=  facturaActivaController.EmitirComprobanteElectronicoV2(doc)

            BdConnectionSql.getSinglentonInstance().ActualizarCabeceraVentaIdFacturacionElectronica(id, result)

        }

    }

    fun Reenvio(){

           GlobalScope.launch {
                var it=16114

                var r= mRespuestaVenta()
                r.cabeceraVenta= BdConnectionSql.getSinglentonInstance().GetCabeceraVenta(it)
                r.cabeceraVenta.idVenta=it
                val lista = BdConnectionSql.getSinglentonInstance().ObtenerDetalleVenta(r.cabeceraVenta.getIdVenta())
                val httpConsultas= HttpConsultas()
                r.cabeceraVenta.idComprobantePagoSunat=1

                var result=  httpConsultas.GenerarDocumentoElectronicoNubefact(mDocVenta(r.cabeceraVenta,lista))
                BdConnectionSql.getSinglentonInstance().ActualizarCabeceraVentaIdFacturacionElectronica(it, result)
                var doc=mDocVenta(r.cabeceraVenta, lista)
            }

/*
         GlobalScope.launch {

            var listId=BdConnectionSql.getSinglentonInstance().ObtenerIdReenvio()
            listId.forEach {

                var r= mRespuestaVenta()
                r.cabeceraVenta= BdConnectionSql.getSinglentonInstance().GetCabeceraVenta(it)
                r.cabeceraVenta.idVenta=it
                val lista = BdConnectionSql.getSinglentonInstance().ObtenerDetalleVenta(r.cabeceraVenta.getIdVenta())
                val httpConsultas= HttpConsultas()
                  r.cabeceraVenta.idComprobantePagoSunat=1

                var result=  httpConsultas.GenerarDocumentoElectronicoNubefact(mDocVenta(r.cabeceraVenta,lista))
                BdConnectionSql.getSinglentonInstance().ActualizarCabeceraVentaIdFacturacionElectronica(it, result)
                 var doc=mDocVenta(r.cabeceraVenta, lista)
                val  facturaActivaController = FacturaActivaController()

                var result=  facturaActivaController.EmitirComprobanteElectronico(doc)

               BdConnectionSql.getSinglentonInstance().ActualizarCabeceraVentaIdFacturacionElectronica(it, result)
            }
        }
*/


    }

    fun AnularDoc(idDoc:Int){

         GlobalScope.launch {
            try {
                val facturaActivaController = FacturaActivaController()
                val doc = BdConnectionSql.getSinglentonInstance().AnularDocumento(idDoc, "Error en los montos")
                var r = facturaActivaController.ComunicacionBajaDocumento(doc, "Error en montos en linea")
                BdConnectionSql.getSinglentonInstance().ActualizarEstadoComunicacionBaja(idDoc, r)
            }catch (e:Exception){
                e.toString()
            }

        }
    }

    fun generarNota(idCabeceraVenta: Int,motivo:String){

         GlobalScope.launch  {
            var docVenta=BdConnectionSql.getSinglentonInstance().GenerarNota(idCabeceraVenta,motivo)


           launch(Dispatchers.Main){

            }
        }
    }

    fun EnvioNota(id:Int){

         GlobalScope.launch {
            var resultadoComprobante: ResultadoComprobante? = null
            val doc = BdConnectionSql.getSinglentonInstance().GenerarNota(id, "Error en descripciones en linea")
            if (Constantes.ConfigTienda.CodeFacturacion == Constantes.TFacturacion.cNuFactura) {
                val c = HttpConsultas()
                resultadoComprobante = c.GenerarDocumentoElectronicoNubefact(doc)
            } else if (Constantes.ConfigTienda.CodeFacturacion == Constantes.TFacturacion.cActFactura) {
                val facturaActivaController = FacturaActivaController()
                resultadoComprobante = facturaActivaController.EmitirComprobanteElectronicoV2(doc)
            }
            BdConnectionSql.getSinglentonInstance().ActualizarEstadoNotaGenerada(resultadoComprobante, id)
        }

    }

    fun envioNotaCredito(){

         GlobalScope.launch  {

            var r=BdConnectionSql.getSinglentonInstance().GenerarNotaFacturacion(2)
            val facturaActivaController=FacturaActivaController()
            var result=facturaActivaController.EmitirComprobanteElectronico(r)
            result.toString()

        }

    }

}