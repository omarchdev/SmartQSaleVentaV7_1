package com.omarchdev.smartqsale.smartqsaleventas

import com.omarchdev.smartqsale.smartqsaleventas.API.ApiConsultaDocumento
import org.junit.Test

class ConsultaDocumentoTest {
    @Test
    @Throws(Exception::class)
    fun consultaApiDocumento_isCorrect() {
        var api=ApiConsultaDocumento()
        var result=api.ConsultaPersonaPorTipoDocumento("10715286195","RUC_1")
        var resultTemp=result
    }
}
