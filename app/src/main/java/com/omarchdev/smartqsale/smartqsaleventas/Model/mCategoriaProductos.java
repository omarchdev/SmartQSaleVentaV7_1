package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OMAR CHH on 20/01/2018.
 */

public class mCategoriaProductos {
    @SerializedName("id_categoria")
    int idCategoria;
    @SerializedName("descripcion_categoria")
    String descripcionCategoria;
    String EstadoCategoria;
    int idCompany;
    @SerializedName("estado_mod")
    boolean estadoMod;
    @SerializedName("tipo_categoria")
    int tipoCatgoria;

    public mCategoriaProductos() {
        idCategoria=0;
        descripcionCategoria="";
        EstadoCategoria="";
        idCompany=0;
        estadoMod=false;
    }




    public int getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }

    public mCategoriaProductos(int idCategoria, String descripcionCategoria) {
        this.idCategoria = idCategoria;
        this.descripcionCategoria = descripcionCategoria;
    }

    public mCategoriaProductos(int idCategoria, String descripcionCategoria, String estadoCategoria) {
        this.idCategoria = idCategoria;
        this.descripcionCategoria = descripcionCategoria;
        EstadoCategoria = estadoCategoria;
    }


    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public String getEstadoCategoria() {
        return EstadoCategoria;
    }

    public void setEstadoCategoria(String estadoCategoria) {
        EstadoCategoria = estadoCategoria;
    }

    public boolean isEstadoMod() {
        return estadoMod;
    }

    public void setEstadoMod(boolean estadoMod) {
        this.estadoMod = estadoMod;
    }
}
