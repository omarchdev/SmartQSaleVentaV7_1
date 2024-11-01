package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncClientes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorCliente;
import com.omarchdev.smartqsale.smartqsaleventas.InterfaceDataCustomerProvider;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.ValidarExistenciaDialogAlert;
import com.github.clans.fab.FloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 26/11/2017.
 */

public class DialogAddEditCustomer extends DialogFragment implements View.OnClickListener {

    Context context;
    mCustomer cliente;
    ListenerAddCustomer listenerAddCustomer;

    InterfaceDataCustomerProvider interfaceDataCustomerProvider;
    ValidarExistenciaDialogAlert validarExistenciaDialogAlert;
    DialogCargaAsync dialogCargaAsync;
    ControladorCliente controladorCliente;
    Dialog dialog;

    FloatingActionMenu actionMenu;

    List<String> listTipoCliente;
    Spinner spinner;
    TextInputLayout tilNombre;
    TextInputLayout tilApellidoPaterno;
    TextInputLayout tilApellidoMaterno;
    TextInputLayout tilNumeroTelefono;
    TextInputLayout tilDireccion;
    TextInputLayout tilEmail,tilRazonSocial,tilNumeroRuc;
    Button btnGuardar,btnCancelar;

    int idCliente;


    AVLoadingIndicatorView avi;
    TextView txtCargando;
    AsyncClientes asyncClientes;
    RelativeLayout rlContent;
    int TipoCliente;



    byte metodo;

    public DialogAddEditCustomer newInstance(){

        DialogAddEditCustomer d=new DialogAddEditCustomer();

        return d;
    }

    public DialogAddEditCustomer() {

        this.context=context;

        metodo=0;
    }


    public void setInfo(Context context,mCustomer customer,byte metodo){

        this.context=context;
        this.cliente=customer;
        this.metodo=metodo;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View v = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_registro_cliente, null);
            cliente = new mCustomer();
            listTipoCliente = new ArrayList<>();
            listTipoCliente.add(context.getResources().getInteger(R.integer.PositionPNatural), context.getResources().getString(R.string.TituloPersonaNatural));
            listTipoCliente.add(context.getResources().getInteger(R.integer.PositionPJuridica), context.getResources().getString(R.string.TituloPersonaJuridica));
            avi = v.findViewById(R.id.avi);
            txtCargando = v.findViewById(R.id.txtCargando);
            actionMenu = v.findViewById(R.id.actionMenu);
            spinner = v.findViewById(R.id.spnTipoCliente);

            btnCancelar=v.findViewById(R.id.btnCancelar);
            btnGuardar=v.findViewById(R.id.btnGuardar);
            idCliente = 0;
            dialogCargaAsync = new DialogCargaAsync(context);
            controladorCliente = new ControladorCliente();
            tilNombre = v.findViewById(R.id.tilNombreCliente);
            tilApellidoPaterno = v.findViewById(R.id.tilApellidoPaternoCliente);
            tilApellidoMaterno = v.findViewById(R.id.tilApellidoMaternoCLiente);
            tilNumeroTelefono = v.findViewById(R.id.tilNumeroTelefonoCustomer);
            tilRazonSocial = v.findViewById(R.id.tilRazonSocial);
            tilNumeroRuc = v.findViewById(R.id.tilNumeroRuc);
            tilDireccion = v.findViewById(R.id.tilDireccionCliente);
            tilEmail = v.findViewById(R.id.tilEmailCliente);
            asyncClientes=new AsyncClientes();


            txtCargando.setVisibility(View.INVISIBLE);
            btnCancelar.setOnClickListener(this);
            btnGuardar.setOnClickListener(this);
            avi.hide();

            actionMenu.setVisibility(View.GONE);
            builder.setView(v);
            builder.setTitle("Agregar Cliente");

            spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, listTipoCliente));
            spinner.setSelection(0);

            InterfazPersonaNatural();
            asyncClientes.setContext(context);
            spinner.setOnItemSelectedListener(selectionListener);

            dialog = builder.create();


            dialog.setCanceledOnTouchOutside(false);

        }catch (Exception e){
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
        }
        return dialog;
    }

    public void setListenerCustomer(InterfaceDataCustomerProvider interfaceDataCustomerProvider){
        this.interfaceDataCustomerProvider = interfaceDataCustomerProvider;
    }
    public void GuardarVendedor(){

        if(VerificarDatos()) {
            cliente = new mCustomer();
            if(spinner.getSelectedItemPosition()==0) {
                cliente.setiId(idCliente);
                cliente.setcName(tilNombre.getEditText().getText().toString());
                cliente.setcApellidoPaterno(tilApellidoPaterno.getEditText().getText().toString());
                cliente.setcApellidoMaterno(tilApellidoMaterno.getEditText().getText().toString());
                cliente.setcNumberPhone(tilNumeroTelefono.getEditText().getText().toString());
                cliente.setcEmail(tilEmail.getEditText().getText().toString());
                cliente.setcDireccion(tilDireccion.getEditText().getText().toString());
                cliente.setTipoCliente(this.getResources().getInteger(R.integer.ValorPersonaNatural));
                asyncClientes.GuardarCliente(cliente);
            }
            else if(spinner.getSelectedItemPosition()==1){
                cliente.setiId(idCliente);
                cliente.setRazonSocial(tilRazonSocial.getEditText().getText().toString());
                cliente.setNumeroRuc(tilNumeroRuc.getEditText().getText().toString());
                cliente.setcNumberPhone(tilNumeroTelefono.getEditText().getText().toString());
                cliente.setcEmail(tilEmail.getEditText().getText().toString());
                cliente.setcDireccion(tilDireccion.getEditText().getText().toString());
                cliente.setTipoCliente(this.getResources().getInteger(R.integer.ValorPersonaJuridica));
                asyncClientes.GuardarCliente(cliente);
            }

            asyncClientes.setRegistroClientes(new AsyncClientes.RegistroClientes() {
                @Override
                public void ErrorConnection() {
                    actionMenu.close(true);
                    MensajeAlerta("Error","Error al registrar al cliente.Verifique su conexión a internet");
                }

                @Override
                public void ErrorRegistro() {

                    actionMenu.close(true);
                    MensajeAlerta("Error","Error al registrar al cliente. Codigo 00099");
                }

                @Override
                public void ActualizarExito() {

                    actionMenu.close(true);

                    MensajeAlerta("Confirmacion","El cliente se actualizo con éxito");
                }

                @Override
                public void RegistrarExito() {

                    actionMenu.close(true);


                    MensajeAlerta("Confirmacion","El cliente se registró con éxito");
                    dialog.dismiss();
                }

                @Override
                public void ExisteCliente() {

                }
            });

        }

    }

    public boolean VerificarDatos(){

        boolean permitir=true;
        if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaNatural)) {
            if (tilNombre.getEditText().getText().toString().equals("")) {
                tilNombre.setError("Este campo es obligatorio");
                permitir = false;

            }
        }else if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaJuridica)){
            if (tilRazonSocial.getEditText().getText().toString().equals("")) {
                tilRazonSocial.setError("Este campo es obligatorio");
                permitir = false;

            }
        }
        if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaNatural)) {
            if (tilApellidoPaterno.getEditText().getText().toString().equals("")) {

                tilApellidoPaterno.setError("Este campo es obligatorio");
                permitir = false;
            }
        }else if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaJuridica)){
            if (tilNumeroRuc.getEditText().getText().toString().equals("")) {

                tilNumeroRuc.setError("Este campo es obligatorio");
                permitir = false;
            }
        }
        if(TipoCliente==this.getResources().getInteger(R.integer.ValorPersonaNatural)) {
            if (tilApellidoMaterno.getEditText().getText().toString().equals("")) {
                tilApellidoMaterno.setError("Este campo es obligatorio");
                permitir = false;
            }
        }




        return permitir;
    }

    public void MensajeAlerta(String titulo,String mensaje){

        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje).setTitle(titulo).setPositiveButton("Salir",null).create().show();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGuardar) {

            GuardarVendedor();

        } else if (v.getId() == R.id.btnCancelar) {
            dialog.dismiss();
        }

    }

    public void setListenerAddCustomer(ListenerAddCustomer listenerAddCustomer) {
        this.listenerAddCustomer = listenerAddCustomer;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        if(listenerAddCustomer!=null)
        listenerAddCustomer.actualizar();
    }

    public interface ListenerAddCustomer {

        public void actualizar();
    }


    AdapterView.OnItemSelectedListener selectionListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position==context.getResources().getInteger(R.integer.PositionPNatural)){
                TipoCliente=context.getResources().getInteger(R.integer.ValorPersonaNatural);
                InterfazPersonaNatural();
            }else if(position==context.getResources().getInteger(R.integer.PositionPJuridica)){
                TipoCliente=context.getResources().getInteger(R.integer.ValorPersonaJuridica);

                InterfazPersonaJuridica();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private void InterfazPersonaNatural(){
        TipoCliente=this.getResources().getInteger(R.integer.ValorPersonaNatural);
        tilNumeroRuc.setVisibility(View.GONE);
        tilRazonSocial.setVisibility(View.GONE);


        tilNombre.setVisibility(View.VISIBLE);
        tilApellidoPaterno.setVisibility(View.VISIBLE);

        tilApellidoMaterno.setVisibility(View.VISIBLE);




    }

    private void InterfazPersonaJuridica(){
        TipoCliente=this.getResources().getInteger(R.integer.ValorPersonaJuridica);
        tilRazonSocial.setVisibility(View.VISIBLE);
        tilNumeroRuc.setVisibility(View.VISIBLE);


        tilNombre.setVisibility(View.GONE);
        tilApellidoPaterno.setVisibility(View.GONE);

        tilApellidoMaterno.setVisibility(View.GONE);



    }

}
