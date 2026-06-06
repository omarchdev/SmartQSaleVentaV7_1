package com.omarchdev.smartqsale.smartqsaleventas.Controlador

import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta

class cFormateadorImpresion {
    private val constructorFactura = ConstructorFactura()

    fun obtenerListadoProductos(productos: List<ProductoEnVenta>, ancho: Int): String {
        return if (ancho == 2) {
            constructorFactura.generarListadoItems80mm(productos)
        } else {
            constructorFactura.generarListadoItems53mm(productos)
        }
    }

    fun obtenerListadoProductosPedidoPreCuenta(productos: List<ProductoEnVenta>, ancho: Int): String {
        return if (ancho == 2) {
            constructorFactura.generarListadoItems80mm(productos)
        } else {
            constructorFactura.generarListadoItems53mmPedidoPreCuenta(productos)
        }
    }

    fun obtenerCabecerasTicket(ancho: Int): String {
        return if (ancho == 2) {
            completarEspacios(24, "Descripcion") +
                    completarEspaciosI(12, "P.U") +
                    completarEspaciosI(12, "P.T")
        } else {
            completarEspacios(14, "Desc.") +
                    completarEspaciosI(9, "P.U") +
                    completarEspaciosI(9, "P.T")
        }
    }

    fun obtenerCabecerasTicketPedido(ancho: Int): String {
        return if (ancho == 2) {
            completarEspacios(24, "Descripcion") +
                    completarEspaciosI(12, "P.U") +
                    completarEspaciosI(12, "P.T")
        } else {
             completarEspacios(14, "Desc.") +
                    completarEspaciosI(9, "P.U") +
                    completarEspaciosI(9, "P.T")
        }
    }

    fun formatearLineaTotal(etiqueta: String, valor: String, ancho: Int): String {
        return if (ancho == 2) {
            completarEspaciosI(36, etiqueta) + completarEspaciosI(12, valor)
        } else {
            completarEspaciosI(20, etiqueta) + completarEspaciosI(12, valor)
        }
    }
}
