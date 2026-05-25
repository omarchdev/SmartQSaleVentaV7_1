package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVendedores;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterClientes;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class ListadoVendedores extends ActivityParent implements RvAdapterClientes.ListenerPosition, View.OnClickListener {

    List<mVendedor> vendedores;
    RecyclerView rvVendedores;
    RvAdapterVendedores adapterVendedores;
    AsyncVendedores asyncVendedores;
    AVLoadingIndicatorView avi;
    TextView txt;
    int idSelected;
    FloatingActionButton fabAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_vendedores);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
                    getSupportActionBar().setTitle("Vendedores");
                }
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }

            rvVendedores = findViewById(R.id.rvVendedores);
            avi = findViewById(R.id.avi);
            txt = findViewById(R.id.txtCargando);
            fabAgregar = findViewById(R.id.fab);

            adapterVendedores = new RvAdapterVendedores();
            rvVendedores.setAdapter(adapterVendedores);
            rvVendedores.setLayoutManager(new LinearLayoutManager(this));
         asyncVendedores = new AsyncVendedores();
            asyncVendedores.setContextLoading(this, avi, txt, null, rvVendedores);

            asyncVendedores.setListenerVendedores(listenerVendedores);

            adapterVendedores.setListenerPosition(this);
            fabAgregar.setOnClickListener(this);
            adapterVendedores.setContext(this);
        }catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
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

        asyncVendedores.ObtenerVendedores();
        idSelected=0;
    }

    AsyncVendedores.ListenerVendedores listenerVendedores=new AsyncVendedores.ListenerVendedores() {
        @Override
        public void ObtenerVendedores(List<mVendedor> vendedors) {

            if(vendedors!=null) {
                vendedores=vendedors;
                adapterVendedores.setVendedorList(vendedors);
            }
        }

        @Override
        public void ErrorObtener() {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        asyncVendedores.CancelarObtenerVendedores();
    }

    @Override
    public void ObtenerPosicion(int position) {

        Intent intent=new Intent(this,RegistroVendedor.class);
        intent.putExtra("IdVendedor",vendedores.get(position).getIdVendedor());
        intent.putExtra("accionConfig", Constantes.EstadoConfiguracion.Editar);
        startActivity(intent);

    }

    @Override
    public void ObtenerPosVisualizar(int position) {
        Intent intent=new Intent(this,RegistroVendedor.class);
        intent.putExtra("IdVendedor",vendedores.get(position).getIdVendedor());
        intent.putExtra("accionConfig", Constantes.EstadoConfiguracion.Visualizar);
        startActivity(intent);
    }

    @Override
    public void ObtenerPosAnular(int position) {

    }



    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.fab){

            Intent intent=new Intent(this,RegistroVendedor.class);
            intent.putExtra("IdVendedor",0);
            intent.putExtra("accionConfig", Constantes.EstadoConfiguracion.Nuevo);
            startActivity(intent);
        }

    }
}

