package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Control.NumberTextWatcher;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ClickEditTextNumberKt;
import com.omarchdev.smartqsale.smartqsaleventas.Model.TipoModificadorPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 11/04/2018.
 */

public class DialogEditInsertProductPack extends DialogFragment {


    boolean guardar;
    byte metodo;//metodo 100-> ingresar   101->editar
    int idProducto;
    TextInputLayout edtPrecioVenta, edtCantidad, edtMontoModifica;
    BigDecimal PrecioVenta, Cantidad, PrecioTemp, MontoModifica;
    int idTipoModifica;
    String nombreArticulo;
    TextView txtTitulo;
    Button btnAceptar,btnCancelar;
    GuardarDatosProductoPack guardarDatosProductoPack;
    Spinner spnModificadorPack;
    List<TipoModificadorPack> listModificadores = new ArrayList<>();
    ArrayAdapter<TipoModificadorPack> adapter;

    public interface GuardarDatosProductoPack {

        public void setDatosProducto(byte metodo, mProduct product);

    }

    public void setListenerGuardarProducto(GuardarDatosProductoPack guardarDatosProductoPack) {
        this.guardarDatosProductoPack = guardarDatosProductoPack;
    }


    public DialogEditInsertProductPack() {
    }

    public void setData(byte metodo, mProduct product) {
        this.metodo = metodo;
        this.idProducto = product.getIdProduct();
        this.PrecioVenta = product.getPrecioVenta();
        this.Cantidad = product.getStockDisponible();
        this.nombreArticulo = product.getcProductName();
        this.MontoModifica = product.getMontoModifica();
        this.idTipoModifica = product.getIdTipoModifica();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_insert_product_pack, null);
        View f = getActivity().getLayoutInflater().inflate(R.layout.custom_title_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCustomTitle(f);
        builder.setView(v);
        guardar = false;
        try {
            edtCantidad = v.findViewById(R.id.edtCantidad);
            edtPrecioVenta = v.findViewById(R.id.edtPrecioVenta);
            spnModificadorPack = v.findViewById(R.id.spnModificadorPack);
            edtMontoModifica = v.findViewById(R.id.edtMontoModifica);
            txtTitulo = f.findViewById(R.id.txtTitulo);
            txtTitulo.setText(nombreArticulo);
            btnAceptar= v.findViewById(R.id.btnAceptar);
            btnCancelar=v.findViewById(R.id.btnCancelar);
            edtMontoModifica.getEditText().setText(String.format("%.2f", MontoModifica));
            edtCantidad.getEditText().setText(String.format("%.2f", Cantidad));
            edtPrecioVenta.getEditText().setText(String.format("%.2f", PrecioVenta));
            ClickEditTextNumberKt.ClickTextInputLayout(edtCantidad);
            ClickEditTextNumberKt.ClickTextInputLayout(edtMontoModifica);
            ClickEditTextNumberKt.ClickTextInputLayout(edtPrecioVenta);
            edtMontoModifica.getEditText().addTextChangedListener(new NumberTextWatcher(edtMontoModifica.getEditText()));
            NumberTextWatcher textW=new NumberTextWatcher(edtCantidad.getEditText());

            edtCantidad.getEditText().addTextChangedListener(textW);

            adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, Constantes.TipoModificadoresPack.listadoModificadoresPack);
            spnModificadorPack.setAdapter(adapter);
            btnAceptar.setOnClickListener(v1 -> {

                Cantidad=new BigDecimal(edtCantidad.getEditText().getText().toString());
                if (Cantidad.compareTo(new BigDecimal(0)) == 0) {

                    guardar = false;
                } else {
                    guardar = true;
                }
                if (guardar) {

                    mProduct product = new mProduct();
                    product.setPrecioVenta(new BigDecimal(edtPrecioVenta.getEditText().getText().toString()));
                    product.setStockDisponible(new BigDecimal(edtCantidad.getEditText().getText().toString()));
                    product.setMontoModifica(new BigDecimal(edtMontoModifica.getEditText().getText().toString()));
                    product.setIdProduct(idProducto);
                    product.setIdTipoModifica(Constantes.TipoModificadoresPack.listadoModificadoresPack.get(spnModificadorPack.getSelectedItemPosition()).getIdTipoMod());
                    dismiss();
                    guardarDatosProductoPack.setDatosProducto(metodo, product);

                }

            });
            btnCancelar.setOnClickListener(v1 -> {
                dismiss();
            });
            int pos=0;
            for (int i = 0; i < Constantes.TipoModificadoresPack.listadoModificadoresPack.size(); i++) {
                if(Constantes.TipoModificadoresPack.listadoModificadoresPack.get(i).getIdTipoMod()==idTipoModifica){
                    pos=i;
                }
            }

            spnModificadorPack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                  edtMontoModifica.setVisibility((Constantes.TipoModificadoresPack.listadoModificadoresPack.get(position).getBVisibleMonto())?View.VISIBLE:View.GONE);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spnModificadorPack.setSelection(pos);
          /*  builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });*/

        } catch (Exception ex) {
            Log.e("er1", ex.toString());
            Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

        return builder.create();

    }

    TextWatcher watcherCantidad = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
               /* if (s.toString().equals("")) {

                    edtPrecioVenta.getEditText().setText(String.format("%.2f",PrecioVenta.multiply(new BigDecimal(0))));
                } else {

                    edtPrecioVenta.getEditText().setText(String.format("%.2f",PrecioVenta.multiply(new BigDecimal(s.toString()))));
                }*/
            } catch (Exception e) {
                e.toString();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher watcherPrecioVenta = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().equals("")) {
                edtPrecioVenta.getEditText().setText("0.00");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
