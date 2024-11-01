package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configproductvisibilidadtienda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductVisibilidadEnTiendas
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigProductVisibilidadTiendaViewModel(idProduct:Int) : ViewModel() {

   private val visibilidadPorTienda: MutableLiveData<ProductVisibilidadEnTiendas> by lazy{
       MutableLiveData<ProductVisibilidadEnTiendas>().also {
           GetVisibilidad(idProduct)
       }
   }
    val cargandoInfo=MutableLiveData<Boolean>().apply{
        postValue(true)
    }

    fun GetVisibilidadPorTienda(): LiveData<ProductVisibilidadEnTiendas> {
        return visibilidadPorTienda
    }

    fun ActualizarVisibilidadTiendaProducto(idProductTienda:Int,visible:Boolean){
         GlobalScope.launch{
            BdConnectionSql.getSinglentonInstance().ActualizarVisibilidadProductoTienda(idProductTienda,visible)

            visibilidadPorTienda.value!!.listadoVisibilidad.find{x->x.idProductConfigTienda==idProductTienda}!!.visible=visible

        }


    }

    fun GetVisibilidad(idProducto:Int){
        cargandoInfo.postValue(true)

        GlobalScope.launch {

            val visibilidadPorTiendaTemp=BdConnectionSql.getSinglentonInstance().GetVisibilidaEnTiendasProducto(idProducto)
            visibilidadPorTiendaTemp.idProduct=idProducto
            visibilidadPorTienda.apply {
                    postValue(visibilidadPorTiendaTemp)
            }
            cargandoInfo.postValue(false)

        }
    }

}

class ConfigProductVisibilidadTiendaViewModelFactory(val idProduct:Int): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConfigProductVisibilidadTiendaViewModel(idProduct) as T
    }

}
