package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.Variante;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 28/03/2018.
 */

public class RvAdapterVariantesProducto extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Variante> list;
    int pos;
    int oldPos;
    boolean click;
    ClickVariante clickVariante;
    Context context;
    public interface ClickVariante{
        public void getVarianteClick(int position);
    }

    public void setClickVariante(ClickVariante clickVariante){
        this.clickVariante=clickVariante;
    }

    public RvAdapterVariantesProducto() {
        list =new ArrayList<>();
        pos=-10;
        oldPos=-10;
        click=false;
    }

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(View v,int position);
    }



    private class VarianteVH extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtNombreVariante,txtStockVariante,txtPc,txtPv;
        RelativeLayout rlItemVariante;
        OnItemClicked onItemClicked;

        public void setOnItemClicked(OnItemClicked onItemClicked){
                this.onItemClicked=onItemClicked;
        }


        public VarianteVH(View itemView) {

             super(itemView);
            txtNombreVariante=(TextView)itemView.findViewById(R.id.txtNombreVariante);
            txtStockVariante=(TextView)itemView.findViewById(R.id.edtStock);
            txtPc=(TextView)itemView.findViewById(R.id.txtPc);
            txtPv=(TextView)itemView.findViewById(R.id.txtPv);
            rlItemVariante=(RelativeLayout)itemView.findViewById(R.id.rlItemVariante);
            itemView.setOnClickListener(this);


         }


        @Override
        public void onClick(View view) {
            onItemClicked.onItemClick(view,getAdapterPosition());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_variante_edit,parent,false);
        context=parent.getContext();
        return new VarianteVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        VarianteVH vh=(VarianteVH)holder;

        vh.txtNombreVariante.setText(list.get(position).getNombreVariante());
        vh.txtStockVariante.setText(String.format("%.0f",list.get(position).getStockProducto()));
        vh.txtPv.setText(String.format("%.2f",list.get(position).getPrecioVenta()));
        vh.txtPc.setText(String.format("%.2f",list.get(position).getPrecioCompra()));

        vh.setOnItemClicked(new OnItemClicked() {
            @Override
            public void onItemClick(View v, int position) {
                if(click){
                    oldPos=pos;
                    DeleteSelection(oldPos);
                }
                ChangeColor(position);
                clickVariante.getVarianteClick(position);
                click=true;

            }
        });
        if(click) {
            if (pos == position) {
                vh.rlItemVariante.setBackgroundResource(R.color.colorPrimary);
                vh.txtNombreVariante.setTextColor(Color.WHITE);
            }
            else{
                vh.rlItemVariante.setBackgroundColor(Color.TRANSPARENT);
                vh.txtNombreVariante.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
            if(oldPos>=0){
                if(oldPos!=position){
                if(oldPos==position){
                    vh.rlItemVariante.setBackgroundColor(Color.TRANSPARENT);
                }
                }
            }
        }
    }

    public void DeleteSelection(int pos){

        notifyItemChanged(pos);
    }
    public void ChangeColor(int pos){
        this.pos=pos;
        notifyItemChanged(this.pos);

    }

    public void ActualizarVariante(int pos,Variante variante){
        list.get(pos).setPrecioCompra(variante.getPrecioCompra());
        list.get(pos).setPrecioVenta(variante.getPrecioVenta());
        list.get(pos).setStockProducto(variante.getStockProducto());

        notifyItemChanged(pos);
    }

    public void AddElement(List<Variante> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void EliminarVariante(int pos){

        this.list.remove(pos);
        notifyItemRemoved(pos);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
