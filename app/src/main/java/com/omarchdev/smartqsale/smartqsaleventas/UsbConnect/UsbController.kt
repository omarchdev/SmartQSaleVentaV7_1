package com.omarchdev.smartqsale.smartqsaleventas.UsbConnect

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val ANDROID_USB_PERMISSION = "com.rockspoon.printertest.ACTION_USB_PERMISSION"
private val bulkModeTimeout = 5000

interface USBDevices{

    fun ReceivedDevicesUsb(listDevices:List<UsbDevice>)

}

class UsbController{

    private val context:Context
    var printerReady=false
    private val printerJobList=ArrayList<PrinterJob> ()
    private val usbManager: UsbManager
    var  usbDevice:UsbDevice?=null
    private val currentConnection = PrinterConnection()
    var usbDevicesInterface:USBDevices?=null
    private val listDevices=ArrayList<UsbDevice>()


    @SuppressLint("NewApi")
    constructor(context:Context){
        this.context=context
        usbManager = context.getSystemService(UsbManager::class.java)
         printDeviceList()
    }

    fun subscribeBroadcast(){
        val filter = IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(ANDROID_USB_PERMISSION)
        context.registerReceiver(usbReceiver, filter)
    }

    fun searchDevice(vendorId:Int,productId:Int):UsbDevice?{
        var usbDevice:UsbDevice?=null
        var s=""
        try{
            s="previo"
            if(usbManager!=null){
                val connectedDevices = usbManager.deviceList
                s="previo1"
                if(connectedDevices!=null){
                    for (device in connectedDevices.values) {
                        //Use the last device detected (if multiple) to open
                        s="previo2"
                        s=device.vendorId.toString() +"  -  "+device.productId
                        if(device.vendorId==vendorId && device.productId==productId){
                            usbDevice=device
                            s="previo3"
                        }
                        listDevices.add(device)

                    }
                }else{
                    Toast.makeText(context,"USB MANAGER NULL",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context,"USB MANAGER NULL",Toast.LENGTH_SHORT).show()
            }


        }catch (e:Exception){
            usbDevice=null
            Toast.makeText(context,s+"->  search-> "+e.toString(),Toast.LENGTH_SHORT).show()

        }

        return usbDevice
    }

    fun printDeviceList(){
        try{
            val connectedDevices = usbManager.deviceList
            listDevices.clear()
            if(connectedDevices!=null){
                for (device in connectedDevices.values) {
                    //Use the last device detected (if multiple) to open

                     listDevices.add(device)

                }
            }else{
                Toast.makeText(context,"NULL",Toast.LENGTH_LONG).show()
            }
           Toast.makeText(context,connectedDevices.size.toString() ,Toast.LENGTH_LONG).show()
            usbDevicesInterface?.ReceivedDevicesUsb(listDevices)
        }catch (e:Exception){
            Toast.makeText(context,"1   "+e.toString(),Toast.LENGTH_SHORT).show()
        }


    }
    fun unSubscribeBroadcast(){

        context.unregisterReceiver(usbReceiver)
    }
    private var usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (UsbManager.ACTION_USB_DEVICE_DETACHED == intent.action  ) {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                device?.let {
                    printStatus(context.getString(R.string.status_removed))
                    try{
                        printDeviceList()
                    }catch (e:Exception){
                        Toast.makeText(context,e.toString(), Toast.LENGTH_SHORT).show()
                    }
               }
            }
            else if (UsbManager.ACTION_USB_DEVICE_ATTACHED== intent.action ) {

                    try{
                        val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)

                        device?.let {

                            printDeviceList()
                        }
                    }catch (e:Exception){
                        Toast.makeText(context,e.toString(), Toast.LENGTH_SHORT).show()
                    }

            }else if(intent.action==ANDROID_USB_PERMISSION){
                usbDevice = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice
                printerReady = intent.getBooleanExtra(
                        UsbManager.EXTRA_PERMISSION_GRANTED,
                        false
                ) && usbDevice != null

                if(printerReady){
                    Toast.makeText(context,"Imprimiendo", Toast.LENGTH_LONG).show()

                    GlobalScope.launch {
                        try{
                            Imprime()
                        }catch (e:Exception){
                            Toast.makeText(context,"Error al imprimir", Toast.LENGTH_LONG).show()

                        }

                    }


                }else{
                    Toast.makeText(context,"No tiene permiso para imprimir",Toast.LENGTH_SHORT).show()
                }


            }

        }
    }

    private fun askUsbPermissions(device: UsbDevice) {
        val permissionIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(ANDROID_USB_PERMISSION),
                0
        )
        usbManager.requestPermission(device, permissionIntent)
    }
    private fun getAndOpenPrinterConnection(): PrinterConnection? {
        var printerConnection: PrinterConnection? = null
        if (usbManager.hasPermission(usbDevice)) {
            val iface = usbDevice!!.getInterface(0)

            for (i in 0 until iface.endpointCount) {
                val endPoint = iface.getEndpoint(i)
                if (endPoint.type == UsbConstants.USB_ENDPOINT_XFER_BULK && endPoint.direction == UsbConstants.USB_DIR_OUT) {
                    printerConnection = PrinterConnection()
                    printerConnection.connection = usbManager.openDevice(usbDevice)
                    if (printerConnection.connection == null) {
                        Log.e("PrinterManager", "Cannot connect to device.")
                        return null
                    }
                    printerConnection.iface = iface
                    printerConnection.endPoint = endPoint
                    printerConnection.connection!!.claimInterface(iface, true)
                    printerConnection.active = true
                    break
                }
            }
        }

        return printerConnection
    }


    fun imprimeEnDispositivo(printerJobLista:ArrayList<PrinterJob>,device: UsbDevice){
        subscribeBroadcast()
        this.printerJobList.clear()
        this.printerJobList.addAll(printerJobLista)
        usbDevice=device
        askUsbPermissions(device)
    }

    private fun executePrintJob(
            printerConnection: PrinterConnection,
            job: PrinterJob
    ): Boolean {



        return printerConnection.active && printerConnection.connection!!.bulkTransfer(
                printerConnection.endPoint,
                job.text,
                job.text.size,
                bulkModeTimeout
        ) >= 0
    }

    private fun Imprime():Boolean{

        if (printerReady) {
            if (printerJobList.size > 0) {
                val newConn = getAndOpenPrinterConnection()
                if (newConn != null) {
                    try
                    {
                        currentConnection.setPrinterConnection(newConn)
                        printerJobList.forEach {
                            val job: PrinterJob = it
                            if (executePrintJob(currentConnection, job)) {
                                Log.d("PrinterManager", "Printing job")
                            } else {
                                Log.e("PrinterManager", "Cannot print job.")
                            }
                        }
                    }catch (e:Exception){
                        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show()
                    }
                    currentConnection.closeConnection()
                } else {
                    Log.e(
                            "PrinterManager",
                            "Cannot open usb device connection!"
                    )
                }
            }
        }
        unSubscribeBroadcast()
        return true
    }



    private fun printStatus(status: String) {
    //    statusView.text = status
     //   Log.i(TAG, status)
    }

    private class PrinterConnection {
        var connection: UsbDeviceConnection? = null
        var endPoint: UsbEndpoint? = null
        var iface: UsbInterface? = null
        var active = false
        fun setPrinterConnection(printerConnection: PrinterConnection?) {

            if (printerConnection != null) {
                connection = printerConnection.connection
                endPoint = printerConnection.endPoint
                iface = printerConnection.iface
                active = printerConnection.active
            }

        }

        fun closeConnection() {


            if (active) {
                Log.d("PrinterManager", "Closing Printer Connection")
                connection!!.releaseInterface(iface)
                active = false
                connection = null
                endPoint = null
                iface = null
            } else {
                Log.d("PrinterManager", "Connection already closed!")
            }
        }
    }


}
