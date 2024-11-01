package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

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

import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleModificador;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 02/05/2018.
 */

public class DialogAgregarValorModificador extends DialogFragment {


    TextView txtTitulo;
    TextInputLayout edtValorModificador,edtMonto;
    ListenerValorModificador listenerValorModificador;
    byte Accion;
    String descripcion;
    RadioGroup rgTipoMd;
    DetalleModificador detalleModificador;


    public void setDetalleModificador(DetalleModificador detalleModificador) {
        this.detalleModificador = detalleModificador;
    }

    public void setAccion(byte Accion){

        this.Accion=Accion;

    }

    public DialogAgregarValorModificador newInstance(DetalleModificador detalleModificador,byte accion){

        DialogAgregarValorModificador d=new DialogAgregarValorModificador();
        d.setAccion(accion);
        d.setDetalleModificador(detalleModificador);
        return d;
    }

    public void setDescripcion(String descripcion){
        this.descripcion=descripcion;
    }
    public interface ListenerValorModificador{

        public void setValorModificador(DetalleModificador detalleModificador);
        public void setEditarValorModificador(DetalleModificador detalleModificador);

    }

    public void setListenerValorModificador(ListenerValorModificador listenerValorModificador){
        this.listenerValorModificador=listenerValorModificador;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view= (getActivity()).getLayoutInflater().inflate(R.layout.dialog_add_modificador,null);
        View title=(getActivity()).getLayoutInflater().inflate(R.layout.custom_title_dialog,null);
        rgTipoMd=view.findViewById(R.id.rgTipoMd);
        rgTipoMd.setVisibility(View.GONE);
        edtValorModificador=view.findViewById(R.id.edtModificador);
        edtValorModificador.setHint("Descripcion del valor");

        edtMonto=view.findViewById(R.id.edtMonto);
        edtMonto.setVisibility(View.GONE);
        txtTitulo=title.findViewById(R.id.txtTitulo);
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(rgTipoMd.getCheckedRadioButtonId()==R.id.rbAumenta){
                    detalleModificador.setFactorModificador(1);
                }
                else if(rgTipoMd.getCheckedRadioButtonId()==R.id.rbResta){
                    detalleModificador.setFactorModificador(-1);
                }
                detalleModificador.setDescripcionModificador(edtValorModificador.getEditText().getText().toString().trim());
                if(edtMonto.getEditText().getText().toString().equals("")){
                    detalleModificador.setMonto(new BigDecimal(0));
                }else if(edtMonto.getEditText().getText().toString().equals(".")){
                    detalleModificador.setMonto(new BigDecimal(0));
                }else{
                    detalleModificador.setMonto(new BigDecimal(edtMonto.getEditText().getText().toString()));

                }
                 if(Accion==0) {
                    listenerValorModificador.setValorModificador(detalleModificador);
                }
                else if(Accion==1){

                    listenerValorModificador.setEditarValorModificador(detalleModificador);

                }
            }
        }).setNegativeButton("Salir",null);
        if(Accion==1){

            txtTitulo.setText("Editar valor");
            edtValorModificador.getEditText().setText(detalleModificador.getDescripcionModificador());
            edtMonto.getEditText().setText(String.format("%.2f",detalleModificador.getMonto()));
            if(detalleModificador.getFactorModificador()==1){
                rgTipoMd.check(R.id.rbAumenta);
            }else if(detalleModificador.getFactorModificador()==-1){
                rgTipoMd.check(R.id.rbResta);
            }
        }
        else if(Accion==0){
            rgTipoMd.check(R.id.rbAumenta);
            txtTitulo.setText("Agregar valor");
            edtValorModificador.getEditText().setText("");
            edtMonto.getEditText().setText(String.format("%.2f",new BigDecimal(0)));

        }
        builder.setCustomTitle(title);
        builder.setView(view);
        return builder.create();
    }
}
