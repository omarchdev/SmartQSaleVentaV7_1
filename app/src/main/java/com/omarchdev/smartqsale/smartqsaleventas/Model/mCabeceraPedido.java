package com.omarchdev.smartqsale.smartqsaleventas.Model;

import android.graphics.Bitmap;

import java.math.BigDecimal;
import java.util.List;


import com.google.gson.annotations.SerializedName;

/**
 * import androidmads.library.qrgenearator.QRGContents;
 * import androidmads.library.qrgenearator.QRGEncoder;
 * Created by OMAR CHH on 14/12/2017.
 */


public class mCabeceraPedido {
    @SerializedName("id_pedido")
    private int idCabecera;
    @SerializedName("identificadorPedido")
    private String identificadorPedido;
    @SerializedName("num_pedido")
    private String numPedido;
    private String observacion;
    private int idCliente;
    private int idDocCliente;
    private int idTipoDocSunat;
    @SerializedName("nombreCliente")
    private String denominacionCliente;
    private String numDocCliente;
    private String direccionCliente;
    private String emailCliente;
    private String numTelefonoCliente;
    private int idVendedor;
    @SerializedName("nombreVendedor")
    private String nombreVendedor;
    private float valorDescuento;
    @SerializedName("fechaCreacion")
    private int fechaCreacion;
    @SerializedName("descuentoPrecio")
    private BigDecimal descuentoPrecio;
    private byte tipoDescuento;
    private boolean bEstadoPagado;
    @SerializedName("totalBruto")
    private BigDecimal totalBruto;
    @SerializedName("total_pedido")
    private BigDecimal totalNeto;
    private char estadoPermanecia;
    private mCustomer cliente;
    private BigDecimal porcentajeDescuento;
    private mVendedor vendedor;
    private String fecha;
    private String hora;
    @SerializedName("descripcion_documento_pago")
    private String documentoPago;
    private String cEstadoAtencion;
    private String cEstadoPermanencia;
    private String FechaHoraAtencion;
    @SerializedName("fechaReservaPedido")
    private String FechaReserva;
    private mZonaServicio zonaServicio;
    @SerializedName("idEntregaPedido")
    private int IdEntregaPedido;
    @SerializedName("horaInicio")
    private String HoraInicio;
    @SerializedName("horaFinal")
    private String HoraFinal;
    @SerializedName("fechaInicial")
    private String FechaInicial;
    @SerializedName("fechaFinal")
    private String FechaFinal;
    private String ObservacionPedido;
    private List<mOperario> operarios;
    @SerializedName("descripcionPedido")
    private String DescripcionPedido;
    @SerializedName("descripcionEstadoPedido")
    private String observacionReserva;
    @SerializedName("esAnulado")
    private boolean anulado;
    @SerializedName("permiteOpciones")
    private boolean permitirModificaciones;
    private String identificador2;
    @SerializedName("tipoPedido")
    private String cTipoPedido;
    private String cEstadoEntregaPedido;


    public String getcEstadoEntregaPedido() {
        return cEstadoEntregaPedido;
    }

    public void setcEstadoEntregaPedido(String cEstadoEntregaPedido) {
        this.cEstadoEntregaPedido = cEstadoEntregaPedido;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public boolean isbEntregado() {
        return bEntregado;
    }

    public void setbEntregado(boolean bEntregado) {
        this.bEntregado = bEntregado;
    }

    @SerializedName("fecha_entrega")
    private String fechaEntrega;

    @SerializedName("pedido_entregado")
    private boolean bEntregado;

    public String getObservacionReserva() {
        return observacionReserva;
    }

    public void setObservacionReserva(String observacionReserva) {
        this.observacionReserva = observacionReserva;
    }

    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }


    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getIdentificador2() {
        return identificador2;
    }

    public void setIdentificador2(String identificador2) {
        this.identificador2 = identificador2;
    }

    public mCabeceraPedido() {
        identificador2="";
        permitirModificaciones=false;
        bEstadoPagado=false;
        HoraFinal="";
        HoraInicio="";
        DescripcionPedido="";
        porcentajeDescuento=new BigDecimal(0);
        idCabecera = 0;
        identificadorPedido = "";
        cEstadoEntregaPedido="";
        observacion = "";
        idCliente = 0;
        denominacionCliente = "";
        FechaFinal="";
        FechaInicial="";
        idVendedor = 0;
        nombreVendedor = "";
        valorDescuento = 0.00f;
        estadoPermanecia = 'T';
        descuentoPrecio = new BigDecimal(0);
        totalBruto = new BigDecimal(0);
        totalNeto = new BigDecimal(0);
        tipoDescuento=0;
        cliente=new mCustomer();
        vendedor=new mVendedor();
        hora="";
        documentoPago="";
        zonaServicio=new mZonaServicio();
        fecha="";
        cEstadoAtencion="";
        cEstadoPermanencia="";
        observacionReserva="";
        anulado=false;
        fechaEntrega="";
        bEntregado=false;
        cTipoPedido="";
    }

    public mCabeceraPedido(int idCabecera, String identificadorPedido, String observacion, int idCliente, String denominacionCliente, int idVendedor, String nombreVendedor, int fechaCreacion, BigDecimal descuentoPrecio, BigDecimal totalBruto, BigDecimal totalNeto, char estadoPermanecia) {
        this.idCabecera = idCabecera;
        this.identificadorPedido = identificadorPedido;
        this.observacion = observacion;
        this.idCliente = idCliente;
        this.denominacionCliente = denominacionCliente;
        this.idVendedor = idVendedor;
        this.nombreVendedor = nombreVendedor;
        this.fechaCreacion = fechaCreacion;
        this.descuentoPrecio = descuentoPrecio;
        this.totalBruto = totalBruto;
        this.totalNeto = totalNeto;
        this.estadoPermanecia = estadoPermanecia;
        cliente=new mCustomer();

    }

    public boolean isbEstadoPagado() {
        return bEstadoPagado;
    }

    public void setbEstadoPagado(boolean bEstadoPagado) {
        this.bEstadoPagado = bEstadoPagado;
    }

    public String getFechaInicial() {
        return FechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        FechaInicial = fechaInicial;
    }

    public String getDocumentoPago() {
        return documentoPago;
    }

    public void setDocumentoPago(String documentoPago) {
        this.documentoPago = documentoPago;
    }

    public String getFechaFinal() {
        return FechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        FechaFinal = fechaFinal;
    }

    public mZonaServicio getZonaServicio() {
        return zonaServicio;
    }

    public void setZonaServicio(mZonaServicio zonaServicio) {
        this.zonaServicio = zonaServicio;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
    }

    public String getHoraFinal() {
        return HoraFinal;
    }

    public void setHoraFinal(String horaFinal) {
        HoraFinal = horaFinal;
    }

    public boolean isPermitirModificaciones() {
        return permitirModificaciones;
    }

    public void setPermitirModificaciones(boolean permitirModificaciones) {
        this.permitirModificaciones = permitirModificaciones;
    }

    public List<mOperario> getOperarios() {
        return operarios;
    }

    public String getObservacionPedido() {
        return ObservacionPedido;
    }

    public void setObservacionPedido(String observacionPedido) {
        ObservacionPedido = observacionPedido;
    }

    public String getcTipoPedido() {
        return cTipoPedido;
    }

    public void setcTipoPedido(String cTipoPedido) {
        this.cTipoPedido = cTipoPedido;
    }

    public void setOperarios(List<mOperario> operarios) {
        this.operarios = operarios;
    }

    public int getIdDocCliente() {
        return idDocCliente;
    }

    public void setIdDocCliente(int idDocCliente) {
        this.idDocCliente = idDocCliente;
    }

    public int getIdTipoDocSunat() {
        return idTipoDocSunat;
    }

    public void setIdTipoDocSunat(int idTipoDocSunat) {
        this.idTipoDocSunat = idTipoDocSunat;
    }

    public String getNumDocCliente() {
        return numDocCliente;
    }

    public void setNumDocCliente(String numDocCliente) {
        this.numDocCliente = numDocCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getDescripcionPedido() {
        return DescripcionPedido;
    }

    public void setDescripcionPedido(String descripcionPedido) {
        DescripcionPedido = descripcionPedido;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getNumTelefonoCliente() {
        return numTelefonoCliente;
    }

    public void setNumTelefonoCliente(String numTelefonoCliente) {
        this.numTelefonoCliente = numTelefonoCliente;
    }

    public mCustomer getCliente() {
        return cliente;
    }

    public void setCliente(mCustomer cliente) {
        this.cliente = cliente;
    }

    public mVendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(mVendedor vendedor) {
        this.vendedor = vendedor;
    }

    public mCabeceraPedido(int idCabecera, String identificadorPedido,
                           String observacion, int idCliente, String denominacionCliente, int idVendedor,
                           String nombreVendedor, int fechaCreacion, char estadoPermanecia,
                           BigDecimal totalBruto, BigDecimal descuentoPrecio, BigDecimal totalNeto, byte tipoDescuento, BigDecimal porcentajeDescuento) {
        this.zonaServicio=new mZonaServicio();
        this.idCabecera = idCabecera;
        this.identificadorPedido = identificadorPedido;
        this.observacion = observacion;
        this.idCliente = idCliente;
        this.denominacionCliente = denominacionCliente;
        this.idVendedor = idVendedor;
        this.nombreVendedor = nombreVendedor;
        this.totalBruto = totalBruto;
        this.descuentoPrecio = descuentoPrecio;
        this.totalNeto = totalNeto;
        this.estadoPermanecia = estadoPermanecia;
        this.fechaCreacion = fechaCreacion;
        this.tipoDescuento=tipoDescuento;
        this.porcentajeDescuento=porcentajeDescuento;
        cliente=new mCustomer();

    }

    public mCabeceraPedido(int idCabecera, String identificadorPedido, String denominacionCliente, String nombreVendedor, int fechaCreacion, BigDecimal totalBruto, BigDecimal descuentoPrecio, BigDecimal totalNeto) {
        this.idCabecera = idCabecera;
        this.identificadorPedido = identificadorPedido;
        this.denominacionCliente = denominacionCliente;
        this.nombreVendedor = nombreVendedor;
        this.fechaCreacion = fechaCreacion;
        this.totalBruto = totalBruto;
        this.totalNeto = totalNeto;
        this.descuentoPrecio = descuentoPrecio;
        cliente=new mCustomer();
    }

    public mCabeceraPedido(int idCabecera, String identificadorPedido,
                           String denominacionCliente, String nombreVendedor,
                           int fechaCreacion, BigDecimal totalBruto,
                           BigDecimal descuentoPrecio,
                           BigDecimal totalNeto,String fechaReserva,
                           String descripcionPedido) {
        this.idCabecera = idCabecera;
        this.identificadorPedido = identificadorPedido;
        this.denominacionCliente = denominacionCliente;
        this.nombreVendedor = nombreVendedor;
        this.fechaCreacion = fechaCreacion;
        this.totalBruto = totalBruto;
        this.totalNeto = totalNeto;
        this.descuentoPrecio = descuentoPrecio;
        this.FechaReserva=fechaReserva;
        cliente=new mCustomer();
        this.DescripcionPedido=descripcionPedido;
    }

    public int getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(int fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public char getEstadoPermanecia() {
        return estadoPermanecia;

    }

    public byte getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(byte tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public void setEstadoPermanecia(char estadoPermanecia) {
        this.estadoPermanecia = estadoPermanecia;
    }

    public String getFechaHoraAtencion() {
        return FechaHoraAtencion;
    }

    public void setFechaHoraAtencion(String fechaHoraAtencion) {
        FechaHoraAtencion = fechaHoraAtencion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getcEstadoAtencion() {
        return cEstadoAtencion;
    }

    public void setcEstadoAtencion(String cEstadoAtencion) {
        this.cEstadoAtencion = cEstadoAtencion;
    }

    public String getcEstadoPermanencia() {
        return cEstadoPermanencia;
    }

    public void setcEstadoPermanencia(String cEstadoPermanencia) {
        this.cEstadoPermanencia = cEstadoPermanencia;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(int idCabecera) {
        this.idCabecera = idCabecera;
    }

    public String getIdentificadorPedido() {
        return identificadorPedido;
    }

    public void setIdentificadorPedido(String identificadorPedido) {
        this.identificadorPedido = identificadorPedido;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getDenominacionCliente() {
        return denominacionCliente;
    }

    public void setDenominacionCliente(String denominacionCliente) {
        this.denominacionCliente = denominacionCliente;
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

    public float getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(float valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public BigDecimal getDescuentoPrecio() {
        return descuentoPrecio;
    }

    public void setDescuentoPrecio(BigDecimal descuentoPrecio) {
        this.descuentoPrecio = descuentoPrecio;
    }

    public BigDecimal getTotalBruto() {
        return totalBruto;
    }

    public void setTotalBruto(BigDecimal totalBruto) {
        this.totalBruto = totalBruto;
    }

    public BigDecimal getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(BigDecimal totalNeto) {
        this.totalNeto = totalNeto;
    }

    public int getIdEntregaPedido() {
        return IdEntregaPedido;
    }

    public void setIdEntregaPedido(int idEntregaPedido) {
        this.IdEntregaPedido = idEntregaPedido;
    }

    public enum EstadoPedido {
        T, R, S
    }

    public String getFechaReserva() {
        return FechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        FechaReserva = fechaReserva;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public String GetIdentificadorUnicoPedido(){

        switch (cTipoPedido.trim()){

            case "01":
                return "Pedido \nNro :"+numPedido;


            case "02":
                return "Contrato \nNro :"+numPedido;

            default :
                return "";

        }

    }


    public Bitmap GetQrCabeceraZonaServicio(){
        Bitmap bitmap=null;
  /*       try{
             QRGEncoder qrgEncoder = new QRGEncoder(zonaServicio.getDescripcion(), null, QRGContents.Type.TEXT, 240);

             bitmap = qrgEncoder.encodeAsBitmap();

        }catch (Exception ex){
            bitmap=null;
        }
*/
        return bitmap;
    }

    public String GetIndentificadorUnicoSimple(){
        switch (cTipoPedido.trim()){

            case "01":
                return "Pedido Nro :"+numPedido;


            case "02":
                return "Contrato Nro :"+numPedido;

            default :
                return "";

        }
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }
}
