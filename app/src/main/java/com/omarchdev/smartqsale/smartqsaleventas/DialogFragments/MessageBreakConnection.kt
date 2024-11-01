package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.Main3Activity
import com.omarchdev.smartqsale.smartqsaleventas.R

class MessageBreakConnection : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val dialog=super.onCreateDialog(savedInstanceState)


        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_message_break_connection, container, false)
        val btnOk1: Button =view.findViewById(R.id.btnReiniciar)
        btnOk1.setOnClickListener {
            val intent = Intent(context, Main3Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)



        }
        return view
    }

}
