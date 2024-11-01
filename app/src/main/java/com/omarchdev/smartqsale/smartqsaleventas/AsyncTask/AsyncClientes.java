package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorCliente;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IClienteRepository;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

public class AsyncClientes {

    ControladorProcesoCargar controladorProcesoCargar;
    ControladorCliente controladorCliente;
    ListenerClientes listenerClientes;
    ObtenerClientes obtenerClientes;
    ObtenerDatoCliente obtenerDatoCliente;
    Context context;
    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    ObtenerClienteId obtenerClienteId;
    RegistroClientes registroClientes;
    GuardarCliente guardarCliente;
    EliminarClienta eliminarClienta;


    final String codeCia=GetJsonCiaTiendaBase64x3();
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IClienteRepository iClienteRepository= retro.create(IClienteRepository.class);

    public AsyncClientes(){
        controladorCliente=new ControladorCliente();
    }

    public void setObtenerDatoCliente(ObtenerDatoCliente obtenerDatoCliente){
        this.obtenerDatoCliente=obtenerDatoCliente;
    }

    public interface RegistroClientes{

        public void ErrorConnection();
        public void ErrorRegistro();
        public void ActualizarExito();
        public void RegistrarExito();
        public void ExisteCliente();

    }

    public void setRegistroClientes(RegistroClientes registroClientes){
        this.registroClientes=registroClientes;
    }

    public void setListenerClientes(ListenerClientes listenerClientes){

        this.listenerClientes=listenerClientes;

    }

    public void setContext(Context context){
        if(context!=null) {
            this.context = context;
            controladorProcesoCargar = new ControladorProcesoCargar(context);
        }
    }

    public void setScreenContext(Context context, AVLoadingIndicatorView avi, TextView txt, RelativeLayout rl, RecyclerView rv){

        if(context!=null) {
            this.context = context;
            controladorProcesoCargar = new ControladorProcesoCargar(context, avi, txt, rl, rv);

        }

    }

    public interface ObtenerDatoCliente{

        public void ClienteObtenido(mCustomer customer);

    }
    public interface ListenerClientes{

        public void ObtenerListadoClientes(List<mCustomer> customerList);

    }


    public void ObtenerTodosClientes(){

        obtenerClientes=new ObtenerClientes();
        obtenerClientes.execute();


    }

    public void CancelarObtenerClientes(){

        if(obtenerClientes!=null){
            obtenerClientes.cancel(true);
        }


    }

    public void ObtenerClienteId(int idCliente){

        obtenerClienteId=new ObtenerClienteId();
        obtenerClienteId.execute(idCliente);
    }



    public void BuscarClientePorTelefono(String numTelefono){
        new ObtenerClienteNumTelefono().execute(numTelefono);
    }


    private class ObtenerClienteNumTelefono extends  AsyncTask<String,Void,mCustomer>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected mCustomer doInBackground(String... integers) {
            mCustomer customer=new mCustomer();

            try {
                customer=iClienteRepository.GetClienteNumTelefono("2",codeCia,integers[0]).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception ex){
                ex.toString();
            }

            return customer;
        }

        @Override
        protected void onPostExecute(mCustomer customer) {
            super.onPostExecute(customer);
            if(obtenerDatoCliente!=null) {
                obtenerDatoCliente.ClienteObtenido(customer);
            }
        }


    }







    private class ObtenerClienteId extends  AsyncTask<Integer,Void,mCustomer>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected mCustomer doInBackground(Integer... integers) {
            mCustomer customer=new mCustomer();

            try {
                customer=iClienteRepository.GetCliente("2",codeCia,integers[0]).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception ex){
                ex.toString();
            }

            return customer;
        }

        @Override
        protected void onPostExecute(mCustomer customer) {
            super.onPostExecute(customer);
            if(obtenerDatoCliente!=null) {
                obtenerDatoCliente.ClienteObtenido(customer);
            }
        }


    }

    public void GuardarCliente(mCustomer customer){

        guardarCliente=new GuardarCliente();
        guardarCliente.execute(customer);

    }
    public void cancelarRegistro(){

        if(guardarCliente!=null){
            guardarCliente.cancel(true);
        }
    }

    private class GuardarCliente extends AsyncTask<mCustomer,Void,Byte>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(controladorProcesoCargar!=null) {
                controladorProcesoCargar.IniciarDialogCarga("Guardando datos");
            }
        }

        @Override
        protected Byte doInBackground(mCustomer... mCustomers) {
            SolicitudEnvio<mCustomer> solicitudEnvio=new SolicitudEnvio<>(codeCia,
                    "2",mCustomers[0],
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario);
            Byte result=0;
            try {
                int codeResult=iClienteRepository.EditaCliente(solicitudEnvio).execute().body();
                result=(byte) codeResult;
            } catch (IOException e) {
                e.printStackTrace();
                result=0;
            }catch (Exception ex){
                result=0;
            }
        //    return bdConnectionSql.ClienteInsertEdit(mCustomers[0],"");
            return result;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(controladorProcesoCargar!=null) {
                controladorProcesoCargar.FinalizarDialogCarga();

            }
            if(registroClientes!=null){

                if(aByte==80) {
                    registroClientes.ActualizarExito();
                }
                else if(aByte==70){
                   registroClientes.ExisteCliente();
                }
                else if(aByte==98) {
                   registroClientes.ErrorConnection();
                }
                else if(aByte==60) {
                   registroClientes.RegistrarExito();
                }
                else if(aByte==55 || aByte==99) {
                   registroClientes.ErrorRegistro();
                }
            }
        }
    }

    private class ObtenerClientes extends AsyncTask<Void,Void,List<mCustomer>>{

        @Override
        protected List<mCustomer> doInBackground(Void... voids) {
            return controladorCliente.getAllCliente();
        }

        @Override
        protected void onPostExecute(List<mCustomer> mCustomers) {
            super.onPostExecute(mCustomers);
            if(listenerClientes!=null) {
                listenerClientes.ObtenerListadoClientes(mCustomers);
            }
        }
    }



    ListenerEliminarCliente listenerEliminarCliente;
    public void setListenerEliminarCliente(ListenerEliminarCliente listenerEliminarCliente){

        this.listenerEliminarCliente=listenerEliminarCliente;


    }


    public interface ListenerEliminarCliente{


        public void EliminarClienteExito();

        public void EliminarClienteError();



    }

    public void EliminarClienteId(int idCliente) {
        eliminarClienta=new EliminarClienta();
        eliminarClienta.execute(idCliente);
    }

    private class EliminarClienta extends AsyncTask<Integer,Void,Byte>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(context!=null) {
                controladorProcesoCargar.IniciarDialogCarga("Eliminando Cliente");
            }
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarCliente(integers[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(context!=null){
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if(listenerEliminarCliente!=null){
                if(aByte==100){
                    listenerEliminarCliente.EliminarClienteExito();
                }else{
                    listenerEliminarCliente.EliminarClienteError();
                }

            }
        }
    }

}
