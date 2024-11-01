package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.detallepedido

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterDetallePedido
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPagosEnVenta
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_detalle_pedido.*

class DetallePedidoFragment : Fragment(), SlidingUpPanelLayout.PanelSlideListener {


    companion object {
        fun newInstance() = DetallePedidoFragment()
    }

    private lateinit var viewModel: DetallePedidoViewModel
    private lateinit var rvAdapterDetallePedido: RvAdapterDetallePedido
    private lateinit var rvAdapterPagosEnVenta: RvAdapterPagosEnVenta

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        var r = inflater.inflate(R.layout.activity_detalle_pedido, container, false)
        rvAdapterDetallePedido = RvAdapterDetallePedido()
        rvAdapterPagosEnVenta =
            RvAdapterPagosEnVenta(
                0
            )
        val type: Byte = 2
        rvAdapterPagosEnVenta.setTypeView(type)
        return r
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvDetallePedido.adapter = rvAdapterDetallePedido
        rvDetallePedido.layoutManager = LinearLayoutManager(activity)
        rvMetodosDePago.adapter = rvAdapterPagosEnVenta
        rvMetodosDePago.layoutManager = LinearLayoutManager(activity)
        rvMetodosDePago.setHasFixedSize(true)
        if (!Constantes.ConfigTienda.bUsaFechaEntrega) {
            rlFechaEntrega.visibility = View.GONE
            rlEstadoEntrega.visibility = View.GONE
        }
        if (Constantes.Tienda.ZonasAtencion) {
            if (Constantes.Tienda.cTipoZonaServicio == "A") {
                val image = this.resources.getDrawable(R.drawable.ic_car_side_grey600_24dp)
                image.setBounds(10, 0, image.intrinsicWidth, image.intrinsicHeight)
                txtZonaServicio.setCompoundDrawables(image, null, null, null)
            }
        } else {
            txtZonaServicio.visibility = View.GONE
        }
        if (Constantes.Tienda.cTipoZonaServicio == "A") {
            panel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
        panel.addPanelSlideListener(this)
        panel.isTouchEnabled = false
        rb1.isEnabled = false
        rb2.isEnabled = false
        rgEntregado.setOnCheckedChangeListener { group, checkedId ->
            verificarEstado(viewModel.estadoEntregado.value!!)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetallePedidoViewModel::class.java)
        val id = activity?.intent!!.getIntExtra("idCabeceraPedido", 0)
        val estadoPagado = activity?.intent!!.getBooleanExtra("EstadoPagado", false)
        viewModel.GetPedido(id, estadoPagado)
        viewModel.listPagos.observe(viewLifecycleOwner, Observer {
            rvAdapterPagosEnVenta.AgregarPagosEnVenta(it)
        })
        viewModel.pedido.observe(viewLifecycleOwner, Observer {
            rvAdapterDetallePedido.AddElement(it!!.listProducto)
            txtNombreCliente.text = it.cabeceraPedido.cliente.razonSocial
            txtNombreVendedor.text = it.cabeceraPedido.vendedor.primerNombre
            txtFechaPedido.text = it.cabeceraPedido.fechaReserva
            txtFechaEntrega.text=it.cabeceraPedido.fechaEntrega
            val valDsct=Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", it.cabeceraPedido.descuentoPrecio)
            val valBrt=  Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", it.cabeceraPedido.getTotalBruto())
            val valNet=Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f",it.cabeceraPedido.getTotalNeto())
            txtValorNetoDato.text =valNet
            txtValorBrutoDato.text = valBrt
            valorDescuentoDato.text = valDsct
            if (estadoPagado) {
                txtDocPago.visibility = View.VISIBLE
                txtDocPago.text=it.cabeceraPedido.documentoPago
            } else {
                txtDocPago.visibility = View.GONE
            }

            if (it.cabeceraPedido.getZonaServicio().idZona == 0) {
                txtZonaServicio.visibility = View.GONE
            } else {
                txtZonaServicio.text=it.cabeceraPedido.getZonaServicio().descripcion
                txtZonaServicio.visibility = View.VISIBLE
            }
        })

        viewModel.estadoEntregado.observe(viewLifecycleOwner, Observer {
            /*when(it!!){
                true->rb1.isChecked=true
                false->rb2.isChecked=true
            }*/
        })
    }

    fun verificarEstado(estadoEntrega: Boolean) {
        var estado = estadoEntrega
        var mensaje = ""
        if (!estado) {
            mensaje = "¿Desea marcar este pedido como ENTREGADO ?"
        } else {
            mensaje = "¿Desea marcar este pedido como NO ENTREGADO?"
        }
        AlertDialog.Builder(requireActivity()).setPositiveButton("Aceptar")
        { dialog, which -> viewModel.ActualizarEstadoEntregado() }
                .setNegativeButton("Cancelar") { dialog, which ->
            if (estado) {
                rb1.isChecked = true
            } else {
                rb2.isChecked = true
            }
        }.setMessage(mensaje).setTitle("Advertencia").show()
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {

    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {

    }

}