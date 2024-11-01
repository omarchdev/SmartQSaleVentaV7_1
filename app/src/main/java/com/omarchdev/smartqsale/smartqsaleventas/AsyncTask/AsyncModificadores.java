package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.cProgressDialog;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleModificador;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Modificador;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IModificadorRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by OMAR CHH on 20/04/2018.
 */

public class AsyncModificadores  {


    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    ListenerConfigProdMod listenerConfigProdMod;
    Context context;
    ObtenerConfiguracionModificadorProducto obtenerConfiguracionModificadorProducto;
    AgregarModificadorProducto agregarModificadorProducto;
    EliminarModificadorProducto eliminarModificadorProducto;
    ActualizarEstadoModificador actualizarEstadoModificador;
    BuscarModificadores buscarModificadores;
    Dialog dialog;
    cProgressDialog progressDialog;
    ListenerModProductoVenta listenerModProductoVenta;
    DescargaModificadoresProductoVenta descargaModificadoresProductoVenta;
    ListenerModificadoresConfig listenerModificadoresConfig;
    AgregarModificador agregarModificador;
    AgregarValorModificador agregarValorModificador;
    ObtenerModificadoresCompany obtenerModificadoresCompany;
    EliminarModificador eliminarModificador;
    EditarModificador editarModificador;
    EditarValorModificador editarValorModificador;
    EliminarValorModificador eliminarValorModificador;


    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IModificadorRepository iModificadorRepository= retro.create(IModificadorRepository.class);
    String ciaCode=GetJsonCiaTiendaBase64x3();



    public interface ListenerModificadoresConfig{

            public void ObtenerModificadores(List<Modificador> modificadorList);
            public void ResultadoGuardarModificador(Modificador modificador);
            public void ResultadoGuardarDetalleMod(DetalleModificador detalleModificador);
            public void ResultadoEliminarMod(Modificador modificador);
            public void ResultadoModificarMod(Modificador modificador);
            public void ResultadoEditarValorMod(DetalleModificador detalleModificador);
            public void ResultadoEliminarValorMod(DetalleModificador detalleModificador);
    }

    public void setListenerModificadoresConfig(ListenerModificadoresConfig listenerModificadoresConfig){
        this.listenerModificadoresConfig=listenerModificadoresConfig;
    }

    public AsyncModificadores() {
        progressDialog=new cProgressDialog();
        descargaModificadoresProductoVenta= new DescargaModificadoresProductoVenta();
        agregarModificadorProducto=new AgregarModificadorProducto();
        eliminarModificadorProducto=new EliminarModificadorProducto();
        obtenerConfiguracionModificadorProducto=new ObtenerConfiguracionModificadorProducto();
        buscarModificadores=new BuscarModificadores();
        actualizarEstadoModificador=new ActualizarEstadoModificador();


    }

    public interface ListenerModProductoVenta{

        public void ResultadosModificadoresProducto(List<Modificador> modificadorList);

    }
    public void setListenerModProductoVenta(ListenerModProductoVenta listenerModProductoVenta){

        this.listenerModProductoVenta=listenerModProductoVenta;
    }
    public interface ListenerConfigProdMod{

        public void getConfigProductMod(boolean estado, List<Modificador> modificadores,List<Modificador> modificadoresProducto);
        public void ResultadoIngresarModificadorProducto(Modificador modificador);
        public void ResultadoEliminarModificadorProducto(byte resultado);
        public void ResultadoActEstadoModProd(byte resultado);
        public void ResultadoBusquedaModificador(List<Modificador> modificadorList);
    }

    public void setContext(Context context){
        this.context=context;
    }
    public void setListenerConfigProdMod(ListenerConfigProdMod listenerConfigProdMod){

        this.listenerConfigProdMod=listenerConfigProdMod;

    }

    public void ObtenerConfiguracionModificadoresProducto(int idProduct){
        obtenerConfiguracionModificadorProducto=new ObtenerConfiguracionModificadorProducto();
        obtenerConfiguracionModificadorProducto.execute(idProduct);

    }

    public void EliminarModProducto(int idModProduct,int idProducto){

        eliminarModificadorProducto=new EliminarModificadorProducto();
        eliminarModificadorProducto.execute(idModProduct,idProducto);


    }

    public void BusquedaModificadores(String parametro){

        buscarModificadores=new BuscarModificadores();
        buscarModificadores.execute(parametro);

    }
    public void ActualizarEstadoModProduct(int idProducto,boolean estado){

      actualizarEstadoModificador=new ActualizarEstadoModificador();

        actualizarEstadoModificador.setIdProducto(idProducto);
        actualizarEstadoModificador.setEstado(estado);
        actualizarEstadoModificador.execute(estado);

    }
    public void AgregarModProducto(int idProducto,int idModificador){

        agregarModificadorProducto=new AgregarModificadorProducto();
        agregarModificadorProducto.execute(idProducto,idModificador);
    }

    public void ObtenerModificadoresProductosVenta(int idProducto){

        descargaModificadoresProductoVenta.execute(idProducto);
    }

    public void CancelarBusquedaModificadoresProducto(){

        if(descargaModificadoresProductoVenta!=null){
            descargaModificadoresProductoVenta.cancel(true);
        }

    }

    public void ObtenerModificadores(){
        obtenerModificadoresCompany=new ObtenerModificadoresCompany();
        obtenerModificadoresCompany.execute();
    }

    public void CancelarObtenerMod(){
        if(obtenerModificadoresCompany!=null){
            obtenerModificadoresCompany.cancel(true);
        }
    }

    public void IngresarModificadores(String modificador){

        agregarModificador=new AgregarModificador();
        agregarModificador.execute(modificador);

    }

    public void CancelarIngregarModificador(){

        if(agregarModificador!=null){

            agregarModificador.cancel(true);

        }

    }

    public void PEliminarModificador(int idModificador){
        eliminarModificador=new EliminarModificador();
        eliminarModificador.execute(idModificador);
    }
    public void AgregarValorMod(int idModificador,DetalleModificador detalleModificador){

        agregarValorModificador=new AgregarValorModificador();
        agregarValorModificador.setIdModificador(idModificador);
        agregarValorModificador.setDescripcionValor(detalleModificador);
        agregarValorModificador.execute();

    }

    public void EditarModificador(int idModificador,String descripcion){

        editarModificador=new EditarModificador();
        editarModificador.setIdModificador(idModificador);
        editarModificador.setDescripcion(descripcion);
        editarModificador.execute();

    }

    public void EditarValorMod(int idMod,int idDMod,DetalleModificador detalleModificador){

        editarValorModificador=new EditarValorModificador();
        editarValorModificador.setIdModificador(idMod);
        editarValorModificador.setIdDetalleModificador(idDMod);
        editarValorModificador.setDetalleModificador(detalleModificador);
        editarValorModificador.execute();

    }

    public void EliminarValorMod(int idMod,int idDMod){
        eliminarValorModificador=new EliminarValorModificador();
        eliminarValorModificador.execute(idMod,idDMod);
    }

    private class EliminarValorModificador extends  AsyncTask<Integer,Void,DetalleModificador>{

        @Override
        protected DetalleModificador doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarDetallaModificador(integers[0],integers[1]);
        }

        @Override
        protected void onPostExecute(DetalleModificador detalleModificador) {
            super.onPostExecute(detalleModificador);
            listenerModificadoresConfig.ResultadoEliminarValorMod(detalleModificador);
        }
    }

    private class EditarValorModificador extends  AsyncTask<Void,Void,DetalleModificador>{

        int idModificador;
        int idDetalleModificador;
        DetalleModificador detalleModificador;

        public void setIdModificador(int idModificador) {
            this.idModificador = idModificador;
        }

        public void setIdDetalleModificador(int idDetalleModificador) {
            this.idDetalleModificador = idDetalleModificador;
        }

        public void setDetalleModificador(DetalleModificador detalleModificador) {
            this.detalleModificador = detalleModificador;
        }

        @Override
        protected DetalleModificador doInBackground(Void... voids) {
            return bdConnectionSql.EditarDetalleModificador(idModificador,idDetalleModificador,detalleModificador);
        }

        @Override
        protected void onPostExecute(DetalleModificador detalleModificador) {
            super.onPostExecute(detalleModificador);
            listenerModificadoresConfig.ResultadoEditarValorMod(detalleModificador);
        }
    }

    private class ObtenerModificadoresCompany extends  AsyncTask<Void,Void,List<Modificador>>{

        @Override
        protected List<Modificador> doInBackground(Void... voids) {
            return bdConnectionSql.obtenerModificadoresCompany();
        }

        @Override
        protected void onPostExecute(List<Modificador> modificadorList) {
            super.onPostExecute(modificadorList);
            listenerModificadoresConfig.ObtenerModificadores(modificadorList);
        }
    }

    private class EliminarModificadorProducto extends  AsyncTask<Integer,Void,Byte>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarModificadorProducto(integers[0],integers[1]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            listenerConfigProdMod.ResultadoEliminarModificadorProducto(aByte);
        }
    }
    private class ObtenerConfiguracionModificadorProducto extends AsyncTask<Integer,Void,List<BdConnectionSql.ProductoModificador>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected List<BdConnectionSql.ProductoModificador> doInBackground(Integer... integers) {


            return bdConnectionSql.obtenerCongifModificadorProduct(integers[0]);


        }

        @Override
        protected void onPostExecute(List<BdConnectionSql.ProductoModificador> productoModificadors) {

            super.onPostExecute(productoModificadors);
            boolean estado=false;
            List<Modificador> listGeneral=new ArrayList<>();
            List<Modificador> listModProd=new ArrayList<>();
            Modificador modificador;
            for(BdConnectionSql.ProductoModificador productoModificador:productoModificadors){
                if(productoModificador.getTipo()==1){

                     estado=productoModificador.isEstado();

                }
                else if(productoModificador.getTipo()==2){

                    modificador=new Modificador();
                    modificador.setIdModificador(productoModificador.getIdPri());
                    modificador.setDescripcion(productoModificador.getDescripcion());
                    listGeneral.add(modificador);
                    modificador=null;

                }
                else if(productoModificador.getTipo()==3){
                    modificador=new Modificador();
                    modificador.setIdModificadorProducto(productoModificador.getIdPri());
                    modificador.setIdModificador(productoModificador.getIdSec());
                    listModProd.add(modificador);
                    modificador=null;

                }
            }

            if(listModProd.size()>0){
                for(int i=0;i<listModProd.size();i++){
                    for(int a=0;a<listGeneral.size();a++){

                        if(listModProd.get(i).getIdModificador()==listGeneral.get(a).getIdModificador()){

                            listModProd.get(i).setDescripcion(listGeneral.get(a).getDescripcion());

                        }
                    }
                }
            }

            listenerConfigProdMod.getConfigProductMod(estado,listGeneral,listModProd);

        }

    }

    private class BuscarModificadores extends AsyncTask<String,Void,List<Modificador>>{

        @Override
        protected List<Modificador> doInBackground(String... strings) {
            return bdConnectionSql.obtenerModificadores(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Modificador> list) {
            super.onPostExecute(list);
            listenerConfigProdMod.ResultadoBusquedaModificador(list);
        }
    }

    private class AgregarModificadorProducto extends AsyncTask<Integer,Void,Modificador>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=progressDialog.getProgressDialog(context,"Agregando modificador al producto");
            dialog.show();
        }

        @Override
        protected Modificador doInBackground(Integer... integers) {
            return bdConnectionSql.insertarModificador(integers[0],integers[1]);
        }

        @Override
        protected void onPostExecute(Modificador modificador) {
            super.onPostExecute(modificador);
            listenerConfigProdMod.ResultadoIngresarModificadorProducto(modificador);
            dialog.hide();
        }
    }

    private class ActualizarEstadoModificador extends AsyncTask<Boolean,Void,Byte>{

        int idProducto;
        boolean estado;

        public void setEstado(boolean estado) {
            this.estado = estado;
        }

        public void setIdProducto(int idProducto) {
            this.idProducto = idProducto;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(estado){
                dialog=progressDialog.getProgressDialog(context,"Activando los modificadores del producto");
            }
            else{
                dialog=progressDialog.getProgressDialog(context,"Desactivando los modificadores del producto");

            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Byte doInBackground(Boolean... booleans) {
            return bdConnectionSql.ActualizarEstadoModProd(idProducto,estado);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            listenerConfigProdMod.ResultadoActEstadoModProd(aByte);
            dialog.hide();
        }
    }


    private class DescargaModificadoresProductoVenta extends AsyncTask<Integer,Void,List<Modificador>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Modificador> doInBackground(Integer... integers) {
            try {
                List<Modificador> list=iModificadorRepository.ObtenerModificadoresProductoVenta(ciaCode,TIPO_CONSULTA,integers[0]).execute().body();
                return list;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Adv14",e.toString());
                return new ArrayList<>();
            } catch (Exception ex){
                Log.d("Adv14",ex.toString());
                return new ArrayList<>();
            }
          //  return bdConnectionSql.ObtenerModificadoresProductoVenta(integers[0]);
        }
        @Override
        protected void onPostExecute(List<Modificador> modificadorList) {

            super.onPostExecute(modificadorList);

            listenerModProductoVenta.ResultadosModificadoresProducto(modificadorList);

        }
    }

    private class AgregarModificador extends AsyncTask<String,Void,Modificador>{


        @Override
        protected Modificador doInBackground(String... strings) {
            SolicitudEnvio<String> sol=new SolicitudEnvio<String>(ciaCode,TIPO_CONSULTA,strings[0], Constantes.Terminal.idTerminal,Constantes.Usuario.idUsuario);
            try {
                return iModificadorRepository.InsertarModificador(sol).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                Modificador mod=new Modificador();
                mod.setIdModificador(-5);
                return mod;
            } catch (Exception ex){
                Modificador mod=new Modificador();
                mod.setIdModificador(-5);
                return mod;
            }
            //  return bdConnectionSql.InsertarModificador(strings[0]);
        }

        @Override
        protected void onPostExecute(Modificador modificador) {
            super.onPostExecute(modificador);
            listenerModificadoresConfig.ResultadoGuardarModificador(modificador);
        }
    }

    private class AgregarValorModificador extends AsyncTask<Void,Void,DetalleModificador>{

        int idModificador;
        DetalleModificador DescripcionValor;

        public void setIdModificador(int idModificador) {
            this.idModificador = idModificador;
        }

        public void setDescripcionValor(DetalleModificador descripcionValor) {
            DescripcionValor = descripcionValor;
        }

        @Override
        protected DetalleModificador doInBackground(Void... voids) {
            DetalleModificador det=DescripcionValor;
            det.setIdModificador(idModificador);
            SolicitudEnvio<DetalleModificador> sol=
                    new SolicitudEnvio(ciaCode,TIPO_CONSULTA,det, Constantes.Terminal.idTerminal,Constantes.Usuario.idUsuario);
            try {
                return iModificadorRepository.GuardarDetalleModificador(sol).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                DetalleModificador mod=new DetalleModificador();
                mod.setIdDetalleModificador(-5);
                return mod;
            } catch (Exception ex){
                DetalleModificador mod=new DetalleModificador();
                mod.setIdDetalleModificador(-5);
                return mod;
            }
         //   return bdConnectionSql.GuardarDetalleModificador(idModificador,DescripcionValor);
        }

        @Override
        protected void onPostExecute(DetalleModificador detalleModificador) {
            super.onPostExecute(detalleModificador);
            listenerModificadoresConfig.ResultadoGuardarDetalleMod(detalleModificador);
        }
    }
    public class EliminarModificador extends AsyncTask<Integer,Void,Modificador>{


        @Override
        protected Modificador doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarModificador(integers[0]);
        }

        @Override
        protected void onPostExecute(Modificador modificador) {
            super.onPostExecute(modificador);
            listenerModificadoresConfig.ResultadoEliminarMod(modificador);
        }
    }
    public class EditarModificador extends  AsyncTask<Void,Void,Modificador>{

        int idModificador;
        String Descripcion;

        public void setIdModificador(int idModificador) {
            this.idModificador = idModificador;
        }

        public void setDescripcion(String descripcion) {
            Descripcion = descripcion;
        }

        @Override
        protected Modificador doInBackground(Void... voids) {
            return bdConnectionSql.EditarModificador(idModificador,Descripcion);
        }

        @Override
        protected void onPostExecute(Modificador modificador) {
            super.onPostExecute(modificador);
            listenerModificadoresConfig.ResultadoModificarMod(modificador);
        }
    }


    public void cancelarBusqueda(){
        if(buscarModificadores!=null) {
            buscarModificadores.cancel(true);
        }
    }

}
