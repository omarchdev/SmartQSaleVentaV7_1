package com.omarchdev.smartqsale.smartqsaleventas;

/**
 * Created by OMAR CHH on 06/04/2018.
 */

 public enum TipoProducto{
    Normal("Normal",0),
    Pack("Pack/Combo",1);

    private String Descripcion;
    private int Num;

    private TipoProducto(String nombre,int id){
        this.Descripcion=nombre;
        this.Num=id;
    }

    public String getDescripcion(){
        return Descripcion;
    }

    public int getNum(){
        return Num;
    }

    @Override
    public String toString() {
        return Descripcion;
    }
}

