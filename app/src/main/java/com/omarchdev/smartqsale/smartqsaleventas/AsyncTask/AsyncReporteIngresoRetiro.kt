package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask


import android.content.Context
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Html
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.PdfGenerator
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDetalleMovCaja
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.record.aggregates.RowRecordsAggregate.createRow
import android.R.attr.data
import org.apache.poi.hssf.record.aggregates.RowRecordsAggregate.createRow
import android.R.attr.data
import android.os.Environment
import android.widget.Toast
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA
import org.apache.poi.hssf.record.aggregates.RowRecordsAggregate.createRow
import org.apache.poi.ss.usermodel.Cell
import java.io.File
import java.io.FileOutputStream

import android.support.v4.content.ContextCompat.startActivity
import org.apache.poi.hssf.usermodel.HeaderFooter.file
import android.content.Intent
import android.net.Uri
import org.apache.poi.ss.usermodel.CreationHelper
*/
import java.util.*




class AsyncReporteIngresoRetiro(context: Context){
    val context=context
    private val generarPdfGenerator=PdfGenerator(context)
    private val html= Html()
    private val listaIngresos=ArrayList<mDetalleMovCaja>()
    private val listaRetiros=ArrayList<mDetalleMovCaja>()
    private val tabla=ArrayList<ArrayList<String>>()
    private val cabecera=ArrayList<String>()
    private var textoHtml=""
    private val controladorProcesoCargar=ControladorProcesoCargar(context)

    fun ObtenerReporteIngresosRetirosPeriodoFecha(fechaI:String,fechaF:String){

        controladorProcesoCargar.IniciarDialogCarga("Generando Reporte")


         GlobalScope.launch  {

             var lista=BdConnectionSql.getSinglentonInstance().MovimientosCajaPorPeriodoFecha(fechaI,fechaF)
            textoHtml=GenerarReporteIngresosGastos(lista)
           launch(Dispatchers.Main){
            //  CrearExcel(lista)

                controladorProcesoCargar.FinalizarDialogCarga()
                 generarPdfGenerator.GenerarPdf(textoHtml,"Reporte de Ingreso y Salidas")
            }

        }

    }

    private fun GenerarReporteIngresosGastos(IngresosRetiros:List<mDetalleMovCaja>):String{

        html.AgregarTituloCentral("${Constantes.Empresa.nombreTienda}")

        html.AgregarTituloCentral("Reporte de Ingresos y Salidas de Caja")

        listaRetiros.clear()
        listaIngresos.clear()
        listaIngresos.addAll(IngresosRetiros.filter { it->it.tipoRegistro==1.toByte() })
        listaRetiros.addAll(IngresosRetiros.filter { it->it.tipoRegistro==2.toByte() })
        cabecera.clear()
        tabla.clear()


        cabecera.add("Fecha Movimiento")
        cabecera.add("Periodo de Caja")
        cabecera.add("Motivo Ingreso")
        cabecera.add("Descripcion")

        cabecera.add("Monto ${Constantes.DivisaPorDefecto.SimboloDivisa}")


        listaIngresos.forEach {
            var fila=ArrayList<String>()

            fila.add(it.getcFechaTransaccion())
            fila.add("${it.cierre.getcFechaApertura()} - ${it.cierre.getcFechaCierre()} ")
            fila.add(it.descripcion)
            fila.add(it.descripcionMotivo)
            fila.add("${String.format("%.2f",it.monto)}")

            tabla.add(fila)
        }



        html.AgregarTituloIntermedio("Ingresos a caja")
        html.AgregarTabla(cabecera,tabla)

        html.SaltoPagina()

        cabecera.clear()
        tabla.clear()

        cabecera.add("Fecha Movimiento")
        cabecera.add("Periodo de Caja")
        cabecera.add("Motivo Retiro")
        cabecera.add("Descripcion")
        cabecera.add("Monto ${Constantes.DivisaPorDefecto.SimboloDivisa}")

         listaRetiros.forEach {

            var fila=ArrayList<String>()
            fila.add(it.getcFechaTransaccion())
             fila.add("${it.cierre.getcFechaApertura()} - ${it.cierre.getcFechaCierre()} ")
             fila.add(it.descripcion)
            fila.add(it.descripcionMotivo)
            fila.add("${String.format("%.2f",it.monto)}")

            tabla.add(fila)
        }

        html.AgregarTituloIntermedio("Retiros de caja")
        html.AgregarTabla(cabecera,tabla)

        return html.ObtenerHtml()
    }


  /*  private fun CrearExcel(IngresosRetiros:List<mDetalleMovCaja>){
        try {
            val workbook = HSSFWorkbook()
            val sheet = workbook.createSheet()
            val sheet2=workbook.createSheet()
            val tipo1:Byte=1
            val tipo2:Byte=2

            var listaIngreso=IngresosRetiros.filter {it-> it.tipoRegistro==tipo1}
            var listaRetiro=IngresosRetiros.filter { it->it.tipoRegistro==tipo2 }
            cabecera.clear()
            cabecera.add("Fecha Ingreso")
            cabecera.add("Descripcion")
            cabecera.add("Observacion")
            cabecera.add("Monto ${Constantes.DivisaPorDefecto.SimboloDivisa}")
            workbook.setSheetName(0, "Ingresos a caja")
            workbook.setSheetName(1,"Retiros de caja")

            val headerStyle = workbook.createCellStyle()
            val font = workbook .createFont()
            font.boldweight = Font.BOLDWEIGHT_BOLD
            val createHelper = workbook.getCreationHelper()

            headerStyle.setFont(font)
            val style = workbook.createCellStyle()
            val cellStyleNum=workbook.createCellStyle()
            cellStyleNum.dataFormat=createHelper.createDataFormat().getFormat("0.00")
            style.fillPattern = CellStyle.SOLID_FOREGROUND
            val headerRow = sheet.createRow(0)
            for (i in 0 until cabecera.size) {
                val header = cabecera.get(i)
                val cell = headerRow.createCell(i)
                cell.setCellStyle(headerStyle)
                cell.setCellValue(header)
            }



            for (i in 0 until listaIngreso.size) {
                val dataRow = sheet.createRow(i + 1)
                 dataRow.createCell(0).setCellValue(listaIngreso.get(i).getcFechaTransaccion())
                dataRow.createCell(1).setCellValue(listaIngreso.get(i).descripcion)
                dataRow.createCell(2).setCellValue(listaIngreso.get(i).descripcionMotivo)
                var q= dataRow.createCell(3)
                q.setCellValue(listaIngreso.get(i).monto.toDouble())
                q.setCellStyle(cellStyleNum)

            }
            val dataRow = sheet.createRow(1 + listaIngreso.size)
            val total = dataRow.createCell(3)
            total.cellType = Cell.CELL_TYPE_FORMULA
            total.setCellStyle(cellStyleNum)
            total.cellFormula = String.format("SUM(D2:D%d)", 1 +listaIngreso.size)

            cabecera.clear()
            cabecera.add("Fecha Retiro")
            cabecera.add("Descripcion")
            cabecera.add("Observacion")
            cabecera.add("Monto ${Constantes.DivisaPorDefecto.SimboloDivisa}")

            val headerRow2 = sheet2.createRow(0)

            for (i in 0 until cabecera.size) {
                val header = cabecera.get(i)
                val cell = headerRow2.createCell(i)
                cell.setCellStyle(headerStyle)
                cell.setCellValue(header)
            }
            for (i in 0 until listaRetiro.size) {
                val dataRow = sheet2.createRow(i + 1)

                dataRow.createCell(0).setCellValue(listaRetiro.get(i).getcFechaTransaccion())
                d   ataRow.createCell(1).setCellValue(listaRetiro.get(i).descripcion)
                dataRow.createCell(2).setCellValue(listaRetiro.get(i).descripcionMotivo)
                var q= dataRow.createCell(3)
                q.setCellValue(listaRetiro.get(i).monto.toDouble())
                q.setCellStyle(cellStyleNum)
            }

            val dataRow2 = sheet2.createRow(1 + listaRetiro.size)
            val total2 = dataRow2.createCell(2)
            total2.cellType = Cell.CELL_TYPE_FORMULA
            total2.setCellStyle(cellStyleNum)
            total2.cellFormula = String.format("SUM(D2:D%d)", 1 +listaRetiro.size)
            val c = Calendar.getInstance()
             var extension=".xls"
            var nombre="Reporte_${c.get(Calendar.MINUTE)}${c.get(Calendar.SECOND)}${c.get(Calendar.YEAR)}" +
                    "${c.get(Calendar.MONTH)+1}${c.get(Calendar.DAY_OF_MONTH)}"
            var f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"$nombre$extension")
            f.createNewFile()
            f.mkdir()

            val file = FileOutputStream(f,true)

            workbook.write(file)
           file.close()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(f),"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            context.startActivity(intent)
        }catch (e:Exception){
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }*/

}