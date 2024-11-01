package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCaja;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncUsers;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAperturaCaja;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogSelectPrinter;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.df_confi_terminal;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.FragmentInventario;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.VentasFragment;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.clienteFragment;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.proovedorFragment;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.transitionseverywhere.Transition;

import java.math.BigDecimal;

public class PantallaPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DialogAperturaCaja.AperturaCaja,
        AsyncCaja.ListenerAperturaCaja, AsyncUsers.ListenerConfigInit, View.OnClickListener {


    static final int CODE_REQUEST_RESULT = 1;
    FragmentInventario fragmentInventario;
    VentasFragment fragmentVentas;
    DbHelper helper;
    Dialog dialog;
    DialogCargaAsync dialogCargaAsync;
    DialogAperturaCaja dialogAperturaCaja;
    AsyncCaja asyncCaja;
    int id = 0;
    Button btnCerrarSesion;
    Transition set;
    private static int codigoNuevo;
    private static int codigoRecibido;
    NavigationView navigationView;
    View headerView;
    TextView txtNombreEmpresa, txtNombrePropietario;
    Context context;
    MenuItem item;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        helper = new DbHelper(this);
        codigoNuevo = this.getResources().getInteger(R.integer.CodNuevoUsuario);
        codigoRecibido = 0;
        codigoRecibido = getIntent().getIntExtra(this.getResources().getString(R.string.EstadoNuevoUsuario), 0);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setVisibility(View.GONE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        asyncCaja = new AsyncCaja(this);
        asyncCaja.setListenerAperturaCaja(this);
        fragmentInventario = new FragmentInventario();
        fragmentVentas = new VentasFragment();
        dialogCargaAsync = new DialogCargaAsync(this);
        dialogAperturaCaja = new DialogAperturaCaja();
        dialogAperturaCaja.setAperturaCaja(this);
        context = this;
        onNavigationItemSelected(navigationView.getMenu().getItem(1));
        headerView = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.titleAR);
        MenuItem titleConfig = menu.findItem(R.id.titleConfiguraciones);
        MenuItem titleActividad = menu.findItem(R.id.titleActividades);
        MenuItem titleCatalogo = menu.findItem(R.id.titleCatalogos);
        MenuItem titleContacto = menu.findItem(R.id.titleContactos);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TitleApparence), 0, s.length(), 0);
        tools.setTitle(s);
        s = new SpannableString(titleConfig.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TitleApparence), 0, s.length(), 0);
        titleConfig.setTitle(s);
        s = new SpannableString(titleActividad.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TitleApparence), 0, s.length(), 0);
        titleActividad.setTitle(s);
        s = new SpannableString(titleCatalogo.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TitleApparence), 0, s.length(), 0);
        titleCatalogo.setTitle(s);
        s = new SpannableString(titleContacto.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TitleApparence), 0, s.length(), 0);
        titleContacto.setTitle(s);
        getSupportActionBar().setSubtitle(Constantes.Empresa.nombrePropietario + "-" + Constantes.Tienda.nombreTienda);
        FragmentoVentas();
        btnCerrarSesion.setOnClickListener(this);
        try {
            txtNombreEmpresa = headerView.findViewById(R.id.txtNombreEmpresa);
            txtNombreEmpresa.setText(Constantes.Empresa.nombreTienda + "\n  " + Constantes.Tienda.nombreTienda);
            txtNombrePropietario = headerView.findViewById(R.id.txtNombrePropietario);
            txtNombrePropietario.setText(Constantes.Empresa.nombrePropietario);
        } catch (Exception e) {
            //  Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            //  Toast.makeText(this,"HOLA",Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentVentas.VerificarPanelExpandido()) {
            fragmentVentas.comprimirPanel();
        } else {
            if (fragmentVentas.VolverPantallaPrinciparCategorias()) {
            } else {
                Toast.makeText(this, "Adios", Toast.LENGTH_LONG).show();
                super.onBackPressed();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pantalla_principal, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Constantes.Empresa.nombreTienda == null) {
            Intent intent = new Intent(this, Main3Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Inventario) {
            if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.AdministracionProductos)) {
                ActivityListadoProductos();
            } else {
                MensajeAlerta("Advertencia", "No tiene permitido el acceso a está opción.");
            }
        } else if (id == R.id.nav_Ventas) {
            FragmentoVentas();
        } else if (id == R.id.nav_config_web) {
            try {
                Intent intent = new Intent(this, ConfigWebMenu.class);
                startActivity(intent);
            } catch (Exception ex) {
                ex.toString();
            }


        } else if (id == R.id.nav_Config_Product) {
            if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.ConfigAdvProducto)) {
                ActivityConfigProducto();
            } else {
                MensajeAlerta("Advertencia", "No tiene permitido el acceso a está opción.");
            }
        } else if (id == R.id.nav_Impresoras) {

            DialogFragment dialogFragment = new DialogSelectPrinter();
            dialogFragment.show(getFragmentManager(), "Seleccionar Impresora");

        } else if (id == R.id.nav_CuentasCliente) {
            if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.CuentaCorrienteCliente)) {
                MostrarCuentasCliente();
            } else {
                MensajeAlerta("Advertencia", "No tiene permitido el acceso a está opción.");
            }
        } else if (id == R.id.nav_ControlCaja) {
            if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.FujoCaja)) {
                asyncCaja.ObtenerIdCierreCaja();
            } else {
                MensajeAlerta("Advertencia", "No tiene permitido el acceso a está opción.");
            }

        } else if (id == R.id.nav_Pedidos) {

            Intent i = new Intent(this, PedidosTienda.class);
            startActivity(i);

        }else if(id == R.id.nav_lista_mesa){
            Intent intent=new Intent(this,ListadoZonaServicio.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_pedido_resumen) {

            Intent intent=new Intent(this,PedidoReservaResumen.class);
            startActivity(intent);


        } else if (id == R.id.nav_modificadores) {
            ActivityModificadoresConfig();
        } else if (id == R.id.nav_categorias) {
            ActivityCategorias();
        } else if (id == R.id.nav_MediosPago) {
            ActivityListadoMedioPagos();
        } else if (id == R.id.nav_Tiendas) {
            Intent intent = new Intent(this, ListadoTiendas.class);
            startActivity(intent);

        } else if (id == R.id.nav_Clientes) {
            if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.AdminClientes)) {
                ActivityClientes();
            } else {
                MensajeAlerta("Advertencia", "No tiene permitido el acceso a está opción.");
            }

        } else if (id == R.id.nav_Vendedores) {
            if (helper.ObtenerPermiso(Constantes.ProcesosPantalla.AdminVendedores)) {
                ActivityVendedores();
            } else {
                MensajeAlerta("Advertencia", "No tiene permitido el acceso a está opción.");
            }
        } else if (id == R.id.nav_Almacen) {
            Intent intent = new Intent(this, ActivityListadoMovAlmacen.class);
            startActivity(intent);

        } else if (id == R.id.nav_Configuracion_Roles) {
            ActivityListadoRoles();
        } else if (id == R.id.nav_Configuracion_almacen) {
            Intent intent = new Intent(this, Listado_Almacenes.class);
            startActivity(intent);
        } else if (id == R.id.nav_impresoras_areas_atencion) {
            Intent intent = new Intent(this, ListaImpresorasAreas.class);
            startActivity(intent);
        } else if (id == R.id.nav_usuarios) {
            ActivityUsuarios();
        } else if (id == R.id.nav_ReporVendedor) {
            Intent intent = new Intent(this, ReporteVendedor.class);
            startActivity(intent);
        } else if (id == R.id.nav_lista_precios) {
            Intent intent = new Intent(this, ListaPreciosSelect.class);
            startActivity(intent);
        } else if (id == R.id.nav_ReporVentasVendedor) {
            Intent intent = new Intent(this, VentasPorVendedor.class);
            startActivity(intent);
        } else if (id == R.id.nav_ReporVentasXCierre) {
            Intent intent = new Intent(this, ReporteVentasCaja.class);
            startActivity(intent);
        } else if (id == R.id.nav_ReporVentasXCierreDetalle) {
            Intent intent = new Intent(this, ReporteVentasCierre.class);
            startActivity(intent);
        } else if (id == R.id.navReporteIngresoSalidas) {
            Intent intent = new Intent(this, ReporteIngresosRetirosPeriodo.class);
            startActivity(intent);
        } else if (id == R.id.nav_ReporAlmacen) {
            Intent intent = new Intent(this, ReporteAlmacen.class);
            startActivity(intent);
        } else if (id == R.id.nav_soporte_tecnico) {
            Intent intent = new Intent(this, SoporteTecnico.class);
            startActivity(intent);


        } else if (id == R.id.nav_reporte_periodo) {
            Intent intent = new Intent(this, ReportePeriodoTienda.class);
            startActivity(intent);

        } else if (id == R.id.nav_ReporProducto) {
            Intent intent = new Intent(this, ReporteVentasProductos.class);
            startActivity(intent);
        } else if (id == R.id.nav_areas_atencion) {
            Intent intent = new Intent(this, AreasProduccionLista.class);
            startActivity(intent);
        } else if (id == R.id.nav_Stock_Product) {
            Intent intent = new Intent(this, ListadoProductosStock.class);
            startActivity(intent);
        } else if (id == R.id.nav_terminal) {
            new df_confi_terminal().show(getSupportFragmentManager(), "");
        } else if (id == R.id.nav_salir) {

            BdConnectionSql.getSinglentonInstance().finalizar();
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ActivityVendedores() {

        Intent intent = new Intent(this, ListadoVendedores.class);
        startActivity(intent);

    }

    private void ActivityClientes() {
        Intent intent = new Intent(this, ListadoClientes.class);
        startActivity(intent);
    }

    private void MensajeCerrarSesion() {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia").
                setMessage("¿Está seguro de cerrar su sesión?").
                setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.DeleteUserRegister();
                        helper.DeleteAccesoRolAcceso();
                        helper.DeletePrint();
                        finish();

                    }
                }).
                setNegativeButton("Cancelar", null);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void ActivityListadoProductos() {

        Intent intent = new Intent(this, ListadoProductos.class);
        startActivity(intent);

    }

    private void ActivityConfigProducto() {

        Intent intent = new Intent(this, ActivityConfigProducto.class);
        startActivity(intent);

    }

    private void ActivityRegistros() {

        Intent intent = new Intent(this, ActivityRegistros.class);
        startActivity(intent);

    }

    private void ActivityListadoMedioPagos() {
        Intent intent = new Intent(this, ListadoMedioPago.class);
        startActivity(intent);
    }

    private void FragmentoInventario() {

        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        ft1.replace(R.id.content_frame, new FragmentInventario());
        ft1.commit();
    }

    private void FragmentoCompra() {
    }

    private void FragmentoVentas() {
        FragmentManager fml = getSupportFragmentManager();
        FragmentTransaction ftl = fml.beginTransaction();
        ftl.replace(R.id.content_frame, fragmentVentas);
        ftl.commit();
    }

    private void FragmentoCliente() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new clienteFragment()).commit();
    }

    private void FragmentoProveedor() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new proovedorFragment()).commit();
    }

    private void MostrarCuentasCliente() {

        Intent intent = new Intent(this, CtaClientesActivity.class);
        startActivity(intent);

    }

    private void ActivityListadoRoles() {
        Intent intent = new Intent(this, ListadoRoles.class);
        startActivity(intent);
    }

    private void ActivityCategorias() {
        Intent intent = new Intent(this, CategoriasActivity.class);
        startActivity(intent);
    }


    public void MostrarFlujoCaja(mCierre Cierre) {

        Intent intent = new Intent(this, CajaFlujoActivity.class);
        intent.putExtra("idCierre", Cierre.getIdCierre());
        intent.putExtra("cEstadoCierre", Cierre.getEstadoCierre());
        startActivity(intent);
    }

    public void ActivityModificadoresConfig() {
        Intent intent = new Intent(this, ActivityModificadorConfig.class);
        startActivity(intent);
    }

    public void ActivityUsuarios() {
        Intent intent = new Intent(this, ListadoUsuarios.class);
        startActivity(intent);
    }

    @Override
    public void VerificarCajaAbierta(BigDecimal montoApertura) {
        // bdConnectionSql.aperturarCaja(montoApertura);

        asyncCaja.AperturarCaja(montoApertura);
        asyncCaja.setListenerAbrirCaja(new AsyncCaja.ListenerAbrirCaja() {
            @Override
            public void ErrorApertura() {

            }

            @Override
            public void CajaAperturada(int idCierre) {
                Intent intent = new Intent(context, CajaFlujoActivity.class);
                intent.putExtra("idCierre", idCierre);
                intent.putExtra("cEstadoCierre", "A");
                startActivity(intent);
            }

            @Override
            public void ErrorConexion() {

            }
        });


    }

    @Override
    public void ConfirmacionAperturaCaja() {

    }

    @Override
    public void ExisteCierre(mCierre Cierre) {
        MostrarFlujoCaja(Cierre);
    }


    @Override
    public void AperturarCaja() {

        DialogFragment dialogFragment = dialogAperturaCaja;
        dialogFragment.show(getFragmentManager(), "Apertura caja");
    }

    @Override
    public void ConfirmarCierreCaja() {

    }

    @Override
    public void ExisteCajaAbiertaDisponible() {

    }

    @Override
    public void CajaCerrada() {

    }

    @Override
    public void ErrorEncontrarCaja() {

    }

    @Override
    protected void onDestroy() {
        //     bdConnectionSql.closeConnection();
        super.onDestroy();
    }

    @Override
    public void ConfigInit() {
        /*
        final int item=2;

       */
        if (dialog != null) {

            dialog.hide();
        }

    }

    @Override
    public void ConfigInitError() {

    }

    @Override
    public void ConfigInitErrorInternet() {


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCerrarSesion) {
            MensajeCerrarSesion();
        }
    }

    public void MensajeAlerta(String titulo, String mensaje) {

        new AlertDialog.Builder(this).setPositiveButton("Salir", null)
                .setMessage(mensaje).setTitle(titulo).create().show();
    }
}
