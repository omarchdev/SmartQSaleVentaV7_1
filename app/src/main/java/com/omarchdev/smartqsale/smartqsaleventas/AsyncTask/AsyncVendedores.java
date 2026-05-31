package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.wang.avi.AVLoadingIndicatorView;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVendedorRepository;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsyncVendedores {

    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    Context context;
    ControladorProcesoCargar controladorProcesoCargar;
    ListenerVendedores listenerVendedores;
    ObtenerVendedores obtenerVendedores;
    ListenerRegistroVendedor listenerRegistroVendedor;
    GuardarVendedor guardarVendedor;
    ObtenerVendedorId obtenerVendedorId;
    EliminarVendedor eliminarVendedor;

    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API).client(Constantes.ConfiRetrofitTimeOut.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IVendedorRepository iVendedorRepository = retro.create(IVendedorRepository.class);
    final String codeCia = GetJsonCiaTiendaBase64x3();

    public void setListenerRegistroVendedor(ListenerRegistroVendedor listenerRegistroVendedor){


        this.listenerRegistroVendedor=listenerRegistroVendedor;

    }

    public interface ListenerVendedores{

        public void ObtenerVendedores(List<mVendedor> vendedors);
        public void ErrorObtener();

    }


    public interface ListenerRegistroVendedor{

        public void ProcesoExitoso();
        public void ErrorConnection();
        public void ErrorSql();
        public void ObtenerVendedorId(mVendedor vendedor);

    }


    public void setListenerVendedores(ListenerVendedores listenerVendedores){

        this.listenerVendedores=listenerVendedores;

    }



    public AsyncVendedores(){


    }

    public void setContext(Context context){
        this.context=context;
    }

    public void setContextLoading(Context context, AVLoadingIndicatorView avi, TextView txt, RelativeLayout rl, RecyclerView rv){

        this.context=context;
        if(context!=null) {
            controladorProcesoCargar = new ControladorProcesoCargar(context, avi, txt, rl,rv);

        }
    }


    public void ObtenerVendedores(){

        obtenerVendedores=new ObtenerVendedores();
        obtenerVendedores.execute();

    }

    public void CancelarObtenerVendedores(){

        if(obtenerVendedores!=null){
            obtenerVendedores.cancel(true);
        }

    }
    private class ObtenerVendedores extends AsyncTask<Void,Void,List<mVendedor>>{

        int id;
        String parametro;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            iniciarCarga();
        }

        @Override
        protected List<mVendedor> doInBackground(Void... voids) {

            return bdConnectionSql.getVendedor(0, "", Constantes.ParametrosVendedor.TodosVendedores);
        }

        @Override
        protected void onPostExecute(List<mVendedor> mVendedors) {
            super.onPostExecute(mVendedors);

            finalizarCarga();
            if(listenerVendedores!=null){
                if(mVendedors!=null) {
                    listenerVendedores.ObtenerVendedores(mVendedors);
                }else{
                    listenerVendedores.ErrorObtener();
                }
            }
        }
    }

    private void iniciarCarga(){
        if(context!=null) {
            controladorProcesoCargar.CargaInicio();
        }
    }

    private void finalizarCarga(){
        if(context!=null){
            controladorProcesoCargar.CargaFinal();
        }

    }

    public void ObtenerVendedorId(int idVendedor){

        obtenerVendedorId=new ObtenerVendedorId();
        obtenerVendedorId.execute(idVendedor);


    }



    private class ObtenerVendedorId extends AsyncTask<Integer,Void,mVendedor>{

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
             iniciarCarga();
        }

        @Override
        protected mVendedor doInBackground(Integer... integers) {
            try {
                return iVendedorRepository.ObtenerVendedorPorId(integers[0], TIPO_CONSULTA, codeCia).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                mVendedor vendedor = new mVendedor();
                vendedor.setIdVendedor(-99);
                return vendedor;
            } catch (Exception ex) {
                ex.printStackTrace();
                mVendedor vendedor = new mVendedor();
                vendedor.setIdVendedor(-99);
                return vendedor;
            }
        }

        @Override
        protected void onPostExecute(mVendedor vendedor) {
            super.onPostExecute(vendedor);
            finalizarCarga();

            if(listenerRegistroVendedor!=null) {
                if(vendedor.getIdVendedor()>0) {

                    listenerRegistroVendedor.ObtenerVendedorId(vendedor);


                }else if(vendedor.getIdVendedor()==-99){

                    listenerRegistroVendedor.ErrorSql();

                }else if(vendedor.getIdVendedor()==-98){

                    listenerRegistroVendedor.ErrorConnection();
                }

            }


        }
    }

    public void RegistrarVendedor(mVendedor vendedor){

        guardarVendedor=new GuardarVendedor();
        guardarVendedor.setVendedor(vendedor);
        guardarVendedor.execute();

    }



    private class GuardarVendedor extends AsyncTask<Void,Void,Byte>{

        mVendedor vendedor;
        byte respuesta=0;
        public void setVendedor(mVendedor vendedor) {
            this.vendedor = vendedor;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(controladorProcesoCargar!=null) {
                controladorProcesoCargar.IniciarDialogCarga("Guardando datos");
            }
        }

        @Override
        protected Byte doInBackground(Void... voids) {

            return bdConnectionSql.RegistroVendedor(vendedor);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);

            if(controladorProcesoCargar!=null){
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if(listenerRegistroVendedor!=null){

                if(aByte==99){
                    listenerRegistroVendedor.ErrorSql();
                }else if(aByte==98){
                    listenerRegistroVendedor.ErrorConnection();


                }else if(aByte==0){
                    listenerRegistroVendedor.ErrorSql();
                }
                else {

                    listenerRegistroVendedor.ProcesoExitoso();

                }
           }
        }

    }

    ListenerEliminarVendedor listenerEliminarVendedor;


    public void setListenerEliminarVendedor(ListenerEliminarVendedor listenerEliminarVendedor){


        this.listenerEliminarVendedor=listenerEliminarVendedor;

    }



    public interface ListenerEliminarVendedor{

        public void EliminarVendedorExito();
        public void EliminarErrorVendedor();


    }

    public void EliminarVendedorId(int idVendedor){
            eliminarVendedor=new EliminarVendedor();
            eliminarVendedor.execute(idVendedor);
    }

    public class EliminarVendedor extends  AsyncTask<Integer,Void,Byte>{

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarVendedor(integers[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(listenerEliminarVendedor!=null){

                if(aByte==100){
                    listenerEliminarVendedor.EliminarVendedorExito();
                }else{
                    listenerEliminarVendedor.EliminarErrorVendedor();
                }

            }
        }
    }


}
