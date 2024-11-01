package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 23/02/2018.
 */

public class RvGridAdapterCategoria extends BaseAdapter{
    List<mCategoriaProductos> list;
    List<mCategoriaProductos> listaOrigen;
    ImagenesController imgController;


    public RvGridAdapterCategoria() {
        list=new ArrayList<>();
        listaOrigen=new ArrayList<>();
        listaOrigen.add(new mCategoriaProductos(-1,"Todos los productos"));
        listaOrigen.add(new mCategoriaProductos(0,"Favoritos"));
        imgController= new ImagenesController();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public mCategoriaProductos getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getIdCategoria();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view ==null){

            LayoutInflater inflater=(LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_grid_categoria, viewGroup, false);

        }
        ImageView imagenCategoria=(ImageView) view.findViewById(R.id.imgCategoria);
        TextView txtCategoria=(TextView) view.findViewById(R.id.txtNombreCategoriaGrid);
        switch (i){

            case 0:
                imagenCategoria.setImageResource(viewGroup.getContext().getResources().getIdentifier("@drawable/shopping",null,viewGroup.getContext().getPackageName()));
                txtCategoria.setText(list.get(i).getDescripcionCategoria());

                break;

            case 1:
                imagenCategoria.setImageResource(viewGroup.getContext().getResources().getIdentifier("@drawable/favorite_outline",null,viewGroup.getContext().getPackageName()));
                txtCategoria.setText(list.get(i).getDescripcionCategoria());

                break;
            default:
                imagenCategoria.setImageBitmap(imgController.textAsBitmap(list.get(i).getDescripcionCategoria()));
                txtCategoria.setText(list.get(i).getDescripcionCategoria());
                 break;


        }

        return view;
    }
    public void AddElement(List<mCategoriaProductos> list){

        this.list.clear();
        this.list.addAll(listaOrigen);
        this.list.addAll(list);
        notifyDataSetChanged();

    }
}
