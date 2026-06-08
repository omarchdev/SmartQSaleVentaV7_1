package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.MensajeError;
import com.omarchdev.smartqsale.smartqsaleventas.Model.MotivoAnulacionDto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ActualizarNotaDto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRespuestaVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVentaRepository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProcesoVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Bluetooth.BluetoothConnection;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.ConsultaHttp.HttpConsultas;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ExternalApps;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.FacturaActivaController;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.cImpresion;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogEnvioCPENumero;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.SelectAnulacion;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dfMotivoNota;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DecimalControlKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultProcces;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultadoComprobante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.TipoAnulacion;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Venta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDetalleVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDocVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mPagosEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVenta;
import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.PrintOptions;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterDetalleVenta;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPagosEnVenta;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetalleVenta extends ActivityParent
        implements View.OnClickListener, dfMotivoNota.IMotivoNota, SelectAnulacion.CodeAnulacion {

    BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API).client(Constantes.ConfiRetrofitTimeOut.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build();
    IVentaRepository iVentaRepository = retro.create(IVentaRepository.class);
    final String codeCia = GetJsonCiaTiendaBase64x3();
    TextView txtFechaVenta, txtEstadoVenta, txtIdentificador, txtNumFactura, txtNombreCliente;
    TextView txtEstadoDocumentoCpe, txtNombreVendedor, txtValorBruto, txtValorDescuento, txtValorNeto, txtValorCambio;
    RvAdapterDetalleVenta adapter;
    String deviceAddressBt = "";
    AsyncProcesoVenta asyncProcesoVenta;
    RvAdapterPagosEnVenta rvAdapterPagosEnVenta;
    RecyclerView rvDetallePedido, rvMetodosPago;
    NestedScrollView svContent;
    ProgressBar progressBar;
    FloatingActionButton fcancelButton, fabImprimirVenta;
    FloatingActionButton fabEnvioCpe;
    FloatingActionsMenu floatingActionsMenu;
    mVenta venta;
    TextView txtObservacion;
    mCabeceraVenta cabeceraVenta;
    List<mDetalleVenta> listDetalleVenta;
    List<ProductoEnVenta> listaProductos;
    List<mPagosEnVenta> listPagosVenta;
    Dialog dialog;
    int idCabeceraVenta = 0;
    DbHelper helper;
    byte type = 2;
    BluetoothConnection btConnection;
    PrintOptions printOptions;
    DbHelper dbHelper;
    List<ProductoEnVenta> listaP;
    private WebView mWebView;
    Context context;
    String motivo;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    int codeAnulacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_venta);
        helper = new DbHelper(this);
        btConnection = BluetoothConnection.getSinglentonInstance(this);
        context = this;
        idCabeceraVenta = getIntent().getIntExtra("idCabeceraVenta", 0);
        progressBar = (ProgressBar) findViewById(R.id.pbDetalleVenta);
        venta = new mVenta();
        listDetalleVenta = new ArrayList<>();
        listPagosVenta = new ArrayList<>();
        txtEstadoDocumentoCpe = (TextView) findViewById(R.id.txtEstadoDocumentoCpe);
        svContent = findViewById(R.id.svContent);
        txtFechaVenta = (TextView) findViewById(R.id.txtFechaPedido);
        txtEstadoVenta = (TextView) findViewById(R.id.txtEstadoVenta);
        txtNombreCliente = (TextView) findViewById(R.id.txtNombreCliente);
        txtNombreVendedor = (TextView) findViewById(R.id.txtNombreVendedor);
        txtValorBruto = (TextView) findViewById(R.id.txtValorBrutoDato);
        txtValorDescuento = (TextView) findViewById(R.id.valorDescuentoDato);
        txtValorNeto = (TextView) findViewById(R.id.txtValorNetoDato);
        rvDetallePedido = (RecyclerView) findViewById(R.id.rvDetallePedido);
        rvMetodosPago = (RecyclerView) findViewById(R.id.rvMetodosDePago);
        txtValorCambio = (TextView) findViewById(R.id.txtValorCambio);
        fcancelButton = (FloatingActionButton) findViewById(R.id.fabCancelarVenta);
        txtIdentificador = findViewById(R.id.txtIdentificador);
        fabImprimirVenta = findViewById(R.id.fabImprimirVenta);
        fabEnvioCpe = findViewById(R.id.fabEnvioCpe);

        txtObservacion=findViewById(R.id.txtObservacion);
        asyncProcesoVenta=new AsyncProcesoVenta();
        fabEnvioCpe.setOnClickListener(this);
        fcancelButton.setOnClickListener(this);
        txtNumFactura = findViewById(R.id.txtNumFactura);
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floating_button_Venta);
        listaP = new ArrayList<>();
        dbHelper = new DbHelper(this);
        if (idCabeceraVenta == 0) {
            finish();
            Toast.makeText(this, "Error al descargar la venta", Toast.LENGTH_SHORT).show();
        }

        adapter = new RvAdapterDetalleVenta();
        rvDetallePedido.setAdapter(adapter);
        rvDetallePedido.setLayoutManager(new LinearLayoutManager(this));
        rvDetallePedido.setNestedScrollingEnabled(true);
        rvMetodosPago.setLayoutManager(new LinearLayoutManager(this));
        rvAdapterPagosEnVenta = new RvAdapterPagosEnVenta(0);
        rvAdapterPagosEnVenta.setTypeView(type);
        rvMetodosPago.setAdapter(rvAdapterPagosEnVenta);
        fabEnvioCpe.setVisibility(View.GONE);
        txtEstadoDocumentoCpe.setVisibility(View.GONE);
        fabImprimirVenta.setOnClickListener(this);
        txtNumFactura.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
        }
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Detalle Venta");
        }

        new DownloadDetalle().execute(idCabeceraVenta);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fabCancelarVenta:
                if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.HistorialVenta)) {
                    EjecutarValidacionPrevioAnulacion();
                } else {
                    new AlertDialog.Builder(context).setTitle("Atención")
                            .setPositiveButton("Salir", null)
                            .setMessage("No permitido").show();
                }
                break;
            case R.id.fabImprimirVenta:
                SeleccionarMetodo();
                break;
            case R.id.fabEnvioCpe:

                MandarWhatsAppCPE();
                break;
        }
    }


    private void MandarWhatsAppCPE() {
        asyncProcesoVenta.GetNumeroTelefono(cabeceraVenta.getIdVenta(), resultNumTelefono -> {
            DialogEnvioCPENumero dialog= new DialogEnvioCPENumero().newInstance(
                    cabeceraVenta.getIdVenta(),
                    Constantes.Empresa.idEmpresa,
                    resultNumTelefono.getNumTelefono());
            dialog.setIDialogEnvioCPENumero((idCabeceraVenta, IdCompany, numero) -> {
                asyncProcesoVenta.GetContentWhatCPE(idCabeceraVenta, numero, envioCpeW -> {
                    new ExternalApps(context).EnvioMensajeWhatsapp(envioCpeW.getContent());
                });

            });
            dialog.show(getSupportFragmentManager(), "");
        });

    }

    private void EjecutarValidacionPrevioAnulacion() {
        asyncProcesoVenta.setContext(context);
        asyncProcesoVenta.ValidacionPrevioAnulacion(
            idCabeceraVenta,
            Constantes.Tienda.idTienda,
            Constantes.Empresa.idEmpresa,
            new AsyncProcesoVenta.IValidacionPrevioAnulacion() {
                @Override
                public void onResultadoValidacion(List<MensajeError> mensajes) {
                    if (mensajes == null) {
                        new AlertDialog.Builder(context)
                            .setTitle("Atención")
                            .setMessage("Error de conexión al realizar la validación previa.")
                            .setPositiveButton("Aceptar", null)
                            .show();
                        return;
                    }
                    if (!mensajes.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (MensajeError msg : mensajes) {
                            sb.append(msg.getCMensaje()).append("\n");
                        }
                        new AlertDialog.Builder(context)
                            .setTitle("Atención")
                            .setMessage(sb.toString().trim())
                            .setPositiveButton("Aceptar", null)
                            .show();
                    } else {
                        ConfirmarCancelarVenta();
                    }
                }
            }
        );
    }

    private void ConfirmarCancelarVenta() {

        if (cabeceraVenta.getNumSerie() != null && cabeceraVenta.getNumeroCorrelativo() != null) {
            if (cabeceraVenta.getNumSerie().trim().isEmpty() && cabeceraVenta.getNumeroCorrelativo().trim().isEmpty()) {
                new AlertDialog.Builder(this).setTitle("ATENCIÓN").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new CancelarVenta().execute();
                    }
                }).setNegativeButton("No", null).setMessage("¿Desea anular la venta?").show();
            } else if (!cabeceraVenta.getNumSerie().trim().isEmpty() && !cabeceraVenta.getNumeroCorrelativo().trim().isEmpty()) {
                new ObtenerTiposAnulacion().execute();
                /*
                new AlertDialog.Builder(this).setTitle("Atencion").setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new dfMotivoNota().show(getSupportFragmentManager(),"");
                            }
                        }).setNegativeButton("No",null).setMessage("Este documento genero un documento electrónico.Se generará " +
                        "una nota de crédito al eliminar.¿Desea continuar?")
                        .create().show();
                */

            }


        } else {

        }
    }

    private void MostrarProgressDialog() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Descargando informacion");
        dialog = progressDialog;
        dialog.show();
    }

    private ProgressDialog mostrarMensaje() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando solicitud");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDialog;
    }

    @Override
    public void RecibirMotivoNota(@NotNull String motivo) {
        this.motivo = motivo;
        switch (codeAnulacion) {
            case Constantes.TiposAnulacionDocElectronico.Anulacion:
                new CancelarVentaAnularDoc().execute();
                break;
            case Constantes.TiposAnulacionDocElectronico.GenerarNota:
                new CancelarVentaNota().execute();

                break;
        }

    }

    @Override
    public void codigoSeleccionado(int code) {
        codeAnulacion = code;
        dfMotivoNota fmotivo = null;
        switch (code) {
            case Constantes.TiposAnulacionDocElectronico.Anulacion:
                fmotivo = new dfMotivoNota().newInstance("Anulacion", "Motivo de la anulación");
                break;
            case Constantes.TiposAnulacionDocElectronico.GenerarNota:
                fmotivo = new dfMotivoNota().newInstance("Nota de crédito", "Motivo de la nota de crédito");
                break;
        }
        if (fmotivo != null)
            fmotivo.show(getSupportFragmentManager(), "");

    }

    private class DownloadDetalle extends AsyncTask<Integer, Void, mCabeceraVenta> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            svContent.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            floatingActionsMenu.setVisibility(View.GONE);
        }

        @Override
        protected mCabeceraVenta doInBackground(Integer... integers) {
            try {
                mRespuestaVenta respuestaVenta = iVentaRepository.ObtenerVentaId(codeCia, TIPO_CONSULTA, integers[0]).execute().body();
                if (respuestaVenta != null) {
                    cabeceraVenta = respuestaVenta.getCabeceraVenta();
                    listaP = respuestaVenta.getList();
                    if (cabeceraVenta != null) {
                        listPagosVenta = iVentaRepository.GetPagosVenta(codeCia, TIPO_CONSULTA, cabeceraVenta.getIdVenta()).execute().body();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                cabeceraVenta = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                cabeceraVenta = null;
            }
            return cabeceraVenta;
        }


        @Override
        protected void onPostExecute(mCabeceraVenta result) {
            try {
                if (result != null) {
                    txtFechaVenta.setText(result.getFechaEmision());
                    if (cabeceraVenta.getNumSerie() != null && cabeceraVenta.getNumeroCorrelativo() != null) {
                        if (cabeceraVenta.getNumSerie().length() > 0 && cabeceraVenta.getNumeroCorrelativo().length() != 0) {
                            txtNumFactura.setVisibility(View.VISIBLE);
                            txtNumFactura.setText(cabeceraVenta.getTipoDocumento() + " " +
                                    cabeceraVenta.getNumSerie() + " " + cabeceraVenta.getNumeroCorrelativo());
                        } else {
                            txtNumFactura.setText(cabeceraVenta.getTipoDocumento());
                            txtNumFactura.setVisibility(View.VISIBLE);
                        }
                    }
                    if (result.getIdentificador().length() > 0) {
                        if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {
                            Drawable image = getResources().getDrawable(R.drawable.ic_car_side_grey600_24dp);
                            image.setBounds(10, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());

                            txtIdentificador.setCompoundDrawables(
                                    image,
                                    null,
                                    null,
                                    null);
                        }
                        txtIdentificador.setText(result.getIdentificador());
                        txtIdentificador.setVisibility(View.VISIBLE);
                    } else {
                        txtIdentificador.setVisibility(View.GONE);
                    }

                    if (result.isUsaFacElectronica()) {
                        fabEnvioCpe.setVisibility(View.VISIBLE);
                        txtEstadoDocumentoCpe.setVisibility(View.VISIBLE);
                        if (result.getCodeStatusCPE().equals("0")) {
                            txtEstadoDocumentoCpe.setText("NO DISPONIBLE SUNAT");
                            txtEstadoDocumentoCpe.setTextColor(Color.RED);
                        } else {
                            txtEstadoDocumentoCpe.setText("DISPONIBLE SUNAT");
                            txtEstadoDocumentoCpe.setTextColor(Color.rgb(69, 160, 86));
                        }
                        //txtEstadoDocumentoCpe.setText(result.getcEstadoCPE());
                    } else {
                        txtEstadoDocumentoCpe.setVisibility(View.GONE);
                    }


                    if (result.getEstadoVenta().equals("N")) {

                        if (result.isAnulado()) {
                            fcancelButton.setVisibility(View.GONE);
                        }
                        if (result.getIdTipoDocPago() == 1007) {
                            fcancelButton.setVisibility(View.GONE);

                        }
                        txtEstadoVenta.setText("COMPLETA");
                        txtEstadoVenta.setTextColor(Color.rgb(69, 160, 86));
                    } else if (result.getEstadoVenta().equals("C")) {
                        fcancelButton.setVisibility(View.GONE);
                        txtEstadoVenta.setText("ANULADA");
                        txtEstadoVenta.setTextColor(Color.parseColor("#FFF73838"));
                    }
                    if (result.getCliente().getiId() != 0) {
                        txtNombreCliente.setText(result.getCliente().getRazonSocial());
                    }
                    if (result.getVendedor().getIdVendedor() != 0) {
                        txtNombreVendedor.setText(result.getNombreVendedor());
                    }
                    txtValorBruto.setText(DecimalControlKt.montoDecimalPrecioSimbolo(result.getTotalBruto()));
                    txtValorDescuento.setText(DecimalControlKt.montoDecimalPrecioSimbolo(result.getTotalDescuento()));
                    txtValorNeto.setText(DecimalControlKt.montoDecimalPrecioSimbolo(result.getTotalPagado()));
                    txtValorCambio.setText(DecimalControlKt.montoDecimalPrecioSimbolo(result.getTotalCambio()));
                    if (listDetalleVenta != null) {
                        adapter.AddElement(listaP);
                    } else {
                        finish();
                        Toast.makeText(getBaseContext(), "Inconveniente al descargar la información del detalle" +
                                ".Verifique su conexión a internet.", Toast.LENGTH_LONG).show();
                    }
                    if (listPagosVenta != null) {
                        rvAdapterPagosEnVenta.AgregarPagosEnVenta(listPagosVenta);
                    } else {
                        finish();
                        Toast.makeText(getBaseContext(), "Inconveniente al descargar la información del detalle" +
                                ".Verifique su conexión a internet.", Toast.LENGTH_LONG).show();
                    }
                } else if (result == null) {

                    finish();
                    Toast.makeText(getBaseContext(), "Inconveniente al descargar la información del detalle." +
                            "Verifique su conexión a internet.", Toast.LENGTH_LONG).show();
                }
                svContent.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                floatingActionsMenu.setVisibility(View.VISIBLE);

            } catch (Exception ex) {
                Log.e("ErrorVenta", ex.toString());
            }

        }
    }

    private class CancelarVenta extends AsyncTask<Void, Void, Byte> {
        byte respuesta = 0;
        ResultadoComprobante resultadoComprobante;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = mostrarMensaje();
            dialog.show();
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            try {
                respuesta = iVentaRepository.GetEstadoVenta(codeCia, TIPO_CONSULTA, idCabeceraVenta).execute().body().byteValue();
                if (respuesta == 2) {
                    SolicitudEnvio<Integer> solicitud = new SolicitudEnvio<>(
                        codeCia,
                        TIPO_CONSULTA,
                        idCabeceraVenta,
                        Constantes.Terminal.idTerminal,
                        Constantes.Usuario.idUsuario
                    );
                    respuesta = iVentaRepository.CancelarVenta(solicitud).execute().body().getRespuesta();
                }
                if (cabeceraVenta.getNumeroCorrelativo() != null && cabeceraVenta.getNumSerie() != null) {
                    if (!cabeceraVenta.getNumSerie().trim().isEmpty() && !cabeceraVenta.getNumeroCorrelativo().trim().isEmpty()) {
                        SolicitudEnvio<MotivoAnulacionDto> solNota = new SolicitudEnvio<>(
                            codeCia,
                            TIPO_CONSULTA,
                            new MotivoAnulacionDto(idCabeceraVenta, motivo),
                            Constantes.Terminal.idTerminal,
                            Constantes.Usuario.idUsuario
                        );
                        mDocVenta doc = iVentaRepository.GenerarNota(solNota).execute().body();

                        if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cNuFactura)) {
                            HttpConsultas c = new HttpConsultas();
                            resultadoComprobante = c.GenerarDocumentoElectronicoNubefact(doc);
                        } else if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cActFactura)) {
                            FacturaActivaController facturaActivaController = new FacturaActivaController();
                            resultadoComprobante = facturaActivaController.EmitirComprobanteElectronico(doc);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                respuesta = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                respuesta = 0;
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (aByte == 0) {
                new AlertDialog.Builder(context).setTitle("Atención")
                        .setPositiveButton("Salir", null)
                        .setMessage("Error al anular la venta. Verifique su conexión").show();
            } else if (aByte == 1) {
                new AlertDialog.Builder(context).setTitle("Atención")
                        .setPositiveButton("Salir", null)
                        .setMessage("La venta ya se encuentra anulada").show();
                if (resultadoComprobante != null) {

                }
                txtEstadoVenta.setText("ANULADA");
                txtEstadoVenta.setTextColor(Color.parseColor("#FFF73838"));
            } else if (aByte == 100) {
                new AlertDialog.Builder(context).setTitle("Advertencia")
                        .setPositiveButton("Salir", null)
                        .setMessage("La venta no se puede anular.La caja donde se realizo la venta está cerrada").show();
            } else if (aByte == 101) {
                new AlertDialog.Builder(context).setTitle("Éxito")
                        .setPositiveButton("Salir", null)
                        .setMessage("Venta anulada con éxito").show();
                txtEstadoVenta.setText("ANULADA");
                txtEstadoVenta.setTextColor(Color.parseColor("#FFF73838"));
                floatingActionsMenu.collapse();
                fcancelButton.setVisibility(View.GONE);
                fabImprimirVenta.setVisibility(View.GONE);
            } else if (respuesta == 0) {
                new AlertDialog.Builder(context).setTitle("Atención")
                        .setPositiveButton("Salir", null)
                        .setMessage("Error al anular la venta. Verifique su conexión").show();
            }
            dialog.dismiss();
        }
    }

    private class CancelarVentaNota extends AsyncTask<Void, Void, Byte> {
        byte respuesta = 0;
        ResultadoComprobante resultadoComprobante;
        ResultProcces r;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = mostrarMensaje();
            dialog.show();
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            try {
                r = iVentaRepository.VerificarConfigCorrelativosNota(codeCia, TIPO_CONSULTA, Constantes.Terminal.idTerminal).execute().body();
                if (r.getPermitir()) {
                    respuesta = iVentaRepository.GetEstadoVenta(codeCia, TIPO_CONSULTA, idCabeceraVenta).execute().body().byteValue();
                    if (cabeceraVenta.getNumeroCorrelativo() != null && cabeceraVenta.getNumSerie() != null) {
                        if (!cabeceraVenta.getNumSerie().trim().isEmpty() && !cabeceraVenta.getNumeroCorrelativo().trim().isEmpty()) {

                            SolicitudEnvio<MotivoAnulacionDto> solNota = new SolicitudEnvio<>(
                                codeCia,
                                TIPO_CONSULTA,
                                new MotivoAnulacionDto(idCabeceraVenta, motivo),
                                Constantes.Terminal.idTerminal,
                                Constantes.Usuario.idUsuario
                            );
                            mDocVenta doc = iVentaRepository.GenerarNota(solNota).execute().body();

                            if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cNuFactura)) {
                                HttpConsultas c = new HttpConsultas();
                                resultadoComprobante = c.GenerarDocumentoElectronicoNubefact(doc);
                                if (resultadoComprobante.getCodeSuccess() == 200) {
                                    respuesta = 101;
                                }
                                SolicitudEnvio<ActualizarNotaDto> solActualizar = new SolicitudEnvio<>(
                                    codeCia,
                                    TIPO_CONSULTA,
                                    new ActualizarNotaDto(idCabeceraVenta, resultadoComprobante),
                                    Constantes.Terminal.idTerminal,
                                    Constantes.Usuario.idUsuario
                                );
                                iVentaRepository.ActualizarEstadoNotaGenerada(solActualizar).execute();
                            } else if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cActFactura)) {
                                FacturaActivaController facturaActivaController = new FacturaActivaController();
                                resultadoComprobante = facturaActivaController.EmitirComprobanteElectronico(doc);
                                if (resultadoComprobante.getCodeSuccess() == 200) {
                                    respuesta = 101;
                                }
                                SolicitudEnvio<ActualizarNotaDto> solActualizar = new SolicitudEnvio<>(
                                    codeCia,
                                    TIPO_CONSULTA,
                                    new ActualizarNotaDto(idCabeceraVenta, resultadoComprobante),
                                    Constantes.Terminal.idTerminal,
                                    Constantes.Usuario.idUsuario
                                );
                                iVentaRepository.ActualizarEstadoNotaGenerada(solActualizar).execute();
                            } else if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cMobileSoftPeru)) {
                                resultadoComprobante = new ResultadoComprobante();
                                resultadoComprobante.setCodeSuccess(200);
                                resultadoComprobante.setEstadoRespuesta("OK");
                                resultadoComprobante.setMensaje("");
                                resultadoComprobante.setRecibido(true);

                                SolicitudEnvio<ActualizarNotaDto> solActualizar = new SolicitudEnvio<>(
                                    codeCia,
                                    TIPO_CONSULTA,
                                    new ActualizarNotaDto(idCabeceraVenta, resultadoComprobante),
                                    Constantes.Terminal.idTerminal,
                                    Constantes.Usuario.idUsuario
                                );
                                iVentaRepository.ActualizarEstadoNotaGenerada(solActualizar).execute();

                                respuesta = 101;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                respuesta = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                respuesta = 0;
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (r.getPermitir()) {
                if (aByte == 0) {
                    new AlertDialog.Builder(context).setTitle("Atención")
                            .setPositiveButton("Salir", null)
                            .setMessage("Error al anular la venta. Verifique su conexión").show();
                } else if (aByte == 1) {
                    new AlertDialog.Builder(context).setTitle("Atención")
                            .setPositiveButton("Salir", null)
                            .setMessage("La venta ya se encuentra anulada").show();
                    if (resultadoComprobante != null) {

                    }
                    txtEstadoVenta.setText("ANULADA");
                    txtEstadoVenta.setTextColor(Color.parseColor("#FFF73838"));
                } else if (aByte == 100) {
                    new AlertDialog.Builder(context).setTitle("Advertencia")
                            .setPositiveButton("Salir", null)
                            .setMessage("La venta no se puede anular.La caja donde se realizo la venta está cerrada").show();
                } else if (aByte == 101) {
                    new AlertDialog.Builder(context).setTitle("Éxito")
                            .setPositiveButton("Salir", null)
                            .setMessage("Venta anulada con éxito").show();
                    txtEstadoVenta.setText("ANULADA");
                    txtEstadoVenta.setTextColor(Color.parseColor("#FFF73838"));
                    floatingActionsMenu.collapse();
                    fcancelButton.setVisibility(View.GONE);
                    fabImprimirVenta.setVisibility(View.GONE);
                } else if (respuesta == 0) {
                    new AlertDialog.Builder(context).setTitle("Atención")
                            .setPositiveButton("Salir", null)
                            .setMessage("Error al anular la venta. Verifique su conexión").show();
                }
            } else {

                new AlertDialog.Builder(context).setTitle("Advertencia").
                        setPositiveButton("Salir", null)
                        .setMessage(r.getMessageResult()).show();
            }
            dialog.dismiss();
        }
    }

    private class CancelarVentaAnularDoc extends AsyncTask<Void, Void, Byte> {
        byte respuesta = 0;
        ResultadoComprobante resultadoComprobante;
        ResultProcces r;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = mostrarMensaje();
            dialog.show();
        }

        @Override
        protected Byte doInBackground(Void... voids) {
            try {
                respuesta = iVentaRepository.GetEstadoVenta(codeCia, TIPO_CONSULTA, idCabeceraVenta).execute().body().byteValue();
                if (respuesta == 2) {
                    SolicitudEnvio<Integer> solicitud = new SolicitudEnvio<>(
                        codeCia,
                        TIPO_CONSULTA,
                        idCabeceraVenta,
                        Constantes.Terminal.idTerminal,
                        Constantes.Usuario.idUsuario
                    );
                    respuesta = iVentaRepository.CancelarVenta(solicitud).execute().body().getRespuesta();
                }
                if (cabeceraVenta.getNumeroCorrelativo() != null && cabeceraVenta.getNumSerie() != null) {
                    if (!cabeceraVenta.getNumSerie().trim().isEmpty() && !cabeceraVenta.getNumeroCorrelativo().trim().isEmpty()) {
                        SolicitudEnvio<MotivoAnulacionDto> solAnulacion = new SolicitudEnvio<>(
                            codeCia,
                            TIPO_CONSULTA,
                            new MotivoAnulacionDto(idCabeceraVenta, motivo),
                            Constantes.Terminal.idTerminal,
                            Constantes.Usuario.idUsuario
                        );
                        mDocVenta doc = iVentaRepository.AnularDocumento(solAnulacion).execute().body();
                        if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cNuFactura)) {
                            HttpConsultas c = new HttpConsultas();
                            resultadoComprobante = c.AnularDocumento(doc, motivo);
                        } else if (Constantes.ConfigTienda.CodeFacturacion.equals(Constantes.TFacturacion.cActFactura)) {
                            FacturaActivaController facturaActivaController = new FacturaActivaController();
                            if (doc.getCodeResult() == 100) {
                                resultadoComprobante = facturaActivaController.ComunicacionBajaDocumento(doc, motivo);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                respuesta = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                respuesta = 0;
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (aByte == 0) {
                new AlertDialog.Builder(context).setTitle("Atención")
                        .setPositiveButton("Salir", null)
                        .setMessage("Error al anular la venta. Verifique su conexión").show();
            } else if (aByte == 1) {
                new AlertDialog.Builder(context).setTitle("Atención")
                        .setPositiveButton("Salir", null)
                        .setMessage("La venta ya se encuentra anulada").show();
                if (resultadoComprobante != null) {

                }
                txtEstadoVenta.setText("ANULADA");
                txtEstadoVenta.setTextColor(Color.parseColor("#FFF73838"));
            } else if (aByte == 100) {
                new AlertDialog.Builder(context).setTitle("Advertencia")
                        .setPositiveButton("Salir", null)
                        .setMessage("La venta no se puede anular.La caja donde se realizo la venta está cerrada").show();
            } else if (aByte == 101) {
                new AlertDialog.Builder(context).setTitle("Éxito")
                        .setPositiveButton("Salir", null)
                        .setMessage("Venta anulada con éxito").show();
                txtEstadoVenta.setText("ANULADA");
                txtEstadoVenta.setTextColor(Color.parseColor("#FFF73838"));
                floatingActionsMenu.collapse();
                fcancelButton.setVisibility(View.GONE);
                fabImprimirVenta.setVisibility(View.GONE);
            } else if (respuesta == 0) {
                new AlertDialog.Builder(context).setTitle("Atención")
                        .setPositiveButton("Salir", null)
                        .setMessage("Error al anular la venta. Verifique su conexión").show();
            }
            dialog.dismiss();
        }
    }

    private class ObtenerTiposAnulacion extends AsyncTask<Void, Void, List<TipoAnulacion>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = mostrarMensaje();
            dialog.show();
        }

        @Override
        protected List<TipoAnulacion> doInBackground(Void... voids) {
            try {
                return iVentaRepository.ObtenerTiposAnulacionDocumento(codeCia, TIPO_CONSULTA, idCabeceraVenta).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<TipoAnulacion> tipoAnulacions) {
            super.onPostExecute(tipoAnulacions);
            if (tipoAnulacions != null) {
                if (tipoAnulacions.size() > 1) {
                    new SelectAnulacion().newInstance(tipoAnulacions).show(getSupportFragmentManager(), "");
                } else {
                    if (tipoAnulacions.get(0).getCodeAnulacion() == Constantes.TiposAnulacionDocElectronico.GenerarNota) {
                        codeAnulacion = Constantes.TiposAnulacionDocElectronico.GenerarNota;
                        new AlertDialog.Builder(context).setTitle("Atencion").setPositiveButton("Si",
                                        (dialogInterface, i) -> new dfMotivoNota().newInstance("Nota de crédito", "Motivo de la nota de crédito").show(getSupportFragmentManager(), "")).setNegativeButton("No", null).setMessage("Este documento genero un documento electrónico.Se generará " +
                                        "una nota de crédito al eliminar.¿Desea continuar?")
                                .create().show();
                    }
                }
            }
            dialog.dismiss();

        }
    }

    public void SeleccionarMetodo() {
        try {
            cImpresion impresion = new cImpresion(this);
            if (Constantes.Empresa.cDireccion != null) {
                if (Constantes.Empresa.cDireccion.length() > 0) {
                    cabeceraVenta.setNombreCiudad(Constantes.Empresa.cDireccion);
                }
            }

            if (Constantes.Empresa.NumRuc != null) {
                if (Constantes.Empresa.NumRuc.length() > 0) {
                    cabeceraVenta.setRucEmisor(Constantes.Empresa.NumRuc);

                }
            }

            if (cabeceraVenta.getNumSerie().length() > 0) {
                if (Constantes.ConfigTienda.LinkTicket.isEmpty()) {
                    cabeceraVenta.setRucEmisor(Constantes.Empresa.NumRuc);
                    cabeceraVenta.setEnlaceNf("https://www.nubefact.com/" + cabeceraVenta.getRucEmisor());
                } else {
                    cabeceraVenta.setEnlaceNf(Constantes.ConfigTienda.LinkTicket);
                }
            }
            if (Constantes.Empresa.Razon_Social != null) {
                if (Constantes.Empresa.Razon_Social.length() > 0) {
                    cabeceraVenta.setEmisor(Constantes.Empresa.Razon_Social);
                }
            }
            new AlertDialog.Builder(this).setTitle("Confirmación")
                    .setMessage("Se mando a imprimir el comprobante")
                    .setPositiveButton("Salir", null).create().show();
            impresion.ImpresionDocVenta(listaP, cabeceraVenta, cabeceraVenta.getIdTipoDocPago());
        } catch (Exception e) {
            e.toString();
        }
    }
}

















