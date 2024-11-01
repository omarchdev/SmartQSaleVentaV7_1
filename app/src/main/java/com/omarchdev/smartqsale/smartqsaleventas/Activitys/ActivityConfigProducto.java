package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogScannerCam;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class ActivityConfigProducto extends ActivityParent implements AsyncProducto.ObtenerProductos, RvAdapter.AccionConfigProduct, DialogScannerCam.ScannerResult, SearchView.OnQueryTextListener, RadioRealButtonGroup.OnPositionChangedListener {

    RecyclerView rvProductos;
    RvAdapter rvAdapter;
    AsyncProducto asyncProducto;
    AVLoadingIndicatorView avLoadingIndicatorView;
    final byte metodoTodos=100;
    final byte metodoBusqueda=103;
    byte metodoRealizar=0;
    SearchView searchView;
    DialogScannerCam dialogScannerCam;
    EditText searchBox;
    RadioRealButtonGroup rgVar2;
    Context context;
    int positionSelected;
    String busqueda;

    @Override
    public MenuInflater getMenuInflater() {

        return super.getMenuInflater();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_listado_productos,menu);
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView=(SearchView)menu.findItem(R.id.searchToolbar1).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        int searchimgId=getResources().getIdentifier ("android:id/search_button", null, null);
        int imageId=getResources().getIdentifier ("android:id/search_close_btn", null, null);
        int searchTextId = getResources().getIdentifier ("android:id/search_src_text", null, null);
        searchBox=((EditText) searchView.findViewById (searchTextId));
        ImageView searchClose=((ImageView)searchView.findViewById(imageId));
        ImageView imgSearch=((ImageView)searchView.findViewById(searchimgId));


        searchView.setQueryHint("Busqueda de producto");
        searchView.setSearchableInfo(searchableInfo);
        searchView.setOnQueryTextListener(this);

        imgSearch.setColorFilter(getResources().getColor(R.color.colorAccent));
        searchClose.setColorFilter(getResources().getColor(R.color.colorAccent));
        searchBox.setHintTextColor(getResources().getColor(R.color.colorAccent));
        searchBox.setTextColor(getResources().getColor(R.color.colorAccent));
        searchBox.setHighlightColor(getResources().getColor(R.color.colorAccent));
        searchBox.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorAccent));
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_producto);

        getSupportActionBar().setTitle("Listado artículos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#757575\">" + "Listado productos"+ "</font>")));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.arrow_back_home);
        getSupportActionBar().setElevation(0);
        dialogScannerCam =new DialogScannerCam();
        dialogScannerCam.setScannerResult(this);
        positionSelected=0;
        busqueda="";
        metodoRealizar=(byte)100;
        avLoadingIndicatorView=(AVLoadingIndicatorView) findViewById(R.id.pbIndicator);
        rvProductos=(RecyclerView)findViewById(R.id.rvListadoProductos);
        rvAdapter=new RvAdapter(this,1);
        rvAdapter.setAccionConfigProduct(this);
        rvAdapter.setPermitirAccion(true);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        rvProductos.setAdapter(rvAdapter);
        asyncProducto=new AsyncProducto(this);
        rgVar2=findViewById(R.id.rgVar2);
        asyncProducto.setListenerObtenerProductos(this);
        rgVar2.setVisibility(View.INVISIBLE);
        rgVar2.setPosition(0);
        context=this;
        asyncProducto.setListenerObtenerProductos(this);
        rgVar2.setOnPositionChangedListener(this);
        rvProductos.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView.show();

        CargaLista();
    }

    public void CargaLista(){
        asyncProducto.BusquedaProductosConfigAvanzada(metodoRealizar,busqueda);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CargaLista();
        avLoadingIndicatorView.show();
        rvProductos.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onPause() {
        super.onPause();
        rgVar2.setVisibility(View.INVISIBLE);
    }

    @Override
    public void ObtenerListaProductos(List<mProduct> mProductList) {

        rgVar2.setVisibility(View.VISIBLE);
        rvProductos.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.hide();
        if (mProductList != null) {

            rvAdapter.AddProduct(mProductList);
        }

    }


    @Override
    public void ObtenerIdAccion(int idProducto, int Accion,String nombre,
                                boolean esVariante,boolean esPack,boolean estadoModificador) {

        //Accion -> 1 =Edicion variante
        switch ( Accion){

            case 1:
                if(esPack){
                    Toast.makeText(this,"No puede configurar está opcion.El producto es de tipo pack/combo",Toast.LENGTH_LONG).show();
                }
                else{
                    if(estadoModificador){
                     Toast.makeText(this,"No puede configurar está opcion debe deshabilitar las variantes",Toast.LENGTH_SHORT).show();
                    }else {
                        EdiccionVariante(idProducto, nombre);
                    }
                }
                break;
            case 2:
                if(esVariante){
                    Toast.makeText(this,"No puede configurar está opcion.Debe desactivar las variantes del producto",Toast.LENGTH_LONG).show();
                }
                else{
                    if(esPack) {
                        EditPackProduct(idProducto, nombre);
                    }
                    else{
                        Toast.makeText(this,"Debe cambiar el estado de producto a pack/combo",Toast.LENGTH_LONG).show();

                    }
                }
                break;
            case 3:
                if(esVariante){
                    Toast.makeText(this,"Debe deshabilitar las variantes para ingresar a los modificadores",Toast.LENGTH_LONG).show();
                }
                else if(esPack){
                    Toast.makeText(this,"El producto no debe ser tipo PACK/COMBO,para usar la opción modificadores.",Toast.LENGTH_LONG).show();

                }
                else {
                    EditModificadores(idProducto, nombre);
                }
                break;
        }


    }

    @Override
    public void ConfigPorTienda(int idProducto, String nombre) {
        Intent intent=new Intent(this,ConfigProductVisibilidadTienda.class);
        intent.putExtra("idProducto",idProducto);
        intent.putExtra("nombreProducto",nombre);
        startActivity(intent);

    }



    public void EditPackProduct(int idProduct,String Nombre){
        Intent intent=new Intent(this,ActivityConfigPack.class);
        intent.putExtra("IdProducto",idProduct);
        intent.putExtra("NombreProducto",Nombre);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void EdiccionVariante(int idProduct,String nombre){


        Intent intent=new Intent(this,ActivityEdiccionVariantes.class);
        intent.putExtra("IdProduct",idProduct);
        intent.putExtra("NombreProducto",nombre);
        startActivity(intent);


    }

    private void EditModificadores(int idProduct,String nombre){


        Intent intent= new Intent(this,ConfigModificadorProducto.class);
        intent.putExtra("IdProducto",idProduct);
        intent.putExtra("NombreProducto",nombre);
        startActivity(intent);


    }

    @Override
    public void ResultadoScanner(String resultText) {

    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        busqueda = s;
        asyncProducto.BusquedaProductosConfigAvanzada(metodoRealizar, busqueda);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if(s.isEmpty()) {
            busqueda = s;
            asyncProducto.BusquedaProductosConfigAvanzada(metodoRealizar, busqueda);
        }

        return false;
    }

    @Override
    public void onPositionChanged(RadioRealButton button, int currentPosition, int lastPosition) {
        rvProductos.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView.show();
        asyncProducto.CancelarBusqueda();

        switch (currentPosition){

            case 0:

                metodoRealizar=(byte)100;
                break;

            case 1:
               metodoRealizar=(byte)109;
               break;

            case 2:

                metodoRealizar=(byte)110;

                break;

        }

        asyncProducto.BusquedaProductosConfigAvanzada(metodoRealizar,busqueda);
    }
}
