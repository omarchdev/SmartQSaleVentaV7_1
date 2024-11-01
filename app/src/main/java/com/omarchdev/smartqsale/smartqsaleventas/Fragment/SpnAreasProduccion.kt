package com.omarchdev.smartqsale.smartqsaleventas.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAreasProduccion
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion

import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_spn_areas_produccion.*


class SpnAreasProduccion : Fragment(), AsyncAreasProduccion.ListenerAreasProduccion {

    private val areasProduccion=mAreaProduccion()
    private var primerRegistro=""
    private var areasInterface:AreasInterface?=null
    private var listaAreas=ArrayList<mAreaProduccion>()
    private var adapter:ArrayAdapter<String>?=null
    private var lista=ArrayList<String>()
    var asyncAreaProduccion=AsyncAreasProduccion()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    interface AreasInterface{
        fun AreasPorDefecto()
        fun AreaSeleccionada(id:Int)
        fun SeCargoAreas()
    }

    fun newInstance(areasInterface: AreasInterface,primerRegistro:String):SpnAreasProduccion{

        val f=SpnAreasProduccion()
        f.primerRegistro=primerRegistro
        f.areasInterface=areasInterface
        return f

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_spn_areas_produccion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lista=ArrayList<String>()
        lista.add(primerRegistro)
        adapter= ArrayAdapter<String>(requireActivity(),android.R.layout.simple_expandable_list_item_1,lista)
  //
        asyncAreaProduccion.ObtenerAreasProduccionListado()
        asyncAreaProduccion.listenerAreasProduccion=this
        areasProduccion.cDescripcionArea=primerRegistro
        areasProduccion.idArea=0
        listaAreas.add(areasProduccion)
        spnAreasProduccion.adapter=adapter
        spnAreasProduccion.setOnItemSelectedListener( object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                areasInterface?.AreasPorDefecto()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                areasInterface?.AreaSeleccionada(listaAreas.get(position).idArea)
            }
        })
    }

    override fun ResultadosBusquedaArea(result: MutableList<mAreaProduccion>) {

        try {

            lista.clear()
            listaAreas.clear()
            listaAreas.add(areasProduccion)
            listaAreas.addAll(result)
            adapter?.clear()
            listaAreas.forEach {

        //        lista.add(it.cDescripcionArea)
                adapter!!.add(it.cDescripcionArea)
            }
          //  adapter?.addAll(lista)
            adapter?.notifyDataSetChanged()
            spnAreasProduccion.setSelection(0)
            spnAreasProduccion.setSelection(0)
            areasInterface?.SeCargoAreas()
        }catch (e:Exception){

            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()


        }
    }
    override fun ErrorConsultaAreas() {
    }


    fun seleccionarArea(idArea:Int){
        listaAreas.forEachIndexed { i, mAreaProduccion ->
            if(mAreaProduccion.idArea==idArea){
                spnAreasProduccion.setSelection(i)
            }
        }
    }
}
