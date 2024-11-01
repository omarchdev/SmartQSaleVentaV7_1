package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * Created by OMAR CHH on 01/05/2018.
 */

public class DialogAgregarOpcion extends DialogFragment {

    TextView txtTitulo;
    TextInputLayout edtModificador,edtMonto;
    ListenerAddOpcion listenerAddOpcion;
    RadioGroup rgTipoMd;
    String titulo;
    String descripcion;
    byte Accion;

    public void setListenerAddOpcion(ListenerAddOpcion listenerAddOpcion){

        this.listenerAddOpcion=listenerAddOpcion;


    }

    public void setAccion(byte accion){
        this.Accion=accion;
    }


    public interface ListenerAddOpcion{

        public void IngresarModificador(String modificadorDescripcion);
        public void EditarModificador(String modificadorDescripcion);

    }

    public void setTitulo(String titulo){
        this.titulo=titulo;
    }
    public void setDescripcion(String descripcion){
        this.descripcion=descripcion;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view= ((Activity)getActivity()).getLayoutInflater().inflate(R.layout.dialog_add_modificador,null);
        View title=((Activity)getActivity()).getLayoutInflater().inflate(R.layout.custom_title_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setCustomTitle(title);
        edtModificador=view.findViewById(R.id.edtModificador);
        edtMonto=view.findViewById(R.id.edtMonto);
        rgTipoMd=view.findViewById(R.id.rgTipoMd);
        rgTipoMd.setVisibility(View.GONE);
        edtMonto.setVisibility(View.GONE);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Accion==0){
                    listenerAddOpcion.IngresarModificador(edtModificador.getEditText().getText().toString());
                }
                else if(Accion==1){
                    listenerAddOpcion.EditarModificador(edtModificador.getEditText().getText().toString());
                }

            }
        }).setNegativeButton("Salir",null);

        txtTitulo=title.findViewById(R.id.txtTitulo);
        if(Accion==0) {
            txtTitulo.setText("Agregar modificador");
        }
        else if(Accion==1){
            txtTitulo.setText("Editar modificador");
            edtModificador.getEditText().setText(descripcion);
        }

        builder.setView(view);

        return builder.create();
    }
}
