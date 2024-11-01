package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 05/05/2018.
 */

public class RvAdapterMedioPago extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<mMedioPago> medioPagoList;
    Context context;

    ListenerListadoMP listenerListadoMP;

    public  interface ListenerListadoMP{

        public void setPositionMP(int position);

    }

    public void setListenerListadoMP(ListenerListadoMP listenerListadoMP){

        this.listenerListadoMP=listenerListadoMP;
    }


    public RvAdapterMedioPago() {
        medioPagoList=new ArrayList<>();

    }

    public class MedioPagoVh extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgMedioPago;
        TextView txtMedioPago;
        ImageButton imgSetting;

        public MedioPagoVh(View itemView) {
            super(itemView);
            imgMedioPago=itemView.findViewById(R.id.imgMedioPago);
            txtMedioPago=itemView.findViewById(R.id.txtMedioPago);
            imgSetting=itemView.findViewById(R.id.imgSetting);
            imgSetting.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.imgSetting){
                listenerListadoMP.setPositionMP(getAdapterPosition());
            }
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_medio_pago,parent,false);
        context=parent.getContext();
        return new MedioPagoVh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MedioPagoVh vh=(MedioPagoVh)holder;

        vh.txtMedioPago.setText(medioPagoList.get(position).getcDescripcionMedioPago());
        if(medioPagoList.get(position).isEstadoModificador()){
            vh.imgSetting.setVisibility(View.VISIBLE);
        }else{
            vh.imgSetting.setVisibility(View.INVISIBLE);
        }
        String uri= "@drawable/"+medioPagoList.get(position).getIdImagen().trim();
        uri=uri.trim();
        vh.imgMedioPago.setImageResource(context.getResources().getIdentifier(uri,null,context.getPackageName()));

        /*

            ImageView imagen = (ImageView) convertView.findViewById(R.id.imgMetodoPago);
        TextView txt = (TextView) convertView.findViewById(R.id.txtMetodoPago);
        String uri = "@drawable/" + getItem(position).getIdImagen();
        uri = uri.trim();
        txt.setText(getItem(position).getcDescripcionMedioPago());
        imagen.setImageResource(parent.getContext().getResources().getIdentifier(uri, null, parent.getContext().getPackageName()));

*/

    }

    @Override
    public int getItemCount() {
        return medioPagoList.size();
    }

    public void AgregarDatos(List<mMedioPago> medioPagoList){

        this.medioPagoList.clear();
        this.medioPagoList.addAll(medioPagoList);
        notifyDataSetChanged();

    }

}
