package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes
import com.omarchdev.smartqsale.smartqsaleventas.Model.GetJsonCiaTiendaBase64x3
import com.omarchdev.smartqsale.smartqsaleventas.Model.OpcionVariante
import com.omarchdev.smartqsale.smartqsaleventas.Model.ValorOpcionVariante
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVarianteRepo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.SQLException

class ControladorVariante {

    private val codeCia = GetJsonCiaTiendaBase64x3()
    private var retro = Retrofit.Builder().baseUrl(Constantes.BASECONN.BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create()).build()
    private var iVarianteRepo = retro.create(
        IVarianteRepo::class.java
    )

    fun getOpcionesValores(idProduct: Int): List<OpcionVariante>? {

        var opcionVariante: OpcionVariante?
        var valorOpcionVariante: ValorOpcionVariante?
        var list: MutableList<OpcionVariante>?
        var listVariantes: MutableList<ValorOpcionVariante>?
        try {
            val variantesApi= iVarianteRepo.GetVariantesBase(codeCia, "2", idProduct)
            val variantesExec=variantesApi.execute()
            val variantesBase =variantesExec.body()
            list = ArrayList(variantesBase!!.opciones)
            if (list.size == 1) {
                list.add(OpcionVariante())
                list.add(OpcionVariante())
            } else if (list.size == 2) {
                list.add(OpcionVariante())
            }
            listVariantes = ArrayList(variantesBase!!.valoresOpciones)

            for (i in list!!.indices) {
                for (a in listVariantes.indices) {
                    if (listVariantes[a].numItemPadre == i + 1) {
                        list[i].listValores.add(listVariantes[a])
                    }
                }
            }
            try {
                if (list.size > 0) {
                    var i = list.size - 1
                    while (0 <= i) {
                        if (list[i].listValores[0].descripcion == "") {
                            list.removeAt(i)
                        }
                        i--
                    }
                }
            } catch (e: Exception) {
                e.toString()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            list = null
        } finally {

        }
        return list
    }
}