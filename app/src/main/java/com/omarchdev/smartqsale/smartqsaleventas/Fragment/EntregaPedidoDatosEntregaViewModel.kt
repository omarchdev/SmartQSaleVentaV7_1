package com.omarchdev.smartqsale.smartqsaleventas.Fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.Model.DatosEntregaPedido

class EntregaPedidoDatosEntregaViewModel : ViewModel() {


    var datosEntrega= MutableLiveData<DatosEntregaPedido>()

}
