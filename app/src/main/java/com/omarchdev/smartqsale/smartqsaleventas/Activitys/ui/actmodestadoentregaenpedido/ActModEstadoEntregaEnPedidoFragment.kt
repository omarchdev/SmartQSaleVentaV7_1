package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.actmodestadoentregaenpedido

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.ComplementoEntregaFragment
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterEstadoPedido
import kotlinx.android.synthetic.main.act_mod_estado_entrega_en_pedido_fragment.*


class ActModEstadoEntregaEnPedidoFragment : Fragment(), RvAdapterEstadoPedido.IEstadoPorPedidoList, ComplementoEntregaFragment.IComplementoEntrega {

    companion object {
        fun newInstance() = ActModEstadoEntregaEnPedidoFragment()
    }

    var mostroCarga = false
    private lateinit var viewModel: ActModEstadoEntregaEnPedidoViewModel
    private val rvAdapterEstadoPedido = RvAdapterEstadoPedido()
    private lateinit var dialogCarga: DialogCargaAsync
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.act_mod_estado_entrega_en_pedido_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = activity?.intent!!.getIntExtra("idCabeceraPedido", 0)
        viewModel = ViewModelProviders.of(this, ActModEstadoEntregaEnPedidoFactory(id)).get(ActModEstadoEntregaEnPedidoViewModel::class.java)
        dialogCarga = DialogCargaAsync(context)
        rvEstadoPedido.adapter = rvAdapterEstadoPedido
        rvEstadoPedido.layoutManager = LinearLayoutManager(context)
        rvEstadoPedido.setHasFixedSize(true)
        rvAdapterEstadoPedido.iEstadoPorPedidoList = this
        viewModel.mostrarCarga.observe(viewLifecycleOwner, Observer {
            if (it!!) {
                mostroCarga = true
                dialogCarga.getDialogCarga("Guardando estado").show()
            } else {
                if (mostroCarga) {
                    dialogCarga.hide()
                }
            }
        })
        viewModel.GetFlujo().observe(viewLifecycleOwner, Observer {
            try {
                rvAdapterEstadoPedido.setItems(it!!.listEstadoEntregaPedido)
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        })
    }

    override fun GetPostionPedidoClick(position: Int, marcado: Boolean, descripcion: String) {

        if (marcado) {
            val frm = fragmentManager
            if (frm != null) {

                var dialogComplemento: ComplementoEntregaFragment = ComplementoEntregaFragment.newInstance(position, descripcion)
                dialogComplemento.setTargetFragment(this,22)
                dialogComplemento?.show(parentFragmentManager, "complemento_estado_pedido")
            }

        } else {

            viewModel.MarcaFlujo(position, marcado, "")
        }

    }

    override fun CaptureInfo(message: String, idEntrega: Int) {
        viewModel.MarcaFlujo(idEntrega, true, message)
    }

    override fun CierraDialog() {
        rvAdapterEstadoPedido.Refresh()
    }

}
