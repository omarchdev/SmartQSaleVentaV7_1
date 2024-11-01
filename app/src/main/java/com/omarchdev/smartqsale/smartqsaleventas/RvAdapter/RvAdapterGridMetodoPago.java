package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 20/12/2017.
 */

public class RvAdapterGridMetodoPago extends BaseAdapter {

    List<mMedioPago> list;

    public RvAdapterGridMetodoPago() {
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public mMedioPago getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getiIdMedioPago();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_metodo_pago, parent, false);
        }
        ImageView imagen = (ImageView) convertView.findViewById(R.id.imgMetodoPago);
        TextView txt = (TextView) convertView.findViewById(R.id.txtMetodoPago);
        String uri = "@drawable/" + getItem(position).getIdImagen();
        uri = uri.trim();
        txt.setText(getItem(position).getcDescripcionMedioPago());
        imagen.setImageResource(parent.getContext().getResources().getIdentifier(uri, null, parent.getContext().getPackageName()));

        return convertView;
    }

    public void AddElement(List<mMedioPago> lista) {
        list = lista;
        notifyDataSetChanged();
    }
}
