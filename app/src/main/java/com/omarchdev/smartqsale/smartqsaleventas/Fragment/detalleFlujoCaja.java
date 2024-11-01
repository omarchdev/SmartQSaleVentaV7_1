package com.omarchdev.smartqsale.smartqsaleventas.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.omarchdev.smartqsale.smartqsaleventas.Model.mDetalleMovCaja;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterMovimientoCaja;

import java.util.List;

public class detalleFlujoCaja extends Fragment {

    RecyclerView rv;
    ProgressBar progressBar;
    VistaCreada vistaCreada;
    List<mDetalleMovCaja> list;
    RvAdapterMovimientoCaja rvAdapterMovimientoCaja;



    public detalleFlujoCaja() {
        // Required empty public constructor
}

    public void setVistaCreada(VistaCreada vistaCreada) {
        this.vistaCreada = vistaCreada;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalle_flujo_caja, container, false);
        rv = (RecyclerView) v.findViewById(R.id.rvDetalleMovimientoC);
        rvAdapterMovimientoCaja = new RvAdapterMovimientoCaja();
        progressBar = (ProgressBar) v.findViewById(R.id.pbDCaja);

        rv.setAdapter(rvAdapterMovimientoCaja);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    vistaCreada.EstadoFloatingButton(false);
                } else if (dy < 0) {
                    vistaCreada.EstadoFloatingButton(true);
                }
            }
        });


        vistaCreada.seCreoVista();
        return v;
    }

    public void OcultarPantalla() {

        rv.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void AgregarListaDetalle(List<mDetalleMovCaja> list) {
        this.list = list;
        rvAdapterMovimientoCaja.AddElement(list);
        rvAdapterMovimientoCaja.notifyDataSetChanged();

    }

    public void MostrarPantalla() {

        progressBar.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);

    }

    public void AgregarNuevoMovimiento(mDetalleMovCaja movCaja) {
        rvAdapterMovimientoCaja.AddMov(movCaja);
    }

    public interface VistaCreada {

        public void seCreoVista();

        public void EstadoFloatingButton(boolean estado);
    }

}
