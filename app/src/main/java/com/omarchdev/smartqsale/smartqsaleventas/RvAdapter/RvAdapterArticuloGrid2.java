package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorString;
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

public class RvAdapterArticuloGrid2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    ListenerRvArticuloGrid listenerRvArticuloGrid;
    ControladorString controladorString;
    private String NombreProducto;
    final String CodeCia= GetJsonCiaTiendaBase64x3();
    public void setListenerRvArticuloGrid(ListenerRvArticuloGrid listenerRvArticuloGrid){
        this.listenerRvArticuloGrid=listenerRvArticuloGrid;
    }
    public interface ListenerRvArticuloGrid{
        public void PositionItem(int position);
    }
    List<mProduct> list;
    Context context;
    ImagenesController imagenesController;
    byte[] imgData;
    String Uri;
    Bitmap bmp;


    public RvAdapterArticuloGrid2(Context context) {
        controladorString=new ControladorString();
        this.context = context;
        list = new ArrayList<>();
        this.context = context;
        imagenesController=new ImagenesController();
        imgData=null;
        Uri="";
        bmp=null;
        NombreProducto="";
    }

    private class ItemVHArticulo extends RecyclerView.ViewHolder{

        TextView txtCantidadProducto;
        ImageView imagenArticulo;
        TextView nombreArticulo;
        public ItemVHArticulo(View itemView) {
            super(itemView);

             txtCantidadProducto=itemView.findViewById(R.id.txtCantidadProducto);
             imagenArticulo = (ImageView)itemView.findViewById(R.id.imageArticuloGrid);
             nombreArticulo = (TextView) itemView.findViewById(R.id.txtNombreArticuloGrid);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_grid_articulo,parent,false);
        return new ItemVHArticulo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final ItemVHArticulo h=(ItemVHArticulo)holder;
        imgData = list.get(position).getbImage();
        ViewGroup.LayoutParams params = h.nombreArticulo.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        h.nombreArticulo.setLayoutParams(params);
        if(list.get(position).isControlStock() && !list.get(position).isEstadoVariante()){

            h.txtCantidadProducto.setText(String.format(String.format("%.1f", list.get(position).getdQuantity()) +
                    "-" + String.format("%.1f",list.get(position).getCantidadReserva()))+"="+String.format(
                            "%.1f",list.get(position).getdQuantity()-list.get(position).getCantidadReserva()

            ));
            h.txtCantidadProducto.setVisibility(View.VISIBLE);
        }else {
            h.txtCantidadProducto.setVisibility(View.INVISIBLE);

        }

        if(list.get(position).getTipoRepresentacionImagen()==2){
            h.imagenArticulo.clearColorFilter();
            if (imgData != null) {
              bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                h.imagenArticulo.setPadding(0, 0, 0, 0);
                h.imagenArticulo.setImageBitmap(bmp);
            }
            else{
                Uri="@drawable/"+list.get(position).getCodigoForma();
                Uri=Uri.trim();
                h.imagenArticulo.setImageResource(context.getResources().getIdentifier(Uri,null,context.getPackageName()));
            }
            Picasso.get()
                    .load(BASE_URL_API+"api/producto/GetImageProduct?codeCia="+CodeCia+"&tipoConsulta=2&idProduct=" +list.get(position).idProduct)
                    .placeholder(context.getResources().getIdentifier(Uri, null, context.getPackageName()))
                    .error(R.drawable.circle_full_error)
                    .into(  h.imagenArticulo);
        }   else  if(list.get(position).getTipoRepresentacionImagen()==1){

            Uri="@drawable/"+list.get(position).getCodigoForma();
            Uri=Uri.trim();
            h.imagenArticulo.setImageResource(context.getResources().getIdentifier(Uri,null,context.getPackageName()));
            h.imagenArticulo.setColorFilter(Color.parseColor(list.get(position).getCodigoColor()));

        }

        if( !list.get(position).isEstadoVariante()) {

            if(Constantes.ConfigTienda.nombreConCategoria) {
                if(list.get(position).getIdSubCategoria()==0){
                    NombreProducto=String.valueOf(  controladorString.EliminarPrefijo("0",list.get(position).getcKey())+"/"
                            +list.get(position).getDescripcionCategoria().trim()
                            + "/" + list.get(position).getcProductName().trim() + "\n" +
                            Constantes.DivisaPorDefecto.SimboloDivisa +
                            String.format("%.2f", list.get(position).getPrecioVenta())).trim();

                    h.nombreArticulo.setText(NombreProducto);
                }else{
                    NombreProducto=String.valueOf( controladorString.EliminarPrefijo("0",list.get(position).getcKey())+"/"
                                    +list.get(position).getDescripcionCategoria().trim()+"/"+
                            list.get(position).getDescripcionSubCategoria().trim()+"/"+
                            list.get(position).getcProductName().trim() + "\n" +
                            Constantes.DivisaPorDefecto.SimboloDivisa +
                            String.format("%.2f", list.get(position).getPrecioVenta())).trim();
                    h.nombreArticulo.setText(NombreProducto);
                }
            }
            else{
                NombreProducto=String.valueOf(controladorString.EliminarPrefijo("0",list.get(position).getcKey())+"/"+list.get(position).getcProductName()
                        + "\n" +
                        Constantes.DivisaPorDefecto.SimboloDivisa +
                        String.format("%.2f", list.get(position).getPrecioVenta())).trim();
                h.nombreArticulo.setText(NombreProducto);
            }
        }else{
            if(Constantes.ConfigTienda.nombreConCategoria) {
                if(list.get(position).getIdSubCategoria()==0) {
                    NombreProducto=(controladorString.EliminarPrefijo("0",list.get(position).getcKey())+
                            "/"+list.get(position).getDescripcionCategoria().trim()
                            +"/"  + list.get(position).getcProductName()).trim();
                    h.nombreArticulo.setText(NombreProducto);
                }else{
                    NombreProducto=(controladorString.EliminarPrefijo("0",list.get(position).getcKey())
                            +"/"+list.get(position).getDescripcionCategoria()+"/"+
                            list.get(position).getDescripcionSubCategoria().trim()+"/"+
                             list.get(position).getcProductName()).trim();

                    h.nombreArticulo.setText(NombreProducto);
                }
            }else{
                h.nombreArticulo.setText( controladorString.EliminarPrefijo("0",list.get(position).getcKey())+"/"
                        +list.get(position).getcProductName().trim());
            }
        }
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenerRvArticuloGrid!=null) {
                    listenerRvArticuloGrid.PositionItem(h.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void limpiarLista(){
        list.clear();
        notifyDataSetChanged();
    }

    public void AddElement(List<mProduct> lista) {
        list = lista;
        notifyDataSetChanged();

    }

    public void ActualizarCantidadReserva(int position,float cantidad,float stockActual){

        try {
            list.get(position).setdQuantity(stockActual);
            list.get(position).setCantidadReserva(cantidad);
            notifyItemChanged(position);
        }catch (Exception e){
            e.toString();
        }
    }
}
