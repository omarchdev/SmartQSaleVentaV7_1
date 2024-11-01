package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 08/04/2018.
 */

public class PackElemento {
    @SerializedName("idCategoria")
    int idCategoria;
    @SerializedName("descripcion")
    String Descripcion;
    @SerializedName("productList")
    List<mProduct> productList;
    @SerializedName("idDetallePedido")
    int idDetallePedido;
    @SerializedName("idProducto")
    int idProducto;
    @SerializedName("cantidad")
    int Cantidad;
    @SerializedName("precioVenta")
    BigDecimal PrecioVenta;
    @SerializedName("precioVentaFinal")
    BigDecimal PrecioVentaFinal;
    @SerializedName("nombreProducto")
    String nombreProducto;
    @SerializedName("numItem")
    int numItem;
    @SerializedName("listaFaltante")
    List<String> listaFaltante;
    @SerializedName("permitir")
    boolean permitir;
    @SerializedName("precioNeto")
    private BigDecimal precioNeto;
    @SerializedName("montoIgv")
    private BigDecimal montoIgv;
    @SerializedName("descUnidad")
    private String descUnidad;
    @SerializedName("codUnidSunat")
    private String codUnidSunat;
    @SerializedName("valorUnitario")
    private BigDecimal valorUnitario;

    @SerializedName("idCabeceraPedido")
    private int IdCabeceraPedido;


    public int getIdCabeceraPedido() {
        return IdCabeceraPedido;
    }

    public void setIdCabeceraPedido(int idCabeceraPedido) {
        IdCabeceraPedido = idCabeceraPedido;
    }


    public PackElemento() {

        idCategoria=0;
        Descripcion="";
        productList=new ArrayList<>();
        idDetallePedido=0;
        idProducto=0;
        Cantidad=0;
        PrecioVenta=new BigDecimal(0);
        nombreProducto="";
        PrecioVentaFinal=new BigDecimal(0);
        numItem=0;
        precioNeto=new BigDecimal(0);
        montoIgv=new BigDecimal(0);
        descUnidad="";
        codUnidSunat="";
        valorUnitario=new BigDecimal(0);

    }

    public List<String> getListaFaltante() {
        return listaFaltante;
    }

    public void setListaFaltante(List<String> listaFaltante) {
        this.listaFaltante = listaFaltante;
    }

    public boolean isPermitir() {
        return permitir;
    }

    public void setPermitir(boolean permitir) {
        this.permitir = permitir;
    }

    public int getNumItem() {
        return numItem;
    }

    public void setNumItem(int numItem) {
        this.numItem = numItem;
    }

    public BigDecimal getPrecioVentaFinal() {
        return PrecioVentaFinal;
    }

    public void setPrecioVentaFinal(BigDecimal precioVentaFinal) {
        PrecioVentaFinal = precioVentaFinal;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public List<mProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<mProduct> productList) {
        this.productList = productList;
    }

    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public BigDecimal getPrecioVenta() {
        return PrecioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        PrecioVenta = precioVenta;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getPrecioNeto() {
        return precioNeto;
    }

    public void setPrecioNeto(BigDecimal precioNeto) {
        this.precioNeto = precioNeto;
    }

    public BigDecimal getMontoIgv() {
        return montoIgv;
    }

    public void setMontoIgv(BigDecimal montoIgv) {
        this.montoIgv = montoIgv;
    }

    public String getDescUnidad() {
        return descUnidad;
    }

    public void setDescUnidad(String descUnidad) {
        this.descUnidad = descUnidad;
    }

    public String getCodUnidSunat() {
        return codUnidSunat;
    }

    public void setCodUnidSunat(String codUnidSunat) {
        this.codUnidSunat = codUnidSunat;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
