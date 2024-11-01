package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by OMAR CHH on 10/01/2018.
 */

public class DetalleCuentaCorriente {


    int idCtaCteCliente;
    int idTransaccion;
    int idCliente;
    int idUsuario;
    int idTerminal;
    int idCabeceraVenta;
    String codTipoTransaccion;
    String metodoPago;
    String cObservacion;
    String TipoTransaccion;
    Timestamp fechaOrigen;
    Timestamp fechaEliminacion;
    String estadoEliminacion;
    String estadoCtaCte;
    BigDecimal Monto;


    public DetalleCuentaCorriente() {

        idCtaCteCliente = 0;
        idTransaccion = 0;
        idCliente = 0;
        idUsuario = 0;
        idTerminal = 0;
        idCabeceraVenta = 0;
        codTipoTransaccion = "";
        TipoTransaccion = "";
        java.util.Date utilDate = new Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        fechaOrigen = new Timestamp(lnMilisegundos);
        fechaEliminacion = new Timestamp(lnMilisegundos);
        estadoEliminacion = "";
        estadoCtaCte = "";
        cObservacion = "";
        Monto = new BigDecimal(0);
        metodoPago = "";

    }


    public int getIdCtaCteCliente() {
        return idCtaCteCliente;
    }

    public void setIdCtaCteCliente(int idCtaCteCliente) {
        this.idCtaCteCliente = idCtaCteCliente;
    }


    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal(int idTerminal) {
        this.idTerminal = idTerminal;
    }

    public String getTipoTransaccion() {
        return TipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        TipoTransaccion = tipoTransaccion;
    }

    public Timestamp getFechaOrigen() {
        return fechaOrigen;
    }

    public void setFechaOrigen(Timestamp fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    public Timestamp getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(Timestamp fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public String getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(String estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public String getEstadoCtaCte() {
        return estadoCtaCte;
    }

    public void setEstadoCtaCte(String estadoCtaCte) {
        this.estadoCtaCte = estadoCtaCte;
    }

    public BigDecimal getMonto() {
        return Monto;
    }

    public void setMonto(BigDecimal monto) {
        Monto = monto;
    }

    public int getIdCabeceraVenta() {
        return idCabeceraVenta;
    }

    public void setIdCabeceraVenta(int idCabeceraVenta) {
        this.idCabeceraVenta = idCabeceraVenta;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getIdTipoTransaccion() {
        return codTipoTransaccion;
    }

    public void setIdTipoTransaccion(String codTipoTransaccion) {
        this.codTipoTransaccion = codTipoTransaccion;
    }

    public String getCodTipoTransaccion() {
        return codTipoTransaccion;
    }

    public void setCodTipoTransaccion(String codTipoTransaccion) {
        this.codTipoTransaccion = codTipoTransaccion;
    }

    public String getcObservacion() {
        return cObservacion;
    }

    public void setcObservacion(String cObservacion) {
        this.cObservacion = cObservacion;
    }


}
