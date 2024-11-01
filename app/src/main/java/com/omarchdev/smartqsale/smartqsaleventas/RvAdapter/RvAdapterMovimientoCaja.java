package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDetalleMovCaja;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 02/02/2018.
 */

public class RvAdapterMovimientoCaja extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mDetalleMovCaja> list;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

    public RvAdapterMovimientoCaja() {

        this.list = new ArrayList<>();


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_movimiento_caja, parent, false);
        return new MovimientoCajaVH(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MovimientoCajaVH mHolder = (MovimientoCajaVH) holder;
        mHolder.txtMedioPago.setText(list.get(position).getNombreMedioPago());
        list.get(position).ConvierteFechaJSON();
        if (list.get(position).getDescripcionMotivo().length() > 0) {
            mHolder.txtMotivoRetiro.setVisibility(View.VISIBLE);
        } else {
            mHolder.txtMotivoRetiro.setVisibility(View.GONE);
        }

        mHolder.txtMotivoRetiro.setText(list.get(position).getDescripcionMotivo());
        if (list.get(position).getTipoRegistro() == 1) {
            mHolder.txtMonto.setTextColor(Color.parseColor("#64DD17"));
        } else if (list.get(position).getTipoRegistro() == 2) {

            mHolder.txtMonto.setTextColor(Color.parseColor("#D50000"));
        }
        if (list.get(position).getDescripcion().length() > 0) {
            mHolder.txtDescripcion.setVisibility(View.VISIBLE);
        } else {
            mHolder.txtDescripcion.setVisibility(View.GONE);
        }
        mHolder.txtMonto.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMonto()));

        mHolder.txtDescripcion.setText(list.get(position).getDescripcion());
        mHolder.txtFecha.setText(dateFormat.format(list.get(position).getFechaTransaccion()));


    }

    public void AddElement(List<mDetalleMovCaja> list) {

        this.list.clear();
        this.list.addAll(list);

    }

    public void AddMov(mDetalleMovCaja mov) {

        this.list.add(mov);
        notifyItemInserted(list.size() - 1);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MovimientoCajaVH extends RecyclerView.ViewHolder {

        TextView txtMedioPago, txtMotivoRetiro, txtDescripcion, txtFecha, txtMonto;

        public MovimientoCajaVH(View itemView) {
            super(itemView);

            txtMedioPago = (TextView) itemView.findViewById(R.id.txtMetodoPago);
            txtMonto = (TextView) itemView.findViewById(R.id.txtMonto);
            txtMotivoRetiro = (TextView) itemView.findViewById(R.id.txtMotivo);
            txtDescripcion = (TextView) itemView.findViewById(R.id.txtDescripcion);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFechaTransaccion);



            txtMotivoRetiro.setVisibility(View.GONE);
            txtDescripcion.setVisibility(View.GONE);


        }
    }
}
