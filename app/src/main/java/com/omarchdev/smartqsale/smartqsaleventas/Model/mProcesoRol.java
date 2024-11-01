package com.omarchdev.smartqsale.smartqsaleventas.Model;

public class mProcesoRol {

    int idProceso;
    String nombreProceso;
    boolean bEstadoAcceso;

    public mProcesoRol() {

        idProceso=0;
        nombreProceso="";
        bEstadoAcceso=false;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public boolean isbEstadoAcceso() {
        return bEstadoAcceso;
    }

    public void setbEstadoAcceso(boolean bEstadoAcceso) {
        this.bEstadoAcceso = bEstadoAcceso;
    }
}
