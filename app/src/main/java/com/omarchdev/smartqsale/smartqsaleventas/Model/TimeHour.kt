package com.omarchdev.smartqsale.smartqsaleventas.Model

class TimeHour {

    var hour:Int=0
    var minute:Int=0
    var HoraFormateada:String="00:00"
        get(){

            var hourt=hour
            when(hourt){
                13->hourt=1
                14->hourt=2
                15->hourt=3
                16->hourt=4
                17->hourt=5
                18->hourt=6
                19->hourt=7
                20->hourt=8
                21->hourt=9
                22->hourt=10
                23->hourt=11
            }
            val horaf= if (hourt < 10) java.lang.String.valueOf("0" + hourt) else java.lang.String.valueOf(hourt)

            val minf=if (minute < 10) java.lang.String.valueOf("0" + minute) else java.lang.String.valueOf(minute)
            val sign=if(hour<12){"A.M."}else{"P.M."}
            return horaf+":"+minf+" "+sign
        }
    var Hora24:String="00:00"
        get(){
            return ""
        }
}