package com.omarchdev.smartqsale.smartqsaleventas.Controles

import android.content.Context
import android.widget.Switch

class CSwitch(context: Context,idControler:Int):Switch(context) {

    var idControler=idControler
    var position:Int

    init {
        position=0

    }


}