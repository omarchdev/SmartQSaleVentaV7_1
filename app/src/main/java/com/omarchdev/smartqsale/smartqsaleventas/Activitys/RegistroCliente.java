package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncClientes;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class RegistroCliente extends ActivityParent implements View.OnClickListener{

    List<String> listTipoCliente;
    Spinner spinner;
    TextInputLayout tilNombre;
    TextInputLayout tilApellidoPaterno;
    TextInputLayout tilApellidoMaterno;
    TextInputLayout tilNumeroTelefono;
    TextInputLayout tilDireccion;
    TextInputLayout tilEmail,tilRazonSocial,tilNumeroRuc;
    int estadoConfig;
    mCustomer cliente;
    boolean edicionH;
    boolean salir;
    int idCliente;
    MenuItem itemEliminar;
    MenuItem itemGuardar;
    FloatingActionMenu actionMenu;
    int TipoCliente;
    FloatingActionButton fabGuardar,fabEditar,fabCancelar,fabEliminar;
    Context context;
    AVLoadingIndicatorView avi;
    TextView txtCargando;
    AsyncClientes asyncClientes;
    RelativeLayout rlContent;

    boolean cargaFinalizada;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_congif_cliente, menu);
        itemEliminar=menu.findItem(R.id.action_eliminar);
        itemGuardar=menu.findItem(R.id.action_guardar);
        return super.onCreateOptionsMenu(menu);
    }
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
            EliminarUsuario();

        }

        return super.onOptionsItemSelected(item);
    }
    public void MensajeError(String titulo,String mensaje){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle(titulo).setMessage(mensaje).setPositiveButton("Salir",null).create().show();
    }
    public void EliminarUsuario(){

        asyncClientes.EliminarClienteId(idCliente);
        asyncClientes.setListenerEliminarCliente(new AsyncClientes.ListenerEliminarCliente() {
            @Override
            public void EliminarClienteExito() {
                itemEliminar.setVisible(false);
                itemGuardar.setVisible(false);
                MensajeError("Confirmación","El cliente se eliminó con éxito.");
            }

            @Override
            public void EliminarClienteError() {

                itemEliminar.setVisible(false);
                itemGuardar.setVisible(false);
                MensajeError("Advertencia","Error al elimimar cliente.Verifique su conexión.");

            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registro_cliente);
            idCliente = 0;
            idCliente=getIntent().getIntExtra("IdCliente",0);

            estadoConfig=getIntent().getIntExtra("accionConfig",0);

            avi=findViewById(R.id.avi);
            txtCargando=findViewById(R.id.txtCargando);
            edicionH = true;

            salir = false;
            cliente = new mCustomer();
            spinner = findViewById(R.id.spnTipoCliente);
            rlContent=findViewById(R.id.rlContent);
           listTipoCliente = new ArrayList<>();
            listTipoCliente.add(this.getResources().getInteger(R.integer.PositionPNatural),this.getResources().getString(R.string.TituloPersonaNatural));
            listTipoCliente.add(this.getResources().getInteger(R.integer.PositionPJuridica),this.getResources().getString(R.string.TituloPersonaJuridica));
            tilNombre = findViewById(R.id.tilNombreCliente);
            tilApellidoPaterno = findViewById(R.id.tilApellidoPaternoCliente);
            tilApellidoMaterno = findViewById(R.id.tilApellidoMaternoCLiente);
            tilNumeroTelefono = findViewById(R.id.tilNumeroTelefonoCustomer);
            tilRazonSocial=findViewById(R.id.tilRazonSocial);
            tilNumeroRuc=findViewById(R.id.tilNumeroRuc);

            tilDireccion = findViewById(R.id.tilDireccionCliente);
            tilEmail = findViewById(R.id.tilEmailCliente);
            fabGuardar = findViewById(R.id.menu_item_guardar);
            fabEditar = findViewById(R.id.menu_item_edit);
            fabCancelar = findViewById(R.id.menu_item_cancelar);
            fabEliminar = findViewById(R.id.menu_item_eliminar);
            actionMenu = findViewById(R.id.actionMenu);
            TipoCliente = 1;
            spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listTipoCliente));

            spinner.setSelection(0);
            fabEliminar.setOnClickListener(this);
            fabCancelar.setOnClickListener(this);
            fabEditar.setOnClickListener(this);
            fabGuardar.setOnClickListener(this);
            context = this;
            txtCargando.setVisibility(View.INVISIBLE);
            avi.hide();
            fabEditar.setVisibility(View.GONE);
            fabEliminar.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Agregar Cliente");
            asyncClientes=new AsyncClientes();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle( "Agregar Cliente");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
            getSupportActionBar().setElevation(4);

            if(idCliente>0){
                BloquearEditText();
                InterfazPersonaNatural();
                spinner.setOnItemSelectedListener(null);
                cargaFinalizada=false;
                asyncClientes.ObtenerClienteId(idCliente);
                asyncClientes.setScreenContext(this,avi,txtCargando,rlContent,null);
                asyncClientes.setObtenerDatoCliente(    new AsyncClientes.ObtenerDatoCliente() {
                    @Override
                    public void ClienteObtenido(mCustomer customer) {
                        cliente=customer;

                        if(estadoConfig==Constantes.EstadoConfiguracion.Visualizar){
                            itemEliminar.setVisible(false);
                            itemGuardar.setVisible(false);
                            getSupportActionBar().setTitle(cliente.getcName()+" "+cliente.getcApellidoPaterno());

                        }else if(estadoConfig==Constantes.EstadoConfiguracion.Editar){
                            itemEliminar.setVisible(true);
                            itemGuardar.setVisible(true);
                            getSupportActionBar().setSubtitle( cliente.getcName()+" "+cliente.getcApellidoPaterno() );

                            getSupportActionBar().setTitle( "Editar");

                        }
                         spinner.setOnItemSelectedListener(selectionListener);

                        if(customer.getTipoCliente()==1){
                            spinner.setSelection(0);


                            cargaFinalizada=true;
                        }
                        else if(customer.getTipoCliente()==2){

                             spinner.setSelection(1);



                            cargaFinalizada=true;


                        }

                        if(cliente.getTipoCliente()==2){

                            tilDireccion.getEditText().setText(cliente.getcDireccion());
                            tilEmail.getEditText().setText(cliente.getcEmail());
                            tilNumeroTelefono.getEditText().setText(cliente.getcNumberPhone());
                            tilRazonSocial.getEditText().setText(cliente.getRazonSocial());
                            tilNumeroRuc.getEditText().setText(cliente.getNumeroRuc());
                        }
                        else if(cliente.getTipoCliente()==1) {


                            tilNombre.getEditText().setText(customer.getcName());
                            tilApellidoPaterno.getEditText().setText(cliente.getcApellidoPaterno());
                            tilApellidoMaterno.getEditText().setText(cliente.getcApellidoMaterno());
                            tilDireccion.getEditText().setText(cliente.getcDireccion());
                            tilEmail.getEditText().setText(cliente.getcEmail());
                            tilNumeroTelefono.getEditText().setText(cliente.getcNumberPhone());


                        }
                     //
                    actionMenu.setVisibility(View.GONE);
                    }
                });

            }
            else if(idCliente==0){
                InterfazPersonaNatural();
                asyncClientes.setContext(this);
                spinner.setOnItemSelectedListener(selectionListener);
                cargaFinalizada=true;


            }
            actionMenu.setVisibility(View.GONE);


            ConfigurarPantalla();

        }
        catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void ConfigurarPantalla(){

        if(estadoConfig==Constantes.EstadoConfiguracion.Nuevo){
            DesbloquearPantalla();
        }else if(estadoConfig==Constantes.EstadoConfiguracion.Editar){
            DesbloquearPantalla();
        }else if(estadoConfig==Constantes.EstadoConfiguracion.Visualizar){
            BloquearEditText();
        }

        actionMenu.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
    public void Salir(){



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

    public void PoblarCampos(){

        if(cliente.getTipoCliente()==1){
            spinner.setSelection(0,true);
        }
        else if(cliente.getTipoCliente()==2){
            spinner.setSelection(1,true);
        }

       tilNombre.getEditText().setText(cliente.getcNumberPhone());
        tilApellidoPaterno.getEditText().setText(cliente.getcApellidoPaterno());
       tilApellidoMaterno.getEditText().setText(cliente.getcApellidoMaterno());
        tilEmail.getEditText().setText(cliente.getcEmail());
        tilNumeroTelefono.getEditText().setText(cliente.getcNumberPhone());

   }

    public void DesbloquearPantalla(){
        edicionH=true;
        actionMenu.setVisibility(View.VISIBLE);
       tilNombre.setEnabled(true);
        tilApellidoMaterno.setEnabled(true);
        tilApellidoPaterno.setEnabled(true);
       tilEmail.setEnabled(true);
        tilNumeroTelefono.setEnabled(true);
        fabGuardar.setVisibility(View.VISIBLE);
        fabEditar.setVisibility(View.GONE);
        fabEliminar.setVisibility(View.GONE);
        fabCancelar.setVisibility(View.VISIBLE);
        actionMenu.close(true);
        tilDireccion.setEnabled(true);
        tilRazonSocial.setEnabled(true);
        tilNumeroRuc.setEnabled(true);
        spinner.setEnabled(true);
    }
    public void BloquearEditText(){

        edicionH=false;
        actionMenu.setVisibility(View.VISIBLE);
        tilNombre.setEnabled(false);
       tilApellidoMaterno.setEnabled(false);
        tilApellidoPaterno.setEnabled(false);
        tilEmail.setEnabled(false);
        tilNumeroTelefono.setEnabled(false);
        fabGuardar.setVisibility(View.GONE);
        fabEditar.setVisibility(View.VISIBLE);
        fabEliminar.setVisibility(View.VISIBLE);
        fabCancelar.setVisibility(View.VISIBLE);
        tilNumeroRuc.setEnabled(false);
        tilRazonSocial.setEnabled(false);
        tilDireccion.setEnabled(false);
        spinner.setEnabled(false);

    }


    public void PoblarCamposVacios(){


        tilNombre.getEditText().setText("");
        tilApellidoPaterno.getEditText().setText("");
        tilApellidoMaterno.getEditText().setText("");
        tilEmail.getEditText().setText("");
        tilNumeroTelefono.getEditText().setText("");
    }

    public void GuardarVendedor(){

        if(VerificarDatos()) {
            cliente = new mCustomer();
            if(spinner.getSelectedItemPosition()==0) {
                cliente.setiId(idCliente);
                cliente.setcName(tilNombre.getEditText().getText().toString());
                cliente.setcApellidoPaterno(tilApellidoPaterno.getEditText().getText().toString());
                cliente.setcApellidoMaterno(tilApellidoMaterno.getEditText().getText().toString());
                cliente.setcNumberPhone(tilNumeroTelefono.getEditText().getText().toString());
                cliente.setcEmail(tilEmail.getEditText().getText().toString());
                cliente.setcDireccion(tilDireccion.getEditText().getText().toString());
                cliente.setTipoCliente(this.getResources().getInteger(R.integer.ValorPersonaNatural));
                asyncClientes.GuardarCliente(cliente);
            }
            else if(spinner.getSelectedItemPosition()==1){
                cliente.setiId(idCliente);
                cliente.setRazonSocial(tilRazonSocial.getEditText().getText().toString());
                cliente.setNumeroRuc(tilNumeroRuc.getEditText().getText().toString());
                cliente.setcNumberPhone(tilNumeroTelefono.getEditText().getText().toString());
                cliente.setcEmail(tilEmail.getEditText().getText().toString());
                cliente.setcDireccion(tilDireccion.getEditText().getText().toString());
                cliente.setTipoCliente(this.getResources().getInteger(R.integer.ValorPersonaJuridica));
                asyncClientes.GuardarCliente(cliente);
            }

            asyncClientes.setRegistroClientes(new AsyncClientes.RegistroClientes() {
                @Override
                public void ErrorConnection() {
                    actionMenu.close(true);
                    MensajeAlerta("Error","Error al registrar al cliente.Verifique su conexión a internet");
                }

                @Override
                public void ErrorRegistro() {

                    actionMenu.close(true);
                     MensajeAlerta("Error","Error al registrar al cliente. Codigo 00099");
                }

                @Override
                public void ActualizarExito() {

                    actionMenu.close(true);
                    edicionH=false;

                    MensajeAlerta("Confirmacion","El cliente se actualizo con éxito");
                }

                @Override
                public void RegistrarExito() {

                    actionMenu.close(true);
                    edicionH=false;

                    MensajeAlerta("Confirmacion","El cliente se registró con éxito");
                }

                @Override
                public void ExisteCliente() {

                }
            });

        }

    }

    public void MensajeAlerta(String titulo,String mensaje){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(mensaje).setTitle(titulo).setPositiveButton("Salir",null).create().show();

    }

    public boolean VerificarDatos(){

        boolean permitir=true;
        if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaNatural)) {
            if (tilNombre.getEditText().getText().toString().equals("")) {
                tilNombre.setError("Este campo es obligatorio");
                permitir = false;

            }
        }else if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaJuridica)){
            if (tilRazonSocial.getEditText().getText().toString().equals("")) {
                tilRazonSocial.setError("Este campo es obligatorio");
                permitir = false;

            }
        }
        if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaNatural)) {
            if (tilApellidoPaterno.getEditText().getText().toString().equals("")) {

                tilApellidoPaterno.setError("Este campo es obligatorio");
                permitir = false;
            }
        }else if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaJuridica)){
            if (tilNumeroRuc.getEditText().getText().toString().equals("")) {

                tilNumeroRuc.setError("Este campo es obligatorio");
                permitir = false;
            }
        }
        if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaNatural)) {
            if (tilApellidoMaterno.getEditText().getText().toString().equals("")) {
                tilApellidoMaterno.setError("Este campo es obligatorio");
                permitir = false;
            }
        }




        return permitir;
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

    private void InterfazPersonaNatural(){
        TipoCliente=this.getResources().getInteger(R.integer.ValorPersonaNatural);
        tilNumeroRuc.setVisibility(View.GONE);
        tilRazonSocial.setVisibility(View.GONE);


        tilNombre.setVisibility(View.VISIBLE);
        tilApellidoPaterno.setVisibility(View.VISIBLE);

        tilApellidoMaterno.setVisibility(View.VISIBLE);




    }

    private void InterfazPersonaJuridica(){
        TipoCliente=this.getResources().getInteger(R.integer.ValorPersonaJuridica);
        tilRazonSocial.setVisibility(View.VISIBLE);
        tilNumeroRuc.setVisibility(View.VISIBLE);


        tilNombre.setVisibility(View.GONE);
        tilApellidoPaterno.setVisibility(View.GONE);

        tilApellidoMaterno.setVisibility(View.GONE);



    }


    AdapterView.OnItemSelectedListener selectionListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position==context.getResources().getInteger(R.integer.PositionPNatural)){
                TipoCliente=context.getResources().getInteger(R.integer.ValorPersonaNatural);
                InterfazPersonaNatural();
            }else if(position==context.getResources().getInteger(R.integer.PositionPJuridica)){
                TipoCliente=context.getResources().getInteger(R.integer.ValorPersonaJuridica);

                InterfazPersonaJuridica();
            }
        }

            @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


}
