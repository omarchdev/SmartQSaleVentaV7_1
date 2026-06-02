package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorMediosPago;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogPagoCtaCte;
import com.omarchdev.smartqsale.smartqsaleventas.MedioPagoSpinnerAdapter;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProcesarPagoCtaCteRequest;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IClienteRepository;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class ActivityPagoCtaCte extends ActivityParent implements DialogPagoCtaCte.ListenerCalculadoraPago, View.OnClickListener, DialogDatePickerSelect.interfaceFecha {

    int idCliente;
    int idMetodoPago;
    int dia, mes, anio, fecha;
    BdConnectionSql bdConnectionSql;
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API).client(Constantes.ConfiRetrofitTimeOut.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IClienteRepository iClienteRepository = retro.create(IClienteRepository.class);
    Button btnIngresarMonto, btnConfirmarPago, btnElegirFecha;
    Spinner spinnerMedioPago;
    TextInputLayout edtObservacion;
    TextView txtMonto;
    DialogPagoCtaCte pagoCtaCte;
    DialogFragment dialogFragment;
    BigDecimal monto;
    Dialog dialog;
    Context context;
    List<mMedioPago> list ;
    Toolbar toolbar;
    ControladorProcesoCargar controladorProcesoCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_cta_cte);
        context=this;

        bdConnectionSql = BdConnectionSql.getSinglentonInstance();
        btnIngresarMonto = (Button) findViewById(R.id.btnIngresarMonto);
        btnConfirmarPago = (Button) findViewById(R.id.btnConfirmarPago);
        btnElegirFecha = (Button) findViewById(R.id.btnSeleccionarFecha);
        spinnerMedioPago = (Spinner) findViewById(R.id.spn_Medio_Pago);
        edtObservacion = (TextInputLayout) findViewById(R.id.edtObservacion);

        txtMonto = (TextView) findViewById(R.id.txtMonto);
        pagoCtaCte = new DialogPagoCtaCte();
        pagoCtaCte.setListenerCalculadoraPago(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtObservacion.getEditText().setText("");
        btnIngresarMonto.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( "Agregar Pago");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
        getSupportActionBar().setElevation(4);
        btnConfirmarPago.setOnClickListener(this);
        btnElegirFecha.setOnClickListener(this);

        monto = new BigDecimal(0);
        idCliente = getIntent().getIntExtra("idcliente", 0);
        txtMonto.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", monto));

        controladorProcesoCargar=new ControladorProcesoCargar(context);
       // List<mMedioPago> list = bdConnectionSql.getMPagos();
  /*      Iterator<mMedioPago> i = list.iterator();

        while (i.hasNext()) {

            mMedioPago a = i.next();

            if (a.isPorCobrar() == true) {
                i.remove();
            }
        }


*/

        final Calendar c = Calendar.getInstance();
        anio = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH) + 1;
        dia = c.get(Calendar.DAY_OF_MONTH);
        fecha = (anio * 10000) + (mes * 100) + dia;
        btnElegirFecha.setText(convertirFormatoFecha(fecha));
        new ObtenerMediosPago().execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
    private class ObtenerMediosPago extends AsyncTask<Void,Void,List<mMedioPago>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            controladorProcesoCargar.IniciarDialogCarga("Obteniendo datos");
        }

        @Override
        protected List<mMedioPago> doInBackground(Void... voids) {
            return
                     new ControladorMediosPago().GetMediosPago();
        }

        @Override
        protected void onPostExecute(List<mMedioPago> mMedioPagos) {
            super.onPostExecute(mMedioPagos);
            list =mMedioPagos;
            Iterator<mMedioPago> i = list.iterator();
            controladorProcesoCargar.FinalizarDialogCarga();
            while (i.hasNext()) {

                mMedioPago a = i.next();

                if (a.isPorCobrar() == true) {
                    i.remove();
                }
            }
            MedioPagoSpinnerAdapter medioPagoSpinnerAdapter = new MedioPagoSpinnerAdapter(context, list);
            spinnerMedioPago.setAdapter(medioPagoSpinnerAdapter);
            dialogFragment = pagoCtaCte;
            dialogFragment.show(getSupportFragmentManager(), "Pago");

            spinnerMedioPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idMetodoPago = ((mMedioPago) parent.getItemAtPosition(position)).getiIdMedioPago();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void CapturarMontoPago(BigDecimal monto) {
        this.monto = monto;
        txtMonto.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", monto));
    }

    @Override
    public void onClick(View v) {
        int a, m, d;
        switch (v.getId()) {

            case R.id.btnIngresarMonto:
                dialogFragment.show(getSupportFragmentManager(), "Pago");

                break;

            case R.id.btnConfirmarPago:

                if (monto.compareTo(new BigDecimal(0)) == 0) {

                    Toast.makeText(this, "Ingresa un monto mayor a CERO", Toast.LENGTH_SHORT).show();
                } else {
                    new ProcesarPagoCtaCte().execute();
                }
                //bdConnectionSql.ProcesarPagoCtaCte()

                break;
            case R.id.btnSeleccionarFecha:

                a = fecha / 10000;
                m = (fecha % 10000) / 100;
                d = fecha % 100;
                MostrarDialogDatePicker((byte) 1, a, m, d);
                break;

        }
    }

    private ProgressDialog mostrarProgressDialog(String Mensaje) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(Mensaje);
        return progressDialog;

    }

    @Override
    public void getFechaSelecionada(int day, int month, int year, byte origen) {

        int fechaTemp = (year * 10000) + (month * 100) + day;
        if (fecha >= fechaTemp) {
            this.anio = year;
            this.mes = month;
            this.dia = day;
            fecha = (year * 10000) + (month * 100) + day;
            btnElegirFecha.setText(convertirFormatoFecha(fecha));
        } else {

            Toast.makeText(this, "Seleccione una fecha menor o igual a la actual", Toast.LENGTH_LONG).show();
        }

    }

    private String convertirFormatoFecha(int fecha) {
        int anio = fecha / 10000;
        int mes = (fecha % 10000) / 100;
        int dia = fecha % 100;
        if (dia < 10) {
            return " Seleccionar fecha de pago: \n 0" + String.valueOf(dia) + "/" + String.valueOf(mes) + "/" + String.valueOf(anio);
        }

        return "Seleccionar fecha de pago: \n" + String.valueOf(dia) + "/" + String.valueOf(mes) + "/" + String.valueOf(anio);
    }

    private void MostrarDialogDatePicker(byte origen, int year, int month, int day) {
        DialogDatePickerSelect dialogDatePickerSelect = new DialogDatePickerSelect().newInstance(origen, year, month, day);
        dialogDatePickerSelect.setFechaListener(this);
        DialogFragment dialogFragment = dialogDatePickerSelect;
        dialogFragment.show(this.getSupportFragmentManager(), "Dialog Fecha");
    }

    private class ProcesarPagoCtaCte extends AsyncTask<Void, Void, Byte> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = mostrarProgressDialog("Procesando pago...");
            dialog.show();
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            String codeCia = GetJsonCiaTiendaBase64x3();
            ProcesarPagoCtaCteRequest data = new ProcesarPagoCtaCteRequest(
                monto,
                idMetodoPago,
                edtObservacion.getEditText().getText().toString(),
                idCliente,
                Constantes.Tienda.idTienda
            );
            SolicitudEnvio<ProcesarPagoCtaCteRequest> solicitud = new SolicitudEnvio<>(
                codeCia,
                "2",
                data,
                Constantes.Terminal.idTerminal,
                Constantes.Usuario.idUsuario
            );
            try {
                retrofit2.Response<Byte> response = iClienteRepository.ProcesarPagoCtaCte(solicitud).execute();
                if (response.isSuccessful() && response.body() != null) {
                    return response.body();
                } else {
                    return (byte) 100;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return (byte) 0; // Return 0 if connection fails or error occurs
            }
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (aByte == 0) {
                Toast.makeText(getBaseContext(), "No se realizó el pago", Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "Verifique su conexión", Toast.LENGTH_SHORT).show();
            } else if (aByte == 99) {
                Toast.makeText(getBaseContext(),
                        "El metodo de pago no se encuentra disponible",
                        Toast.LENGTH_LONG).show();

            } else if (aByte == 100) {
                Toast.makeText(getBaseContext(), "Error al realizar el pago", Toast.LENGTH_LONG).show();

            } else if (aByte == 101) {
                Toast.makeText(getBaseContext(), "El pago se realizo con éxito", Toast.LENGTH_LONG).show();
                onBackPressed();
                finish();
            }else if(aByte==102){

                new AlertDialog.Builder(context).
                        setMessage("Debe tener una caja abierta para poder realizar el pago")
                        .setTitle("").setPositiveButton("Salir  ",null).show();
            }

            dialog.dismiss();
        }


    }

}

