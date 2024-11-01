package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 05/02/2018.
 */

public class mVentasPorHora {

    private int horaInicio;
    private BigDecimal montoPorHora;


    public mVentasPorHora(int horaInicio, BigDecimal montoPorHora) {
        this.horaInicio = horaInicio;
        this.montoPorHora = montoPorHora;
    }


    public int getHoraInicio() {
        return horaInicio;
    }

    public BigDecimal getMontoPorHora() {
        return montoPorHora;
    }
}
