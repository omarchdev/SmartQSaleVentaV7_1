package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configdiasentrega

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Switch
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.Model.DiaSemanaConfig
import com.omarchdev.smartqsale.smartqsaleventas.Model.SemanaConfigWeb
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.config_dias_entrega_fragment.*

class ConfigDiasEntregaFragment : Fragment(), CompoundButton.OnCheckedChangeListener {

    companion object {
        fun newInstance() = ConfigDiasEntregaFragment()
    }

    private lateinit var viewModel: ConfigDiasEntregaViewModel
    val ArraySwitch = ArrayList<Switch>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.config_dias_entrega_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Días de entrega"
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        val dialogCarga = DialogCargaAsync(requireContext())
        val p = dialogCarga.getDialogCarga("Actualizando su información")
        viewModel = ViewModelProviders.of(this).get(ConfigDiasEntregaViewModel::class.java)


        viewModel.mostrarDialogCarga.observe(viewLifecycleOwner, Observer {


            if (!it!!) {
                p.hide()
            } else {
                p.show()
            }
        })
        viewModel.resultGuardar.observe(viewLifecycleOwner, Observer {
            var openDialog = false
            var ok = false
            var message = ""
            it!!.forEach {
                when (it.key) {
                    "A" -> {
                        openDialog = it!!.value as Boolean
                    }
                    "B" -> {
                        ok = it!!.value as Boolean
                    }
                    "C" -> {
                        message = it!!.value.toString() as String
                    }
                }
            }
            if (openDialog) {
                if (ok) {
                    AlertDialog.Builder(requireContext()).setTitle("Confirmación").setOnDismissListener {
                        viewModel.closeDialogMessage()
                    }.setPositiveButton("Aceptar", null)
                            .setMessage(message).create().show()
                } else {
                    AlertDialog.Builder(requireContext()).setTitle("Advertencia").setOnDismissListener {
                        viewModel.closeDialogMessage()
                    }.setPositiveButton("Salir", null)
                            .setMessage(message).create().show()
                }

            }

        })
        viewModel.SemanaConfigWeb.observe(viewLifecycleOwner, Observer {

            val dias = it!!.diasSemana
            dias.forEachIndexed { index, diaSemanaConfig ->
                val switch = Switch(activity)
                switch.id = diaSemanaConfig.IdSemanaConfig
                switch.text = diaSemanaConfig.Descripcion
                switch.isChecked = diaSemanaConfig.Activo
                switch.textSize = 24f
                val params = RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(8, 24, 8, 24)
                switch.setOnCheckedChangeListener(this)
                switch.layoutParams = params
                content_layout.addView(switch)

            }

        })


    }

    fun Guardar() {
        val sem = SemanaConfigWeb()

        ArraySwitch.forEach {
            val diaSemanaConfig = DiaSemanaConfig()
            diaSemanaConfig.IdSemanaConfig = it.id
            diaSemanaConfig.Activo = it.isChecked
            sem.diasSemana.add(diaSemanaConfig)

        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        viewModel.GuardarDiaSemana(buttonView!!.id, isChecked)

    }


}
