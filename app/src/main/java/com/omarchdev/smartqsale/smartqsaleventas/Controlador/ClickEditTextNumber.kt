package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.google.android.material.textfield.TextInputLayout
import android.util.Log
import android.widget.EditText

fun ClickTextInputLayout(edt: TextInputLayout) {

    edt.clearFocus()
    edt.setOnFocusChangeListener { v, hasFocus ->
        Log.i("focus    ","clicl")
        if(hasFocus)
        edt.editText!!.setSelection(edt.editText!!.text.toString().length)
    }

    edt.setOnClickListener {

        it.setOnClickListener {
            edt.editText!!.setSelection(edt.editText!!.text.toString().length)
        }
        edt.editText!!.setSelection(edt.editText!!.text.toString().length)
    }
    edt.editText!!.setOnFocusChangeListener { v, hasFocus ->
        if(hasFocus)
        edt.editText!!.setSelection(edt.editText!!.text.toString().length)
    }

    edt.editText!!.setOnClickListener {
        it.setOnClickListener {
            edt.editText!!.setSelection(edt.editText!!.text.toString().length)
        }
        edt.editText!!.setSelection(edt.editText!!.text.toString().length)
        Log.i("iet","clicl")
    }
}

fun ClickEditText(edt: EditText) {

    edt.setOnClickListener {
        edt.setSelection(edt.text.toString().length)
    }

}
