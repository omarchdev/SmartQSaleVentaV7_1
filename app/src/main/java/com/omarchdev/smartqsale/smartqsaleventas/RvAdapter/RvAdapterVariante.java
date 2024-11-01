package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.Variante;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 17/03/2018.
 */

public class RvAdapterVariante extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<Variante> list;

    public RvAdapterVariante() {
        this.list=new ArrayList<>();
    }

    class VarianteVH extends RecyclerView.ViewHolder{
            TextView txtNombre;
            TextView edtPrecioCompra,edtPrecioVenta;


            public VarianteVH(View itemView) {
                super(itemView);
                txtNombre=(TextView)itemView.findViewById(R.id.txtNombreVariante);
                edtPrecioCompra=(TextView)itemView.findViewById(R.id.txtPCompra);
                edtPrecioVenta=(TextView)itemView.findViewById(R.id.txtPVenta);
             }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_variante,parent,false);
            return new VarianteVH(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                 VarianteVH vh=(VarianteVH)holder;
                 vh.txtNombre.setText(list.get(position).getNombreVariante());
                 vh.edtPrecioCompra.setText(String.format("%.2f",list.get(position).getPrecioCompra()));
                 vh.edtPrecioVenta.setText(String.format("%.2f",list.get(position).getPrecioVenta()));


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void ActualizarVariante(int position,Variante variante){
            list.get(position).setPrecioCompra(variante.getPrecioCompra());
            list.get(position).setPrecioVenta(variante.getPrecioVenta());
            list.get(position).setStockProducto(variante.getStockProducto());
            notifyItemChanged(position);

        }

    public void EliminarVariante(int pos){

        this.list.remove(pos);
        notifyItemRemoved(pos);

    }
       public void AgregarVariantes(List<Variante> list){
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }
}
