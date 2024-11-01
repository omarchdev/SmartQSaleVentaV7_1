package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 06/04/2018.
 */

public class ConfigPack {

    int idPack;
    int idItem;
    int numItem;
    int idTipo;
    String cDescripcion;
    String CodigoProducto;
    BigDecimal precio;
    BigDecimal cantidad;
    byte metodoGuardado;
    BigDecimal montoModifica;
    int idTipoModifica;

    public ConfigPack() {
        idPack=0;
        idItem=0;
        numItem=0;
        idTipo=0;
        cDescripcion="";
        CodigoProducto="";
        precio=new BigDecimal(0);
        cantidad=new BigDecimal(0);
        metodoGuardado=0;
        montoModifica=new BigDecimal(0);
        idTipoModifica=0;
    }

    public byte getMetodoGuardado() {
        return metodoGuardado;
    }

    public void setMetodoGuardado(byte metodoGuardado) {
        this.metodoGuardado = metodoGuardado;
    }

    public int getIdPack() {
        return idPack;
    }

    public void setIdPack(int idPack) {
        this.idPack = idPack;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public BigDecimal getMontoModifica() {
        return montoModifica;
    }

    public void setMontoModifica(BigDecimal montoModifica) {
        this.montoModifica = montoModifica;
    }

    public int getIdTipoModifica() {
        return idTipoModifica;
    }

    public void setIdTipoModifica(int idTipoModifica) {
        this.idTipoModifica = idTipoModifica;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoProducto() {
        return CodigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        CodigoProducto = codigoProducto;
    }

    public int getNumItem() {
        return numItem;
    }

    public void setNumItem(int numItem) {
        this.numItem = numItem;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getcDescripcion() {
        return cDescripcion;
    }

    public void setcDescripcion(String cDescripcion) {
        this.cDescripcion = cDescripcion;
    }

}
