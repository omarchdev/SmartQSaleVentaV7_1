package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.operarioconfig.operario_config
import com.omarchdev.smartqsale.smartqsaleventas.R

class ActivityOperarioEdit : ActivityParent() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operario_config)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, operario_config.newInstance())
                .commitNow()
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()!!.setHomeAsUpIndicator(R.drawable.arrow_back_home)
            getSupportActionBar()!!.setTitle("Editar operario")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}