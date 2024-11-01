package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * Created by OMAR CHH on 28/11/2017.
 */

public class DialogEditQuantity extends DialogFragment implements View.OnClickListener {

    Context context;
    EditText edtquantity;
    ImageButton imgPlus,imgMinus;

    public DialogEditQuantity newInstance(){
        DialogEditQuantity d=new DialogEditQuantity();

        return d;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        View v=((Activity)context).getLayoutInflater().inflate(R.layout.dialog_edit_price,null);
        edtquantity=(EditText)v.findViewById(R.id.edtQuantityProduct);
        imgPlus=(ImageButton)v.findViewById(R.id.btnPlus);
        imgMinus=(ImageButton)v.findViewById(R.id.btnMinusb);
        imgPlus.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
         Dialog dialog=builder.setTitle("Editar Cantidad Producto").setView(v).create();

        return dialog;
    }

    @Override
    public void onClick(View v) {


    }
}
