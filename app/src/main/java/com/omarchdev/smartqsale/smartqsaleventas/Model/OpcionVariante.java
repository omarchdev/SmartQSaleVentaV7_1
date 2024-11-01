package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 02/03/2018.
 */

public class OpcionVariante  {

    int idOpcionVariante;
    int idProduct;
    String Descripcion;
    int iNumIntem;
    String Valores;
    List<ValorOpcionVariante> listValores;

    public OpcionVariante(String Descripcion){

        this.Descripcion=Descripcion;
    }


    public String Contenido(){

        String d="Debe txtTituloes a la opcion";
        int longitud=0;
        if(listValores!=null) {
            if (listValores.size() > 0) {
                d="";
                for (ValorOpcionVariante listValore : listValores) {

                    d=d+listValore.getDescripcion()+",";
                }
                longitud=d.length();
                if(longitud>0) {
                    d = d.substring(0, longitud - 1);
                }
            }
        }

        return d;
    }
    public OpcionVariante() {
        idOpcionVariante=0;
        idProduct=0;
        Descripcion="";
        iNumIntem=0;
        Valores="";
        listValores=new ArrayList<>();

    }

    public int getIdOpcionVariante() {
        return idOpcionVariante;
    }

    public void setIdOpcionVariante(int idOpcionVariante) {
        this.idOpcionVariante = idOpcionVariante;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getiNumIntem() {
        return iNumIntem;
    }

    public void setiNumIntem(int iNumIntem) {
        this.iNumIntem = iNumIntem;
    }

    public String getValores() {
        return Valores;
    }

    public void setValores(String valores) {
        Valores = valores;
    }

    public List<ValorOpcionVariante> getListValores() {
        return listValores;
    }


    public void setListValores(List<ValorOpcionVariante> listValores) {
        this.listValores.clear();
        this.listValores.addAll(listValores);
        changeNumPadre();

    }
    private void changeNumPadre(){

        for(int i=0;i<listValores.size();i++){

            listValores.get(i).setNumItemPadre(iNumIntem);
        }

    }
}
