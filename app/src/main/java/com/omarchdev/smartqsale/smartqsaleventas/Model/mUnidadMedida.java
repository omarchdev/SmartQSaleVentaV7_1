package com.omarchdev.smartqsale.smartqsaleventas.Model;

public class mUnidadMedida {

    int idUnidad;
    String cDescripcion;
    boolean edicionHabilitada;
    String cDescripcionLarga;


    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public mUnidadMedida() {

        idUnidad=0;
        cDescripcion="";
        cDescripcionLarga="";
        edicionHabilitada=false;
    }

    public String getcDescripcion() {
        return cDescripcion;
    }

    public void setcDescripcion(String cDescripcion) {
        this.cDescripcion = cDescripcion;
    }

    public boolean isEdicionHabilitada() {
        return edicionHabilitada;
    }

    public void setEdicionHabilitada(boolean edicionHabilitada) {
        this.edicionHabilitada = edicionHabilitada;
    }

    public String getcDescripcionLarga() {
        return cDescripcionLarga;
    }

    public void setcDescripcionLarga(String cDescripcionLarga) {
        this.cDescripcionLarga = cDescripcionLarga;
    }
}
