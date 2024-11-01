package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 30/11/2017.
 */

public class dialogCalculadoraDescuento extends DialogFragment implements View.OnClickListener{

    byte tipoDescuento; // 0=Sin tipo descuento 1=Por valor  2=Por procentaje
    BigDecimal valorComparacion;
    BigDecimal valorMaximoPorcentajeDescuento;
    String cadenaIngresada;
    String cadenaDescuento;
    Context context;
    RadioGroup radioGroup;
    RadioButton rbValue,rbPercent;
    Button btnNumber1,btnNumber2,btnNumber3,btnNumber4,btnNumber5,btnNumber6,btnNumber7,btnNumber8,btnNumber9,btnNumber0,btnCancel,btnSalir,btnGuardar;
    ImageButton btnDelete;

    TextView txtPrecioOriginal,txtValorDescuento;
    String simboloPorcentaje;
    Dialog dialog;
    BigDecimal precioOriginal;
    BigDecimal valorDescuento;
    BigDecimal cantidadCobrar;
    Descuento descuento;
    String tDescuento;

    public void setInfoDescuento(Context contex,BigDecimal cantidadCobrar,BigDecimal valorDescuento,byte TipoDescuento){
        this.context=contex;
        this.valorDescuento=valorDescuento;
        this.tipoDescuento=TipoDescuento;
        this.cantidadCobrar=cantidadCobrar;

    }
    /*
    public dialogCalculadoraDescuento(Context context, BigDecimal cantidadCobrar, BigDecimal valorDescuento, byte tipoDescuento) {
        this.context=context;
        this.cantidadCobrar = cantidadCobrar;
        this.valorDescuento = valorDescuento;
        this.tipoDescuento = tipoDescuento;
        tDescuento="P";
    }
*/
    public void setListenerDescuento(Descuento descuento) {

        this.descuento = descuento;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View v= ((Activity)context).getLayoutInflater().inflate(R.layout.calculador_descuento,null);
        cadenaIngresada="";
        simboloPorcentaje="%";
        //---------Float------//
        valorComparacion = new BigDecimal(0);
        precioOriginal = cantidadCobrar;
        valorMaximoPorcentajeDescuento = new BigDecimal(99.00);

        //----------TextView----------//

        txtPrecioOriginal=(TextView)v.findViewById(R.id.txtValorOriginal);
        txtValorDescuento=(TextView)v.findViewById(R.id.txtValorDescuento);

        //---------Declarar Botones---------//
        btnDelete=(ImageButton)v.findViewById(R.id.btnNumberDelete);
        btnCancel=(Button)v.findViewById(R.id.btnNumberCancel);
        btnSalir=(Button)v.findViewById(R.id.btnCancelar);

        btnGuardar=(Button)v.findViewById(R.id.btnGuardar);
        btnNumber0=(Button)v.findViewById(R.id.btnNumber0);
        btnNumber1=(Button)v.findViewById(R.id.btnNumber1);
        btnNumber2=(Button)v.findViewById(R.id.btnNumber2);
        btnNumber3=(Button)v.findViewById(R.id.btnNumber3);
        btnNumber4=(Button)v.findViewById(R.id.btnNumber4);
        btnNumber5=(Button)v.findViewById(R.id.btnNumber5);
        btnNumber6=(Button)v.findViewById(R.id.btnNumber6);
        btnNumber7=(Button)v.findViewById(R.id.btnNumber7);
        btnNumber8=(Button)v.findViewById(R.id.btnNumber8);
        btnNumber9=(Button)v.findViewById(R.id.btnNumber9);

        //---------Declarar Radio Button Group--------//

        radioGroup=(RadioGroup)v.findViewById(R.id.rGroupTipoDescuento);
        rbValue=(RadioButton)v.findViewById(R.id.rbValue);
        rbPercent=(RadioButton)v.findViewById(R.id.rbPercent);
        //-----String------------//
        if (valorDescuento.compareTo(new BigDecimal(0.00)) == 0) {
            cadenaDescuento = "00.00";
        } else {
            cadenaDescuento = String.format("%.2f", valorDescuento);
        }


        rbValue.setText(Constantes.DivisaPorDefecto.SimboloDivisa);
        //----------Enviar Valores----///
        txtPrecioOriginal.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", precioOriginal));
        txtValorDescuento.setText(cadenaDescuento);

        valorComparacion = valorMaximoPorcentajeDescuento;
        setListenerClick();

        if(tipoDescuento==0){
            radioGroup.check(R.id.rbValue);
            valorComparacion = precioOriginal;
            validarCantidadPorValor();
            EnviarDatoTexto();
            tDescuento="V";

        }
        else if(tipoDescuento==1){
            radioGroup.check(R.id.rbValue);

            valorComparacion = precioOriginal;
            validarCantidadPorValor();
            EnviarDatoTexto();
            tDescuento="V";


        }else if(tipoDescuento==2){
            radioGroup.check(R.id.rbPercent);

            valorComparacion = valorMaximoPorcentajeDescuento;
            validarCantidadPorcentajeDescuento();
            EnviarDatoTexto();
            tDescuento="P";

        }

        //----------------------------------//
        dialog=builder.setView(v).create();

        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
           case R.id.btnNumber0:
                ModificarValorDescuentoPorcentaje(new BigDecimal(0));
                VerificarValorDescuento();
                break;
            case R.id.btnCancelar:
                dialog.dismiss();
                break;
            case R.id.btnGuardar:
                GuardarInformacionDescuento();
                dialog.dismiss();
                break;
            case R.id.btnNumber1:
                ModificarValorDescuentoPorcentaje(new BigDecimal(1));
                VerificarValorDescuento();
                break;
            case R.id.btnNumber2:
                ModificarValorDescuentoPorcentaje(new BigDecimal(2));
                VerificarValorDescuento();
                break;
            case R.id.btnNumber3:
                ModificarValorDescuentoPorcentaje(new BigDecimal(3));
                VerificarValorDescuento();
                break;
            case R.id.btnNumber4:
                ModificarValorDescuentoPorcentaje(new BigDecimal(4));
                VerificarValorDescuento();
                break;
            case R.id.btnNumber5:
                ModificarValorDescuentoPorcentaje(new BigDecimal(5));
                VerificarValorDescuento();
                break;
            case R.id.btnNumber6:
                ModificarValorDescuentoPorcentaje(new BigDecimal(6));
                VerificarValorDescuento();
                break;
            case R.id.btnNumber7:
                ModificarValorDescuentoPorcentaje(new BigDecimal(7));
                VerificarValorDescuento();
                break;
            case R.id.btnNumber8:
                ModificarValorDescuentoPorcentaje(new BigDecimal(8));
                VerificarValorDescuento();
                break;
            case R.id.btnNumber9:
                ModificarValorDescuentoPorcentaje(new BigDecimal(9));
                VerificarValorDescuento();
                break;
            case R.id.btnNumberDelete:
                EliminarCifras();
                EnviarDatoTexto();
                break;
            case R.id.btnNumberCancel:
                valorDescuento = new BigDecimal(0);
                EnviarDatoTexto();
                break;
            case R.id.rbPercent:
                valorComparacion = valorMaximoPorcentajeDescuento;
                validarCantidadPorcentajeDescuento();
                EnviarDatoTexto();
                tDescuento="P";
                break;
            case R.id.rbValue:
                valorComparacion = precioOriginal;
                validarCantidadPorValor();
                EnviarDatoTexto();
                tDescuento="V";
                break;
        }

    }

    public void VerificarValorDescuento(){

        if (rbPercent.isChecked()) {
            validarCantidadPorcentajeDescuento();
            tipoDescuento = 2;
        } else if (rbValue.isChecked()) {
            validarCantidadPorValor();
            tipoDescuento = 1;
        }
        EnviarDatoTexto();
    }

    public void validarCantidadPorcentajeDescuento() {

        if (valorDescuento.compareTo(valorComparacion) > 0) {
            valorDescuento = new BigDecimal(99.00);
        }
    }
    public void validarCantidadPorValor() {
        if (valorDescuento.compareTo(valorComparacion) >= 0) {
            valorDescuento = precioOriginal.subtract(new BigDecimal(0.10));
        }
    }
    public void ModificarValorDescuentoPorcentaje(BigDecimal valorIngresado) {

        valorDescuento = (valorDescuento.multiply(new BigDecimal(10))).add(valorIngresado.divide(new BigDecimal(100)));
    }
    public void EliminarCifras() {

        String nuevaCadena = "";
        nuevaCadena = txtValorDescuento.getText().toString();
        nuevaCadena = nuevaCadena.substring(0, nuevaCadena.length() - 1);
        valorDescuento = new BigDecimal(nuevaCadena).divide(new BigDecimal(10));
    }
    public void EnviarDatoTexto(){
        txtValorDescuento.setText(String.format("%.2f", valorDescuento));
    }
    public void setListenerClick(){
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
        rbValue.setOnClickListener(this);
        rbPercent.setOnClickListener(this);
    }

    public void GuardarInformacionDescuento() {

        if (valorDescuento.compareTo(new BigDecimal(0.00)) == 0) {
            tipoDescuento = 0;
            descuento.InformacionDescuento(tDescuento,tipoDescuento, new BigDecimal(0.00));
        } else if (valorDescuento.compareTo(new BigDecimal(0.01)) > 0) {
            descuento.InformacionDescuento(tDescuento,tipoDescuento, valorDescuento);
        }

    }

    public interface Descuento {

        public void InformacionDescuento(String tDescuento,byte tipoDescuento, BigDecimal valorDescuento);
    }


}
