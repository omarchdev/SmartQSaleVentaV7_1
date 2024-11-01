package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 28/01/2018.
 */

public class RvAdapterHistorialCierres extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mCierre> list;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    ObtenerCierre obtenerCierre;
    public RvAdapterHistorialCierres() {
        list = new ArrayList<>();
    }

    public void setObtenerCierre(ObtenerCierre obtenerCierre) {

        this.obtenerCierre = obtenerCierre;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_hcierre, parent, false);
        return new CierresViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CierresViewHolder h = (CierresViewHolder) holder;
        h.fechaApertura.setText(dateFormat.format(list.get(position).getFechaApertura()));
        h.txtNumTransa.setText("# Transacciones "+String.valueOf(list.get(position).getNumTransacciones()));
        if (list.get(position).getEstadoCierre().equals("C")) {
            h.txtInfoCierre.setText("Caja cerrada:\n"+"#"+String.valueOf(list.get(position).getIdCierre()));
            if (list.get(position).getFechaCierre() != null) {
                h.fechaCierre.setText(dateFormat.format(list.get(position).getFechaCierre()));
            } else {
                h.fechaCierre.setText("-");
            }
        } else if (list.get(position).getEstadoCierre().equals("A")) {
            h.txtInfoCierre.setText("Caja Abierta:\n"+"#"+String.valueOf(list.get(position).getIdCierre()));

            if (list.get(position).getFechaCierre() != null) {
                h.fechaCierre.setText(dateFormat.format(list.get(position).getFechaCierre()));

            } else {
                h.fechaCierre.setText("-");
            }
        }

    }

    public void AddElement(List<mCierre> list) {

        this.list.clear();
        this.list.addAll(list);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ObtenerCierre {

        void ObtenerIdCierre(int idCierre);
    }

    private class CierresViewHolder extends RecyclerView.ViewHolder  {

        TextView fechaApertura, fechaCierre,txtInfoCierre,txtNumTransa;
        public CierresViewHolder(View itemView) {
            super(itemView);
            fechaApertura = (TextView) itemView.findViewById(R.id.txtFechaApertura);
            fechaCierre = (TextView) itemView.findViewById(R.id.txtFechaCierre);
            txtInfoCierre=(TextView)itemView.findViewById(R.id.txtInfoCierre);
            txtNumTransa=itemView.findViewById(R.id.txtNumTransa);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obtenerCierre.ObtenerIdCierre(list.get(getAdapterPosition()).getIdCierre());
                }
            });
            fechaApertura.setText("-----");
            fechaCierre.setText("------");
        }

    }
}
