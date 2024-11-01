package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.os.Handler
import java.util.*

class TimerTasks:TimerTask(){

    private var timer=Timer("EXECUTE",true)

    var otimerTask:OTimerTask?=null
    private val handler = Handler()

    public interface OTimerTask{
        fun ExecuteTask()
    }

    fun StopTask(){
        timer.cancel()
    }

    fun ExecuteTimer(timeExec:Long){
        timer.schedule(this,10000 ,timeExec*1000)
    }
    override fun run() {

        handler.post {
            otimerTask!!.ExecuteTask()
        }

    }
}