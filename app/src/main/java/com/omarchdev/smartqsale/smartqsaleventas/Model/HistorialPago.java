package com.omarchdev.smartqsale.smartqsaleventas.Model;

/**
 * Created by OMAR CHH on 06/12/2017.
 */

public class HistorialPago {

    private int numeroVenta;
    private String idTipoDePago;
    private float montoPagado;
    private String fechaDePago;
    private String cliente;

    public HistorialPago() {

        numeroVenta = 0;
        idTipoDePago = "";
        montoPagado = 0.00f;
        fechaDePago = "";
        cliente = "";
    }

    public HistorialPago(int numeroVenta, String idTipoDePago, float montoPagado, String fechaDePago, String cliente) {
        this.numeroVenta = numeroVenta;
        this.idTipoDePago = idTipoDePago;
        this.montoPagado = montoPagado;
        this.fechaDePago = fechaDePago;
        this.cliente = cliente;
    }


    public int getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(int numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public String getIdTipoDePago() {
        return idTipoDePago;
    }

    public void setIdTipoDePago(String idTipoDePago) {
        this.idTipoDePago = idTipoDePago;
    }

    public float getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(float montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getFechaDePago() {
        return fechaDePago;
    }

    public void setFechaDePago(String fechaDePago) {
        this.fechaDePago = fechaDePago;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
