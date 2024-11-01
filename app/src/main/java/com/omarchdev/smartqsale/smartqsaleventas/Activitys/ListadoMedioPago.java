package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterMedioPago;

import java.util.ArrayList;
import java.util.List;

public class ListadoMedioPago extends ActivityParent implements AsyncMedioPago.ListenerMedioPago, View.OnClickListener, RvAdapterMedioPago.ListenerListadoMP {

    RecyclerView rvMedioPago;
    RvAdapterMedioPago rvAdapterMedioPago;
    AsyncMedioPago asyncMedioPago;
    FloatingActionButton fbMedioPago;
    List<mMedioPago> medioPagoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_medio_pago);
        rvMedioPago=findViewById(R.id.rvMedioPago);
        rvMedioPago.setLayoutManager(new LinearLayoutManager(this));
        fbMedioPago=findViewById(R.id.fbMedioPago);

        rvAdapterMedioPago=new RvAdapterMedioPago();
        rvMedioPago.setAdapter(rvAdapterMedioPago);
        asyncMedioPago=new AsyncMedioPago();
        asyncMedioPago.setListenerMedioPago(this);
        fbMedioPago.setOnClickListener(this);
        rvAdapterMedioPago.setListenerListadoMP(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Medios de Pago");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_home);

        fbMedioPago.setVisibility(View.GONE);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        asyncMedioPago.ObtenerMediosPago();
        medioPagoList=new ArrayList<>();
    }

    @Override
    public void ResultadoListaMedioPagos(List<mMedioPago> medioPagoList) {

        if(medioPagoList!=null) {
            this.medioPagoList.clear();
            this.medioPagoList.addAll(medioPagoList);
            rvAdapterMedioPago.AgregarDatos(this.medioPagoList);
        }
        else{

        }
    }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.fbMedioPago){
            ActivityAddEditMedioPago();
        }
    }

    public void ActivityAddEditMedioPago(){

        Intent intent =new Intent(this,RegistroMedioPago.class);
        intent.putExtra("id",0);
        startActivity(intent);
    }

    @Override
    public void setPositionMP(int position) {

        Intent intent =new Intent(this,RegistroMedioPago.class);
        intent.putExtra("id",medioPagoList.get(position).getiIdMedioPago());
        intent.putExtra("idTipoPago",medioPagoList.get(position).getiIdTipoPago());
        intent.putExtra("nombreMP",medioPagoList.get(position).getcDescripcionMedioPago());
        intent.putExtra("nombreImagen",medioPagoList.get(position).getIdImagen());
        intent.putExtra("codigoMP",medioPagoList.get(position).getcCodigoMedioPago());

        startActivity(intent);

    }
}
