package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.Dialog;
import android.content.DialogInterface;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipo_Pago;
import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.RvAdapterSelectMedioPagoImg;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RegistroMedioPago extends ActivityParent implements AsyncMedioPago.ListenerConfigMedioPago, RvAdapterSelectMedioPagoImg.ListenerMedioPago, View.OnClickListener {

    String nombreImagen,nombreMedioPago;
    AsyncMedioPago asyncMedioPago;
    Spinner spnTipoPago;
    TextInputLayout edtNombreMedioPago,edtCodigoMedioPago,edtMontoMinimo;
    RecyclerView rvSeleccionImagen;
    RvAdapterSelectMedioPagoImg rvAdapterSelectMedioPagoImg;
    List<String> listaTiposPago;
    List<mTipo_Pago> tipodePagoList;
    String cadena[];
    int longitud;
    int idMedioPago;
    int idTipo;
    DialogCargaAsync dialogCargaAsync;
    AVLoadingIndicatorView pbIndicator;
    TextView txtCargando1;
    RelativeLayout rlContent;
    SimpleAdapter simpleAdapter;
    ArrayAdapter<String> arrayAdapter;
    FloatingActionMenu actionMenuMedioPago;
    Dialog carga;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    int IdTipoPago;
    FloatingActionButton menu_item_cancelar,menu_item_edit,menu_item_delete,menu_item_guardar;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_medio_pago);
        spnTipoPago=findViewById(R.id.spnTipoPago);
        rlContent=findViewById(R.id.rlContent);
        longitud=0;
        dialogCargaAsync=new DialogCargaAsync(this);
        txtCargando1=findViewById(R.id.txtCargando1);
        pbIndicator=findViewById(R.id.pbIndicator);
        idMedioPago=getIntent().getIntExtra("id",0);
        rvAdapterSelectMedioPagoImg=new RvAdapterSelectMedioPagoImg();
        edtNombreMedioPago=findViewById(R.id.edtNombreMedioPago);
        edtCodigoMedioPago=findViewById(R.id.edtCodigoMedioPago);
        edtMontoMinimo=findViewById(R.id.edtMontoMinimo);
        rvSeleccionImagen=findViewById(R.id.rvSeleccionImagen);
        listaTiposPago=new ArrayList<>();
        asyncMedioPago=new AsyncMedioPago();
        asyncMedioPago.ObtenerTipoPago();
        asyncMedioPago.setListenerConfigMedioPago(this);
        rvSeleccionImagen.setAdapter(rvAdapterSelectMedioPagoImg);
        actionMenuMedioPago=findViewById(R.id.actionMenuMedioPago);
        menu_item_cancelar=findViewById(R.id.menu_item_cancelar);
        menu_item_edit=findViewById(R.id.menu_item_edit);
        menu_item_delete=findViewById(R.id.menu_item_delete);
        menu_item_guardar=findViewById(R.id.menu_item_guardar);
        menu_item_delete.setOnClickListener(this);
        menu_item_edit.setOnClickListener(this);
        menu_item_cancelar.setOnClickListener(this);
        menu_item_guardar.setOnClickListener(this);
        rvSeleccionImagen.setLayoutManager(new GridLayoutManager(this,4));
        rvAdapterSelectMedioPagoImg.setListenerMedioPago(this);
        listaTiposPago=new ArrayList<>();
        img=new ImageView(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( "Clientes");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
        getSupportActionBar().setElevation(6);

        if(idMedioPago==0){
            getSupportActionBar().setTitle("Agregar medio de pago");
            menu_item_delete.setVisibility(View.GONE);
            menu_item_edit.setVisibility(View.GONE);
            menu_item_guardar.setVisibility(View.VISIBLE);
            menu_item_cancelar.setVisibility(View.VISIBLE);


        }
        else if(idMedioPago>=0){

            edtNombreMedioPago.setEnabled(false);
            edtCodigoMedioPago.setEnabled(false);
            menu_item_delete.setVisibility(View.VISIBLE);
            menu_item_edit.setVisibility(View.VISIBLE);
            menu_item_guardar.setVisibility(View.GONE);
            menu_item_cancelar.setVisibility(View.GONE);
            edtNombreMedioPago.getEditText().setText(getIntent().getStringExtra("nombreMP"));
            edtCodigoMedioPago.getEditText().setText(getIntent().getStringExtra("codigoMP"));
            nombreMedioPago=getIntent().getStringExtra("nombreMP");
            nombreImagen=getIntent().getStringExtra("nombreImagen");
            idTipo=getIntent().getIntExtra("idTipoPago",0);
            spnTipoPago.setEnabled(false);
            getSupportActionBar().setTitle(nombreMedioPago);
            rvSeleccionImagen.setEnabled(false);
            rvAdapterSelectMedioPagoImg.setClick(false);
        }
        tipodePagoList=new ArrayList<>();
        rlContent.setVisibility(View.INVISIBLE);
        pbIndicator.show();
        txtCargando1.setVisibility(View.VISIBLE);

    }


    public void MensajeConfirmacion(String title,String mensaje){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(mensaje).setPositiveButton("Salir",null).create().show();

    }


    @Override
    public void ResultadoGuardarMedioPago(byte resultado) {
        carga.hide();
        if(resultado==100){
            MensajeConfirmacion("Confirmacion","El medio de pago fue guardado con éxito");
        }
        else if(resultado==99){
            MensajeConfirmacion("Advertencia","El medio de pago no se logro guardar .");

        }
        else if(resultado==98){
            MensajeConfirmacion("Advertencia","El medio de pago no se logro guardar .Verifique su conexión a internet");

        }

    }

    @Override
    public void ResultadoEliminarMedioPago(byte resultado) {
        carga.hide();
        if(resultado==100){
            actionMenuMedioPago.setVisibility(View.GONE);
            actionMenuMedioPago.setVisibility(View.GONE);
            MensajeConfirmacion("Confirmacion","El medio de pago fue guardado con éxito");

        }
        else if(resultado==99){
            MensajeConfirmacion("Advertencia","El medio de pago no se eliminó .");

        }
        else if(resultado==98){
            MensajeConfirmacion("Advertencia","El medio de pago no se eliminó.Verifique su conexión a internet");

        }
    }

   public void ObtenerTiposPago(List<mTipo_Pago> tipoPagoList) {

       rlContent.setVisibility(View.VISIBLE);
       pbIndicator.hide();
       txtCargando1.setVisibility(View.INVISIBLE);
        listaTiposPago.add("Seleccione un tipo de pago");
        this.tipodePagoList=tipoPagoList;
        int position=0;
        try {
            if (tipoPagoList != null) {
                longitud = tipoPagoList.size();
                for (int i = 0; i < longitud; i++) {
                    listaTiposPago.add(tipoPagoList.get(i).getcDescripcion());

                    if(idMedioPago!=0 && tipoPagoList.get(i).getIdTipoPago()==idTipo){
                        position=i;
                    }
                }

                arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaTiposPago);
                spnTipoPago.setAdapter(arrayAdapter);
                if(idMedioPago!=0){

                    spnTipoPago.setSelection(position+1);
                    rvAdapterSelectMedioPagoImg.selectImage(nombreImagen);
                }
            } else if (tipoPagoList == null) {
                Toast.makeText(this, "Error al descargar la información.Reinicie su aplicación", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        catch (Exception e){
            e.toString();
        }
    }

    @Override
    public void MetodoPagoSelect(String nombreImagen) {
        this.nombreImagen=nombreImagen;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id. menu_item_guardar:
                GuardarMedioPago();
                break;

            case R.id.menu_item_delete:

                MensajeAlertaEliminar();
                break;
            case R.id.menu_item_edit:

                HabilitarEdicion();

                break;

            case R.id.menu_item_cancelar:
                finish();
                break;


        }

    }

    public void GuardarMedioPago(){

        actionMenuMedioPago.close(true);
        if(spnTipoPago.getSelectedItemPosition()==0){
            Toast.makeText(this,"Debe seleccionar un tipo de pago",Toast.LENGTH_LONG).show();

        }
        else if(spnTipoPago.getSelectedItemPosition()>0) {
            IdTipoPago=tipodePagoList.get(spnTipoPago.getSelectedItemPosition()-1).getIdTipoPago();
            if(edtCodigoMedioPago.getEditText().getText().toString().trim().length()==0 || edtNombreMedioPago.getEditText().getText().toString().trim().length()==0 ){
                edtNombreMedioPago.setError("Este campo es obligatorio");
                edtCodigoMedioPago.setError("Este campo es obligatorio");
            }
            else {
                carga =dialogCargaAsync.getDialogCarga("Guardando");
                carga.show();
                asyncMedioPago.GuardarMedioPago(idMedioPago, edtCodigoMedioPago.getEditText().getText().toString(), edtNombreMedioPago.getEditText().getText().toString(), IdTipoPago, new BigDecimal(0), nombreImagen);
            }
        }

    }
    public void MensajeAlertaEliminar(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setNegativeButton("Cancelar",null).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                asyncMedioPago.EliminarMedioPago(idMedioPago);
                carga=dialogCargaAsync.getDialogCarga("Eliminando medio de pago");
                carga.show();
                actionMenuMedioPago.close(true);
            }
        }).setMessage("¿Desea eliminar el metodo de pago "+nombreMedioPago+"?").setTitle("Advertencia").create().show();

    }

    @Override
    public void onBackPressed() {
       if(actionMenuMedioPago.isOpened()){
            actionMenuMedioPago.close(true);

        }
        else{
             super.onBackPressed();


        }

    }
    public void HabilitarEdicion(){
        menu_item_delete.setVisibility(View.GONE);
        menu_item_edit.setVisibility(View.GONE);
        menu_item_guardar.setVisibility(View.VISIBLE);
        menu_item_cancelar.setVisibility(View.VISIBLE);
        edtNombreMedioPago.setEnabled(true);
        edtCodigoMedioPago.setEnabled(true);
        spnTipoPago.setEnabled(true);
        rvSeleccionImagen.setEnabled(true);
        rvAdapterSelectMedioPagoImg.setClick(true);
        actionMenuMedioPago.close(true);
        Toast.makeText(this,"Edición habilitada",Toast.LENGTH_LONG).show();

    }
}
