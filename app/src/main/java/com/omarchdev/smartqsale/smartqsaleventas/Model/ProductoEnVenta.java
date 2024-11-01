package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by OMAR CHH on 17/11/2017.
 */

public class ProductoEnVenta {
    int idCabeceraPedido;

    @SerializedName("idProducto")
    int IdProducto;
    int itemNum;
    int idDetallePedido;
    @SerializedName("productName")
    String ProductName;
    boolean ComboSimple;
    String observacion;
    @SerializedName("descripcionVariante")
    String descripcionVariante;
    @SerializedName("cantidad")
    float cantidad;
    float cantidadEnStock;
    @SerializedName("precioOriginal")
    BigDecimal precioOriginal;
    float descuento;
    String observacionProducto;
    @SerializedName("precioVentaFinal")
    BigDecimal precioVentaFinal;
    int cod_Variante;
    @SerializedName("esVariante")
    boolean esVariante;
    boolean esPack;
    boolean esDetallePack;
    boolean esModificado;
    String DescripcionModificador;
    List<ProductoEnVenta> productoEnVentaList;
    int idProductoPadre;
    String metodoGuardar;
    int AccionGuardarCodeBar;
    float cantidadReserva;
    float stockActual;
    boolean disponibleStock;
    byte respuestaGuardar;
    private String descripcionCategoria;
    private String descripcionSubCategoria;
    private int codPrecioAlterno;
    @SerializedName("precioNeto")
    private BigDecimal precioNeto;
    @SerializedName("montoIgv")
    private BigDecimal montoIgv;
    private String descUnidad;
    @SerializedName("codUnidSunat")
    private String codUnidSunat;
    @SerializedName("codigoProducto")
    private String codigoProducto;
    @SerializedName("valorUnitario")
    private BigDecimal valorUnitario;
    @SerializedName("montoDescuento")
    private BigDecimal montoDescuento;
    private boolean aplicarDescuento;
    private boolean usaDescuento;
    private String descripcionCombo;
    private int idTienda;
    private String fecha;
    private BigDecimal costoTotal;
    private mAreaProduccion areaProduccion;
    private boolean bPrecioVariable;
    private boolean ControlTiempo;
    private String horaInicio;
    private String horaFinal;
    private String estadoEliminado;
    private String EstadoGuardado;
    private String DetallePack;
    private String InformacionAdicionalTiempo;
    private boolean ProductoUnico;
    private byte[] Image;
    private int ITipoImagen;
    private String CCodigoColor;
    private String CCodigoImagen;
    private TimeData TiempoInicio;
    private TimeData TiempoFinal;


    @SerializedName("tiempoInicioT")
    private String TiempoInicioTemp;
    @SerializedName("tiempoFinalT")
    private String TiempoFinalTemp;

    public int idTerminal;
    public int idUsuario ;

    public void ConvierteFechaJSON(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = sdf.parse(TiempoInicioTemp.replace("T"," "));
            TiempoInicio=new TimeData();
            TiempoInicio.setTimestamp( new Timestamp( date1.getTime()));
            //TiempoInicio.setDatemilis(date1.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Date date2 = sdf.parse(TiempoFinalTemp.replace("T"," "));
            TiempoFinal=new TimeData();
            TiempoFinal.setTimestamp(new Timestamp( date2.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TimeData getTiempoInicio() {
        return TiempoInicio;
    }

    public TimeData getTiempoFinal() {
        return TiempoFinal;
    }

    public void setHoraInicio(TimeData horaInicio) {
        TiempoInicio = horaInicio;
    }

    public void setHoraFinal(TimeData horaFinal) {
        TiempoInicio = horaFinal;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public int getITipoImagen() {
        return ITipoImagen;
    }

    public void setITipoImagen(int ITipoImagen) {
        this.ITipoImagen = ITipoImagen;
    }

    public String getCCodigoColor() {
        return CCodigoColor;
    }

    public void setCCodigoColor(String CCodigoColor) {
        this.CCodigoColor = CCodigoColor;
    }

    public String getCCodigoImagen() {
        return CCodigoImagen;
    }

    public void setCCodigoImagen(String CCodigoImagen) {
        this.CCodigoImagen = CCodigoImagen;
    }

    public boolean isProductoUnico() {
        return ProductoUnico;
    }

    public void setProductoUnico(boolean productoUnico) {
        ProductoUnico = productoUnico;
    }


    public String getInformacionAdicionalTiempo() {
        return InformacionAdicionalTiempo;
    }

    public void setInformacionAdicionalTiempo(String informacionAdicionalTiempo) {
        InformacionAdicionalTiempo = informacionAdicionalTiempo;
    }

    public BigDecimal getUtilidad() {
        return utilidad;
    }

    public String getCodigoSunat() {
        return CodigoSunat;
    }

    public void setCodigoSunat(String codigoSunat) {
        CodigoSunat = codigoSunat;
    }

    private String CodigoSunat;


    public void setUtilidad(BigDecimal utilidad) {
        this.utilidad = utilidad;
    }

    private BigDecimal utilidad;

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public mAreaProduccion getAreaProduccion() {
        return areaProduccion;
    }

    public void setAreaProduccion(mAreaProduccion areaProduccion) {
        this.areaProduccion = areaProduccion;
    }

    public ProductoEnVenta() {
        Image=null;
        ITipoImagen=0;
        CCodigoColor="";
        CCodigoImagen="";
        TiempoInicio=new TimeData();
        TiempoFinal=new TimeData();
        ProductoUnico = false;
        InformacionAdicionalTiempo="";
        aplicarDescuento = false;
        montoDescuento = new BigDecimal(0);
        cantidadReserva = 0f;
        bPrecioVariable = false;
        metodoGuardar = "";
        idCabeceraPedido = 0;
        esPack = false;
        itemNum = 0;
        IdProducto = 0;
        ProductName = "";
        estadoEliminado = "";
        cantidad = 0;
        descuento = 0;
        horaInicio = "NN";
        horaFinal = "NN";
        observacion = "";
        utilidad = new BigDecimal(0);
        precioVentaFinal = new BigDecimal(0);
        cantidadEnStock = 0;
        valorUnitario = new BigDecimal(0);
        esVariante = false;
        cod_Variante = 0;
        descripcionVariante = "";
        idDetallePedido = 0;
        respuestaGuardar = 0;
        esDetallePack = false;
        esModificado = false;
        idProductoPadre = 0;
        costoTotal = new BigDecimal(0);
        DescripcionModificador = "";
        AccionGuardarCodeBar = 0;
        disponibleStock = false;
        stockActual = 0;
        codPrecioAlterno = 0;
        precioNeto = new BigDecimal(0);
        montoIgv = new BigDecimal(0);
        descUnidad = "";
        codUnidSunat = "";
        codigoProducto = "";
        usaDescuento = false;
        descripcionCategoria = "";
        descripcionSubCategoria = "";
        descripcionCombo = "";
        idTienda = 0;
        fecha = "";
        EstadoGuardado = "";
        areaProduccion = new mAreaProduccion();
        ComboSimple = false;
    }



    public ProductoEnVenta(int idProducto,
                           String productName,
                           int itemNum, float cantidad,
                           BigDecimal precioOriginal,
                           BigDecimal precioVentaFinal,
                           String observacion) {
        this.IdProducto = idProducto;
        this.ProductName = productName;

        this.cantidad = cantidad;
        this.observacion = observacion;
        this.precioVentaFinal = precioVentaFinal;
        this.precioOriginal = precioOriginal;
        this.itemNum = itemNum;
        this.estadoEliminado = "";
        this.EstadoGuardado = "";
    }


    public boolean isComboSimple() {
        return ComboSimple;
    }

    public void setComboSimple(boolean comboSimple) {
        ComboSimple = comboSimple;
    }

    public String getEstadoEliminado() {
        return estadoEliminado;
    }

    public void setEstadoEliminado(String estadoEliminado) {
        this.estadoEliminado = estadoEliminado;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public boolean isDisponibleStock() {
        return disponibleStock;
    }

    public void setDisponibleStock(boolean disponibleStock) {
        this.disponibleStock = disponibleStock;
    }

    public boolean isbPrecioVariable() {
        return bPrecioVariable;
    }

    public boolean isControlTiempo() {
        return ControlTiempo;
    }

    public void setControlTiempo(boolean controlTiempo) {
        ControlTiempo = controlTiempo;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public void setbPrecioVariable(boolean bPrecioVariable) {
        this.bPrecioVariable = bPrecioVariable;
    }

    public String getDescripcionCombo() {
        return descripcionCombo;
    }

    public void setDescripcionCombo(String descripcionCombo) {
        this.descripcionCombo = descripcionCombo;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public String getDescripcionSubCategoria() {
        return descripcionSubCategoria;
    }

    public void setDescripcionSubCategoria(String descripcionSubCategoria) {
        this.descripcionSubCategoria = descripcionSubCategoria;
    }

    public void inicializarLista() {

        productoEnVentaList = new ArrayList<>();

    }

    public BigDecimal getMontoDescuento() {
        return montoDescuento;
    }

    public void setMontoDescuento(BigDecimal montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    public int getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(int idTienda) {
        this.idTienda = idTienda;
    }

    public boolean isAplicarDescuento() {

        return aplicarDescuento;
    }

    public String getEstadoGuardado() {
        return EstadoGuardado;
    }

    public void setEstadoGuardado(String estadoGuardado) {
        EstadoGuardado = estadoGuardado;
    }

    public boolean isUsaDescuento() {
        return usaDescuento;
    }

    public void setUsaDescuento(boolean usaDescuento) {
        this.usaDescuento = usaDescuento;
    }

    public void setAplicarDescuento(boolean aplicarDescuento) {
        this.aplicarDescuento = aplicarDescuento;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public boolean isEsPack() {
        return esPack;
    }

    public float getCantidadReserva() {
        return cantidadReserva;
    }

    public void setCantidadReserva(float cantidadReserva) {
        this.cantidadReserva = cantidadReserva;
    }

    public void setEsPack(boolean esPack) {
        this.esPack = esPack;
    }

    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public float getCantidadEnStock() {
        return cantidadEnStock;
    }

    public void setCantidadEnStock(float cantidadEnStock) {
        this.cantidadEnStock = cantidadEnStock;
    }

    public int getIdCabeceraPedido() {
        return idCabeceraPedido;
    }

    public void setIdCabeceraPedido(int idCabeceraPedido) {
        this.idCabeceraPedido = idCabeceraPedido;
    }

    public BigDecimal getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(BigDecimal precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    public BigDecimal getPrecioNeto() {
        return precioNeto;
    }

    public void setPrecioNeto(BigDecimal precioNeto) {
        this.precioNeto = precioNeto;
    }

    public BigDecimal getMontoIgv() {
        return montoIgv;
    }

    public void setMontoIgv(BigDecimal montoIgv) {
        this.montoIgv = montoIgv;
    }

    public String getDescUnidad() {
        return descUnidad;
    }

    public void setDescUnidad(String descUnidad) {
        this.descUnidad = descUnidad;
    }

    public String getCodUnidSunat() {
        return codUnidSunat;
    }

    public void setCodUnidSunat(String codUnidSunat) {
        this.codUnidSunat = codUnidSunat;
    }

    public int getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(int idProducto) {
        IdProducto = idProducto;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecioVentaFinal() {
        return precioVentaFinal;
    }


    public void setPrecioVentaFinal(BigDecimal precioVentaFinal) {
        this.precioVentaFinal = precioVentaFinal;
    }

    @Override
    public String toString() {
        return "Numero Item:" + String.valueOf(itemNum) + " PreciosVenta " + String.valueOf(precioVentaFinal) + " " + String.valueOf(precioOriginal) + " Cantidad:" + String.valueOf(cantidad);
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public String getObservacionProducto() {
        return observacionProducto;
    }

    public void setObservacionProducto(String observacionProducto) {
        this.observacionProducto = observacionProducto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getCod_Variante() {
        return cod_Variante;
    }

    public void setCod_Variante(int cod_Variante) {
        this.cod_Variante = cod_Variante;
    }

    public boolean isEsVariante() {
        return esVariante;
    }

    public void setEsVariante(boolean esVariante) {
        this.esVariante = esVariante;
    }

    public String getDescripcionVariante() {
        return descripcionVariante;
    }

    public void setDescripcionVariante(String descripcionVariante) {
        this.descripcionVariante = descripcionVariante;
    }

    public boolean isEsDetallePack() {
        return esDetallePack;
    }

    public void setEsDetallePack(boolean esDetallePack) {
        this.esDetallePack = esDetallePack;
    }


    public int getIdProductoPadre() {
        return idProductoPadre;
    }

    public void setIdProductoPadre(int idProductoPadre) {
        this.idProductoPadre = idProductoPadre;
    }

    public List<ProductoEnVenta> getProductoEnVentaList() {
        return productoEnVentaList;
    }

    public void setProductoEnVentaList(List<ProductoEnVenta> productoEnVentaList) {
        this.productoEnVentaList = productoEnVentaList;
    }

    public String getMetodoGuardar() {
        return metodoGuardar;
    }

    public void setMetodoGuardar(String metodoGuardar) {
        this.metodoGuardar = metodoGuardar;
    }

    public boolean isEsModificado() {
        return esModificado;
    }

    public void setEsModificado(boolean esModificado) {
        this.esModificado = esModificado;
    }

    public String getDescripcionModificador() {
        return DescripcionModificador;
    }

    public void setDescripcionModificador(String descripcionModificador) {
        DescripcionModificador = descripcionModificador;
    }

    public int getAccionGuardarCodeBar() {
        return AccionGuardarCodeBar;
    }

    public void setAccionGuardarCodeBar(int accionGuardarCodeBar) {
        AccionGuardarCodeBar = accionGuardarCodeBar;
    }

    public float getStockActual() {
        return stockActual;
    }

    public void setStockActual(float stockActual) {
        this.stockActual = stockActual;
    }

    public byte getRespuestaGuardar() {
        return respuestaGuardar;
    }

    public void setRespuestaGuardar(byte respuestaGuardar) {
        this.respuestaGuardar = respuestaGuardar;
    }

    public int getCodPrecioAlterno() {
        return codPrecioAlterno;
    }

    public void setCodPrecioAlterno(int codPrecioAlterno) {
        this.codPrecioAlterno = codPrecioAlterno;
    }

    public String getNombreProductoDetallePack(){
        String nombre="";
        if(!getDescripcionCategoria().trim().equals("")){
            nombre=getDescripcionCategoria()+"\n";
        }
        nombre=nombre+getProductName();
        /*
        if(!getDetallePack().trim().equals("")){
            nombre=nombre+"\n"+ getDetallePackFinal();
        }*/
        return nombre;
    }
    public String getDetallePack() {
        return DetallePack.trim();
    }

    public String getDetallePackFinal(){
        return DetallePack.replace("||","\n");
    }
    public void setDetallePack(String detallePack) {
        DetallePack = detallePack;
    }

    public void setTiempoInicioTemp(String tiempoInicioTemp) {
        TiempoInicioTemp = tiempoInicioTemp;
    }

    public void setTiempoFinalTemp(String tiempoFinalTemp) {
        TiempoFinalTemp = tiempoFinalTemp;
    }
}




