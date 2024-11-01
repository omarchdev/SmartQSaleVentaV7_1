package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.Model.TipoZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterSelectZonaServicio
import kotlinx.android.synthetic.main.df_select_zona_servicio.*


class Df_Select_Zona_Servicio():DialogFragment(){



    private var asyncZonaServicio=AsyncZonaServicio()
    private var actualZona=mZonaServicio()
    private var rvAdapterSelectZonaServicio= RvAdapterSelectZonaServicio()
    private val listaZonasServicios=ArrayList<mZonaServicio>()
    private var listenerSeleccionZonaServicio:ListenerSeleccionZonaServicio?=null
    private var permitir=true
    interface ListenerSeleccionZonaServicio{
        fun ZonaServicioSeleccionada(zonaServicio:mZonaServicio)
        fun NombreZona(nombre:String)
        fun EliminarSeleccion()
    }

    fun newInstance(zonaServicio: mZonaServicio,listenerSeleccionZonaServicio: ListenerSeleccionZonaServicio):Df_Select_Zona_Servicio{

        val f=Df_Select_Zona_Servicio()
        f.actualZona=zonaServicio
        f.listenerSeleccionZonaServicio=listenerSeleccionZonaServicio
        return f

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_select_zona_servicio,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_zonas_servicios.layoutManager= GridLayoutManager(context,requireContext().resources.getInteger(R.integer.NumColZonaServicio))
        rv_zonas_servicios.adapter=rvAdapterSelectZonaServicio

        btnEliminarZona.setOnClickListener {
            listenerSeleccionZonaServicio?.EliminarSeleccion()
            dialog?.dismiss()
        }
        btnSalir.setOnClickListener {
            this.dialog?.dismiss()
        }
        btnGuardar.setOnClickListener {
            permitir=true
            if(listaZonasServicios.size==0){
                permitir=false
            }
            if(permitir) {
                if(listaZonasServicios.get(rvAdapterSelectZonaServicio.posActual).idZona!=actualZona.idZona) {
                    if (listaZonasServicios.get(rvAdapterSelectZonaServicio.posActual).numReservas != 0) {
                        if (!listaZonasServicios.get(rvAdapterSelectZonaServicio.posActual).bZonaLibre) {
                            permitir = false
                        }
                    }
                }
            }

            if(permitir){

                listenerSeleccionZonaServicio?.ZonaServicioSeleccionada(
                        listaZonasServicios.get(rvAdapterSelectZonaServicio.posActual))

            }
           this.dialog?.dismiss()
        }
        asyncZonaServicio.ObtenerZonasServicio()
        asyncZonaServicio.listenerZonasServicio=object :AsyncZonaServicio.ListenerZonasServicio{
            override fun ResultadosZonaServicio(listaZonasServicio: ArrayList<mZonaServicio>) {
                this@Df_Select_Zona_Servicio.listaZonasServicios.clear()
                this@Df_Select_Zona_Servicio.listaZonasServicios.addAll(listaZonasServicio)
                rvAdapterSelectZonaServicio.AgregarListaPrecios(listaZonasServicio)
                if(listaZonasServicio.size>0){
                    if(actualZona.idZona!=0){
                        listaZonasServicio.forEachIndexed { i, it ->
                            if(it.idZona==actualZona.idZona){
                                rvAdapterSelectZonaServicio.posActual=i
                                rvAdapterSelectZonaServicio.notifyDataSetChanged()
                            }
                        }
                    }
                }else{

                }
            }

            override fun ResultadoGetTipoZonaServicio(tipoZonaServicio: TipoZonaServicio) {

            }

        }

    }


    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}