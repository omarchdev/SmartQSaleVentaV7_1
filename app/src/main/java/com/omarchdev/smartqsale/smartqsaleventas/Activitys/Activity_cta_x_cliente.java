package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.CtaCteCliente;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleCuentaCorriente;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterDetalleCtaCte;

import java.util.List;

public class Activity_cta_x_cliente extends ActivityParent implements RvAdapterDetalleCtaCte.GetDataCtaCte, View.OnClickListener {

    Toolbar toolbar;
    RecyclerView rv;
    RvAdapterDetalleCtaCte adapterDetalleCtaCte;
    BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    int idCliente;
    int idCtaCte;
    Dialog dialog;
    int idCabeceraVenta;
    mCustomer customer;
    String nombreCliente = "", email = "", saldo = "",estadoEliminado="";
    TextView txtEmail, txtSaldo;
    Context context;

    FloatingActionButton floatingActionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cta_x_cliente);
        toolbar = (Toolbar) findViewById(R.id.toolbarExpandable);
        txtEmail = (TextView) findViewById(R.id.txtEmailDato);
        txtSaldo = (TextView) findViewById(R.id.txtSaldoDatos);
        customer = new mCustomer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        idCliente = getIntent().getIntExtra("idcliente", 0);
        nombreCliente = getIntent().getStringExtra("Nombre");
        email = getIntent().getStringExtra("Email");
        estadoEliminado=getIntent().getStringExtra("Estado");

        txtEmail.setText(email);
        getSupportActionBar().setTitle(nombreCliente);
        rv = (RecyclerView) findViewById(R.id.rvDetalleCtaCte);
        rv.setLayoutManager(new LinearLayoutManager(this));
        floatingActionsMenu = (FloatingActionButton) findViewById(R.id.fb_AgregarPago);
        floatingActionsMenu.setOnClickListener(this);
        adapterDetalleCtaCte = new RvAdapterDetalleCtaCte();
        adapterDetalleCtaCte.setListenerCtaCte(this);
        rv.setAdapter(adapterDetalleCtaCte);
        context=this;
    }

    @Override
    public void getIdCabeceraVenta(int idCabeceraVenta) {
        this.idCabeceraVenta = idCabeceraVenta;
        new VerificarConexion().execute();

    }

    @Override
    public void getIdCtaCte(int CtaCte) {
        this.idCtaCte = CtaCte;
        ADialogCancelarVenta();
        Toast.makeText(this, String.valueOf(CtaCte), Toast.LENGTH_SHORT).show();
    }

    private void ADialogCancelarVenta() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea cancelar el pago realizado?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new CancelarPagoAsync().execute(idCtaCte, idCliente);

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setTitle("Alerta").show();


    }

    private void MostrarDetalleVenta(int idCabeceraVenta) {
        Intent intent = new Intent(this, DetalleVenta.class);
        intent.putExtra("idCabeceraVenta", idCabeceraVenta);
        startActivity(intent);

    }

    private ProgressDialog getProgressDialog(String Mensaje) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(Mensaje);

        return progressDialog;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fb_AgregarPago) {
            if(estadoEliminado.equals("E")) {
                new AlertDialog.Builder(context).
                        setTitle("Advertencia").
                        setMessage("No puede realizar pagos.El cliente se encuentra eliminado")
                        .setPositiveButton("Salir",null)
                        .create().show();
            }
            else {
                AgregarPago();
            }
        }
    }

    private void AgregarPago() {
        Intent intent = new Intent(getBaseContext(), ActivityPagoCtaCte.class);
        intent.putExtra("idcliente", idCliente);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ObtenerDetalleCtaCte().execute(idCliente);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class VerificarConexion extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = getProgressDialog("Verificando Conexion");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            if (aBoolean == true) {
                MostrarDetalleVenta(idCabeceraVenta);
            } else {

                Toast.makeText(getBaseContext(), "Error al obtener la informacion", Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "Verifique su conexión", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private class ObtenerDetalleCtaCte extends AsyncTask<Integer, Void, List<DetalleCuentaCorriente>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = getProgressDialog("Obteniendo movimientos del cliente");
            dialog.show();
        }

        @Override
        protected List<DetalleCuentaCorriente> doInBackground(Integer... integers) {
       //     saldo = bdConnectionSql.getSaldoCliente(idCliente);
            CtaCteCliente ctaCteCliente=bdConnectionSql.ObtenerCtaCteCorriente(idCliente);
            saldo= Constantes.DivisaPorDefecto.SimboloDivisa +String.format("%.2f",ctaCteCliente.getSaldoCliente());
            return ctaCteCliente.getListaCuentaCorrient();
        }


        @Override
        protected void onPostExecute(List<DetalleCuentaCorriente> detalleCuentaCorrientes) {
            super.onPostExecute(detalleCuentaCorrientes);
            if (detalleCuentaCorrientes != null) {
                adapterDetalleCtaCte.AddList(detalleCuentaCorrientes);

                txtSaldo.setText(saldo);


            } else {

                Toast.makeText(getBaseContext(), "No se obtuvieron resultados", Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "Verifique su conexión", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        }

    }

    private class CancelarPagoAsync extends AsyncTask<Integer, Void, Byte> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = getProgressDialog("Cancelando pago...");
            dialog.show();
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.CancelarPagoCtaCte(integers[0], integers[1]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            dialog.dismiss();
            if (aByte == 99) {
                Toast.makeText(getBaseContext(), "Verifique su conexión a internet", Toast.LENGTH_LONG).show();
            } else if (aByte == 100) {
                Toast.makeText(getBaseContext(), "No se realizo cancelo el pago con éxito", Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), "Verifique su conexión a internet", Toast.LENGTH_LONG).show();
            } else if (aByte == 101) {

                new ObtenerDetalleCtaCte().execute(idCliente);
  /*              adapterDetalleCtaCte.AddList(bdConnectionSql.getDetalleCuentaCorriente(idCliente));
                saldo = bdConnectionSql.getSaldoCliente(idCliente);
                txtSaldo.setText(saldo);

*/
            }


        }
    }
}
