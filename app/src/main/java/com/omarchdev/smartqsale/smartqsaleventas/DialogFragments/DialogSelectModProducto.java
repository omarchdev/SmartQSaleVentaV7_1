package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;


import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncModificadores;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleModificador;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Modificador;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 24/04/2018.
 */

public class DialogSelectModProducto extends DialogFragment implements AsyncModificadores.ListenerModProductoVenta {

    LinearLayout llContent;
    int idProducto;
    AVLoadingIndicatorView pbIndicator;
    AsyncModificadores asyncModificadores;
    List<Modificador> modificadorList;
    RecyclerView rvModProduct;
    AdapterModProducto adapterModProducto;
    List<AdapterModProducto> adapterModProductoList;
    View v;
    String modificadores;
    String nombreProducto;
    ListenerModProdSeleccion listenerModProdSeleccion;
    TextView txtTitulo,txtCargando,txtMensaje;
    int idPventa;
    EditText edtModificador;
    float cantidad;


    public void setIdPventaCantidad(int idPventa,float cantidad){
        this.idPventa=idPventa;
        this.cantidad=cantidad;
    }


    public interface ListenerModProdSeleccion{

        public void obtenerModificadorProducto(int idProducto,String modificador,int idPventa,float cantidad);
    }
    public void setIdProduct(int idProducto,String nombreProducto){

        this.idProducto=idProducto;
        this.nombreProducto=nombreProducto;


    }

    @Override
    public void onStart() {

        super.onStart();
        if(getDialog()!=null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }



    public void setListenerModProdSeleccion(ListenerModProdSeleccion listenerModProdSeleccion){
        this.listenerModProdSeleccion=listenerModProdSeleccion;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View vTitulo=getActivity().getLayoutInflater().inflate(R.layout.custom_title_dialog,null);
        v=getActivity().getLayoutInflater().inflate(R.layout.dialog_select__mod_producto,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        txtCargando=v.findViewById(R.id.txtCargando);
        pbIndicator=v.findViewById(R.id.pbIndicator);
        txtTitulo=vTitulo.findViewById(R.id.txtTitulo);
        txtTitulo.setText(nombreProducto);
        this.edtModificador = v.findViewById(R.id.edtModificador);
        llContent=v.findViewById(R.id.llContent);
        asyncModificadores=new AsyncModificadores();
        asyncModificadores.setListenerModProductoVenta(this);
        asyncModificadores.ObtenerModificadoresProductosVenta(idProducto);
        adapterModProductoList=new ArrayList<>();
        modificadores="";
        edtModificador.setMaxLines(4);
        txtMensaje=v.findViewById(R.id.txtMensaje);
        txtMensaje.setVisibility(View.GONE);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modificadores="";
                if(edtModificador.getText().toString().trim().length()>0){
                    modificadores=edtModificador.getText().toString().trim()+"\n";
                }

                if(modificadorList!=null) {
                    for (int i = 0; i < adapterModProductoList.size(); i++) {
                        if (adapterModProductoList.get(i).getPos() >= 0) {

                            modificadores = modificadores + modificadorList.get(i).getDescripcion() + ":" +
                            modificadorList.get(i).getDetalleModificadorList().get(
                                    adapterModProductoList.get(i).getPos()).
                                            getDescripcionModificador() + "/\n";
                        }
                    }
                    if(modificadores.length()>0) {
                        listenerModProdSeleccion.obtenerModificadorProducto(idProducto, modificadores,idPventa,cantidad);
                    }
                }
            }
        });
            builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               asyncModificadores.CancelarBusquedaModificadoresProducto();
            }
        });
        builder.setCustomTitle(vTitulo);
        builder.setView(v);
        pbIndicator.show();
            txtCargando.setVisibility(View.VISIBLE);
        return builder.create();



    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        asyncModificadores.CancelarBusquedaModificadoresProducto();
    }

    @Override
    public void ResultadosModificadoresProducto(List<Modificador> modificadorList) {
        pbIndicator.hide();
        txtCargando.setVisibility(View.INVISIBLE);
        if(modificadorList!=null) {
            if(modificadorList.size()>0) {
                this.modificadorList = modificadorList;

                for (int i = 0; i < this.modificadorList.size(); i++) {
                    txtTitulo = new TextView(getActivity());
                    txtTitulo.setText(this.modificadorList.get(i).getDescripcion());
                    txtTitulo.setPadding(12, 6, 12, 6);
                    txtTitulo.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    txtTitulo.setTextSize(16);
                    llContent.addView(txtTitulo);
                    adapterModProducto = new AdapterModProducto(modificadorList.get(i).getDetalleModificadorList(), i);
                    adapterModProductoList.add(adapterModProducto);
                    rvModProduct = new RecyclerView(getActivity());
                    rvModProduct.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    rvModProduct.setAdapter(adapterModProducto);
                    llContent.addView(rvModProduct);

                }
            }else{
                txtMensaje.setVisibility(View.VISIBLE)  ;
            }
        }
        else{
            getDialog().dismiss();
        }
    }


    private class AdapterModProducto extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<DetalleModificador> detalleModificadorList;
        int id;
        int pos;
        int oldPos;
        boolean click;
        boolean clickEliminar;

        public AdapterModProducto(List<DetalleModificador> detalleModificadorList, int idPosition) {
            this.detalleModificadorList =new ArrayList<>();
            this.detalleModificadorList.addAll(detalleModificadorList);
            this.id = idPosition;
            pos=-10;
            oldPos=-10;
            click=false;
            clickEliminar=false;
        }

        private class ItemModProdVH extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView txtDescModProducto;
            public ItemModProdVH(View itemView) {

                super(itemView);
                txtDescModProducto=itemView.findViewById(R.id.txtDescModProducto);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(v.getId()==itemView.getId()){
                 if(getAdapterPosition()==oldPos ){

                        oldPos=-10;
                        clickEliminar=true;
                        pos=getAdapterPosition();
                        EliminarSeleccion(pos);


                 }
                 else if(getAdapterPosition()!=oldPos){


                    pos = getAdapterPosition();
                    EliminarSeleccion(oldPos);
                    SeleccionarElemento(pos);
                    oldPos=pos;
                }

            }
            }
        }
        public void SeleccionarElemento(int pos){
            notifyItemChanged(pos);
        }
        public void EliminarSeleccion(int pos){
            notifyItemChanged(pos);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v=getLayoutInflater().inflate(R.layout.cv_item_mod_product,parent,false);
            return new ItemModProdVH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ItemModProdVH vh=(ItemModProdVH)holder;
            vh.txtDescModProducto.setText(detalleModificadorList.get(position).getDescripcionModificador());
            vh.txtDescModProducto.setTypeface(vh.txtDescModProducto.getTypeface(), Typeface.NORMAL);

            if(clickEliminar==true) {
                if(position==pos) {
                    vh.itemView.setBackground(getResources().getDrawable(R.drawable.fondo_mod_prod));
                    vh.txtDescModProducto.setTextColor(Color.BLACK);
                    clickEliminar = false;
                    pos = -10;
                }
            }
            else if( clickEliminar==false){
                if(position==pos){
                   vh.itemView.setBackground(getResources().getDrawable(R.drawable.fondo_mod_prod_on));
                   vh.txtDescModProducto.setTextColor(Color.WHITE);
                }
                else{
                   vh.itemView.setBackground(getResources().getDrawable(R.drawable.fondo_mod_prod));
                   vh.txtDescModProducto.setTextColor(Color.BLACK);
                }
            }

        }

        @Override
        public int getItemCount() {
            return detalleModificadorList.size();
        }

        public int getPos() {
            return pos;
        }
    }
}
