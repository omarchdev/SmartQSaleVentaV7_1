package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class ControlArchivoTxt() {


    fun GenerarArchivo(mensaje:String,archivo:String){
        var f=File(Constantes.PATH.DIRFILE,archivo)
        var a=Existe(archivo)
        if(!a) {
            f.createNewFile()
        }
        var fw=FileWriter(f,false)
        fw.write(mensaje)
        fw.close()
    }

    fun BorrarArchivo(archivo:String){

        val f=File(Constantes.PATH.DIRFILE,archivo)
        if(Existe(archivo)){
            f.delete()
        }
    }

    fun Archivo(){

  /*      var filename=""
        var directory: File
        if (filename.isEmpty()) {
            directory = context.getFilesDir()
        } else {
            directory = context.getDir(filename, MODE_PRIVATE)
        }
        val files = directory.listFiles()
*/


        var f=File(Constantes.PATH.DIRFILE,"ia.txt")
        var a=Existe("ia.txt")
        if(!a) {
            f.createNewFile()


        }

        if (Existe("ia.txt")) {

           try {

                var c= Calendar.getInstance()
              /*  var z=Calendar.getInstance()
                var r=c.timeInMillis

                var fw=FileWriter(f,false)
                fw.write("A\n")
                fw.write("1\n")
                fw.write(c.timeInMillis.toString()+"\n")
                fw.write("1111\n")
                fw.flush()
                fw.close()
                */

                var fr=FileReader(f)
                var bf=BufferedReader(fr)

                var s:String?=null
                var g=""
               var periodo=""
                var pin=""
                var time=""
                var estado=""
                var rr=0
                s=bf.readLine()

                while(s!=null){
                    when(rr){
                        0->estado=s
                        1->periodo=s
                        2->time=s
                        3->pin=s
                    }
                    rr++
                    s=bf.readLine()

                }
                if(time.isNotEmpty()){
                    var xx=time.toLong()
                    c.timeInMillis=xx
                    c.add(Calendar.HOUR_OF_DAY,periodo.toInt())
                    var g=Calendar.getInstance().time.toString()
                    g.length
                    var h=c.time.toString()

                    if(c.time.compareTo(Calendar.getInstance().time)>0){

                        h.length
                    }else{

                        h.length
                    }
                }

                            /*  var fos=FileOutputStream(f)
                fos.write("".toByteArray())

                fos.close()
*/
            }catch (e:Exception){
                e.toString()
            }
        }
    }

    fun LeerArchivo(archivo:String):ArrayList<String>{
        var list=ArrayList<String>()
        var f=File(Constantes.PATH.DIRFILE,archivo)
        var a=Existe(archivo)
        if(a){
            var fr=FileReader(f)
            var bf=BufferedReader(fr)
            var s:String?=bf.readLine()
            while(s!=null){
                list.add(s)
                s=bf.readLine()
           }
        }
        return list

    }

     fun Existe(nombre:String):Boolean{

        var f= File(Constantes.PATH.DIRFILE,nombre)
        return f.exists()
    }


    fun GenerarArchivo(nombre: String){



    }

}
