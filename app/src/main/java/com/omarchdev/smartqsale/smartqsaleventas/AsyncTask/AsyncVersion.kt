package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import java.io.File
import kotlin.TypeCastException
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.os.Environment
import android.os.Handler
import kotlin.jvm.internal.Intrinsics
import android.content.IntentFilter
import androidx.core.app.NotificationCompat
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AsyncVersion(context: Context){

    val context=context
    var versionListener:VerificarVersion?=null
    private var permitir=true

    private var queueid: Long? = null

    var nombre=""
    private var handler: Handler? = null
    private var dm: DownloadManager? = null
    private var downloadId: Long = 0
    private var versionCode=0
    private var versionName=""
    private var packageName=""
    val controladorProcesoCargar=ControladorProcesoCargar(context)
    interface VerificarVersion{

        fun ErrorConexion()
        fun ErrorVerificar() 
        fun VersionActualizada()
        fun VersionNoActualizada()
    }

   fun verificarVersionApp(versionCode:Int,versionName:String,packageName:String){

       this.versionCode=versionCode
       this.versionName=versionName
       this.packageName=packageName

         GlobalScope.launch  {
            var respuestaVersion=BdConnectionSql.getSinglentonInstance().VerificarVersion(versionName,versionCode,packageName)
           launch(Dispatchers.Main){
                when(respuestaVersion) {
                  Constantes.EstadoApp.actualizado->versionListener?.VersionActualizada()
                  Constantes.EstadoApp.noactualizado->versionListener?.VersionNoActualizada()
                  Constantes.EstadoApp.errorVerificar->versionListener?.ErrorVerificar()
                  Constantes.EstadoApp.errorConexion->versionListener?.ErrorConexion()

                }
            }
        }
    }


    fun BuscarActualizacion(packageName:String){

        controladorProcesoCargar?.IniciarDialogCarga("Descargando la actualizacion")
         GlobalScope.launch  {
            var url=BdConnectionSql.getSinglentonInstance().URL_New_Version(packageName)
           launch(Dispatchers.Main){
                if(url.link.isNotEmpty()){
            //        var controlArchivoTxt=ControlArchivoTxt()
          //          controlArchivoTxt.BorrarArchivo(url.name)
                    DownloadApp(url.link,url.name,"Descargando SmartQSale")
                }else{
                    controladorProcesoCargar?.FinalizarDialogCarga()
                }
            }
        }

    }
    fun ActualizarVersionApp(){
        var intent:Intent?=null
        controladorProcesoCargar.IniciarDialogCarga("Buscando Actualizaciones")
         GlobalScope.launch  {
            var conexion=BdConnectionSql.getSinglentonInstance().getHttpNewVersion(packageName,versionName,versionCode)
            if(conexion.trim().equals("N.A.")){
                permitir=false
            }else if(conexion.trim().equals("N.O.")) {
                permitir=false
            }else if(conexion.trim().equals("E.C.")){
                permitir=false
            }else if(conexion.trim().equals("N.I.")) {
                permitir=false
            }else {
                permitir=true
            }
            if(permitir) {
                var uri = Uri.parse(conexion)
                intent= Intent(Intent.ACTION_VIEW,uri)
            }
           launch(Dispatchers.Main){
                controladorProcesoCargar.FinalizarDialogCarga()
                if(permitir)
                context.startActivity(intent)
            }
        }
    }

    fun EliminarArchivo(context: Context, name: String) {

        val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val stringBuilder = StringBuilder()
        stringBuilder.append(name)
        val f = File(externalFilesDir, stringBuilder.toString())
        if (f.exists()) {
            f.delete()
        }

    }

    fun verificarArchivo(name: String, extension: String): String {
        var count = 0
        var nombre = ""
        val stringBuilder = StringBuilder()
        stringBuilder.append(name)
        stringBuilder.append(extension)
        nombre = stringBuilder.toString()
        var a = File(Constantes.PATH.DIRFILE, nombre)
        while (!a.exists()) {
            count++
            val stringBuilder2 = StringBuilder()
            stringBuilder2.append(name)
            stringBuilder2.append('_')
            stringBuilder2.append(count)
            stringBuilder2.append('.')
            stringBuilder2.append(extension)
            nombre = stringBuilder2.toString()
            a = File(Constantes.PATH.DIRFILE, nombre)
        }
        return nombre
    }

    private fun startDownload(downloadPath: String, destinationPath: String,name:String) {
        val uri = Uri.parse(downloadPath) // Path where you want to download file.
        val request = DownloadManager.Request(uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)  // This will show notification on top when downloading the file.
        request.setTitle("Descargando actualizacion SmartQSale") // Title for notification.
        request.setVisibleInDownloadsUi(true)

        request.setDestinationUri(Uri.parse(destinationPath+name))
        //request.setDestinationInExternalFilesDir(context,destinationPath,name)
        //   request.setDestinationInExternalPublicDir(destinationPath, uri.lastPathSegment,)  // Storage directory path
        (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)

        //(getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request) // This will start downloading
    }

    private fun DownloadApp(url: String, name: String, workTitle: String) {
        val systemService = this.context.getSystemService(Context.DOWNLOAD_SERVICE)
        if (systemService != null) {
            this.dm = systemService as DownloadManager
            val request = DownloadManager.Request(Uri.parse(url))
            val context = this.context
            val str = Environment.DIRECTORY_DOWNLOADS
            val stringBuilder = StringBuilder()
            stringBuilder.append(name)
            request.setDescription("SmartQSale")
            request.setDestinationInExternalFilesDir(context, str, stringBuilder.toString())
            request.setTitle(workTitle)
            request.setVisibleInDownloadsUi(true)
            val downloadManager = this.dm
            this.queueid = if (downloadManager != null) java.lang.Long.valueOf(downloadManager!!.enqueue(request)) else null
             EliminarArchivo(this.context, name)
            this.handler = Handler()
            val stringBuilder2 = StringBuilder()
            stringBuilder2.append(name)
            ActualizarApp(stringBuilder2.toString())
            return
        }
        throw TypeCastException("null cannot be cast to non-null type android.app.DownloadManager")
    }

    private fun ActualizarApp(nameApp: String) {
        this.context.registerReceiver(object: BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {

                var file: File? = null
                if ("android.intent.action.DOWNLOAD_COMPLETE" == (if (p1!= null) p1!!.getAction() else null)) {
                    val query = DownloadManager.Query()
                    val jArr = LongArray(1)
                    val queueid =queueid

                    jArr[0] = queueid!!.toLong()
                    query.setFilterById(*jArr)
                    var c = dm!!.query(query)
                    //c = c!!.query(query)
                    if (c!!.moveToFirst()) {
                        val columnIndex = c!!.getColumnIndex(NotificationCompat.CATEGORY_STATUS)
                        if (8 == c!!.getInt(columnIndex)) {
                            val uriString = c!!.getString(c!!.getColumnIndex("local_uri"))
                            val intent2 = Intent("android.intent.action.VIEW")
                            if (context != null) {
                                file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                            }
                            intent2.setDataAndType(Uri.fromFile(File(file, nameApp)), "application/vnd.android.package-archive")
                            intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            if (context == null) {
                                Intrinsics.throwNpe()
                            }
                            context.startActivity(intent2)
                        } else if (1 == c!!.getInt(columnIndex)) {
                            Toast.makeText(context, "Pendiente", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

        },
                IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"))

    }
}