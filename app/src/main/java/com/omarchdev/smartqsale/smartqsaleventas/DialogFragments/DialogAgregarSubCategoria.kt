package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.*
import android.widget.Toast

import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_dialog_agregar_sub_categoria.*
import kotlinx.android.synthetic.main.fragment_dialog_agregar_sub_categoria.view.*

class DialogAgregarSubCategoria : DialogFragment(), View.OnClickListener {

    var idSub:Int=0
    var descripcion:String=""
    override fun onClick(v: View?) {
       when(v?.id){
            R.id.btnPositive->{
                permitir=true
                try {
                    if (edtDescripcionSubCategoria.editText?.text.toString().length < 3) {
                        permitir = false
                        edtDescripcionSubCategoria.error = "La descripción debe tener como mínimo 3 caracteres"
                    }
                    if (permitir) {
                        if(idSub==0) {
                            listenerSub?.ObtenerDescripcionSubCategoria(edtDescripcionSubCategoria.editText?.text.toString())
                        }
                        else{
                            listenerSub?.EditarSubCategoria(edtDescripcionSubCategoria.editText?.text.toString(),idSub)
                        }
                        this.dialog?.dismiss()
                    }
                }catch (e:Exception){
                    e.toString()
                }
            }
            R.id.btnNegative->{
                this.dialog?.dismiss()
            }
        }
    }


    private var listenerSub:ListenerSubCategoriaAgregar?=null
    private var listener: OnFragmentInteractionListener? = null
    private var esDialog=false
    private var permitir=true

    fun newInstance(id:Int,descripcion:String):DialogAgregarSubCategoria{
        val f=DialogAgregarSubCategoria()
        var args=Bundle()
        args.putInt("id",id)
        args.putString("descripcion",descripcion)
        f.arguments=args
        return f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v=inflater.inflate(R.layout.fragment_dialog_agregar_sub_categoria, container, false)
        v.btnPositive.setOnClickListener(this)
        v.btnNegative.setOnClickListener(this)
        idSub=requireArguments().getInt("id")
        descripcion= requireArguments().getString("descripcion").toString()
        try {
            if (idSub != 0) {
                v.edtDescripcionSubCategoria.editText?.setText(descripcion.trim())
                v.txtTitulo?.text = "Editar subcategoria"
            } else {
                v.txtTitulo.text = "Agregar subcategoria"


            }
        }catch (e:Exception){
            Toast.makeText(activity,e.toString(),Toast.LENGTH_SHORT).show()
        }
        if(esDialog){
            v.btnPositive.visibility=View.VISIBLE
            v.btnNegative.visibility=View.VISIBLE
            v.btnPositive.text="Guardar"
            v.btnNegative.text="Salir"
        }else{
            v.btnPositive.visibility=View.GONE
            v.btnNegative.visibility=View.GONE
        }
        return v
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        esDialog=true

        val dialog=super.onCreateDialog(savedInstanceState)

        var window=dialog.window

        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is ListenerSubCategoriaAgregar){
            listenerSub=context

        }else{
            Toast.makeText(context,"Error al subscribir la subcategoria",Toast.LENGTH_LONG).show()
        }

        /*
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }*/
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.

     */
    interface ListenerSubCategoriaAgregar{

        fun ObtenerDescripcionSubCategoria(descripcion:String)
        fun EditarSubCategoria(descripcion: String,id:Int)
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

}
