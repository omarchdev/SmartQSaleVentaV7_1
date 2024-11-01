package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncLogUser;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncUsers;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.InfoSesionTemp;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.SesionUsuario;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import static com.omarchdev.smartqsale.smartqsaleventas.Controlador.CEstadoAppKt.SalirApp;

public class PinLoginActivity extends ActivityParent implements View.OnClickListener, AsyncUsers.ListenerVerificarResultadoPinLog, AsyncLogUser.ResultLogPrincipal, AsyncLogUser.ResultLogPin {

    AVLoadingIndicatorView avi;
    TextView txtTextoPin,txtMensajeConfirmacion;
    Button btnNumber1,btnNumber2,btnNumber3,btnNumber4,btnNumber5,btnNumber6,btnNumber7,btnNumber8,btnNumber9,btnNumber0;
    ImageButton btnNumberDelete;
    ImageView circle1,circle2,circle3,circle4;
    AsyncLogUser asyncLogUser;
    int longitudPin;
    int maxLongitud;
    int cont;
    boolean permitirEscribir;
    String pin,myIMEI;
    AsyncUsers asyncUsers;
    SesionUsuario s;
    InfoSesionTemp p;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVerificaConexionBd(false);
        setContentView(R.layout.activity_pin_login);
        myIMEI = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        getSupportActionBar().hide();
        asyncUsers=new AsyncUsers();
        asyncUsers.setContext(this);
        permitirEscribir=true;
        pin="";
        s=new SesionUsuario();
        longitudPin=0;
        maxLongitud=4;
        cont=0;
        asyncLogUser=new AsyncLogUser();
        avi=findViewById(R.id.avi);
        txtMensajeConfirmacion=findViewById(R.id.txtMensajeConfirmacion);
        txtTextoPin=findViewById(R.id.txtTextoPin);
        btnNumber1=findViewById(R.id.btnNumber1);
        btnNumber2=findViewById(R.id.btnNumber2);
        btnNumber3=findViewById(R.id.btnNumber3);
        btnNumber4=findViewById(R.id.btnNumber4);
        btnNumber5=findViewById(R.id.btnNumber5);
        btnNumber6=findViewById(R.id.btnNumber6);
        btnNumber7=findViewById(R.id.btnNumber7);
        btnNumber8=findViewById(R.id.btnNumber8);
        btnNumber9=findViewById(R.id.btnNumber9);
        btnNumber0=findViewById(R.id.btnNumber0);
        btnNumberDelete=findViewById(R.id.btnNumberDelete);
        circle1=findViewById(R.id.circle1);
        circle2=findViewById(R.id.circle2);
        circle3=findViewById(R.id.circle3);
        circle4=findViewById(R.id.circle4);
        btnNumber0.setOnClickListener(this);
        btnNumber1.setOnClickListener(this);
        btnNumber2.setOnClickListener(this);
        btnNumber3.setOnClickListener(this);
        btnNumber4.setOnClickListener(this);
        btnNumber5.setOnClickListener(this);
        btnNumber6.setOnClickListener(this);
        btnNumber7.setOnClickListener(this);
        btnNumber8.setOnClickListener(this);
        btnNumber9.setOnClickListener(this);
        btnNumberDelete.setOnClickListener(this);
        txtTextoPin.setVisibility(View.INVISIBLE);
        asyncUsers.setListenerVerificarResultadoPinLog(this);
        avi.hide();
        p=s.ObtenerInfoSesionTemp();
        if(p.getPermitirAcceso()){
            cont=4;
            pin="0000";
            AccederPin();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);

            }
        }

        /*

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ArrayList<String> list = new ArrayList<String>();

            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                list.add(Manifest.permission.BLUETOOTH_CONNECT);
                //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
            }
            if(list.size()>0){
                requestPermissions(new String[] { Manifest.permission.BLUETOOTH_CONNECT },101);

            }
        }
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              //  populateAutoComplete();
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(permitirEscribir) {
            if (v.getId() == R.id.btnNumberDelete) {
                cont = 0;
                pin = "";
                circle1.setImageResource(R.drawable.circle_void);
                circle2.setImageResource(R.drawable.circle_void);
                circle3.setImageResource(R.drawable.circle_void);
                circle4.setImageResource(R.drawable.circle_void);
                txtMensajeConfirmacion.setText("Ingrese el PIN");
            } else {
                if (cont < maxLongitud) {
                    cont++;
                    cambiarColorCirculo();
                    switch (v.getId()) {
                        case R.id.btnNumber0:
                            pin = pin + "0";
                            AccederPin();
                            break;
                        case R.id.btnNumber1:
                            pin = pin + "1";
                            AccederPin();
                            break;
                        case R.id.btnNumber2:
                            pin = pin + "2";
                            AccederPin();
                            break;
                        case R.id.btnNumber3:
                            pin = pin + "3";
                            AccederPin();
                            break;
                        case R.id.btnNumber4:
                            pin = pin + "4";
                            AccederPin();
                            break;
                        case R.id.btnNumber5:
                            pin = pin + "5";
                            AccederPin();
                            break;
                        case R.id.btnNumber6:
                            pin = pin + "6";
                            AccederPin();
                            break;
                        case R.id.btnNumber7:
                            pin = pin + "7";
                            AccederPin();
                            break;
                        case R.id.btnNumber8:
                            pin = pin + "8";
                            AccederPin();
                            break;
                        case R.id.btnNumber9:
                            pin = pin + "9";
                            AccederPin();
                            break;

                    }
                }
            }
        }
    }


    public void AccederPin(){

        if(cont==4){
            avi.smoothToShow();
         /*  asyncUsers.VerificarPinUsuario(pin,myIMEI, Build.BRAND, Build.MODEL, Build.VERSION.RELEASE);*/
            asyncLogUser.LogPin(pin,myIMEI,Build.BRAND,Build.MODEL, Build.VERSION.RELEASE,p.getUser(),p.getPermitirAcceso());
            asyncLogUser.setResultLogPin(this);
            permitirEscribir=false;
        }
    }
    public void cambiarColorCirculo(){
        if(cont==1){
            circle1.setImageResource(R.drawable.circle_full);
        }
        if(cont==2){
            circle2.setImageResource(R.drawable.circle_full);
        }
        if(cont==3){
            circle3.setImageResource(R.drawable.circle_full);
        }
        if(cont==4){
            circle4.setImageResource(R.drawable.circle_full);
        }
    }

    @Override
    public void EsAdministrador(byte respuesta) {
        Intent intent=new Intent(this,SelectTienda.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        SalirApp(this,this);
    }


    @Override
    public void respuestaVerificarUsuarioPin(byte respuesta) {
        permitirEscribir=true;
        txtTextoPin.setVisibility(View.INVISIBLE);

        Intent intent=new Intent(this,PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void ErrorInternetPin() {
        permitirEscribir=true;
        avi.hide();
        txtTextoPin.setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Advertencia").setMessage("Error al verificar el usuario.Verifique su internet").
                setPositiveButton("Salir",null).create().show();
    }

    @Override
    public void ErrorProcedurePin() {
        permitirEscribir=true;
        avi.hide();
        txtTextoPin.setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Advertencia").setMessage("Error al verificar el usuario.Verifique su internet").
                setPositiveButton("Salir",null).create().show();
    }

    @Override
    public void UserNoExistsPin() {
        permitirEscribir=true;
        avi.hide();
        txtTextoPin.setVisibility(View.INVISIBLE);
        circle1.setImageResource(R.drawable.circle_full_error);
        circle2.setImageResource(R.drawable.circle_full_error);
        circle3.setImageResource(R.drawable.circle_full_error);
        circle4.setImageResource(R.drawable.circle_full_error);
        txtMensajeConfirmacion.setText("Pin incorrecto");

    }

    @Override
    public void LogExito() {

    }

    @Override
    public void LogError(@NotNull String mensaje) {

    }

    @Override
    public void PinLogExito() {

      s.ReiniciarConteoUsuario();
        if(Constantes.Usuario.esAdministrador) {
            Intent intent = new Intent(this, SelectTienda.class);
            startActivity(intent);
            finish();
        }else{
            permitirEscribir=true;
            Intent intent=new Intent(this,PantallaPrincipal.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void PinLogExitoAccesoDirecto() {
        s.ReiniciarConteoUsuario();
        permitirEscribir=true;
        Intent intent=new Intent(this,PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void PinLogError(@NotNull String mensaje) {
        permitirEscribir=true;
        try{
            avi.hide();
        }catch (Exception ex){
            ex.toString();

        }

        txtTextoPin.setVisibility(View.INVISIBLE);
        circle1.setImageResource(R.drawable.circle_full_error);
        circle2.setImageResource(R.drawable.circle_full_error);
        circle3.setImageResource(R.drawable.circle_full_error);
        circle4.setImageResource(R.drawable.circle_full_error);
        txtMensajeConfirmacion.setText(mensaje);
    }
}
