package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 23/09/2017.
 */

public class mProduct {
    @SerializedName("codproducto")
    public int idProduct;
    @SerializedName("codigo_string_producto")
    public String cKey;
    private boolean bVisibleWeb;
    @SerializedName("descripcion")
    public String cProductName;
    private String cUnit;
    private String descripcionVariante;
    @SerializedName("cantidad_stock")
    private float dQuantity;
    private float dQuantityReserve;
    private float dPurcharsePrice;
    private float dSalesPrice;
    @SerializedName("cAdditionalInformation")
    public String cAdditionalInformation;
    private byte[] bImage;
    @SerializedName("precio_venta_unitario")
    private BigDecimal precioVenta;
    private BigDecimal precioCompra;
    private BigDecimal stockDisponible;
    private BigDecimal stockReserva;
    @SerializedName("esFavorito")
    private boolean esFavorito;
    private String estadoActivo;
    private String estadoVisible;
    private String codigoBarra;
    public boolean controlPeso;
    @SerializedName("control_stock")
    public boolean controlStock;
    private String observacionProducto;
    @SerializedName("cCodigo_Color")
    public String codigoColor;
    @SerializedName("cCodigo_Imagen")
    public String codigoForma;
    @SerializedName("iTipo_Imagen")
    public byte tipoRepresentacionImagen;
    @SerializedName("idCategoria")
    private int idCategoria;
    private int idProductImage;
    @SerializedName("bEstado_variante")
    public boolean estadoVariante;
    private List<OpcionVariante> opcionVarianteList;
    private List<Variante> varianteList;
    private int numVariantes;
    private boolean tipoNormal;
    @SerializedName("es_pack")
    public boolean tipoPack;
    private boolean bDetallePack;
    private int idPack;
    private int numItem;
    private byte metodoGuardado;
    @SerializedName("bEstado_modificador")
    public boolean estadoModificador;
    private String modificadores;
    @SerializedName("cantidad_pedido")
    public float cantidadReserva;
    private String unidadMedida;
    private List<AdditionalPriceProduct> priceProductList;
    @SerializedName("es_precio_multiple")
    public boolean multiplePVenta;
    @SerializedName("descripcion_categoria")
    public String  DescripcionCategoria;
    @SerializedName("id_subcategoria")
    public int idSubCategoria;
    @SerializedName("descripcion_subcategoria")
    public String descripcionSubCategoria;
    private String codUnidadMedidaSunat;
    private int idUnidadMedida;
    private int idAreaProduccion;
    @SerializedName("control_peso_activo")
    private boolean bControlPeso;
    private boolean pVentaLibre;
    @SerializedName("uso_tiempo_activo")
    public boolean bControlTiempo;
    private boolean bSeModificoImagen;
    @SerializedName("es_combo_simple")
    public boolean ComboSimple;
    @SerializedName("monto_modifica")
    private BigDecimal MontoModifica;
    private int IdTipoModifica;
    public int factorModificacion;
    @SerializedName("visibleTipoModificacionPack")
    public boolean VisibleTipoModificacionPack;
    private boolean  ProductoUnico;

    public boolean isVisibleTipoModificacionPack() {
        return VisibleTipoModificacionPack;
    }

    public void setVisibleTipoModificacionPack(boolean visibleTipoModificacionPack) {
        VisibleTipoModificacionPack = visibleTipoModificacionPack;
    }
    public boolean isProductoUnico() {
        return ProductoUnico;
    }

    public void setProductoUnico(boolean productoUnico) {
        ProductoUnico = productoUnico;
    }

    public int getFactorModificacion() {
        return factorModificacion;
    }

    public void setFactorModificacion(int factorModificacion) {
        this.factorModificacion = factorModificacion;
    }

    public BigDecimal getMontoModificacionPack() {

        return MontoModifica.multiply(new BigDecimal(factorModificacion));

    }

    public BigDecimal getMontoModifica() {
        return MontoModifica;
    }

    public void setMontoModifica(BigDecimal montoModifica) {
        MontoModifica = montoModifica;
    }

    public int getIdTipoModifica() {
        return IdTipoModifica;
    }

    public void setIdTipoModifica(int idTipoModifica) {
        IdTipoModifica = idTipoModifica;
    }

    public BigDecimal getDCantidadMaximaPedido() {
        return DCantidadMaximaPedido;
    }

    public void setDCantidadMaximaPedido(BigDecimal DCantidadMaximaPedido) {
        this.DCantidadMaximaPedido = DCantidadMaximaPedido;
    }

    private BigDecimal DCantidadMaximaPedido;


    public boolean isbSeModificoImagen() {
        return bSeModificoImagen;
    }

    public void setbSeModificoImagen(boolean bSeModificoImagen) {
        this.bSeModificoImagen = bSeModificoImagen;
    }


    public boolean isbVisibleWeb() {
        return bVisibleWeb;
    }

    public void setbVisibleWeb(boolean bVisibleWeb) {
        this.bVisibleWeb = bVisibleWeb;
    }


    public boolean isComboSimple() {
        return ComboSimple;
    }

    public void setComboSimple(boolean comboSimple) {
        ComboSimple = comboSimple;
    }


    public mProduct(String cKey, String cProductName, float dQuantity, float dSalesPrice, byte[] bImage) {

        this.cKey = cKey;
        this.cProductName = cProductName;
        this.dQuantity = dQuantity;
        this.dSalesPrice = dSalesPrice;
        this.bImage = bImage;
    }

    public mProduct(int idProduct, String cProductName,
                    BigDecimal precioCompra, String codigoBarra, boolean estadoVariante) {
        this.idProduct = idProduct;
        this.cProductName = cProductName;
        this.precioCompra = precioCompra;
        this.codigoBarra = codigoBarra;
        this.estadoVariante = estadoVariante;

    }

    public String getDescripcionCategoria() {
        return DescripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        DescripcionCategoria = descripcionCategoria;
    }

    public mProduct() {
        ProductoUnico=false;
        DCantidadMaximaPedido = new BigDecimal(0);
        idAreaProduccion = 0;
        bDetallePack = false;
        numItem = 0;
        idPack = 0;
        tipoPack = false;
        descripcionVariante = "";
        tipoNormal = false;
        numVariantes = 0;
        idCategoria = 0;
        estadoActivo = "";
        estadoVisible = "";
        controlPeso = false;
        controlStock = false;
        codigoBarra = "";
        stockDisponible = new BigDecimal(0);
        stockReserva = new BigDecimal(0);
        codigoColor = "";
        codigoForma = "";
        unidadMedida = "";
        tipoRepresentacionImagen = 0;
        esFavorito = false;
        this.idProduct = 0;
        this.cProductName = "";
        this.cKey = "";
        this.dQuantityReserve = 0;
        this.dQuantity = 0;
        this.dPurcharsePrice = 0;
        this.dSalesPrice = 0;
        this.cAdditionalInformation = "";
        this.cUnit = "";
        this.bImage = null;
        this.bControlTiempo = false;
        this.precioCompra = new BigDecimal(0);
        this.precioVenta = new BigDecimal(0);
        this.idProductImage = 0;
        this.estadoVariante = false;
        opcionVarianteList = new ArrayList<>();
        varianteList = new ArrayList<>();
        metodoGuardado = 0;
        estadoModificador = false;
        modificadores = "";
        cantidadReserva = 0;
        priceProductList = new ArrayList<>();
        multiplePVenta = false;
        DescripcionCategoria = "";
        idSubCategoria = 0;
        descripcionSubCategoria = "";
        codUnidadMedidaSunat = "";
        idUnidadMedida = 0;
        bControlPeso = false;
        pVentaLibre = false;
        bVisibleWeb = false;
        descripcionVariante = "";
        MontoModifica = new BigDecimal(0);
        IdTipoModifica = 0;
        idProduct=0;
    }


    public byte getMetodoGuardado() {
        return metodoGuardado;
    }

    public void setMetodoGuardado(byte metodoGuardado) {
        this.metodoGuardado = metodoGuardado;
    }

    public int getNumItem() {
        return numItem;
    }

    public void setNumItem(int numItem) {
        this.numItem = numItem;
    }

    public int getIdPack() {
        return idPack;
    }

    public void setIdPack(int idPack) {
        this.idPack = idPack;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getDescripcionVariante() {
        return descripcionVariante;
    }

    public void setDescripcionVariante(String descripcionVariante) {
        this.descripcionVariante = descripcionVariante;
    }

    public boolean isbControlPeso() {
        return bControlPeso;
    }

    public boolean ispVentaLibre() {
        return pVentaLibre;
    }

    public void setpVentaLibre(boolean pVentaLibre) {
        this.pVentaLibre = pVentaLibre;
    }

    public void setbControlPeso(boolean bControlPeso) {
        this.bControlPeso = bControlPeso;
    }

    public String getDescripcionSubCategoria() {
        return descripcionSubCategoria;
    }

    public void setDescripcionSubCategoria(String descripcionSubCategoria) {
        this.descripcionSubCategoria = descripcionSubCategoria;
    }

    public String getCodUnidadMedidaSunat() {
        return codUnidadMedidaSunat;
    }

    public void setCodUnidadMedidaSunat(String codUnidadMedidaSunat) {
        this.codUnidadMedidaSunat = codUnidadMedidaSunat;
    }

    public void setEstadoActivo(String estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public void setEstadoVisible(String estadoVisible) {
        this.estadoVisible = estadoVisible;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getEstadoActivo() {
        return estadoActivo;
    }

    public String getEstadoVisible() {
        return estadoVisible;
    }

    public String getCodigoColor() {
        return codigoColor;
    }

    public void setCodigoColor(String codigoColor) {
        this.codigoColor = codigoColor;
    }

    public String getCodigoForma() {
        return codigoForma;
    }

    public boolean isbControlTiempo() {
        return bControlTiempo;
    }

    public void setbControlTiempo(boolean bControlTiempo) {
        this.bControlTiempo = bControlTiempo;
    }

    public void setCodigoForma(String codigoForma) {
        this.codigoForma = codigoForma;
    }

    public byte getTipoRepresentacionImagen() {
        return tipoRepresentacionImagen;
    }



    public void setTipoRepresentacionImagen(byte tipoRepresentacionImagen) {
        this.tipoRepresentacionImagen = tipoRepresentacionImagen;
    }

    public BigDecimal getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(BigDecimal stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public int getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(int idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public int getIdSubCategoria() {
        return idSubCategoria;
    }

    public void setIdSubCategoria(int idSubCategoria) {
        this.idSubCategoria = idSubCategoria;
    }

    public BigDecimal getStockReserva() {
        return stockReserva;
    }

    public void setStockReserva(BigDecimal stockReserva) {
        this.stockReserva = stockReserva;
    }

    public boolean isEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(boolean esFavorito) {
        this.esFavorito = esFavorito;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public boolean isControlPeso() {
        return controlPeso;
    }

    public void setControlPeso(boolean controlPeso) {
        this.controlPeso = controlPeso;
    }

    public boolean isControlStock() {
        return controlStock;
    }

    public void setControlStock(boolean controlStock) {
        this.controlStock = controlStock;
    }


    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public mProduct(int idProduct, String cKey, String cProductName, String cUnit, float dQuantity, float dQuantityReserve, float dPurcharsePrice, float dSalesPrice, String cAdditionalInformation, byte[] img) {

        this.idProduct = idProduct;
        this.cKey = cKey;
        this.cProductName = cProductName;
        this.cUnit = cUnit;
        this.dQuantity = dQuantity;
        this.dQuantityReserve = dQuantityReserve;
        this.dPurcharsePrice = dPurcharsePrice;
        this.dSalesPrice = dSalesPrice;
        this.cAdditionalInformation = cAdditionalInformation;
        this.bImage = img;

    }

    public mProduct(int idProduct, String cKey, String cProductName, String cUnit, float dQuantity, float dQuantityReserve, BigDecimal precioCompra, BigDecimal precioVenta, String cAdditionalInformation, byte[] bImage) {
        this.idProduct = idProduct;
        this.cKey = cKey;
        this.cProductName = cProductName;
        this.cUnit = cUnit;
        this.dQuantity = dQuantity;
        this.dQuantityReserve = dQuantityReserve;
        this.precioVenta = precioVenta;
        this.precioCompra = precioCompra;
        this.cAdditionalInformation = cAdditionalInformation;
        this.bImage = bImage;

    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public byte[] getbImage() {
        return bImage;
    }

    public void setbImage(byte[] imageData) {

        this.bImage = imageData;
    }


    public String getcKey() {
        return cKey;
    }

    public void setcKey(String cKey) {
        this.cKey = cKey;
    }

    public String getcProductName() {
        return cProductName;
    }

    public String getProductNamePrecioPedido(){
        return cProductName+"\n"+ DecimalControlKt.montoDecimalPrecioSimbolo( getPrecioVenta());
    }

    public String getProductNameInPack() {

        if (VisibleTipoModificacionPack) {

            String signo = "";
            switch (factorModificacion) {

                case 1:
                    signo = "+";
                    break;
                case -1:
                    signo = "-";
                    break;
                case 0:
                    signo="";
                    break;
            }

            if(factorModificacion!=0){
                return cProductName +"\n["+ signo+  DecimalControlKt.montoDecimalPrecioSimbolo(MontoModifica)+"]";
            }else{
                return cProductName;
            }

        } else {
            return cProductName;
        }


    }

    public void setcProductName(String cProductName) {
        this.cProductName = cProductName;
    }

    public String getcUnit() {
        return cUnit;
    }

    public void setcUnit(String cUnit) {
        this.cUnit = cUnit;
    }

    public float getdQuantity() {
        return dQuantity;
    }

    public void setdQuantity(float dQuantity) {
        this.dQuantity = dQuantity;
    }

    public float getdQuantityReserve() {
        return dQuantityReserve;
    }

    public void setdQuantityReserve(float dQuantityReserve) {
        this.dQuantityReserve = dQuantityReserve;
    }

    public float getdPurcharsePrice() {
        return dPurcharsePrice;
    }

    public void setdPurcharsePrice(float dPurcharsePrice) {
        this.dPurcharsePrice = dPurcharsePrice;
    }

    public float getdSalesPrice() {
        return dSalesPrice;
    }

    public void setdSalesPrice(float dSalesPrice) {
        this.dSalesPrice = dSalesPrice;
    }

    public String getcAdditionalInformation() {
        return cAdditionalInformation;
    }

    public void setcAdditionalInformation(String cAdditionalInformation) {
        this.cAdditionalInformation = cAdditionalInformation;
    }

    @Override
    public String toString() {
        final String nuevaLinea = System.getProperty("line.separator");
        return " " + cKey + nuevaLinea + " " + cProductName;
    }


    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public String getPrecioVentaTexto(){
        return DecimalControlKt.montoDecimalPrecioSimbolo(precioVenta);
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getObservacionProducto() {
        return observacionProducto;
    }

    public void setObservacionProducto(String observacionProducto) {
        this.observacionProducto = observacionProducto;
    }

    public boolean isEstadoVariante() {
        return estadoVariante;
    }

    public void setEstadoVariante(boolean estadoVariante) {
        this.estadoVariante = estadoVariante;
    }

    public int getIdProductImage() {
        return idProductImage;
    }

    public void setIdProductImage(int idProductImage) {
        this.idProductImage = idProductImage;
    }

    public List<OpcionVariante> getOpcionVarianteList() {
        return opcionVarianteList;
    }

    public void setOpcionVarianteList(List<OpcionVariante> opcionVarianteList) {

        this.opcionVarianteList = opcionVarianteList;
    }

    public int getNumVariantes() {
        return numVariantes;
    }

    public void setNumVariantes(int numVariantes) {
        this.numVariantes = numVariantes;
    }

    public List<Variante> getVarianteList() {
        return varianteList;
    }

    public void setVarianteList(List<Variante> varianteList) {
        this.varianteList = varianteList;
    }

    public boolean isTipoNormal() {
        return tipoNormal;
    }

    public void setTipoNormal(boolean tipoNormal) {
        this.tipoNormal = tipoNormal;
    }

    public boolean isTipoPack() {
        return tipoPack;
    }

    public void setTipoPack(boolean tipoPack) {
        this.tipoPack = tipoPack;
    }

    public boolean isbDetallePack() {
        return bDetallePack;
    }

    public void setbDetallePack(boolean bDetallePack) {
        this.bDetallePack = bDetallePack;
    }

    public boolean isEstadoModificador() {
        return estadoModificador;
    }

    public void setEstadoModificador(boolean estadoModificador) {
        this.estadoModificador = estadoModificador;
    }

    public String getModificadores() {
        return modificadores;
    }

    public List<AdditionalPriceProduct> getPriceProductList() {
        return priceProductList;
    }

    public void setPriceProductList(List<AdditionalPriceProduct> priceProductList) {
        this.priceProductList = priceProductList;
    }

    public boolean isMultiplePVenta() {
        return multiplePVenta;
    }

    public void setMultiplePVenta(boolean multiplePVenta) {
        this.multiplePVenta = multiplePVenta;
    }

    public void setModificadores(String modificadores) {
        this.modificadores = modificadores;
    }


    public float getCantidadReserva() {
        return cantidadReserva;
    }

    public void setCantidadReserva(float cantidadReserva) {
        this.cantidadReserva = cantidadReserva;
    }

    public int getIdAreaProduccion() {
        return idAreaProduccion;
    }

    public void setIdAreaProduccion(int idAreaProduccion) {
        this.idAreaProduccion = idAreaProduccion;
    }

    public String getNombreCompletoProducto() {
        return this.cProductName + " " + this.descripcionVariante;
    }

    public BigDecimal getPrecioUnitarioItemPack(){
        return precioVenta.add(MontoModifica.multiply(new BigDecimal(factorModificacion)));
    }
}
