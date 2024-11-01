

package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.InterfaceDetalleCarritoVenta;
import com.omarchdev.smartqsale.smartqsaleventas.ItemClickListener;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

/**
 * Created by OMAR CHH on 25/09/2017.
 */

public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    String simboloMoneda;
    BdConnectionSql bdConnectionSql;
    int tipoLista = 0;
    InterfaceDetalleCarritoVenta interfaceDetalleVenta;
    private List<mProduct> mProductList;
    private Context contexto;
    String Uri;
    boolean accionMenu;
    AccionConfigProduct accionConfigProduct;
    final int ACCION_CONFIG_VARIANTE = 1;
    final int ACCION_CONFIG_PACK = 2;
    final int ACCION_CONFIG_MODIFICADOR = 3;
    final int ACCION_CONFIG_TIENDA = 4;
    DatosSeleccionProductoLista datosSeleccionProductoLista;
    final String CodeCia = GetJsonCiaTiendaBase64x3();

    public interface DatosSeleccionProductoLista {

        public void ProductoPosicionSeleccionado(int position, View view);

    }

    public interface AccionConfigProduct {
        public void ObtenerIdAccion(int idProducto, int Accion, String nombre, boolean esVariante, boolean esPack, boolean estadoModificador);

        public void ConfigPorTienda(int idProducto, String nombre);
    }

    public void setDatosSeleccionProductoLista(DatosSeleccionProductoLista datosSeleccionProductoLista) {

        this.datosSeleccionProductoLista = datosSeleccionProductoLista;

    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public void setAccionConfigProduct(AccionConfigProduct accionConfigProduct) {
        this.accionConfigProduct = accionConfigProduct;
    }

    public void setPermitirAccion(boolean b) {
        accionMenu = b;
    }

    public RvAdapter(Context context, int tipoLista) {
        simboloMoneda = Constantes.DivisaPorDefecto.SimboloDivisa;
        mProductList = new ArrayList<>();
        this.contexto = context;
        bdConnectionSql = new BdConnectionSql();
        this.tipoLista = tipoLista;
        Uri = "";
        accionMenu = false;
    }

    public void setInterfaceDetalleVenta(InterfaceDetalleCarritoVenta interfaceDetalleVenta) {
        this.interfaceDetalleVenta = interfaceDetalleVenta;
    }

    public void AddProduct(List<mProduct> list) {


        mProductList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int a = 0;
        if (tipoLista == 1) {
            a = 1;
        } else if (tipoLista == 2) {
            a = 2;
        }
        return a;
    }

    public void clear() {

        mProductList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {

            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product, parent, false);
                return new ProductosViewHolder(v);


            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewproductsinsale, parent, false);
                return new ProductosInSaleViewHolder(v);


        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        byte[] imgData;

        switch (holder.getItemViewType()) {
            case 1:
                final ProductosViewHolder productosViewHolder = (ProductosViewHolder) holder;
                productosViewHolder.tvProductName.setText(mProductList.get(position).getDescripcionCategoria().trim()
                        + "/" + mProductList.get(position).getDescripcionSubCategoria().trim() + "/" + mProductList.get(position).getcProductName());
                productosViewHolder.tvPrecio.setText(simboloMoneda + String.format("%.2f", mProductList.get(position).getPrecioVenta()));
                productosViewHolder.tvCantidad.setText(String.valueOf(mProductList.get(position).getdQuantity()));
                if (mProductList.get(position).isEsFavorito()) {
                    productosViewHolder.imgFavorito.setVisibility(View.VISIBLE);
                } else {
                    productosViewHolder.imgFavorito.setVisibility(View.INVISIBLE);
                }
                if (!accionMenu) {
                    productosViewHolder.textViewOptions.setVisibility(View.GONE);
                } else {
                    productosViewHolder.tvCantidad.setText("%conf");

                    productosViewHolder.textViewOptions.setVisibility(View.VISIBLE);
                    productosViewHolder.textViewOptions.setOnClickListener(view -> {

                        PopupMenu popupMenu = new PopupMenu(contexto, productosViewHolder.textViewOptions);
                        popupMenu.inflate(R.menu.menu_item_producto_config);
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                switch (menuItem.getItemId()) {
                                    case R.id.action_edit_variante:

                                        accionConfigProduct.ObtenerIdAccion(mProductList.get(productosViewHolder.getAdapterPosition()).getIdProduct(), ACCION_CONFIG_VARIANTE, mProductList.get(productosViewHolder.getAdapterPosition()
                                        ).getcProductName(), mProductList.get(productosViewHolder.getAdapterPosition()).isEstadoVariante(), mProductList.get(productosViewHolder.getAdapterPosition()).isTipoPack(), mProductList.get(productosViewHolder.getAdapterPosition()).isEstadoModificador());

                                        break;
                                    case R.id.action_edit_Pack:

                                        accionConfigProduct.ObtenerIdAccion(mProductList.get(productosViewHolder.getAdapterPosition()).getIdProduct(), ACCION_CONFIG_PACK, mProductList.get(productosViewHolder.getAdapterPosition()
                                        ).getcProductName(), mProductList.get(productosViewHolder.getAdapterPosition()).isEstadoVariante(), mProductList.get(productosViewHolder.getAdapterPosition()).isTipoPack(), mProductList.get(productosViewHolder.getAdapterPosition()).isEstadoModificador());

                                        break;
                                    case R.id.action_edit_modificadores:
                                        accionConfigProduct.ObtenerIdAccion(mProductList.get(productosViewHolder.getAdapterPosition()).getIdProduct(), ACCION_CONFIG_MODIFICADOR, mProductList.get(productosViewHolder.getAdapterPosition()
                                        ).getcProductName(), mProductList.get(productosViewHolder.getAdapterPosition()).isEstadoVariante(), mProductList.get(productosViewHolder.getAdapterPosition()).isTipoPack(), mProductList.get(productosViewHolder.getAdapterPosition()).isEstadoModificador());

                                        break;
                                    case R.id.action_edit_tienda:
                                        accionConfigProduct.ConfigPorTienda(mProductList.get(productosViewHolder.getAdapterPosition()).getIdProduct(), mProductList.get(productosViewHolder.getAdapterPosition()
                                        ).getcProductName());
                                        break;
                                }


                                return false;
                            }
                        });


                    });
                }
                if (mProductList.get(position).isTipoNormal() || (!mProductList.get(position).isEstadoVariante() && !mProductList.get(position).isTipoPack())) {
                    holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.color_Fondo));

                } else if (mProductList.get(position).isEstadoModificador()) {
                    holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.color_Fondo));

                } else if (mProductList.get(position).isTipoPack()) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#ef5350"));

                } else if (mProductList.get(position).isEstadoVariante()) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#00e676"));
                } else {

                    holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.color_Fondo));
                }


                imgData = mProductList.get(position).getbImage();
                if (mProductList.get(position).getTipoRepresentacionImagen() == 2) {
                    productosViewHolder.ciProductImage.clearColorFilter();

                    if (imgData != null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                        productosViewHolder.ciProductImage.setImageBitmap(bmp);
                    } else {
                        try {
                            Picasso.get()
                                    .load(BASE_URL_API + "api/producto/GetImageProduct?codeCia=" + CodeCia + "&tipoConsulta=2&idProduct=" + mProductList.get(position).idProduct)
                                    .error(R.drawable.circle_full_error)
                                    .into(productosViewHolder.ciProductImage);
                        } catch (Exception ex) {
                            ex.toString();
                        }


            /*            Uri = "@drawable/" + mProductList.get(position).getCodigoForma();
                        Uri = Uri.trim();
                        productosViewHolder.ciProductImage.setImageResource(contexto.getResources().getIdentifier(Uri, null, contexto.getPackageName()));
*/
                    }

                } else if (mProductList.get(position).getTipoRepresentacionImagen() == 1) {
                    Uri = "@drawable/" + mProductList.get(position).getCodigoForma();
                    Uri = Uri.trim();
                    productosViewHolder.ciProductImage.setImageResource(contexto.getResources().getIdentifier(Uri, null, contexto.getPackageName()));
                    productosViewHolder.ciProductImage.setColorFilter(Color.parseColor(mProductList.get(position).getCodigoColor()));
                }
                final Context context = productosViewHolder.tvCantidad.getContext();

                break;

            case 2:
                ProductosInSaleViewHolder productosInSaleViewHolder = (ProductosInSaleViewHolder) holder;
                if (Constantes.ConfigTienda.nombreConCategoria) {
                    if (mProductList.get(position).getIdSubCategoria() == 0) {
                        productosInSaleViewHolder.productName.setText(mProductList.get(position).getDescripcionCategoria() + "/" + mProductList.get(position).getcProductName());
                    } else {
                        productosInSaleViewHolder.productName.setText(mProductList.get(position).getDescripcionCategoria() + "/" + mProductList.get(position).getDescripcionSubCategoria().trim() + "/" + mProductList.get(position).getcProductName());
                    }

                } else {
                    productosInSaleViewHolder.productName.setText(mProductList.get(position).getcProductName());

                }
                productosInSaleViewHolder.productPrice.setText(mProductList.get(position).getPrecioVentaTexto());

                imgData = mProductList.get(position).getbImage();
                if (mProductList.get(position).getTipoRepresentacionImagen() == 2) {
                    productosInSaleViewHolder.imageViewProduct.clearColorFilter();

                    if (imgData != null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                        productosInSaleViewHolder.imageViewProduct.setImageBitmap(bmp);
                    } else {
                        Uri = "@drawable/" + mProductList.get(position).getCodigoForma();
                        Uri = Uri.trim();
                        productosInSaleViewHolder.imageViewProduct.setImageResource(contexto.getResources().getIdentifier(Uri, null, contexto.getPackageName()));
                        Picasso.get()
                                .load(BASE_URL_API + "api/producto/GetImageProduct?codeCia=" + CodeCia + "&tipoConsulta=2&idProduct=" + mProductList.get(position).idProduct)
                                .placeholder(contexto.getResources().getIdentifier(Uri, null, contexto.getPackageName()))
                                .error(R.drawable.circle_full_error)
                                .into(productosInSaleViewHolder.imageViewProduct);
                    }

                } else if (mProductList.get(position).getTipoRepresentacionImagen() == 1) {
                    Uri = "@drawable/" + mProductList.get(position).getCodigoForma();
                    Uri = Uri.trim();
                    productosInSaleViewHolder.imageViewProduct.setImageResource(contexto.getResources().getIdentifier(Uri, null, contexto.getPackageName()));
                    productosInSaleViewHolder.imageViewProduct.setColorFilter(Color.parseColor(mProductList.get(position).getCodigoColor()));

                }
                break;

        }

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class ProductosInSaleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout cv;
        ImageView imageViewProduct;
        TextView productName;
        TextView productPrice;
        int position;

        public ProductosInSaleViewHolder(View itemView) {

            super(itemView);
            cv = (RelativeLayout) itemView.findViewById(R.id.cvProductInSale);
            imageViewProduct = (ImageView) itemView.findViewById(R.id.ImageProductPhoto);
            productName = (TextView) itemView.findViewById(R.id.txtNombreProducto);
            productPrice = (TextView) itemView.findViewById(R.id.txtPrecioProducto);
            cv.setOnClickListener(this);
            position = 0;

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cvProductInSale:
                    position = getAdapterPosition();

                    datosSeleccionProductoLista.ProductoPosicionSeleccionado(getAdapterPosition(), v);
                    break;
            }
        }
    }

    class ProductosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout cv;
        TextView tvPrecio;
        TextView tvCantidad;
        TextView tvProductId;
        TextView tvProductName;
        ImageView ciProductImage;
        ItemClickListener itemClickListener;
        TextView textViewOptions;
        ImageButton imgFavorito;


        public ProductosViewHolder(View itemView) {
            super(itemView);
            cv = (RelativeLayout) itemView.findViewById(R.id.cvProduct);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvCantidad);
            tvProductName = (TextView) itemView.findViewById(R.id.tvproductName);
            ciProductImage = (ImageView) itemView.findViewById(R.id.ImageProductPhoto);
            textViewOptions = (TextView) itemView.findViewById(R.id.textViewOptions);
            imgFavorito = itemView.findViewById(R.id.imgFavorito);
            cv.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;

        }

        @Override
        public void onClick(View v) {
            if (!accionMenu) {

                if (v.getId() == R.id.cvProduct) {
                    interfaceDetalleVenta.PasarInformacionProductoaDetalleVenta(mProductList.get(getAdapterPosition()).getIdProduct());
                }

            }
        }

    }


}
