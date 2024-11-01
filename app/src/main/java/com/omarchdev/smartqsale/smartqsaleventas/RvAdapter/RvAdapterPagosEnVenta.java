package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DecimalControlKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mPagosEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 06/12/2017.
 */

public class RvAdapterPagosEnVenta extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String simboloMoneda;
    private final BdConnectionSql bdConnectionSql;
    private final int idMetodoPago;
    private final BigDecimal cantidadCancelada;
    private final String tipoPago;
    private final String nombreTipoPago;
    private List<mPagosEnVenta> list;
    private CantidadPagosEnVenta cantidadPagosEnVenta;
    private int longitudRecyclerView;
    private Context context;
    private BigDecimal montoPagado;
    private int idCabeceraPedido;
    private int position;
    private byte type;
    private IAdapterPagos iAdapterPagos;
    private boolean showDelete;

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
    }

    public void setiAdapterPagos(IAdapterPagos iAdapterPagos) {
        this.iAdapterPagos = iAdapterPagos;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                PagoEnVentaViewhHolder viewHolder = (PagoEnVentaViewhHolder) holder;
                viewHolder.txtTipoPago.setText(list.get(position).getTipoPago());
                viewHolder.txtCantidadPagada.setText(DecimalControlKt.montoDecimalPrecioSimbolo(list.get(position).getCantidadPagada()));

                if (list.get(position).getIdTipoPago() == 0 || list.get(position).isActivaPagoExterno()) {
                    viewHolder.imgBorrar.setVisibility(View.INVISIBLE);
                }

                break;
            case 2:
                PagosEnVentaSimple vHolder = (PagosEnVentaSimple) holder;
                vHolder.txtNombre.setText(list.get(position).getTipoPago());
                vHolder.txtMonto.setText(DecimalControlKt.montoDecimalPrecioSimbolo(list.get(position).getCantidadPagada()));

                if (list.get(position).getIdTipoPago() == 0  || list.get(position).isActivaPagoExterno()) {
                    vHolder.ibtnDeletePago.setVisibility(View.INVISIBLE);
                } else {


                    vHolder.ibtnDeletePago.setVisibility((showDelete) ? View.VISIBLE : View.INVISIBLE);
                }


                vHolder.ibtnDeletePago.setOnClickListener(v -> {
                    this.iAdapterPagos.ClickPosition(position, 1);
                });


                break;
        }

    }

    public RvAdapterPagosEnVenta(int idCabeceraPedido) {
        list = new ArrayList<>();
        longitudRecyclerView = 0;
        montoPagado = new BigDecimal(0);
        simboloMoneda = Constantes.DivisaPorDefecto.SimboloDivisa;
        bdConnectionSql = BdConnectionSql.getSinglentonInstance();
        idMetodoPago = 0;
        cantidadCancelada = new BigDecimal(0);
        tipoPago = "";
        nombreTipoPago = "";
        this.idCabeceraPedido = idCabeceraPedido;
    }

    public void setListenerCantidadPagos(CantidadPagosEnVenta cantidadPagosEnVenta) {
        this.cantidadPagosEnVenta = cantidadPagosEnVenta;
        longitudRecyclerView = 0;
        position = 0;
        type = 1;
    }

    public void setIdCabeceraPedido(int idCabeceraPedido) {
        this.idCabeceraPedido = idCabeceraPedido;
    }

    @Override
    public int getItemViewType(int position) {
        if (type == 1) {
            return 1;
        } else if (type == 2) {
            return 2;
        }
        return 0;
    }

    public void setTypeView(byte type) {
        this.type = type;
    }

    public List<mPagosEnVenta> getList() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = null;
        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_tipo_pago_monto, parent, false);
            context = parent.getContext();
            return new PagoEnVentaViewhHolder(v);
        } else if (viewType == 2) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago_venta_simple, parent, false);
            return new PagosEnVentaSimple(v);
        }
        return null;
    }

    public List<mPagosEnVenta> obtenerPagos() {
        return list;
    }

    @Override
    public int getItemCount() {
        return longitudRecyclerView;
    }

    public void eliminarPago(int idCabeceraPedido, int position) {
        this.position = position;
        new EliminaPago().execute(idCabeceraPedido, position);

    }

    public void removeItem(int position) {
        montoPagado = montoPagado.subtract(list.get(position).getCantidadPagada());
        list.remove(position);
        notifyItemRemoved(position);
        longitudRecyclerView--;
        cantidadPagosEnVenta.numeroElementos(longitudRecyclerView, montoPagado);

    }

    public void AgregarPagosEnVenta(List<mPagosEnVenta> list) {
        this.list = list;
        longitudRecyclerView = list.size();
        notifyDataSetChanged();
    }

    public void AgregarMetodoPagoTemporal(List<mPagosEnVenta> list) {
        longitudRecyclerView = list.size();
        this.list.addAll(list);
        notifyDataSetChanged();
        for (int i = 0; i < longitudRecyclerView; i++) {
            montoPagado = montoPagado.add(list.get(i).getCantidadPagada());
        }
        cantidadPagosEnVenta.numeroElementos(longitudRecyclerView, montoPagado);

    }

    public void AddElement(int idTipoPago, String tipoPago, String nombreTipoPago, BigDecimal cantidadPagada, boolean esEfectivo) {

        BigDecimal mP = new BigDecimal(0);
        boolean existe = false;
        for (int i = 0; i < longitudRecyclerView; i++) {

            if (list.get(i).getIdTipoPago() == idTipoPago) {
                existe = true;
                list.get(i).setCantidadPagada(cantidadPagada);
                notifyItemChanged(i);
            }

        }
        if (existe != true) {
            list.add(new mPagosEnVenta(idTipoPago, tipoPago, nombreTipoPago, cantidadPagada, esEfectivo));
            notifyDataSetChanged();
            longitudRecyclerView++;
        }

        for (int i = 0; i < longitudRecyclerView; i++) {
            mP = mP.add(list.get(i).getCantidadPagada());
        }
        montoPagado = mP;

        cantidadPagosEnVenta.numeroElementos(longitudRecyclerView, mP);


    }


    public interface IAdapterPagos {
        void ClickPosition(int iPosition, int type);
    }

    public interface CantidadPagosEnVenta {
        public void numeroElementos(int cantidad, BigDecimal montoPagado);
    }

    class PagoEnVentaViewhHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTipoPago;
        TextView txtCantidadPagada;
        ImageView imgBorrar;

        public PagoEnVentaViewhHolder(View itemView) {
            super(itemView);
            txtTipoPago = (TextView) itemView.findViewById(R.id.txtTipodePagoEnVenta);
            txtCantidadPagada = (TextView) itemView.findViewById(R.id.txtMontoPagado);
            imgBorrar = (ImageView) itemView.findViewById(R.id.imgDeleteTipoPago);
            imgBorrar.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imgDeleteTipoPago) {
                ConfirmacionEliminarElemento();
            }
        }

        public void ConfirmacionEliminarElemento() {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(txtTipoPago.getText().toString() + " " + txtCantidadPagada.getText().toString());
            builder.setMessage("¿Desea realmente eliminar el pago?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    eliminarPago(idCabeceraPedido, getAdapterPosition());
                }
            });
            builder.create().show();

        }
    }

    class PagosEnVentaSimple extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtMonto;
        ImageButton ibtnDeletePago;

        public PagosEnVentaSimple(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreTipoPago);
            txtMonto = itemView.findViewById(R.id.txtPrecioPagado);
            ibtnDeletePago = itemView.findViewById(R.id.ibtnDeletePago);

        }

    }

    public class EliminaPago extends AsyncTask<Integer, Void, Byte> {
        Dialog dialogEliminar = crearDialog();

        public ProgressDialog crearDialog() {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Eliminando pago");
            progressDialog.setCanceledOnTouchOutside(false);

            return progressDialog;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogEliminar.show();
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarPagoTemporal(integers[0], list.get(integers[1]).getIdTipoPago());
        }


        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (aByte == 1) {
                removeItem(position);
            } else if (aByte == 0) {
                Toast.makeText(context, "No se elimino el pago con éxito", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Verifique si conexion a internet", Toast.LENGTH_SHORT).show();
            }
            dialogEliminar.dismiss();

        }
    }
}
