package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.omarchdev.smartqsale.smartqsaleventas.Fragment.FragmentInventario;
import com.omarchdev.smartqsale.smartqsaleventas.R;

public class ActivityInventario extends ActivityParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario2);

        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        ft1.replace(R.id.content_Inventario, new FragmentInventario());
        ft1.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
