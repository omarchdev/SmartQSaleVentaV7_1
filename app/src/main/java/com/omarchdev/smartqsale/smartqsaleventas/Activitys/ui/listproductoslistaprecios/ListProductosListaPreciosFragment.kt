package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listproductoslistaprecios

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.DialogModPrecioLista
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecioDetalle
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecios
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.ClickPositionRv
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterDetalleListaPrecios
import kotlinx.android.synthetic.main.list_productos_lista_precios_fragment.*

class ListProductosListaPreciosFragment : Fragment(), ClickPositionRv, SearchView.OnQueryTextListener, DialogModPrecioLista.IDialogModPrecioLista {

    companion object {
        fun newInstance() = ListProductosListaPreciosFragment()
    }
    var dialog:DialogModPrecioLista?=null
    var dialogCargaAsync:DialogCargaAsync?=null
    private lateinit var viewModel: ListProductosListaPreciosViewModel

    val rvAdapterDetalleListaPrecios = RvAdapterDetalleListaPrecios()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.list_productos_lista_precios_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try{

            svProductos.onActionViewExpanded()
            svProductos.setQuery("", false);
            val listaprecio= requireActivity().intent!!.getSerializableExtra("listaprecio") as ListaPrecios
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.title =listaprecio.descripcionLista
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            viewModel = ViewModelProviders.of(this, ListProductosListaPreciosViewModelFact(listaprecio.idLista)).get(ListProductosListaPreciosViewModel::class.java)
            //   viewModel = ViewModelProviders.of(this).get(ListProductosListaPreciosViewModel::class.java)
            viewModel.listaPrecioDetalle.observe(viewLifecycleOwner, Observer {
                rvAdapterDetalleListaPrecios.setItems(it!!)
            })
            viewModel.loadingProcessSave.observe(viewLifecycleOwner,Observer{
                if(it!!.loading){
                    dialogCargaAsync= DialogCargaAsync(context)
                    dialogCargaAsync?.getDialogCarga("Guardando información")?.show()
                }else{
                    dialogCargaAsync?.hide()

                    if(it.terminate){
                        if(it.resultOk){
                            rvAdapterDetalleListaPrecios.notifyDataSetChanged()
                        }
                        AlertDialog.Builder(requireContext())
                                .setTitle(if(it.resultOk){"Confirmación"}else{"Advertencia"})
                                .setMessage(it.message)
                                .setPositiveButton("Salir") { dialog, which ->
                                    if(it.resultOk){
                                        dialog?.dismiss()
                                    }
                                }.create().show()
                    }
                }
            })
            viewModel.loadingInit.observe(viewLifecycleOwner, Observer {
                rvListaDetalle.visibility=if(it!!.loading){View.GONE}else{View.VISIBLE }
                pbLPrecioDetalle.visibility=if(it!!.loading){View.VISIBLE}else{View.GONE }
            })
            rvAdapterDetalleListaPrecios.clickPositionRv = this
            rvListaDetalle.adapter = rvAdapterDetalleListaPrecios
            rvListaDetalle.layoutManager = LinearLayoutManager(context)
            svProductos.queryHint = "Busqueda de producto"
            svProductos.setOnQueryTextListener(this)
        }catch (ex:Exception){
            Log.e("Er1",ex.toString())
        }


    }

    override fun clickPosition(pos: Int, action: Int) {

        dialog = DialogModPrecioLista().newInstance(viewModel.GetItemListaPos(pos))
        dialog?.iDialogModPrecioLista =this
        dialog?.show(requireFragmentManager(), "")
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            viewModel.GetListaDetalle(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if(newText.equals("")){
            viewModel.GetListaDetalle("")
        }

        return false
    }

    override fun saveDetalleLista(listaPrecioDetalle: ListaPrecioDetalle) {
        viewModel.UpdateDetalleListaPrecio(listaPrecioDetalle)
    }


}
