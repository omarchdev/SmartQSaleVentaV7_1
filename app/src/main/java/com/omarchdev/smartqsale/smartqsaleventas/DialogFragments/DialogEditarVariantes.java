package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVariantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Variante;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVariantesProducto;

import java.util.List;


/**
 * Created by OMAR CHH on 28/03/2018.
 */

public class DialogEditarVariantes extends DialogFragment implements  AsyncVariantes.ListenerVariante {

    RecyclerView rv;
    RvAdapterVariantesProducto rvAdapterVariantesProducto;
    AsyncVariantes asyncVariantes;
    int idProduct;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view= getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_variantes,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        asyncVariantes=new AsyncVariantes();
        asyncVariantes.setListenerVariante(this);
        rv=(RecyclerView)view.findViewById(R.id.rvVariantesEditable);

        rvAdapterVariantesProducto=new RvAdapterVariantesProducto();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(rvAdapterVariantesProducto);
        asyncVariantes.getVariantesProduct(idProduct);
        return builder.create();
    }

    public void setIdProduct(int idProduct){

        this.idProduct=idProduct;
    }

    @Override
    public void VariantesProducto(List<Variante> varianteList) {
        rvAdapterVariantesProducto.AddElement(varianteList);

            }
}
