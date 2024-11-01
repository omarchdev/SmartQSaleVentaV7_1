package com.omarchdev.smartqsale.smartqsaleventas.Model;

public class mTransacciones_Almacen {


    int idTransaccion;
    String cDescTrans;
    String cCodTrans;

    public mTransacciones_Almacen(int idTransaccion,String cDescTrans, String cCodTrans) {

        this.idTransaccion=idTransaccion;
        this.cDescTrans = cDescTrans;
        this.cCodTrans = cCodTrans;
    }

    public mTransacciones_Almacen() {

        cDescTrans="";
        cDescTrans="";

    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getcDescTrans() {
        return cDescTrans;
    }

    public void setcDescTrans(String cDescTrans) {
        this.cDescTrans = cDescTrans;
    }

    public String getcCodTrans() {
        return cCodTrans;
    }

    public void setcCodTrans(String cCodTrans) {
        this.cCodTrans = cCodTrans;
    }
}
