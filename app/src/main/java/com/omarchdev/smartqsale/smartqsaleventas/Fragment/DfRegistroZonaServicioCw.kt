package com.omarchdev.smartqsale.smartqsaleventas.Fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.DialogFragment
import com.omarchdev.smartqsale.smartqsaleventas.Model.mZonaServicio
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.df_registro_zona_servicio_cw.*

class DfRegistroZonaServicioCw(): DialogFragment(){

    private var zonaServicio=mZonaServicio()
    private var listenerObtenerDatoRegistroZonaServicio:ListenerObtenerDatoRegistroZonaServicio?=null
    interface ListenerObtenerDatoRegistroZonaServicio{
        fun DatosRegistoZonaServicio(zonaServicio: mZonaServicio)
    }
     fun newInstance(zonaServicio:mZonaServicio,listenerObtenerDatoRegistroZonaServicio: ListenerObtenerDatoRegistroZonaServicio):DfRegistroZonaServicioCw{
        var f=DfRegistroZonaServicioCw()
        f.zonaServicio=zonaServicio
        f.listenerObtenerDatoRegistroZonaServicio=listenerObtenerDatoRegistroZonaServicio
        return f
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=super.onCreateDialog(savedInstanceState)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.df_registro_zona_servicio_cw, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }


            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.length==3){
                    edt2.requestFocus()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        edt2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }


            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.length==0){
                    edt1.requestFocus()
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        if(zonaServicio.idZona!=0){
            if(zonaServicio.descripcion.length>0){
                edt1.setText(zonaServicio.descripcion.substring(
                        0,
                        zonaServicio.descripcion.indexOf("-")))
                edt2.setText(zonaServicio.descripcion.substring(
                        zonaServicio.descripcion.indexOf("-")+1,
                        zonaServicio.descripcion.length))

            }
        }
        btnGuardar.setOnClickListener{
            if(edt1.text.toString().length>0 && edt2.text.toString().length>0){

                var z=mZonaServicio()
                z.idZona=zonaServicio.idZona
                z.descripcion=edt1.text.toString()+"-"+edt2.text.toString()
                listenerObtenerDatoRegistroZonaServicio?.DatosRegistoZonaServicio(z)
                dialog?.dismiss()
            }
        }
        btnCancelar.setOnClickListener {
            dialog?.dismiss()
        }
    }


}
