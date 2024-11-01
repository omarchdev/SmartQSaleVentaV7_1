package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listapreciosselect

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ListProductosListaPrecios
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.ClickPositionRv
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterListaPrecios
import kotlinx.android.synthetic.main.lista_precios_select_fragment.*

class ListaPreciosSelectFragment : Fragment(), ClickPositionRv {

    companion object {
        fun newInstance() = ListaPreciosSelectFragment()
    }

    val rvAdapterListaPrecios= RvAdapterListaPrecios()
    private lateinit var viewModel: ListaPreciosSelectViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.lista_precios_select_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Listas de precio"
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

        viewModel = ViewModelProviders.of(this).get(ListaPreciosSelectViewModel::class.java)
        viewModel.listasPrecios.observe(viewLifecycleOwner, Observer {
            rvAdapterListaPrecios.SetItemsLista(it!!)
        })
        rvAdapterListaPrecios.clickPositionRv=this
        rvListaPrecios.adapter=rvAdapterListaPrecios
        rvListaPrecios.layoutManager=LinearLayoutManager(context)
    }

    override fun clickPosition(pos: Int, action: Int) {

        val intent= Intent(context,ListProductosListaPrecios::class.java)
        intent.putExtra("listaprecio",viewModel.GetListaPrecioItem(pos))
        startActivity(intent)
    }

}
