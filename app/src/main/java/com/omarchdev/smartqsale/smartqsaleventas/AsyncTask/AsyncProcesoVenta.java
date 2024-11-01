package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.Model.AdditionalPriceProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ComboRapidoSol;
import com.omarchdev.smartqsale.smartqsaleventas.Model.EnvioCpeW;
import com.omarchdev.smartqsale.smartqsaleventas.Model.PackElemento;
import com.omarchdev.smartqsale.smartqsaleventas.Model.PedidoVentaPermite;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProcessResult;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoTiempo;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoTiempoSol;
import com.omarchdev.smartqsale.smartqsaleventas.Model.RespuestaProductoVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultNumTelefono;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultProcces;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultProcessData;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolModificadorProductoDetallePedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.VarianteBusqueda;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IProductoRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVentaRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by OMAR CHH on 23/03/2018.
 */

public class AsyncProcesoVenta {
    final String codeCia = GetJsonCiaTiendaBase64x3();
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IPedidoRespository iPedidoRespository = retro.create(IPedidoRespository.class);
    IProductoRepository iProductoRepository = retro.create(IProductoRepository.class);
    IVentaRepository iVentaRepository = retro.create(IVentaRepository.class);


    ProductoSeleccionadoVariante productoSeleccionadoVariante;
    ListenerPackSeleccion listenerPackSeleccion;
    ListenerProductoSeleccionado listenerProductoSeleccionado;
    BusquedaProductoSeleccionado busquedaProductoSeleccionado;
    GuardarProductoModificador guardarProductoModificador;
    Context context;
    public ListenerAgregarListaProductosDetallePedido listenerAgregarListaProductosDetallePedido;
    DialogCargaAsync dialogCargaAsync;
    ControladorProcesoCargar controladorProcesoCargar;
    GuardarProductoCodigoBarra guardarProductoCodigoBarra;
    ResultadoCodigoBarraBusqueda resultadoCodigoBarraBusqueda;
    ListenerModificadorProductoSeleccion listenerModificadorProductoSeleccion;
    EliminarProductoDetallePedido eliminarProductoDetallePedido;
    VerificarStockDisponible verificarStockDisponible;
    IGetContentWhat iGetContentWhat;
    StockListener stockListener;
    int longitud = 0;
    List<String> listaProductos;
    public ListenerAgregarComboRapidoPedido listenerAgregarComboRapidoPedido;
    TaskEliminarProductoDetallePedido taskEliminarProductoDetallePedido;
    IGetNumeroTelefonoClienteVenta iGetNumeroTelefonoClienteVenta;

    public IGetNumeroTelefonoClienteVenta getiGetNumeroTelefonoClienteVenta() {
        return iGetNumeroTelefonoClienteVenta;
    }

    public void setiGetNumeroTelefonoClienteVenta(IGetNumeroTelefonoClienteVenta iGetNumeroTelefonoClienteVenta) {
        this.iGetNumeroTelefonoClienteVenta = iGetNumeroTelefonoClienteVenta;
    }

    public void setListenerAgregarComboRapidoPedido(ListenerAgregarComboRapidoPedido listenerAgregarComboRapidoPedido) {
        this.listenerAgregarComboRapidoPedido = listenerAgregarComboRapidoPedido;
    }

    public void AgregarComboRapido(ProductoEnVenta p) {
        new AgregarComboRapidoPedido().execute(new ProductoEnVenta[]{p});
    }

    IVerificarPermitirVenta iVerificarPermitirVenta;

    public interface ListenerAgregarComboRapidoPedido {
        void AgregadoComboRapidoExito(ProductoEnVenta productoEnVenta);

        void ErrorAgregarComboRapido(String str);
    }


    public void setStockListener(StockListener stockListener) {
        this.stockListener = stockListener;
    }

    public void setResultadoCodigoBarraBusqueda(ResultadoCodigoBarraBusqueda resultadoCodigoBarraBusqueda) {
        this.resultadoCodigoBarraBusqueda = resultadoCodigoBarraBusqueda;
    }

    public void setEliminarProductoDetallePedido(EliminarProductoDetallePedido eliminarProductoDetallePedido) {

        this.eliminarProductoDetallePedido = eliminarProductoDetallePedido;

    }

    public interface ResultadoCodigoBarraBusqueda {

        public void NoExisteProducto(ProductoEnVenta productoEnVenta);

        public void NoExisteStock(ProductoEnVenta productoEnVenta);

        public void ProductoNormalAgregado(ProductoEnVenta productoEnVenta);

        public void ProductoNormalAgregadoEditar(ProductoEnVenta productoEnVenta);

        public void ProductoVarianteAgregado(ProductoEnVenta productoEnVenta);

        public void ProductoVarianteEditado(ProductoEnVenta productoEnVenta);

        public void ProductoPadreVariante(ProductoEnVenta productoEnVenta);

        public void ProductoEsPack(ProductoEnVenta productoEnVenta);

        public void ProductoTieneModificador(ProductoEnVenta productoEnVenta);

        public void RepiteCodigoBarra(ProductoEnVenta productoEnVenta);

        public void ErrorConexion();

        public void ErrorProcedimiento();

    }

    public interface ListenerModificadorProductoSeleccion {

        public void ResultadoGuardarProdMod(ProductoEnVenta productoEnVenta);
    }

    public void setContext(Context context) {

        this.context = context;
        if (context != null) {
            controladorProcesoCargar = new ControladorProcesoCargar(context);
        }

    }

    public interface ListenerAgregarListaProductosDetallePedido {
        void ResultadoAgregarProductos(List<ProductoEnVenta> list);
    }

    public void setListenerModificadorProductoSeleccion(ListenerModificadorProductoSeleccion listenerModificadorProductoSeleccion) {

        this.listenerModificadorProductoSeleccion = listenerModificadorProductoSeleccion;

    }

    public AsyncProcesoVenta() {

        listaProductos = new ArrayList<>();
    }

    public void setProductoSeleccionadoVariante(ProductoSeleccionadoVariante productoSeleccionadoVariante) {

        this.productoSeleccionadoVariante = productoSeleccionadoVariante;
    }

    public interface ListenerProductoSeleccionado {

        public void ObtenerProductoSeleccionado(ProductoEnVenta productoEnVenta);

        public void NoExisteStock();

    }

    public interface ProductoSeleccionadoVariante {

        public void ResultadoSeleccion(ProductoEnVenta productoEnVenta);

    }

    public interface EliminarProductoDetallePedido {

        public void ResultadoEliminarCorrecto();

        public void ResultadoEliminarError();

        public void ResultadoNoConexion();

    }

    public interface ListenerPackSeleccion {

        public void PackResultadoGuardar(PackElemento packElemento);

        public void EliminarPackPedido(ProductoEnVenta productoEnVenta);

    }

    public void setListenerPackSeleccion(ListenerPackSeleccion listenerPackSeleccion) {

        this.listenerPackSeleccion = listenerPackSeleccion;

    }

    public void setListenerProductoSeleccionado(ListenerProductoSeleccionado listenerProductoSeleccionado) {

        this.listenerProductoSeleccionado = listenerProductoSeleccionado;
    }


    public void BusquedaVariante(VarianteBusqueda varianteBusqueda) {
        new BusquedaVariantePorParametros().execute(varianteBusqueda);

    }

    public void EliminarDetalleProductoPack(ProductoEnVenta productoEnVenta) {

        new EliminarPackProducto().execute(productoEnVenta);

    }

    public void GuardarProductoModificado(int idProduct, float cantidad, String modificado, int idCabeceraPedido, int idPventa) {
        guardarProductoModificador = new GuardarProductoModificador();
        guardarProductoModificador.setCantidad(cantidad);
        guardarProductoModificador.setIdProducto(idProduct);
        guardarProductoModificador.setModificador(modificado);
        guardarProductoModificador.setIdCabeceraPedido(idCabeceraPedido);
        guardarProductoModificador.setIdPventa(idPventa);
        guardarProductoModificador.execute();
    }

    //Obtener por id el producto para guardar en detalle pedido
    public void ObtenerProductoId(ProductoEnVenta productoEnVenta) {

        busquedaProductoSeleccionado = new BusquedaProductoSeleccionado();

        busquedaProductoSeleccionado.execute(productoEnVenta);

    }

    public void guardarPackenPedido(int idCabeceraPedido, PackElemento packElemento) {
        try {
            GuardarPackDetallePedido guardarPackDetallePedido = new GuardarPackDetallePedido();
            guardarPackDetallePedido.setIdCabeceraPedido(idCabeceraPedido);
            guardarPackDetallePedido.execute(packElemento);


        } catch (Exception e) {
            e.toString();
        }
    }

    public void AgregarListaProductosDetallePedido(List<ProductoEnVenta> lista, ListenerAgregarListaProductosDetallePedido listenerAgregarListaProductosDetallePedido) {
        this.listenerAgregarListaProductosDetallePedido = listenerAgregarListaProductosDetallePedido;
        new AgregarListaProductosDetallePedido().execute(new List[]{lista});
    }

    private class AgregarListaProductosDetallePedido extends AsyncTask<List<ProductoEnVenta>, Void, List<ProductoEnVenta>> {
        private AgregarListaProductosDetallePedido() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected List<ProductoEnVenta> doInBackground(List<ProductoEnVenta>... lists) {
            List<ProductoEnVenta> lista = new ArrayList();
            for (int i = 0; i < lists[0].size(); i++) {
                try {
                    SolicitudEnvio<ProductoEnVenta> sol = new SolicitudEnvio<>(
                            codeCia, TIPO_CONSULTA, lists[0].get(i), Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario
                    );
                    ProductoEnVenta p = iPedidoRespository.IngresaProductoPedido(sol).execute().body();
                    lista.add(p);
                } catch (Exception ex) {
                    ProductoEnVenta p = new ProductoEnVenta();
                    p.setIdProducto(-10);
                    lista.add(p);
                }
                // lista.add(AsyncProcesoVenta.this.bdConnectionSql.guardarProductoNDetalle());
            }
            return lista;
        }

        protected void onPostExecute(List<ProductoEnVenta> productoEnVentas) {
            super.onPostExecute(productoEnVentas);
            AsyncProcesoVenta.this.listenerAgregarListaProductosDetallePedido.ResultadoAgregarProductos(productoEnVentas);
        }
    }

    private class GuardarPackDetallePedido extends AsyncTask<PackElemento, Void, PackElemento> {

        int idCabeceraPedido;

        public void setIdCabeceraPedido(int idCabeceraPedido) {
            this.idCabeceraPedido = idCabeceraPedido;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected PackElemento doInBackground(PackElemento... packElementos) {
            packElementos[0].setIdCabeceraPedido(idCabeceraPedido);
            SolicitudEnvio sol = new SolicitudEnvio(codeCia, TIPO_CONSULTA, packElementos[0], Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            String tempSol = new Gson().toJson(sol);
            String temp = tempSol;
            try {
                return (PackElemento) iProductoRepository.GuardarPackDetallePedido(sol).execute().body();
            } catch (Exception ex) {
                //   e.printStackTrace();
                PackElemento result = new PackElemento();

                return result;
            }
            //    return bdConnectionSql.GuardarPackDetallePedido(idCabeceraPedido,packElementos[0]);
        }

        @Override
        protected void onPostExecute(PackElemento packElemento) {
            super.onPostExecute(packElemento);
            listenerPackSeleccion.PackResultadoGuardar(packElemento);
        }
    }

    //PRUEBA API
    private class EliminarPackProducto extends AsyncTask<ProductoEnVenta, Void, ProductoEnVenta> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ProductoEnVenta doInBackground(ProductoEnVenta... productoEnVentas) {
            SolicitudEnvio<ProductoEnVenta> solicitudEnvio = new SolicitudEnvio<>(codeCia,
                    "2", productoEnVentas[0],
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario);
            Gson gson = new Gson();
            String JSONS = gson.toJson(solicitudEnvio);
            int code = 0;
            try {
                Response<ProductoEnVenta> response = iPedidoRespository.EliminarProductoPack(solicitudEnvio).execute();
                if (response.code() == 200) {
                    ProductoEnVenta productResult = response.body();
                    return productResult;
                } else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ProductoEnVenta productoEnVenta) {
            super.onPostExecute(productoEnVenta);
            listenerPackSeleccion.EliminarPackPedido(productoEnVenta);
        }
    }

    ListenerPrecioVentaAdiccional listenerPrecioVentaAdiccional;
    ListenerAgregarProductoPedidoTiempo listenerAgregarProductoPedidoTiempo;


    private class GuardarProductoModificador extends AsyncTask<Void, Void, ProductoEnVenta> {

        String modificador;
        int idProducto;
        float cantidad;
        int idCabeceraPedido;
        int idPventa;

        public int getIdCabeceraPedido() {
            return idCabeceraPedido;
        }

        public void setIdCabeceraPedido(int idCabeceraPedido) {
            this.idCabeceraPedido = idCabeceraPedido;
        }

        public String getModificador() {
            return modificador;
        }

        public void setModificador(String modificador) {
            this.modificador = modificador;
        }

        public int getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(int idProducto) {
            this.idProducto = idProducto;
        }

        public int getIdPventa() {
            return idPventa;
        }

        public void setIdPventa(int idPventa) {
            this.idPventa = idPventa;
        }

        public float getCantidad() {
            return cantidad;
        }

        public void setCantidad(float cantidad) {
            this.cantidad = cantidad;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected ProductoEnVenta doInBackground(Void... voids) {

            SolicitudEnvio<SolModificadorProductoDetallePedido> sol = new SolicitudEnvio(codeCia, TIPO_CONSULTA,
                    new SolModificadorProductoDetallePedido(idProducto, idCabeceraPedido, cantidad, modificador, idPventa), Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            try {

                return iPedidoRespository.GuardarProductoModificadorDetallePedido(sol).execute().body();

            } catch (Exception ex) {
                ProductoEnVenta p = new ProductoEnVenta();
                p.setIdProducto(-5);
                return p;
            }

            /*return bdConnectionSql
                    .GuardarProductoModificadorDetallePedido
                            (idProducto,idCabeceraPedido,cantidad,modificador,idPventa);*/
        }


        @Override
        protected void onPostExecute(ProductoEnVenta productoEnVenta) {
            super.onPostExecute(productoEnVenta);
            listenerModificadorProductoSeleccion.ResultadoGuardarProdMod(productoEnVenta);
        }
    }

    public void VerificarPermitirVenta(int idCabeceraPedido, boolean validaPagos) {
        VerificarPermitirVentaAsync verificarPermitirVentaAsync = new VerificarPermitirVentaAsync();
        verificarPermitirVentaAsync.setValidaPagos(validaPagos);
        verificarPermitirVentaAsync.execute(idCabeceraPedido);


    }

    public void BuscarProductoCodigoBarra(String codigoBarra, int idCabecera) {

        try {
            guardarProductoCodigoBarra = new GuardarProductoCodigoBarra();
            guardarProductoCodigoBarra.setIdCabecera(idCabecera);
            guardarProductoCodigoBarra.setCodigoBarra(codigoBarra);

            guardarProductoCodigoBarra.execute();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();

        }


    }

    public void EliminarProductoDetalle(int idCabecera, int idDetalle) {
        if (idCabecera != 0 && idDetalle != 0) {
            taskEliminarProductoDetallePedido = new TaskEliminarProductoDetallePedido();
            taskEliminarProductoDetallePedido.setIdCabeceraPedido(idCabecera);
            taskEliminarProductoDetallePedido.setIdDetallePedido(idDetalle);
            taskEliminarProductoDetallePedido.execute();
        } else {
            Toast.makeText(context, "No se puede eliminar", Toast.LENGTH_LONG).show();
        }
    }

    public void setiVerificarPermitirVenta(IVerificarPermitirVenta iVerificarPermitirVenta) {
        this.iVerificarPermitirVenta = iVerificarPermitirVenta;
    }

    public void VerificarStockDisponible(int idCabeceraPedido) {

        verificarStockDisponible = new VerificarStockDisponible();
        verificarStockDisponible.execute(idCabeceraPedido);


    }

    public void setListenerPrecioVentaAdiccional(ListenerPrecioVentaAdiccional listenerPrecioVentaAdiccional) {
        this.listenerPrecioVentaAdiccional = listenerPrecioVentaAdiccional;
    }

    public void ObtenerPreciosVenta(int idProducto) {
        new ObtenerPreciosVentaAdiccional().execute(idProducto);
    }

    public void AgregarProductoTiempoDetallePedido(mProduct p, int idCabeceraPedido, String horaInicio) {
        AgregarProductoTiempoDetallePedido f = new AgregarProductoTiempoDetallePedido();
        f.setHoraInicio(horaInicio);
        f.setIdPedido(idCabeceraPedido);
        f.execute(p);
    }

    public void setListenerAgregarProductoPedidoTiempo(ListenerAgregarProductoPedidoTiempo listenerAgregarProductoPedidoTiempo) {

        this.listenerAgregarProductoPedidoTiempo = listenerAgregarProductoPedidoTiempo;

    }


    public interface IVerificarPermitirVenta {
        public void PermitirVentaPedido();

        public void NoPermitirVenta(String mensaje);

        public void CajaNoDisponible();
    }

    public interface ListenerAgregarProductoPedidoTiempo {

        public void ExitoAgregar(ProductoEnVenta productoEnVenta);

        public void ErrorAgregar();

        public void ErrorAgregarAdvertencia(String mensaje);


    }

    public interface StockListener {

        public void StockInsuficiente(List<String> listaProductos);

        public void StockSuficiente();

        public void VerificarStockError();

        public void VerificarStockErrorConnection();

    }


    public interface ListenerPrecioVentaAdiccional {

        public void ObtenerPreciosVentaAd(List<AdditionalPriceProduct> additionalPriceProductList);

        public void ErrorConsulta();

    }


    public interface IGetNumeroTelefonoClienteVenta {
        public void ResultGetNumTelefono(ResultNumTelefono resultNumTelefono);
    }

    public void GetNumeroTelefono(int idCabeceraVenta, IGetNumeroTelefonoClienteVenta iGetNumeroTelefonoClienteVenta) {
        this.iGetNumeroTelefonoClienteVenta = iGetNumeroTelefonoClienteVenta;
        GetNumeroTelefonoClienteVenta get = new GetNumeroTelefonoClienteVenta();
        get.execute(idCabeceraVenta);
    }

    private class GetNumeroTelefonoClienteVenta extends AsyncTask<Integer, Void, ResultNumTelefono> {


        @Override
        protected ResultNumTelefono doInBackground(Integer... ints) {
            try {
                return iVentaRepository.GetNumeroCelularVentaCliente(codeCia, TIPO_CONSULTA, ints[0]).execute().body();

            } catch (Exception ex) {
                return new ResultNumTelefono();
            }
        }

        @Override
        protected void onPostExecute(ResultNumTelefono resultNumTelefono) {
            super.onPostExecute(resultNumTelefono);
            iGetNumeroTelefonoClienteVenta.ResultGetNumTelefono(resultNumTelefono);

        }
    }


    private class GetContentMensajeW extends AsyncTask<String, Void, ResultNumTelefono> {

        @Override
        protected ResultNumTelefono doInBackground(String... strings) {
            return null;
        }
    }


    private class AgregarComboRapidoPedido extends AsyncTask<ProductoEnVenta, Void, RespuestaProductoVenta> {
        private AgregarComboRapidoPedido() {
        }

        protected RespuestaProductoVenta doInBackground(ProductoEnVenta... productoEnVentas) {
            ComboRapidoSol comboRapidoSol = new ComboRapidoSol(productoEnVentas[0].getIdCabeceraPedido(), productoEnVentas[0]);
            SolicitudEnvio sol = new SolicitudEnvio(codeCia, TIPO_CONSULTA, comboRapidoSol, Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            String tempSol = "";
            try {
                RespuestaProductoVenta res1 = (RespuestaProductoVenta) iProductoRepository.AgregarComboRapidoDetallePedido(sol).execute().body();
                return res1;
            } catch (Exception ex) {
                RespuestaProductoVenta res = new RespuestaProductoVenta();
                res.setCodeRespuesta(0);
                return res;
            }
        }

        protected void onPostExecute(RespuestaProductoVenta respuestaProductoVenta) {
            super.onPostExecute(respuestaProductoVenta);
            if (respuestaProductoVenta.getCodeRespuesta() == 100) {
                AsyncProcesoVenta.this.listenerAgregarComboRapidoPedido.AgregadoComboRapidoExito(respuestaProductoVenta.getProductoEnVenta());
            } else {
                AsyncProcesoVenta.this.listenerAgregarComboRapidoPedido.ErrorAgregarComboRapido(respuestaProductoVenta.getMensaje());
            }
        }
    }

    private class BusquedaProductoSeleccionado extends AsyncTask<ProductoEnVenta, Void, ProductoEnVenta> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ProductoEnVenta doInBackground(ProductoEnVenta... productoEnVentas) {
            try {
                SolicitudEnvio<ProductoEnVenta> sol = new SolicitudEnvio<>(
                        codeCia, TIPO_CONSULTA, productoEnVentas[0], Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario
                );
                String json = new Gson().toJson(sol);
                String demo = json;
                return iPedidoRespository.IngresaProductoPedido(sol).execute().body();
            } catch (Exception ex) {
                ProductoEnVenta p = new ProductoEnVenta();
                p.setIdProducto(-10);
                return p;
            }

            //  return bdConnectionSql.guardarProductoNDetalle(productoEnVentas[0]);
        }

        @Override
        protected void onPostExecute(ProductoEnVenta productoEnVenta) {
            super.onPostExecute(productoEnVenta);
            /*if(controladorProcesoCargar!=null){
                controladorProcesoCargar.FinalizarDialogCarga();
            }*/
            if (listenerProductoSeleccionado != null) {

                if (productoEnVenta.getIdProducto() == -10) {
                    listenerProductoSeleccionado.NoExisteStock();
                } else {
                    listenerProductoSeleccionado.ObtenerProductoSeleccionado(productoEnVenta);
                }
            }
        }
    }

    private class BusquedaVariantePorParametros extends AsyncTask<VarianteBusqueda, Void, ProductoEnVenta> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected ProductoEnVenta doInBackground(VarianteBusqueda... varianteBusquedas) {

            SolicitudEnvio<VarianteBusqueda> solicitud = new SolicitudEnvio<>(codeCia, "2", varianteBusquedas[0], Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            try {
                return iPedidoRespository.GuardarProductoVariante(solicitud).execute().body();
            } catch (IOException e) {
                ProductoEnVenta productoEnVenta = new ProductoEnVenta();
                productoEnVenta.setIdProducto(0);
                return productoEnVenta;
            }
            //  return bdConnectionSql.GuardarProductoVarianteDetallePedido(varianteBusquedas[0]);
        }

        @Override
        protected void onPostExecute(ProductoEnVenta productoEnVenta) {
            super.onPostExecute(productoEnVenta);
            productoSeleccionadoVariante.ResultadoSeleccion(productoEnVenta);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    private class GuardarProductoCodigoBarra extends AsyncTask<Void, Void, ProductoEnVenta> {

        String codigoBarra;
        int idCabecera;


        public void setCodigoBarra(String codigoBarra) {
            this.codigoBarra = codigoBarra;
        }

        public void setIdCabecera(int idCabecera) {
            this.idCabecera = idCabecera;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            controladorProcesoCargar.IniciarDialogCarga("Buscando producto seleccionado");
        }

        @Override
        protected ProductoEnVenta doInBackground(Void... voids) {
            try {

                return iProductoRepository.BuscarProductoCodigoBarra(codeCia,
                        TIPO_CONSULTA,
                        codigoBarra,
                        idCabecera,
                        Constantes.Terminal.idTerminal,
                        Constantes.Usuario.idUsuario).execute().body();

            } catch (Exception ex) {
                ProductoEnVenta productoEnVenta = new ProductoEnVenta();
                productoEnVenta.setIdProducto(-99);
                return productoEnVenta;
            }

        }

        @Override
        protected void onPostExecute(ProductoEnVenta productoEnVenta) {
            super.onPostExecute(productoEnVenta);
            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.FinalizarDialogCarga();
            }
          /*
            if(context!=null){
                Toast.makeText(context,String.valueOf(productoEnVenta.getAccionGuardarCodeBar()),Toast.LENGTH_LONG).show();

            }*/
            if (resultadoCodigoBarraBusqueda != null) {

                if (productoEnVenta.getAccionGuardarCodeBar() == 30) {
                    productoEnVenta.setMetodoGuardar("N");
                    resultadoCodigoBarraBusqueda.ProductoNormalAgregado(productoEnVenta);

                } else if (productoEnVenta.getAccionGuardarCodeBar() == 35) {

                    productoEnVenta.setMetodoGuardar("M");
                    resultadoCodigoBarraBusqueda.ProductoNormalAgregadoEditar(productoEnVenta);

                } else if (productoEnVenta.getAccionGuardarCodeBar() == 10) {

                    productoEnVenta.setMetodoGuardar("N");

                    resultadoCodigoBarraBusqueda.ProductoVarianteAgregado(productoEnVenta);

                } else if (productoEnVenta.getAccionGuardarCodeBar() == 12) {


                    productoEnVenta.setMetodoGuardar("M");


                    resultadoCodigoBarraBusqueda.ProductoVarianteEditado(productoEnVenta);

                } else if (productoEnVenta.getAccionGuardarCodeBar() == 20) {


                    resultadoCodigoBarraBusqueda.ProductoEsPack(productoEnVenta);

                } else if (productoEnVenta.getAccionGuardarCodeBar() == 15) {

                    resultadoCodigoBarraBusqueda.ProductoPadreVariante(productoEnVenta);

                } else if (productoEnVenta.getAccionGuardarCodeBar() == 25) {

                    resultadoCodigoBarraBusqueda.ProductoTieneModificador(productoEnVenta);

                } else if (productoEnVenta.getAccionGuardarCodeBar() == 5) {

                    resultadoCodigoBarraBusqueda.NoExisteStock(productoEnVenta);

                } else if (productoEnVenta.getAccionGuardarCodeBar() == 0) {
                    resultadoCodigoBarraBusqueda.NoExisteProducto(productoEnVenta);


                } else if (productoEnVenta.getAccionGuardarCodeBar() == 66) {
                    resultadoCodigoBarraBusqueda.RepiteCodigoBarra(productoEnVenta);
                }


                if (productoEnVenta.getIdProducto() == -99) {

                } else if (productoEnVenta.getIdProducto() == -98) {

                }

            }
        }
    }

    private class TaskEliminarProductoDetallePedido extends AsyncTask<Void, Void, Byte> {

        int idCabeceraPedido;
        int idDetallePedido;

        public void setIdCabeceraPedido(int idCabeceraPedido) {
            this.idCabeceraPedido = idCabeceraPedido;
        }

        public void setIdDetallePedido(int idDetallePedido) {
            this.idDetallePedido = idDetallePedido;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.IniciarDialogCarga("Eliminando Producto");
            }
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            try {
                int result = iPedidoRespository.EliminaDetallePedido(codeCia, TIPO_CONSULTA, idCabeceraPedido, idDetallePedido).execute().body();
                return (byte) result;
            } catch (IOException e) {
                return 99;
            } catch (Exception ex) {
                return 98;
            }
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (controladorProcesoCargar != null) {

                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if (eliminarProductoDetallePedido != null) {
                if (aByte == 100) {
                    eliminarProductoDetallePedido.ResultadoEliminarCorrecto();
                } else if (aByte == 99) {
                    eliminarProductoDetallePedido.ResultadoEliminarError();
                } else if (aByte == 98) {
                    eliminarProductoDetallePedido.ResultadoNoConexion();
                }

            }
        }
    }

    private class VerificarPermitirVentaAsync extends AsyncTask<Integer, Void, ResultProcces> {

        public boolean ValidaPagos;

        public void setValidaPagos(boolean ValidaPagos) {
            this.ValidaPagos = ValidaPagos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.IniciarDialogCarga("Verificando datos");
            }
        }

        @Override
        protected ResultProcces doInBackground(Integer... integers) {
            SolicitudEnvio<PedidoVentaPermite> sol = new SolicitudEnvio<>(codeCia, "2",
                    new PedidoVentaPermite(integers[0], ValidaPagos),
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario);
            ProcessResult<Integer> result = new ProcessResult<>();
            try {
                result = iPedidoRespository.PermitirPedidoVenta(sol).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ResultProcces re = new ResultProcces();
            re.setMessageResult(result.getMessageResult());
            re.setCodeResult(result.getCodeResult());
            re.setCodeProcess(result.getData());
            // return bdConnectionSql.PermitirVenta(integers[0],ValidaPagos);
            return re;
        }

        @Override
        protected void onPostExecute(ResultProcces resultProcces) {
            super.onPostExecute(resultProcces);
            controladorProcesoCargar.FinalizarDialogCarga();
            if (resultProcces.getCodeResult() == 100) {
                iVerificarPermitirVenta.PermitirVentaPedido();
            } else if (resultProcces.getCodeResult() == 99) {
                if (resultProcces.getCodeProcess() == 999) {
                    iVerificarPermitirVenta.CajaNoDisponible();
                } else {
                    iVerificarPermitirVenta.NoPermitirVenta(resultProcces.getMessageResult());
                }
            }
        }
    }

    public class VerificarStockDisponible extends AsyncTask<Integer, Void, Byte> {

        byte resultado = 101;
        List<ProductoEnVenta> productoEnVentaList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.IniciarDialogCarga("Verificar Stock Disponible");

            }
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            try {
                productoEnVentaList = iPedidoRespository.VerificaStockDisponible(codeCia, TIPO_CONSULTA, integers[0]).execute().body();
            } catch (IOException e) {
                productoEnVentaList = new ArrayList<>();

            } catch (Exception ex) {
                productoEnVentaList = new ArrayList<>();

            }
            if (productoEnVentaList.size() > 0) {
                if (productoEnVentaList.get(0).getIdProducto() >= 0) {
                    longitud = productoEnVentaList.size();
                    listaProductos.clear();
                    for (int i = 0; i < longitud; i++) {
                        if (productoEnVentaList.get(i).isDisponibleStock()) {

                        } else {
                            resultado = 100;
                            listaProductos.add(productoEnVentaList.get(i).getProductName());

                        }

                    }
                } else if (productoEnVentaList.get(0).getIdProducto() == -99) {

                    resultado = 99;

                } else if (productoEnVentaList.get(0).getIdProducto() == -98) {

                    resultado = 98;
                }
            } else if (listaProductos.size() == 0) {
                resultado = 101;
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if (aByte == 101) {
                stockListener.StockSuficiente();
            } else if (aByte == 100) {
                stockListener.StockInsuficiente(listaProductos);
            } else if (aByte == 99) {
                stockListener.VerificarStockError();

            } else if (aByte == 98) {
                stockListener.VerificarStockErrorConnection();
            }

        }


    }

    private class ObtenerPreciosVentaAdiccional extends AsyncTask<Integer, Void, List<AdditionalPriceProduct>> {

        @Override
        protected List<AdditionalPriceProduct> doInBackground(Integer... integers) {
            try {
                return iProductoRepository.ObtenerPreciosAdiccionales(codeCia, TIPO_CONSULTA, integers[0]).execute().body();
            } catch (IOException e) {
                return new ArrayList<>();
                //    e.printStackTrace();

            } catch (Exception ex) {
                return new ArrayList<>();

            }

            //   return bdConnectionSql.ObtenerPreciosAdiccionales(integers[0]);
        }

        @Override
        protected void onPostExecute(List<AdditionalPriceProduct> additionalPriceProductList) {
            super.onPostExecute(additionalPriceProductList);

            if (listenerPrecioVentaAdiccional != null) {

                if (additionalPriceProductList != null) {
                    listenerPrecioVentaAdiccional.ObtenerPreciosVentaAd(additionalPriceProductList);
                } else {
                    listenerPrecioVentaAdiccional.ErrorConsulta();
                }

            }

        }
    }

    private class AgregarProductoTiempoDetallePedido extends AsyncTask<mProduct, Void, ResultProcessData<ProductoEnVenta>> {

        int idPedido;
        String horaInicio;

        public void setIdPedido(int idPedido) {
            this.idPedido = idPedido;
        }

        public void setHoraInicio(String horaInicio) {
            this.horaInicio = horaInicio;
        }

        @Override
        protected ResultProcessData<ProductoEnVenta> doInBackground(mProduct... mProducts) {

            ProductoTiempo productoTiempo = new ProductoTiempo();
            productoTiempo.setProduct(mProducts[0]);
            productoTiempo.setHoraInicio(horaInicio);
            productoTiempo.setIdPedido(idPedido);
            SolicitudEnvio<ProductoTiempo> sol = new SolicitudEnvio<>(codeCia, TIPO_CONSULTA, productoTiempo, Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            String json = new Gson().toJson(sol);
            Log.i("tiempo-log", json);
            ResultProcessData<ProductoEnVenta> result = null;
            try {
                result = iPedidoRespository.GuardarProductoTiempoPedido(sol).execute().body();
            } catch (Exception ex) {
                result = new ResultProcessData<>();
                result.setCodeResult(99);
                result.setMessageResult("Existe un inconveniente al realizar al proceso . Verifique su conexión a internet.");
            }

            return result;
        }

        @Override
        protected void onPostExecute(ResultProcessData<ProductoEnVenta> productoEnVenta) {
            super.onPostExecute(productoEnVenta);

            if (productoEnVenta.getCodeResult() == 200) {
                if (productoEnVenta.getData().getIdProducto() != -99) {
                    listenerAgregarProductoPedidoTiempo.ExitoAgregar(productoEnVenta.getData());
                } else {
                    listenerAgregarProductoPedidoTiempo.ErrorAgregar();
                    ;
                }
            } else {
                listenerAgregarProductoPedidoTiempo.ErrorAgregarAdvertencia(productoEnVenta.getMessageResult());
            }


        }
    }

    public void EditarProductoTiempoPedido(ProductoEnVenta productoEnVenta, int idCabeceraPedido, String horaFinal) {

        EditarProductoTiempoPedido a = new EditarProductoTiempoPedido();
        a.setIdCabeceraPedido(idCabeceraPedido);
        a.setProductoEnVenta(productoEnVenta);
        a.setHoraFinal(horaFinal);
        a.execute();

    }

    public interface ListenerEditarProductoTiempoPedido {

        public void ExitoGuardar(ProductoEnVenta p);

        public void ErrorGuardar();

    }

    public ListenerEditarProductoTiempoPedido listenerEditarProductoTiempoPedido;

    public void setListenerEditarProductoTiempoPedido(ListenerEditarProductoTiempoPedido listener) {
        this.listenerEditarProductoTiempoPedido = listener;
    }

    private class EditarProductoTiempoPedido extends AsyncTask<Void, Void, ProductoEnVenta> {

        private ProductoEnVenta productoEnVenta;
        private int idCabeceraPedido;
        private String horaFinal;

        public void setProductoEnVenta(ProductoEnVenta productoEnVenta) {
            this.productoEnVenta = productoEnVenta;
        }

        public void setIdCabeceraPedido(int idCabeceraPedido) {
            this.idCabeceraPedido = idCabeceraPedido;
        }

        public void setHoraFinal(String horaFinal) {
            this.horaFinal = horaFinal;
        }

        @Override
        protected ProductoEnVenta doInBackground(Void... voids) {
            ProductoTiempoSol productoTiempoSol = new ProductoTiempoSol(productoEnVenta, idCabeceraPedido, horaFinal);
            SolicitudEnvio sol = new SolicitudEnvio(
                    codeCia, TIPO_CONSULTA, productoTiempoSol,
                    Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);

            try {
                return (ProductoEnVenta) iPedidoRespository.EditarProductoTiempoPedido(sol).execute().body();
            } catch (Exception ex) {
                ProductoEnVenta productoEnVenta = new ProductoEnVenta();
                productoEnVenta.setIdDetallePedido(-99);
                return productoEnVenta;

            }
            //  return BdConnectionSql.getSinglentonInstance().EditarProductoTiempoPedido(productoEnVenta,idCabeceraPedido,horaFinal);
        }

        @Override
        protected void onPostExecute(ProductoEnVenta productoEnVenta) {
            super.onPostExecute(productoEnVenta);
            if (productoEnVenta.getIdDetallePedido() != -99) {
                listenerEditarProductoTiempoPedido.ExitoGuardar(productoEnVenta);
            } else {
                listenerEditarProductoTiempoPedido.ErrorGuardar();
            }
        }
    }


    public interface IGetContentWhat {
        public void ResultContentWhat(EnvioCpeW envioCpeW);
    }

    public void GetContentWhatCPE(int idCabeceraVenta, String numTelefono, IGetContentWhat iGetContentWhat) {
        this.iGetContentWhat=iGetContentWhat;
        GetContentWhatCPE get = new GetContentWhatCPE();
        get.setNumTelefono(numTelefono);
        get.execute(idCabeceraVenta);
    }

    private class GetContentWhatCPE extends AsyncTask<Integer, Void, EnvioCpeW> {
        public String numTelefono;

        public void setNumTelefono(String numTelefono) {
            this.numTelefono = numTelefono;
        }

        @Override
        protected EnvioCpeW doInBackground(Integer... ints) {
            try {
                return iVentaRepository.CrearMensajeEnvioWhatsappCpe(codeCia, TIPO_CONSULTA, ints[0], numTelefono).execute().body();

            } catch (Exception ex) {
                return new EnvioCpeW();
            }
        }

        @Override
        protected void onPostExecute(EnvioCpeW resultNumTelefono) {
            super.onPostExecute(resultNumTelefono);
            iGetContentWhat.ResultContentWhat(resultNumTelefono);

        }
    }


}













