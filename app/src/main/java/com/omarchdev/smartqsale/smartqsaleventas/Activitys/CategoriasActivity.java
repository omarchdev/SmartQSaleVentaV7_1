package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUnidadMedida;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterCategoriaP;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class CategoriasActivity extends ActivityParent implements AsyncCategoria.ListenerCategoria, View.OnClickListener, RvAdapterCategoriaP.ListenerSelectCategoria {

    RecyclerView rvCategorias;
    RvAdapterCategoriaP rvAdapterCategoriaP;
    AsyncCategoria asyncCategoria;
    FloatingActionButton floatingActionButton;
    List<mCategoriaProductos> categoriaProductosList;
    AVLoadingIndicatorView pbIndicator;
    TextView txtCargando1;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       MenuInflater inflater=getMenuInflater();
       inflater.inflate(R.menu.menu_config_pack,menu);
       return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_config){

            Intent intent=new Intent(this,ConfigCategorias.class);
            startActivity(intent);
        }

        return false;
    }
    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Categorías");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
        getSupportActionBar().setElevation(4);
        pbIndicator=findViewById(R.id.pbIndicator);
        txtCargando1=findViewById(R.id.txtCargando1);
        asyncCategoria=new AsyncCategoria();
        rvCategorias=findViewById(R.id.rvCategorias);
        rvAdapterCategoriaP= new RvAdapterCategoriaP();
        rvCategorias.setAdapter(rvAdapterCategoriaP);
        rvCategorias.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton=findViewById(R.id.floatingActionButton);
        categoriaProductosList=new ArrayList<>();
        rvAdapterCategoriaP.setContext(this);
        asyncCategoria.setListenerCategoria(this);
        floatingActionButton.setOnClickListener(this);
        rvAdapterCategoriaP.setListenerSelectCategoria(this);


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
    @Override
    protected void onResume() {
         super.onResume();
        txtCargando1.setVisibility(View.VISIBLE);
        pbIndicator.show();
        rvCategorias.setVisibility(View.INVISIBLE);
        asyncCategoria.getCategorias();
    }

    @Override
    public void CategoriasObtenidas(List<mCategoriaProductos> categoriaProductosList) {

        txtCargando1.setVisibility(View.INVISIBLE);
        pbIndicator.hide();
        rvCategorias.setVisibility(View.VISIBLE);
        this.categoriaProductosList.clear();
        this.categoriaProductosList.addAll(categoriaProductosList);
        rvAdapterCategoriaP.AgregarLista(categoriaProductosList);
    }

    @Override
    public void ObtenerUnidadesMedidad(List<mUnidadMedida> listaUnidades) {

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.floatingActionButton){

            Intent intent=new Intent(this,ActivityRegistroCategorias.class);
            intent.putExtra("Estado",0);
            startActivity(intent);

        }
    }


    @Override
    public void setPositionSelect(int positionSelect) {

        if( categoriaProductosList.get(positionSelect).isEstadoMod()==true) {

            Intent intent = new Intent(this, ActivityRegistroCategorias.class);
            intent.putExtra("Estado", 1);
            intent.putExtra("idCategoria", categoriaProductosList.get(positionSelect).getIdCategoria());
            intent.putExtra("nombre", categoriaProductosList.get(positionSelect).getDescripcionCategoria());
            intent.putExtra("estadoConfig", Constantes.EstadoConfiguracion.Editar);
            startActivity(intent);
        }
        else{

            MensajeAlerta("Advertencia","No se puede modificar la categoría "+ categoriaProductosList.get(positionSelect).getDescripcionCategoria());
        }
    }

    @Override
    public void setPositionDelete(int position) {

    }

    @Override
    public void setPositionVisualizar(int position) {
        Intent intent = new Intent(this, ActivityRegistroCategorias.class);
        intent.putExtra("Estado", 1);
        intent.putExtra("idCategoria", categoriaProductosList.get(position).getIdCategoria());
        intent.putExtra("nombre", categoriaProductosList.get(position).getDescripcionCategoria());
        intent.putExtra("estadoConfig", Constantes.EstadoConfiguracion.Visualizar);
        startActivity(intent);
    }

    @Override
    public void setPositionAgregarSubCategorias(int position) {
        Intent intent = new Intent(this, RegistroSubCategoria.class);
        intent.putExtra("idCategoria", categoriaProductosList.get(position).getIdCategoria());
        intent.putExtra("nombre", categoriaProductosList.get(position).getDescripcionCategoria());
        startActivity(intent);
    }

    public void MensajeAlerta(String titulo,String mensaje){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(titulo).setMessage(mensaje).setPositiveButton("Salir",null).create().show();

    }
}
