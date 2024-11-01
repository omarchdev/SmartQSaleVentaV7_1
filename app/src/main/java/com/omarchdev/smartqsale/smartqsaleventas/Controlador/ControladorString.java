package com.omarchdev.smartqsale.smartqsaleventas.Controlador;

public class ControladorString {


    public String EliminarPrefijo(String prefijo,String cadena){
        String c=cadena.trim();
        while(c.startsWith(prefijo)){
            c=c.substring(1);
        }
        return c;
    }


}
