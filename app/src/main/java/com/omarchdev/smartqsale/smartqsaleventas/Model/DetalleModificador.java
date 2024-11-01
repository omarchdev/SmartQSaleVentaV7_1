package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 24/04/2018.
 */

public class DetalleModificador {

    int idDetalleModificador;
    String DescripcionModificador;
    String codigoModificador;
    int idModificador;
    int factorModificador;
    BigDecimal monto;

    public DetalleModificador() {
        idDetalleModificador=0;
        DescripcionModificador="";
        idModificador=0;
        codigoModificador="";
    }

    public int getIdDetalleModificador() {
        return idDetalleModificador;
    }

    public void setIdDetalleModificador(int idDetalleModificador) {
        this.idDetalleModificador = idDetalleModificador;
    }

    public String getDescripcionModificador() {
        return DescripcionModificador;
    }

    public void setDescripcionModificador(String descripcionModificador) {
        DescripcionModificador = descripcionModificador;
    }

    public int getIdModificador() {
        return idModificador;
    }

    public void setIdModificador(int idModificador) {
        this.idModificador = idModificador;
    }

    public String getCodigoModificador() {
        return codigoModificador;
    }

    public void setCodigoModificador(String codigoModificador) {
        this.codigoModificador = codigoModificador;
    }

    public int getFactorModificador() {
        return factorModificador;
    }

    public void setFactorModificador(int factorModificador) {
        this.factorModificador = factorModificador;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
