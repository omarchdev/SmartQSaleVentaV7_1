package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ClientePedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DescuentoSol;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.VendedorSol;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository;

import java.math.BigDecimal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsyncCabeceraVenta {


    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    ListenerClienteVenta listenerClienteVenta;
    GuardarClienteCabeceraVenta guardarClienteCabeceraVenta;
    DialogCargaAsync dialogCargaAsync;
    GuardarDescuento guardarDescuento;
    ListenerDescuentoPedido listenerDescuentoPedido;
    Dialog dialog;
    Context context;
    final String codeCia = GetJsonCiaTiendaBase64x3();
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IPedidoRespository iPedidoRespository = retro.create(IPedidoRespository.class);


    public AsyncCabeceraVenta(Context context) {
        this.context = context;
    }

    public void setListenerClienteVenta(ListenerClienteVenta listenerClienteVenta){

        this.listenerClienteVenta=listenerClienteVenta;

    }


    public interface ListenerClienteVenta{

        public void ResultadoGuardarCabeceraVenta(byte respuesta);

    }

    public void GuardarCabeceraVenta(int idCabeceraVenta,mVendedor vendedor,mCustomer customer){

        guardarClienteCabeceraVenta=new GuardarClienteCabeceraVenta();
        guardarClienteCabeceraVenta.setId(idCabeceraVenta);
        guardarClienteCabeceraVenta.setVendedor(vendedor);
        guardarClienteCabeceraVenta.setCustomer(customer);
        guardarClienteCabeceraVenta.execute();

    }

    public void GuardarClienteVenta(int idCabeceraVenta,mCustomer customer){
        GuardarClienteCabecera guardarClienteCabecera=new GuardarClienteCabecera();
        guardarClienteCabecera.setId(idCabeceraVenta);
        guardarClienteCabecera.setCustomer(customer);
        guardarClienteCabecera.execute();
    }

    public void GuardarVendedorVenta(int idCabeceraVenta,mVendedor vendedor){
        GuardarVendedorCabecera guardarVendedorCabecera=new GuardarVendedorCabecera();
        guardarVendedorCabecera.setId(idCabeceraVenta);
        guardarVendedorCabecera.setVendedor(vendedor);
        guardarVendedorCabecera.execute();
    }
    private class GuardarVendedorCabecera extends AsyncTask<Void,Void,Byte>{
        int id;
        mVendedor vendedor;
        public void setId(int id) {
            this.id = id;
        }
        public void setVendedor(mVendedor vendedor) {
            this.vendedor = vendedor;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogCargaAsync=new DialogCargaAsync(context);
            dialog=dialogCargaAsync.getDialogCarga("Guardando vendedor");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        @Override
        protected Byte doInBackground(Void... voids) {
            try{
                VendedorSol vendedorSol=new VendedorSol();
                vendedorSol.setIdCabeceraVenta(id);
                vendedorSol.setVendedor(vendedor);
                SolicitudEnvio<VendedorSol> sol=new SolicitudEnvio<>(codeCia,TIPO_CONSULTA,vendedorSol,Constantes.Terminal.idTerminal,Constantes.Usuario.idUsuario);

                String soldemo=new Gson().toJson(sol);

                return  iPedidoRespository.GuardarVendedorCabeceraPedido(sol).execute().body();
            }catch (Exception ex){
                return 99 ;
            }

        //    return bdConnectionSql.GuardarVendedorCabeceraPedido(id,vendedor);
        }
        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(listenerClienteVenta!=null) {
                listenerClienteVenta.ResultadoGuardarCabeceraVenta(aByte);
            }
            dialog.hide();
        }
    }
    private class GuardarClienteCabecera extends AsyncTask<Void,Void,Byte>{
        int id;
        mCustomer customer;

        public void setId(int id) {
            this.id = id;
        }
        public void setCustomer(mCustomer customer) {
            this.customer = customer;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogCargaAsync=new DialogCargaAsync(context);
            dialog=dialogCargaAsync.getDialogCarga("Guardando cliente");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        @Override
        protected Byte doInBackground(Void... voids) {

            try{
                ClientePedido clientePedido=new ClientePedido();
                clientePedido.setCliente(customer);
                clientePedido.setIdPedido(id);
                SolicitudEnvio<ClientePedido> sol=new SolicitudEnvio<>(codeCia,TIPO_CONSULTA,clientePedido,Constantes.Terminal.idTerminal,Constantes.Usuario.idUsuario);
              //  String de=new Gson().toJson(sol);
                return iPedidoRespository.GuardarClienteCabeceraPedido(sol).execute().body();

            }catch (Exception ex){
                return 99;
            }

        //    return bdConnectionSql.GuardarClienteCabeceraPedido(id,customer);
        }
        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(listenerClienteVenta!=null) {
                listenerClienteVenta.ResultadoGuardarCabeceraVenta(aByte);
            }
            dialog.hide();
        }
    }

    private class GuardarClienteCabeceraVenta extends AsyncTask<Void,Void,Byte>{
        int id;
        mVendedor vendedor;
        mCustomer customer;

        public void setId(int id) {
            this.id = id;
        }

        public void setVendedor(mVendedor vendedor) {
            this.vendedor = vendedor;
        }

        public void setCustomer(mCustomer customer) {
            this.customer = customer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogCargaAsync=new DialogCargaAsync(context);
            dialog=dialogCargaAsync.getDialogCarga("Guardando datos");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            return  bdConnectionSql.GuardarCabeceraPedido(id, vendedor, customer);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(listenerClienteVenta!=null) {
                listenerClienteVenta.ResultadoGuardarCabeceraVenta(aByte);
            }
            dialog.hide();
        }
    }

    public interface ListenerDescuentoPedido{

        public void RespuestaGuardarDescuento(byte respuesta);

    }

    public void setListenerDescuentoPedido(ListenerDescuentoPedido listenerDescuentoPedido){

        this.listenerDescuentoPedido=listenerDescuentoPedido;

    }

    public void GuardarDescuentoPedido(int idCabecera,BigDecimal montoDescuento,byte tipoDescuento){

        guardarDescuento=new GuardarDescuento();
        guardarDescuento.setIdCabecera(idCabecera);
        guardarDescuento.setMontoDescuento(montoDescuento);
        guardarDescuento.setTipoDescuento(tipoDescuento);
        guardarDescuento.execute();

    }


    private class GuardarDescuento extends AsyncTask<Void,Void,Byte>{

        private int idCabecera;
        private BigDecimal montoDescuento;
        private byte tipoDescuento;

        public GuardarDescuento() {
            idCabecera=0;
            montoDescuento=new BigDecimal(0);
            tipoDescuento=0;
        }

        public void setIdCabecera(int idCabecera) {
            this.idCabecera = idCabecera;
        }

        public void setMontoDescuento(BigDecimal montoDescuento) {
            this.montoDescuento = montoDescuento;
        }

        public void setTipoDescuento(byte tipoDescuento) {
            this.tipoDescuento = tipoDescuento;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogCargaAsync=new DialogCargaAsync(context);
            dialog=dialogCargaAsync.getDialogCarga("Aplicando descuento");
            dialog.show();
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            DescuentoSol descuentoSol=new DescuentoSol();
            descuentoSol.setIdCabeceraActual(idCabecera);
            descuentoSol.setValorDescuento(montoDescuento);
            descuentoSol.setTipoDescuento(tipoDescuento);
            SolicitudEnvio<DescuentoSol> sol = new SolicitudEnvio<>(codeCia, TIPO_CONSULTA, descuentoSol, Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            try{
                return iPedidoRespository.GuardarDescuentoPedido(sol).execute().body();

            }catch (Exception ex){
                return 99;
            }

            // return bdConnectionSql.GuardarDescuentoPedido(idCabecera,montoDescuento,tipoDescuento);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            dialog.hide();
            if(listenerDescuentoPedido!=null) {
                listenerDescuentoPedido.RespuestaGuardarDescuento(aByte);
            }
        }
    }

}
