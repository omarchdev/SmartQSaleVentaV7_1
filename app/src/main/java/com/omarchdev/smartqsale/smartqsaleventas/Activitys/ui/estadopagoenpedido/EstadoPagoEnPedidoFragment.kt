package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.estadopagoenpedido

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.omarchdev.smartqsale.smartqsaleventas.Control.RadioButtonV2
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.estado_pago_en_pedido_fragment.*

class EstadoPagoEnPedidoFragment : Fragment() {

    companion object {
        fun newInstance() = EstadoPagoEnPedidoFragment()
    }

    private lateinit var viewModel: EstadoPagoEnPedidoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.estado_pago_en_pedido_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EstadoPagoEnPedidoViewModel::class.java)
        viewModel.flujoPagoPedido.observe(viewLifecycleOwner, Observer {
            it!!.listEstadoPago.forEachIndexed { index, estadoPagoPedido ->
                val rb= RadioButtonV2(requireActivity())
                rb.id=estadoPagoPedido.idEstadoPago
                rb.tag=10000+index
                val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(52, 16, 16, 16)
                rb.textButton(estadoPagoPedido.cDescripcionEstadoPago,""    )
                rb.setPadding(16,4,16,4)
                rb.layoutParams=params
               /* var medi= MedioPagoEntrega("")
                medi.cCodeMedioPago=""*/
                rgFlujoPago.addView(rb)
            }


        })
    }

}
