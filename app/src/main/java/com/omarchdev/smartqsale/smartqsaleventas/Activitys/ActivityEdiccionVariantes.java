package com.omarchdev.smartqsale.smartqsaleventas.Activitys;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.SearchView;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVariantes;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogScannerCam;
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.VariantesProducto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.OpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Variante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

public  class ActivityEdiccionVariantes extends ActivityParent implements AsyncVariantes.ConfigVariantes, AsyncVariantes.ListenerVariante, SearchView.OnQueryTextListener, DialogScannerCam.ScannerResult, VariantesProducto.ListenerPanel {
    AsyncVariantes asyncVariantes;
    VariantesProducto variantesProducto;
    String nombreProducto;

    int idProduct;
    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ediccion_variantes);

        nombreProducto=getIntent().getExtras().getString("NombreProducto", " Nombre Producto");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#757575\">" + nombreProducto + "</font>")));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.arrow_back_home);
        getSupportActionBar().setElevation(0);

        idProduct = getIntent().getExtras().getInt("IdProduct", 0);
        variantesProducto = new VariantesProducto();
        variantesProducto.setIdProduct(idProduct);
        asyncVariantes = new AsyncVariantes();
        PantallaVariante();
        asyncVariantes.setConfigVariantes(this);
        asyncVariantes.ObtenerConfigVariantes(idProduct);
        asyncVariantes.setListenerVariante(this);
        variantesProducto.setListenerPanel(this);
            }
            catch (Exception e){
                e.toString();
            }
        }


    private void PantallaVariante(){
        FragmentManager fragment=getFragmentManager();

        FragmentTransaction ft=fragment.beginTransaction();
        ft.replace(R.id.content_Edit_Variante,variantesProducto);
        ft.commit();
        /*FragmentManager fml=getFragmentManager();


        FragmentTransaction ftl=fml.beginTransaction();
        ftl.replace(R.id.content_frame,variantesProducto);
        ftl.commit();*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        if(variantesProducto.EstadoPantallaExpand()== SlidingUpPanelLayout.PanelState.EXPANDED) {
            variantesProducto.OcultarPanel();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void ObtenerConfiguracionVariantes(mProduct product) { try{
        product.setIdProduct(idProduct);
        variantesProducto.setConfiguracionVariantes(product.getOpcionVarianteList());

        variantesProducto.setEstadoSwitch(product.isEstadoVariante());
        variantesProducto.setVariantes(product.getVarianteList());
     //  asyncVariantes.getVariantesProduct(idProduct);
        }
        catch (Exception e){
              e.toString();
        }

    }

    @Override
    public void ResultadoActualizarVariante(byte resultado) {

    }

    @Override
    public void ResultadoEliminarVariante(byte resultado) {

    }

    @Override
    public void GenerarVariantes(List<Variante> varianteList) {

    }

    @Override
    public void ResultadoGuardarOpcion(OpcionVariante opcionVariante) {

    }

    @Override
    public void ResultadoEliminarOpcion(byte respuesta) {

    }

    @Override
    public void ResultadoGuardarValores(OpcionVariante opcionVariante) {

    }

    @Override
    public void ActualizarEstadoVariante(byte respuesta) {

    }

    @Override
    public void VariantesProducto(List<Variante> varianteList) {
        variantesProducto.setVariantes(varianteList);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void ResultadoScanner(String resultText) {

    }

    @Override
    public void listenerStatePanel(boolean state) {

        if(!state){
            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#757575\">" + nombreProducto + "</font>")));
        }
        else{
            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#757575\">" + "Edicción de variantes" + "</font>")));
        }
    }
}
