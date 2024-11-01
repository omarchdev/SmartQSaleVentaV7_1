package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OMAR CHH on 17/11/2017.
 */

public class mCabeceraVenta {

    private String CountrySubEntity = "";
    @SerializedName("descripcionVenta")
    private String DescripcionVenta = "";
    private boolean Estado;
    private byte EstadoCobrar = (byte) 0;
    private String Identificador = "";
    private String cValorTotal = "";
    @SerializedName("cliente")
    private mCustomer cliente = new mCustomer();
    @SerializedName("codDocPago")
    private String codDocPago = "";
    private String codigoHash = "";
    private String denominacionCliente = "";
    private BigDecimal descuentoGlobal = new BigDecimal(0);
    private String distrito = "";
    private String emisor = "";
    private String enlace = "";
    private String enlaceNf = "";
    private String estadoVenta = "";

    @SerializedName("fechaEmision")
    private String fechaEmision = GenerarFechar();
    @SerializedName("fechaVentaV2")
    private String fechaV2 = "";
    @SerializedName("fechaVenta")
    private String fechaVenta = "";
    @SerializedName("idComprobantePagoSunat")
    private int idComprobantePagoSunat = 0;
    private int idTipoDocPago = 0;
    @SerializedName("idVenta")
    private int idVenta = 0;
    private boolean anulado=false;
    private String informacionAdicional;
    private String mediosPago = "";
    private String nombreCalle = "";
    private String nombreCiudad = "";
    private String nombreVendedor = "";
    @SerializedName("numeroCorrelativo")
    private int numCorrelativo = 0;
    @SerializedName("numeroSerie")
    private String numSerie = "";
    @SerializedName("inumeroCorrelativo")
    private String numeroCorrelativo = "";
    private int numeroRuc = 0;
    private float porCobrar = 0.0f;
    private String rucEmisor = "";
    private int sunatTransaccion = 0;
    @SerializedName("cTipoDocumento")
    private String tipoDocumento = "";
    @SerializedName("totalAnticipo")
    private BigDecimal totalAnticipo = new BigDecimal(0);
    @SerializedName("totalBruto")
    private BigDecimal totalBruto = new BigDecimal(0);
    @SerializedName("totalCambio")
    private BigDecimal totalCambio = new BigDecimal(0);
    @SerializedName("totalDescuento")
    private BigDecimal totalDescuento = new BigDecimal(0);
    @SerializedName("totalExonerada")
    private BigDecimal totalExonerada = new BigDecimal(0);
    @SerializedName("totalGratuita")
    private BigDecimal totalGratuita = new BigDecimal(0);
    @SerializedName("totalGravado")
    private BigDecimal totalGravado = new BigDecimal(0);
    @SerializedName("totalIgv")
    private BigDecimal totalIgv = new BigDecimal(0);
    @SerializedName("totalInafecta")
    private BigDecimal totalInafecta = new BigDecimal(0);
    @SerializedName("totalOtrosCargos")
    private BigDecimal totalOtrosCargos = new BigDecimal(0);
    @SerializedName("totalVenta")
    private BigDecimal totalPagado = new BigDecimal(0);
    @SerializedName("ftotalVenta")
    private float totalVenta;
    private boolean usaFacElectronica = false;
    @SerializedName("vendedor")
    private mVendedor vendedor = new mVendedor();
    private boolean ventaCredito = false;
    private String observacion="";
    private String cEstadoCPE;
    private String codeStatusCPE;

    public String getcEstadoCPE() {
        return cEstadoCPE;
    }

    public void setcEstadoCPE(String cEstadoCPE) {
        this.cEstadoCPE = cEstadoCPE;
    }

    public String getCodeStatusCPE() {
        return codeStatusCPE;
    }

    public void setCodeStatusCPE(String codeStatusCPE) {
        this.codeStatusCPE = codeStatusCPE;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getIdTipoDocPago() {
        return this.idTipoDocPago;
    }

    public boolean isVentaCredito() {
        return this.ventaCredito;
    }

    public void setVentaCredito(boolean ventaCredito) {
        this.ventaCredito = ventaCredito;
    }

    public String getMediosPago() {
        return this.mediosPago;
    }

    public void setMediosPago(String mediosPago) {
        this.mediosPago = mediosPago;
    }

    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }

    public String getFechaV2() {
        return this.fechaV2;
    }

    public void setFechaV2(String fechaV2) {
        this.fechaV2 = fechaV2;
    }

    public String getCodDocPago() {
        return this.codDocPago;
    }

    public void setCodDocPago(String codDocPago) {
        this.codDocPago = codDocPago;
    }

    public void setIdTipoDocPago(int idTipoDocPago) {
        this.idTipoDocPago = idTipoDocPago;
    }

    public String getEnlaceNf() {
        return this.enlaceNf;
    }

    public void setEnlaceNf(String enlaceNf) {
        this.enlaceNf = enlaceNf;
    }

    public BigDecimal getDescuentoGlobal() {
        return this.descuentoGlobal;
    }

    public BigDecimal getTotalBruto() {
        return this.totalBruto;
    }

    public void setTotalBruto(BigDecimal totalBruto) {
        this.totalBruto = totalBruto;
    }

    public String getFechaVenta() {
        return this.fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setDescuentoGlobal(BigDecimal descuentoGlobal) {
        this.descuentoGlobal = descuentoGlobal;
    }

    public String getDescripcionVenta() {
        return this.DescripcionVenta;
    }

    public void setDescripcionVenta(String descripcionVenta) {
        this.DescripcionVenta = descripcionVenta;
    }



    public boolean isUsaFacElectronica() {
        return this.usaFacElectronica;
    }

    public void setUsaFacElectronica(boolean usaFacElectronica) {
        this.usaFacElectronica = usaFacElectronica;
    }

    public String getEnlace() {
        return this.enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getCodigoHash() {
        return this.codigoHash;
    }

    public void setCodigoHash(String codigoHash) {
        this.codigoHash = codigoHash;
    }

    public String getCountrySubEntity() {
        return this.CountrySubEntity;
    }

    public void setCountrySubEntity(String countrySubEntity) {
        this.CountrySubEntity = countrySubEntity;
    }

    public String getIdentificador() {
        return this.Identificador;
    }

    public void setIdentificador(String identificador) {
        this.Identificador = identificador;
    }

    public String getEstadoVenta() {
        return this.estadoVenta;
    }

    public void setEstadoVenta(String estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public BigDecimal getTotalAnticipo() {
        return this.totalAnticipo;
    }

    public void setTotalAnticipo(BigDecimal totalAnticipo) {
        this.totalAnticipo = totalAnticipo;
    }

    private String GenerarFechar() {
        return new SimpleDateFormat("yyyymmdd").format(new Date());
    }

    public String getRucEmisor() {
        return this.rucEmisor;
    }

    public void setRucEmisor(String rucEmisor) {
        this.rucEmisor = rucEmisor;
    }

    public BigDecimal getTotalCambio() {
        return this.totalCambio;
    }

    public void setTotalCambio(BigDecimal totalCambio) {
        this.totalCambio = totalCambio;
    }

    public BigDecimal getTotalDescuento() {
        return this.totalDescuento;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public String getNombreVendedor() {
        return this.nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public BigDecimal getTotalInafecta() {
        return this.totalInafecta;
    }

    public void setTotalInafecta(BigDecimal totalInafecta) {
        this.totalInafecta = totalInafecta;
    }

    public BigDecimal getTotalExonerada() {
        return this.totalExonerada;
    }

    public void setTotalExonerada(BigDecimal totalExonerada) {
        this.totalExonerada = totalExonerada;
    }

    public BigDecimal getTotalGratuita() {
        return this.totalGratuita;
    }

    public void setTotalGratuita(BigDecimal totalGratuita) {
        this.totalGratuita = totalGratuita;
    }

    public BigDecimal getTotalOtrosCargos() {
        return this.totalOtrosCargos;
    }

    public String getcValorTotal() {
        return this.cValorTotal;
    }

    public void setcValorTotal(String cValorTotal) {
        this.cValorTotal = cValorTotal;
    }

    public void setTotalOtrosCargos(BigDecimal totalOtrosCargos) {
        this.totalOtrosCargos = totalOtrosCargos;
    }

    public String getNombreCalle() {
        return this.nombreCalle;
    }

    public void setNombreCalle(String nombreCalle) {
        this.nombreCalle = nombreCalle;
    }

    public String getNombreCiudad() {
        return this.nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public String getDistrito() {
        return this.distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getEmisor() {
        return this.emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public BigDecimal getTotalIgv() {
        return this.totalIgv;
    }

    public void setTotalIgv(BigDecimal totalIgv) {
        this.totalIgv = totalIgv;
    }

    public BigDecimal getTotalGravado() {
        return this.totalGravado;
    }

    public void setTotalGravado(BigDecimal totalGravado) {
        this.totalGravado = totalGravado;
    }

    public int getIdVenta() {
        return this.idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getDenominacionCliente() {
        return this.denominacionCliente;
    }

    public void setDenominacionCliente(String denominacionCliente) {
        this.denominacionCliente = denominacionCliente;
    }

    public mVendedor getVendedor() {
        return this.vendedor;
    }

    public void setVendedor(mVendedor vendedor) {
        this.vendedor = vendedor;
    }

    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getFechaEmision() {
        return this.fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public boolean isEstado() {
        return this.Estado;
    }

    public void setEstado(boolean estado) {
        this.Estado = estado;
    }

    public float getTotalVenta() {
        return this.totalVenta;
    }

    public void setTotalVenta(float totalVenta) {
        this.totalVenta = totalVenta;
    }

    public int getNumeroRuc() {
        return this.numeroRuc;
    }

    public void setNumeroRuc(int numeroRuc) {
        this.numeroRuc = numeroRuc;
    }

    public String getNumeroCorrelativo() {
        return this.numeroCorrelativo;
    }

    public void setNumeroCorrelativo(String numeroCorrelativo) {
        this.numeroCorrelativo = numeroCorrelativo;
    }

    public String getNumSerie() {
        return this.numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public int getNumCorrelativo() {
        return this.numCorrelativo;
    }

    public void setNumCorrelativo(int numCorrelativo) {
        this.numCorrelativo = numCorrelativo;
    }

    public int getIdComprobantePagoSunat() {
        return this.idComprobantePagoSunat;
    }

    public void setIdComprobantePagoSunat(int idComprobantePagoSunat) {
        this.idComprobantePagoSunat = idComprobantePagoSunat;
    }

    public int getSunatTransaccion() {
        return this.sunatTransaccion;
    }

    public void setSunatTransaccion(int sunatTransaccion) {
        this.sunatTransaccion = sunatTransaccion;
    }

    public mCustomer getCliente() {
        return this.cliente;
    }

    public void setCliente(mCustomer cliente) {
        this.cliente = cliente;
    }

    public String getInformacionAdicional() {
        return this.informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public byte getEstadoCobrar() {
        return this.EstadoCobrar;
    }

    public void setEstadoCobrar(byte estadoCobrar) {
        this.EstadoCobrar = estadoCobrar;
    }

    public BigDecimal getTotalPagado() {
        return this.totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public float getPorCobrar() {
        return this.porCobrar;
    }

    public void setPorCobrar(float porCobrar) {
        this.porCobrar = porCobrar;
    }
}
