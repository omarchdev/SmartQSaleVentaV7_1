package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.omarchdev.smartqsale.smartqsaleventas.R;

public class InventarioProductos extends ActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_productos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
