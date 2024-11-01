package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProducto;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncVariantes;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.AdditionalPriceProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Model.OpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterPriceAdditional;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import pl.polak.clicknumberpicker.ClickNumberPickerView;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

/**
 * Created by OMAR CHH on 20/03/2018.
 */

public class DialogVariantesProducto extends DialogFragment implements AsyncVariantes.VariantesProduct, View.OnClickListener {
    AVLoadingIndicatorView avLoadingIndicatorView;

    BigDecimal precioSelect;
    View view;
    List<AdditionalPriceProduct> priceProductList;
    boolean inicio;
    AsyncVariantes asyncVariantes;
    ImageView imgProducto;
    RvAdapterPriceAdditional adapterPriceAdditional;
    RecyclerView rvMultiplePrecio;
    int idProducto = 0;
    Dialog dialog;
    Bitmap bmp;
    String codigoForma;
    boolean multiplePV;
    byte tipoRepresentacion;
    String Uri;
    String codigoColor;
    RadioRealButtonGroup radioRealButtonGroup1, radioRealButtonGroup2, radioRealButtonGroup3;
    TextView txtTituloOpcion1, txtTituloOpcion2, txtTituloOpcion3, txtAlertaValores, txtInformacionProducto, txtCantidad, contentMensaje, txtPrecioVenta;
    LinearLayout contenedorValores;
    int numValores;
    int numOpciones;
    List<String> opcionesSeleccionadas;
    List<String> precios;
    String a, b, c;
    ListenerValoresSeleccionadas listenerValoresSeleccionadas;
    ClickNumberPickerView clickNumberPickerView;
    String descripcion;
    String titulo;
    AsyncProducto asyncProducto;
    Button btnAceptar, btnCancelar;
    BigDecimal cantidadDisponible;
    boolean permitirCantidad;
    boolean controlStock;
    boolean controlCarga;
    ProgressBar progressBar;

    public void setListenerValoresSeleccionadas(ListenerValoresSeleccionadas listenerValoresSeleccionadas) {
        this.listenerValoresSeleccionadas = listenerValoresSeleccionadas;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAceptar) {
            if (!opcionesSeleccionadas.get(0).equals("")) {
                if (controlStock) {
                    if (cantidadDisponible.floatValue() < clickNumberPickerView.getValue()) {
                        permitirCantidad = false;
                    } else if (cantidadDisponible.floatValue() >= clickNumberPickerView.getValue()) {
                        permitirCantidad = true;
                    }
                } else {
                    permitirCantidad = true;
                }
                if (permitirCantidad) {
                    if (controlCarga) {
                        if (multiplePV) {
                            precioSelect = priceProductList.get(adapterPriceAdditional.getPosActual()).getAdditionalPrice();
                        } else {
                            precioSelect = new BigDecimal(0);
                        }
                        listenerValoresSeleccionadas.obtenerValores(opcionesSeleccionadas, idProducto, clickNumberPickerView.getValue(), multiplePV, precioSelect);
                        dialog.dismiss();
                    }
                } else {
                    contentMensaje.setVisibility(View.VISIBLE);
                }
            } else {

            }
        } else if (v.getId() == R.id.btnCancelar) {
            dialog.dismiss();

        }

    }

    public interface ListenerValoresSeleccionadas {

        public void obtenerValores(List<String> valoresSeleccionados, int idProduct, float CantidadSeleccionada, boolean multiPv, BigDecimal precio);

    }

    public void setControStock(boolean controStock) {
        this.controlStock = controStock;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_elegir_variante, null);
        avLoadingIndicatorView = (AVLoadingIndicatorView) v.findViewById(R.id.pbIndicator);
        precios = new ArrayList<>();
        adapterPriceAdditional = new RvAdapterPriceAdditional();
        rvMultiplePrecio = v.findViewById(R.id.rvMultiplePrecio);
        rvMultiplePrecio.setAdapter(adapterPriceAdditional);
        rvMultiplePrecio.setLayoutManager(new LinearLayoutManager(getActivity()));
        permitirCantidad = true;
        contentMensaje = v.findViewById(R.id.contentMensaje);
        btnAceptar = v.findViewById(R.id.btnAceptar);
        btnCancelar = v.findViewById(R.id.btnCancelar);
        txtCantidad = v.findViewById(R.id.txtCantidad);
        progressBar = v.findViewById(R.id.progressBar);
        txtCantidad.setVisibility(View.GONE);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        asyncProducto = new AsyncProducto();
        opcionesSeleccionadas = new ArrayList<>();
        opcionesSeleccionadas.add("");
        opcionesSeleccionadas.add("");
        opcionesSeleccionadas.add("");
        txtInformacionProducto = v.findViewById(R.id.txtInformacionProducto);
        contenedorValores = v.findViewById(R.id.contenedorValores);
        txtAlertaValores = v.findViewById(R.id.txtMensajeValores);
        txtTituloOpcion1 = v.findViewById(R.id.txtTituloOpcion1);
        txtTituloOpcion2 = v.findViewById(R.id.txtTituloOpcion2);
        txtTituloOpcion3 = v.findViewById(R.id.txtTituloOpcion3);
        txtPrecioVenta = v.findViewById(R.id.txtPrecioVenta);
        clickNumberPickerView = v.findViewById(R.id.clickNumberPickerView);
        radioRealButtonGroup1 = v.findViewById(R.id.rgVar1);
        radioRealButtonGroup2 = v.findViewById(R.id.rgVar2);
        radioRealButtonGroup3 = v.findViewById(R.id.rgVar3);
        radioRealButtonGroup1.setBorderColor(Color.parseColor("#b6b6b6"));
        radioRealButtonGroup1.setDividerColor(R.color.colorAccent);
        radioRealButtonGroup2.setBorderColor(Color.parseColor("#b6b6b6"));
        radioRealButtonGroup2.setDividerColor(Color.parseColor("#b6b6b6"));
        radioRealButtonGroup3.setBorderColor(Color.parseColor("#b6b6b6"));
        radioRealButtonGroup3.setDividerColor(R.color.colorAccent);
        progressBar.setVisibility(View.GONE);
        priceProductList = new ArrayList<>();
        asyncVariantes = new AsyncVariantes();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        numOpciones = 0;
        numValores = 0;
        imgProducto = (ImageView) v.findViewById(R.id.imgProduct);
        builder.setView(v);
        builder.setTitle(titulo);
        txtTituloOpcion1.setText("");
        txtTituloOpcion2.setText("");
        txtTituloOpcion3.setText("");
        clickNumberPickerView.setPickerValue(1);
        controlCarga = false;
        contentMensaje.setVisibility(View.GONE);
        txtPrecioVenta.setVisibility(View.GONE);
        switch (tipoRepresentacion) {

            case 1:
                try {
                    Uri = codigoForma;
                    Uri = Uri.trim();
                    imgProducto.setImageResource(getActivity().getResources().getIdentifier(Uri, null, getActivity().getPackageName()));
                    imgProducto.setColorFilter(Color.parseColor(codigoColor));
                } catch (Exception e) {
                    e.toString();
                }
                break;
            case 2:
                try {
                    if (bmp != null) {


                    } else {
                        Uri = Uri + codigoForma;
                        Picasso.get()
                                .load(BASE_URL_API+"api/producto/GetImageProduct?codeCia="+GetJsonCiaTiendaBase64x3()+"&tipoConsulta=2&idProduct=" +idProducto)
                                .error(R.drawable.circle_full_error)
                                .into( imgProducto);
                    }
                } catch (Exception e) {
                    e.toString();
                }
                break;

        }
        txtInformacionProducto.setText(descripcion);
        contenedorValores.setVisibility(View.INVISIBLE);
        txtAlertaValores.setVisibility(View.INVISIBLE);
        asyncVariantes.ObtenerValoresVariante(idProducto);
        asyncVariantes.setVariantesProduct(this);
        avLoadingIndicatorView.show();
        dialog = builder.create();
        return dialog;
    }


    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setBmpProduct(Bitmap bmp, byte tipoRepresentacion, String codigoForma, String codigoColor) {
        this.bmp = bmp;
        this.codigoColor = codigoColor;
        this.tipoRepresentacion = tipoRepresentacion;
        this.codigoForma = "@drawable/" + codigoForma;
    }


    @Override
    public void ValoresObtenidos(List<OpcionVariante> opcionVarianteList) {
        inicio = true;
        if (opcionVarianteList != null) {

            for (int i = 0; i < opcionVarianteList.size(); i++) {
                numValores = numValores + opcionVarianteList.get(i).getListValores().size();
            }
            if (numValores > 0) {
                txtAlertaValores.setVisibility(View.INVISIBLE);
                contenedorValores.setVisibility(View.VISIBLE);
                try {

                    for (int i = 0; i < opcionVarianteList.size(); i++) {
                        numOpciones = i + 1;
                        if (i == 0) {
                            txtTituloOpcion1.setText(opcionVarianteList.get(i).getDescripcion());
                            radioRealButtonGroup1.setOnPositionChangedListener((button, currentPosition, lastPosition) -> {
                                opcionesSeleccionadas.set(0, button.getText());
                                if (!inicio) {
                                    ObtenerCantidadDisponibleVariante();
                                }
                            });


                        } else if (i == 1) {
                            txtTituloOpcion2.setText(opcionVarianteList.get(i).getDescripcion());
                            radioRealButtonGroup2.setOnPositionChangedListener((button, currentPosition, lastPosition) -> {
                                opcionesSeleccionadas.set(1, button.getText());
                                if (!inicio) {

                                    ObtenerCantidadDisponibleVariante();
                                }

                            });

                        } else if (i == 2) {
                            txtTituloOpcion3.setText(opcionVarianteList.get(i).getDescripcion());

                            radioRealButtonGroup3.setOnPositionChangedListener((button, currentPosition, lastPosition) -> {

                                opcionesSeleccionadas.set(2, button.getText());
                                if (!inicio) {

                                    ObtenerCantidadDisponibleVariante();
                                }
                            });

                        }
                        radioRealButtonGroup1.setLayoutParams(new RadioRealButtonGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        for (int a = 0; a < opcionVarianteList.get(i).getListValores().size(); a++) {
                            RadioRealButton radioRealButton = new RadioRealButton(getActivity());
                            if (i == 0) {

                                radioRealButton.setText(opcionVarianteList.get(i).getListValores().get(a).getDescripcion());
                                radioRealButton.setTextSizeSP(14);
                                radioRealButton.setGravity(Gravity.CENTER);
                                radioRealButton.setLayoutParams(new RadioRealButton.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT + 180, radioRealButtonGroup1.getHeight()));
                                radioRealButtonGroup1.addView(radioRealButton);
                            } else if (i == 1) {
                                radioRealButton.setText(opcionVarianteList.get(i).getListValores().get(a).getDescripcion());
                                radioRealButton.setTextSizeSP(14);
                                radioRealButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT + 180, radioRealButtonGroup2.getHeight()));
                                radioRealButtonGroup2.addView(radioRealButton);
                            } else if (i == 2) {
                                radioRealButton.setText(opcionVarianteList.get(i).getListValores().get(a).getDescripcion());
                                radioRealButton.setTextSizeSP(14);
                                radioRealButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT + 180, radioRealButtonGroup3.getHeight()));
                                radioRealButtonGroup3.addView(radioRealButton);
                            }
                        }
                        if (i == 0) {

                            radioRealButtonGroup1.setPosition(0);
                        } else if (i == 1) {
                            radioRealButtonGroup2.setPosition(0);
                        } else if (i == 2) {
                            radioRealButtonGroup3.setPosition(0);

                        }


                    }


                } catch (Exception e) {
                    e.toString();
                }
            } else {
                contenedorValores.setVisibility(View.INVISIBLE);
                txtAlertaValores.setVisibility(View.VISIBLE);
            }
            avLoadingIndicatorView.hide();
            controlCarga = false;
            if (inicio) {
                ObtenerCantidadDisponibleVariante();
            }
            inicio = false;
        } else {
            Toast.makeText(getActivity(), "Error al descargar los datos.Verifique su conexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    public void ObtenerCantidadDisponibleVariante() {
        progressBar.setVisibility(View.VISIBLE);
        contentMensaje.setVisibility(View.GONE);
        controlCarga = false;

        txtPrecioVenta.setVisibility(View.GONE);
        txtCantidad.setVisibility(View.GONE);
        asyncProducto.ObtenerDisponibilidadProductoVariante(idProducto, opcionesSeleccionadas);
        asyncProducto.setListenerDisponibilidadVariantes(new AsyncProducto.ListenerDisponibilidadVariantes() {
            @Override
            public void ResultadoProducto(mProduct product) {
                progressBar.setVisibility(View.GONE);
                if (controlStock) {
                    txtCantidad.setVisibility(View.VISIBLE);
                    txtCantidad.setText(String.format("%.2f", product.getdQuantity()) + "-" +
                            String.format("%.2f", product.getStockReserva()) + "=" +
                            String.format("%.2f", new BigDecimal(product.getdQuantity()).subtract(product.getStockReserva()))
                    );
                }

                controlCarga = true;
                multiplePV = product.isMultiplePVenta();

                priceProductList.clear();

                if (product.isMultiplePVenta()) {
                    rvMultiplePrecio.setVisibility(View.VISIBLE);
                    priceProductList.addAll(product.getPriceProductList());
                    precios.clear();
                    for (AdditionalPriceProduct a : product.getPriceProductList()) {
                        precios.add(String.format("%.2f", a.getAdditionalPrice()));
                    }
                    txtPrecioVenta.setVisibility(View.GONE);
                    adapterPriceAdditional.AgregarListaPrecios(precios);
                } else {
                    rvMultiplePrecio.setVisibility(View.GONE);
                    txtPrecioVenta.setVisibility(View.VISIBLE);
                }
                txtPrecioVenta.setText(Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", product.getPrecioVenta()));
                cantidadDisponible = new BigDecimal(product.getdQuantity()).subtract(product.getStockReserva());
            }

            @Override
            public void ErrorConsulta() {
                progressBar.setVisibility(View.GONE);
                txtPrecioVenta.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error al obtener el producto", Toast.LENGTH_LONG).show();
                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Error al obtener el producto", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

}
