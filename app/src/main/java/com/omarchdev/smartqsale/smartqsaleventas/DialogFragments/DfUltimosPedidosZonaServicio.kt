package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import android.widget.ArrayAdapter
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVentasZonaServicio
import kotlinx.android.synthetic.main.df_ventas_zona_servicio.*


class DfUltimosPedidosZonaServicio():DialogFragment(){

    private var cliente: mCustomer?=null
    private var zonaServicio:mZonaServicio?=null
    private var p:ArrayList<ProductoEnVenta>?=null
    private var asyncZonaServicio=AsyncZonaServicio()
    private var bTieneVenta:Boolean?=null
    private val listadoMarcas=ArrayList<String>()
    private lateinit var arrayAdapterModelo:ArrayAdapter<String>
    private val listadoModelos=ArrayList<String>()
    private val listaModelos=ArrayList<cModelo>()
    private lateinit var arrayAdapterMarcas:ArrayAdapter<String>
    private val listaMarcas=ArrayList<mMarca>()
    private var listenerActualizarZonaServicio:ListenerActualizarZonaServicio?=null
    private var listenerDatosCliente:ListenerDatosCliente?=null
    interface ListenerActualizarZonaServicio{
        fun ActualizarZonaServicio(zonaServicio:mZonaServicio)
    }
    interface ListenerDatosCliente{

        fun DatosObtenidosCliente(cliente:mCustomer)

    }

    fun ListenerDatosCliente(listenerDatosCliente:ListenerDatosCliente):DfUltimosPedidosZonaServicio{
        this.listenerDatosCliente=listenerDatosCliente
        return this
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    fun newInstance(bTieneVenta:Boolean,cliente:mCustomer,zonaServicio:
    mZonaServicio,listenerActualizarZonaServicio: ListenerActualizarZonaServicio)
            :DfUltimosPedidosZonaServicio{

        var f=DfUltimosPedidosZonaServicio()
        f.cliente=cliente
        f.zonaServicio=zonaServicio
        f.bTieneVenta=bTieneVenta
        f.listenerActualizarZonaServicio=listenerActualizarZonaServicio
        return f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_ventas_zona_servicio,container,false)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var rvAdapterVentasZonaServicio: RvAdapterVentasZonaServicio
        arrayAdapterModelo= ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,listadoModelos)
        arrayAdapterMarcas=ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,listadoMarcas)
        spnMarcas.adapter=arrayAdapterMarcas
        spnModelos.adapter=arrayAdapterModelo
        asyncZonaServicio.ObtenerDatosZonaServicioCW(zonaServicio!!.idZona,object :AsyncZonaServicio.ListenerZonaServicio{
            override fun datosZonaServicio(zonaServicio: mZonaServicio,
                                           marcas: ArrayList<mMarca>, modelos: ArrayList<cModelo>) {


                edtColor.editText!!.setText(zonaServicio.color)
                if(marcas.size>0){
                    listaMarcas.addAll(marcas)
                    listaMarcas.forEach {
                        listadoMarcas.add(it.Descripcion)
                    }
                }

                if(modelos.size>0){
                    listaModelos.addAll(modelos)
                    listaModelos.forEach {
                            listadoModelos.add(it.cDescripcion)
                    }
                }
                spnModelos.setSelection(0)
                spnMarcas.setSelection(0)
                arrayAdapterModelo.notifyDataSetChanged()
                arrayAdapterMarcas.notifyDataSetChanged()
                listaMarcas.forEachIndexed { i, it ->
                    if(zonaServicio.idMarca==it.idMarca){
                        spnMarcas.setSelection(i)
                    }
                }
                listaModelos.forEachIndexed {i,it->
                    if(zonaServicio.idModelo==it.idModelo){
                         spnModelos.setSelection(i)
                    }
                }
            }
        })
        if(bTieneVenta!!) {
            asyncZonaServicio.ObtenerUltimosServicios(zonaServicio!!.idZona,
                    object : AsyncZonaServicio.ListenerUltimosPedidos {
                        override fun UltimosPedidosZona(listaVentas: ArrayList<mCabeceraVenta>) {
                            rvAdapterVentasZonaServicio = RvAdapterVentasZonaServicio(listaVentas)
                            rvVentasZonaServicio.adapter = rvAdapterVentasZonaServicio
                            rvVentasZonaServicio.layoutManager = LinearLayoutManager(context)
                        }
                    })
        }
        if(cliente!!.getiId()!=0){
            txtNombreCliente.setText(cliente!!.getcName())
            listenerDatosCliente?.DatosObtenidosCliente(cliente!!)
        }else{
            txtNombreCliente.setText("Sin cliente")
        }
        btnSalir.setOnClickListener {
            dialog?.dismiss()
        }
        btnGuardar.setOnClickListener {
            zonaServicio!!.idMarca=listaMarcas.get(spnMarcas.selectedItemPosition).idMarca
            zonaServicio!!.idModelo=listaModelos.get(spnModelos.selectedItemPosition).idModelo
            zonaServicio!!.color=edtColor.editText!!.text.toString()
            listenerActualizarZonaServicio?.ActualizarZonaServicio(zonaServicio!!)
            this.dismiss()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog


    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listadoMarcas.clear()
        listaModelos.clear()
        listadoModelos.clear()
        listaMarcas.clear()
    }


}