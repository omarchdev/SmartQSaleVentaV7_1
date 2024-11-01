package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.ClickListener;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.MenuResources;
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

public class RvAdapterClientes extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mCustomer> list;
    ImagenesController imgController;
    ListenerPosition listenerPosition;
    Context context;
    SelectClienteListener selectClienteListener;
    boolean visibilitySettings;
    MenuResources menuResources;
    ClickListener clickListener;
    int pos;
    byte p=100;
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setVisibleSettings(boolean visibleSettings){
        this.visibilitySettings=visibleSettings;
    }
    public interface SelectClienteListener{

        public void ClienteSeleccionado(mCustomer cliente);

    }

    public void setSelectClienteListener(SelectClienteListener selectClienteListener){

        this.selectClienteListener=selectClienteListener;

    }

    public void setContext(Context context){

        if(context!=null){
            menuResources=new MenuResources(context);
        }

    }

    public void setListenerPosition(ListenerPosition listenerPosition){
        this.listenerPosition=listenerPosition;
    }

    public interface ListenerPosition{

        public void ObtenerPosicion(int position);
        public void ObtenerPosVisualizar(int position);
        public void ObtenerPosAnular(int position);


    }
    public RvAdapterClientes() {
        list=new ArrayList<>();
        imgController=new ImagenesController();
        context=null;
        visibilitySettings=true;
        pos=0;
    }
    public void setVisibilitySettings(boolean visibilitySettings){
        this.visibilitySettings=visibilitySettings;
    }

    private class ClienteVH extends RecyclerView.ViewHolder{



        ImageView  imgCliente;
        TextView txtNombreCliente,txtEmailCliente,txtNumDocumento;
        ImageButton btnSetting,btnEdit;

        public ClienteVH(View itemView) {
            super(itemView);
            imgCliente=itemView.findViewById(R.id.imgCliente);

            txtNombreCliente=itemView.findViewById(R.id.txtNombreCliente);
            txtEmailCliente=itemView.findViewById(R.id.txtEmailCliente);
            btnSetting=itemView.findViewById(R.id.btnSetting);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            txtNumDocumento=itemView.findViewById(R.id.txtNumDocumento);
/*            btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenerPosition!=null) {
                        listenerPosition.ObtenerPosicion(getAdapterPosition());
                    }
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectClienteListener!=null) {
                        selectClienteListener.ClienteSeleccionado(list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_cliente,parent,false);
        context=parent.getContext();
         return new ClienteVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ClienteVH h=(ClienteVH)holder;
        if(list.get(position).getTipoCliente()==context.getResources().getInteger(R.integer.ValorPersonaNatural)) {
            h.txtNombreCliente.setText(list.get(position).getcName() + " " + list.get(position).getcApellidoPaterno());

            h.imgCliente.setImageBitmap(imgController.textAsBitmap(list.get(position).getcName()));
        }else if(list.get(position).getTipoCliente()==context.getResources().getInteger(R.integer.ValorPersonaJuridica)){
            h.txtNombreCliente.setText(list.get(position).getRazonSocial());
            h.imgCliente.setImageBitmap(imgController.textAsBitmap(list.get(position).getRazonSocial()));

        }
        h.txtNumDocumento.setTextColor(Color.parseColor
                (list.get(position).getTipoDocumento().getCColorDescripcion().trim()));
        h.txtNumDocumento.setText(list.get(position).getTipoDocumento().getCDescripcionCorta()+"\n"+
                list.get(position).getNumeroRuc());

        if(visibilitySettings){
            h.btnEdit.setVisibility(View.INVISIBLE);
            h.btnSetting.setVisibility(View.VISIBLE);
            h.btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos=h.getAdapterPosition();
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
            });
        }else {
            h.btnSetting.setVisibility(View.INVISIBLE);

            h.btnEdit.setVisibility(View.VISIBLE);
            h.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener!=null){

                        clickListener.clickPositionOption(h.getAdapterPosition(),p);


                    }
                }
            });
        }


        if(!list.get(position).getControl1().trim().isEmpty()){

            h.txtNumDocumento.setText(list.get(position).getControl1().trim());

        }
       // h.txtEmailCliente.setText(list.get(position).getcEmail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void AgregarListado(List<mCustomer> list){

        this.list=list;
        notifyDataSetChanged();

    }
}
