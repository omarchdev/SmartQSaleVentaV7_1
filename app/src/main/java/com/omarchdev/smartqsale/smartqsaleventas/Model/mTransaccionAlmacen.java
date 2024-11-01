package com.omarchdev.smartqsale.smartqsaleventas.Model;

public class mTransaccionAlmacen {

    String nombre;
    String nombreCorto;
    String codigo;
    String descripcion;

    int id;


    public mTransaccionAlmacen(int id, String nombre, String nombreCorto, String codigo,String descripcion) {
        this.nombre = nombre;
        this.nombreCorto = nombreCorto;
        this.codigo = codigo;
        this.descripcion=descripcion;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
