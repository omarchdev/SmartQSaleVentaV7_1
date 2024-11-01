package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAlmacenes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ObtenerNombreTienda
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct
import com.omarchdev.smartqsale.smartqsaleventas.R


class DialogSelectAlmacen : DialogFragment(), AsyncAlmacenes.ListenerAlmacenes {

    val asyncAlmacen = AsyncAlmacenes()
    val listaAlmacenes = mutableListOf(mAlmacen())
    private lateinit var listview: ListView
    private lateinit var pbCargar: ProgressBar
    var product = mProduct()


    private lateinit var almacenSeleccionListener: AlmacenSeleccionListener


    public fun setListenerAlmacen(almacenSeleccionListener: AlmacenSeleccionListener) {
        this.almacenSeleccionListener = almacenSeleccionListener
    }

    interface AlmacenSeleccionListener {
        fun obtenerAlmacen(idAlmacen: Int, descripcion: String)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = AlertDialog.Builder(activity)
        val view: View
        view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_almacen, null)
        asyncAlmacen.ObtenerAlmacenes()
        asyncAlmacen.setListenerAlmacenes(this)
        listview = view.findViewById(R.id.listAlmacenes)
        pbCargar = view.findViewById(R.id.pbCargar)
        pbCargar.visibility = View.VISIBLE
        dialog.setView(view)
        dialog.setTitle("Seleccione un almacén")
        dialog.setNegativeButton("Salir", DialogInterface.OnClickListener { dialog, which ->
        })

        return dialog.create()

    }


    private fun clickList() {
        listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            almacenSeleccionListener.obtenerAlmacen(listaAlmacenes.get(i).idAlmacen, "${listaAlmacenes.get(i).descripcionAlmacen}\n ${ObtenerNombreTienda(listaAlmacenes.get(i).idTienda)}")
            dialog?.dismiss()

        }
    }

    override fun ObtenerBusquedaAlmacenes(almacenList: MutableList<mAlmacen>?) {
        this.listaAlmacenes.clear()
        this.listaAlmacenes.addAll(almacenList!!)
        pbCargar.visibility = View.GONE
        val nombreAlmacenes = arrayOfNulls<String>(listaAlmacenes.size)

        for (i in 0 until listaAlmacenes.size) {
            nombreAlmacenes[i] = "${listaAlmacenes.get(i).descripcionAlmacen} \n ${ObtenerNombreTienda(listaAlmacenes.get(i).idTienda)}"
        }
        listview.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, nombreAlmacenes)
        clickList()

    }


    override fun ErrorConsulta() {


    }


}