package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OMAR CHH on 05/03/2018.
 */

public class ValorOpcionVariante {

    @SerializedName("id_valor")
   int idValor;
    @SerializedName("descripcion")
   String Descripcion;
    @SerializedName("iNum_item")
   int iNumItem;
    @SerializedName("numItemPadre")
   int NumItemPadre;
    @SerializedName("id_opcion")
   int idOpcionVariante;
    @SerializedName("idProducto")
    int idProducto;

    public ValorOpcionVariante() {
        idValor=0;
        Descripcion="";
        iNumItem=0;
        NumItemPadre=0;
        idOpcionVariante=0;
        idProducto=0;
    }

    public int getIdValor() {
        return idValor;
    }

    public void setIdValor(int idValor) {
        this.idValor = idValor;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getiNumItem() {
        return iNumItem;
    }

    public void setiNumItem(int iNumItem) {
        this.iNumItem = iNumItem;
    }

    public int getNumItemPadre() {
        return NumItemPadre;
    }

    public void setNumItemPadre(int numItemPadre) {
        NumItemPadre = numItemPadre;
    }

    public int getIdOpcionVariante() {
        return idOpcionVariante;
    }

    public void setIdOpcionVariante(int idOpcionVariante) {
        this.idOpcionVariante = idOpcionVariante;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
}
