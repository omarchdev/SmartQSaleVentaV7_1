package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listapreciosselect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListaPrecios
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IListaPrecioRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaPreciosSelectViewModel : ViewModel() {

    val codeCia = GetJsonCiaTiendaBase64x3()
    var retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    var iListaPrecioRepository = retro.create(IListaPrecioRepository::class.java)

    val listasPrecios: MutableLiveData<ArrayList<ListaPrecios>> by lazy {
        MutableLiveData<ArrayList<ListaPrecios>>().also {
            GetListaPrecios()
        }
    }

    fun GetListaPrecioItem(pos:Int):ListaPrecios=listasPrecios.value!![pos]

    fun GetListaPrecios() {

        GlobalScope.launch {
            val result = iListaPrecioRepository.GetListaPreciosSelect(codeCia,TIPO_CONSULTA).execute().body()
            listasPrecios.apply {
                if(result!=null)
                postValue(result.listas)
            }
        }

    }

}
