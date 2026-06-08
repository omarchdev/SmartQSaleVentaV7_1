package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVendedor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 01/12/2017.
 */

public class dialogSelectVendedor extends DialogFragment implements View.OnClickListener, SearchView.OnQueryTextListener, RvAdapterVendedor.Vendedor {

    Dialog dialog;
    SearchView svVendedor;
    RecyclerView rv;
    Button btnEliminarVendedor;
    RvAdapterVendedor rvAdapterVendedor;
    ControladorVendedor controladorVendedor;
    static InformacionVendedor informacionVendedor;
    ImageButton imgArrowBack;
    Button btnAñadirVendedor;


    public void setListenerVendedor(InformacionVendedor informacionVendedor) {
        this.informacionVendedor = informacionVendedor;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()!=null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new DownloadListVendedor().execute("");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getLayoutInflater().inflate(R.layout.busqueda_vendedores_venta, null);
        controladorVendedor = new ControladorVendedor();
        imgArrowBack = v.findViewById(R.id.imgArrowBack);
        svVendedor = v.findViewById(R.id.svVendedor);
        rv = v.findViewById(R.id.rvVendedoresParaVenta);
        btnAñadirVendedor = v.findViewById(R.id.btnAgregarVendedor);
        if (getTag() != null && getTag().equals("Mostrar Vendedores Registro")) {
            btnAñadirVendedor.setVisibility(View.VISIBLE);
        }
        btnEliminarVendedor = v.findViewById(R.id.btnEliminarVendedor);
        btnAñadirVendedor.setOnClickListener(this);
        rvAdapterVendedor = new RvAdapterVendedor();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rv.setHasFixedSize(true);
        rv.setAdapter(rvAdapterVendedor);
        rvAdapterVendedor.setListener(this);

        svVendedor.setOnQueryTextListener(this);
        btnEliminarVendedor.setOnClickListener(this);

        imgArrowBack.setOnClickListener(this);
        dialog = builder.setView(v).create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgArrowBack) {

            dialog.dismiss();
        }
        else if(v.getId()==R.id.btnEliminarVendedor){

            informacionVendedor.ObtenerInformacion(new mVendedor());
            dialog.dismiss();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        new DownloadListVendedor().execute(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        new DownloadListVendedor().execute(newText);
        return true;
    }

    @Override
    public void ObtenerVendedor(mVendedor vendedor) {
        if (informacionVendedor != null) {
            Log.d("dialogSelectVendedor", "Vendedor seleccionado: " + vendedor.getPrimerNombre());
            informacionVendedor.ObtenerInformacion(vendedor);
            dialog.dismiss();
        } else {
            Log.e("dialogSelectVendedor", "Error: informacionVendedor (listener) es NULL");
        }
    }



    public interface InformacionVendedor {
        public void ObtenerInformacion(mVendedor vendedor);
    }

    private class DownloadListVendedor extends AsyncTask<String, Void, List<mVendedor>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<mVendedor> doInBackground(String... strings) {
            List<mVendedor> list;
            if (strings[0].length() <= 1) {
                list = controladorVendedor.getAllVendedor();
            } else {
                list = controladorVendedor.getBusquedaNombreApellido(strings[0]);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<mVendedor> mVendedors) {
            super.onPostExecute(mVendedors);
            if (mVendedors != null) {
                Log.d("dialogSelectVendedor", "Vendedores cargados: " + mVendedors.size());
                rvAdapterVendedor.AddElement(mVendedors);
            } else {
                Log.e("dialogSelectVendedor", "La lista de vendedores es NULL");
                rvAdapterVendedor.AddElement(new ArrayList<mVendedor>());
            }
        }
    }
}
