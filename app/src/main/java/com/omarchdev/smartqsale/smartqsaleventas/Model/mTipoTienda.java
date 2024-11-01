package com.omarchdev.smartqsale.smartqsaleventas.Model;

/**
 * Created by OMAR CHH on 09/05/2018.
 */

public class mTipoTienda {

    int idTipoTienda;
    String descripcionTienda;

    public mTipoTienda() {
        idTipoTienda=0;
        descripcionTienda="";
    }

    public int getIdTipoTienda() {
        return idTipoTienda;
    }

    public void setIdTipoTienda(int idTipoTienda) {
        this.idTipoTienda = idTipoTienda;
    }

    public String getDescripcionTienda() {
        return descripcionTienda;
    }

    public void setDescripcionTienda(String descripcionTienda) {
        this.descripcionTienda = descripcionTienda;
    }
}
