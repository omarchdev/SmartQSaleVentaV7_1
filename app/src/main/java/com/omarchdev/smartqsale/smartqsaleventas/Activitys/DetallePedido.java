package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ParamActivitys.PARAM_ESTADO_PEDIDO_PAGADO;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.ParamActivitys.PARAM_IDPEDIDO;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncPedidos;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorVentas;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.cImpresion;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfRegistroDatosPedidoCW;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCalculadoraPago;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DecimalControlKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Pedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.RetornoPagoTemporal;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mPagosEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterDetallePedido;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPagosEnVenta;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.math.BigDecimal;
import java.util.List;

public class DetallePedido extends ActivityParent implements SlidingUpPanelLayout.PanelSlideListener, View.OnClickListener, DialogCalculadoraPago.CantidadPago, RvAdapterPagosEnVenta.IAdapterPagos {

    ControladorVentas controladorVentas;
    TextView txtValorDescuento, txtFechaPedido, txtZonaServicio, txtFechaEntrega,
            txtValorBruto, txtDocPago, txtValorNeto,
            txtNombreCliente, txtNombreVendedor, txtObservaciones, txtNumPedido,
            txtEstadoActual, txtClienteEntrega, txtNroCelular, txtFechaCreacion,
            txtEmailEntrega, txtTipoEntrega, txtDireccionEntrega, txtNroPedido, txtTiempoEntrega, txtMetodoPagoEntrega;
    RecyclerView rvDetalle, rvMetodosDePago;
    RelativeLayout rlFechaEntrega, rlEstadoEntrega;
    RvAdapterDetallePedido rvAdapterDetallePedido;
    RvAdapterPagosEnVenta rvAdapterPagosEnVenta;
    List<mPagosEnVenta> listpagosventa;
    RadioGroup rgEntregado;
    Button btnEstadoEntrega, btnEstadoPago;
    int idCabeceraPedido;
    FloatingActionButton fabImprimirVenta, fabAdelanto;
    FloatingActionsMenu floating_button_pedido;
    mCabeceraPedido cabeceraPedido;
    DetalleVenta detalleVenta;
    SlidingUpPanelLayout panel;
    MenuItem action_add, action_Save;
    DfRegistroDatosPedidoCW dfRegistroDatosPedidoCW;
    List<ProductoEnVenta> listaProductos;
    RadioButton rb1, rb2;
    FrameLayout fLayout;
    boolean permitir;
    DbHelper helper;
    boolean estadoPagado;
    boolean estadoEntregadoTemporal;
    LinearLayout content_datos_entrega, llContentPedido;
    ProgressBar pbPedido;
    BigDecimal saldoPendiente;
    Context context;
    Pedido pedidoResult;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.menu_item_config, menu);
            action_add = menu.findItem(R.id.action_add);
            action_Save = menu.findItem(R.id.action_Save);
            action_add.setVisible(false);
            action_Save.setVisible(false);

            if (Constantes.Tienda.ZonasAtencion) {
                if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {
                    action_add.setVisible(true);
                } else if (Constantes.Tienda.cTipoZonaServicio.equals("M")) {
                    action_add.setVisible(false);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add:

                if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.ModificarZonaAtenciónPedidoReserva)) {
                    panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("No tiene permiso para realizar está opción").setTitle("Advertencia").setPositiveButton("Salir", null).create().show();
                }

                break;

            case R.id.action_Save:

                permitir = true;
                if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {
                    if (dfRegistroDatosPedidoCW != null) {

                        new AlertDialog.Builder(this).setMessage("¿Está seguro de guardar los datos ingresados?")
                                .setTitle("Confirmación").setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dfRegistroDatosPedidoCW.GuardarDatosDetallePedido();
                                    }
                                }).setNegativeButton("Salir", null).create().show();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);
        context = this;
        try {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            saldoPendiente = new BigDecimal(0);
            helper = new DbHelper(this);
            permitir = true;
            estadoPagado = false;
            cabeceraPedido = new mCabeceraPedido();
            detalleVenta = new DetalleVenta();
            controladorVentas = new ControladorVentas();
            panel = findViewById(R.id.panel);
            txtNumPedido = findViewById(R.id.txtNumPedido);
            llContentPedido = findViewById(R.id.llContentPedido);
            btnEstadoPago = findViewById(R.id.btnEstadoPago);
            content_datos_entrega = findViewById(R.id.content_datos_entrega);
            rlEstadoEntrega = findViewById(R.id.rlEstadoEntrega);
            floating_button_pedido = findViewById(R.id.floating_button_pedido);
            fabImprimirVenta = findViewById(R.id.fabImprimirVenta);
            fabAdelanto = findViewById(R.id.fabAdelanto);
            txtZonaServicio = findViewById(R.id.txtZonaServicio);
            rgEntregado = findViewById(R.id.rgEntregado);
            txtDocPago = findViewById(R.id.txtDocPago);
            txtTiempoEntrega = findViewById(R.id.txtTiempoEntrega);
            txtEstadoActual = findViewById(R.id.txtEstadoActual);
            txtClienteEntrega = findViewById(R.id.txtClienteEntrega);
            txtMetodoPagoEntrega = findViewById(R.id.txtMetodoPagoEntrega);
            txtNroCelular = findViewById(R.id.txtNroCelular);
            txtEmailEntrega = findViewById(R.id.txtEmailEntrega);
            txtTipoEntrega = findViewById(R.id.txtTipoEntrega);
            txtDireccionEntrega = findViewById(R.id.txtDireccionEntrega);
            txtNroPedido = findViewById(R.id.txtNroPedido);
            txtFechaCreacion = findViewById(R.id.txtFechaCreacion);
            btnEstadoEntrega = findViewById(R.id.btnEstadoEntrega);
            txtObservaciones = findViewById(R.id.txtObservaciones);
            rlFechaEntrega = findViewById(R.id.rlFechaEntrega);
            txtFechaPedido = findViewById(R.id.txtFechaPedido);
            txtNombreCliente = findViewById(R.id.txtNombreCliente);
            txtNombreVendedor = findViewById(R.id.txtNombreVendedor);
            txtValorDescuento = findViewById(R.id.valorDescuentoDato);
            txtValorBruto = findViewById(R.id.txtValorBrutoDato);
            txtValorNeto = findViewById(R.id.txtValorNetoDato);
            txtFechaEntrega = findViewById(R.id.txtFechaEntrega);
            rvAdapterDetallePedido = new RvAdapterDetallePedido();
            rvDetalle = findViewById(R.id.rvDetallePedido);
            rvDetalle.setAdapter(rvAdapterDetallePedido);
            rvDetalle.setLayoutManager(new LinearLayoutManager(this));
            idCabeceraPedido = getIntent().getIntExtra(PARAM_IDPEDIDO, 0);
            estadoPagado = getIntent().getBooleanExtra(PARAM_ESTADO_PEDIDO_PAGADO, false);
            rvMetodosDePago = findViewById(R.id.rvMetodosDePago);
            rvAdapterPagosEnVenta = new RvAdapterPagosEnVenta(idCabeceraPedido);
            rvMetodosDePago.setAdapter(rvAdapterPagosEnVenta);
            rvMetodosDePago.setLayoutManager(new LinearLayoutManager(this));
            rvMetodosDePago.setHasFixedSize(true);
            byte type = 2;
            rvAdapterPagosEnVenta.setTypeView(type);
            btnEstadoPago.setOnClickListener(this);

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            fLayout = findViewById(R.id.content);
            getSupportActionBar().setTitle("Detalle del Pedido");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
            fabImprimirVenta.setOnClickListener(this);
            rb1 = findViewById(R.id.rb1);
            rb2 = findViewById(R.id.rb2);
            btnEstadoEntrega.setOnClickListener(this);
            if (!Constantes.ConfigTienda.bUsaFechaEntrega) {
                rlFechaEntrega.setVisibility(View.GONE);
                rlEstadoEntrega.setVisibility(View.GONE);
            }
            if (Constantes.Tienda.ZonasAtencion) {
                if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {
                    Drawable image = this.getResources().getDrawable(R.drawable.ic_car_side_grey600_24dp);
                    image.setBounds(10, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                    txtZonaServicio.setCompoundDrawables(image, null, null, null);
                }
            } else {
                txtZonaServicio.setVisibility(View.GONE);
            }
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
            //   rvAdapterDetallePedido.AddElement(controladorVentas.getDetallePedidoId(idCabeceraPedido));
            if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            panel.addPanelSlideListener(this);
            panel.setTouchEnabled(false);
            rb1.setEnabled(false);
            rb2.setEnabled(false);
            content_datos_entrega.setVisibility(View.GONE);
            pbPedido = findViewById(R.id.pbPedido);
            fabAdelanto.setOnClickListener(this);
            if (estadoPagado) {

                fabAdelanto.setVisibility(View.GONE);

            }
            rvAdapterPagosEnVenta.setiAdapterPagos(this);
            rvAdapterPagosEnVenta.setShowDelete((!estadoPagado && Constantes.ConfigTienda.bUsaAdelantoPagoPedido));
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new DownloadCabecera().execute(idCabeceraPedido);
    }

    @Override
    public void onBackPressed() {
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void AsignarValorCabecera(final mCabeceraPedido cabeceraPedido) {
        this.cabeceraPedido = cabeceraPedido;
        txtNombreCliente.setText(cabeceraPedido.getCliente().getRazonSocial());
        txtNombreVendedor.setText(cabeceraPedido.getVendedor().getPrimerNombre());
        txtFechaPedido.setText(cabeceraPedido.getFechaReserva());
        txtNumPedido.setText(cabeceraPedido.GetIndentificadorUnicoSimple());
        if (cabeceraPedido.getObservacion() != null) {
            txtObservaciones.setText(cabeceraPedido.getObservacion().replace("\\n", "\n"));
        }
        rb1.setEnabled(true);
        rb2.setEnabled(true);
        if (cabeceraPedido.isbEntregado()) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }

        rgEntregado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        if (!cabeceraPedido.isbEntregado())
                            verificarEstado();
                        break;

                    case R.id.rb2:
                        if (cabeceraPedido.isbEntregado())
                            verificarEstado();
                        break;
                }
            }
        });
        rvAdapterPagosEnVenta.AgregarPagosEnVenta(listpagosventa);
        txtFechaEntrega.setText(cabeceraPedido.getFechaEntrega());
        txtValorDescuento.setText(DecimalControlKt.montoDecimalPrecioSimbolo(cabeceraPedido.getDescuentoPrecio()));
        txtValorBruto.setText(DecimalControlKt.montoDecimalPrecioSimbolo(cabeceraPedido.getTotalBruto()));
        txtValorNeto.setText(DecimalControlKt.montoDecimalPrecioSimbolo(cabeceraPedido.getTotalNeto()));
        if (estadoPagado) {
            txtDocPago.setVisibility(View.VISIBLE);
            txtDocPago.setText(cabeceraPedido.getDocumentoPago());
        } else {
            txtDocPago.setVisibility(View.GONE);
        }
        if (cabeceraPedido.getZonaServicio().getIdZona() == 0) {

            txtZonaServicio.setVisibility(View.GONE);
        } else {
            txtZonaServicio.setText(cabeceraPedido.getZonaServicio().getDescripcion());
            txtZonaServicio.setVisibility(View.VISIBLE);
        }
    }

    public void ActualizarEstadoEntregado() {

        AsyncPedidos asyncPedidos = new AsyncPedidos(this);
        asyncPedidos.ActualizarEstadoEntregado(idCabeceraPedido);
        asyncPedidos.setIActualizarEstadoEntrega(estadoEntrega -> {
            cabeceraPedido.setbEntregado(estadoEntrega.getBestado());
            if (cabeceraPedido.isbEntregado()) {
                rb1.setChecked(true);
            } else {
                rb2.setChecked(true);
            }
        });
    }

    public void verificarEstado() {
        if (!cabeceraPedido.isbEntregado()) {
            new AlertDialog.Builder(this).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActualizarEstadoEntregado();
                }
            }).setNegativeButton("Cancelar", (dialog, which) -> {
                if (cabeceraPedido.isbEntregado()) {
                    rb1.setChecked(true);
                } else {
                    rb2.setChecked(true);
                }
            }).setMessage("¿Desea marcar este pedido como ENTREGADO ?").setTitle("Advertencia").show();
        } else {
            new AlertDialog.Builder(this).setPositiveButton("Aceptar", (dialog, which) -> ActualizarEstadoEntregado()).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (cabeceraPedido.isbEntregado()) {
                        rb1.setChecked(true);
                    } else {
                        rb2.setChecked(true);
                    }
                }
            }).setMessage("¿Desea marcar este pedido como NO ENTREGADO?").setTitle("Advertencia").show();
        }
    }

    public String convertirFormatoFecha(int fecha) {
        int anio = fecha / 10000;
        int mes = (fecha % 10000) / 100;
        int dia = fecha % 100;
        return String.valueOf(dia) + "/" + String.valueOf(mes) + "/" + anio;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {


        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            if (action_add != null) {
                action_add.setVisible(false);
                action_Save.setVisible(true);
                if (Constantes.Tienda.ZonasAtencion) {
                    if (Constantes.Tienda.cTipoZonaServicio.equals("A")) {
                        if (dfRegistroDatosPedidoCW == null) {
                            dfRegistroDatosPedidoCW = new DfRegistroDatosPedidoCW().newInstance(idCabeceraPedido);
                            FragmentManager fml = getSupportFragmentManager();
                            FragmentTransaction ftl = fml.beginTransaction();
                            ftl.replace(R.id.content, dfRegistroDatosPedidoCW);
                            ftl.commit();
                        }
                    } else if (Constantes.Tienda.cTipoZonaServicio.equals("M")) {
                        action_add.setVisible(false);
                        action_Save.setVisible(false);
                    }

                }

            }
        } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            if (action_add != null) {
                action_add.setVisible(true);
                action_Save.setVisible(false);
                if (Constantes.Tienda.cTipoZonaServicio.equals("M")) {
                    action_add.setVisible(false);
                    action_Save.setVisible(false);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabImprimirVenta) {

            new AlertDialog.Builder(this).setTitle("Confirmacion")
                    .setMessage("Se envio a imprimir")
                    .setPositiveButton("Salir", null).show();
            cImpresion impresion = new cImpresion(this);

            impresion.ImpresionPedido(pedidoResult);
        } else if (v.getId() == R.id.btnEstadoEntrega) {

            Intent intent = new Intent(this, ActModEstadoEntregaEnPedido.class);
            intent.putExtra("idCabeceraPedido", idCabeceraPedido);
            startActivity(intent);
        } else if (v.getId() == R.id.btnEstadoPago) {
            Intent intent = new Intent(this, ActEstadoPagoFlujoPedido.class);
            intent.putExtra("idCabeceraPedido", idCabeceraPedido);
            startActivity(intent);
        } else if (v.getId() == R.id.fabAdelanto) {
            Toast.makeText(this, saldoPendiente.toString(), Toast.LENGTH_SHORT).show();
            if (saldoPendiente.compareTo(new BigDecimal(0)) == 1) {
                DialogCalculadoraPago dialogCalculadoraPago = new DialogCalculadoraPago().newInstance(Constantes.MediosPago.mediosPago.get(0).getiIdMedioPago(),
                        this, saldoPendiente,
                        Constantes.MediosPago.mediosPago.get(0).getcCodigoMedioPago(),
                        Constantes.MediosPago.mediosPago.get(0).getcDescripcionMedioPago());
                DialogFragment dialogFragment = dialogCalculadoraPago;
                dialogFragment.show(getFragmentManager(), "CalculadoraPago");
                dialogCalculadoraPago.setListenerCantidadPago(this);
            }
        }


    }

    @Override
    public void PasarCantidadCancelada(int idMetodoPago, BigDecimal cantidadCancelada, String codigoTipoPago, String nombreTipoPago) {
        BigDecimal cantidadPagada = new BigDecimal(0);
        boolean encontro = false;
        if (listpagosventa.size() > 0) {
            for (int i = 0; i < listpagosventa.size(); i++) {
                if (listpagosventa.get(i).getIdTipoPago() == idMetodoPago) {
                    cantidadPagada = listpagosventa.get(i).getCantidadPagada().add(cantidadCancelada);
                    encontro = true;
                }
            }
        } else {
            cantidadPagada = cantidadCancelada;
        }
        if (!encontro) {
            cantidadPagada = cantidadCancelada;
        }


        mPagosEnVenta pagosEnVenta = new mPagosEnVenta(idMetodoPago, codigoTipoPago, nombreTipoPago, cantidadPagada, false);
        new AgregarPagoTemporal().execute(pagosEnVenta);
    }

    @Override
    public void ClickPosition(int iPosition, int type) {


        new DeletePago().execute(idCabeceraPedido, listpagosventa.get(iPosition).getIdTipoPago(),listpagosventa.get(iPosition).getIdPago());

    }

    private class DeletePago extends AsyncTask<Integer, Void, Integer> {

        DialogCargaAsync dialogCargaAsync;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialogCargaAsync = new DialogCargaAsync(context);
            dialogCargaAsync.getDialogCarga("Eliminado pago").show();

        }

        @Override
        protected Integer doInBackground(Integer... integers) {

            BdConnectionSql.getSinglentonInstance().EliminarPagoTemporalv2(integers[0], integers[1], integers[2]);

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dialogCargaAsync.hide();
            new DownloadCabecera().execute(idCabeceraPedido);
        }


    }

    private class AgregarPagoTemporal extends AsyncTask<mPagosEnVenta, Void, RetornoPagoTemporal> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected RetornoPagoTemporal doInBackground(mPagosEnVenta... mPagosEnVentas) {
            return BdConnectionSql.getSinglentonInstance().GuardarPagoTemporal(idCabeceraPedido, mPagosEnVentas[0]);
        }

        @Override
        protected void onPostExecute(RetornoPagoTemporal retornoPagoTemporal) {
            super.onPostExecute(retornoPagoTemporal);
            new DownloadCabecera().execute(idCabeceraPedido);
        }

    }

    private class DownloadCabecera extends AsyncTask<Integer, Void, Pedido> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            llContentPedido.setVisibility(View.GONE);
            pbPedido.setVisibility(View.VISIBLE);
            fabAdelanto.setVisibility(View.GONE);
            fabImprimirVenta.setVisibility(View.GONE);
        }

        @Override
        protected Pedido doInBackground(Integer... integers) {

            return controladorVentas.ObtenerPedidoId(integers[0], estadoPagado);

        }

        @Override
        protected void onPostExecute(Pedido pedido) {
            super.onPostExecute(pedido);
            fabAdelanto.setVisibility(View.VISIBLE);
            fabImprimirVenta.setVisibility(View.VISIBLE);
            llContentPedido.setVisibility(View.VISIBLE);
            pbPedido.setVisibility(View.GONE);
            pedidoResult = pedido;
            if (pedido != null) {
                listpagosventa = pedido.getPagosEnPedido();

                saldoPendiente = new BigDecimal(0);
                boolean encontro = false;
                for (int i = 0; i < listpagosventa.size(); i++) {
                    if (listpagosventa.get(i).getIdTipoPago() ==0) {
                        saldoPendiente = listpagosventa.get(i).getCantidadPagada();
                        encontro = true;
                    }
                }
                if (encontro == false) {
                    saldoPendiente = new BigDecimal(0);
                }

                AsignarValorCabecera(pedido.getCabeceraPedido());
                listaProductos = pedido.getListProducto();
                rvAdapterDetallePedido.AddElement(listaProductos);
                if (pedido.getIdEntregaPedido() != 0) {
                    content_datos_entrega.setVisibility(View.VISIBLE);
                    txtEstadoActual.setText("");
                    txtMetodoPagoEntrega.setText(pedido.getEntregaPedidoInfo().getMedioPagoEntrega().getCDescripcionMedioPago());
                    txtFechaCreacion.setText(pedido.getEntregaPedidoInfo().getCFechaCreacion());
                    txtClienteEntrega.setText(pedido.getEntregaPedidoInfo().getClienteEntrega().getcName() + " " + pedido.getEntregaPedidoInfo().getClienteEntrega().getcApellidoPaterno());
                    txtNroCelular.setText(pedido.getEntregaPedidoInfo().getClienteEntrega().getcNumberPhone());
                    txtEmailEntrega.setText(pedido.getEntregaPedidoInfo().getClienteEntrega().getCemail2());
                    txtTipoEntrega.setText(pedido.getEntregaPedidoInfo().getTipoEntregaPedido().getDescripcionTipoEntrega());
                    txtDireccionEntrega.setText(pedido.getEntregaPedidoInfo().getTipoEntregaPedido().getCiudadLocalidad()
                            + " " + pedido.getEntregaPedidoInfo().getTipoEntregaPedido().getCalleNumero()
                            + " " + pedido.getEntregaPedidoInfo().getTipoEntregaPedido().getReferencia()
                    );

                    txtNroPedido.setText(pedido.getEntregaPedidoInfo().getCNumeroPedido());
                    txtTiempoEntrega.setText(pedido.getEntregaPedidoInfo().getTiempoEntregaPedido().getCDescripcionEntrega());
                    try {
                        btnEstadoEntrega.setText("Estado de entrega\n" + pedido.getFlujoEntrega().EstadoEntrega());
                        btnEstadoPago.setText("Estado de pago\n" + pedido.getFlujoPagoPedido().EstadoPagoEntrega());
                    } catch (Exception e) {

                        Log.d("error", e.toString());

                    }

                } else {

                    content_datos_entrega.setVisibility(View.GONE);
                }
                fabAdelanto.setVisibility((Constantes.ConfigTienda.bUsaAdelantoPagoPedido) ? View.VISIBLE : View.GONE);

            } else {
                finish();

            }
        }
    }


}
