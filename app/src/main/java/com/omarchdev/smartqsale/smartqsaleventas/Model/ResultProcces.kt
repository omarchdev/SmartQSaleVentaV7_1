package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName

class ResultProcces {

    constructor(){

    }


    constructor(codeResult:Int,mensaje:String){

        this.codeResult=codeResult;
        this.messageResult=mensaje;
    }

    @SerializedName("codeResult")
    var codeResult=0
    @SerializedName("messageResult")
    var messageResult=""
    @SerializedName("codeProcess")
    var codeProcess=0
    @SerializedName("permitir")
    var permitir=false

}


class ResultProcessData<T>{
    constructor(){

    }


    constructor(codeResult:Int,mensaje:String,data:T){
        this.data=data
        this.codeResult=codeResult;
        this.messageResult=mensaje;
    }

    @SerializedName("data")
    var data:T?=null
    @SerializedName("code_result")
    var codeResult=0
    @SerializedName("message")
    var messageResult=""

    var codeProcess=0
    var permitir=false

}


public class ProcessResult<T>{

    @SerializedName("message")
    var messageResult:String=""
    @SerializedName("code_result")
    var codeResult:Int=0
    @SerializedName("data")
    var data:T?=null
    @SerializedName("processOk")
    var processOk=false

}