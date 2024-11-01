package com.omarchdev.smartqsale.smartqsaleventas.Model;

/**
 * Created by OMAR CHH on 09/05/2018.
 */

public class mUsuario {

    int idUsuario;
    String nombreUsuario;
    int idTienda;
    String nombreTienda;
    String email;
    String contrasena;
    String telefono;
    int idSegmento;
    mRol mRol;
    String cEstadoUsuario;
    String cNumRuc;
    String cDireccion;

    public mUsuario() {
        idUsuario=0;
        nombreUsuario="";
        idTienda=0;
        nombreTienda="";
        email="";
        contrasena ="";
        telefono="";
        idSegmento=0;
        mRol =new mRol();
        cEstadoUsuario="";
        cNumRuc="";
        cDireccion="";

    }

    public String getcNumRuc() {
        return cNumRuc;
    }

    public void setcNumRuc(String cNumRuc) {
        this.cNumRuc = cNumRuc;
    }

    public String getcDireccion() {
        return cDireccion;
    }

    public void setcDireccion(String cDireccion) {
        this.cDireccion = cDireccion;
    }

    public mRol getmRol() {
        return mRol;
    }

    public void setmRol(mRol mRol) {
        this.mRol = mRol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public String getcEstadoUsuario() {
        return cEstadoUsuario;
    }

    public void setcEstadoUsuario(String cEstadoUsuario) {
        this.cEstadoUsuario = cEstadoUsuario;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdSegmento() {
        return idSegmento;
    }


    public void setIdSegmento(int idSegmento) {
        this.idSegmento = idSegmento;
    }
}
