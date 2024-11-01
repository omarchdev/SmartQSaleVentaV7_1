package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.config_zona_servicio_fragment.*

class ConfigZonaServicioMesaFragment : Fragment(), IConfigZonaServicio {



    companion object {
        fun newInstance() = ConfigZonaServicioMesaFragment()
    }

    private lateinit var viewModel: ConfigZonaServicioMesaVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    fun guardar() {
        var permite = true
        if (txtDescripcionZona.editText!!.text.toString().trim().length < 3) {
            permite = false
        }
        if (permite) {
            val dialogCargaAsync=DialogCargaAsync(context)
            val dialog=dialogCargaAsync.getDialogCarga("Espere un momento")
            dialog.show()
            viewModel.guardarZonaServicio(txtDescripcionZona.editText!!.text.toString().trim(),checkDelivery!!.isChecked)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            guardar()
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_config_zona_servicio, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.config_zona_servicio_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfigZonaServicioMesaVM::class.java)
        val id = requireActivity().intent!!.getIntExtra("id", 0)
        if (id != 0) {
            viewModel.getZonaServicio(id)
        }
        viewModel.iConfigZonaServicio=this

        viewModel.mesaData.observe(viewLifecycleOwner) {
            txtDescripcionZona.editText!!.setText(it.descripcion)
            checkDelivery.isChecked = it.idTipoZona==400
        }
        if(Constantes.ConfigTienda.idTipoZonaServicio==300){
            checkDelivery.visibility=View.VISIBLE
        }else{

            checkDelivery.visibility=View.GONE
        }

    }

    override fun cargaExito() {
     activity?.finish()
    }

}