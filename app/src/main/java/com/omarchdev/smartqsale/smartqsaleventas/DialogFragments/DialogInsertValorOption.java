package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.dpizarro.autolabel.library.Label;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 03/03/2018.
 */

public class DialogInsertValorOption extends DialogFragment implements View.OnClickListener {
    Toast toast;
    AutoLabelUI mAutoLabel;
    Dialog dialog;
    EditText edt;
    Button btnAgregarValor;
    ListenerValorOpcion listenerValorOpcion;
    int numPositionPadre;
    List<Label> listLabel = new ArrayList<>();
    List<String> stringList = new ArrayList<>();
    boolean encontrado;
    int contador;
    int idOpcion;


    public interface ListenerValorOpcion {

        void ObtenerValores(List<String> listLabel, int NumPositionPadre);

    }

    public void setListenerValorOpcion(ListenerValorOpcion listenerValorOpcion) {
        this.listenerValorOpcion = listenerValorOpcion;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View v = (getActivity().getLayoutInflater().inflate(R.layout.dialog_insert_values_option, null));
        toast = new Toast(getActivity());
        contador = 0;
        encontrado = false;
        edt = (EditText) v.findViewById(R.id.edtValor);
        btnAgregarValor = (Button) v.findViewById(R.id.btnAgregarValor);
        btnAgregarValor.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mAutoLabel = (AutoLabelUI) v.findViewById(R.id.label_view);

        AutoLabelUISettings autoLabelUISettings = new AutoLabelUISettings.Builder()
                .withMaxLabels(10)
                .withShowCross(true)

                .withLabelsClickables(false)
                .withLabelsClickables(false)
                .withLabelPadding(30)
                .withBackgroundResource(R.drawable.round_corner_background)
                .withIconCross(R.drawable.close_circle).build();
        mAutoLabel.setSettings(autoLabelUISettings);
        builder.setTitle("Ingreso de valores");
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (Label label : mAutoLabel.getLabels()) {
                    stringList.add(label.getText());

                }
                listenerValorOpcion.ObtenerValores(stringList, numPositionPadre);

            }
        });

        for (String string : stringList) {

            mAutoLabel.addLabel(string);

        }

        stringList.clear();
        builder.setView(v);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnAgregarValor) {

            if (edt.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Ingrese el nombre del valor.", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < mAutoLabel.getLabels().size(); i++) {

                    if (edt.getText().toString().equals(mAutoLabel.getLabel(i).getText())) {
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    mAutoLabel.addLabel(edt.getText().toString());
                    edt.setText("");
                } else {
                    contador++;
                    toast.makeText(getActivity(), "El valor ya fue ingresado", Toast.LENGTH_SHORT).show();
                }

                if (contador == 5) {
                    toast.makeText(getActivity(), "Eres terco,no? ", Toast.LENGTH_SHORT).show();
                    contador = 0;
                }
                encontrado = false;

            }
        }
    }

    public void setNumPositionPadre(int numPositionPadre) {

        this.numPositionPadre = numPositionPadre;

    }


    public void setStringList(List<String> stringList) {
        this.stringList.clear();
        this.stringList.addAll(stringList);
    }

    public void setListLabel(List<Label> listLabel) {

        this.listLabel = listLabel;

    }
}
