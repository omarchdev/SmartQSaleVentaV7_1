package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 19/12/2017.
 */

public class RvAdapterDetallePedido extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String simboloMoneda = Constantes.DivisaPorDefecto.SimboloDivisa;
    private List<ProductoEnVenta> list;


    public RvAdapterDetallePedido() {
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_producto_detalle_pedido, parent, false);
        return new DetallePedidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DetallePedidoViewHolder h = (DetallePedidoViewHolder) holder;
        if (list.get(position).isEsDetallePack()) {

            h.txtNombreProducto.setPadding(50, 0, 0, 0);
            h.txtNombreProducto.setText(list.get(position).getProductName() + "\n" + list.get(position).getObservacionProducto());
            h.txtCantidadProducto.setText("x" + String.valueOf(list.get(position).getCantidad()));
            h.txtSubtotalProducto.setVisibility(View.INVISIBLE);

        } else if (list.get(position).isEsModificado()) {
            h.txtNombreProducto.setPadding(0, 0, 0, 0);
            h.txtNombreProducto.setText(list.get(position).getProductName() + "\n" + list.get(position).getDescripcionModificador() + "\n" + list.get(position).getObservacionProducto());
            h.txtCantidadProducto.setText("x" + String.valueOf(list.get(position).getCantidad()));
            h.txtSubtotalProducto.setVisibility(View.VISIBLE);
            h.txtSubtotalProducto.setText(simboloMoneda + String.format("%.2f", list.get(position).getPrecioVentaFinal()));

        } else {
            h.txtNombreProducto.setPadding(0, 0, 0, 0);
            h.txtNombreProducto.setText(list.get(position).getProductName() + "\n" + list.get(position).getObservacionProducto());
            h.txtCantidadProducto.setText("x" + String.valueOf(list.get(position).getCantidad()));
            h.txtSubtotalProducto.setVisibility(View.VISIBLE);
            h.txtSubtotalProducto.setText(simboloMoneda + String.format("%.2f", list.get(position).getPrecioVentaFinal()));
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

    public class DetallePedidoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProducto;
        TextView txtCantidadProducto;
        TextView txtSubtotalProducto;
        TextView txtDetalleArticulo;

        public DetallePedidoViewHolder(View itemView) {
            super(itemView);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreArticuloDetalePedido);
            txtCantidadProducto = itemView.findViewById(R.id.txtCantidadProductoDetalle);
            txtSubtotalProducto = itemView.findViewById(R.id.txtSubtotalProducto);
            txtDetalleArticulo = itemView.findViewById(R.id.txtDetalleArticulo);
        }
    }
}
