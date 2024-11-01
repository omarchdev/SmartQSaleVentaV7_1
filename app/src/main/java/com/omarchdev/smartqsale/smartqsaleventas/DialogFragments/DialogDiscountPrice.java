package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.InterfaceDiscount;
import com.omarchdev.smartqsale.smartqsaleventas.R;


/**
 * Created by OMAR CHH on 22/11/2017.
 */

public class DialogDiscountPrice extends DialogFragment implements View.OnClickListener,TextWatcher,DialogInterface.OnDismissListener{
    ImageButton imgCancel,imgAccept;
    InterfaceDiscount interfaceDiscount;
    float Descuento;
    Context context;
    TextView txtPrecioOriginal;
    EditText edtPorcentaje;
    Dialog dialog;
    TextView txtPrecioFinal;
    float precioFinal;
    float precio;

    public void setListener(InterfaceDiscount interfaceDiscount){
        this.interfaceDiscount=interfaceDiscount;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=((Activity)context).getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_discount_price_sale,null);
        txtPrecioOriginal=(TextView)v.findViewById(R.id.txtPrecioOriginal);
        txtPrecioFinal=(TextView)v.findViewById(R.id.txtPrecioDescuentoFinal);
        edtPorcentaje=(EditText)v.findViewById(R.id.edtPorcentaje);
        edtPorcentaje.addTextChangedListener(this);
        txtPrecioOriginal.setText("S/"+String.valueOf(precio));
        builder.setTitle("Descuento").setIcon(R.drawable.sale).setView(v);
        dialog=builder.create();
        return dialog;


    }

    public float GenerarDescuento(float precio,float porcentaje){

        return((100-porcentaje)*precio)/100;

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String porcentajeTexto=String.valueOf(s);
        float porcentaje=0f;
        if(porcentajeTexto.equals("")){
            porcentaje=0;
        }
        else if(!porcentajeTexto.equals("")){
         porcentaje=Float.valueOf(porcentajeTexto);

        }
         Descuento=porcentaje;
       precioFinal=GenerarDescuento(precio,porcentaje);
       interfaceDiscount.precioDescuentoFinal(precioFinal,Descuento);
       txtPrecioFinal.setText(String.valueOf(precioFinal));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onClick(View v) {
    }
}
