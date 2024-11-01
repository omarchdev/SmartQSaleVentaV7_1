package com.omarchdev.smartqsale.smartqsaleventas.Model

import java.io.Serializable
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class TimeData:Serializable {

    var day=0
    var month=0
    var year=0
    var hour=0
    var minute=0
    var dateInCalendar=Calendar.getInstance()
    var datemilis=0L
    fun getTimeStringFormatSql():String{


        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, 0)
        val timeMilis = calendar.timeInMillis
        val date = Date()
        date.time = timeMilis
        val textDate = sdf.format(date)

        return textDate
    }

    fun getTimeStringFormatInterface():String{

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, 0)
        val timeMilis = calendar.timeInMillis
        val date = Date()
        date.time = timeMilis
        val textDate = sdf.format(date)

        return textDate
    }

    fun getDate():Date{

        val calendar=Calendar.getInstance()
        calendar.set(year,month,day,hour,minute)

        val date=Date(calendar.timeInMillis)

        return date

    }

    fun setTimestamp(tiempo:Timestamp){

        if(tiempo!=null){

            var date=Date()
            date.time=tiempo.time
            dateInCalendar=Calendar.getInstance()
            dateInCalendar.time=date
            datemilis=tiempo.time

            year=dateInCalendar.get(Calendar.YEAR)
            month=dateInCalendar.get(Calendar.MONTH)
            day=dateInCalendar.get(Calendar.DAY_OF_MONTH)
            hour=dateInCalendar.get(Calendar.HOUR_OF_DAY)
            minute=dateInCalendar.get(Calendar.MINUTE)
        }
    }
}
