package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultRegisterUser;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipoTienda;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario;

import java.util.List;

/**
 * Created by OMAR CHH on 09/05/2018.
 */

public class AsyncRegistroUsuario {


    ObtenerSegmento obtenerSegmento;
    ListenerSegmentos listenerSegmentos;
    BdConnectionSql bdConnectionSql;
    RegistroUsuario registroUsuario;
    Context context;
    ControladorProcesoCargar c;

    public void setiEstadoRegistroUsuario(IEstadoRegistroUsuario iEstadoRegistroUsuario) {
        this.iEstadoRegistroUsuario = iEstadoRegistroUsuario;
    }

    IEstadoRegistroUsuario iEstadoRegistroUsuario;

    public void setListenerSegmentos(ListenerSegmentos listenerSegmentos){

        this.listenerSegmentos=listenerSegmentos;

    }
    public void setContext(Context context){
        this.context=context;
        c=new ControladorProcesoCargar(context);
    }

    public interface ListenerSegmentos{

        public void ResultadoSegmentos(List<mTipoTienda> tipoTiendas);
        public void ResultadoRegistrarUsuario(String respuesta);

    }

    public AsyncRegistroUsuario() {

        obtenerSegmento=new ObtenerSegmento();
        bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    }


    public void CancelarObtenerSegmentos(){
        if(obtenerSegmento!=null){
            obtenerSegmento.cancel(true);
        }
    }
    public void ObtenerSegmentos(){
          obtenerSegmento.execute();



    }
    public class ObtenerSegmento extends AsyncTask<Void,Void,List<mTipoTienda>> {

        @Override
        protected List<mTipoTienda> doInBackground(Void... voids) {
            return bdConnectionSql.ObtenerTiposTienda();
        }

        @Override
        protected void onPostExecute(List<mTipoTienda> mTipoTiendas) {
            super.onPostExecute(mTipoTiendas);
            listenerSegmentos.ResultadoSegmentos(mTipoTiendas);
        }
    }

    public void RegistrarUsuario(mUsuario usuario){
      /*  registroUsuario=new RegistroUsuario();
        registroUsuario.execute(usuario);*/
        new RegistroUsuarioV2().execute(usuario);
    }

    public interface IEstadoRegistroUsuario{

        public void RegistroExitoso();
        public void ErrorRegistro(String mensaje);
    }

    public class RegistroUsuarioV2 extends AsyncTask<mUsuario,Void, ResultRegisterUser>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            c.IniciarDialogCarga("Registrando nueva compañia");
        }

        @Override
        protected ResultRegisterUser doInBackground(mUsuario... mUsuarios) {
            return bdConnectionSql.RegistrarUsuarioV2(mUsuarios[0]);
        }

        @Override
        protected void onPostExecute(ResultRegisterUser resultRegisterUser) {
            super.onPostExecute(resultRegisterUser);
            c.FinalizarDialogCarga();
            switch (resultRegisterUser.getCode()){
                case 200:
                    iEstadoRegistroUsuario.RegistroExitoso();
                    break;

                default:
                    iEstadoRegistroUsuario.ErrorRegistro(resultRegisterUser.getMessage());
                    break;

            }
        }
    }
    public class RegistroUsuario extends AsyncTask<mUsuario,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            c.IniciarDialogCarga("Registrando nueva compañia");
        }

        @Override
        protected String doInBackground(mUsuario... mUsuarios) {
            return bdConnectionSql.RegistrarUsuario(mUsuarios[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            c.FinalizarDialogCarga();
            listenerSegmentos.ResultadoRegistrarUsuario(s);
        }
    }
}
