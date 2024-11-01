package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

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
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 03/05/2018.
 */

public class RvAdapterCategoriaP extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mCategoriaProductos> categoriaProductosList;
    ImagenesController imagenesController;
    ListenerSelectCategoria listenerSelectCategoria;
    MenuResources menuResources;
    Context context;
    int pos;
    public interface ListenerSelectCategoria{

        public void setPositionSelect(int positionSelect);
        public void setPositionDelete(int position);
        public void setPositionVisualizar(int position);
        public void setPositionAgregarSubCategorias(int position);

    }

    public void setListenerSelectCategoria(ListenerSelectCategoria listenerSelectCategoria){

        this.listenerSelectCategoria=listenerSelectCategoria;
    }

    public void setContext(Context context){
        if(context!=null) {
            this.context = context;
            menuResources=new MenuResources(context);
        }
    }

    public RvAdapterCategoriaP() {
        categoriaProductosList=new ArrayList<>();
        imagenesController= new ImagenesController();
    }

    private class CategoriaPVH extends  RecyclerView.ViewHolder  {

        TextView txtCategoria;
        ImageView imgCategoria;
        ImageButton btnSetting;

        public CategoriaPVH(View itemView) {
            super(itemView);
            txtCategoria=itemView.findViewById(R.id.txtCategoria);
            imgCategoria=itemView.findViewById(R.id.imgCategoria);
            btnSetting=itemView.findViewById(R.id.btnSetting);

        }



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_item_categoria,parent,false);
        return new CategoriaPVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final CategoriaPVH vh=(CategoriaPVH)holder;
        if( categoriaProductosList.get(position).isEstadoMod()==true) {

                    vh.btnSetting.setVisibility(View.VISIBLE);
        }
        else{
            vh.btnSetting.setVisibility(View.GONE);
          }
        vh.txtCategoria.setText(categoriaProductosList.get(position).getDescripcionCategoria());
        vh.imgCategoria.setImageBitmap(imagenesController.textAsBitmap(categoriaProductosList.get(position).getDescripcionCategoria()));
        vh.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=vh.getAdapterPosition();
                menuResources.OpcionesListadoCategoria(v);
                menuResources.setListenerListadoOpciones(new MenuResources.ListenerListadoOpciones() {
                    @Override
                    public void AgregarSubCategoria() {
                        listenerSelectCategoria.setPositionAgregarSubCategorias(pos);
                    }

                    @Override
                    public void AccionEditar() {
                        listenerSelectCategoria.setPositionSelect(pos);
                    }

                    @Override
                    public void AccionAnular() {
                        listenerSelectCategoria.setPositionDelete(pos);
                    }

                    @Override
                    public void AccionVisualizar() {
                        listenerSelectCategoria.setPositionVisualizar(pos);
                    }
                });
            }
        });
    }

    public void AgregarLista(List<mCategoriaProductos> categoriaProductosList){
        this.categoriaProductosList.clear();
        this.categoriaProductosList.addAll(categoriaProductosList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return categoriaProductosList.size();
    }
}
