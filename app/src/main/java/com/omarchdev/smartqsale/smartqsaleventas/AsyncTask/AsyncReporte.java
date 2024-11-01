package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import android.os.AsyncTask;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacenProducto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedorProducto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVentasVendedor;

import java.util.List;

public class AsyncReporte {


    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    ObtenerRVentasVProductVendedor obtenerRVentasVProductVendedor;
    ObtenerVentasVendedor obtenerVentasVendedor;
    ListenerReportePVendedor listenerReportePVendedor;

    public void setListenerReportePVendedor(ListenerReportePVendedor listenerReportePVendedor){
        this.listenerReportePVendedor=listenerReportePVendedor;
    }
    public interface ListenerReportePVendedor{
        public void ErrorResultado();
        public void ResultadoReporte(List<mVendedorProducto> mVendedorProductoList);
    }

    public void ObtenerReporteVendedor(int idVendedor,String fechaIni,String fechaFin,int tipoReporte){
        obtenerRVentasVProductVendedor=new ObtenerRVentasVProductVendedor();
        obtenerRVentasVProductVendedor.setInit(fechaIni);
        obtenerRVentasVProductVendedor.setEnd(fechaFin);
        obtenerRVentasVProductVendedor.execute(idVendedor,tipoReporte);
    }
    private class ObtenerRVentasVProductVendedor extends AsyncTask<Integer,Void,List<mVendedorProducto>>{
        String init;
        String end;
        List<mVendedorProducto>list;
        public void setInit(String init) {
            this.init = init;
        }
        public void setEnd(String end) {
            this.end = end;
        }

        @Override
        protected List<mVendedorProducto> doInBackground(Integer... integers) {
            if(integers[1]==100) {
              //  list = bdConnectionSql.obtenerReporteVendedorVenta(integers[0], init, end);
            }else if(integers[1]==200){
              //  list=bdConnectionSql.obtenerReporteVendedorVentaAcumulado(integers[0],init,end);
            }
           return list;

        }

        @Override
        protected void onPostExecute(List<mVendedorProducto> mVendedorProductos) {
            super.onPostExecute(mVendedorProductos);
            if(listenerReportePVendedor!=null){

                if(mVendedorProductos!=null){
                    listenerReportePVendedor.ResultadoReporte(mVendedorProductos);
                }else{
                    listenerReportePVendedor.ErrorResultado();
                }
            }

        }
    }

    public interface ResultadoVentasVendedor{

        public void ErrorConsulta();
        public void ResultadosConsulta(List<mVentasVendedor> ventasVendedors);

    }
    ResultadoVentasVendedor resultadoVentasVendedor;

    public void setResultadoVentasVendedor(ResultadoVentasVendedor resultadoVentasVendedor){
        this.resultadoVentasVendedor=resultadoVentasVendedor;
    }

    public void ObtenerVentasVendedor(int idVendedor,String desde,String hasta){
        obtenerVentasVendedor=new ObtenerVentasVendedor();
        obtenerVentasVendedor.setDesde(desde);
        obtenerVentasVendedor.setHasta(hasta);
        obtenerVentasVendedor.execute(idVendedor);
    }

    private class ObtenerVentasVendedor extends AsyncTask<Integer,Void,List<mVentasVendedor>>{

        String desde;
        String hasta;
        public void setDesde(String desde) {
            this.desde = desde;
        }
        public void setHasta(String hasta) {
            this.hasta = hasta;
        }
        @Override
        protected List<mVentasVendedor> doInBackground(Integer... integers) {
            return bdConnectionSql.obtenerVentasPorVendedor(integers[0],desde,hasta);
        }

        @Override
        protected void onPostExecute(List<mVentasVendedor> mVentasVendedors) {
            super.onPostExecute(mVentasVendedors);
            if(resultadoVentasVendedor!=null){
                if(mVentasVendedors!=null){
                    resultadoVentasVendedor.ResultadosConsulta(mVentasVendedors);
                }else{
                    resultadoVentasVendedor.ErrorConsulta();
                }
            }
        }
    }

    ListenerVentasPorCierre listenerVentasPorCierre;

    public void setListenerVentasPorCierre(ListenerVentasPorCierre listenerVentasPorCierre){
        this.listenerVentasPorCierre=listenerVentasPorCierre;
    }
    public interface ListenerVentasPorCierre{
        public void ErrorConsulta();
        public void ResultadoVentasPorCierre(List<mVentasVendedor> listaResultado,mCierre cierre);
    }
    public void ObtenerVentasPorCierre(int idCierre){
        new ObtenerVentasCierre().execute(idCierre);
    }
    public void ObtenerCabeceraCierre(int idCierre){
        new ObtenerCabeceraCierre().execute(idCierre);
    }
    private class   ObtenerCabeceraCierre extends AsyncTask<Integer,Void,mCierre>{
        @Override
        protected mCierre doInBackground(Integer... integers) {
            return bdConnectionSql.getCabeceraCierreCaja(integers[0]);
        }
        @Override
        protected void onPostExecute(mCierre mCierre) {
            super.onPostExecute(mCierre);
            if(listenerVentasPorCierre!=null){
                if(mCierre!=null){
                    listenerVentasPorCierre.ResultadoVentasPorCierre(null,mCierre);
                }else{
                    listenerVentasPorCierre.ErrorConsulta();

                }
            }
        }
    }
    private class ObtenerVentasCierre extends AsyncTask<Integer,Void,List<mVentasVendedor>>{

        mCierre cierre=null;
        @Override
        protected List<mVentasVendedor> doInBackground(Integer... integers) {
            cierre=bdConnectionSql.getCabeceraCierreCaja(integers[0]);
            return bdConnectionSql.obtenerVentasPorCierre(integers[0]);
        }

        @Override
        protected void onPostExecute(List<mVentasVendedor> ventasVendedors) {
            super.onPostExecute(ventasVendedors);
            if(listenerVentasPorCierre!=null){
                if(ventasVendedors!=null){
                    listenerVentasPorCierre.ResultadoVentasPorCierre(ventasVendedors,cierre);
                }else{
                    listenerVentasPorCierre.ErrorConsulta();

                }
            }
        }
    }

    public void ObtenerAcumuladoVentasCierre(int idCierre, int idVendedor){

        new ObtenerVentasAcumuladoCierre().execute(idCierre,idVendedor);

    }

    ListenerReporteDetalleVentaCierre listenerReporteDetalleVentaCierre;

    public void setListenerReporteDetalleVentaCierre(ListenerReporteDetalleVentaCierre listenerReporteDetalleVentaCierre){
        this.listenerReporteDetalleVentaCierre=listenerReporteDetalleVentaCierre;
    }

    public interface ListenerReporteDetalleVentaCierre{

        public void ResultadoReporteCierre(List<mVendedorProducto> listaReporte);
        public void ErrorConsultaReporte();
    }
    private class ObtenerVentasAcumuladoCierre extends AsyncTask<Integer,Void,List<mVendedorProducto>>{

        @Override
        protected List<mVendedorProducto> doInBackground(Integer... integers) {
            return bdConnectionSql.obtenerAcumuladoVentasCierre(integers[0],integers[1]);
        }
        @Override
        protected void onPostExecute(List<mVendedorProducto> mVendedorProductoList) {
            super.onPostExecute(mVendedorProductoList);

            if(listenerReporteDetalleVentaCierre!=null){

                if(mVendedorProductoList!=null){

                    listenerReporteDetalleVentaCierre.ResultadoReporteCierre(mVendedorProductoList);
                }else{
                    listenerReporteDetalleVentaCierre.ErrorConsultaReporte();
                }

            }

        }
    }




    public void ObtenerDetalleVentasCierre(int idCierre, int iVendedor){

        new ObtenerDetalleVentasCierre().execute(idCierre,iVendedor);

    }

    private class ObtenerDetalleVentasCierre extends AsyncTask<Integer,Void,List<mVendedorProducto>>{

        @Override
        protected List<mVendedorProducto> doInBackground(Integer... integers) {
            return bdConnectionSql.obtenerDetalleVentaCierre(integers[0],integers[1]);
        }

        @Override
        protected void onPostExecute(List<mVendedorProducto> mVendedorProductoList) {
            super.onPostExecute(mVendedorProductoList);

            if(listenerReporteDetalleVentaCierre!=null){
                if(mVendedorProductoList!=null){

                    listenerReporteDetalleVentaCierre.ResultadoReporteCierre(mVendedorProductoList);

                }else{
                    listenerReporteDetalleVentaCierre.ErrorConsultaReporte();
                }
            }
        }
    }


    ReporteAlmacen reporteAlmacen;

    public void setReporteAlmacen(ReporteAlmacen reporteAlmacen){
        this.reporteAlmacen=reporteAlmacen;
    }
    public interface ReporteAlmacen{

        public void ObtenerReporteAlmacen(List<mAlmacenProducto> listaReporte);
        public void ErrorObtenerAlmacen();

    }

    public void ObtenerReporteProductosAlmacen(int idAlmacen){
        new ObtenerReporteProductosAlmacen().execute(idAlmacen);
    }

    private class ObtenerReporteProductosAlmacen extends AsyncTask<Integer,Void,List<mAlmacenProducto>>{

        @Override
        protected List<mAlmacenProducto> doInBackground(Integer... integers) {
            return bdConnectionSql.ObtenerProductosAlmacen(integers[0]);
        }

        @Override
        protected void onPostExecute(List<mAlmacenProducto> mAlmacenProductos) {
            super.onPostExecute(mAlmacenProductos);
            if(reporteAlmacen!=null){

                if(mAlmacenProductos!=null){
                    reporteAlmacen.ObtenerReporteAlmacen(mAlmacenProductos);
                }else{
                    reporteAlmacen.ErrorObtenerAlmacen();
                }

            }
        }
    }

}
