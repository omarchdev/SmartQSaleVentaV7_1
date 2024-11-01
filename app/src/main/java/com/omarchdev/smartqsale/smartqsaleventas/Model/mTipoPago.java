package com.omarchdev.smartqsale.smartqsaleventas.Model;

/**
 * Created by OMAR CHH on 13/12/2017.
 */

public class mTipoPago {

    private int idTipoPago;
    private String descripcionTipoPago;

    public mTipoPago(int idTipoPago, String descripcionTipoPago) {
        this.idTipoPago = idTipoPago;
        this.descripcionTipoPago = descripcionTipoPago;
    }


    public mTipoPago() {

    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public void setDescripcionTipoPago(String descripcionTipoPago) {
        this.descripcionTipoPago = descripcionTipoPago;
    }

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public String getDescripcionTipoPago() {
        return descripcionTipoPago;
    }
}
