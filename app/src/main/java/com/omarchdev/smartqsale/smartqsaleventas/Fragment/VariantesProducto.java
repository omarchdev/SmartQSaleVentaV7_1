package com.omarchdev.smartqsale.smartqsaleventas.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVariantes;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfAgregarOpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogEditVariantes;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogInsertOption;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogInsertValorOption;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.cProgressDialog;
import com.omarchdev.smartqsale.smartqsaleventas.Model.AdditionalPriceProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListOpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ListValoresVariantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.OpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ValorOpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Variante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVariante;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVariantesOpciones;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterVariantesProducto;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VariantesProducto extends Fragment implements View.OnClickListener, DialogInsertOption.AgregarOpcion,
        DialogInsertValorOption.ListenerValorOpcion,
        RvAdapterVariantesOpciones.AgregarValores, RvAdapterVariantesProducto.ClickVariante,
        DialogEditVariantes.InfoModificarListener, AsyncVariantes.ConfigVariantes,
        SlidingUpPanelLayout.PanelSlideListener, DfAgregarOpcionVariante.IValVariante, AsyncVariantes.IActualizaValorOpcion {

    cProgressDialog progressDialog;
    Dialog dialogCarga;
    TextView txtCargando;
    int positionSelected;
    int positionDeleted;
    SlidingUpPanelLayout panelVariantes;
    Button btnAgregarOpcion;
    Switch sActivarVariantes;
    DialogInsertOption dialogInsertOption;
    TextView txtNumVariantes;
    RvAdapterVariantesOpciones rvAdapterVariantesOpciones;
    RecyclerView rvOpcionesVariantes,rvVariantes;
    DialogInsertValorOption dialogInsertValorOption;
    ListOpcionVariante listOpcionVariante;
    List<OpcionVariante> opcionVarianteList;
    Variante varianteTemp;
    ListOpcionVariante valoresVariantesList;
    List<String> listaEtiquetas;
    List<String> listaLabel;
    ListValoresVariantes listValoresVariantes;
    int position;
    int idProduct;
    Button btnEliminarVariante,btnEditarVariante;
    boolean estadoConfigVaria;
    ListenerProductoVariante listenerProductoVariante;
    boolean EstadoVariante;
    boolean EstadoVarianteTemp;
    RvAdapterVariante rvAdapterVariante;
    List<Variante> varianteList;
    RecyclerView rv;
    RvAdapterVariantesProducto rvAdapterVariantesProducto;
    AsyncVariantes asyncVariantes;
    Button btnGenerarVariantes;
    boolean validacionOpcVal;
    int numVariantes;
    boolean existeVariantes;
    Button btnEditarVariantes;
    DialogEditVariantes dialogEditarVariantes;
    mProduct productTemp;
    ImageButton imgArrow;
    AVLoadingIndicatorView pbIndicator;
    RelativeLayout contenedorPrimario,contenedorSecundario;
    ListenerPanel panelListener;


    public void setListenerPanel(ListenerPanel panelListener){

        this.panelListener=panelListener;

    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            if(newState== SlidingUpPanelLayout.PanelState.COLLAPSED){
                panelListener.listenerStatePanel(false);
            }
            else if(newState== SlidingUpPanelLayout.PanelState.EXPANDED){
                panelListener.listenerStatePanel(true);
            }
    }

    @Override
    public void setInfoEditVariante(int idVariante, BigDecimal stock, BigDecimal precioVenta, BigDecimal precioCompra, String codigoBarra, List<AdditionalPriceProduct> additionalPriceProductList,boolean bPVMultiple) {
        varianteTemp=new Variante();
        varianteTemp.setIdVariante(idVariante);
        varianteTemp.setStockProducto(stock.floatValue());
        varianteTemp.setPrecioVenta(precioVenta);
        varianteTemp.setPrecioCompra(precioCompra);
        varianteTemp.setCodigoBarra(codigoBarra);
        varianteTemp.setLista(additionalPriceProductList);
        varianteTemp.setPVMultiple(bPVMultiple);
        asyncVariantes.ActualizarVariantePorId(varianteTemp);
        dialogCarga=progressDialog.getProgressDialog(getActivity(),"Actualizando variante");
        dialogCarga.show();
    }

    String nuevoValor;
    int idItemAct;
    int numItemAct;
    @Override
    public void RecepcionaValor(@NotNull String valor, int idItem, int numItem, int IdProduct) {
        this.nuevoValor=valor.toUpperCase();
        this.idItemAct=idItem;
        this.numItemAct=numItem;
        asyncVariantes.GuardarNuevoValorVariante(valor.toUpperCase(),idItem,this.idProduct);
    }

    @Override
    public void ActualizoValorOpcionExito() {

        ValorOpcionVariante a=new ValorOpcionVariante();
        a.setDescripcion(nuevoValor);

        opcionVarianteList.get(numItemAct-1).getListValores().add(a);
        new AlertDialog.Builder(getActivity()).setTitle("Confirmación").setMessage("Se generaron los nuevos productos variantes").setPositiveButton("Salir",null).create().show();
            rvAdapterVariantesOpciones.notifyDataSetChanged();
    }

    @Override
    public void ErrorActulizoValorOpcion() {
        new AlertDialog.Builder(getActivity()).setTitle("Advertencia").setMessage("No se agrego el valor. Es posible que ya se encuentre agregado").setPositiveButton("Salir",null).create().show();
        rvAdapterVariantesOpciones.notifyDataSetChanged();
    }

    @Override
    public void ResultadoListaVariantes(List<Variante> resultado) {

        setVariantes(resultado);
    }

    public interface ListenerPanel{

        public void listenerStatePanel(boolean state);

    }


    public void setListenerProductoVariante(ListenerProductoVariante listenerProductoVariante){
        this.listenerProductoVariante=listenerProductoVariante;
    }

    @Override
    public void getVarianteClick(int position) {
        this.positionSelected=position;

    }

    @Override
    public void ObtenerConfiguracionVariantes(mProduct product) {

    }

    @Override
    public void ResultadoActualizarVariante(byte resultado) {
        dialogCarga.hide();
        if(resultado==100){
            try {
                varianteList.get(positionSelected).setStockProducto(varianteTemp.getStockProducto());
                varianteList.get(positionSelected).setPrecioVenta(varianteTemp.getPrecioVenta());
                varianteList.get(positionSelected).setPrecioCompra(varianteTemp.getPrecioCompra());
                varianteList.get(positionSelected).setCodigoBarra(varianteTemp.getCodigoBarra());
                varianteList.get(positionSelected).setPVMultiple(varianteTemp.isPVMultiple());
                rvAdapterVariantesProducto.ActualizarVariante(positionSelected, varianteTemp);
                rvAdapterVariante.ActualizarVariante(positionSelected, varianteTemp);

            }
            catch (Exception e){
                e.toString();
            }
        }
        else if(resultado==99){
            Toast.makeText(getActivity(),"Error al guardar la informacion.",Toast.LENGTH_SHORT).show();
        }
        else if(resultado==98){
            Toast.makeText(getActivity(),"Error al guardar la informacion.Verifique su conexión a internet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ResultadoEliminarVariante(byte resultado)
    {
        dialogCarga.hide();
        if(resultado==100){
            varianteList.remove(positionSelected);
            rvAdapterVariante.EliminarVariante(positionSelected);
            rvAdapterVariantesProducto.EliminarVariante(positionSelected);
            positionSelected=-1;
            txtNumVariantes.setText("Num Variantes:"+ "\n" +String.valueOf(varianteList.size()));

        }
        else if(resultado==99){
            Toast.makeText(getActivity(),"Error al guardar la informacion.",Toast.LENGTH_SHORT).show();
        }
        else if(resultado==98){
            Toast.makeText(getActivity(),"Error al guardar la informacion.Verifique su conexión a internet",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void GenerarVariantes(List<Variante> varianteList) {
        dialogCarga.hide();
        setVariantes(varianteList);
    }

    @Override
    public void ResultadoGuardarOpcion(OpcionVariante opcionVariante) {
        dialogCarga.hide();
        if(rvAdapterVariantesOpciones.getItemCount()<3){
            listOpcionVariante.Insertar(opcionVariante);
            rvAdapterVariantesOpciones.AddElement1(listOpcionVariante.getOpcionVarianteList());
        }
    }

    @Override
    public void ResultadoEliminarOpcion(byte respuesta) {
        dialogCarga.hide();
        if(respuesta==100){

            rvAdapterVariantesOpciones.DeleteElement(positionDeleted);
            listOpcionVariante.ChangeNumItem();
            positionDeleted=-10;

        }

    }

    @Override
    public void ResultadoGuardarValores(OpcionVariante opcionVariante) {

        dialogCarga.hide();
        listOpcionVariante.getList().get(opcionVariante.getiNumIntem()-1).setListValores(listValoresVariantes.getList());
        for(int i=0;i<opcionVariante.getListValores().size();i++){
            listaLabel.add(opcionVariante.getListValores().get(i).getDescripcion());
        }
        rvAdapterVariantesOpciones.ModificarTextoInferior(listaLabel,opcionVariante.getiNumIntem());
        listaLabel.clear();
    }

    @Override
    public void ActualizarEstadoVariante(byte respuesta) {
        dialogCarga.hide();
        if(respuesta==100){
            EstadoVariante=EstadoVarianteTemp;
            sActivarVariantes.setChecked(EstadoVariante);
            if(EstadoVariante){
                sActivarVariantes.setText("Variantes:Activo");
            }
            else{
                sActivarVariantes.setText("Variantes:Desactivado");
                varianteList.clear();
                rvAdapterVariante.AgregarVariantes(varianteList);
                rvAdapterVariantesProducto.AddElement(varianteList);
                positionSelected=-11;
            }

        }
        else if(respuesta==99){

            Toast.makeText(getActivity(),"Error al actualizar.",Toast.LENGTH_LONG).show();

        }
        else if(respuesta==98){

            Toast.makeText(getActivity(),"Error al actualizar.Verifique su conexión a internet",Toast.LENGTH_LONG).show();

        }


    }


    public interface ListenerProductoVariante{

        public void GetInfoVariantes(boolean EstadoVariantes, List<OpcionVariante> opcionVarianteList,boolean estadoConfigVaria);
        void GenerarVariantes();
    }


    public VariantesProducto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_varientes_producto, container, false);
       dialogEditarVariantes=new DialogEditVariantes();
        try {

            progressDialog=new cProgressDialog();
            pbIndicator=v.findViewById(R.id.pbIndicator);
            txtCargando=v.findViewById(R.id.txtCargando);
            contenedorSecundario=v.findViewById(R.id.contenedorSecundario);
            contenedorPrimario=v.findViewById(R.id.contenedorPrimario);
            contenedorPrimario.setVisibility(View.INVISIBLE);
            contenedorSecundario.setVisibility(View.INVISIBLE);
            imgArrow=v.findViewById(R.id.btnArrow);
            txtCargando.setVisibility(View.VISIBLE);
            txtNumVariantes=v.findViewById(R.id.txtNumVariantes);
            productTemp=new mProduct();
            positionDeleted=-10;
            existeVariantes = false;
            numVariantes = 0;
            positionSelected = -1;
            validacionOpcVal = false;
            estadoConfigVaria = false;
            listaEtiquetas = new ArrayList<>();
            opcionVarianteList = new ArrayList<>();
            btnEliminarVariante = (Button) v.findViewById(R.id.btnEliminarVariante);
            btnEditarVariante = (Button) v.findViewById(R.id.btnEditarVariante);
            btnEliminarVariante.setOnClickListener(this);
            btnEditarVariante.setOnClickListener(this);
            varianteList = new ArrayList<>();
            panelVariantes = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
            btnEditarVariantes = (Button) v.findViewById(R.id.btnEdicionVariantes);
            sActivarVariantes = (Switch) v.findViewById(R.id.sActivarVariantes);
            rv = (RecyclerView) v.findViewById(R.id.rvVariantesEditable);
            rvAdapterVariantesProducto = new RvAdapterVariantesProducto();
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(rvAdapterVariantesProducto);
            valoresVariantesList = new ListOpcionVariante();
            listValoresVariantes = new ListValoresVariantes();
            listOpcionVariante = new ListOpcionVariante();
            dialogInsertValorOption = new DialogInsertValorOption();
            dialogInsertValorOption.setListenerValorOpcion(this);
            dialogInsertOption = new DialogInsertOption();
            dialogInsertOption.setAgregarOpcion(this);
            btnAgregarOpcion = (Button) v.findViewById(R.id.btnAgregarOpcion);
            btnAgregarOpcion.setOnClickListener(this);
            rvAdapterVariantesOpciones = new RvAdapterVariantesOpciones();
            rvOpcionesVariantes = (RecyclerView) v.findViewById(R.id.rvOpcionesVariantes);
            rvOpcionesVariantes.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvOpcionesVariantes.setHasFixedSize(false);
            rvOpcionesVariantes.setAdapter(rvAdapterVariantesOpciones);
            rvAdapterVariantesOpciones.setAgregarValores(this);
            rvAdapterVariantesProducto.setClickVariante(this);

            asyncVariantes = new AsyncVariantes();
            sActivarVariantes.setChecked(false);
            EstadoVariante = false;
            btnEditarVariantes.setOnClickListener(this);
            rvOpcionesVariantes.setVisibility(View.VISIBLE);
            sActivarVariantes.setText("Variantes:Desactivado");
            sActivarVariantes.setChecked(EstadoVariante);
            rvAdapterVariante = new RvAdapterVariante();
            rvVariantes = (RecyclerView) v.findViewById(R.id.rvVariantes);
            rvVariantes.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvVariantes.setAdapter(rvAdapterVariante);
            btnGenerarVariantes = (Button) v.findViewById(R.id.btnGenerarVariantes);
            btnGenerarVariantes.setOnClickListener(this);
            dialogEditarVariantes.setInfoModificarListener(this);
            sActivarVariantes.setOnClickListener(this);
            asyncVariantes.setConfigVariantes(this);
            listaLabel=new ArrayList<>();
            EstadoVariante=false;
            imgArrow.setOnClickListener(this);
            pbIndicator.show();
            panelVariantes.addPanelSlideListener(this);
            asyncVariantes.setiActualizaValorOpcion(this);

        }catch (Exception e){
            e.toString();
        }
        return v;
    }

    public void DeshabilitarInterfaz(){
        btnGenerarVariantes.setVisibility(View.GONE);
        sActivarVariantes.setEnabled(false);
        btnAgregarOpcion.setVisibility(View.GONE);
        btnAgregarOpcion.setEnabled(false);

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    public void CambiarEstadoVariante(boolean EstadoVarianteTemp){
        productTemp.setIdProduct(idProduct);
        productTemp.setEstadoVariante(EstadoVarianteTemp);
        asyncVariantes.ActualizarEstadoVariante(productTemp);
    }

    public void VerificarEstadoVarianteDialog(String mensaje){
    final Dialog dialog;

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                     if(EstadoVarianteTemp)
                        dialogCarga=progressDialog.getProgressDialog(getActivity(),"Activando variantes");
                    else
                        dialogCarga=progressDialog.getProgressDialog(getActivity(),"Desactivando variantes");
                    if(!EstadoVarianteTemp) {
                        asyncVariantes.VerificarExisteVariante(idProduct);
                        asyncVariantes.setListenerVerificarExisteVariante(
                                new AsyncVariantes.ListenerVerificarExisteVariante() {
                            @Override
                            public void PermitirEliminar() {
                                CambiarEstadoVariante(EstadoVarianteTemp);

                            }
                            @Override
                            public void NoPermitirEliminar(String mensaje) {
                                dialogCarga.dismiss();
                                new AlertDialog.Builder(getActivity())
                                        .setMessage(mensaje.replace("\\n","\n")).
                                        setTitle("Advertencia").
                                        setPositiveButton("Salir",null)
                                        .create().show();
                            }
                        });
                    }else{
                        CambiarEstadoVariante(EstadoVarianteTemp);
                    }
                  dialogCarga.show();
            }
        });
        dialog=builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnArrow:
                panelVariantes.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case R.id.sActivarVariantes:
                if(EstadoVariante){
                    EstadoVarianteTemp=false;
                    sActivarVariantes.setChecked(EstadoVariante);

                    VerificarEstadoVarianteDialog("¿Esta seguro de desactivar la configuración de variantes? Se perderán todas las variantes del producto");
                }
                else{
                    EstadoVarianteTemp=true;
                    sActivarVariantes.setChecked(EstadoVariante);

                    VerificarEstadoVarianteDialog("¿Desea activar la configuración de variantes?");
                }

                break;

            case R.id.btnEdicionVariantes:
                if(varianteList.size()>0) {
                    panelVariantes.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                else{
                    Toast.makeText(getActivity(),"No tiene variantes para editar",Toast.LENGTH_SHORT).show();
                }
                /*
                dialogEditarVariantes.setIdProduct(idProduct);
                DialogFragment dialogFragment=dialogEditarVariantes;
                dialogFragment.show(getActivity().getFragmentManager(),"");*/
                break;
            case R.id.btnEditarVariante:

                if(positionSelected>=0) {
                    try{
                    dialogEditarVariantes.setData(varianteList.get(positionSelected).getIdVariante(),varianteList.get(positionSelected).getStockProducto(),varianteList.get(positionSelected).getPrecioVenta(),varianteList.get(positionSelected).getPrecioCompra(),varianteList.get(positionSelected).getNombreVariante(),varianteList.get(positionSelected).getCodigoBarra(),varianteList.get(positionSelected).isPVMultiple() );
                    DialogFragment dialogFragment=dialogEditarVariantes;
                    dialogFragment.show(getActivity().getFragmentManager(),"EdtVar");
                    }
                    catch (Exception e){
                        e.toString();
                    }
                }
                else{

                    Toast.makeText(getActivity(),"Debe seleccionar una variante de la lista",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnEliminarVariante:
                if(positionSelected>=0){
                    AlertEliminar();
                }
                else{
                    Toast.makeText(getActivity(),"Debe seleccionar una variante de la lista",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnAgregarOpcion:

                if(EstadoVariante){
                    if(varianteList.size()>0){
                        AlertaFaltaValores("Debe eliminar las variantes del producto ,para agregar o eliminar opciones.");
                    }
                    else{
                        if(listOpcionVariante.getList().size()<3){
                            MostrarAgregarOpcion();
                        }
                        else{

                            Toast.makeText(getActivity(),"Se alcanzo el limite de opciones",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{

                    AlertaFaltaValores("Debe activar las variantes para agregar opciones");

                }


                break;

                case R.id.btnGenerarVariantes:

                    if(EstadoVariante) {
                        if(varianteList.size()==0){
                        if (listOpcionVariante.getList().size() > 0) {
                            for (int i = 0; i < listOpcionVariante.getList().size(); i++) {
                                if (listOpcionVariante.getList().get(i).getListValores().size() == 0) {

                                    validacionOpcVal = false;
                                    break;
                                } else {
                                    validacionOpcVal = true;
                                }
                            }

                            if (validacionOpcVal) {
                                dialogCarga=progressDialog.getProgressDialog(getActivity(),"Generando variantes del producto");
                                dialogCarga.show();
                                asyncVariantes.GenerarVariantes(idProduct);
                            } else {
                                AlertaFaltaValores("Todas sus opciones deben tener valores para generar variantes del producto");
                            }
                        } else {
                            AlertaFaltaValores("Debe ingresar opciones y valores para generar variantes del producto");
                        }
                        }
                        else{
                            AlertaFaltaValores("Debe eliminar todas sus variantes existentes para generar nuevas.");
                        }
                    }
                    else{
                        AlertaFaltaValores("Debe activar las variantes para usar esta opcion");
                    }
                    break;

            /*case R.id.btnGenerarVariantes:

                for(int i=0;i<listOpcionVariante.getList().size();i++){

                    if(listOpcionVariante.getList().get(i).getListValores().size()==0){
                        validacionOpcVal=false;
                        break;
                    }
                    else{
                        validacionOpcVal=true;
                    }

                }
                if(validacionOpcVal) {
                    listenerProductoVariante.GenerarVariantes();
                }
                else {
                    AlertaFaltaValores();
                }break;
        */
        }

    }

    public void AlertaFaltaValores(String mensaje){




  /*      AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje).setTitle("Advertencia")
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).setTitle("Advertencia").create().show();
*/
    }



    public void AlertEliminar(){

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Advertencia").setMessage("¿Está seguro de eliminar la variante '"+varianteList.get(positionSelected).getNombreVariante()+"' ?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogCarga=progressDialog.getProgressDialog(getActivity(),"Eliminando variante");
                dialogCarga.show();
                asyncVariantes.EliminarVariante(varianteList.get(positionSelected).getIdVariante());
            }
        }).setNegativeButton("Cancelar",null).create().show();

    }

    public void MostrarAgregarOpcion(){

        DialogFragment fragment=dialogInsertOption;
        fragment.show(((Activity)getActivity()).getFragmentManager(),"Agregar Opcion");

    }

    @Override
    public void obtenerDatoOpcion(String opcion) {
        dialogCarga=progressDialog.getProgressDialog(getActivity(),"Agregando opción");

        dialogCarga.show();
        OpcionVariante opcionVariante=new OpcionVariante();
        opcionVariante.setDescripcion(opcion);
        opcionVariante.setIdProduct(idProduct);
        asyncVariantes.GuardarOpcion(opcionVariante);
    /*    if(rvAdapterVariantesOpciones.getItemCount()<3){
            listOpcionVariante.InsertarOpcion(opcion);
            rvAdapterVariantesOpciones.AddElement1(listOpcionVariante.getOpcionVarianteList());
        }
    */}

    public SlidingUpPanelLayout.PanelState EstadoPantallaExpand(){

        return panelVariantes.getPanelState();
    }
    public void OcultarPanel(){
        panelVariantes.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public void setVariantes(List<Variante> varianteList){
        try{
        this.varianteList=varianteList;
        this.numVariantes=varianteList.size();
        rvAdapterVariante.AgregarVariantes(this.varianteList);
        rvAdapterVariantesProducto.AddElement(this.varianteList);
            txtNumVariantes.setText("Num Variantes:"+ "\n" +String.valueOf(varianteList.size()));
        if(numVariantes>0){
            existeVariantes=true;
            btnGenerarVariantes.setVisibility(View.VISIBLE);
            /*btnGenerarVariantes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"No tiene Accion",Toast.LENGTH_SHORT).show();
                }
            });*/
        }
        else {
            btnGenerarVariantes.setVisibility(View.VISIBLE);
            existeVariantes=false;
            /*btnGenerarVariantes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getActivity(),"tiene Accion",Toast.LENGTH_SHORT).show();
                }
            });*/
        }
        }catch (Exception e){
            e.toString();
        }
    }

    public void setEstadoSwitch(boolean a){
        EstadoVariante=a;
        sActivarVariantes.setChecked(a);
        if(a){
            sActivarVariantes.setText("Variantes:Activado");
        }
        else{
            sActivarVariantes.setText("Variantes:Desactivado");
        }


    }
    public void setConfiguracionVariantes(List<OpcionVariante> opcionVarianteList){
        pbIndicator.hide();
        txtCargando.setVisibility(View.INVISIBLE);
        contenedorPrimario.setVisibility(View.VISIBLE);
        contenedorSecundario.setVisibility(View.VISIBLE);
                this.opcionVarianteList=opcionVarianteList;
                int numItem=0;
                for(int i=0;i<this.opcionVarianteList.size();i++){
                        numItem=opcionVarianteList.get(i).getiNumIntem();
                        for(int a=0;a<this.opcionVarianteList.get(i).getListValores().size();a++){

                            this.opcionVarianteList.get(i).getListValores().get(a).setNumItemPadre(numItem);

                        }
                }
            //    sActivarVariantes.setVisibility(View.GONE);
                listOpcionVariante.setOpcionVarianteList(this.opcionVarianteList);

               rvAdapterVariantesOpciones.AddElement1(opcionVarianteList);

     //   listenerProductoVariante.GetInfoVariantes(EstadoVariante,listOpcionVariante.getList(),estadoConfigVaria);

            btnAgregarOpcion.setVisibility(View.VISIBLE);
    }
    public void SwitchVisible(){
            sActivarVariantes.setVisibility(View.VISIBLE);

    }

    @Override
    public void MostrarInterfaz(int NumItemPadre) {
        if(varianteList.size()>0){
            try {
                new DfAgregarOpcionVariante().Instance(this,
                        listOpcionVariante.getList().get(NumItemPadre - 1)).show(getFragmentManager()
                        , "");
            }catch (Exception e){
                e.toString();
            }
           // listOpcionVariante.getList().
          //  AlertaFaltaValores("Debe eliminar todas la variantes del producto parar modificar las opciones");
        }
        else if(varianteList.size()==0){
            try {
                DialogFragment fragment = dialogInsertValorOption;
                dialogInsertValorOption.setNumPositionPadre(NumItemPadre);
                for (ValorOpcionVariante valorOpcionVariante : listOpcionVariante.getList().get(NumItemPadre - 1).getListValores()) {
                    listaEtiquetas.add(valorOpcionVariante.getDescripcion());
                }
                dialogInsertValorOption.setStringList(listaEtiquetas);
                listaEtiquetas.clear();
                fragment.show(((Activity) getActivity()).getFragmentManager(), "Agregar Opcion");
            }
            catch (Exception e){
                e.toString();
            }
        }
    }

    @Override
    public void DeleteElement(int position) {
        if (varianteList.size() > 0) {
            AlertaFaltaValores("Debe eliminar todas la variantes del producto parar modificar las opciones");
        } else if (varianteList.size() == 0) {
            if (listOpcionVariante.getList().get(position).getListValores().size() == 0) {
                MensajeConfirmacionEliminarOpcion(position);
            } else {
                MensajeAlertaEliminarOpcion(position);
            }
        }
    }
    @Override
    public void setPositionElement(int position) {
    }
    public void setIdProduct(int idProduct){
        this.idProduct=idProduct;

    }

    @Override
    public void ObtenerValores(List<String> listLabel,int NumPositionPadre) {
        dialogCarga=progressDialog.getProgressDialog(getActivity(),"Guardando valores");
        dialogCarga.show();
        listValoresVariantes.AgregarValor(listLabel);
        OpcionVariante opcionVariante= listOpcionVariante.getList().get(NumPositionPadre-1);
        opcionVariante.setListValores(listValoresVariantes.getList());
        opcionVariante.setIdProduct(idProduct);
        opcionVariante.setiNumIntem(NumPositionPadre);
        asyncVariantes.GuardarValorOpcion(opcionVariante);


     //
    }

    public void ActivarEdicion(){
        sActivarVariantes.setEnabled(true);
        btnAgregarOpcion.setEnabled(true);
        btnAgregarOpcion.setVisibility(View.VISIBLE);
    }


    public void MensajeAlertaEliminarOpcion(int position){
        Dialog dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()).setTitle("Advertencia").setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setMessage("La opción '"+listOpcionVariante.getList().get(position).getDescripcion()+"' no se puede eliminar.Primero debe borrar sus valores.");

        dialog=builder.create();
        dialog.show();
    }

    public void MensajeConfirmacionEliminarOpcion(int pos){
        position=pos;
        final Dialog dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()).setTitle("Advertencia").setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogCarga=progressDialog.getProgressDialog(getActivity(),"Eliminando opcion");
                dialogCarga.show();
                EliminarOpcion(position);
            }
        }).setMessage("¿Desea eliminar la opción '"+ listOpcionVariante.getList().get(pos).getDescripcion()+"' ?").setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog=builder.create();
        dialog.show();
    }
    public void EliminarOpcion(int position){

        listOpcionVariante.getList().get(position).setIdProduct(idProduct);
        asyncVariantes.EliminarOpcion(listOpcionVariante.getList().get(position));
        positionDeleted=position;


    }
}
