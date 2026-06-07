package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OMAR CHH on 28/01/2018.
 */

public class mCierre {

    @SerializedName("idCierre")
    private int idCierre;
    @SerializedName("fechaAperturaEX")
    private Timestamp fechaApertura;
    @SerializedName("fechaCierreEx")
    private Timestamp fechaCierre;
    @SerializedName("estadoCierre")
    private String estadoCierre;
    @SerializedName("cFechaApertura")
    private String cFechaApertura;
    @SerializedName("cFechaCierre")
    private String cFechaCierre;
    @SerializedName("cPeriodo")
    private String cPeriodo;
    @SerializedName("totalVentas")
    private BigDecimal totalVentas;
    @SerializedName("descripcionEstado")
    private String descripcionEstado;
    @SerializedName("numTransacciones")
    private int numTransacciones;
    @SerializedName("idUsuario")
    private int idUsuario;
    private String nombreUsuario;
/*    @SerializedName("fechaApertura")
    private String fechaAperturaTemp;
    @SerializedName("fechaCierre")
    private String fechaCierreTemp;
*/

    @SerializedName("fechaApertura")
    private String fechaAperturaTemp;
    @SerializedName("fechaCierre")
    private String fechaCierreTemp;
    public void ConvierteFechaJSON(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = sdf.parse(fechaAperturaTemp.replace("T"," "));
            fechaApertura.setTime(date1.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Date date2 = sdf.parse(fechaCierreTemp.replace("T"," "));
            fechaCierre.setTime(date2.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public mCierre() {

        idCierre = 0;
        estadoCierre = "";
        java.util.Date utilDate = new Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        cFechaApertura="";
        cFechaCierre="";
        cPeriodo="";
        totalVentas=new BigDecimal(0);
        descripcionEstado="";
        fechaApertura = new Timestamp(lnMilisegundos);
        fechaCierre = new Timestamp(lnMilisegundos);
        numTransacciones=0;
        idUsuario = 0;
        nombreUsuario = "";
    }

    public int getNumTransacciones() {
        return numTransacciones;
    }

    public void setNumTransacciones(int numTransacciones) {
        this.numTransacciones = numTransacciones;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdCierre() {
        return idCierre;
    }

    public void setIdCierre(int idCierre) {
        this.idCierre = idCierre;
    }

    public Timestamp getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Timestamp fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Timestamp getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Timestamp fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getEstadoCierre() {
        return estadoCierre;
    }

    public void setEstadoCierre(String estadoCierre) {
        this.estadoCierre = estadoCierre;
    }

    public String getcFechaApertura() {
        return cFechaApertura;
    }

    public void setcFechaApertura(String cFechaApertura) {
        this.cFechaApertura = cFechaApertura;
    }

    public String getcFechaCierre() {
        return cFechaCierre;
    }

    public void setcFechaCierre(String cFechaCierre) {
        this.cFechaCierre = cFechaCierre;
    }

    public String getcPeriodo() {
        return cPeriodo;
    }

    public void setcPeriodo(String cPeriodo) {
        this.cPeriodo = cPeriodo;
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public String getFechaCierreTemp() {
        return fechaCierreTemp;
    }

    public void setFechaCierreTemp(String fechaCierreTemp) {
        this.fechaCierreTemp = fechaCierreTemp;
    }

    public String getFechaAperturaTemp() {
        return fechaAperturaTemp;
    }

    public void setFechaAperturaTemp(String fechaAperturaTemp) {
        this.fechaAperturaTemp = fechaAperturaTemp;
    }
}
