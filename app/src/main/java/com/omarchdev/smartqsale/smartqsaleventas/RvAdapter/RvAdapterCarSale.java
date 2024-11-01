package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorVentas;
import com.omarchdev.smartqsale.smartqsaleventas.Model.PackElemento;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;
import java.util.List;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.EstadosPedidoColor.Guardado;

/**
 * Created by OMAR CHH on 24/11/2017.
 */

public class RvAdapterCarSale extends RecyclerView.Adapter<RvAdapterCarSale.ProductInCarSaleViewHolder> {


    String simboloMoneda;
    PasarCantidad ListenerCantidad;
    Context context;
    Button btnGuardarCantidadProducto, btnSalirDialog;
    TextInputLayout txtObservacion;
    EditText edtquantity;
    Dialog dialog;
    int numeroItem;
    ControladorVentas controladorVentas;
    ListenerDetalleVenta listenerDetalleVenta;
    boolean esPack;
    private List<ProductoEnVenta> list;
    boolean permitir;
    ListenerCarSale listenerCarSale;
    Drawable img;
    boolean permitirOpciones = true;
    int position;

    public void PermitirOpciones(boolean permitirOpciones) {
        this.permitirOpciones = permitirOpciones;
    }
    public void setListenerCarSale(ListenerCarSale listenerCarSale) {
        this.listenerCarSale = listenerCarSale;
    }


    public void setListenerDetalleVenta(ListenerDetalleVenta listenerDetalleVenta){

        this.listenerDetalleVenta=listenerDetalleVenta;

    }


    public interface ListenerDetalleVenta{

        public void EliminarProducto(int position);

    }

    public interface ListenerCarSale{
        public void EditarProductoVenta(ProductoEnVenta productoEnVenta);
    }


    public RvAdapterCarSale(List<ProductoEnVenta> list) {
        this.list = list;
        simboloMoneda = Constantes.DivisaPorDefecto.SimboloDivisa;
        numeroItem = 1;
        controladorVentas = new ControladorVentas();
    }


    public void setNumeroItem(int numeroItem) {
        this.numeroItem = numeroItem;
    }

    public void setListenerCantidad(PasarCantidad ListenerCantidad) {
        this.ListenerCantidad = ListenerCantidad;
    }

    public void   ModificarCantidad() {
         position = list.size() - 1;
        notifyItemChanged(position);

    }

    @Override
    public ProductInCarSaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        img = context.getResources().getDrawable(R.drawable.percent1);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_in_sale_quotation, parent, false);
        return new ProductInCarSaleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductInCarSaleViewHolder holder, int position) {

        if (list.get(position).isEsPack()) {
            holder.itemView.setLayoutParams(new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            String nombre = "";
            for (int i = 0; i < list.get(position).getProductoEnVentaList().size(); i++) {
                nombre = nombre + "x" + String.format("%.0f", list.get(position)
                        .getProductoEnVentaList().get(i).getCantidad()) + "." +
                        list.get(position).getProductoEnVentaList().get(i).getProductName() + "\n";

            }
            holder.productName.setText(list.get(position).getProductName());
            holder.txtCombo.setText(nombre);
            holder.productName.setLayoutParams(new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        else {
            holder.itemView.setLayoutParams(new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.productName.setLayoutParams(new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (list.get(position).isEsVariante()) {
                holder.productName.setText(list.get(position).getProductName() + "\n"
                        + list.get(position).getDescripcionVariante());
            }else if(list.get(position).isEsModificado()){
                holder.productName.setText(list.get(position).getProductName() +
                        "\n"+list.get(position).getDescripcionModificador());
            }else {
                holder.productName.setText(list.get(position).getProductName());
            }
        }
        if(list.get(position).isUsaDescuento()){
           img.setBounds(0,0,60,60);
            holder.productName.setCompoundDrawables(null,null,img,null);
        }else{
            holder.productName.setCompoundDrawables(null,null,null,null);
        }
        holder.subTotalPrice.setText(simboloMoneda +
                String.format("%.2f",list.get(position).getPrecioVentaFinal()
                        .subtract(list.get(position).getMontoDescuento())));
        holder.quantityProduct.setText(String.format("%.2f",
                list.get(position).getCantidad()));

        if(list.get(position).getEstadoEliminado().equals("")){
            if(list.get(position).getEstadoGuardado().equals("")){


            }else if(list.get(position).getEstadoGuardado().equals("G")){
                holder.itemView.setBackgroundColor(Color.parseColor(Guardado));
            }
        }else{
            holder.imgDelete.setVisibility(View.GONE);


        }
        if (!this.permitirOpciones) {
            holder.imgDelete.setVisibility(View.GONE);
        }
    }

    public void addElementVariante(ProductoEnVenta productoEnVenta) {

        list.add(productoEnVenta);
        ListenerCantidad.cantidad();
        notifyDataSetChanged();

    }

    public void addElementoPack(PackElemento packElemento) {

        ProductoEnVenta productoEnVenta = new ProductoEnVenta();
        productoEnVenta.setProductName(packElemento.getNombreProducto());
        productoEnVenta.setEsPack(true);
        productoEnVenta.setIdProducto(packElemento.getIdProducto());
        productoEnVenta.setItemNum(packElemento.getNumItem());
        productoEnVenta.setPrecioOriginal(packElemento.getPrecioVenta());
        productoEnVenta.setPrecioVentaFinal(packElemento.getPrecioVentaFinal());
        productoEnVenta.setCantidad(packElemento.getCantidad());
        productoEnVenta.setIdDetallePedido(packElemento.getIdDetallePedido());


        productoEnVenta.inicializarLista();
        for (int i = 0; i < packElemento.getProductList().size(); i++) {

            productoEnVenta.getProductoEnVentaList().add(new ProductoEnVenta(packElemento.getProductList().get(i).getIdProduct(), packElemento.getProductList().get(i).getcProductName(), 0, packElemento.getProductList().get(i).getStockDisponible().multiply(new BigDecimal(packElemento.getCantidad())).floatValue(), new BigDecimal(0), new BigDecimal(0), ""));

        }
        list.add(productoEnVenta);
        ListenerCantidad.cantidad();
        notifyDataSetChanged();
        productoEnVenta = null;
    }

    public void addElement(ProductoEnVenta productoEnVenta) {
        productoEnVenta.setItemNum(numeroItem);
        numeroItem++;
        list.add(productoEnVenta);

        ListenerCantidad.cantidad();
        notifyDataSetChanged();

    }

    public void setiNumItem(int iNumItem) {

        this.numeroItem = iNumItem;
    }

    public void ActualizarEstadoProducto(int position){


    }

    public void RemoveElement(int position) {

        try {
         //   ListenerCantidad.eliminarProductoEnDetalle(list.get(position), position);
            /*if (!list.get(position).isEsPack()) {
                list.remove(position);
                ListenerCantidad.cantidad();

                if (list.size() == 0) {
                    ListenerCantidad.detalleListaVacio();
                }

                notifyItemRemoved(position);
            }*/
            list.remove(position);
            ListenerCantidad.cantidad();

            if (list.size() == 0) {
                ListenerCantidad.detalleListaVacio();
            }

            notifyItemRemoved(position);

        }catch (Exception e){
            e.toString();
        }
    }

    public void EliminarProductoPack(int position){

        list.remove(position);
        ListenerCantidad.cantidad();

        if(list.size()==0)

        {
            ListenerCantidad.detalleListaVacio();
        }

        notifyItemRemoved(position);

    }

    public void RemoveAllElement() {
        list.clear();
        ListenerCantidad.cantidad();
        if (list.size() == 0) {
            ListenerCantidad.detalleListaVacio();
        }
        numeroItem = 1;
        notifyDataSetChanged();
    }


    public int CodigoUltimoProduct() {

        int id = 0;
        int posicion = 0;
        if (list.size() == 0) {
            id = 0;
        } else {
            posicion = getItemCount() - 1;
            id = list.get(posicion).getIdProducto();
        }
        return id;
    }

    public void AddElementList(List<ProductoEnVenta> list) {
        //this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    public void ActualizarProducto(int position){
        notifyItemChanged(position);
        ListenerCantidad.cantidad();
    }



    public void AumentarNumItem(){
        numeroItem++;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface PasarCantidad{

        public void cantidad();

        public void eliminarProductoEnDetalle(ProductoEnVenta productoEnVenta,int position);
        public void detalleListaVacio();

        public void EditarCantidadProducto(int position);
    }

    public class ProductInCarSaleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout linearLayout;
        RelativeLayout cv;
        TextView productName,quantityProduct,subTotalPrice,txtCombo;

        ImageButton imgDelete;
        float PrecioSubtotal=0;
        public ProductInCarSaleViewHolder(View itemView) {
            super(itemView);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.llayoutCardViewProductInCarSale);
            cv=(RelativeLayout)itemView.findViewById(R.id.cvProduct_in_sale_quotation);
            productName=(TextView)itemView.findViewById(R.id.productNameInSale);
            txtCombo=(TextView)itemView.findViewById(R.id.txtCombo);
            quantityProduct=(TextView)itemView.findViewById(R.id.quantityInSale);
            subTotalPrice=(TextView)itemView.findViewById(R.id.priceInSale);
            imgDelete=(ImageButton)itemView.findViewById(R.id.deleteInSale);
            linearLayout.setOnClickListener(this);
            cv.setOnClickListener(this);
            imgDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.deleteInSale){
                VerificarEliminacionProductoLista();
            }
            else if(v.getId()==R.id.llayoutCardViewProductInCarSale){

                if (RvAdapterCarSale.this.permitirOpciones) {

                    permitir=true;
                    if(list.get(getAdapterPosition()).isEsPack()){
                        permitir=false;
                        Toast.makeText(context,"No se puede modificar este tipo de productos",Toast.LENGTH_SHORT).show();
                    }else{
                        if(listenerCarSale!=null) {
                            listenerCarSale.EditarProductoVenta(list.get(getAdapterPosition()));
                        }
                    }
                }

            }
            else if(v.getId()==R.id.btnGuardarCantidadProducto){
                if(Float.valueOf(edtquantity.getText().toString())>0) {
                     ModificarCantidaPrecioSubtotal(getAdapterPosition());
                    ListenerCantidad.cantidad();
                    dialog.dismiss();
                }else if(Float.valueOf(edtquantity.getText().toString())<=0){
                    Toast.makeText(context,"Debe ingresar una cantidad mayor a 0",Toast.LENGTH_LONG).show();
                }
            }
            else if(v.getId()==R.id.btnSalirDialog){
                dialog.dismiss();
            } else if (v.getId() == R.id.btnPlus) {
                float cantidad = Float.valueOf(edtquantity.getText().toString());
                cantidad++;
                edtquantity.setText(String.valueOf(cantidad));
            } else if (v.getId() == R.id.btnMinusb) {
                float cantidad  = Float.valueOf(edtquantity.getText().toString());
                if (cantidad - 1 >= 0) {
                    cantidad--;
                    edtquantity.setText(String.valueOf(cantidad));
                } else if (cantidad - 1 < 0) {

                }
            }

        }

        public void modificarCantidadProducto(float cantidad, String comentario,boolean pack)     {

            ImageButton imgPlus,imgMinus;
            AlertDialog.Builder builder= new AlertDialog.Builder(context);
            View v=((Activity)context).getLayoutInflater().inflate(R.layout.dialog_edit_price,null);
            esPack=pack;

            btnSalirDialog=(Button)v.findViewById(R.id.btnSalirDialog);
            btnGuardarCantidadProducto =(Button)v.findViewById(R.id.btnGuardarCantidadProducto);
            edtquantity=(EditText)v.findViewById(R.id.edtQuantityProduct);
            imgPlus=(ImageButton)v.findViewById(R.id.btnPlus);
            imgMinus=(ImageButton)v.findViewById(R.id.btnMinusb);
            txtObservacion = (TextInputLayout) v.findViewById(R.id.txtObservacionProducto);
            imgPlus.setOnClickListener(this);
            imgMinus.setOnClickListener(this);
            btnGuardarCantidadProducto.setOnClickListener(this);
            btnSalirDialog.setOnClickListener(this);
            edtquantity.setText(String.valueOf(Math.round(cantidad)));
            txtObservacion.getEditText().setText(comentario);
            dialog = builder.setView(v).create();
            dialog.show();
        }


        public void VerificarEliminacionProductoLista() {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alerta").setMessage("Desea eliminar el articulo:" + productName.getText().toString());
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(
                    "Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            listenerDetalleVenta.EliminarProducto(getAdapterPosition());

                        }
                    }
            );
            builder.create().show();
        }

        public void ModificarCantidaPrecioSubtotal(int position){

                list.get(position).setCantidad(Float.valueOf(edtquantity.getText().toString()));
                list.get(position).setObservacionProducto(txtObservacion.getEditText().getText().toString());
                list.get(position).setPrecioVentaFinal(list.get(position).getPrecioOriginal()
                        .multiply(BigDecimal.valueOf(list.get(position).getCantidad())));
                ListenerCantidad.EditarCantidadProducto(position);

           // notifyItemChanged(position);
      }

    }

}
