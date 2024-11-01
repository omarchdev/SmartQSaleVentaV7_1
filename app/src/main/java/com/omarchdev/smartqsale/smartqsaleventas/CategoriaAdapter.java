package com.omarchdev.smartqsale.smartqsaleventas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by OMAR CHH on 21/01/2018.
 */

public class CategoriaAdapter extends ArrayAdapter<mCategoriaProductos> {


    Context context;
    List<mCategoriaProductos> list;
    List<mCategoriaProductos> listaTemporal;
    mCategoriaProductos categoriaProductosTemp;

    public CategoriaAdapter(@NonNull Context context, int resource, List<mCategoriaProductos> list) {
        super(context, resource, list);
        this.context = context;
        listaTemporal=new ArrayList<>();
        listaTemporal.add(new mCategoriaProductos(-1,"Todos los productos"));
        listaTemporal.add(new mCategoriaProductos(0,"Favoritos"));
        categoriaProductosTemp=new mCategoriaProductos(0,"Todos los productos");
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public mCategoriaProductos getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView label = new TextView(context);
        label.setPadding(10, 30, 10, 30);
        label.setText(list.get(position).getDescripcionCategoria());

        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView label = new TextView(context);
        label.setPadding(10, 30, 10, 30);

        label.setText(list.get(position).getDescripcionCategoria());

        return label;
    }
    public void AddElementSpinnerVentas(List<mCategoriaProductos> list){
        this.list.clear();
        this.list.addAll(listaTemporal);
        this.list.addAll(list);
        notifyDataSetChanged();

    }

    public void AddElementRegistroProductos(List<mCategoriaProductos> list){
        this.list.addAll(list);
        notifyDataSetChanged();
}

    public void AddElementConfiguracionPack(List<mCategoriaProductos> list){
        this.list=list;
        this.list.add(0,categoriaProductosTemp);

        notifyDataSetChanged();
    }
}
