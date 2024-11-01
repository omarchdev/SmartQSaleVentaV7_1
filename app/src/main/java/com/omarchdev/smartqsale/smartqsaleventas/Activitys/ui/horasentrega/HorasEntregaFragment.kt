package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.horasentrega

import android.app.TimePickerDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.horas_entrega_fragment.*

class HorasEntregaFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = HorasEntregaFragment()
    }

    private lateinit var viewModel: HorasEntregaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.horas_entrega_fragment, container, false)
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
            viewModel.GuardarHoras()
        }
        return false
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Periodo de entrega"
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)


        viewModel = ViewModelProviders.of(this).get(HorasEntregaViewModel::class.java)
        imgBtnInit.setOnClickListener(this)
        imgBtnEnd.setOnClickListener(this)
        val cargaDialog=DialogCargaAsync(context)
        val dialogAsync= cargaDialog.getDialogCarga("Espere un momento")
        val cargaDialogGuardar=DialogCargaAsync(context)
        val dialogAsyncGuardar=cargaDialogGuardar.getDialogCarga("Espere un momento")
        viewModel.horaInicio.observe(viewLifecycleOwner, Observer {
            val time=it!!.HoraFormateada
            edtHoraInicio.setText(time)
        })

        viewModel.initCharge.observe(viewLifecycleOwner, Observer {
            if(it!!){
                dialogAsync.show()
            }else{
                dialogAsync.hide()
            }
        })
        viewModel.loadingStateGuardar.observe(viewLifecycleOwner, Observer {
            if(it!!.loading){
                dialogAsyncGuardar.show()
            }else{
                dialogAsyncGuardar.hide()
            }
            if(it!!.terminate){
                var titulo=""
                val mensaje=it!!.message
                if(it!!.resultOk){
                    titulo="Confirmación"
                }else{
                    titulo="Advertencia"
                }
                AlertDialog.Builder(requireContext()).setTitle(titulo).setMessage(mensaje)
                        .setOnDismissListener {
                            viewModel.OcultarDialogResult()
                        }.setPositiveButton("Aceptar") { dialog, which ->
                            if(it!!.resultOk){
                                requireActivity().finish()
                            }else{
                                dialog.dismiss()
                            }
                        }.create().show()
            }
        })
        viewModel.horaFinal.observe(viewLifecycleOwner, Observer {
            val time=it!!.HoraFormateada
            edtHoraFinal.setText(time)
        })

    }

    fun GetTime(type:Int){

        val hour=if(type==1){viewModel.horaInicio.value!!.hour}else{viewModel.horaFinal.value!!.hour}
        val minute=if(type==1){viewModel.horaInicio.value!!.minute}else{viewModel.horaFinal.value!!.minute}

        val timePicker=TimePickerDialog(requireContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    viewModel.SetHora(hourOfDay,minute,type)
        },hour,minute,false)
        timePicker.show()
    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.imgBtnInit->{
                GetTime(1)
            }
            R.id.imgBtnEnd->{
                GetTime(2)
            }
        }
    }

}
