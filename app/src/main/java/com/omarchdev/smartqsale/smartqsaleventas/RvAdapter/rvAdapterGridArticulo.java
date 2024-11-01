package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

/**
 * Created by OMAR CHH on 11/12/2017.
 */

public class rvAdapterGridArticulo extends BaseAdapter {

    List<mProduct> list;
    Context context;
    ImagenesController imagenesController;
    byte[] imgData;
    String Uri;
    final String CodeCia= GetJsonCiaTiendaBase64x3();
    public rvAdapterGridArticulo(Context context) {
        list = new ArrayList<>();
        this.context = context;
        imagenesController=new ImagenesController();
        imgData=null;
        Uri="";
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public mProduct getItem(int position) {
        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getIdProduct();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_grid_articulo, parent, false);
        }

        TextView txtCantidadProducto=convertView.findViewById(R.id.txtCantidadProducto);
        ImageView imagenArticulo = (ImageView) convertView.findViewById(R.id.imageArticuloGrid);
        TextView nombreArticulo = (TextView) convertView.findViewById(R.id.txtNombreArticuloGrid);
        imgData = list.get(position).getbImage();
        if(list.get(position).isControlStock()){

            txtCantidadProducto.setText(String.format(String.format("%.1f", list.get(position).getdQuantity()) +
                    "-" + String.format("%.1f",list.get(position).getCantidadReserva())));
            txtCantidadProducto.setVisibility(View.VISIBLE);
        }else {
            txtCantidadProducto.setVisibility(View.INVISIBLE);

        }
        if(list.get(position).getTipoRepresentacionImagen()==2){
            imagenArticulo.clearColorFilter();
            if (imgData != null) {
                 Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                imagenArticulo.setPadding(0, 0, 0, 0);

                imagenArticulo.setImageBitmap(bmp);

                }
                else{

                    Uri="@drawable/"+getItem(position).getCodigoForma();
                    Uri=Uri.trim();
                    imagenArticulo.setImageResource(parent.getContext().getResources().getIdentifier(Uri,null,parent.getContext().getPackageName()));

                }
            Picasso.get()
                    .load(BASE_URL_API+"api/producto/GetImageProduct?codeCia="+CodeCia+"&tipoConsulta=2&idProduct=" +list.get(position).idProduct)
                    .placeholder(context.getResources().getIdentifier(Uri, null, context.getPackageName()))
                    .error(R.drawable.circle_full_error)
                    .into(imagenArticulo);

        }
        else  if(list.get(position).getTipoRepresentacionImagen()==1){

            Uri="@drawable/"+getItem(position).getCodigoForma();
            Uri=Uri.trim();
            imagenArticulo.setImageResource(parent.getContext().getResources().getIdentifier(Uri,null,parent.getContext().getPackageName()));
            imagenArticulo.setColorFilter(Color.parseColor(getItem(position).getCodigoColor()));

        }
        //  int i=2131165274;
        // imagenArticulo.setImageResource(i);
            nombreArticulo.setText(getItem(position).getProductNamePrecioPedido());


        return convertView;
    }

    public void limpiarLista(){
        list.clear();
        notifyDataSetChanged();
    }
    public void AddElement(List<mProduct> lista) {
        list = lista;
        notifyDataSetChanged();

    }


}
