package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by OMAR CHH on 31/12/2017.
 */

public class mVenta {

    Calendar calendar = Calendar.getInstance();
    
    private int idCabeceraVenta;
    private int idCabeceraPedido;
    private int FechaEmision;
    @SerializedName("fechaVentaRealizadaV2")
    private Date fechaVentaRealizadaV2;
    @SerializedName("fechaCancelarVentaV2")
    private Date fechaCancelarVentaV2;
    private Timestamp fechaVentaRealizada;
    private Timestamp fechaCancelarVenta;
    private boolean EstadoCobrar;
    private BigDecimal pagado;
    private BigDecimal porCobrar;
    private int tipoDocumento;
    private String numeroSerie;
    private String numeroCorrelativo;
    private String numeroRuc;
    private String descripcionTienda;
    private int idCliente;
    private String nombreCliente;
    private int idVendedor;
    private String nombreVendedor;
    private String cEstadoVenta;
    private BigDecimal totalBruto;
    private BigDecimal descuento;
    private BigDecimal totalNeto;
    private BigDecimal cambio;
    private String descripcionDocumento;
    private boolean bUsaFacturaElectronica;
    private String estadoCpe;

    public mVenta() {
        descripcionDocumento="";
        idCabeceraVenta = 0;
        idCabeceraPedido = 0;
        FechaEmision = 0;
        EstadoCobrar = false;
        pagado = new BigDecimal(0);
        porCobrar = new BigDecimal(0);
        tipoDocumento = 0;
        numeroSerie = "";
        numeroCorrelativo = "";
        numeroRuc = "";
        descripcionTienda = "";
        idCliente = 0;
        nombreCliente = "";
        idVendedor = 0;
        nombreVendedor = "";
        cEstadoVenta = "";
        totalBruto = new BigDecimal(0);
        descuento = new BigDecimal(0);
        totalNeto = new BigDecimal(0);
        cambio = new BigDecimal(0);
        java.util.Date utilDate = new Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();

        fechaVentaRealizada = new Timestamp(lnMilisegundos);
        fechaCancelarVenta = new Timestamp(lnMilisegundos);

    }

    public Date getFechaVentaRealizadaV2() {
        return fechaVentaRealizadaV2;
    }

    public void setFechaVentaRealizadaV2(Date fechaVentaRealizadaV2) {
        this.fechaVentaRealizadaV2 = fechaVentaRealizadaV2;
    }

    public Date getFechaCancelarVentaV2() {
        return fechaCancelarVentaV2;
    }

    public void setFechaCancelarVentaV2(Date fechaCancelarVentaV2) {
        this.fechaCancelarVentaV2 = fechaCancelarVentaV2;
    }

    public boolean isbUsaFacturaElectronica() {
        return bUsaFacturaElectronica;
    }

    public void setbUsaFacturaElectronica(boolean bUsaFacturaElectronica) {
        this.bUsaFacturaElectronica = bUsaFacturaElectronica;
    }

    public String getEstadoCpe() {
        return estadoCpe;
    }

    public void setEstadoCpe(String estadoCpe) {
        this.estadoCpe = estadoCpe;
    }

    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }

    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }

    public int getIdCabeceraVenta() {
        return idCabeceraVenta;
    }

    public void setIdCabeceraVenta(int idCabeceraVenta) {
        this.idCabeceraVenta = idCabeceraVenta;
    }

    public int getIdCabeceraPedido() {
        return idCabeceraPedido;
    }

    public void setIdCabeceraPedido(int idCabeceraPedido) {
        this.idCabeceraPedido = idCabeceraPedido;
    }

    public int getFechaEmision() {
        return FechaEmision;
    }

    public void setFechaEmision(int fechaEmision) {
        FechaEmision = fechaEmision;
    }

    public Timestamp getFechaVentaRealizada() {
        return fechaVentaRealizada;
    }

    public void setFechaVentaRealizada(Timestamp fechaVentaRealizada) {
        this.fechaVentaRealizada = fechaVentaRealizada;
    }

    public boolean isEstadoCobrar() {
        return EstadoCobrar;
    }

    public void setEstadoCobrar(boolean estadoCobrar) {
        EstadoCobrar = estadoCobrar;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }

    public BigDecimal getPorCobrar() {
        return porCobrar;
    }

    public void setPorCobrar(BigDecimal porCobrar) {
        this.porCobrar = porCobrar;
    }

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNumeroCorrelativo() {
        return numeroCorrelativo;
    }

    public void setNumeroCorrelativo(String numeroCorrelativo) {
        this.numeroCorrelativo = numeroCorrelativo;
    }

    public String getNumeroRuc() {
        return numeroRuc;
    }

    public void setNumeroRuc(String numeroRuc) {
        this.numeroRuc = numeroRuc;
    }

    public String getDescripcionTienda() {
        return descripcionTienda;
    }

    public void setDescripcionTienda(String descripcionTienda) {
        this.descripcionTienda = descripcionTienda;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public BigDecimal getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(BigDecimal totalBruto) {
        this.totalBruto = totalBruto;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(BigDecimal totalNeto) {
        this.totalNeto = totalNeto;
    }

    public Timestamp getFechaCancelarVenta() {
        return fechaCancelarVenta;
    }

    public void setFechaCancelarVenta(Timestamp fechaCancelarVenta) {
        this.fechaCancelarVenta = fechaCancelarVenta;
    }

    public String getcEstadoVenta() {
        return cEstadoVenta;
    }

    public void setcEstadoVenta(String cEstadoVenta) {
        this.cEstadoVenta = cEstadoVenta;
    }

    public BigDecimal getCambio() {
        return cambio;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    public void LimpiarObjeto() {

        idCabeceraVenta = 0;
        idCabeceraPedido = 0;
        FechaEmision = 0;
        EstadoCobrar = false;
        pagado = new BigDecimal(0);
        porCobrar = new BigDecimal(0);
        tipoDocumento = 0;
        numeroSerie = "";
        numeroCorrelativo = "";
        numeroRuc = "";
        descripcionTienda = "";
        idCliente = 0;
        nombreCliente = "";
        idVendedor = 0;
        nombreVendedor = "";
        cEstadoVenta = "";
        totalBruto = new BigDecimal(0);
        descuento = new BigDecimal(0);
        totalNeto = new BigDecimal(0);
        java.util.Date utilDate = new Date(); //fecha actual
        long lnMilisegundos = utilDate.getTime();
        fechaVentaRealizada = new Timestamp(lnMilisegundos);
        fechaCancelarVenta = new Timestamp(lnMilisegundos);
    }
}
