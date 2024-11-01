package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by OMAR CHH on 01/01/2018.
 */

public class RvAdapterDetalleVenta extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ProductoEnVenta> list;

    public RvAdapterDetalleVenta() {
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_producto_detalle_pedido, parent, false);

        return new DetalleVentaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DetalleVentaViewHolder h = (DetalleVentaViewHolder) holder;
        try {

            h.txtNombreProducto.setText(list.get(position).getNombreProductoDetallePack());
            h.txtNombreProducto.setPadding(0, 0, 0, 0);
            if(list.get(position).getDetallePack().equals("")){
                h.txtComboDetalle.setVisibility(View.GONE);
            }else{
                h.txtComboDetalle.setVisibility(View.VISIBLE);
            }
            h.txtComboDetalle.setText(list.get(position).getDetallePackFinal());
            if (list.get(position).isEsDetallePack()) {
                h.txtSubtotalProducto.setVisibility(View.INVISIBLE);

            } else {
                h.txtSubtotalProducto.setVisibility(View.VISIBLE);
            }

            if (list.get(position).isEsDetallePack()) {
                h.Slinea.setVisibility(View.INVISIBLE);
            } else {
                h.Slinea.setVisibility(View.VISIBLE);
            }
            h.txtCantidadProducto.setText(String.valueOf(list.get(position).getCantidad()));
            h.txtSubtotalProducto.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getPrecioVentaFinal()));
        } catch (Exception ex) {
            Log.e("ErrorDV", ex.toString());
        }

    }

    public void AddElement(List<ProductoEnVenta> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class DetalleVentaViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout Slinea;
        TextView txtNombreProducto, txtCantidadProducto, txtSubtotalProducto, txtDetalleArticulo, txtComboDetalle;

        public DetalleVentaViewHolder(View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreArticuloDetalePedido);
            txtCantidadProducto = itemView.findViewById(R.id.txtCantidadProductoDetalle);
            txtSubtotalProducto = itemView.findViewById(R.id.txtSubtotalProducto);
            txtDetalleArticulo = itemView.findViewById(R.id.txtDetalleArticulo);
            txtComboDetalle=itemView.findViewById(R.id.txtComboDetalle);
            Slinea = itemView.findViewById(R.id.Slinea);
        }
    }
}
