package com.omarchdev.smartqsale.smartqsaleventas.Model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal


/*
    public class DetraccionCalculo
    {


        public decimal Monto_depositar_detraccion_banco { get; set; } = 0;
        public decimal Monto_depositar_sin_detraccion { get; set; } = 0;
        public decimal Porcentaje_detraccion { get; set; } = 0;
        public string CCuenta_detraccion { get; set; } = "";
        public bool BUsa_detraccion { get; set; } = false;
    }

* */
data class DetraccionCalculo(
    @SerializedName("monto_depositar_detraccion_banco")
    val Monto_depositar_detraccion_banco: BigDecimal ,
    @SerializedName("monto_depositar_sin_detraccion")
    val Monto_depositar_sin_detraccion: BigDecimal ,
    @SerializedName("porcentaje_detraccion")
    val Porcentaje_detraccion: BigDecimal ,
    @SerializedName("cCuenta_detraccion")
    val CCuenta_detraccion: String = "",
    @SerializedName("bUsa_detraccion")
    val BUsa_detraccion: Boolean = false
)
