package com.omarchdev.smartqsale.smartqsaleventas.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.TipoProducto;


public class ConfigEstadoProductFragment extends Fragment {

    boolean controlPeso,controlStock,tipoPack,pVentaLibre,bVisibleWeb;
    String estadoProducto,estadoVisible;
     Switch sEstadoProducto,sEstadoProductoVisible,sControlPeso,sControlStock,switchPVLibre,switchTiempo,switchVisibleVentaWeb;
     Spinner spTipoProducto;



     public void BloquearBotones(){

         sEstadoProducto.setEnabled(false);
         sEstadoProductoVisible.setEnabled(false);
         sControlPeso.setEnabled(false);
         sControlStock.setEnabled(false);
         spTipoProducto.setEnabled(false);
         switchPVLibre.setEnabled(false);
         switchTiempo.setEnabled(false);
         switchVisibleVentaWeb.setEnabled(false);
     }

     public void DesbloquearBotones(){
         switchTiempo.setEnabled(true);
         sEstadoProducto.setEnabled(true);
         sEstadoProductoVisible.setEnabled(true);
         sControlPeso.setEnabled(true);
         sControlStock.setEnabled(true);
         spTipoProducto.setEnabled(true);
         switchPVLibre.setEnabled(true);
         switchVisibleVentaWeb.setEnabled(true);
     }

    public ConfigEstadoProductFragment(){

        estadoProducto="";
        estadoVisible="";
        controlPeso=false;
        controlStock=false;
        pVentaLibre=false;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_config_estado_product, container, false);

        switchPVLibre=v.findViewById(R.id.switchPVLibre);
        switchTiempo=v.findViewById(R.id.switchTiempo);
        spTipoProducto=v.findViewById(R.id.spTipoProducto);
        switchVisibleVentaWeb=v.findViewById(R.id.switchVisibleVentaWeb);
        sControlPeso=v.findViewById(R.id.switchControlPorPeso);
        sControlStock=v.findViewById(R.id.switchControlStock);;
        sEstadoProducto=v.findViewById(R.id.switchEstado);
        sEstadoProductoVisible=v.findViewById(R.id.switchEstadoVisible);
        spTipoProducto.setAdapter(new ArrayAdapter<TipoProducto>(getContext(),
                android.R.layout.simple_list_item_1,TipoProducto.values() ));

        spTipoProducto.setOnItemSelectedListener(itemSelectedListener);
        estadoProducto=getString(R.string.EstadoProductoActivo);
        estadoVisible=getString(R.string.EstadoProductoVisible);

        estadoSwitch();
        return v;


    }
    Spinner.OnItemSelectedListener itemSelectedListener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if(i==1){
                tipoPack=true;
                controlPeso=false;
                controlStock=false;
                pVentaLibre=false;
                switchTiempo.setChecked(false);
                sControlPeso.setChecked(false);
                sControlStock.setChecked(false);
                sControlStock.setVisibility(View.GONE);
                sControlPeso.setVisibility(View.GONE);
                switchPVLibre.setVisibility(View.GONE);
                switchTiempo.setVisibility(View.GONE);
            }
            else if(i==0){
                tipoPack=false;
                controlPeso=false;
                controlStock=false;
                pVentaLibre=false;
                sControlPeso.setChecked(false);
                sControlStock.setChecked(false);

                switchTiempo.setVisibility(View.VISIBLE);
                sControlStock.setVisibility(View.VISIBLE);
                sControlPeso.setVisibility(View.VISIBLE);
                switchPVLibre.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public boolean getVisibleWeb(){return bVisibleWeb;}
    public boolean getControlPeso(){
        return controlPeso;
    }
    public boolean getControlStock(){
        return controlStock;
    }
    public String getEstadoProducto(){
        return estadoProducto;
    }
    public String getEstadoVisible(){
        return estadoVisible;
    }
    public boolean getpVentaLibre(){
        return pVentaLibre;
    }
    public boolean getControlTiempo(){ return switchTiempo.isChecked();}
    public boolean isTipoPack(){
        return tipoPack;
    }
    public void setInfoProduct(mProduct product){
        bVisibleWeb=product.isbVisibleWeb();
        estadoVisible=product.getEstadoVisible();
        controlPeso=product.isControlPeso();
        controlStock=product.isControlStock();
        pVentaLibre=product.ispVentaLibre();
        tipoPack=product.isTipoPack();
        sControlPeso.setChecked(controlPeso);
        sControlStock.setChecked(controlStock);
        switchPVLibre.setChecked(pVentaLibre);
        switchTiempo.setChecked(product.isbControlTiempo());

        if(product.isEstadoVariante()){
            spTipoProducto.setOnItemSelectedListener(null);
            tipoPack=false;
            spTipoProducto.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Toast.makeText(getContext(),"No puede usar esta seccion.Debe desactivar la opcion variante del articulo",Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
        else{
            spTipoProducto.setOnItemSelectedListener(itemSelectedListener);
        }
        if(tipoPack==true){
            spTipoProducto.setSelection(1);

        }
        else if(tipoPack==false){

            spTipoProducto.setSelection(0);
        }

        switch(estadoProducto){

            case "A":
                sEstadoProducto.setChecked(true);
                break;

            case "I":

                sEstadoProducto.setChecked(false);
                break;
        }
        switch (estadoVisible){

            case"V":
                sEstadoProductoVisible.setChecked(true);
                break;

            case "I":
                sEstadoProductoVisible.setChecked(false);
                break;

        }
        switchVisibleVentaWeb.setChecked(bVisibleWeb);
    }

    public void SetEstados(String Visible){

    }
    public void estadoSwitch(){

        switchVisibleVentaWeb.setChecked(true);
        sEstadoProductoVisible.setChecked(true);
        sEstadoProducto.setChecked(true);
        sEstadoProductoVisible.setText(getString(R.string.estado_visible_cabecera)+getString(R.string.estado_visible));
        sEstadoProducto.setText(getString(R.string.estado_producto_cabecera)+getString(R.string.estado_activo));
        bVisibleWeb=true;
        switchPVLibre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                pVentaLibre=isChecked;
                if(isChecked){
                    switchPVLibre.setText("Precio de Venta Variable:\nActivo");
                }else{
                    switchPVLibre.setText("Precio de Venta Variable:\nInactivo");
                }
            }
        });
        sEstadoProductoVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    buttonView.setText(getString(R.string.estado_visible_cabecera)+getString(R.string.estado_visible));
                    estadoVisible=getString(R.string.EstadoProductoVisible);
                }
                else{
                    buttonView.setText(getString(R.string.estado_visible_cabecera)+getString(R.string.estado_invisible));
                    estadoVisible=getString(R.string.EstadoProductoInvisible);

                }

            }
        });
        switchVisibleVentaWeb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bVisibleWeb=isChecked;
            String texto=((isChecked))?"Visible en web: Visible":"Visible en web: invisible";
            buttonView.setText(texto);
        });
        sEstadoProducto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    estadoProducto=getString(R.string.EstadoProductoActivo);
                    buttonView.setText(getString(R.string.estado_producto_cabecera)+getString(R.string.estado_activo));

                }
                else{
                    new AlertDialog.Builder(getContext()).setTitle("Advertencia")
                            .setMessage("Al guardar el artículo como inactivo ,el artículo dejará de estar disponible   ").setPositiveButton("Salir",null).create().show();
                    estadoProducto=getString(R.string.EstadoProductoInactivo);
                    buttonView.setText(getString(R.string.estado_producto_cabecera)+getString(R.string.estado_inactivo));


                }
            }
        });

        sControlPeso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    controlPeso=isChecked;
                    buttonView.setText("Control Peso:Activado");
                }
                else{
                    buttonView.setText("Control Peso:Desactivado");
                    controlPeso=isChecked;

                }

            }
        });
        sControlStock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                controlStock=isChecked;
               buttonView.setText("Control Stock:Activado");
            }
            else{
                controlStock=isChecked;
                buttonView.setText("Control Stock:Desactivado");
            }
        });


    }

}
