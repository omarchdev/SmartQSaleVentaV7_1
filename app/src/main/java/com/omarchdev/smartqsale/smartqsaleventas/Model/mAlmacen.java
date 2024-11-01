package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

public class mAlmacen {

    @SerializedName("id_almacen")
    int idAlmacen;
    @SerializedName("descripcion_almacen")
    String descripcionAlmacen;
    @SerializedName("id_tienda")
    int idTienda;
    //Almacen se encuentra en tienda
    @SerializedName("es_tienda")
    boolean Tienda;
    int idProducto;

    float cantidad;
    public mAlmacen() {
        idAlmacen=0;
        descripcionAlmacen="";
        Tienda=false;
        idProducto=0;
        cantidad=0f;
        idTienda=0;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public mAlmacen(int idAlmacen, String descripcionAlmacen, boolean tienda, float cantidad,int idTienda) {
        this.idAlmacen = idAlmacen;
        this.descripcionAlmacen = descripcionAlmacen;
        Tienda = tienda;
       this.cantidad=cantidad;
       this.idTienda=idTienda;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public String getDescripcionAlmacen() {
        return descripcionAlmacen;
    }

    public void setDescripcionAlmacen(String descripcionAlmacen) {
        this.descripcionAlmacen = descripcionAlmacen;
    }

    public boolean isTienda() {
        return Tienda;
    }

    public void setTienda(boolean tienda) {
        Tienda = tienda;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
