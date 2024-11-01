package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 20/01/2018.
 */

public class RvAdapterCategoria extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mCategoriaProductos> list;

    public RvAdapterCategoria() {
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_categoria, parent, false);

        return new CategoriaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CategoriaViewHolder categoriaViewHolder = (CategoriaViewHolder) holder;
        categoriaViewHolder.txtNombre.setText(list.get(position).getDescripcionCategoria());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void AddElements(List<mCategoriaProductos> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    public class CategoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rv;
        TextView txtNombre;

        public CategoriaViewHolder(View itemView) {
            super(itemView);
            rv = (RelativeLayout) itemView.findViewById(R.id.rvCategoriaProductos);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombreCategoria);
            txtNombre.setText(" ");
            rv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rvCategoriaProductos) {


            }

        }
    }
}
