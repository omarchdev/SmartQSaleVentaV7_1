package com.omarchdev.smartqsale.smartqsaleventas.PrintOptions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 06/05/2018.
 */

public class RvAdapterSelectMedioPagoImg extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> lista;
    Context context;
    boolean click;
    int position;

    int oldPosition;
    ListenerMedioPago listenerMedioPago;

    public interface ListenerMedioPago{

        public void MetodoPagoSelect(String nombreImagen);
    }

    public void setListenerMedioPago(ListenerMedioPago listenerMedioPago){

        this.listenerMedioPago=listenerMedioPago;

    }

    public void setClick(boolean click){
        this.click=click;
    }

    public RvAdapterSelectMedioPagoImg() {
        position=0;
        oldPosition=-10;
        lista=new ArrayList<>();
        lista.add("money_cash");
        lista.add("money_simple");
        lista.add("money_ticket");
        lista.add("money_usd");
        lista.add("cash_usd");
        lista.add("cheque");
        lista.add("credit_card");
        click=true;
    }


    public class MedioPagoVH extends RecyclerView.ViewHolder {
        ImageView imgMedioPago;
        public MedioPagoVH(View itemView) {
            super(itemView);
            imgMedioPago=itemView.findViewById(R.id.imgMedioPago);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(click==true) {
                        oldPosition = position;
                        position = getAdapterPosition();
                        ClearElement(oldPosition);
                        SelectElement(position);
                        listenerMedioPago.MetodoPagoSelect(lista.get(position));
                    }

                }
            });
        }

    }
    public void ClearElement(int position){

        notifyItemChanged(position);
    }
    public void SelectElement(int pos){
        notifyItemChanged(pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_img_metodo_pago,parent,false);
        context=parent.getContext();
        return new MedioPagoVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MedioPagoVH vh=(MedioPagoVH)holder;
        vh.imgMedioPago.setImageResource(context.getResources().getIdentifier( "@drawable/"+lista.get(position).trim(),null,context.getPackageName()));
            if (position == this.position) {

                vh.imgMedioPago.setBackground(context.getResources().getDrawable(R.drawable.fondo_mod_prod_on));
            }
            else if(position==oldPosition){
                vh.imgMedioPago.setBackground(context.getResources().getDrawable(R.drawable.fondo_img));
            }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public void selectImage(String imagen){

        for(int i=0;i<lista.size();i++){
            oldPosition=position;
            ClearElement(oldPosition);
            if(lista.get(i).equals(imagen.trim())) {
                position=i;
            }
        }

        SelectElement(position);


    }
}
