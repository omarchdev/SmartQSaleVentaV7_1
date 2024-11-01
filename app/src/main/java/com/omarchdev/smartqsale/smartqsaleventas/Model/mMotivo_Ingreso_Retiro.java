package com.omarchdev.smartqsale.smartqsaleventas.Model;

/**
 * Created by OMAR CHH on 30/01/2018.
 */

public class mMotivo_Ingreso_Retiro {

    int idMotivo;
    String descripcionMotivo;
    int tipoMotivo;

    public mMotivo_Ingreso_Retiro() {
        idMotivo = 0;
        descripcionMotivo = "";
        tipoMotivo=0;
    }

    public mMotivo_Ingreso_Retiro(int idMotivo, String descripcionMotivo) {
        this.idMotivo = idMotivo;
        this.descripcionMotivo = descripcionMotivo;
    }

    public int getTipoMotivo() {
        return tipoMotivo;
    }

    public void setTipoMotivo(int tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
    }

    public int getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(int idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getDescripcionMotivo() {
        return descripcionMotivo;
    }

    public void setDescripcionMotivo(String descripcionMotivo) {
        this.descripcionMotivo = descripcionMotivo;
    }
}
