package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Controlador.MenuResources;
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterClientes;

import java.util.ArrayList;
import java.util.List;

public class RvAdapterVendedores extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mVendedor> vendedorList;
    ImagenesController imagenesController;
    RvAdapterClientes.ListenerPosition listenerPosition;
    MenuResources menuResources;
    Context context;
    int pos;
    public void setListenerPosition(RvAdapterClientes.ListenerPosition listenerPosition){
        this.listenerPosition=listenerPosition;
    }



    public RvAdapterVendedores() {
        imagenesController=new ImagenesController();
        vendedorList=new ArrayList<>();
    }
    public void setContext(Context context){

        if(context!=null){
            this.context=context;
            menuResources=new MenuResources(context);
        }
    }
    private class VendedorVH extends RecyclerView.ViewHolder{

        ImageView imgCliente;
        TextView txtNombreCliente,txtNumDocumento;
        ImageButton btnSetting,btnEdit;

        public VendedorVH(View itemView) {
            super(itemView);
            imgCliente=itemView.findViewById(R.id.imgCliente);
            txtNombreCliente=itemView.findViewById(R.id.txtNombreCliente);
            btnSetting=itemView.findViewById(R.id.btnSetting);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            txtNumDocumento=itemView.findViewById(R.id.txtNumDocumento);
            /*
            btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerPosition!=null){
                       listenerPosition.ObtenerPosicion(getAdapterPosition());
                    }
                }
            });*/
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_cliente,parent,false);
        return new VendedorVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final VendedorVH vh=(VendedorVH)holder;
        vh.txtNumDocumento.setVisibility(View.GONE);
        vh.btnEdit.setVisibility(View.GONE);
        vh.imgCliente.setImageBitmap(imagenesController.textAsBitmap(vendedorList.get(position).getPrimerNombre()));
        vh.txtNombreCliente.setText(vendedorList.get(position).getPrimerNombre()+" "+vendedorList.get(position).getApellidoPaterno());
        vh.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=vh.getAdapterPosition();
                if(context!=null) {
                    menuResources.OpcionesListado(v);
                    menuResources.setListenerListadoOpciones(new MenuResources.ListenerListadoOpciones() {
                        @Override
                        public void AgregarSubCategoria() {

                        }

                        @Override
                        public void AccionEditar() {
                            listenerPosition.ObtenerPosicion(pos);

                        }

                        @Override
                        public void AccionAnular() {
                            listenerPosition.ObtenerPosAnular(pos);
                        }

                        @Override
                        public void AccionVisualizar() {
                            listenerPosition.ObtenerPosVisualizar(pos);
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendedorList.size();
    }

    public void setVendedorList(List<mVendedor> mVendedors){

        this.vendedorList=mVendedors;
        notifyDataSetChanged();

    }
}
