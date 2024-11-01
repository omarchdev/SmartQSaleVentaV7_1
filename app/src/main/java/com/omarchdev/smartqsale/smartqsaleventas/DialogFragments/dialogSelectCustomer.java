package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Spinner;

import com.omarchdev.smartqsale.smartqsaleventas.ClickListener;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorCliente;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Control1Cliente;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Control2Cliente;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterClientes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class dialogSelectCustomer extends DialogFragment implements DialogAddEditCustomer.ListenerAddCustomer, View.OnClickListener, TextWatcher, RvAdapterClientes.SelectClienteListener, ClickListener, AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {
    ImageButton imgArrowBack;
    Dialog dialog;
    Context context;
    EditText edtBusquedaCliente;
    Button btnAgregar;
    Button btnEliminarCliente;
    ListView listView;
    RecyclerView rv;
   RvAdapterClientes adapterClientes;
    ControladorCliente controladorCliente;
    DatosCliente datosCliente;
    DialogAddEditCustomer dialogAddEditCustomer;
    DFmRegistroCliente registroCliente;
    List<mCustomer> listaClientes=new ArrayList<>();

    SearchView svCliente;
    LinearLayout contentBusquedaAvanzada;
    Spinner spnControl1;
    Spinner spnControl2;
    ArrayAdapter adapSpinner1Cliente;
    ArrayAdapter adapSpinner2Cliente;
    String Control1;
    String Control2;
    List<String> lista1;
    List<String> lista2;

    @Override
    public void onStart() {

        super.onStart();
        /*if(getDialog()!=null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }*/
    }

    public void setContext(Context context){
        this.context=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;

        View v=((Activity)context).getLayoutInflater().inflate(R.layout.busqueda_cliente_venta,null);
        try {

            context=getActivity();
            builder = new AlertDialog.Builder(getActivity());
            controladorCliente = new ControladorCliente();
            dialogAddEditCustomer = new DialogAddEditCustomer();
            contentBusquedaAvanzada=v.findViewById(R.id.contentBusquedaAvanzada);
            edtBusquedaCliente = (EditText) v.findViewById(R.id.edtBusquedaCliente);

            svCliente=v.findViewById(R.id.svCliente);
            svCliente.setQueryHint("Busqueda de cliente");
            svCliente.setQuery("", false);
            svCliente.onActionViewExpanded();
            svCliente.setOnQueryTextListener(this);

            edtBusquedaCliente.addTextChangedListener(this);
            rv = (RecyclerView) v.findViewById(R.id.rvClientesParaVenta);
            imgArrowBack = (ImageButton) v.findViewById(R.id.imgArrowBack);
            btnAgregar = (Button) v.findViewById(R.id.btnAgregarCliente);
            adapterClientes=new RvAdapterClientes();
            rv.setAdapter(adapterClientes);
            btnEliminarCliente=v.findViewById(R.id.btnEliminarCliente);

            adapterClientes.setSelectClienteListener(this);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(
                    context,context.getResources().getInteger(R.integer.NumColumnsClienteSelect));
            rv.setLayoutManager(gridLayoutManager);
            adapterClientes.setClickListener(this);
            btnAgregar.setOnClickListener(this);
            dialogAddEditCustomer.setListenerAddCustomer(this);
            imgArrowBack.setOnClickListener(this);
            builder.setView(v);
            adapterClientes.setVisibilitySettings(false);
            btnEliminarCliente.setOnClickListener(this);
            adapterClientes.setVisibilitySettings(false);

            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


            this.spnControl1 = (Spinner) v.findViewById(R.id.spnControl1);
            this.spnControl2 = (Spinner) v.findViewById(R.id.spnControl2);
            this.Control1 = "";
            this.Control2 = "";
            if (Constantes.ConfigTienda.visibleBusquedaAvanzadaCliente) {
                this.contentBusquedaAvanzada.setVisibility(View.VISIBLE);
                this.lista1 = new ArrayList();
                this.lista2 = new ArrayList();
                for (Control1Cliente control1Cliente : Constantes.ControlCliente.control1Clientes) {
                    this.lista1.add(control1Cliente.getDescripcionControl());
                }
                this.adapSpinner1Cliente = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, this.lista1);
                this.spnControl1.setAdapter(this.adapSpinner1Cliente);
                this.adapSpinner1Cliente.notifyDataSetChanged();
                Iterator it = ((Control1Cliente) Constantes.ControlCliente.control1Clientes.get(0)).getListaControl2().iterator();
                while (it.hasNext()) {
                    this.lista2.add(((Control2Cliente) it.next()).getDescripcicionControl2Cliente());
                }
                this.adapSpinner2Cliente = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, this.lista2);
                this.spnControl2.setAdapter(this.adapSpinner2Cliente);
                this.adapSpinner2Cliente.notifyDataSetChanged();
                this.spnControl1.setOnItemSelectedListener(this);
                this.spnControl2.setOnItemSelectedListener(new C06141());
            } else {
                this.contentBusquedaAvanzada.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.toString();
            builder=null;
         }

        new DownloadListClientes().execute("");
        return dialog;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        new DownloadListClientes().execute(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if(s.isEmpty()) {
            new DownloadListClientes().execute("");
        }
        return false;
    }

    class C06141 implements AdapterView.OnItemSelectedListener {
        C06141() {
        }
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            if (position == 0) {
                dialogSelectCustomer.this.Control2 = "";
                return;
            }
            dialogSelectCustomer dialogselectcustomer = dialogSelectCustomer.this;
            dialogselectcustomer.Control2 = (String) dialogselectcustomer.lista2.get(position);
            new DownloadListClientes().execute(new String[]{dialogSelectCustomer.this.edtBusquedaCliente.getText().toString()});
        }
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void setListenerCliente(DatosCliente datosCliente) {
        this.datosCliente = datosCliente;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAgregarCliente) {
            mostrarDialogAgregar();
        } else if (v.getId() == R.id.imgArrowBack) {
            dialog.dismiss();
        }
        else if(v.getId()==R.id.btnEliminarCliente){
            datosCliente.obtenerDato(new mCustomer());
            dialog.dismiss();
        }
    }
    public void mostrarDialogAgregar() {
        registroCliente=new DFmRegistroCliente().newInstance(0);
        registroCliente.setListenerAddEditCustomer(this);
        DialogFragment df=registroCliente;
        df.show( getFragmentManager(), "Agregar Cliente");
    }

    @Override
    public void actualizar() {
        new DownloadListClientes().execute("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        new DownloadListClientes().execute(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    @Override
    public void ClienteSeleccionado(mCustomer cliente) {
        mCustomer customer=new mCustomer();
        if(cliente.getTipoCliente()==context.getResources().getInteger(R.integer.ValorPersonaNatural)) {
            customer.setcName(cliente.getcName());
        }
        else if(cliente.getTipoCliente()==context.getResources().getInteger(R.integer.ValorPersonaJuridica)){
            customer.setcName(cliente.getRazonSocial());
        }
        customer.setcEmail(cliente.getcEmail());
        customer.setiId(cliente.getiId());
        customer.setIdTipoDocSunat(cliente.getIdTipoDocSunat());
        customer.setIdTipoDocumento(cliente.getIdTipoDocumento());
        customer.setcDireccion(cliente.getcDireccion());
        customer.setNumeroRuc(cliente.getNumeroRuc());
        customer.setControl1(cliente.getControl1());
        datosCliente.obtenerDato(customer);
        dialog.dismiss();

    }

    @Override
    public void clickPositionOption(int position, byte accion) {

        registroCliente=new DFmRegistroCliente().newInstance(listaClientes.get(position).getiId());
        registroCliente.setListenerAddEditCustomer(this);
        DialogFragment df=registroCliente;
        df.show( getFragmentManager(), "Agregar Cliente");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.adapSpinner2Cliente.clear();

        Iterator it = ((Control1Cliente) Constantes.ControlCliente.control1Clientes.get(position)).getListaControl2().iterator();
        while (it.hasNext()) {
            this.adapSpinner2Cliente.add(((Control2Cliente) it.next()).getDescripcicionControl2Cliente());
        }
        this.adapSpinner2Cliente.notifyDataSetChanged();
        this.spnControl2.setSelection(0);
        if (position != 0) {
            this.Control1 = (String) this.lista1.get(position);
            this.Control2 = "";
        } else {
            this.Control1 = "";
            this.Control2 = "";
        }
        new DownloadListClientes().execute(new String[]{this.edtBusquedaCliente.getText().toString()});

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public interface DatosCliente {
        public void obtenerDato(mCustomer customer);
    }

    private class DownloadListClientes extends AsyncTask<String, Void, List<mCustomer>> {

        @Override
        protected List<mCustomer> doInBackground(String... strings) {

            return controladorCliente.getClienteNombreApellido(strings[0],dialogSelectCustomer.this.Control1, dialogSelectCustomer.this.Control2);
        }
        @Override
        protected void onPostExecute(List<mCustomer> mCustomers) {
            super.onPostExecute(mCustomers);
            adapterClientes.AgregarListado(mCustomers);
            listaClientes.clear();
            listaClientes.addAll(mCustomers);
        }

    }


}
