package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ConstructorFactura;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.cImpresion;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;


/**
 * import androidmads.library.qrgenearator.QRGContents;
 * import androidmads.library.qrgenearator.QRGEncoder;
 * Created by OMAR CHH on 27/12/2017.
 */

public class DialogVentaResultado extends DialogFragment implements View.OnClickListener {

    final Calendar c = Calendar.getInstance();
    Dialog dialog;
    Button btnImprimir, btnProximaVenta;
    List<ProductoEnVenta> list;
    TextView txtCambio;
    BigDecimal CantidadCambio = new BigDecimal(0);
    ListenerTerminarVenta listenerTerminarVenta;
    ConstructorFactura constructorFactura;
    Context context;
    private mCabeceraVenta getCabeceraVenta() {
        return cabeceraVenta;
    }

    private void setCabeceraVenta(mCabeceraVenta cabeceraVenta) {
        this.cabeceraVenta = cabeceraVenta;
    }

    mCabeceraVenta cabeceraVenta;
    private WebView mWebView;


    public void setList(List<ProductoEnVenta> list) {
        this.list = list;
    }

    public void setCantidadCambio(BigDecimal cantidadCambio) {
        CantidadCambio = cantidadCambio;
    }

    public DialogVentaResultado newInstance(List<ProductoEnVenta>list, BigDecimal CantidadCambio,mCabeceraVenta cabeceraVenta){

        DialogVentaResultado d=new DialogVentaResultado();
        cabeceraVenta.setTotalCambio(CantidadCambio);
        d.setList(list);
        d.setCantidadCambio(CantidadCambio);
        d.setCabeceraVenta(cabeceraVenta);
        return d;

    }



    public void setListenerTerminarVenta(ListenerTerminarVenta listenerTerminarVenta) {

        this.listenerTerminarVenta = listenerTerminarVenta;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_resultado_venta, null);
        txtCambio = (TextView) v.findViewById(R.id.txtCambioVenta);
        btnImprimir = (Button) v.findViewById(R.id.btnImprimir);
        btnProximaVenta = (Button) v.findViewById(R.id.btnProximaVenta);
        constructorFactura=new ConstructorFactura();
        btnProximaVenta.setOnClickListener(this);
        btnImprimir.setOnClickListener(this);
        txtCambio.setText("Cambio " + Constantes.DivisaPorDefecto.SimboloDivisa
                + String.format("%.2f", CantidadCambio.multiply(new BigDecimal(-1))));
        builder.setView(v);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        context=getActivity().getBaseContext();
        return dialog;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
           case R.id.btnImprimir:
                String r="";
                cImpresion impresion=new cImpresion(getActivity());
            /*   Drawable vectorDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_account_black_48dp,  context.getTheme());
               Bitmap myLogo = ((BitmapDrawable) vectorDrawable).getBitmap();*/

               String data = "http://www.google.com";
             //  QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 200);
               Bitmap   bitmap=null;
               /*try {
                   bitmap = qrgEncoder.encodeAsBitmap();
               } catch (WriterException e) {
                   e.printStackTrace();
               }*/
               Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_card_details_white_48dp);
                r=impresion.ImpresionDocVenta(list,cabeceraVenta,cabeceraVenta.getIdTipoDocPago());
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmación")
                        .setMessage("Se mando a imprimir la venta")
                        .setPositiveButton("Salir",null)
                        .create().show();
              //  SeleccionarMetodo();
                break;

            case R.id.btnProximaVenta:

                dialog.dismiss();
                break;


        }

    }

    private static final int qr_image_width = 200;
    private static final int qr_image_height = 200;
    public void demo(){
        String data = "http://www.google.com";
  /*      QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 200);


        try {
         Bitmap   bitmap = qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }
*/
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        listenerTerminarVenta.FinalizarVenta();

    }


    public interface ListenerTerminarVenta {

        public void FinalizarVenta();

    }
}
