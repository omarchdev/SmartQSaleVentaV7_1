package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncTiendas
import com.omarchdev.smartqsale.smartqsaleventas.R
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterTiendasIngreso
import kotlinx.android.synthetic.main.activity_select_tienda.*

class SelectTienda : ActivityParent(), RvAdapterTiendasIngreso.PositionClick1, AsyncTiendas.ListenerConfigTienda {

    val rvAdapterTiendasIngreso=RvAdapterTiendasIngreso()
    val asyncTiendas=AsyncTiendas()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verificaConexionBd=false
        setContentView(R.layout.activity_select_tienda)
        rvTiendasSelect.adapter=rvAdapterTiendasIngreso
        rvTiendasSelect.layoutManager=LinearLayoutManager(this)
        rvAdapterTiendasIngreso.positionClick=this
        asyncTiendas.listenerConfigTienda=this
        supportActionBar!!.title ="Seleccione tienda a ingresar  "

        supportActionBar!!.elevation = 2f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val list = ArrayList<String>()
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                list.add(Manifest.permission.BLUETOOTH_CONNECT)
                //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
            }
            if (list.size > 0) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    100
                )
            }
        }
    }
    override fun IngresarTienda() {
        val intent = Intent(this, PantallaPrincipal::class.java)
        startActivity(intent)
        finish()
    }

    override fun ObtenerTienda(id: Int) {
        asyncTiendas.ObtenerConfigTienda(id,this)
    }

}
