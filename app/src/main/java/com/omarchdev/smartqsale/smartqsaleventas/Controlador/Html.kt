package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.graphics.Bitmap
import android.util.Base64
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes

import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController
import kotlin.collections.ArrayList


class Html(){

   private val inicio="<html><body>"
    private val final="</body></html>"
    private  val IetiquetaPcenter="<p Align='center'>"
    private val FetiquetaP="</p>"
    private val imgController=ImagenesController()
    private val IetiquetaDiv="<div style='border: 1px solid #ccc ; border-radius: 4px;'>"
    private val FetiquetaDiv="</div>"


     private var cuerpo=""

    fun limpiarHtml(){
        cuerpo=""
    }

    fun SaltoPagina(){
     cuerpo="$cuerpo<div style='page-break-after:always'></div>"
    }

    fun SaltoLinea(){
        cuerpo="$cuerpo<br/>"
    }

    fun AgregarTituloIntermedio(titulo:String?){
        cuerpo="$cuerpo<h3 Align=left>$titulo</h3><br/>"
    }

    private fun Titulo(t:String):String{
        return "<h3 Align='center'>$t</h3>"
    }


    fun AgregarTabla(Cabeceras:ArrayList<String>,Registros:ArrayList<ArrayList<String>>){
        val tabla=TableHtml()
        cuerpo="$cuerpo${tabla.TablaH(Cabeceras,Registros)}"
    }

    fun AgregarDiv2ColumnasTablas(CT1:String,titulo1:ArrayList<String>,list:ArrayList<ArrayList<String>>,CT2:String,titulo2:ArrayList<String>,listb:ArrayList<ArrayList<String>>){
        val tabla1=TableHtml()
         cuerpo="$cuerpo<div  style='box-sizing: border-box; padding:10px ; '>" +
                "<div  align='center' style='float: left;width: 50%; padding: 10px; height: 350px; box-sizing: border-box;'>${Titulo(CT1)}${tabla1.TablaH(titulo1,list)}" +
                 "</div>" +
                "<div  align='center' style='float: left;width: 50%; padding: 10px; height: 350px;box-sizing: border-box;'>${Titulo(CT2)}" +
                 "${tabla1.TablaH(titulo2,listb)}</div></div>"


    }


    fun AgregarDiv2ColumnasImagenes(bmp1:Bitmap,titulo1: String,bmp2:Bitmap,titulo2:String){
        var b1=imgController.convertBitmapToByteArray80(bmp1)
        var b2:ByteArray=imgController.convertBitmapToByteArray(bmp2)
        var s1=Base64.encodeToString(b1,Base64.DEFAULT)
        var s2=Base64.encodeToString(b2,Base64.DEFAULT)
        cuerpo="$cuerpo<div  style='box-sizing: border-box; padding:10px ;margin-bottom:30px; '>" +
                "<div  align='center' style='float: left;width: 50%;margin-bottom:80px; padding: 10px; height: 350px; box-sizing: border-box;'>  " +
                "${Titulo(titulo1)}" +
                "<img style='width:90%;height=90%' src=\"data:image/png;base64,"+s1+"\" hspace=10 vspace=10 >" +
                "</div>" +

                "<div  align='center' style='float: left;width: 50%;margin-bottom:80px; padding: 10px; height: 350px;box-sizing: border-box;'> " +
                " ${Titulo(titulo2)} <img style='width:90%;height=90%' src=\"data:image/png;base64,"+s2+"\" hspace=10 vspace=10 > " +
                "</div></div><br/>"


    }
    fun AgregarTituloCentral(Titulo:String){
        cuerpo="$cuerpo<h1 Align='center'>$Titulo</h2>"
    }
    fun AgregarSubtitulo(Titulo:String){
        cuerpo="$cuerpo<h2 Align='center'>$Titulo</h2>"
    }
    fun ObtenerHtml():String{
        return "$inicio$cuerpo$final"
    }

    fun AgregarImagen(img: Bitmap){

        var b=imgController.convertBitmapToByteArray80(img)
        var s=Base64.encodeToString(b,Base64.DEFAULT)
        cuerpo="$cuerpo<img style='width:50%;height=50%' src=\"data:image/png;base64,"+s+"\" hspace=10 vspace=10 ><br>"

    }

    fun AgregarDiv(lista:ArrayList<String>){

        cuerpo="$cuerpo ${divCenterContent(lista)}"

    }
    private fun divCenterContent(r:ArrayList<String>):String {
        var c=""
        r.forEachIndexed { i, s ->

            if (i == 0) {
                c = "$c${Titulo(s)}"
            }
            else{
                c = "$c${TextoP(s)}"
            }
        }
        c.length
        return "$IetiquetaDiv $c  $FetiquetaDiv"
    }


    private fun TextoP(r:String):String{
        return "$IetiquetaPcenter $r $FetiquetaP"}

     inner class TableHtml() {
         private val InitTable = "<table style='width:100%;border-collapse: collapse;font-size:60%;'>"
         private val finTable = "</table>"
         private val tr = "<tr style='border: 1px solid #dd; '>"
         private val trA= "<tr style='border: 1px solid #ddd;background-color: #dddddd;'>"
         private val trE = "</tr>"
         private val th = "<th style='border: 1px solid #ddd;background-color: #3693FF;color: white;padding-top: 8px;padding-bottom: 8px;'>"
         private val thE = "</th >"
         private val td = "<td style='border: 1px solid #ddd;text-align:center;padding:6px'>"
         private val tdL="<td style='border: 1px solid #ddd;text-align:left;padding:6px'>"
         private val tdE = "</td>"
         private val registros={
             r:ArrayList<ArrayList<String>>->
             "${r.forEach {
                 registro(it)
             }}"
         }
         private val registro = { r: ArrayList<String> ->
             "$tr${r.forEach {
                 "${datoRegistro(it)}"
             }}$trE"
         }
         private val sRegistro={x:String->"$tr$x$trE"}
         private val sRegistroAl={x:String->"$trA$x$trE"}
         private val datoRegistro = { dato: String -> "$td$dato$tdE" }
         private val datoRegistroLeft={dato:String->"$tdL$dato$tdE"}
         private val datoCabecera = { x: String -> "$th$x$thE" }

         fun CTabla(nombresCabecera:ArrayList<String>):String{

             var c=""
             nombresCabecera.forEach {
                 c="$c${datoCabecera(it)}"
             }
             return "${sRegistro(c)}"
         }
         private fun FilaAl(datosRegistro:ArrayList<String>):String{
             var c=""
             datosRegistro.forEach {
                 if(it.contains(Constantes.EHTML.Left)){

                     c="$c${datoRegistroLeft(it.replace(Constantes.EHTML.Left,""))}"

                 }else{
                     c="$c${datoRegistro(it)}"

                 }
             }
             return "${sRegistroAl(c)}"
         }
         private fun Fila(datosRegistro:ArrayList<String>):String{
             var c=""
             datosRegistro.forEach {
                 if(it.contains(Constantes.EHTML.Left)){
                     c="$c${datoRegistroLeft(it.replace(Constantes.EHTML.Left,""))}"
                 }else{
                     c="$c${datoRegistro(it)}"
                 }
             }
             return "${sRegistro(c)}"
         }
        private fun CrearTabla(datos:ArrayList<ArrayList<String>>):String{
             var cuerpo=""
             datos.forEachIndexed { index, arrayList ->
                 if(index%2==0) {
                     cuerpo = "$cuerpo${Fila(arrayList)}"
                 }else{
                     cuerpo = "$cuerpo${FilaAl(arrayList)}"
                 }
             }
             return cuerpo
         }

         fun TablaH(nombresCabecera:ArrayList<String>,datos:ArrayList<ArrayList<String>>):String{
             return  "$InitTable${CTabla(nombresCabecera)}${CrearTabla(datos)}$finTable"}
         }

}

