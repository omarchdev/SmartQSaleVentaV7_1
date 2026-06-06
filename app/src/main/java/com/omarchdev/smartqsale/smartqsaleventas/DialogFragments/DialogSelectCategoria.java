package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUnidadMedida;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterCategoria;

import java.util.List;

/**
 * Created by OMAR CHH on 20/01/2018.
 */

public class DialogSelectCategoria extends DialogFragment implements AsyncCategoria.ListenerCategoria {

    Dialog dialog;
    RecyclerView rv;
    RvAdapterCategoria rvAdapterCategoria;
    AsyncCategoria asyncCategoria;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = (getActivity().getLayoutInflater().inflate(R.layout.dialog_select_categoria, null));
        rv = (RecyclerView) v.findViewById(R.id.rvCategoriaProductos);
        rvAdapterCategoria = new RvAdapterCategoria();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.setAdapter(rvAdapterCategoria);

        dialog = builder.setView(v).create();

        asyncCategoria = new AsyncCategoria();
        asyncCategoria.setListenerCategoria(this);
        asyncCategoria.getCategorias();

        return dialog;
    }

    @Override
    public void CategoriasObtenidas(List<mCategoriaProductos> categoriaProductosList) {
        if (categoriaProductosList != null) {
            rvAdapterCategoria.AddElements(categoriaProductosList);
        } else {
            Toast.makeText(getActivity(), "Error al obtener categorías", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ObtenerUnidadesMedidad(List<mUnidadMedida> listaUnidades) {
        // No-op
    }
}















