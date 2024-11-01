package com.omarchdev.smartqsale.smartqsaleventas.Controlador;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.Activitys.PantallaPrincipal;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;

/**
 * Created by OMAR CHH on 04/10/2017.
 */

public class ControladorLogin extends AsyncTask<String,Void,String>  {
    Context context;
    ProgressBar progressBar;
    BdConnectionSql bdConnectionSql=new BdConnectionSql();
    DbHelper helper;
    String user;
    String password;


    public ControladorLogin(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar=progressBar;
        helper=new DbHelper(context);
        user="";
        password="";

    }

       @Override
        protected void onPreExecute() {
            super.onPreExecute();

           progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            user=params[0];
            password=params[1];
            return bdConnectionSql.checkLogBdUser(user,password);
        }
       @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if(s.equals(Constantes.AccesUser.messageAccesOk)) {

                Toast.makeText(context, "El Usuario tiene permitido Ingresar", Toast.LENGTH_LONG).show();
                IngresarUsuarioBaseDatoInterna();
                mostrarDatosUsuario();
                IngresarAPantallaPrincipal();

            }
            else if(s.equals(Constantes.AccesUser.messageAccesDenied)){
                Toast.makeText(context, "Error en datos", Toast.LENGTH_LONG).show();
            }
        }

        private void IngresarAPantallaPrincipal(){
            Intent intent=new Intent(context, PantallaPrincipal.class);
            context.startActivity(intent);
        }

        private long IngresarUsuarioBaseDatoInterna(){
            return helper.insertUser(user,password);
        }
        private void mostrarDatosUsuario(){
            String nombre="";
            String contrasenia;


           Cursor c= helper.checkExistUserInDb();
            int a= c.getCount();
            Toast.makeText(context, String.valueOf(a), Toast.LENGTH_SHORT).show();
            if(c.moveToFirst());
            do{
                nombre=c.getString(0);
                contrasenia=c.getString(1);
            }while(c.moveToNext());


        }
        private void eliminarDatoUsuario(){

            helper.deleteUser();
        }

}
