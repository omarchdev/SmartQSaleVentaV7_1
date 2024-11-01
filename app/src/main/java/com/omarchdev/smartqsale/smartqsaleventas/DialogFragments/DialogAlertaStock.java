package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

public class DialogAlertaStock extends DialogFragment {

    ListView listStock;
    List<String> listaProductos=new ArrayList<>();

    public void SetList(List<String> listaProductos){

        this.listaProductos.clear();
        this.listaProductos.addAll(listaProductos);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_alert_stock, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Stock no disponible");
        try {
            listStock = v.findViewById(R.id.listStock);
            listStock.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaProductos));
            builder.setView(v).setPositiveButton("Salir", null);
        }catch (Exception e){

            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();

        }

        return builder.create();
    }
}
