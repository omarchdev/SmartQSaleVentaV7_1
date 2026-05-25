package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncClientes;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorCliente;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterClientes;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ListadoClientes extends ActivityParent implements RvAdapterClientes.ListenerPosition, AsyncClientes.ListenerEliminarCliente, SearchView.OnQueryTextListener {

    RecyclerView rvListadoClientes;
    AsyncClientes asyncClientes;
    RvAdapterClientes rvAdapterClientes;
    TextView txtCargando;
    AVLoadingIndicatorView avi;
    List<mCustomer> customersList;
    Context context;
    int pos;
    SearchView searchView;
    EditText searchBox;
    ControladorCliente controladorCliente;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try{
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_clientes, menu);

            // Ocultar el item de búsqueda del menú ya que usamos el inline
            MenuItem searchItem = menu.findItem(R.id.searchToolbar1);
            if (searchItem != null) {
                searchItem.setVisible(false);
            }

        }catch (Exception ex){
            Toast.makeText(this,ex.toString(),Toast.LENGTH_SHORT).show();
        }

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_listado_clientes);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            controladorCliente = new ControladorCliente();

            context = this;
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
                getSupportActionBar().setTitle("Clientes");
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            // Inicializar SearchView Inline
            searchView = findViewById(R.id.searchViewInline);
            if (searchView != null) {
                searchView.setOnQueryTextListener(this);
            }

            asyncClientes = new AsyncClientes();
            avi = findViewById(R.id.avi);
            txtCargando = findViewById(R.id.txtCargando);
            rvListadoClientes = findViewById(R.id.rvListadoClientes);
            rvAdapterClientes = new RvAdapterClientes();
            customersList = new ArrayList<>();
            rvListadoClientes.setAdapter(rvAdapterClientes);
            rvListadoClientes.setLayoutManager(new LinearLayoutManager(this));
            rvAdapterClientes.setVisibleSettings(true);
            rvAdapterClientes.setContext(this);
            asyncClientes.setListenerEliminarCliente(this);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AgregarCliente();
                }
            });

            rvAdapterClientes.setListenerPosition(this);
            asyncClientes.setContext(this);

        } catch (Exception ex) {
            Toast.makeText(this,ex.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Actualizando();
        asyncClientes.ObtenerTodosClientes();
        asyncClientes.setListenerClientes(customerList -> RecibirClientes(customerList));
    }

    public void RecibirClientes(List<mCustomer> customerList) {
        if (customerList != null) {
            rvAdapterClientes.AgregarListado(customerList);
            ActualizandoTerminar();
            customersList = customerList;
        } else {
            Toast.makeText(context, "", Toast.LENGTH_LONG).show();
        }
    }

    public void Actualizando() {

        try{
            avi.show();
            txtCargando.setVisibility(View.VISIBLE);
            rvListadoClientes.setVisibility(View.INVISIBLE);
        }catch (Exception ex){
         Toast.makeText(this,ex.toString(),Toast.LENGTH_SHORT).show();
        }


    }

    public void ActualizandoTerminar() {

        avi.hide();
        txtCargando.setVisibility(View.INVISIBLE);
        rvListadoClientes.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        asyncClientes.CancelarObtenerClientes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void AgregarCliente() {
        Intent intent = new Intent(this, ConfigClientes.class);
        intent.putExtra("IdCliente", 0);
        intent.putExtra("accionConfig", Constantes.EstadoConfiguracion.Nuevo);
        startActivity(intent);
    }

    @Override
    public void ObtenerPosicion(int position) {

        Intent intent = new Intent(context, ConfigClientes.class);
        intent.putExtra("IdCliente", customersList.get(position).getiId());
        intent.putExtra("accionConfig", Constantes.EstadoConfiguracion.Editar);
        startActivity(intent);
    }

    @Override
    public void ObtenerPosVisualizar(int position) {


        Intent intent = new Intent(context, ConfigClientes.class);
        intent.putExtra("IdCliente", customersList.get(position).getiId());
        intent.putExtra("accionConfig", Constantes.EstadoConfiguracion.Visualizar);
        startActivity(intent);


    }

    @Override
    public void ObtenerPosAnular(int position) {
        MensajeEliminar(position);
    }


    public void MensajeEliminar(int position) {
        this.pos = position;
        new AlertDialog.Builder(this).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                asyncClientes.EliminarClienteId(customersList.get(pos).getiId());
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setMessage("¿Desea eliminar al cliente " + customersList.get(position).getRazonSocial() + "?").create().show();

    }

    @Override
    public void EliminarClienteExito() {
        new AlertDialog.Builder(this).setPositiveButton("Salir", null).setMessage("Se elimino al cliente con exito").setTitle("Confirmacion").create().show();
        rvAdapterClientes.notifyItemRemoved(pos);

        customersList.remove(pos);

    }

    @Override
    public void EliminarClienteError() {
        new AlertDialog.Builder(this).setPositiveButton("Salir", null).setMessage("Error al eliminar el cliente. Verifique su conexión a internet").setTitle("Advertencia").create().show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        new DownloadListClientes().execute(newText);
        return false;
    }

    private class DownloadListClientes extends AsyncTask<String, Void, List<mCustomer>> {

        @Override
        protected List<mCustomer> doInBackground(String... strings) {

            return controladorCliente.getClienteNombreApellido(strings[0], "", "");
        }

        @Override
        protected void onPostExecute(List<mCustomer> mCustomers) {
            super.onPostExecute(mCustomers);
            customersList.clear();
            customersList.addAll(mCustomers);
            rvAdapterClientes.notifyDataSetChanged();
        }

    }
}
