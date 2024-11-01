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
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.Model.CategoriaPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ConfigPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.PackElemento;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResponseCodProducto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultProcces;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Variante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacen;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICategoriaRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IProductoRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVarianteRepo;

import org.apache.poi.util.Beta;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by OMAR CHH on 20/02/2018.
 */

public class AsyncProducto {


    public Context context;
    DialogCargaAsync dialogCargaAsync;
    String mensaje;
    Dialog dialog;
    BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    RespuestaProducto respuestaProducto;
    List<mProduct> listaProducto;
    ObtenerProductos obtenerProductos;
    String campoNombreCodigo;
    int idCategoria;
    GetProduct getProduct;
    boolean EstadoVariante;
    boolean estadoConfigVaria;
    ProcesoVariantes procesoVariantes;
    ListenerConfiguracionPack listenerConfiguracionPack;
    ListenerComboPack listenerComboPack;
    ObtenerComboPackProducto obtenerComboPackProducto;
    DownloadListProductVenta downloadListProductVenta;
    GenerarCodigoProducto generarCodigoProducto;
    ListenerComprasProducto listenerComprasProducto;
    ObtenerProductosCompra obtenerProductosCompra;
    ObtenerDatosCompraProducto obtenerDatosCompraProducto;
    ListenerDatosCompra listenerDatosCompra;
    ControladorProcesoCargar controladorProcesoCargar;
    ActualizarCantidadProductoPedido actualizarCantidadProductoPedido;
    ObtenerProductosEnAlmacen obtenerProductosEnAlmacen;
    ObtenerDiponiblesVariantes obtenerDiponiblesVariantes;
    DownloadListProductConfig downloadListProductConfig;
    ActualizoEstadoComboRapido actualizoEstadoComboRapido;
    ListenerConfigProducto listenerConfigProducto;
    ListenerPreciosCategorias listenerPreciosCategorias;
    ResultadoGuardarProducto resultadoGuardarProducto;
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IProductoRepository iProductoRepository = retro.create(IProductoRepository.class);
    ICategoriaRepository iCategoriaRepository = retro.create(ICategoriaRepository.class);
    IVarianteRepo iVarianteRepo = retro.create(IVarianteRepo.class);
    IPedidoRespository iPedidoRespository = retro.create(IPedidoRespository.class);

    final String codeCia = GetJsonCiaTiendaBase64x3();
    /*


     */

    public void setResultadoGuardarProducto(ResultadoGuardarProducto resultadoGuardarProducto) {
        this.resultadoGuardarProducto = resultadoGuardarProducto;
    }

    public void setActualizoEstadoComboRapido(ActualizoEstadoComboRapido actualizoEstadoComboRapido) {
        this.actualizoEstadoComboRapido = actualizoEstadoComboRapido;
    }

    public void setListenerConfigProducto(ListenerConfigProducto listenerConfigProducto) {
        this.listenerConfigProducto = listenerConfigProducto;
    }

    public void setListenerPreciosCategorias(ListenerPreciosCategorias listenerPreciosCategorias) {
        this.listenerPreciosCategorias = listenerPreciosCategorias;
    }

    public void GuardarProducto(mProduct product) {


        new GuardarProductoV2().execute(product);
    }

    public interface ListenerPreciosCategorias {
        void ErrorBusquedaPrecioCategoria();

        void ListadoCategoriasPrecio(List<CategoriaPack> list);
    }

    public void ActualizarComboRapido(int idProducto, boolean ComboSimple) {
        ActualizarEstadoComboRapido a = new ActualizarEstadoComboRapido();
        a.setEstado(ComboSimple);
        a.execute(new Integer[]{Integer.valueOf(idProducto)});
    }


    private class ActualizarEstadoComboRapido extends AsyncTask<Integer, Void, Byte> {
        public boolean Estado;

        private ActualizarEstadoComboRapido() {
        }

        public void setEstado(boolean estado) {
            this.Estado = estado;
        }

        protected Byte doInBackground(Integer... integers) {
            return Byte.valueOf(AsyncProducto.this.bdConnectionSql.ActualizarEstadoVentaRapida(integers[0].intValue(), this.Estado));
        }

        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (aByte.byteValue() == (byte) 100) {
                AsyncProducto.this.actualizoEstadoComboRapido.ExitoActualizaComboRapido();
            } else if (aByte.byteValue() == Constantes.EstadosAppLoguin.EstadoErrorProc) {
                AsyncProducto.this.actualizoEstadoComboRapido.ErrorActualizarComboRapido();
            }
        }
    }

    public void setListenerComprasProducto(ListenerComprasProducto listenerComprasProducto) {

        this.listenerComprasProducto = listenerComprasProducto;
    }

    public void setContext(Context contex) {

        this.context = context;
        if (context != null) {
            controladorProcesoCargar = new ControladorProcesoCargar(context);
        }
    }

    public interface ListenerDatosCompra {
        public void ObtenerDatosProductoEnAlmacen(BigDecimal precioCompra, List<mAlmacen> almacenList);
    }

    public void setListenerDatosCompra(ListenerDatosCompra listenerDatosCompra) {
        this.listenerDatosCompra = listenerDatosCompra;
    }

    public interface ListenerComboPack {

        public void getPackProducto(List<PackElemento> packElementos, List<CategoriaPack> categoriasPack);
    }

    public interface ListenerComprasProducto {

        public void ObtenerProductosAlmacen(List<mProduct> productList);
    }

    public interface ListenerConfigProducto {
        void ConfigProducto(mProduct mproduct);
    }

    public interface ListenerConfiguracionPack {

        public void configuracionPack(List<ConfigPack> configPackList);

        public void ResultadoEliminar(byte resultadoEliminar);

        public void ResultadoInsertar(mProduct product);

        public void ResultadoBusqueda(List<mProduct> productList);
    }

    public void setListenerConfiguracionPack(ListenerConfiguracionPack listenerConfiguracionPack) {
        this.listenerConfiguracionPack = listenerConfiguracionPack;
    }

    public void setListenerComboPack(ListenerComboPack listenerComboPack) {
        this.listenerComboPack = listenerComboPack;
    }

    public void setProcesoVariantes(ProcesoVariantes procesoVariantes) {
        this.procesoVariantes = procesoVariantes;
    }


    public void ObtenerPreciosCategorias(int idPack) {
        new ObtenerPreciosCategorias().execute(new Integer[]{Integer.valueOf(idPack)});
    }

    public interface ProcesoVariantes {

        public void ObtenerVariantes(List<Variante> varianteList);

    }

    public void ObtenerProductosPack(int idProducto) {
        this.obtenerComboPackProducto = new ObtenerComboPackProducto();
        this.obtenerComboPackProducto.execute(new Integer[]{Integer.valueOf(idProducto)});
    }

    @Beta
    private class ObtenerPreciosCategorias extends AsyncTask<Integer, Void, List<CategoriaPack>> {

        private ObtenerPreciosCategorias() {
        }

        protected List<CategoriaPack> doInBackground(Integer... integers) {
//AsyncProducto.this.bdConnectionSql.GetCategoriasPrecio(integers[0].intValue());
            //API-DEBUG
            List<CategoriaPack> l = null;
            try {
                l = iCategoriaRepository.GetCategoriasPrecio(integers[0].intValue(), TIPO_CONSULTA, codeCia).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return l;
        }

        protected void onPostExecute(List<CategoriaPack> categoriaPacks) {
            super.onPostExecute(categoriaPacks);
            if (categoriaPacks == null) {
                return;
            }
            if (AsyncProducto.this.listenerPreciosCategorias == null) {
                return;
            }
            if (categoriaPacks == null) {
                AsyncProducto.this.listenerPreciosCategorias.ErrorBusquedaPrecioCategoria();
            } else if (categoriaPacks.size() > 0) {
                AsyncProducto.this.listenerPreciosCategorias.ListadoCategoriasPrecio(categoriaPacks);
            }
        }
    }

    public AsyncProducto() {

    }

    public interface GetProduct {

        public void GetCodigoGenerado(String codigoGenerado);

        public void ObtenerUnidadesMedida(List<String> unidadesMedida);

        public void GetProductResultById(mProduct product);

        public void ConfimarcionEliminar(byte Respuesta);
    }

    public void setListenerGetProduct(GetProduct getProduct) {

        this.getProduct = getProduct;

    }

    public void setRespuestaProducto(RespuestaProducto respuestaProducto) {

        this.respuestaProducto = respuestaProducto;

    }

    public void setListenerObtenerProductos(ObtenerProductos obtenerProductos) {
        this.obtenerProductos = obtenerProductos;
    }

    public interface ObtenerProductos {

        void ObtenerListaProductos(List<mProduct> mProductList);

    }

    public interface ActualizoEstadoComboRapido {
        void ErrorActualizarComboRapido();

        void ExitoActualizaComboRapido();
    }

    public interface RespuestaProducto {

        public void RespuestaGuardadoProducto(byte respuesta);

        public void ConfirmacionActualizarProducto(byte Resultado);
    }

    public AsyncProducto(Context context) {
        this.context = context;
        dialogCargaAsync = new DialogCargaAsync(context);
        mensaje = "";
        bdConnectionSql = BdConnectionSql.getSinglentonInstance();
        listaProducto = new ArrayList<>();
        campoNombreCodigo = "";
        idCategoria = 0;


    }

    public void GuardarPreciosPack(List<CategoriaPack> list, int idPack) {
        GuardarPreciosPack preciosPack = new GuardarPreciosPack();
        preciosPack.setIdPack(idPack);
        preciosPack.execute(new List[]{list});
    }

    ListenerGuardarPreciosCategoria listenerGuardarPreciosCategoria;

    public interface ListenerGuardarPreciosCategoria {
        void ErrorGuardarPrecioCategoria();

        void ExitoGuardarPrecioCategoria();
    }

    public void setListenerGuardarPreciosCategoria(ListenerGuardarPreciosCategoria listenerGuardarPreciosCategoria) {
        this.listenerGuardarPreciosCategoria = listenerGuardarPreciosCategoria;
    }

    private class GuardarPreciosPack extends AsyncTask<List<CategoriaPack>, Void, Integer> {
        int idPack;

        private GuardarPreciosPack() {
        }

        public void setIdPack(int idPack) {
            this.idPack = idPack;
        }

        protected Integer doInBackground(List<CategoriaPack>... lists) {
            return Integer.valueOf(AsyncProducto.this.bdConnectionSql.GuardarPrecioCategoria(lists[0], this.idPack));
        }

        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer.intValue() == 100) {
                AsyncProducto.this.listenerGuardarPreciosCategoria.ExitoGuardarPrecioCategoria();
            } else if (integer.intValue() == 99) {
                AsyncProducto.this.listenerGuardarPreciosCategoria.ErrorGuardarPrecioCategoria();
            }
        }
    }

    public void CancelarObtenerProductos() {

        if (downloadListProductVenta != null) {
            downloadListProductVenta.cancel(false);
        }

    }


    public void BusquedaProductosConfigAvanzada(byte metodo, String desc) {

        DescargaProductosConfigAvanzada d = new DescargaProductosConfigAvanzada();
        d.setMetodo(metodo);
        d.execute(desc);
    }

    private class DescargaProductosConfigAvanzada extends AsyncTask<String, Void, List<mProduct>> {

        byte metodo;


        public void setMetodo(byte metodo) {
            this.metodo = metodo;
        }

        @Override
        protected List<mProduct> doInBackground(String... strings) {
            return bdConnectionSql.ObtenerProductosConfigAvanzada(strings[0], metodo);
        }

        @Override
        protected void onPostExecute(List<mProduct> mProducts) {
            super.onPostExecute(mProducts);
            obtenerProductos.ObtenerListaProductos(mProducts);

        }
    }

    public void getObtenerProductosConfig(String campoBusqueda, byte metodoBusqueda, int idCategoria) {
        this.idCategoria = idCategoria;
        this.campoNombreCodigo = campoBusqueda;
        downloadListProductConfig = new DownloadListProductConfig();
        downloadListProductConfig.execute(metodoBusqueda);


    }

    public void getObtenerProductosVenta(String campoBusqueda, byte metodoBusqueda, int idCategoria) {
        this.idCategoria = idCategoria;
        this.campoNombreCodigo = campoBusqueda;
        downloadListProductVenta = new DownloadListProductVenta();
        downloadListProductVenta.execute(metodoBusqueda);


    }

    public void CancelarBusqueda() {

        if (downloadListProductVenta != null) {
            downloadListProductVenta.cancel(true);
        }

    }

    public void ActualizarSinVerificacionCodigo(mProduct product, boolean estadoConfigVaria) {
        this.estadoConfigVaria = estadoConfigVaria;
        new ActualizarProductoSinVerificacionCodigo().execute(product);

    }

    public void ActualizarConVerificacionCodigo(mProduct product, boolean estadoConfigVaria) {
        this.estadoConfigVaria = estadoConfigVaria;
        new ActualizarProductoConVerificacionCodigo().execute(product);
    }

    public void GenerarVariantes(int idProduct) {
        new GenerarVariantes().execute(idProduct);
    }

    public void AgregarMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void EliminarProducto(int idProducto) {
        new EliminarProducto().execute(idProducto);
    }

    public interface ResultadoGuardarProducto {
        public void GuardarProductoExito();

        public void ErrorGuardarProducto(String mensaje);
    }

    public void BuscarProductoPorIdImagen(int idProducto) {

        new ObtenerProducto().execute(idProducto);

    }

    public void getConfigPack(int idProductoPadre) {

        new ObtenerConfiguracionPack().execute(idProductoPadre);

    }


    public void EliminarProductoPack(ConfigPack configPack) {

        new EliminarProductoPack().execute(configPack);

    }

    public void AgregarProductoPack(ConfigPack configPack) {
        new AgregarProductoPack().execute(configPack);
    }


    public void CancelarBusquedaProductos() {

        if (obtenerComboPackProducto != null) {
            obtenerComboPackProducto.cancel(true);
        }

    }

    public void BusquedaProductoParametroPack(String parametro, int idCategoria, byte metodoBusqueda) {

        BusquedaParametroProducto busquedaParametroProducto = new BusquedaParametroProducto();
        busquedaParametroProducto.setIdCategoria(idCategoria);
        busquedaParametroProducto.setParametro(parametro);
        busquedaParametroProducto.setMetodoBusqueda(metodoBusqueda);
        busquedaParametroProducto.execute();
    }


    private class BusquedaParametroProducto extends AsyncTask<Void, Void, List<mProduct>> {

        int idCategoria;
        String parametro;
        byte metodoBusqueda;

        public int getIdCategoria() {
            return idCategoria;
        }

        public void setIdCategoria(int idCategoria) {
            this.idCategoria = idCategoria;
        }

        public String getParametro() {
            return parametro;
        }

        public void setParametro(String parametro) {
            this.parametro = parametro;
        }

        public byte getMetodoBusqueda() {
            return metodoBusqueda;
        }

        public void setMetodoBusqueda(byte metodoBusqueda) {
            this.metodoBusqueda = metodoBusqueda;
        }

        @Override
        protected List<mProduct> doInBackground(Void... voids) {
            return bdConnectionSql.BusquedaProductoCongifPack(metodoBusqueda, parametro, idCategoria);
        }

        @Override
        protected void onPostExecute(List<mProduct> productList) {
            super.onPostExecute(productList);
            listenerConfiguracionPack.ResultadoBusqueda(productList);
        }
    }

    private class AgregarProductoPack extends AsyncTask<ConfigPack, Void, mProduct> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected mProduct doInBackground(ConfigPack... configPacks) {

            return bdConnectionSql.IngresarProductoPack(configPacks[0]);
        }

        @Override
        protected void onPostExecute(mProduct product) {
            super.onPostExecute(product);
            listenerConfiguracionPack.ResultadoInsertar(product);
        }
    }

    private class EliminarProductoPack extends AsyncTask<ConfigPack, Void, Byte> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Byte doInBackground(ConfigPack... configPacks) {
            return bdConnectionSql.EliminarProductoPack(configPacks[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            listenerConfiguracionPack.ResultadoEliminar(aByte);
        }
    }

    private class ObtenerConfiguracionPack extends AsyncTask<Integer, Void, List<ConfigPack>> {

        mProduct product;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<ConfigPack> doInBackground(Integer... integers) {

            if (Constantes.TipoModificadoresPack.listadoModificadoresPack.isEmpty()) {
                Constantes.TipoModificadoresPack.listadoModificadoresPack.addAll(bdConnectionSql.GetTipoModificadoresPack());

            }
            product = bdConnectionSql.ConfigProduct(integers[0].intValue());
            return bdConnectionSql.getProductosPack(integers[0]);
        }

        @Override
        protected void onPostExecute(List<ConfigPack> configPacks) {
            super.onPostExecute(configPacks);
            listenerConfiguracionPack.configuracionPack(configPacks);
            listenerConfigProducto.ConfigProducto(product);
        }
    }

    private class EliminarProducto extends AsyncTask<Integer, Void, Byte> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarProducto(integers[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            getProduct.ConfimarcionEliminar(aByte);
        }


    }

    private class GenerarVariantes extends AsyncTask<Integer, Void, List<Variante>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Variante> doInBackground(Integer... integers) {
            return bdConnectionSql.GenerarVariantes(integers[0]);
        }

        @Override
        protected void onPostExecute(List<Variante> variantes) {
            super.onPostExecute(variantes);
            procesoVariantes.ObtenerVariantes(variantes);
        }
    }

    private class ActualizarProductoSinVerificacionCodigo extends AsyncTask<mProduct, Void, Byte> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = dialogCargaAsync.getDialogCarga(mensaje);
            dialog.show();
        }

        @Override
        protected Byte doInBackground(mProduct... mProducts) {
            byte respuesta = 0;

            respuesta = bdConnectionSql.ActualizarProducto(mProducts[0], estadoConfigVaria);

            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            respuestaProducto.ConfirmacionActualizarProducto(aByte);
            dialog.dismiss();
        }

    }

    private class ActualizarProductoConVerificacionCodigo extends AsyncTask<mProduct, Void, Byte> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = dialogCargaAsync.getDialogCarga(mensaje);
            dialog.show();
        }

        @Override
        protected Byte doInBackground(mProduct... mProducts) {
            byte respuesta = 0;
            respuesta = bdConnectionSql.VerificarCodigoProducto(mProducts[0].getcKey());
            if (respuesta == 0) {

                respuesta = bdConnectionSql.ActualizarProducto(mProducts[0], estadoConfigVaria);
            }

            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            respuestaProducto.ConfirmacionActualizarProducto(aByte);
            dialog.dismiss();
        }

    }

    private class GuardarProductoV2 extends AsyncTask<mProduct, Void, ResultProcces> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = dialogCargaAsync.getDialogCarga(mensaje);
            dialog.show();
        }

        @Override
        protected ResultProcces doInBackground(mProduct... mProducts) {
            return bdConnectionSql.GuardarNuevoProducto(mProducts[0]);
        }

        @Override
        protected void onPostExecute(ResultProcces resultProcces) {
            super.onPostExecute(resultProcces);
            dialog.dismiss();
            if (resultProcces.getCodeResult() == 200) {
                resultadoGuardarProducto.GuardarProductoExito();
            } else {
                resultadoGuardarProducto.ErrorGuardarProducto(resultProcces.getMessageResult());
            }


        }

    }

    private class GuardarProducto extends AsyncTask<mProduct, Void, Byte> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = dialogCargaAsync.getDialogCarga(mensaje);
            dialog.show();
        }

        @Override
        protected Byte doInBackground(mProduct... mProducts) {
            byte respuesta = 0;
            respuesta = bdConnectionSql.VerificarCodigoProducto(mProducts[0].getcKey());
            if (respuesta == 0) {
//                respuesta= bdConnectionSql.GuardarNuevoProducto(mProducts[0]);
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);

            dialog.dismiss();
            respuestaProducto.RespuestaGuardadoProducto(aByte);
        }
    }


    private class ObtenerProducto extends AsyncTask<Integer, Void, mProduct> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected mProduct doInBackground(Integer... integers) {
            return bdConnectionSql.ObtenerProductoId(integers[0]);
        }

        @Override
        protected void onPostExecute(mProduct product) {
            super.onPostExecute(product);
            getProduct.GetProductResultById(product);
        }

    }


    public void ObtenerProductosLista(String descripcion) {

        new DescargaListaProducto().execute(descripcion);
    }


    private class DescargaListaProducto extends AsyncTask<String, Void, List<mProduct>> {

        @Override
        protected List<mProduct> doInBackground(String... strings) {

            try {
                return iProductoRepository.GetProductosConfig(codeCia, TIPO_CONSULTA, strings[0]).execute().body();
            } catch (IOException ieox) {
                ieox.printStackTrace();
                return new ArrayList<>();
                //e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<mProduct> mProducts) {
            super.onPostExecute(mProducts);
            obtenerProductos.ObtenerListaProductos(mProducts);
        }
    }

    private class DownloadListProductConfig extends AsyncTask<Byte, Void, List<mProduct>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<mProduct> doInBackground(Byte... bytes) {
            if (bytes[0] == 100 || bytes[0] == 104 || bytes[0] == 109) {
                listaProducto = bdConnectionSql.ObtenerProductosConfiguracion(0, "", bytes[0]);
            } else if (bytes[0] == 103) {
                listaProducto = bdConnectionSql.ObtenerProductosConfiguracion(0, campoNombreCodigo, bytes[0]);
            } else if (bytes[0] == 105) {
                listaProducto = bdConnectionSql.ObtenerProductosConfiguracion(idCategoria, "", bytes[0]);
            } else if (bytes[0] == 108) {
                listaProducto = bdConnectionSql.ObtenerProductosConfiguracion(0, "", bytes[0]);
            } else if (bytes[0] == 110) {
                listaProducto = bdConnectionSql.ObtenerProductosConfiguracion(0, "", bytes[0]);

            }
            return listaProducto;
        }


        @Override
        protected void onPostExecute(List<mProduct> mProductList) {
            super.onPostExecute(mProductList);
            obtenerProductos.ObtenerListaProductos(mProductList);
        }
    }

    private class DownloadListProductVenta extends AsyncTask<Byte, Void, List<mProduct>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<mProduct> doInBackground(Byte... bytes) {
            IProductoRepository iProductoRepository = retro.create(IProductoRepository.class);
            String temp = GetJsonCiaTiendaBase64x3();

            if (bytes[0] == 100 || bytes[0] == 104 || bytes[0] == 109) {
                try {
                    Call<List<mProduct>> result = iProductoRepository.GetProductosVenta("2", temp, bytes[0], 0, "");

                    listaProducto = result.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.toString();
                }
                // listaProducto = bdConnectionSql.ObtenerProductosVentasV2(0, "", bytes[0]);
            } else if (bytes[0] == 103) {
                try {
                    Call<List<mProduct>> result = iProductoRepository.GetProductosVenta("2", temp, 103, 0, campoNombreCodigo);

                    listaProducto = result.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.toString();
                }
                //    listaProducto=bdConnectionSql.ObtenerProductosVentasV2(0,campoNombreCodigo,bytes[0]);
            } else if (bytes[0] == 105) {
                try {

                    listaProducto = iProductoRepository.GetProductosVenta("2", temp, 105, idCategoria, "").execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.toString();
                }
                //      listaProducto=bdConnectionSql.ObtenerProductosVentasV2(idCategoria,"",bytes[0]);
            } else if (bytes[0] == 108) {
                //  listaProducto=bdConnectionSql.ObtenerProductosVentasV2(0,"",bytes[0]);
                try {

                    Response<List<mProduct>> response = iProductoRepository.GetProductosVenta("2", temp, 108, idCategoria, "").execute();
                    listaProducto = response.body();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.toString();
                }
            } else if (bytes[0] == 110) {
                //         listaProducto=bdConnectionSql.ObtenerProductosVentasV2(0,"",bytes[0]);
                try {
                    Call<List<mProduct>> result = iProductoRepository.GetProductosVenta("2", temp, bytes[0], 0, "");
                    listaProducto = result.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ex) {
                    ex.toString();
                }
            }

            return listaProducto;
        }


        @Override
        protected void onPostExecute(List<mProduct> mProductList) {
            super.onPostExecute(mProductList);
            obtenerProductos.ObtenerListaProductos(mProductList);
        }
    }

    private class ObtenerComboPackProducto extends AsyncTask<Integer, Void, List<PackElemento>> {
        List<CategoriaPack> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<PackElemento> doInBackground(Integer... integers) {

            try {
                ResultPack result = iProductoRepository.GetContenidoPack(
                        TIPO_CONSULTA,
                        codeCia, integers[0]).execute().body();
                list = result.getPrecioCategoria();
                return result.getDetallePack();

            } catch (IOException e) {
                e.printStackTrace();
                list = new ArrayList<>();
                return new ArrayList<>();


            } catch (Exception ex) {
                ex.printStackTrace();
                list = new ArrayList<>();
                return new ArrayList<>();

            }
            /*
            list=bdConnectionSql.GetCategoriasPrecio(integers[0]);
            return bdConnectionSql.ObtenerPackProductos(integers[0]);
            */

        }

        @Override
        protected void onPostExecute(List<PackElemento> packElementos) {
            super.onPostExecute(packElementos);
            if (listenerComboPack != null) {
                listenerComboPack.getPackProducto(packElementos, list);
            }
        }

    }

    public void ObtenerCodigoGenerado() {

        generarCodigoProducto = new GenerarCodigoProducto();
        generarCodigoProducto.execute();

    }

    @Beta
    public class GenerarCodigoProducto extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            try {
                //API-DEBUG
                Call<ResponseCodProducto> call= iProductoRepository.GenerarCodigoNumericoProducto(TIPO_CONSULTA, codeCia);

                Response<ResponseCodProducto> response = call.execute();
                return response.body().getCodigo();
            } catch (Exception e) {
                return "";
            }
            //   return bdConnectionSql.GenerarCodigoNumericoProducto();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (getProduct != null) {
                getProduct.GetCodigoGenerado(s);
            }
        }
    }


    public void ObtenerPorductosAlmacen(String descripcion) {

        obtenerProductosCompra = new ObtenerProductosCompra();
        obtenerProductosCompra.setDescripcion(descripcion);
        obtenerProductosCompra.execute();

    }

    ListenerProductosAlmacen listenerProductosAlmacen;

    public void setListenerProductosAlmacen(ListenerProductosAlmacen listenerProductosAlmacen) {
        this.listenerProductosAlmacen = listenerProductosAlmacen;
    }

    public interface ListenerProductosAlmacen {

        public void ListadoProductosAlmacen(List<mProduct> productList);

    }

    public void ObtenerProductosEnAlmacen(int idAlmacenOrigen, String descripcion) {

        obtenerProductosEnAlmacen = new ObtenerProductosEnAlmacen();
        obtenerProductosEnAlmacen.setDescripcion(descripcion);
        obtenerProductosEnAlmacen.execute(idAlmacenOrigen);

    }

    private class ObtenerProductosEnAlmacen extends AsyncTask<Integer, Void, List<mProduct>> {
        byte respuesta;
        List<mProduct> productList;

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        String descripcion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            respuesta = 98;
            productList = new ArrayList<>();
        }

        @Override
        protected List<mProduct> doInBackground(Integer... integers) {

            productList = bdConnectionSql.ProductosEnAlmacenSeleccionado(integers[0], descripcion);
            return productList;
        }

        @Override
        protected void onPostExecute(List<mProduct> productList) {
            super.onPostExecute(productList);
            if (listenerProductosAlmacen != null) {
                listenerProductosAlmacen.ListadoProductosAlmacen(productList);
            }
        }
    }


    private class ObtenerProductosCompra extends AsyncTask<Void, Void, List<mProduct>> {

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        private String descripcion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<mProduct> doInBackground(Void... voids) {
            return bdConnectionSql.ProductosParaCompra(descripcion);
        }

        @Override
        protected void onPostExecute(List<mProduct> productList) {
            super.onPostExecute(productList);
            if (listenerComprasProducto != null) {
                listenerComprasProducto.ObtenerProductosAlmacen(productList);
            }
        }
    }

    public void ObtenerDatosCompraProducto(int idProducto) {

        obtenerDatosCompraProducto = new ObtenerDatosCompraProducto();
        obtenerDatosCompraProducto.execute(idProducto);
    }

    private class ObtenerDatosCompraProducto extends AsyncTask<Integer, Void, Void> {

        BigDecimal precioCompra;
        List<mAlmacen> listaAlmacens;

        @Override
        protected Void doInBackground(Integer... integers) {

            precioCompra = bdConnectionSql.obtenerPrecioCompraProducto(integers[0]);
            listaAlmacens = bdConnectionSql.obtenerAlmacenesProducto(integers[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (listenerDatosCompra != null) {
                listenerDatosCompra.ObtenerDatosProductoEnAlmacen(precioCompra, listaAlmacens);
            }
        }
    }

    public void ActualizarProductoEnPedido(int idCabeceraPedido, ProductoEnVenta productoEnVenta) {

        ActualizarProductoDetallePedido a = new ActualizarProductoDetallePedido();
        a.setIdCabeceraPedido(idCabeceraPedido);
        a.execute(productoEnVenta);


    }

    private class ActualizarProductoDetallePedido extends AsyncTask<ProductoEnVenta, Void, ProductoEnVenta> {

        private int idCabeceraPedido;

        public void setIdCabeceraPedido(int idCabeceraPedido) {
            this.idCabeceraPedido = idCabeceraPedido;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.IniciarDialogCarga("Actualizando pedido");
            }
        }

        @Override
        protected ProductoEnVenta doInBackground(ProductoEnVenta... productoEnVentas) {
            SolicitudEnvio<ProductoEnVenta> productoEnVentaSolicitudEnvio = new SolicitudEnvio<>(codeCia, TIPO_CONSULTA, productoEnVentas[0], Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            try {
                String jsonT = new Gson().toJson(productoEnVentaSolicitudEnvio).toString();
                return iPedidoRespository.EditarCantidadProducto(productoEnVentaSolicitudEnvio, idCabeceraPedido).execute().body();
            } catch (IOException e) {
                //    e.printStackTrace();
                ProductoEnVenta productoEnVenta = new ProductoEnVenta();
                productoEnVenta.setIdDetallePedido(-99);
                return productoEnVenta;
            } catch (Exception ex) {
                ProductoEnVenta productoEnVenta = new ProductoEnVenta();
                productoEnVenta.setIdDetallePedido(-99);
                return productoEnVenta;
            }
            //    return  bdConnectionSql.EditarCantidadProducto(productoEnVentas[0],idCabeceraPedido);
        }

        @Override
        protected void onPostExecute(ProductoEnVenta productoEnVenta) {
            super.onPostExecute(productoEnVenta);
            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if (listenerActualizarCantidad != null) {
                listenerActualizarCantidad.ResultadoActualizar(productoEnVenta);
            }

        }
    }

    public void ActualizarCantidadProductoPedido(int idCabeceraPedido, int idDetallePedido, float cantidad) {

        actualizarCantidadProductoPedido = new ActualizarCantidadProductoPedido();
        actualizarCantidadProductoPedido.setIdCabeceraPedido(idCabeceraPedido);
        actualizarCantidadProductoPedido.setIdDetallePedido(idDetallePedido);
        actualizarCantidadProductoPedido.setCantidad(cantidad);
        actualizarCantidadProductoPedido.execute();

    }

    ListenerActualizarCantidad listenerActualizarCantidad;

    public interface ListenerActualizarCantidad {

        public void ResultadoActualizar(ProductoEnVenta productoEnVenta);

    }

    public void setListenerActualizarCantidad(ListenerActualizarCantidad listenerActualizarCantidad) {
        this.listenerActualizarCantidad = listenerActualizarCantidad;
    }

    private class ActualizarCantidadProductoPedido extends AsyncTask<Void, Void, ProductoEnVenta> {

        int idCabeceraPedido;
        int idDetallePedido;
        float cantidad;

        public void setIdCabeceraPedido(int idCabeceraPedido) {
            this.idCabeceraPedido = idCabeceraPedido;
        }

        public void setIdDetallePedido(int idDetallePedido) {
            this.idDetallePedido = idDetallePedido;
        }

        public void setCantidad(float cantidad) {
            this.cantidad = cantidad;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.IniciarDialogCarga("Actualizando pedido");
            }
        }

        @Override
        protected ProductoEnVenta doInBackground(Void... voids) {
            return bdConnectionSql.EditarCantidadProducto(new ProductoEnVenta(), idCabeceraPedido);
        }

        @Override
        protected void onPostExecute(ProductoEnVenta productoEnVenta) {
            super.onPostExecute(productoEnVenta);
            if (controladorProcesoCargar != null) {
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if (listenerActualizarCantidad != null) {
                listenerActualizarCantidad.ResultadoActualizar(productoEnVenta);
            }

        }
    }

    ListenerDisponibilidadVariantes listenerDisponibilidadVariantes;

    public void setListenerDisponibilidadVariantes(ListenerDisponibilidadVariantes listenerDisponibilidadVariantes) {

        this.listenerDisponibilidadVariantes = listenerDisponibilidadVariantes;

    }

    public interface ListenerDisponibilidadVariantes {

        public void ResultadoProducto(mProduct product);

        public void ErrorConsulta();

    }

    public void ObtenerDisponibilidadProductoVariante(int idProducto, List<String> productos) {
        obtenerDiponiblesVariantes = new ObtenerDiponiblesVariantes(idProducto, productos);
        obtenerDiponiblesVariantes.execute();
    }

    private class ObtenerDiponiblesVariantes extends AsyncTask<Void, Void, mProduct> {

        int idProducto;
        List<String> descripciones;

        public ObtenerDiponiblesVariantes(int idProducto, List<String> descripciones) {
            this.idProducto = idProducto;
            this.descripciones = descripciones;
        }

        @Override
        protected mProduct doInBackground(Void... aVoid) {
            try {
                return iVarianteRepo.ObtenerCantidadDisponibleVariante(codeCia, TIPO_CONSULTA, idProducto, descripciones.get(0), descripciones.get(1), descripciones.get(2)).execute().body();

            } catch (Exception ex) {
                mProduct product = new mProduct();
                product.setIdProduct(-99);
                return product;
            }
        }

        @Override
        protected void onPostExecute(mProduct mProduct) {
            super.onPostExecute(mProduct);

            if (listenerDisponibilidadVariantes != null) {
                if (mProduct.getIdProduct() != -99) {
                    listenerDisponibilidadVariantes.ResultadoProducto(mProduct);
                } else if (mProduct.getIdProduct() == -99) {
                    listenerDisponibilidadVariantes.ErrorConsulta();
                }
            }

        }
    }
}
