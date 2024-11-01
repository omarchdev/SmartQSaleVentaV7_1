package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 26/01/2018.
 */

public class mResumenFlujoCaja {


    private String codtitulo;
    private String titutloPago;
    private String descripcionTitulo;
    private String codColor;
    private String descripcion;
    private String subtituloCaja;
    private BigDecimal monto;
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
