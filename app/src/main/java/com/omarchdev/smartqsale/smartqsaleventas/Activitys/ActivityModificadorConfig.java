package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncModificadores;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAgregarOpcion;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAgregarValorModificador;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DetalleModificador;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Modificador;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterModificador;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterValorMod;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ActivityModificadorConfig extends ActivityParent implements View.OnClickListener, AsyncModificadores.ListenerModificadoresConfig, DialogAgregarOpcion.ListenerAddOpcion, RvAdapterModificador.ListenerModificadorConfig, DialogAgregarValorModificador.ListenerValorModificador, RvAdapterValorMod.ListenerValorMod {

    SlidingUpPanelLayout sliding_layout;
    Button btnEditarValores;
    List<Modificador> modificadorList;
    AsyncModificadores asyncModificadores;
    RecyclerView rvModificadores, rvValoresModificadores, rvValoresEdiccion;
    Button btnAddModificador, btnAgregarValorMod, btnEditarValorMod, btnEliminarValorMod;
    DialogAgregarOpcion dialogAgregarOpcion;
    RvAdapterModificador rvAdapterModificador;
    AVLoadingIndicatorView pbIndicatorValores, pbIndicator;
    TextView txtCargando, txtMensaje, txtCargando1;
    RvAdapterValorMod rvAdapterValorMod1;
    DialogAgregarValorModificador dialogAgregarValorModificador;


    int posValorMod;
    int posMod;
    int posDelete;
    int posEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificador_config);
        try {
            posMod = -10;
            posDelete = -10;
            posEdit = -10;
            posValorMod = -10;
            pbIndicator = findViewById(R.id.pbIndicator);
            rvAdapterValorMod1 = new RvAdapterValorMod();
            txtMensaje = findViewById(R.id.txtMensaje);
            txtCargando1 = findViewById(R.id.txtCargando1);
            sliding_layout = findViewById(R.id.sliding_layout);
            sliding_layout.addPanelSlideListener(listenerPanel);
            btnEditarValores = findViewById(R.id.btnEditarValores);
            btnEliminarValorMod = findViewById(R.id.btnEliminarValorMod);
            asyncModificadores = new AsyncModificadores();
            modificadorList = new ArrayList<>();
            asyncModificadores.setListenerModificadoresConfig(this);
            btnEditarValorMod = findViewById(R.id.btnEditarValorMod);
            rvModificadores = findViewById(R.id.rvModificadores);
            rvValoresModificadores = findViewById(R.id.rvValoresModificadores);
            rvValoresModificadores.setAdapter(rvAdapterValorMod1);
            rvValoresModificadores.setLayoutManager(new LinearLayoutManager(this));
            rvValoresEdiccion = findViewById(R.id.rvValoresEdiccion);
            btnAddModificador = findViewById(R.id.btnAddModificador);
            txtCargando = findViewById(R.id.txtCargando);
            btnAgregarValorMod = findViewById(R.id.btnAgregarValorMod);
            dialogAgregarOpcion = new DialogAgregarOpcion();
            dialogAgregarOpcion.setListenerAddOpcion(this);
            asyncModificadores.ObtenerModificadores();
            rvAdapterModificador = new RvAdapterModificador();
            rvModificadores.setAdapter(rvAdapterModificador);
            rvModificadores.setLayoutManager(new LinearLayoutManager(this));
            pbIndicatorValores = findViewById(R.id.pbIndicatorValores);
            btnEditarValores.setOnClickListener(this);
            btnAddModificador.setOnClickListener(this);
            rvValoresEdiccion.setAdapter(rvAdapterValorMod1);
            rvValoresEdiccion.setLayoutManager(new LinearLayoutManager(this));
            pbIndicatorValores.hide();
        } catch (Exception e) {
            e.toString();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        txtCargando.setVisibility(View.INVISIBLE);
        rvAdapterModificador.setListenerModificadorConfig(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Modificadores");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.arrow_back_home);
        dialogAgregarValorModificador = new DialogAgregarValorModificador();
        dialogAgregarValorModificador.setListenerValorModificador(this);
        btnAgregarValorMod.setOnClickListener(this);
        rvAdapterValorMod1.setListenerValorMod(this);
        btnEditarValorMod.setOnClickListener(this);
        btnEliminarValorMod.setOnClickListener(this);
        pbIndicator.show();
        txtCargando1.setVisibility(View.VISIBLE);
        sliding_layout.setVisibility(View.INVISIBLE);

    }


    SlidingUpPanelLayout.PanelSlideListener listenerPanel = new SlidingUpPanelLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {

        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {

                getSupportActionBar().setTitle( "Valores de " + modificadorList.get(posMod).getDescripcion());

            } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                getSupportActionBar().setTitle( "Modificadores" );
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asyncModificadores.CancelarObtenerMod();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnEditarValores:


                AccPanelEdiccion();

                break;

            case R.id.btnAddModificador:

                DialogAgregarMod();

                break;

            case R.id.btnAgregarValorMod:
                DialogAgregarValorMod();
                break;

            case R.id.btnEditarValorMod:
                DialogEditarValorMod();
                break;

            case R.id.btnEliminarValorMod:
                DialogEliminarValorMod();
                break;
        }

    }

    public void DialogEliminarValorMod() {
        if (posValorMod >= 0) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("¿Desea eliminar el valor '" + modificadorList.get(posMod).getDetalleModificadorList().get(posValorMod).getDescripcionModificador() + "' ? ");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        asyncModificadores.EliminarValorMod(modificadorList.get(posMod).getIdModificador(), modificadorList.get(posMod).getDetalleModificadorList().get(posValorMod).getIdDetalleModificador());
                    }
                }).setNegativeButton("Cancelar", null).create().show();
            } catch (Exception e) {
                e.toString();
            }
        }
    }

    public void DialogEditarValorMod() {
        if (posValorMod >= 0) {
            DialogFragment dialogFragment = dialogAgregarValorModificador;
            dialogAgregarValorModificador.setAccion((byte) 1);
            dialogAgregarValorModificador.setDetalleModificador(modificadorList.get(posMod).getDetalleModificadorList().get(posValorMod));
            dialogFragment.show(getSupportFragmentManager(), "Editar Valor");
        }

    }

    public void DialogAgregarValorMod() {
        DialogFragment dialogFragment = dialogAgregarValorModificador;

        dialogAgregarValorModificador.setAccion((byte) 0);
        dialogAgregarValorModificador.setDetalleModificador(new DetalleModificador());
        dialogFragment.show(getSupportFragmentManager(), "txtTitulo");
    }

    public void DialogAgregarMod() {
        DialogFragment dialogFragment = dialogAgregarOpcion;
        dialogAgregarOpcion.setAccion((byte) 0);
        dialogFragment.show(getSupportFragmentManager(), "Agregar Modificador");


    }

    public void DialogEditarMod() {
        DialogFragment dialogFragment = dialogAgregarOpcion;
        dialogAgregarOpcion.setAccion((byte) 1);
        dialogAgregarOpcion.setDescripcion(modificadorList.get(posEdit).getDescripcion());
        dialogFragment.show(getSupportFragmentManager(), "Agregar Modificador");

    }

    private void AccPanelEdiccion() {
        if (posMod >= 0) {
            if (modificadorList.size() > 0) {
                if (posMod >= 0) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else {
                    AlertaMensaje("Advertencia", "Debe seleccionar un modificador para editar sus valores");
                }
            } else if (modificadorList.size() == 0) {
                AlertaMensaje("Advertencia", "Debe ingresar modificadores para usar usar esta opción.");
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed() {
        if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {

            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        } else {
            super.onBackPressed();
            finish();
        }
    }

    public void AlertaMensaje(String titulo, String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo).setMessage(mensaje).setPositiveButton("Salir", null)
                .create().show();

    }


    @Override
    public void ObtenerModificadores(List<Modificador> modificadorList) {
        pbIndicator.hide();
        txtCargando1.setVisibility(View.INVISIBLE);
        sliding_layout.setVisibility(View.VISIBLE);
        if (modificadorList != null) {
            this.modificadorList = modificadorList;

            rvAdapterModificador.ActualizarLista(modificadorList);
            if (modificadorList.size() > 0) {

                txtMensaje.setText("Seleccione un modificador para ver sus valores");

            } else {
                txtMensaje.setText("No tiene modificadores");
            }

        }
    }


    @Override
    public void ResultadoGuardarModificador(Modificador modificador) {
        if (modificador.getIdModificador() >= 0) {
            rvAdapterModificador.IngresarModificador(modificador);
            modificadorList.add(modificador);
        } else if (modificador.getIdModificador() == -5) {
            AlertaMensaje("Error", "Error en el proceso de guardado");
        } else if (modificador.getIdModificador() == -10) {

            AlertaMensaje("Error Internet", "Error en el proceso de guardado.Verifique su conexión a internet");
        }
    }

    @Override
    public void ResultadoGuardarDetalleMod(DetalleModificador detalleModificador) {

        if (detalleModificador.getIdModificador() >= 0) {


            modificadorList.get(posMod).getDetalleModificadorList().add(detalleModificador);
            rvAdapterValorMod1.AgregarValorModificador(detalleModificador);
            if (modificadorList.get(posMod).getDetalleModificadorList().size() > 0) {
                txtMensaje.setVisibility(View.INVISIBLE);
                rvValoresModificadores.setVisibility(View.VISIBLE);
            }

        } else if (detalleModificador.getIdModificador() == -5) {
            AlertaMensaje("Error", "Error al guardar el valor");
        } else if (detalleModificador.getIdModificador() == -10) {

            AlertaMensaje("Error", "Error al guardar el valor.Verifique su conexión");

        }
    }

    @Override
    public void ResultadoEliminarMod(Modificador modificador) {

        if (modificador.getIdModificador() == 0) {
            modificadorList.remove(posDelete);
            rvAdapterModificador.EliminarModificador(posDelete);
            posDelete = -10;
        } else if (modificador.getIdModificador() == -5) {
            AlertaMensaje("Error", "Error al eliminar el modificador");

        } else if (modificador.getIdModificador() == -10) {
            AlertaMensaje("Error", "Error al eliminar el modificador.Verifique su conexión");

        }

        if (modificadorList.size() == 0) {
            rvModificadores.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void ResultadoModificarMod(Modificador modificador) {
        if (modificador.getIdModificador() > 0) {
            modificadorList.get(posEdit).setDescripcion(modificador.getDescripcion());
            rvAdapterModificador.EditarPosicion(modificador.getDescripcion(), posEdit);
        } else if (modificador.getIdModificador() == -5) {
            AlertaMensaje("Error", "Error al editar el modificador");

        } else if (modificador.getIdModificador() == -10) {
            AlertaMensaje("Error", "Error al editar el modificador.Verifique su conexión");


        }
    }

    @Override
    public void ResultadoEditarValorMod(DetalleModificador detalleModificador) {
        if (detalleModificador.getIdModificador() >= 0) {
            modificadorList.get(posMod).getDetalleModificadorList().get(posValorMod).setDescripcionModificador(detalleModificador.getDescripcionModificador());
            rvAdapterValorMod1.EditarValor(posValorMod, detalleModificador.getDescripcionModificador());
        } else if (detalleModificador.getIdModificador() == -5) {
            AlertaMensaje("Error", "Error al editar el valor");


        } else if (detalleModificador.getIdModificador() == -10) {
            AlertaMensaje("Error", "Error al editar el valor.Verifique su conexión");


        }
        posValorMod = -10;
    }

    @Override
    public void ResultadoEliminarValorMod(DetalleModificador detalleModificador) {

        if (detalleModificador.getIdModificador() == 0) {
            modificadorList.get(posMod).getDetalleModificadorList().remove(posValorMod);
            rvAdapterValorMod1.EliminarElementoPosicion(posValorMod);
        } else if (detalleModificador.getIdModificador() == -5) {
            AlertaMensaje("Error", "Error al eliminar el valor");


        } else if (detalleModificador.getIdModificador() == -10) {
            AlertaMensaje("Error", "Error al eliminar el valor.Verifique su conexión");
        }


        if (modificadorList.get(posMod).getDetalleModificadorList().size() == 0) {

            rvValoresModificadores.setVisibility(View.INVISIBLE);
            txtMensaje.setVisibility(View.VISIBLE);
            txtMensaje.setText("El modificador seleccionado no tiene valores.Para agregar un valor presione la opción editar valores");

        }
        posValorMod = -10;

    }

    @Override
    public void IngresarModificador(String modificadorDescripcion) {


        asyncModificadores.IngresarModificadores(modificadorDescripcion);

    }

    @Override
    public void EditarModificador(String modificadorDescripcion) {

        asyncModificadores.EditarModificador(modificadorList.get(posEdit).getIdModificador(), modificadorDescripcion);
    }

    @Override
    public void getPositionSelect(int position) {

        posMod = position;
        posValorMod = -10;
        if (modificadorList.get(position).getDetalleModificadorList().size() > 0) {
            rvAdapterValorMod1.AddListValores(modificadorList.get(position).getDetalleModificadorList());
            txtMensaje.setVisibility(View.INVISIBLE);
            rvValoresModificadores.setVisibility(View.VISIBLE);
        } else {
            rvAdapterValorMod1.AddListValores(modificadorList.get(position).getDetalleModificadorList());

            rvValoresModificadores.setVisibility(View.INVISIBLE);
            txtMensaje.setVisibility(View.VISIBLE);
            txtMensaje.setText("El modificador seleccionado no tiene valores.Para agregar un valor presione la opción editar valores");

        }

    }

    @Override
    public void editPositionSelect(int position) {

        posEdit = position;
        DialogEditarMod();
    }

    @Override
    public void deletePosition(int position) {
        EliminarModificador(position);
    }


    @Override
    public void setValorModificador(DetalleModificador detalleModificador) {
        asyncModificadores.AgregarValorMod(modificadorList.get(posMod).getIdModificador(), detalleModificador);
    }

    @Override
    public void setEditarValorModificador(DetalleModificador detalleModificador) {

        asyncModificadores.EditarValorMod(modificadorList.get(posMod).getIdModificador(), modificadorList.get(posMod).getDetalleModificadorList().get(posValorMod).getIdDetalleModificador(), detalleModificador);
    }

    private void EliminarModificador(int position) {
        posDelete = position;

        if (modificadorList.get(posDelete).getDetalleModificadorList().size() > 0) {
            AlertaMensaje("Advertencia", "Debe eliminar todos los valores del modificador para realizar está acción.");
        } else if (modificadorList.get(posDelete).getDetalleModificadorList().size() == 0) {
            MensajeEliminar();
        }
    }

    public void MensajeEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Advertencia").setMessage("¿Desea eliminar el modificador '" + modificadorList.get(posDelete).getDescripcion() + "'? ")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        asyncModificadores.PEliminarModificador(modificadorList.get(posDelete).getIdModificador());
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.create().show();
    }

    @Override
    public void getPositionValor(int position) {
        posValorMod = position;
    }
}
