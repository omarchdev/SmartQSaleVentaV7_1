package com.omarchdev.smartqsale.smartqsaleventas

import com.omarchdev.smartqsale.smartqsaleventas.AsyncTask.AsyncCategoria
import org.junit.Test

class CatagoriaApiTest {

    @Test
    @Throws(Exception::class)
    fun AgregarCategoria_isCorrect() {
        try{
            var api= AsyncCategoria()
            api.GuardarCategoria("Categoria Api  333")

        }catch (ex:Exception){
            ex.toString()
        }


    }
}