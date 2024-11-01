package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.view.View;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * Created by OMAR CHH on 02/03/2018.
 */

public class DialogInsertOption extends DialogFragment {

    TextInputLayout edtNombreOpcion;
    Dialog dialog;
    AgregarOpcion agregarOpcion;
    public interface AgregarOpcion{
        void obtenerDatoOpcion(String opcion);
    }

    public void setAgregarOpcion(AgregarOpcion agregarOpcion){
        this.agregarOpcion=agregarOpcion;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=((Activity)getActivity()).getLayoutInflater().inflate(R.layout.dialog_insert_option,null);
        edtNombreOpcion=(TextInputLayout)v.findViewById(R.id.edtNombreOpcion);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle("Ingrese nombre de opción");
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(edtNombreOpcion.getEditText().getText().toString().trim().equals("")){
                        dialogInterface.cancel();
                        Toast.makeText(getActivity(),"Debe ingresar el nombre de la opción",Toast.LENGTH_SHORT).show();
                }
                else {
                    agregarOpcion.obtenerDatoOpcion(edtNombreOpcion.getEditText().getText().toString());
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
