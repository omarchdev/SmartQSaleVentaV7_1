package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 09/12/2017.
 */

public class mPagosEnVenta {
    @SerializedName("idTipoPago")
    int idTipoPago;
    @SerializedName("cTipoPago")
    String cTipoPago;
    @SerializedName("tipoPago")
    String tipoPago;
    @SerializedName("cantidadPagada")
    BigDecimal cantidadPagada;
    @SerializedName("esEfectivo")
    boolean esEfectivo;
    @SerializedName("activaPagoExterno")
    boolean activaPagoExterno;
    @SerializedName("id_cab_pedido")
    int IdCabeceraPedido;

    @SerializedName("idPago")
    int IdPago;

    public int getIdPago() {
        return IdPago;
    }

    public void setIdPago(int idPago) {
        IdPago = idPago;
    }

    public int getIdCabeceraPedido() {
        return IdCabeceraPedido;
    }

    public void setIdCabeceraPedido(int idCabeceraPedido) {
        IdCabeceraPedido = idCabeceraPedido;
    }




    public boolean isActivaPagoExterno() {
        return activaPagoExterno;
    }

    public void setActivaPagoExterno(boolean activaPagoExterno) {
        this.activaPagoExterno = activaPagoExterno;
    }

    public mPagosEnVenta() {
        idTipoPago = 0;
        cTipoPago = "";
        tipoPago = "";
        cantidadPagada = new BigDecimal(0);
        esEfectivo=false;
        IdCabeceraPedido=0;
    }

    public mPagosEnVenta(int idTipoPago, String cTipoPago, String tipoPago, BigDecimal cantidadPagada,boolean esEfectivo) {
        this.idTipoPago = idTipoPago;
        this.cTipoPago = cTipoPago;
        this.tipoPago = tipoPago;
        this.cantidadPagada = cantidadPagada;
        this.esEfectivo=esEfectivo;
    }

    public boolean isEsEfectivo() {
        return esEfectivo;
    }

    public void setEsEfectivo(boolean esEfectivo) {
        this.esEfectivo = esEfectivo;
    }

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getcTipoPago() {
        return cTipoPago;
    }

    public void setcTipoPago(String cTipoPago) {
        this.cTipoPago = cTipoPago;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public BigDecimal getCantidadPagada() {
        return cantidadPagada;
    }

    public void setCantidadPagada(BigDecimal cantidadPagada) {
        this.cantidadPagada = cantidadPagada;
    }

}
