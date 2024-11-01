package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.estadopagoflujopedido

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.omarchdev.smartqsale.smartqsaleventas.Control.RadioButtonV2
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.estado_pago_flujo_pedido_fragment.*

class EstadoPagoFlujoPedidoFragment : Fragment() {

    companion object {
        fun newInstance() = EstadoPagoFlujoPedidoFragment()
    }

    private lateinit var viewModel: EstadoPagoFlujoPedidoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.estado_pago_flujo_pedido_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = activity?.intent!!.getIntExtra("idCabeceraPedido", 0)
        viewModel = ViewModelProviders.of(this, EstadoPagoEnPedidoVMFactory(id)).get(EstadoPagoFlujoPedidoViewModel::class.java)
        viewModel.GetFlujoPago().observe(viewLifecycleOwner, Observer {
            var idButton=0

            it?.listEstadoPago?.forEach {
                val rb=RadioButtonV2(requireContext())
                rb.textButton(it.cDescripcionEstadoPago,it.cDescripcionAdicionalPago)
                rb.id=it.idEstadoPago
                if(it.bMarcado){
                    idButton=it.idEstadoPago
                }
                var lParams=RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

                lParams.setMargins(8,16,8,16)
                rb.layoutParams=lParams
                rb.textSize=18f
                rb.setPadding(16,56,16,56)
                rgEstadoPago.addView(rb)
                rgEstadoPago.showDividers=LinearLayout.SHOW_DIVIDER_MIDDLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rgEstadoPago.dividerDrawable=resources.getDrawable(android.R.drawable.divider_horizontal_dark, requireActivity().theme  )
                }

            }
            if(it!!.listEstadoPago.size>0){
                rgEstadoPago.check(idButton)
                rgEstadoPago.setOnCheckedChangeListener { group, checkedId ->
                        viewModel.MarcarEstadoPago(checkedId,true)
                }
            }

        })



    }

}
