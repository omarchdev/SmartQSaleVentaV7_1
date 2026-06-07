package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ConfiRetrofitTimeOut
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio
import com.omarchdev.smartqsale.smartqsaleventas.Model.mSubCategoria
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ISubCategoriaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsyncSubCategorias {

    var resultadoSubCategorias: ResultadoSubCategorias? = null
    var listenerConfigSubCategoria: ListenerConfigSubCategoria? = null

    private val codeCia = GetJsonCiaTiendaBase64x3()
    private val retro = Retrofit.Builder()
        .baseUrl(BASE_URL_API)
        .client(ConfiRetrofitTimeOut.okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val repository = retro.create(ISubCategoriaRepository::class.java)

    interface ResultadoSubCategorias {
        fun ErrorBusqueda()
        fun ResultadoBusqueda(listaSubCategoria: MutableList<mSubCategoria>?)
    }

    interface ListenerConfigSubCategoria {
        fun errorEditar()
        fun editarSubCategoria(subCategoria: mSubCategoria)
        fun subCategoriaAgregada(subCategoria: mSubCategoria)
        fun errorAgregarSubCategoria()
        fun ResultadoEliminarExito(mSubCategoria: mSubCategoria)
        fun ErrorEliminar()
    }

    fun ObtenerSubCategorias(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = repository.GetSubCategorias(codeCia, TIPO_CONSULTA, id).execute()
                val list = response.body()
                launch(Dispatchers.Main) {
                    if (list != null) {
                        resultadoSubCategorias?.ResultadoBusqueda(list.toMutableList())
                    } else {
                        resultadoSubCategorias?.ErrorBusqueda()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    resultadoSubCategorias?.ErrorBusqueda()
                }
            }
        }
    }

    fun AgregarSubCategoria(id: Int, descripcion: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val sub = mSubCategoria(0, descripcion)
            sub.idCategoria = id
            val sol = SolicitudEnvio(codeCia, TIPO_CONSULTA, sub)
            try {
                val response = repository.AgregarSubCategoria(sol).execute()
                val added = response.body()
                launch(Dispatchers.Main) {
                    if (added != null && added.idSubCategoria != -99) {
                        listenerConfigSubCategoria?.subCategoriaAgregada(added)
                    } else {
                        listenerConfigSubCategoria?.errorAgregarSubCategoria()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    listenerConfigSubCategoria?.errorAgregarSubCategoria()
                }
            }
        }
    }

    fun EditarSubCategoria(subCategoria: mSubCategoria) {
        GlobalScope.launch(Dispatchers.IO) {
            val sol = SolicitudEnvio(codeCia, TIPO_CONSULTA, subCategoria)
            try {
                val response = repository.EditarSubCategoria(sol).execute()
                val edited = response.body()
                launch(Dispatchers.Main) {
                    if (edited != null && edited.idSubCategoria != -99) {
                        listenerConfigSubCategoria?.editarSubCategoria(edited)
                    } else {
                        listenerConfigSubCategoria?.errorEditar()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    listenerConfigSubCategoria?.errorEditar()
                }
            }
        }
    }

    fun EliminarSubCategoria(subCategoria: mSubCategoria) {
        GlobalScope.launch(Dispatchers.IO) {
            val sol = SolicitudEnvio(codeCia, TIPO_CONSULTA, subCategoria)
            try {
                val response = repository.EliminarSubCategoria(sol).execute()
                val result = response.body()
                launch(Dispatchers.Main) {
                    if (result != null && result.codeResult == 100) {
                        listenerConfigSubCategoria?.ResultadoEliminarExito(subCategoria)
                    } else {
                        listenerConfigSubCategoria?.ErrorEliminar()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    listenerConfigSubCategoria?.ErrorEliminar()
                }
            }
        }
    }
}
