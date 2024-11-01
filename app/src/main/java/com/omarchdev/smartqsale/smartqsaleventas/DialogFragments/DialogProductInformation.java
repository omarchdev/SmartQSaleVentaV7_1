package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;


/**
 * Created by OMAR CHH on 12/12/2017.
 */

public class DialogProductInformation extends DialogFragment {

    int idProduct;
    ProgressBar progressBar;
    Dialog dialog;
    ImageView imageProduct;
    TextView txtNameProduct, txtCodigo, txtCantidadEnStock, txtCantidadReserva, txtPrecioCompra, txtPrecioVenta, txtInformacionAdicional;
    ScrollView scrollView;
    ControladorProductos controladorProductos;
    String simboloMoneda;

    public DialogProductInformation newInstance(int idProduct) {


        DialogProductInformation d = new DialogProductInformation().newInstance(idProduct);
        d.setIdProduct(idProduct);
        return d;

    }

    private void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = ((Activity) getActivity()).getLayoutInflater().inflate(R.layout.dialog_information_product, null);
        controladorProductos = new ControladorProductos(getActivity());
        declararElementoVista(v);
        simboloMoneda = Constantes.DivisaPorDefecto.SimboloDivisa;
        dialog = builder.setView(v).create();
        new DescargarInformacionProducto().execute(idProduct);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void declararElementoVista(View v) {
        scrollView = (ScrollView) v.findViewById(R.id.contenedorInformacion);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        imageProduct = (ImageView) v.findViewById(R.id.imgProductInformation);
        txtNameProduct = (TextView) v.findViewById(R.id.txtNombreInformacion);
        txtCodigo = (TextView) v.findViewById(R.id.txtCodigoInformacion);
        txtCantidadEnStock = (TextView) v.findViewById(R.id.txtCantidadDato);
        txtCantidadReserva = (TextView) v.findViewById(R.id.txtCantidadReservaDato);
        txtPrecioCompra = (TextView) v.findViewById(R.id.txtPrecioCompraDato);
        txtPrecioVenta = (TextView) v.findViewById(R.id.txtPrecioVentaDato);
        txtInformacionAdicional = (TextView) v.findViewById(R.id.txtInformacionAdicional);

    }

    public void completarDatos(mProduct p) {
        byte[] imgData;
        txtNameProduct.setText(p.getcProductName());
        txtCodigo.setText(p.getcKey());
        txtCantidadEnStock.setText(String.valueOf(p.getdQuantity()));
        txtCantidadReserva.setText(String.valueOf(p.getdQuantityReserve()));
        txtPrecioCompra.setText(simboloMoneda + String.format("%.2f", p.getPrecioCompra()));
        txtPrecioVenta.setText(simboloMoneda + String.format("%.2f", p.getPrecioVenta()));
        txtInformacionAdicional.setText(p.getcAdditionalInformation());
        imgData = p.getbImage();
        if (imgData != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
            imageProduct.setImageBitmap(bmp);

        }
    }

    public class DescargarInformacionProducto extends AsyncTask<Integer, Void, mProduct> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }

        @Override
        protected mProduct doInBackground(Integer... integers) {
            return controladorProductos.getProductoPorIdImagen(integers[0]);
        }


        @Override
        protected void onPostExecute(mProduct product) {
            super.onPostExecute(product);
            progressBar.setVisibility(View.GONE);
            completarDatos(product);
            scrollView.setVisibility(View.VISIBLE);
        }
    }
}
