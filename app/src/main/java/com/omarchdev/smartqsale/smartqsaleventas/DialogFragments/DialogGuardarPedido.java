package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncClientes;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncZonaServicio;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorMediosPago;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorVentas;
import com.omarchdev.smartqsale.smartqsaleventas.Model.EntregaPedidoInfo;
import com.omarchdev.smartqsale.smartqsaleventas.Model.InfoAdicionalEntregaPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.InfoGuardadoPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.MedioPagoEntrega;
import com.omarchdev.smartqsale.smartqsaleventas.Model.TipoEntregaPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.TipoZonaServicio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mZonaServicio;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by OMAR CHH on 15/12/2017.
 */

public class DialogGuardarPedido extends DialogFragment implements View.OnClickListener, AsyncZonaServicio.ListenerZonasServicio, AsyncClientes.ObtenerDatoCliente {
    ImageButton btnScan;
    CapturaDato capturaDato;
    Dialog dialog;
    ImageButton btnGetDate;
    TextInputLayout edtNombreCliente;
    String identificador2;
    int idPedido;
    boolean identificadorCompuesto;
    String identificadorPedido;
    String observacion;
    String fechaEntrega;

    ControladorVentas controladorVentas;

    TextInputLayout txtIndentificador;
    TextInputLayout txtObservacion;
    TextInputLayout edtCelular;
    //////////Campos para entrega
    TextInputLayout edtNombreClienteDel;
    TextInputLayout edtCallaDel;
    TextInputLayout edtCiudad;
    TextInputLayout edtReferencia;
    TextInputLayout edtEmail;
    List<MedioPagoEntrega> listMedioPago = new ArrayList<>();
    //////////////////////////
    EditText edtFechaEntrega;
    AlertDialog.Builder builder;

    Button btnSalir, btnAceptar;
    Spinner spinner;
    TextView txtFEntrega;
    boolean visibleID2;
    LinearLayout contentEntregaDatos;
    mZonaServicio zonaServicio;
    Calendar fEntrega;
    int dia, mes, anio;
    DialogDatePickerSelect dateDialog;
    CheckBox cbTipoContrato;
    String cTipoPedido;
    AsyncZonaServicio asyncZonaServicio;
    AsyncClientes asyncClientes;
    boolean activaEntregaPedido = false;
    boolean buscandoEntrega = false;
    boolean reservaPedido = true;
    TipoZonaServicio tipoZonaServicio;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnGetDate:


                AbrirDialogFecha();

                break;

            case R.id.btnSalirPed:
                dismiss();
                break;
            case R.id.btnAceptarPed:
                ClickAceptar();
                break;
        }
    }

    public void AbrirDialogFecha() {

        dateDialog = new DialogDatePickerSelect();
        dateDialog.setOrigen((byte) 0, anio, mes, dia);
        dateDialog.show(this.getFragmentManager(), "PedidoReservaDate");
        dateDialog.setFechaListener(new DialogDatePickerSelect.interfaceFecha() {
            @Override
            public void getFechaSelecionada(int day, int month, int year, byte origen) {
                dia = day;
                mes = month;
                anio = year;
                fechaEntrega = TextoFecha(day, month, year);
                txtFEntrega.setText(fechaEntrega);
            }
        });

    }

    @Override
    public void ResultadosZonaServicio(@NonNull ArrayList<mZonaServicio> listaZonasServicio) {

    }

    @Override
    public void ResultadoGetTipoZonaServicio(@NonNull TipoZonaServicio tipoZonaServicio) {
        this.tipoZonaServicio = tipoZonaServicio;
        if (tipoZonaServicio.getIdTipoZonaServicio() == Constantes.TIPOZONASERVICIO.DELIVERY) {
            activaEntregaPedido = true;
            contentEntregaDatos.setVisibility(View.VISIBLE);
            edtNombreCliente.setVisibility(View.GONE);
            GetPedidosEntregaInfo(idPedido);
            if (reservaPedido == false) {
                txtIndentificador.setVisibility(View.GONE);
                builder.setTitle("Guardar datos del cliente");
            } else {
                txtIndentificador.setVisibility(View.VISIBLE);
                builder.setTitle("Guardar Pedido");
            }
        } else {
            activaEntregaPedido = false;
            contentEntregaDatos.setVisibility(View.GONE);
            edtNombreCliente.setVisibility(View.VISIBLE);
            builder.setTitle("Guardar Pedido");
        }

    }

    @Override
    public void ClienteObtenido(mCustomer customer) {
        if (customer.getiId() != 0) {
            //    edtCelular.getEditText().setText(customer.getcNumberPhone());
            edtNombreClienteDel.getEditText().setText(customer.getRazonSocial());
            edtEmail.getEditText().setText(customer.getcEmail());
            edtCallaDel.getEditText().setText(customer.getcDireccion());
            edtCiudad.getEditText().setText(customer.getCiudadLocalidad());
            edtReferencia.getEditText().setText(customer.getCReferencia());

        }
    }

    /* renamed from: com.omarchdev.smartqsale.smartqsaleventa.DialogFragments.DialogGuardarPedido$1 */
    class C05951 implements TextWatcher {
        C05951() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.omarchdev.smartqsale.smartqsaleventa.DialogFragments.DialogGuardarPedido$2 */
    class C05962 implements DialogInterface.OnClickListener {
        C05962() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public void ClickAceptar() {
        if (!DialogGuardarPedido.this.identificadorCompuesto && !activaEntregaPedido) {

            InfoGuardadoPedido info = new InfoGuardadoPedido(
                    DialogGuardarPedido.this.txtIndentificador.getEditText().getText().toString().trim(),
                    DialogGuardarPedido.this.txtObservacion.getEditText().getText().toString(),
                    DialogGuardarPedido.this.edtNombreCliente.getEditText().getText().toString()
                    , fechaEntrega, cbTipoContrato.isChecked());
            info.setEntregaPedidoInfo(new EntregaPedidoInfo());
            DialogGuardarPedido.this.capturaDato.ObtenerDatoPedido(info
            );
            dismiss();
        } else if (!DialogGuardarPedido.this.zonaServicio.getDescripcion().trim().isEmpty() && !activaEntregaPedido) {
            CapturaDato capturaDato = DialogGuardarPedido.this.capturaDato;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(DialogGuardarPedido.this.zonaServicio.getDescripcion().trim());
            stringBuilder.append("-");
            stringBuilder.append(DialogGuardarPedido.this.edtNombreCliente.getEditText().getText().toString());
            capturaDato.ObtenerDatoPedido(new InfoGuardadoPedido(stringBuilder.toString(),
                    DialogGuardarPedido.this.txtObservacion.getEditText().getText().toString(),
                    DialogGuardarPedido.this.edtNombreCliente.getEditText().getText().toString(),
                    fechaEntrega, cbTipoContrato.isChecked()));

            dismiss();
        } else if (!DialogGuardarPedido.this.zonaServicio.getDescripcion().trim().isEmpty() && activaEntregaPedido) {
            CapturaDato capturaDato = DialogGuardarPedido.this.capturaDato;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(DialogGuardarPedido.this.zonaServicio.getDescripcion().trim());
            stringBuilder.append("-");
            stringBuilder.append(DialogGuardarPedido.this.edtNombreCliente.getEditText().getText().toString());
            InfoGuardadoPedido info = new InfoGuardadoPedido(stringBuilder.toString(),
                    DialogGuardarPedido.this.txtObservacion.getEditText().getText().toString(),
                    DialogGuardarPedido.this.edtNombreCliente.getEditText().getText().toString(),
                    fechaEntrega, cbTipoContrato.isChecked());
            info.setReservaPedido(this.reservaPedido);
            EntregaPedidoInfo entrega = new EntregaPedidoInfo();
            info.setEntregaPedidoInfo(entrega);
            info.setUsaEntregaPedido(activaEntregaPedido);
            mCustomer cliente = new mCustomer();
            cliente.setcNumberPhone(edtCelular.getEditText().getText().toString());
            cliente.setRazonSocial(edtNombreClienteDel.getEditText().getText().toString());
            cliente.setcDireccion(edtCallaDel.getEditText().getText().toString());
            cliente.setcEmail(edtEmail.getEditText().getText().toString());
            InfoAdicionalEntregaPedido infoAdicional = new InfoAdicionalEntregaPedido();
            TipoEntregaPedido tipoEntregaPedido = new TipoEntregaPedido();
            tipoEntregaPedido.setCalleNumero(edtCallaDel.getEditText().getText().toString());
            tipoEntregaPedido.setCiudadLocalidad(edtCiudad.getEditText().getText().toString());
            tipoEntregaPedido.setReferencia(edtReferencia.getEditText().getText().toString());
            entrega.setTipoEntregaPedido(tipoEntregaPedido);
            entrega.setInfoAdicionalEntregaPedido(new InfoAdicionalEntregaPedido());
            entrega.setClienteEntrega(cliente);
            entrega.setMedioPagoEntrega(new MedioPagoEntrega());

            entrega.getMedioPagoEntrega().setIid_Metodo_Pago_Entrega(listMedioPago.get(spinner.getSelectedItemPosition()).getIid_Metodo_Pago_Entrega());
            boolean permiteGuardar = true;
            edtCelular.setError(null);
            edtCallaDel.setError(null);
            edtNombreClienteDel.setError(null);
            if (edtCelular.getEditText().getText().toString().trim().isEmpty()) {
                permiteGuardar = false;
                edtCelular.setError("Campo obligatorio");
            }
            if (edtCallaDel.getEditText().getText().toString().trim().isEmpty()) {
                permiteGuardar = false;
                edtCallaDel.setError("Campo obligatorio");
            }
            if (edtNombreClienteDel.getEditText().getText().toString().trim().isEmpty()) {
                permiteGuardar = false;
                edtNombreClienteDel.setError("Campo obligatorio");
            }
            if (permiteGuardar) {
                capturaDato.ObtenerDatoPedido(info);
                dismiss();
            } else {
                new AlertDialog.Builder(getActivity()).setTitle("Advertencia").setMessage("Debe completar todos la campos obligatorios").setNegativeButton("Salir", null).create().show();
            }
        }
    }

    /* renamed from: com.omarchdev.smartqsale.smartqsaleventa.DialogFragments.DialogGuardarPedido$3 */
    class C05973 implements DialogInterface.OnClickListener {
        C05973() {
        }

        public void onClick(DialogInterface dialog, int which) {


        }
    }

    /* renamed from: com.omarchdev.smartqsale.smartqsaleventa.DialogFragments.DialogGuardarPedido$4 */
    class C05984 implements View.OnClickListener {
        C05984() {
        }

        public void onClick(View v) {
            DialogGuardarPedido.this.Scan();
        }
    }

    public interface CapturaDato {
        void ObtenerDatoPedido(InfoGuardadoPedido info);
    }

    /* renamed from: com.omarchdev.smartqsale.smartqsaleventa.DialogFragments.DialogGuardarPedido$5 */
    class C09375 implements DialogScannerCam.ScannerResult {
        C09375() {
        }

        public void ResultadoScanner(String resultText) {
            try {
                DialogGuardarPedido.this.txtIndentificador.getEditText().setText(resultText);
            } catch (Exception e) {
                Toast.makeText(DialogGuardarPedido.this.getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public DialogGuardarPedido newInstance() {
        return new DialogGuardarPedido();
    }

    public DialogGuardarPedido setListenerCapturarDato(CapturaDato capturaDato) {
        this.capturaDato = capturaDato;
        return this;
    }

    public DialogGuardarPedido setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
        return this;

    }

    public DialogGuardarPedido setIdPedido(int idPedido) {
        this.idPedido = idPedido;
        return this;
    }

    public DialogGuardarPedido setNombreIdentificador(String identificadorPedido) {
        this.identificadorPedido = identificadorPedido;
        return this;
    }

    public DialogGuardarPedido setTipoPedido(String cTipoPedido) {
        this.cTipoPedido = cTipoPedido;
        return this;
    }

    public DialogGuardarPedido setReservaPedido(boolean reservaPedido) {
        this.reservaPedido = reservaPedido;
        return this;
    }

    public DialogGuardarPedido setObservacionPedido(String observacion) {
        this.observacion = observacion;
        return this;
    }

    public DialogGuardarPedido setIdentificador2(String identificador2) {
        this.identificador2 = identificador2;
        return this;
    }

    public DialogGuardarPedido setIdentificador2Visible(boolean visibleID2) {
        this.visibleID2 = visibleID2;
        this.identificadorCompuesto = visibleID2;
        return this;
    }


    public void setListenerCapturaDato(CapturaDato capturaDato) {
        this.capturaDato = capturaDato;
    }

    public DialogGuardarPedido setZonaServicio(mZonaServicio zonaServicio) {
        this.zonaServicio = zonaServicio;
        return this;
    }

    public DialogGuardarPedido setIdentificadorCompuesto(boolean identificadorCompuesto) {
        this.identificadorCompuesto = identificadorCompuesto;
        return this;
    }

    public void setIdentificadorPedido(String identificadorPedido) {
        this.identificadorPedido = identificadorPedido;
    }


    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    private void fechaDefecto() {

        dia = Integer.valueOf(fechaEntrega.substring(0, 2));
        mes = Integer.valueOf(fechaEntrega.substring(3, 5));
        anio = Integer.valueOf(fechaEntrega.substring(6, 10));
        txtFEntrega.setText(fechaEntrega);


    }

    private String TextoFecha(int dia, int mes, int anio) {

        return String.valueOf(dia) + "-" + String.valueOf(mes) + "-" + String.valueOf(anio);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_salvar_perdido, null);
        try {
            activaEntregaPedido = false;
            cbTipoContrato = v.findViewById(R.id.cbTipoContrato);
            fEntrega = Calendar.getInstance();
            this.txtIndentificador = (TextInputLayout) v.findViewById(R.id.InLIndentificadorPedido);
            this.txtObservacion = (TextInputLayout) v.findViewById(R.id.InLObservacionPedido);
            this.edtNombreCliente = (TextInputLayout) v.findViewById(R.id.edtNombreCliente);
            this.contentEntregaDatos = (LinearLayout) v.findViewById(R.id.linearContentEntregaDatos);
            this.spinner = v.findViewById(R.id.spnMediosPagoDel);
            this.edtNombreClienteDel = v.findViewById(R.id.edtNombreClienteDel);
            this.edtEmail = v.findViewById(R.id.edtEmail);
            this.edtReferencia = v.findViewById(R.id.edtReferencia);
            this.edtCiudad = v.findViewById(R.id.edtCiudad);
            this.asyncClientes = new AsyncClientes();
            this.btnSalir = v.findViewById(R.id.btnSalirPed);
            this.btnAceptar = v.findViewById(R.id.btnAceptarPed);
            this.edtCelular = v.findViewById(R.id.edtCelular);
            this.edtNombreCliente.setVisibility(View.GONE);
            this.edtCallaDel = v.findViewById(R.id.edtCallaDel);
            this.btnGetDate = v.findViewById(R.id.btnGetDate);
            btnGetDate.setOnClickListener(this);
            this.txtFEntrega = v.findViewById(R.id.txtFEntrega);
            asyncClientes.setObtenerDatoCliente(this);
            this.btnSalir.setOnClickListener(this);
            this.btnAceptar.setOnClickListener(this);
            this.edtCelular.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() == 9 && !buscandoEntrega) {
                        asyncClientes.BuscarClientePorTelefono(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            contentEntregaDatos.setVisibility(View.GONE);
            if (this.visibleID2) {
                this.edtNombreCliente.setVisibility(View.VISIBLE);
                this.edtNombreCliente.getEditText().setText(this.identificador2);
                this.edtNombreCliente.getEditText().addTextChangedListener(new C05951());
            } else {
                this.edtNombreCliente.setVisibility(View.GONE);
                this.edtNombreCliente.getEditText().setText("");
            }
            if (Constantes.Tienda.ZonasAtencion) {
                this.txtIndentificador.getEditText().setInputType(View.VISIBLE);
                this.txtIndentificador.getEditText().setTextIsSelectable(false);
                this.txtIndentificador.getEditText().setClickable(false);
                this.txtIndentificador.getEditText().setFocusable(false);
            }
            if (!Constantes.ConfigTienda.bUsaFechaEntrega) {
                txtFEntrega.setVisibility(View.GONE);
                btnGetDate.setVisibility(View.GONE);
            }
            if (zonaServicio.getIdZona() != 0) {
                asyncZonaServicio = new AsyncZonaServicio();
                asyncZonaServicio.ObtenerTipoZonaServicio(zonaServicio.getIdZona());
                asyncZonaServicio.setListenerZonasServicio(this);
            }
            this.btnScan = (ImageButton) v.findViewById(R.id.btnScanCode);
            if (reservaPedido == false) {
                txtIndentificador.setVisibility(View.GONE);
                builder.setView(v).setTitle("Guardar datos del cliente");
            } else {
                txtIndentificador.setVisibility(View.VISIBLE);
                builder.setView(v).setTitle("Guardar Pedido");
            }
            builder.setView(v);
            //  builder.setView(v).setPositiveButton("Guardar", new C05973()).setNegativeButton("Cancelar", new C05962()).setTitle("Guardar Pedido");
            this.txtIndentificador.getEditText().setText(this.identificadorPedido);
            this.txtObservacion.getEditText().setText(this.observacion);
            this.dialog = builder.create();
            this.dialog.setCanceledOnTouchOutside(false);
            this.btnScan.setOnClickListener(new C05984());
            controladorVentas = new ControladorVentas();
            if (cTipoPedido.trim().equals("02")) {
                this.cbTipoContrato.setChecked(true);
            } else {
                this.cbTipoContrato.setChecked(false);
            }
            fechaDefecto();
            if (tipoZonaServicio != null) {
                if (tipoZonaServicio.getIdTipoZonaServicio() == Constantes.TIPOZONASERVICIO.DELIVERY) {
                    if (!reservaPedido) {
                        builder.setTitle("Guardar datos del cliente");
                    } else {
                        builder.setTitle("Guardar Pedido");
                    }
                } else {
                    builder.setTitle("Guardar Pedido");
                }
            }

        } catch (Exception e) {

            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }

        return this.dialog;
    }

    public void GetPedidosEntregaInfo(int idPedido) {
        new GetPedidoDatosEntrega().execute(idPedido);
    }


    private class GetPedidoDatosEntrega extends AsyncTask<Integer, Void, EntregaPedidoInfo> {

        ControladorMediosPago controladorMediosPago;
        DialogCargaAsync dialogCargaAsync;
        Dialog dialog1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogCargaAsync = new DialogCargaAsync(getActivity());
            dialog1 = dialogCargaAsync.getDialogCarga("Obteniendo pedido...");
            dialog1.show();
            controladorMediosPago = new ControladorMediosPago();
        }

        @Override
        protected EntregaPedidoInfo doInBackground(Integer... integers) {
            listMedioPago = controladorMediosPago.GetMetodosPagosCompanyDelivery();
            return controladorVentas.GetEntregaPedidoInfo(integers[0]);
        }

        @Override
        protected void onPostExecute(EntregaPedidoInfo entregaPedidoInfo) {
            super.onPostExecute(entregaPedidoInfo);
            int pos = 0;
            spinner.setAdapter(new ArrayAdapter<MedioPagoEntrega>(getActivity(), android.R.layout.simple_dropdown_item_1line, listMedioPago));

            if (entregaPedidoInfo.getTipoEntregaPedido() != null) {
                AsignarCamposEntregaPedido(entregaPedidoInfo);

                for (int i = 0; i < listMedioPago.size(); i++) {
                    if (listMedioPago.get(i).getIid_Metodo_Pago_Entrega() == entregaPedidoInfo.getMedioPagoEntrega().getIid_Metodo_Pago_Entrega()) {
                        pos = i;
                    }
                }
            }
            spinner.setSelection(pos);
            dialog1.dismiss();
        }
    }


    public void AsignarCamposEntregaPedido(EntregaPedidoInfo entregaPedidoInfo) {
        buscandoEntrega = true;
        edtCallaDel.getEditText().setText(entregaPedidoInfo.getTipoEntregaPedido().getCalleNumero());
        edtCiudad.getEditText().setText(entregaPedidoInfo.getTipoEntregaPedido().getCiudadLocalidad());
        edtReferencia.getEditText().setText(entregaPedidoInfo.getTipoEntregaPedido().getReferencia());
        edtNombreClienteDel.getEditText().setText(entregaPedidoInfo.getClienteEntrega().getRazonSocial());
        edtCelular.getEditText().setText(entregaPedidoInfo.getClienteEntrega().getcNumberPhone());
        edtEmail.getEditText().setText(entregaPedidoInfo.getClienteEntrega().getCemail2());
        buscandoEntrega = false;
    }


    public void Scan() {
        DialogScannerCam dialogScannerCam = new DialogScannerCam();
        dialogScannerCam.setScannerResult(new C09375());
        dialogScannerCam.show(getActivity().getFragmentManager(), "hOLA");
    }
}
