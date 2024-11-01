package com.omarchdev.smartqsale.smartqsaleventas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;

import java.util.List;

/**
 * Created by OMAR CHH on 16/01/2018.
 */

public class MedioPagoSpinnerAdapter extends ArrayAdapter<mMedioPago> {

    List<mMedioPago> list = null;
    private Context context = null;

    public MedioPagoSpinnerAdapter(Context context, List<mMedioPago> list) {
        super(context, R.layout.spinner_adapter_medio_pago, list);

        this.context = context;
        this.list = list;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_adapter_medio_pago, null);
        }
        String uri = "@drawable/" + list.get(position).getIdImagen();
        uri = uri.trim();
        ((TextView) convertView.findViewById(R.id.txtNombreMedioPago)).setText(list.get(position).getcDescripcionMedioPago());
        ((ImageView) convertView.findViewById(R.id.imgMetodoPago)).setImageResource(parent.getContext().getResources().getIdentifier(uri, null, parent.getContext().getPackageName()));


        return convertView;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.spinner_list_medio_pago, parent, false);

        }
        if (row.getTag() == null) {
            MedioPagoHolder medioPagoHolder = new MedioPagoHolder();
            medioPagoHolder.setIcono((ImageView) row.findViewById(R.id.icono));
            medioPagoHolder.setTextView((TextView) row.findViewById(R.id.texto));
            row.setTag(medioPagoHolder);

        }

        mMedioPago medioPago = list.get(position);
        String uri = "@drawable/" + medioPago.getIdImagen();
        uri = uri.trim();
        ((MedioPagoHolder) row.getTag()).getIcono().setImageResource(parent.getContext().getResources().getIdentifier(uri, null, parent.getContext().getPackageName()));
        ((MedioPagoHolder) row.getTag()).getTextView().setText(medioPago.getcDescripcionMedioPago());
        return row;
    }

    private static class MedioPagoHolder {

        private ImageView icono;
        private TextView textView;

        public ImageView getIcono() {
            return icono;
        }

        public void setIcono(ImageView icono) {
            this.icono = icono;
        }

        public TextView getTextView() {

            return textView;

        }

        public void setTextView(TextView textView) {

            this.textView = textView;

        }
    }

}
