package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncLogUser;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncRegistroUsuario;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncTiendas;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncUsers;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.HttpConsultas;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipoTienda;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RegistroUsuario extends ActivityParent implements View.OnClickListener, AsyncRegistroUsuario.ListenerSegmentos, AdapterView.OnItemSelectedListener, AsyncUsers.ListenerVerificarResultadoLogin, AsyncTiendas.ListenerConfigTienda, HttpConsultas.ListenerResultadoBusquedaCliente, AsyncRegistroUsuario.IEstadoRegistroUsuario, AsyncLogUser.ResultLogPrincipal, AsyncLogUser.ResultLogPin, AsyncLogUser.ResultFirstLogin {

    TextInputLayout edtNombreUsuario,edtNombreTienda,edtEmail,edtContrasena,edtContrasenaConf,edtTelefono,edtNumRuc,edtDireccion;
    String nombreUsuario,nombreTienda,contrasena,contrasenaConf,telefono,email;
    Button btnRegistrarUsuario,btnVerificaRuc;
    AsyncRegistroUsuario asyncRegistroUsuario;
    Spinner spnTipoTienda;
    List<String> listaTipos;
    List<mTipoTienda> tipoTiendas;
    ArrayAdapter<String>arrayAdapter;
    int position;
    int idSegmento;
    mUsuario usuario;
    int minLength;
    HttpConsultas httpConsultas;
    AVLoadingIndicatorView pbIndicator;
    TextView txtCargando;
    ScrollView scContent;
    DbHelper dbHelper;
    AsyncUsers asyncUsers;
    String myImei;
    Context context;
    AsyncTiendas asyncTiendas;
    AsyncLogUser asyncLogUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        minLength=11;
        asyncLogUser=new AsyncLogUser();
        asyncLogUser.setResultLogPrincipal(this);
        myImei= Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        httpConsultas=new HttpConsultas();
        dbHelper=new DbHelper(this);
        edtDireccion=findViewById(R.id.edtDireccion);
        btnVerificaRuc=findViewById(R.id.btnVerificaRuc);
        edtNombreUsuario=findViewById(R.id.edtNombreUsuario);
        edtNombreTienda=findViewById(R.id.edtNombreTienda);
        edtContrasena=findViewById(R.id.edtContrasena);
        edtContrasenaConf=findViewById(R.id.edtContrasenaConf);
        edtTelefono=findViewById(R.id.edtTelefono);
        edtEmail=findViewById(R.id.edtEmail);
        btnRegistrarUsuario=findViewById(R.id.btnRegistrarUsuario);
        btnRegistrarUsuario.setOnClickListener(this);
        asyncRegistroUsuario=new AsyncRegistroUsuario();
        asyncRegistroUsuario.setListenerSegmentos(this);
        spnTipoTienda=findViewById(R.id.spnTipoTienda);
        httpConsultas.setListenerResultadoBusquedaCliente(this);
        listaTipos=new ArrayList<>();
        asyncRegistroUsuario.ObtenerSegmentos();
        position=0;
        spnTipoTienda.setOnItemSelectedListener(this);
        idSegmento=0;
        getSupportActionBar().setTitle("Registrar nueva cuenta");
        usuario=new mUsuario();
        pbIndicator=findViewById(R.id.pbIndicator);
        txtCargando=findViewById(R.id.txtCargando);
        edtNumRuc=findViewById(R.id.edtNumRuc);
        scContent=findViewById(R.id.scContent);
        scContent.setVisibility(View.INVISIBLE);
        pbIndicator.show();
        asyncLogUser.setResultFistLogin(this);
        asyncTiendas=new AsyncTiendas();
        txtCargando.setVisibility(View.VISIBLE);
        asyncUsers=new AsyncUsers();
        asyncUsers.setListenerVerificarResultadoLogin(this);
        asyncUsers.setContext(this);
        btnVerificaRuc.setOnClickListener(this);
        context=this;
        asyncTiendas.setListenerConfigTienda(this);
        asyncRegistroUsuario.setiEstadoRegistroUsuario(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( "Registro de Compañia");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);

   }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean ValidarEmail(String email){

       return email.contains("@");

    }

    private boolean ValidarLongitud(String texto){
        return texto.length()>0;
    }




    public void RegistrarUsuario(){
        boolean cancelar=false;
        contrasena=edtContrasena.getEditText().getText().toString();
        contrasenaConf=edtContrasenaConf.getEditText().getText().toString();
        telefono=edtTelefono.getEditText().getText().toString();
        nombreUsuario=edtNombreUsuario.getEditText().getText().toString();
        nombreTienda=edtNombreTienda.getEditText().getText().toString();
        email=edtEmail.getEditText().getText().toString();

        if(!ValidarLongitud(contrasena)){
            cancelar=true;
            edtContrasena.setError("Este campo es obligatorio");
        }
        if(!ValidarLongitud(contrasenaConf)){
            cancelar=true;
            edtContrasenaConf.setError("Este campo es obligatorio");
        }
        if(!cancelar){
            if(!TextUtils.equals(contrasena,contrasenaConf)){
                edtContrasenaConf.setError("La contraseña no coincide");
                cancelar=true;
             }
        }

        if(!ValidarLongitud(nombreUsuario)){
            cancelar=true;
            edtNombreUsuario.setError("Este campo es obligatorio");
        }
        if(!ValidarLongitud(nombreTienda)){
            cancelar=true;
            edtNombreTienda.setError("Este campo es obligatorio");
        }

        if(!ValidarEmail(email)){
            cancelar=true;
            edtEmail.setError("El correo no es válido");
        }

        if(TextUtils.isEmpty(email)){
            cancelar=true;
            edtEmail.setError("Este campo es obligatorio");
        }
        if(position==0){
            cancelar=true;
            TextView errorText=(TextView) spnTipoTienda.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Debe seleccionar un tipo de tienda");
        }
        if(!cancelar){

            usuario.setNombreUsuario(nombreUsuario);
            usuario.setNombreTienda(nombreTienda);
            usuario.setEmail(email);
            usuario.setContrasena(contrasena);
            usuario.setTelefono(telefono);
            usuario.setIdSegmento(idSegmento);
            usuario.setcDireccion(edtDireccion.getEditText().getText().toString().trim());
            usuario.setcNumRuc(edtNumRuc.getEditText().getText().toString().trim());
            asyncRegistroUsuario.setContext(this);
            asyncRegistroUsuario.RegistrarUsuario(usuario);
          }
        else{
            Toast.makeText(this,"Complete sus datos correctamente",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnRegistrarUsuario:
                RegistrarUsuario();
                break;
            case R.id.btnVerificaRuc:
                if(minLength==edtNumRuc.getEditText().getText().toString().trim().length()){
                  httpConsultas.ObtenerDatosClienteNumRuc(edtNumRuc.getEditText().getText().toString().trim());
                }else{
                }
                break;
        }

    }

    @Override
    public void ResultadoSegmentos(List<mTipoTienda> tipoTiendas) {

        int longitud;
        listaTipos.add("Seleccione su tipo de Negocio");
        if(tipoTiendas!=null){
            try {
                scContent.setVisibility(View.VISIBLE);
                pbIndicator.hide();
                txtCargando.setVisibility(View.INVISIBLE);
                this.tipoTiendas = tipoTiendas;
                longitud = this.tipoTiendas.size();

                for (int i = 0; i < longitud; i++) {

                    listaTipos.add(this.tipoTiendas.get(i).getDescripcionTienda());
                }
                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaTipos);
                spnTipoTienda.setAdapter(arrayAdapter);
                asyncRegistroUsuario.CancelarObtenerSegmentos();
            }catch (Exception e){
                e.toString();
                finish();

            }
        }
        else{
            finish();
            Toast.makeText(this,"Error al obtener datos.Verifique su conexión",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void ResultadoRegistrarUsuario(String respuesta) {

        switch (respuesta){

            case "0":
                Toast.makeText(this,"Registrado",Toast.LENGTH_LONG).show();
                dbHelper.DeleteUserRegister();
                dbHelper.InsertOptionUserRegister(usuario.getEmail(),usuario.getContrasena());
                dbHelper.SelectUsuario();
                asyncUsers.setContext(this);
                asyncUsers.VerificarCredencialesUsuario(usuario.getEmail(),usuario.getContrasena());

                break;

            case "1":
                Toast.makeText(this,"Error al registrar",Toast.LENGTH_LONG).show();
                break;

            case "n":
                Toast.makeText(this,"Error al registrar.Verifique su conexión",Toast.LENGTH_LONG).show();
            case "e":
                Toast.makeText(this,"El email ya se encuentra registrado",Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            this.position=position;
            try {
                    if (position > 0) {

                        idSegmento = tipoTiendas.get(position-1).getIdTipoTienda();

                    } else if (position == 0) {

                        idSegmento = 0;
                    }
            }catch (Exception e){
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void respuestaVerificarUsuario(byte respuesta) {

        asyncUsers.VerificarPinUsuario(Constantes.ValorPorDefecto.PinDefecto,myImei,Build.BRAND,Build.MODEL,Build.VERSION.RELEASE);
        asyncUsers.setListenerVerificarResultadoPinLog(new AsyncUsers.ListenerVerificarResultadoPinLog() {
            @Override
            public void EsAdministrador(byte respuesta) {
                  asyncTiendas.ObtenerConfigTienda(Constantes.Tiendas.tiendaList.get(0).getIdTienda(),context);
            }

            @Override
            public void respuestaVerificarUsuarioPin(byte respuesta) {
             }

            @Override
            public void ErrorInternetPin() {
            }

            @Override
            public void ErrorProcedurePin() {
            }

            @Override
            public void UserNoExistsPin() {
                }
        });

    }

    @Override
    public void ErrorInternet() {

    }

    @Override
    public void ErrorProcedure() {

    }

    @Override
    public void UserNoExists() {

    }

    @Override
    public void FinDemo() {

    }

    private void IngresarPantallaPrincipal(){
        Intent intent=new Intent(this,PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void IngresarTienda() {
        IngresarPantallaPrincipal();

    }

    @Override
    public void DatosClienteResultadoSunat(@Nullable mCustomer cliente) {
        edtNombreTienda.getEditText().setText(cliente.getRazonSocial());
        edtDireccion.getEditText().setText(cliente.getcDireccion());
    }

    @Override
    public void ErrorConsultaCliente(@NotNull String mensaje) {

    }

    @Override
    public void ClienteNoHabilitado(@NotNull String nombre) {

    }

    @Override
    public void RegistroExitoso() {
        dbHelper.DeleteUserRegister();
        dbHelper.InsertOptionUserRegister(usuario.getEmail(),usuario.getContrasena());
        dbHelper.SelectUsuario();
        asyncUsers.setContext(this);
        asyncLogUser.PrimerLog(usuario.getEmail(),usuario.getContrasena(),Constantes.ValorPorDefecto.PinDefecto,myImei,Build.BRAND,Build.MODEL, Build.VERSION.RELEASE,0,false);

    }

    @Override
    public void ErrorRegistro(String mensaje) {

        new AlertDialog.Builder(this)
                .setTitle("Advertencia")
                .setMessage(mensaje)
                .setPositiveButton("Salir",null)
                .create()
                .show();
    }

    @Override
    public void LogExito() {
        asyncLogUser.LogPin(Constantes.ValorPorDefecto.PinDefecto,myImei,Build.BRAND,Build.MODEL, Build.VERSION.RELEASE,0,false);
        asyncLogUser.setResultLogPin(this);
    }

    @Override
    public void LogError(@NotNull String mensaje) {

    }

    @Override
    public void PinLogExito() {
        if(Constantes.Usuario.esAdministrador) {
            Intent intent = new Intent(this, SelectTienda.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(this,PantallaPrincipal.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void PinLogExitoAccesoDirecto() {

    }

    @Override
    public void PinLogError(@NotNull String mensaje) {

    }

    @Override
    public void LogExitoFirst() {
        Intent intent=new Intent(this,PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void LogErrorFirst(@NotNull String mensaje) {

    }
}
