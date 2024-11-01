package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogSelectCategoria;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogSelectCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.dialogSelectVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.R;

public class ActivityRegistros extends ActivityParent implements View.OnClickListener {

    Button btnRCliente;
    Button btnRVendedor;
    Button btnRInventario;
    Button btnRCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        btnRCliente = (Button) findViewById(R.id.btnRCliente);
        btnRVendedor = (Button) findViewById(R.id.btnRVendedor);
        btnRInventario = (Button) findViewById(R.id.btnRInventario);
        btnRCategoria = (Button) findViewById(R.id.btnCategoria);
        btnRCliente.setOnClickListener(this);
        btnRInventario.setOnClickListener(this);
        btnRVendedor.setOnClickListener(this);
        btnRCategoria.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRCliente:
                MostrarDialogCliente();
                break;
            case R.id.btnRVendedor:
                MostrarVendedores();
                break;
            case R.id.btnRInventario:
                MostrarInventario();
                break;
            case R.id.btnCategoria:
                MostrarDialogCategorias();
                break;
        }
    }

    private void MostrarInventario() {
        Intent intent = new Intent(this, ActivityInventario.class);
        startActivity(intent);
    }
    private void MostrarVendedores() {
        dialogSelectVendedor selectVendedor = new dialogSelectVendedor();
        DialogFragment dialogFragment = selectVendedor;
        dialogFragment.show(getFragmentManager(), "Mostrar Vendedores Registro");
    }

    private void MostrarDialogCliente() {
         new dialogSelectCustomer().show(getSupportFragmentManager(),"Editar");

    }

    private void MostrarDialogCategorias() {
        DialogFragment dialogFragment = new DialogSelectCategoria();
        dialogFragment.show(getFragmentManager(), "Seleccionar sCategorias");
    }
}









