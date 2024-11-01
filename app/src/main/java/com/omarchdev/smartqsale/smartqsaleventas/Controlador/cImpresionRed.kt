package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.content.ContentValues.TAG
import android.util.Log
import com.omarchdev.smartqsale.smartqsaleventas.Model.DocVenta
import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.PrinterCommands
import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.PrinterCommands.FEED_PAPER_AND_CUT
import java.io.*
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException
import java.util.*

class cImpresionRed{
    private    val cc = byteArrayOf(0x1B, 0x21, 0x03)  // 0- normal size text
    private val bb = byteArrayOf(0x1B, 0x21, 0x08)  // 1- only bold text
    private val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- bold  with medium text
    private val bb3 = byteArrayOf(0x1B, 0x21, 0x3C) // 3- bold with large text
    private val bb4 = byteArrayOf(0x1B, 0x21, 0x10) // 3- bold with large text
    private val bb5 = byteArrayOf(0x1B, 0x21, 0xC) // 3- bold with large text
    val ESC_ALIGN_LEFT = byteArrayOf(0x1b, 'a'.toByte(), 0x00)
    var c = Calendar.getInstance()


    fun ImpresionNotaVenta(){

    }

    fun ImpresionFactura(ip:String,puerto:Int,titulo:String="",docVenta:DocVenta):Boolean{
        var outToServer: DataOutputStream?=null
        var clientSocket: Socket?=null
        var p=true

        try {

            clientSocket = Socket()
            clientSocket.connect(InetSocketAddress(ip, puerto),4000)

            if(clientSocket.isConnected) {
                clientSocket!!.getInputStream()
                var out:OutputStream=clientSocket!!.getOutputStream()

                outToServer = DataOutputStream(clientSocket?.getOutputStream())
                out.write(docVenta.nombreReceptor.toByteArray())

  /*              if(docVenta.nombreEmisor.length>0) {
                    outToServer.writeChars(docVenta.nombreEmisor)

                }

                if(docVenta.direccion.length>0){
                    outToServer.writeChars(docVenta.direccion)

                }
                if(docVenta.nombreEmisor.length>0) {
                    outToServer.writeChars(docVenta.nombreEmisor)
                }
                if(docVenta.docEmisor.length>0) {
                    outToServer.writeChars(docVenta.docEmisor)
                }
                if(docVenta.serie.length>0) {
                    outToServer.writeChars(docVenta.tipoDoc)
                    outToServer.writeChars(docVenta.serie+"-"+docVenta.correlativo)
                }
                if(docVenta.tituloReceptor.length>0){
                    outToServer.writeChars(docVenta.tituloReceptor)
                }
                if(docVenta.docReceptor.length>0) {
                    outToServer.writeChars(docVenta.docReceptor)
                }
                if(docVenta.nombreReceptor.length>0)
                {
                    outToServer.writeChars(docVenta.nombreReceptor)
                }
                if(docVenta.fechaEmision.length>0)
                {
                    outToServer.writeChars(docVenta.fechaEmision)
                }
                if(docVenta.fechaVencimiento.length>0)
                {
                    outToServer.writeChars(docVenta.fechaVencimiento)
                }
                if(docVenta.moneda.length>0)
                {
                    outToServer.writeChars(docVenta.moneda)
                }
                if(docVenta.IGV.length>0)
                {
                    outToServer.writeChars(docVenta.IGV)
                }

                if(docVenta.cabecerasTicket.length>0){
                    outToServer.writeChars(docVenta.tituloReceptor)
                }
                if(docVenta.productos.length>0){
                    outToServer.writeChars(docVenta.productos)
                }
                if(docVenta.totalDescuento.length>0) {
                    outToServer.writeChars(docVenta.totalDescuento)

                }
                if(docVenta.totalGravada.length>0){
                    outToServer.writeChars(docVenta.totalGravada)
                }
                if(docVenta.totalIgv.length>0)
                    outToServer.writeChars(docVenta.totalIgv)

                if(docVenta.total.length>0)
                    outToServer.writeChars(docVenta.total)

                if(docVenta.importeLetra.length>0){
                      printCustom(outToServer,docVenta.importeLetra,2,1)
                }
*/
                //    outToServer.write(texto.toByteArray())
             //   if(texto.toByteArray().inputStream().read(buffer)!==-1)
                /*   while (fileInputStream.read(buffer) !== -1) {
                outToServer.write(buffer)
            }*/
                    outToServer.flush()
                    p=true
            }else{
                p=false
            }
        } catch (connectException: ConnectException) {
            Log.e(TAG, connectException.toString(), connectException)
        } catch (e1: UnknownHostException) {
            Log.e(TAG, e1.toString(), e1)
        } catch (e1: IOException) {
            Log.e(TAG, e1.toString(), e1)
        } finally {
            outToServer?.close()

        }

        return p
    }

    private fun printCustom(mmOutputStream:DataOutputStream,msg: String, size: Int, align: Int) {


        //Print config "mode"
        val cc = byteArrayOf(0x1B, 0x21, 0x03)  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        val bb = byteArrayOf(0x1B, 0x21, 0x08)  // 1- only bold text
        val bb2 = byteArrayOf(0x1B, 0x21, 0x20) // 2- bold  with medium text
        val bb3 = byteArrayOf(0x1B, 0x21, 0x3C) // 3- bold with large text
        val bb4 = byteArrayOf(0x1B, 0x21, 0x10) // 3- bold with large text
        val bb5 = byteArrayOf(0x1B, 0x21, 0xC) // 3- bold with large text
        try {
            when (size) {
                0 -> mmOutputStream.write(cc)
                1 -> mmOutputStream.write(bb)
                2 -> mmOutputStream.write(bb2)
                3 -> mmOutputStream.write(bb3)
                4 -> mmOutputStream.write(bb4)
                5 -> mmOutputStream.write(bb5)
            }

            when (align) {
                0 ->
                    //left align
                    mmOutputStream.write(ESC_ALIGN_LEFT)
                1 ->
                    //center align

                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER)
                2 ->
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT)
            }
            mmOutputStream.write(msg.toByteArray())
            mmOutputStream.write(PrinterCommands.LF.toInt())
            //outputStream.write(cc);
            //printNewLine();
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun ImpresionPedido(ip:String, puerto:Int, texto:String,cabecera:String):Boolean{

        var outToServer: DataOutputStream?=null
        var clientSocket: Socket?=null
        var p=true
        try {
            clientSocket = Socket()
            clientSocket.connect(InetSocketAddress(ip, puerto),3000)

            if(clientSocket.isConnected) {
                outToServer = DataOutputStream(clientSocket?.getOutputStream())
                var buffer = ByteArray(1000)
                outToServer.write(bb4)
                outToServer.write(cabecera.toByteArray())
                outToServer.write(bb3)
                outToServer.write(texto.toByteArray())

                outToServer.write(FEED_PAPER_AND_CUT)
                if(texto.toByteArray().inputStream().read(buffer)!==-1)
                outToServer.flush()
                p=true
            }else{
                p=false
            }
        } catch (connectException: ConnectException) {
            Log.e(TAG, connectException.toString(), connectException)
        } catch (e1: UnknownHostException) {
            Log.e(TAG, e1.toString(), e1)
        } catch (e1: IOException) {
            Log.e(TAG, e1.toString(), e1)
        } finally {
            outToServer?.close()

        }
        return p
    }

    fun ImpresionNotaVenta(ip:String,puerto:Int,cabecera:String,cuerpo:String){
        var outToServer: DataOutputStream?=null
        var clientSocket: Socket?=null

        var p=true
        try {
            //  createHtmlDocument(htmlString)
            /*   val fileInputStream = FileInputStream(Environment.getExternalStorageDirectory().toString() + File.separator + "test.pdf")*/
            clientSocket = Socket()
            clientSocket.connect(InetSocketAddress(ip, puerto),4000)

            if(clientSocket.isConnected) {
                outToServer = DataOutputStream(clientSocket?.getOutputStream())
                var buffer = ByteArray(1000)
                outToServer.write(bb4)
                outToServer.write(cabecera.toByteArray())
                outToServer.write(bb3)
                outToServer.write(cuerpo.toByteArray())
                 /*   while (fileInputStream.read(buffer) !== -1) {
                outToServer.write(buffer)
            }*/
                    outToServer.flush()

            }else{
                p=false
            }
        } catch (connectException: ConnectException) {
            Log.e(TAG, connectException.toString(), connectException)
        } catch (e1: UnknownHostException) {
            Log.e(TAG, e1.toString(), e1)
        } catch (e1: IOException) {
            Log.e(TAG, e1.toString(), e1)
        } finally {
            outToServer?.close()

        }

    }


    fun ProbarImpresora(ip:String,puerto:Int):Boolean{

        var outToServer: DataOutputStream?=null
        var clientSocket: Socket?=null
        var p=false
        try {
            clientSocket = Socket()
            clientSocket.connect(InetSocketAddress(ip, puerto),3000)
            if(clientSocket.isConnected) {
                outToServer = DataOutputStream(clientSocket?.getOutputStream())
                outToServer.writeChars("Prueba de impresora")
                outToServer.writeChars(c.time.toString())
                outToServer.writeChars("\n")
                outToServer.writeChars("\n")
                outToServer.writeChars("\n")
                outToServer.write(FEED_PAPER_AND_CUT)
              /*   while (fileInputStream.read(buffer) !== -1) {
               outToServer.write(buffer)
           }*/
                outToServer.flush()
                p=true
            }else{
                p=false
            }
            return p
        } catch (connectException: ConnectException) {
            Log.e(TAG, connectException.toString(), connectException)
            return p
        } catch (e1: UnknownHostException) {
            Log.e(TAG, e1.toString(), e1)
            return p
        } catch (e1: IOException) {
            Log.e(TAG, e1.toString(), e1)
            return p
        } finally {
            outToServer?.close()

        }
    }
}

