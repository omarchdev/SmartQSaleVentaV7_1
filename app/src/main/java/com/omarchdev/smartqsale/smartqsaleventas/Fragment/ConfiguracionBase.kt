package com.omarchdev.smartqsale.smartqsaleventas.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.omarchdev.smartqsale.smartqsaleventas.Controles.CSwitch

import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfiguracionBase : Fragment() {

    private var listaSwicht:ArrayList<CSwitch>?=null
    private var contentMantenimiento:LinearLayout?=null
    private var visibleSwitch:Boolean=false
    private var top=8
    private var left=8
    private var right=8
    private var bottom=8

    val params = LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    class EstadoSwitch(idSwitch:Int,estado:Boolean){
        var idSwitch=idSwitch
        var estado=estado
    }


    fun EstadosSwitch():ArrayList<EstadoSwitch>{
        var listaResultados:ArrayList<EstadoSwitch>?=null
        if(listaSwicht!=null) {
            listaResultados=ArrayList()
            for (i in listaSwicht!!.indices){
                listaResultados!!.add(EstadoSwitch(listaSwicht!!.get(i).idControler,listaSwicht!!.get(i).isChecked))
            }
        }
        return listaResultados!!
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_configuracion_base, container, false)
        contentMantenimiento=view.findViewById(R.id.contentMantenimiento)
        return view
    }


    fun GenerarPantalla(context:Context){
        if(visibleSwitch){
            generarSwitch(context)
        }
    }

     private fun generarSwitch(context:Context){
        if(listaSwicht!=null){
            for(i in listaSwicht!!.indices) {
                contentMantenimiento?.addView(listaSwicht?.get(i))
            }
        }
    }
    fun useSwitch(visible:Boolean){
        this.visibleSwitch=visible
        if(visible){
            listaSwicht=ArrayList()
        }else {
            listaSwicht=null

        }
    }

    fun HabilitarSwitch(habilitado:Boolean){
        listaSwicht?.forEach {
            it.isEnabled=habilitado
        }
    }

    fun AgregarSwitch(id:Int,texto:String,estadoSwitch:Boolean){
       if(visibleSwitch){
           var nSwitch=CSwitch(context = requireContext(),idControler = id)
           nSwitch.textOff="Desactivado"
           nSwitch.textOn="Activado "
           nSwitch.text=texto
           nSwitch.isChecked=estadoSwitch
           params.setMargins(left,top,right,bottom)
           nSwitch.layoutParams=params
           listaSwicht?.add(nSwitch)
        }
        listaSwicht?.size
    }

    fun setMargins(top:Int,bottom:Int,left:Int,right:Int) {
        this.top=top
        this.bottom=bottom
        this.left=left
        this.right=right
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
     }




}
