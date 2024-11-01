package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configwebimage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.graphics.Bitmap

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController
import com.omarchdev.smartqsale.smartqsaleventas.Model.LoadingProcessState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigWebImageViewModel : ViewModel() {

    private val imgController= ImagenesController()
    val loadingProcessInit= MutableLiveData<LoadingProcessState>().apply {
         val sState= LoadingProcessState()
        sState.message=""
        sState.resultOk=false
        sState.terminate=false
        sState.loading=false
         value=sState
    }
    val loadingProcessSave=MutableLiveData<LoadingProcessState>().apply {
        val sState= LoadingProcessState()
        sState.message=""
        sState.resultOk=false
        sState.terminate=false
        sState.loading=false
        value=sState
    }
    val imgLog:MutableLiveData<Bitmap>by lazy {
        MutableLiveData<Bitmap>().also {
            GetLogoCompany()
        }
    }

    fun SetImage(img:Bitmap){
        imgLog.value=img
    }

    fun GetLogoCompany(){

        GlobalScope.launch {
            val sStateInit= LoadingProcessState()
            sStateInit.message=""
            sStateInit.resultOk=false
            sStateInit.terminate=false
            sStateInit.loading=true
            loadingProcessInit.postValue(sStateInit)

            val result:ByteArray?=BdConnectionSql.getSinglentonInstance().GetLogoCompany()
            if(result!=null){
               val img= imgController.ConvertByteArrayToBitmap(result)
                imgLog.postValue(img)
            }

            val sStateEnd= LoadingProcessState()
            sStateEnd.message=""
            sStateEnd.resultOk=true
            sStateEnd.terminate=true
            sStateEnd.loading=false
            loadingProcessInit.postValue(sStateEnd)
         }

    }

    fun UpdateImage(){
        GlobalScope.launch {
            val sStateInit= LoadingProcessState()
            sStateInit.message=""
            sStateInit.resultOk=false
            sStateInit.terminate=false
            sStateInit.loading=true
            loadingProcessSave.postValue(sStateInit)

            var guardo=true
            if(imgLog.value!=null){
                val byteArray=imgController.convertBitmapToByteArrayPNG(imgLog!!.value)
                val result=BdConnectionSql.getSinglentonInstance().UpdateLogoCompany(byteArray)
                guardo=result

            }
            val sStateEnd= LoadingProcessState()
            if(guardo){
                sStateEnd.message="Se actualizó su logo en éxito"
                sStateEnd.resultOk=true
            }else{
                sStateEnd.message="Hubo un inconveniente al guardar el logo. Verifique su conexión a internet."
                sStateEnd.resultOk=false
            }

            sStateEnd.terminate=true
            sStateEnd.loading=false
            loadingProcessSave.postValue(sStateEnd)
        }
    }
}
