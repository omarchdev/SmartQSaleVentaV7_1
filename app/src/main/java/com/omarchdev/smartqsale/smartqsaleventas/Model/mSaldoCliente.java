package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 09/01/2018.n
 */

public class mSaldoCliente {

    private int idSaldoCliente;
    private int idCliente;
    private String nombreCliente;
    private String descripcion;
    private BigDecimal montoSaldo;

    public mSaldoCliente(int idSaldoCliente, int idCliente, String nombreCliente, String descripcion, BigDecimal montoSaldo) {
        this.idSaldoCliente = idSaldoCliente;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.descripcion = descripcion;
        this.montoSaldo = montoSaldo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdSaldoCliente() {
        return idSaldoCliente;
    }

    public void setIdSaldoCliente(int idSaldoCliente) {
        this.idSaldoCliente = idSaldoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMontoSaldo() {
        return montoSaldo;
    }

    public void setMontoSaldo(BigDecimal montoSaldo) {
        this.montoSaldo = montoSaldo;
    }
}
