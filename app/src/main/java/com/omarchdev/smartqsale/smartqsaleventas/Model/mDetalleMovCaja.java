package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OMAR CHH on 02/02/2018.
 */

public class mDetalleMovCaja {

    private byte tipoRegistro;
    private String nombreMedioPago;
    private String descripcionMotivo;
    private String descripcion;
    private BigDecimal monto;
    private Timestamp fechaTransaccion;
    private String fechaTransaccionT;
    private int cantidadMov;
    private String cFechaTransaccion;
    private mCierre cierre;

    public void ConvierteFechaJSON(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = sdf.parse(fechaTransaccionT.replace("T"," "));
            fechaTransaccion.setTime(date1.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public mDetalleMovCaja() {

        tipoRegistro = 0;
        nombreMedioPago = "";
        descripcionMotivo = "";
        descripcion = "";
        monto = new BigDecimal(0);
        java.util.Date utilDate = new Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        fechaTransaccion = new java.sql.Timestamp(lnMilisegundos);
        cantidadMov=0;
        cFechaTransaccion="";
        cierre=new mCierre();
    }

    public Timestamp getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Timestamp fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getNombreMedioPago() {
        return nombreMedioPago;
    }

    public void setNombreMedioPago(String nombreMedioPago) {
        this.nombreMedioPago = nombreMedioPago;
    }

    public int getCantidadMov() {
        return cantidadMov;
    }

    public void setCantidadMov(int cantidadMov) {
        this.cantidadMov = cantidadMov;
    }

    public String getDescripcionMotivo() {
        return descripcionMotivo;
    }

    public void setDescripcionMotivo(String descripcionMotivo) {
        this.descripcionMotivo = descripcionMotivo;
    }

    public String getcFechaTransaccion() {
        return cFechaTransaccion;
    }

    public void setcFechaTransaccion(String cFechaTransaccion) {
        this.cFechaTransaccion = cFechaTransaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public byte getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(byte tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public mCierre getCierre() {
        return cierre;
    }

    public void setCierre(mCierre cierre) {
        this.cierre = cierre;
    }
}

