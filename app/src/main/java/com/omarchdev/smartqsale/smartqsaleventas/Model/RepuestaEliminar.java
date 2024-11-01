package com.omarchdev.smartqsale.smartqsaleventas.Model;

public class RepuestaEliminar {

    int cantidad;
    byte respuesta;

    public RepuestaEliminar() {
        cantidad = 0;
        respuesta = 0;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public byte getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(byte respuesta) {
        this.respuesta = respuesta;
    }
}
