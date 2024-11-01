package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.graphics.Color;
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

public class RvAdapterProductosSelect extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<mProduct> productList;
    int pos;
    int oldPos;
    boolean click;
    ListenerSelectedProduct listenerSelectedProduct;
    boolean mostrarCantidad;
    Context context;

    public interface ListenerSelectedProduct{

        public void productSelected(int position,int idProduct);
    }

    public interface AdapterListener{

        public void selectedItem(View v,int position);

    }

    public void setListenerSelectedProduct(ListenerSelectedProduct listenerSelectedProduct){
        this.listenerSelectedProduct=listenerSelectedProduct;
    }

    public RvAdapterProductosSelect() {

        productList=new ArrayList<>();
        pos=-10;
        oldPos=-10;
        click=false;
        mostrarCantidad=false;
    }

    private class productViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCodigo,txtNombre,txtCantidad;
        AdapterListener adapterListener;

        public void setAdapterListener(AdapterListener adapterListener){
            this.adapterListener=adapterListener;
        }

        public productViewHolder(View itemView) {
            super(itemView);
            txtCodigo=(TextView) itemView.findViewById(R.id .txtCodigo);
            txtNombre=(TextView) itemView.findViewById(R.id.txtMonto);
            txtCantidad=(TextView)itemView.findViewById(R.id.txtCantidad);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

               adapterListener.selectedItem(view,getAdapterPosition());
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_pack,parent,false);
        context=parent.getContext();
        return new productViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        productViewHolder vh=(productViewHolder)holder;
        vh.txtNombre.setText(productList.get(position).getcProductName());
        vh.txtCodigo.setText(productList.get(position).getcKey());
        vh.itemView.setBackgroundColor(Color.TRANSPARENT);
        if(mostrarCantidad){
            vh.txtCantidad.setVisibility(View.VISIBLE);
            vh.txtCantidad.setText(String.format("%.2f",productList.get(position).getStockDisponible()));
            vh.txtCantidad.setTextColor(Color.parseColor("#ffffff"));

        }
        else {

            vh.txtCantidad.setVisibility(View.INVISIBLE);

        }
        vh.txtCantidad.setTextColor(Color.parseColor("#ffffff"));

        vh.setAdapterListener(new AdapterListener() {
            @Override
            public void selectedItem(View v, int position) {
                if(click){
                    oldPos=pos;
                    DeleteSelection(oldPos);
                }
                listenerSelectedProduct.productSelected(position,productList.get(position).getIdProduct());
                ChangeColor(position);
                click=true;
            }
        });
        if(click){
            if(pos==position) {
                vh.itemView.setBackgroundResource(R.color.colorPrimary);
                vh.txtNombre.setTextColor(Color.WHITE);
            }
            else{

                vh.itemView.setBackgroundColor(Color.TRANSPARENT);
                vh.txtNombre.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
            if(oldPos>=0){
                if(oldPos!=position){
                    if(oldPos==position){
                        vh.itemView.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void ChangeColor(int pos){
        this.pos=pos;
        notifyItemChanged(this.pos);

    }



    public void EliminarProducto(int position){
        this.productList.remove(position);
        notifyItemRemoved(position);
    }
    public void InsertarProducto(mProduct product){
        this.productList.add(product);
        notifyItemInserted(getItemCount());
    }
    public void AgregarElementos(List<mProduct> productList){
        this.productList.clear();
        this.productList.addAll(productList);
        pos=-10;
        oldPos=-10;
        click=false;
        listenerSelectedProduct.productSelected(pos,0);
        notifyDataSetChanged();
    }
    public void MostrarCantidad(boolean mostrarCantidad){
        this.mostrarCantidad=mostrarCantidad;
    }

    public void DeleteSelection(int pos){

        notifyItemChanged(pos);
    }

    public void ChangeInfoPosition(int post,mProduct product){
        productList.get(post).setPrecioVenta(product.getPrecioVenta());
        productList.get(post).setStockDisponible(product.getStockDisponible());
        productList.get(post).setIdTipoModifica(product.getIdTipoModifica());
        productList.get(post).setMontoModifica(product.getMontoModifica());
        notifyItemChanged(post,product);
    }
}
