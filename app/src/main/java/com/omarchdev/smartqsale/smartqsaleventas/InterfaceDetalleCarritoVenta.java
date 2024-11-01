package com.omarchdev.smartqsale.smartqsaleventas;

/**
 * Created by OMAR CHH on 27/11/2017.
 */

public interface InterfaceDetalleCarritoVenta {

    public void CantidadProductosEnCarrito(int cantidad);
    public void InformacionUltimoProducto(String nombre,String precio);
    public void PasarInformacionProductoaDetalleVenta(int id);

}
