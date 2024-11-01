package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configweb

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.config_web_fragment.*

class ConfigWebFragment : Fragment(), TextWatcher {


    var dialogGuardar: DialogCargaAsync? = null

    companion object {
        fun newInstance() = ConfigWebFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_config_categoria, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.ic_guardar) {
            GuardarLink()
        }
        return false
    }

    private lateinit var viewModel: ConfigWebViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.config_web_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {

            dialogGuardar = DialogCargaAsync(requireContext())

            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.title = "Link de web"
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

            viewModel = ViewModelProviders.of(this).get(ConfigWebViewModel::class.java)
            dialogGuardar?.getDialogCarga("Cargando su configuración para la web.")?.show()
            viewModel.CargaUrl()
            edtNombreWeb.editText?.addTextChangedListener(this)
            viewModel.linkResult.observe(viewLifecycleOwner, Observer {
                dialogGuardar?.hide()
                txtLinkWeb.setText(it)
            })
            viewModel.resultGuardarOk.observe(viewLifecycleOwner, Observer {
                AlertDialog.Builder(context).setTitle("Confirmación").setMessage(it).setPositiveButton("Salir", null)
                        .create().show()
                dialogGuardar?.hide()
            })
            viewModel.resultGuardarError.observe(viewLifecycleOwner, Observer {
                AlertDialog.Builder(context).setTitle("Advertencia").setMessage(it).setPositiveButton("Salir", null)
                        .create().show()
                dialogGuardar?.hide()
            })
            viewModel.UrlInit.observe(viewLifecycleOwner, Observer {
                edtNombreWeb.editText?.setText(it)
            })

        } catch (ex: Exception) {
            ex.toString()
        }

    }

    fun GuardarLink() {
        var guardarProcess = viewModel.PermiteGuardarLinkTienda()
        var guardar = guardarProcess.get(1)
        var mensaje = guardarProcess.get(2).toString()

        if (guardar == false) {
            AlertDialog.Builder(context).setTitle("Advertencia").setMessage(mensaje).setPositiveButton("Salir", null)
                    .create().show()
        } else {
            dialogGuardar?.getDialogCarga("Guardando su configuración para la web.")?.show()
            viewModel.GuardarUrlLink()
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        txtLinkWeb.setText(viewModel.TextoLink(s.toString()))
    }

}
