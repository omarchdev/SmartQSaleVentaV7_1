package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 25/10/2017.
 */

public class RvAdapterCustomer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ClienteListener listener;
    private List<mCustomer> customerList=new ArrayList<>();

    public void setListener(ClienteListener listener) {

        this.listener = listener;

    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_customer,parent,false);
        context=parent.getContext();
        return new CustomerViewHolder(v) ;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomerViewHolder customerViewHolder = (CustomerViewHolder) holder;
        customerViewHolder.tvName.setText(customerList.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void Add(List<mCustomer> list) {
        customerList = list;
        notifyDataSetChanged();
    }

    public interface ClienteListener {

        public void obtenerDatosClienteSeleccionado(mCustomer customer);
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        private TextView tvName;
        private TextView tvAlias;
        private ImageButton btnInformation;

        public CustomerViewHolder(View itemView) {

            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.cvCustomer);
            tvName=(TextView) itemView.findViewById(R.id.txtcvNameCustomer);
            tvAlias=(TextView)itemView.findViewById(R.id.txtcvAliasCustomer);
            btnInformation = (ImageButton) itemView.findViewById(R.id.btnInformacionCliente);
            btnInformation.setOnClickListener(this);
            tvName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.btnInformacionCliente) {
            } else if (v.getId() == R.id.txtcvNameCustomer) {
                listener.obtenerDatosClienteSeleccionado(customerList.get(getAdapterPosition()));
            }

        }
    }
}
