package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * Created by OMAR CHH on 26/11/2017.
 */

public class RvAdapterProvider extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    class ProviderDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView txtNameProvider,txtNameCompany;

        public ProviderDetailViewHolder(View itemView) {
            super(itemView);

            cv=(CardView)itemView.findViewById(R.id.cv_provider);
            txtNameCompany=(TextView)itemView.findViewById(R.id.txtProviderCompany);
            txtNameProvider=(TextView)itemView.findViewById(R.id.txtProviderName);
            cv.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.cv_provider){

            }

        }

    }

    public void AddProvider(){
        notifyDataSetChanged();
    }
    public void RemoveProvider(int position){

        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ProviderDetailViewHolder providerDetailViewHolder=(ProviderDetailViewHolder)holder;
        providerDetailViewHolder.txtNameProvider.setText("");
        providerDetailViewHolder.txtNameCompany.setText("");
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
