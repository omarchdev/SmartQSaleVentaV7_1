package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.operarioconfig

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA
import com.omarchdev.smartqsale.smartqsaleventas.Model.*
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IOperarioRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OperarioConfigViewModel(idOperario: Int) : ViewModel() {


    val codeCia = GetJsonCiaTiendaBase64x3()
    val nombres = MutableLiveData<String>("")
    val apellidos = MutableLiveData<String>("")
    val retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val iOperarioRepository = retro.create(IOperarioRepository::class.java)
    val processCargaInit = MutableLiveData<LoadingProcessState>().apply {
        val sState = LoadingProcessState()
        sState.message = ""
        sState.resultOk = false
        sState.terminate = false
        sState.loading = false
        value = sState
    }

    val processCargaGuardar = MutableLiveData<LoadingProcessState>().apply {
        val sState = LoadingProcessState()
        sState.message = ""
        sState.resultOk = false
        sState.terminate = false
        sState.loading = false
        value = sState
    }

    val processCargaEliminar = MutableLiveData<LoadingProcessState>().apply {
        val sState = LoadingProcessState()
        sState.message = ""
        sState.resultOk = false
        sState.terminate = false
        sState.loading = false
        value = sState
    }


    val operarioData: MutableLiveData<mOperario> by lazy {
        MutableLiveData<mOperario>().also {
            GetIdOperario(idOperario)
        }
    }


    fun GuardarOperario() {

        GlobalScope.launch {
            val process1 = LoadingProcessState()
            process1.message = ""
            process1.resultOk = false
            process1.terminate = false
            process1.loading = true
            processCargaGuardar.postValue(process1)
            var resultProcces = ResultProcces()
            val operario = mOperario()
            operario.PrimerNombre=nombres.value!!
            operario.ApellidoPaterno=apellidos.value!!
            var call: Call<ResultProcces>? = null
            if (operarioData.value!!.idOperario == 0) {
                val solicitudEnvio = SolicitudEnvio(
                    codeCia,
                    TIPO_CONSULTA, operario,
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario
                )
                var soltemp= Gson().toJson(solicitudEnvio)
                call = iOperarioRepository.InsertaOperario(solicitudEnvio)
            } else {

                operario.idOperario=operarioData.value!!.idOperario

                val solicitudEnvio = SolicitudEnvio(
                    codeCia,
                    TIPO_CONSULTA, operario,
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario
                )
                var soltemp= Gson().toJson(solicitudEnvio)
                call = iOperarioRepository.ActualizaOperario(solicitudEnvio)
            }
            var response = call.execute()
            var result = response.body()

            val process2 = LoadingProcessState()
            process2.message = result!!.messageResult
            process2.resultOk = true
            process2.terminate = true
            process2.loading = false
            processCargaGuardar.postValue(process2)

        }


    }


    fun EliminarOperario() {
        GlobalScope.launch {

            val process1 = LoadingProcessState()
            process1.message = ""
            process1.resultOk = false
            process1.terminate = false
            process1.loading = true
            processCargaEliminar.postValue(process1)
            val result = iOperarioRepository.EliminaOperario(
                codeCia,
                TIPO_CONSULTA,
                operarioData.value!!.idOperario
            ).execute().body()

            val process2 = LoadingProcessState()
            process2.message = result!!.messageResult
            process2.resultOk = true
            process2.terminate = true
            process2.loading = false
            processCargaEliminar.postValue(process2)
        }
    }

    private fun GetIdOperario(idOperario: Int) {

        GlobalScope.launch {

            val process1 = LoadingProcessState()
            process1.message = ""
            process1.resultOk = false
            process1.terminate = false
            process1.loading = true
            processCargaInit.postValue(process1)

            if (idOperario != 0) {
                val callable = iOperarioRepository.GetOperarioId(codeCia, TIPO_CONSULTA, idOperario)
                val response = callable.execute()
                val operario = response.body()

                nombres.postValue(operario?.PrimerNombre)
                apellidos.postValue(operario?.ApellidoPaterno)
                operarioData.postValue(operario)
            } else {
                val operario = mOperario()

                nombres.postValue(operario.PrimerNombre)
                apellidos.postValue(operario.ApellidoPaterno)
                operarioData.postValue(operario)
            }


            val process2 = LoadingProcessState()
            process2.message = ""
            process2.resultOk = true
            process2.terminate = true
            process2.loading = false
            processCargaInit.postValue(process2)
        }


    }

    fun setNombres(nombre: String) {
        if (this.nombres.value != nombre)
            this.nombres.value = nombre
    }

    fun setApellidos(apellidos: String) {
        if (this.apellidos.value != apellidos)
            this.apellidos.value = apellidos
    }


    fun IngresaOperario() {

        val operario = mOperario()
        val solicitudEnvio = SolicitudEnvio<mOperario>(
            codeCia,
            TIPO_CONSULTA,
            operario,
            Constantes.Terminal.idTerminal,
            Constantes.Usuario.idUsuario
        )
        var result = iOperarioRepository.InsertaOperario(solicitudEnvio).execute().body()

    }


}

class OperarioConfigViewModelViewModelFactory(val idOperario: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OperarioConfigViewModel(idOperario) as T
    }

}
