package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.CategoriaPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DecimalControlKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.MetricWindow;
import com.omarchdev.smartqsale.smartqsaleventas.Model.PackElemento;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.polak.clicknumberpicker.ClickNumberPickerView;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

/**
 * Created by OMAR CHH on 08/04/2018.
 */

public class DialogSelectCombo extends DialogFragment implements AsyncProducto.ListenerComboPack {


    final String CodeCia = GetJsonCiaTiendaBase64x3();

    public interface ListenerSelectCombo {

        public void ComboPackSelect(PackElemento packElemento);

        public void ProductosComboIndividuales(PackElemento packElemento);
    }

    @Override
    public void onStart() {

        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void getPackProducto(List<PackElemento> packElementos, List<CategoriaPack> categoriasPack) {
        this.categoriasPack = categoriasPack;
        if (packElementos != null) {
            this.packElementos = new ArrayList<>();

            if (packElementos.size() > 0) {
                this.packElementos = packElementos;
                InsertarPackProductos(this.packElementos);
                pbIndicator.hide();
                txtCargando.setVisibility(View.INVISIBLE);
                txtMensaje.setVisibility(View.GONE);
            } else {
                pbIndicator.hide();
                txtMensaje.setVisibility(View.VISIBLE);
                txtMensaje.setText("Debe terminar de configurar el combo/pack");
                txtCargando.setVisibility(View.INVISIBLE);
                clickNumberPickerView.setVisibility(View.GONE);
                txtCargando.setVisibility(View.INVISIBLE);


            }
        } else {
            pbIndicator.hide();
            txtTitulo.setVisibility(View.VISIBLE);
            txtTitulo.setText("Error al descargar la información.Verfifique su conexión a internet");
        }

    }


    interface ListSeleccion {
        void ProductSeleccion(int origen, mProduct product);

        void IgnoreList(int origen);
    }

    interface ListenerSeleccion {
        void ObtenerInformacion(int posSeleccion, int id, mProduct productSelection);
    }

    MetricWindow metricWindow;
    List<CategoriaPack> categoriasPack;
    ListenerSelectCombo listenerSelectCombo;
    int numCat;
    AVLoadingIndicatorView pbIndicator;
    TextView txtCargando, txtMensaje;
    AsyncProducto asyncProducto;
    Listas listaAdapter;
    BigDecimal precio;
    RecyclerView rvPackProducto;
    LinearLayout rlComboPack;
    RecyclerView rv;
    List<RecyclerView> recyclerViews;
    List<Listas> listaAdapters;
    TextView txtTitulo;
    List<TextView> textViewList;
    int idProducto;
    List<PackElemento> packElementos;
    String title;
    PackElemento pack;
    List<mProduct> productList;
    Dialog dialog;
    ClickNumberPickerView clickNumberPickerView;
    TextView txtEligeCantidad, txtTituloCombo;
    int numCatSelect = 0;
    BigDecimal MontoModificacionTotal;
    mProduct productTemp;

    public interface DatosPackSeleccionado {

        public void ObtenerInformacionPack(PackElemento packElemento);

    }

    public void setProductTemp(mProduct productTemp) {
        this.productTemp = productTemp;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }


    public void setListenerSelectCombo(ListenerSelectCombo listenerSelectCombo) {

        this.listenerSelectCombo = listenerSelectCombo;

    }


    public void AsignaTituloPrecioBase() {
        String tituloCombo = productTemp.getcProductName() + "-" + DecimalControlKt.montoDecimalPrecioSimbolo(precio);

        txtTituloCombo.setText(tituloCombo);
    }

    public void ActualizaPrecioVenta() {

        String textprecio = "Precio: ";
        BigDecimal precioTemp = precio.add(MontoModificacionTotal);
        String textoTemp = DecimalControlKt.montoDecimalPrecioSimbolo(precioTemp);
        textprecio = textprecio + textoTemp;

        Log.i("textp", textoTemp);
        Log.i("textp2", textprecio);

        String tituloCombo = productTemp.getcProductName() + "-" + DecimalControlKt.montoDecimalPrecioSimbolo(precioTemp);

        txtTituloCombo.setText(tituloCombo);
    }

    public void setItemProductoList(int idOrigen, mProduct product) {

        productList.set(idOrigen, product);

    }

    public BigDecimal TotalFinalCombo() {
        return precio.add(MontoModificacionTotal);
    }

    public BigDecimal ActualizaMontoModificacion() {
        BigDecimal montoAfectacion = new BigDecimal(0);
        for (mProduct product : productList) {
            montoAfectacion = montoAfectacion.add(product.getMontoModificacionPack());
        }
        MontoModificacionTotal = montoAfectacion;
        return MontoModificacionTotal;
    }

    public void setIdProducto(int idProducto) {

        this.idProducto = idProducto;
    }

    public void setTitle(String title) {


        this.title = title;
    }

    public void setListPack(List<PackElemento> packElementos) {
        this.packElementos = new ArrayList<>();
        this.packElementos = packElementos;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_combo, null);
        View f = LayoutInflater.from(getActivity()).inflate(R.layout.custom_title_dialog, null);
        v.setPadding(0, 40, 0, 0);
        TextView txtTitulo = f.findViewById(R.id.txtTitulo);
        MontoModificacionTotal = new BigDecimal(0);
        txtTitulo.setText(title);

        this.metricWindow = new MetricWindow(getActivity());
        txtTituloCombo = v.findViewById(R.id.txtTituloCombo);
        txtTituloCombo.setText(title);
        recyclerViews = new ArrayList<>();
        clickNumberPickerView = v.findViewById(R.id.clickNumberPickerView);
        txtEligeCantidad = v.findViewById(R.id.txtEligeCantidad);
        txtMensaje = v.findViewById(R.id.txtMensaje);

        asyncProducto = new AsyncProducto(getActivity());
        pbIndicator = v.findViewById(R.id.pbIndicator);
        txtCargando = v.findViewById(R.id.txtCargando);
        rlComboPack = v.findViewById(R.id.rlComboPack);
        rlComboPack.setOrientation(LinearLayout.VERTICAL);
        listaAdapters = new ArrayList<>();
        textViewList = new ArrayList<>();
        asyncProducto.ObtenerProductosPack(idProducto);

        pbIndicator.show();

        asyncProducto.setListenerComboPack(this);

        builder.setNegativeButton("Salir", (dialog, which) -> asyncProducto.CancelarBusquedaProductos());
        builder.setView(v).setPositiveButton("Seleccionar", (dialog, which) -> {

            if (packElementos != null) {
                if (packElementos.size() > 0) {
                    List<mProduct> list = new ArrayList<>();
                    numCatSelect = 0;
                    pack.setCantidad(Math.round(clickNumberPickerView.getValue()));
                    for (int i = 0; i < listaAdapters.size(); i++) {
                        if (listaAdapters.get(i).getPos() >= 0) {
                            numCatSelect++;
                            mProduct p = packElementos.get(i).getProductList().get((listaAdapters.get(i)).getPos());
                            p.setbDetallePack(true);
                            p.setStockDisponible((packElementos.get(i).getProductList().get((listaAdapters.get(i)).getPos())).getStockDisponible());
                            p.setcProductName(((packElementos.get(i)).getProductList().get((listaAdapters.get(i)).getPos())).getcProductName());
                            p.setPrecioVenta(((packElementos.get(i)).getProductList().get((listaAdapters.get(i)).getPos())).getPrecioVenta());
                            p.setIdProduct(((packElementos.get(i)).getProductList().get((listaAdapters.get(i)).getPos())).getIdProduct());
                            p.setcAdditionalInformation(((packElementos.get(i)).getProductList().get((listaAdapters.get(i)).getPos())).getcAdditionalInformation());
                            p.setEstadoModificador(((packElementos.get(i)).getProductList().get((listaAdapters.get(i)).getPos())).isEstadoModificador());
                            list.add(p);
                        }
                    }
                    ActualizaMontoModificacion();
                    pack.setPrecioVenta(TotalFinalCombo());
                    pack.setProductList(list);
                    if (DialogSelectCombo.this.numCatSelect <= 0) {
                        return;
                    }
                    if (DialogSelectCombo.this.numCatSelect == DialogSelectCombo.this.numCat) {
                        BigDecimal r = new BigDecimal(0);
                        for (CategoriaPack pack : DialogSelectCombo.this.categoriasPack) {
                            r = r.add(pack.getPrecio());
                        }
                        if (r.compareTo(DialogSelectCombo.this.precio) == 0) {
                            DialogSelectCombo.this.listenerSelectCombo.ComboPackSelect(DialogSelectCombo.this.pack);
                        } else {
                            new AlertDialog.Builder(DialogSelectCombo.this.getActivity()).setTitle("Advertencia")
                                    .setMessage("No se puede realizar la operación. Debe configurar los precios de los items en la configuración avanzada de productos")
                                    .setPositiveButton("Aceptar", null).create().show();
                        }
                        return;
                    }
                    DialogSelectCombo.this.listenerSelectCombo.ProductosComboIndividuales(DialogSelectCombo.this.pack);

                }
            }

        });
        clickNumberPickerView.setPickerValue(1);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;

    }

    public void InsertarPackProductos(List<PackElemento> packElementos) {

        pack = new PackElemento();
        pack.setIdProducto(idProducto);
        pack.setCantidad(1);
        productList = new ArrayList<>();
        this.packElementos = packElementos;
        this.numCat = packElementos.size();
        if (getContext().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            rlComboPack.setOrientation(LinearLayout.VERTICAL);
        } else {
            rlComboPack.setOrientation(LinearLayout.HORIZONTAL);
        }


        for (int i = 0; i < packElementos.size(); i++) {


            productList.add(packElementos.get(i).getProductList().get(0));
            productList.get(i).setbDetallePack(true);
            txtTitulo = new TextView(getActivity());
            txtTitulo.setText(packElementos.get(i).getDescripcion());
            txtTitulo.setLayoutParams(new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            txtTitulo.setPadding(10, 5, 10, 2);
            txtTitulo.setTextColor(getResources().getColor(R.color.colorAccent));
            txtTitulo.setTextSize(16);

            listaAdapter = new Listas("Hola", packElementos.get(i).getProductList(), i);
            listaAdapters.add(listaAdapter);
            listaAdapters.get(i).setListSeleccion(new ListSeleccion() {
                @Override
                public void ProductSeleccion(int origen, mProduct product) {
                    Log.d("db1", "db1");
                    productList.set(origen, product);
                    ActualizaMontoModificacion();
                    ActualizaPrecioVenta();
                }

                @Override
                public void IgnoreList(int origen) {
                    AsignaTituloPrecioBase();
                }
            });

            rv = new RecyclerView(getActivity());

            rv.setLayoutParams(new RecyclerView.LayoutParams
                    (RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));

            rv.setId(i);
            if (getContext().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                if (this.metricWindow.width() < this.metricWindow.height()) {
                    if (this.metricWindow.width() >= 400.0f) {
                        this.rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    } else {
                        this.rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                    }
                }
                rlComboPack.addView(txtTitulo);

                rv.setAdapter(listaAdapters.get(i));
                recyclerViews.add(rv);
                rlComboPack.addView(recyclerViews.get(i));

            } else {

                this.rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                rv.setAdapter(listaAdapters.get(i));
                recyclerViews.add(rv);
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(txtTitulo);
                linearLayout.addView(recyclerViews.get(i));

                linearLayout.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                rlComboPack.addView(linearLayout);
                LinearLayout l = new LinearLayout(getContext());
                l.setLayoutParams(new LinearLayout.LayoutParams(4, ViewGroup.LayoutParams.MATCH_PARENT));
                l.setBackgroundColor(Color.RED);
                rlComboPack.addView(l);
            }


        }

        ActualizaMontoModificacion();
        ActualizaPrecioVenta();
    }

    private class Listas extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        final List<mProduct> list;
        String title;
        int pos;
        int oldPos;
        boolean click;
        int id;
        byte[] imgData;
        Bitmap bmp;
        String Uri;
        ListenerSeleccion listenerSeleccion;
        DatosPackSeleccionado listenerSelectProducto;
        ListSeleccion listSeleccion;

        public void setListSeleccion(ListSeleccion listSeleccion) {
            this.listSeleccion = listSeleccion;
        }

        public void setListenerSeleccion(ListenerSeleccion listenerSeleccion) {
            this.listenerSeleccion = listenerSeleccion;
        }

        public void setListenerSelectProducto(DatosPackSeleccionado listenerSelectProducto) {
            this.listenerSelectProducto = listenerSelectProducto;
        }

        @Override

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo_combo, parent, false);
            v.setPadding(3, 2, 3, 10);
            return new ItemProducto(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemProducto vh = (ItemProducto) holder;
            vh.imageCheck.setVisibility(View.INVISIBLE);
            vh.txtCantidad.setVisibility(View.VISIBLE);
            vh.txtCantidad.setText("x" + String.format("%.0f", list.get(position).getStockDisponible()));
            vh.txtNombreArticuloGrid.setText(list.get(position).getProductNameInPack());
            vh.itemView.setOnClickListener(v -> {
                Log.i("click", "click " + String.valueOf(id));

            });
            if (position == pos) {
                vh.txtNombreArticuloGrid.setBackgroundColor(Color.parseColor("#C4EF5350"));
                vh.imageCheck.setVisibility(View.VISIBLE);
                vh.contentProducto.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                vh.txtCantidad.setBackgroundColor(Color.parseColor("#C4EF5350"));
            } else {
                vh.contentProducto.setBackgroundColor(Color.parseColor("#71000000"));
                vh.txtNombreArticuloGrid.setBackgroundColor(Color.parseColor("#71000000"));
                vh.imageCheck.setVisibility(View.INVISIBLE);
                vh.txtCantidad.setBackgroundColor(Color.parseColor("#71000000"));
            }

            imgData = list.get(position).getbImage();
            if (list.get(position).getTipoRepresentacionImagen() == 2) {
                vh.imageArticuloGrid.clearColorFilter();
                if (imgData != null) {
                    bmp = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                    vh.imageArticuloGrid.setPadding(0, 0, 0, 0);
                    vh.imageArticuloGrid.setImageBitmap(bmp);
                } else {
                    Uri = "@drawable/" + list.get(position).getCodigoForma();
                    Uri = Uri.trim();
                    vh.imageArticuloGrid.setImageResource(getContext().getResources().getIdentifier(Uri, null, getContext().getPackageName()));
                    //   vh.imageArticuloGrid.setColorFilter(Color.parseColor(list.get(position).getCodigoColor()));
                }
                Picasso.get()
                        .load(BASE_URL_API + "api/producto/GetImageProduct?codeCia=" + CodeCia + "&tipoConsulta=2&idProduct=" + list.get(position).idProduct)
                        .placeholder(getContext().getResources().getIdentifier(Uri, null, getContext().getPackageName()))
                        .error(R.drawable.circle_full_error)
                        .into(vh.imageArticuloGrid);
            } else if (list.get(position).getTipoRepresentacionImagen() == 1) {
                Uri = "@drawable/" + list.get(position).getCodigoForma();
                Uri = Uri.trim();
                vh.imageArticuloGrid.setImageResource(getActivity().getResources().getIdentifier(Uri, null, getActivity().getPackageName()));
                vh.imageArticuloGrid.setColorFilter(Color.parseColor(list.get(position).getCodigoColor()));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class ItemProducto extends RecyclerView.ViewHolder implements View.OnClickListener, DialogSelectModProducto.ListenerModProdSeleccion {
            DialogSelectModProducto dialogSelectModProducto;
            private TextView txtCantidad;
            private TextView txtNombreArticuloGrid;
            private ImageView imageArticuloGrid;
            private ImageView imageCheck;
            private RelativeLayout contentProducto;

            public ItemProducto(View itemView) {
                super(itemView);
                txtCantidad = itemView.findViewById(R.id.txtCantidadProducto);
                contentProducto = itemView.findViewById(R.id.contentProducto);
                txtNombreArticuloGrid = itemView.findViewById(R.id.txtNombreArticuloGrid);
                imageArticuloGrid = itemView.findViewById(R.id.imageArticuloGrid);
                imageCheck = itemView.findViewById(R.id.imageCheck);
                imageArticuloGrid.setOnClickListener(this);
                dialogSelectModProducto = new DialogSelectModProducto();
                dialogSelectModProducto.setListenerModProdSeleccion(this);
            }


            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.imageArticuloGrid:
                        if (getAdapterPosition() == pos) {
                            pos = -10;
                            oldPos = -10;
                        } else {
                            oldPos = pos;
                            pos = getAdapterPosition();
                            list.get(pos).setcAdditionalInformation("");
                            if (list.get(getAdapterPosition()).isEstadoModificador()) {
                                DialogFragment dialogFragment = dialogSelectModProducto;
                                dialogSelectModProducto.setIdProduct(list.get(getAdapterPosition()).getIdProduct(), list.get(getAdapterPosition()).getcProductName());
                                dialogFragment.show(getFragmentManager(), "SelectProduct");
                            }
                        }
                        if (pos >= 0) {
                            try {
                                Log.d("cl1", "" + id);
                                Log.d("cl2", list.get(pos).getcProductName());
                                Log.d("cl3", "" + pos);
                                listSeleccion.ProductSeleccion(id, list.get(pos));
                                Log.d("click", "click " + id);
                            } catch (Exception ex) {
                                Log.e("err-1", ex.toString());
                                Log.e("err-2", "" + id);
                                Log.e("err-3", list.get(pos).getcProductName());
                                Log.e("err-4", "" + pos);
                            }
                        } else {
                            listSeleccion.IgnoreList(getAdapterPosition());
                        }
                        notifyDataSetChanged();
                        break;

                }
            }

            @Override
            public void obtenerModificadorProducto(int idProducto, String modificador, int idPventa, float cantidad) {
                try {
                    list.get(pos).setcAdditionalInformation(modificador);
                } catch (Exception e) {
                    e.toString();
                }
            }
        }

        public Listas(String title, List<mProduct> list, int id) {
            this.list = new ArrayList<>();
            this.list.addAll(list);
            this.title = title;
            pos = 0;
            oldPos = -10;
            this.id = id;
            click = true;
        }

        public int getPos() {
            return pos;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        asyncProducto.CancelarBusquedaProductos();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

