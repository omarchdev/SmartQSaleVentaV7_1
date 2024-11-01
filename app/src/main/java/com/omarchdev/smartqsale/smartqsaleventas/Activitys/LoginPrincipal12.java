package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;

import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncLogUser;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncUsers;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.InfoSesionTemp;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.SesionUsuario;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginPrincipal12 extends ActivityParent implements LoaderCallbacks<Cursor>,
        AsyncUsers.ListenerVerificarResultadoLogin,
        OnClickListener,AsyncLogUser.ResultLogPrincipal, AsyncLogUser.ResultLogPin {

    private static final int REQUEST_READ_CONTACTS = 0;
    // UI references.
    AVLoadingIndicatorView avi;
    AsyncLogUser asyncLogUser;
    private TextInputLayout mEmailView;
    private TextInputLayout mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Button btnCrearCuenta;
    String email, myIMEI ;
    String contrasena;
    LinearLayout email_login_form;
    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    AsyncUsers asyncUsers;
    DbHelper dbHelper;
     ScrollView sv;
    SesionUsuario s;
    InfoSesionTemp p;
    int tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_principal12);
        // Set up the login form.
        try {
            tipo=0;
            myIMEI = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            asyncLogUser=new AsyncLogUser();
            asyncLogUser.setResultLogPrincipal(this);
            email_login_form=findViewById(R.id.email_login_form);
            sv=findViewById(R.id.login_form);
            mEmailView = findViewById(R.id.email);
         //   populateAutoComplete();

            mPasswordView = findViewById(R.id.password);
            asyncUsers = new AsyncUsers();
            asyncUsers.setContext(this);
            asyncUsers.setListenerVerificarResultadoLogin(this);
            dbHelper = new DbHelper(this);
            email = "";
            contrasena = "";
            avi=findViewById(R.id.avi);

        /*
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/
            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                    email_login_form.setVisibility(View.GONE);
                    avi.show();
                }
            });
            btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
            btnCrearCuenta.setOnClickListener(this);
            sv.setVisibility(View.INVISIBLE);
            getSupportActionBar().hide();
            mPasswordView.setEnabled(true);
            mPasswordView.getEditText().setEnabled(true);
/*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED )
                {
                    ActivityCompat.requestPermissions(this,new String[]{
                            Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },100);
                }
            }

*/

            ArrayList<String> list = new ArrayList<String>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED )
                {

                    list.add(Manifest.permission.CAMERA);
                }
                if(  checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

                    list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                    list.add(Manifest.permission.BLUETOOTH_CONNECT);
                    //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
                }

            }

                if(list.size()>0){

                    ActivityCompat.requestPermissions(this,list.toArray(new String[list.size()]),100);
                }

           /* ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
            },100);*/

        }
        catch (Exception e){
            e.toString();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        avi.show();
        verificarExisteUsuario();
    }
    private void permissionGranted() {
        Toast.makeText(this, "Permiso camara", Toast.LENGTH_SHORT).show();
    }

    public void verificarExisteUsuario(){
        mUsuario usuario=dbHelper.SelectUsuario();
        if(!usuario.getEmail().equals("") && !usuario.getContrasena().equals("")){
            email_login_form.setVisibility(View.INVISIBLE);
            email=usuario.getEmail();
            contrasena=usuario.getContrasena();
            asyncLogUser.LogEmpresa(email,contrasena);
            //asyncUsers.VerificarCredencialesUsuario(email,contrasena);
        }
        else{
            avi.hide();
            sv.setVisibility(View.VISIBLE);
        }

    }
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private void requestBluetooth(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
            }

        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getEditText().getText().toString();
        String password = mPasswordView.getEditText().getText().toString();
        this.email=email;
        this.contrasena=password;
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        else if(!isEmailValid2(email)){
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;

        }
        else if(!isEmailValid3(email)){
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
     //       showProgress(true);
            avi.show();
            sv.setVisibility(View.INVISIBLE);

            asyncLogUser.LogEmpresa(email,password);
         //   asyncUsers.VerificarCredencialesUsuario(email,password);
            /*
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");

    }

    private boolean isEmailValid2(String email){
     return email.contains(".");
    }
    private boolean isEmailValid3(String email){
        return email.length()>5;
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

      //  addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginPrincipal12.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

    //    mEmailView.getEditText().setAdapter(adapter);
    }


    @Override
    public void respuestaVerificarUsuario(byte respuesta) {

            try {
                avi.hide();
                dbHelper.InsertOptionUserRegister(email,contrasena);
                //.SimboloMonedaPorDefecto();
                Constantes.infoUsuario.EmailUsuario=email;
                Intent intent=new Intent(this,PinLoginActivity.class);
                startActivity(intent);
                finish();
           }catch (Exception e){
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public void ErrorInternet() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Advertencia").setMessage("Error al ingresar al sistema.Verifique su conexión a internet");
        builder.setPositiveButton("Aceptar",null);
        builder.create().show();
        avi.hide();
        sv.setVisibility(View.VISIBLE);
        email_login_form.setVisibility(View.VISIBLE);

    }

    @Override
    public void ErrorProcedure() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Advertencia").setMessage("Error al ingresar al sistema.");
        builder.setPositiveButton("Aceptar",null);
        builder.create().show();
        avi.hide();
        sv.setVisibility(View.VISIBLE);
        email_login_form.setVisibility(View.VISIBLE);

    }

    @Override
    public void UserNoExists() {
         AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Advertencia").setMessage("El usuario o la contraseña no son correctas.");
        builder.setPositiveButton("Aceptar",null);
        builder.create().show();
        avi.hide();
        sv.setVisibility(View.VISIBLE);
        email_login_form.setVisibility(View.VISIBLE);

    }

    @Override
    public void FinDemo() {
        email_login_form.setVisibility(View.VISIBLE);
        avi.hide();
        sv.setVisibility(View.VISIBLE);
        MensajeAlerta("Advertencia","El periodo de uso ha finalizado");
    }

    public void MensajeAlerta(String titulo,String mensaje){

        new AlertDialog.Builder(this).setMessage(mensaje).setTitle(titulo).setPositiveButton("Salir",null).create().show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnCrearCuenta){
            RegistroCuenta();
        }
    }

    private void RegistroCuenta(){

        Intent intent=new Intent(this,RegistroUsuario.class);
        startActivity(intent);
        // startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {

            } else {
                int id = data.getIntExtra("RESULTADOID",200);
            }
        }

    }

    @Override
    public void LogExito() {

        SesionUsuario s;
        InfoSesionTemp p;
        try {
            s=new SesionUsuario();

            p=s.ObtenerInfoSesionTemp();
            if(p.getPermitirAcceso()){
                AccederPin();
            }else {

                avi.hide();
                dbHelper.InsertOptionUserRegister(email, contrasena);
                //.SimboloMonedaPorDefecto();
                Constantes.infoUsuario.EmailUsuario = email;
                Intent intent = new Intent(this, PinLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void AccederPin(){


          //  avi.smoothToShow();
            /*  asyncUsers.VerificarPinUsuario(pin,myIMEI, Build.BRAND, Build.MODEL, Build.VERSION.RELEASE);*/
            asyncLogUser.LogPin("0000",myIMEI,Build.BRAND,Build.MODEL, Build.VERSION.RELEASE,p.getUser(),p.getPermitirAcceso());
            asyncLogUser.setResultLogPin(this);
        //    permitirEscribir=false;
        }

    @Override
    public void LogError(@NotNull String mensaje) {

        MensajeAlerta("Aviso",mensaje);
        email_login_form.setVisibility(View.VISIBLE);
        avi.hide();
        sv.setVisibility(View.VISIBLE);

    }

    @Override
    public void PinLogExito() {

        s.ReiniciarConteoUsuario();
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
     //   permitirEscribir=true;
        Intent intent=new Intent(this,PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void PinLogError(@NotNull String mensaje) {

    }

private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;


    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */


}

