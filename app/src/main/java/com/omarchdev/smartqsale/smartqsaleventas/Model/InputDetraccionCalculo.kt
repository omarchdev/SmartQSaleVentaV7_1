package com.omarchdev.smartqsale.smartqsaleventas.Model

import java.math.BigDecimal

/*
        public decimal Monto_con_igv { get; set; } = 0;

        public string Codigo_Detraccion { get; set; } = "";*/
data class InputDetraccionCalculo(
    var Monto_con_igv: BigDecimal = BigDecimal.ZERO,
    var Codigo_Detraccion: String = ""
)
