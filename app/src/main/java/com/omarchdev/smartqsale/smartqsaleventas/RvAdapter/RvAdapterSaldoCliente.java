package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mSaldoCliente;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 09/01/2018.
 */

public class RvAdapterSaldoCliente extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mSaldoCliente> list;
    ClickItem clickItem;

    public RvAdapterSaldoCliente() {
        list = new ArrayList<>();
    }

    public void setClickItem(ClickItem clickItem) {
        this.clickItem = clickItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saldo, parent, false);
        return new SaldoClienteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SaldoClienteViewHolder h = (SaldoClienteViewHolder) holder;
        h.txtNombre.setText(list.get(position).getNombreCliente());
        h.txtSaldo.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", list.get(position).getMontoSaldo()));


    }

    public void AddList(List<mSaldoCliente> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface ClickItem {
        public void getCliente(int idCliente);
    }

    private class SaldoClienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtNombre;
        TextView txtSaldo;
        ConstraintLayout relativeLayout;

        public SaldoClienteViewHolder(View itemView) {
            super(itemView);

            txtNombre = (TextView) itemView.findViewById(R.id.txtNombreCliente);
            txtSaldo = (TextView) itemView.findViewById(R.id.txtMontoCliente);
            relativeLayout = itemView.findViewById(R.id.rlSaldoCliente);
            relativeLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.rlSaldoCliente) {
                clickItem.getCliente(list.get(getAdapterPosition()).getIdCliente());
            }

        }
    }
}
