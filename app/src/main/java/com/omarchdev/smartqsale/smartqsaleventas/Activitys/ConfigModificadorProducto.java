package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncModificadores;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Modificador;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterModificares;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ConfigModificadorProducto extends ActivityParent implements AsyncModificadores.ListenerConfigProdMod, View.OnClickListener, CompoundButton.OnCheckedChangeListener, SearchView.OnQueryTextListener {

    SlidingUpPanelLayout sliding_layout;
    RecyclerView rvModificadores,rvModificadoresProducto;
    RvAdapterModificares adapterModificaresProduct,adapterModificares;
    TextView txtCargando;
    Button btnAgregarModificador,btnAgregar,btnEliminarModProd,btnCancelar,btnAddMod;
    int idProducto;
    String nombre;
    LinearLayout content_mod_add;
    AsyncModificadores asyncModificadores;
    List<Modificador> modificadores,modificadoresProducto;
    int positionMP;
    int positionM;
    boolean estadoActual,estadoTemp;
    Switch switch1;
    AVLoadingIndicatorView pbIndicator,pbIndicator2;
    TextView txtNumModProd,txtAdvModProd;
    SearchView searchView;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_modificador_producto);
        txtNumModProd=findViewById(R.id.txtNumModProd);
        modificadores=new ArrayList<>();
        modificadoresProducto=new ArrayList<>();
        pbIndicator=findViewById(R.id.pbIndicator);
        txtCargando=findViewById(R.id.txtCargando);
        positionM=-10;
        positionMP=-10;
        nombre=getIntent().getStringExtra("NombreProducto");
            idProducto=getIntent().getIntExtra("IdProducto",0);
        btnAgregarModificador=findViewById(R.id.btnAgregarModificador);
        switch1=findViewById(R.id.switch1);
        getSupportActionBar().setTitle(nombre);
        asyncModificadores=new AsyncModificadores();
        asyncModificadores.setContext(this);

        txtAdvModProd=findViewById(R.id.txtAdvModProd);
        btnAddMod=findViewById(R.id.btnAddMod);

        content_mod_add=findViewById(R.id.content_mod_add);
        sliding_layout=findViewById(R.id.sliding_layout);
        rvModificadoresProducto=findViewById(R.id.rvModificadoresProducto);
        rvModificadores=findViewById(R.id.rvModificares);
        btnCancelar=findViewById(R.id.btnCancelar);
        adapterModificares=new RvAdapterModificares();
        adapterModificaresProduct=new RvAdapterModificares();
        btnEliminarModProd=findViewById(R.id.btnEliminarModProd);
        rvModificadoresProducto.setLayoutManager(new LinearLayoutManager(this));
        rvModificadores.setLayoutManager(new LinearLayoutManager(this));
        rvModificadores.setAdapter(adapterModificares);
        rvModificadoresProducto.setAdapter(adapterModificaresProduct);
        pbIndicator2=findViewById(R.id.pbIndicator2);
        btnAgregar=findViewById(R.id.btnAgregar);
        asyncModificadores.setListenerConfigProdMod(this
        );

        btnAgregarModificador.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);

        adapterModificares.setClickModificador(position -> positionM=position);
        adapterModificaresProduct.setClickModificador(position -> positionMP=position);
        sliding_layout.setVisibility(View.INVISIBLE);
        pbIndicator.show();
        switch1.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnEliminarModProd.setOnClickListener(this);

        sliding_layout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState== SlidingUpPanelLayout.PanelState.COLLAPSED){

                    getSupportActionBar().setTitle(nombre);

                }
                else{
                    getSupportActionBar().setTitle("Modificadores disponibles");
                }
            }
        });
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);

        searchView=findViewById(R.id.searchView);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        int searchimgId=getResources().getIdentifier ("android:id/search_button", null, null);
        int imageId=getResources().getIdentifier ("android:id/search_close_btn", null, null);
        int searchTextId = getResources().getIdentifier ("android:id/search_src_text", null, null);
        searchBox=((EditText) searchView.findViewById (searchTextId));
        ImageView searchClose=((ImageView)searchView.findViewById(imageId));
        ImageView imgSearch=((ImageView)searchView.findViewById(searchimgId));


        searchView.setQueryHint("Busqueda de producto");
        searchView.setSearchableInfo(searchableInfo);
        searchView.setOnQueryTextListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
        getSupportActionBar().setElevation(10);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nombre);
        btnAddMod.setOnClickListener(v -> {
            Toast.makeText(this,"as",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,ActivityModificadorConfig.class);
            startActivity(intent);
        });
        pbIndicator2.hide();
        content_mod_add.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();

        asyncModificadores.ObtenerConfiguracionModificadoresProducto(idProducto);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
    @Override
    public void onBackPressed() {
        if(sliding_layout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED){

            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        }
        else {
            super.onBackPressed();
            finish();
        }

    }

    @Override
    public void getConfigProductMod(boolean estado, List<Modificador> modificadores, List<Modificador> modificadoresProducto) {
        estadoActual=estado;
        this.modificadoresProducto.clear();
        this.modificadores.clear();
        this.modificadores.addAll(modificadores);
        if(modificadores.size()==0){
            content_mod_add.setVisibility(View.VISIBLE);
            txtAdvModProd.setText("No existen modificadores para seleccionar.Presione el botón 'Crear modificadores' para iniciar.");
            btnAddMod.setText("Crear modificadores");
            rvModificadores.setVisibility(View.GONE);
        }else{
            content_mod_add.setVisibility(View.GONE);
            rvModificadores.setVisibility(View.VISIBLE);
        }
        this.modificadoresProducto.addAll(modificadoresProducto);
        switch1.setChecked(estadoActual);
            try {
                switch1.setOnCheckedChangeListener(this);
                if(estado){

                    switch1.setText("Forzar modificadores:Activado");
                }
                else{
                    switch1.setText("Forzar modificadores:Desactivado");
                }

                adapterModificaresProduct.AddModificadores(modificadoresProducto);
                adapterModificares.AddModificadores(modificadores);
            }catch (Exception e){
                e.toString();
            }

            pbIndicator.hide();
            sliding_layout.setVisibility(View.VISIBLE);
            txtCargando.setVisibility(View.INVISIBLE);
            txtNumModProd.setText("Numero de modificadores\ndel producto : "+Html.fromHtml("<font color=\"#000000\" >" + String.valueOf(modificadoresProducto.size()+ "</font>")));
    }

    @Override
    public void ResultadoIngresarModificadorProducto(Modificador modificador) {

        switch (modificador.getIdModificador()){

            case -10:
                new AlertDialog.Builder(this).setTitle("Advertencia")
                        .setMessage(modificador.getDescripcion())
                        .setPositiveButton("Salir",null)
                        .create().show();
                break;

            case -5:
                break;

            default:

                    modificadoresProducto.add(modificador);
                    adapterModificaresProduct.AgregarModificador(modificador);
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    txtNumModProd.setText("Numero de modificadores\ndel producto : "+String.valueOf(modificadoresProducto.size()));


                break;

        }

    }

    @Override
    public void ResultadoEliminarModificadorProducto(byte resultado) {

        switch(resultado){
            case 100:
                txtNumModProd.setText("Numero de modificadores\ndel producto : "+String.valueOf(modificadoresProducto.size()));
                modificadoresProducto.remove(positionMP);
                adapterModificaresProduct.EliminarModificador(positionMP);
                break;

            case 99:
                break;

            case 98:
                break;

        }

    }

    @Override
    public void ResultadoActEstadoModProd(byte resultado) {

        switch(resultado){

            case 100:
                switch1.setChecked(estadoTemp);
                estadoActual=estadoTemp;
                break;

            case 99:
                break;

            case 98:
                break;

        }
    }

    @Override
    public void ResultadoBusquedaModificador(List<Modificador> modificadorList) {
        pbIndicator2.hide();
        rvModificadores.setVisibility(View.VISIBLE);

        if(modificadorList!=null) {
            if(modificadorList.size()==0){
                rvModificadores.setVisibility(View.GONE);
                content_mod_add.setVisibility(View.VISIBLE);
                txtAdvModProd.setText("No existen modificadores para seleccionar.Presione el botón 'Crear modificadores' para iniciar.");
                btnAddMod.setText("Crear modificadores");
                rvModificadores.setVisibility(View.GONE);
            }else{
                rvModificadores.setVisibility(View.VISIBLE);
                content_mod_add.setVisibility(View.GONE);
            }
            adapterModificares.AddModificadores(modificadorList);
            this.modificadores.clear();
            this.modificadores.addAll(modificadorList);
        }
    }

    @Override
    public void onClick(View v) {
            switch(v.getId()){

                case R.id.btnAgregarModificador:
                    if(estadoActual) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                   }else{
                        MensajeAlerta("Advertencia","Debe activar los modificadores para agregar");
                    }
                    break;
                case R.id.btnAddMod:
                    Intent intent=new Intent(this,ActivityModificadorConfig.class);
                    startActivity(intent);
                    Toast.makeText(this,"as",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnAgregar:
                    if(estadoActual) {
                        if (positionM >= 0) {

                            asyncModificadores.AgregarModProducto(idProducto, modificadores.get(positionM).getIdModificador());
                        } else {
                            MensajeAlerta("Advertencia", "Debe seleccionar un modificador para poder agregarlo");
                        }
                    }else{
                        MensajeAlerta("Advertencia","Debe activar los modificadores para agregar");
                    }
                    break;
                case R.id.switch1:

                    if(estadoActual){
                        estadoTemp=false;
                        switch1.setChecked(estadoActual);
                        VerificarEstadoModificador("¿Está seguro de desactivar los modificadores para el producto?");
                    }
                    else{
                        estadoTemp=true;
                        switch1.setChecked(estadoActual);
                        VerificarEstadoModificador("¿Está seguro de activar los modificadores para el producto?");

                    }
                    break;
                case R.id.btnEliminarModProd:
                    if(estadoActual) {
                        if (positionMP >= 0) {
                            asyncModificadores.EliminarModProducto(modificadoresProducto.get(positionMP).getIdModificadorProducto(), this.idProducto);
                        } else {
                            MensajeAlerta("Advertencia", "Debe seleccionar un modificador para realizar está opción");
                        }
                    }
                    break;
                case R.id.btnCancelar:
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    break;
            }
    }
    private void MensajeAlerta(String titulo,String mensaje){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(mensaje).setTitle(titulo).setPositiveButton("Salir",null).create().show();

    }
    private void CambiarEstadoVariante(boolean EstadoModificador){
        asyncModificadores.ActualizarEstadoModProduct(idProducto,EstadoModificador);
    }

    private void VerificarEstadoModificador(String mensaje){

        Dialog dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CambiarEstadoVariante(estadoTemp);

            }

        });

        dialog=builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){


        }
        else{



        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        asyncModificadores.BusquedaModificadores(query);
        rvModificadores.setVisibility(View.INVISIBLE);

        pbIndicator2.show();
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}
