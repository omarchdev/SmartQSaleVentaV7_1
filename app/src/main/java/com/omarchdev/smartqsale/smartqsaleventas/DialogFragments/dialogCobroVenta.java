package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Control.MethodsNumber.ReplaceCommaToDot;
import static com.omarchdev.smartqsale.smartqsaleventas.Controlador.ClickEditTextNumberKt.ClickTextInputLayout;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProcesoVenta;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.TiposAtencion;
import com.omarchdev.smartqsale.smartqsaleventas.Control.NumberTextWatcher;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorMediosPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.PagoVentaTemp;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProcessResult;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Promocion;
import com.omarchdev.smartqsale.smartqsaleventas.Model.RetornoPagoTemporal;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDocPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mPagosEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterGridMetodoPago;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPagosEnVenta;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by OMAR CHH on 09/12/2017.
 */

public class dialogCobroVenta extends DialogFragment implements View.OnClickListener, DialogCalculadoraPago.CantidadPago, RvAdapterPagosEnVenta.CantidadPagosEnVenta, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    //  BdConnectionSql bdConnectionSql;
    Dialog dialog;
    String simboloMoneda;
    String observacion;
    int idCabeceraPedido;
    String TextoTituloPago;
    ImageButton imgArrowBack;
    RelativeLayout rvContenedorPago;
    TextView txtSinMetodoDePago, txtTituloPrecio;
    Button btnFinalizarVenta;
    List<RadioButton> listRadioTiposAtencion;
    RecyclerView rvMetodosDePago;
    LinearLayout contentDetraccion;
    RvAdapterPagosEnVenta rvAdapterPagosEnVenta;
    RvAdapterGridMetodoPago rvGridMPagos;
    Context context;
    GridView gridView;
    ArrayAdapter adTipoDoc;
    BigDecimal cantidadTotalPago;
    BigDecimal cantidadACuenta;
    ListenerVentaFinalizada listenerVentaFinalizada;
    byte MetodoRealizar = 1; //1 Guardar pagos //  2 GuardarPagos y realizar venta
    List<mPagosEnVenta> pagosEnVentaList;
    List<mMedioPago> listMedioPago;
    mPagosEnVenta pagosEnVenta;
    int idCliente = 0;
    AsyncProcesoVenta asyncProcesoVenta;
    int posSelect;
    boolean permitirCobrar;
    List<String> TipoDocList;
    Spinner spnTipoDocPago;
    mCustomer cliente;
    BigDecimal montoTotal;
    boolean permitirVenta;
    Switch swPromocion;
    Promocion promocionGeneral;
    RadioGroup rgAtencion;
    BigDecimal montoUtilizarPromocion;
    CheckBox cbPagoTotal, cbDetraccion;
    TextView txtTituloAtencion;
    TextInputLayout edtObservacion, txtPorcentajeDetraccion, txtMontoDetraccion, txtCuentaDetraccion;
    BigDecimal saldoPendiente;

    final String codeCia = GetJsonCiaTiendaBase64x3();
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IPedidoRespository iPedidoRespository = retro.create(IPedidoRespository.class);

    public dialogCobroVenta() {

    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public dialogCobroVenta newInstance(int idCabeceraPedido, Context context, BigDecimal CantidadCobrar, int idCliente,
                                        List<mMedioPago> listMedioPago, mCustomer cliente, String obs) {

        dialogCobroVenta c = new dialogCobroVenta();
        c.setIdCabeceraPedido(idCabeceraPedido);

        c.setContext(context);
        c.setCantidadTotalPago(CantidadCobrar);
        c.setIdCliente(idCliente);
        c.setListMedioPago(listMedioPago);
        c.setCliente(cliente);
        c.setMontoTotal(CantidadCobrar);
        c.setObservacion(obs);
        return c;
    }

    public void setIdCabeceraPedido(int idCabeceraPedido) {
        this.idCabeceraPedido = idCabeceraPedido;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setCantidadTotalPago(BigDecimal cantidadTotalPago) {
        this.cantidadTotalPago = cantidadTotalPago;
    }

    public void setCliente(mCustomer cliente) {
        this.cliente = cliente;
    }

    public void setListMedioPago(List<mMedioPago> listMedioPago) {
        this.listMedioPago = listMedioPago;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /*
        public dialogCobroVenta(int idCabeceraPedido, Context context, BigDecimal CantidadCobrar, int idCliente, List<mMedioPago> listMedioPago) {
            this.idCabeceraPedido = idCabeceraPedido;
            this.context = context;
            this.cantidadTotalPago = CantidadCobrar;
            this.idCliente = idCliente;
            this.listMedioPago=listMedioPago;

        }
    */
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setListenerVentaFinalizada(ListenerVentaFinalizada listenerVentaFinalizada) {

        this.listenerVentaFinalizada = listenerVentaFinalizada;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //    bdConnectionSql = BdConnectionSql.getSinglentonInstance();
        pagosEnVentaList = new ArrayList<>();
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.elegir_metodo_pago, null);
        declararVariables(v);
        setOnclick();
        this.montoUtilizarPromocion = new BigDecimal(0);
        dialog = builder.setView(v).create();
        asyncProcesoVenta = new AsyncProcesoVenta();
        setItemClickListener();

        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void declararVariables(View v) {

        try {
            saldoPendiente = new BigDecimal(0);
            cantidadACuenta = cantidadTotalPago;
            rvContenedorPago = v.findViewById(R.id.contenedorPago);
            imgArrowBack = v.findViewById(R.id.imgArrowBack);
            this.txtTituloAtencion = v.findViewById(R.id.txtTituloAtencion);
            cbPagoTotal = v.findViewById(R.id.cbPagoTotal);
            txtTituloPrecio = v.findViewById(R.id.txtPrecioTotal);


            txtPorcentajeDetraccion = v.findViewById(R.id.txtPorcentajeDetraccion);
            txtMontoDetraccion = v.findViewById(R.id.txtMontoDetraccion);
            txtCuentaDetraccion = v.findViewById(R.id.txtCuentaDetraccion);
            contentDetraccion = v.findViewById(R.id.contentDetraccion);
            cbDetraccion = v.findViewById(R.id.cbDetraccion);
            cbDetraccion.setChecked(false);
            CierraDetraccion();
            NumberTextWatcher watcherEdtMontoDetraccion = new NumberTextWatcher(txtMontoDetraccion.getEditText());
            NumberTextWatcher watcherEdtPorcentajeDetraccion = new NumberTextWatcher(txtPorcentajeDetraccion.getEditText());
            watcherEdtMontoDetraccion.setINumberTextWatcher(number -> {
            });
            watcherEdtPorcentajeDetraccion.setINumberTextWatcher(number -> {
            });
            txtMontoDetraccion.getEditText().addTextChangedListener(watcherEdtMontoDetraccion);
            txtPorcentajeDetraccion.getEditText().addTextChangedListener(watcherEdtPorcentajeDetraccion);
            ClickTextInputLayout(txtMontoDetraccion);
            ClickTextInputLayout(txtPorcentajeDetraccion);
            cbDetraccion.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    AbreDetraccion();
                } else {
                    CierraDetraccion();
                }
            });


            txtSinMetodoDePago = v.findViewById(R.id.txtSinMetodoDePago);
            rvMetodosDePago = v.findViewById(R.id.rvPagos);
            btnFinalizarVenta = v.findViewById(R.id.btnFinalizarVenta);
            gridView = v.findViewById(R.id.gv_metodosPago);
            spnTipoDocPago = v.findViewById(R.id.spnTipoDocPago);
            rvAdapterPagosEnVenta = new RvAdapterPagosEnVenta(idCabeceraPedido);
            rvMetodosDePago.setLayoutManager(new LinearLayoutManager(context));
            rvMetodosDePago.setAdapter(rvAdapterPagosEnVenta);
            swPromocion = v.findViewById(R.id.swPromocion);
            edtObservacion = v.findViewById(R.id.edtObservacion);
            rgAtencion = v.findViewById(R.id.rgAtencion);
            rvAdapterPagosEnVenta.setListenerCantidadPagos(this);
            rvGridMPagos = new RvAdapterGridMetodoPago();
            gridView.setAdapter(rvGridMPagos);
            cantidadACuenta = cantidadTotalPago;
            simboloMoneda = Constantes.DivisaPorDefecto.SimboloDivisa;
            TextoTituloPago = "Total " + simboloMoneda + String.format("%.2f", cantidadACuenta);
            txtTituloPrecio.setText(TextoTituloPago);
            TipoDocList = new ArrayList<>();
            adTipoDoc = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, TipoDocList);
            spnTipoDocPago.setAdapter(adTipoDoc);
            spnTipoDocPago.setOnItemSelectedListener(this);

            TipoDocList.clear();
            try {
                for (mDocPago it : Constantes.TiposDocPago.listaTipoDocPago) {
                    TipoDocList.add(it.getCDescripcion());
                }
            } catch (Exception e) {
                e.toString();
            }
            adTipoDoc.notifyDataSetChanged();
            spnTipoDocPago.setSelection(0);
            VerificarUsoFacturaElectronica();
            btnFinalizarVenta.setOnClickListener(this);

            if (Constantes.ConfigTienda.usaPromocion) {
                this.swPromocion.setVisibility(View.VISIBLE);
            } else {
                this.swPromocion.setVisibility(View.GONE);
            }

            this.swPromocion.setOnCheckedChangeListener(this);
            this.swPromocion.setChecked(false);
            this.swPromocion.setText("PROMOCION: DESACTIVADO");
            this.listRadioTiposAtencion = new ArrayList();
            for (int i = 0; i < TiposAtencion.lista.size(); i++) {
                RadioButton r = new RadioButton(getActivity());
                r.setText(TiposAtencion.lista.get(i).getDescripcion());
                this.listRadioTiposAtencion.add(r);
                LayoutParams a = new LayoutParams(-2, -2);
                a.setMargins(16, 0, 16, 0);
                r.setLayoutParams(a);
                r.setId(TiposAtencion.lista.get(i).getIdTipoAtencion());
                this.rgAtencion.addView(r);
                if (i == 0) {
                    r.setChecked(true);
                }
            }

            edtObservacion.setCounterMaxLength(250);
            edtObservacion.setCounterEnabled(true);
            edtObservacion.getEditText().setMaxLines(4);


            this.cbPagoTotal.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    dialogCobroVenta.this.txtSinMetodoDePago.setVisibility(View.GONE);
                } else if (dialogCobroVenta.this.rvAdapterPagosEnVenta.getList().isEmpty()) {
                    dialogCobroVenta.this.txtSinMetodoDePago.setVisibility(View.VISIBLE);
                } else {
                    dialogCobroVenta.this.txtSinMetodoDePago.setVisibility(View.GONE);
                }
            });
            this.cbPagoTotal.setChecked(Constantes.ConfigTienda.pagoUnico);
            this.cbPagoTotal.setChecked(Constantes.ConfigTienda.pagoUnico);
            this.cbPagoTotal.setChecked(Constantes.ConfigTienda.pagoUnico);
            if (!this.cliente.getControl1().trim().isEmpty()) {
                this.rgAtencion.check(TiposAtencion.lista.get(1).getIdTipoAtencion());
            }
            if (!Constantes.ConfigTienda.bUsaTipoAtencion) {
                this.rgAtencion.setVisibility(View.GONE);
                this.txtTituloAtencion.setVisibility(View.GONE);
            }
            if (!this.cliente.getControl1().trim().isEmpty()) {
                this.rgAtencion.check(TiposAtencion.lista.get(1).getIdTipoAtencion());
            }
            edtObservacion.getEditText().setText(observacion);

            new DownloadList().execute();
        } catch (Exception e) {
            e.toString();
        }
    }

    public void AbreDetraccion() {
        try {
            contentDetraccion.setVisibility(View.VISIBLE);
            BigDecimal valor = new BigDecimal(0);
            txtMontoDetraccion.getEditText().setText(String.format("%.2f",valor));
            txtPorcentajeDetraccion.getEditText().setText(String.format("%.2f",valor));
            txtCuentaDetraccion.getEditText().setText("");
        }catch (Exception ex){
            ex.toString();
        }
    }

    public void CierraDetraccion() {
        BigDecimal valor = new BigDecimal(0);
        contentDetraccion.setVisibility(View.GONE);
        txtMontoDetraccion.getEditText().setText(String.format("%.2f",valor));
        txtPorcentajeDetraccion.getEditText().setText(String.format("%.2f",valor));
        txtCuentaDetraccion.getEditText().setText("");
    }

    public void VerificarUsoFacturaElectronica() {

        if (Constantes.ConfigTienda.bUsa_Facturacion == true) {
            spnTipoDocPago.setEnabled(true);

            for (int i = 0; i < Constantes.TiposDocPago.listaTipoDocPago.size(); i++) {

                if (Constantes.ConfigTienda.idDocumentoPagoDefecto == Constantes.TiposDocPago.listaTipoDocPago.get(i).getIdDoc()) {
                    spnTipoDocPago.setSelection(i);
                }

            }
        } else if (Constantes.ConfigTienda.bUsa_Facturacion == false) {
            spnTipoDocPago.setEnabled(false);
        } else {
            spnTipoDocPago.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imgArrowBack:

                dialog.dismiss();
                break;
            case R.id.btnFinalizarVenta:

                if (PermitirVenta()) {
                    RealizarPago(rvAdapterPagosEnVenta.getList(),
                            Constantes.TiposDocPago.listaTipoDocPago.get(spnTipoDocPago.getSelectedItemPosition()).getIdDoc(),
                            Constantes.TiposDocPago.listaTipoDocPago.get(spnTipoDocPago.getSelectedItemPosition()).getGeneraDocPago());
                }

                break;
        }

    }

    public boolean PermitirVenta() {
        permitirVenta = true;

        if (idCliente == 0) {
            if (Constantes.TiposDocPago.listaTipoDocPago.get(spnTipoDocPago.getSelectedItemPosition()).getGeneraDocPago()) {
                permitirVenta = false;
                Toast.makeText(getActivity(), "Debe elegir un cliente para realizar la venta", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (Constantes.TiposDocPago.listaTipoDocPago.get(spnTipoDocPago.getSelectedItemPosition()).getGeneraDocPago()) {
                if (cliente.getIdTipoDocumento() == Constantes.TipoDocumentoIdentidad.DNI) {
                    if (Constantes.TiposDocPago.listaTipoDocPago.get(spnTipoDocPago.getSelectedItemPosition()).getIdDoc()
                            == Constantes.TipoDocumentoPago.FACTURA) {
                        permitirVenta = false;
                        Toast.makeText(getActivity(), "El cliente no puede recibir Factura", Toast.LENGTH_SHORT).show();
                    }
                } /*else if (cliente.getIdTipoDocumento() == Constantes.TipoDocumentoIdentidad.RUC) {
                    if (Constantes.TiposDocPago.listaTipoDocPago.get(spnTipoDocPago.getSelectedItemPosition()).getIdDoc()
                            == Constantes.TipoDocumentoPago.BOLETA) {
                        permitirVenta = false;
                        Toast.makeText(getActivity(), "El cliente no puede recibir Boleta", Toast.LENGTH_SHORT).show();
                    }
                } */ else if (cliente.getIdTipoDocumento() == Constantes.TipoDocumentoIdentidad.VARIOS) {
                    if (Constantes.TiposDocPago.listaTipoDocPago.get(spnTipoDocPago.getSelectedItemPosition()).getIdDoc()
                            == Constantes.TipoDocumentoPago.FACTURA) {
                        permitirVenta = false;
                        Toast.makeText(getActivity(), "El cliente no puede recibir Factura", Toast.LENGTH_SHORT).show();
                    } else if (Constantes.TiposDocPago.listaTipoDocPago.get(spnTipoDocPago.getSelectedItemPosition()).getIdDoc()
                            == Constantes.TipoDocumentoPago.BOLETA) {
                        if (montoTotal.compareTo(Constantes.MontosSunat.montoMaximoLibre) == 1) {
                            permitirVenta = false;
                            Toast.makeText(getActivity(),
                                    "El cliente debe tener registrado un número de" +
                                            " documento para recibir boletas con " +
                                            " monto mayores a " + String.format("%.2f",
                                            Constantes.MontosSunat.montoMaximoLibre), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }




        return permitirVenta;
    }

    public void RealizarPago(List<mPagosEnVenta> lista, int idDocPago, boolean generaDoc) {
        boolean permitir = true;
        BigDecimal MontoEfectivo = new BigDecimal(0);
        BigDecimal MontoNoEfectivo = new BigDecimal(0);
        boolean efectivo = false;
        boolean noEfectivo = false;
        for (mPagosEnVenta pagosEnVenta : lista) {
            if (pagosEnVenta.isEsEfectivo()) {
                MontoEfectivo = MontoEfectivo.add(pagosEnVenta.getCantidadPagada());
                efectivo = true;
            } else if (!pagosEnVenta.isEsEfectivo()) {
                noEfectivo = true;
                MontoNoEfectivo = MontoNoEfectivo.add(pagosEnVenta.getCantidadPagada());
            }
        }
        if (!efectivo && noEfectivo) {
            if (MontoNoEfectivo.compareTo(cantidadTotalPago) <= 0) {
                permitir = true;
            } else {
                permitir = false;
                new AlertDialog.Builder(context).setPositiveButton("Salir", null).setTitle("Advertencia").
                        setMessage("El monto en pagos que no son efectivo debe ser menor o igual al monto total a pagar").create().show();
            }
        } else if (efectivo && !noEfectivo) {
            permitir = true;
        } else if (efectivo && noEfectivo) {
            if (MontoNoEfectivo.compareTo(cantidadTotalPago) >= 0) {
                permitir = false;
                new AlertDialog.Builder(context).setPositiveButton("Salir", null).setTitle("Advertencia").
                        setMessage("El monto en pagos que no son efectivo debe ser menor  al monto total a pagar").create().show();
            } else {
                permitir = true;
            }
        }

        if (permitir) {
            listenerVentaFinalizada.GuardarPagos(this.cantidadACuenta,
                    idDocPago, generaDoc, this.rgAtencion.getCheckedRadioButtonId(),
                    this.montoUtilizarPromocion, this.edtObservacion.getEditText().getText().toString(),
                    new BigDecimal(ReplaceCommaToDot(txtMontoDetraccion.getEditText().getText().toString())),
                    new BigDecimal(ReplaceCommaToDot(txtPorcentajeDetraccion.getEditText().getText().toString())),
                    txtCuentaDetraccion.getEditText().getText().toString(),
                    cbDetraccion.isChecked()
            );
            dialog.dismiss();
        }

    }

    public void setOnclick() {
        imgArrowBack.setOnClickListener(this);


    }

    private void setItemClickListener() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (dialogCobroVenta.this.cbPagoTotal.isChecked()) {
                    dialogCobroVenta dialogcobroventa = dialogCobroVenta.this;
                    dialogcobroventa.permitirCobrar = true;
                    if (!dialogcobroventa.rvAdapterPagosEnVenta.getList().isEmpty()) {
                        dialogcobroventa = dialogCobroVenta.this;
                        dialogcobroventa.permitirCobrar = false;
                        new AlertDialog.Builder(dialogcobroventa.getActivity()).setTitle("Advertencia").setMessage("Debe eliminar todos los pagos realizados ").setPositiveButton("Salir", null).create().show();
                    }
                    if (dialogCobroVenta.this.rvGridMPagos.getItem(position).isPorCobrar() && dialogCobroVenta.this.idCliente != 0) {
                        dialogCobroVenta.this.permitirCobrar = true;
                    } else if (dialogCobroVenta.this.rvGridMPagos.getItem(position).isPorCobrar() && dialogCobroVenta.this.idCliente == 0) {
                        dialogcobroventa = dialogCobroVenta.this;
                        dialogcobroventa.permitirCobrar = false;
                        new AlertDialog.Builder(dialogcobroventa.getActivity()).setPositiveButton("Salir", null).setMessage("Debe seleccionar un cliente").setTitle("Advertencia").create().show();
                    }
                    if (dialogCobroVenta.this.permitirCobrar) {
                        dialogcobroventa = dialogCobroVenta.this;
                        dialogcobroventa.pagosEnVenta = new mPagosEnVenta((int) dialogcobroventa.rvGridMPagos.getItemId(position), dialogCobroVenta.this.rvGridMPagos.getItem(position).getcCodigoMedioPago(), dialogCobroVenta.this.rvGridMPagos.getItem(position).getcDescripcionMedioPago(), dialogCobroVenta.this.cantidadTotalPago, false);
                        new RealizarCobroTotal().execute(dialogCobroVenta.this.pagosEnVenta);
                        return;
                    }

                } else {

                    posSelect = position;
                    mostrarCalculadoraPago((int) rvGridMPagos.getItemId(position),
                            rvGridMPagos.getItem(position).isPorCobrar(),
                            rvGridMPagos.getItem(position).getcCodigoMedioPago(),
                            rvGridMPagos.getItem(position).getcDescripcionMedioPago(),

                            cantidadACuenta, rvGridMPagos.getItem(position).isbActivaraCamara());


                }

            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        buttonView.setText(isChecked ? "PROMOCION: ACTIVADO  " : "PROMOCION: DESACTIVADO ");
        if (isChecked) {
            new GetPromocion().execute();
            return;
        }
        this.cantidadTotalPago = this.cantidadTotalPago.add(this.montoUtilizarPromocion);
        this.cantidadACuenta = this.cantidadACuenta.add(this.montoUtilizarPromocion);
        TextView textView;
        StringBuilder stringBuilder;
        if (this.cantidadACuenta.compareTo(new BigDecimal(0)) <= 0) {
            ocultarMetodoPago();
            if (this.cantidadACuenta.compareTo(new BigDecimal(0)) == 0) {
                this.txtTituloPrecio.setText("Pago Terminado");
            } else {
                textView = this.txtTituloPrecio;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Cambio ");
                stringBuilder.append(this.simboloMoneda);
                stringBuilder.append(String.format("%.2f", this.cantidadACuenta.multiply(new BigDecimal(-1))));
                textView.setText(stringBuilder.toString());
            }
        } else if (this.cantidadACuenta.compareTo(new BigDecimal(0)) > 0) {
            mostrarMetodosPago();
            textView = this.txtTituloPrecio;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Total ");
            stringBuilder.append(this.simboloMoneda);
            stringBuilder.append(String.format("%.2f", this.cantidadACuenta));
            textView.setText(stringBuilder.toString());
        }
        this.montoUtilizarPromocion = new BigDecimal(0);
    }

    public void mostrarCalculadoraPago(int idMetodoPago, boolean esPorCobrar,
                                       String codigoMetodoPago, String nombreTipoPago, BigDecimal CantidadTotalPago, boolean activaCamara) {

        byte permitir = 1;

        if (esPorCobrar == true && idCliente != 0) {
            permitir = 1;
        } else if (esPorCobrar == true && idCliente == 0) {
            permitir = 0;
            Toast.makeText(getActivity(), "Debe seleccionar un cliente", Toast.LENGTH_LONG).show();
        }

        if (permitir == 1) {

            if (!activaCamara) {
                DialogCalculadoraPago dialogCalculadoraPago = new DialogCalculadoraPago().newInstance(idMetodoPago,
                        context, CantidadTotalPago, codigoMetodoPago, nombreTipoPago);
                DialogFragment dialogFragment = dialogCalculadoraPago;
                dialogFragment.show(getFragmentManager(), "CalculadoraPago");
                dialogCalculadoraPago.setListenerCantidadPago(this);

            } else {
                final int idMP = idMetodoPago;
                final String codigo = codigoMetodoPago;
                final String nombreMedio = nombreTipoPago;
                DialogScannerCam scannerCam = new DialogScannerCam();
                scannerCam.setScannerResult(resultText -> PasarCantidadCancelada(idMP, new BigDecimal(resultText), codigo, nombreMedio));
                scannerCam.show(getFragmentManager(), "");
            }
        }
    }

    @Override
    public void PasarCantidadCancelada(int idMetodoPago, BigDecimal cantidadCancelada, String tipoPago, String nombreTipoPago) {
        pagosEnVentaList.clear();
        pagosEnVentaList.addAll(rvAdapterPagosEnVenta.getList());
        int longitud = pagosEnVentaList.size();
        byte encontro = 0;
        BigDecimal cantidadPagada = new BigDecimal(0);
        if (longitud > 0) {
            for (int i = 0; i < longitud; i++) {
                if (pagosEnVentaList.get(i).getIdTipoPago() == idMetodoPago) {

                    cantidadPagada = pagosEnVentaList.get(i).getCantidadPagada().add(cantidadCancelada);
                    encontro = 1;
                }
            }
        } else if (longitud == 0) {
            cantidadPagada = cantidadCancelada;
        }
        if (encontro == 0) {

            cantidadPagada = cantidadCancelada;
        }


        pagosEnVenta = new mPagosEnVenta(idMetodoPago, tipoPago, nombreTipoPago, cantidadPagada, false);
        new AgregarPagoTemporal().execute(pagosEnVenta);

    }

    public void ocultarMetodoPago() {
        btnFinalizarVenta.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }

    public void mostrarMetodosPago() {
        btnFinalizarVenta.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void numeroElementos(int cantidad, BigDecimal montoPagado) {

        if (cantidad == 0) {

            rvMetodosDePago.setVisibility(View.GONE);
            txtSinMetodoDePago.setVisibility(View.VISIBLE);
        } else if (cantidad > 0) {

            rvMetodosDePago.setVisibility(View.VISIBLE);
            txtSinMetodoDePago.setVisibility(View.GONE);
        }

        cantidadACuenta = cantidadTotalPago.subtract(montoPagado);

        if (cantidadACuenta.compareTo(new BigDecimal(0)) <= 0) {
            ocultarMetodoPago();
            if (cantidadACuenta.compareTo(new BigDecimal(0)) == 0) {
                txtTituloPrecio.setText("Pago Terminado");

            } else {
                txtTituloPrecio.setText("Cambio " + simboloMoneda + String.format("%.2f", cantidadACuenta.multiply(new BigDecimal(-1))));
            }
        } else if (cantidadACuenta.compareTo(new BigDecimal(0)) > 0) {

            mostrarMetodosPago();
            txtTituloPrecio.setText("Total " + simboloMoneda + String.format("%.2f", cantidadACuenta));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("ch-p",String.valueOf( position));
        Log.d("ch-p1",String.valueOf( id));
        if(position!=2){

            cbDetraccion.setChecked(false);
            cbDetraccion.setVisibility(View.GONE);
        }else{

            if(montoTotal.compareTo(new BigDecimal(700))>=0){
                cbDetraccion.setChecked(false);
                cbDetraccion.setVisibility(View.VISIBLE);
            }else{
                cbDetraccion.setChecked(false);
                cbDetraccion.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface ListenerVentaFinalizada {

        void GuardarPagos(BigDecimal CantidadCambio, int TipoDocPago,
                          boolean generaDoc, int idTipoAtencion, BigDecimal montoPromocion
                , String obs, BigDecimal montoDetraccion, BigDecimal porcentajeDetraccion,
                          String cuentaDetraccion, boolean usaDetraccion);

    }

    public class RealizarCobroTotal extends AsyncTask<mPagosEnVenta, Void, RetornoPagoTemporal> {
        Dialog dialog = progressDialog();

        public ProgressDialog progressDialog() {
            ProgressDialog progressDialog = new ProgressDialog(dialogCobroVenta.this.context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Procesando pago...");
            progressDialog.setProgressStyle(0);
            return progressDialog;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.show();
        }

        protected RetornoPagoTemporal doInBackground(mPagosEnVenta... mPagosEnVentas) {

            mPagosEnVenta pago = mPagosEnVentas[0];
            pago.setIdCabeceraPedido(dialogCobroVenta.this.idCabeceraPedido);
            PagoVentaTemp pagoVentaTemp = new PagoVentaTemp(pago.getIdTipoPago(), pago.getcTipoPago(), pago.getTipoPago(), pago.getCantidadPagada(), pago.isEsEfectivo(), pago.isActivaPagoExterno(), idCabeceraPedido);


            SolicitudEnvio<PagoVentaTemp> sol = new SolicitudEnvio<>(codeCia,
                    "2",
                    pagoVentaTemp,
                    Constantes.Terminal.idTerminal,
                    Constantes.Usuario.idUsuario);
            ProcessResult<Boolean> result;
            try {
                result = iPedidoRespository.GuardarPagoTemporal(sol).execute().body();
            } catch (IOException e) {
                result = new ProcessResult<>();
                result.setData(false);
                result.setCodeResult(0);
            }
            RetornoPagoTemporal retorno = new RetornoPagoTemporal();
            retorno.setRespuesta((byte) result.getCodeResult());
            retorno.setEsEfectivo(result.getData());
            return retorno;
        }

        protected void onPostExecute(RetornoPagoTemporal aByte) {
            super.onPostExecute(aByte);
            this.dialog.dismiss();
            if (aByte.getRespuesta() == (byte) 100) {
                dialogCobroVenta.this.rvAdapterPagosEnVenta.AddElement(
                        dialogCobroVenta.this.pagosEnVenta.getIdTipoPago()
                        , dialogCobroVenta.this.pagosEnVenta.getcTipoPago(),
                        dialogCobroVenta.this.pagosEnVenta.getTipoPago(),
                        dialogCobroVenta.this.pagosEnVenta.getCantidadPagada(),
                        aByte.isEsEfectivo());
                if (dialogCobroVenta.this.PermitirVenta()) {
                    dialogCobroVenta dialogcobroventa = dialogCobroVenta.this;
                    dialogcobroventa.RealizarPago(dialogcobroventa.rvAdapterPagosEnVenta.getList(), Constantes.TiposDocPago.listaTipoDocPago.get(dialogCobroVenta.this.spnTipoDocPago.getSelectedItemPosition()).getIdDoc(), Constantes.TiposDocPago.listaTipoDocPago.get(dialogCobroVenta.this.spnTipoDocPago.getSelectedItemPosition()).getGeneraDocPago());
                }
            } else if (aByte.getRespuesta() == (byte) 0) {
                Toast.makeText(dialogCobroVenta.this.context, "No se logro guardar el pago", Toast.LENGTH_LONG).show();
                Toast.makeText(dialogCobroVenta.this.context, "Verifique su conexion a internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetPromocion extends AsyncTask<Void, Void, Promocion> {
        protected Promocion doInBackground(Void... voids) {
            return BdConnectionSql.getSinglentonInstance().EstadoPromocionPedido();
        }

        protected void onPostExecute(Promocion promocion) {
            super.onPostExecute(promocion);
            dialogCobroVenta.this.promocionGeneral = promocion;
            if (promocion.getIdPromocion() == -99) {
                dialogCobroVenta.this.swPromocion.setChecked(false);
            } else if (promocion.getIdPromocion() > 0) {
                Switch switchR = dialogCobroVenta.this.swPromocion;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("PROMOCION : ACTIVADO ");
                stringBuilder.append(dialogCobroVenta.this.promocionGeneral.getDescripcion());
                switchR.setText(stringBuilder.toString());
                if (promocion.getTipoPromocion().equals("P")) {
                    dialogCobroVenta.this.montoUtilizarPromocion = promocion.getMontoDescuento().divide(new BigDecimal(100)).multiply(dialogCobroVenta.this.cantidadTotalPago);
                    if (promocion.getTieneLimite()) {
                        if (dialogCobroVenta.this.montoUtilizarPromocion.compareTo(promocion.getMontoLimite()) == 1) {
                            dialogCobroVenta.this.montoUtilizarPromocion = promocion.getMontoLimite();
                        }
                    }
                    dialogCobroVenta dialogcobroventa = dialogCobroVenta.this;
                    dialogcobroventa.cantidadTotalPago = dialogcobroventa.montoTotal.subtract(dialogCobroVenta.this.montoUtilizarPromocion);
                    dialogcobroventa = dialogCobroVenta.this;
                    dialogcobroventa.cantidadACuenta = dialogcobroventa.cantidadACuenta.subtract(dialogCobroVenta.this.montoUtilizarPromocion);
                    TextView textView;
                    StringBuilder stringBuilder2;
                    if (dialogCobroVenta.this.cantidadACuenta.compareTo(new BigDecimal(0)) <= 0) {
                        dialogCobroVenta.this.ocultarMetodoPago();
                        if (dialogCobroVenta.this.cantidadACuenta.compareTo(new BigDecimal(0)) == 0) {
                            dialogCobroVenta.this.txtTituloPrecio.setText("Pago Terminado");
                            return;
                        }
                        textView = dialogCobroVenta.this.txtTituloPrecio;
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Cambio ");
                        stringBuilder2.append(dialogCobroVenta.this.simboloMoneda);
                        stringBuilder2.append(String.format("%.2f", dialogCobroVenta.this.cantidadACuenta.multiply(new BigDecimal(-1))));
                        textView.setText(stringBuilder2.toString());
                    } else if (dialogCobroVenta.this.cantidadACuenta.compareTo(new BigDecimal(0)) > 0) {
                        dialogCobroVenta.this.mostrarMetodosPago();
                        textView = dialogCobroVenta.this.txtTituloPrecio;
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Total ");
                        stringBuilder2.append(dialogCobroVenta.this.simboloMoneda);
                        stringBuilder2.append(String.format("%.2f", dialogCobroVenta.this.cantidadACuenta));
                        textView.setText(stringBuilder2.toString());
                    }
                }
            }
        }
    }

    public class AgregarPagoTemporal extends AsyncTask<mPagosEnVenta, Void, RetornoPagoTemporal> {

        Dialog dialog = progressDialog();

        public ProgressDialog progressDialog() {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Procesando pago...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            return progressDialog;
        }

        String variable = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            dialog.show();

        }


        @Override
        protected RetornoPagoTemporal doInBackground(mPagosEnVenta... mPagosEnVentas) {
            mPagosEnVentas[0].setIdCabeceraPedido(idCabeceraPedido);
            mPagosEnVenta pago = mPagosEnVentas[0];
            pago.setIdCabeceraPedido(idCabeceraPedido);
            PagoVentaTemp pagoVentaTemp = new PagoVentaTemp(pago.getIdTipoPago(), pago.getcTipoPago(), pago.getTipoPago(), pago.getCantidadPagada(), pago.isEsEfectivo(), pago.isActivaPagoExterno(), idCabeceraPedido);


            SolicitudEnvio<PagoVentaTemp> sol = new SolicitudEnvio<>(codeCia,
                    "2",
                    pagoVentaTemp, Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            variable = new Gson().toJson(sol);
            ProcessResult<Boolean> result;
            try {
                result = iPedidoRespository.GuardarPagoTemporal(sol).execute().body();
            } catch (IOException e) {
                result = new ProcessResult<>();
                result.setData(false);
                result.setCodeResult(0);
            }
            RetornoPagoTemporal retorno = new RetornoPagoTemporal();
            retorno.setRespuesta((byte) result.getCodeResult());
            retorno.setEsEfectivo(result.getData());
            return retorno;
            //      return bdConnectionSql.GuardarPagoTemporal(idCabeceraPedido, mPagosEnVentas[0]);
        }

        @Override
        protected void onPostExecute(RetornoPagoTemporal aByte) {
            super.onPostExecute(aByte);
            dialog.dismiss();
            if (aByte.getRespuesta() == 100) {
                rvAdapterPagosEnVenta.AddElement(pagosEnVenta.getIdTipoPago(),
                        pagosEnVenta.getcTipoPago(), pagosEnVenta.getTipoPago(), pagosEnVenta.getCantidadPagada(), aByte.isEsEfectivo());

            } else if (aByte.getRespuesta() == 0) {
                Toast.makeText(context, "No se logro guardar el pago", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Verifique su conexion a internet", Toast.LENGTH_SHORT).show();

            }
        }
    }


    public class DownloadList extends AsyncTask<Void, Void, List<mPagosEnVenta>> {


        @Override
        protected List<mPagosEnVenta> doInBackground(Void... voids) {
            if (listMedioPago.size() == 0 || listMedioPago == null) {


                listMedioPago = new ControladorMediosPago().GetMediosPago();
            }
            SolicitudEnvio<Integer> solicitudEnvio = new SolicitudEnvio<>(codeCia, "2", idCabeceraPedido, Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
            List<mPagosEnVenta> list = null;
            try {
                Log.d("d_desc", "descarga");
                return iPedidoRespository.GetPagosTemporalesPedido(solicitudEnvio).execute().body();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<mPagosEnVenta> mMedioPagos) {
            super.onPostExecute(mMedioPagos);


            rvGridMPagos.AddElement(listMedioPago);
            if (mMedioPagos != null) {
                rvAdapterPagosEnVenta.AgregarMetodoPagoTemporal(mMedioPagos);
            }
        }
    }
}
