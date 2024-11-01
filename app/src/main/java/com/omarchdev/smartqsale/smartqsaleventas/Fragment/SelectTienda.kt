package com.omarchdev.smartqsale.smartqsaleventas.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncTiendas
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTienda

import com.omarchdev.smartqsale.smartqsaleventas.R

import kotlinx.android.synthetic.main.fragment_select_tienda.*


class SelectTienda : Fragment() {


    private val asyncTiendas = AsyncTiendas()
    private var LTiendas = ArrayList<mTienda>()
    private val tienda = mTienda()
    private var tiendaInterface: TiendaInterface? = null
    private var primerRegistro = ""
    fun newInstance(tiendaInterface: TiendaInterface, primerRegistro: String): SelectTienda {

        val f = SelectTienda()
        f.tiendaInterface = tiendaInterface
        f.primerRegistro = primerRegistro
        return f
    }

    interface TiendaInterface {

        fun TiendaPorDefecto()
        fun TiendaSeleccionada(id: Int)
        fun SeCargoTiendas()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_tienda, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {

        super.onViewCreated(v, savedInstanceState)
        val lista = ArrayList<String>()
        lista.add("Cargando tiendas")
        val adapter = ArrayAdapter<String>(requireActivity(),
                android.R.layout.simple_expandable_list_item_1, lista)
        tienda.nombreTienda = "$primerRegistro"
        tienda.idTienda = 0
        tienda.codigo = ""

        spnTiendas.adapter = adapter
        spnTiendas.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                tiendaInterface?.TiendaPorDefecto()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                tiendaInterface?.TiendaSeleccionada(LTiendas.get(position).idTienda)
            }

        })
        asyncTiendas.ObtenerTiendas(object : AsyncTiendas.ListenerRecuperarTiendas {
            override fun TiendasRecuperadas(Tiendas: List<mTienda>?) {

                lista.clear()
                LTiendas.add(tienda)
                LTiendas.addAll(Tiendas!!)
                LTiendas?.forEach {
                    lista.add(it.nombreTienda)
                }

                adapter.notifyDataSetChanged()
                tiendaInterface?.SeCargoTiendas()
                spnTiendas.setSelection(0)

                spnTiendas.setSelection(0)
            }

            override fun ErrorTiendas() {


            }

        })

    }

    fun seleccionarTienda(idTienda: Int) {

        LTiendas.forEachIndexed { i, mTienda ->
            if (mTienda.idTienda == idTienda) {
                spnTiendas.setSelection(i)
            }
        }

    }


}
