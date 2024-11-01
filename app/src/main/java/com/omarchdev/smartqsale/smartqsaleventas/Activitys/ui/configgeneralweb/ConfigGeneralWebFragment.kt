package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configgeneralweb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.omarchdev.smartqsale.smartqsaleventas.R

class ConfigGeneralWebFragment : Fragment() {

    companion object {
        fun newInstance() = ConfigGeneralWebFragment()
    }

    private lateinit var viewModel: ConfigGeneralWebViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.config_general_web_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConfigGeneralWebViewModel::class.java)

    }

}
