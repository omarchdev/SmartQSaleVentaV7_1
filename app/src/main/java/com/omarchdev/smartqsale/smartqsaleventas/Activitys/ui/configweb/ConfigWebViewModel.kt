package com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.configweb
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConfigWebViewModel : ViewModel() {

    val linkBase= Constantes.ConfigTienda.cLinkBaseWeb
    val linkResult=MutableLiveData<String>().apply{postValue(Constantes.ConfigTienda.cLinkBaseWeb)}
    private var urlNuevoPedio=Constantes.ConfigTienda.cLinkAddPedidoNuevo
    var urlIngresado=""
    val resultGuardarOk=MutableLiveData<String>()
    val resultGuardarError=MutableLiveData<String>()
    val UrlInit=MutableLiveData<String>().apply { postValue("") }
    val cargaInit= MutableLiveData<Boolean>().apply { postValue(true) }

    fun CargaUrl(){

        GlobalScope.launch {
            cargaInit.postValue(true)
            var a=BdConnectionSql.getSinglentonInstance().GetCodeWeb()
            cargaInit.postValue(false)
            UrlInit.apply{postValue(a)}

        }
    }

    fun PermiteGuardarLinkTienda():Map<Int,Any>{
        var guardar=true
        var mensaje=""
        var urlUsuario=urlIngresado.trim()
        if(urlUsuario.length<=5 || urlUsuario.length>=25){
             guardar=false
            mensaje="El nombre corto para la web debe contener entre 5 hasta 25 caracteres."
        }
        if(urlUsuario.contains(" ")){
            guardar=false
            mensaje="El nombre corto para la web no debe contener espacios"
        }
        if(urlUsuario.contains("/") || urlUsuario.contains("-") ||
                urlUsuario.contains("@") || urlUsuario.contains("!")
                || urlUsuario.contains("\\") || urlUsuario.contains(",") ){
            guardar=false
            mensaje="El nombre corto para la web no debe contener los siguientes signos ! @ - / , \\"
        }
        UrlInit.value= urlIngresado

        return mapOf(1 to guardar,2 to mensaje)
    }


    fun GuardarUrlLink() {

        GlobalScope.launch {
            var r= BdConnectionSql.getSinglentonInstance().GuardarUrlCompanyTienda(urlIngresado)
            if(r.get(1).toString().toInt()==100){
                resultGuardarOk.apply { postValue(r.get(2).toString())  }
            }else{
                resultGuardarError.apply { postValue(r.get(2).toString())  }
            }
        }
    }

    fun TextoLink(textoIngresado:String):String{
                var link=linkBase.trim()
                var last=link.last()
                if(last!='/'){
                    link=linkBase+"/"
                }

                urlIngresado=textoIngresado.trim()
                return link+ urlNuevoPedio.replace("{0}",textoIngresado.trim())
          //      linkResult.apply { postValue(link+ urlNuevoPedio.replace("{0}",textoIngresado.trim())) }

    }


}
