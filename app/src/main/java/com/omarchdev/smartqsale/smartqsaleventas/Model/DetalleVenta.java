package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DetalleVenta {

    int itemPosition;
    List<ProductoEnVenta> productoEnVentaList;
    int longitud;
    float cantidad;
    float cantidadProducto;
    BigDecimal nuevoSubtotal;

    BigDecimal totalDescuento;

    public DetalleVenta() {
        productoEnVentaList = new ArrayList<>();
        longitud = 0;
        cantidad = 0;
        cantidadProducto = 0;
        nuevoSubtotal = new BigDecimal(0);
        totalDescuento = new BigDecimal(0);
    }

    public void EliminarProducto(int position) {
        productoEnVentaList.remove(position);
        longitud--;
    }

    public List<ProductoEnVenta> getProductoEnVentaList() {
        return productoEnVentaList;
    }

    public void setProductoEnVentaList(List<ProductoEnVenta> list) {
       // productoEnVentaList.clear();
        productoEnVentaList.addAll(list);
        longitud = list.size();
    }


    public boolean ExisteProductoUnico(){

        boolean existe=false;
        for (int i=0;i<productoEnVentaList.size();i++){
            if(productoEnVentaList.get(i).isProductoUnico()){
                existe=true;
            }

        }
       return  existe;
    }

    public ProductoEnVenta GetProductoUnico(){
        ProductoEnVenta result=new ProductoEnVenta();

        for (int i=0;i<productoEnVentaList.size();i++){
            if(productoEnVentaList.get(i).isProductoUnico()){
                result=productoEnVentaList.get(i);
            }

        }
        return  result;
    }

    public int getLongitud() {
        return longitud;
    }

    public float cantidadTotalProductos() {
        cantidad = 0;
        longitud = productoEnVentaList.size();
        if (longitud > 0) {
            for (int i = 0; i < longitud; i++) {
                cantidad += productoEnVentaList.get(i).getCantidad();
            }
        }
        return cantidad;
    }

    public BigDecimal TotalVentaLinea(ProductoEnVenta productoEnVenta) {

        return (productoEnVenta.getPrecioOriginal().multiply(new BigDecimal(cantidad))).subtract(productoEnVenta.getMontoDescuento());
    }

    public String TotalDescuentoText() {
        return Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", TotalDescuento());
    }


    public BigDecimal TotalDescuento() {
        totalDescuento = new BigDecimal(0);
        for (ProductoEnVenta productoEnVenta : productoEnVentaList) {
            totalDescuento = totalDescuento.add(productoEnVenta.getMontoDescuento());
        }
        return totalDescuento;
    }

    public BigDecimal montoSubTotalLinea(ProductoEnVenta productoEnVenta) {
        return (productoEnVenta.getPrecioOriginal().multiply(new BigDecimal(cantidad)));
    }

    public ProductoEnVenta getProductoEnPosicion(int position) {
        return productoEnVentaList.get(position);
    }

    public void aumentarCantidadPorUnidadEnDetalleVentaPorUnidad() {
        //
        int ultimaPosicion = longitud - 1;
        cantidad = Math.round(productoEnVentaList.get(ultimaPosicion).getCantidad() + 1);
        productoEnVentaList.get(ultimaPosicion).setCantidad(cantidad);
    }

    public void ModificarSubtotalVentaPorUnidad() {

        //
        int ultimaPosicion = longitud - 1;
        nuevoSubtotal = productoEnVentaList.get(ultimaPosicion).precioOriginal.multiply(BigDecimal.valueOf(productoEnVentaList.get(ultimaPosicion).getCantidad()));
        productoEnVentaList.get(ultimaPosicion).setPrecioVentaFinal(nuevoSubtotal);


    }


    public ProductoEnVenta getObtenerUltimoProducto() {
        int ultimaPosicion = productoEnVentaList.size() - 1;
        return productoEnVentaList.get(ultimaPosicion);
    }

    public String getTotalMontoPedido() {



        return DecimalControlKt.montoDecimalPrecioSimbolo(getObtenerUltimoProducto().getPrecioVentaFinal().subtract(getObtenerUltimoProducto().getMontoDescuento()));

    }

    public String getTextoUltimoItemPedido() {
        if (productoEnVentaList.size() == 0) {
            return "Sin productos";
        }

        String titulo = "";

        if (getUltimoProductoIngresado().isEsPack()) {
            titulo = getObtenerUltimoProducto().getProductName() + " " +
                    DecimalControlKt.montoDecimalTexto(new BigDecimal(getObtenerUltimoProducto().getCantidad()));
            for (int i = 0; i < getObtenerUltimoProducto().getProductoEnVentaList().size(); i++) {

                titulo = titulo + getUltimoProductoIngresado().getProductoEnVentaList().get(i).getProductName() + "/";

            }

        } else if (getUltimoProductoIngresado().isEsModificado()) {
            titulo = getObtenerUltimoProducto().getProductName()
                    + "\n" + getUltimoProductoIngresado().getDescripcionModificador()
                    + " x" + String.format("%.2f", getObtenerUltimoProducto().getCantidad());
        } else {
            if (getUltimoProductoIngresado().isEsVariante()) {
                titulo = getObtenerUltimoProducto().getProductName()
                        + "\n" + getUltimoProductoIngresado().getDescripcionVariante() + " x"
                        + String.format("%.2f", getObtenerUltimoProducto().getCantidad());

            } else {
                titulo = getObtenerUltimoProducto().getProductName()
                        + " x" + String.format("%.2f", getObtenerUltimoProducto().getCantidad());
            }
        }

        return titulo;
    }

    public int RetornarCantidadTotalProductosEnVenta() {
        cantidad = 0;
        int c = 0;
        if (longitud >= 0) {
            for (int i = 0; i < longitud; i++) {
                c += Math.round(productoEnVentaList.get(i).cantidad);

            }

        }

        return c;
    }

    public String ObtenerNombreUltimoProducto() {
        if (longitud > 0) {
            return productoEnVentaList.get(longitud - 1).getProductName();
        }
        return "Sin productos";
    }

    public BigDecimal ObtenerPrecioUltimoProducto() {
        if (longitud > 0) {
            return productoEnVentaList.get(longitud - 1).getPrecioOriginal();
        }
        return new BigDecimal(0);
    }

    public ProductoEnVenta getUltimoProductoIngresado() {

        return getProductoEnVentaList().get(longitud - 1);
    }

    public BigDecimal TotalCobrar() {
        BigDecimal Total = new BigDecimal(0);

        for (ProductoEnVenta productoEnVenta : productoEnVentaList) {
            Total = Total.add(productoEnVenta.getPrecioVentaFinal()
                    .subtract(productoEnVenta.getMontoDescuento()));
        }


        return Total;
    }

    public boolean PermitirGuardarEnUltimo() {

        boolean p = false;
        if (productoEnVentaList.isEmpty()) {
            p = true;

        } else {
            if (productoEnVentaList.get(productoEnVentaList.size() - 1).getEstadoGuardado().equals("")) {
                p = true;
            } else {
                p = false;
            }
        }


        return p;
    }

    public BigDecimal getTiempoTranscurrido(){

        BigDecimal horas=new BigDecimal(0);
        for(int i=0;i<productoEnVentaList.size();i++){

            if(productoEnVentaList.get(i).isControlTiempo()){
                horas=horas.add(new BigDecimal(productoEnVentaList.get(i).getCantidad()));
            }

        }

        return horas;
    }

    public int getEtapasContabilizadas(){

        int etapas=0;

        for(int i=0;i<productoEnVentaList.size();i++){

            if(productoEnVentaList.get(i).isControlTiempo()){
                etapas++;
            }

        }

        return etapas;
    }


    public boolean VerficarProductosControlTiempo() {

        boolean a = true;
        for (ProductoEnVenta p : productoEnVentaList) {

            if (p.isControlTiempo()) {
                if (p.getHoraFinal().equals("NN")) {

                    a = false;
                }
            }

        }

        return a;
    }
}


