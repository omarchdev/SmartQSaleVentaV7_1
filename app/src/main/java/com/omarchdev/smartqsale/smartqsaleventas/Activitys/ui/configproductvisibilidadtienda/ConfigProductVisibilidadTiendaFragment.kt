package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configproductvisibilidadtienda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.config_product_visibilidad_tienda_fragment.*


class ConfigProductVisibilidadTiendaFragment : Fragment(), CompoundButton.OnCheckedChangeListener {

    companion object {
        fun newInstance() = ConfigProductVisibilidadTiendaFragment()
    }

    private lateinit var viewModel: ConfigProductVisibilidadTiendaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.config_product_visibilidad_tienda_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val idProduct=activity?.intent!!.getIntExtra("idProducto", 0)
        viewModel = ViewModelProviders.of(this,ConfigProductVisibilidadTiendaViewModelFactory(idProduct)).get(ConfigProductVisibilidadTiendaViewModel::class.java)
        viewModel.cargandoInfo.observe(viewLifecycleOwner, Observer{
            if(it!!){
                pbConfigVisibilidad.visibility=View.VISIBLE
            }else{
                pbConfigVisibilidad.visibility=View.GONE
            }
        })
        viewModel.GetVisibilidadPorTienda().observe(viewLifecycleOwner, Observer {

            it?.listadoVisibilidad?.forEachIndexed { index, productVisibilidadTienda ->

                val switch=Switch(activity)
                switch.id=productVisibilidadTienda.idProductConfigTienda
                switch.isChecked=productVisibilidadTienda.visible
                switch.textSize=24f
                val params= RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(8,16,8,16)
                switch.layoutParams=params
                switch.setOnCheckedChangeListener(this)
                content.addView(switch)
                switch.text=productVisibilidadTienda.tienda.descripcionTienda
            }
        })
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        viewModel.ActualizarVisibilidadTiendaProducto(buttonView!!.id,isChecked)
    }

}
