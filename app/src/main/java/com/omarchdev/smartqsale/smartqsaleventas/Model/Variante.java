package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by OMAR CHH on 17/03/2018.
 */

public class Variante {

    int idVariante;
    String codigoVariante;
    float stockProducto;
    BigDecimal precioCompra;
    BigDecimal precioVenta;
    String NombreVariante;
    String EstadoEliminado;
    String codigoBarra;
    List<AdditionalPriceProduct> lista;
    boolean PVMultiple;
    public Variante() {

        codigoBarra="";
        idVariante=0;
        codigoVariante="";
        stockProducto=0f;
        precioCompra=new BigDecimal(0);
        precioVenta=new BigDecimal(0);
        NombreVariante="";
        EstadoEliminado="";
        PVMultiple=false;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public List<AdditionalPriceProduct> getLista() {
        return lista;
    }

    public void setLista(List<AdditionalPriceProduct> lista) {
        this.lista = lista;
    }

    public boolean isPVMultiple() {
        return PVMultiple;
    }

    public void setPVMultiple(boolean PVMultiple) {
        this.PVMultiple = PVMultiple;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public int getIdVariante() {
        return idVariante;
    }

    public void setIdVariante(int idVariante) {
        this.idVariante = idVariante;
    }

    public String getCodigoVariante() {
        return codigoVariante;
    }

    public void setCodigoVariante(String codigoVariante) {
        this.codigoVariante = codigoVariante;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getNombreVariante() {
        return NombreVariante;
    }

    public void setNombreVariante(String nombreVariante) {
        NombreVariante = nombreVariante;
    }

    public float getStockProducto() {
        return stockProducto;
    }

    public void setStockProducto(float stockProducto) {
        this.stockProducto = stockProducto;
    }

    public String getEstadoEliminado() {
        return EstadoEliminado;
    }

    public void setEstadoEliminado(String estadoEliminado) {
        EstadoEliminado = estadoEliminado;
    }

}

