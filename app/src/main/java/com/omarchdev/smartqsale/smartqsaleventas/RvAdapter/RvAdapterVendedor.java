package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 07/12/2017.
 */

public class RvAdapterVendedor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Vendedor listenerVendedor;
    List<mVendedor> listVendedores;

    public RvAdapterVendedor() {
        listVendedores = new ArrayList<>();
    }

    public void setListener(Vendedor listenerVendedor) {
        this.listenerVendedor = listenerVendedor;
    }

    public void AddElement(List<mVendedor> list) {
        if (list != null) {
            listVendedores = list;
        } else {
            listVendedores = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_vendedor_en_venta, parent, false);
        return new VendedorEnVentaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VendedorEnVentaViewHolder vendedorEnVentaViewHolder = (VendedorEnVentaViewHolder) holder;
        vendedorEnVentaViewHolder.txtNombre.setText(listVendedores.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return listVendedores.size();
    }

    public interface Vendedor {

        public void ObtenerVendedor(mVendedor vendedor);
    }

    class VendedorEnVentaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtNombre;
        View layoutVendedor;

        public VendedorEnVentaViewHolder(View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombreVendedor);
            layoutVendedor = itemView.findViewById(R.id.linearLayoutVendedor);
            
            itemView.setOnClickListener(this);
            if (layoutVendedor != null) {
                layoutVendedor.setOnClickListener(this);
            }
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listenerVendedor != null) {
                listenerVendedor.ObtenerVendedor(listVendedores.get(position));
            }
        }
    }
}

