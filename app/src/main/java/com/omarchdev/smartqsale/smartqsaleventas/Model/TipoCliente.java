package com.omarchdev.smartqsale.smartqsaleventas.Model;

public enum TipoCliente {

    PersonaNatural("Persona natural",1),PersonaJuridica("Persona jurídica",2);

    private String DescripcionPersona;
    private int idTipo;

    private TipoCliente(String descripcionPersona,int idTipo){
        this.DescripcionPersona=descripcionPersona;
        this.idTipo=idTipo;
    }

    public String getDescripcionPersona() {
        return DescripcionPersona;
    }

    public int getIdTipo() {
        return idTipo;
    }
}
