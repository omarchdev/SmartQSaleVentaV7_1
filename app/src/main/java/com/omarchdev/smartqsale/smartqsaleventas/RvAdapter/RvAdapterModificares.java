package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.Modificador;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 20/04/2018.
 */

public class RvAdapterModificares extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Modificador> modificadorList;
    int pos;
    int oldPos;
    boolean click;
    ClickModificador clickModificador;
    public interface ClickModificador{

        public void getModificadorClick(int position);
    }

    public void setClickModificador(ClickModificador clickModificador){

        this.clickModificador=clickModificador;

    }
    public RvAdapterModificares() {

        modificadorList=new ArrayList<>();
        pos=-10;
        oldPos=-10;
        click=false;

    }
    public interface OnModClicked {
        void onModClick(View v,int position);
    }

    private class ModificadorVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlContent;
        TextView txtDescripcionModificador;
        OnModClicked onModClicked;

        public void setOnModClicked(OnModClicked onModClicked){

            this.onModClicked=onModClicked;

        }


        public ModificadorVH(View itemView) {
            super(itemView);
            rlContent=itemView.findViewById(R.id.rlContent);
            txtDescripcionModificador=itemView.findViewById(R.id.txtDescripcionModificador);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            onModClicked.onModClick(v,getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_modificador,parent,false);
        return new ModificadorVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModificadorVH vh=(ModificadorVH)holder;
        vh.txtDescripcionModificador.setText(modificadorList.get(position).getDescripcion());
        vh.setOnModClicked(new OnModClicked() {
            @Override
            public void onModClick(View v, int position) {
                if(click){
                    oldPos=pos;
                    DeleteSelection(oldPos);
                }
                ChangeColor(position);
                clickModificador.getModificadorClick(position);
                click=true;
            }
        });

        if(click){

            if (pos == position) {
                vh.rlContent.setBackgroundResource(R.color.colorPrimary);
            }
            else{
                vh.rlContent.setBackgroundColor(Color.TRANSPARENT);
            }
            if(oldPos>=0){
                if(oldPos!=position){
                    if(oldPos==position){
                        vh.rlContent.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return modificadorList.size();
    }

    public void DeleteSelection(int pos){

        notifyItemChanged(pos);
    }
    public void ChangeColor(int pos){
        this.pos=pos;
        notifyItemChanged(this.pos);

    }

    public void AgregarModificador(Modificador modificador){
        this.modificadorList.add(modificador);
        notifyDataSetChanged();
    }
    public void AddModificadores(List<Modificador> list){
        this.modificadorList.clear();
        this.modificadorList.addAll(list);
        pos=-10;
        oldPos=-10;
        click=false;
        clickModificador.getModificadorClick(pos);
        notifyDataSetChanged();
    }

    public void EliminarModificador(int pos){

        this.modificadorList.remove(pos);
        notifyItemRemoved(pos);
        this.pos=-10;
        oldPos=-10;
        clickModificador.getModificadorClick(this.pos);
    }


}
