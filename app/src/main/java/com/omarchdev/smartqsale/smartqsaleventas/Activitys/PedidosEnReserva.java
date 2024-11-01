package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncPedido;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorVentas;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDatePickerSelect;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogSelectCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListTipoPedidos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.TipoPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPedidos;

import java.util.Calendar;
import java.util.List;

public class PedidosEnReserva extends ActivityParent implements View.OnClickListener,
        DialogDatePickerSelect.interfaceFecha, dialogSelectCustomer.DatosCliente,
        RvAdapterPedidos.interfaceListaPedidos, AdapterView.OnItemSelectedListener, TextWatcher, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView rv;
    RvAdapterPedidos rvAdapterPedidos;
    ControladorVentas controladorVentas;
    Button btnSelectDate1, btnSelectDate2;
    EditText edtNumPedido;
    int fechaInicio, fechaFinal;
    int year, month, day;
    int idCliente;
    byte origen;
    String nroPedido;
    ImageButton btnDelete;
    TextView txtSelectCliente;
    dialogSelectCustomer selectCustomer;
    Intent i;
    AsyncPedido asyncPedido;
    Context context;
    DbHelper dbHelper;
    boolean recuperando;
    String tiposVisible;
    Spinner spnTipos;
    ListTipoPedidos tiposPedido;
    ProgressBar pb1;
    SwipeRefreshLayout swipePedidos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nroPedido="";
        recuperando=false;
        setContentView(R.layout.activity_pedidos_en_reserva);
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        controladorVentas = new ControladorVentas();
        tiposPedido=new ListTipoPedidos();
        swipePedidos=findViewById(R.id.swipePedidos);
        rv =  findViewById(R.id.rvPedidosGuardados);
        btnSelectDate1 =  findViewById(R.id.btnSelectDate1);
        edtNumPedido=findViewById(R.id.edtNumPedido);
        btnSelectDate2 =  findViewById(R.id.btnSelectDate2);
        btnDelete =  findViewById(R.id.btnDeleteCliente);
        txtSelectCliente =  findViewById(R.id.txtSeleccionarCliente);
        spnTipos=findViewById(R.id.spnTipos);
        btnSelectDate1.setOnClickListener(this);
        btnSelectDate2.setOnClickListener(this);
        txtSelectCliente.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        i = getIntent();
        dbHelper=new DbHelper(this);
        rvAdapterPedidos = new RvAdapterPedidos();
        rvAdapterPedidos.setListenerPedidos(this);
        pb1=findViewById(R.id.pb1);
        pb1.setIndeterminate(true);
        rv.setAdapter(rvAdapterPedidos);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pedidos ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);

        asyncPedido=new AsyncPedido(this);
        idCliente = 0;
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        fechaFinal = (year * 10000) + (month * 100) + day;
        c.add(Calendar.DAY_OF_MONTH, -5);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        fechaInicio = (year * 10000) + (month * 100) + day;

        edtNumPedido.addTextChangedListener(this);


        btnSelectDate1.setText(convertirFormatoFecha(fechaInicio));
        btnSelectDate2.setText(convertirFormatoFecha(fechaFinal));
        ArrayAdapter<TipoPedido> adapterTipo=new ArrayAdapter<TipoPedido>(this,android.R.layout.simple_dropdown_item_1line,tiposPedido.getList());
        spnTipos.setAdapter(adapterTipo);

     //   new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);

        spnTipos.setOnItemSelectedListener(this);
        swipePedidos.setOnRefreshListener(this);
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
    public void onClick(View v) {
        int a, m, d;
        switch (v.getId()) {
            case R.id.btnSelectDate1:
                origen = 1;
                a = fechaInicio / 10000;
                m = (fechaInicio % 10000) / 100;
                d = fechaInicio % 100;
                MostrarDialogDatePicker(origen, a, m, d);
                break;
            case R.id.btnSelectDate2:
                origen = 2;
                a = fechaFinal / 10000;
                m = (fechaFinal % 10000) / 100;
                d = fechaFinal % 100;
                MostrarDialogDatePicker(origen, a, m, d);
                break;
            case R.id.btnDeleteCliente:
                EliminarDatosCliente();
                new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
                break;
            case R.id.txtSeleccionarCliente:
                MostrarDialogSeleccionCliente();

                break;

        }
    }

    private void EliminarDatosCliente() {
        idCliente = 0;
        txtSelectCliente.setText("Seleccione cliente para la busqueda");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void getFechaSelecionada(int day, int month, int year, byte origen) {

        if (origen == 1) {

            this.year = year;
            this.month = month;
            this.day = day;
            fechaInicio = (year * 10000) + (month * 100) + day;
            new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
            btnSelectDate1.setText(convertirFormatoFecha(fechaInicio));
        } else if (origen == 2) {
            this.year = year;
            this.month = month;
            this.day = day;
            fechaFinal = (year * 10000) + (month * 100) + day;

            new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
            btnSelectDate2.setText(convertirFormatoFecha(fechaFinal));
        }
    }

    private void MostrarDialogSeleccionCliente() {


        selectCustomer = new dialogSelectCustomer();

        selectCustomer.setContext(this);
        selectCustomer.setListenerCliente(this);
        selectCustomer.show(getSupportFragmentManager(), "Selecciona Cliente");


    }

    @Override
    public void obtenerDato(mCustomer customer) {
        idCliente = customer.getiId();
        txtSelectCliente.setText(customer.getcName() + " " + customer.getcApellidoMaterno());

        new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position>=0)
        new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        nroPedido=s.toString();
        new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onRefresh() {

        ActualizarListaPedidos();

    }


    private class SuspenderPedido extends AsyncTask<mCabeceraPedido,Void,Void>
    {
        boolean a,b;

        @Override
        protected Void doInBackground(mCabeceraPedido... mCabeceraPedidos) {
            a=dbHelper.ObtenerPermiso(Constantes.ProcesosPantalla.AnularpedidoReserva);
            if(a){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    @Override
    public void ObtenerDatoPedidoSupender(mCabeceraPedido cabeceraPedido) {

        if(dbHelper.ObtenerPermiso(Constantes.ProcesosPantalla.AnularpedidoReserva)) {
            asyncPedido.EliminarPedido(cabeceraPedido.getIdCabecera());
            asyncPedido.setListenerEliminarPedido(new AsyncPedido.ListenerEliminarPedido() {
                @Override
                public void EliminarPedidoRespuesta() {
                    new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
                    new AlertDialog.Builder(context).
                            setTitle("Confirmacion").setPositiveButton("Salir", null)
                            .setMessage("El pedido se eliminó con éxito").create().show();
                }

                @Override
                public void ErrorEliminar() {
                    new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
                }

                @Override
                public void PedidoNoDisponible() {
                    new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);
                    DialogPedidoNoDisponible();
                }
            });
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Advertencia").setMessage("No tiene permiso para realizar está opción")
                    .setPositiveButton("Salir",null).create().show();
        }
    }

    @Override
    public void ObtenerDatoPedidoRegresarVenta(mCabeceraPedido cabeceraPedido) {
        if(!recuperando){


            if(dbHelper.ObtenerPermiso(Constantes.ProcesosPantalla.RecuperarPedidoReserva)){
                new ObtenerPedido().execute(cabeceraPedido);

            }else{

                Toast.makeText(this,"No tiene permiso para recuperar el pedido",Toast.LENGTH_LONG).show();
            }
        }

    }

    private class ObtenerPedido extends AsyncTask<mCabeceraPedido,Void,Boolean>{

        Dialog dialog;
        DialogCargaAsync dialogCargaAsync;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recuperando=true;
            dialogCargaAsync=new DialogCargaAsync(context);
            dialog= dialogCargaAsync.getDialogCarga("Obteniendo pedido...");
            dialog.show();
        }

        boolean a,b;
        mCabeceraPedido p;
        @Override
        protected Boolean doInBackground(mCabeceraPedido... mCabeceraPedidos) {

         //   a=dbHelper.ObtenerPermiso(Constantes.ProcesosPantalla.RecuperarPedidoReserva);

            b= controladorVentas.GetEstadoPedido(mCabeceraPedidos[0].getIdCabecera());
            p=mCabeceraPedidos[0];
            return b;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            recuperando=false;
                dialog.dismiss();
                if (aBoolean) {
                    i.putExtra("RESULTADOID", p.getIdCabecera());

                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    DialogPedidoNoDisponible();
                    new DownloadListPedidos().execute(idCliente, fechaInicio, fechaFinal);

                }

        }
    }
    @Override
    public void ActualizarListaPedidos() {

        DownloadListPedidos d=new DownloadListPedidos();
       d.execute(idCliente, fechaInicio, fechaFinal);
    }

    public void DialogPedidoNoDisponible() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta").setIcon(R.drawable.alert).setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setMessage("El pedido no se encuentra disponible");

        dialog = builder.create();
        dialog.show();
    }

    private class DownloadListPedidos extends AsyncTask<Integer, Void, List<mCabeceraPedido>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb1.setVisibility(View.GONE);
            swipePedidos.setRefreshing(true);
            swipePedidos.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        }

        @Override
        protected List<mCabeceraPedido> doInBackground(Integer... integers) {

            return controladorVentas.getListaPedidosReserva(integers[0], integers[1],
                    integers[2],tiposPedido.getList().get(spnTipos.getSelectedItemPosition()).getCod(),nroPedido);
        }


        @Override
        protected void onPostExecute(List<mCabeceraPedido> mCabeceraPedidos) {

            super.onPostExecute(mCabeceraPedidos);

            rvAdapterPedidos.AddElement(mCabeceraPedidos);
            pb1.setVisibility(View.GONE);
            swipePedidos.setRefreshing(false);

        }
    }
}
