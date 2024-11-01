package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.webkit.WebView;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.Calendar;

public class LoginActivity extends ActivityParent {

    final Calendar c = Calendar.getInstance();
    WebView mWebView;
    BdConnectionSql bdConnectionSql;
    String myIMEI = "";
    Dialog dialog;
    byte respuesta = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bdConnectionSql = BdConnectionSql.getSinglentonInstance();
        myIMEI = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        bdConnectionSql.GetStringConnectionStart();
        new loguinUser().execute();


    }

    private void LoadLog() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Cargando datos");
        progressDialog.setCanceledOnTouchOutside(false);
        dialog = progressDialog;
        dialog.show();

    }

    private void OpenApp() {
        Intent intent = new Intent(this, PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }

    class loguinUser extends AsyncTask<Void, Void, Boolean> {
        boolean resp = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadLog();
            myIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            respuesta = bdConnectionSql.SimboloMonedaPorDefecto();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean.equals(true)) {
                dialog.dismiss();
                OpenApp();
            } else if (aBoolean.equals(false)) {
                dialog.dismiss();
            }

        }

    }
}
