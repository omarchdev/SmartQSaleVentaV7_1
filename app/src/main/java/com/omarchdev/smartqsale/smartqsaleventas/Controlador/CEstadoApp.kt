package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

fun SalirApp(context:Context,activity: Activity){



    AlertDialog.Builder(context).setTitle("Advertencia").setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
        var intent= Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
        activity.finish()
    }).setNegativeButton("Cancelar",null).setMessage("¿Desea salir de la aplicación?")
            .create().show()



}