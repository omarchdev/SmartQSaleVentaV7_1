package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleModificador;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 01/05/2018.
 */

public class RvAdapterValorMod extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DetalleModificador> detalleModificadorList;

    boolean click;
    int pos;
    int oldPos;
    ListenerValorMod listenerValorMod;
    Context context;

    public interface ListenerValorMod{


        public void getPositionValor(int position);

    }

    public void setListenerValorMod(ListenerValorMod listenerValorMod){

        this.listenerValorMod=listenerValorMod;

    }

    public RvAdapterValorMod() {
        detalleModificadorList=new ArrayList<>();
        click=false;
        pos=-10;
        oldPos=-10;
    }

    public void AddListValores(List<DetalleModificador> detalleModificadorList){
        pos=-10;
        oldPos=-10;
        this.detalleModificadorList.clear();
        this.detalleModificadorList.addAll(detalleModificadorList);
        notifyDataSetChanged();

    }

    public void EditarValor(int position,String Descripcion){
        detalleModificadorList.get(position).setDescripcionModificador(Descripcion);
        notifyItemChanged(position);
    }
    public void AgregarValorModificador(DetalleModificador detalleModificador){
        this.detalleModificadorList.add(detalleModificador);
        notifyItemInserted(detalleModificadorList.size()-1);
    }
    public void EliminarElementoPosicion(int position){
        pos=-10;
        oldPos=-10;
        click=false;
        detalleModificadorList.remove(position);
        notifyItemRemoved(position);

    }
    public void setClick(boolean click){
        this.click=click;
    }

    private class ValorModVH extends RecyclerView.ViewHolder{

        TextView txtDescripcionValor;
        public ValorModVH(View itemView) {
            super(itemView);
            txtDescripcionValor=itemView.findViewById(R.id.txtDescripcionValor);
            itemView.setOnClickListener(v -> {

                if(click){
                    oldPos=pos;
                    DeleteSelection(oldPos);
                }
                pos=getAdapterPosition();
                Seleccion(pos);
                listenerValorMod.getPositionValor(pos);
                click=true;


            });

        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_valor_modificador,parent,false);
        context=parent.getContext();
        return new ValorModVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ValorModVH vh=(ValorModVH)holder;
        vh.txtDescripcionValor.setText(detalleModificadorList.get(position).getDescripcionModificador());
        if(position==pos){
            vh.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            vh.txtDescripcionValor.setTextColor(Color.WHITE);

        }
        else{
            vh.itemView.setBackgroundColor(Color.TRANSPARENT);
            vh.txtDescripcionValor.setTextColor(Color.WHITE);
        }
        if(oldPos>=0){
            if(oldPos!=position){

                if(oldPos==position){

                    vh.itemView.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return detalleModificadorList.size();
    }


    public void Seleccion(int position){
        notifyItemChanged(position);

    }
    public void DeleteSelection(int pos){

        notifyItemChanged(pos);
    }
}
