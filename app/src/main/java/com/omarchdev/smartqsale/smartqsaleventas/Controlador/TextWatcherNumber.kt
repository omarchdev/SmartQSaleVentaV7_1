package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.math.BigDecimal

open class TextWatcherNumber():TextWatcher{


    var edt:EditText?=null
    var temp=""
    var pos=0
    var texto=""
    var sub1=""
    var sub2=""
    var number=BigDecimal(0)

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        edt?.setSelection(edt!!.text.length-1)
        temp=p0.toString()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        pos=0
        texto=p0.toString()
        pos=texto.indexOf(".")
        if(pos!=-1){
            sub1=""
            sub2=""
            sub1=texto.substring(0,pos)
            sub2=texto.substring(pos+1)
            texto=sub1+sub2
            number= BigDecimal(texto)
            number=number.divide(BigDecimal(100))
        }
        texto=String.format("%.2f",number)
    }
    override fun afterTextChanged(p0: Editable?) {
        edt?.removeTextChangedListener(this)
        edt?.setText(texto)
        edt?.setSelection(texto.length)
        edt?.addTextChangedListener(this)
    }


}