package com.omarchdev.smartqsale.smartqsaleventas.Model;

/**
 * Created by OMAR CHH on 06/12/2017.
 */

public class TipodePago {

    private String idTipoDePago;
    private String descripcion;
    private byte esTarjeta;
    private byte esEfectivo;
    private byte esPorCobrar;

    public TipodePago() {
        idTipoDePago = "";
        descripcion = "";
        esTarjeta = 0;
        esEfectivo = 0;
        esPorCobrar = 0;

    }

    public TipodePago(String idTipoDePago, String descripcion, byte esTarjeta, byte esEfectivo, byte esPorCobrar) {
        this.idTipoDePago = idTipoDePago;
        this.descripcion = descripcion;
        this.esTarjeta = esTarjeta;
        this.esEfectivo = esEfectivo;
        this.esPorCobrar = esPorCobrar;
    }
}
