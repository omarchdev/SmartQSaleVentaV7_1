package com.omarchdev.smartqsale.smartqsaleventas.Fragment;


import static com.omarchdev.smartqsale.smartqsaleventas.Control.MethodsNumber.ReplaceCommaToDot;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
//import android.widget.EditText;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncAreasProduccion;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncProductKt;
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncSubCategorias;
import com.omarchdev.smartqsale.smartqsaleventas.CategoriaAdapter;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Control.NumberTextWatcher;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ClickEditTextNumberKt;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogScannerCam;
import com.omarchdev.smartqsale.smartqsaleventas.ImagenesController.ImagenesController;
import com.omarchdev.smartqsale.smartqsaleventas.Model.AdditionalPriceProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Model.DecimalControlKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mSubCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUnidadMedida;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RVAdapterAdditionalPrice;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ProductoDBasicos extends Fragment implements View.OnClickListener, DialogScannerCam.ScannerResult, CompoundButton.OnCheckedChangeListener, AsyncSubCategorias.ResultadoSubCategorias, AsyncProductKt.IVerificarExisteNombre {
    List<mUnidadMedida> listaUnidades;
    int idProducto;
    int idSubcategoria;
    boolean cargaInit;
    Switch switchPrecioMult;
    EditText edtPVentaN;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    ImageButton btnAgregarPVenta;
    String Directorio = null;
    ImageButton imgChoisePhoto, imgTakePhoto;
    ImageView imageProduct;
    ArrayAdapter adapterMedida;
    Button btnScan;
    boolean permitir;
    boolean campoNombre, campoCodigo, campoPrecioVenta;
    TextInputLayout edtcodeBar, edtNombre, edtCodigo, edtDescripcion, edtCantidadMaximaWeb, edtObservacion, edtCantidadStock, edtCantidadReserva, edtPrecioCompra, edtPrecioVenta;
    RecyclerView rvPrecioVentas;
    ImagenesController imgController;
    String nombreProducto, codigoProducto, codigoBarras, descripcionProducto, observacionProducto;
    BigDecimal stock, stockReserva, precioCompra, precioVenta;
    int idCategoria;
    byte esFavorito;
    ListenerDatosBasicos listenerDatosBasicos;
    List<mCategoriaProductos> listCategorias;
    private String NombreTemp;
    private Spinner spinnerCategoria;
    CategoriaAdapter categoriaAdapter;
    int longitudCategorias;
    Spinner spnUnidadMedida, spnAreasProduccion, spnSubCategoria;
    List<String> listUnidades;
    RVAdapterAdditionalPrice adapterAdditionalPrice;
    List<AdditionalPriceProduct> priceProductList;
    AsyncAreasProduccion asyncAreasProduccion;
    ArrayAdapter adapterAreasP;
    List<String> listaAreas;
    List<String> listaSubCategorias;
    List<mAreaProduccion> listaAreasProduccion;
    List<mSubCategoria> mSubCategoriaList;
    ArrayAdapter adapterSubCategorias;
    AsyncSubCategorias asyncSubCategorias;
    mProduct product;
    AsyncProductKt asyncProductKt;

    @Override
    public void NombreEnUso(@NotNull String mensaje) {
        edtNombre.setError(mensaje);
        new AlertDialog.Builder(getContext()).setTitle("Advertencia").setMessage(mensaje).setPositiveButton("Salir", null).create().show();
    }

    @Override
    public void ErrorConexion(@NotNull String mensaje) {
        new AlertDialog.Builder(getContext()).setTitle("Advertencia").setMessage(mensaje).setPositiveButton("Salir", null).create().show();
    }

    public interface ListenerDatosBasicos {

        public void obtenerDatosBasicos(String nombreProducto, String codigoProducto, String codigoBarras,
                                        String descripcion, String observacionProducto,
                                        BigDecimal stock, BigDecimal stockReserva, BigDecimal precioCompra, BigDecimal precioVenta, int idCategoria);


        public void ValidacionCampos(boolean campoNombre, boolean campoCodigo, boolean campoPrecioVenta);

    }

    public void setListAreasProduccion(List<mAreaProduccion> result) {
        listaAreasProduccion = result;
        listaAreas.clear();
        for (mAreaProduccion a : result) {
            listaAreas.add(a.getCDescripcionArea());
        }
        adapterAreasP.notifyDataSetChanged();
    }

    public int getIdAreaProducction() {

        return listaAreasProduccion.get(spnAreasProduccion.getSelectedItemPosition()).getIdArea();

    }

    public void setListCategorias(List<mCategoriaProductos> listCategorias) {
        this.listCategorias = listCategorias;
        longitudCategorias = listCategorias.size();
        categoriaAdapter.AddElementRegistroProductos(listCategorias);
    }

    public void setListenerDatosBasicos(ListenerDatosBasicos listenerDatosBasicos) {

        this.listenerDatosBasicos = listenerDatosBasicos;

    }

    public ProductoDBasicos() {
        // Required empty public constructor
        nombreProducto = "";
        codigoProducto = "";
        codigoBarras = "";
        stock = new BigDecimal(0);
        stockReserva = new BigDecimal(0);
        precioCompra = new BigDecimal(0);
        precioVenta = new BigDecimal(0);
        descripcionProducto = "";
        observacionProducto = "";
        idCategoria = 0;
        listCategorias = new ArrayList<>();
        campoNombre = false;
        campoCodigo = false;
        campoPrecioVenta = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = null;
        try {
            v = inflater.inflate(R.layout.activity_inventario, container, false);
            cargaInit = false;
            NombreTemp = "";
            adapterAdditionalPrice = new RVAdapterAdditionalPrice();
            btnAgregarPVenta = v.findViewById(R.id.btnAgregarPVenta);
            imgController = new ImagenesController();
            edtCantidadMaximaWeb = v.findViewById(R.id.edtCantidadMaximaWeb);
            rvPrecioVentas = v.findViewById(R.id.rvPVentaAdd);
            switchPrecioMult = v.findViewById(R.id.switchPrecioMult);
            imgChoisePhoto =  v.findViewById(R.id.btnBuscarImagen);
            spnSubCategoria = v.findViewById(R.id.spnSubCategoria);
            imgTakePhoto =  v.findViewById(R.id.btnCamaraImagen);
            imageProduct =  v.findViewById(R.id.imageProduct);
            edtNombre =  v.findViewById(R.id.edtProductName);
            edtCodigo =  v.findViewById(R.id.editCodigoProducto);
            edtCantidadStock = v.findViewById(R.id.edtCantidadStock);
            edtCantidadReserva = v.findViewById(R.id.edtCantidadReserva);
            edtPrecioCompra = v.findViewById(R.id.edtPrecioCompra);
            edtPrecioVenta = v.findViewById(R.id.edtPrecioVenta);
            edtDescripcion =  v.findViewById(R.id.edtDescripcion);
            edtObservacion =  v.findViewById(R.id.edtObservacion);

            spnAreasProduccion = v.findViewById(R.id.spnAreasProduccion);
            edtcodeBar = v.findViewById(R.id.edtcodeBar);
            spnUnidadMedida = v.findViewById(R.id.spnUnidadMedida);
            btnScan =  v.findViewById(R.id.btnScan);
            btnScan.setOnClickListener(this);
            spinnerCategoria =  v.findViewById(R.id.spinner_categoria);
            listUnidades = new ArrayList<>();
            asyncProductKt = new AsyncProductKt();
            categoriaAdapter = new CategoriaAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, listCategorias);
            spinnerCategoria.setAdapter(categoriaAdapter);
            esFavorito = 0;
            spinnerCategoria.setOnItemSelectedListener(listenerCategoria);
            Botones();
            SetListenerEditText();
            edtCantidadStock.setEnabled(false);
            edtPrecioCompra.setEnabled(false);
            edtCantidadReserva.setEnabled(false);
            adapterMedida = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, listUnidades);
            spnUnidadMedida.setAdapter(adapterMedida);
            rvPrecioVentas.setAdapter(adapterAdditionalPrice);
            rvPrecioVentas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            switchPrecioMult.setOnCheckedChangeListener(this);
            switchPrecioMult.setChecked(false);
            listaAreas = new ArrayList<>();
            listaAreas.add("Buscando areas de produccion");
            adapterAreasP = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listaAreas);
            spnAreasProduccion.setAdapter(adapterAreasP);
            listaSubCategorias = new ArrayList<>();
            listaSubCategorias.add("Sin subcategorias");
            mSubCategoriaList = new ArrayList<>();
            adapterSubCategorias = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listaSubCategorias);
            asyncSubCategorias = new AsyncSubCategorias();
            asyncSubCategorias.setResultadoSubCategorias(this);
            spnSubCategoria.setAdapter(adapterSubCategorias);
            asyncProductKt.setIverificarExisteNombre(this);
            ClickEditTextNumberKt.ClickTextInputLayout(edtPrecioVenta);
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
        //  asyncAreasProduccion.setListenerAreasProduccion(this);
        //  asyncAreasProduccion.ObtenerAreasProduccionRegProd();

        return v;
    }

    @Override
    public void ErrorBusqueda() {
        Toast.makeText(getContext(), "Error al conseguir las subcategorias", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void ResultadoBusqueda(@Nullable List<mSubCategoria> listaSubCategoria) {
        mSubCategoriaList = listaSubCategoria;
        listaSubCategorias.clear();
        listaSubCategorias.add("Sin subcategoria");
        for (mSubCategoria sb : mSubCategoriaList) {
            listaSubCategorias.add(sb.getDescripcionSubCategoria());
        }
        adapterSubCategorias.notifyDataSetChanged();
        if (cargaInit) {
            if (idProducto != 0) {
                for (int i = 0; i < mSubCategoriaList.size(); i++) {
                    if (mSubCategoriaList.get(i).getIdSubCategoria() == product.getIdSubCategoria()) {
                        spnSubCategoria.setSelection(i + 1);
                        break;
                    }
                }
            }
        }
        cargaInit = false;
    }

    public int ObteneriIdSubCategoria() {
        if (spnSubCategoria.getSelectedItemPosition() > 0) {
            return mSubCategoriaList.get(spnSubCategoria.getSelectedItemPosition() - 1).getIdSubCategoria();
        } else {

            return 0;
        }
    }

    AdapterView.OnItemSelectedListener listenerCategoria = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            idCategoria = listCategorias.get(position).getIdCategoria();
            listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                    descripcionProducto, observacionProducto,
                    stock, stockReserva, precioCompra, precioVenta, idCategoria);
            if (position == 0) {

                listaSubCategorias.clear();
                listaSubCategorias.add("Sin subcategoria");
                adapterSubCategorias.notifyDataSetChanged();

            } else {
                asyncSubCategorias.ObtenerSubCategorias(idCategoria);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            edtPrecioVenta.setEnabled(false);
            rvPrecioVentas.setVisibility(View.VISIBLE);
        } else {
            edtPrecioVenta.setEnabled(true);
            rvPrecioVentas.setVisibility(View.GONE);
        }


    }

    public void setCantidadMaximaDefault(double cantidadMaxima) {
        edtCantidadMaximaWeb.getEditText().setText(String.format("%.0f", cantidadMaxima));
    }

    public BigDecimal getCantidadMaxima() {
        return new BigDecimal(ReplaceCommaToDot(edtCantidadMaximaWeb.getEditText().getText().toString()));
    }

    public void Botones() {
        imgChoisePhoto.setOnClickListener(this);
        imgTakePhoto.setOnClickListener(this);
        btnAgregarPVenta.setOnClickListener(this);
    }

    private void SetListenerEditText() {
        edtNombre.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                nombreProducto = s.toString();
                try {

                    if (nombreProducto.trim().length() < 2 && nombreProducto.trim().length() > 0) {
                        edtNombre.setError(null);
                        edtNombre.setError("El nombre debe tener mas de 3 carácteres");
                        campoNombre = false;
                    } else if (nombreProducto.trim().length() == 0) {
                        edtNombre.setError(null);
                        edtNombre.setError("Este campo es obligatorio");
                        campoNombre = false;
                    } else {
                        edtNombre.setError(null);
                        campoNombre = true;
                    }
                    listenerDatosBasicos.ValidacionCampos(campoNombre, campoCodigo, campoPrecioVenta);
                    listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                            descripcionProducto, observacionProducto,
                            stock, stockReserva, precioCompra, precioVenta, idCategoria);
                } catch (Exception e) {
                    e.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtCodigo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codigoProducto = s.toString();
                if (codigoProducto.trim().length() < 3 && codigoProducto.trim().length() > 0) {
                    edtCodigo.setError("El nombre debe tener mas de 3 carácteres");
                    campoCodigo = false;
                } else if (codigoProducto.trim().length() == 0) {
                    edtCodigo.setError("Este campo es obligatorio");
                    campoCodigo = false;
                } else {
                    edtCodigo.setError(null);
                    campoCodigo = true;
                }

                listenerDatosBasicos.ValidacionCampos(campoNombre, campoCodigo, campoPrecioVenta);
                listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                        descripcionProducto, observacionProducto,
                        stock, stockReserva, precioCompra, precioVenta, idCategoria);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtcodeBar.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codigoBarras = s.toString();
                listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                        descripcionProducto, observacionProducto,
                        stock, stockReserva, precioCompra, precioVenta, idCategoria);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtNombre.getEditText().setOnFocusChangeListener((view, b) -> {
            if (!b) {
                if (edtNombre.getEditText().getText().toString().length() > 2 && !edtNombre.getEditText().getText().toString().trim().equals(NombreTemp.trim())) {

                    asyncProductKt.VerificarNombreExistenciaProducto(idProducto, edtNombre.getEditText().getText().toString());
                }
            }
        });
        edtCantidadStock.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (s.toString().equals(".")) {
                        stock = new BigDecimal(0);
                    } else {
                        stock = new BigDecimal(s.toString().replace(",", "."));
                    }

                } else if (s.toString().equals("")) {
                    stock = new BigDecimal(0);

                }

                listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                        descripcionProducto, observacionProducto,
                        stock, stockReserva, precioCompra, precioVenta, idCategoria);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtCantidadReserva.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (s.toString().equals(".")) {
                        stockReserva = new BigDecimal(0);
                    } else {
                        stockReserva = new BigDecimal(s.toString().replace(",", "."));
                    }
                } else if (s.toString().equals("")) {
                    stockReserva = new BigDecimal(0);
                }

                listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                        descripcionProducto, observacionProducto,
                        stock, stockReserva, precioCompra, precioVenta, idCategoria);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPrecioCompra.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    precioCompra = new BigDecimal(0);

                } else if (!s.toString().equals("")) {
                    if (s.toString().equals(".")) {
                        precioCompra = new BigDecimal(0);
                    } else {

                        precioCompra = new BigDecimal(s.toString().replace(",", "."));
                    }
                }
                listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                        descripcionProducto, observacionProducto,
                        stock, stockReserva, precioCompra, precioVenta, idCategoria);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        try{
            edtPrecioVenta.getEditText().setText(DecimalControlKt.montoDecimalTexto(new BigDecimal(0)));
            NumberTextWatcher pventaWatcher = new NumberTextWatcher(edtPrecioVenta.getEditText());
            pventaWatcher.setINumberTextWatcher(number -> {
                try{
                    precioVenta = number;
                    listenerDatosBasicos.ValidacionCampos(campoNombre, campoCodigo, campoPrecioVenta);
                    listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                            descripcionProducto, observacionProducto,
                            stock, stockReserva, precioCompra, precioVenta, idCategoria);
                }catch (Exception ex2){
                    Log.e("ex2->",ex2.toString());
                }

            });
            edtPrecioVenta.getEditText().addTextChangedListener(pventaWatcher);
        }catch (Exception ex){
            Log.e("ex1->",ex.toString());
        }

        /*edtPrecioVenta.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.toString().equals("")){
                                 precioVenta = new BigDecimal(0);
                                                 campoPrecioVenta=false;
                        }
                        else{
                            if(s.toString().equals(".")){
                                precioVenta=new BigDecimal(0);
                            }
                            else {

                                precioVenta = new BigDecimal(s.toString().replace(",","."));
                            }
                                campoPrecioVenta=true;
                        }

                listenerDatosBasicos.ValidacionCampos(campoNombre,campoCodigo,campoPrecioVenta);
                listenerDatosBasicos.obtenerDatosBasicos(nombreProducto,codigoProducto,codigoBarras,
                        descripcionProducto,observacionProducto,
                        stock,stockReserva,precioCompra,precioVenta,idCategoria);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        edtDescripcion.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descripcionProducto = s.toString();

                listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                        descripcionProducto, observacionProducto,
                        stock, stockReserva, precioCompra, precioVenta, idCategoria);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtObservacion.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                observacionProducto = s.toString();

                listenerDatosBasicos.obtenerDatosBasicos(nombreProducto, codigoProducto, codigoBarras,
                        descripcionProducto, observacionProducto,
                        stock, stockReserva, precioCompra, precioVenta, idCategoria);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void Scan() {

        DialogScannerCam dialogScannerCam = new DialogScannerCam();
        dialogScannerCam.setScannerResult(this);
        dialogScannerCam.show(getActivity().getFragmentManager(), "hOLA");
    }


    public void ValidarCampoNombre() {
        edtNombre.setError("El nombre es muy corto ");
    }

    public BigDecimal getPrecioVenta(){

        return new BigDecimal(ReplaceCommaToDot(edtPrecioVenta.getEditText().getText().toString()));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnScan:

                SolicitarPermiso();

                break;

            case R.id.btnAgregarPVenta:
                if (switchPrecioMult.isChecked()) {
                    AgregarPVentaAddicional();
                } else {
                    new AlertDialog.Builder(getContext()).setTitle("Advertencia")
                            .setMessage("Debe activar la opcion multiples precios de venta para usar está opción")
                            .setPositiveButton("Salir", null).create().show();
                }

                break;

        }

    }

    public void ObtenerUnidadesMedida(List<String> unidadesMedida) {
        this.listUnidades = unidadesMedida;
        adapterMedida.addAll(listUnidades);

    }

    public void UnidadesMedida(List<mUnidadMedida> listaUnidades) {

        this.listaUnidades = listaUnidades;
    }

    public int ObtenerUnidadSeleccionada() {

        return spnUnidadMedida.getSelectedItemPosition();

    }


    public void ObtenerCodigoGenerado(String codigoGenerado) {

        edtCodigo.getEditText().setText(codigoGenerado);

    }

    public void HabilitarCampos() {
        edtCantidadStock.setEnabled(true);
        edtCantidadStock.getEditText().setEnabled(true);
        edtPrecioCompra.setEnabled(true);
        edtPrecioCompra.getEditText().setEnabled(true);
        edtCantidadMaximaWeb.getEditText().setEnabled(true);
        spinnerCategoria.setEnabled(true);
        edtNombre.getEditText().setEnabled(true);
        edtCodigo.getEditText().setEnabled(true);
        edtcodeBar.getEditText().setEnabled(true);
        edtObservacion.getEditText().setEnabled(true);
        edtDescripcion.getEditText().setEnabled(true);
        edtCantidadStock.getEditText().setEnabled(true);
        edtCantidadReserva.getEditText().setEnabled(true);
        edtPrecioCompra.getEditText().setEnabled(true);
        edtPrecioVenta.getEditText().setEnabled(true);
        btnScan.setEnabled(true);
        spnUnidadMedida.setEnabled(true);
        edtCantidadStock.getEditText().setEnabled(false);
        edtPrecioCompra.getEditText().setEnabled(false);
        edtCantidadReserva.getEditText().setEnabled(false);
        spnSubCategoria.setEnabled(true);
        switchPrecioMult.setEnabled(true);
        btnAgregarPVenta.setEnabled(true);
        spnAreasProduccion.setEnabled(true);
        btnAgregarPVenta.setEnabled(true);
    }

    public void DeshabilitarCampos() {
        edtCantidadStock.setEnabled(true);
        edtCantidadStock.getEditText().setEnabled(false);
        edtPrecioCompra.setEnabled(true);
        edtPrecioCompra.getEditText().setEnabled(false);
        edtCantidadMaximaWeb.getEditText().setEnabled(false);
        spnUnidadMedida.setEnabled(false);
        spinnerCategoria.setEnabled(false);

        edtCodigo.getEditText().setEnabled(false);
        edtcodeBar.getEditText().setEnabled(false);
        edtObservacion.getEditText().setEnabled(false);
        edtDescripcion.getEditText().setEnabled(false);
        edtCantidadStock.getEditText().setEnabled(false);
        edtCantidadReserva.getEditText().setEnabled(false);
        edtPrecioCompra.getEditText().setEnabled(false);
        edtNombre.getEditText().setEnabled(false);
        edtPrecioVenta.getEditText().setEnabled(false);
        btnScan.setEnabled(false);
        spnSubCategoria.setEnabled(false);
        switchPrecioMult.setEnabled(false);
        btnAgregarPVenta.setEnabled(false);
        edtCantidadStock.getEditText().setEnabled(false);
        edtPrecioCompra.getEditText().setEnabled(false);
        edtCantidadReserva.getEditText().setEnabled(false);
        spnAreasProduccion.setEnabled(false);
    }

    @Override
    public void ResultadoScanner(String resultText) {

        edtcodeBar.getEditText().setText(resultText);
    }

    public void ValidarCampoNombre(boolean valor) {

        if (valor == false) {
            edtNombre.setError("Este campo es obligatorio");
        } else {
            edtNombre.setError(null);
        }

    }

    public void ValidarCampoCodigo(boolean valor) {
        if (valor == false) {
            edtCodigo.setError("Este campo es obligatorio");
        } else {
            edtCodigo.setError(null);
        }
    }

    public void ValidarCampoPrecioVenta(boolean valor) {
        if (valor == false) {
            edtPrecioVenta.setError("Este campo es obligatorio");
        } else {
            edtPrecioVenta.setError(null);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {

        }
    }

    public void AdvertenciaPreciosAdicionales() {
        new AlertDialog.Builder(getContext()).setMessage("Cuando active la opción multiple precios de venta." +
                "Debe agregar como mínimo 1 precio de venta.").setTitle("").setPositiveButton("Salir", null).create().show();
    }

    public List<AdditionalPriceProduct> obtenerPreciosAdiccionales() {

        return adapterAdditionalPrice.AdditionalPrice();
    }

    public boolean TienePreciosVentaAdiccionales() {
        return switchPrecioMult.isChecked();
    }

    public boolean EsCeroCampoPVenta() {
        if (edtPrecioVenta.getEditText().getText().toString().length() == 0) {
            return false;
        } else if (edtPrecioVenta.getEditText().getText().toString().equals(".")) {
            return false;
        } else if (new BigDecimal(ReplaceCommaToDot(edtPrecioVenta.getEditText().getText().toString())).compareTo(new BigDecimal(0)) == 0) {
            return false;
        } else {
            return true;
        }

    }

    public void setInfoProduct(mProduct product) {

        this.product = product;
        cargaInit = true;
        idProducto = product.getIdProduct();
        for (int i = 0; i < longitudCategorias; i++) {
            if (listCategorias.get(i).getIdCategoria() == product.getIdCategoria()) {
                spinnerCategoria.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < listUnidades.size(); i++) {
            if (listUnidades.get(i).equals(product.getUnidadMedida())) {
                spnUnidadMedida.setSelection(i);
            }
        }
        for (int i = 0; i < listaUnidades.size(); i++) {
            if (listaUnidades.get(i).getIdUnidad() == product.getIdUnidadMedida()) {
                spnUnidadMedida.setSelection(i);
            }
        }
        for (int i = 0; i < this.listaAreasProduccion.size(); i++) {
            if ((this.listaAreasProduccion.get(i)).getIdArea() == product.getIdAreaProduccion()) {
                this.spnAreasProduccion.setSelection(i);
            }
        }
        NombreTemp = product.getcProductName();
        nombreProducto = product.getcProductName();
        codigoProducto = product.getcKey();
        codigoBarras = product.getCodigoBarra();
        observacionProducto = product.getObservacionProducto();
        descripcionProducto = product.getcAdditionalInformation();
        stock = product.getStockDisponible();
        stockReserva = product.getStockReserva();
        precioCompra = product.getPrecioCompra();
        precioVenta = product.getPrecioVenta();
        switchPrecioMult.setChecked(product.isMultiplePVenta());
        edtNombre.getEditText().setText(nombreProducto);
        edtCodigo.getEditText().setText(codigoProducto);
        edtcodeBar.getEditText().setText(codigoBarras);
        edtObservacion.getEditText().setText(observacionProducto);
        edtDescripcion.getEditText().setText(descripcionProducto);
        edtCantidadStock.getEditText().setText(String.format("%.2f", stock));
        edtCantidadReserva.getEditText().setText(String.format("%.2f", stockReserva));
        edtPrecioCompra.getEditText().setText(String.format("%.2f", precioCompra));
        edtPrecioVenta.getEditText().setText(String.format("%.2f", precioVenta));
        edtCantidadMaximaWeb.getEditText().setText(String.format("%.0f", product.getDCantidadMaximaPedido()));
        if (product.isMultiplePVenta()) {
            if (product.getPriceProductList() != null) {
                adapterAdditionalPrice.AgregarListado(product.getPriceProductList());
            } else {
                Toast.makeText(getContext(), "Error al obtener los precios adicionales del producto", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }
    }

    private void SolicitarPermiso() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            Toast.makeText(getContext(), "Debe activar la camara para usar el scanner", Toast.LENGTH_LONG).show();

        } else {
            Scan();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Si el permiso está concedido prosigue con el flujo normal
                Scan();
            } else {
                //Si el permiso no fue concedido no se puede continuar
            }
        }
    }

    public void SetErrorCampoCodigo() {
        edtCodigo.setError("Ingrese otro código");
    }

    public void AlertaPackMultiplePrecio() {


    }


    public void AgregarPVentaAddicional() {

        View d = getLayoutInflater().inflate(R.layout.dialog_agregar_pventa, null);
        edtPVentaN = d.findViewById(R.id.edtPVentaN);
        edtPVentaN.setText(DecimalControlKt.montoDecimalTexto(new BigDecimal(0)));
        NumberTextWatcher pventaWatcherAdd = new NumberTextWatcher(edtPVentaN);
        edtPVentaN.addTextChangedListener(pventaWatcherAdd);
        pventaWatcherAdd.setINumberTextWatcher(number -> {});
        permitir = true;
        new AlertDialog.Builder(getContext()).
                setView(d).setTitle("Agregar precio de Venta")
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permitir = true;
                        if (edtPVentaN.getText().toString().equals(".")) {
                            permitir = false;
                        } else if (edtPVentaN.getText().toString().equals("")) {
                            permitir = false;
                        }
                        if (permitir) {
                            if (new BigDecimal(ReplaceCommaToDot(edtPVentaN.getText().toString())).compareTo(new BigDecimal(0)) == 1) {
                                adapterAdditionalPrice.add(new BigDecimal(ReplaceCommaToDot(edtPVentaN.getText().toString())));
                            } else {
                                Toast.makeText(getContext(), "El valor debe ser mayor a 0", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).setNegativeButton("Cancelar", null).create().show();


        edtPVentaN.setHint(Constantes.DivisaPorDefecto.SimboloDivisa + "0.00");

    }

    public void IngresoFragment() {


        edtNombre.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (edtNombre.getEditText().getText().toString().length() > 2) {
                        asyncProductKt.VerificarNombreExistenciaProducto(idProducto, edtNombre.getEditText().getText().toString());
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        edtNombre.getEditText().setOnFocusChangeListener(null);

    }

    public void EstadoGuardar() {


    }
}













