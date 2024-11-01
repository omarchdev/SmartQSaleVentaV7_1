package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 07/02/2018.
 */

public class mResumenTotalVentas {


    private int num_Ventas;
    private BigDecimal monto_Total;
    private BigDecimal monto_PromedioVentas;

    public mResumenTotalVentas() {
    }

    public mResumenTotalVentas(int num_Ventas, BigDecimal monto_Total, BigDecimal monto_PromedioVentas) {
        this.num_Ventas = num_Ventas;
        this.monto_Total = monto_Total;
        this.monto_PromedioVentas = monto_PromedioVentas;
    }

    public int getNum_Ventas() {
        return num_Ventas;
    }

    public BigDecimal getMonto_Total() {
        return monto_Total;
    }

    public BigDecimal getMonto_PromedioVentas() {
        return monto_PromedioVentas;
    }
}
