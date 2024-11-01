
package com.omarchdev.smartqsale.smartqsaleventas.PrintOptions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;

import androidx.annotation.RequiresApi;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ConstructorFacturaKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DocVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.UsbConnect.PrinterJob;

//import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.List;

import static com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.PrinterCommands.ESC_ALIGN_LEFT;

/**
 * Created by OMAR CHH on 29/12/2017.
 */

public class PrintOptions {

    final Calendar c = Calendar.getInstance();
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Context context;
    WebView mWebView;
    final String Cabecera = "Descripcion  C    PU      PT";
    final int numCaracteres55mm = 32;

    public PrintOptions(Context context, OutputStream mmOutputStream, InputStream mmInputStream) {

        this.context = context;
        this.mmOutputStream = mmOutputStream;
        this.mmInputStream = mmInputStream;
    }

    public PrintOptions(Context context) {
        this.context = context;
        mWebView = new WebView(context);
    }


    private void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                    img);
            if (bmp != null) {
                byte[] command = Utils.decodeBitmap(bmp);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }
    //print unicode

    public void printUnicode() {
        try {
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();

        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + 1 + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }

    private String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 31) {
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void imprimirLinea(String a) {


    }

    private void imprimir(String a, String b) {


        String puntos = "";
        String textoImprimir = "";
        int longitudPuntos = 0;

        if (a.length() < 18) {
            longitudPuntos = 32 - (a.length() + b.length());

            for (int i = 0; i < longitudPuntos; i++) {
                puntos = puntos + ".";
            }
            textoImprimir = a + puntos + b;
            printText(textoImprimir);

        } else if (a.length() >= 18) {


            longitudPuntos = 32 - b.length();

            for (int i = 0; i < longitudPuntos; i++) {
                puntos = puntos + ".";
            }
            textoImprimir = puntos + b;
            printText(a);
            printNewLine();
            printText(textoImprimir);

        }


    }

    private void printText(String msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private PrinterJob printNewLineJob() {


        return new PrinterJob(PrinterCommands.FEED_LINE);
    }

    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static final byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33};


    public void PrintReportVendedor(String nombreProducto, String cantidad, String Subtotal) {

        printText(nombreProducto + cantidad + Subtotal);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ReporteVenta(String mensaje) {
        printCustom(Constantes.Empresa.nombreTienda, 2, 1);
        printCustom(context.getResources().getString(R.string.app_name), 1, 1);
        printText(mensaje);

    }


    public String LineaNombreDemo(ProductoEnVenta p) {
        String n = "";
        int count = 0;
        if (p.getProductName().trim().length() <= numCaracteres55mm) {
            n = p.getProductName().trim();
            count = numCaracteres55mm - n.length();
            for (int i = 0; i < count; i++) {
                n = n + " ";
            }
        } else {
            n = p.getProductName().trim().substring(0, numCaracteres55mm);
        }
        return n.replace("ñ", "n").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
    }

    public void CerrarConexion() {
        try {
            this.mmOutputStream.flush();
            this.mmOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ImpresionAdicionalPedido(DocVenta docVenta) {
        try {
            this.mmOutputStream.write(PrinterCommands.OPEN_CASH_DRAWER);

            if (Constantes.ConfigTienda.CabeceraPieTicketAdicional) {
                printCustom("------------------------", 1, 0);
                printCustom(Constantes.Empresa.nombreTienda, 2, 1);
                printCustom(docVenta.getFechaEmision(), 1, 0);
                printCustom(docVenta.getNombreReceptor(), 1, 0);
                if (docVenta.getDireccion() != null) {
                    if (docVenta.getDireccion().length() > 0) {
                        printCustom(docVenta.getDireccion(), 1, 0);
                    }
                }
                printCustom(docVenta.getNombreVendedor(), 1, 0);
                printCustom(docVenta.getIdentificador(), 1, 0);
            }
            printNewLine();
            printNewLine();
            printCustom(Constantes.ConfigTienda.ContenidoPieTicketAdicional.replace("(/n)", "\n"), 1, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<PrinterJob> ImpresionAdicionalPedidoPrintJob(DocVenta docVenta) {
        List<PrinterJob> list = new ArrayList<>();
        list.add(new PrinterJob(PrinterCommands.OPEN_CASH_DRAWER));

        if (Constantes.ConfigTienda.CabeceraPieTicketAdicional) {

            list.add(new PrinterJob(PrinterCommands.OPEN_CASH_DRAWER));
            list.addAll(GenerarLinea("------------------------", 1, 0));
            list.addAll(GenerarLinea(Constantes.Empresa.nombreTienda, 2, 1));
            list.addAll(GenerarLinea(docVenta.getFechaEmision(), 1, 0));
            list.addAll(GenerarLinea(docVenta.getNombreReceptor(), 1, 0));
            if (docVenta.getDireccion() != null) {
                if (docVenta.getDireccion().length() > 0) {
                    list.addAll(GenerarLinea(docVenta.getDireccion(), 1, 0));
                }
            }
            list.addAll(GenerarLinea(docVenta.getNombreVendedor(), 1, 0));
            list.addAll(GenerarLinea(docVenta.getIdentificador(), 1, 0));

        }
        list.add(printNewLineJob());
        list.add(printNewLineJob());
        list.addAll(GenerarLinea(Constantes.ConfigTienda.ContenidoPieTicketAdicional.replace("(/n)", "\n"), 1, 0));

        return list;
    }


    private List<PrinterJob> GenerarLinea(String msg, int size, int align) {

        List<PrinterJob> list = new ArrayList<>();
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold  with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x3C}; // 3- bold with large text
        byte[] bb4 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        byte[] bb5 = new byte[]{0x1B, 0x21, 0xC}; // 3- bold with large text
        switch (size) {
            case 0:
                list.add(new PrinterJob((cc)));
                break;
            case 1:
                list.add(new PrinterJob((bb)));
                break;
            case 2:
                list.add(new PrinterJob((bb2)));
                break;
            case 3:
                list.add(new PrinterJob((bb3)));
                break;
            case 4:
                list.add(new PrinterJob((bb4)));
                break;
            case 5:
                list.add(new PrinterJob((bb5)));
                break;
        }
        switch (align) {
            case 0:
                //left align
                list.add(new PrinterJob((ESC_ALIGN_LEFT)));
                break;
            case 1:
                list.add(new PrinterJob((PrinterCommands.ESC_ALIGN_CENTER)));

                break;
            case 2:
                //right align
                list.add(new PrinterJob((PrinterCommands.ESC_ALIGN_RIGHT)));
                break;
        }
        list.add(new PrinterJob(msg.getBytes()));
        list.add(new PrinterJob(new byte[]{PrinterCommands.LF}));

        return list;
    }

    private void printCustom(String msg, int size, int align) {

        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold  with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x3C}; // 3- bold with large text
        byte[] bb4 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        byte[] bb5 = new byte[]{0x1B, 0x21, 0xC}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(new byte[]{27,33, 0});
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
                case 4:
                    mmOutputStream.write(bb4);
                    break;
                case 5:
                    mmOutputStream.write(bb5);
                    break;
            }
            switch (align) {
                case 0:
                    //left align
                    mmOutputStream.write(ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException x1) {
            x1.printStackTrace();
            Log.e("Error1", x1.toString());
        } catch (Exception ex) {

            Log.e("Error2", ex.toString());
        }


    }

    public void ImprimirPedidoPrecuenta(DocVenta docVenta) {
        try {
            this.mmOutputStream.write(PrinterCommands.OPEN_CASH_DRAWER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printNewLine();
        printCustom(docVenta.getNombreEmisor(), 2, 1);
        if (docVenta.getSerie().length() > 0 && docVenta.getCorrelativo().length() > 0) {
            printCustom(docVenta.getRucEmisor(), 2, 1);
        }
        printNewLine();
        if (docVenta.getDireccionEmisor().length() > 0) {
            printCustom(docVenta.getDireccionEmisor(), 0, 1);
        }
        printNewLine();
        printCustom(docVenta.getTipoDoc(), 1, 1);
        if (docVenta.getSerie().length() > 0 && docVenta.getCorrelativo().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(docVenta.getSerie());
            stringBuilder.append("-");
            stringBuilder.append(docVenta.getCorrelativo());
            printCustom(stringBuilder.toString(), 1, 1);
        }
        printNewLine();
        if (!docVenta.getIdentificador().trim().isEmpty()) {
            printCustom(docVenta.getIdentificador(), 1, 0);
        }
        printCustom(docVenta.getNroPedido(), 1, 0);
        printCustom(docVenta.getFechaEmision(), 1, 0);
        printCustom(docVenta.getFechaEntrega(), 1, 0);
        if (Constantes.ConfigTienda.bUsaFechaEntrega) {
            printCustom(docVenta.getCEstadoEntrega(), 1, 0);

        }
        if (!docVenta.getNombreReceptor().isEmpty())
            printCustom(docVenta.getNombreReceptor(), 1, 0);


        if (docVenta.getDireccion() != null) {
            if (docVenta.getDireccion().length() > 0) {
                printCustom(docVenta.getDireccion(), 1, 0);
            }
        }
        if (!docVenta.getNombreVendedor().isEmpty())
            printCustom(docVenta.getNombreVendedor(), 1, 0);


       /*if (docVenta.getVentaCredito().length() > 0) {
            printCustom(docVenta.getVentaCredito(), 1, 1);
            printNewLine();
        }*/
        if (!docVenta.getObservacion().trim().isEmpty()) {
            printCustom(docVenta.getObservacion(), 1, 0);
            printNewLine();
        }

        printCustom(docVenta.getDatosEntrega(), 1, 0);

        printCustom(docVenta.getCabecerasTicket(), 1, 0);

        printCustom(ConstructorFacturaKt.generarCadenaCaracteres("-", 31), 1, 0);

        printCustom(docVenta.getProductos(), 1, 0);
        printCustom(ConstructorFacturaKt.generarCadenaCaracteres("-", 31), 1, 0);
        printNewLine();
        if (Constantes.ConfigTienda.bImprimePagosPrecuenta) {
            if (docVenta.getPagosVenta().length() > 0) {
                printCustom(docVenta.getPagosVenta(), 1, 2);
            }
        }

        if (docVenta.getTotalDescuento().length() > 0) {
            printCustom(docVenta.getTotalDescuento(), 1, 2);
        }
        if (docVenta.getTotalGravada() != null) {
            if (docVenta.getTotalGravada().length() > 0) {
                printCustom(docVenta.getTotalGravada(), 1, 2);
            }
        }
        if (docVenta.getTotalIgv() != null) {
            if (docVenta.getTotalIgv().length() > 0) {
                printCustom(docVenta.getTotalIgv(), 1, 2);
            }
        }

        printCustom(docVenta.getTotal(), 1, 2);


        if (docVenta.getTotalCambio() != null) {
            if (docVenta.getTotalCambio().length() > 0) {
                printCustom(docVenta.getTotalCambio(), 1, 2);
            }
        }


        printNewLine();
        if (docVenta.getImporteLetra() != null) {
            if (docVenta.getImporteLetra().length() > 0) {
                printCustom(docVenta.getImporteLetra(), 1, 0);
            }
        }
        if (docVenta.getMensajeRepre().length() > 0) {
            printCustom(docVenta.getMensajeRepre(), 1, 1);
            printNewLine();
        }
        if (docVenta.getSerie().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(" Visita:\n");
            stringBuilder.append(docVenta.getEnlaceNf());
            printCustom(stringBuilder.toString(), 1, 1);
        }
        if (docVenta.getCodigoHash().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resumen \n");
            stringBuilder.append(docVenta.getCodigoHash());
            printCustom(stringBuilder.toString(), 1, 1);
        }
        if (docVenta.getPieDoc().length() > 0) {
            printCustom(docVenta.getPieDoc(), 1, 1);
        }
        printNewLine();
        if(docVenta.getImageQrZonaServicio()!=null){
            PrinterBitmap(docVenta.getImageQrZonaServicio());
        }


        if (Constantes.ConfigTienda.cMensajePrecuenta.trim().length() > 0)
            printCustom(Constantes.ConfigTienda.cMensajePrecuenta.trim(), 1, 0);
        printNewLine();

        try {

            this.mmOutputStream.write(PrinterCommands.FEED_PAPER_AND_CUT);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    public List<PrinterJob> ImprimirPedidoPrecuentaPrinterJob(DocVenta docVenta) {
        List<PrinterJob> list = new ArrayList<>();
        list.add(new PrinterJob(PrinterCommands.OPEN_CASH_DRAWER));
        list.add(printNewLineJob());

        list.addAll(GenerarLinea(docVenta.getNombreEmisor(), 2, 1));
        printCustom(docVenta.getNombreEmisor(), 2, 1);
        if (docVenta.getSerie().length() > 0 && docVenta.getCorrelativo().length() > 0) {
            printCustom(docVenta.getRucEmisor(), 2, 1);
        }
        list.add(printNewLineJob());

        if (docVenta.getDireccionEmisor().length() > 0) {
            list.addAll(GenerarLinea(docVenta.getDireccionEmisor(), 1, 1));

        }
        list.add(printNewLineJob());
        list.addAll(GenerarLinea(docVenta.getTipoDoc(), 1, 1));
        if (docVenta.getSerie().length() > 0 && docVenta.getCorrelativo().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(docVenta.getSerie());
            stringBuilder.append("-");
            stringBuilder.append(docVenta.getCorrelativo());
            list.addAll(GenerarLinea(stringBuilder.toString(),1, 1));

        }
        list.add(printNewLineJob());
        if (!docVenta.getIdentificador().trim().isEmpty()) {
            list.addAll(GenerarLinea(docVenta.getIdentificador(), 1, 0));

        }

        list.addAll(GenerarLinea(docVenta.getNroPedido(), 1, 0));
        list.addAll(GenerarLinea(docVenta.getFechaEmision(), 1, 0));
        list.addAll(GenerarLinea(docVenta.getFechaEntrega(), 1, 0));


        if (Constantes.ConfigTienda.bUsaFechaEntrega) {
            list.addAll(GenerarLinea(docVenta.getCEstadoEntrega(), 1, 0));


        }
        if (!docVenta.getNombreReceptor().isEmpty())
            list.addAll(GenerarLinea(docVenta.getNombreReceptor(), 1, 0));


        if (docVenta.getDireccion() != null) {
            if (docVenta.getDireccion().length() > 0) {
                list.addAll(GenerarLinea(docVenta.getDireccion(), 1, 0));

            }
        }
        if (!docVenta.getNombreVendedor().isEmpty())
            list.addAll(GenerarLinea(docVenta.getNombreVendedor(), 1, 0));


        if (!docVenta.getIdentificador().trim().isEmpty())
            list.addAll(GenerarLinea(docVenta.getIdentificador(), 1, 0));


       /*if (docVenta.getVentaCredito().length() > 0) {
            printCustom(docVenta.getVentaCredito(), 1, 1);
            printNewLine();
        }*/
        if (!docVenta.getObservacion().trim().isEmpty()) {
            list.addAll(GenerarLinea(docVenta.getObservacion(), 1, 0));

            list.add(printNewLineJob());

        }
        list.addAll(GenerarLinea(docVenta.getCabecerasTicket(), 1, 0));
        list.addAll(GenerarLinea(ConstructorFacturaKt.generarCadenaCaracteres("-", 31), 1, 0));
        list.addAll(GenerarLinea(docVenta.getProductos(), 1, 0));
        list.addAll(GenerarLinea(ConstructorFacturaKt.generarCadenaCaracteres("-", 31), 1, 0));

        list.add(printNewLineJob());
        if (Constantes.ConfigTienda.bImprimePagosPrecuenta) {
            if (docVenta.getPagosVenta().length() > 0) {

                list.addAll(GenerarLinea(docVenta.getPagosVenta(), 1, 2));
            }
        }

        if (docVenta.getTotalDescuento().length() > 0) {
            list.addAll(GenerarLinea(docVenta.getTotalDescuento(), 1, 2));

        }
        if (docVenta.getTotalGravada() != null) {
            if (docVenta.getTotalGravada().length() > 0) {
                list.addAll(GenerarLinea(docVenta.getTotalGravada(), 1, 2));

            }
        }
        if (docVenta.getTotalIgv() != null) {
            if (docVenta.getTotalIgv().length() > 0) {
                list.addAll(GenerarLinea(docVenta.getTotalIgv(), 1, 2));

            }
        }
        list.addAll(GenerarLinea(docVenta.getTotal(), 1, 2));

        if (docVenta.getTotalCambio() != null) {
            if (docVenta.getTotalCambio().length() > 0) {
                list.addAll(GenerarLinea(docVenta.getTotalCambio(), 1, 2));

            }
        }


        list.add(printNewLineJob());
        if (docVenta.getImporteLetra() != null) {
            if (docVenta.getImporteLetra().length() > 0) {
                list.addAll(GenerarLinea(docVenta.getImporteLetra(), 1, 0));

            }
        }
        if (docVenta.getMensajeRepre().length() > 0) {

            list.addAll(GenerarLinea(docVenta.getMensajeRepre(), 1, 1));

            list.add(printNewLineJob());
        }
        if (docVenta.getSerie().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(" Visita:\n");
            stringBuilder.append(docVenta.getEnlaceNf());
            list.addAll(GenerarLinea(stringBuilder.toString(), 1, 1));

        }
        if (docVenta.getCodigoHash().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resumen \n");
            stringBuilder.append(docVenta.getCodigoHash());
            list.addAll(GenerarLinea(stringBuilder.toString(), 1, 1));

        }
        if (docVenta.getPieDoc().length() > 0) {
            list.addAll(GenerarLinea(docVenta.getPieDoc(), 1, 1));

        }
        list.add(printNewLineJob());
        if (Constantes.ConfigTienda.cMensajePrecuenta.trim().length() > 0)
            list.addAll(GenerarLinea(docVenta.getPieDoc(), 1, 1));
        printCustom(Constantes.ConfigTienda.cMensajePrecuenta.trim(), 1, 0);
        list.add(printNewLineJob());
        list.add(new PrinterJob(PrinterCommands.FEED_PAPER_AND_CUT));

        return list;
    }

    public List<PrinterJob> GenerarPrintJobs(DocVenta docVenta) {
        List<PrinterJob> printerJobs = new ArrayList<>();
        try {
            printerJobs.add(new PrinterJob(PrinterCommands.OPEN_CASH_DRAWER));
            printerJobs.add(printNewLineJob());
            printerJobs.addAll(GenerarLinea(docVenta.getNombreEmisor(), 2, 1));

            if (docVenta.getSerie().length() > 0 && docVenta.getCorrelativo().length() > 0) {
                printerJobs.addAll(GenerarLinea(docVenta.getRucEmisor(), 2, 1));

            }
            printerJobs.add(printNewLineJob());
            if (docVenta.getDireccionEmisor().length() > 0) {
                printerJobs.addAll(GenerarLinea(docVenta.getDireccionEmisor(), 0, 1));

            }
            printerJobs.add(printNewLineJob());
            printerJobs.addAll(GenerarLinea(docVenta.getTipoDoc(), 1, 1));

            if (docVenta.getSerie().length() > 0 && docVenta.getCorrelativo().length() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(docVenta.getSerie());
                stringBuilder.append("-");
                stringBuilder.append(docVenta.getCorrelativo());
                printerJobs.addAll(GenerarLinea(stringBuilder.toString(), 1, 1));

            }
            printerJobs.add(printNewLineJob());
            printerJobs.addAll(GenerarLinea(docVenta.getFechaEmision(), 1, 0));


            if (!docVenta.getNombreReceptor().isEmpty())
                printerJobs.addAll(GenerarLinea(docVenta.getNombreReceptor(), 1, 0));


            if (docVenta.getDireccion() != null) {
                if (docVenta.getDireccion().length() > 0) {
                    printerJobs.addAll(GenerarLinea(docVenta.getDireccion(), 1, 0));

                }
            }
            if (!docVenta.getNombreVendedor().isEmpty())
                printerJobs.addAll(GenerarLinea(docVenta.getNombreVendedor(), 1, 0));


            if (!docVenta.getIdentificador().isEmpty())
                printerJobs.addAll(GenerarLinea(docVenta.getIdentificador(), 1, 0));


            if (!docVenta.getObservacion().trim().isEmpty()) {
                printerJobs.addAll(GenerarLinea(docVenta.getObservacion(), 1, 0));
            }

            printerJobs.add(printNewLineJob());
            printerJobs.addAll(GenerarLinea(docVenta.getCabecerasTicket(), 1, 0));
            printerJobs.addAll(GenerarLinea(ConstructorFacturaKt.generarCadenaCaracteres("-", 31), 1, 0));
            printerJobs.addAll(GenerarLinea(docVenta.getProductos(), 1, 0));

            printerJobs.addAll(GenerarLinea(ConstructorFacturaKt.generarCadenaCaracteres("-", 31), 1, 0));


            printerJobs.add(printNewLineJob());
            if (docVenta.getTotalDescuento().length() > 0) {
                printerJobs.addAll(GenerarLinea(docVenta.getTotalDescuento(), 1, 0));

            }
            if (docVenta.getTotalGravada() != null) {
                if (docVenta.getTotalGravada().length() > 0) {
                    printerJobs.addAll(GenerarLinea(docVenta.getTotalGravada(), 1, 0));

                }
            }
            if (docVenta.getTotalIgv() != null) {
                if (docVenta.getTotalIgv().length() > 0) {
                    printerJobs.addAll(GenerarLinea(docVenta.getTotalIgv(), 1, 0));
                }
            }
            printerJobs.addAll(GenerarLinea(docVenta.getTotal(), 1, 0));

            if (docVenta.getTotalCambio() != null) {
                if (docVenta.getTotalCambio().length() > 0) {
                    printerJobs.addAll(GenerarLinea(docVenta.getTotalCambio(), 1, 0));

                }
            }

            printerJobs.add(printNewLineJob());
            if (docVenta.getImporteLetra() != null) {
                if (docVenta.getImporteLetra().length() > 0) {
                    printerJobs.addAll(GenerarLinea(docVenta.getImporteLetra(), 1, 0));

                }
            }
            if (docVenta.getMensajeRepre().length() > 0) {
                printerJobs.addAll(GenerarLinea(docVenta.getMensajeRepre(), 1, 1));

                printerJobs.add(printNewLineJob());
            }
            if (docVenta.getSerie().length() > 0) {
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append(" Visita:\n");
                stringBuilder.append(docVenta.getEnlaceNf());
                printerJobs.addAll(GenerarLinea(stringBuilder.toString(), 1, 1));

            }
            if (docVenta.getCodigoHash().length() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Resumen \n");
                stringBuilder.append(docVenta.getCodigoHash());
                printerJobs.addAll(GenerarLinea(stringBuilder.toString(), 1, 1));

            }
            if (docVenta.getPieDoc().length() > 0) {
                printerJobs.addAll(GenerarLinea(docVenta.getPieDoc(), 1, 1));

            }
            printerJobs.add(printNewLineJob());
            printerJobs.add(printNewLineJob());
            printerJobs.add(printNewLineJob());

            printerJobs.add(new PrinterJob(PrinterCommands.FEED_PAPER_AND_CUT));

        } catch (Exception e) {
            printerJobs = null;
        }
        return printerJobs;

    }

    public void ImprimirFactura(DocVenta docVenta) {
        try {
            this.mmOutputStream.write(PrinterCommands.OPEN_CASH_DRAWER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printNewLine();
        printCustom(docVenta.getNombreEmisor(), 2, 1);
        if (docVenta.getSerie().length() > 0 && docVenta.getCorrelativo().length() > 0) {
            printCustom(docVenta.getRucEmisor(), 1, 1);
        }
        printNewLine();
        if (docVenta.getDireccionEmisor().length() > 0) {
            printCustom(docVenta.getDireccionEmisor(), 1, 1);
        }
        printNewLine();
        printCustom(docVenta.getTipoDoc(), 1, 1);
        if (docVenta.getSerie().length() > 0 && docVenta.getCorrelativo().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(docVenta.getSerie());
            stringBuilder.append("-");
            stringBuilder.append(docVenta.getCorrelativo());
            printCustom(stringBuilder.toString(), 1, 1);
        }
        printNewLine();
        printCustom(docVenta.getFechaEmision(), 1, 0);

        if (!docVenta.getNombreReceptor().isEmpty())
            printCustom(docVenta.getNombreReceptor(), 1, 0);
        if (docVenta.getDireccion() != null) {
            if (docVenta.getDireccion().length() > 0) {
                printCustom(docVenta.getDireccion(), 1, 0);
            }
        }
        if (!docVenta.getNombreVendedor().isEmpty())
            printCustom(docVenta.getNombreVendedor(), 1, 0);

        if (!docVenta.getIdentificador().isEmpty())
            printCustom(docVenta.getIdentificador(), 1, 0);

        if (!docVenta.getObservacion().trim().isEmpty()) {
            printCustom(docVenta.getObservacion(), 1, 0);
        }

        printNewLine();
        /*if (docVenta.getVentaCredito().length() > 0) {
            printCustom(docVenta.getVentaCredito(), 1, 1);
            printNewLine();
        }*/
        printCustom(docVenta.getCabecerasTicket(), 1, 0);
        printCustom(ConstructorFacturaKt.generarCadenaCaracteres("-", 31), 1, 0);
        printCustom(docVenta.getProductos(), 1, 0);
        printCustom(ConstructorFacturaKt.generarCadenaCaracteres("-", 31), 1, 0);
        printNewLine();
        if (docVenta.getTotalDescuento().length() > 0) {
            printCustom(docVenta.getTotalDescuento(), 1, 0);
        }
        if (docVenta.getTotalGravada() != null) {
            if (docVenta.getTotalGravada().length() > 0) {
                printCustom(docVenta.getTotalGravada(), 1, 0);
            }
        }
        if (docVenta.getTotalIgv() != null) {
            if (docVenta.getTotalIgv().length() > 0) {
                printCustom(docVenta.getTotalIgv(), 1, 0);
            }
        }

        printCustom(docVenta.getTotal(), 1, 0);

        if (docVenta.getTotalCambio() != null) {
            if (docVenta.getTotalCambio().length() > 0) {
                printCustom(docVenta.getTotalCambio(), 1, 0);
            }
        }

        printNewLine();
        if (docVenta.getImporteLetra() != null) {
            if (docVenta.getImporteLetra().length() > 0) {
                printCustom(docVenta.getImporteLetra(), 1, 0);
            }
        }
        if (docVenta.getMensajeRepre().length() > 0) {
            printCustom(docVenta.getMensajeRepre(), 1, 1);
            printNewLine();
        }
        if (docVenta.getSerie().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(" Visita:\n");
            stringBuilder.append(docVenta.getEnlaceNf());
            printCustom(stringBuilder.toString(), 1, 1);
        }
        if (docVenta.getCodigoHash().length() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resumen \n");
            stringBuilder.append(docVenta.getCodigoHash());
            printCustom(stringBuilder.toString(), 1, 1);
        }
        if (docVenta.getPieDoc().length() > 0) {
            printCustom(docVenta.getPieDoc(), 1, 1);
        }
        printNewLine();
        printNewLine();
        printNewLine();
        try {

            this.mmOutputStream.write(PrinterCommands.FEED_PAPER_AND_CUT);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void ImprimirPedido(String cabecera, String cuerpo) {
        printNewLine();
        //cabecera = StringExKt.ReplaceCharSpecial(cabecera);
        //cuerpo = StringExKt.ReplaceCharSpecial(cuerpo);
        printCustom(cabecera, 1, 0);
        printCustom(ConstructorFacturaKt.generarCadenaCaracteres("_", 33), 1, 1);
        printCustom(cuerpo, 1, 0);
        try {
            this.mmOutputStream.write(PrinterCommands.FEED_PAPER_AND_CUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String LineaDesripcionDemo(ProductoEnVenta p) {
        String n = "";
        String cantidad = "";
        String PrecioUnitario = "";
        String PrecioTotal = "";
        int len = 10;
        int lenCantidad = 0;
        cantidad = String.format("%.1f", p.getCantidad());
        PrecioUnitario = String.format("%.2f", p.getPrecioOriginal());
        PrecioTotal = String.format("%.2f", p.getPrecioVentaFinal());

        cantidad = "  " + cantidad;
        lenCantidad = len - cantidad.length();

        for (int i = 0; i < lenCantidad; i++) {
            cantidad = cantidad + " ";
        }
        PrecioUnitario = "  " + PrecioUnitario;
        lenCantidad = len - PrecioUnitario.length();
        for (int i = 0; i < lenCantidad; i++) {
            PrecioUnitario = PrecioUnitario + " ";
        }
        PrecioTotal = "  " + PrecioTotal;
        lenCantidad = len - PrecioTotal.length();
        for (int i = 0; i < lenCantidad; i++) {
            PrecioTotal = PrecioTotal + " ";
        }
        n = cantidad + PrecioUnitario + PrecioTotal + "  ";

        return n;
    }

    public String LineaNombre(ProductoEnVenta p) {

        String nombre = "";
        String nvariante = "";
        int count = 0;
        if (p.isEsVariante()) {
            if ((p.getProductName().length()) <= numCaracteres55mm) {
                count = numCaracteres55mm - (p.getProductName().length());
                nombre = p.getProductName();
                for (int i = 0; i < count; i++) {
                    nombre = nombre + " ";
                }
                if (p.getDescripcionVariante().trim().length() <= numCaracteres55mm) {
                    count = numCaracteres55mm - p.getDescripcionVariante().trim().length();
                    nombre = nombre + p.getDescripcionVariante().trim();
                    for (int i = 0; i < count; i++) {
                        nombre = nombre + " ";
                    }
                } else {
                    nvariante = p.getDescripcionVariante().trim().substring(0, numCaracteres55mm);
                    count = numCaracteres55mm - nvariante.trim().length();
                    nombre = nombre + nvariante;
                    for (int i = 0; i < count; i++) {
                        nombre = nombre + " ";
                    }
                }
            } else {


            }
        } else {

        }

        if ((p.getProductName().length()) < 32) {
            count = 32 - (p.getProductName().length());
            nombre = p.getProductName();
            for (int i = 0; i < count; i++) {
                nombre = nombre + " ";
            }
        } else {
            nombre = p.getProductName().substring(0, 32);
        }
        return nombre;
    }


    public String LineaProducto(ProductoEnVenta p) {
        String precio = "";
        String nombre = "";
        String cantidad = "";
        String precioU = "";
        nombre = p.getProductName().substring(0, 11);

        if (nombre.length() < 11) {
            int n = 11 - nombre.length();
            for (int i = 0; i < n; i++) {
                nombre = nombre + " ";
            }
        }
        cantidad = String.format("%.0f", p.getCantidad());
        if (cantidad.length() < 2) {
            int a = 2 - cantidad.length();
            for (int i = 0; i < a; i++) {
                cantidad = cantidad + " ";
            }
        }

        precioU = String.format("%.2f", p.getPrecioOriginal());
        if (precioU.length() < 4) {
            int q = 4 - precioU.length();
            for (int i = 0; i < q; i++) {
                precioU = " " + precioU;
            }
        }
        precio = String.format("%.2f", p.getPrecioVentaFinal());
        if (precio.length() < 6) {
            int l = 6 - precio.length();
            for (int i = 0; i < l; i++) {
                precio = " " + precio;
            }
        }
        return nombre + "  " + cantidad + "   " + precioU + "  " + precio;
    }

    /*
        public int[][] getPixelsSlow(BufferedImage image) {
            int width = image.getWidth();
            int height = image.getHeight();
            int[][] result = new int[height][width];
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    result[row][col] = image.getRGB(col, row);
                }
            }

            return result;
        }
    */
    public void ImpresionTicketNotaVenta(List<ProductoEnVenta> list) {

        String[] tiempo = getDateTime();
        int longitud = list.size();
        String linea = "";
        BigDecimal total = new BigDecimal(0);
        printNewLine();
        printCustom(Constantes.Empresa.nombreTienda, 3, 1);
        printCustom("Nota de venta", 1, 1);
        printNewLine();
        printCustom(tiempo[0] + " " + tiempo[1], 0, 1);
        printNewLine();
        printCustom(Cabecera, 0, 0);
        printNewLine();
        for (int i = 0; i < longitud; i++) {
            total = total.add(list.get(i).getPrecioVentaFinal());
            printCustom(LineaNombreDemo(list.get(i)), 5, 0);
            printCustom(LineaDesripcionDemo(list.get(i)), 5, 0);
        }
        printNewLine();
        for (int i = 0; i < 32; i++) {
            linea = linea + "-";
        }
        printText(linea);
        printNewLine();
        imprimir("Total", Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", total));
        printNewLine();
        printNewLine();
        printNewLine();
        printNewLine();

    }

    public void PrintPdf() {

        doWebViewPrint();
    }

    private void doWebViewPrint() {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    createWebPrintJob(view);
                }
                mWebView = null;
            }
        });


        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
                "testing, testing...</p></body></html>";
        webView.loadDataWithBaseURL("Texto", htmlDocument, "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) context
                .getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = null;
        // Get a print adapter instance
        printAdapter = webView.createPrintDocumentAdapter(context.getString(R.string.app_name) + " " + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + String.valueOf(c.get(Calendar.MONTH) + 1)
                + String.valueOf(c.get(Calendar.YEAR))
        );

        // Create a print job with name and adapter instance
        String jobName = context.getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking

    }

    public void ImprimirDocVenta(String nombreEmpresa, String direccion, String cuerpo, String Pie) {

        printCustom(nombreEmpresa, 3, 1);
        printCustom(direccion, 2, 1);


    }

    public Bitmap test(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                // use 128 as threshold, above -> white, below -> black
                if (gray > 128) {
                    gray = 255;
                } else {
                    gray = 0;
                }
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        return bmOut;
    }

    BitSet dots;
    int mWidth;
    int mHeight;
    public void printPhoto(Bitmap bmp) {
        try {

            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }
    public void PrinterImage(Bitmap bmp) {

        try {
            byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33, -128, 0};


            convertBitmap(bmp);

            mmOutputStream.write(PrinterCommands.SET_LINE_SPACING_24);
            int offset = 0;

            while (offset < bmp.getHeight()) {
                mmOutputStream.write(SELECT_BIT_IMAGE_MODE);
                for (int x = 0; x < bmp.getWidth(); x++) {

                    for (int k = 0; k < 3; k++) {
                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }
                        mmOutputStream.write(slice);
                    }

                }
                offset += 24;
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
                mmOutputStream.write(PrinterCommands.FEED_LINE);
            }
            mmOutputStream.write(PrinterCommands.SET_LINE_SPACING_30);

        } catch (Exception ex) {

        }

    }

    public void PrinterBitmap(Bitmap bmp){

        byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33, (byte) 255, 3};
        try{
            Bitmap nbmp= toGrayscale(bmp);
            byte[] array= decodeBitmap(nbmp);

            printNewLine();

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
             mmOutputStream.write(array);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printNewLine();

        }catch (Exception ex){


        }


    }
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    public String convertBitmap(Bitmap inputBitmap) {
        mWidth = inputBitmap.getWidth();
        mHeight = inputBitmap.getHeight();

        convertArgbToGrayscale(inputBitmap, mWidth, mHeight);
        String mStatus = "ok";
        return mStatus;

    }


    private void convertArgbToGrayscale(Bitmap bmpOriginal, int width,
                                        int height) {
        int pixel;
        int k = 0;
        int B = 0, G = 0, R = 0;

        dots=new BitSet();
        try {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x);

                    // retrieve color of all channels
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k);//this is the bitset that i'm printing
                    }
                    k++;

                }


            }


        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    public void printImage(Bitmap bmp) {
        int h = 0;
        int w = 0;

        byte[] command = Utils.decodeBitmap(bmp);
        byte[] printFormat = new byte[]{0x1B, 0x21, 0x03};


        try {
            mmOutputStream.write(printFormat);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            mmOutputStream.write(PrinterCommands.FEED_LINE);
            mmOutputStream.write(command);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static final byte[] UNICODE_TEXT = new byte[] {0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,0x23, 0x23, 0x23,
            0x23, 0x23, 0x23};

    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray = { "0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111" };

    public static byte[] decodeBitmap(Bitmap bmp){
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<String>(); //binaryString list
        StringBuffer sb;


        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;

        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1;
            for (int i = 0; i < (8 - zeroCount); i++) {
                zeroStr = zeroStr + "0";
            }
        }

        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);

                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 160 && g > 160 && b > 160)
                    sb.append("0");
                else
                    sb.append("1");
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));
        if (widthHexString.length() > 2) {
            Log.e("decodeBitmap error", " width is too large");
            return null;
        } else if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        widthHexString = widthHexString + "00";

        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
            Log.e("decodeBitmap error", " height is too large");
            return null;
        } else if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        heightHexString = heightHexString + "00";

        List<String> commandList = new ArrayList<String>();
        commandList.add(commandHexString+widthHexString+heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<String>();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                String str = binaryStr.substring(i, i + 8);

                String hexString = myBinaryStrToHexString(str);
                sb.append(hexString);
            }
            hexList.add(sb.toString());
        }
        return hexList;

    }

    public static String myBinaryStrToHexString(String binaryStr) {
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }
        for (int i = 0; i < binaryArray.length; i++) {
            if (b4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }

        return hex;
    }

    public static byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList<byte[]>();

        for (String hexStr : list) {
            commandList.add(hexStringToBytes(hexStr));
        }
        byte[] bytes = sysCopy(commandList);
        return bytes;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
