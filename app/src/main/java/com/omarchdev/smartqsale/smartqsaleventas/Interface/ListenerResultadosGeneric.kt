package com.omarchdev.smartqsale.smartqsaleventas.Interface

interface ListenerResultadosGeneric<T> {

    fun ObtenerResultadosBusqueda(resultado:T)
    fun ErrorBusqueda()

}