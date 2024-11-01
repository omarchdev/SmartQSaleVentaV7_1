package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterCarSale;

import java.util.List;

/**
 * Created by OMAR CHH on 23/11/2017.
 */

public class DialogDetalleCarrito extends DialogFragment{


     List<ProductoEnVenta>list;
     Context context;
     RecyclerView rv;
     TextView txtCarrito;
     RvAdapterCarSale rvAdapterCarSale;
     DetalleVentaInterface listener;


    public interface DetalleVentaInterface
    {
        public void MensajeSalida(DialogFragment dialogFragment);
    }

    public DialogDetalleCarrito newInstance(List<ProductoEnVenta> list,Context context,DetalleVentaInterface detalleVentaInterface){
        DialogDetalleCarrito d=new DialogDetalleCarrito();
        d.setList(list);
        d.setContext(context);
        d.setListener(detalleVentaInterface);
        return d;


    }



    private List<ProductoEnVenta> getList() {
        return list;
    }

    private void setList(List<ProductoEnVenta> list) {
        this.list = list;
    }

    @Override
    public Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public DetalleVentaInterface getListener() {
        return listener;
    }

    public void setListener(DetalleVentaInterface listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View v=((Activity)context).getLayoutInflater().inflate(R.layout.dialog_detalle_carrito_venta,null);
            rv=(RecyclerView)v.findViewById(R.id.rvDetalleVenta);
            txtCarrito=(TextView)v.findViewById(R.id.txtTextoCarrito);
            txtCarrito.setVisibility(View.GONE);
            rvAdapterCarSale = new RvAdapterCarSale( list);
            rv.setLayoutManager(new LinearLayoutManager(context));
            rv.setHasFixedSize(true);
            rv.setAdapter(rvAdapterCarSale);
               rv.setVisibility(View.VISIBLE);
           builder.setNegativeButton("Regresar", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                        listener.MensajeSalida(DialogDetalleCarrito.this);
                        }
           });


            Dialog dialog=builder.setTitle("Detalle de venta").setIcon(R.drawable.car_sale).setView(v).create();
             return dialog;
    }


}

