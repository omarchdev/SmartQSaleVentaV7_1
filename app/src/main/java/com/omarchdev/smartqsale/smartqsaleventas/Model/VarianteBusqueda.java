package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 26/03/2018.
 */

public class VarianteBusqueda {

    int idCabeceraPedido;
    List<String> VariablesBusqueda;
    float cantidadSeleccionada;
    int idProducto;
    BigDecimal pv;
    boolean multiPv;

    public VarianteBusqueda(){
        idCabeceraPedido=0;
        cantidadSeleccionada=0;
        idProducto=0;
        VariablesBusqueda=new ArrayList<>();
        pv=new BigDecimal(0);
        multiPv=false;
    }

    public BigDecimal getPv() {
        return pv;
    }

    public void setPv(BigDecimal pv) {
        this.pv = pv;
    }

    public boolean isMultiPv() {
        return multiPv;
    }

    public void setMutiPv(boolean mutiPv) {
        this.multiPv = mutiPv;
    }

    public int getIdCabeceraPedido() {
        return idCabeceraPedido;
    }

    public void setIdCabeceraPedido(int idCabeceraPedido) {
        this.idCabeceraPedido = idCabeceraPedido;
    }

    public List<String> getVariablesBusqueda() {
        return VariablesBusqueda;
    }

    public void setVariablesBusqueda(List<String> variablesBusqueda) {
        VariablesBusqueda = variablesBusqueda;
    }

    public float getCantidadSeleccionada() {
        return cantidadSeleccionada;
    }

    public void setCantidadSeleccionada(float cantidadSeleccionada) {
        this.cantidadSeleccionada = cantidadSeleccionada;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
