package com.omarchdev.smartqsale.smartqsaleventas.Model

class OperarioSelect{

    var Select=false
    var operario=mOperario()

    constructor(select:Boolean,operario:mOperario){
        this.Select=select
        this.operario=operario
    }
}