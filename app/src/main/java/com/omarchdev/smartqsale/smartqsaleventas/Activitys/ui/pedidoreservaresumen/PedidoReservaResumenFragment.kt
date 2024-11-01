package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.pedidoreservaresumen

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdaterResumenPedido
import kotlinx.android.synthetic.main.pedido_reserva_resumen_fragment.*

class PedidoReservaResumenFragment : Fragment() {

    companion object {
        fun newInstance() = PedidoReservaResumenFragment()
    }

    private lateinit var viewModel: ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.pedido_reserva_resumen_fragment, container, false)
    }
    var dialogCargaAsync:DialogCargaAsync?=null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        val rvAdapterResumen=RvAdaterResumenPedido()
        rvPedidosReserva.adapter=rvAdapterResumen
        rvPedidosReserva.layoutManager=LinearLayoutManager(context)
        viewModel.listResumePedido.observe(viewLifecycleOwner, Observer{
            rvAdapterResumen.AddItems(ArrayList(it))
        })

        viewModel.loadingProcessSave.observe(viewLifecycleOwner, Observer{


            if(it.loading==false){
                dialogCargaAsync?.hide()
            }else{
                dialogCargaAsync=DialogCargaAsync(context)
                dialogCargaAsync?.getDialogCarga("Espere un momento")?.show()
            }

            if(it.terminate==true){
                if(it.resultOk){
                    AlertDialog.Builder(context).setTitle("Confirmación").setMessage(it.message)
                            .setPositiveButton("Aceptar"){dialog, which ->
                                viewModel.CierraMensajeConfirmacion()
                            }.create().show()
                }else{
                    AlertDialog.Builder(context).setTitle("Advertencia").setMessage(it.message)
                            .setPositiveButton("Aceptar"){dialog, which ->
                                viewModel.CierraMensajeConfirmacion()
                            }.create().show()
                }

            }
        })

        viewModel.loadingInit.observeForever {

            if(it.loading){
                pbPedidos.visibility=View.VISIBLE
            }else{
                pbPedidos.visibility=View.GONE
            }

        }

        rvAdapterResumen.changeStateItem=object: RvAdaterResumenPedido.ChangeStateItem{

            override fun itemChange(position: Int, state: Boolean) {

                viewModel.ActualizaEstadoResumenPedido(state,position)

            }

        }
    }

}