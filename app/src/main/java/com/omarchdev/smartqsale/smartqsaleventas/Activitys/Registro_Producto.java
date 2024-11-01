package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAreasProduccion;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProductos;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.SnackBarsPer;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.ConfigEstadoProductFragment;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.ProductoDBasicos;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.RepresentacionProducto;
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.OpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUnidadMedida;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.github.clans.fab.FloatingActionMenu;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Registro_Producto extends ActivityParent implements View.OnClickListener,
AsyncCategoria.ListenerCategoria, AsyncAreasProduccion.ListenerAreasProduccion, AsyncProducto.GetProduct, AsyncCategoria.ListenerCantidadMaximaPedido {

    Menu menu;
    boolean salir;
    AlertDialog.Builder dialogMensaje;
    MenuItem favoriteItem;
    int idProducto;
    int estado;
    ConfigEstadoProductFragment configEstadoProductFragment;
    boolean campoNombre1,campoCodigo1,campoPrecioVenta1;
    ProductoDBasicos fDatosBasicos;
    RepresentacionProducto representacionProducto;
    boolean EstadoConfigVaria;
    List<OpcionVariante> opcionVarianteList1;
    boolean permitirGuardarVariantes;
    AsyncCategoria asyncCategoria;
    FloatingActionButton fab;
    mProduct product;
    ControladorProductos controladorProductos;
    AsyncProducto asyncProducto;
    SnackBarsPer snackBarsPer;
    DialogCargaAsync cargaAsync;
    Dialog dialog;
    ImagenesController imagenesController;
    com.github.clans.fab.FloatingActionButton buttonEdit,buttonDelete;
    static Context context;
    boolean procesoVariantes;
    byte favorite;
    FloatingActionMenu actionMenu;
    private String CodigoAnterior;
    List<mUnidadMedida> listaUnidades;
    List<mAreaProduccion> listaAreasProduccion;
    Bitmap imgProducto;
    boolean permitirSalir;
    boolean estadoModificar;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    public Registro_Producto() {

        product=new mProduct();
        idProducto=0;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permitirSalir=true;
        setContentView(R.layout.activity_registro__producto);
        salir=false;
        estadoModificar=true;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agregar producto");
        dialogMensaje=new AlertDialog.Builder(this);
        permitirGuardarVariantes=false;
        imagenesController=new ImagenesController();
        asyncProducto=new AsyncProducto(this);
        asyncProducto.setRespuestaProducto(respuestaProducto);
        asyncProducto.setResultadoGuardarProducto(resultadoGuardarProducto);
        asyncCategoria=new AsyncCategoria();
        favorite=0;
        product.setEsFavorito(false);
        EstadoConfigVaria =false;
        cargaAsync=new DialogCargaAsync(this);
        toolbar.inflateMenu(R.menu.menu_registro__producto);
        representacionProducto=new RepresentacionProducto();
        representacionProducto.setListenerRepresentacionProducto(getRepresentacion);
        fab=(FloatingActionButton)findViewById(R.id.fabActionRegistro);
        fab.setOnClickListener(this);
        controladorProductos=new ControladorProductos(getApplicationContext());
        configEstadoProductFragment= new ConfigEstadoProductFragment();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(10);
        listaUnidades=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        actionMenu=(FloatingActionMenu)findViewById(R.id.actionMenu);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        fDatosBasicos=new ProductoDBasicos();
        fDatosBasicos.setListenerDatosBasicos(getDatosBasicos);
        campoNombre1=false;
        campoCodigo1=false;
        campoPrecioVenta1=false;
        opcionVarianteList1=new ArrayList<>();
        estado= getIntent().getExtras().getInt(getResources().getString(R.string.EstadoExistenciaProducto));
        context=this;
        snackBarsPer=new SnackBarsPer(findViewById(R.id.fabActionRegistro),Registro_Producto.this);
        buttonEdit=(com.github.clans.fab.FloatingActionButton)findViewById(R.id.menu_item_edit);
        buttonDelete=(com.github.clans.fab.FloatingActionButton)findViewById(R.id.menu_item_delete);
        buttonDelete.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
        asyncCategoria.setListenerCategoria(this);
        dialog=cargaAsync.getDialogCarga("Cargando categorías");
        dialog.show();
        asyncCategoria.getCategorias();
        asyncProducto.setListenerGetProduct(this);
        asyncCategoria.setDescargarUnidadesMedida(true);
        asyncCategoria.setListenerAreasProduccion(this);
        asyncCategoria.setListenerCantidadMaximaPedido(this);
        if(getResources().getInteger(R.integer.ProductoExistente)==estado){
            idProducto=getIntent().getIntExtra(getResources().getString(R.string.CodigoProducto),0);
            estadoModificar=false;
        }

    }

    @Override
    public void onBackPressed() {

        AlertaSalir();
    }

    AsyncProducto.ResultadoGuardarProducto resultadoGuardarProducto=new AsyncProducto.ResultadoGuardarProducto() {
        @Override
        public void GuardarProductoExito() {
            MensajeAlerta("Artículo guardado con éxito","Confirmación",true);

        }

        @Override
        public void ErrorGuardarProducto(String mensaje) {
            MensajeAlerta(mensaje,"Advertencia",true);
        }
    };


    //Se obtiene la respuesta del guardado
    AsyncProducto.RespuestaProducto respuestaProducto=new AsyncProducto.RespuestaProducto() {
        @Override
        public void RespuestaGuardadoProducto(byte respuesta) {

            if(respuesta==100){

                MensajeAlerta("Artículo guardado con éxito","Confirmación",true);
            }
            else if(respuesta==1){
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Advertencia").
                setMessage("El codigo '"+product.getcKey() +"' de producto ya se encuentra en uso").
                setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setIcon(getResources().getDrawable(R.drawable.alert)).create().show();
                Toast.makeText(Registro_Producto.this,"Codigo de producto existe",Toast.LENGTH_SHORT).show();
                fDatosBasicos.SetErrorCampoCodigo();
            }
            else if(respuesta==99){
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Advertencia").
                setMessage("Error al ingresar el producto").
                setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
            else if(respuesta==98){
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Advertencia").
                setMessage("Error al ingresar el producto.Verifique su conexion").
                setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }

        }

        @Override
        public void ConfirmacionActualizarProducto(byte Resultado) {
            if(Resultado==100){
                if(!procesoVariantes)
                    MensajeAlerta("Artículo actualizado con éxito","Confirmación",true);
                else if(procesoVariantes){

                    asyncProducto.GenerarVariantes(product.getIdProduct());

                }
            }
            else if(Resultado==99){
                Toast.makeText(context,"Error al actualizar",Toast.LENGTH_LONG).show();
            }
            else if(Resultado==98){
                MensajeAlerta("Error al actualizar artículo.Verifique su conexion","Advertencia",false);
            }
            else if(Resultado==0){
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Advertencia").
                setMessage("El codigo '"+product.getcKey() +"' de producto ya se encuentra en uso").
                setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setIcon(getResources().getDrawable(R.drawable.alert)).create().show();
                Toast.makeText(Registro_Producto.this,"Codigo de producto existe",Toast.LENGTH_SHORT).show();
                fDatosBasicos.SetErrorCampoCodigo();
            }
        }
    };
    //Se obtiene los datos basicos
    ProductoDBasicos.ListenerDatosBasicos getDatosBasicos=
    new ProductoDBasicos.ListenerDatosBasicos() {
        @Override
        public void obtenerDatosBasicos(
            String nombreProducto,
            String codigoProducto,
            String codigoBarras,
            String descripcion,
            String observacionProducto,
            BigDecimal stock,
            BigDecimal stockReserva,
            BigDecimal precioCompra,
            BigDecimal precioVenta,
            int idCategoria) {

            product.setcProductName(nombreProducto);
            product.setcKey(codigoProducto);
            product.setCodigoBarra(codigoBarras);
            product.setcAdditionalInformation(descripcion);
            product.setObservacionProducto(observacionProducto);
            product.setStockDisponible(stock);
            product.setStockReserva(stockReserva);
            product.setPrecioCompra(precioCompra);
            product.setPrecioVenta(precioVenta);
            product.setIdCategoria(idCategoria);

        }

        @Override
        public void ValidacionCampos(boolean campoNombre, boolean campoCodigo, boolean campoPrecioVenta) {
            campoNombre1=campoNombre;
            campoCodigo1=campoCodigo;
            campoPrecioVenta1=campoPrecioVenta;
        }
    };
    //Se obtiene los datos de la variante


    RepresentacionProducto.ListenerRepresentacionProducto getRepresentacion=new RepresentacionProducto.ListenerRepresentacionProducto() {
        @Override
        public void obtenerRepresentacionProducto(byte TipoRepresentacion, Bitmap bmpProduct, String ColorSeleccionado, String FormaSeleccionada) {

            product.setTipoRepresentacionImagen(TipoRepresentacion);
            if(product.getTipoRepresentacionImagen()==1){

                bmpProduct=null;
            }
            if(bmpProduct!=null) {
                /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 bmpProduct.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                 */product.setbImage(imagenesController.convertBitmapToByteArray(bmpProduct));

            }
            else {
                product.setbImage(null);

            }

            product.setCodigoColor(ColorSeleccionado);
            product.setCodigoForma(FormaSeleccionada);

        }
    };

    public void AlertaSalir(){
        if(estadoModificar==true){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("¿Desea salir sin guardar los datos del producto?");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }else{
            finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro__producto, menu);
        this.menu=menu;
        favoriteItem=menu.findItem(R.id.actionFavoriteProduct);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }
        else if(id==R.id.actionFavoriteProduct){
            switch (favorite){

                case 0:
                item.setIcon(R.drawable.favorite);
                favorite=1;
                product.setEsFavorito(true);
                break;
                case 1:
                item.setIcon(R.drawable.favorite_outline);

                favorite=0;
                product.setEsFavorito(false);
                break;

            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabActionRegistro:
            boolean permitir=true;
            fDatosBasicos.EstadoGuardar();
            product.setPrecioVenta(fDatosBasicos.getPrecioVenta());

            if(fDatosBasicos.TienePreciosVentaAdiccionales()){

                if(fDatosBasicos.obtenerPreciosAdiccionales().size()>0){
                    campoPrecioVenta1=true;
                    product.setPrecioVenta(new BigDecimal(0));
                }
                else if(fDatosBasicos.obtenerPreciosAdiccionales().size()==0){
                    permitir=false;
                    campoPrecioVenta1=false;
                    fDatosBasicos.AdvertenciaPreciosAdicionales();
                }

            }
            try {
                int r=product.getPrecioVenta().compareTo(new BigDecimal(0));
                boolean b=fDatosBasicos.TienePreciosVentaAdiccionales();

                if (r!=1 && b==false) {
                    permitir = false;
                    new AlertDialog.Builder(this).setTitle("Advertencia")
                        .setMessage("Debe ingresar un precio de venta mayor a cero").setPositiveButton("Aceptar",null).create().show();

                }
            }catch (Exception e){
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }

            if(configEstadoProductFragment.isTipoPack() && fDatosBasicos.TienePreciosVentaAdiccionales()){
                campoPrecioVenta1=false;
                new AlertDialog.Builder(this)
                    .setTitle("Advertencia").setMessage("Los productos tipo pack no pueden usar multiples precios de venta")
                    .setPositiveButton("Salir",null).create().show();
            }


            if(permitir) {
                if (campoCodigo1 == true && campoNombre1 == true) {
                    if(representacionProducto.GetImageBitmap()!=null){
                        product.setbSeModificoImagen(representacionProducto.GetModificoImagen());
                        product.setbImage(imagenesController.convertBitmapToByteArray(representacionProducto.GetImageBitmap()));


                    }
                    product.setbVisibleWeb(configEstadoProductFragment.getVisibleWeb());
                    product.setbControlTiempo(configEstadoProductFragment.getControlTiempo());
                    product.setbControlPeso(configEstadoProductFragment.getControlPeso());
                    product.setpVentaLibre(configEstadoProductFragment.getpVentaLibre());
                    product.setIdUnidadMedida(listaUnidades.get(fDatosBasicos.ObtenerUnidadSeleccionada()).getIdUnidad());
                    product.setDCantidadMaximaPedido(fDatosBasicos.getCantidadMaxima());
                    if (getResources().getInteger(R.integer.NuevoProducto) == estado) {
                        asyncProducto.AgregarMensaje("Guardando producto");

                        product.setControlStock(configEstadoProductFragment.getControlStock());
                        product.setControlPeso(configEstadoProductFragment.getControlPeso());
                        product.setEstadoActivo(configEstadoProductFragment.getEstadoProducto());
                        product.setEstadoVisible(configEstadoProductFragment.getEstadoVisible());
                        product.setTipoPack(configEstadoProductFragment.isTipoPack());
                        product.setUnidadMedida(listaUnidades.get(fDatosBasicos.ObtenerUnidadSeleccionada()).getcDescripcion());
                        product.setIdUnidadMedida(listaUnidades.get(fDatosBasicos.ObtenerUnidadSeleccionada()).getIdUnidad());
                        product.setPriceProductList(fDatosBasicos.obtenerPreciosAdiccionales());
                        product.setMultiplePVenta(fDatosBasicos.TienePreciosVentaAdiccionales());
                        product.setIdSubCategoria(fDatosBasicos.ObteneriIdSubCategoria());
                        product.setpVentaLibre(configEstadoProductFragment.getpVentaLibre());

                        product.setIdAreaProduccion(fDatosBasicos.getIdAreaProducction());
                        asyncProducto.GuardarProducto(product);
                    } else if (getResources().getInteger(R.integer.ProductoExistente) == estado) {
                        asyncProducto.AgregarMensaje("Actualizando producto");
                        product.setControlStock(configEstadoProductFragment.getControlStock());
                        product.setControlPeso(configEstadoProductFragment.getControlPeso());
                        product.setEstadoActivo(configEstadoProductFragment.getEstadoProducto());
                        product.setEstadoVisible(configEstadoProductFragment.getEstadoVisible());
                        product.setTipoPack(configEstadoProductFragment.isTipoPack());
                        product.setUnidadMedida(listaUnidades.get(fDatosBasicos.ObtenerUnidadSeleccionada()).getcDescripcion());
                        product.setPriceProductList(fDatosBasicos.obtenerPreciosAdiccionales());
                        product.setMultiplePVenta(fDatosBasicos.TienePreciosVentaAdiccionales());
                        product.setIdSubCategoria(fDatosBasicos.ObteneriIdSubCategoria());
                        product.setpVentaLibre(configEstadoProductFragment.getpVentaLibre());
                        product.setIdAreaProduccion(fDatosBasicos.getIdAreaProducction());
                        if (CodigoAnterior.equals(product.getcKey())) {
                            asyncProducto.ActualizarSinVerificacionCodigo(product, EstadoConfigVaria);
                        } else {
                            asyncProducto.ActualizarConVerificacionCodigo(product, EstadoConfigVaria);
                        }
                    }
                } else {
                    fDatosBasicos.ValidarCampoNombre(campoNombre1);
                    fDatosBasicos.ValidarCampoCodigo(campoCodigo1);
                    fDatosBasicos.ValidarCampoPrecioVenta(campoPrecioVenta1);


                }
            }
            break;
            case R.id.menu_item_edit:
            Toast.makeText(context,
                "Edición habilitada",Toast.LENGTH_SHORT).show();
            favoriteItem.setEnabled(true);
            fDatosBasicos.HabilitarCampos();
            representacionProducto.habilitarEdicion();
            actionMenu.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            configEstadoProductFragment.DesbloquearBotones();
            estadoModificar=true;

            break;

            case R.id.menu_item_delete:
            if(!product.isEstadoVariante()){
                MensajeAlertarEliminarProducto();}
            else{
                dialogMensaje.setTitle("Advertencia").setPositiveButton("Salir",null).
                setMessage("Debe desactivar las variantes del producto para eliminar al producto").create().show();
            }
            break;

        }


    }



    public void MensajeAlertarEliminarProducto(){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Advertencia");
        builder.setMessage("Desea eliminar el producto '"+product.getcProductName()+"'  ");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                asyncProducto.EliminarProducto(product.getIdProduct());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create();
        builder.show();

    }
    @Override
    public void CategoriasObtenidas(List<mCategoriaProductos> categoriaProductosList) {
        if(categoriaProductosList!=null){
            fDatosBasicos.setListCategorias(categoriaProductosList);
            dialog.dismiss();
            if(getResources().getInteger(R.integer.NuevoProducto)==estado){//Se coloca a los fragment como nuevo producto
                fDatosBasicos.HabilitarCampos();
                actionMenu.setVisibility(View.GONE);
                asyncProducto.ObtenerCodigoGenerado();
                dialog=cargaAsync.getDialogCarga("Obteniendo datos");
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

            }
            else if(getResources().getInteger(R.integer.ProductoExistente)==estado){//Se busca el producto y se prepara los fragments para la edicion
                Toast.makeText(context,"Descargando Producto ",Toast.LENGTH_SHORT).show();
                dialog=cargaAsync.getDialogCarga("Obteniendo producto");
                dialog.show();
                fab.setVisibility(View.GONE);
                fDatosBasicos.DeshabilitarCampos();
                asyncProducto.BuscarProductoPorIdImagen(idProducto);
                actionMenu.setVisibility(View.VISIBLE);
                configEstadoProductFragment.BloquearBotones(); }
        }
        else{
            finish();
            Toast.makeText(context,"Error al descargar información.Verifique su conexión",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void ObtenerUnidadesMedidad(List<mUnidadMedida> listaUnidades) {
        this.listaUnidades=listaUnidades;
        List<String> list=new ArrayList<>();
        fDatosBasicos.UnidadesMedida(this.listaUnidades);
        for(mUnidadMedida unidad:listaUnidades){
        list.add(unidad.getcDescripcion()+"-"+unidad.getcDescripcionLarga());
    }

        fDatosBasicos.ObtenerUnidadesMedida(list);
    }

    @Override
    public void GetCodigoGenerado(String codigoGenerado) {
        dialog.hide();
        fDatosBasicos.ObtenerCodigoGenerado(codigoGenerado);
    }

    @Override
    public void ObtenerUnidadesMedida(List<String> unidadesMedida) {
        dialog.hide();
        fDatosBasicos.ObtenerUnidadesMedida(unidadesMedida);

    }

    @Override
    public void GetProductResultById(mProduct product) {
        dialog.dismiss();
        this.product=product;

        if(product==null){

            Toast.makeText(this,"Error al descargar la informacion",Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"Verifique su conexion",Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            getSupportActionBar().setTitle(product.getcProductName());
            favoriteItem.setEnabled(false);
            configEstadoProductFragment.setInfoProduct(product);
            if(product.isEsFavorito()){
                favoriteItem.setIcon(R.drawable.favorite);
                favorite=1;

            }
            else{
                favoriteItem.setIcon(R.drawable.favorite_outline);
                favorite=0;
            }
            CodigoAnterior=product.getcKey();
            fDatosBasicos.setInfoProduct(this.product);
            representacionProducto.setProductRepresentacion(this.product);

        }
    }

    @Override
    public void ConfimarcionEliminar(byte Respuesta) {

        switch (Respuesta) {
            case 100:
            MensajeAlerta("Artículo '" + product.getcProductName() + "' eliminado con éxito ", "",true);
            break;
            case 99:
            MensajeAlerta("No se logró eliminar el artículo '"+product.getcProductName()+"'. Este se" +
                    " encuentra en  proceso de venta","Advertencia",true);
            break;
            case 0:
            MensajeAlerta("Error al eliminar el articulo. Verifique su conexión","Advertencia",true);
            break;
        }

    }

    public void MensajeAlerta(String mensaje,String titulo,boolean salir){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        if(salir){
            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finish();
                }
            });
        }else{
            builder.setPositiveButton("Confirmar",null);
        }

        builder.create();
        builder.show();

    }

    @Override
    public void ResultadosBusquedaArea(@NotNull List<mAreaProduccion> result) {
        fDatosBasicos.setListAreasProduccion(result);
    }

    @Override
    public void ErrorConsultaAreas() {
        Toast.makeText(this,"Error al encontrar las áreas de produccion.",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void CantidadMaxima(double cantidadMaxima) {
        fDatosBasicos.setCantidadMaximaDefault(cantidadMaxima);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_registro__producto, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment=null;

            switch (position){

                case 0:
                fragment=fDatosBasicos;
                break;
                case 1:
                fragment=representacionProducto;
                break;
                case 2:
                fragment=configEstadoProductFragment;
                break;

            }

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


}


