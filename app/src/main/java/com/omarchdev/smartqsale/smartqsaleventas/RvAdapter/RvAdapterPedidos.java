package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.Activitys.DetallePedido;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 17/12/2017.
 */

public class RvAdapterPedidos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<mCabeceraPedido> list;
    Context context;
    interfaceListaPedidos listenerPedidos;
    String simboloMoneda = Constantes.DivisaPorDefecto.SimboloDivisa;
    BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();

    public RvAdapterPedidos() {
        list = new ArrayList<>();
    }

    public void setListenerPedidos(interfaceListaPedidos listenerPedidos) {
        this.listenerPedidos = listenerPedidos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_pedido_cabecera, parent, false);
        context = parent.getContext();
        return new CabeceraPedidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CabeceraPedidoViewHolder h = (CabeceraPedidoViewHolder) holder;
        if(position==0){
            h.ll_linea_superior.setVisibility(View.GONE);
        }else{
            h.ll_linea_superior.setVisibility(View.VISIBLE);
        }

        h.txtNroPedido.setText(list.get(position).GetIdentificadorUnicoPedido());
        h.txtNombrePedido.setText(list.get(position).getIdentificadorPedido());
        if (!list.get(position).getDenominacionCliente().equals("")) {
            h.txtNombreCliente.setText(list.get(position).getDenominacionCliente());
        } else {

        }
        if (!list.get(position).getNombreVendedor().equals("")) {
            h.txtNombreVendedor.setText(list.get(position).getNombreVendedor());
        } else {

        }
        h.txtFechaPedido.setText(list.get(position).getFechaReserva());
        h.txtValorVenta.setText(Constantes.DivisaPorDefecto.SimboloDivisa +
                String.format("%.2f", list.get(position).getTotalNeto()));

        if(list.get(position).getDescripcionPedido().replace("\\n"," \n").isEmpty()){
            h.txtObservacionZonaServicio.setVisibility(View.GONE);
        }
        h.txtObservacionZonaServicio.setText(list.get(position).getDescripcionPedido().replace("\\n"," \n"));
        h.txtObservacioPedido.setText(list.get(position).getObservacionReserva());
        if(list.get(position).isPermitirModificaciones()){
            h.contentButton.setVisibility(View.VISIBLE);
        }
        else{
            h.contentButton.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void AddElement(List<mCabeceraPedido> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface interfaceListaPedidos {

        public void ObtenerDatoPedidoSupender(mCabeceraPedido cabeceraPedido);

        public void ObtenerDatoPedidoRegresarVenta(mCabeceraPedido cabeceraPedido);

        public void ActualizarListaPedidos();

    }

    private class CabeceraPedidoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtNombrePedido, txtFechaPedido, txtNombreCliente,
                txtNombreVendedor, txtValorVenta,txtNroPedido,txtObservacionZonaServicio,txtObservacioPedido;
        ImageButton btnSuspenderPedido, btnPonerPedidoEnVenta;
        LinearLayout contentButton;
        ConstraintLayout cv;
        LinearLayout ll_linea_superior;

        public CabeceraPedidoViewHolder(View itemView) {

            super(itemView);
            cv = itemView.findViewById(R.id.cv_cabecera_pedido);
            ll_linea_superior=itemView.findViewById(R.id.ll_linea_superior);
            txtFechaPedido = itemView.findViewById(R.id.txtFechaPedido);
            txtNombreVendedor =  itemView.findViewById(R.id.txtNombreVendedor);
            txtObservacionZonaServicio=itemView.findViewById(R.id.txtObservacionZonaServicio);
            txtNombreCliente = itemView.findViewById(R.id.txtNombreCliente);
            txtNombrePedido =  itemView.findViewById(R.id.txtNombrePedido);
            txtNroPedido=itemView.findViewById(R.id.txtNroPedido);
            txtValorVenta =  itemView.findViewById(R.id.txtValorVenta);
            txtObservacioPedido=itemView.findViewById(R.id.txtObservacioPedido);
            contentButton=itemView.findViewById(R.id.contentButton);
            btnSuspenderPedido = itemView.findViewById(R.id.btnSuspenderPedido);
            btnPonerPedidoEnVenta =  itemView.findViewById(R.id.btnPonerPedidoEnVenta);
            cv.setOnClickListener(this);
            btnSuspenderPedido.setOnClickListener(this);
            btnPonerPedidoEnVenta.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSuspenderPedido:
                       MostrarDialogoConfirmacionSuspender();
                        break;
                case R.id.btnPonerPedidoEnVenta:
                    listenerPedidos.ObtenerDatoPedidoRegresarVenta(list.get(getAdapterPosition()));
                    break;
                case R.id.cv_cabecera_pedido:

                    new ConsultarPedido().execute(getAdapterPosition());

                    break;
            }
        }

        private void MostrarDialogNoDisponible() {
            Dialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alerta").setMessage("El pedido no está disponible").
                    setIcon(R.drawable.alert).setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog = builder.create();
            dialog.show();

        }

        public void MostrarDialogoConfirmacionSuspender() {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("¿Desea anular el pedido?");
            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listenerPedidos.ObtenerDatoPedidoSupender(list.get(getAdapterPosition()));
                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.create().show();

        }

        public void MostrarDetallePedido(int idDetallePedido) {
            Intent intent = new Intent(context, DetallePedido.class);
            intent.putExtra("idCabeceraPedido", idDetallePedido);
            intent.putExtra("EstadoPagado",false);
            context.startActivity(intent);
        }

        private class ConsultarPedido extends AsyncTask<Integer,Void,Boolean>{

            int pos=0;
            @Override
            protected Boolean doInBackground(Integer... integers) {
                pos=integers[0];
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean)
                MostrarDetallePedido(list.get(pos).getIdCabecera());

            }
        }
    }



}
