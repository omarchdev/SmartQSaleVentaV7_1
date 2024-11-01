package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogSelectCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVentaRepository;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterListVentas;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

public class HistorialVentas extends ActivityParent implements DialogDatePickerSelect.interfaceFecha, View.OnClickListener, dialogSelectCustomer.DatosCliente, RvAdapterListVentas.ListenerCabeceraVenta {

    byte origen;
    RvAdapterListVentas rvAdapter;
    RecyclerView rv;
    List<mVenta> list;
    int fechaInicio, fechaFinal;
    int year, month, day;
    int idCliente;
    TextView txtTotalVentas,txtObservacion;

 //   Dialog dialog;
    int lenResult = 0;
    BigDecimal valorTotalVenta;
    int position = 0;
    Context context;
    dialogSelectCustomer selectCustomer;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    ImageButton btnDelete;
    Button btnSelectDate1, btnSelectDate2, btnSelectCliente;


    final String codeCia=GetJsonCiaTiendaBase64x3();

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create(gson)).build();
    IVentaRepository iVentaRepository= retro.create(IVentaRepository.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_ventas);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rvAdapter = new RvAdapterListVentas();
        rvAdapter.setListenerCabeceraVenta(this);
        rv = (RecyclerView) findViewById(R.id.rvHistorialVentas);
        if(getOrientationScreen()==0){
            rv.setLayoutManager(new LinearLayoutManager(this));
        }else{
            rv.setLayoutManager(new GridLayoutManager(this,2));

        }
        rv.setAdapter(rvAdapter);
        list = new ArrayList<>();

        valorTotalVenta = new BigDecimal(0);
        idCliente = 0;

        btnSelectDate1 = (Button) findViewById(R.id.btnDateVentas1);
        btnSelectDate2 = (Button) findViewById(R.id.btnDateVentas2);
        btnSelectCliente = (Button) findViewById(R.id.btnSelectCliente);
        btnDelete = (ImageButton) findViewById(R.id.btnDeleteCliente);
        txtTotalVentas = (TextView) findViewById(R.id.txtTotalDatoVentas);
        txtObservacion=findViewById(R.id.txtObservacion);
        btnSelectDate1.setOnClickListener(this);
        btnSelectDate2.setOnClickListener(this);
        btnSelectCliente.setOnClickListener(this);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        fechaFinal = (year * 10000) + (month * 100) + day;
        c.add(Calendar.DAY_OF_MONTH, -2);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        fechaInicio = (year * 10000) + (month * 100) + day;


        btnSelectDate1.setText("Desde \n"+convertirFormatoFecha(fechaInicio));
        btnSelectDate2.setText("Hasta \n"+convertirFormatoFecha(fechaFinal));
        btnDelete.setOnClickListener(this);
        txtTotalVentas.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", valorTotalVenta));
    //    ActualizarListaVentas();
        context=this;
        getSupportActionBar().setTitle("Listado artículos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( "Historial de Ventas ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);

    }

    @Override
    public void getFechaSelecionada(int day, int month, int year, byte origen) {
        if (origen == 1) {
            this.year = year;
            this.month = month;
            this.day = day;
            fechaInicio = (year * 10000) + (month * 100) + day;
            btnSelectDate1.setText("Desde \n"+convertirFormatoFecha(fechaInicio));
            ActualizarListaVentas();

        } else if (origen == 2) {

            this.year = year;
            this.month = month;
            this.day = day;
            fechaFinal = (year * 10000) + (month * 100) + day;
            btnSelectDate2.setText("Hasta \n"+convertirFormatoFecha(fechaFinal));
            ActualizarListaVentas();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

        int a, m, d;
        switch (v.getId()) {

            case R.id.btnDateVentas1:
                origen = 1;
                a = fechaInicio / 10000;
                m = (fechaInicio % 10000) / 100;
                d = fechaInicio % 100;
                MostrarDialogDatePicker(origen, a, m, d);
                break;

            case R.id.btnDateVentas2:
                origen = 2;
                a = fechaFinal / 10000;
                m = (fechaFinal % 10000) / 100;
                d = fechaFinal % 100;
                MostrarDialogDatePicker(origen, a, m, d);
                break;
            case R.id.btnDeleteCliente:
                EliminarDatosCliente();

                break;
            case R.id.btnSelectCliente:
                MostrarDialogSeleccionCliente();
                break;
        }
    }

    private void EliminarDatosCliente() {
        idCliente = 0;
        btnSelectCliente.setText("Seleccione cliente para la busqueda");
        ActualizarListaVentas();
    }

    private void MostrarDialogSeleccionCliente() {

        selectCustomer = new dialogSelectCustomer();
       // DialogFragment dialogFragment = selectCustomer;
        selectCustomer.setContext(this);
        selectCustomer.setListenerCliente(this);
        selectCustomer.show(getSupportFragmentManager(), "Selecciona Cliente");
    }

    @Override
    public void obtenerDato(mCustomer customer) {
        idCliente = customer.getiId();
        btnSelectCliente.setText(customer.getcName() + " " + customer.getcApellidoMaterno());
        ActualizarListaVentas();
    }

    private String convertirFormatoFecha(int fecha) {
        int anio = fecha / 10000;
        int mes = (fecha % 10000) / 100;
        int dia = fecha % 100;
        if (dia < 10) {
            return "0" + String.valueOf(dia) + "/" + String.valueOf(mes) + "/" + String.valueOf(anio);
        }
        return String.valueOf(dia) + "/" + String.valueOf(mes) + "/" + String.valueOf(anio);
    }

    private void MostrarDialogDatePicker(byte origen, int year, int month, int day) {
        DialogDatePickerSelect dialogDatePickerSelect = new DialogDatePickerSelect().newInstance(origen, year, month, day);
        dialogDatePickerSelect.setFechaListener(this);
        DialogFragment dialogFragment = dialogDatePickerSelect;
        dialogFragment.show(this.getFragmentManager(), "Dialog Fecha");
    }

    @Override
    public void EstadoVentaCancelada() {
        ActualizarListaVentas();
    }

    @Override
    public void CancelarVenta(int idCabeceraVenta, int position) {

        this.position = position;
   //     new CancelarVenta().execute(idCabeceraVenta);

    }

  /*  private void MostrarProgressDialog(String mensaje) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mensaje);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        dialog = progressDialog;
        dialog.show();

    }*/

    @Override
    protected void onResume() {
        super.onResume();
        ActualizarListaVentas();

    }

    private void ActualizarListaVentas() {
        new DownloadListVentas().execute(fechaInicio, fechaFinal, idCliente);
     }

    private void SumarVentas(int lenResult, List<mVenta> mVentas) {

        for (int i = 0; i < lenResult; i++) {
            if (mVentas.get(i).getcEstadoVenta().equals("N")) {
                valorTotalVenta = valorTotalVenta.add(mVentas.get(i).getTotalNeto());
            }
        }

    }
/*
    private class CancelarVenta extends AsyncTask<Integer, Void, BdConnectionSql.RetornoCancelar> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
     //       MostrarProgressDialog("Cancelando Venta");
        }

        @Override
        protected BdConnectionSql.RetornoCancelar doInBackground(Integer... integers) {
            return bdConnectionSql.cancelarVenta(integers[0]);
        }

        @Override
        protected void onPostExecute(BdConnectionSql.RetornoCancelar aByte) {
            super.onPostExecute(aByte);

            if (aByte.getRespuesta() == 101) {
                rvAdapter.ChangeData("C", position);
                SumarVentas(list.size(), list);
            }
            else if(aByte.getRespuesta()==100){
                new AlertDialog.Builder(context).setTitle("Error al cancelar venta")
                        .setPositiveButton("Salir",null).setMessage(
                                "El documento de venta "
                        +aByte.getIdCabeceraVenta()+" no se cancelará . La venta pertenece a"+
                                dateFormat.format(aByte.getFechaApertura()).toString()+" "+
                                        dateFormat.format(aByte.getFechaCierre()).toString()

                ).create().show();
            }
            else if (aByte.getRespuesta() == 1) {
                Toast.makeText(getBaseContext(), "Error al cancelar la venta", Toast.LENGTH_SHORT).show();
            } else if (aByte.getRespuesta() == 0) {
                Toast.makeText(getBaseContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
     //       dialog.dismiss();
        }
    }
*/
    private class DownloadListVentas extends AsyncTask<Integer, Void, List<mVenta>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
      //      MostrarProgressDialog("Cargando datos");
            rvAdapter.RemoveList();

        }


        @Override
        protected List<mVenta> doInBackground(Integer... integers) {
            try {
                List<mVenta> list= iVentaRepository.GetCabeceraVenta(codeCia,"2",integers[0], integers[1], integers[2]).execute().body();
                return  list;
            } catch (IOException e) {
                return new ArrayList<>();
            }catch(Exception ex){
                ex.toString();
                return new ArrayList<>();
            }
         //    return bdConnectionSql.getCabeceraVentaList(integers[0], integers[1], integers[2]);
        }

        @Override
        protected void onPostExecute(List<mVenta> mVentas) {
            super.onPostExecute(mVentas);
             //   dialog.dismiss();
                try{
                    valorTotalVenta = new BigDecimal(0);
                    if (mVentas != null) {
                        lenResult = mVentas.size();
                        list = mVentas;
                        if (lenResult > 0) {
                            rvAdapter.AddElement(mVentas);
                            SumarVentas(lenResult, mVentas);
                        } else {
                            rvAdapter.RemoveList();
                            valorTotalVenta = new BigDecimal(0);
                            Toast.makeText(getBaseContext(), "No existen resultados", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        rvAdapter.RemoveList();
                        valorTotalVenta = new BigDecimal(0);
                        Toast.makeText(getBaseContext(), "No existen resultados", Toast.LENGTH_LONG).show();
                    }
                    txtTotalVentas.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", valorTotalVenta));
                }catch (Exception e){
                    e.toString();
                }

            }

    }
}

