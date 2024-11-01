package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto;
import com.omarchdev.smartqsale.smartqsaleventas.CategoriaAdapter;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.OrderListKt;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DfPrecioCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogEditInsertProductPack;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.cProgressDialog;
import com.omarchdev.smartqsale.smartqsaleventas.Model.CategoriaPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ConfigPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterProductosSelect;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class ActivityConfigPack extends ActivityParent implements View.OnClickListener,
        AsyncProducto.ListenerConfiguracionPack, SearchView.OnQueryTextListener,
        AdapterView.OnItemSelectedListener, DialogEditInsertProductPack.GuardarDatosProductoPack,
        AsyncProducto.ActualizoEstadoComboRapido, AsyncProducto.ListenerConfigProducto,
        DfPrecioCategoria.ListenerGuardadoPrecio, AsyncProducto.ListenerGuardarPreciosCategoria, TextWatcher {


    boolean EstadoComboTemp;
    DialogEditInsertProductPack dialogEditInsertProductPack;
    SlidingUpPanelLayout slidingPanel;
    SearchView svFiltroProductPack;
    RvAdapterProductosSelect adapterProductPack;
    RvAdapterProductosSelect adapterProductosSelect;
    RecyclerView rvProductPack, rvProductSelect;
    Button btnAgregarProducto, btnEliminarProducto, btnAgregarPack, btnCancelarInsertar, btnModificar, btnOrdenar;
    ImageButton imgHidePanel;
    int idProducto;
    String NombreProducto;
    int PositionPack;
    int PositionSelectProduct;
    AsyncProducto asyncProducto;
    List<mProduct> productList, productPack;
    List<mCategoriaProductos> listCategorias;
    Spinner spCategorias;
    CategoriaAdapter categoriaAdapter;
    EditText searchBox;
    boolean existeProducto;
    AlertDialog.Builder dialogBuilder;
    Dialog dialog;
    cProgressDialog progressDialog;
    RelativeLayout contentPack;
    AVLoadingIndicatorView pbIndicator;
    TextView txtCargando;
    mProduct product;
    boolean click;
    Switch sComboRapido;
    EditText edtParametro;
    int idCategoria;
    String parametro;
    SearchView svProduct;

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_config_pack, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_pack);
        sComboRapido = findViewById(R.id.sComboRapido);
        btnOrdenar = findViewById(R.id.btnOrdenar);
        sComboRapido.setOnClickListener(this);
        edtParametro = findViewById(R.id.edtParametro);
        idCategoria = 0;
        parametro = "";
        click = false;
        progressDialog = new cProgressDialog();
        productList = new ArrayList<>();
        listCategorias = new ArrayList<>();
        productPack = new ArrayList<>();
        dialogEditInsertProductPack = new DialogEditInsertProductPack();
        dialogEditInsertProductPack.setListenerGuardarProducto(this);
        txtCargando = (TextView) findViewById(R.id.txtCargando);
        dialogBuilder = new AlertDialog.Builder(this);
        idProducto = getIntent().getIntExtra("IdProducto", 0);
        NombreProducto = getIntent().getStringExtra("NombreProducto");
        imgHidePanel = (ImageButton) findViewById(R.id.imgHidePanel);
        btnModificar = (Button) findViewById(R.id.btnModificar);

        try {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            svProduct = findViewById(R.id.svProduct);
            svProduct.setQueryHint("Busqueda de cliente");
            svProduct.setQuery("", false);
            svProduct.onActionViewExpanded();
            svProduct.setOnQueryTextListener(this);

        } catch (Exception e) {
            e.toString();
        }

        pbIndicator = (AVLoadingIndicatorView) findViewById(R.id.pbIndicator);
        btnAgregarPack = (Button) findViewById(R.id.btnAgregarPack);
        btnCancelarInsertar = (Button) findViewById(R.id.btnCancelarInsertar);
        btnAgregarPack.setOnClickListener(this);
        btnCancelarInsertar.setOnClickListener(this);
        asyncProducto = new AsyncProducto(this);
        rvProductPack = (RecyclerView) findViewById(R.id.rvProductPack);
        btnAgregarProducto = (Button) findViewById(R.id.btnAgregarProducto);
        btnEliminarProducto = (Button) findViewById(R.id.btnEliminarProducto);
        rvProductSelect = (RecyclerView) findViewById(R.id.rvSelectProduct);
        slidingPanel = null;
        if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {

            slidingPanel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        }

        spCategorias = (Spinner) findViewById(R.id.spinnerCategoria);
        categoriaAdapter = new CategoriaAdapter(this, R.layout.support_simple_spinner_dropdown_item, listCategorias);
        spCategorias.setAdapter(categoriaAdapter);
        adapterProductosSelect = new RvAdapterProductosSelect();
        adapterProductPack = new RvAdapterProductosSelect();
        contentPack = (RelativeLayout) findViewById(R.id.contentPack);
        PositionPack = -10;
        PositionSelectProduct = -10;
        rvProductSelect.setAdapter(adapterProductosSelect);
        rvProductPack.setAdapter(adapterProductPack);
        btnOrdenar.setOnClickListener(this);
        rvProductPack.setLayoutManager(new LinearLayoutManager(this));
        rvProductSelect.setLayoutManager(new LinearLayoutManager(this));
        edtParametro.addTextChangedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle( NombreProducto);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.arrow_back_home);
        getSupportActionBar().setElevation(0);


        adapterProductosSelect.setListenerSelectedProduct(listenerSelectedProduct);
        adapterProductPack.setListenerSelectedProduct(selectPack);
        btnAgregarProducto.setOnClickListener(this);
        btnEliminarProducto.setOnClickListener(this);
        asyncProducto.setListenerConfiguracionPack(this);
        asyncProducto.getConfigPack(idProducto);
        contentPack.setVisibility(View.INVISIBLE);
        pbIndicator.show();
        txtCargando.setVisibility(View.VISIBLE);
        // svFiltroProductPack.setEnabled(false);
        asyncProducto.setActualizoEstadoComboRapido(this);
        btnAgregarPack.setFocusable(true);
        adapterProductPack.MostrarCantidad(true);
        btnModificar.setOnClickListener(this);
        if (slidingPanel != null) {
            slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                @Override
                public void onPanelSlide(View panel, float slideOffset) {

                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                    if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        //      svFiltroProductPack.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

                    } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        //    svFiltroProductPack.setInputType(InputType.TYPE_NULL);
                    }
                }
            });
        }

        asyncProducto.setListenerConfigProducto(this);
        asyncProducto.setListenerGuardarPreciosCategoria(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (slidingPanel != null) {
            if (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            } else {
                super.onBackPressed();
            }


        } else {
            super.onBackPressed();
        }
    }

    RvAdapterProductosSelect.ListenerSelectedProduct listenerSelectedProduct = new RvAdapterProductosSelect.ListenerSelectedProduct() {
        @Override
        public void productSelected(int position, int idProduct) {
            PositionSelectProduct = position;
        }
    };

    RvAdapterProductosSelect.ListenerSelectedProduct selectPack = new RvAdapterProductosSelect.ListenerSelectedProduct() {
        @Override
        public void productSelected(int position, int idProduct) {
            PositionPack = position;
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnModificar:

                MostrarDialogEditarProducto();

                break;

            case R.id.btnAgregarProducto:
                if (slidingPanel != null)
                    slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
            case R.id.btnOrdenar:
                OrdenarPack();
                break;

            case R.id.btnEliminarProducto:
                if (PositionPack >= 0) {
                    AlertaMensajeEliminar();
                } else {
                    MostrarDialog("Debe seleccionar un producto de la lista para eliminar", "Advertencia");
                }
                break;
            case R.id.btnCancelarInsertar:

                CancelarInsertarProducto();

                break;

            case R.id.btnAgregarPack:
                MostarDialogInsertarProducto();
                break;

            case R.id.imgHidePanel:
                if (slidingPanel != null)
                    slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case R.id.sComboRapido:
                if (product.isComboSimple()) {
                    this.EstadoComboTemp = false;
                    MostrarMensajeConfirmacion("¿Desea desactivar la opcion combo rapido?");
                    this.sComboRapido.setChecked(this.product.isComboSimple());
                    return;
                }
                this.EstadoComboTemp = true;
                MostrarMensajeConfirmacion("¿Desea activar la opcion combo rapido?");
                this.sComboRapido.setChecked(this.product.isComboSimple());
                return;
        }
    }

    public void MostrarDialog(String mensaje, String titulo) {
        dialogBuilder.setTitle(titulo).setMessage(mensaje)
                .setPositiveButton("Salir", null).create().show();
    }

    public void MostrarDialogEditarProducto() {

        if (PositionPack >= 0) {
            try {
                dialogEditInsertProductPack.setData((byte) 101, productPack.get(PositionPack));
                DialogFragment dialogFragment = dialogEditInsertProductPack;
                dialogFragment.show(getSupportFragmentManager(), "Insertar");
            } catch (Exception e) {
                e.toString();
            }
        } else {
            Toast.makeText(this, "Debe seleccionar un producto de la lista para modificar", Toast.LENGTH_SHORT).show();
        }
    }

    public void MostarDialogInsertarProducto() {

        if (PositionSelectProduct >= 0) {
            dialogEditInsertProductPack.setData((byte) 100, productList.get(PositionSelectProduct));
            DialogFragment dialogFragment = dialogEditInsertProductPack;
            dialogFragment.show(getSupportFragmentManager(), "Insertar");
        } else {
            Toast.makeText(this, "Debe seleccionar un producto de la lista para agregar al Pack", Toast.LENGTH_SHORT).show();
        }
    }

    public void AgregarProductoPack() {

        if (PositionSelectProduct >= 0) {

            dialog = progressDialog.getProgressDialog(this, "Agregando producto");
            dialog.show();
            if (slidingPanel != null)
                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            ConfigPack configPack = new ConfigPack();
            configPack.setIdItem(productList.get(PositionSelectProduct).getIdProduct());
            configPack.setIdTipo(idProducto);
            asyncProducto.AgregarProductoPack(configPack);


        } else {
            Toast.makeText(this, "Debe seleccionar un producto de la lista para agregar al Pack", Toast.LENGTH_SHORT).show();
        }
    }

    public void CancelarInsertarProducto() {
        if (slidingPanel != null)
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

    }

    private void EliminarProductoPack() {

        if (PositionPack >= 0) {
            dialog = progressDialog.getProgressDialog(this, "Eliminando producto del pack");
            dialog.show();
            ConfigPack configPack = new ConfigPack();
            configPack.setIdItem(productPack.get(PositionPack).getIdProduct());
            configPack.setIdTipo(idProducto);
            configPack.setNumItem(productPack.get(PositionPack).getNumItem());
            asyncProducto.EliminarProductoPack(configPack);

        } else {
            Toast.makeText(this, "Debe seleccionar un producto para eliminar", Toast.LENGTH_SHORT).show();

        }
    }

    public void AlertaMensajeEliminar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this).
                setMessage("¿Desea eliminar el producto " + productPack.get(PositionPack).getcProductName() + " del pack ?").setTitle("Eliminar")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EliminarProductoPack();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create();
        builder.show();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_config) {
            MostrarConfigPrecio();
        }
        return super.onOptionsItemSelected(item);
    }

    private void MostrarConfigPrecio() {
        mProduct mproduct = this.product;
        if (mproduct == null) {
            return;
        }
        if (mproduct.getIdProduct() != 0) {
            new DfPrecioCategoria()
                    .newInstance(this.product).show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void ConfigProducto(mProduct producto) {
        this.product = producto;
        this.product.setIdProduct(this.idProducto);
        this.sComboRapido.setChecked(producto.isComboSimple());
    }

    @Override
    public void configuracionPack(List<ConfigPack> configPackList) {
        contentPack.setVisibility(View.VISIBLE);
        pbIndicator.hide();
        txtCargando.setVisibility(View.INVISIBLE);
        mProduct product = new mProduct();
        mCategoriaProductos categoria = new mCategoriaProductos();
        PositionPack = -10;
        PositionSelectProduct = -10;
        for (ConfigPack configPack : configPackList) {

            if (configPack.getIdTipo() == 1) {
                categoria = new mCategoriaProductos();
                categoria.setIdCategoria(configPack.getIdItem());
                categoria.setDescripcionCategoria(configPack.getcDescripcion());
                listCategorias.add(categoria);
                categoria = null;
            } else if (configPack.getIdTipo() == 2) {
                product = new mProduct();
                product.setIdProduct(configPack.getIdItem());
                product.setcProductName(configPack.getcDescripcion());
                product.setcKey(configPack.getCodigoProducto());
                product.setPrecioVenta(configPack.getPrecio());
                productList.add(product);
                product = null;
            } else if (configPack.getIdTipo() == 3) {
                product = new mProduct();
                product.setNumItem(configPack.getNumItem());
                product.setIdProduct(configPack.getIdItem());
                product.setcProductName(configPack.getcDescripcion());
                product.setcKey(configPack.getCodigoProducto());
                product.setPrecioVenta(configPack.getPrecio());
                product.setStockDisponible(configPack.getCantidad());
                product.setIdPack(configPack.getIdPack());
                product.setIdTipoModifica(configPack.getIdTipoModifica());
                product.setMontoModifica(configPack.getMontoModifica());
                productPack.add(product);
                product = null;
            }
        }
        adapterProductosSelect.AgregarElementos(productList);
        categoriaAdapter.AddElementConfiguracionPack(listCategorias);
        adapterProductPack.AgregarElementos(productPack);
        spCategorias.setOnItemSelectedListener(this);
        asyncProducto.BusquedaProductoParametroPack(parametro, idCategoria, (byte) 100);
    }


    @Override
    public void ResultadoEliminar(byte resultadoEliminar) {

        dialog.hide();
        if (resultadoEliminar == 100) {
            MostrarDialog("Artículo eliminado", "Confirmación");
            productPack.remove(PositionPack);
            adapterProductPack.EliminarProducto(PositionPack);
            PositionPack = -10;
        } else if (resultadoEliminar == 99) {

            MostrarDialog("No se realizo la operación con éxito.", "Advertencia");

        } else if (resultadoEliminar == 98) {

            MostrarDialog("No se realizo la operación.Verifique su conexión", "Advertencia");

        }

    }

    @Override
    public void ResultadoInsertar(mProduct product) {

        dialog.hide();
        if (product.getMetodoGuardado() == 100) {
            productPack.add(product);
            adapterProductPack.InsertarProducto(product);
            MostrarDialog("Artículo ingresado con exito", "Confirmación");

        } else if (product.getMetodoGuardado() == 101) {

            MostrarDialog("Artículo actualizado con éxito", "Confirmación");

            adapterProductPack.ChangeInfoPosition(PositionPack, product);
        }
    }


    @Override
    public void ResultadoBusqueda(List<mProduct> productList) {
        this.productList = productList;
        adapterProductosSelect.AgregarElementos(productList);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        parametro = s;
        asyncProducto.BusquedaProductoParametroPack(parametro, idCategoria, (byte) 100);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.isEmpty()) {
            parametro = s;
            asyncProducto.BusquedaProductoParametroPack(parametro, idCategoria, (byte) 100);
        }
        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            if (click) {
                idCategoria = 0;

                asyncProducto.BusquedaProductoParametroPack(parametro, idCategoria, (byte) 100);
            }
        } else {
            click = true;
            idCategoria = listCategorias.get(position).getIdCategoria();
            asyncProducto.BusquedaProductoParametroPack(parametro, idCategoria, (byte) 101);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void setDatosProducto(byte metodo, mProduct product) {

        try{
            dialog = progressDialog.getProgressDialog(this, "Agregando producto");
            dialog.show();
            if (slidingPanel != null) {
                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            ConfigPack configPack = new ConfigPack();
            if (metodo == 100) {
                configPack.setIdItem(productList.get(PositionSelectProduct).getIdProduct());

            } else if (metodo == 101) {
                configPack.setIdItem(productPack.get(PositionPack).getIdProduct());
                configPack.setIdPack(productPack.get(PositionPack).getIdPack());
                configPack.setNumItem(productPack.get(PositionPack).getNumItem());
            }
            configPack.setIdTipo(idProducto);
            configPack.setCantidad(product.getStockDisponible());
            configPack.setPrecio(product.getPrecioVenta());
            configPack.setMetodoGuardado(metodo);
            configPack.setMontoModifica(product.getMontoModifica());
            configPack.setIdTipoModifica(product.getIdTipoModifica());
            asyncProducto.AgregarProductoPack(configPack);

        }catch (Exception ex){
            ex.toString();
        }

    }


    public void MostrarMensajeConfirmacion(String Mensaje) {
        new AlertDialog.Builder(this).setMessage(Mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        asyncProducto.ActualizarComboRapido(idProducto,
                                EstadoComboTemp);
                    }
                })
                .setNegativeButton("", null).create().show();
    }


    @Override
    public void ErrorActualizarComboRapido() {

    }

    @Override
    public void ExitoActualizaComboRapido() {

        product.setComboSimple(ActivityConfigPack.this.EstadoComboTemp);
        sComboRapido.setChecked(ActivityConfigPack.this.EstadoComboTemp);
    }

    @Override
    public void GuardarInfoPrecioCategoria(@NotNull ArrayList<CategoriaPack> arrayList) {

        if (arrayList.size() > 0) {

            this.asyncProducto.GuardarPreciosPack(arrayList, this.idProducto);
            this.progressDialog = new cProgressDialog();
            this.dialog = this.progressDialog.getProgressDialog(this, "Guardando información");
            this.dialog.show();
        }


    }

    @Override
    public void ErrorGuardarPrecioCategoria() {
        new AlertDialog.Builder(this).setMessage("Existe un problema al guardar los precios.Verifique su conexion a internet")
                .setTitle("Advertencia").setPositiveButton("Salir", null).create().show();
        dialog.dismiss();
    }

    @Override
    public void ExitoGuardarPrecioCategoria() {
        new AlertDialog.Builder(this).setMessage("Se guardo con éxito los precios")
                .setTitle("Confirmación").setPositiveButton("Salir", null).create().show();
        dialog.dismiss();
    }

    public void OrdenarPack() {


        productPack = OrderListKt.OrdernarList(productPack);
        adapterProductPack.AgregarElementos(this.productPack);


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        parametro = charSequence.toString();
        asyncProducto.BusquedaProductoParametroPack(parametro, idCategoria, (byte) 100);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
