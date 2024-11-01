package com.omarchdev.smartqsale.smartqsaleventas.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ConfigTienda.bVisibleBtnCambioPantalla;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.MediosPago.mediosPago;
import static com.omarchdev.smartqsale.smartqsaleventas.Control.UtilKt.GenerarFecha;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.HistorialVentas;
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.PedidosEnReserva;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCabeceraVenta;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCaja;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncPedido;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProcesoVenta;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncZonaServicio;
import com.omarchdev.smartqsale.smartqsaleventas.CategoriaAdapter;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.HttpConsultas;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorMediosPago;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorVentas;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.FacturaActivaController;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.Numero_a_Letra;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.TaskNotificationPedido;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DateTimePicker;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfCalculadorPeso;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfEditProductCarSale;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfEditProductTiempoPedido;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfProductoControlTiempoPedido;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfUltimosPedidosZonaServicio;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.Df_Select_Zona_Servicio;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAlertaStock;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAperturaCaja;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogDetalleCarrito;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogEditQuantity;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogGuardarPedido;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogScannerCam;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogSelectCombo;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogSelectModProducto;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogSelectPrecioAdic;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogVariantesProducto;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogVentaResultado;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.SelectProductTime;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.cProgressDialog;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.df_venta_rapida;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogCalculadoraDescuento;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogCobroVenta;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogCobroVenta.ListenerVentaFinalizada;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogSelectCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogSelectVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.InterfaceDetalleCarritoVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.CategoriaPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DecimalControlKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.InfoGuardadoPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.PackElemento;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Pedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResZonaServicio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultadoComprobante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.RetornoApertura;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.TimeData;
import com.omarchdev.smartqsale.smartqsaleventas.Model.VarianteBusqueda;
import com.omarchdev.smartqsale.smartqsaleventas.Model.VentaGeneracion;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDocVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRespuestaVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUnidadMedida;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mZonaServicio;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICajaRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapter;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterArticuloGrid2;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterCarSale;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvGridAdapterCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.rvAdapterGridArticulo;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VentasFragment extends Fragment implements DialogGuardarPedido.CapturaDato, dialogSelectCustomer.DatosCliente, InterfaceDetalleCarritoVenta, View.OnClickListener, FloatingActionsMenu.OnFloatingActionsMenuUpdateListener,
        DialogDetalleCarrito.DetalleVentaInterface, RvAdapterCarSale.PasarCantidad,
        dialogSelectVendedor.InformacionVendedor, dialogCalculadoraDescuento.Descuento,
        ListenerVentaFinalizada, DialogVentaResultado.ListenerTerminarVenta,
        DialogAperturaCaja.AperturaCaja, AsyncCaja.ListenerAperturaCaja, AsyncProducto.ListenerComboPack,
        DialogSelectCombo.ListenerSelectCombo, AsyncProcesoVenta.ListenerPackSeleccion,
        AsyncProcesoVenta.ListenerProductoSeleccionado, AsyncProcesoVenta.ListenerModificadorProductoSeleccion,
        DialogSelectModProducto.ListenerModProdSeleccion, RvAdapter.DatosSeleccionProductoLista,
        RvAdapterCarSale.ListenerDetalleVenta, RvAdapterCarSale.ListenerCarSale, AsyncPedido.ResultAforo {
    static final int CODE_REQUEST_RESULT = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    static int heigth;
    static ImageButton imgSelectViewTypeList;
    static Button btnCancelarVenta;
    boolean generaDocumento;
    int tp;
    BigDecimal montoPromocion;
    mZonaServicio s;
    String textoZonaServicio = "Vehículo";
    AsyncZonaServicio asyncZonaServicio;
    EditText edtCodigoBarra, edtPlaca, edtHoraIngreso, edtObservacion, edtHoraSalida, edtTiempoTranscurrido, edtEtapaCont, edtEspaciosLibres;
    EditText searchBox;
    AsyncCabeceraVenta asyncCabeceraVenta;
    DbHelper helper;
    DialogSelectModProducto dialogSelectModProducto;
    DialogSelectCombo dialogSelectCombo;
    Dialog dialogProceso;
    AlertDialog.Builder dialogAlerta;
    FacturaActivaController facturaActivaController;
    cProgressDialog progressDialog;
    int idProduct;
    boolean permitirModificarDatosProductoUnico;
    String mensajeNoCobrar;
    boolean permitirCobrar;
    ImageButton imgBackGrid;
    RvGridAdapterCategoria adapterCategoria;
    AsyncCategoria asyncCategoria;
    List<mMedioPago> medioPagoList;
    DialogCargaAsync dialogCargaAsync;
    //  BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    LinearLayout lcontent_venta;
    String simboloMoneda, observacion, textoCantidad;
    AsyncPedido asyncPedido;
    mVendedor vendedor;
    ImageView imgCandado;
    BigDecimal montoApertura, CantidadCambio, CantidadCobrar, CantidadDescuento, CobrarSinDescuento, cantidadADescontar;
    dialogSelectCustomer selectCustomer;
    dialogSelectVendedor selectVendedor;
    boolean EstadoBloqueoVendedor = false;
    DialogAperturaCaja dialogAperturaCaja;
    List<ProductoEnVenta> productoEnVentaList;
    List<ProductoEnVenta> listTemporal = new ArrayList<>();
    mProduct product;
    ControladorProductos controladorProductos;
    DetalleVenta detalleVenta;
    ImageButton imgBtnScan, btnScanPlaca;
    ImageView imgArrowDisplay;
    boolean permitirGuardarPedido;
    List<mProduct> productList;
    RecyclerView rv;
    Numero_a_Letra a;
    RvAdapter rvAdapter;
    ControladorVentas controladorVentas;
    mProduct Product;
    TextView textoCarrito, txtDescuentoVenta, txtAforoLibre;
    Button btnElegirCliente, btnModoVenta, btnTipoLista, btnHabMod;
    byte TipoDescuento;
    byte tipoVistaArticulos;
    String parametroBusqueda;
    mCabeceraPedido cabeceraPedido;
    ProgressBar pb;
    View f;
    Button btnCobrar, btnElegirVendedor, btnZonaServicio;
    TextView txtNombreUltimoProductoEnCarrito, txtPrecioUltimoProductoEnCarrito, txtCantidadProducto, txtEstadoPermitir;
    ViewGroup transantioContent;
    LinearLayout linearLayoutbtnDetalle;
    SlidingUpPanelLayout slidingUpPanelLayout;
    mCustomer cliente;
    String ultimoProducto, ultimoPrecio;
    ImageButton btnOpenDialogSalvarPedido, btnLimpiar;
    RelativeLayout rvVentas;
    RecyclerView rvDetalleVenta;
    RvAdapterCarSale adapterDetalleVenta;
    boolean descargaDatos;
    View background_dimmer;
    Dialog dialog;
    LinearLayout linearLayout, content_aforo;
    int idCabeceraActual = 0;
    AsyncCaja asyncCaja;
    TransitionSet set;
    AsyncProducto asyncProducto;
    Spinner spinnerCategorias;
    CategoriaAdapter categoriaAdapter;
    List<mCategoriaProductos> listaCategorias;
    Context context;
    DialogVariantesProducto dialogVariantesProducto;
    ImagenesController imagenesController;
    AsyncProcesoVenta asyncProcesoVenta;
    View rootView;
    SearchView edtSearchProduct;
    RelativeLayout content_venta1, content_venta2;
    int posEliminar = 0;
    RecyclerView rvGridarticulosVenta;
    RvAdapterArticuloGrid2 rvAdapterArticuloGrid2;
    AVLoadingIndicatorView avi;
    TaskNotificationPedido taskNotificationPedido;
    DialogAlertaStock dialogAlertaStock;
    int posSelect = 0;
    String nombreProducto = "";
    String TipoPantallaPedido;
    ImageView imgTipoLista;
    mProduct productoTempTiempoVehiculo;
    String tiempoTempVehiculo = "";
    ProductoEnVenta productoEnVentaTempVehiculo;
    TimeData timeDataInicio, timeDataSalida;
    ImageButton btnHoraFin, btnHoraInit;
    boolean estadoModProductoUnico;
    BigDecimal montoDetraccion;
    BigDecimal porcentajeDetraccion;
    String cuentaDetraccion;
    boolean usaDetraccion;
    AsyncProcesoVenta.ResultadoCodigoBarraBusqueda listenerBusquedaCodigoBarra =
            new AsyncProcesoVenta.ResultadoCodigoBarraBusqueda() {
                @Override
                public void NoExisteProducto(ProductoEnVenta productoEnVenta) {
                    MostrarMensajeAlerta(productoEnVenta.getProductName());
                }

                @Override
                public void NoExisteStock(ProductoEnVenta productoEnVenta) {
                    MostrarMensajeAlerta(productoEnVenta.getProductName());
                }

                @Override
                public void ProductoNormalAgregado(ProductoEnVenta productoEnVenta) {
                    try {
                        GuardarProductoNormalDetallePedido(productoEnVenta);

                        Snackbar.make(rootView, "Agregado:" + productoEnVenta.getProductName(), Snackbar.LENGTH_SHORT).
                                setAction("Listo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setActionTextColor(getResources().getColor(R.color.colorCinco)).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, productoEnVenta.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void ProductoNormalAgregadoEditar(ProductoEnVenta productoEnVenta) {
                    try {
                        GuardarProductoNormalDetallePedido(productoEnVenta);

                        Snackbar.make(rootView, "Agregado:" + productoEnVenta.getProductName(), Snackbar.LENGTH_SHORT)
                                .setAction("Listo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setActionTextColor(getResources().getColor(R.color.colorCinco)).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, productoEnVenta.toString(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void ProductoVarianteAgregado(ProductoEnVenta productoEnVenta) {

                    try {
                        GuardarProductoNormalDetallePedido(productoEnVenta);

                        Snackbar.make(rootView, "Agregado:" + productoEnVenta.getProductName(),
                                Snackbar.LENGTH_SHORT).setAction("Listo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, productoEnVenta.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void ProductoVarianteEditado(ProductoEnVenta productoEnVenta) {
                    try {
                        GuardarProductoNormalDetallePedido(productoEnVenta);

                        Snackbar.make(rootView, "Agregado:" + productoEnVenta.getProductName(), Snackbar.LENGTH_SHORT)
                                .setAction("Listo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).setActionTextColor(getResources().getColor(R.color.colorCinco)).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, productoEnVenta.toString(), Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void ProductoPadreVariante(ProductoEnVenta productoEnVenta) {

                }

                @Override
                public void ProductoEsPack(ProductoEnVenta productoEnVenta) {
                    mProduct product = new mProduct();
                    product.setIdProduct(productoEnVenta.getIdProducto());
                    product.setcProductName(productoEnVenta.getProductName());
                    product.setPrecioVenta(productoEnVenta.getPrecioVentaFinal());

                    AbrirDialogComboPack(product);

                }

                @Override
                public void ProductoTieneModificador(ProductoEnVenta productoEnVenta) {

                }

                @Override
                public void RepiteCodigoBarra(ProductoEnVenta productoEnVenta) {

                    MostrarMensajeAlerta(productoEnVenta.getProductName());

                }

                @Override
                public void ErrorConexion() {

                }

                @Override
                public void ErrorProcedimiento() {

                }
            };
    TextWatcher listenerCodigoBarra = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            CapturarCodigoBarra(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    AdapterView.OnItemSelectedListener listenerCategorias = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i) {

                case 0:
                    asyncProducto.getObtenerProductosVenta("", (byte) 108, 0);
                    break;

                case 1:
                    asyncProducto.getObtenerProductosVenta("", (byte) 104, 0);
                    break;

                default:
                    asyncProducto.getObtenerProductosVenta("", (byte) 105, (int) adapterCategoria.getItemId(i));
                    break;

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    //Se reciben los valores de seleccion variantes
    DialogVariantesProducto.ListenerValoresSeleccionadas listenerValoresSeleccionadas = new DialogVariantesProducto.ListenerValoresSeleccionadas() {
        @Override
        public void obtenerValores(List<String> valoresSeleccionados, int idProduct, float CantidadSeleccionada, boolean multiPv, BigDecimal precio) {
            dialogProceso = dialogCargaAsync.getDialogCarga("Obteniedo variante de producto");
            dialogProceso.show();
            dialogProceso.setCanceledOnTouchOutside(false);
            VarianteBusqueda varianteBusqueda = new VarianteBusqueda();
            varianteBusqueda.setMutiPv(multiPv);
            varianteBusqueda.setPv(precio);
            varianteBusqueda.setIdProducto(idProduct);
            varianteBusqueda.setCantidadSeleccionada(CantidadSeleccionada);
            varianteBusqueda.setIdCabeceraPedido(idCabeceraActual);
            varianteBusqueda.setVariablesBusqueda(valoresSeleccionados);

            asyncProcesoVenta.BusquedaVariante(varianteBusqueda);

        }
    };
    //Obtener resultado guardar Variante en detalle pedido
    AsyncProcesoVenta.ProductoSeleccionadoVariante listenerSeleccionProductoVariante = new AsyncProcesoVenta.ProductoSeleccionadoVariante() {
        @Override
        public void ResultadoSeleccion(ProductoEnVenta productoEnVenta) {
            dialogProceso.hide();
            if (productoEnVenta.getIdProducto() != 0) {
                adapterDetalleVenta.addElementVariante(productoEnVenta);
                adapterDetalleVenta.setNumeroItem(detalleVenta.getUltimoProductoIngresado().getItemNum() + 1);
                cambiarTextoUltimoProductoIngresado();
            } else {
                MostrarMensajeAlerta("El producto no se encuentra disponible");
            }
        }
    };
    AsyncProducto.ObtenerProductos listenerObtenerProductos = new AsyncProducto.ObtenerProductos() {
        @Override
        public void ObtenerListaProductos(List<mProduct> mProductList) {
            pb.setVisibility(View.GONE);
            setListenerText();
            if (mProductList != null) {
                if (tipoVistaArticulos == 2) {

                    edtSearchProduct.setVisibility(View.VISIBLE);
                    gvCategoria.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                    imgBackGrid.setVisibility(View.VISIBLE);
                    // gridview.setVisibility(View.VISIBLE);
                    rvGridarticulosVenta.setVisibility(View.VISIBLE);

                    CargarGridEnPantallaList(mProductList);

                } else if (tipoVistaArticulos == 1) {
                    gvCategoria.setVisibility(View.GONE);
                    //  gridview.setVisibility(View.GONE);
                    rvGridarticulosVenta.setVisibility(View.GONE);
                    edtSearchProduct.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                    CargarListaEnPantallaLista(mProductList);

                }
                if (mProductList.size() == 0) {

                }

            } else {
                Toast.makeText(context, "Error al descargar la informacion.Verifique su conexion", Toast.LENGTH_LONG).show();

            }
        }
    };
    AsyncCategoria.ListenerCategoria listenerCategoria = new AsyncCategoria.ListenerCategoria() {
        @Override
        public void CategoriasObtenidas(List<mCategoriaProductos> categoriaProductosList) {
            pb.setVisibility(View.GONE);
            if (categoriaProductosList != null) {
                if (tipoVistaArticulos == 2) {
                    gvCategoria.setVisibility(View.VISIBLE);


                    rvGridarticulosVenta.setVisibility(View.GONE);
                    imgBackGrid.setVisibility(View.GONE);
                    adapterCategoria.AddElement(categoriaProductosList);
                    spinnerCategorias.setVisibility(View.GONE);


                } else if (tipoVistaArticulos == 1) {

                    spinnerCategorias.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                    listaCategorias.clear();
                    listaCategorias = categoriaProductosList;
                    categoriaAdapter.AddElementSpinnerVentas(listaCategorias);
                    spinnerCategorias.setSelection(0);
                    pb.setVisibility(View.GONE);

                }


            } else {

                Toast.makeText(context, "Error al descargar la informacion.Verifique su conexion", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void ObtenerUnidadesMedidad(List<mUnidadMedida> listaUnidades) {

        }
    };
    private int idTipoAtencionP;
    private GridView gridview, gvCategoria;
    private rvAdapterGridArticulo rvAdapterGridArticulo;

    public VentasFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ventas, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionScan:

                MostrarVentanaVentaRapida();
                return true;
            case R.id.actionHistorialPedidos:
                if (detalleVenta.getLongitud() == 0) {
                    MostrarHistorialPedidos();
                } else if (detalleVenta.getLongitud() > 0) {
                    mostrarDialogGuardarPedido(true);

                }
                return true;

            case R.id.actionHistorialVentas:
                if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.HistorialVenta)) {
                    MostrarHistorialVentas();
                } else {
                    Toast.makeText(context, "No permitido", Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ventas, container, false);
        TipoPantallaPedido = Constantes.ConfigTienda.cTipoPantallaPedido;
        try {
            content_aforo = rootView.findViewById(R.id.content_aforo);
            txtAforoLibre = rootView.findViewById(R.id.txtAforoLibre);
            edtPlaca = rootView.findViewById(R.id.edtPlaca);
            btnHoraFin = rootView.findViewById(R.id.btnHoraFin);
            btnHoraInit = rootView.findViewById(R.id.btnHoraInit);
            btnLimpiar = rootView.findViewById(R.id.btnLimpiar);
            edtPlaca.setMaxLines(1);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(7);
            timeDataInicio = new TimeData();
            timeDataSalida = new TimeData();
            edtPlaca.setFilters(filterArray);
            edtHoraIngreso = rootView.findViewById(R.id.edtHoraIngreso);
            imgTipoLista = rootView.findViewById(R.id.imgTipoLista);
            imgTipoLista.setOnClickListener(this);
            asyncPedido = new AsyncPedido(getContext());
            setHasOptionsMenu(true);
            dialogSelectModProducto = new DialogSelectModProducto();
            dialogSelectCombo = new DialogSelectCombo();
            dialogAlerta = new AlertDialog.Builder(getActivity());
            asyncProcesoVenta = new AsyncProcesoVenta();
            imagenesController = new ImagenesController();
            asyncCaja = new AsyncCaja(getContext());
            asyncCaja.setListenerAperturaCaja(this);
            asyncProducto = new AsyncProducto(getContext());
            asyncProducto.setListenerObtenerProductos(listenerObtenerProductos);
            montoApertura = new BigDecimal(0);
            btnHabMod = rootView.findViewById(R.id.btnHabMod);
            dialogCargaAsync = new DialogCargaAsync(getContext());
            dialogAperturaCaja = new DialogAperturaCaja();
            btnTipoLista = rootView.findViewById(R.id.btnTipoLista);
            dialogAperturaCaja.setAperturaCaja(this);
            a = new Numero_a_Letra();
            txtEstadoPermitir = rootView.findViewById(R.id.txtEstadoPermitir);
            rvVentas = rootView.findViewById(R.id.rVentas);
            gridview = rootView.findViewById(R.id.gv_articulosVenta);
            gvCategoria = rootView.findViewById(R.id.gv_Categorias);
            edtHoraSalida = rootView.findViewById(R.id.edtHoraSalida);
            edtTiempoTranscurrido = rootView.findViewById(R.id.edtTiempoTranscurrido);
            edtEtapaCont = rootView.findViewById(R.id.edtEtapaCont);
            edtEspaciosLibres = rootView.findViewById(R.id.edtEspaciosLibres);
            controladorVentas = new ControladorVentas();
            edtCodigoBarra = rootView.findViewById(R.id.edtCodigoBarra);
            btnModoVenta = rootView.findViewById(R.id.btnModoVenta);
            btnScanPlaca = rootView.findViewById(R.id.btnScanPlaca);
            //--Detalle Venta----//
            context = getContext();
            linearLayout = rootView.findViewById(R.id.layoutInfo);
            rvAdapterGridArticulo = new rvAdapterGridArticulo(getContext());
            gridview.setAdapter(rvAdapterGridArticulo);
            tipoVistaArticulos = 2; //  1 para mostrar como lista  --- 2 para mostrar como grid
            CantidadCobrar = new BigDecimal(0);
            //bdConnectionSql = BdConnectionSql.getSinglentonInstance();
            imgCandado = rootView.findViewById(R.id.imgLock);

            try {

                edtSearchProduct = rootView.findViewById(R.id.edtSearchProduct);

            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            imgArrowDisplay = rootView.findViewById(R.id.imgArrowDisplay);
            cliente = new mCustomer();
            detalleVenta = new DetalleVenta();
            descargaDatos = false;
            simboloMoneda = Constantes.DivisaPorDefecto.SimboloDivisa;
            ultimoProducto = "No se ingreso producto";
            ultimoPrecio = simboloMoneda + "0.00";
            parametroBusqueda = "";
            CantidadCobrar = new BigDecimal(0);
            CantidadDescuento = new BigDecimal(0);
            textoCantidad = "0.0";
            TipoDescuento = 0;
            cantidadADescontar = new BigDecimal(0);
            CobrarSinDescuento = new BigDecimal(0);
            productList = new ArrayList<>();
            productoEnVentaList = new ArrayList<>();
            txtDescuentoVenta = rootView.findViewById(R.id.txtDescuentoPrecioVenta);
            f = rootView.findViewById(R.id.background_dimmer);
            rv = rootView.findViewById(R.id.rvProductsInSale);
            vendedor = new mVendedor();
            selectVendedor = new dialogSelectVendedor();
            selectCustomer = new dialogSelectCustomer();
            txtCantidadProducto = rootView.findViewById(R.id.txtCantidadProducto);
            linearLayoutbtnDetalle = rootView.findViewById(R.id.linearLayoutBtnDetalleVenta);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            transantioContent = rootView.findViewById(R.id.content_Text);
            textoCarrito = rootView.findViewById(R.id.txtTextoTotalVenta);
            btnElegirVendedor = rootView.findViewById(R.id.btnElegirVendedor);
            edtObservacion = rootView.findViewById(R.id.edtObservacion);
            btnElegirVendedor.setOnClickListener(this);
            Product = new mProduct();
            btnElegirCliente = rootView.findViewById(R.id.btnElegirCliente);
            btnCancelarVenta = rootView.findViewById(R.id.btnCancelarVenta);
            dialogVariantesProducto = new DialogVariantesProducto();
            selectVendedor.setListenerVendedor(this);
            selectCustomer.setListenerCliente(this);
            txtDescuentoVenta.setOnClickListener(this);
            txtNombreUltimoProductoEnCarrito = rootView.findViewById(R.id.txtNombreUltimoProductoEnCarrito);
            txtPrecioUltimoProductoEnCarrito = rootView.findViewById(R.id.txtPrecioUltimoProductoEnCarrito);
            imgBackGrid = rootView.findViewById(R.id.imgBackGrid);
            imgSelectViewTypeList = rootView.findViewById(R.id.imgTipoVistaLista);
            imgBtnScan = rootView.findViewById(R.id.imgBtnScan);
            imgBtnScan.setOnClickListener(this);
            rv = rootView.findViewById(R.id.rvProductsInSale);
            btnOpenDialogSalvarPedido = rootView.findViewById(R.id.btnOpenGuardarPedido);
            controladorProductos = new ControladorProductos(getContext());
            rvAdapter = new RvAdapter(getContext(), 2);
            rv.setAdapter(rvAdapter);
            rvAdapter.setInterfaceDetalleVenta(this);
            btnModoVenta.setOnClickListener(this);
            btnCancelarVenta.setOnClickListener(this);
            textoCarrito = rootView.findViewById(R.id.txtTextoCarrito);
            btnCobrar = rootView.findViewById(R.id.btnCobrar);
            btnCobrar.setOnClickListener(this);
            content_venta1 = rootView.findViewById(R.id.content_venta1);
            content_venta2 = rootView.findViewById(R.id.content_venta2);
            linearLayoutbtnDetalle.setOnClickListener(this);
            f.setOnClickListener(this);
            cabeceraPedido = new mCabeceraPedido();
            cabeceraPedido.setZonaServicio(new mZonaServicio());
            btnOpenDialogSalvarPedido.setOnClickListener(this);
            btnElegirCliente.setOnClickListener(this);
            slidingUpPanelLayout = null;
            if (context.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                slidingUpPanelLayout = rootView.findViewById(R.id.sliding_layout);
            }

            if (slidingUpPanelLayout != null) {
                slidingUpPanelLayout.setEnabled(true);
                slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                    @Override
                    public void onPanelSlide(View panel, float slideOffset) {
                    }

                    @Override
                    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            imgArrowDisplay.setImageDrawable(getResources().getDrawable(R.drawable.arrowdisplay));
                        }
                    }
                });
            }
            imgBackGrid.setVisibility(View.GONE);
            imgBackGrid.setOnClickListener(this);
            if (slidingUpPanelLayout != null) {
                slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                });
            }
            imgArrowDisplay.setOnClickListener(this);
            imgArrowDisplay.setVisibility(View.GONE);
            imgCandado.setOnClickListener(this);
            imgSelectViewTypeList.setOnClickListener(this);
            imgSelectViewTypeList.setImageDrawable(getResources().getDrawable(R.drawable.list));
            rvDetalleVenta = rootView.findViewById(R.id.rvCarritoVenta);
            adapterDetalleVenta = new RvAdapterCarSale(detalleVenta.getProductoEnVentaList());
            rvDetalleVenta.setAdapter(adapterDetalleVenta);
            rvDetalleVenta.setLayoutManager(new LinearLayoutManager(getContext()));
            rvDetalleVenta.setHasFixedSize(false);
            btnTipoLista.setOnClickListener(this);
            adapterDetalleVenta.setListenerCantidad(this);
            pb = rootView.findViewById(R.id.pbPedido);
            pb.setVisibility(View.GONE);
            rvVentas.setVisibility(View.VISIBLE);
            asyncCaja = new AsyncCaja(getContext());
            btnTipoLista.setText("Procesar");
            set = new TransitionSet().
                    addTransition(new Fade())
                    .setInterpolator(isVisible() ? new LinearOutSlowInInterpolator() :
                            new FastOutLinearInInterpolator());
            //AsyncCategoria Carga
            adapterCategoria = new RvGridAdapterCategoria();
            gvCategoria.setAdapter(adapterCategoria);
            asyncCaja.setListenerAperturaCaja(this);
            asyncCategoria = new AsyncCategoria();
            asyncCategoria.setListenerCategoria(listenerCategoria);
            setItemClickListenerCategorias();
            listaCategorias = new ArrayList<>();
            asyncProcesoVenta.setProductoSeleccionadoVariante(listenerSeleccionProductoVariante);
            spinnerCategorias = rootView.findViewById(R.id.spinnerCategoria);
            categoriaAdapter = new CategoriaAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, listaCategorias);
            spinnerCategorias.setVisibility(View.GONE);
            spinnerCategorias.setAdapter(categoriaAdapter);
            spinnerCategorias.setOnItemSelectedListener(listenerCategorias);
            dialogVariantesProducto.setListenerValoresSeleccionadas(
                    listenerValoresSeleccionadas);
            rv.setVisibility(View.GONE);
            asyncProducto.setListenerComboPack(this);
            dialogSelectCombo.setListenerSelectCombo(this);
            asyncProcesoVenta.setListenerPackSeleccion(this);
            asyncProcesoVenta.setListenerProductoSeleccionado(this);
            asyncProcesoVenta.setListenerModificadorProductoSeleccion(this);
            setRetainInstance(true);
            dialogSelectModProducto.setListenerModProdSeleccion(this);
            rvAdapter.setDatosSeleccionProductoLista(this);
            //new DownloadList().execute(parametroBusqueda);
            pb.setVisibility(View.GONE);
            //VerificarPedidoEnProceso();
            helper = new DbHelper(context);
            rvDetalleVenta.setHasFixedSize(false);
            asyncCabeceraVenta = new AsyncCabeceraVenta(context);
            edtSearchProduct.onActionViewExpanded();
            edtSearchProduct.setVisibility(View.VISIBLE);
            asyncProcesoVenta.setContext(getContext());
            setListenerText();
            //bdConnectionSql.setContext1(getContext());
            asyncProcesoVenta.setResultadoCodigoBarraBusqueda(listenerBusquedaCodigoBarra);
            adapterDetalleVenta.setListenerDetalleVenta(this);
            rvAdapterArticuloGrid2 = new RvAdapterArticuloGrid2(context);
            rvGridarticulosVenta = rootView.findViewById(R.id.rvGridarticulosVenta);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, context.getResources().getInteger(R.integer.NumColumnVentas));
            rvGridarticulosVenta.setLayoutManager(gridLayoutManager);
            rvGridarticulosVenta.setAdapter(rvAdapterArticuloGrid2);
            setItemClickListener();
            background_dimmer = rootView.findViewById(R.id.background_dimmer);
            avi = rootView.findViewById(R.id.avi);
            background_dimmer.setOnClickListener(this);
            background_dimmer.setVisibility(View.INVISIBLE);
            avi.hide();
            dialogAlertaStock = new DialogAlertaStock();
            asyncProducto.setContext(getContext());
            adapterDetalleVenta.setListenerCarSale(this);
            asyncZonaServicio = new AsyncZonaServicio();
            btnZonaServicio = rootView.findViewById(R.id.btnZonaServicio);
            permitirModificarDatosProductoUnico = false;
            edtSearchProduct.setQuery("", false);
            observacion = "";
            edtPlaca.setInputType(EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            edtHoraIngreso.setKeyListener(null);
            edtHoraSalida.setKeyListener(null);
            if (bVisibleBtnCambioPantalla) {
                btnModoVenta.setVisibility(View.VISIBLE);
            } else {
                btnModoVenta.setVisibility(View.GONE);
            }

            if (Constantes.Tienda.ZonasAtencion) {
                btnZonaServicio.setVisibility(View.VISIBLE);
                if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {
                    textoZonaServicio = Constantes.ZonaServicio.ZonaAutos;
                    Drawable image = context.getResources().getDrawable(R.drawable.ic_car_side_grey600_24dp);
                    image.setBounds(10, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                    btnZonaServicio.setCompoundDrawables(
                            image,
                            null,
                            null,
                            null);
                } else {
                    textoZonaServicio = Constantes.ZonaServicio.ZonaServicio;
                }
                btnZonaServicio.setTextColor(getResources().getColor(R.color.white));
            } else {
                btnZonaServicio.setVisibility(View.GONE);
            }

            taskNotificationPedido = new TaskNotificationPedido(getContext(), getActivity());
            taskNotificationPedido.taskPedidoNuevos();
            btnZonaServicio.setOnClickListener(this);

            new CargaInformacion().execute();
            productoTempTiempoVehiculo = new mProduct();
            productoEnVentaTempVehiculo = new ProductoEnVenta();
            txtEstadoPermitir.setText("----------");
            btnHabMod.setOnClickListener(this);
            edtHoraIngreso.setOnClickListener(this);
            edtHoraSalida.setOnClickListener(this);
            btnHoraFin.setOnClickListener(this);
            btnHoraInit.setOnClickListener(this);
            //new DateTimePicker().show(getFragmentManager(),"date_dialog");
            btnScanPlaca.setOnClickListener(this);
            btnLimpiar.setOnClickListener(this);
            asyncPedido.setResultAforo(this);
            edtTiempoTranscurrido.setVisibility(View.VISIBLE);
            edtObservacion.setVisibility(View.GONE);

        } catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


        return rootView;
    }

    public void VerificarZonaServicio(String placa) {


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtCodigoBarra.addTextChangedListener(listenerCodigoBarra);


    }

    private void setListenerText() {

        edtSearchProduct.setQueryHint("Busqueda de producto");
        edtSearchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pb.setVisibility(View.GONE);
                rvGridarticulosVenta.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
                parametroBusqueda = query;
                if (!parametroBusqueda.equals("")) {
                    asyncProducto.getObtenerProductosVenta(parametroBusqueda, (byte) 103, 0);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()) {
                    pb.setVisibility(View.GONE);
                    //gridview.setVisibility(View.GONE);
                    rvGridarticulosVenta.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                    parametroBusqueda = newText;
                    asyncProducto.getObtenerProductosVenta(parametroBusqueda, (byte) 103, 0);

                }
                return false;
            }
        });

    }

    public void MostrarMensajeAlerta(String mensaje) {
        dialogAlerta.setMessage(mensaje).setPositiveButton("Salir", null).setTitle("Advertencia").create().show();
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        asyncCategoria.cancelObtenerCategorias();
        asyncProducto.CancelarObtenerProductos();
    }

    public void PantallaInicio() {

        if (TipoPantallaPedido != null) {
            switch (TipoPantallaPedido) {
                case Constantes.TipoPantallaPedido.NORMAL:
                    try {
                        btnModoVenta.setText("Vehiculo");
                        content_venta1.setVisibility(View.VISIBLE);
                        content_venta2.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        Log.d("", ex.toString());
                    }
                    if (Constantes.ConfigTienda.bCategoriaDefecto) {
                        CargaCategoriaAutomatica();
                    } else {
                        PantallaCategorias();
                    }
                    break;
                case Constantes.TipoPantallaPedido.VEHICULO:
                    btnModoVenta.setText("Productos");
                    content_venta1.setVisibility(View.GONE);
                    content_venta2.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    public void CargaCategoriaAutomatica() {

        switch (Constantes.ConfigTienda.idCategoriaDefecto) {

            case -1:
                pb.setVisibility(View.VISIBLE);

                asyncProducto.getObtenerProductosVenta("", (byte) 108, 0);
                break;
            case 0:
                pb.setVisibility(View.VISIBLE);
                asyncProducto.getObtenerProductosVenta("", (byte) 104, 0);
                break;
            default:
                pb.setVisibility(View.VISIBLE);

                asyncProducto.getObtenerProductosVenta("", (byte) 105, Constantes.ConfigTienda.idCategoriaDefecto);
                break;


        }

    }

    @Override
    public void onResume() {
        edtSearchProduct.clearFocus();
        super.onResume();
        PantallaInicio();
        asyncPedido.ConsultaAforoDisponible();
    }

    //Obtener seleccion de pack en pantalla
    @Override
    public void ComboPackSelect(PackElemento packElemento) {
        dialogProceso = dialogCargaAsync.getDialogCarga("Guardando combo/pack");
        dialogProceso.setCanceledOnTouchOutside(false);
        dialogProceso.show();
        asyncProcesoVenta.guardarPackenPedido(idCabeceraActual, packElemento);

    }

    @Override
    public void ProductosComboIndividuales(PackElemento packElemento) {
        List<ProductoEnVenta> lista = new ArrayList();
        for (mProduct p : packElemento.getProductList()) {
            ProductoEnVenta productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setIdProducto(p.getIdProduct());
            productoEnVenta.setIdCabeceraPedido(this.idCabeceraActual);
            productoEnVenta.setMetodoGuardar("N");
            productoEnVenta.setCantidad(p.getStockDisponible().multiply(new BigDecimal(packElemento.getCantidad())).floatValue());
            lista.add(productoEnVenta);
        }
        this.asyncProcesoVenta.AgregarListaProductosDetallePedido(lista, new AsyncProcesoVenta.ListenerAgregarListaProductosDetallePedido() {
            public void ResultadoAgregarProductos(List<ProductoEnVenta> productoEnVentaList) {
                for (int i = 0; i < productoEnVentaList.size(); i++) {
                    if (((ProductoEnVenta) productoEnVentaList.get(i)).getCantidad() != 0.0f) {
                        VentasFragment.this.GuardarProductoNormalDetallePedido((ProductoEnVenta) productoEnVentaList.get(i));
                    } else {
                        AlertDialog.Builder title = new AlertDialog.Builder(VentasFragment.this.context).setTitle("Advertencia");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("El producto '");
                        stringBuilder.append(((ProductoEnVenta) productoEnVentaList.get(i)).getProductName());
                        stringBuilder.append("' no tiene stock disponible.");
                        title.setMessage(stringBuilder.toString()).setPositiveButton("Salir", null).create().show();
                    }
                }
            }
        });
    }

    //Resultado de Guardar Pack en detalle Pedido
    @Override
    public void PackResultadoGuardar(PackElemento packElemento) {
        dialogProceso.hide();
        if (packElemento.isPermitir()) {
            adapterDetalleVenta.addElementoPack(packElemento);
            adapterDetalleVenta.setNumeroItem(detalleVenta.getUltimoProductoIngresado().getItemNum() + 1);
            cambiarTextoUltimoProductoIngresado();
        } else {
            String[] prNoStock = new String[packElemento.getListaFaltante().size()];

            for (int i = 0; i < packElemento.getListaFaltante().size(); i++) {
                prNoStock[i] = packElemento.getListaFaltante().get(i);
            }

            new AlertDialog.Builder(context).setTitle("Productos sin stock disponible")
                    .setItems(prNoStock, null).setPositiveButton("Salir", null).create().show();
        }
    }

    @Override
    public void EliminarPackPedido(ProductoEnVenta productoEnVenta) {

        adapterDetalleVenta.EliminarProductoPack(productoEnVenta.getItemNum());
    }


    public void GuardarProductoNormalDetallePedido(ProductoEnVenta productoEnVenta) {

        background_dimmer.setVisibility(View.INVISIBLE);
        avi.hide();
        BuscarProductoEnLista(productoEnVenta.getIdProducto(),
                productoEnVenta.getCantidadReserva(), productoEnVenta.getStockActual());
        if (productoEnVenta.getCantidad() > 0) {
            if (productoEnVenta.getMetodoGuardar().equals("M")) {

                detalleVenta.getUltimoProductoIngresado().setCantidad(productoEnVenta.getCantidad());
                detalleVenta.getUltimoProductoIngresado().setPrecioVentaFinal(productoEnVenta.getPrecioVentaFinal());
                ModificarValorDescuento();
                cambiarTextoUltimoProductoIngresado();
                CambiarCantidadProducto();
                adapterDetalleVenta.ModificarCantidad();
                ActualizaBotonCobrar();
            } else if (productoEnVenta.getMetodoGuardar().equals("N")) {
                adapterDetalleVenta.addElement(productoEnVenta);
                cambiarTextoUltimoProductoIngresado();
                detalleVenta.RetornarCantidadTotalProductosEnVenta();
                ModificarValorDescuento();
                CambiarCantidadProducto();
                ActualizaBotonCobrar();

            }
        } else if (productoEnVenta.getCantidad() <= 0) {

            MostrarMensajeAlerta("El producto no tiene stock disponible");
        }
    }

    @Override
    public void ObtenerProductoSeleccionado(ProductoEnVenta productoEnVenta) {

        GuardarProductoNormalDetallePedido(productoEnVenta);

    }

    @Override
    public void NoExisteStock() {
        MostrarMensajeAlerta("No existe stock disponible para el producto");
    }

    @Override
    public void ResultadoGuardarProdMod(ProductoEnVenta productoEnVenta) {
        dialogProceso.hide();
        BuscarProductoEnLista(productoEnVenta.getIdProducto(), productoEnVenta.getCantidadReserva(), productoEnVenta.getStockActual());
        if (productoEnVenta.getIdProducto() >= 0) {
            if (productoEnVenta.getRespuestaGuardar() == 100) {
                adapterDetalleVenta.addElement(productoEnVenta);
                cambiarTextoUltimoProductoIngresado();
                detalleVenta.RetornarCantidadTotalProductosEnVenta();
                ModificarValorDescuento();
                CambiarCantidadProducto();
            } else if (productoEnVenta.getRespuestaGuardar() == 80) {
                new AlertDialog.Builder(context).setTitle("Advertencia").setMessage("El producto '" + productoEnVenta.getProductName() + "' no tiene el stock solicitado")
                        .setPositiveButton("Salir", null).create().show();
            }
        } else if (productoEnVenta.getIdProducto() == -5) {
            Toast.makeText(getActivity(), "Error al guardar informacion", Toast.LENGTH_LONG).show();
        } else if (productoEnVenta.getIdProducto() == -10) {
            Toast.makeText(getActivity(), "Error internet.Verifique su conexion", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void obtenerModificadorProducto(int idProducto, String modificador, int idPventa, float cantidad) {
        dialogProceso = dialogCargaAsync.getDialogCarga("Obteniendo producto modificado");
        dialogProceso.show();
        dialogProceso.setCanceledOnTouchOutside(false);
        asyncProcesoVenta.GuardarProductoModificado(idProducto, cantidad, modificador, idCabeceraActual, idPventa);
    }

    @Override
    public void ProductoPosicionSeleccionado(int position, View view) {

        if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.AgregarProductoDP)) {

            GuardarProductoEnPedido(position);
        } else {
            MostrarMensajeAlerta("No tiene permitido agregar productos al pedido");
        }
    }

    @Override
    public void InformacionDescuento(String tDescuento, byte tipoDescuento, BigDecimal valorDescuento) {
        TipoDescuento = tipoDescuento;
        CantidadDescuento = valorDescuento;
        cabeceraPedido.setTipoDescuento(tipoDescuento);
        asyncCabeceraVenta.GuardarDescuentoPedido(idCabeceraActual, valorDescuento, tipoDescuento);
        asyncCabeceraVenta.setListenerDescuentoPedido(respuesta -> {
            if (respuesta == 100) {
                ModificarValorDescuento();
            } else if (respuesta == 99) {
            } else if (respuesta == 98) {
            } else if (respuesta == 0) {
            }
        });
    }

    @java.lang.Deprecated
    public void VerificarPedidoEnProceso() {
        idCabeceraActual = controladorVentas.verificarExistePedido();
        if (idCabeceraActual != 0) {

            descargaDatos = true;
            cabeceraPedido = controladorVentas.getCabeceraUltimoPedido(idCabeceraActual);

            if (CantidadDescuento.compareTo(new BigDecimal(0)) > 0) {
                TipoDescuento = 3;
            }

            detalleVenta.setProductoEnVentaList(controladorVentas.getDetallePedidoId(cabeceraPedido.getIdCabecera()));
            if (detalleVenta.getLongitud() > 0) {

                adapterDetalleVenta.setNumeroItem(detalleVenta.getUltimoProductoIngresado().getItemNum() + 1);
            } else if (detalleVenta.getLongitud() == 0) {
                adapterDetalleVenta.setNumeroItem(1);
            }

            List<ProductoEnVenta> listproducto = detalleVenta.getProductoEnVentaList();

            for (int i = 0; i < listproducto.size(); i++) {
                if (listproducto.get(i).isEsPack()) {
                    listproducto.get(i).inicializarLista();
                    for (int a = 0; a < listproducto.size(); a++) {
                        if (listproducto.get(a).isEsDetallePack() && listproducto.get(a).getIdProductoPadre() == listproducto.get(i).getIdDetallePedido()) {
                            listproducto.get(i).getProductoEnVentaList().add(new ProductoEnVenta(listproducto.get(a).getIdProducto(), listproducto.get(a).getProductName(), listproducto.get(a).getItemNum(), listproducto.get(a).getCantidad(), new BigDecimal(0), new BigDecimal(0), ""));
                        }
                    }
                }
            }

            Iterator itr = listproducto.iterator();
            while (itr.hasNext()) {

                ProductoEnVenta productoEnVenta = (ProductoEnVenta) itr.next();
                if (productoEnVenta.isEsDetallePack()) {
                    itr.remove();
                }
            }
            adapterDetalleVenta.AddElementList(listproducto);
            modificarCantidadProductos();
            vendedor.setIdVendedor(cabeceraPedido.getIdVendedor());
            vendedor.setPrimerNombre(cabeceraPedido.getNombreVendedor());
            ObtenerInformacionVendedor(vendedor);
            cabeceraPedido.setVendedor(vendedor);
            cliente.setiId(cabeceraPedido.getIdCliente());
            cliente.setcName(cabeceraPedido.getDenominacionCliente());

            ObtenerInformacionCliente(cliente);

            txtDescuentoVenta.setText(simboloMoneda + String.format("%.2f", CantidadDescuento));
            ActualizaBotonCobrar();
            descargaDatos = false;
        } else if (idCabeceraActual == 0) {
            idCabeceraActual = controladorVentas.GenerarNuevoPedido();
            cabeceraPedido.setIdCabecera(idCabeceraActual);

        }
        medioPagoList = new ControladorMediosPago().GetMediosPago();
        ;

    }

    @Override
    public void EliminarProducto(int position) {
        if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.EliminarProductoDP)) {
            posEliminar = position;
            asyncProcesoVenta.EliminarProductoDetalle(idCabeceraActual,
                    detalleVenta.getProductoEnPosicion(position).getIdDetallePedido());
            asyncProcesoVenta.setEliminarProductoDetallePedido(new AsyncProcesoVenta.EliminarProductoDetallePedido() {
                @Override
                public void ResultadoEliminarCorrecto() {
                    adapterDetalleVenta.RemoveElement(posEliminar);
                    ModificarTextoDescuento();
                }

                @Override
                public void ResultadoEliminarError() {

                }

                @Override
                public void ResultadoNoConexion() {

                }
            });
        } else {
            MostrarMensajeAlerta("No tiene permitido usar está opción");
        }
    }

    @Override
    public void EditarProductoVenta(ProductoEnVenta productoEnVenta) {

        if (!productoEnVenta.isControlTiempo()) {
            new DfEditProductCarSale().newInstance(productoEnVenta,
                    productoEnVenta1 -> asyncProducto.ActualizarProductoEnPedido(idCabeceraActual, productoEnVenta1)).show(getFragmentManager(), "Editar Producto");
        } else {
            new DfEditProductTiempoPedido()
                    .newInstance(productoEnVenta)
                    .listener((product, horaFinal) -> {
                        asyncProcesoVenta.EditarProductoTiempoPedido(product, idCabeceraActual, horaFinal);
                        asyncProcesoVenta.setListenerEditarProductoTiempoPedido(new AsyncProcesoVenta.ListenerEditarProductoTiempoPedido() {
                            @Override
                            public void ExitoGuardar(ProductoEnVenta p) {
                                CambiarDatosProductoEnVenta(p);
                                ModificarTextoDescuento();
                                ActualizaBotonCobrar();
                            }

                            @Override
                            public void ErrorGuardar() {
                            }
                        });
                    }).show(getFragmentManager(), "EditarTiempo");
        }


        asyncProducto.setListenerActualizarCantidad(productoEnVenta12 -> {

            if (productoEnVenta12.getIdDetallePedido() > 0) {

                CambiarDatosProductoEnVenta(productoEnVenta12);
                ModificarTextoDescuento();
                ActualizaBotonCobrar();

            } else if (productoEnVenta12.getIdDetallePedido() == -99) {
                Toast.makeText(context,
                        "Error a editar el producto.Verifique su conexión a internet.",
                        Toast.LENGTH_SHORT).show();

            } else if (productoEnVenta12.getIdDetallePedido() == -98) {

                new AlertDialog.Builder(context).
                        setMessage("No hay stock disponible suficiente").
                        setTitle("Advertencia").setPositiveButton("Salir", null).show();

            }

        });
    }

    private void ModificarTextoDescuento() {
        txtDescuentoVenta.setText("Total descuento:\n" +
                "" + detalleVenta.TotalDescuentoText());
    }

    public void CambiarDatosProductoEnVenta(ProductoEnVenta productoEnVenta) {
        int pos = -1;
        for (int i = 0; i < detalleVenta.getProductoEnVentaList().size(); i++) {
            if (detalleVenta.getProductoEnVentaList().get(i).getIdDetallePedido() == productoEnVenta.getIdDetallePedido()) {
                pos = i;
                detalleVenta.getProductoEnVentaList().get(i).setMontoDescuento(productoEnVenta.getMontoDescuento());
                detalleVenta.getProductoEnVentaList().get(i).setCantidad(productoEnVenta.getCantidad());
                detalleVenta.getProductoEnVentaList().get(i).setPrecioOriginal(productoEnVenta.getPrecioOriginal());
                detalleVenta.getProductoEnVentaList().get(i).setObservacion(productoEnVenta.getObservacion());
                detalleVenta.getProductoEnVentaList().get(i).setUsaDescuento(productoEnVenta.isUsaDescuento());
                if (productoEnVenta.getHoraFinal() == null) {
                    detalleVenta.getProductoEnVentaList().get(i).setHoraFinal("NN");
                } else if (productoEnVenta.getHoraFinal().equals("NN")) {
                    detalleVenta.getProductoEnVentaList().get(i).setHoraFinal(productoEnVenta.getHoraFinal());
                } else {
                    detalleVenta.getProductoEnVentaList().get(i).setHoraFinal(productoEnVenta.getHoraFinal());
                }

                detalleVenta.getProductoEnVentaList().get(i).setPrecioVentaFinal(
                        (detalleVenta.getProductoEnVentaList().get(i).getPrecioOriginal().multiply(new BigDecimal(productoEnVenta.getCantidad())))
                );
            }
        }
        if (pos >= 0) {
            adapterDetalleVenta.ActualizarProducto(pos);
        }
    }

    @Override
    public void getPackProducto(List<PackElemento> packElementos, List<CategoriaPack> categoriasPack) {

    }

    public void ActualizaBotonCobrar() {
        btnCobrar.setText("Cobrar" + "\n" + DecimalControlKt.montoDecimalPrecioSimbolo(CantidadCobrar));
    }


    public String dem() {
        return "";
    }

    @Override
    public void GetResultAforoDisponible(int aforo) {

        txtAforoLibre.setText(Integer.toString(aforo));

    }

    private void SolicitarPermiso() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            Toast.makeText(getContext(), "Debe activar la camara para usar el scanner", Toast.LENGTH_LONG).show();

        } else {
            Scan();
        }


    }

    @Override
    public void GuardarPagos(BigDecimal CantidadCambio, int TipoDocPago,
                             boolean generaDoc, int idTipoAtencion, BigDecimal montoPromocion,
                             String obs, BigDecimal montoDetraccion, BigDecimal porcentajeDetraccion,
                             String cuentaDetraccion, boolean usaDetraccion) {

        this.tp = TipoDocPago;
        this.idTipoAtencionP = idTipoAtencion;
        this.CantidadCambio = CantidadCambio;
        this.generaDocumento = generaDoc;
        this.montoPromocion = montoPromocion;
        this.observacion = obs;
        this.montoDetraccion = montoDetraccion;
        this.porcentajeDetraccion = porcentajeDetraccion;
        this.cuentaDetraccion = cuentaDetraccion;
        this.usaDetraccion = usaDetraccion;
        asyncProcesoVenta.VerificarPermitirVenta(idCabeceraActual, true);
        asyncProcesoVenta.setiVerificarPermitirVenta(new AsyncProcesoVenta.IVerificarPermitirVenta() {
            @Override
            public void PermitirVentaPedido() {
                listTemporal.addAll(detalleVenta.getProductoEnVentaList());
                new ProcesarVenta().execute(cabeceraPedido.getIdCabecera(), tp);

            }

            @Override
            public void NoPermitirVenta(String mensaje) {
                new AlertDialog.Builder(getActivity()).setTitle("Advertencia").setMessage(mensaje)
                        .setPositiveButton("Salir", null).create().show();
            }

            @Override
            public void CajaNoDisponible() {
                AperturarCaja();
            }

        });

    }

    public void CambiarTipoPantallaNormalVehiculo() {

        TipoPantallaPedido = (TipoPantallaPedido.equals(Constantes.TipoPantallaPedido.NORMAL)) ? Constantes.TipoPantallaPedido.VEHICULO : Constantes.TipoPantallaPedido.NORMAL;
        PantallaInicio();

        if (detalleVenta.getLongitud() == 0) {
        }


    }

    public void OpenDialogProductoTipoTiempo() {

        SelectProductTime dialogProductTime = SelectProductTime.Companion.newInstance(
                (SelectProductTime.ISelectProductTime) product -> {
                    switch (product.getTipoRepresentacionImagen()) {
                        case 1:
                            imgTipoLista.setImageResource(getContext().getResources().getIdentifier("@drawable/" + product.getCodigoForma().trim(), null, getContext().getPackageName()));
                            break;
                        case 2:
                            imgTipoLista.setImageBitmap(BitmapFactory.decodeByteArray(product.getbImage(), 0, product.getbImage().length));
                            break;
                    }
                    productoTempTiempoVehiculo = product;
                    if (cabeceraPedido.getcEstadoEntregaPedido().trim().equals("00")) {
                        DialogCargaAsync cargaTiempo = new DialogCargaAsync(context);
                        cargaTiempo.getDialogCarga("Espere un momento").show();
                        asyncPedido.GetTiempoAsync(time -> {
                            cargaTiempo.hide();
                            tiempoTempVehiculo = time;
                            edtHoraIngreso.setText(time);
                        });
                       /* new DfProductoControlTiempoPedido().
                                newInstance(productoTempTiempoVehiculo).f((p, horaInicio) -> {
                                    tiempoTempVehiculo = horaInicio;
                                    edtHoraIngreso.setText(horaInicio);
                                }
                        ).show(getFragmentManager(), "dialog_time_select");*/
                    }
                });
        dialogProductTime.show(getFragmentManager(), "dialog_time");
    }

    public void GuardarPedidoVehiculo() {
        //api-work
        boolean guardar = true;
        if (edtPlaca.getText().toString().trim().length() < 5) {
            guardar = false;
        }

        if (guardar) {

            mZonaServicio zonaServicio = new mZonaServicio();
            zonaServicio.setIdZona(0);
            zonaServicio.setDescripcion(edtPlaca.getText().toString().trim());
            mCabeceraPedido cabPedidoTemp1 = new mCabeceraPedido();
            cabPedidoTemp1.setIdCabecera(idCabeceraActual);
            cabPedidoTemp1.setZonaServicio(zonaServicio);
            DialogCargaAsync cargaAsyncTemp = new DialogCargaAsync(context);
            cargaAsyncTemp.getDialogCarga("Consultando información").show();
            asyncZonaServicio.RegistrarZonaServicioPedido(cabPedidoTemp1, new AsyncZonaServicio.ListenerZonaServicioPedido() {

                @Override
                public void RegistroExito(@NotNull ResZonaServicio respuesta) {
                    cargaAsyncTemp.hide();
                    if (productoTempTiempoVehiculo.getIdProduct() != 0) {
                        txtEstadoPermitir.setText("Entra");
                        cabeceraPedido.setZonaServicio(respuesta.getZonaServicio());
                        cabeceraPedido.setIdentificadorPedido(
                                respuesta.getZonaServicio()
                                        .getDescripcion());
                        cabeceraPedido.setObservacion(respuesta.getObservacion());
                        productoTempTiempoVehiculo.setProductoUnico(true);
                        cargaAsyncTemp.getDialogCarga("Espere un momento").show();
                        asyncProcesoVenta.AgregarProductoTiempoDetallePedido(productoTempTiempoVehiculo, idCabeceraActual, tiempoTempVehiculo);
                        asyncProcesoVenta.setListenerAgregarProductoPedidoTiempo(
                                new AsyncProcesoVenta.ListenerAgregarProductoPedidoTiempo() {
                                    @Override
                                    public void ExitoAgregar(ProductoEnVenta productoEnVenta) {
                                        cargaAsyncTemp.hide();
                                        txtEstadoPermitir.setText("Entrada");
                                        productoEnVenta.setMetodoGuardar("N");
                                        GuardarProductoNormalDetallePedido(productoEnVenta);
                                        ObtenerDatoPedido(new InfoGuardadoPedido(edtPlaca.getText().toString(), edtObservacion.getText().toString(),
                                                cliente.getRazonSocial(), "", false
                                        ));

                                    }

                                    @Override
                                    public void ErrorAgregar() {
                                        cargaAsyncTemp.hide();
                                        new AlertDialog.Builder(context).setTitle("Advertencia")
                                                .setMessage("Hubo un problema al guardar la información" +
                                                        ".Verifique su conexión a internet")
                                                .setPositiveButton("Salir", null).create().show();
                                    }

                                    @Override
                                    public void ErrorAgregarAdvertencia(String mensaje) {
                                        cargaAsyncTemp.hide();
                                        new AlertDialog.Builder(context).setTitle("Advertencia").setMessage(mensaje).setPositiveButton("Aceptar", null).create().show();
                                        asyncPedido.ConsultaAforoDisponible();
                                    }
                                });
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Advertencia")
                                .setMessage("Debe seleccionar un tipo de lista")
                                .setNegativeButton("Salir", null).create().show();
                    }

                }

                @Override
                public void ErrorRegistro() {
                    cargaAsyncTemp.hide();
                }

                @Override
                public void ExisteEnPedido(@NotNull ResZonaServicio respuesta) {
                    cargaAsyncTemp.hide();
                    txtEstadoPermitir.setText("Salida");
                    DialogCargaAsync cargaAsyncTemp2 = new DialogCargaAsync(context);
                    cargaAsyncTemp2.getDialogCarga("Espere un momento").show();
                    asyncPedido.GetIdPedidoZonaServicio(
                            edtPlaca.getText().toString().trim(),
                            new AsyncPedido.IResultZonaServicioPedido() {
                                @Override
                                public void PedidoEncontrado(int idPedido) {
                                    asyncPedido.ConsultaAforoDisponible();
                                    cargaAsyncTemp2.hide();
                                    try {
                                        txtEstadoPermitir.setText("Salida");
                                        new ObtenerPedido().execute(idPedido);
                                    } catch (Exception ex) {
                                        Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void PedidoError() {

                                    asyncPedido.ConsultaAforoDisponible();
                                    cargaAsyncTemp2.hide();
                                }
                            });

                }
            });
        }
    }

    public void ActualizaDatosPedidoProductoUnico() {
        Pedido pedidoTemp = new Pedido();

        pedidoTemp.setCabeceraPedido(cabeceraPedido);

        pedidoTemp.getCabeceraPedido().getZonaServicio().setDescripcion(edtPlaca.getText().toString().trim());
        ProductoEnVenta productoEnVentaTemp = productoEnVentaTempVehiculo;
        productoEnVentaTemp.setIdProducto(productoTempTiempoVehiculo.getIdProduct());
        productoEnVentaTemp.setHoraFinal(timeDataSalida);
        productoEnVentaTemp.setHoraInicio(timeDataInicio);
        pedidoTemp.getCabeceraPedido().setObservacion(edtObservacion.getText().toString().trim());
        pedidoTemp.getListProducto().add(productoEnVentaTemp);

        DialogCargaAsync cargaAsyncPrUnico = new DialogCargaAsync(context);
        cargaAsyncPrUnico.getDialogCarga("Espere un momento").show();
        asyncPedido.ActualizaPedidoProductoUnico(pedidoTemp, new AsyncPedido.IUpdateInfo() {
            @Override
            public void ResultUpdateOk(@NotNull String mensaje) {
                cargaAsyncPrUnico.hide();
                cabeceraPedido.setIdentificadorPedido(edtPlaca.getText().toString().trim());
                new AlertDialog.Builder(getContext()).setPositiveButton("Aceptar", null)
                        .setTitle("Confirmación").setMessage(mensaje).create().show();
                DeshabilitarModificacionSalida();
                new GetDetallePedido().execute(cabeceraPedido.getIdCabecera());
            }

            @Override
            public void ResultError(@NotNull String mensaje) {
                cargaAsyncPrUnico.hide();
                new AlertDialog.Builder(getContext()).setPositiveButton("Aceptar", null).setTitle("Advertencia").setMessage(mensaje).create().show();

            }
        });
    }

    void ClearPedidoVehiculo() {

        if (productoEnVentaTempVehiculo.getIdProducto() != 0) {
            InfoGuardadoPedido info = new InfoGuardadoPedido(cabeceraPedido.getZonaServicio().getDescripcion(), cabeceraPedido.getObservacion(),
                    cliente.getRazonSocial(), "", false
            );
            info.setImprimi(false);
            ObtenerDatoPedido(info);
        } else {
            ReinicioPantalla();
        }

    }

    public void onClick(View v) {
        if (v.getId() == R.id.imgBtnScan) {
            if (helper.ObtenerPermiso(15)) {
                SolicitarPermiso();
            } else {
                MostrarMensajeAlerta("No tiene permitido usar la opción");
            }
        } else if (v.getId() == R.id.btnLimpiar) {
            ClearPedidoVehiculo();
        } else if (v.getId() == R.id.btnScanPlaca) {
            DialogScannerCam camPlaca = new DialogScannerCam();
            camPlaca.setScannerResult(resultText -> {
                if (!resultText.trim().equals(cabeceraPedido.getZonaServicio().getDescripcion())) {
                    edtPlaca.setText(resultText);
                    GuardarPedidoVehiculo();
                }

            });

            camPlaca.show(getActivity().getFragmentManager(), "");
        } else if (v.getId() == R.id.btnHabMod) {

            HabilitarModificacion();
        } else if (v.getId() == R.id.btnHoraInit) {

            new DateTimePicker().newInstance(productoEnVentaTempVehiculo.getTiempoInicio()
                    , "Ingreso",
                    (DateTimePicker.ChangeDateTimePicker) timeData -> {
                        timeDataInicio = timeData;
                        edtHoraIngreso.setText(timeData.getTimeStringFormatInterface());

                    }).show(getParentFragmentManager(), "time_init_dialog");

        } else if (v.getId() == R.id.btnHoraFin) {

            new DateTimePicker().newInstance(productoEnVentaTempVehiculo.getTiempoFinal()
                    , "Salida",
                    (DateTimePicker.ChangeDateTimePicker) timeData -> {
                        timeDataSalida = timeData;
                        edtHoraSalida.setText(timeData.getTimeStringFormatInterface());
                    }).show(getParentFragmentManager(), "time_end_dialog");
        } else if (v.getId() == R.id.imgTipoLista) {
            OpenDialogProductoTipoTiempo();
        } else if (v.getId() == R.id.btnTipoLista) {

            if (cabeceraPedido.getcEstadoEntregaPedido().trim().equals("02")) {
                ActualizaDatosPedidoProductoUnico();
            } else {
                GuardarPedidoVehiculo();
            }


        } else if (v.getId() == R.id.btnModoVenta) {
            CambiarTipoPantallaNormalVehiculo();
        } else if (v.getId() == R.id.btnZonaServicio) {

            AbrirDialogZonaServicio();
        } else if (v.getId() == R.id.imgTipoVistaLista) {
            CambiarTipoDeVistaLista();
        } else if (v.getId() == R.id.btnCobrar) {// Cobrar la venta en proceso
            try {
                if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.Cobrar)) {
                    permitirCobrar = true;
                    if (detalleVenta.VerficarProductosControlTiempo() == false) {
                        permitirCobrar = false;
                        new AlertDialog.Builder(context)
                                .setTitle("Advertencia").setMessage("Debe agregar la hora final a los servicio con CONTROL DE TIEMPO.")
                                .setPositiveButton("Salir", null).create().show();
                    }
                    if (productoEnVentaTempVehiculo.getIdProducto() != 0 && productoEnVentaTempVehiculo.isProductoUnico()) {


                    }
                    if (Constantes.Tienda.ZonasAtencion) {

                        if (cabeceraPedido.getZonaServicio().getIdZona() == 0) {

                            if (Constantes.Empresa.idEmpresa != 3089) {
                                permitirCobrar = false;
                                if (Constantes.Tienda.cTipoZonaServicio.equals("M")) {
                                    mensajeNoCobrar = "Debe seleccionar una zona de atención para realizar la venta.";
                                } else if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {

                                    mensajeNoCobrar = "Debe seleccionar un vehículo para realizar la venta.";
                                }
                                new AlertDialog.Builder(context).setTitle("Advertencia").
                                        setPositiveButton("Salir", null).setMessage(mensajeNoCobrar).create().show();
                            }
                        }
                    }
                    if (permitirCobrar) {
                        asyncProcesoVenta.VerificarPermitirVenta(idCabeceraActual, false);
                        asyncProcesoVenta.setiVerificarPermitirVenta(new AsyncProcesoVenta.IVerificarPermitirVenta() {
                            @Override
                            public void PermitirVentaPedido() {
                                if (CantidadCobrar.equals(new BigDecimal(0))) {//No mostrar si no tiene producto
                                    mensajeAlertaNoHayProducto();
                                } else if (!CantidadCobrar.equals(0)) {//Mostrar si tiene producto
                                    mostrarMetodosDePago();
                                }
                            }

                            @Override
                            public void NoPermitirVenta(String mensaje) {
                                new AlertDialog.Builder(getActivity()).setTitle("Advertencia").setMessage(mensaje)
                                        .setPositiveButton("Salir", null).create().show();
                            }

                            @Override
                            public void CajaNoDisponible() {
                                AperturarCaja();
                            }

                        });
                    }

                } else {
                    Toast.makeText(context, "No tiene permiso para usar la siguiente acción", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
            }
        } else if (v.getId() == R.id.btnOpenGuardarPedido) {//Colocar venta en proceso como pedido en reserva
            mostrarDialogGuardarPedido(true);
        } else if (v.getId() == R.id.btnElegirVendedor) {//Elegir vendedor para la venta
            DialogVendedor();
        } else if (v.getId() == R.id.background_dimmer) {

        } else if (v.getId() == R.id.linearLayoutBtnDetalleVenta || v.getId() == R.id.imgArrowDisplay) {

            if (detalleVenta.cantidadTotalProductos() > 0) {

                if (slidingUpPanelLayout != null) {
                    if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }

                    if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {

                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        imgArrowDisplay.setImageDrawable(getResources().getDrawable(R.drawable.arrow_hidden));

                    } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        imgArrowDisplay.setImageDrawable(getResources().getDrawable(R.drawable.arrowdisplay));
                    }

                }
            }

        } else if (v.getId() == R.id.txtDescuentoPrecioVenta) {// abrir calculadora descuento

        } else if (v.getId() == R.id.imgLock) {//Bloquear vendedor
            VerificarListenerVendedor();
        } else if (v.getId() == R.id.btnElegirCliente) {//Elegir cliente para la venta

            mostrarSeleccionCliente();
        } else if (v.getId() == R.id.btnCancelarVenta) {//cancelar Pedido y generar nuevo pedido
            if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.AnularPedido)) {
                MensajeConfirmacionEliminarPedido();
            } else {
                Toast.makeText(context, "No permitido", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.imgBackGrid) {

            PantallaCategorias();
        }

    }

    private void RegistroVehiculo() {
        try {

            new DfRegistroZonaServicioCw().newInstance(
                    cabeceraPedido.getZonaServicio(),
                    zonaServicio -> {
                        mCabeceraPedido c = new mCabeceraPedido();
                        c.setIdCabecera(idCabeceraActual);
                        c.setZonaServicio(zonaServicio);
                        asyncZonaServicio.RegistrarZonaServicioPedido(c,
                                new AsyncZonaServicio.ListenerZonaServicioPedido() {
                                    @Override
                                    public void ExisteEnPedido(@NotNull ResZonaServicio respuesta) {
                                        new AlertDialog.Builder(context).
                                                setTitle("Advertencia").
                                                setMessage("El vehículo con placa " +
                                                        respuesta.getZonaServicio().getDescripcion()
                                                        + " ya se encuentra en un pedido guardado")
                                                .setPositiveButton("Salir", null).create().show();
                                    }

                                    @Override
                                    public void RegistroExito(@NotNull ResZonaServicio respuesta) {
                                        cabeceraPedido.setZonaServicio(respuesta.getZonaServicio());
                                        cabeceraPedido.setIdentificadorPedido(
                                                respuesta.getZonaServicio()
                                                        .getDescripcion());
                                        cabeceraPedido.setObservacion(respuesta.getObservacion());
                                        btnZonaServicio.setText(cabeceraPedido.getZonaServicio().getDescripcion());

                                        new DfUltimosPedidosZonaServicio().
                                                newInstance(respuesta.getBTieneVentas(),
                                                        respuesta.getCliente(),
                                                        respuesta.getZonaServicio(),
                                                        new DfUltimosPedidosZonaServicio.ListenerActualizarZonaServicio() {
                                                            @Override
                                                            public void ActualizarZonaServicio(@NotNull mZonaServicio zonaServicio) {
                                                                asyncZonaServicio.ActualizarZonaServicio(zonaServicio,

                                                                        new AsyncZonaServicio.ListenerActualizarZonaServicio() {
                                                                            @Override
                                                                            public void ActualizarExito() {

                                                                                new AlertDialog.Builder(context)
                                                                                        .setTitle("Confirmación")
                                                                                        .setMessage("Se guardo los datos con éxito")
                                                                                        .setPositiveButton("Salir",
                                                                                                null).create().show();
                                                                            }

                                                                            @Override
                                                                            public void ErrorActualizar() {
                                                                                new AlertDialog.Builder(context)
                                                                                        .setTitle("Advertencia")
                                                                                        .setMessage("Hubo un problema " +
                                                                                                "al guardar los datos." +
                                                                                                "Verifique su conexión a internet")
                                                                                        .setPositiveButton("Salir",
                                                                                                null).create().show();

                                                                            }
                                                                        });
                                                            }
                                                        })
                                                .ListenerDatosCliente(
                                                        new DfUltimosPedidosZonaServicio.ListenerDatosCliente() {
                                                            @Override
                                                            public void DatosObtenidosCliente(@NotNull mCustomer cliente) {
                                                                obtenerDato(cliente);
                                                            }
                                                        }
                                                ).show(getFragmentManager(), "");


                                    }

                                    @Override
                                    public void ErrorRegistro() {
                                        new AlertDialog.Builder(context).
                                                setTitle("Advertencia").
                                                setMessage("Existe un problema al momento de" +
                                                        " registrar el vehículo.Verifique su " +
                                                        "conexión a internet.").create().show();
                                    }
                                });

                    }).show(getFragmentManager(), "");

        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void AbrirDialogZonaServicio() {
        if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {
            RegistroVehiculo();
        } else if (Constantes.Tienda.cTipoZonaServicio.equals("M")) {
            SeleccionZonaServicio();
        }
    }

    private void SeleccionZonaServicio() {
        new Df_Select_Zona_Servicio().newInstance(cabeceraPedido.getZonaServicio(), new Df_Select_Zona_Servicio.ListenerSeleccionZonaServicio() {
            @Override
            public void EliminarSeleccion() {
                asyncZonaServicio.EliminarZonaServicioPedido(idCabeceraActual);
                asyncZonaServicio.setListenerEliminarZonaPedido(new AsyncZonaServicio.ListenerEliminarZonaPedido() {
                    @Override
                    public void ExitoEliminar() {
                        cabeceraPedido.setZonaServicio(new mZonaServicio());
                        cabeceraPedido.setIdentificadorPedido("");
                        btnZonaServicio.setText(textoZonaServicio);
                    }

                    @Override
                    public void ErrorEliminar() {

                    }
                });
            }

            @Override
            public void ZonaServicioSeleccionada(@NotNull mZonaServicio zonaServicio) {
                s = zonaServicio;
                if (s.getIdZona() != 0) {
                    asyncZonaServicio.GuardarZonaServicioPedido(zonaServicio, idCabeceraActual);
                    asyncZonaServicio.setListenerGuardarZonaServicioPedido(
                            new AsyncZonaServicio.ListenerGuardarZonaServicioPedido() {
                                @Override
                                public void GuardadoExito() {
                                    cabeceraPedido.setIdentificadorPedido(s.getDescripcion());
                                    cabeceraPedido.setZonaServicio(s);
                                    btnZonaServicio.setText(s.getDescripcion());
                                    if (zonaServicio.getIdTipoZona() == 400) {
                                        mostrarDialogGuardarPedido(false);

                                    }
                                }

                                @Override
                                public void ZonaServicioOcupado() {

                                    new AlertDialog.Builder(context).setTitle("Advertencia")
                                            .setMessage("La zona seleccionada no se encuentra disponible")
                                            .setPositiveButton("Salir", null).create().show();
                                }

                                @Override
                                public void ErrorGuardar() {

                                    new AlertDialog.Builder(context).setTitle("Advertencia")
                                            .setMessage("Error al guardar la zona seleccionada")
                                            .setPositiveButton("Salir", null).create().show();
                                }
                            });
                }
            }

            @Override
            public void NombreZona(@NotNull String nombre) {


            }


        }).show(getFragmentManager(), "ZonaServicio");

    }

    private void PantallaCategorias() {
        try {
            spinnerCategorias.setVisibility(View.GONE);
            rvGridarticulosVenta.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            edtSearchProduct.setVisibility(View.VISIBLE);
            asyncCategoria.getCategorias();
            tipoVistaArticulos = 2;
        } catch (Exception ex) {

            Log.e("e-panta", ex.toString());

        }

    }

    public void MensajeConfirmacionEliminarPedido() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desea anular la venta actual?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new CancelarVenta().execute();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle("Advertencia").setIcon(R.drawable.alert).show();
    }

    @Override
    public void onMenuExpanded() {
        f.setVisibility(View.VISIBLE);


    }

    public void DialogVendedor() {
        DialogFragment dialogFragment = selectVendedor;
        dialogFragment.show(((Activity) getContext()).getFragmentManager(), "Elegir Vendedor");

    }

    public void comprimirPanel() {
        if (slidingUpPanelLayout != null) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    public boolean VerificarPanelExpandido() {


        boolean a = false;
        if (slidingUpPanelLayout != null) {
            if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                a = true;
            } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {

                a = false;

            }
        }
        return a;

    }

    public void CapturarCodigoBarra(String codigoBarra) {
        if (codigoBarra != null) {
            if (!codigoBarra.trim().isEmpty()) {
                if (!codigoBarra.equals("Hola")) {
                    asyncProcesoVenta.BuscarProductoCodigoBarra(codigoBarra, idCabeceraActual);

                }
            }
        }

    }

    public void Scan() {

        DialogScannerCam dialogScannerCam = new DialogScannerCam();
        dialogScannerCam.setScannerResult(resultText -> {

            try {
                if (resultText.startsWith("*")) {

                    asyncPedido.GeneraPedidoPlaca(idCabeceraActual, resultText.replace("*", ""), () -> {
                        Log.i("imprime", "imprime codigo");
                        new ObtenerPedido().execute(idCabeceraActual);

                    });
                } else {
                    edtCodigoBarra.setText(resultText);
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        });
        dialogScannerCam.show(getActivity().getFragmentManager(), "cam");
    }

    private void setItemClickListenerCategorias() {

        gvCategoria.setOnItemClickListener((adapterView, view, i, l) -> {
            // Snackbar.make(view,"Cargando productos",Snackbar.LENGTH_SHORT).show();
            gvCategoria.setVisibility(View.GONE);
            switch (i) {

                case 0:
                    pb.setVisibility(View.VISIBLE);

                    asyncProducto.getObtenerProductosVenta("", (byte) 108, 0);
                    break;

                case 1:
                    pb.setVisibility(View.VISIBLE);
                    asyncProducto.getObtenerProductosVenta("", (byte) 104, 0);
                    break;

                default:
                    pb.setVisibility(View.VISIBLE);

                    asyncProducto.getObtenerProductosVenta("", (byte) 105, (int) adapterCategoria.getItemId(i));
                    break;

            }

        });

    }

    public void DialogProductTiempo(mProduct product) {
        new DfProductoControlTiempoPedido().
                newInstance(product)
                .f((p, horaInicio) -> {
                    asyncProcesoVenta.AgregarProductoTiempoDetallePedido(p, idCabeceraActual, horaInicio);
                    asyncProcesoVenta.setListenerAgregarProductoPedidoTiempo(
                            new AsyncProcesoVenta.ListenerAgregarProductoPedidoTiempo() {
                                @Override
                                public void ExitoAgregar(ProductoEnVenta productoEnVenta) {
                                    productoEnVenta.setMetodoGuardar("N");
                                    GuardarProductoNormalDetallePedido(productoEnVenta);
                                }

                                @Override
                                public void ErrorAgregar() {
                                    new AlertDialog.Builder(context).setTitle("Advertencia")
                                            .setMessage("Hubo un problema al guardar la información" +
                                                    ".Verifique su conexión a internet")
                                            .setPositiveButton("Salir", null).create().show();
                                }

                                @Override
                                public void ErrorAgregarAdvertencia(String mensaje) {
                                    new AlertDialog.Builder(context)
                                            .setTitle("Advertencia")
                                            .setMessage(mensaje)
                                            .setPositiveButton("Aceptar", null)
                                            .create()
                                            .show();
                                }
                            });
                })
                .show(getFragmentManager(), "HoraInicio");
    }

    private void GuardarProductoEnPedido(int position) {

        try {
            if (productList.get(position).isEstadoVariante()) {
                try {
                    MostrarVariantes(productList.get(position).getIdProduct(),
                            imagenesController.ConvertByteArrayToBitmap(productList.get(position).getbImage()),
                            productList.get(position).getTipoRepresentacionImagen(), productList.get(position).getCodigoForma(),
                            productList.get(position).getCodigoColor(), productList.get(position).getcAdditionalInformation(),
                            (productList.get(position).getcKey() + " " + productList.get(position).getDescripcionCategoria()
                                    + " " + productList.get(position).getDescripcionSubCategoria()
                                    + " " + productList.get(position).getcProductName()).trim(), productList.get(position).isControlStock());
                } catch (Exception e) {
                    e.toString();
                }
            } else if (productList.get(position).isbControlTiempo()) {
                DialogProductTiempo(productList.get(position));
               /* new DfProductoControlTiempoPedido().
                        newInstance(productList.get(position))
                        .f((p, horaInicio) -> {
                            asyncProcesoVenta.AgregarProductoTiempoDetallePedido(p, idCabeceraActual, horaInicio);
                            asyncProcesoVenta.setListenerAgregarProductoPedidoTiempo(
                                    new AsyncProcesoVenta.ListenerAgregarProductoPedidoTiempo() {
                                        @Override
                                        public void ExitoAgregar(ProductoEnVenta productoEnVenta) {
                                            productoEnVenta.setMetodoGuardar("N");
                                            GuardarProductoNormalDetallePedido(productoEnVenta);
                                        }

                                        @Override
                                        public void ErrorAgregar() {
                                            new AlertDialog.Builder(context).setTitle("Advertencia")
                                                    .setMessage("Hubo un problema al guardar la información" +
                                                            ".Verifique su conexión a internet")
                                                    .setPositiveButton("Salir", null).create().show();
                                        }
                                    });
                        })
                        .show(getFragmentManager(), "HoraInicio");
*/
            } else if (productList.get(position).isTipoPack()) {

                try {
                    if (((mProduct) this.productList.get(position)).isComboSimple()) {
                        ProductoEnVenta p = new ProductoEnVenta();
                        p.setIdProducto(((mProduct) this.productList.get(position)).getIdProduct());
                        p.setIdCabeceraPedido(this.idCabeceraActual);
                        this.asyncProcesoVenta.AgregarComboRapido(p);
                        this.asyncProcesoVenta.setListenerAgregarComboRapidoPedido(new AsyncProcesoVenta.ListenerAgregarComboRapidoPedido() {
                            public void AgregadoComboRapidoExito(ProductoEnVenta p) {
                                p.setMetodoGuardar("N");
                                VentasFragment.this.GuardarProductoNormalDetallePedido(p);
                            }

                            public void ErrorAgregarComboRapido(String mensaje) {
                                new AlertDialog.Builder(VentasFragment.this.context).setTitle("Advertencia").setMessage(mensaje).setPositiveButton("Salir", null).create().show();
                            }
                        });
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(((mProduct) this.productList.get(position)).getcProductName());
                        stringBuilder.append("-");
                        stringBuilder.append(Constantes.DivisaPorDefecto.SimboloDivisa);
                        stringBuilder.append(String.format("%.2f", new Object[]{((mProduct) this.productList.get(position)).getPrecioVenta()}));
                        AbrirDialogComboPack(this.productList.get(position));
                    }
                } catch (Exception e2) {
                    e2.toString();
                }
            } else if (productList.get(position).isbControlPeso()) {
                new DfCalculadorPeso().newInstance(productList.get(position), (peso, product) -> {
                    if (product.isMultiplePVenta()) {

                    } else {
                        product.setdQuantity(peso.floatValue());
                        BusquedaProductoPeso(product);
                    }
                }).show(getFragmentManager(), "");
            } else if (productList.get(position).isEstadoModificador()) {
                if (!productList.get(position).isMultiplePVenta()) {
                    try {
                        AbrirDialogModificador(productList.get(position).getIdProduct(), productList.get(position).getcProductName());
                    } catch (Exception e) {
                        e.toString();
                        Toast.makeText(getContext(), "Error al descargar la informacion", Toast.LENGTH_LONG).show();
                    }
                } else {
                    DialogSelectPriceProductMod(productList.get(position).getIdProduct(), productList.get(position).getcProductName());
                }
            } else {
                if (productList.get(position).isControlStock()) {
                    if (!productList.get(position).isMultiplePVenta()) {
                        if (productList.get(position).getdQuantity() <= 0) {
                            MostrarMensajeAlerta("No existen productos disponibles en el stock");
                        } else if (productList.get(position).getdQuantity() > 0) {
                            if (productList.get(position).getdQuantity() > productList.get(position).getCantidadReserva()) {
                                BusquedaProductoId(productList.get(position).getIdProduct());
                                background_dimmer.setVisibility(View.VISIBLE);
                                avi.show();
                            } else {
                                MostrarMensajeAlerta("No existen productos disponibles en el stock");
                            }
                        }
                    } else {
                        DialogSelectPrice(productList.get(position).getIdProduct(), productList.get(position).getcProductName());
                        background_dimmer.setVisibility(View.INVISIBLE);
                    }
                } else {
                    background_dimmer.setVisibility(View.VISIBLE);
                    avi.show();
                    if (!productList.get(position).isMultiplePVenta()) {
                        BusquedaProductoId(productList.get(position).getIdProduct());

                    } else {
                        DialogSelectPrice(productList.get(position).getIdProduct(), productList.get(position).getcProductName());
                        background_dimmer.setVisibility(View.INVISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            e.toString();
        }

    }

    public void DialogSelectPriceProductMod(int idProducto, String titulo) {
        DialogSelectPrecioAdic adic = new DialogSelectPrecioAdic();
        nombreProducto = titulo;
        adic.setObtenerInfoProduct((idProducto1, cantidad, idPventa) -> {

            AbrirDialogModificadorPventa(idProducto1, nombreProducto, idPventa, cantidad);/*
                background_dimmer.setVisibility(View.INVISIBLE);
                ProductoEnVenta productoEnVenta=new ProductoEnVenta();
                productoEnVenta.setIdProducto(idProducto);
                productoEnVenta.setIdCabeceraPedido(idCabeceraActual);
                productoEnVenta.setMetodoGuardar("N");
                productoEnVenta.setCantidad(cantidad);
                productoEnVenta.setCodPrecioAlterno(idPventa);
                asyncProcesoVenta.ObtenerProductoId(productoEnVenta);*/
        });
        adic.setIdProducto(idProducto);
        adic.setTitulo(titulo);
        adic.show(getFragmentManager(), "adic_dialog2");
     /*   androidx.core.app.DialogFragment df = adic;
        df.show(getFragmentManager(), "");*/
    }

    public void DialogSelectPrice(int idProduct, String titulo) {

        DialogSelectPrecioAdic adic = new DialogSelectPrecioAdic();
        adic.setObtenerInfoProduct((idProducto, cantidad, idPventa) -> {
            background_dimmer.setVisibility(View.INVISIBLE);
            ProductoEnVenta productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setIdProducto(idProducto);
            productoEnVenta.setIdCabeceraPedido(idCabeceraActual);
            productoEnVenta.setMetodoGuardar("N");
            productoEnVenta.setCantidad(cantidad);
            productoEnVenta.setCodPrecioAlterno(idPventa);
            asyncProcesoVenta.ObtenerProductoId(productoEnVenta);
        });
        adic.setIdProducto(idProduct);
        adic.setTitulo(titulo);
        adic.show(getFragmentManager(), "adic_dialog");
 /*       DialogFragment df = adic;
        df.show(getFragmentManager(), "");

*/
    }

    public void AbrirDialogModificadorPventa(int idProducto, String nombreProducto, int idPrecioVenta, float cantidad) {
// DialogFragment dialogFragment = dialogSelectModProducto;
        dialogSelectModProducto.setIdProduct(idProducto, nombreProducto);
        dialogSelectModProducto.setIdPventaCantidad(idPrecioVenta, cantidad);
        dialogSelectModProducto.show(getFragmentManager(), "SelectModProduct");
        //  dialogFragment.show(getFragmentManager(), "SelectModProduct");
    }

    public void AbrirDialogModificador(int idProducto, String nombreProducto) {

        // DialogFragment dialogFragment = dialogSelectModProducto;
        dialogSelectModProducto.setIdProduct(idProducto, nombreProducto);
        dialogSelectModProducto.setIdPventaCantidad(0, 1f);
        //  dialogFragment.show(getFragmentManager(), "SelectModProduct");
        dialogSelectModProducto.show(getFragmentManager(), "SelectModProduct");
    }

    public void AbrirDialogComboPack(mProduct producto) {

        // DialogFragment dialogFragment = dialogSelectCombo;
        dialogSelectCombo.setTitle(producto.getcProductName());
        dialogSelectCombo.setIdProducto(producto.getIdProduct());
        dialogSelectCombo.setProductTemp(producto);
        dialogSelectCombo.setPrecio(producto.getPrecioVenta());

        dialogSelectCombo.show(getFragmentManager(), "");

    }

    private void setItemClickListener() {
        //Obtener el id del producto en el Grid

        rvAdapterArticuloGrid2.setListenerRvArticuloGrid(new RvAdapterArticuloGrid2.ListenerRvArticuloGrid() {
            @Override
            public void PositionItem(int position) {
                if (helper.ObtenerPermiso(15)) {
                    GuardarProductoEnPedido(position);
                } else {
                    MostrarMensajeAlerta("No tiene permitido agregar productos");
                }
            }
        });
            /*
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    GuardarProductoEnPedido(position);

                }
            });*/
    }

    public void BusquedaProductoPeso(mProduct product) {
        ProductoEnVenta productoEnVenta = new ProductoEnVenta();
        productoEnVenta.setIdProducto(product.getIdProduct());
        productoEnVenta.setIdCabeceraPedido(idCabeceraActual);
        productoEnVenta.setMetodoGuardar("N");
        productoEnVenta.setCantidad(product.getdQuantity());
        asyncProcesoVenta.ObtenerProductoId(productoEnVenta);
    }

    public void BusquedaProductoId(int id) {
        ProductoEnVenta productoEnVenta = new ProductoEnVenta();
        productoEnVenta.setIdProducto(id);
        productoEnVenta.setIdCabeceraPedido(idCabeceraActual);
        if (adapterDetalleVenta.CodigoUltimoProduct() == id) {
            if (detalleVenta.PermitirGuardarEnUltimo()) {
                productoEnVenta.setMetodoGuardar("M");
                productoEnVenta.setIdDetallePedido(detalleVenta.getObtenerUltimoProducto().getIdDetallePedido());

            } else {
                productoEnVenta.setMetodoGuardar("N");
                productoEnVenta.setCantidad(1f);

            }
        } else if (adapterDetalleVenta.CodigoUltimoProduct() != id) {
            productoEnVenta.setMetodoGuardar("N");
            productoEnVenta.setCantidad(1f);
        }
        asyncProcesoVenta.ObtenerProductoId(productoEnVenta);
    }

    @Override
    public void onMenuCollapsed() {
        f.setVisibility(View.GONE);
    }

    @Override
    public void PasarInformacionProductoaDetalleVenta(int id) {

    }

    public void CambiarCantidadProducto() {
        txtCantidadProducto.setText("x" + DecimalControlKt.montoDecimalTexto(new BigDecimal(detalleVenta.cantidadTotalProductos())));

    }

    private void cambiarTextoUltimoProductoIngresado() {

        txtNombreUltimoProductoEnCarrito.setText(detalleVenta.getTextoUltimoItemPedido());
        txtPrecioUltimoProductoEnCarrito.setText(detalleVenta.getTotalMontoPedido());
        linearLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_top));
    }

    @Override
    public void CantidadProductosEnCarrito(int cantidad) {

    }

    @Override
    public void InformacionUltimoProducto(String nombre, String precio) {

        txtNombreUltimoProductoEnCarrito.setText(nombre);
        txtPrecioUltimoProductoEnCarrito.setText(precio);
    }

    @Override
    public void MensajeSalida(DialogFragment dialogFragment) {
        mostrarEditarCantidadProducto();
    }

    @Override
    public void cantidad() {
        modificarCantidadProductos();
        if (detalleVenta.getLongitud() > 0) {
            cambiarTextoUltimoProductoIngresado();
        }
    }

    @Override
    public void eliminarProductoEnDetalle(ProductoEnVenta productoEnVenta, int position) {
        if (productoEnVenta.isEsPack()) {
            productoEnVenta.setItemNum(position);
            asyncProcesoVenta.EliminarDetalleProductoPack(productoEnVenta);
        } else {
            controladorVentas.GuardarProductoDetallePedido(idCabeceraActual, productoEnVenta, 'E');
        }
    }

    public void modificarCantidadProductos() {
        float i;
        i = detalleVenta.cantidadTotalProductos();
        txtCantidadProducto.setText(DecimalControlKt.montoDecimalTexto(new BigDecimal(i)));

        if (slidingUpPanelLayout != null) {
            if (i == 0 && slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                imgArrowDisplay.setImageDrawable(getResources().getDrawable(R.drawable.arrowdisplay));
                slidingUpPanelLayout.setPanelHeight(6);
            } else if (i > 0) {
                if (imgArrowDisplay.getVisibility() == View.GONE) {
                    imgArrowDisplay.setVisibility(View.VISIBLE);
                    imgArrowDisplay.setImageDrawable(getResources().getDrawable(R.drawable.arrowdisplay));
                }
            }
        } else {
            imgArrowDisplay.setVisibility(View.GONE);
            if (i > 0) {
                btnCancelarVenta.setVisibility(View.VISIBLE);
                btnOpenDialogSalvarPedido.setVisibility(View.VISIBLE);
            } else {
                btnCancelarVenta.setVisibility(View.GONE);
                btnOpenDialogSalvarPedido.setVisibility(View.GONE);


            }

        }
        ModificarValorDescuento();
    }

    @Override
    public void detalleListaVacio() {
        txtNombreUltimoProductoEnCarrito.setText("Sin productos");
        txtPrecioUltimoProductoEnCarrito.setText(DecimalControlKt.montoDecimalPrecioSimbolo(new BigDecimal(0)));
        imgArrowDisplay.setVisibility(View.GONE);
        adapterDetalleVenta.setNumeroItem(1);
    }

    @Override
    public void EditarCantidadProducto(int position) {
        this.posSelect = 0;
        this.posSelect = position;
        asyncProducto.ActualizarCantidadProductoPedido(idCabeceraActual,
                detalleVenta.getProductoEnPosicion(position).getIdDetallePedido(),
                detalleVenta.getProductoEnPosicion(position).getCantidad());
    }

    @Override
    public void ObtenerInformacion(mVendedor personaVendedor) {
        this.vendedor = personaVendedor;
        ObtenerInformacionVendedor(personaVendedor);
        asyncCabeceraVenta.GuardarVendedorVenta(idCabeceraActual, personaVendedor);
        asyncCabeceraVenta.setListenerClienteVenta(respuesta -> {
            if (respuesta == 100) {
                ObtenerInformacionVendedor(vendedor);
            } else if (respuesta == 99) {
                vendedor = null;
                vendedor = new mVendedor();
                Toast.makeText(context, "Error al guardar los datos del vendedor.Codigo 999", Toast.LENGTH_LONG).show();

            } else if (respuesta == 98) {
                vendedor = null;
                vendedor = new mVendedor();
                Toast.makeText(context, "Error al guardar los datos del vendedor.Verifique su internet", Toast.LENGTH_LONG).show();

            } else if (respuesta == 0) {
                Toast.makeText(context, "Error al guardar los datos del vendedor.Codigo 0001", Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void obtenerDato(mCustomer customer) {
        this.cliente = customer;
        asyncCabeceraVenta.GuardarClienteVenta(idCabeceraActual, cliente);
        asyncCabeceraVenta.setListenerClienteVenta(new AsyncCabeceraVenta.ListenerClienteVenta() {
            @Override
            public void ResultadoGuardarCabeceraVenta(byte respuesta) {
                if (respuesta == 100) {
                    ObtenerInformacionCliente(cliente);
                } else if (respuesta == 99) {
                    cliente = null;
                    cliente = new mCustomer();
                    Toast.makeText(context, "Error al guardar los datos del cliente.Codigo 0099", Toast.LENGTH_LONG).show();
                } else if (respuesta == 98) {
                    cliente = null;
                    cliente = new mCustomer();
                    Toast.makeText(context, "Error al guardar los datos del cliente.Verifique su internet", Toast.LENGTH_LONG).show();
                } else if (respuesta == 0) {
                    Toast.makeText(context, "Error al guardar los datos del cliente.Codigo 0001", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void ObtenerInformacionCliente(mCustomer customer) {
        cliente = customer;
        if (cliente.getiId() == 0) {
            btnElegirCliente.setText("Cliente");
        } else {
            btnElegirCliente.setText(cliente.getcName());
        }
    }

    public void ObtenerInformacionVendedor(mVendedor vendedor) {
        this.vendedor = vendedor;
        if (vendedor.getIdVendedor() != 0) {
            btnElegirVendedor.setText(vendedor.getPrimerNombre());
        } else if (vendedor.getIdVendedor() == 0) {
            btnElegirVendedor.setText("Vendedor");
        }
    }

    @Override
    public void ObtenerDatoPedido(InfoGuardadoPedido info) {

        permitirGuardarPedido = true;
        if (productoEnVentaTempVehiculo.getIdProducto() == 0 && info.getReservaPedido()) {
            if (detalleVenta.getLongitud() > 0) {
                if (Constantes.Tienda.ZonasAtencion) {
                    if (info.getStr1().isEmpty()) {
                        permitirGuardarPedido = false;
                    }

                }
                if (permitirGuardarPedido) {

                    asyncPedido.GuardarPedido(
                            idCabeceraActual,
                            detalleVenta.getProductoEnVentaList(),
                            cabeceraPedido, info
                    );
                    asyncPedido.setPedidoReserva(new AsyncPedido.PedidoReserva() {
                        @Override
                        public void ExitoReservar() {

                            if (info.getReservaPedido()) {

                                GenerarNuevoPedido();
                            }
                        }

                        @Override
                        public void ErrorReservar(@NotNull String mensaje) {

                            new AlertDialog.Builder(context).setTitle("Advertencia").setMessage(mensaje).setPositiveButton("Aceptar", null).create().show();
                            asyncPedido.ConsultaAforoDisponible();
                        }
                    });

                } else {

                    new AlertDialog.Builder(context).setTitle("Advertencia")
                            .setMessage("Debe agregar un identificador al pedido para guardar")
                            .setPositiveButton("Salir", null).create().show();
                }

            }
        } else if (info.getReservaPedido()) {
            asyncPedido.GuardarPedido(
                    idCabeceraActual,
                    detalleVenta.getProductoEnVentaList(),
                    cabeceraPedido, info
            );
            asyncPedido.setPedidoReserva(new AsyncPedido.PedidoReserva() {
                @Override
                public void ExitoReservar() {
                    GenerarNuevoPedido();
                }

                @Override
                public void ErrorReservar(@NotNull String mensaje) {

                    new AlertDialog.Builder(context).setTitle("Advertencia").setMessage(mensaje).setPositiveButton("Aceptar", null).create().show();
                    asyncPedido.ConsultaAforoDisponible();
                }
            });
        } else if (!info.getReservaPedido()) {
            asyncPedido.GuardarPedido(
                    idCabeceraActual,
                    detalleVenta.getProductoEnVentaList(),
                    cabeceraPedido, info
            );
            asyncPedido.setPedidoReserva(new AsyncPedido.PedidoReserva() {
                @Override
                public void ExitoReservar() {

                    new ObtenerCabeceraPedido().execute(0);
                }

                @Override
                public void ErrorReservar(@NotNull String mensaje) {

                    new AlertDialog.Builder(context).setTitle("Advertencia").setMessage(mensaje).setPositiveButton("Aceptar", null).create().show();
                    asyncPedido.ConsultaAforoDisponible();
                }
            });
        }

/*
        if(productoEnVentaTempVehiculo.getIdProducto()!=0){
            asyncPedido.GuardarPedido(
                    idCabeceraActual,
                    detalleVenta.getProductoEnVentaList(),
                    cabeceraPedido, info
            );
            asyncPedido.setPedidoReserva(this::GenerarNuevoPedido);
        }
*/
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void MostrarVentanaVentaRapida() {
        new df_venta_rapida().newInstance(this.idCabeceraActual)
                .SetListenerAgregarProductoDetallePedido(new df_venta_rapida.ListenerAgregarProductoDetallePedido() {
                    public void AgregarProductoDetallePedido(@NotNull ProductoEnVenta productoEnVenta) {
                        VentasFragment.this.ObtenerProductoSeleccionado(productoEnVenta);
                    }
                }).show(getFragmentManager(), "");
    }

    @Override
    public void FinalizarVenta() {
        listTemporal.clear();
    }

    @Override
    public void VerificarCajaAbierta(BigDecimal montoApertura) {

        this.montoApertura = montoApertura;
        new ConfirmarCajaAbierta().execute();
    }

    private void CambiarTipoDeVistaLista() {
        //Cambiar el tipo de vista de productos  en recyclerview o gridview
        if (tipoVistaArticulos == 1) {
            rvAdapter.clear();
            tipoVistaArticulos = 2;

            TransitionManager.beginDelayedTransition(rv, set);
            imgSelectViewTypeList.setImageDrawable(getResources().getDrawable(R.drawable.list));
            rv.setVisibility(View.GONE);
            asyncCategoria.getCategorias();
        } else if (tipoVistaArticulos == 2) {
            //rvAdapterGridArticulo.limpiarLista();
            rvAdapterArticuloGrid2.limpiarLista();
            tipoVistaArticulos = 1;
            imgBackGrid.setVisibility(View.VISIBLE);
            rvGridarticulosVenta.setVisibility(View.GONE);
            // gridview.setVisibility(View.GONE);
            gvCategoria.setVisibility(View.GONE);
            asyncCategoria.getCategorias();
        }
    }

    public void CargarListaEnPantallaLista(List<mProduct> list) {
        productList.clear();
        productList.addAll(list);
        rvAdapter.AddProduct(productList);
    }

    public void CargarGridEnPantallaList(List<mProduct> list) {
        rvGridarticulosVenta.setVisibility(View.VISIBLE);

        //  gridview.setVisibility(View.VISIBLE);
        productList.clear();
        productList.addAll(list);
        rvAdapterArticuloGrid2.AddElement(productList);
        //rvAdapterGridArticulo.AddElement(productList);
    }

    public void BuscarProductoEnLista(int idProducto, float cantidadReserva, float stockActual) {

        posSelect = -1;
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getIdProduct() == idProducto) {
                posSelect = i;
                break;
            }
        }
        if (posSelect >= 0) {
            if (productList.get(posSelect).isControlStock()) {
                rvAdapterArticuloGrid2.ActualizarCantidadReserva(posSelect, cantidadReserva, stockActual);
            }
        }
    }

    public void mostrarEditarCantidadProducto() {

        DialogFragment dialogFragment = new DialogEditQuantity().newInstance();
        dialogFragment.show(((Activity) getContext()).getFragmentManager(), "Detalle Venta");
    }

    public void mensajeAlertaNoHayProducto() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("No existe productos en el carrito")
                .setNegativeButton("Salir", null);
        builder.setTitle("Atencion").setIcon(R.drawable.alert);
        Dialog dialog = builder.create();
        dialog.show();


    }

    private void mostrarMetodosDePago() {
        try {
            dialogCobroVenta CobroVenta = new dialogCobroVenta().newInstance(
                    cabeceraPedido.getIdCabecera()
                    , getContext(),
                    detalleVenta.TotalCobrar(),
                    cliente.getiId(),
                    medioPagoList, cliente, observacion);
            CobroVenta.setListenerVentaFinalizada(this);
            DialogFragment dialogFragmet = CobroVenta;
            dialogFragmet.show(((Activity) getContext()).getFragmentManager(), "Metodos de Pago");
        } catch (Exception e) {
            e.toString();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void MostrarCalculadoraDescuento() {
        dialogCalculadoraDescuento dialogCalculadoraDescuento = new dialogCalculadoraDescuento();
        dialogCalculadoraDescuento.setInfoDescuento(getContext(), CobrarSinDescuento, CantidadDescuento, TipoDescuento);
        dialogCalculadoraDescuento.setListenerDescuento(this);
        DialogFragment dialogFragment = dialogCalculadoraDescuento;
        dialogFragment.show(((Activity) getContext()).getFragmentManager(), "Calculadora Descuento");

    }

    private void VerificarListenerVendedor() {

        if (EstadoBloqueoVendedor == false) {
            if (vendedor.getIdVendedor() == 0) {
                alertaFaltaDeVendedor();
            } else if (vendedor.getIdVendedor() != 0) {
                imgCandado.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_white_24dp));
                EstadoBloqueoVendedor = true;
            }
        } else if (EstadoBloqueoVendedor == true) {
            imgCandado.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_open_variant_white_24dp));
            EstadoBloqueoVendedor = false;
        }
    }

    private void mostrarSeleccionCliente() {
        //  DialogFragment dialogFragment = selectCustomer;
        selectCustomer.setContext(getContext());
        //dialogFragment.show(getFragmentManager(), "seleccionCliente");
        selectCustomer.show(getParentFragmentManager(), "seleccionCliente");
    }

    private void alertaFaltaDeVendedor() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Seleccione a un vendedor antes de guardarlo");
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setTitle("Alerta").setIcon(R.drawable.alert);
        dialog = builder.create();
        dialog.show();
    }

    public void mostrarDialogGuardarPedido(boolean reservaPedido) {
        if (Constantes.Tienda.ZonasAtencion) {
            mCabeceraPedido mcabecerapedido = this.cabeceraPedido;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.cabeceraPedido.getZonaServicio().getDescripcion());
            stringBuilder.append(" ");
            stringBuilder.append(this.cabeceraPedido.getIdentificador2());
            mcabecerapedido.setIdentificadorPedido(stringBuilder.toString());
        }
        new DialogGuardarPedido().newInstance().setNombreIdentificador(
                        this.cabeceraPedido.getIdentificadorPedido()).setObservacionPedido
                        (this.cabeceraPedido.getObservacion()).setIdentificador2(this.cabeceraPedido.getIdentificador2())
                .setListenerCapturarDato(this)
                .setTipoPedido(this.cabeceraPedido.getcTipoPedido())
                .setFechaEntrega(this.cabeceraPedido.getFechaEntrega())
                .setIdPedido(idCabeceraActual)
                .setReservaPedido(reservaPedido)
                .setIdentificador2Visible(
                        this.cabeceraPedido.getZonaServicio().getBZonaLibre()).setZonaServicio
                        (this.cabeceraPedido.getZonaServicio()).show(((Activity) getContext()).getFragmentManager(), "");


    }

    @java.lang.Deprecated
    private void ModificarValorDescuento() {

        CobrarSinDescuento = detalleVenta.TotalCobrar();
        cabeceraPedido.setTotalBruto(CobrarSinDescuento);
        if (TipoDescuento == 0) {
            CantidadDescuento = new BigDecimal(0);
            CantidadCobrar = CobrarSinDescuento;
            cabeceraPedido.setTotalNeto(CantidadCobrar);
            cabeceraPedido.setDescuentoPrecio(CantidadDescuento);
            cabeceraPedido.setTotalBruto(CobrarSinDescuento);
        } else if (TipoDescuento == 1) {
            CantidadCobrar = CobrarSinDescuento.subtract(CantidadDescuento);
            cabeceraPedido.setTotalNeto(CantidadCobrar);
            cabeceraPedido.setDescuentoPrecio(CantidadDescuento);
            cabeceraPedido.setTotalBruto(CobrarSinDescuento);
            txtDescuentoVenta.setText(
                    simboloMoneda + String.format("%.2f", CantidadDescuento)
            );
        } else if (TipoDescuento == 2) {

            cantidadADescontar = (CantidadDescuento.divide(new BigDecimal(100)).multiply(detalleVenta.TotalCobrar()));
            CantidadCobrar = CobrarSinDescuento.subtract(cantidadADescontar);
            txtDescuentoVenta.setText(
                    DecimalControlKt.montoDecimalPrecioSimbolo(cantidadADescontar)
                            + "(" +
                            DecimalControlKt.montoDecimalTexto(CantidadDescuento)
                            + "%)"
            );
            cabeceraPedido.setTotalNeto(CantidadCobrar);
            cabeceraPedido.setDescuentoPrecio(CantidadDescuento);
            cabeceraPedido.setTotalBruto(CobrarSinDescuento);
        }
        btnCobrar.setText("Cobrar" + "\n" + simboloMoneda + String.format("%.2f", detalleVenta.TotalCobrar()));
    }

    private void GenerarNuevoPedido2(int idCabeceraPedido, mCustomer client, String nuevaFechaEntrega) {
        edtSearchProduct.clearFocus();
        idCabeceraActual = idCabeceraPedido;
        cabeceraPedido = new mCabeceraPedido();
        cabeceraPedido.setcEstadoEntregaPedido("00");
        cabeceraPedido.setIdCabecera(idCabeceraActual);
        if (EstadoBloqueoVendedor != true) {
            vendedor = new mVendedor();
            btnElegirVendedor.setText("Vendedor");
        }
        cabeceraPedido.setFechaEntrega(nuevaFechaEntrega);
        cabeceraPedido.setZonaServicio(new mZonaServicio());
        cabeceraPedido.setObservacion("");
        cabeceraPedido.setObservacionPedido("");
        cliente = client;
        CobrarSinDescuento = new BigDecimal(0);
        CantidadCobrar = new BigDecimal(0);
        CantidadDescuento = new BigDecimal(0);
        observacion = "";
        if (cliente.getiId() == 0) {
            btnElegirCliente.setText("Cliente");
        } else {
            btnElegirCliente.setText(cliente.getRazonSocial());
        }
        btnZonaServicio.setText(textoZonaServicio);
        //      btnCobrar.setText("Cobrar" + "\n" + simboloMoneda + "0.00");
        ActualizaBotonCobrar();
        btnCobrar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
        adapterDetalleVenta.RemoveAllElement();
        ReinicioPantalla();
    }

    public void EstadoPedidoRecuperado() {

        edtPlaca.setEnabled(false);
        btnHabMod.setVisibility(View.VISIBLE);
        btnHoraInit.setVisibility(View.GONE);
        btnHoraFin.setVisibility(View.GONE);

    }

    public void HabilitarModificacion() {

        estadoModProductoUnico = true;

        btnTipoLista.setVisibility(View.VISIBLE);
        btnTipoLista.setText("Guardar cambios");
        btnHoraFin.setVisibility(View.VISIBLE);
        btnHoraInit.setVisibility(View.VISIBLE);
        edtPlaca.setEnabled(false);
    }

    public void DeshabilitarModificacionSalida() {
        estadoModProductoUnico = false;
        btnTipoLista.setVisibility(View.GONE);
        btnTipoLista.setText("Procesar");
        btnHoraFin.setVisibility(View.GONE);
        btnHoraInit.setVisibility(View.GONE);
        edtPlaca.setEnabled(false);
    }

    public void DeshabilitarModificacion() {
        estadoModProductoUnico = false;
        btnTipoLista.setVisibility(View.VISIBLE);
        btnTipoLista.setText("Procesar");
        btnHoraFin.setVisibility(View.GONE);
        btnHoraInit.setVisibility(View.GONE);
        edtPlaca.setEnabled(false);
    }

    public void ReinicioPantalla() {
        estadoModProductoUnico = false;

        productoEnVentaTempVehiculo = new ProductoEnVenta();
        productoTempTiempoVehiculo = new mProduct();
        timeDataInicio = new TimeData();
        timeDataSalida = new TimeData();
        edtHoraSalida.setText("");
        edtHoraIngreso.setText("");
        edtPlaca.setText("");
        edtObservacion.setText("");
        edtTiempoTranscurrido.setText("");
        edtEtapaCont.setText("");
        btnHabMod.setVisibility(View.GONE);
        btnTipoLista.setVisibility(View.VISIBLE);
        btnTipoLista.setText("Procesar");
        btnHoraInit.setVisibility(View.GONE);
        btnHoraFin.setVisibility(View.GONE);
        edtPlaca.setEnabled(true);
        permitirModificarDatosProductoUnico = false;
        imgTipoLista.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_folder_open_outline_grey600_48dp));
    }

    private void GenerarNuevoPedido() {


        new GenerarNuevoPedido().execute();
    }

    public boolean VolverPantallaPrinciparCategorias() {
        if (gvCategoria.getVisibility() == View.VISIBLE) {
            return false;
        } else {
            tipoVistaArticulos = 2;
            spinnerCategorias.setVisibility(View.GONE);
            rvGridarticulosVenta.setVisibility(View.GONE);
            rv.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            asyncCategoria.getCategorias();

            return true;
        }

    }

    public void CambiarPantallaEstadoProductoUnico(String estadoEntregaPedido) {

        if (estadoEntregaPedido.trim().equals("02")) {

            btnHabMod.setText("Habilitar cambios");
            btnHabMod.setVisibility(View.VISIBLE);
            btnHoraInit.setVisibility(View.VISIBLE);
            btnHoraFin.setVisibility(View.VISIBLE);
        } else {

            btnHabMod.setVisibility(View.GONE);
            btnHoraInit.setVisibility(View.GONE);
            btnHoraFin.setVisibility(View.GONE);
        }


    }

    private void ActualizarInterfazDetallePedido(List<ProductoEnVenta> listproductot) {
        try {
            for (int i = 0; i < listproductot.size(); i++) {
                if (listproductot.get(i).isEsPack()) {
                    listproductot.get(i).inicializarLista();
                    for (int a = 0; a < listproductot.size(); a++) {
                        if (listproductot.get(a).isEsDetallePack() && listproductot.get(a).getIdProductoPadre()
                                == listproductot.get(i).getIdDetallePedido()) {
                            listproductot.get(i).getProductoEnVentaList().add(
                                    new ProductoEnVenta(listproductot.get(a).getIdProducto(),
                                            listproductot.get(a).getProductName(),
                                            listproductot.get(a).getItemNum(),
                                            listproductot.get(a).getCantidad(),
                                            new BigDecimal(0), new BigDecimal(0), ""));
                        }
                    }
                }
            }

            Iterator itr = listproductot.iterator();
            while (itr.hasNext()) {

                ProductoEnVenta productoEnVenta = (ProductoEnVenta) itr.next();
                if (productoEnVenta.isEsDetallePack())
                    if (!productoEnVenta.isProductoUnico()) {
                        itr.remove();
                    } else {
                        EstadoPedidoRecuperado();

                        productoTempTiempoVehiculo = new mProduct();
                        productoEnVentaTempVehiculo = productoEnVenta;
                        ProductoEnVenta tempProducto = productoEnVenta;
                        productoTempTiempoVehiculo.setIdProduct(tempProducto.getIdProducto());
                        edtHoraIngreso.setText(tempProducto.getHoraInicio());
                        edtObservacion.setText(cabeceraPedido.getObservacion());

                        edtHoraSalida.setText(tempProducto.getHoraFinal());
                        edtTiempoTranscurrido.setText("0");
                        edtEtapaCont.setText("0");
                        edtEspaciosLibres.setText("0");
                        timeDataInicio = tempProducto.getTiempoInicio();
                        timeDataSalida = tempProducto.getTiempoInicio();

                        switch (tempProducto.getITipoImagen()) {
                            case 1:
                                imgTipoLista.setImageResource(getContext().getResources().getIdentifier("@drawable/" + tempProducto.getCCodigoImagen(), null, getContext().getPackageName()));

                                break;
                            case 2:
                                imgTipoLista.setImageBitmap(BitmapFactory.decodeByteArray(tempProducto.getImage(), 0, tempProducto.getImage().length));

                                break;
                        }


                        itr.remove();
                    }
            }


            detalleVenta.setProductoEnVentaList(listproductot);

            if (detalleVenta.getLongitud() > 0) {
                adapterDetalleVenta.setNumeroItem(detalleVenta.getUltimoProductoIngresado().getItemNum() + 1);
            } else if (detalleVenta.getLongitud() == 0) {
                adapterDetalleVenta.setNumeroItem(1);
            }


            adapterDetalleVenta.AddElementList(detalleVenta.getProductoEnVentaList());
            modificarCantidadProductos();

            if (productoEnVentaTempVehiculo.getIdProducto() != 0) {
                btnTipoLista.setVisibility(View.GONE);
                String tiempo = String.format("%.0f", detalleVenta.getTiempoTranscurrido());
                edtTiempoTranscurrido.setText(tiempo);
                String etapas = String.valueOf(detalleVenta.getEtapasContabilizadas());
                edtEtapaCont.setText(etapas);
                txtEstadoPermitir.setText("Salida");
            } else {
                btnTipoLista.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_REQUEST_RESULT) {
            if (resultCode == RESULT_CANCELED) {

            } else {
                int id = data.getIntExtra("RESULTADOID", 0);
                new ObtenerPedido().execute(id);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Si el permiso está concedido prosigue con el flujo normal
                Scan();
            } else {
                //Si el permiso no fue concedido no se puede continuar
                Toast.makeText(getContext(), "Debe permitir a la camara usar la opcion", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void MostrarHistorialPedidos() {

        Intent intent = new Intent(getContext(), PedidosEnReserva.class);
        startActivityForResult(intent, CODE_REQUEST_RESULT);
    }

    private void MostrarResultadoVenta(List<ProductoEnVenta> list, mCabeceraVenta cabeceraVenta) {
        DialogVentaResultado ventaResultado = new DialogVentaResultado().newInstance(list, CantidadCambio, cabeceraVenta);
        ventaResultado.setListenerTerminarVenta(this);
        DialogFragment dialogFragment = ventaResultado;
        dialogFragment.show(getActivity().getFragmentManager(), "Venta Finalizada");
    }

    private void MostrarVentaFallida() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error").setMessage("Existe un error al procesar la venta");
        builder.setPositiveButton("Salir", (dialog, which) -> {

        });
        builder.setIcon(R.drawable.alert);
        builder.create().show();

    }

    public void MostrarVariantes(int idProduct, Bitmap bmp, byte tipoRepresentacion, String codigoForma, String codigoColor, String descripcion, String titulo, boolean controlStock) {

        DialogFragment dialogFragment = dialogVariantesProducto;
        dialogVariantesProducto.setBmpProduct(bmp, tipoRepresentacion, codigoForma, codigoColor);
        dialogVariantesProducto.setIdProducto(idProduct);
        dialogVariantesProducto.setDescripcion(descripcion);
        dialogVariantesProducto.setTitulo(titulo);
        dialogVariantesProducto.setControStock(controlStock);
        dialogFragment.show(getActivity().getFragmentManager(), "");

    }

    private void MostrarHistorialVentas() {

        Intent intent = new Intent(getContext(), HistorialVentas.class);
        startActivity(intent);

    }


    @Override
    public void ConfirmacionAperturaCaja() {

    }

    @Override
    public void ExisteCierre(mCierre Cierre) {

    }

    public void AperturarCaja() {

        DialogFragment dialogFragment = dialogAperturaCaja;
        dialogFragment.show(getActivity().getFragmentManager(), "AperturaCaja");
    }

    @Override
    public void ConfirmarCierreCaja() {

    }

    @Override
    public void ExisteCajaAbiertaDisponible() {
        if (CantidadCobrar.equals(new BigDecimal(0))) {//No mostrar si no tiene producto
            mensajeAlertaNoHayProducto();
        } else if (!CantidadCobrar.equals(0)) {//Mostrar si tiene producto
            mostrarMetodosDePago();
        }
    }

    @Override
    public void CajaCerrada() {
        AperturarCaja();
    }

    @Override
    public void ErrorEncontrarCaja() {

    }


    private class CargaInformacion extends AsyncTask<Void, Void, Void> {
        List<ProductoEnVenta> listproducto;
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogCargaAsync.getDialogCarga("Descargando datos").show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            Pedido p = controladorVentas.getPedidoUltimoPedido();
            idCabeceraActual = p.getCabeceraPedido().getIdCabecera();
            medioPagoList = new ControladorMediosPago().GetMediosPago();
            mediosPago = medioPagoList;
            if (idCabeceraActual != 0) {
                descargaDatos = true;
                cabeceraPedido = p.getCabeceraPedido();
                TipoDescuento = cabeceraPedido.getTipoDescuento();
                if (TipoDescuento == 1) {
                    CantidadDescuento = cabeceraPedido.getDescuentoPrecio();
                } else if (TipoDescuento == 2) {
                    CantidadDescuento = cabeceraPedido.getPorcentajeDescuento();
                } else if (TipoDescuento == 0) {
                    CantidadDescuento = new BigDecimal(0);
                }
                //  detalleVenta.setProductoEnVentaList(p.getListProducto());
                listproducto = p.getListProducto();
                vendedor = cabeceraPedido.getVendedor();

                cliente = cabeceraPedido.getCliente();
            } else if (idCabeceraActual == 0) {
                idCabeceraActual = controladorVentas.GenerarNuevoPedido();
                cabeceraPedido.setIdCabecera(idCabeceraActual);
                TipoDescuento = 2;
                detalleVenta.setProductoEnVentaList(new ArrayList<ProductoEnVenta>());
                listproducto = detalleVenta.getProductoEnVentaList();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            asyncPedido.ConsultaAforoDisponible();
            dialogCargaAsync.hide();
            try {


                if (idCabeceraActual != 0) {
                    CambiarPantallaEstadoProductoUnico(cabeceraPedido.getcEstadoEntregaPedido());
                    ActualizarInterfazDetallePedido(listproducto);

                    edtPlaca.setText(cabeceraPedido.getZonaServicio().getDescripcion());
                    ObtenerInformacionVendedor(vendedor);
                    ObtenerInformacionCliente(cliente);
                    modificarCantidadProductos();
                    ModificarTextoDescuento();
                    observacion = cabeceraPedido.getObservacion();
                    ActualizaBotonCobrar();
                    descargaDatos = false;
                    Snackbar.make(rootView, "Pedido Recuperado", Snackbar.LENGTH_SHORT).show();
                    ModificarValorDescuento();
                    if (cabeceraPedido.getZonaServicio().getDescripcion().length() > 0) {
                        btnZonaServicio.setText(cabeceraPedido.getZonaServicio().getDescripcion());
                    } else {
                        btnZonaServicio.setText(textoZonaServicio);

                    }
                    if (detalleVenta.getLongitud() > 0) {
                        txtNombreUltimoProductoEnCarrito.setText(detalleVenta.ObtenerNombreUltimoProducto());
                        txtPrecioUltimoProductoEnCarrito.setText(DecimalControlKt.montoDecimalPrecioSimbolo(detalleVenta.ObtenerPrecioUltimoProducto()));
                    }
                } else if (idCabeceraActual == 0) {
                    //   Toast.makeText(context, "Nuevo pedido", Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();

            }
        }
    }

    private class GenerarNuevoPedido extends AsyncTask<Void, Void, Pedido> {
        @Override
        protected Pedido doInBackground(Void... voids) {
            return controladorVentas.getPedidoUltimoPedido();
        }

        @Override
        protected void onPostExecute(Pedido pedido) {
            super.onPostExecute(pedido);
            idCabeceraActual = pedido.getCabeceraPedido().getIdCabecera();
            edtSearchProduct.clearFocus();
            cabeceraPedido = pedido.getCabeceraPedido();
            imgTipoLista.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_folder_open_outline_grey600_48dp));
            edtPlaca.setText("");
            edtObservacion.setText("");
            edtHoraIngreso.setText("");
            productoTempTiempoVehiculo = new mProduct();
            productoEnVentaTempVehiculo = new ProductoEnVenta();
            ReinicioPantalla();
            if (EstadoBloqueoVendedor != true) {
                vendedor = new mVendedor();
                btnElegirVendedor.setText("Vendedor");
            }
            ActualizaBotonCobrar();
            //    btnCobrar.setText("Cobrar" + "\n" + simboloMoneda + "0.00");
            btnZonaServicio.setText(textoZonaServicio);


            vendedor = cabeceraPedido.getVendedor();
            if (cabeceraPedido.getVendedor().getIdVendedor() != 0) {
                btnElegirVendedor.setText(cabeceraPedido.getVendedor().getPrimerNombre());
            }

            cabeceraPedido.setZonaServicio(new mZonaServicio());
            cliente = cabeceraPedido.getCliente();
            CobrarSinDescuento = new BigDecimal(0);
            CantidadCobrar = new BigDecimal(0);
            CantidadDescuento = new BigDecimal(0);
            if (cliente.getiId() == 0) {
                btnElegirCliente.setText("Cliente");
            } else {
                btnElegirCliente.setText(cliente.getRazonSocial());
            }

            cabeceraPedido.setZonaServicio(new mZonaServicio());
            //btnCobrar.setText("Cobrar" + "\n" + simboloMoneda + "0.00");
            ActualizaBotonCobrar();
            btnCobrar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
            adapterDetalleVenta.RemoveAllElement();

            if (Constantes.Tienda.ZonasAtencion) {
                btnZonaServicio.setText(textoZonaServicio);
            }
            asyncPedido.ConsultaAforoDisponible();

        }
    }

    private class GetDetallePedido extends AsyncTask<Integer, Void, List<ProductoEnVenta>> {


        DialogCargaAsync cargaAsync;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cargaAsync = new DialogCargaAsync(context);

            cargaAsync.getDialogCarga("Espere un momento").show();
        }

        @Override
        protected List<ProductoEnVenta> doInBackground(Integer... integers) {
            return controladorVentas.getDetallePedidoId(integers[0]);
        }

        @Override
        protected void onPostExecute(List<ProductoEnVenta> productoEnVentas) {
            super.onPostExecute(productoEnVentas);
            detalleVenta.getProductoEnVentaList().clear();
            ActualizarInterfazDetallePedido(productoEnVentas);

            txtNombreUltimoProductoEnCarrito.setText(detalleVenta.getTextoUltimoItemPedido());
            txtPrecioUltimoProductoEnCarrito.setText(DecimalControlKt.montoDecimalPrecioSimbolo(detalleVenta.ObtenerPrecioUltimoProducto()));
            cargaAsync.hide();
            ModificarTextoDescuento();
            cantidad();
        }
    }

    private class ObtenerCabeceraPedido extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            cabeceraPedido = controladorVentas.getCabeceraUltimoPedido(cabeceraPedido.getIdCabecera());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            cliente = cabeceraPedido.getCliente();

            if (cliente.getiId() != 0) {
                btnElegirCliente.setText(cliente.getcName());
            } else if (cliente.getiId() == 0) {
                btnElegirCliente.setText("CLIENTE");
            }
        }
    }

    private class ObtenerPedido extends AsyncTask<Integer, Void, Void> {
        int idNuevo;
        List<ProductoEnVenta> listproducto;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = dialogCargaAsync.getDialogCarga("Obteniendo pedido...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            idNuevo = integers[0];
            controladorVentas.CambioPedidoActualPorReservado(idCabeceraActual, idNuevo);
            idCabeceraActual = idNuevo;
            cabeceraPedido = controladorVentas.getCabeceraUltimoPedido(idNuevo);
            listproducto = controladorVentas.getDetallePedidoId(cabeceraPedido.getIdCabecera());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                productoEnVentaTempVehiculo = new ProductoEnVenta();
                if (cabeceraPedido.getZonaServicio().getDescripcion().length() > 0) {
                    btnZonaServicio.setText(cabeceraPedido.getZonaServicio().getDescripcion());
                }
                CambiarPantallaEstadoProductoUnico(cabeceraPedido.getcEstadoEntregaPedido());
                observacion = cabeceraPedido.getObservacion();
                ActualizarInterfazDetallePedido(listproducto);
                if (productoEnVentaTempVehiculo.getIdProducto() != 0) {
                    edtPlaca.setText(cabeceraPedido.getZonaServicio().getDescripcion());
                }
  /*
                for (int i = 0; i < listproducto.size(); i++) {
                    if (listproducto.get(i).isEsPack()) {
                        listproducto.get(i).inicializarLista();
                        for (int a = 0; a < listproducto.size(); a++) {
                            if (listproducto.get(a).isEsDetallePack() && listproducto.get(a).getIdProductoPadre()
                                    == listproducto.get(i).getIdDetallePedido()) {
                                listproducto.get(i).getProductoEnVentaList().add(
                                        new ProductoEnVenta(listproducto.get(a).getIdProducto(),
                                                listproducto.get(a).getProductName(),
                                                listproducto.get(a).getItemNum(),
                                                listproducto.get(a).getCantidad(),
                                                new BigDecimal(0), new BigDecimal(0), ""));
                            }
                        }
                    }
                }

                Iterator itr = listproducto.iterator();
                while (itr.hasNext()) {
                    ProductoEnVenta productoEnVenta = (ProductoEnVenta) itr.next();
                    if (productoEnVenta.isEsDetallePack())
                        if (!productoEnVenta.isProductoUnico()) {
                            itr.remove();
                        } else {
                            EstadoPedidoRecuperado();


                            ProductoEnVenta tempProducto = productoEnVenta;
                            productoTempTiempoVehiculo.setIdProduct(tempProducto.getIdProducto());
                            edtHoraIngreso.setText(tempProducto.getHoraInicio());
                            edtObservacion.setText(cabeceraPedido.getObservacion());

                            edtHoraSalida.setText(tempProducto.getHoraFinal());
                            edtTiempoTranscurrido.setText("0");
                            edtEtapaCont.setText("0");
                            edtEspaciosLibres.setText("0");
                            timeDataInicio = tempProducto.getTiempoInicio();
                            timeDataSalida = tempProducto.getTiempoInicio();

                            switch (tempProducto.getITipoImagen()) {
                                case 1:
                                    imgTipoLista.setImageResource(getContext().getResources().getIdentifier("@drawable/" + tempProducto.getCCodigoImagen(), null, getContext().getPackageName()));

                                    break;
                                case 2:
                                    imgTipoLista.setImageBitmap(BitmapFactory.decodeByteArray(tempProducto.getImage(), 0, tempProducto.getImage().length));

                                    break;
                            }

                            productoEnVentaTempVehiculo = productoEnVenta;
                            productoTempTiempoVehiculo = new mProduct();
                            itr.remove();
                        }
                }


                detalleVenta.setProductoEnVentaList(listproducto);
                if (detalleVenta.getLongitud() > 0) {
                    adapterDetalleVenta.setNumeroItem(detalleVenta.getUltimoProductoIngresado().getItemNum() + 1);
                } else if (detalleVenta.getLongitud() == 0) {
                    adapterDetalleVenta.setNumeroItem(1);
                }
*/
                EstadoBloqueoVendedor = false;
                imgCandado.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_open_variant_white_24dp));
                //     adapterDetalleVenta.AddElementList(detalleVenta.getProductoEnVentaList());
                //        modificarCantidadProductos();
                vendedor.setIdVendedor(cabeceraPedido.getIdVendedor());
                vendedor.setPrimerNombre(cabeceraPedido.getNombreVendedor());
                cliente = cabeceraPedido.getCliente();
                if (vendedor.getIdVendedor() != 0) {
                    btnElegirVendedor.setText(vendedor.getPrimerNombre());
                } else if (vendedor.getIdVendedor() == 0) {
                    btnElegirVendedor.setText("VENDEDOR");
                }
                if (cliente.getiId() != 0) {
                    btnElegirCliente.setText(cliente.getcName());
                } else if (cliente.getiId() == 0) {
                    btnElegirCliente.setText("CLIENTE");
                }
                ActualizaBotonCobrar();
                //  btnElegirCliente.setText(cliente.getcName());
                txtNombreUltimoProductoEnCarrito.setText(detalleVenta.getTextoUltimoItemPedido());
                txtPrecioUltimoProductoEnCarrito.setText(DecimalControlKt.montoDecimalPrecioSimbolo(detalleVenta.ObtenerPrecioUltimoProducto()));

                asyncPedido.ConsultaAforoDisponible();
                ModificarTextoDescuento();
                cantidad();
                dialog.dismiss();
/*
            if(detalleVenta.ExisteProductoUnico()){
                EstadoPedidoRecuperado();


                ProductoEnVenta tempProducto=detalleVenta.GetProductoUnico();
                productoTempTiempoVehiculo.setIdProduct(tempProducto.getIdProducto());
                edtHoraIngreso.setText(tempProducto.getHoraInicio());
                edtObservacion.setText(cabeceraPedido.getObservacion());

                edtHoraSalida.setText(tempProducto.getHoraFinal());
                edtTiempoTranscurrido.setText("0");
                edtEtapaCont.setText("0");
                edtEspaciosLibres.setText("0");
                timeDataInicio=tempProducto.getTiempoInicio();
                timeDataSalida=tempProducto.getTiempoInicio();

                switch(tempProducto.getITipoImagen())
                {
                    case 1:
                        imgTipoLista.setImageResource(getContext().getResources().getIdentifier("@drawable/" +tempProducto.getCCodigoImagen(),null,getContext().getPackageName()));

                         break;
                    case 2:
                        imgTipoLista.setImageBitmap(BitmapFactory.decodeByteArray(tempProducto.getImage(),0,tempProducto.getImage().length));

                        break;
                }

                productoEnVentaTempVehiculo=tempProducto;
            }
            */

            } catch (Exception ex) {
                Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProcesarVenta extends AsyncTask<Integer, Void, mRespuestaVenta> {

        List<ProductoEnVenta> lista;
        ResultadoComprobante resultadoComprobante;
        mCabeceraVenta cabeceraVenta;
        final String codeCia = GetJsonCiaTiendaBase64x3();
        Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create()).build();
        IPedidoRespository iPedidoRespository = retro.create(IPedidoRespository.class);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = dialogCargaAsync.getDialogCarga("Venta en proceso...");
            dialog.show();

            resultadoComprobante = new ResultadoComprobante();
        }

        @Override
        protected mRespuestaVenta doInBackground(Integer... integers) {

            VentaGeneracion ventaGeneracion = new VentaGeneracion(integers[0],
                    CantidadCambio.multiply(new BigDecimal(-1)), integers[1],
                    generaDocumento, idTipoAtencionP,
                    montoPromocion, observacion, GenerarFecha(),
                    montoDetraccion,porcentajeDetraccion,
                    cuentaDetraccion,usaDetraccion);
            SolicitudEnvio<VentaGeneracion> solicitudEnvio = new SolicitudEnvio<>(codeCia, "2", ventaGeneracion,
                    Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            mRespuestaVenta r = new mRespuestaVenta();
            String data = new Gson().toJson(solicitudEnvio);

            try {
                r = iPedidoRespository.GenerarVentaV2(solicitudEnvio).execute().body();
                r.getCabeceraVenta().setRucEmisor(Constantes.Empresa.NumRuc);
            } catch (IOException e) {
                e.toString();
            } catch (Exception ex) {

                ex.toString();
         /*       bdConnectionSql.GenerarVenta(
                        integers[0], CantidadCambio.multiply(new BigDecimal(-1))
                        , integers[1], generaDocumento, idTipoAtencionP, montoPromocion, observacion);*/
            }

 /*           mRespuestaVenta r = bdConnectionSql.GenerarVenta(
                    integers[0], CantidadCambio.multiply(new BigDecimal(-1))
                    , integers[1], generaDocumento, idTipoAtencionP, montoPromocion, observacion);
*/
            lista = r.getList();
            listTemporal = lista;
            resultadoComprobante.setCodeSuccess(200);
            cabeceraVenta = r.getCabeceraVenta();
            cabeceraVenta.setIdentificador(cabeceraPedido.getIdentificadorPedido());
            cabeceraVenta.setIdTipoDocPago(integers[1]);
            HttpConsultas c = new HttpConsultas();


            if (Constantes.Empresa.Razon_Social != null) {
                if (Constantes.Empresa.Razon_Social.length() > 0) {
                    cabeceraVenta.setEmisor(Constantes.Empresa.Razon_Social);
                } else {
                    cabeceraVenta.setEmisor(Constantes.Tienda.nombreTienda);
                }
            }
            if (Constantes.ConfigTienda.bUsa_Facturacion && generaDocumento) {
                resultadoComprobante.setCodeSuccess(0);
                if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cNuFactura)) {
                    resultadoComprobante = c.GenerarDocumentoElectronicoNubefact(new mDocVenta(r.getCabeceraVenta(), lista));
                } else if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cActFactura)) {
                    facturaActivaController = new FacturaActivaController();
                    resultadoComprobante = facturaActivaController.EmitirComprobanteElectronicoV2(new mDocVenta(r.getCabeceraVenta(), lista));

                }
                resultadoComprobante.setUsaFactura(true);
                if (Constantes.Empresa.cDireccion != null) {
                    if (Constantes.Empresa.cDireccion.length() > 0) {
                        cabeceraVenta.setNombreCiudad(Constantes.Empresa.cDireccion);
                    }
                }
                if (resultadoComprobante.getRptaSunat().getEnlace() != null) {
                    if (resultadoComprobante.getRptaSunat().getEnlace().length() > 0) {
                        cabeceraVenta.setEnlace(resultadoComprobante.getRptaSunat().getEnlace());
                    }
                }
                if (resultadoComprobante.getRptaSunat().getCodigo_hash() != null) {
                    if (resultadoComprobante.getRptaSunat().getCodigo_hash().length() > 0) {
                        cabeceraVenta.setCodigoHash(resultadoComprobante.getRptaSunat().getCodigo_hash());
                    }
                }
                if (Constantes.Empresa.NumRuc != null) {
                    if (Constantes.Empresa.NumRuc.length() > 0) {
                        if (Constantes.ConfigTienda.LinkTicket.isEmpty()) {
                            cabeceraVenta.setRucEmisor(Constantes.Empresa.NumRuc);
                            cabeceraVenta.setEnlaceNf("https://www.nubefact.com/" + cabeceraVenta.getRucEmisor());
                        } else {
                            cabeceraVenta.setEnlaceNf(Constantes.ConfigTienda.LinkTicket);
                        }
                    }
                }
                cabeceraVenta.setcValorTotal(a.Convertir(
                        String.format("%.2f", cabeceraVenta.getTotalPagado()), true));

            }
            /*BdConnectionSql.getSinglentonInstance().
                    ActualizarCabeceraVentaIdFacturacionElectronica(cabeceraVenta.getIdVenta(), resultadoComprobante);*/
            return r;
        }

        @Override
        protected void onPostExecute(mRespuestaVenta aByte) {

            if (aByte.getValorRespuesta() > 0) {
                //    PantallaCategorias();
                PantallaInicio();
                MostrarResultadoVenta(listTemporal, cabeceraVenta);
                if (resultadoComprobante.getCodeSuccess() != 200) {
                    if (!Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cMobileSoftPeru))
                        new AlertDialog.Builder(context).setTitle("Advertencia").
                                setMessage(resultadoComprobante.getMensaje())
                                .setPositiveButton("Salir", null).create().show();
                }
                //GenerarNuevoPedido2(aByte.getValorRespuesta());
                GenerarNuevoPedido2(aByte.getValorRespuesta(), aByte.getCliente(), aByte.getFechaEntregaTemp());
            } else if (aByte.getValorRespuesta() == 0) {
                MostrarVentaFallida();
                listTemporal.clear();
            }
            asyncPedido.ConsultaAforoDisponible();
            dialog.dismiss();

        }
    }

    private class CancelarVenta extends AsyncTask<Void, Void, Integer> {

        Dialog dialogCancelar = getProgressDialog("Cancelando venta");

        private ProgressDialog getProgressDialog(String Mensaje) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(Mensaje);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);


            return progressDialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogCancelar.show();
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            int a = controladorVentas.CambiarEstadoPedidoSuspender(idCabeceraActual);
            //    asyncPedido.AnulacionPedidoAreasProduccion(idCabeceraActual,cabeceraPedido);
            //         idCabeceraActual = controladorVentas.GenerarNuevoPedido();

            return a;
        }


        @Override
        protected void onPostExecute(Integer aByte) {
            super.onPostExecute(aByte);
            asyncPedido.ConsultaAforoDisponible();
            if (aByte == 100) {
                observacion = "";
                dialogCancelar.dismiss();
                //  new CargaInformacion().execute();
                //  dialogCancelar = getProgressDialog("Generando nuevo pedido");
                GenerarNuevoPedido();

                // dialogCancelar.dismiss();
                //          Toast.makeText(getContext(), "Se generó un nuevo pedido", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Error al cancelar la venta.Verifique su conexión a internet", Toast.LENGTH_LONG).show();

            }

        }

    }

    private class ConfirmarCajaAbierta extends AsyncTask<Void, Void, RetornoApertura> {

        final String codeCia = GetJsonCiaTiendaBase64x3();
        Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ICajaRepository iCajaRepository = retro.create(ICajaRepository.class);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = dialogCargaAsync.getDialogCarga("Verificando datos...");
            dialog.show();
        }

        @Override
        protected RetornoApertura doInBackground(Void... voids) {
            SolicitudEnvio<BigDecimal> solicitudEnvio = new SolicitudEnvio<>(codeCia,
                    "2", montoApertura,
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario);


            try {
                return iCajaRepository.AperturarCaja(solicitudEnvio).execute().body();
            } catch (IOException e) {
                RetornoApertura retorno = new RetornoApertura((byte) 99, 0);
                return retorno;

            } catch (Exception ex) {
                RetornoApertura retorno = new RetornoApertura((byte) 99, 0);
                return retorno;
            }
        }

        @Override
        protected void onPostExecute(RetornoApertura retornoApertura) {
            super.onPostExecute(retornoApertura);
            if (retornoApertura.getRespuesta() == 99) {
                Toast.makeText(getContext(), "Error al abrir caja", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Verifique su conexión", Toast.LENGTH_SHORT).show();
            } else if (retornoApertura.getRespuesta() == 100) {

                Toast.makeText(getContext(), "Ya existe una caja abierta", Toast.LENGTH_SHORT).show();
            } else if (retornoApertura.getRespuesta() == 101) {
                Toast.makeText(getContext(), "La caja se aperturo", Toast.LENGTH_SHORT).show();
            } else if (retornoApertura.getRespuesta() == 0) {
                Toast.makeText(getContext(), "Error al abrir caja", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Verifique su conexión", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

}
