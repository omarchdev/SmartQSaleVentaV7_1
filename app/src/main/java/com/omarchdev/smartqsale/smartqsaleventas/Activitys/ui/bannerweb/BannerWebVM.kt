package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.bannerweb

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.MediosRetro
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.GetValueCodeCia
import com.omarchdev.smartqsale.smartqsaleventas.Model.LoadingProcessState
import com.omarchdev.smartqsale.smartqsaleventas.Model.MedioImagen
import com.omarchdev.smartqsale.smartqsaleventas.Model.MediosSolicitud
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BannerWebVM : ViewModel() {

    private val idItemsDelete = ArrayList<Int>()
    val loadingStateGuardar = MutableLiveData<LoadingProcessState>().apply {
        value = LoadingProcessState()
    }

    val activoWeb = MutableLiveData<Boolean>(false)

    val loadingStateCarga = MutableLiveData<LoadingProcessState>().apply {
        value = LoadingProcessState()
    }
    private val listMedio: MutableLiveData<List<MedioImagen>> by lazy {
        MutableLiveData<List<MedioImagen>>().also {
            CargaMedios()
        }
    }

    fun setVisibleWeb(visible: Boolean) {

            activoWeb.value = visible

    }

    fun CloseLoadingState() {
        val log = LoadingProcessState()
        log.message = ""
        log.terminate = false
        log.resultOk = false
        log.loading = false
        loadingStateGuardar.value = log

    }

    fun AddItemDelete(medioImagen: MedioImagen) {
        if (medioImagen.IdMedio != 0) {
            idItemsDelete.add(medioImagen.IdMedio)
        }
    }


    fun UpdateList(list: ArrayList<MedioImagen>) {
        listMedio.postValue(list)
    }

    fun GetImagenes(): MutableLiveData<List<MedioImagen>> {
        return listMedio
    }

    fun GuardarDatosMedios() {

        GlobalScope.launch {
            val loading = LoadingProcessState()
            loading.loading = true
            loading.message = "Espere un momento"
            loading.resultOk = false
            loading.terminate = false
            loadingStateGuardar.postValue(loading)

            val list = listMedio.value!!

            list.forEachIndexed { index, medioImagen ->
                medioImagen.INumItem = index + 1
                medioImagen.ConvertBitmapToImageDataArray()
            }

            val medioRetro = MediosRetro()
            val mediosSol = MediosSolicitud()
            mediosSol.medios = ArrayList(list)
            mediosSol.tipoMov = Constantes.TipoPedidoWeb.idTipoMovimiento
            mediosSol.codeCiaTienda = GetValueCodeCia()
            mediosSol.idMediosDelete = idItemsDelete
            mediosSol.imagenesVisible = activoWeb.value!!
            val api = medioRetro.iMediosRetro.saveMediosBanner(mediosSol)
            val response = api.execute()
            if (response.isSuccessful) {
                val result = response.body()!!
                val loading = LoadingProcessState()
                loading.loading = false
                loading.message = result.messageResult
                loading.resultOk = result.codeResult == 200
                loading.terminate = true
                loadingStateGuardar.postValue(loading)

            } else {
                val loading = LoadingProcessState()
                loading.loading = false
                loading.message = response.message()
                loading.resultOk = false
                loading.terminate = true
                loadingStateGuardar.postValue(loading)
            }


        }

    }


    private fun CargaMedios() {
        val medioRetro = MediosRetro()

        GlobalScope.launch {
            val log = LoadingProcessState()
            log.message = ""
            log.terminate = false
            log.resultOk = false
            log.loading = true
            loadingStateCarga.postValue(log)

        }
        try {
            medioRetro.iMediosRetro.listRepos(GetValueCodeCia(), Constantes.TipoPedidoWeb.idTipoMovimiento)
                    .enqueue(object : Callback<MediosSolicitud> {
                        override fun onResponse(call: Call<MediosSolicitud>, response: Response<MediosSolicitud>) {
                            listMedio.postValue(response.body()!!.medios!!)
                            activoWeb.postValue((response.body()!!.imagenesVisible))
                            GlobalScope.launch {
                                val log = LoadingProcessState()
                                log.message = ""
                                log.terminate = false
                                log.resultOk = false
                                log.loading = false
                                loadingStateCarga.postValue(log)
                            }

                        }

                        override fun onFailure(call: Call<MediosSolicitud>, t: Throwable) {
                            Log.i("size_medio", "error")
                            GlobalScope.launch {
                                val log = LoadingProcessState()
                                log.message = ""
                                log.terminate = false
                                log.resultOk = false
                                log.loading = false
                                loadingStateCarga.postValue(log)

                            }
                        }
                    })
            Log.i("size_medio", "Ejecuta")
        } catch (ex: Exception) {
            Log.i("error_retro", ex.toString())
        }
    }

}

