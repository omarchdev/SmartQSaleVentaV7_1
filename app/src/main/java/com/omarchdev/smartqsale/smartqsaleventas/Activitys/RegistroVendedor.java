package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.MenuResources;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVendedores;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

public class RegistroVendedor extends ActivityParent implements View.OnClickListener, MenuResources.ListenerAccionRegistro {

    RelativeLayout rlContent;
    AVLoadingIndicatorView avi;
    TextView txtCargando;
    AsyncVendedores asyncVendedores;
    int idVendedor;
    mVendedor vendedorActual;
   TextInputLayout edtNombre,edtApellidoPaterno,edtApellidMaterno,edtNumeroTelefono,edtEmail;
    FloatingActionMenu actionMenu;
    Context context;
    boolean edicionH;
    boolean salir;
    MenuResources menuResources;
    int estadoConfig;
    MenuItem itemEliminar;
    MenuItem itemGuardar;

    /*
    * fun GuardarNuevoUsuario(view: View) {
        val popupMenu = popupMenu {
            section {
                title="Opciones"
                item {
                    label =labelNuevo
                    icon = R.drawable.check //optional
                    callback = { //optional
                      ConfirmacionGuardar()
                    }
                }

            }
        }

        popupMenu.show(this@ConfiguracionUsuario, view)
    }
    *
    * */

   FloatingActionButton fabGuardar,fabEditar,fabCancelar,fabEliminar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_guardar) {
            GuardarVendedor();

            return true;
        }else if(id==R.id.action_eliminar){
            EliminarVendedor();
        }

        return super.onOptionsItemSelected(item);
    }

    public void EliminarVendedor(){
        asyncVendedores.EliminarVendedorId(idVendedor);
        asyncVendedores.setListenerEliminarVendedor(new AsyncVendedores.ListenerEliminarVendedor() {
            @Override
            public void EliminarVendedorExito(){

                itemEliminar.setVisible(false);
                itemGuardar.setVisible(false);
                MensajeAdvertencia("Confirmación","El vendedor se eliminó con éxito.");
            }

            @Override
            public void EliminarErrorVendedor() {
                itemEliminar.setVisible(false);
                itemGuardar.setVisible(false);
                MensajeAdvertencia("Error","Error al eliminar al vendedor.Verifique su conexión a internet.");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_congif_cliente, menu);
        itemEliminar=menu.findItem(R.id.action_eliminar);
        itemGuardar=menu.findItem(R.id.action_guardar);
        return super.onCreateOptionsMenu(menu);
    }

    public void MensajeAdvertencia(String titulo,String mensaje){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setMessage(mensaje).setTitle(titulo)
                .create().show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_vendedor);
        edicionH=true;
        idVendedor=0;
        salir=false;
        menuResources=new MenuResources(this);
        menuResources.setListernerAccionRegistro(this);
        rlContent=findViewById(R.id.rlContent);
        avi=findViewById(R.id.avi);
        txtCargando=findViewById(R.id.txtCargando);
        asyncVendedores=new AsyncVendedores();
        actionMenu=findViewById(R.id.actionMenu);
        idVendedor=getIntent().getIntExtra("IdVendedor",0);
        estadoConfig=getIntent().getIntExtra("accionConfig",0);
        edtNombre=findViewById(R.id.edtNombre);
        edtApellidoPaterno=findViewById(R.id.edtApellidoPaterno);
        edtApellidMaterno=findViewById(R.id.edtApellidMaterno);
        edtNumeroTelefono=findViewById(R.id.edtNumeroTelefono);
        edtEmail=findViewById(R.id.edtEmail);
        asyncVendedores.setContextLoading(this,avi,txtCargando,rlContent,null);
        fabGuardar=findViewById(R.id.menu_item_guardar);
        fabEditar=findViewById(R.id.menu_item_edit);
        fabCancelar=findViewById(R.id.menu_item_cancelar);
        fabEliminar=findViewById(R.id.menu_item_eliminar);


        fabEliminar.setVisibility(View.GONE);
        fabCancelar.setVisibility(View.GONE);
        fabEditar.setVisibility(View.GONE);
        fabGuardar.setVisibility(View.GONE);

        fabEliminar.setOnClickListener(this);
        fabCancelar.setOnClickListener(this);
        fabEditar.setOnClickListener(this);
        fabGuardar.setOnClickListener(this);
        context=this;
        avi.hide();
        txtCargando.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Agregar Vendedor");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
        getSupportActionBar().setElevation(4);
        ElegirAccion();
        pantallaVendedor();
        actionMenu.setVisibility(View.GONE);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    public void ElegirAccion(){

        if(idVendedor==0){
            PoblarCamposVacios();
            fabGuardar.setVisibility(View.VISIBLE);
            fabCancelar.setVisibility(View.VISIBLE);
            fabEliminar.setVisibility(View.GONE);
            fabEditar.setVisibility(View.GONE );
            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#757575\">" + "Agregar Vendedor"+ "</font>")));

        }else if(idVendedor!=0){
            BloquearEditText();
            fabEditar.setVisibility(View.VISIBLE);
            fabCancelar.setVisibility(View.VISIBLE);
            asyncVendedores.ObtenerVendedorId(idVendedor);
            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#757575\">" + "Editar datos vendedor"+ "</font>")));

            asyncVendedores.setListenerRegistroVendedor(new AsyncVendedores.ListenerRegistroVendedor() {
                @Override
                public void ProcesoExitoso() {

                }

                @Override
                public void ErrorConnection() {

                }

                @Override
                public void ErrorSql() {

                }

                @Override
                public void ObtenerVendedorId(mVendedor vendedor) {
                    if(vendedor!=null) {
                        vendedorActual = vendedor;

                        PoblarCampos();
                        if(estadoConfig==Constantes.EstadoConfiguracion.Visualizar){
                            BloquearEditText();
                            itemEliminar.setVisible(false);
                            itemGuardar.setVisible(false);
                            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#757575\">" + vendedorActual.getPrimerNombre()+" "+ vendedor.getApellidoMaterno() + "</font>")));

                        }
                        else if(estadoConfig==Constantes.EstadoConfiguracion.Editar){
                            DesbloquearPantalla();
                            itemEliminar.setVisible(true);
                            getSupportActionBar().setSubtitle((Html.fromHtml("<font color=\"#757575\">" +  vendedorActual.getPrimerNombre()+" "+ vendedor.getApellidoMaterno() + "</font>")));

                            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#757575\">" + "Editar"+ "</font>")));

                        }
                        actionMenu.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    public void pantallaVendedor(){

        if(estadoConfig== Constantes.EstadoConfiguracion.Nuevo){
            DesbloquearPantalla();
        }else if(estadoConfig== Constantes.EstadoConfiguracion.Editar){
            DesbloquearPantalla();
        }else  if(estadoConfig== Constantes.EstadoConfiguracion.Visualizar){
            BloquearEditText();
        }


    }

    public void PoblarCampos(){


            edtNombre.getEditText().setText(vendedorActual.getPrimerNombre());
            edtApellidoPaterno.getEditText().setText(vendedorActual.getApellidoPaterno());
            edtApellidMaterno.getEditText().setText(vendedorActual.getApellidoMaterno());
            edtEmail.getEditText().setText(vendedorActual.getEmail());
            edtNumeroTelefono.getEditText().setText(vendedorActual.getNumeroTelefono());

    }

    public void PoblarCamposVacios(){


        edtNombre.getEditText().setText("");
        edtApellidoPaterno.getEditText().setText("");
        edtApellidMaterno.getEditText().setText("");
        edtEmail.getEditText().setText("");
        edtNumeroTelefono.getEditText().setText("");
    }

    @Override
    public void onBackPressed() {
        if(edicionH) {
            if (salir) {
                super.onBackPressed();
            } else {

                Salir();

            }
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.menu_item_guardar){


            GuardarVendedor();


        }else if(v.getId()==R.id.menu_item_edit){


            DesbloquearPantalla();


        }else if(v.getId()==R.id.menu_item_cancelar){


            Salir();


        }else if(v.getId()==R.id.menu_item_eliminar){




        }


    }

    public void Salir(){


        if(edicionH){

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    salir=true;
                    onBackPressed();
                }
            }).setNegativeButton("Cancelar",null).setTitle("Advertencia").setMessage("¿Está seguro de salir sin guardar?")
                    .create().show();

        }
        else{



        }


    }

    public void DesbloquearPantalla(){
        edicionH=true;
        actionMenu.setVisibility(View.VISIBLE);
        edtNombre.setEnabled(true);
        edtApellidMaterno.setEnabled(true);
        edtApellidoPaterno.setEnabled(true);
        edtEmail.setEnabled(true);
        edtNumeroTelefono.setEnabled(true);
        fabGuardar.setVisibility(View.VISIBLE);
        fabEditar.setVisibility(View.GONE);
        fabEliminar.setVisibility(View.GONE);
        fabCancelar.setVisibility(View.VISIBLE);
        actionMenu.close(true);

    }

    public void BloquearEditText(){

        edicionH=false;
        actionMenu.setVisibility(View.VISIBLE);
        edtNombre.setEnabled(false);
        edtApellidMaterno.setEnabled(false);
        edtApellidoPaterno.setEnabled(false);
        edtEmail.setEnabled(false);
        edtNumeroTelefono.setEnabled(false);
        fabGuardar.setVisibility(View.GONE);
        fabEditar.setVisibility(View.VISIBLE);
        fabEliminar.setVisibility(View.VISIBLE);
        fabCancelar.setVisibility(View.VISIBLE);

    }


    public void GuardarVendedor(){

        if(VerificarDatos()){
            vendedorActual=new mVendedor();
            vendedorActual.setIdVendedor(idVendedor);
            vendedorActual.setPrimerNombre(edtNombre.getEditText().getText().toString());
            vendedorActual.setApellidoPaterno(edtApellidoPaterno.getEditText().getText().toString());
            vendedorActual.setApellidoMaterno(edtApellidMaterno.getEditText().getText().toString());
            vendedorActual.setNumeroTelefono(edtNumeroTelefono.getEditText().getText().toString());
            vendedorActual.setEmail(edtEmail.getEditText().getText().toString());

            asyncVendedores.RegistrarVendedor(vendedorActual);
            asyncVendedores.setListenerRegistroVendedor(new AsyncVendedores.ListenerRegistroVendedor() {
                @Override
                public void ProcesoExitoso() {

                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Confirmación").setMessage("Éxito en guardar los datos");
                    builder.setPositiveButton("Aceptar",null);
                    builder.create().show();
                    edicionH=false;
                    actionMenu.close(true);
                    itemGuardar.setVisible(false);

                }

                @Override
                public void ErrorConnection() {

                    actionMenu.setVisibility(View.GONE);
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Advertencia").setMessage("Error al guardar los datos.Verifique su conexión");
                    builder.setPositiveButton("Aceptar",null);
                    builder.create().show();
                    itemGuardar.setVisible(false);
                }

                @Override
                public void ErrorSql() {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Advertencia").setMessage("Error a guardar los datos");
                    builder.setPositiveButton("Aceptar",null);
                    builder.create().show();

                    actionMenu.setVisibility(View.GONE);


                }

                @Override
                public void ObtenerVendedorId(mVendedor vendedor) {



                }
            });

        }

    }

    public boolean VerificarDatos(){

        boolean permitir=true;

            if(edtNombre.getEditText().getText().toString().equals("")){
                edtNombre.setError("Este campo es obligatorio");
                permitir=false;

            }
            if(edtApellidoPaterno.getEditText().getText().toString().equals("")){

                edtApellidoPaterno.setError("Este campo es obligatorio");
                permitir=false;
            }
            if(edtApellidMaterno.getEditText().getText().toString().equals("")){
                edtApellidMaterno.setError("Este campo es obligatorio");
                permitir=false;
            }



        return permitir;
    }

    @Override
    public void AccionGuardar() {
        GuardarVendedor();

    }
}
