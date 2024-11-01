package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes


class ExternalApps(context:Context){

    val context=context


    fun  EnvioMensajeWhatsapp(contentMensaje:String){
        try{

            val msgurl = contentMensaje
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(msgurl)
            context.startActivity(i)

        }catch (ex:Exception){

        }
    }



    fun AperturaWhatsapp(){
  /*      val msgurl = "https://api.whatsapp.com/send?phone=${Constantes.Contacto.numContacto}=Hello"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(msgurl)
        context.startActivity(i)
*/
/*
        val sendIntent = Intent()
        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
*/
        if(estaInstaladaAplicacion("com.whatsapp")){
            val uri = Uri.parse("smsto:${Constantes.Contacto.numContacto}")
            val i = Intent(Intent.ACTION_SENDTO, uri)
            i.`package` = "${Constantes.Contacto.COM_WHATSAPP}"
            context.startActivity(i)
        }else   if(estaInstaladaAplicacion(Constantes.Contacto.COM_WHATSAPP)){
            val uri = Uri.parse("smsto:${Constantes.Contacto.numContacto}")
            val i = Intent(Intent.ACTION_SENDTO, uri)
            i.`package` = "${Constantes.Contacto.COM_WHATSAPP}"
            context.startActivity(i)
//esta instalada.
        }else{
//no esta instalada.
            Toast.makeText(context,"Debe instalar la aplicación Whatsapp", Toast.LENGTH_LONG).show()
        }
    }

    private fun estaInstaladaAplicacion(nombrePaquete: String): Boolean {

        val pm = context.getPackageManager()
        try {
            pm.getPackageInfo(nombrePaquete, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }

    }

    @SuppressLint("MissingPermission")
    fun LlamarServicioTecnico(){
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",Constantes.Contacto.numContacto, null))
        //var intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constantes.Contacto.numContacto))
        context.startActivity(intent)
    }
}