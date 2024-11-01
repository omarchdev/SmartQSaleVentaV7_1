package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import android.os.AsyncTask;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorMediosPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipo_Pago;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by OMAR CHH on 05/05/2018.
 */

public class AsyncMedioPago {


    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    ListenerMedioPago listenerMedioPago;
     ObtenerMedioPagos obtenerMedioPagos;
     ListenerConfigMedioPago listenerConfigMedioPago;
    ObtenerTipoPago obtenerTipoPago;
    GuardarMedioPago guardarMedioPago;
    EliminarMedioPago eliminarMedioPago;

     public interface ListenerConfigMedioPago{

         public void ResultadoGuardarMedioPago(byte resultado);
         public void ResultadoEliminarMedioPago(byte resultado);
         public void ObtenerTiposPago(List<mTipo_Pago> tipoPagoList);

     }

     public void setListenerConfigMedioPago(ListenerConfigMedioPago listenerConfigMedioPago){
         this.listenerConfigMedioPago=listenerConfigMedioPago;
     }

    public interface ListenerMedioPago{

        public void ResultadoListaMedioPagos(List<mMedioPago> medioPagoList);

    }
    public AsyncMedioPago() {
    }

    public void setListenerMedioPago(ListenerMedioPago listenerMedioPago){
        this.listenerMedioPago=listenerMedioPago;
    }

    public void ObtenerMediosPago(){

        obtenerMedioPagos=new ObtenerMedioPagos();
        obtenerMedioPagos.execute();

    }

    public void EliminarMedioPago(int idMedioPago){

        eliminarMedioPago=new EliminarMedioPago();
        eliminarMedioPago.execute(idMedioPago);

    }

    private class EliminarMedioPago extends AsyncTask<Integer,Void,Byte>{

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.eliminarMedioPago(integers[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);

            if(listenerConfigMedioPago!=null) {
            listenerConfigMedioPago.ResultadoEliminarMedioPago(aByte);
            }

        }
    }
    private class ObtenerMedioPagos extends AsyncTask<Void,Void,List<mMedioPago>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<mMedioPago> doInBackground(Void... voids) {
            return
                    new ControladorMediosPago().GetMediosPago();
        }

        @Override
        protected void onPostExecute(List<mMedioPago> mMedioPagos) {
            super.onPostExecute(mMedioPagos);
            if(listenerMedioPago!=null) {
                listenerMedioPago.ResultadoListaMedioPagos(mMedioPagos);
            }
        }
    }

    public void ObtenerTipoPago(){

        obtenerTipoPago=new ObtenerTipoPago();
        obtenerTipoPago.execute();

    }

    private class ObtenerTipoPago extends AsyncTask<Void,Void,List<mTipo_Pago>>{

        @Override
        protected List<mTipo_Pago> doInBackground(Void... voids) {
            return bdConnectionSql.getTipoPago();
        }

        @Override
        protected void onPostExecute(List<mTipo_Pago> mTipo_pagos) {
            super.onPostExecute(mTipo_pagos);
            if(listenerConfigMedioPago!=null){

            listenerConfigMedioPago.ObtenerTiposPago(mTipo_pagos);

            }
        }
    }

    public void GuardarMedioPago(int idMedioPago,String codigo,String descripcion,int idTipo,BigDecimal valorMinimo,String nombreImagen){

        guardarMedioPago=new GuardarMedioPago();
        guardarMedioPago.setIdMedio(idMedioPago);
        guardarMedioPago.setCodigo(codigo);
        guardarMedioPago.setIdTipo(idTipo);
        guardarMedioPago.setDescripcion(descripcion);
        guardarMedioPago.setValorMinimo(valorMinimo);
        guardarMedioPago.setNombreImagen(nombreImagen);
        guardarMedioPago.execute();

    }
    private class GuardarMedioPago extends  AsyncTask<Void,Void,Byte>{
        int idMedio;
        String codigo;
        String descripcion;
        int idTipo;
        BigDecimal valorMinimo;
        String nombreImagen;
        byte respuesta;

        public GuardarMedioPago() {
            respuesta=98;
        }

        public void setIdMedio(int idMedio) {
            this.idMedio = idMedio;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public void setIdTipo(int idTipo) {
            this.idTipo = idTipo;
        }

        public void setValorMinimo(BigDecimal valorMinimo) {
            this.valorMinimo = valorMinimo;
        }

        public void setNombreImagen(String nombreImagen) {
            this.nombreImagen = nombreImagen;
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            if(idMedio==0){
                respuesta=bdConnectionSql.guardarMedioPago(codigo,descripcion,idTipo,valorMinimo,nombreImagen);
            }
            else if(idMedio!=0){
                respuesta=bdConnectionSql.editarMedioPago(idMedio,codigo,descripcion,idTipo,valorMinimo,nombreImagen);
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(listenerConfigMedioPago!=null) {
                listenerConfigMedioPago.ResultadoGuardarMedioPago(aByte);
            }

        }
    }

}
