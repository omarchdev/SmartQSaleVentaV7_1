package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configwebmenu

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.*
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.IRvAdapterMenu
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterOpcionesMenuConfig
import kotlinx.android.synthetic.main.config_web_menu_fragment.*

class ConfigWebMenuFragment : Fragment(), IRvAdapterMenu {

    companion object {
        fun newInstance() = ConfigWebMenuFragment()
    }

    val rvAdapter = RvAdapterOpcionesMenuConfig()
    private lateinit var viewModel: ConfigWebMenuViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.config_web_menu_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConfigWebMenuViewModel::class.java)
        rvMenuConfigWeb.adapter = rvAdapter
        rvMenuConfigWeb.layoutManager = LinearLayoutManager(requireContext())
        viewModel.ItemsMenu.observe(viewLifecycleOwner, Observer {
            rvAdapter.SetItems(it!!)
        })
        rvAdapter.iRvAdapter = this
    }

    override fun clickListener(position: Int, action: Int) {
        val id = viewModel.GetId(position)
        when (id) {
            1 -> {
                val intent = Intent(requireContext(), ConfigWeb::class.java)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(requireContext(), ConfigNumPhoneWeb::class.java)
                startActivity(intent)
            }
            3 -> {
                val intent = Intent(requireContext(), config_dias_entrega::class.java)
                startActivity(intent)
            }
            4 -> {
                val intent = Intent(requireContext(), HorasEntregaActivity::class.java)
                startActivity(intent)
            }
            5 -> {
                val intent = Intent(requireContext(), ConfigWebImage::class.java)
                startActivity(intent)
            }
            6 -> {
                val intent = Intent(requireContext(), BannerWeb::class.java)
                startActivity(intent)
            }

        }
    }

}
