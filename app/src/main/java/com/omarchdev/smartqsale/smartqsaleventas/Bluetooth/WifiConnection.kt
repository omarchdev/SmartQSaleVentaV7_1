package com.omarchdev.smartqsale.smartqsaleventas.Bluetooth

import android.content.ContentValues
import android.util.Log
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException

class WifiConnection{

    var mmOutputStream:OutputStream?=null
    var mmInputStream:InputStream?=null
    private var clientSocket: Socket?=null
    private var outToServer:DataOutputStream?=null


    fun ConnectDevice(ip:String,puerto:Int):Boolean{
        clientSocket= Socket()


        try {
            clientSocket?.connect(InetSocketAddress(ip,puerto),4000)
                if(clientSocket?.isConnected!!){

                    mmOutputStream=clientSocket!!.getOutputStream()
                    mmInputStream=clientSocket!!.getInputStream()
                    mmInputStream.toString()
                    return true
                }else{
                    return false
                }
        } catch (connectException: ConnectException) {
            Log.e(ContentValues.TAG, connectException.toString(), connectException)
            return false
        } catch (e1: UnknownHostException) {
            Log.e(ContentValues.TAG, e1.toString(), e1)
            return false
        } catch (e1: IOException) {
            Log.e(ContentValues.TAG, e1.toString(), e1)
            return false
        }
    }

    fun CloseConnection(){
        outToServer?.flush()
        outToServer?.close()
    }
}