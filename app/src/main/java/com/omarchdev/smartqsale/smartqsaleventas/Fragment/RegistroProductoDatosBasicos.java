package com.omarchdev.smartqsale.smartqsaleventas.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroProductoDatosBasicos extends Fragment implements View.OnClickListener {

    Switch sEstadoProducto;

    public RegistroProductoDatosBasicos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_registro_producto_datos_basicos, container, false);

        sEstadoProducto=(Switch)v.findViewById(R.id.switchEstado);
        sEstadoProducto.setOnClickListener(this);


        estadoSwitch();
        return v;
    }

    public void estadoSwitch(){


            sEstadoProducto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.getText();
                    if(isChecked){
                        Toast.makeText(getContext(),"Hola",Toast.LENGTH_LONG).show();
                        sEstadoProducto.setText("Hola");
                        //sEstadoProducto.setTextOff(getString(R.string.estado_producto_cabecera)+":"+getString(R.string.estado_activo));
                        //sEstadoProducto.setTextOn(getString(R.string.estado_producto_cabecera)+":"+getString(R.string.estado_inactivo));

                    }
                    else{
                        sEstadoProducto.setText(getString(R.string.estado_producto_cabecera)+":"+getString(R.string.estado_inactivo));
                    }
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.switchEstado:

                if(sEstadoProducto.isChecked()){
                    sEstadoProducto.setTextOff(getString(R.string.estado_producto_cabecera)+":"+getString(R.string.estado_activo));

                }
                else {
                    sEstadoProducto.setText(getString(R.string.estado_producto_cabecera)+":"+getString(R.string.estado_inactivo));
                }

                break;

        }

    }
}
