package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Control.NumberTextWatcher;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 22/01/2018.
 */

public class DialogAperturaCaja extends DialogFragment implements View.OnClickListener, NumberTextWatcher.INumberTextWatcher {

    Dialog dialog;
    String montoApertura = "";
    boolean cambio = false;
    BigDecimal bMonto;
    Button btn1, btn5, btn10, btn50;
    TextView txtMonto;
    EditText editText;
    AperturaCaja aperturaCaja;
  //  BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    TextView txtMoneda;

    public void setAperturaCaja(AperturaCaja aperturaCaja) {

        this.aperturaCaja = aperturaCaja;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_apertura_caja, null);

        bMonto = new BigDecimal(0);
        montoApertura = String.format("%.2f", bMonto);

        btn1 = (Button) v.findViewById(R.id.btn1);
        btn5 = (Button) v.findViewById(R.id.btn5);
        btn10 = (Button) v.findViewById(R.id.btn10);
        btn50 = (Button) v.findViewById(R.id.btn50);
        txtMonto = (TextView) v.findViewById(R.id.txtMonto);
        editText = (EditText) v.findViewById(R.id.edtMontoApertura);
        txtMoneda=v.findViewById(R.id.txtMoneda);


        txtMoneda.setText(Constantes.DivisaPorDefecto.SimboloDivisa);
        btn1.setText("+" + Constantes.DivisaPorDefecto.SimboloDivisa + "1");
        btn5.setText("+" + Constantes.DivisaPorDefecto.SimboloDivisa + "5");
        btn10.setText("+" + Constantes.DivisaPorDefecto.SimboloDivisa + "10");
        btn50.setText("+" + Constantes.DivisaPorDefecto.SimboloDivisa + "50");
        editText.setText(montoApertura);
        editText.setSelection(montoApertura.length());
        NumberTextWatcher textWatcher=new NumberTextWatcher( editText);
        textWatcher.setINumberTextWatcher(this);
        editText.addTextChangedListener(textWatcher);
       // editText.addTextChangedListener(this);
        btn1.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn50.setOnClickListener(this);
        builder.setPositiveButton("Abrir Caja", (dialog, which) -> aperturaCaja.VerificarCajaAbierta(bMonto)).
                setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        dialog = builder.setView(v).create();
        dialog.setCanceledOnTouchOutside(false);


        return dialog;
    }
/*
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        if(ReplaceCommaToDot(s.toString()).equals(".")){
            bMonto=new BigDecimal(0);

        }else if(ReplaceCommaToDot(s.toString()).equals("")){
            bMonto=new BigDecimal(0);
        }else if(ReplaceCommaToDot(s.toString()).equals(".0")){
            bMonto=new BigDecimal(0);
        }else{
            bMonto=new BigDecimal(ReplaceCommaToDot(s.toString()));
        }
          montoApertura = String.format("%.2f", bMonto);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn1:
                modificarMonto(new BigDecimal(1));
                break;


            case R.id.btn5:
                modificarMonto(new BigDecimal(5));
                break;

            case R.id.btn10:
                modificarMonto(new BigDecimal(10));
                break;

            case R.id.btn50:
                modificarMonto(new BigDecimal(50));
                break;
        }

    }

    private void modificarMonto(BigDecimal monto) {
      //  editText.removeTextChangedListener(this);
        bMonto = bMonto.add(monto);
        montoApertura = String.format("%.2f", bMonto);
        editText.setText(montoApertura);
        editText.setSelection(montoApertura.length());
     //   editText.addTextChangedListener(this);

    }

    @Override
    public void cantidadBigDecimal(@NonNull BigDecimal number) {
    //    modificarMonto(number);
        bMonto=number;
    }

    public interface AperturaCaja {

        public void VerificarCajaAbierta(BigDecimal montoApertura);
    }

}
