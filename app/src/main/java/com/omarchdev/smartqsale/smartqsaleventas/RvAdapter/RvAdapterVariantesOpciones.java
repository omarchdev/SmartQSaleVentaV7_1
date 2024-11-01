package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Model.OpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 02/03/2018.
 */

public class RvAdapterVariantesOpciones extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<OpcionVariante> opcionVarianteList;
    public AgregarValores agregarValores;
     Context context;

    public interface AgregarValores{

        public void MostrarInterfaz(int NumItemPadre);
        public void DeleteElement(int position);
        public void setPositionElement(int position);
    }
    public void setAgregarValores(AgregarValores agregarValores){
        this.agregarValores=agregarValores;
    }
    public RvAdapterVariantesOpciones(){

        opcionVarianteList=new ArrayList<>();
    }


    private class VarianteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTituloOpcion;
        TextView txtContenedorValores;
        Button btnAgregarValores;
        ImageButton btnDeleteElement;

        public VarianteViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            txtTituloOpcion=(TextView)itemView.findViewById(R.id.txtTituloOpcion);
            btnAgregarValores=(Button)itemView.findViewById(R.id.btnAddValoresOpcionVariante);
            txtContenedorValores=(TextView)itemView.findViewById(R.id.txtContenedorValores);
            btnDeleteElement=(ImageButton)itemView.findViewById(R.id.btnDeleteElement);

            btnAgregarValores.setOnClickListener(this);
            btnDeleteElement.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
                if (view.getId() == R.id.btnAddValoresOpcionVariante) {
                    agregarValores.MostrarInterfaz(opcionVarianteList.get(getAdapterPosition()).getiNumIntem());
                }
                else if(view.getId()==R.id.btnDeleteElement){

                    agregarValores.DeleteElement(getAdapterPosition());
                }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opcion_variante,parent,false);
        return new VarianteViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VarianteViewHolder vh=(VarianteViewHolder) holder;

        vh.txtTituloOpcion.setText(opcionVarianteList.get(position).getDescripcion());
        /*if(opcionVarianteList.get(position).getValores().equals("")){
            vh.txtContenedorValores.setText("Debe txtTituloes a la opcion");
        }
        else {

        }*/
        vh.txtContenedorValores.setText(opcionVarianteList.get(position).Contenido());
    }

    @Override
    public int getItemCount() {
        return opcionVarianteList.size();
    }

    public void AddElement(String nombreOpcion){

        this.opcionVarianteList.add(new OpcionVariante(nombreOpcion));
        notifyItemInserted(opcionVarianteList.size()-1);

    }

    public void AddElement1(List<OpcionVariante> opcionVariantes){
        this.opcionVarianteList=opcionVariantes;
        notifyDataSetChanged();
    }

    public void ModificarTextoInferior(List<String> labels, int position){

        position=position-1;
        String cadena="";
        int longitud=0;
        for(String label:labels){

            cadena=cadena+label+",";
        }
        longitud=cadena.length();
        if(longitud>0) {
            cadena = cadena.substring(0, longitud - 1);
        }
        opcionVarianteList.get(position).setValores(cadena);
        notifyItemChanged(position);


    }

    public void DeleteElement(int position){

        opcionVarianteList.remove(position);
        notifyItemRemoved(position);

    }
  }
