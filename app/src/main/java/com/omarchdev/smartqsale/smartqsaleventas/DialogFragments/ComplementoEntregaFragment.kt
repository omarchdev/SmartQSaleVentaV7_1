package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_complemento_entrega.*


class ComplementoEntregaFragment : DialogFragment() {
    private var idEntrega: Int = 0
    private var iComplementoEntrega: IComplementoEntrega? = null
    private var titulo: String = ""

    interface IComplementoEntrega {
        fun CaptureInfo(message: String, idEntrega: Int)
        fun CierraDialog()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            var r = targetFragment as IComplementoEntrega
            iComplementoEntrega = r
        } catch (ex: Exception) {


        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        try {
            var r = context as IComplementoEntrega
            iComplementoEntrega = r
        } catch (ex: Exception) {


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idEntrega = it.getInt("IDENTREGA")
            titulo = it.getString("TITULO")!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtTituloEstadoEntrega.setText(titulo)

        btnGuardarCom.setOnClickListener {
            iComplementoEntrega?.CaptureInfo(edtDescripcionEntrega.text.toString(), idEntrega)
            this.dismiss()
        }
        btnSalirCom.setOnClickListener {
            this.dismiss()
            iComplementoEntrega?.CierraDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complemento_entrega, container, false)
    }

    override fun dismiss() {
        super.dismiss()
        iComplementoEntrega?.CierraDialog()
        Log.i("info_clos", "salio1")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, titulo: String) =
                ComplementoEntregaFragment().apply {
                    arguments = Bundle().apply {
                        putInt("IDENTREGA", param1)
                        putString("TITULO", titulo)

                    }
                }
    }
}