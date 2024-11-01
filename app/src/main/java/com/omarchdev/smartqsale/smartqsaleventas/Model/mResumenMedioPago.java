package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 01/02/2018.
 */

public class mResumenMedioPago {

    private int idMedioPago;
    private String descripcionMedioPago;
    private BigDecimal montoEntrada, montoSalida, montoDisponible;

    public mResumenMedioPago(int idMedioPago, String descripcionMedioPago, BigDecimal montoEntrada, BigDecimal montoSalida, BigDecimal montoDisponible) {
        this.idMedioPago = idMedioPago;
        this.descripcionMedioPago = descripcionMedioPago;
        this.montoEntrada = montoEntrada;
        this.montoSalida = montoSalida;
        this.montoDisponible = montoDisponible;
    }

    public int getIdMedioPago() {
        return idMedioPago;
    }

    public void setIdMedioPago(int idMedioPago) {
        this.idMedioPago = idMedioPago;
    }

    public String getDescripcionMedioPago() {
        return descripcionMedioPago;
    }

    public void setDescripcionMedioPago(String descripcionMedioPago) {
        this.descripcionMedioPago = descripcionMedioPago;
    }

    public BigDecimal getMontoEntrada() {
        return montoEntrada;
    }

    public void setMontoEntrada(BigDecimal montoEntrada) {
        this.montoEntrada = montoEntrada;
    }

    public BigDecimal getMontoSalida() {
        return montoSalida;
    }

    public void setMontoSalida(BigDecimal montoSalida) {
        this.montoSalida = montoSalida;
    }

    public BigDecimal getMontoDisponible() {
        return montoDisponible;
    }

    public void setMontoDisponible(BigDecimal montoDisponible) {
        this.montoDisponible = montoDisponible;
    }
}
