package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.android.synthetic.main.fragment_message_slow_connect.*

class MessageSlowConnect : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_slow_connect, container, false)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val dialog=super.onCreateDialog(savedInstanceState)

        btnOk.setOnClickListener {
            dismiss()
        }
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

}
