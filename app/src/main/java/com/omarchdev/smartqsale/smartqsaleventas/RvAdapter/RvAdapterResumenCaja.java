package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mResumenFlujoCaja;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 27/01/2018.
 */

public class RvAdapterResumenCaja extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mResumenFlujoCaja> list;
    String textoDescripcion = "";

    public RvAdapterResumenCaja() {
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resumen_ventas, parent, false);
        return new ResumenPagoVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ResumenPagoVH vh = (ResumenPagoVH) holder;

        vh.imgColor.setBackgroundColor(Color.parseColor(list.get(position).getCodColor()));
        textoDescripcion = list.get(position).getDescripcionTitulo();
        if (!list.get(position).getSimbolo().equals(" ")) {
            textoDescripcion = textoDescripcion + "( " + list.get(position).getSimbolo() + " )";


        }

        vh.subtitulo.setText(list.get(position).getSubtituloCaja());
        vh.txtTipo.setText(textoDescripcion);

        vh.txtMonto.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMonto()));
        vh.txtMonto.setTextColor(Color.parseColor(list.get(position).getCodColor()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void ChangeElement(mResumenFlujoCaja resumenFlujoCaja, byte tipo) {

        if (tipo == 1) {
            list.set(2, resumenFlujoCaja);
            notifyItemChanged(2);
        } else if (tipo == 2) {
            list.set(3, resumenFlujoCaja);
            notifyItemChanged(3);
        }

    }

    public void AddElement(List<mResumenFlujoCaja> list) {

        this.list.clear();
        this.list.addAll(list);
    }

    public class ResumenPagoVH extends RecyclerView.ViewHolder {

        ImageView imgColor;
        TextView txtTipo;
        TextView txtMonto;
        TextView subtitulo;


        public ResumenPagoVH(View itemView) {
            super(itemView);

            txtTipo = (TextView) itemView.findViewById(R.id.txtTipo);
            txtMonto = (TextView) itemView.findViewById(R.id.montoDinero);
            imgColor = (ImageView) itemView.findViewById(R.id.imgColor);
            subtitulo = (TextView) itemView.findViewById(R.id.txtSubtitulo);

        }
    }

}
