package com.omarchdev.smartqsale.smartqsaleventas.Activitys

import android.os.Bundle
import com.omarchdev.smartqsale.smartqsaleventas.Fragment.ConfiguracionBase
import com.omarchdev.smartqsale.smartqsaleventas.R

class ActivityDemo : ActivityParent() {

    val demoFragment=ConfiguracionBase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
      /*  demoFragment.useSwitch(true)
        demoFragment.AgregarSwitch(this,1,"Prueba",true)
        demoFragment.AgregarSwitch(this,2,"Prueba2",false)
        demoFragment.AgregarSwitch(this,3,"Prueba3",true)
        demoFragment.AgregarSwitch(this,4,"Prueba4",false)
        demoFragment.AgregarSwitch(this,5,"Prueba5",true)
        demoFragment.AgregarSwitch(this,6,"Prueba6",false)
        demoFragment.setMarginsSwitch(top =40,bottom = 40,left = 2,right = 2 )
*/
        val fm1 = supportFragmentManager
        val ft1 = fm1.beginTransaction()
        ft1.replace(R.id.content_frame, demoFragment)
        ft1.commit()
/*

*/
  }
}
