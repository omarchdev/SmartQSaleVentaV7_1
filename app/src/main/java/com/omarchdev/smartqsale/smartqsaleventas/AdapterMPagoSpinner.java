package com.omarchdev.smartqsale.smartqsaleventas;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 31/01/2018.
 */

public class AdapterMPagoSpinner extends ArrayAdapter<mMedioPago> {


    Context context;
    List<mMedioPago> list;

    public AdapterMPagoSpinner(Context context, int resource) {
        super(context, resource);
        this.context = context;
        list = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public mMedioPago getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView label = new TextView(context);
        label.setPadding(10, 30, 10, 30);
        label.setTextColor(Color.WHITE);
        label.setText(list.get(position).getcDescripcionMedioPago());
        return label;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView label = new TextView(context);
        label.setPadding(10, 30, 10, 30);
        label.setTextColor(Color.WHITE);
        label.setText(list.get(position).getcDescripcionMedioPago());

        return label;
    }

    public void AddElement(List<mMedioPago> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
