package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.os.AsyncTask;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMovAlmacen;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTransaccionAlmacen;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTransacciones_Almacen;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IAlmacenesRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsyncAlmacenes {

    BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    ListenerAlmacenes listenerAlmacenes;
    BusquedaAlmacenes busquedaAlmacenes;
    ObtenerMovAlmacenes obtenerMovAlmacenes;
    ListenerMovAlmacen listenerMovAlmacen;
    RegistrarMovAlmacen registrarMovAlmacen;
    ListenerRegistroCompra listenerRegistroCompra;
    ObtenerMovAlmacen obtenerMovAlmacen;
    ListenerRecuperarMov listenerRecuperarMov;
    ObtenerTransacciones obtenerTransacciones;
    ObtenerTransferenciasAlmacen obtenerTransferenciasAlmacen;
    ListenerTransferenciasAlmacen listenerTransferenciasAlmacen;
    ObtenerProductosMovimiento obtenerProductosMovimiento;
    CompletrarMovTransferencia completrarMovTransferencia;
    ObtenerTipoTransacciones obtenerTipoTransacciones;
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IAlmacenesRepository iAlmacenesRepository = retro.create(IAlmacenesRepository.class);
    String ciaCode = GetJsonCiaTiendaBase64x3();


    public void setListenerMovAlmacen(ListenerMovAlmacen listenerMovAlmacen) {

        this.listenerMovAlmacen = listenerMovAlmacen;

    }

    public AsyncAlmacenes() {

        iAlmacenesRepository = retro.create(IAlmacenesRepository.class);
    }

    public interface ListenerMovAlmacen {

        public void MovimientosRealizados(List<mMovAlmacen> movAlmacenList);

        public void ErrorObtenerMovimientos();
    }

    public interface ListenerAlmacenes {

        public void ObtenerBusquedaAlmacenes(List<mAlmacen> almacenList);

        public void ErrorConsulta();

    }

    public void setListenerAlmacenes(ListenerAlmacenes listenerAlmacenes) {
        this.listenerAlmacenes = listenerAlmacenes;
    }

    public void ObtenerAlmacenes() {
        busquedaAlmacenes = new BusquedaAlmacenes();
        busquedaAlmacenes.execute();

    }

    private class BusquedaAlmacenes extends AsyncTask<Void, Void, List<mAlmacen>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<mAlmacen> doInBackground(Void... voids) {

            try {
                return iAlmacenesRepository.GetAlmacenes(TIPO_CONSULTA, ciaCode).execute().body();
            } catch (IOException ei) {
                ei.toString();
                return null;
            } catch (Exception ex) {
                ex.toString();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<mAlmacen> almacens) {
            super.onPostExecute(almacens);
            if (listenerAlmacenes != null) {
                if (almacens != null) {
                    listenerAlmacenes.ObtenerBusquedaAlmacenes(almacens);
                } else {
                    listenerAlmacenes.ErrorConsulta();
                }
            }
        }
    }


    public void cancelarMovAlmacen() {

        if (obtenerMovAlmacenes != null) {
            obtenerMovAlmacenes.cancel(true);
        }

    }


    public void ObtenerMovimientosAlmacen(String fechaInicio, String fechaFinal) {

        obtenerMovAlmacenes = new ObtenerMovAlmacenes();

        obtenerMovAlmacenes.execute(fechaInicio, fechaFinal);


    }


    private class ObtenerMovAlmacenes extends AsyncTask<String, Void, List<mMovAlmacen>> {

        @Override
        protected List<mMovAlmacen> doInBackground(String... strings) {

            try {
                return iAlmacenesRepository.ObtenerMovimientosAlmacen(TIPO_CONSULTA, ciaCode, strings[0], strings[1]).execute().body();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<mMovAlmacen> mMovAlmacens) {
            super.onPostExecute(mMovAlmacens);
            if (listenerMovAlmacen != null) {
                if (mMovAlmacens != null) {

                    listenerMovAlmacen.MovimientosRealizados(mMovAlmacens);
                } else if (mMovAlmacens == null) {
                    listenerMovAlmacen.ErrorObtenerMovimientos();
                }
            }
        }
    }

    public void setListenerRegistroCompra(ListenerRegistroCompra listenerRegistroCompra) {

        this.listenerRegistroCompra = listenerRegistroCompra;

    }

    public interface ListenerRegistroCompra {

        public void RegistroProcesarExito();

        public void RegistrogGuardarCompletar();

        public void RegistroProcesarIncompleto();

        public void ErrorProcedimiento();

        public void ErrorConnection();

        public void FaltaStock();

    }

    public void RegistrarMovAlmacen(int idMovAlmacen, String fechaMov,
                                    String fechaGuia,
                                    String fechaCompra,
                                    String numRemision,
                                    String nombreProveedor,
                                    int idAlmacen,
                                    int idAlmacenDestino,
                                    List<mProduct> productList,
                                    String metodoGuardar, String codTransaccion, String estadoMov, boolean MovSalida) {
        registrarMovAlmacen = new RegistrarMovAlmacen();
        registrarMovAlmacen.setIdMovCabecera(idMovAlmacen);
        registrarMovAlmacen.setFechaMov(fechaMov);
        registrarMovAlmacen.setFechaCompra(fechaCompra);
        registrarMovAlmacen.setFechaGuia(fechaGuia);
        registrarMovAlmacen.setIdAlmacenDestino(idAlmacenDestino);
        registrarMovAlmacen.setNumRemision(numRemision);
        registrarMovAlmacen.setNombreProveedor(nombreProveedor);
        registrarMovAlmacen.setIdAlmacen(idAlmacen);
        registrarMovAlmacen.setProductList(productList);
        registrarMovAlmacen.setMetodoGuardar(metodoGuardar);
        registrarMovAlmacen.setCodTransaccion(codTransaccion);
        registrarMovAlmacen.setEstadoMov(estadoMov);
        registrarMovAlmacen.setMovSalida(MovSalida);
        registrarMovAlmacen.execute();
    }

    private class RegistrarMovAlmacen extends AsyncTask<Void, Void, Byte> {

        int idMovCabecera;
        String fechaMov;
        String fechaGuia;
        String fechaCompra;
        String numRemision;
        String nombreProveedor;
        int idAlmacen;
        int idAlmacenDestino;
        List<mProduct> productList;
        String MetodoGuardar;
        String codTransaccion;
        byte respuesta;
        String estadoMov;
        List<mProduct> productosVerificar;
        boolean permitirCantidad;

        boolean MovSalida;

        public void setIdAlmacenDestino(int idAlmacenDestino) {
            this.idAlmacenDestino = idAlmacenDestino;
        }

        public void setEstadoMov(String estadoMov) {
            this.estadoMov = estadoMov;
        }

        public void setIdMovCabecera(int idMovCabecera) {
            this.idMovCabecera = idMovCabecera;
        }

        public void setCodTransaccion(String codTransaccion) {
            this.codTransaccion = codTransaccion;
        }

        public void setMovSalida(boolean movSalida) {
            MovSalida = movSalida;
        }

        public void setFechaMov(String fechaMov) {
            this.fechaMov = fechaMov;
        }

        public void setFechaGuia(String fechaGuia) {
            this.fechaGuia = fechaGuia;
        }

        public void setFechaCompra(String fechaCompra) {
            this.fechaCompra = fechaCompra;
        }

        public void setNumRemision(String numRemision) {
            this.numRemision = numRemision;
        }

        public void setNombreProveedor(String nombreProveedor) {
            this.nombreProveedor = nombreProveedor;
        }

        public void setMetodoGuardar(String metodoGuardar) {
            MetodoGuardar = metodoGuardar;
        }

        public void setIdAlmacen(int idAlmacen) {
            this.idAlmacen = idAlmacen;
        }

        public void setProductList(List<mProduct> productList) {
            this.productList = productList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            respuesta = 0;
            permitirCantidad = true;
        }


        @Override
        protected Byte doInBackground(Void... voids) {

            if (estadoMov.equals("N")) {
                if (MetodoGuardar.equals("P")) {
                    if (MovSalida) {
                        respuesta = bdConnectionSql.VerificarTipoAlmacen(idAlmacen);
                        if (respuesta == 40) {

                            productosVerificar = bdConnectionSql.VerificarStockAlmacen(idAlmacen, productList);
                        } else if (respuesta == 50) {
                            productosVerificar = bdConnectionSql.consultarProductosDisponibles(idAlmacen, productList);

                        }
                        for (int i = 0; i < productosVerificar.size(); i++) {
                            for (int a = 0; a < productList.size(); a++) {
                                if (productosVerificar.get(i).getIdProduct() == productList.get(a).getIdProduct()) {
                                    if (productosVerificar.get(i).getdQuantity() < productList.get(a).getdQuantity()) {
                                        permitirCantidad = false;
                                    }
                                }
                            }
                        }
                    }
                    if (permitirCantidad) {
                        respuesta = bdConnectionSql.GuardarIngresoAlmacenProcesarCompra(fechaMov,
                                fechaGuia, fechaCompra,
                                numRemision, nombreProveedor,
                                idAlmacen, idAlmacenDestino, productList,
                                codTransaccion, MovSalida);
                    } else {
                        respuesta = 50;
                    }
                } else if (MetodoGuardar.equals("A")) {
                    respuesta = bdConnectionSql.GuardarMovAlmacenSinProcesar(fechaMov,
                            fechaGuia, fechaCompra,
                            numRemision, nombreProveedor, idAlmacen, idAlmacenDestino, productList, codTransaccion);
                }
            } else if (estadoMov.equals("E")) {
                if (MetodoGuardar.equals("P")) {
                    if (MovSalida) {
                        respuesta = bdConnectionSql.VerificarTipoAlmacen(idAlmacen);
                        if (respuesta == 50) {
                            productosVerificar = bdConnectionSql.consultarProductosDisponibles(idAlmacen, productList);
                            for (int i = 0; i < productosVerificar.size(); i++) {
                                for (int a = 0; a < productList.size(); a++) {
                                    if (productosVerificar.get(i).getIdProduct() == productList.get(a).getIdProduct()) {
                                        if (productosVerificar.get(i).getdQuantity() < productList.get(a).getdQuantity()) {
                                            permitirCantidad = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (permitirCantidad) {
                        respuesta = bdConnectionSql.EditarMovAlmacenProcesar(idMovCabecera, fechaMov,
                                fechaGuia, fechaCompra, numRemision, nombreProveedor, idAlmacen, idAlmacenDestino, productList,
                                codTransaccion, MovSalida);
                    } else {
                        respuesta = 50;
                    }

                } else if (MetodoGuardar.equals("A")) {
                    respuesta = bdConnectionSql.EditarMovAlmacenSinProcesar(idMovCabecera, fechaMov,
                            fechaGuia, fechaCompra, numRemision, nombreProveedor, idAlmacen, idAlmacenDestino, productList,
                            codTransaccion);
                }
            }

            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (listenerRegistroCompra != null) {
                if (aByte == 102) {
                    listenerRegistroCompra.RegistroProcesarExito();
                } else if (aByte == 101) {
                    listenerRegistroCompra.RegistrogGuardarCompletar();
                } else if (aByte == 100) {
                    listenerRegistroCompra.RegistroProcesarIncompleto();
                } else if (aByte == 99) {
                    listenerRegistroCompra.ErrorProcedimiento();
                } else if (aByte == 98) {
                    listenerRegistroCompra.ErrorConnection();
                } else if (aByte == 50) {

                    listenerRegistroCompra.FaltaStock();

                }
            }


        }
    }

    public void setListenerRecuperarMov(ListenerRecuperarMov listenerRecuperarMov) {
        this.listenerRecuperarMov = listenerRecuperarMov;
    }

    public interface ListenerRecuperarMov {
        public void DatosMovimiento(mMovAlmacen movAlmacen, ArrayList<mProduct> productList);

        public void ErrorProcedimient();

        public void ErrorConnection();
    }

    public void ObtenerMovAlmacen(int idMov) {

        obtenerMovAlmacen = new ObtenerMovAlmacen();
        obtenerMovAlmacen.execute(idMov);

    }

    private class ObtenerMovAlmacen extends AsyncTask<Integer, Void, Void> {

        mMovAlmacen cabeceraMov;
        ArrayList<mProduct> productList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            cabeceraMov = bdConnectionSql.ObtenerCabeceraMovimiento(integers[0]);
            if (cabeceraMov.getIdMovAlmacen() > 0) {
                productList = bdConnectionSql.productListMovAlmacen(integers[0]);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (listenerRecuperarMov != null) {
                try {
                    if (cabeceraMov.getIdMovAlmacen() > 0) {
                        listenerRecuperarMov.DatosMovimiento(cabeceraMov, productList);

                    } else if (cabeceraMov.getIdMovAlmacen() == 99) {
                        listenerRecuperarMov.ErrorProcedimient();

                    } else if (cabeceraMov.getIdMovAlmacen() == 98) {

                        listenerRecuperarMov.ErrorConnection();
                    }
                } catch (Exception e) {
                    e.toString();
                }
            }
        }
    }

    ListenerTransaccioneAlmacen listenerTransaccioneAlmacen;

    public void setListenerTransaccioneAlmacen(ListenerTransaccioneAlmacen listenerTransaccioneAlmacen) {
        this.listenerTransaccioneAlmacen = listenerTransaccioneAlmacen;
    }

    public interface ListenerTransaccioneAlmacen {

        public void TransaccionesResultados(List<mTransacciones_Almacen> listaTranAlmacen);

        public void ErrorConsulta();

        public void ErrorConexion();

    }

    public void ObtenerTransaccionesAlmacen() {

        obtenerTransacciones = new ObtenerTransacciones();
        obtenerTransacciones.execute();

    }

    private class ObtenerTransacciones extends AsyncTask<Void, Void, List<mTransacciones_Almacen>> {

        @Override
        protected List<mTransacciones_Almacen> doInBackground(Void... voids) {
            return bdConnectionSql.ObtenerTransaccionesAlmacen();
        }

        @Override
        protected void onPostExecute(List<mTransacciones_Almacen> mTransacciones_almacens) {
            super.onPostExecute(mTransacciones_almacens);
            if (mTransacciones_almacens != null) {

                if (mTransacciones_almacens.get(0).getIdTransaccion() == -99) {

                    listenerTransaccioneAlmacen.ErrorConsulta();
                } else if (mTransacciones_almacens.get(0).getIdTransaccion() == -98) {

                    listenerTransaccioneAlmacen.ErrorConexion();

                } else {

                    listenerTransaccioneAlmacen.TransaccionesResultados(mTransacciones_almacens);
                }

            }
        }
    }


    public void ObtenerTransferenciasAlmacen(int idAlmacen) {

        obtenerTransferenciasAlmacen = new ObtenerTransferenciasAlmacen();
        obtenerTransferenciasAlmacen.execute(idAlmacen);

    }


    public void setListenerTransferenciasAlmacen(ListenerTransferenciasAlmacen listenerTransferenciasAlmacen) {

        this.listenerTransferenciasAlmacen = listenerTransferenciasAlmacen;

    }

    public interface ListenerTransferenciasAlmacen {

        public void ListadoTransferencias(List<mMovAlmacen> mMovAlmacenList);

        public void ErrorProc();

        public void ErrorConnection();
    }

    private class ObtenerTransferenciasAlmacen extends AsyncTask<Integer, Void, List<mMovAlmacen>> {

        @Override
        protected List<mMovAlmacen> doInBackground(Integer... integers) {

            return bdConnectionSql.ObtenerTranferenciasAlmacen(integers[0]);
        }

        @Override
        protected void onPostExecute(List<mMovAlmacen> movAlmacenList) {
            super.onPostExecute(movAlmacenList);
            if (listenerTransferenciasAlmacen != null) {
                listenerTransferenciasAlmacen.ListadoTransferencias(movAlmacenList);
            }
        }
    }

    ListenerProductosMovimiento listenerProductosMovimiento;

    public void setListenerProductosMovimiento(ListenerProductosMovimiento listenerProductosMovimiento) {
        this.listenerProductosMovimiento = listenerProductosMovimiento;
    }

    public interface ListenerProductosMovimiento {
        public void ProductosEnTransferencia(List<mProduct> mProductList);
    }

    public void ObtenerProductosMovimiento(int idMovimiento) {
        obtenerProductosMovimiento = new ObtenerProductosMovimiento();
        obtenerProductosMovimiento.execute(idMovimiento);
    }

    private class ObtenerProductosMovimiento extends AsyncTask<Integer, Void, List<mProduct>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<mProduct> doInBackground(Integer... integers) {
            return bdConnectionSql.productListMovAlmacen(integers[0]);
        }

        @Override
        protected void onPostExecute(List<mProduct> productList) {
            super.onPostExecute(productList);
            if (listenerProductosMovimiento != null) {
                listenerProductosMovimiento.ProductosEnTransferencia(productList);
            }
        }
    }


    ListenerCompletarMovTrans listenerCompletarMovTrans;

    public void setListenerCompletarMovTrans(ListenerCompletarMovTrans listenerCompletarMovTrans) {

        this.listenerCompletarMovTrans = listenerCompletarMovTrans;

    }

    public interface ListenerCompletarMovTrans {

        public void ExitoTransferencia();

        public void ErrorTransferencia();

        public void ErrorConnection();

        public void AnotherError();

    }

    public void CompletarMovTransferencia(int idMov, String fechaMov, String descripcion, String fechaTransferencia) {
        completrarMovTransferencia = new CompletrarMovTransferencia();
        completrarMovTransferencia.setIdMovimiento(idMov);
        completrarMovTransferencia.setFechaMov(fechaMov);
        completrarMovTransferencia.setDescripcion(descripcion);
        completrarMovTransferencia.setFechaTranferencia(fechaTransferencia);
        completrarMovTransferencia.execute();
    }


    private class CompletrarMovTransferencia extends AsyncTask<Void, Void, Byte> {

        int idMovimiento;
        String fechaMov;
        String fechaTranferencia;
        String descripcion;

        public void setIdMovimiento(int idMovimiento) {
            this.idMovimiento = idMovimiento;
        }

        public void setFechaMov(String fechaMov) {
            this.fechaMov = fechaMov;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public void setFechaTranferencia(String fechaTranferencia) {
            this.fechaTranferencia = fechaTranferencia;
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            return bdConnectionSql.CompletarTranferenciaAlmacen(idMovimiento, descripcion, fechaMov, fechaTranferencia);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (listenerCompletarMovTrans != null) {

                if (aByte == 100) {
                    listenerCompletarMovTrans.ExitoTransferencia();
                } else if (aByte == 99) {
                    listenerCompletarMovTrans.ErrorTransferencia();
                } else if (aByte == 98) {
                    listenerCompletarMovTrans.ErrorConnection();
                } else {
                    listenerCompletarMovTrans.AnotherError();
                }

            }

        }

    }

    ListenerTipoTransacciones listenerTipoTransacciones;

    public void setListenerTipoTransacciones(ListenerTipoTransacciones listenerTipoTransacciones) {
        this.listenerTipoTransacciones = listenerTipoTransacciones;
    }

    public interface ListenerTipoTransacciones {
        public void TransaccionResultado(List<mTransaccionAlmacen> transaccionAlmacenList);

        public void ErrorConsulta();

        public void ErrorConexion();
    }

    public void ObtenerTipoTransaccionesAlmacen() {
        obtenerTipoTransacciones = new ObtenerTipoTransacciones();
        obtenerTipoTransacciones.execute();
    }

    private class ObtenerTipoTransacciones extends AsyncTask<Void, Void, List<mTransaccionAlmacen>> {

        @Override
        protected List<mTransaccionAlmacen> doInBackground(Void... voids) {
            return bdConnectionSql.obtenerTipoTransacciones();
        }

        @Override
        protected void onPostExecute(List<mTransaccionAlmacen> mTransaccionAlmacens) {
            super.onPostExecute(mTransaccionAlmacens);
            if (listenerTipoTransacciones != null) {
                if (mTransaccionAlmacens.get(0).getId() == -99) {
                    listenerTipoTransacciones.ErrorConsulta();
                } else if (mTransaccionAlmacens.get(0).getId() == -98) {
                    listenerTipoTransacciones.ErrorConexion();
                } else if (mTransaccionAlmacens.get(0).getId() == 1) {
                    listenerTipoTransacciones.TransaccionResultado(mTransaccionAlmacens);
                }
            }

        }
    }


    public interface ConfiguracionAlmacen {


        public void ExitoGuardarAlmacen();

        public void ExitoEditarAlmacen();

        public void ErrorGuardarAlmacen();

        public void ErrorEditarAlmacen();

        public void ExisteAlmacenPrincipal();

        public void ExisteStockComprometido();

        public void AlmacenNoDisponible();

        public void FinProceso();

    }

    ConfiguracionAlmacen configuracionAlmacen;

    public void setConfiguracionAlmacen(ConfiguracionAlmacen configuracionAlmacen) {

        this.configuracionAlmacen = configuracionAlmacen;

    }

    public void GuardarAlmacen(mAlmacen almacen) {

        new GuardarNuevoAlmacen().execute(almacen);


    }

    public void EditarAlmacen(mAlmacen almacen) {

        new EditarAlmacen().execute(almacen);

    }

    private class EditarAlmacen extends AsyncTask<mAlmacen, Void, Byte> {

        @Override
        protected Byte doInBackground(mAlmacen... mAlmacens) {
            return bdConnectionSql.EditarAlmacen(mAlmacens[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (configuracionAlmacen != null) {

                configuracionAlmacen.FinProceso();
                if (aByte == 100) {
                    configuracionAlmacen.ExitoEditarAlmacen();
                } else if (aByte == 85) {
                    configuracionAlmacen.AlmacenNoDisponible();
                } else if (aByte == 70) {
                    configuracionAlmacen.ExisteAlmacenPrincipal();
                } else if (aByte == 65) {
                    configuracionAlmacen.ExisteStockComprometido();
                } else if (aByte == 98 || aByte == 99) {
                    configuracionAlmacen.ErrorEditarAlmacen();
                }
            }
        }
    }

    private class GuardarNuevoAlmacen extends AsyncTask<mAlmacen, Void, Byte> {

        @Override
        protected Byte doInBackground(mAlmacen... mAlmacens) {
            return bdConnectionSql.GuardarAlmacen(mAlmacens[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {

            super.onPostExecute(aByte);
            if (configuracionAlmacen != null) {
                configuracionAlmacen.FinProceso();
                if (aByte == 100) {
                    configuracionAlmacen.ExitoGuardarAlmacen();
                } else if (aByte == 98 || aByte == 99) {
                    configuracionAlmacen.ErrorGuardarAlmacen();
                } else if (aByte == 50) {
                    configuracionAlmacen.ExisteAlmacenPrincipal();
                }
            }
        }
    }

    public interface ObtenerInfoAlmacen {

        public void InfoAlmacen(mAlmacen almacen);

        public void AlmacenNoDisponible();

        public void ErrorObtenerInfoAlmacen();
    }

    ObtenerInfoAlmacen obtenerInfoAlmacen;

    public void setObtenerInfoAlmacen(ObtenerInfoAlmacen obtenerInfoAlmacen) {
        this.obtenerInfoAlmacen = obtenerInfoAlmacen;
    }

    public void ObtenerAlmacenId(int idAlmacen) {
        new ObtenerAlmacen().execute(idAlmacen);
    }

    private class ObtenerAlmacen extends AsyncTask<Integer, Void, mAlmacen> {


        @Override
        protected mAlmacen doInBackground(Integer... integers) {
            return bdConnectionSql.obtenerAlmacen(integers[0]);
        }

        @Override
        protected void onPostExecute(mAlmacen almacen) {
            super.onPostExecute(almacen);
            if (obtenerInfoAlmacen != null) {

                if (almacen.getIdAlmacen() > 0) {
                    obtenerInfoAlmacen.InfoAlmacen(almacen);
                } else if (almacen.getIdAlmacen() == -99 || almacen.getIdAlmacen() == -98) {
                    obtenerInfoAlmacen.ErrorObtenerInfoAlmacen();
                } else if (almacen.getIdAlmacen() == 0) {
                    obtenerInfoAlmacen.AlmacenNoDisponible();
                }

            }

        }
    }

    ListenerEliminarAlmacen listenerEliminarAlmacen;

    public void setListenerEliminarAlmacen(ListenerEliminarAlmacen listenerEliminarAlmacen) {
        this.listenerEliminarAlmacen = listenerEliminarAlmacen;
    }

    public interface ListenerEliminarAlmacen {

        public void ErrorEliminar();

        public void ExitoEliminar();

        public void EliminarIncompleto();

        public void ExisteStockAlmacen();

    }

    public void EliminarAlmacen(int idAlmacen, int idTienda) {
        new EliminarAlmacen().execute(idAlmacen, idTienda);
    }

    private class EliminarAlmacen extends AsyncTask<Integer, Void, Byte> {


        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.eliminarAlmacen(integers[0], integers[1]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (listenerEliminarAlmacen != null) {
                if (aByte == 100) {
                    listenerEliminarAlmacen.ExitoEliminar();
                } else if (aByte == 90) {
                    listenerEliminarAlmacen.EliminarIncompleto();
                } else if (aByte == 98 || aByte == 99) {
                    listenerEliminarAlmacen.ErrorEliminar();
                } else if (aByte == 69) {
                    listenerEliminarAlmacen.ExisteStockAlmacen();
                }
            }
        }
    }

    IAnularMovAlmacen iAnularMovAlmacen;

    public interface IAnularMovAlmacen {
        public void MovEliminado();

        public void EliminarMovError();
    }

    public void AnularMovimientoAlmacen(int idAlmacen) {
        new AtAnularMovAlmacen().execute(idAlmacen);
    }

    public void setIAnularMovAlmacen(IAnularMovAlmacen iAnularMovAlmacen) {
        this.iAnularMovAlmacen = iAnularMovAlmacen;
    }

    private class AtAnularMovAlmacen extends AsyncTask<Integer, Void, Byte> {
        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.AnularMovimientoAlmacen(integers[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (iAnularMovAlmacen != null) {
                if (aByte == 100) {
                    iAnularMovAlmacen.MovEliminado();
                } else if (aByte == 98) {
                    iAnularMovAlmacen.EliminarMovError();
                }
            }
        }
    }

}




















