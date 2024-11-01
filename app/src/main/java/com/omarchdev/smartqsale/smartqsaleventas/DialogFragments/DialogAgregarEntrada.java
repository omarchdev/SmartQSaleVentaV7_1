package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import static com.omarchdev.smartqsale.smartqsaleventas.Control.MethodsNumber.ReplaceCommaToDot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AdapterMPagoSpinner;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDetalleMovCaja;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMotivo_Ingreso_Retiro;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mResumenMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.MotivoRetiroSpinnerAdapter;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;

/**
 * Created by OMAR CHH on 30/01/2018.
 */

public class DialogAgregarEntrada extends DialogFragment implements View.OnClickListener, TextWatcher {


    mDetalleMovCaja movCaja;
    String temp;
    RelativeLayout relativeLayout;
    mCierre cierre;
    int idMedioPago;
    int idMotivo;
    byte tipoMonv = 0;
    List<mMedioPago> listMedioPago;
    List<mMotivo_Ingreso_Retiro> listMotivo;
    List<mResumenMedioPago> mResumenMedioPagoList;
    EditText edtMontoAgregar, edtObservacion;
    Button btn1, btn5, btn10, btn50, btnConfirmar, btnCancelar;
    Spinner spnMotivo, spnMedioPago;
    Dialog dialog;
    BigDecimal bMonto;
    String montoApertura = "";
    BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    TextView txtTitulo, txtValorDisponible;
    String tipo = "";
    AdapterMPagoSpinner adapterMPagoSpinner;
    MotivoRetiroSpinnerAdapter adapterMotivoRetiro;
    CardView cvValorDisponible;
    ProgressBar pbDialog;
    AlertDialog.Builder builder;
    BigDecimal montoComparar;
    CargarDatos cargarDatos;
    EntradaRetiro entradaRetiro;
    Context context;

    public DialogAgregarEntrada() {

    }

    public void setEntradaRetiroListener(EntradaRetiro entradaRetiro) {
        this.entradaRetiro = entradaRetiro;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_entrada_dinero_caja, null);
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        tipo = getTag();
        listMedioPago = new ArrayList<>();
        bMonto = new BigDecimal(0);
        mResumenMedioPagoList = new ArrayList<>();
        context=getActivity();
        montoApertura = Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", bMonto);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.rlContent);
        spnMedioPago = (Spinner) v.findViewById(R.id.spMedio_Pago);
        spnMotivo = (Spinner) v.findViewById(R.id.spMotivo_Ingreso);
        edtMontoAgregar = (EditText) v.findViewById(R.id.edtMontoApertura);
        cvValorDisponible = (CardView) v.findViewById(R.id.cvValorDisponible);
        txtValorDisponible = (TextView) v.findViewById(R.id.txtValorDisponible);
        btnCancelar = (Button) v.findViewById(R.id.btnCancelar);
        btnConfirmar = (Button) v.findViewById(R.id.btnConfirmar);
        edtObservacion = (EditText) v.findViewById(R.id.edtObservacion);

        pbDialog = (ProgressBar) v.findViewById(R.id.pbDialog);
        btn1 = (Button) v.findViewById(R.id.btn1);
        btn5 = (Button) v.findViewById(R.id.btn5);
        btn10 = (Button) v.findViewById(R.id.btn10);
        btn50 = (Button) v.findViewById(R.id.btn50);
        edtMontoAgregar.setInputType(InputType.TYPE_CLASS_NUMBER);
        txtTitulo = (TextView) v.findViewById(R.id.txtTitulo);
        movCaja = new mDetalleMovCaja();
        ModificarDialog();
        montoComparar = new BigDecimal(0);

        btn1.setText("+" + Constantes.DivisaPorDefecto.SimboloDivisa + "1");
        btn5.setText("+" + Constantes.DivisaPorDefecto.SimboloDivisa + "5");
        btn10.setText("+" + Constantes.DivisaPorDefecto.SimboloDivisa + "10");
        btn50.setText("+" + Constantes.DivisaPorDefecto.SimboloDivisa + "50");

        btn1.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn50.setOnClickListener(this);
        btnConfirmar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        edtMontoAgregar.setText(montoApertura);
        edtMontoAgregar.setSelection(montoApertura.length());
        edtMontoAgregar.addTextChangedListener(this);

        adapterMPagoSpinner = new AdapterMPagoSpinner(getActivity(), R.layout.support_simple_spinner_dropdown_item);
        spnMedioPago.setAdapter(adapterMPagoSpinner);
        adapterMotivoRetiro = new MotivoRetiroSpinnerAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item);
        spnMotivo.setAdapter(adapterMotivoRetiro);

        dialog = builder.create();
        ListenerSpinnerMetodoPago();
        ListenerSpinnerMotivo();


        cargarDatos = new CargarDatos();
        cargarDatos.execute();

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (cargarDatos.getStatus() == AsyncTask.Status.RUNNING) {
            cargarDatos.cancel(true);
            Toast.makeText(getActivity(), "Cargar", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (cargarDatos.getStatus() == AsyncTask.Status.RUNNING) {
            cargarDatos.cancel(true);
        }
        Toast.makeText(getActivity(), "Cargar", Toast.LENGTH_SHORT).show();
    }

    public void setCierre(mCierre cierre) {
        this.cierre = cierre;
    }

    private void ListenerSpinnerMotivo() {

        spnMotivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                idMotivo = listMotivo.get(position).getIdMotivo();
                movCaja.setDescripcionMotivo(listMotivo.get(position).getDescripcionMotivo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void ListenerSpinnerMetodoPago() {

        spnMedioPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idMedioPago = listMedioPago.get(position).getiIdMedioPago();
                movCaja.setNombreMedioPago(listMedioPago.get(position).getcDescripcionMedioPago());
                if (tipo.equals("Entrada")) {

                } else if (tipo.equals("Retiro")) {

                    for (int i = 0; i < mResumenMedioPagoList.size(); i++) {

                        if (mResumenMedioPagoList.get(i).getIdMedioPago() == idMedioPago) {
                            montoComparar = mResumenMedioPagoList.get(i).getMontoDisponible();
                            txtValorDisponible.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", montoComparar));
                            break;
                        }

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

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
            case R.id.btnConfirmar:

                new ProcesarMovimiento().execute();

                break;

            case R.id.btnCancelar:
                dialog.dismiss();
                break;
        }


    }



    private byte ConfirmarProceso() {
        byte respuesta = 0;
        if (bMonto.compareTo(new BigDecimal(0)) == 0) {

        } else {

            if (tipo.equals("Retiro")) {


                if (bMonto.compareTo(montoComparar) == 1) {
                    respuesta=2;
                } else if (bMonto.compareTo(montoComparar) <= 0) {
                    respuesta = bdConnectionSql.AgregarIngresoRetiro((byte) 2, bMonto, idMotivo, idMedioPago, edtObservacion.getText().toString(), cierre.getIdCierre());

                }


            } else if (tipo.equals("Entrada")) {
                respuesta = bdConnectionSql.AgregarIngresoRetiro((byte) 1, bMonto, idMotivo, idMedioPago, edtObservacion.getText().toString(), cierre.getIdCierre());

            }

        }

        return respuesta;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        edtMontoAgregar.setSelection(montoApertura.length() - 1);
        temp = s.toString();

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int position = 0;

        temp.length();
        montoApertura = ReplaceCommaToDot(s.toString()) ;
        montoApertura = montoApertura.replace(Constantes.DivisaPorDefecto.SimboloDivisa, "");
       position = montoApertura.indexOf(".");
        if (position != -1) {
            String sub1 = "", sub2 = "";
            sub1 = montoApertura.substring(0, position);
            sub2 = montoApertura.substring(position + 1);
            montoApertura = sub1.concat(sub2);
            bMonto = new BigDecimal(montoApertura);
            bMonto = bMonto.divide(new BigDecimal(100));


        }
        montoApertura = Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", bMonto);
    }

    @Override
    public void afterTextChanged(Editable s) {

        edtMontoAgregar.removeTextChangedListener(this);
        edtMontoAgregar.setText(montoApertura);
        edtMontoAgregar.setSelection(montoApertura.length());
        edtMontoAgregar.addTextChangedListener(this);
    }

    private void modificarMonto(BigDecimal monto) {
        edtMontoAgregar.removeTextChangedListener(this);
        bMonto = bMonto.add(monto);
        montoApertura = Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", bMonto);
        edtMontoAgregar.setText(montoApertura);
        edtMontoAgregar.addTextChangedListener(this);
        edtMontoAgregar.setSelection(montoApertura.length());

    }

    private void ModificarDialog() {

        switch (tipo) {

            case "Entrada":

                txtTitulo.setText("AGREGAR ENTRADA DINERO");
                txtTitulo.setBackgroundColor(Color.parseColor("#04F300"));
                cvValorDisponible.setVisibility(View.GONE);
                movCaja.setTipoRegistro((byte) 1);
                break;

            case "Retiro":

                movCaja.setTipoRegistro((byte) 2);
                txtTitulo.setText("RETIRO DE DINERO");
                txtTitulo.setBackgroundColor(Color.parseColor("#DC0207"));
                cvValorDisponible.setVisibility(View.VISIBLE);
                break;

        }

    }

    public interface EntradaRetiro {


        public void InformacionRetiroEntrada();

    }

    private class CargarDatos extends AsyncTask<Void, Void, Byte> {

        int count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            relativeLayout.setVisibility(View.GONE);
            pbDialog.setVisibility(View.VISIBLE);

        }

        @Override
        protected Byte doInBackground(Void... voids) {

            if (tipo.equals("Entrada")) {
                listMedioPago = bdConnectionSql.getMedioPagoEfectivo();
            } else if (tipo.equals("Retiro")) {
                mResumenMedioPagoList = bdConnectionSql.getResumenMP(cierre.getIdCierre());
                if (mResumenMedioPagoList != null) {
                    count = mResumenMedioPagoList.size();

                    for (int i = 0; i < count; i++) {
                        listMedioPago.add(new mMedioPago());
                        listMedioPago.get(i).setiIdMedioPago(mResumenMedioPagoList.get(i).getIdMedioPago());
                        listMedioPago.get(i).setcDescripcionMedioPago(mResumenMedioPagoList.get(i).getDescripcionMedioPago());

                    }

                }

            }
            if (listMedioPago != null) {
                if (mResumenMedioPagoList != null) {
                    listMotivo = bdConnectionSql.getMotivoIngresoRetiro(tipo);
                }
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            pbDialog.setVisibility(View.GONE);
            if (mResumenMedioPagoList.size() == 0 && tipo.equals("Retiro")) {
                Toast.makeText(context, "No tiene saldo para hacer un retiro", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            if (listMedioPago != null) {
                if (mResumenMedioPagoList != null) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    adapterMPagoSpinner.AddElement(listMedioPago);
                    adapterMotivoRetiro.AddElement(listMotivo);
                } else {

                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Error al conseguir la informacion ", Toast.LENGTH_SHORT).show();

                    Toast.makeText(getActivity(), "Verifique su conexion", Toast.LENGTH_SHORT).show();
                }
            } else {

                dialog.dismiss();
                Toast.makeText(getActivity(), "Error al conseguir la informacion ", Toast.LENGTH_SHORT).show();

                Toast.makeText(getActivity(), "Verifique su conexion", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private class ProcesarMovimiento extends AsyncTask<Void, Void, Byte> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            relativeLayout.setVisibility(View.GONE);
            pbDialog.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            return ConfirmarProceso();
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            pbDialog.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            if (aByte == 0) {

                Toast.makeText(getActivity(), "Verifique su conexion", Toast.LENGTH_SHORT).show();

            } else if (aByte == 2) {

                Toast.makeText(getActivity(), "El monto ingresado es mayor al disponible", Toast.LENGTH_SHORT).show();

            } else if (aByte == 1) {

                movCaja.setMonto(bMonto);
                movCaja.setFechaTransaccion(new Timestamp(System.currentTimeMillis()));
                movCaja.setDescripcion(edtObservacion.getText().toString());
                entradaRetiro.InformacionRetiroEntrada();
                Toast.makeText(getActivity(), "Se Realizo con exito", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        }
    }

}
