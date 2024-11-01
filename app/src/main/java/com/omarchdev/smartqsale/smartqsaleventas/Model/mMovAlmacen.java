package com.omarchdev.smartqsale.smartqsaleventas.Model;

public class mMovAlmacen {

    int idMovAlmacen;
    String fechaMov;
    String fechaGuia;
    String nroGuia;
    String fechaFactura;
    String descripcionMov;
    String almacen;
    String tipoMovAlmacen;
    String cISTipoMov;
    String cEstadoRegistro;
    int idAlmacenI;
    String descAlmacenI;
    int idAlmacenD;
    String descAlmacenD;
    String codTransaccion;
    int idMovOrigenTransf;
    int idTiendaOrigen;
    int idTiendaD;
    public mMovAlmacen() {
        idMovOrigenTransf=0;
        idMovAlmacen = 0;
        fechaMov = "";
        descripcionMov = "";
        almacen = "";
        tipoMovAlmacen = "";
        cEstadoRegistro = "";
        fechaGuia = "";
        nroGuia = "";
        fechaFactura = "";
        idAlmacenI = 0;
        idAlmacenD = 0;
        descAlmacenI = "";
        descAlmacenD = "";
        codTransaccion = "";
        idTiendaOrigen=0;
        idTiendaD=0;
    }

    public mMovAlmacen(int idMovAlmacen, String fechaMov, String descripcionMov, String almacen, String tipoMovAlmacen, String cISTipoMov, String cEstadoRegistro) {
        this.idMovAlmacen = idMovAlmacen;
        this.fechaMov = fechaMov;
        this.descripcionMov = descripcionMov;
        this.almacen = almacen;
        this.tipoMovAlmacen = tipoMovAlmacen;
        this.cISTipoMov = cISTipoMov;
        this.cEstadoRegistro = cEstadoRegistro;
    }

    public int getIdMovAlmacen() {
        return idMovAlmacen;
    }

    public void setIdMovAlmacen(int idMovAlmacen) {
        this.idMovAlmacen = idMovAlmacen;
    }

    public String getFechaMov() {
        return fechaMov;
    }

    public void setFechaMov(String fechaMov) {
        this.fechaMov = fechaMov;
    }

    public String getDescripcionMov() {
        return descripcionMov;
    }

    public void setDescripcionMov(String descripcionMov) {
        this.descripcionMov = descripcionMov;
    }

    public String getAlmacen() {
        return almacen;
    }

    public void setAlmacenOrigen(String almacen) {
        this.almacen = almacen;
    }

    public int getIdTiendaOrigen() {
        return idTiendaOrigen;
    }

    public void setIdTiendaOrigen(int idTiendaOrigen) {
        this.idTiendaOrigen = idTiendaOrigen;
    }

    public int getIdTiendaD() {
        return idTiendaD;
    }

    public void setIdTiendaD(int idTiendaD) {
        this.idTiendaD = idTiendaD;
    }

    public String getTipoMovAlmacen() {
        return tipoMovAlmacen;
    }

    public void setTipoMovAlmacen(String tipoMovAlmacen) {
        this.tipoMovAlmacen = tipoMovAlmacen;
    }

    public String getcISTipoMov() {
        return cISTipoMov;
    }

    public void setcISTipoMov(String cISTipoMov) {
        this.cISTipoMov = cISTipoMov;
    }

    public String getcEstadoRegistro() {
        return cEstadoRegistro;
    }

    public void setcEstadoRegistro(String cEstadoRegistro) {
        this.cEstadoRegistro = cEstadoRegistro;
    }

    public int getIdMovOrigenTransf() {
        return idMovOrigenTransf;
    }

    public void setIdMovOrigenTransf(int idMovOrigenTransf) {
        this.idMovOrigenTransf = idMovOrigenTransf;
    }

    public String getFechaGuia() {
        return fechaGuia;
    }

    public void setFechaGuia(String fechaGuia) {
        this.fechaGuia = fechaGuia;
    }

    public String getNroGuia() {
        return nroGuia;
    }

    public void setNroGuia(String nroGuia) {
        this.nroGuia = nroGuia;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public void setAlmacen(String almacen) {
        this.almacen = almacen;
    }

    public int getIdAlmacenI() {
        return idAlmacenI;
    }

    public void setIdAlmacenI(int idAlmacenI) {
        this.idAlmacenI = idAlmacenI;
    }

    public String getDescAlmacenI() {
        return descAlmacenI;
    }

    public void setDescAlmacenI(String descAlmacenI) {
        this.descAlmacenI = descAlmacenI;
    }

    public int getIdAlmacenD() {
        return idAlmacenD;
    }

    public void setIdAlmacenD(int idAlmacenD) {
        this.idAlmacenD = idAlmacenD;
    }

    public String getDescAlmacenD() {
        return descAlmacenD;
    }

    public void setDescAlmacenD(String descAlmacenD) {
        this.descAlmacenD = descAlmacenD;
    }

    public String getCodTransaccion() {
        return codTransaccion;
    }

    public void setCodTransaccion(String codTransaccion) {
        this.codTransaccion = codTransaccion;
    }
}

