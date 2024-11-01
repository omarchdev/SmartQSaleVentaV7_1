package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.operarioconfig

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.operario_config_fragment.*

class operario_config : Fragment() {


    var dialogCargaAsync: DialogCargaAsync?=null
    companion object {
        fun newInstance() = operario_config()
    }

    private lateinit var viewModel: OperarioConfigViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val idOperario= activity?.intent?.extras!!.getInt("idOperario")
        if(idOperario!=0){
            inflater.inflate(R.menu.menu_config_operario, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }else{
            inflater.inflate(R.menu.menu_config_operario_nuevo, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_guardar) {
            viewModel.GuardarOperario()
        }else if(item.itemId==R.id.action_eliminar){
            viewModel.EliminarOperario()
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.operario_config_fragment, container, false)

    }

    fun GuardarOperario(){
        viewModel.IngresaOperario()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val idOperario= activity?.intent?.extras!!.getInt("idOperario")
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title ="Edición de operario"
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
        viewModel = ViewModelProvider(this,OperarioConfigViewModelViewModelFactory(idOperario)).get(OperarioConfigViewModel::class.java)

        viewModel.operarioData.observe(viewLifecycleOwner, Observer { })
        viewModel.nombres.observe(viewLifecycleOwner, Observer {
            edtNombresOperarios.editText?.setText(it)
        })
        viewModel.apellidos.observe(viewLifecycleOwner, Observer {
            edtApellidosOperarios.editText?.setText(it)
        })



        viewModel.processCargaInit.observe(viewLifecycleOwner, Observer {

            if(it.loading==true){
                dialogCargaAsync= DialogCargaAsync(context)
                dialogCargaAsync?.getDialogCarga("Obteniendo información")?.show()
            }else{
                dialogCargaAsync?.hide()
                if(it.terminate==true){

                }
            }
        })
        viewModel.processCargaGuardar.observe(viewLifecycleOwner, Observer {

            if(it.loading==true){
                dialogCargaAsync= DialogCargaAsync(context)
                dialogCargaAsync?.getDialogCarga("Guardando operario")?.show()
            }else{
                dialogCargaAsync?.hide()
                if(it.terminate==true){

                    AlertDialog.Builder(requireContext())
                        .setTitle(if(it.resultOk){"Confirmación"}else{"Advertencia"})
                        .setMessage(it.message)
                        .setPositiveButton("Salir") { dialog, which ->
                            if(it.resultOk){
                                activity?.onBackPressed()
                            }
                        }.create().show()
                }
            }
        })
        viewModel.processCargaEliminar.observe(viewLifecycleOwner, Observer {
            if(it.loading==true){
            }else{
                if(it.terminate==true){


                    AlertDialog.Builder(requireContext())
                        .setTitle(if(it.resultOk){"Confirmación"}else{"Advertencia"})
                        .setMessage(it.message)
                        .setPositiveButton("Salir") { dialog, which ->
                            if(it.resultOk){
                                activity?.onBackPressed()
                            }
                        }.create().show()
                }
            }
        })




        edtNombresOperarios.editText!!.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setNombres(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        edtApellidosOperarios.editText!!.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setApellidos(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

}