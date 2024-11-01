package com.omarchdev.smartqsale.smartqsaleventas.Control

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import java.math.BigDecimal


class NumberTextWatcher(et: EditText) : TextWatcher {

    interface INumberTextWatcher{
        fun cantidadBigDecimal(number: BigDecimal)
    }
    private val et: EditText
    var cantidadInit= BigDecimal(0)
    var iNumberTextWatcher:INumberTextWatcher?=null
    var montoApertura="0.00"
    var temp=""
    override fun afterTextChanged(s: Editable) {
        et.removeTextChangedListener(this)
        et.setText(montoApertura)
        et.setSelection(montoApertura.length)
        et.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        et.setSelection(montoApertura.length - 1)
        temp = s.toString()
    }
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        var position = 0
        montoApertura = s.toString().replace(",",".")
        montoApertura = montoApertura.replace(Constantes.DivisaPorDefecto.SimboloDivisa, "")
        position = montoApertura.indexOf(".")
        if (position != -1) {
            var sub1 = ""
            var sub2 = ""
            sub1 = montoApertura.substring(0, position)
            sub2 = montoApertura.substring(position + 1)
            montoApertura = sub1 + sub2
            cantidadInit = BigDecimal(montoApertura)
            cantidadInit = cantidadInit.divide(BigDecimal(100))
        }
        montoApertura =  String.format("%.2f", cantidadInit)
        iNumberTextWatcher?.cantidadBigDecimal(cantidadInit)
    }

    companion object {
        private const val TAG = "NumberTextWatcher"
    }

    init {
        this.et=et
    }
}
