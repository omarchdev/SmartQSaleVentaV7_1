package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OMAR CHH on 05/05/2018.
 */

public class mTipo_Pago {

    @SerializedName("idTipoPago")
    int idTipoPago;
    @SerializedName("cDescripcion")
    String cDescripcion;

    public mTipo_Pago() {

        idTipoPago=0;
        cDescripcion="";
    }

    public int getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public String getcDescripcion() {
        return cDescripcion;
    }

    public void setcDescripcion(String cDescripcion) {
        this.cDescripcion = cDescripcion;
    }
}
