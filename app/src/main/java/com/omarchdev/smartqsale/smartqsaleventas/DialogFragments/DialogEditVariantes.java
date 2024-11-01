package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProcesoVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.AdditionalPriceProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RVAdapterAdditionalPrice;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by OMAR CHH on 29/03/2018.
 */

public class DialogEditVariantes extends DialogFragment implements DialogScannerCam.ScannerResult, View.OnClickListener, CompoundButton.OnCheckedChangeListener, AsyncProcesoVenta.ListenerPrecioVentaAdiccional {
    boolean p;
    boolean pmultiple;
    boolean permitir;
    EditText edtPVentaN;
    DialogScannerCam dialogScannerCam;
    Button btnSalir,btnGuardar;
    String tituloVariante;
    TextInputLayout edtStock,edtPCompra,edtPVenta,edtCodigoBarra;
    ImageButton btnScanCode,btnAgregarPVenta;
    BigDecimal stock,pVenta,pCompra;
    int idVariante;
    InfoModificarListener infoModificarListener;
    String codigoBarra;
    RVAdapterAdditionalPrice adapterAdditionalPrice;
    CheckBox cbPVenta;
    RecyclerView rvPrecioVentas;
    Switch switchPrecioMult;
    AsyncProcesoVenta asyncProcesoVenta;
    @Override
    public void ResultadoScanner(String resultText) {
        edtCodigoBarra.getEditText().setText(resultText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnScanCode:
                DialogFragment dialogFragment=dialogScannerCam;
                dialogFragment.show(getFragmentManager(),"Scanner");
            break;
            case R.id.btnAgregarPVenta:
                if(switchPrecioMult.isChecked()) {
                    AgregarPVentaAddicional();
                }else{
                    new AlertDialog.Builder(getActivity()).setTitle("Advertencia")
                            .setMessage("Debe activar la opcion multiples precios de venta para usar está opción")
                            .setPositiveButton("Salir",null).create().show();
                }
                break;
            case R.id.btnSalir:

                getDialog().dismiss();

                break;
            case R.id.btnGuardar:
                p=true;

                if(switchPrecioMult.isChecked() && adapterAdditionalPrice.AdditionalPrice().size()==0){
                    p=false;
                    Toast.makeText(getActivity(),
                            "Debe agregar los precios de venta para el producto",
                            Toast.LENGTH_LONG).show();
                }
                if(p) {
                    getDialog().dismiss();
                    infoModificarListener.setInfoEditVariante(idVariante,
                            stock,
                            pVenta,
                            pCompra,
                            codigoBarra, adapterAdditionalPrice.AdditionalPrice(),
                            switchPrecioMult.isChecked());
                }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            edtPVenta.setEnabled(false);
            rvPrecioVentas.setVisibility(View.VISIBLE);
        }else{
            edtPVenta.setEnabled(true);
            rvPrecioVentas.setVisibility(View.GONE);
        }
    }

    @Override
    public void ObtenerPreciosVentaAd(List<AdditionalPriceProduct> additionalPriceProductList) {
        adapterAdditionalPrice.AgregarListado(additionalPriceProductList);
    }

    @Override
    public void ErrorConsulta() {

    }

    public interface InfoModificarListener{

        public void setInfoEditVariante(int idVariante, BigDecimal stock,
                                        BigDecimal precioVenta, BigDecimal precioCompra,
                                        String codigoBarra,
                                        List<AdditionalPriceProduct> additionalPriceProductList,boolean bPVMultiple);

    }

    public void setInfoModificarListener(InfoModificarListener infoModificarListener){
        this.infoModificarListener=infoModificarListener;
    }

    public void setData(int idVariante,float st,BigDecimal pV,BigDecimal  pC,String tituloVariante,String codigoBarra,boolean pmultiple){
        this.idVariante=idVariante;
        stock=new BigDecimal(st);
        pVenta=pV;
        pCompra=pC;
        this.tituloVariante=tituloVariante;
        this.codigoBarra=codigoBarra;
        this.pmultiple=pmultiple;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_variante,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()).setView(v);
        try {

            btnSalir=v.findViewById(R.id.btnSalir);
            btnGuardar=v.findViewById(R.id.btnGuardar);
            switchPrecioMult = v.findViewById(R.id.switchPrecioMult);
            switchPrecioMult.setOnCheckedChangeListener(this);
            btnGuardar.setOnClickListener(this);
            btnSalir.setOnClickListener(this);
            btnAgregarPVenta = v.findViewById(R.id.btnAgregarPVenta);
            switchPrecioMult.setChecked(false);
            dialogScannerCam = new DialogScannerCam();
            dialogScannerCam.setScannerResult(this);
            btnSalir.setOnClickListener(this);
            edtStock = (TextInputLayout) v.findViewById(R.id.edtStock);
            edtPCompra = (TextInputLayout) v.findViewById(R.id.edtPrecioCompra);
            edtPVenta = (TextInputLayout) v.findViewById(R.id.edtPrecioVenta);
            edtCodigoBarra = (TextInputLayout) v.findViewById(R.id.edtCodigoBarra);
            btnScanCode = (ImageButton) v.findViewById(R.id.btnScanCode);
            edtPVenta.getEditText().setText(String.format("%.2f", pVenta));
            edtStock.getEditText().setText(String.format("%.2f", stock));
            edtPCompra.getEditText().setText(String.format("%.2f", pCompra));
            edtCodigoBarra.getEditText().setText(codigoBarra);
            btnScanCode.setOnClickListener(this);
            edtStock.getEditText().setEnabled(false);
            rvPrecioVentas=v.findViewById(R.id.rvPVentaAdd);
            adapterAdditionalPrice = new RVAdapterAdditionalPrice();
            edtStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            "Los cambios en el stock se realizan en la seccion Movimientos Almacén.", Toast.LENGTH_LONG).show();
                }

            });

            builder.setTitle(tituloVariante);
            edtPCompra.getEditText().addTextChangedListener(listenerTextWatcherCompra);
            edtStock.getEditText().addTextChangedListener(listenerTextWatcherStock);
            edtPVenta.getEditText().addTextChangedListener(listenerTextWatcherPVenta);
            edtCodigoBarra.getEditText().addTextChangedListener(listenerTextWatcherCodigoBarra);
            edtStock.setVisibility(View.GONE);
            rvPrecioVentas.setAdapter(adapterAdditionalPrice);
            rvPrecioVentas.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            btnAgregarPVenta.setOnClickListener(this);
            switchPrecioMult.setChecked(pmultiple);
            asyncProcesoVenta=new AsyncProcesoVenta();
            if(pmultiple){
                asyncProcesoVenta.ObtenerPreciosVenta(idVariante);
                asyncProcesoVenta.setListenerPrecioVentaAdiccional(this);
            }
        }catch (Exception e){
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
        }
        return builder.create();
    }

    TextWatcher listenerTextWatcherCodigoBarra=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            codigoBarra=charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    TextWatcher listenerTextWatcherCompra=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!charSequence.toString().equals("")){
                pCompra=new BigDecimal(charSequence.toString());
            }
            else{
               pCompra=new BigDecimal(0);
               edtPCompra.getEditText().setText("0.00");
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    TextWatcher listenerTextWatcherStock=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!charSequence.toString().equals("")){
                stock=new BigDecimal(charSequence.toString());
            }
            else{
                stock=new BigDecimal(0);
                edtStock.getEditText().setText("0.00");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    TextWatcher listenerTextWatcherPVenta=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!charSequence.toString().equals("")){

                pVenta=new BigDecimal(charSequence.toString());

            }
            else{

                pVenta=new BigDecimal(0);
                edtPVenta.getEditText().setText("0.00");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void AgregarPVentaAddicional(){

        View d= getActivity().getLayoutInflater().inflate(R.layout.dialog_agregar_pventa,null);
        edtPVentaN=d.findViewById(R.id.edtPVentaN);
         permitir=true;
        new AlertDialog.Builder(getActivity()).
                setView(d).setTitle("Agregar precio de Venta")
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permitir=true;
                        if(edtPVentaN.getText().toString().equals(".")){
                            permitir=false;
                        }else if(edtPVentaN.getText().toString().equals("")){
                            permitir=false;
                        }
                        if(permitir) {
                            if (new BigDecimal(edtPVentaN.getText().toString()).
                                    compareTo(new BigDecimal(0)) == 1) {
                                adapterAdditionalPrice.add(new BigDecimal(edtPVentaN.getText()
                                        .toString().replace(",",".")));
                            } else {
                                Toast.makeText(getActivity(),
                                        "El valor debe ser mayor a 0", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).setNegativeButton("Cancelar",null).create().show();
        edtPVentaN.setHint(Constantes.DivisaPorDefecto.SimboloDivisa+"0.00");

    }

}
