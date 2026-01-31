package com.omarchdev.smartqsale.smartqsaleventas.Model

import java.math.BigDecimal

class ListaPrecioVenta {

    var descripcionVenta:String=""
    var cCodigo_unidad_medida_venta:String=""
    var npreci_unitario: BigDecimal =BigDecimal.ZERO
    var nfactor_venta: BigDecimal =BigDecimal.ZERO
    var descripcionCompra:String=""
    var cCodigo_unidad_medida_compra:String=""
    var nprecio_unitario_compra: BigDecimal =BigDecimal.ZERO
    var nfactor_compra: BigDecimal =BigDecimal.ZERO
    var descripcionConsumo:String=""
    var cCodigo_unidad_medida_consumo:String=""
    var nprecio_unitario_consumo: BigDecimal =BigDecimal.ZERO
    var nfactor_consumo: BigDecimal =BigDecimal.ZERO



}