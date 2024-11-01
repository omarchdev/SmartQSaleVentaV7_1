package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMovAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterTransferencia

class DialogSelectTransferencia : DialogFragment() {

    var adapterTransferencia = RvAdapterTransferencia()

    var idAlmacen = 0
    private lateinit var listenerMovSeleccionado: ListenerMovSeleccionado

    public interface ListenerMovSeleccionado {

        fun MovSeleccionado(movAlmacen: mMovAlmacen)


    }

    fun setData(idAlmacen: Int) {
        this.idAlmacen = idAlmacen
    }

    public fun setListenerMovSeleccionado(listenerMovSeleccionado: ListenerMovSeleccionado) {

        this.listenerMovSeleccionado = listenerMovSeleccionado

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val asyncAlmacen = AsyncAlmacenes()
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_listado_transferencias, null)
        val dialog = AlertDialog.Builder(activity)
        val rv = view.findViewById<RecyclerView>(R.id.rvListadoTranf)
        dialog.setView(view)

        rv.adapter = adapterTransferencia
        rv.layoutManager = LinearLayoutManager(activity)

        asyncAlmacen.ObtenerTransferenciasAlmacen(idAlmacen)
        asyncAlmacen.setListenerTransferenciasAlmacen(object : AsyncAlmacenes.ListenerTransferenciasAlmacen {

            override fun ListadoTransferencias(mMovAlmacenList: MutableList<mMovAlmacen>?) {
                adapterTransferencia.setTransferencias(ArrayList(mMovAlmacenList!!))

            }

            override fun ErrorProc() {

            }

            override fun ErrorConnection() {

            }

        })
        adapterTransferencia.setListenerTrasferenciasAlmacen(object : RvAdapterTransferencia.ListenerTrasferenciasAlmacen {
            override fun obtenerTransferenciaAlmacen(mMovAlmacen: mMovAlmacen) {
                listenerMovSeleccionado.MovSeleccionado(mMovAlmacen)
                CerrarDialog()
            }
        })
        return dialog.create()
    }

    fun CerrarDialog() {
        this.dismiss()
    }
}