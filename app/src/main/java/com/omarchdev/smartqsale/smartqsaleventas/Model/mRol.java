package com.omarchdev.smartqsale.smartqsaleventas.Model;

public class mRol {

    int idRol;
    String cDescripcion;
    boolean bEsAdmistrador;
    public mRol() {
        idRol=0;
        cDescripcion="";
        bEsAdmistrador=false;
    }

    public boolean isbEsAdmistrador() {
        return bEsAdmistrador;
    }

    public void setbEsAdmistrador(boolean bEsAdmistrador) {
        this.bEsAdmistrador = bEsAdmistrador;
    }

    public mRol(int idRol, String cDescripcion) {
        this.idRol = idRol;
        this.cDescripcion = cDescripcion;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getcDescripcion() {
        return cDescripcion;
    }

    public void setcDescripcion(String cDescripcion) {
        this.cDescripcion = cDescripcion;
    }
}
