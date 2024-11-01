package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_dialog_envio_c_p_e_numero.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [DialogEnvioCPENumero.newInstance] factory method to
 * create an instance of this fragment.
 */

interface IDialogEnvioCPENumero {
    fun confirmaEnvio(idCabeceraVenta: Int, IdCompany: Int, numero: String)
}

class DialogEnvioCPENumero : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var idCabeceraVenta: Int? = null
    private var IdCompany: Int? = null
    private var numTelefono:String?=null
    var IDialogEnvioCPENumero: IDialogEnvioCPENumero? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idCabeceraVenta = it.getInt(ARG_PARAM1)
            IdCompany = it.getInt(ARG_PARAM2)
            numTelefono= it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog.setTitle("Envio por WhatsApp")
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtCelularEnvio.editText?.setText(numTelefono)
        btnGuardar.setOnClickListener {
            if( edtCelularEnvio.editText!!.getText().toString().trim().length==9){
                IDialogEnvioCPENumero!!.confirmaEnvio(idCabeceraVenta!!,IdCompany!!,edtCelularEnvio.editText!!.getText().toString().trim())
                dismiss()
            }
        }
        btnSalir.setOnClickListener {
            dismiss()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_envio_c_p_e_numero, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(idCabeceraVenta: Int, IdCompany: Int, numTelefono: String) =
            DialogEnvioCPENumero().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, idCabeceraVenta)
                    putInt(ARG_PARAM2, IdCompany)
                    putString(ARG_PARAM3, numTelefono)
                }
            }
    }
}