package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listadozonaservicio

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ConfigZonaServicioMesa
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.ListZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterSelectZonaServicioV2
import kotlinx.android.synthetic.main.listado_zona_servicio_fragment.*

class ListadoZonaServicioFragment : Fragment(), ListZonaServicio {

    companion object {
        fun newInstance() = ListadoZonaServicioFragment()
    }
    val adapter= RvAdapterSelectZonaServicioV2()
    private lateinit var viewModel: ListadoZonaServicioVM
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.listado_zona_servicio_fragment, container, false)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_zona_servicio, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            val intent=Intent(context,ConfigZonaServicioMesa::class.java)
            startActivity(intent)
        }
        return false
    }

    fun cargarLista(){
        viewModel.listaZonaServicios.observe(viewLifecycleOwner, {
            adapter.AgregarListaPrecios(it)
        })
    }


    override fun onResume() {
        super.onResume()

        cargarLista()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter.listZonaServicio=this
        rvZonasServicioEdit.adapter=adapter
        rvZonasServicioEdit.layoutManager= GridLayoutManager(context,requireContext().resources.getInteger(R.integer.NumColZonaServicio))
        viewModel = ViewModelProvider(this).get(ListadoZonaServicioVM::class.java)

        cargarLista()

    }

    override fun clickZonaServicio(id: Int) {
        val intent=Intent(context,ConfigZonaServicioMesa::class.java).apply {
            putExtra("id",id)
        }
        startActivity(intent)
    }

}