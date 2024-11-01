package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.omarchdev.smartqsale.smartqsaleventas.Activitys.ui.confignumphoneweb.ConfigNumPhoneWebFragment
import com.omarchdev.smartqsale.smartqsaleventas.R


class ConfigNumPhoneWeb : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config_num_phone_web_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfigNumPhoneWebFragment.newInstance())
                    .commitNow()
        }
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_home)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
