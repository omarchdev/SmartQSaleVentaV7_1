package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listapreciosselect.ListaPreciosSelectFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ListaPreciosSelect : ActivityParent() {
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_precios_select_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListaPreciosSelectFragment.newInstance())
                    .commitNow()
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            getSupportActionBar()!!.setTitle("Listas de precios")
        }
    }

}
