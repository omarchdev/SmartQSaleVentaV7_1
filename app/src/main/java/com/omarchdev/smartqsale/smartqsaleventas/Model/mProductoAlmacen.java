package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;

public class mProductoAlmacen {

    int idProducto;
    String codigoBarra;
    String nombreProducto;
    String descripcionVariante;
    BigDecimal precioCompra;
    BigDecimal precioVenta;
    float cantidadDisponible;
    boolean esVariante;
    int idAlmacen;
    String descripcionAlmacen;
    int idProductoAlmacen;
    boolean esTienda;


    public mProductoAlmacen() {
    }

    public mProductoAlmacen(int idProducto, String codigoBarra, String nombreProducto
            , BigDecimal precioCompra, float cantidadDisponible, boolean esVariante,
                            int idAlmacen, String descripcionAlmacen, int idProductoAlmacen,boolean esTienda) {
        this.idProducto = idProducto;
        this.codigoBarra = codigoBarra;
        this.nombreProducto = nombreProducto;
        this.precioCompra = precioCompra;
        this.cantidadDisponible = cantidadDisponible;
        this.esVariante = esVariante;
        this.idAlmacen = idAlmacen;
        this.descripcionAlmacen = descripcionAlmacen;
        this.idProductoAlmacen = idProductoAlmacen;
        this.esTienda=esTienda;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public float getCantidadDisponible() {
        return cantidadDisponible;
    }

    public boolean isEsVariante() {
        return esVariante;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public boolean isEsTienda() {
        return esTienda;
    }

    public void setEsTienda(boolean esTienda) {
        this.esTienda = esTienda;
    }

    public String getDescripcionAlmacen() {
        return descripcionAlmacen;
    }

    public int getIdProductoAlmacen() {
        return idProductoAlmacen;
    }

    public BigDecimal getTotalCompra(){
        return precioCompra.multiply(new BigDecimal(cantidadDisponible));
    }
    public BigDecimal getTotalVenta(){
        return  precioVenta.multiply(new BigDecimal(cantidadDisponible));
    }
}
