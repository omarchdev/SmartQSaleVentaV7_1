package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCaja;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncReporteCierreCaja;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAgregarEntrada;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAperturaCaja;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.detalleFlujoCaja;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.resumenFlujoCaja;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDetalleMovCaja;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mResumenFlujoCaja;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mResumenMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mResumenTotalVentas;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVentasPorHora;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICierreRepository;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CajaFlujoActivity extends ActivityParent implements View.OnClickListener, resumenFlujoCaja.CrearVista, detalleFlujoCaja.VistaCreada, DialogAgregarEntrada.EntradaRetiro, DialogAperturaCaja.AperturaCaja, AsyncCaja.ListenerAperturaCaja, AsyncReporteCierreCaja.ListenerRecuperarReporteCierre {

    final int CODE_RESULT_ID_CAJA = 1;
    AsyncReporteCierreCaja asyncReporteCierreCaja;
    mCierre cierre;
    resumenFlujoCaja flujoCajaresumen;
    detalleFlujoCaja flujoCajaDetalle;
    FloatingActionButton btnAddDinero, btnRetDinero, btnCerrarCaja, btnAbrirCaja, btnGenerarReporte;
    FloatingActionsMenu floatingActionsMenu;
    //BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    DialogAgregarEntrada dialogAgregarEntrada;
    List<mDetalleMovCaja> detalleMovCajaList;
    DialogAperturaCaja aperturaCaja;
    AsyncCaja asyncCaja;
    View view;
    int idCierreCaja = 0;
    Context context;
    final String codeCia = GetJsonCiaTiendaBase64x3();

    Retrofit retro;
    ICierreRepository iCierreRepository;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>)
                                (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(),
                                        (json.getAsString().length() == 23) ? DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS") : DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                                )).create();
                retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
                        .addConverterFactory(GsonConverterFactory.create(gson)).build();
            }
            iCierreRepository = retro.create(ICierreRepository.class);
            context = this;
            setContentView(R.layout.activity_caja_flujo);
            dialogAgregarEntrada = new DialogAgregarEntrada();
            dialogAgregarEntrada.setEntradaRetiroListener(this);
            asyncReporteCierreCaja = new AsyncReporteCierreCaja(this);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            asyncCaja = new AsyncCaja(this);
            asyncCaja.setListenerAperturaCaja(this);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Flujo de caja");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.fab);
            view = findViewById(R.id.main_content);
            flujoCajaresumen = new resumenFlujoCaja();

            flujoCajaDetalle = new detalleFlujoCaja();
            flujoCajaresumen.setCrearVista(this);
            flujoCajaDetalle.setVistaCreada(this);
            detalleMovCajaList = new ArrayList<>();
            aperturaCaja = new DialogAperturaCaja();
            aperturaCaja.setAperturaCaja(this);
            btnAddDinero = findViewById(R.id.btnIngresos);
            btnRetDinero = findViewById(R.id.btnRetiros);
            btnCerrarCaja = findViewById(R.id.btnCerrarCaja);
            btnAbrirCaja = findViewById(R.id.btnAbrirCaja);
            btnGenerarReporte = findViewById(R.id.btnGenerarReporte);
            btnGenerarReporte.setOnClickListener(this);
            btnAddDinero.setOnClickListener(this);
            btnRetDinero.setOnClickListener(this);
            btnCerrarCaja.setOnClickListener(this);
            btnAbrirCaja.setOnClickListener(this);

            cierre = new mCierre();
            cierre.setIdCierre(getIntent().getIntExtra("idCierre", 0));
            cierre.setEstadoCierre(getIntent().getStringExtra("EstadoCierre"));

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

            asyncReporteCierreCaja.setListenerRecuperarReporteCierre(this);

            Toast.makeText(getBaseContext(), "Visible", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.menu_caja_flujo, menu);
            return true;

        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
            return true;
        }
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
    public void onClick(View v) {
        DialogFragment dialogFragment = dialogAgregarEntrada;
        switch (v.getId()) {

            case R.id.btnGenerarReporte:

                asyncReporteCierreCaja.ObtenerReporteCierreId(cierre.getIdCierre(), flujoCajaresumen.GraficoMedioPago(), flujoCajaresumen.GraficoProductos(), flujoCajaresumen.GetBitmapCierres(), flujoCajaresumen.GetBitmapChartVentasHora());

                break;
            case R.id.btnIngresos:
                floatingActionsMenu.collapse();
                dialogAgregarEntrada.setCierre(cierre);
                dialogFragment.show(getFragmentManager(), "Entrada");

                break;

            case R.id.btnRetiros:
                floatingActionsMenu.collapse();
                dialogAgregarEntrada.setCierre(cierre);
                dialogFragment.show(getFragmentManager(), "Retiro");
                break;

            case R.id.btnCerrarCaja:
                DialogCerrarCaja();
                floatingActionsMenu.collapse();
                break;

            case R.id.btnAbrirCaja:
                DialogAperturaCaja();
                floatingActionsMenu.collapse();
                break;

            case R.id.btnSendEmail:

                floatingActionsMenu.collapse();
                break;

            case R.id.btnImprimir:

                floatingActionsMenu.collapse();
                break;

        }


    }

    @Override
    public void VistaCreada() {

    }

    @Override
    public void ObtenerIdCierre(int idCierre) {

        if (idCierre != cierre.getIdCierre()) {
            asyncCaja.ObtenerDatosCierre(idCierre);
            asyncCaja.setObtenerCierre(cierre -> CambiarCierre(cierre));

        }

    }

    public void CambiarCierre(mCierre cierre) {
        Intent intent = new Intent(this, CajaFlujoActivity.class);
        intent.putExtra("idCierre", cierre.getIdCierre());
        intent.putExtra("", cierre.getEstadoCierre());
        startActivity(intent);
        finish();
        ;
    }

    @Override
    public void seCreoVista() {

        new DescargarCabereceResumen().execute(cierre.getIdCierre());


    }

    @Override
    public void EstadoFloatingButton(boolean estado) {

        VisibilidadBotonFloat(estado);
    }

    protected void VisibilidadBotonFloat(boolean estado) {
        if (estado == false) {

            if (floatingActionsMenu.getVisibility() == View.VISIBLE)
                floatingActionsMenu.setVisibility(View.GONE);


        } else if (estado == true) {
            if (floatingActionsMenu.getVisibility() == View.GONE)
                floatingActionsMenu.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void InformacionRetiroEntrada() {
        new ResumenMovCaja().execute(cierre.getIdCierre());
        Snackbar snackbar = Snackbar.make(findViewById(R.id.fab), "Se realizo el movimiento", Snackbar.LENGTH_LONG)
                .setAction("Action", null).setDuration(4000);

        View snack = snackbar.getView();
        snack.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        /*TextView tv = (TextView) snack.findViewById(android.support.design.R.id.snackbar_text);

        tv.setTextColor(Color.parseColor("#ffffff"));
        */
        snackbar.show();


    }

    @Override
    public void VerificarCajaAbierta(BigDecimal montoApertura) {

        asyncCaja.AperturarCaja(montoApertura);
        asyncCaja.setListenerAbrirCaja(new AsyncCaja.ListenerAbrirCaja() {
            @Override
            public void ErrorApertura() {

            }

            @Override
            public void CajaAperturada(int idCierre) {

                cierre.setIdCierre(idCierre);
                Intent intent = new Intent(context, CajaFlujoActivity.class);
                intent.putExtra("idCierre", cierre.getIdCierre());
                intent.putExtra("EstadoCierre", "A");
                finish();
                //   CargarActivity();
            }

            @Override
            public void ErrorConexion() {

            }
        });
    }

    public void VisualizarCajaAbierta() {

        finish();
        //      startActivity(intent);

    }

    @Override
    public void ConfirmacionAperturaCaja() {

        asyncCaja.ObtenerIdCierreCaja();

    }

    @Override
    public void ExisteCierre(mCierre Cierre) {
        cierre.setIdCierre(Cierre.getIdCierre());
        CargarActivity();
    }

    @Override
    public void AperturarCaja() {

    }

    @Override
    public void ConfirmarCierreCaja() {
        asyncCaja.ObtenerIdCierreCaja();


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

    private void DialogCerrarCaja() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta").setMessage("¿Desea cerrar la caja actual?").setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                asyncCaja.CerrarCaja(cierre.getIdCierre());
            }
        }).create().show();

    }

    private void DialogAperturaCaja() {

        DialogFragment dialogFragment = aperturaCaja;
        dialogFragment.show(getFragmentManager(), "Apertura Caja");

    }

    public void CargarActivity() {
        finish();
        Intent intent = new Intent(this, CajaFlujoActivity.class);
        intent.putExtra("idCierre", cierre.getIdCierre());
        intent.putExtra("EstadoCierre", "A");
        startActivity(intent);


    }


    @Override
    public void ReporteGraficos(@Nullable ArrayList<ProductoEnVenta> productList, @Nullable ArrayList<mCierre> ultimosCierres) {
        flujoCajaresumen.Top10ProductosVendidos(productList);
        flujoCajaresumen.setInfoChartCierres(ultimosCierres);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */


    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);

        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {

                case 0:
                    fragment = flujoCajaresumen;
                    break;
                case 1:
                    fragment = flujoCajaDetalle;
                    break;

            }

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return fragment;
        }


        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                case 0:
                    return "Resumen";

                case 1:
                    return "Detalle";

            }
            return null;

        }

    }

    private class DescargarCabereceResumen extends AsyncTask<Integer, Void, mResumenTotalVentas> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            floatingActionsMenu.setVisibility(View.GONE);
            flujoCajaresumen.OcultarPantalla();

        }

        @Override
        protected mResumenTotalVentas doInBackground(Integer... integers) {
            mResumenTotalVentas resumenTotalVentas = new mResumenTotalVentas();
            try {

                cierre = iCierreRepository.getCabeceraCierreCaja(integers[0], TIPO_CONSULTA, codeCia).execute().body();
                cierre.ConvierteFechaJSON();
                resumenTotalVentas = iCierreRepository.ObtenerCabeceraResumen(integers[0], TIPO_CONSULTA, codeCia).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.toString();
            }
            //   cierre = bdConnectionSql.getCabeceraCierreCaja(integers[0]);
            return resumenTotalVentas;
        }

        @Override
        protected void onPostExecute(mResumenTotalVentas mResumenTotalVentas) {

            super.onPostExecute(mResumenTotalVentas);
            flujoCajaresumen.ProcesoCabeceraResumen(mResumenTotalVentas);
            floatingActionsMenu.setVisibility(View.VISIBLE);
            flujoCajaresumen.MostrarPantalla();
            if (cierre.getEstadoCierre().equals("A")) {
                btnAbrirCaja.setVisibility(View.GONE);
            } else if (cierre.getEstadoCierre().equals("C")) {
                btnAddDinero.setVisibility(View.GONE);
                btnRetDinero.setVisibility(View.GONE);
                btnAbrirCaja.setVisibility(View.VISIBLE);
                btnCerrarCaja.setVisibility(View.GONE);
            }

            asyncReporteCierreCaja.ObtenerDatosResumenCaja(cierre.getIdCierre());

            new CargarBarChart().execute(cierre.getIdCierre());
        }
    }

    private class ObtenerMovimientosCaja extends AsyncTask<Integer, Void, List<mDetalleMovCaja>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flujoCajaDetalle.OcultarPantalla();
        }

        @Override
        protected List<mDetalleMovCaja> doInBackground(Integer... integers) {
            try {

                return iCierreRepository.getMovimientoCaja(cierre.getIdCierre(), TIPO_CONSULTA, codeCia).execute().body();
            } catch (Exception ex) {
                return new ArrayList();
            }
            //    return bdConnectionSql.getMovimientoCaja(cierre.getIdCierre());

        }

        @Override
        protected void onPostExecute(List<mDetalleMovCaja> mDetalleMovCajas) {
            super.onPostExecute(mDetalleMovCajas);
            flujoCajaDetalle.AgregarListaDetalle(mDetalleMovCajas);
            flujoCajaDetalle.MostrarPantalla();
        }


    }

    private class CargarBarChart extends AsyncTask<Integer, Void, List<mVentasPorHora>> {

        @Override
        protected List<mVentasPorHora> doInBackground(Integer... integers) {
            try {
                List<mVentasPorHora> list = iCierreRepository.getVentasPorHora(integers[0], TIPO_CONSULTA, codeCia).execute().body();
                return list;
            } catch (Exception ex) {
                return new ArrayList();
            }
            // return bdConnectionSql.getVentasPorHora(integers[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<mVentasPorHora> mVentasPorHoras) {
            super.onPostExecute(mVentasPorHoras);
            flujoCajaresumen.ObtenerDatosVentas(mVentasPorHoras);
            new ResumenMovCaja().execute(cierre.getIdCierre());
        }
    }

    private class ResumenMovCaja extends AsyncTask<Integer, Void, Byte> {
        List<mResumenFlujoCaja> flujoCajaList;
        List<mResumenMedioPago> resumenMedioPagoList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Byte doInBackground(Integer... integers) {

            try {
                flujoCajaList = iCierreRepository.getResumenFlujoCaja(integers[0], TIPO_CONSULTA, codeCia).execute().body();
                resumenMedioPagoList = iCierreRepository.getResumenMP(integers[0], TIPO_CONSULTA, codeCia).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // flujoCajaList = bdConnectionSql.getResumenFlujoCaja(integers[0]);
            // resumenMedioPagoList = bdConnectionSql.getResumenMP(integers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);

            flujoCajaresumen.ModificarDatosCabecera(cierre);
            flujoCajaresumen.ModificarResumenFlujoCaja(flujoCajaList, resumenMedioPagoList);

            new ObtenerMovimientosCaja().execute(cierre.getIdCierre());

        }

    }
}


















