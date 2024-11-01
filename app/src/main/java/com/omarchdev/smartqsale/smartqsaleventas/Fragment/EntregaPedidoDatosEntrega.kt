package com.omarchdev.smartqsale.smartqsaleventas.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.omarchdev.smartqsale.smartqsaleventas.R

class EntregaPedidoDatosEntrega : Fragment() {

    companion object {
        fun newInstance() = EntregaPedidoDatosEntrega()
    }

    private lateinit var viewModel: EntregaPedidoDatosEntregaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.entrega_pedido_datos_entrega_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(EntregaPedidoDatosEntregaViewModel::class.java)

        viewModel.datosEntrega.observe(viewLifecycleOwner, Observer {

        })

    }

}
