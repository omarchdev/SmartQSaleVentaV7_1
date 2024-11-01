package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.Bluetooth.BluetoothConnection
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper
import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.PrintOptions
import com.omarchdev.smartqsale.smartqsaleventas.R

class PrinterAct : ActivityParent() {

    lateinit var printOptions:PrintOptions
    var deviceAddressBt = ""
    val btConnection = BluetoothConnection.getSinglentonInstance(this@PrinterAct)
    var dbHelper = DbHelper(this@PrinterAct)
    val c = dbHelper.SelectOptionPrint()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer)
        try {
            val cc = dbHelper.SelectDevice()
            if (cc.count > 0) {
                while (cc.moveToNext()) {
                    deviceAddressBt = cc.getString(0)
                }
                btConnection.selectDevice(deviceAddressBt)
                btConnection.openBT()

                printOptions = PrintOptions(this@PrinterAct, btConnection.outputStream, btConnection.mmInputStream)
              //  printOptions.printUnicode()
            } else if (cc.count == 0) {
            }
        }catch (e:Exception){
            Toast.makeText(this@PrinterAct,e.toString(),Toast.LENGTH_LONG).show()
        }
    }


}
