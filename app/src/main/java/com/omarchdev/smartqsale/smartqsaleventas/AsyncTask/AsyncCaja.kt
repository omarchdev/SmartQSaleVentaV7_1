package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.RetornoApertura
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICierreRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.math.BigDecimal

/**
 * Created by OMAR CHH on 09/02/2018.
 */
class AsyncCaja(private val context: Context?) {
    var aperturaCaja: ListenerAperturaCaja? = null
    var bdConnectionSql = BdConnectionSql.getSinglentonInstance()
    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iCierreRepository = retro.create(ICierreRepository::class.java)
    private var Monto: BigDecimal
    private val dialogCargaAsync: DialogCargaAsync
    private var controladorProcesoCargar: ControladorProcesoCargar? = null
    fun setListenerAperturaCaja(aperturaCaja: ListenerAperturaCaja?) {
        this.aperturaCaja = aperturaCaja
    }

    fun AperturarCaja(monto: BigDecimal) {
        Monto = monto
        AperturaCargaAsync().execute(Monto)
    }

    fun ObtenerIdCierreCaja() {
        ObtenerUltimoCierreDisponiblePorCaja().execute()
    }

    fun VerificarCajaAbierta() {
        try {
            VerificarCierreAbierto().execute()
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun CerrarCaja(idCierre: Int) {
        CerrarCajaAsync().execute(idCierre)
    }

    interface ListenerAperturaCaja {
        fun ConfirmacionAperturaCaja()
        fun ExisteCierre(Cierre: mCierre?)
        fun AperturarCaja()
        fun ConfirmarCierreCaja()
        fun ExisteCajaAbiertaDisponible()
        fun CajaCerrada()
        fun ErrorEncontrarCaja()
    }

    private inner class CerrarCajaAsync : AsyncTask<Int?, Void?, Byte>() {
        var dialog: Dialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            dialog = dialogCargaAsync.getDialogCarga("Cerrando caja")
            dialog!!.show()
        }

        override fun onPostExecute(aByte: Byte) {
            super.onPostExecute(aByte)
            dialog!!.dismiss()
            if (aByte.toInt() == 100) {
                Toast.makeText(context, "Se cerro la caja", Toast.LENGTH_SHORT).show()
                aperturaCaja!!.ConfirmarCierreCaja()
            } else if (aByte.toInt() == 99) {
                Toast.makeText(context, "Error al cerrar", Toast.LENGTH_SHORT).show()
            } else if (aByte.toInt() == 0) {
                Toast.makeText(context, "Verificar conexion", Toast.LENGTH_SHORT).show()
            } else if (aByte.toInt() == 1) {
                Toast.makeText(context, "Error al cerrar", Toast.LENGTH_SHORT).show()
            }
        }

        override fun doInBackground(vararg integers: Int?): Byte {
            return bdConnectionSql.CerrarCaja(integers[0]!!)
        }
    }

    var listenerAbrirCaja: ListenerAbrirCaja? = null


    interface ListenerAbrirCaja {
        fun ErrorApertura()
        fun CajaAperturada(idCierre: Int)
        fun ErrorConexion()
    }

    private inner class AperturaCargaAsync : AsyncTask<BigDecimal?, Void?, RetornoApertura>() {
        override fun onPreExecute() {
            super.onPreExecute()
            if (context != null) {
                controladorProcesoCargar!!.IniciarDialogCarga("Aperturando Caja")
            }
        }

        override fun onPostExecute(retornoApertura: RetornoApertura) {
            super.onPostExecute(retornoApertura)
            if (context != null) {
                controladorProcesoCargar!!.FinalizarDialogCarga()
            }
            if (listenerAbrirCaja != null) {
                if (retornoApertura.respuesta.toInt() == 99) {
                    Toast.makeText(context, "Error al aperturar la caja", Toast.LENGTH_SHORT).show()
                } else if (retornoApertura.respuesta.toInt() == 0) {
                    Toast.makeText(context, "Verifique su conexion", Toast.LENGTH_SHORT).show()
                } else if (retornoApertura.respuesta.toInt() == 101) {
                    Toast.makeText(context, "La caja se aperturo", Toast.LENGTH_SHORT).show()
                    //     listenerAbrirCaja.CajaAperturada(retornoApertura.getIdCaja());
                    aperturaCaja!!.ConfirmacionAperturaCaja()
                }
            }
        }

        override fun doInBackground(vararg bigDecimals: BigDecimal?): RetornoApertura {
            return bdConnectionSql.aperturarCaja(bigDecimals[0])
        }
    }

    private inner class VerificarCierreAbierto : AsyncTask<Void?, Void?, mCierre>() {
        var dialog: Dialog = dialogCargaAsync.getDialogCarga("Obteniendo Caja")
        override fun onPreExecute() {
            dialog.show()
            super.onPreExecute()
        }

        override fun doInBackground(vararg voids: Void?): mCierre {
            return bdConnectionSql.ObtenerIdCierre()
        }

        override fun onPostExecute(mCierre: mCierre) {
            super.onPostExecute(mCierre)
            try {
                if (mCierre.idCierre == -2 || mCierre.idCierre == -1) {
                    aperturaCaja!!.ErrorEncontrarCaja()
                } else if (mCierre.idCierre > 0) {
                    if (mCierre.estadoCierre == "C") {
                        aperturaCaja!!.CajaCerrada()
                    } else if (mCierre.estadoCierre == "A") {
                        aperturaCaja!!.ExisteCajaAbiertaDisponible()
                    }
                } else if (mCierre.idCierre == 0) {
                    aperturaCaja!!.CajaCerrada()
                }
            } catch (e: Exception) {
                e.toString()
            }
            dialog.dismiss()
        }
    }

    private inner class ObtenerUltimoCierreDisponiblePorCaja : AsyncTask<Void?, Void?, mCierre>() {
        var dialog: Dialog = dialogCargaAsync.getDialogCarga("Obteniendo Caja")
        override fun onPreExecute() {
            dialog.show()
            super.onPreExecute()
        }

        override fun doInBackground(vararg voids: Void?): mCierre {
            return bdConnectionSql.ObtenerIdCierre()
        }

        override fun onPostExecute(cierre: mCierre) {
            super.onPostExecute(cierre)
            dialog.dismiss()
            if (aperturaCaja != null) {
                if (cierre.idCierre > 0) {
                    aperturaCaja!!.ExisteCierre(cierre)
                } else if (cierre.idCierre == 0) {
                    Toast.makeText(context, "Debe abrir caja", Toast.LENGTH_SHORT).show()
                    aperturaCaja!!.AperturarCaja()
                } else if (cierre.idCierre == -2) {
                    Toast.makeText(context, "Verifique su conexion", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var obtenerCierre: ObtenerCierre? = null

    init {
        Monto = BigDecimal(0)
        dialogCargaAsync = DialogCargaAsync(context)
        if (context != null) {
            controladorProcesoCargar = ControladorProcesoCargar(context)
        }
    }


    interface ObtenerCierre {
        fun ObtenerInfoCierre(cierre: mCierre?)
    }

    fun ObtenerDatosCierre(idCierre: Int) {
        ObtenerCierrePorId().execute(idCierre)
    }

    private inner class ObtenerCierrePorId : AsyncTask<Int?, Void?, mCierre?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            controladorProcesoCargar!!.IniciarDialogCarga("Obteniendo información")
        }

        override fun doInBackground(vararg integers: Int?): mCierre? {
            return try {
                iCierreRepository.ObtenerCierrePorId(
                    integers[0]!!,
                    BASECONN.TIPO_CONSULTA,
                    codeCia
                ).execute().body()
            } catch (e: IOException) {
                mCierre()
            }
            //      return bdConnectionSql.ObtenerCierrePorId(integers[0]);
        }

        override fun onPostExecute(mCierre: mCierre?) {
            super.onPostExecute(mCierre)
            obtenerCierre!!.ObtenerInfoCierre(mCierre)
            controladorProcesoCargar!!.FinalizarDialogCarga()
        }
    }
}