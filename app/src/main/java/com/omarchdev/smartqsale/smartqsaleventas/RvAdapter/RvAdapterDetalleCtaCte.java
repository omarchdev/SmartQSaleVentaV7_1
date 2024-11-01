package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleCuentaCorriente;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 13/01/2018.
 */

public class RvAdapterDetalleCtaCte extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DetalleCuentaCorriente> list;
    GetDataCtaCte getDataCtaCte;

    public RvAdapterDetalleCtaCte() {
        this.list = new ArrayList<>();
    }

    public void setListenerCtaCte(GetDataCtaCte getDataCtaCte) {
        this.getDataCtaCte = getDataCtaCte;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuenta_corriente, parent, false);
        return new DetalleCtaCteVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DetalleCtaCteVH h = (DetalleCtaCteVH) holder;
        h.txtTipoTransaccion.setText(list.get(position).getTipoTransaccion());
        String fecha = new SimpleDateFormat("EEE dd MMM yyyy").format(list.get(position).getFechaOrigen());
        h.txtFecha.setText(fecha);

        String TipoTransaccion = list.get(position).getIdTipoTransaccion();
        //Tipo transaccion 1 ->Venta
        if (TipoTransaccion.equals("01")) {

            h.txtMonto.setTextColor(Color.parseColor("#B20805"));
            h.txtMonto.setText("+ " + Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMonto()));
            h.imageButton.setImageResource(R.drawable.information_outline);
            h.imageButton.setVisibility(View.VISIBLE);
            h.imageButton.setColorFilter(Color.WHITE    );
            h.txtDescripcion.setVisibility(View.GONE);
            // h.cv.setBackgroundColor(Color.parseColor("#FFFF9393"));
        }
        //Tipo transaccion 2->Pago
        else if (TipoTransaccion.equals("02")) {

            h.txtMonto.setTextColor(Color.parseColor("#1CFF1C"));
            h.txtMonto.setText("- " + Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMonto().multiply(new BigDecimal(-1))));
            if (list.get(position).getEstadoCtaCte().equals("00")) {
                h.imageButton.setVisibility(View.VISIBLE);
                h.imageButton.setImageResource(R.drawable.close_circle);
                h.imageButton.setColorFilter(Color.RED);
            } else if (list.get(position).getEstadoCtaCte().equals("02")) {
                h.imageButton.setVisibility(View.INVISIBLE);
            }
            h.txtMetodoPago.setTextColor(Color.BLACK);
            h.txtMetodoPago.setText(list.get(position).getMetodoPago());
            //h.cv.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if (list.get(position).getcObservacion().length() == 0) {
                h.txtDescripcion.setVisibility(View.GONE);
            } else {
                h.txtDescripcion.setVisibility(View.VISIBLE);
            }
            h.txtDescripcion.setText(list.get(position).getcObservacion());

        }
        //Tipo transaccion 3-> Reembolso
        else if (TipoTransaccion.equals("03")) {

            h.txtMonto.setTextColor(Color.parseColor("#FF8A04"));
            h.txtMonto.setText("- " + Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMonto().multiply(new BigDecimal(-1))));
            h.imageButton.setVisibility(View.INVISIBLE);
            h.txtDescripcion.setVisibility(View.GONE);

        }

        //Tipo transaccion 4->Pago Cancelado
        else if (TipoTransaccion.equals("04")) {

            h.txtMonto.setTextColor(Color.parseColor("#E85F00"));
            h.txtMonto.setText("+ " + Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMonto()));
            h.imageButton.setVisibility(View.INVISIBLE);
            h.txtMetodoPago.setVisibility(View.GONE);
            h.txtDescripcion.setVisibility(View.GONE);

        }


        if (!list.get(position).getIdTipoTransaccion().equals("02")) {
            h.txtMetodoPago.setVisibility(View.GONE);
        } else {
            h.txtMetodoPago.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void AddList(List<DetalleCuentaCorriente> list) {
        this.list = list;
        notifyDataSetChanged();

    }

    public interface GetDataCtaCte {

        public void getIdCabeceraVenta(int idCabeceraVenta);

        public void getIdCtaCte(int CtaCte);

    }

    public class DetalleCtaCteVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtFecha, txtTipoTransaccion, txtMetodoPago, txtMonto, txtDescripcion;
        ImageButton imageButton;

        public DetalleCtaCteVH(View itemView) {
            super(itemView);
            txtFecha =  itemView.findViewById(R.id.txtFechaCtaCte);
            txtTipoTransaccion =  itemView.findViewById(R.id.txtTipoTransaccion);
            txtMonto =  itemView.findViewById(R.id.txtMontoTrans);
            txtMetodoPago =  itemView.findViewById(R.id.txtMetodoPagoTrans);
            imageButton = itemView.findViewById(R.id.imgInfoCancel);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            imageButton.setOnClickListener(this);

            txtMetodoPago.setVisibility(View.GONE);
            txtDescripcion.setVisibility(View.GONE);
            imageButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.imgInfoCancel:

                    if (list.get(getAdapterPosition()).getIdTipoTransaccion().equals("01")) {
                        getDataCtaCte.getIdCabeceraVenta(list.get(getAdapterPosition()).getIdCabeceraVenta());
                    } else if (list.get(getAdapterPosition()).getIdTipoTransaccion().equals("02")) {
                        getDataCtaCte.getIdCtaCte(list.get(getAdapterPosition()).getIdCtaCteCliente());
                    }

                    break;
            }

        }
    }
}
