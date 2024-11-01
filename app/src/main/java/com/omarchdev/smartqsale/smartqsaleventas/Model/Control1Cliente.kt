package com.omarchdev.smartqsale.smartqsaleventas.Model

class Control1Cliente{

    var descripcionControl=""
    var idControl=0
    var listaControl2: ArrayList<Control2Cliente> = ArrayList()


    fun AddControl2List() {
        val control2Cliente = Control2Cliente()
        control2Cliente.idControl2Cliente = 0
        control2Cliente.descripcicionControl2Cliente = "Todos"
        this.listaControl2.add(control2Cliente)
    }
}