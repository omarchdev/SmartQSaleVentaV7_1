package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.Modificador;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by OMAR CHH on 01/05/2018.
 */

public class RvAdapterModificador extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Modificador> modificadorList;
    boolean click;
    int pos;
    int oldPos;
    ListenerModificadorConfig listenerModificadorConfig;
    Context context;

    public interface ListenerModificadorConfig{

        public void getPositionSelect(int position);
        public void editPositionSelect(int position);
        public void deletePosition(int position);

    }
    public void setListenerModificadorConfig(ListenerModificadorConfig listenerModificadorConfig){

        this.listenerModificadorConfig=listenerModificadorConfig;

    }

    public RvAdapterModificador() {
        modificadorList=new ArrayList<>();
        pos=-10;
        oldPos=-10;
        click=false;
    }

    private class ModificadorVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDescripcionModificador;
        Button btnEditar;
        ImageButton btnDelete;

        public ModificadorVH(View itemView) {
            super(itemView);
            txtDescripcionModificador=itemView.findViewById(R.id.txtDescripcionModificador);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnEditar=itemView.findViewById(R.id.btnEditar);

            btnEditar.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(click){
                        oldPos=pos;
                        DeleteSelection(oldPos);
                    }
                    pos=getAdapterPosition();
                    Seleccion(pos);
                    listenerModificadorConfig.getPositionSelect(pos);
                    click=true;
                }
            });
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.btnDelete:
                    listenerModificadorConfig.deletePosition(getAdapterPosition());
                    break;
                case R.id.btnEditar:
                    listenerModificadorConfig.editPositionSelect(getAdapterPosition());
                    break;
            }
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_modificador_config,parent,false);
        context=parent.getContext();
        return new ModificadorVH(v);
    }


    public void DeleteSelection(int pos){

        notifyItemChanged(pos);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        ModificadorVH vh=(ModificadorVH)holder;

        vh.txtDescripcionModificador.setText(modificadorList.get(position).getDescripcion());

        if(position==pos){

            vh.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            vh.txtDescripcionModificador.setTextColor(Color.WHITE);
        }
        else{

            vh.itemView.setBackgroundColor(Color.TRANSPARENT);
            vh.txtDescripcionModificador.setTextColor(Color.WHITE);

        }
        if(oldPos>=0){
            if(oldPos!=position){

                if(oldPos==position){

                    vh.itemView.setBackgroundColor(Color.TRANSPARENT);
                }

            }

        }
        else{

        }


    }

    public void Seleccion(int position){
        notifyItemChanged(position);

    }

    public void ActualizarLista(List<Modificador> modificadorList){

        this.modificadorList.addAll(modificadorList);
        notifyDataSetChanged();
    }

    public void EliminarModificador(int position){

        this.modificadorList.remove(position);
        notifyItemRemoved(position);

    }

    public void EditarPosicion(String modificador ,int position){

        this.modificadorList.get(position).setDescripcion(modificador);
        notifyItemChanged(position);

    }
    public void IngresarModificador(Modificador modificador){

        this.modificadorList.add(modificador);
        notifyItemInserted(modificadorList.size()-1);

    }

    @Override
    public int getItemCount() {

        return modificadorList.size();
    }
}
