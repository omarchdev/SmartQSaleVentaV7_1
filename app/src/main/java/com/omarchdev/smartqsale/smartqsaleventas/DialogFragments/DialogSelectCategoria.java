package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterCategoria;

/**
 * Created by OMAR CHH on 20/01/2018.
 */

public class DialogSelectCategoria extends DialogFragment {

    Dialog dialog;
    RecyclerView rv;
    RvAdapterCategoria rvAdapterCategoria;
    BdConnectionSql bdConnectionSql;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        bdConnectionSql = BdConnectionSql.getSinglentonInstance();
        View v = (getActivity().getLayoutInflater().inflate(R.layout.dialog_select_categoria, null));
        rv = (RecyclerView) v.findViewById(R.id.rvCategoriaProductos);
        rvAdapterCategoria = new RvAdapterCategoria();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.setAdapter(rvAdapterCategoria);

        dialog = builder.setView(v).create();


        rvAdapterCategoria.AddElements(bdConnectionSql.getCategorias(0, " "));

        return dialog;


    }
}















