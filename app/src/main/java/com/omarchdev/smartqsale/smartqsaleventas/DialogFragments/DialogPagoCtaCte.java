package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 16/01/2018.
 */

public class DialogPagoCtaCte extends DialogFragment implements View.OnClickListener {

    Button btnNumber1, btnNumber2, btnNumber3, btnNumber4, btnNumber5, btnNumber6, btnNumber7, btnNumber8, btnNumber9, btnNumber0, btnCancel, btnSalir, btnGuardar;
    ImageButton btnDelete;
    Dialog dialog;
    BigDecimal montoPagar;
    TextView txtMontoPagar;
    ListenerCalculadoraPago listenerCalculadoraPago;


    public DialogPagoCtaCte() {
    }

    public void setListenerCalculadoraPago(ListenerCalculadoraPago listenerCalculadoraPago) {

        this.listenerCalculadoraPago = listenerCalculadoraPago;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = (getActivity().getLayoutInflater().inflate(R.layout.dialog_pago_cta_cte, null));

        montoPagar = new BigDecimal(0);

        btnDelete = (ImageButton) v.findViewById(R.id.btnNumberDelete);
        btnCancel = (Button) v.findViewById(R.id.btnNumberCancel);
        btnSalir = (Button) v.findViewById(R.id.btnCancelar);
        txtMontoPagar = (TextView) v.findViewById(R.id.txtValorOriginal);
        btnGuardar = (Button) v.findViewById(R.id.btnGuardar);
        btnNumber0 = (Button) v.findViewById(R.id.btnNumber0);
        btnNumber1 = (Button) v.findViewById(R.id.btnNumber1);
        btnNumber2 = (Button) v.findViewById(R.id.btnNumber2);
        btnNumber3 = (Button) v.findViewById(R.id.btnNumber3);
        btnNumber4 = (Button) v.findViewById(R.id.btnNumber4);
        btnNumber5 = (Button) v.findViewById(R.id.btnNumber5);
        btnNumber6 = (Button) v.findViewById(R.id.btnNumber6);
        btnNumber7 = (Button) v.findViewById(R.id.btnNumber7);
        btnNumber8 = (Button) v.findViewById(R.id.btnNumber8);
        btnNumber9 = (Button) v.findViewById(R.id.btnNumber9);

        txtMontoPagar.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", montoPagar));
        setListenerClick();

        dialog = builder.setView(v).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnNumber0:
                ModificarValorDescuentoPorcentaje(new BigDecimal(0));

                break;
            case R.id.btnCancelar:
                dialog.dismiss();
                break;
            case R.id.btnGuardar:
                dialog.dismiss();
                listenerCalculadoraPago.CapturarMontoPago(montoPagar);
                break;
            case R.id.btnNumber1:
                ModificarValorDescuentoPorcentaje(new BigDecimal(1));

                break;
            case R.id.btnNumber2:
                ModificarValorDescuentoPorcentaje(new BigDecimal(2));

                break;
            case R.id.btnNumber3:
                ModificarValorDescuentoPorcentaje(new BigDecimal(3));

                break;
            case R.id.btnNumber4:
                ModificarValorDescuentoPorcentaje(new BigDecimal(4));

                break;
            case R.id.btnNumber5:
                ModificarValorDescuentoPorcentaje(new BigDecimal(5));

                break;
            case R.id.btnNumber6:

                ModificarValorDescuentoPorcentaje(new BigDecimal(6));

                break;
            case R.id.btnNumber7:
                ModificarValorDescuentoPorcentaje(new BigDecimal(7));

                break;
            case R.id.btnNumber8:
                ModificarValorDescuentoPorcentaje(new BigDecimal(8));

                break;
            case R.id.btnNumber9:
                ModificarValorDescuentoPorcentaje(new BigDecimal(9));

                break;
            case R.id.btnNumberDelete:
                EliminarCifras();
                EnviarDatoTexto();
                break;
            case R.id.btnNumberCancel:
                montoPagar = new BigDecimal(0);
                EnviarDatoTexto();
                break;
        }

    }

    public void ModificarValorDescuentoPorcentaje(BigDecimal valorIngresado) {
        montoPagar = (montoPagar.multiply(new BigDecimal(10))).add(valorIngresado.divide(new BigDecimal(100)));
        EnviarDatoTexto();
    }

    public void setListenerClick() {
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        btnNumber0.setOnClickListener(this);
        btnNumber1.setOnClickListener(this);
        btnNumber2.setOnClickListener(this);
        btnNumber3.setOnClickListener(this);
        btnNumber4.setOnClickListener(this);
        btnNumber5.setOnClickListener(this);
        btnNumber6.setOnClickListener(this);
        btnNumber7.setOnClickListener(this);
        btnNumber8.setOnClickListener(this);
        btnNumber9.setOnClickListener(this);
    }

    public void EliminarCifras() {

        String nuevaCadena = "";
        nuevaCadena = txtMontoPagar.getText().toString();
        nuevaCadena = nuevaCadena.substring(0, nuevaCadena.length() - 1);
        montoPagar = new BigDecimal(nuevaCadena).divide(new BigDecimal(10));


    }

    public void EnviarDatoTexto() {


        txtMontoPagar.setText(String.format("%.2f", montoPagar));
    }

    public interface ListenerCalculadoraPago {

        public void CapturarMontoPago(BigDecimal monto);
    }


}
