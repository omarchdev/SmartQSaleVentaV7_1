package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.AdditionalPriceProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 01/10/2017.
 */

public class RVAdapterAdditionalPrice extends  RecyclerView.Adapter<RVAdapterAdditionalPrice.AdditionalPriceViewHolder>{

    private List<AdditionalPriceProduct> additionalPriceProductList=new ArrayList<>();


    @Override
    public AdditionalPriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_additionalprice,parent,false);
        return new AdditionalPriceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdditionalPriceViewHolder holder,int position) {

        holder.tvPrecio.setText(Constantes.SimboloMoneda.moneda+String.format("%.2f",additionalPriceProductList.get(position).getAdditionalPrice()));

    }

    @Override
    public int getItemCount() {
        return additionalPriceProductList.size();
    }


    public class AdditionalPriceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
         TextView tvPrecio;
        ImageButton imgDeletePrice;
        public AdditionalPriceViewHolder(View itemView) {
            super(itemView);

            cv=(CardView)itemView.findViewById(R.id.cvAdditionalPrice);
            tvPrecio=(TextView)itemView.findViewById(R.id.txtAdditionalPrice);
            imgDeletePrice=(ImageButton)itemView.findViewById(R.id.imgbtnDeletePriceAdditional);
            imgDeletePrice.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            removeItem(getAdapterPosition());
        }
    }


    public RVAdapterAdditionalPrice(){

            this.additionalPriceProductList=new ArrayList<>();
    }

    public void AgregarListado(List<AdditionalPriceProduct> list){
        this.additionalPriceProductList.clear();
        this.additionalPriceProductList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(BigDecimal price){
        additionalPriceProductList.add(new AdditionalPriceProduct(0,price,0));
        notifyItemInserted(getItemCount());
    }
    public void removeItem(int position){
        additionalPriceProductList.remove(position);
        notifyItemRemoved(position);
    }
    public List<AdditionalPriceProduct> AdditionalPrice(){

        return additionalPriceProductList;
    }


}
