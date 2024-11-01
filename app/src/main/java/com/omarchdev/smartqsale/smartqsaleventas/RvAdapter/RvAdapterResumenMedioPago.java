package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDetalleMovCaja;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mResumenMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 01/02/2018.
 */

public class RvAdapterResumenMedioPago extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mResumenMedioPago> list;

    public RvAdapterResumenMedioPago() {

        this.list = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_resumen_mp_cierre, parent, false);
        return new ResumentMPVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResumentMPVH h = (ResumentMPVH) holder;
        h.txtDescMp.setText(list.get(position).getDescripcionMedioPago());
        h.txtMontoEntrada.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMontoEntrada()));
        h.txtMontoSalida.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMontoSalida()));
        h.txtMontoDisponible.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMontoDisponible()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void ChangeElement(mDetalleMovCaja movCaja, int idMedioPago, byte tipo) {

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getIdMedioPago() == idMedioPago) {

                if (tipo == 1) {
                    list.get(i).setMontoEntrada(list.get(i).getMontoEntrada().add(movCaja.getMonto()));

                } else if (tipo == 2) {
                    list.get(i).setMontoSalida(list.get(i).getMontoSalida().add(movCaja.getMonto()));
                }
                list.get(i).setMontoDisponible(list.get(i).getMontoEntrada().subtract(list.get(i).getMontoSalida()));
                notifyItemChanged(i);

                break;

            }

        }

    }

    public void AddElement(List<mResumenMedioPago> list) {

        this.list.clear();
        this.list.addAll(list);

    }

    public class ResumentMPVH extends RecyclerView.ViewHolder {

        TextView txtDescMp, txtMontoEntrada, txtMontoSalida, txtMontoDisponible;


        public ResumentMPVH(View itemView) {
            super(itemView);
            txtDescMp = (TextView) itemView.findViewById(R.id.txtDescMedioPago);
            txtMontoEntrada = (TextView) itemView.findViewById(R.id.txtMontoEntrada);
            txtMontoSalida = (TextView) itemView.findViewById(R.id.txtMontoSalida);
            txtMontoDisponible = (TextView) itemView.findViewById(R.id.txtMontoDisponible);

            txtDescMp.setText("");
            txtMontoEntrada.setText(Constantes.DivisaPorDefecto.SimboloDivisa + "0.00");
            txtMontoSalida.setText(Constantes.DivisaPorDefecto.SimboloDivisa + "0.00");
            txtMontoDisponible.setText(Constantes.DivisaPorDefecto.SimboloDivisa + "0.00");

        }


    }







}
