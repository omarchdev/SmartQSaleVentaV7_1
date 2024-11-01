package com.omarchdev.smartqsale.smartqsaleventas.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Activitys.Registro_Producto;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProductos;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogProductInformation;
import com.omarchdev.smartqsale.smartqsaleventas.InterfaceDetalleCarritoVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInventario extends Fragment implements View.OnClickListener, TextWatcher, InterfaceDetalleCarritoVenta {

    private final static byte NUEVO_PRODUCTO=0;
    DialogProductInformation dialogProductInformation;
    String parametroBusqueda;
    ControladorProductos c;
    boolean escaneo=true;
    EditText searchView;
    ImageButton btnScan;
    Button btnAgregarProducto;
    Button btnProductosEnReserva;
    RecyclerView rv;
    RvAdapter rvAdapter;
    ProgressBar mProgressBar;
    boolean regreso=false;
    LinearLayoutManager llm;
    List<mProduct> mProductList;
    BdConnectionSql bdConnectionSql;
    ImageButton imgSearch;
    TextView txtTitulo1;
    TextView txtTitulo2;
    int idProducto;
    public FragmentInventario() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        c=new ControladorProductos(getContext());
        View rootView=inflater.inflate(R.layout.fragment_fragment_inventario, container, false);
        parametroBusqueda = "";
        idProducto = 0;
        txtTitulo1=(TextView)rootView.findViewById(R.id.txtTitulo1);
        txtTitulo2=(TextView)rootView.findViewById(R.id.txtTitulo2);
        rv=(RecyclerView)rootView.findViewById(R.id.rvInventario);
        rv.setHasFixedSize(true);
        mProgressBar=(ProgressBar)rootView.findViewById(R.id.progressBar);
        imgSearch=(ImageButton)rootView.findViewById(R.id.btnBuscarProducto);
        llm=new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        bdConnectionSql=new BdConnectionSql();
        btnAgregarProducto=(Button)rootView.findViewById(R.id.btnAgregarProducto);
        btnProductosEnReserva=(Button)rootView.findViewById(R.id.btnReservaProducto);
        btnScan = (ImageButton) rootView.findViewById(R.id.btnScanProduct);
        searchView=(EditText)rootView.findViewById(R.id.sv_Product);
        btnAgregarProducto.setOnClickListener(this);
        btnProductosEnReserva.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        mProductList=new ArrayList<>();
        rvAdapter=new RvAdapter(getContext(),1);
        rv.setAdapter(rvAdapter);
        searchView.addTextChangedListener(this);

        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        imgSearch.setOnClickListener(this);
        rvAdapter.setInterfaceDetalleVenta(this);

        //new DescargarProductos().execute(parametroBusqueda, "");

        return rootView;
    }
    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.btnAgregarProducto){
            Intent intentInventario=new Intent(getContext(), Registro_Producto.class);
            intentInventario.putExtra("Estado",NUEVO_PRODUCTO);
            startActivity(intentInventario);

        }
        if(v.getId()==R.id.btnReservaProducto){

            rvAdapter.clear();

        }
        if(v.getId()==R.id.btnScanProduct){
            ScanCode();
        }
        if(v.getId()==R.id.btnBuscarProducto){

        }
    }

    private void ScanCode(){

        escaneo=true;



    }
    @Override
    public void onActivityResult(final int requestCode,int resultCode,Intent intent){

        super.onActivityResult(requestCode,resultCode,intent);
        IntentResult scanningResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);

        String scanContent=scanningResult.getContents().toString();

        searchView.setText(scanContent);
        rvAdapter.clear();

    }



    @Override
    public void onPause() {
        super.onPause();
        regreso=true;

    }

    @Override
    public void onResume() {
        super.onResume();

        if(regreso) {
            if(!escaneo) {
                rvAdapter.clear();
              //  new DescargarProductos().execute("", "");
            }
        }
        escaneo=false;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        parametroBusqueda = s.toString();
        new DescargarProductos().execute(parametroBusqueda, "");
    }

    @Override
    public void CantidadProductosEnCarrito(int cantidad) {

    }

    @Override
    public void InformacionUltimoProducto(String nombre, String precio) {

    }

    @Override
    public void PasarInformacionProductoaDetalleVenta(int id) {
     }

    public void CargarListaEnPantalla(List<mProduct> list) {

        rvAdapter.AddProduct(list);

    }

    private class DescargarProductos extends AsyncTask<String, Void, List<mProduct>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            rv.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<mProduct> doInBackground(String... strings) {
            if (strings[0].equals("") && strings[1].equals("")) {
                //  return c.obtenerListaProductos();
                return c.getListaProductos();
            } else if (!strings[0].equals("") && strings[1].equals("")) {
                //return c.obtenerProductoPorParametro(strings[0]);
                return c.getListaProductosPorParametro(strings[0]);
            } else if (!strings[0].equals("") && strings[1].equals("res")) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(List<mProduct> mProductList) {
            super.onPostExecute(mProductList);
            mProgressBar.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            CargarListaEnPantalla(mProductList);
        }
    }

}
