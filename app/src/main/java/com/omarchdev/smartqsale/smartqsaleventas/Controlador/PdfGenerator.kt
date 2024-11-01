package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import android.content.Context
import android.os.Build
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.annotation.RequiresApi
import com.omarchdev.smartqsale.smartqsaleventas.R
import java.io.File
import java.io.IOException
import java.util.*

class PdfGenerator(context: Context){

    val context=context

    internal val c = Calendar.getInstance()

    val nombreFichero="SmartQSale"

    @Throws(IOException::class)
    fun crearFichero(nombreFichero: String): File? {
        val ruta = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getRuta()
        } else {
            TODO("VERSION.SDK_INT < KITKAT")
        }
        var fichero: File? = null
        if (ruta != null)
            fichero = File(ruta, nombreFichero)
        return fichero
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getRuta(): File? {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        var ruta: File? = null
        if (Environment.MEDIA_MOUNTED == Environment
                        .getExternalStorageState()) {
            ruta = File(
                    Environment
                            .getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOCUMENTS),
                    nombreFichero)

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null
                    }
                }
            }
        } else {
        }

        return ruta
    }





    fun GenerarPdf(html:String="", nombreDoc:String=""){/*
        try {
            val pdf = HtmlToPdf()
            val tmpDir: String = System.getProperty("java.io.tmpdir")
            var f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "$nombreDoc.pdf")
            pdf.convert(html, f)


        }catch (e:Exception){
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show()
        }
*/
/*

        var doc=com.itextpdf.text.Document()
        var pdfWriter=PdfWriter.getInstance(doc,outputStream)

        doc.open()
         doc.addAuthor(Constantes.Empresa.nombrePropietario)
        doc.addCreationDate()
        doc.addProducer()
        doc.addCreator("SmartQSale")
        doc.setPageSize(PageSize.A4)


        doc.close()
  */    val webView = WebView(context)
        var mWebView: WebView? = null
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                mWebView = null
            }
        }

        webView.loadDataWithBaseURL("Texto", html, "text/HTML", "UTF-8", null)
          mWebView = webView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val printManager = context
                    .getSystemService(Context.PRINT_SERVICE) as PrintManager
            val printAttributes= PrintAttributes.Builder()
            printAttributes.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            var printAdapter: PrintDocumentAdapter? = null
            // Get a print adapter instance

            printAdapter = webView.createPrintDocumentAdapter("SQ"
                    + "_${nombreDoc}_" + c.get(Calendar.YEAR).toString() + (c.get(Calendar.MONTH) + 1).toString()
                    +  c.get(Calendar.DAY_OF_MONTH).toString()
            )

            // Create a print job with name and adapter instance
            val jobName = context.getString(R.string.app_name) + " Document"
            val printJob = printManager.print(jobName, printAdapter!!,
                    printAttributes.build())

        }
    }


}