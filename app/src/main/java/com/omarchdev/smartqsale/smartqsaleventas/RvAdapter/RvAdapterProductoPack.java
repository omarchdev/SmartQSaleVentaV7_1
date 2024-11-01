package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 05/04/2018.
 */

public class RvAdapterProductoPack extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mProduct> productList;
    int pos;
    int oldPos;
    boolean click;

    public RvAdapterProductoPack() {
        productList=new ArrayList<>();
        pos=-10;
        oldPos=-10;
        click=false;
    }

    private interface AdapterListener{

        public void selectProduct(int position);

    }

    private class productPackVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCodigo,txtNombre;
        AdapterListener adapterListener;
        public void setAdapterListener(AdapterListener adapterListener){

            this.adapterListener=adapterListener;

        }


        public productPackVH(View itemView) {
            super(itemView);

            txtCodigo=(TextView)itemView.findViewById(R.id.txtCodigo);
            txtNombre=(TextView)itemView.findViewById(R.id.txtMonto);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

             adapterListener.selectProduct(getAdapterPosition());


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_pack,parent,false);



        return new productPackVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        productPackVH vh=(productPackVH)holder;
        vh.txtNombre.setText(productList.get(position).getcProductName());
        vh.txtCodigo.setText(productList.get(position).getcKey());



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
