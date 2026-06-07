package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;
import com.google.gson.annotations.SerializedName;

/**
 * Created by OMAR CHH on 26/01/2018.
 */

public class mResumenFlujoCaja {


    @SerializedName("codtitulo")
    private String codtitulo;
    @SerializedName("titutloPago")
    private String titutloPago;
    @SerializedName("descripcionTitulo")
    private String descripcionTitulo;
    @SerializedName("codColor")
    private String codColor;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("subtituloCaja")
    private String subtituloCaja;
    @SerializedName("monto")
    private BigDecimal monto;
    @SerializedName("simbolo")
    private String simbolo;

    public mResumenFlujoCaja() {

        codtitulo = "";
        descripcionTitulo = "";
        codColor = "";
        descripcion = "";
        subtituloCaja = "";
        monto = new BigDecimal(0);
        simbolo = "";
        titutloPago = "";

    }

    public String getCodtitulo() {
        return codtitulo;
    }

    public void setCodtitulo(String codtitulo) {
        this.codtitulo = codtitulo;
    }

    public String getDescripcionTitulo() {
        return descripcionTitulo;
    }

    public void setDescripcionTitulo(String descripcionTitulo) {
        this.descripcionTitulo = descripcionTitulo;
    }

    public String getCodColor() {
        return codColor;
    }

    public void setCodColor(String codColor) {
        this.codColor = "#" + codColor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSubtituloCaja() {
        return subtituloCaja;
    }

    public void setSubtituloCaja(String subtituloCaja) {
        this.subtituloCaja = subtituloCaja;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getTitutloPago() {
        return titutloPago;
    }

    public void setTitutloPago(String titutloPago) {
        this.titutloPago = titutloPago;
    }
}
