package com.omarchdev.smartqsale.smartqsaleventas.Constantes;

import android.provider.BaseColumns;

import com.omarchdev.smartqsale.smartqsaleventas.Model.Control1Cliente;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Control2Cliente;
import com.omarchdev.smartqsale.smartqsaleventas.Model.HorarioTerminal;
import com.omarchdev.smartqsale.smartqsaleventas.Model.TipoModificadorPack;
import com.omarchdev.smartqsale.smartqsaleventas.Model.UrlAccess;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mDocPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mMedioPago;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTienda;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipoAtencion;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mTipoDocumento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 23/09/2017.
 */

public abstract class Constantes {


    public static abstract class MovAlmacen {
        public final static String IngresoIniInventario = "INI";
        public final static String IngresoCompra = "INC";
        public final static String IngresoTransferencia = "INT";
        public final static String IngresoAjusInventario = "INA";
        public final static String SalidaTransferencia = "SAT";
        public final static String SalidaCaducidad = "SAC";
        public final static String SalidadDevolucionPorveedor = "SAD";
        public final static String SalidaAjusteInventario = "SAJ";
        public final static String IngresoCancelacionVenta = "ICV";
        public final static String SalidaVentas = "SAV";
    }

    public static abstract class ProcesosPantalla {
        public final static int Venta = 1;
        public final static int AdministrarPedido = 2;
        public final static int Cobrar = 3;
        public final static int FujoCaja = 4;
        public final static int Reportes = 5;
        public final static int AdministracionProductos = 6;
        public final static int ConfigAdvProducto = 7;
        public final static int AdminClientes = 8;
        public final static int AdminVendedores = 9;
        public final static int CuentaCorrienteCliente = 10;
        public final static int AnularPedido = 11;
        public final static int HistorialVenta = 12;
        public final static int Descuento = 13;
        public final static int CancelarVenta = 14;
        public final static int AgregarProductoDP = 15; //agregar producto a detalle pedido
        public final static int EliminarProductoDP = 16; //eliminar producto de detalle del pedido
        public final static int EditarCantidadProductoDP = 17;//editar cantidad de cada producto en el detalle del pedido
        public final static int AnularpedidoReserva = 18;
        public final static int RecuperarPedidoReserva = 19;
        public final static int ModificarZonaAtenciónPedidoReserva = 20;


    }

    public static abstract class ParamNameMap {

        public final static String MESSAGE_NOT = "mensaje_notificacion";
        public final static String NRO_PEDIDOS = "nro_pedidos_nuevo";
        public final static String TIENE_PEDIDO = "tienePedidosNuevos";
        public final static String ID_PEDIDO = "idPedidoUnico";

    }

    public static abstract class ZonaServicio {

        public final static String ZonaAutos = "Vehículo";
        public final static String ZonaServicio = "Zona de servicio";
    }

    public static abstract class infoUsuario {
        public static String EmailUsuario;
    }

    public static abstract class EstadoEditar {

        public final static String Editable = "Edt";
        public final static String noEditable = "NoEdt";

    }

    public static abstract class TipoModificadoresPack {
        public final static List<TipoModificadorPack> listadoModificadoresPack = new ArrayList<>();
    }

    public static abstract class Almacen {
        public static int idAlmacen;
    }

    public static abstract class Usuario {
        public static int idUsuario;
        public static boolean esAdministrador;
    }

    public static abstract class Tienda {
        public static int idTienda;
        public static String nombreTienda;
        public static boolean esPrincipal;
        public static boolean ZonasAtencion;
        public static String cTipoZonaServicio;
        public static int NumTiendas;
    }


    public static abstract class Etiquetas {

        public static String SaltoLinea = "%br%br%";

    }

    public static abstract  class TipoMovimientoApi{
        public static String DEFAULT="2";
    }

    public static abstract class Terminal {
        public static int idTerminal;
    }

    public static abstract class ConfigCompany {

        public static String tipoReplica;

    }

    public static abstract class MediosPago {

        public static List<mMedioPago> mediosPago;

    }

    public static abstract class DivisaPorDefecto {
        public static final String parametroSimbolo = "simbolo";
        public static String SimboloDivisa;
        public static int idDivisaSunat;
    }

    public static abstract class ValorPorDefecto {
        public static final String PinDefecto = "1234";
    }

    public static abstract class EHTML {

        public static final String Left = " %L%L%";
    }

    private static abstract class bdConstantesStart {

        private final static String url = "192.168.1.57";
        private final static String db = "DB_conexion";
        private final static String user = "omarch1409";
        private final static String password = "1409Chumioque";


    }

    public static abstract class UnidadesMedida {

        public static List<String> unidadesMedida = new ArrayList<>();
    }

    public static abstract class bdConstantesCreate {

        public static String url;
        public static String db;
        public static String user;
        public static String password;
    }

    public static abstract class bdConstantes {

        public static String url;
        public static String db;
        public static String user;
        public static String password;
    }


    public static abstract class UrlConnectionStart {
        public final static String urlConnection = "jdbc:jtds:sqlserver://" + bdConstantesStart.url + ";databaseName=" + bdConstantesStart.db + ";user=" + bdConstantesStart.user + ";password=" + bdConstantesStart.password + ";";
    }

    public static abstract class UrlConnectionCreate {
        public static String urlConnection;
    }

    public static abstract class EstadoConfiguracion {

        public final static int Editar = 1000000;
        public final static int Visualizar = 2000000;
        public final static int Nuevo = 3000000;

    }

    public static abstract class Licencia {

        public static int idLicencia;

    }

    public static abstract class UrlConnection {

        public static String urlConnection;
    }


    public static abstract class Empresa {
        public final static String CodigoEmpresa = "C0001";
        public static int idEmpresa;
        public static String nombrePropietario;
        public static String nombreTienda;
        public static String Razon_Social;
        public static String NumRuc;
        public static String cDireccion;
    }

    public static abstract class TipoPedidoWeb {

        public static String idTipoMovimiento = "2";

    }

    public static abstract class classForName {
        public final static String classForName = "net.sourceforge.jtds.jdbc.Driver";
    }

    public static abstract class ParametrosCliente {
        public final static String metodoGuardar = "cMetodoRealizar";
        public final static String metodoBuscar = "MetodoBusqueda";
        public final static int BusquedaPorId = 100;
        public final static int BusquedaPorNombreApellido = 101;
        public final static int TodosLosClientes = 102;
        public final static String idCliente = "iIdCliente";
        public final static String nuevoCliente = "sp_NuevoCliente";
        public final static String editarCliente = "sp_EditarCliente";
        public final static String nombreUnoCliente = "cPrimerNombre";

        public final static String apellidoPaterno = "cApellidoPaterno";
        public final static String apellidoMaterno = "cApellidoMaterno";
        public final static String numeroTelefono = "cNumeroTelefono";
        public final static String email = "cEmail";
        public final static String direccion = "cDireccion";
    }

    public static abstract class ParametrosVendedor {

        public final static String IdVendedor = "iIdVendedor";
        public final static String NombreUnoVendedor = "cPrimerNombre";
        public final static String NombreDosVendedor = "cSegundoNombre";
        public final static String ApellidoPaterno = "cApellidoPaterno";
        public final static String ApellidoMaterno = "cApellidoMaterno";
        public final static String Comision = "dComision";
        public final static String MetodoBusqueda = "MetodoBusqueda";
        public final static String parametro = "ParametroBusqueda";
        public final static int BusquedaPorId = 107;
        public final static int BusquedaPorNombre = 109;
        public final static int TodosVendedores = 105;

    }

    public static abstract class storedProcedure {

        public final static String sp_getCliente = "sp_buscarCliente";
        public final static String sp_InsertarCliente = "sp_ingresarEditarCliente";
        public final static String sp_getVendedor = "sp_BuscarVendedor";

        public final static String insertProduct = "sp_insertProduct";
        public final static String insertProduct2 = "sp_insertProduct_v7";
        public final static String insertImageProduct = "sp_IngresarImagen";
        public final static String checkloginUser = "sp_checkLoginUser";
        public final static String updateProduct = "sp_editProduct";

    }

    public static abstract class SimboloMoneda {
        public final static String moneda = "S/";
    }


    public static abstract class TFacturacion {
        public static final String cActFactura = "F02";
        public static final String cNuFactura = "F01";
        public static final String cMobileSoftPeru = "F03";
    }


    public static abstract class ConfigTienda {

        public static boolean nombreConCategoria;
        public static boolean tieneAreaDespacho;
        public static boolean precioConIgv;
        public static boolean bUsa_Facturacion;
        public static int idTipoZonaServicio;

        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////
        ///////////////////////////////////////////

        public static boolean CabeceraPieTicketAdicional;
        public static String CodeFacturacion;
        public static String ContenidoPieTicketAdicional;
        public static int EspaciosPieTicketAdicional;
        public static String LinkTicket;
        public static boolean ObtenerControlClientes;
        public static boolean bCategoriaImpresion;
        public static boolean bPieTicketAdicional;
        public static boolean bUsaTipoAtencion;
        //   public static boolean bUsa_Facturacion;
        public static boolean bVentaCredito;
        //   public static int idTipoZonaServicio;
        //   public static boolean nombreConCategoria;
        public static boolean pagoUnico;
        //   public static boolean precioConIgv;
        //    public static boolean tieneAreaDespacho;
        public static boolean usaPromocion;
        public static boolean visibleBusquedaAvanzadaCliente;
        public static boolean visibleNumDocumento;
        public static int idDocumentoPagoDefecto;
        public static boolean bUsaFechaEntrega;

        public static boolean bImprimePagosPrecuenta;
        public static boolean bImprimePrecuentaAutomatica;
        public static String cMensajePrecuenta;
        public static boolean bCategoriaDefecto;
        public static int idCategoriaDefecto;
        public static String cLinkBaseWeb;
        public static String cLinkAddPedidoNuevo;
        public static String cLinkAddPedidoExistente;
        public static String cLinkAddPedidoConsulta;
        public static int iTiempoLecturaPedido;
        public static boolean bUsaAdelantoPagoPedido;
        public static String cTipoPedidoDefault;
        public static String cTipoPantallaPedido;
        public static String cConfiguracionPantallaPedido;
        public static boolean bVisibleBtnCambioPantalla;
        public static boolean bUsaAforo;
    }

    public static abstract class ConfiguracionPantallaPedido {

        public static final String PantallaNormal = "01";
        public static final String PantallaVehiculo = "02";


    }

    public static abstract class PieImpresion {


        public static String pieFactura;
        public static String pieBoleta;
        public static String pieNotaVenta;

    }

    public static abstract class IGV {

        public static BigDecimal valorIgv;

    }

    public static abstract class TipoPantallaPedido {

        public final static String NORMAL = "01";
        public final static String VEHICULO = "02";

    }

    public static abstract class TipoPedido {
        public final static String NORMAL = "01";
        public final static String CONTRACTO = "02";
        public final static String VEHICULO1 = "03";
    }

    public static abstract class Parametros {
        public final static String parameterBusqueda = "cParametroBusqueda";
        public final static String parameterUser = "cCodigoUsuario";
        public final static String parameterPassword = "cPasswordUser";
        public final static String parametercodigoCompania = "cCodigoEmpresa";
        public final static String parameterExisteUsuario = "existe";
        public final static String parameterCodigoProducto = "cCodigoProducto";
        public final static String parameterIdProducto = "iIdProducto";
        public final static String parameterNombreProducto = "cNombreProducto";
        public final static String parameterPrecioVenta = "dPrecioVenta";
        public final static String parameterPrecioCompra = "dPrecioCompra";
        public final static String parameterCantidadProducto = "dCantidadProducto";
        public final static String parameterCantidadReservaProducto = "dCantidadReservaProducto";
        public final static String parameterUnidadProducto = "cUnidadProducto";
        public final static String parameterInformacionAdicionalProducto = "cInformacionAdicionalProducto";
        public final static String parameterImagen = "@bImagen";

    }

    public static abstract class AccesUser {

        public final static String messageAccesOk = "accessOk";
        public final static String messageAccesDenied = "accessDenied";
    }

    public static abstract class TIPOZONASERVICIO {
        public static int DELIVERY=400;
        public static int MESAS=300;
    }

    public static abstract class DBSQLITE_Database {

        public final static int DATABASE_VERSION = 4;
        public final static String DATABASE_NAME = "MiNegocioDemo.db";
    }

    public static abstract class HorariosTerminal {

        public final static List<HorarioTerminal> list = new ArrayList<>();
    }

    public static abstract class NumSerieTerminal {

        public static String NumSerie;

    }

    public static abstract class ParamActivitys {

        public static String PARAM_IDPEDIDO = "idCabeceraPedido";
        public static String PARAM_ESTADO_PEDIDO_PAGADO = "EstadoPagado";

    }

    public static abstract class TiposDocPago {

        public static List<mDocPago> listaTipoDocPago;

    }

    public static abstract class PATH {

        public static String DIRFILE;

    }

    public static abstract class TxtName {

        public static final String txtConfigTerminal = "conTTIME.txt";

    }


    public static abstract class DBSQLITE_Usuario implements BaseColumns {

        public static final String TABLE_NAME = "Usuario";
        public static final String User_Name = "userName";
        public static final String User_Password = "userPassword";
        public static final String TABLE_DEVICE_BLUETOOTH = "DeviceAddress";
        public static final String TABLE_OPTION_PRINT = "PrintDefault";


    }

    public static abstract class TransactionDbSqlLite {

        public static final String ColumnaPuerto = "puerto";
        public static final String Create_Table_User = "CREATE TABLE "
                + DBSQLITE_Usuario.TABLE_NAME + " ("
                + DBSQLITE_Usuario._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBSQLITE_Usuario.User_Name + " TEXT NOT NULL,"
                + DBSQLITE_Usuario.User_Password + " TEXT NOT NULL)";

        public static final String Create_Table_Device = "CREATE TABLE DeviceAddress ( cDeviceAddress TEXT NOT NULL UNIQUE)";

        public static final String Create_Table_Print_Default = "Create Table PrintDefault (cNamePrint TEXT NOT NULL UNIQUE)";

        public static final String Create_Table_User_Register = "CREATE TABLE "
                + Usuario_Registro.TablaUsuarioRegistrado +
                "(" + Usuario_Registro.Email + " TEXT NOT NULL UNIQUE," + Usuario_Registro.Contrasena + " TEXT NOT NULL UNIQUE)";
        public static final String Create_Table_Rol_Acceso = "CREATE TABLE " +
                TABLE_Acceso_Usuario.Tabla_Acceso_Usuario + "(" + TABLE_Acceso_Usuario.Proceso_ID + " INTEGER not null," + TABLE_Acceso_Usuario.EstadoAcceso + " NUMERIC not null)";
    }

    public static abstract class TABLE_Acceso_Usuario {
        public static final String Proceso_ID = "IdProceso";
        public static final String EstadoAcceso = "bAcceso";
        public static final String Tabla_Acceso_Usuario = "Rol_Acceso";

    }

    public static abstract class Usuario_Registro {
        public static final String TablaUsuarioRegistrado = "Usuario_Registrado";
        public static final String Email = "Email_User";
        public static final String Contrasena = "Contrasena_User";

    }

    public static abstract class EstadoProducto {

        public static final String EditarProducto = "EditarProducto";
        public static final String NuevoProducto = "NuevoProducto";
        public static final String EstadoProducto = "EstadoProducto";
    }

    public static abstract class ReporteVendedores {
        public static final int reporteTodosVendedores = 111111;
        public static final int reportePorVendedor = 222222;
    }

    public static abstract class TipoImpresionReporte {
        public static final int ImpPdf = 10000000;
        public static final int ImpBluetooth = 20000000;
    }

    public static abstract class EstadosAppLoguin {

        public static final byte EstadoD = 20;
        public static final byte EstadoL = 40;
        public static final byte EstadoDN = 10;
        public static final byte EstadoLN = 30;
        public static final byte EstadoI = 5;
        public static final byte EstadoNC = 0;
        public static final byte EstadoNoConnect = 98;
        public static final byte EstadoErrorProc = 99;

    }

    public static abstract class EstadoApp {
        public static final long actualizado = 563968561168L;
        public static final long noactualizado = 563968845192L;
        public static final long errorVerificar = 99;
        public static final long errorConexion = 98;
    }

    public static abstract class Contacto {
        public static String numContacto;
        public static final String COM_WHATSAPP = "com.whatsapp";
    }
    //HOLA
    //HOLA
    public static abstract class ResultadoPinReset {
        public static final int PermitirReinicio = 71528619;
        public static final int NoReinicio = 25739320;
        public static final int errorVerificar = 99;
        public static final int errorConexion = 98;
    }


    public static abstract class TiposDocumentoId {


        public static List<mTipoDocumento> listadoDocumentos;

    }


    public static abstract class MontosSunat {

        public final static BigDecimal montoMaximoLibre = new BigDecimal(700);
    }

    public static abstract class TokenFactura {
        public static String tokenEnvio;
        public static String rutaApi;
        public static String tokenP2;
    }

    public static abstract class TipoDocumentoIdentidad {

        public final static int DNI = 100;
        public final static int RUC = 102;
        public final static int VARIOS = 106;

    }

    public static abstract class Links {
        public static List<UrlAccess> links;
    }

    public static abstract class TipoDocumentoPago {
        public final static int BOLETA = 1003;
        public final static int FACTURA = 1001;
        public final static int NOTAVENTA = 1012;
    }


    public static abstract class NumeroDigitosFactura80mm {

        public final static int Descripcion = 16;
        public final static int PrecioU = 8;
        public final static int PrecioT = 8;
    }

    public static abstract class Tiendas {
        public static List<mTienda> tiendaList = new ArrayList<>();
    }

    public static abstract class EstadosPedidoColor {
        public static final String Guardado = "#7993FF87";
        public static final String Nuevo = "#FFFFFF";
    }

    public static abstract class BASECONN {
        public static final String TIPO_CONSULTA = "2";
        public static final String BASE_URL = "http://ventas.mbsoftperu.com/App/";
        public static final String BASE_URL_API = "http://192.99.154.9:8064/";
        //public static final String BASE_URL_API = "http://161.132.38.45:8081/";
       // public static final String BASE_URL_API = "http://192.168.1.7:8003/";


     //    http://161.132.38.45:8081/

        //public static final String BASE_URL_API = "http://192.99.154.9:8024/";
      //   public static final String BASE_URL_API = "http://192.99.154.9:8022/";




        /*

     //   public static final String BASE_URL_API = "http://192.99.154.9:8022/";
     public static final String BASE_URL_API = "http://http://192.168.56.1/:8003/";

       * */

        //public static final String BASE_URL_API = "http://192.168.0.171:8002/";
        //public static final String BASE_URL_API = "http://192.168.0.37:8060/";
        //public static final String BASE_URL_API = "http://192.168.1.37:8060/";
        //public static final String BASE_URL_API = "http://192.168.0.175:8060/";

        // public static final String BASE_URL_API = "https://localhost:44317/";
    }


    public static abstract class ControlCliente {
        public static List<Control1Cliente> control1Clientes;
        public static List<Control2Cliente> control2Clientes;
    }

    public static abstract class TiposAtencion {
        public static List<mTipoAtencion> lista;
    }


    public static abstract class TiposAnulacionDocElectronico {

        public final static int Anulacion = 200;
        public final static int GenerarNota = 300;

    }

}















