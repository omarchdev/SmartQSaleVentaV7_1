package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.listapreciosselect.ListaPreciosSelectFragment
import com.omarchdev.smartqsale.smartqsaleventas.R

class ListaPreciosSelect : AppCompatActivity() {
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_precios_select_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListaPreciosSelectFragment.newInstance())
                    .commitNow()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
    }

}
