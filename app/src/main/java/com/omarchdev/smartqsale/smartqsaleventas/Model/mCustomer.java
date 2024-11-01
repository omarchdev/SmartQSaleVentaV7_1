package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OMAR CHH on 19/10/2017.
 */

public class mCustomer {


    @SerializedName("code_cliente")
    private int iId;
    private String cName;
    private String cApellidoPaterno;
    private String cApellidoMaterno;
    @SerializedName("cNumberPhone")
    private String cNumberPhone;
    @SerializedName("direccion_cliente")
    private String cDireccion;
    private String iIdCustomer;
    @SerializedName("email")
    private String cEmail;
    @SerializedName("tipo_cliente")
    int TipoCliente;
    @SerializedName("razonSocial")
    private String RazonSocial;
    @SerializedName("NumDocumento")
    private String NumeroRuc;
    private String estadoEliminado;
    @SerializedName("id_tipo_documento")
    private int idTipoDocumento;
    private String estadoDomicilio;
    private boolean success;
    private String estadoContribuyente;
    @SerializedName("id_tipo_documento_sunat")
    private int IdTipoDocSunat;
    @SerializedName("documento_cliente")
    private mTipoDocumento tipoDocumento;
    @SerializedName("cTipoDocumento")
    private String cTipoDocumento;
    private String Control1;
    private String Control2;
    private  int idTipoDocPagoDefecto;
    @SerializedName("cEmail")
    private String cemail2;
    @SerializedName("cReferencia")
    private String CReferencia;
    @SerializedName("ciudadLocalidad")
    private String CiudadLocalidad;


    public String getCReferencia() {
        return CReferencia;
    }

    public void setCReferencia(String CReferencia) {
        this.CReferencia = CReferencia;
    }

    public String getCiudadLocalidad() {
        return CiudadLocalidad;
    }

    public void setCiudadLocalidad(String ciudadLocalidad) {
        CiudadLocalidad = ciudadLocalidad;
    }

    public String getCemail2() {
        return cemail2;
    }

    public void setCemail2(String cemail2) {
        this.cemail2 = cemail2;
    }

    public String getControl1() {
        return Control1;
    }

    public void setControl1(String control1) {
        Control1 = control1;
    }

    public String getControl2() {
        return Control2;
    }

    public void setControl2(String control2) {
        Control2 = control2;
    }

    public mCustomer() {
        idTipoDocPagoDefecto=0;
        cTipoDocumento="";
        cName="";
        cApellidoPaterno = "";
        cApellidoMaterno = "";
        cNumberPhone="";
        cEmail="";
        iId=0;
        iIdCustomer="";
        TipoCliente=0;
        RazonSocial="";
        NumeroRuc="";
        estadoEliminado="";
        idTipoDocumento=0;
        estadoDomicilio="";
        success=false;
        estadoContribuyente="";
        tipoDocumento=new mTipoDocumento();
        Control1="";
        Control2="";
    }

    public mCustomer(int iId, String cName, String cApellidoPaterno, String cApellidoMaterno, String cNumberPhone, String cEmail, String cDireccion) {
        this.iId = iId;
        this.cName = cName;
        this.cApellidoPaterno = cApellidoPaterno;
        this.cNumberPhone = cNumberPhone;
        this.cEmail = cEmail;
        this.cApellidoMaterno = cApellidoMaterno;
        this.cDireccion = cDireccion;
    }

    public String getcTipoDocumento() {
        return cTipoDocumento;
    }

    public void setcTipoDocumento(String cTipoDocumento) {
        this.cTipoDocumento = cTipoDocumento;
    }

    public String getEstadoEliminado() {
        return estadoEliminado;
    }

    public void setEstadoEliminado(String estadoEliminado) {
        this.estadoEliminado = estadoEliminado;
    }

    public int getIdTipoDocPagoDefecto() {
        return idTipoDocPagoDefecto;
    }

    public void setIdTipoDocPagoDefecto(int idTipoDocPagoDefecto) {
        this.idTipoDocPagoDefecto = idTipoDocPagoDefecto;
    }

    public String getcApellidoMaterno() {
        return cApellidoMaterno;
    }

    public void setcApellidoMaterno(String cApellidoMaterno) {
        this.cApellidoMaterno = cApellidoMaterno;
    }

    public String getEstadoDomicilio() {
        return estadoDomicilio;
    }

    public void setEstadoDomicilio(String estadoDomicilio) {
        this.estadoDomicilio = estadoDomicilio;

    }

    public mTipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(mTipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public int getIdTipoDocSunat() {
        return IdTipoDocSunat;
    }

    public void setIdTipoDocSunat(int idTipoDocSunat) {
        IdTipoDocSunat = idTipoDocSunat;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEstadoContribuyente() {
        return estadoContribuyente;
    }

    public void setEstadoContribuyente(String estadoContribuyente) {
        this.estadoContribuyente = estadoContribuyente;
    }

    public String getcDireccion() {
        return cDireccion;
    }

    public void setcDireccion(String cDireccion) {
        this.cDireccion = cDireccion;
    }

    public String getiIdCustomer() {
        return iIdCustomer;
    }

    public void setiIdCustomer(String iIdCustomer) {
        this.iIdCustomer = iIdCustomer;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public int getiId() {
        return iId;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getNumeroRuc() {
        return NumeroRuc;
    }

    public void setNumeroRuc(String numeroRuc) {
        NumeroRuc = numeroRuc;
    }

    public String getcApellidoPaterno() {
        return cApellidoPaterno;
    }

    public void setcApellidoPaterno(String cApellidoPaterno) {
        this.cApellidoPaterno = cApellidoPaterno;
    }

    public String getcNumberPhone() {
        return cNumberPhone;
    }

    public void setcNumberPhone(String cNumberPhone) {
        this.cNumberPhone = cNumberPhone;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public int getTipoCliente() {
        return TipoCliente;
    }

    public void setTipoCliente(int tipoCliente) {
        TipoCliente = tipoCliente;
    }

    @Override
    public String toString() {
        return cName + " " + cApellidoPaterno;
    }
}
