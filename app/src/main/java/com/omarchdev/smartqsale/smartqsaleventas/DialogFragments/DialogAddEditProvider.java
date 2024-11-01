package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.omarchdev.smartqsale.smartqsaleventas.InterfaceDataCustomerProvider;
import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * Created by OMAR CHH on 26/11/2017.
 */

public class DialogAddEditProvider extends DialogFragment{
    Context context;
    EditText edtName,edtCompanyName,edtPhone,edtEmail;
    InterfaceDataCustomerProvider interfaceDataCustomerProvider;


    public void setListenerInterfaceData(InterfaceDataCustomerProvider interfaceData){
        this.interfaceDataCustomerProvider=interfaceData;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View v=((Activity)context).getLayoutInflater().inflate(R.layout.add_edit_provider,null);

        edtName=(EditText)v.findViewById(R.id.edtProductName);
        edtCompanyName=(EditText)v.findViewById(R.id.edtProviderCompany);
        edtPhone=(EditText)v.findViewById(R.id.edtProviderPhone);
        edtEmail=(EditText)v.findViewById(R.id.edtProviderEmail);

        builder.setView(v).setTitle("Agregar Proveedor");

        return builder.create();
    }

    public void CapturaInformacion(){

        interfaceDataCustomerProvider.setDataCustomer(edtName.getText().toString(),edtCompanyName.getText().toString(),edtPhone.getText().toString(),edtEmail.getText().toString(),"");

    }
}
