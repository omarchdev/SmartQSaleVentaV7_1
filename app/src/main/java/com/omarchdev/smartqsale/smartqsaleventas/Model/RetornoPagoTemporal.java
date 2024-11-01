package com.omarchdev.smartqsale.smartqsaleventas.Model;


public class RetornoPagoTemporal{

    byte respuesta;
    boolean esEfectivo;

    public RetornoPagoTemporal() {
        respuesta = 0;
        esEfectivo = false;
    }

    public byte getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(byte respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isEsEfectivo() {
        return esEfectivo;
    }

    public void setEsEfectivo(boolean esEfectivo) {
        this.esEfectivo = esEfectivo;
    }
}
