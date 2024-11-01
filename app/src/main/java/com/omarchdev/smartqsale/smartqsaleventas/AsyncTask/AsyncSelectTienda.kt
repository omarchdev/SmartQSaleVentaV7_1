package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AsyncSelectTienda(){



    fun ObtenerConfigTienda(idTienda:Int){


        var respuesta=BdConnectionSql.getSinglentonInstance().ObtenerConfigTienda(idTienda)

         GlobalScope.launch  {




           launch(Dispatchers.Main){


            }

        }


    }



}