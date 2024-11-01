package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.confignumphoneweb

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.config_num_phone_web_fragment.*

class ConfigNumPhoneWebFragment : Fragment() {

    companion object {
        fun newInstance() = ConfigNumPhoneWebFragment()
    }


    private lateinit var viewModel: ConfigNumPhoneWebViewModel
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
            viewModel.GuardarConfiguracionCelularWeb(cbActivo.isChecked,edtNumero.editText!!.text.toString(),edtMensaje.editText!!.text.toString())
        }
        return false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.config_num_phone_web_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title ="Número celular"
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)

        viewModel = ViewModelProviders.of(this).get(ConfigNumPhoneWebViewModel::class.java)
        viewModel.result.observe(viewLifecycleOwner, Observer{
            var show=false
            var ok=false
            var message=""
            it!!.mapValues {
                when(it.key){
                    "A"->{show=it!!.value as Boolean}
                    "B"->{ok=it!!.value as Boolean}
                    "C"->{message=it!!.value as String}
                }
            }
            if(show){ if(ok){
                AlertDialog.Builder(requireContext()).setTitle("Confirmación")
                        .setOnDismissListener {
                            viewModel.CloseDialog()
                        }.setMessage(message).setPositiveButton(
                        "Aceptar", DialogInterface.OnClickListener { dialog, which -> requireActivity().finish()
                }
                ).create().show()
                }else{
                    AlertDialog.Builder(requireContext()).setTitle("Advertencia")
                            .setOnDismissListener {
                                viewModel.CloseDialog()
                            }.setMessage(message.toString()).setPositiveButton("Aceptar ", null).create().show()
                }
            }
        })
        viewModel.configWebTienda.observe(viewLifecycleOwner, Observer{
            edtMensaje.editText!!.setText(it!!.cMensajeInicial)
            edtNumero.editText!!.setText(it!!.cNumero)
            cbActivo.isChecked=it!!.bActivo
        })

    }

}
