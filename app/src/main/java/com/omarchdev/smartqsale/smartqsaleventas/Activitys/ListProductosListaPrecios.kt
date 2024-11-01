package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listproductoslistaprecios.ListProductosListaPreciosFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ListProductosListaPrecios : AppCompatActivity() {
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_productos_lista_precios_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListProductosListaPreciosFragment.newInstance())
                    .commitNow()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
    }
}
