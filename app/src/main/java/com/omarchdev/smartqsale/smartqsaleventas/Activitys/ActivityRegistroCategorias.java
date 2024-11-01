package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.Model.RepuestaEliminar;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import static android.view.View.GONE;

public class ActivityRegistroCategorias extends ActivityParent implements AsyncCategoria.ListenerProcesoCategoria, View.OnClickListener {

    int Estado;
    int idCategoria;
    String descripcionCategoria;
    TextInputLayout edtDescripcionCategoria;
    AsyncCategoria asyncCategoria;
    FloatingActionMenu actionMenuCategoria;
    FloatingActionButton menu_item_cancelar,menu_item_edit,menu_item_delete,menu_item_guardar;
    DialogCargaAsync dialogCargaAsync;
    Dialog dialogCarga;
    byte registro;
    Context context;
    int estadoConfig;
    MenuItem itemGuardar;
    MenuItem itemEliminar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_congif_cliente,menu);

        itemEliminar=menu.findItem(R.id.action_eliminar);
        itemGuardar=menu.findItem(R.id.action_guardar);


        if(estadoConfig==Constantes.EstadoConfiguracion.Visualizar){
            itemGuardar.setVisible(false);
            itemEliminar.setVisible(false);
        }else if(estadoConfig==Constantes.EstadoConfiguracion.Nuevo){
            itemGuardar.setVisible(true);
            itemEliminar.setVisible(false);

        }else if(estadoConfig==Constantes.EstadoConfiguracion.Editar){
            itemGuardar.setVisible(true);
            itemEliminar.setVisible(true);

        }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id){

            case R.id.action_eliminar:
                EliminarCategoriaMensaje();
                break;
            case R.id.action_guardar:
                ValidarCategoria();
                break;


        }


        return super.onOptionsItemSelected(item);
    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_categorias);
        registro=0;
        dialogCargaAsync=new DialogCargaAsync(this);
        Estado=getIntent().getExtras().getInt("Estado",0);
        estadoConfig=getIntent().getIntExtra("estadoConfig",0);
        asyncCategoria=new AsyncCategoria();
        asyncCategoria.setListenerProcesoCategoria(this);
        edtDescripcionCategoria=findViewById(R.id.edtDescripcionCategoria);
        actionMenuCategoria=findViewById(R.id.actionMenuCategoria);
        menu_item_cancelar=findViewById(R.id.menu_item_cancelar);
        menu_item_edit=findViewById(R.id.menu_item_edit);
        menu_item_delete=findViewById(R.id.menu_item_delete);
        menu_item_guardar=findViewById(R.id.menu_item_guardar);
        asyncCategoria.setContext(this);
        if(Estado==0){
            registro=0;
            idCategoria=0;
            descripcionCategoria="";
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agregar Categoría");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
            getSupportActionBar().setElevation(4);
            edtDescripcionCategoria.setEnabled(true);
            menu_item_guardar.setVisibility(View.VISIBLE);
            menu_item_cancelar.setVisibility(View.VISIBLE);
            menu_item_delete.setVisibility(GONE);
            menu_item_edit.setVisibility(GONE);


        }
        else if(Estado==1){
            registro=1;
            idCategoria=getIntent().getExtras().getInt("idCategoria",0);
            descripcionCategoria=getIntent().getExtras().getString("nombre","");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Editar Categoría");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
       //     getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
            getSupportActionBar().setElevation(4);
            edtDescripcionCategoria.getEditText().setText(descripcionCategoria);
            edtDescripcionCategoria.setEnabled(false);
            menu_item_guardar.setVisibility(GONE);
            menu_item_cancelar.setVisibility(View.VISIBLE);
            menu_item_delete.setVisibility(View.VISIBLE);
            menu_item_edit.setVisibility(View.VISIBLE);
   }

        menu_item_guardar.setOnClickListener(this);
        menu_item_delete.setOnClickListener(this);
        menu_item_edit.setOnClickListener(this);
        menu_item_cancelar.setOnClickListener(this);
        if(estadoConfig== Constantes.EstadoConfiguracion.Editar){
            edtDescripcionCategoria.setEnabled(true);
        }else if(estadoConfig==Constantes.EstadoConfiguracion.Visualizar){
            edtDescripcionCategoria.setEnabled(false);
        }else if(estadoConfig==Constantes.EstadoConfiguracion.Nuevo){
            edtDescripcionCategoria.setEnabled(true);
        }
        context=this;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
    public void MensajeAlerta(String titulo,String mensaje){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(titulo).setMessage(mensaje).setPositiveButton("Aceptar",null).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        }).create().show();
    }
    @Override
    public void RespuestaGuardarCategoria(byte respuestaGuardar) {
        dialogCarga.hide();
            if(respuestaGuardar==100){
                MensajeAlerta("Confirmacion","La categoría fue guardada con éxito");
                itemGuardar.setVisible(false);
                registro=1;
            }else if(respuestaGuardar==99){
                MensajeAlerta("Advertencia","La categoría no se logro guardar");
                itemGuardar.setVisible(false);

                registro=0;
            }else if(respuestaGuardar==98){
                MensajeAlerta("Confirmacion","La categoría no se logro guardar.Verifique su conexión");
                itemGuardar.setVisible(false);

                registro=0;
            }
    }

    @Override
    public void RespuestaEliminarCategoria(RepuestaEliminar respuestaEliminar) {
        dialogCarga.hide();
            if(respuestaEliminar.getRespuesta()==99){
               MensajeAlerta("Alerta","No se logro eliminar.Debido a que existen productos en la categoría. ");
                registro=0;
            }else if(respuestaEliminar.getRespuesta()==100) {

                MensajeAlerta("Confirmación", "La categoría fue eliminada con éxito");
                actionMenuCategoria.setVisibility(GONE);
                   registro=1;

            }else if(respuestaEliminar.getRespuesta()==98){
                MensajeAlerta("Confirmación", "Error al eliminar la categoría.Reinicie su aplicación");
                registro=0;
            }
            else if(respuestaEliminar.getRespuesta()==97){
                MensajeAlerta("Confirmación", "La categoría no se elimino.Verifique su conexión a internet");
                registro=0;

            }
    }

    @Override
    public void RespuestaEditarCategoria(byte respuesta) {
        dialogCarga.hide();
            if(respuesta==100){
                MensajeAlerta("Confirmación", "La categoría fue editada con éxito");
                registro=1;
            }
            else if(respuesta==98){
                MensajeAlerta("Confirmación", "La categoría no se logro editar.Reinicie su aplicación");
                registro=0;
            }
            else if(respuesta==99){
                MensajeAlerta("Confirmación", "La categoría no se logro editar.Verifique su conexión a internet");
                registro=0;
            }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.menu_item_guardar:
                    ValidarCategoria();

                break;

            case R.id.menu_item_edit:
                HabilitarEdicion();
                break;

            case R.id.menu_item_delete:
                EliminarCategoriaMensaje();
                break;

            case R.id.menu_item_cancelar:
                MensajeSalir();
                break;

        }

    }

   public void HabilitarEdicion(){
        registro=0;
       actionMenuCategoria.close(true);
        edtDescripcionCategoria.setEnabled(true);
        menu_item_guardar.setVisibility(View.VISIBLE);
        menu_item_edit.setVisibility(GONE);
        menu_item_cancelar.setVisibility(View.VISIBLE);
        menu_item_delete.setVisibility(View.VISIBLE);

   }

    @Override
    public void onBackPressed() {
        asyncCategoria.CancelarEditar();
        if(registro==0) {
            MensajeSalir();
        }
        else if (registro==1){
           super.onBackPressed();
        }
    }
    private void EliminarCategoriaMensaje(){

       AlertDialog.Builder builder=new AlertDialog.Builder(this);
       builder.setMessage("¿Desea eliminar la categoría?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               actionMenuCategoria.close(true);
               asyncCategoria.EliminarCategoriaPorId(idCategoria);
               dialogCarga=dialogCargaAsync.getDialogCarga("Eliminando categoría");
               dialogCarga.setCanceledOnTouchOutside(false);
               dialogCarga.show();
           }
       }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

           }
       }).create().show();

    }


    private void ValidarCategoria(){

        if(edtDescripcionCategoria.getEditText().getText().toString().trim().length()==0){
            edtDescripcionCategoria.setError("Debe ingresar un texto");
        }
        else{
            MensajeGuardar();

        }
    }
    public void MensajeGuardar(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Confirmación").setMessage("¿Desea guardar la categoria '"+ edtDescripcionCategoria.getEditText().getText().toString()+"' ?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actionMenuCategoria.close(true);
                try {
                    if (Estado == 0) {
                        dialogCarga = dialogCargaAsync.getDialogCarga("Guardando categoría");
                        dialogCarga.setCanceledOnTouchOutside(false);
                        dialogCarga.show();
                        asyncCategoria.GuardarCategoria(edtDescripcionCategoria.getEditText().getText().toString());

                    } else if (Estado == 1) {
                        dialogCarga = dialogCargaAsync.getDialogCarga("Editando categoría");
                        dialogCarga.setCanceledOnTouchOutside(false);
                        dialogCarga.show();
                        asyncCategoria.EditarCategoriaId(idCategoria, edtDescripcionCategoria.getEditText().getText().toString());
                    }
                }catch (Exception e){

                    Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();

                }

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    public void MensajeSalir(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Advertencia").setMessage("¿Desea salir sin guardar la categoría?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }
}
