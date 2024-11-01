package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 20/04/2018.
 */

public class Modificador {

    int idModificadorProducto;
    int idModificador;
    String codigo;
    String descripcion;
    int numItem;
    List<DetalleModificador> detalleModificadorList;

    public Modificador() {
        idModificadorProducto=0;
        idModificador=0;
        codigo="";
        descripcion="";
        numItem=0;
        detalleModificadorList=new ArrayList<>();
    }

    public int getNumItem() {
        return numItem;
    }

    public void setNumItem(int numItem) {
        this.numItem = numItem;
    }

    public int getIdModificadorProducto() {
        return idModificadorProducto;
    }

    public void setIdModificadorProducto(int idModificadorProducto) {
        this.idModificadorProducto = idModificadorProducto;
    }

    public int getIdModificador() {
        return idModificador;
    }

    public void setIdModificador(int idModificador) {
        this.idModificador = idModificador;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<DetalleModificador> getDetalleModificadorList() {
        return detalleModificadorList;
    }

    public void setDetalleModificadorList(List<DetalleModificador> detalleModificadorList) {
        this.detalleModificadorList = detalleModificadorList;
    }
}
