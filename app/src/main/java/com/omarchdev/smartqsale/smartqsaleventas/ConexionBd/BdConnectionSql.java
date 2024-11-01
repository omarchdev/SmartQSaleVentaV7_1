package com.omarchdev.smartqsale.smartqsaleventas.ConexionBd;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by OMAR CHH on 23/09/2017.
 */

public class BdConnectionSql {

    // private ResultSet rs;
    private String nombreRed;
    private static BdConnectionSql bdConnectionSql;
    Connection conn;
    Context context;


    DbHelper helper;
    private boolean seConectoVentas;

    static {
        try {
            Class.forName(Constantes.classForName.classForName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final String SelectPVFT = "select cv.iId_Tienda,FORMAT(cast(cv.FechaVentaRealizada-'5:00' as date),'dd/MM/yyyy')" +
            ",isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''), " +
            " p.cProductName,dv.cDescripcion_Variante,sum(dv.dPrecio_Subtotal) as Total_Monto_Venta, " +
            " sum (dv.iCantidad) as unidad_ventas,sum(dMontoDescuento_Igv),sum(d_Igv),dv.cDescripcion_combo,p.ckey" +
            ",sum(dv.dPrecio_Costo*dv.iCantidad ) as PrecioCosto," +
            " sum(dv.dPrecio_Subtotal)-sum(dv.dPrecio_Costo*dv.iCantidad ) as utilidad" +
            " from Detalle_Venta as dv " +
            " inner join Product as p on dv.iIdProducto=p.iIdProduct " +
            " left join Categoria_Productos as c on " +
            " p.id_Categoria=c.id_categoria_producto and c.id_company=? " +
            " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria and " +
            " sc.id_Company=?  inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta ";
    final String wherePVFT = " where dv.id_Company=? and dv.bDetallePack=0 and dv.cEstadoDetalleVenta='A' and " +
            " dv.cEliminado='A' and p.iIdCompany=? and " +
            " cv.FechaVentaRealizada-'5:00' between ? and ? ";
    final String whereTiendaPVFT = " and cv.iId_Tienda=?";

    public boolean isSeConectoVentas() {
        return seConectoVentas;
    }

    public void setSeConectoVentas(boolean seConectoVentas) {
        this.seConectoVentas = seConectoVentas;
    }

    public String getNombreRed() {
        return nombreRed;
    }

    public void setNombreRed(String nombreRed) {
        this.nombreRed = nombreRed;
    }

    int idLockProc;
    final String groupByPVFT = " group by  cv.iId_Tienda,cast(cv.FechaVentaRealizada-'5:00' as date) " +
            " ,dv.iIdProducto,p.ckey,isnull(c.cDescripcion_categoria,'General') " +
            " ,isnull(sc.c_Descripcion_SubCategoria,''),p.cProductName,dv.cDescripcion_combo," +
            " dv.cDescripcion_Variante,p.cDescripcion_Unidad  " +
            " order by cv.iId_Tienda,cast(cv.FechaVentaRealizada-'5:00' as date),isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,'') desc";
    final String selectPDP = " select cv.iId_Tienda,tp.cDescripcion_Visible,count(cv.iId_Cabecera_Venta)," +
            " sum(cv.dTotal_Neto_Venta) from Cabecera_Venta as cv inner join " +
            " Tipo_Documento_Pago as tp on cv.id_TipoDocumentoPago=tp.iIdTipoDocumento ";
    final String wherePDP = "where iId_Company=? and fechaVentaRealizada-'5:00' between ? and ? " +
            "and cEstadoVenta='N' and cEliminado='' ";

    public static BdConnectionSql getSinglentonInstance() {

        if (bdConnectionSql == null) {
            bdConnectionSql = new BdConnectionSql();

        }

        return bdConnectionSql;
    }

    final String whereTiendaPDP = " and  iId_Tienda=?  ";
    final String groupPDP = "  group by cv.iid_tienda,tp.cDescripcion_Visible";
    final String selectPPT = "select cv.iId_Tienda,mp.cDescripcion_Medio_pago," +
            "sum(p.d_monto_pagado) from Cabecera_Venta as cv " +
            " inner join pagos as p on cv.iId_Cabecera_venta=p.id_Cabecera_venta " +
            " inner join Medio_Pago as mp on p.id_medio_pago=mp.iId_Medio_Pago ";
    final String wherePPT = " where cv.iId_Company=? and  cv.cEstadoVenta='N'  " +
            " and cv.cEliminado='' and  cv.fechaventarealizada-'5:00' between ? and ? ";
    final String whereTiendaPPT = " and cv.iId_Tienda=? ";
    final String groupPPT = " group by cv.iId_Tienda,mp.cDescripcion_Medio_pago ";
    final String selectMC = "select c.id_tienda,mc.iTipo_registro," +
            " mir.cDescripcion_Motivo,sum(mMonto) from Movimiento_Caja as mc" +
            " inner join Motivo_Ingreso_retiro as mir " +
            " on mc.id_motivo=mir.id_motivo " +
            " inner join cierres as c on " +
            " c.id_cierre=mc.id_cierre_caja";
    final String whereMC = " where mc.id_company=? and dFecha_Movimiento between ? and ? ";
    final String whereTiendaMC = " AND c.id_tienda=? ";
    final String groupMC = " group by  c.id_tienda,mc.iTipo_registro,mir.cDescripcion_Motivo ";
    final String selectTPT = " select iId_tienda,sum(dv.dPrecio_Subtotal)," +
            "sum(dv.d_Igv),sum(dv.dPrecio_Subtotal-dv.d_Igv-(dv.dPrecio_Costo*dv.iCantidad))" +
            ",sum(dv.dPrecio_Costo*DV.iCantidad) " +
            "from Cabecera_Venta as cv inner join detalle_venta as dv" +
            " on cv.iId_Cabecera_Venta=dv.iIdCabecera_venta";
    final String whereTPT = " where cv.iId_Company=? and cv.cEstadoVenta='N' " +
            " and cv.cEliminado='' and dv.bDetallePack=0 and cv.FechaVentaRealizada-'5:00' between ? and ? ";
    final String whereTiendaTPT = " and cv.iId_Tienda=? ";
    final String groupTPT = " GROUP BY cv.iId_tienda ";
    private final String horaInicio = "00:00:00";
    private final String horaFinal = "23:59:59";
    private Connection connectionInfra = null;

    public BdConnectionSql() {

        nombreRed = "";
        seConectoVentas = false;
    }

    private static Connection getConnectionStart() {//Establece conexion con base de datos start
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        try {
            Class.forName(Constantes.classForName.classForName).newInstance();
            conn = DriverManager.getConnection(Constantes.UrlConnectionStart.urlConnection);


        } catch (Exception e) {
            e.printStackTrace();
            conn = null;
        }
        return conn;
    }

    private static Connection getConnectionCreate() {//Establece conexion con base de datos Create
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        try {
            Class.forName(Constantes.classForName.classForName).newInstance();
            conn = DriverManager.getConnection(Constantes.UrlConnectionCreate.urlConnection);

        } catch (Exception e) {
            e.printStackTrace();
            conn = null;
        }
        return conn;
    }

    private static Connection getConnection() {//Establece conexion con base de datos
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        try {
            Class.forName(Constantes.classForName.classForName).newInstance();
            conn = DriverManager.getConnection(Constantes.UrlConnection.urlConnection);

        } catch (Exception e) {
            e.printStackTrace();
            conn = null;
        }
        return conn;
    }

    public Connection getConnectionVentas() {
        return conn;
    }


    Connection STARTCONN;

    public void setConnectionVentasFin() {
        try {

            if (conn != null) {
                conn.close();
                conn = null;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void finalizar() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setContext1(Context context) {
        this.context = context;
    }

    public boolean VerificarConexion() {
        Connection con = getConnection();
        if (con != null) {
            return true;
        } else {

            return false;
        }
    }

    public void setContext(Context context) {

        this.context = context;
        helper = new DbHelper(context);

    }

    public Connection getCon() {
        return getConnection();
    }

    public void CloseCon(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void OpenConnection(Connection con) {

    }

    public mTienda ActualizarTienda(mTienda t) {
        PreparedStatement ps = null;
        Connection con = getConnectionCreate();

        try {
            ps = con.prepareStatement(" update tienda set cDescripcion_Tienda=? where iIdCompany=? and iIdTienda=?");
            ps.setString(1, t.getNombreTienda());
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, t.getIdTienda());
            ps.execute();
            Constantes.Tiendas.tiendaList.clear();
            Constantes.Tiendas.tiendaList.addAll(ObtenerTiendasCompania(con));

            con.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            t = new mTienda();
            t.setIdTienda(-99);
        }


        return t;
    }

    public Promocion EstadoPromocionPedido() {
        Promocion p = new Promocion();
        try {
            CallableStatement cs = this.conn.prepareCall("call sp_AgregarPromocionEnPedido(?,?,?,?,?,?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.registerOutParameter(3, 4);
            cs.registerOutParameter(4, 12);
            cs.registerOutParameter(5, 3);
            cs.registerOutParameter(6, 12);
            cs.registerOutParameter(7, 3);
            cs.registerOutParameter(8, -7);
            cs.execute();
            p.setIdPromocion(cs.getInt(3));
            p.setDescripcion(cs.getString(6));
            p.setTipoPromocion(cs.getString(4));
            p.setMontoDescuento(cs.getBigDecimal(5));
            p.setTieneLimite(cs.getBoolean(8));
            p.setMontoLimite(cs.getBigDecimal(7));
        } catch (SQLException e) {
            e.printStackTrace();
            p.setIdPromocion(-99);
        }
        return p;
    }

    public mTienda GuardarTienda(mTienda m) {
        PreparedStatement ps = null;
        Connection con = getConnectionCreate();

        try {
            ps = con.prepareStatement("insert into tienda(cDescripcion_tienda,cCodigo_tienda,iIdCompany) values(?,?,?)");
            ps.setString(1, m.getNombreTienda());
            ps.setString(2, "");
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.execute();
            Constantes.Tiendas.tiendaList.clear();
            Constantes.Tiendas.tiendaList.addAll(ObtenerTiendasCompania(con));
            con.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            m = new mTienda();
            m.setIdTienda(-99);
        }
        return m;
    }

    public mTienda ObtenerTiendaId(int idTienda) {

        mTienda m = new mTienda();
        PreparedStatement ps = null;
        Connection con = getConnectionCreate();
        try {
            ps = con.prepareStatement("select iIdTienda,cDescripcion_Tienda," +
                    "cEstado from tienda where iIdCompany=? and iIdTienda=?");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, idTienda);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                m.setIdTienda(rs.getInt(1));
                m.setNombreTienda(rs.getString(2));
                m.setCEstado(rs.getString(3));


            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return m;
    }

    private List<mTienda> ObtenerTiendasCompania(Connection con) {
        List<mTienda> listaTiendas = new ArrayList<>();
        PreparedStatement ps = null;
        mTienda tienda;
        ResultSet rs;

        try {
            ps = con.prepareStatement("select iIdTienda,cDescripcion_Tienda " +
                    " from TiendaCompany where iIdCompany=? ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.execute();
            rs = ps.getResultSet();
            Constantes.Tiendas.tiendaList.clear();
            while (rs.next()) {

                tienda = new mTienda();
                tienda.setIdTienda(rs.getInt(1));
                tienda.setNombreTienda(rs.getString(2));
                Constantes.Tiendas.tiendaList.add(tienda);
                //  listaTiendas.add(tienda);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            listaTiendas = new ArrayList<>();
            tienda = new mTienda();
            tienda.setIdTienda(-99);
            listaTiendas.add(tienda);
        }


        return listaTiendas;
    }

    public String ObtenerConfigCompany(Connection conn) {

        PreparedStatement ps = null;
        String respuesta = "";

        try {
            ps = conn.prepareStatement("select cTipoReplicaProducto from Configuracion_Company where idCompany=? ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                Constantes.ConfigCompany.tipoReplica = rs.getString(1);
                respuesta = Constantes.ConfigCompany.tipoReplica;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Constantes.ConfigCompany.tipoReplica = "";
        }

        return respuesta;
    }

    public void Demo() {

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("select 1");
            ps.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public LogUserResult LoginEmpresaConnection(String user, String pass) {

        LogUserResult log = new LogUserResult();
        connectionInfra = getConnectionCreate();
        CallableStatement cs = null;

        if (connectionInfra != null) {

            try {
                cs = connectionInfra.prepareCall("call sp_login_user_licencia_Principal_v6(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                cs.setString(1, user);
                cs.setString(2, pass);
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.registerOutParameter(4, Types.VARCHAR);
                cs.registerOutParameter(5, Types.VARCHAR);
                cs.registerOutParameter(6, Types.VARCHAR);
                cs.registerOutParameter(7, Types.VARCHAR);
                cs.registerOutParameter(8, Types.VARCHAR);
                cs.registerOutParameter(9, Types.INTEGER);
                cs.registerOutParameter(10, Types.VARCHAR);
                cs.registerOutParameter(11, Types.VARCHAR);
                cs.registerOutParameter(12, Types.VARCHAR);
                cs.registerOutParameter(13, Types.VARCHAR);
                cs.registerOutParameter(14, Types.BIT);
                cs.registerOutParameter(15, Types.INTEGER);
                cs.registerOutParameter(16, Types.VARCHAR);
                cs.registerOutParameter(17, Types.INTEGER);
                cs.registerOutParameter(18, Types.INTEGER);
                cs.registerOutParameter(19, Types.INTEGER);
                cs.registerOutParameter(20, Types.VARCHAR);
                cs.execute();

                ResultSet rs = cs.getResultSet();
                if (rs != null) {
                    Constantes.Tiendas.tiendaList.clear();
                    while (rs.next()) {

                        mTienda tienda = new mTienda();
                        tienda.setIdTienda(rs.getInt(1));
                        tienda.setNombreTienda(rs.getString(2));
                        Constantes.Tiendas.tiendaList.add(tienda);
                        //  listaTiendas.add(tienda);
                    }
                    rs.close();

                }

                log.setNombreTienda(cs.getString(3));
                log.setRazonSocial(cs.getString(4));
                log.setDireccion(cs.getString(5));
                log.setNumRuc(cs.getString(6));
                log.setNombreRegistro(cs.getString(7));
                log.setNombreComercial(cs.getString(8));
                log.setIdCompany(cs.getInt(9));
                log.setHost(cs.getString(10));
                log.setDbName(cs.getString(11));
                log.setUserDbName(cs.getString(12));
                log.setPassDbName(cs.getString(13));
                log.setEstadoCredencial(cs.getBoolean(14));
                log.setCodeResult(cs.getInt(15));
                log.setMensajeResult(cs.getString(16));
                // log.setIdTienda(cs.getInt(17));
                log.setNumTiendas(cs.getInt(18));
                log.setIdLincencia(cs.getInt(19));

                if (log.getCodeResult() == 200) {
                    Constantes.Empresa.idEmpresa = log.getIdCompany();
                    if (log.getNumTiendas() > 1) {

//                       ObtenerTiendasCompania(connectionInfra);
                    } else {

                        /*if(log.getCodeResult()==200) {
                            List<mTienda> listaTiendas = new ArrayList<>();
                            mTienda t = new mTienda();
                            t.setIdTienda(cs.getInt(17));
                            t.setNombreTienda(cs.getString(20));
                            listaTiendas.add(t);
                            Constantes.Tiendas.tiendaList = listaTiendas;
                        }*/
                    }
                }

  /*              if(log.getNumTiendas()>1){
                    if(log.getCodeResult()==200) {
                        Constantes.Empresa.idEmpresa=log.getIdCompany();
                        Constantes.Tiendas.tiendaList = ObtenerTiendasCompania(connectionInfra);
                    }
                }else{
                    if(log.getCodeResult()==200){
                    List<mTienda> listaTiendas=new ArrayList<>();
                    mTienda t=new mTienda();
                    t.setIdTienda(cs.getInt(17));
                    t.setNombreTienda(cs.getString(20));
                    listaTiendas.add(t);
                    Constantes.Tiendas.tiendaList=listaTiendas;
                    }
                }
*/
            } catch (SQLException e) {
                log = new LogUserResult();
                e.printStackTrace();
                log.setCodeResult(0);
                log.setMensajeResult("Existe un inconveniente al conectarse. Verifique su conexión a internet");
                log.setEstadoCredencial(false);
            } finally {
                try {
                    cs.close();
                    // connLoguin.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.setCodeResult(0);
            log.setMensajeResult("Existe un inconveniente al conectarse. Verifique su conexión a internet");
            log.setEstadoCredencial(false);
        }
        return log;
    }

    public byte ValidarUsuarioConnection(String usuario, String password) {

        Connection connLoguin = getConnectionCreate();
        CallableStatement cs = null;
        boolean permitir = false;
        byte respuesta = 0;
        byte respuestaCadena = 0;
        if (connLoguin != null) {
            try {
                cs = connLoguin.prepareCall("call sp_login_user_licencia_v2(?,?,?,?,?,?,?,?,?,?,?,?)");
                cs.setString(1, usuario);
                cs.setString(2, password);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.registerOutParameter(4, Types.INTEGER);
                cs.registerOutParameter(5, Types.TINYINT);
                cs.registerOutParameter(6, Types.INTEGER);
                cs.registerOutParameter(7, Types.VARCHAR);
                cs.registerOutParameter(8, Types.VARCHAR);
                cs.registerOutParameter(9, Types.VARCHAR);
                cs.registerOutParameter(10, Types.VARCHAR);
                cs.registerOutParameter(11, Types.VARCHAR);
                cs.registerOutParameter(12, Types.VARCHAR);
                cs.execute();
                respuesta = cs.getByte(5);
                if (Constantes.EstadosAppLoguin.EstadoD == respuesta) {
                    permitir = true;

                } else if (Constantes.EstadosAppLoguin.EstadoL == respuesta) {
                    permitir = true;

                }

                if (permitir) {
                    Constantes.Empresa.idEmpresa = cs.getInt(4);
                    Constantes.Licencia.idLicencia = cs.getInt(3);
                    //  Constantes.Tienda.idTienda=cs.getInt(6);
                    Constantes.Empresa.nombreTienda = cs.getString(7);
                    //  Constantes.Empresa.nombrePropietario =cs.getString(8);
                    Constantes.Empresa.NumRuc = cs.getString(10);
                    Constantes.Empresa.Razon_Social = cs.getString(11);
                    Constantes.Empresa.cDireccion = cs.getString(12);
                    respuestaCadena = getStringConnectionAppUser(connLoguin);
                    Constantes.Tiendas.tiendaList = ObtenerTiendasCompania(connLoguin);
                    GenerarAreaPorDefecto();
                    if (respuestaCadena == 99) {
                        respuesta = 99;
                    } else if (respuestaCadena == 98) {
                        respuesta = 98;
                    }
                }
                cs.close();
                if (respuesta != 0) {
                }
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    connLoguin.close();
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } else {
            respuesta = 98;
        }
        return respuesta;

    }

    public Connection ConectarInstanciaPrincipal() {
        this.conn = null;
        this.conn = getConnection();
        return null;

    }

    private byte getStringConnectionAppUser(Connection con) {

        PreparedStatement ps = null;
        byte respuestaCreacionConfig = 0;
        byte respuesta = 0;
        boolean respuestaObtenerConfig = false;
        boolean respuestaObtenerTiposDoc = false;
        if (con != null) {
            try {
                ps = con.prepareStatement("select [cNombreServidor],[cBaseDatos],[cUsuario] ,[cClave]" +
                        " from Licencia_Servidor where idLicencia=?");
                ps.setInt(1, Constantes.Licencia.idLicencia);
                ps.execute();
                ResultSet rs = ps.getResultSet();

                while (rs.next()) {

                    Constantes.bdConstantes.url = rs.getString(1);
                    Constantes.bdConstantes.user = rs.getString(3);
                    Constantes.bdConstantes.db = rs.getString(2);
                    Constantes.bdConstantes.password = rs.getString(4);
                }

                Constantes.UrlConnection.urlConnection = "jdbc:jtds:sqlserver://" + Constantes.bdConstantes.url +
                        ";databaseName=" + Constantes.bdConstantes.db + ";user=" + Constantes.bdConstantes.user + ";password=" + Constantes.bdConstantes.password + ";";

                this.conn = getConnection();

                respuesta = 100;
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }
        return respuesta;
    }

    public int VerificarExistePedido() {
        CallableStatement cs = null;
        int id = 0;
        int a = 1;
        try {
            cs = conn.prepareCall("{call sp_get_existe_pedido_usuario_terminal(?,?,?,?)}");

            cs.setInt("iIdUser", Constantes.Usuario.idUsuario);
            cs.setInt("iIdCompany", Constantes.Empresa.idEmpresa);
            cs.setInt("iIdTienda", Constantes.Tienda.idTienda);
            cs.setInt("iIdTerminal", Constantes.Terminal.idTerminal);
            cs.execute();

            ResultSet resultSet = cs.getResultSet();
            while (resultSet.next()) {
                id = resultSet.getInt("IdCabeceraPedido");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    private void ObtenerIgv() {

        PreparedStatement ps = null;
        ResultSet rs = null;
        if (conn != null) {

            try {
                ps = conn.prepareStatement("select valor_igv from igv where id_valor=1");
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    Constantes.IGV.valorIgv = rs.getBigDecimal(1);
                    Constantes.IGV.valorIgv.toString();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    private boolean ObtenerConfiguracionEmpresa() {
        PreparedStatement ps = null;
        boolean respuesta = false;
        Connection connection = this.conn;
        if (connection != null) {
            try {
                ps = connection.prepareStatement("select ct.bTiene_Despacho, ct.bNombresCompuesto, " +
                        " ct.bPrecioConIgv, ct.bUsa_Facturacion, isnull(ct.cPieNotaVenta,''), isnull(ct.cPieFactura,''), " +
                        " isnull(ct.cPieBoleta,''), ct.bUsaZonaServicio, ct.idTipo_Zona_Servicio, tz.cTipoZonaServicio, " +
                        "bVisibleNumDocumento, bVisibleBusquedaAvanzadaCliente, " +
                        "bObtenerControlClientes, bPagoUnicoActivo,bUsaPromocion, [bUsaTipoAtencion],isnull(f.cCodigoFacturador,'NNN'), " +
                        "bCabeceraPedidoEnPieTicketPedido, cContenidoPieTicketPedido,  bAdiccionalPieTicketPedido," +
                        " rtrim(ltrim(cLinkTicketImpresion)), cCategoriaImpresion, bVentaCredito    " +
                        "from Configuracion_GeneralTienda as ct   inner join TipoZonaServicio as tz on  ct.idTipo_Zona_Servicio=TZ.idTipoZonaServicio " +
                        "  left join Facturador as f on f.idFacturador=ct.idTipoFacturacion  where ct.iIdCompany=? and ct.iIdTienda=?  ");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    Constantes.ConfigTienda.tieneAreaDespacho = rs.getBoolean(1);
                    Constantes.ConfigTienda.nombreConCategoria = rs.getBoolean(2);
                    Constantes.ConfigTienda.precioConIgv = rs.getBoolean(3);
                    Constantes.ConfigTienda.bUsa_Facturacion = rs.getBoolean(4);
                    Constantes.PieImpresion.pieNotaVenta = rs.getString(5);
                    Constantes.PieImpresion.pieFactura = rs.getString(6);
                    Constantes.PieImpresion.pieBoleta = rs.getString(7);
                    Constantes.Tienda.ZonasAtencion = rs.getBoolean(8);
                    Constantes.ConfigTienda.idTipoZonaServicio = rs.getInt(9);
                    Constantes.Tienda.cTipoZonaServicio = rs.getString(10);
                    Constantes.ConfigTienda.visibleNumDocumento = rs.getBoolean(11);
                    Constantes.ConfigTienda.visibleBusquedaAvanzadaCliente = rs.getBoolean(12);
                    Constantes.ConfigTienda.ObtenerControlClientes = rs.getBoolean(13);
                    Constantes.ConfigTienda.pagoUnico = rs.getBoolean(14);
                    Constantes.ConfigTienda.usaPromocion = rs.getBoolean(15);
                    Constantes.ConfigTienda.bUsaTipoAtencion = rs.getBoolean(16);
                    Constantes.ConfigTienda.CodeFacturacion = rs.getString(17);
                    Constantes.ConfigTienda.CabeceraPieTicketAdicional = rs.getBoolean(18);
                    Constantes.ConfigTienda.ContenidoPieTicketAdicional = rs.getString(19);
                    Constantes.ConfigTienda.bPieTicketAdicional = rs.getBoolean(20);
                    Constantes.ConfigTienda.LinkTicket = rs.getString(21);
                    Constantes.ConfigTienda.bCategoriaImpresion = rs.getBoolean(22);
                    Constantes.ConfigTienda.bVentaCredito = rs.getBoolean(23);
                    respuesta = true;
                }
                if (Constantes.ConfigTienda.ObtenerControlClientes) {
                    FiltrosClientes();
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (rs != null) {
                    ps.close();
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
                respuesta = false;
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (null != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e3) {
                        e3.printStackTrace();
                    }
                }
                if (null != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            respuesta = false;
        }
        return respuesta;
    }

    private byte GenerarConfiguracionEmpresa() {
        byte respuesta = 0;
        CallableStatement cs = null;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_generar_configuracion_compania(?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.registerOutParameter(3, Types.TINYINT);
                cs.setByte(3, respuesta);
                cs.execute();
                respuesta = cs.getByte(3);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            respuesta = 99;
        }

        return respuesta;
    }

    public ConnectStartOp StarApp(int version, String packageApp) {


        STARTCONN = getConnectionStart();
        ConnectStartOp c = new ConnectStartOp();
        CallableStatement cs = null;

        try {
            cs = STARTCONN.prepareCall("call sp_start_app(" + ParamStoreProcedure(12) + ")");
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.VARCHAR);
            cs.setInt(5, version);
            cs.setString(6, packageApp);
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.registerOutParameter(10, Types.VARCHAR);
            cs.registerOutParameter(11, Types.INTEGER);
            cs.registerOutParameter(12, Types.VARCHAR);

            cs.execute();
            c.setHost(cs.getString(1));
            c.setBase(cs.getString(2));
            c.setUser(cs.getString(3));
            c.setConnp(cs.getString(4).replace("\r", ""));
            c.setLink(cs.getString(7));
            c.setName(cs.getString(8));
            c.setExtend(cs.getString(9));
            c.setTitleWork(cs.getString(10));
            c.setCodeResult(cs.getInt(11));
            c.setMessageResult(cs.getString(12));

        } catch (SQLException e) {
            e.printStackTrace();
            c = new ConnectStartOp();
            c.setCodeResult(99);
            c.setMessageResult("Hubo un problema al momento de conectarse." +
                    "Verifique su  conexión a internet.");
        }
        return c;
    }

    @Deprecated
    public void GetStringConnectionStart() {
        STARTCONN = getConnectionStart();
        PreparedStatement ps = null;
        String conexion = Constantes.UrlConnectionStart.urlConnection;
        conexion.length();

        try {
            ps = STARTCONN.prepareStatement("select cServidor,cBase,cUsuario,cPass from Conecta where id_conexion=3");

            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                conexion = Constantes.bdConstantesCreate.url = rs.getString(1);

                conexion.length();
                Constantes.bdConstantesCreate.url = rs.getString(1).trim();
                Constantes.bdConstantesCreate.db = rs.getString(2).trim();
                Constantes.bdConstantesCreate.user = rs.getString(3).trim();
                Constantes.bdConstantesCreate.password = rs.getString(4).trim();
                Constantes.UrlConnectionCreate.urlConnection = "jdbc:jtds:sqlserver://" + Constantes.bdConstantesCreate.url +
                        ";databaseName=" + Constantes.bdConstantesCreate.db + ";user=" + Constantes.bdConstantesCreate.user +
                        ";password=" + Constantes.bdConstantesCreate.password + ";";
            }

            conexion = Constantes.UrlConnectionCreate.urlConnection;
            conexion.length();
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            try {
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected BdConnectionSql clone() {

        try {
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<mVendedor> getVendedorTienda(int idTienda) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        mVendedor v;
        List<mVendedor> lista = new ArrayList<>();
        try {
            ps = conn.prepareStatement("select iIdVendedor,cPrimerNombre," +
                    "cApellidoPaterno,cApellidoMaterno from MaestroVendedor \n" +
                    "where iIdCompany=? and iIdTienda=? and cEstado_Eliminado=''\n");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, idTienda);
            ps.execute();
            rs = ps.getResultSet();

            while (rs.next()) {

                v = new mVendedor();
                v.setIdVendedor(rs.getInt(1));
                v.setPrimerNombre(rs.getString(2));
                v.setApellidoPaterno(rs.getString(3));
                v.setApellidoMaterno(rs.getString(4));
                lista.add(v);


            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = null;
        }
        return lista;
    }

    public List<mVendedor> getVendedor(int id, String parametro, int MetodoBusqueda) {
        List<mVendedor> list = new ArrayList<>();
        mVendedor vendedor = new mVendedor();
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = conn.prepareCall("{call " + Constantes.storedProcedure.sp_getVendedor + "(?,?,?,?,?) }");
            cs.setInt(1, id);
            cs.setInt(2, MetodoBusqueda);
            cs.setInt(3, Constantes.Empresa.idEmpresa);
            cs.setInt(4, Constantes.Tienda.idTienda);
            cs.setString(5, parametro);


            cs.execute();
            rs = cs.getResultSet();
            while (rs.next()) {
                vendedor = new mVendedor();
                vendedor.setIdVendedor(rs.getInt(Constantes.ParametrosVendedor.IdVendedor));
                vendedor.setPrimerNombre(rs.getString(Constantes.ParametrosVendedor.NombreUnoVendedor));
                vendedor.setApellidoPaterno(rs.getString(Constantes.ParametrosVendedor.ApellidoPaterno));
                vendedor.setApellidoMaterno(rs.getString(Constantes.ParametrosVendedor.ApellidoMaterno));
                list.add(vendedor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public byte EliminarProducto(int idProducto) {

        byte respuesta = 0;
        final String query = "call sp_eliminar_producto(?,?,?,?,?,?)";
        CallableStatement cs = null;
        if (conn != null) {
            try {
                cs = conn.prepareCall(query);
                cs.setInt(1, idProducto);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Tienda.idTienda);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setInt(5, Constantes.Terminal.idTerminal);
                cs.setByte(6, respuesta);
                cs.registerOutParameter(6, Types.TINYINT);
                cs.execute();
                respuesta = cs.getByte(6);


            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 0;
            }
        } else {
            respuesta = 0;
        }

        return respuesta;

    }

    public List<mCustomer> getClientesSp(String parametro, String control1, String control2) {
        List<mCustomer> customers = new ArrayList<>();
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call sp_busqueda_cliente_v1(?,?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setString(2, parametro);
            cs.setString(3, control1);
            cs.setString(4, control2);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            if (rs != null) {
                while (rs.next()) {
                    mCustomer customer = new mCustomer();
                    customer.setiId(rs.getInt(1));
                    customer.setTipoCliente(rs.getInt(2));
                    customer.setIdTipoDocumento(rs.getInt(3));
                    customer.setIdTipoDocSunat(rs.getInt(4));
                    customer.setNumeroRuc(rs.getString(5));
                    customer.setRazonSocial(rs.getString(6));
                    customer.setcName(rs.getString(7));
                    customer.setcApellidoPaterno(rs.getString(8));
                    customer.setcApellidoMaterno(rs.getString(9));
                    customer.setcEmail(rs.getString(10));
                    customer.setcDireccion(rs.getString(11));
                    customer.getTipoDocumento().setCDescripcionCorta(rs.getString(12));
                    customer.getTipoDocumento().setCColorDescripcion(rs.getString(13));
                    customer.setControl1(rs.getString(14));
                    //  customer.setControl2(rs.getString(15));
                    customers.add(customer);
                }
                rs.close();
            }
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            customers = new ArrayList<>();
        }


        return customers;
    }

    public List<mCustomer> getClientes(int id, String parametro, int metodoBusqueda, String control1, String control2) {

        List<mCustomer> customers = new ArrayList<>();
        mCustomer customer = new mCustomer();

        PreparedStatement cs = null;
        String query = "select top(50)mc.iIdCliente,mc.iTipoCliente,mc.id_Tipo_Documento," +
                " td.id_doc_sunat,mc.cNumeroRuc, mc.cRazonSocial,mc.cPrimerNombre,mc.cApellidoPaterno, " +
                " mc.cApellidoMaterno,mc.cEmail,mc.cDireccion," +
                " td.cDescripcionCorta,ltrim(rtrim(td.cColor_Descripcion))," +
                " isnull(cControl1,'')+' '+isnull(cControl2,'') " +
                " from maestroCliente as mc (nolock) inner join TipodeDocumento as td (nolock) " +
                " on mc.id_tipo_documento=td.iIdTipoDocumento where mc.iIdCompany=? and cEstado_Eliminado='' ";
        /*
        String query="select mc.iIdCliente,mc.iTipoCliente," +
                "mc.id_Tipo_Documento,td.id_doc_sunat,mc.cNumeroRuc, " +
                "mc.cRazonSocial,mc.cPrimerNombre,mc.cApellidoPaterno, " +
                "mc.cApellidoMaterno,mc.cEmail,mc.cDireccion, " +
                "td.cDescripcionCorta,ltrim(rtrim(td.cColor_Descripcion)) " +
                "from maestroCliente as mc inner join TipodeDocumento as td " +
                "on mc.id_tipo_documento=td.iIdTipoDocumento " +
                "where mc.iIdCompany=? and cEstado_Eliminado=''  ";*/
        String order = " order by mc.cRazonSocial ";
        String c1 = " and mc.cControl1=? ";
        String c2 = " and mc.cControl2=? ";
        String where = " and(mc.cRazonSocial like '%'+?+'%' or mc.cNumeroRuc like '%'+?+'%') ";


        String f = "";
        if (parametro.equals("")) {

            f = query;

            if (!control1.isEmpty()) {
                f = f + c1;
            }
            if (!control2.isEmpty()) {
                f = f + c2;
            }
            f = f + order;
        } else {
            f = query;
            if (!control1.isEmpty()) {
                f = f + c1;
            }
            if (!control2.isEmpty()) {
                f = f + c2;
            }
            f = f + where + order;
        }
        try {
            cs = conn.prepareStatement(f);

            if (parametro.equals("")) {
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                if (!control1.isEmpty()) {
                    cs.setString(2, control1);
                }
                if (!control2.isEmpty()) {
                    cs.setString(3, control2);
                }
            } else {
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                if (!control1.isEmpty()) {
                    cs.setString(2, control1);
                }
                if (!control2.isEmpty()) {
                    cs.setString(3, control2);
                }
                cs.setString(4, parametro);
                cs.setString(5, parametro);
            }
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while (rs.next()) {
                customer = new mCustomer();
                customer.setiId(rs.getInt(1));
                customer.setTipoCliente(rs.getInt(2));
                customer.setIdTipoDocumento(rs.getInt(3));
                customer.setIdTipoDocSunat(rs.getInt(4));
                customer.setNumeroRuc(rs.getString(5));
                customer.setRazonSocial(rs.getString(6));
                customer.setcName(rs.getString(7));
                customer.setcApellidoPaterno(rs.getString(8));
                customer.setcApellidoMaterno(rs.getString(9));
                customer.setcEmail(rs.getString(10));
                customer.setcDireccion(rs.getString(11));
                customer.getTipoDocumento().setCDescripcionCorta(rs.getString(12));
                customer.getTipoDocumento().setCColorDescripcion(rs.getString(13));
                customer.setControl1(rs.getString(14));
                //  customer.setControl2(rs.getString(15));
                customers.add(customer);

            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cs != null) {
                    cs.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return customers;
    }

    public byte EditarCliente(mCustomer cliente) {
        Connection con = conn;
        PreparedStatement ps = null;
        byte respuesta = 0;
        if (con != null) {
            try {
                ps = con.prepareStatement("UPDATE maestroCliente set cPrimerNombre=?,cApellidoPaterno=?,cApellidoMaterno=?,cDireccion=?,cEmail=?,cNumeroTelefono=? where iIdCompany=? and iIdCliente=?");
                ps.setString(1, cliente.getcName());
                ps.setString(2, cliente.getcApellidoPaterno());
                ps.setString(3, cliente.getcApellidoMaterno());
                ps.setString(4, cliente.getcDireccion());
                ps.setString(5, cliente.getcEmail());
                ps.setString(6, cliente.getcNumberPhone());
                ps.setInt(7, Constantes.Empresa.idEmpresa);
                ps.setInt(8, cliente.getiId());
                ps.execute();
                respuesta = 100;
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }
        return respuesta;
    }

    public byte ClienteInsertEdit(mCustomer customer, String metodoElegido) {
        CallableStatement cs = null;
        byte respuesta = 0;
        if (conn != null) {
            try {
                cs = conn.prepareCall("{call " + Constantes.storedProcedure.sp_InsertarCliente +
                        " (?,?,?,?,?,?,?,?,?,?,?,?,?) }");

                cs.setInt(1, customer.getiId());
                cs.setString(2, customer.getcName());
                cs.setString(3, customer.getcApellidoPaterno());
                cs.setString(4, customer.getcApellidoMaterno());
                cs.setString(5, customer.getcEmail());
                cs.setString(6, customer.getcNumberPhone());
                cs.setString(7, customer.getcDireccion());
                cs.setInt(8, Constantes.Empresa.idEmpresa);
                cs.setByte(9, respuesta);
                cs.registerOutParameter(9, Types.TINYINT);
                cs.setInt(10, customer.getTipoCliente());
                cs.setString(11, customer.getRazonSocial());
                cs.setString(12, customer.getNumeroRuc());
                cs.setInt(13, customer.getIdTipoDocumento());
                cs.execute();
                ResultSet set = cs.getResultSet();

                respuesta = cs.getByte(9);

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    if (cs != null) {
                        cs.close();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    respuesta = 99;
                }

            }
        } else {
            respuesta = 98;
        }

        return respuesta;
    }

    //Verificar si existe el codigo en los registro de product que corresponde a cada compañia
    public byte VerificarCodigoProducto(String codigoProducto) {

        PreparedStatement ps = null;
        Connection con = getConnection();
        byte respuesta = 0;
        int i = 0;
        String codigo = "";
        try {
            ps = con.prepareStatement("select [cKey] from Product where iIdCompany=? and id_tienda=? and cKey=? and cEliminado=''");
            ps.setString(3, codigoProducto);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(1, Constantes.Empresa.idEmpresa);

            ps.execute();

            ResultSet set = ps.getResultSet();

            while (set.next()) {
                codigo = set.getString(1);
                codigo.toString();
                i++;

            }


            if (i == 0) {
                respuesta = 0;
            } else if (i > 0) {
                respuesta = 1;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = 3;
        }
        return respuesta;

    }

    public String GenerarCodigoNumericoProducto() {

        CallableStatement cs = null;

        String codigo = "";
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_obtener_codigo_producto(?,?,?)");

                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.execute();
                codigo = cs.getString(3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {

        }
        return codigo;
    }

    public byte ActualizarEstadosProducto(mProduct product) {

        byte respuesta = 0;

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("update product set id_Area_Produccion=?," +
                    "bPrecioVariable=?,id_Unidad_Medida=? ,bUsaTiempo=? " +
                    " where iIdCompany=? and iIdProduct=?");
            ps.setInt(1, product.getIdAreaProduccion());
            ps.setBoolean(2, product.ispVentaLibre());
            ps.setInt(3, product.getIdUnidadMedida());
            ps.setBoolean(4, product.isbControlTiempo());
            ps.setInt(5, Constantes.Empresa.idEmpresa);
            ps.setInt(6, product.getIdProduct());
            ps.execute();
            respuesta = 100;
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = 99;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return respuesta;
    }

    public ResultProcces GuardarNuevoProducto(mProduct product) {
        int idProducto = 0;
        byte respuesta = 0;
        int codeResult = 0;
        String mensaje = "";
        PreparedStatement ps = null;
        CallableStatement cs = null;
        if (conn != null) {
            try {
                cs = conn.prepareCall("{call " + Constantes.storedProcedure.insertProduct2
                        + "(" + ParamStoreProcedure(39) + ")}");


                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Terminal.idTerminal);
                cs.setInt(3, Constantes.Usuario.idUsuario);
                cs.setString(4, product.getcKey());
                cs.setString(5, product.getCodigoBarra());
                cs.setString(6, product.getcProductName());
                cs.setString(7, " ");
                cs.setBigDecimal(8, product.getStockDisponible());
                cs.setBigDecimal(9, product.getStockReserva());
                cs.setBigDecimal(10, product.getPrecioVenta());
                cs.setBigDecimal(11, product.getPrecioCompra());
                cs.setString(12, product.getcAdditionalInformation());
                cs.setString(13, product.getObservacionProducto());
                cs.setString(14, product.getEstadoActivo());
                cs.setString(15, product.getEstadoVisible());
                cs.setBoolean(16, product.isEsFavorito());
                cs.setBoolean(17, product.isControlStock());
                cs.setBoolean(18, product.isControlPeso());
                cs.setInt(19, product.getIdCategoria());
                cs.setBytes(20, product.getbImage());
                cs.setByte(21, product.getTipoRepresentacionImagen());
                cs.setString(22, product.getCodigoColor());
                cs.setString(23, product.getCodigoForma());
                cs.registerOutParameter(24, Types.INTEGER);
                cs.setBoolean(25, false);
                cs.setBoolean(26, product.isTipoPack());
                cs.setInt(27, Constantes.Tienda.idTienda);
                cs.setString(28, product.getUnidadMedida());
                cs.setBoolean(29, product.isMultiplePVenta());
                cs.setInt(30, product.getIdSubCategoria());
                cs.setInt(31, product.getIdUnidadMedida());
                cs.setString(32, "O");
                cs.setInt(33, product.getIdAreaProduccion());
                cs.setBoolean(34, product.ispVentaLibre());
                cs.setBoolean(35, product.isbControlTiempo());
                cs.registerOutParameter(36, Types.INTEGER);
                cs.registerOutParameter(37, Types.VARCHAR);
                cs.setBoolean(38, product.isbVisibleWeb());
                cs.setBigDecimal(39, product.getDCantidadMaximaPedido());

                cs.execute();
                cs.clearParameters();
                codeResult = cs.getInt(36);
                mensaje = cs.getString(37);

                idProducto = cs.getInt(24);
                product.setIdProduct(idProducto);
                ActualizarEstadosProducto(product);
                respuesta=100;
                cs.close();
                if (codeResult == 200 && product.isMultiplePVenta()) {
                    respuesta = AgregarPreciosVentaAdiccionales(idProducto, product.getPriceProductList());
                }

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
                codeResult = 99;
                mensaje = "Hubo un inconveniente para guardar el producto.Por favor,verifique su conexión a internet";

            } finally {

                try {

                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            respuesta = 98;
        }

        return new ResultProcces(codeResult, mensaje);
        //     return respuesta;
    }

    public String checkLogBdUser(String userName, String userPassword) {
        String message = "";
        Connection con = getConnection();
        CallableStatement cs = null;


        try {
            cs = con.prepareCall("call " + Constantes.storedProcedure.checkloginUser + "(?,?,?,?)");

            cs.setString(Constantes.Parametros.parameterUser, userName);
            cs.setString(Constantes.Parametros.parameterPassword, userPassword);
            cs.setString(Constantes.Parametros.parametercodigoCompania, "C0001");
            cs.registerOutParameter(Constantes.Parametros.parameterExisteUsuario, Types.VARCHAR);
            cs.execute();

            message = cs.getString(Constantes.Parametros.parameterExisteUsuario);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return message;

    }

    @Deprecated
    public boolean saveImageProduct(mProduct product) {

        Connection con = getConnection();
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("call " + Constantes.storedProcedure.insertImageProduct + "(?,?,?)}");
            cs.setString(1, "C0001");
            cs.setString(2, product.getcKey());

            InputStream is = new ByteArrayInputStream(product.getbImage());
            cs.setBinaryStream(3, is, product.getbImage().length);

            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void CloseConnection(Connection connection) {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean LoguinUsuario() {
        Connection con = getConnection();
        PreparedStatement preparedStatement = null;
        if (con != null) {
            try {
                preparedStatement = con.prepareStatement("select iIdUser,iIdCompany from Maestro_Usuarios where cNameUser='SamuelChumioque'");
                preparedStatement.execute();

                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()) {

                    Constantes.Usuario.idUsuario = resultSet.getInt("iIdUser");
                    Constantes.Empresa.idEmpresa = resultSet.getInt("iIdCompany");

                }


            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {

                if (con != null) {
                    CloseConnection(con);
                    return true;
                } else {
                    return false;
                }

            }

        } else {
            return false;
        }

    }

    public boolean ObtenerEstadoPedido(int id) {

        boolean permitir = false;
        PreparedStatement ps;
        CallableStatement cs = null;
        String cEstadoPedido = "";
        if (conn != null) {

            try {

                cs = conn.prepareCall("call sp_verificar_estado_pedido_para_recpuerar_id(" + ParamStoreProcedure(4) + ")");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, id);
                cs.registerOutParameter(4, Types.BOOLEAN);
                cs.execute();
                permitir = cs.getBoolean(4);

            } catch (SQLException e) {
                e.printStackTrace();
                cEstadoPedido = "";
            }


        }
        return permitir;

    }

    public List<ProductoEnVenta> ReadResultSetDetallePedido(ResultSet rs) {
        List<ProductoEnVenta> list = new ArrayList<>();
        try {
            if (rs != null) {
                ProductoEnVenta productoEnVenta;
                while (rs.next()) {
                    productoEnVenta = new ProductoEnVenta();
                    productoEnVenta.setIdProducto(rs.getInt(1));
                    productoEnVenta.setEsVariante(rs.getBoolean(2));
                    productoEnVenta.setProductName(rs.getString(3));
                    productoEnVenta.setDescripcionVariante(rs.getString(4));
                    productoEnVenta.setItemNum(rs.getInt(5));
                    productoEnVenta.setCantidad(rs.getFloat(6));
                    productoEnVenta.setPrecioOriginal(rs.getBigDecimal(7));
                    productoEnVenta.setPrecioVentaFinal(rs.getBigDecimal(8));
                    productoEnVenta.setObservacion(rs.getString(9));
                    productoEnVenta.setEsPack(rs.getBoolean(10));
                    productoEnVenta.setEsDetallePack(rs.getBoolean(11));
                    productoEnVenta.setIdProductoPadre(rs.getInt(12));
                    productoEnVenta.setIdDetallePedido(rs.getInt(13));
                    productoEnVenta.setEsModificado(rs.getBoolean(14));
                    productoEnVenta.setDescripcionModificador(rs.getString(15));
                    productoEnVenta.setMontoDescuento(rs.getBigDecimal(16));
                    productoEnVenta.setUsaDescuento(rs.getBoolean(17));
                    productoEnVenta.setbPrecioVariable(rs.getBoolean(18));
                    productoEnVenta.setObservacionProducto(rs.getString(19));
                    productoEnVenta.setControlTiempo(rs.getBoolean(20));
                    productoEnVenta.setHoraInicio(rs.getString(21));
                    productoEnVenta.setHoraFinal(rs.getString(22));
                    productoEnVenta.setEstadoGuardado(rs.getString(23));
                    productoEnVenta.setEstadoEliminado(rs.getString(24));
                    productoEnVenta.setProductoUnico(rs.getBoolean(25));
                    productoEnVenta.setImage(rs.getBytes(26));
                    productoEnVenta.setITipoImagen(rs.getInt(27));
                    productoEnVenta.setCCodigoColor(rs.getString(28));
                    productoEnVenta.setCCodigoImagen(rs.getString(29));

                    productoEnVenta.getTiempoInicio().setTimestamp(rs.getTimestamp(30));
                    productoEnVenta.getTiempoFinal().setTimestamp(rs.getTimestamp(31));
                    list.add(productoEnVenta);

                }
                rs.close();
            }

        } catch (Exception ex) {
            list = new ArrayList<>();
        }

        return list;
    }
    //Busqueda de lista productos


    //API-PROCESO
    public List<ProductoEnVenta> ObtenerDetallePedidoPorId(int id) {
        List<ProductoEnVenta> list = new ArrayList<>();
        Connection con = conn;
        PreparedStatement ps = null;
        ProductoEnVenta productoEnVenta;

        CallableStatement cs = null;

        if (con != null) {
            try {

                cs = con.prepareCall("call sp_obtener_detalle_pedido_recuperado(" + ParamStoreProcedure(3) + ")");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, id);
                ResultSet rs = cs.executeQuery();
                list.addAll(ReadResultSetDetallePedido(rs));
         /*       if (rs != null) {
                    while (rs.next()) {
                        productoEnVenta = new ProductoEnVenta();
                        productoEnVenta.setIdProducto(rs.getInt(1));
                        productoEnVenta.setEsVariante(rs.getBoolean(2));
                        productoEnVenta.setProductName(rs.getString(3));
                        productoEnVenta.setDescripcionVariante(rs.getString(4));
                        productoEnVenta.setItemNum(rs.getInt(5));
                        productoEnVenta.setCantidad(rs.getFloat(6));
                        productoEnVenta.setPrecioOriginal(rs.getBigDecimal(7));
                        productoEnVenta.setPrecioVentaFinal(rs.getBigDecimal(8));
                        productoEnVenta.setObservacion(rs.getString(9));
                        productoEnVenta.setEsPack(rs.getBoolean(10));
                        productoEnVenta.setEsDetallePack(rs.getBoolean(11));
                        productoEnVenta.setIdProductoPadre(rs.getInt(12));
                        productoEnVenta.setIdDetallePedido(rs.getInt(13));
                        productoEnVenta.setEsModificado(rs.getBoolean(14));
                        productoEnVenta.setDescripcionModificador(rs.getString(15));
                        productoEnVenta.setMontoDescuento(rs.getBigDecimal(16));
                        productoEnVenta.setUsaDescuento(rs.getBoolean(17));
                        productoEnVenta.setbPrecioVariable(rs.getBoolean(18));
                        productoEnVenta.setObservacionProducto(rs.getString(19));
                        productoEnVenta.setControlTiempo(rs.getBoolean(20));
                        productoEnVenta.setHoraInicio(rs.getString(21));
                        productoEnVenta.setHoraFinal(rs.getString(22));
                        productoEnVenta.setEstadoGuardado(rs.getString(23));
                        productoEnVenta.setEstadoEliminado(rs.getString(24));
                        productoEnVenta.setProductoUnico(rs.getBoolean(25));
                        productoEnVenta.setImage(rs.getBytes(26));
                        productoEnVenta.setITipoImagen(rs.getInt(27));
                        productoEnVenta.setCCodigoColor(rs.getString(28));
                        productoEnVenta.setCCodigoImagen(rs.getString(29));

                        productoEnVenta.getTiempoInicio().setTimestamp(rs.getTimestamp(30));
                        productoEnVenta.getTiempoFinal().setTimestamp(rs.getTimestamp(31));
                        list.add(productoEnVenta);

                    }
                    rs.close();
                }

*/

            } catch (SQLException e) {

            } catch (Exception ex) {

                ex.toString();

            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public boolean GuardarDetallePedido(int id, ProductoEnVenta productoEnVenta, char metodo) {

        CallableStatement cs = null;


        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_guardar_producto_detalle_pedido(?,?,?,?,?,?,?,?)");
                cs.setInt("idCabeceraPedido", id);
                cs.setInt("numeroItem", productoEnVenta.getItemNum());
                cs.setInt("idProducto", productoEnVenta.getIdProducto());
                cs.setString("nombreProducto", productoEnVenta.getProductName());
                cs.setInt("cantidad", Math.round(productoEnVenta.getCantidad()));
                cs.setBigDecimal("precioUnidad", productoEnVenta.getPrecioOriginal());
                cs.setBigDecimal("subtotalUnidad", productoEnVenta.getPrecioVentaFinal());
                cs.setString("metodo", String.valueOf(metodo));
                cs.execute();
                cs.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {

            }
        }

        return false;

    }

    public int GuardarTotalPagoCabeceraPedido(int id, mCabeceraPedido cabeceraPedido) {

        int i = 0;
        int a = 0;
        PreparedStatement ps = null;
        final String query = "update Cabecera_Pedido set dValorBrutoVenta=?, cTipoDescuento=?," +
                "dDescuentoPedido=? ,dTotalVenta=? where iIdCompany=?  and iId_Cabecera_Pedido=? ";

        try {
            ps = conn.prepareStatement(query);
            ps.setBigDecimal(1, cabeceraPedido.getTotalBruto());
            ps.setBigDecimal(2, cabeceraPedido.getDescuentoPrecio());
            ps.setBigDecimal(3, cabeceraPedido.getTotalNeto());
            ps.setInt(4, id);
            ps.setInt(5, Constantes.Empresa.idEmpresa);
            i = ps.executeUpdate();
            a = i;
        } catch (SQLException e) {
            e.printStackTrace();
            i = 0;
        }
        return i;
    }

    public byte GuardarClienteCabeceraPedido(int id, mCustomer customer) {

        byte respuesta = 0;
        try {
            CallableStatement cs = null;
            cs = conn.prepareCall("call sp_guardar_cliente_cabecera_pedido(" + ParamStoreProcedure(8) + ")");
            cs.setInt("idCabeceraPedido", id);
            cs.setInt("idCliente", customer.getiId());
            cs.setString("NombreCliente", customer.getcName());
            cs.setInt("idTerminal", Constantes.Terminal.idTerminal);
            cs.setInt("idTienda", Constantes.Tienda.idTienda);
            cs.setInt("idCompany", Constantes.Empresa.idEmpresa);
            cs.setInt("idUser", Constantes.Usuario.idUsuario);
            cs.registerOutParameter("respuesta", Types.TINYINT);
            cs.execute();
            respuesta = cs.getByte("respuesta");
            cs.close();
        } catch (Exception ex) {
            respuesta = 99;
        }
        return respuesta;
    }

    public byte GuardarVendedorCabeceraPedido(int id, mVendedor vendedor) {
        CallableStatement cs = null;
        byte respuesta = 0;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_guardar_vendedor_cabecera_pedido(" + ParamStoreProcedure(8) + ")");
                cs.setInt("idCabeceraPedido", id);
                cs.setInt("idVendedor", vendedor.getIdVendedor());
                cs.setString("NombreVendedor", vendedor.getPrimerNombre());
                cs.setInt("idTerminal", Constantes.Terminal.idTerminal);
                cs.setInt("idTienda", Constantes.Tienda.idTienda);
                cs.setInt("idCompany", Constantes.Empresa.idEmpresa);
                cs.setInt("idUser", Constantes.Usuario.idUsuario);
                cs.registerOutParameter(8, Types.TINYINT);
                cs.execute();
                respuesta = cs.getByte(8);
                cs.close();
                ;
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {

            respuesta = 98;

        }
        return respuesta;
    }

    public byte GuardarCabeceraPedido(int id, mVendedor vendedor, mCustomer customer) {
        CallableStatement cs = null;
        byte respuesta = 0;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_guardar_cabecera_pedido " +
                        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                cs.setInt("idCabeceraPedido", id);
                cs.setInt("idVendedor", vendedor.getIdVendedor());
                cs.setString("NombreVendedor", vendedor.getPrimerNombre());
                cs.setInt("idCliente", customer.getiId());
                cs.setString("NombreCliente", customer.getcName());
                cs.setInt("idTerminal", Constantes.Terminal.idTerminal);
                cs.setInt("idTienda", Constantes.Tienda.idTienda);
                cs.setInt("idCompany", Constantes.Empresa.idEmpresa);
                cs.setInt("idUser", Constantes.Usuario.idUsuario);
                cs.setInt("fecha", GenerarFecha());
                cs.setByte(11, respuesta);
                cs.registerOutParameter(11, Types.TINYINT);
                cs.setInt(12, customer.getIdTipoDocSunat());
                cs.setString(13, customer.getNumeroRuc());
                cs.setString(14, customer.getcDireccion());
                cs.setInt(15, customer.getIdTipoDocumento());
                cs.execute();
                respuesta = cs.getByte(11);
                cs.close();
                ;
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {

            respuesta = 98;

        }
        return respuesta;
    }

    public byte GuardarDescuentoPedido(int idCabeceraPedido, BigDecimal montoDescuento, byte tipoDescuento) {

        byte respuesta = 0;
        CallableStatement cs = null;


        if (conn != null) {

            try {
                cs = conn.prepareCall("call sp_implemetar_descuento_pedido(?,?,?,?,?,?)");
                cs.setByte(1, tipoDescuento);
                cs.setBigDecimal(2, montoDescuento);
                cs.setInt(3, Constantes.Empresa.idEmpresa);
                cs.setInt(4, Constantes.Tienda.idTienda);
                cs.setInt(5, idCabeceraPedido);
                cs.setByte(6, respuesta);
                cs.registerOutParameter(6, Types.TINYINT);
                cs.execute();

                respuesta = cs.getByte(6);

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }


        } else {

            respuesta = 98;

        }

        return respuesta;

    }

    public mCabeceraPedido ObtenerCabeceraPedidoPagado(int id) {

        PreparedStatement ps = null;
        mCabeceraPedido pedido = null;
        try {
            ps = conn.prepareStatement(" select cv.iId_Cabecera_Pedido," +
                    "cv.cNombre_Cliente,isnull(cv.cNombre_Vendedor,'')," +
                    " format( cv.FechaVentaRealizada-'5:00','dd/MM/yyyy hh:mm tt')" +
                    ",isnull(cv.dDescuento_Venta,0),isnull(cv.dTotal_Gravado,0)" +
                    ",cv.dTotal_Neto_Venta,isnull(cv.iId_Cliente,0),cv.iId_Vendedor," +
                    "tp.cDescripcion_Visible+'    '+isnull(cv.cNumeroSerieDocumentoSunat,'')" +
                    "+' '+isnull(cv.cNumero_Correlativo,''),isnull(zs.id_zona_servicio,0), " +
                    " isnull(zs.cDescripcion_Zona,''),cp.cObservacion,isnull(cp.cIdentificador_pedido,''),  " +
                    " dbo.DescripcionZonaServicioOperarioV2" +
                    "(cp.id_zona_servicio,cp.iIdCompany,cp.iIdTienda,?,cp.iId_Cabecera_Pedido) " +
                    " from Cabecera_Venta as cv " +
                    " inner join Cabecera_Pedido as cp on cv.iId_Cabecera_Pedido=cp.iId_Cabecera_Pedido " +
                    " left join zonaSERVICIO as zs on cp.id_zona_servicio=zs.id_zona_servicio " +
                    "INNER JOIN Tipo_Documento_pago " +
                    "as tp on cv.id_TipoDocumentoPago=tp.iIdTipoDocumento " +
                    " where cv.iId_Company=? and cv.iId_Tienda=? " +
                    "  and cv.iId_Cabecera_Pedido=? ");
            ps.setInt(1, Constantes.ConfigTienda.idTipoZonaServicio);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Tienda.idTienda);
            ps.setInt(4, id);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                pedido = new mCabeceraPedido();
                pedido.setIdCabecera(rs.getInt(1));
                pedido.getCliente().setcName(rs.getString(2));
                pedido.setNombreVendedor(rs.getString(3));
                pedido.setFechaReserva(rs.getString(4));
                pedido.setDescuentoPrecio(rs.getBigDecimal(5));
                pedido.setTotalBruto(rs.getBigDecimal(6));
                pedido.setTotalNeto(rs.getBigDecimal(7));
                pedido.setIdCliente(rs.getInt(8));
                pedido.setIdVendedor(rs.getInt(9));
                pedido.setDocumentoPago(rs.getString(10));
                pedido.getZonaServicio().setIdZona(rs.getInt(11));
                pedido.getZonaServicio().setDescripcion(rs.getString(12));
                pedido.setObservacion(rs.getString(15) + " " + rs.getString(13));
                pedido.setIdentificadorPedido(rs.getString(14));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            pedido = new mCabeceraPedido();
        }

        return pedido;
    }

    public Pedido ObtenerPedidoId(int idPedido, boolean pagado) {

        CallableStatement cs = null;
        Pedido pedido = new Pedido();
        mCabeceraPedido cabeceraPedido = new mCabeceraPedido();
        List<ProductoEnVenta> list = new ArrayList<>();
        try {
            cs = conn.prepareCall("call sp_obtener_pedido_id_v6(" + ParamStoreProcedure(25) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.ConfigTienda.idTipoZonaServicio);
            cs.setInt(4, idPedido);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.registerOutParameter(10, Types.DECIMAL);
            cs.registerOutParameter(11, Types.DECIMAL);
            cs.registerOutParameter(12, Types.DECIMAL);
            cs.registerOutParameter(13, Types.VARCHAR);
            cs.registerOutParameter(14, Types.INTEGER);
            cs.registerOutParameter(15, Types.VARCHAR);
            cs.registerOutParameter(16, Types.VARCHAR);
            cs.registerOutParameter(17, Types.VARCHAR);
            cs.registerOutParameter(18, Types.VARCHAR);
            cs.setBoolean(19, pagado);
            cs.registerOutParameter(20, Types.VARCHAR);
            cs.registerOutParameter(21, Types.BOOLEAN);
            cs.registerOutParameter(22, Types.VARCHAR);
            cs.registerOutParameter(23, Types.INTEGER);
            cs.registerOutParameter(24, Types.VARCHAR);
            cs.registerOutParameter(25, Types.VARCHAR);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {

                    ProductoEnVenta productoEnVenta = new ProductoEnVenta();
                    productoEnVenta.setIdProducto(rs.getInt(1));
                    productoEnVenta.setEsVariante(rs.getBoolean(2));
                    productoEnVenta.setProductName(rs.getString(3));
                    productoEnVenta.setDescripcionVariante(rs.getString(4));
                    productoEnVenta.setItemNum(rs.getInt(5));
                    productoEnVenta.setCantidad(rs.getFloat(6));
                    productoEnVenta.setPrecioOriginal(rs.getBigDecimal(7));
                    productoEnVenta.setPrecioVentaFinal(rs.getBigDecimal(8));
                    productoEnVenta.setObservacion(rs.getString(9));
                    productoEnVenta.setEsPack(rs.getBoolean(10));
                    productoEnVenta.setEsDetallePack(rs.getBoolean(11));
                    productoEnVenta.setIdProductoPadre(rs.getInt(12));
                    productoEnVenta.setIdDetallePedido(rs.getInt(13));
                    productoEnVenta.setEsModificado(rs.getBoolean(14));
                    productoEnVenta.setDescripcionModificador(rs.getString(15));
                    productoEnVenta.setMontoDescuento(rs.getBigDecimal(16));
                    productoEnVenta.setUsaDescuento(rs.getBoolean(17));
                    productoEnVenta.setbPrecioVariable(rs.getBoolean(18));
                    productoEnVenta.setObservacionProducto(rs.getString(19));
                    productoEnVenta.setControlTiempo(rs.getBoolean(20));
                    productoEnVenta.setHoraInicio(rs.getString(21));
                    productoEnVenta.setHoraFinal(rs.getString(22));
                    productoEnVenta.setEstadoGuardado(rs.getString(23));
                    productoEnVenta.setEstadoEliminado(rs.getString(24));
                    productoEnVenta.setInformacionAdicionalTiempo(rs.getString(25));
                    list.add(productoEnVenta);
                }
            }

            cabeceraPedido.setIdCabecera(cs.getInt(4));
            cabeceraPedido.getCliente().setiId(cs.getInt(5));
            cabeceraPedido.getCliente().setRazonSocial(cs.getString(6));
            cabeceraPedido.getVendedor().setIdVendedor(cs.getInt(7));
            cabeceraPedido.getVendedor().setPrimerNombre(cs.getString(8));
            cabeceraPedido.setFechaReserva(cs.getString(9));
            cabeceraPedido.setDescuentoPrecio(cs.getBigDecimal(10));
            cabeceraPedido.setTotalBruto(cs.getBigDecimal(11));
            cabeceraPedido.setTotalNeto(cs.getBigDecimal(12));
            cabeceraPedido.setDocumentoPago(cs.getString(13));
            cabeceraPedido.getZonaServicio().setIdZona(cs.getInt(14));
            cabeceraPedido.getZonaServicio().setDescripcion(cs.getString(15));
            cabeceraPedido.setIdentificadorPedido(cs.getString(17));
            cabeceraPedido.setObservacion(cs.getString(16) + " " + cs.getString(18));
            cabeceraPedido.setFechaEntrega(cs.getString(20));
            cabeceraPedido.setbEntregado(cs.getBoolean(21));
            cabeceraPedido.getCliente().setNumeroRuc(cs.getString(22));
            cabeceraPedido.setcTipoPedido(cs.getString(24));
            cabeceraPedido.setNumPedido(cs.getString(25));
            pedido.setCabeceraPedido(cabeceraPedido);
            pedido.setListProducto(new ArrayList(list));
            pedido.setIdEntregaPedido(cs.getInt(23));

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            pedido = null;
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pedido;
    }

    public mCabeceraPedido getCabeceraPedidoIdReserva(int id) {

        mCabeceraPedido cabeceraPedido = null;
        PreparedStatement cs = null;

        final String query = "select " +
                "cp.iId_Cabecera_Pedido," +
                "cp.cIdentificador_Pedido,isnull(cp.cObservacion,'') as obs" +
                ",cp.iIdCliente,cp.cNombreCliente,cp.iIdVendedor," +
                "cp.cEstadoPermanencia,cp.cPrimerNombre as cNombreVendedor," +
                "dValorBrutoVenta,dDescuentoPedido,dTotalVenta," +
                "cp.DateKey,cp.cTipoDescuento,cp.dPorcentajeDescuento,isnull(cp.id_docCliente,0)," +
                "isnull(cp.id_TipoDocSunat,0),isnull(cp.cNumDocCliente,'')" +
                ",isnull(cp.cDireccionCliente,'')," +
                "rtrim(isnull(cp.cEmail,'')) ," +
                "td.cTipoDocumento,isnull(zs.id_zona_servicio,0)," +
                " ltrim(rtrim(isnull(zs.cDescripcion_Zona,'')))," +
                "isnull(format(dFechaGuardadoPedido-'5:00','dd/MM/yyyy hh:mm tt'),'NN')," +
                "dbo.DescripcionZonaServicioOperarioV2" +
                "(cp.id_zona_servicio,cp.iIdCompany,cp.iIdTienda,?,cp.iId_Cabecera_Pedido) " +
                " from Cabecera_Pedido as cp (nolock) left join TipodeDocumento as" +
                " td on cp.id_DocCliente=td.iIdTipoDocumento " +
                " left join zonaservicio as zs on zs.id_zona_servicio=cp.id_zona_servicio " +
                "where cp.iId_Cabecera_Pedido=? and cp.iIdCompany=?  and cp.iIdTienda=?";
        if (conn != null) {
            try {
                cs = conn.prepareStatement(query);
                cs.setInt(1, Constantes.ConfigTienda.idTipoZonaServicio);
                cs.setInt(2, id);
                cs.setInt(3, Constantes.Empresa.idEmpresa);
                cs.setInt(4, Constantes.Tienda.idTienda);
                cs.execute();

                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    cabeceraPedido = new mCabeceraPedido(
                            rs.getInt("iId_Cabecera_Pedido"),
                            rs.getString("cIdentificador_Pedido"),
                            rs.getString(24) + "\n" + rs.getString("obs"),
                            rs.getInt("iIdCliente"),
                            rs.getString("cNombreCliente"),
                            rs.getInt("iIdVendedor"),
                            rs.getString("cNombreVendedor"),
                            rs.getInt("DateKey"),
                            rs.getString("cEstadoPermanencia").charAt(0),
                            rs.getBigDecimal("dValorBrutoVenta"),
                            rs.getBigDecimal("dDescuentoPedido"),
                            rs.getBigDecimal("dTotalVenta"),
                            rs.getByte("cTipoDescuento"),
                            rs.getBigDecimal("dPorcentajeDescuento"));
                    cabeceraPedido.getCliente().setRazonSocial(rs.getString("cNombreCliente"));
                    cabeceraPedido.getCliente().setiId(rs.getInt(4));
                    cabeceraPedido.getCliente().setcName(rs.getString("cNombreCliente"));
                    cabeceraPedido.getCliente().setIdTipoDocumento(rs.getInt(15));
                    cabeceraPedido.getCliente().setIdTipoDocSunat(rs.getInt(16));
                    cabeceraPedido.getCliente().setNumeroRuc(rs.getString(17));
                    cabeceraPedido.getCliente().setcDireccion(rs.getString(18));
                    cabeceraPedido.getCliente().setcEmail(rs.getString(19));
                    cabeceraPedido.getCliente().setcTipoDocumento(rs.getString(20));
                    cabeceraPedido.getZonaServicio().setIdZona(rs.getInt(21));
                    cabeceraPedido.getZonaServicio().setDescripcion(rs.getString(22));
                    cabeceraPedido.setFechaReserva(rs.getString(23));

                }

            } catch (Exception e) {
                e.toString();
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        return cabeceraPedido;

    }

    public Pedido GetUltimoPedido() {

        CallableStatement cs = null;
        Pedido pedido = new Pedido();
        try {
            cs = conn.prepareCall("call sp_get_ultimo_pedido_company_terminal_v8(" + ParamStoreProcedure(34) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Terminal.idTerminal);
            cs.setInt(4, Constantes.Usuario.idUsuario);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.registerOutParameter(10, Types.INTEGER);
            cs.registerOutParameter(11, Types.CHAR);
            cs.registerOutParameter(12, Types.VARCHAR);
            cs.registerOutParameter(13, Types.DECIMAL);
            cs.registerOutParameter(14, Types.DECIMAL);
            cs.registerOutParameter(15, Types.DECIMAL);
            cs.registerOutParameter(16, Types.VARCHAR);
            cs.registerOutParameter(17, Types.INTEGER);
            cs.registerOutParameter(18, Types.DECIMAL);
            cs.registerOutParameter(19, Types.INTEGER);
            cs.registerOutParameter(20, Types.INTEGER);
            cs.registerOutParameter(21, Types.VARCHAR);
            cs.registerOutParameter(22, Types.VARCHAR);
            cs.registerOutParameter(23, Types.VARCHAR);
            cs.registerOutParameter(24, Types.INTEGER);
            cs.registerOutParameter(25, Types.INTEGER);
            cs.registerOutParameter(26, Types.VARCHAR);
            cs.registerOutParameter(27, Types.INTEGER);
            cs.registerOutParameter(28, Types.VARCHAR);
            cs.registerOutParameter(29, Types.BOOLEAN);
            cs.registerOutParameter(30, Types.VARCHAR);
            cs.registerOutParameter(31, Types.VARCHAR);
            cs.registerOutParameter(32, Types.BOOLEAN);
            cs.registerOutParameter(33, Types.VARCHAR);
            cs.registerOutParameter(34, Types.VARCHAR);
            ResultSet rs = cs.executeQuery();

            pedido.getListProducto().addAll(ReadResultSetDetallePedido(rs));
            /*if (rs != null)
                while (rs.next()) {

                    ProductoEnVenta productoEnVenta = new ProductoEnVenta();
                    productoEnVenta.setIdProducto(rs.getInt(1));
                    productoEnVenta.setEsVariante(rs.getBoolean(2));
                    productoEnVenta.setProductName(rs.getString(3));
                    productoEnVenta.setDescripcionVariante(rs.getString(4));
                    productoEnVenta.setItemNum(rs.getInt(5));
                    productoEnVenta.setCantidad(rs.getFloat(6));
                    productoEnVenta.setPrecioOriginal(rs.getBigDecimal(7));
                    productoEnVenta.setPrecioVentaFinal(rs.getBigDecimal(8));
                    productoEnVenta.setObservacion(rs.getString(9));
                    productoEnVenta.setEsPack(rs.getBoolean(10));
                    productoEnVenta.setEsDetallePack(rs.getBoolean(11));
                    productoEnVenta.setIdProductoPadre(rs.getInt(12));
                    productoEnVenta.setIdDetallePedido(rs.getInt(13));
                    productoEnVenta.setEsModificado(rs.getBoolean(14));
                    productoEnVenta.setDescripcionModificador(rs.getString(15));
                    productoEnVenta.setMontoDescuento(rs.getBigDecimal(16));
                    productoEnVenta.setUsaDescuento(rs.getBoolean(17));
                    productoEnVenta.setbPrecioVariable(rs.getBoolean(18));
                    productoEnVenta.setObservacionProducto(rs.getString(19));
                    productoEnVenta.setControlTiempo(rs.getBoolean(20));
                    productoEnVenta.setHoraInicio(rs.getString(21));
                    productoEnVenta.setHoraFinal(rs.getString(22));
                    productoEnVenta.setEstadoGuardado(rs.getString(23));
                    productoEnVenta.setEstadoEliminado(rs.getString(24));
                    pedido.getListProducto().add(productoEnVenta);

                }
             */
            pedido.getCabeceraPedido().setIdCabecera(cs.getInt(5));
            pedido.getCabeceraPedido().setIdentificadorPedido(cs.getString(6));
            pedido.getCabeceraPedido().setObservacion(cs.getString(7));
            pedido.getCabeceraPedido().getCliente().setiId(cs.getInt(8));
            pedido.getCabeceraPedido().getCliente().setcName(cs.getString(9));
            pedido.getCabeceraPedido().getVendedor().setIdVendedor(cs.getInt(10));
            pedido.getCabeceraPedido().setEstadoPermanecia(cs.getString(11).charAt(0));
            pedido.getCabeceraPedido().getVendedor().setPrimerNombre(cs.getString(12));
            pedido.getCabeceraPedido().setTotalBruto(cs.getBigDecimal(13));
            pedido.getCabeceraPedido().setDescuentoPrecio(cs.getBigDecimal(14));
            pedido.getCabeceraPedido().setTotalNeto(cs.getBigDecimal(15));
            pedido.getCabeceraPedido().setFechaCreacion(cs.getInt(16));
            pedido.getCabeceraPedido().setTipoDescuento(cs.getByte(17));
            pedido.getCabeceraPedido().setPorcentajeDescuento(cs.getBigDecimal(18));
            pedido.getCabeceraPedido().getCliente().setIdTipoDocumento(cs.getInt(19));
            pedido.getCabeceraPedido().getCliente().setIdTipoDocSunat(cs.getInt(20));
            pedido.getCabeceraPedido().getCliente().setNumeroRuc(cs.getString(21));
            pedido.getCabeceraPedido().getCliente().setcDireccion(cs.getString(22));
            pedido.getCabeceraPedido().getCliente().setcEmail(cs.getString(23));
            pedido.getCabeceraPedido().getCliente().setcTipoDocumento(cs.getString(24));
            pedido.getCabeceraPedido().getZonaServicio().setIdZona(cs.getInt(25));
            pedido.getCabeceraPedido().getZonaServicio().setDescripcion(cs.getString(26));
            pedido.getCabeceraPedido().getCliente().setIdTipoDocPagoDefecto(cs.getInt(27));
            pedido.getCabeceraPedido().getCliente().setRazonSocial(cs.getString(28));
            pedido.getCabeceraPedido().getZonaServicio().setBZonaLibre(cs.getBoolean(29));
            pedido.getCabeceraPedido().setIdentificador2(cs.getString(30));
            pedido.getCabeceraPedido().setFechaEntrega(cs.getString(31));
            pedido.getCabeceraPedido().setbEntregado(cs.getBoolean(32));
            pedido.getCabeceraPedido().setcTipoPedido(cs.getString(33));
            pedido.getCabeceraPedido().setcEstadoEntregaPedido(cs.getString(34));
            cs.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.toString();
        }
        return pedido;
    }

    public ResultProcces ActualizarPedidoProductoUnico(Pedido pedido) {
        ResultProcces result = new ResultProcces();
        try {

            CallableStatement cs = conn.prepareCall("call sp_actualiza_datos_producto_unico_pedido(" + ParamStoreProcedure(12) + ")");
            cs.setInt(1, pedido.getCabeceraPedido().getZonaServicio().getIdZona());
            cs.setString(2, pedido.getCabeceraPedido().getZonaServicio().getDescripcion());
            cs.setString(3, pedido.getListProducto().get(0).getTiempoInicio().getTimeStringFormatSql());
            cs.setString(4, pedido.getListProducto().get(0).getTiempoFinal().getTimeStringFormatSql());
            cs.setString(5, pedido.getCabeceraPedido().getObservacion());
            cs.setInt(6, pedido.getCabeceraPedido().getIdCabecera());
            cs.setInt(7, pedido.getListProducto().get(0).getIdDetallePedido());
            cs.setInt(8, pedido.getListProducto().get(0).getIdProducto());
            cs.setInt(9, Constantes.Empresa.idEmpresa);
            cs.setInt(10, Constantes.Tienda.idTienda);
            cs.registerOutParameter(11, Types.INTEGER);
            cs.registerOutParameter(12, Types.VARCHAR);

            cs.execute();

            result.setCodeResult(cs.getInt(11));
            result.setMessageResult(cs.getString(12));

        } catch (Exception ex) {
            result.setCodeResult(99);
            result.setMessageResult(ex.toString());
        }

        return result;

    }

    public void CambiarEstadoPedido(int id) {

        PreparedStatement ps = null;
        String query = "update Cabecera_Pedido set cEstadoPermanencia='T',iId_Terminal=? where " +
                "iId_Cabecera_Pedido=? and iIdCompany=?";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, Constantes.Terminal.idTerminal);
            ps.setInt(2, id);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public mCabeceraPedido getCabeceraPedidoPorId(int id) {
        mCabeceraPedido cabeceraPedido = null;

        CallableStatement cs = null;

        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_obtener_cabecera_pedido_id_recuperado_V2(" + ParamStoreProcedure(4) + ")");

                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, Constantes.Usuario.idUsuario);
                cs.setInt(4, id);
                ResultSet rs = cs.executeQuery();
                if (rs != null)
                    while (rs.next()) {
                        cabeceraPedido = new mCabeceraPedido(
                                rs.getInt("iId_Cabecera_Pedido"),
                                rs.getString("cIdentificador_Pedido"),
                                rs.getString("obs"),
                                rs.getInt("iIdCliente"),
                                rs.getString("cNombreCliente"),
                                rs.getInt("iIdVendedor"),
                                rs.getString("cNombreVendedor"),
                                rs.getInt("DateKey"),
                                rs.getString("cEstadoPermanencia").charAt(0),
                                rs.getBigDecimal("dValorBrutoVenta"),
                                rs.getBigDecimal("dDescuentoPedido"),

                                rs.getBigDecimal("dTotalVenta"),
                                rs.getByte("cTipoDescuento"),
                                rs.getBigDecimal("dPorcentajeDescuento")

                        );


                        cabeceraPedido.getCliente().setRazonSocial(rs.getString("cNombreCliente"));
                        cabeceraPedido.getCliente().setiId(rs.getInt(4));
                        cabeceraPedido.getCliente().setcName(rs.getString("cNombreCliente"));
                        cabeceraPedido.getCliente().setIdTipoDocumento(rs.getInt(15));
                        cabeceraPedido.getCliente().setIdTipoDocSunat(rs.getInt(16));
                        cabeceraPedido.getCliente().setNumeroRuc(rs.getString(17));
                        cabeceraPedido.getCliente().setcDireccion(rs.getString(18));
                        cabeceraPedido.getCliente().setcEmail(rs.getString(19));
                        cabeceraPedido.getCliente().setcTipoDocumento(rs.getString(20));
                        cabeceraPedido.getZonaServicio().setIdZona(rs.getInt(21));
                        cabeceraPedido.getZonaServicio().setDescripcion(rs.getString(22));
                        cabeceraPedido.getZonaServicio().setBZonaLibre(rs.getBoolean(23));
                        cabeceraPedido.setIdentificador2(rs.getString(24));
                        cabeceraPedido.setFechaEntrega(rs.getString(25));
                        cabeceraPedido.setbEntregado(rs.getBoolean(26));
                        cabeceraPedido.setcTipoPedido(rs.getString(27));
                        cabeceraPedido.setcEstadoEntregaPedido(rs.getString(28));
                    }

            } catch (Exception e) {
                e.toString();
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        return cabeceraPedido;
    }

    public byte SimboloMonedaPorDefecto() {

        byte respuesta = 0;
        PreparedStatement preparedStatement = null;
        String a = null;
        final String query = "select Simbolo,id_divisa_sunat from Divisas" +
                " where bDefault=1 and iIdCompany=? and cEstado_Divisa='A'";
        if (conn != null) {
            try {
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, Constantes.Empresa.idEmpresa);
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()) {
                    a = resultSet.getString(1);
                    if (a != null) {
                        Constantes.DivisaPorDefecto.SimboloDivisa = resultSet.getString(1);
                        Constantes.DivisaPorDefecto.idDivisaSunat = resultSet.getInt(2);
                    }
                }
                if (a == null) {
                    CrearDivisaPorDefecto(conn);
                }
                resultSet.close();
                respuesta = 2;
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 1;
            } finally {
                try {
                    preparedStatement.close();

                } catch (SQLException e) {
                    e.printStackTrace();

                }

            }

        } else if (conn == null) {
            respuesta = 1;
        }
        return respuesta;
    }

    public mCabeceraPedido GenerarNuevoPedido() {

        mCabeceraPedido pedido = null;
        CallableStatement cs = null;

        try {


            cs = conn.prepareCall("call sp_generar_nuevo_pedido_company_terminal(" + ParamStoreProcedure(4) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Terminal.idTerminal);
            cs.setInt(4, Constantes.Usuario.idUsuario);

            ResultSet rs = cs.executeQuery();

            if (rs != null) {

                pedido = new mCabeceraPedido();
                pedido.setIdCabecera(rs.getInt(1));
                pedido.getCliente().setIdTipoDocumento(rs.getInt(2));
                pedido.getCliente().setIdTipoDocSunat(rs.getInt(3));
                pedido.getCliente().setNumeroRuc(rs.getString(4));
                pedido.getCliente().setRazonSocial(rs.getString(5));

            }
        } catch (Exception e) {

            pedido = new mCabeceraPedido();
        }

        return pedido;
    }

    public byte IniciarVenta(int idCabeceraPedido) {
        idLockProc = 0;
        byte respuesta = 0;  // 0 sin conexion  1 error Procedimiento  2 procedimiento con exito
        PreparedStatement ps = null;
        int a;
        Connection con = getConnection();
        final String query = "insert into lock_process(iIdCabeceraPedido,cStartProcess,dateStart) values (?,?,GETUTCDATE())";
        final String proc_name = "Inicio sp_Generar_Venta";

        if (con != null) {
            try {
                ps = con.prepareStatement(query);
                ps.setInt(1, idCabeceraPedido);
                ps.setString(2, proc_name);
                a = ps.executeUpdate();
                if (a > 0) {
                    respuesta = 2;
                    ps.clearParameters();
                    ps = con.prepareStatement("select id_lock from lock_process where iIdCabeceraPedido=?");
                    ps.setInt(1, idCabeceraPedido);
                    ps.execute();

                    ResultSet set = ps.getResultSet();
                    while (set.next()) {
                        idLockProc = set.getInt(1);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 1;
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            respuesta = 0;
        }

        return respuesta;
    }

    public int generarNuevoPedido
            () {


        PreparedStatement ps = null;
        int idCabecera = 0;
        final String queryInsert = "insert into Cabecera_Pedido(iIdUser,iId_Terminal,iIdTienda,iIdCompany,DateKey) values(?,?,?,?,?) ";
        final String querySelect = "select iId_Cabecera_Pedido from Cabecera_Pedido where  iId_Terminal=? and iIdTienda=? and iIdUser=? and iIdCompany=? and cEstadoPermanencia=?";
        if (conn != null) {
            try {
                ps = conn.prepareStatement(queryInsert);
                ps.setInt(1, Constantes.Usuario.idUsuario);
                ps.setInt(2, Constantes.Terminal.idTerminal);
                ps.setInt(3, Constantes.Tienda.idTienda);
                ps.setInt(4, Constantes.Empresa.idEmpresa);
                ps.setInt(5, GenerarFecha());
                ps.execute();
                ps.clearParameters();
                ps = null;
                ps = conn.prepareStatement(querySelect);
                ps.setInt(1, Constantes.Terminal.idTerminal);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, Constantes.Usuario.idUsuario);
                ps.setInt(4, Constantes.Empresa.idEmpresa);
                ps.setString(5, "T");
                ps.execute();

                ResultSet rs = ps.getResultSet();

                while (rs.next()) {

                    idCabecera = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return idCabecera;
    }

    public int GenerarFecha() {
        Date fecha = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(dateFormat.format(fecha).toString());
    }

    public byte ActualizarCabeceraVentaIdFacturacionElectronica(int idCabeceraVenta,
                                                                ResultadoComprobante r) {

        byte respuesta = 100;

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("update cabecera_venta set " +
                    "cCod_Hash=?," +
                    "codRespuesta=?," +
                    "bEnviado=?," +
                    "bRecibido=?," +
                    "cMensajeRespuesta=?," +
                    "cEstadoRespuesta=? " +
                    "where iId_Company=? and" +
                    "  iId_Cabecera_Venta=?");
            ps.setString(1, r.getRptaSunat().getCodigo_hash());
            ps.setString(2, String.valueOf(r.getCodeSuccess()));
            ps.setBoolean(3, r.getEnviado());
            ps.setBoolean(4, r.getRecibido());
            ps.setString(5, r.getMensaje());
            ps.setString(6, r.getEstadoRespuesta());
            ps.setInt(7, Constantes.Empresa.idEmpresa);
            ps.setInt(8, idCabeceraVenta);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = 99;
        }


        return respuesta;
    }

    public mProduct ObtenerProductoId(int idProduct) {

        mProduct product = new mProduct();
        OpcionVariante op = null;
        ValorOpcionVariante vo = null;
        List<ValorOpcionVariante> listvo = null;
        PreparedStatement ps = null;


        final String query = "SELECT  Product.iIdProduct, Product.cKey, Product.cCodigo_Barra, Product.cProductName," +
                " Product.cUnit, Product.dQuantity, Product.dQuantityReserve, Product.dPurcharsePrice," +
                " Product.dSalesPrice, Product.cAdditionalInformation," +
                " Product.cObservacion_producto, ProductImage.iTipo_Imagen, " +
                " ProductImage.cCodigo_Color, ProductImage.cCodigo_Imagen," +
                " ProductImage.imageFile, Product.id_Categoria, " +
                " Product.bControl_Peso," +
                " Product.bControl_Stock, Product.bEsFavorito, " +
                " Product.cEstado_Visible, Product.cEstado_Producto, " +
                " Product.id_product_image, Product.bEstado_Variantes, Product.bEsPack" +
                ",rtrim(ltrim(isnull( Product.cDescripcion_unidad ,'')))," +
                "bPrecioVentaMulti,Product.id_Subcategoria,Product.bPrecioVariable,product.id_unidad_medida," +
                "product.bUsaTiempo,product.id_Area_Produccion,product.bVisibleWeb,isnull(dCantidad_max_pedido,10) as dCantidad_max_pedido " +
                " FROM dbo.Product INNER JOIN " +
                " dbo.ProductImage ON dbo.Product.id_product_image = dbo.ProductImage.id_product_image  where dbo.Product.iIdCompany=? and dbo.Product.iIdProduct=?";
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idProduct);
                ps.execute();
                ResultSet resultSet;
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    product.setIdProduct(rs.getInt(1));
                    product.setcKey(rs.getString(2));
                    product.setCodigoBarra(rs.getString(3));
                    product.setcProductName(rs.getString(4));
                    product.setcUnit(" ");
                    product.setStockDisponible(rs.getBigDecimal(6));
                    product.setStockReserva(rs.getBigDecimal(7));
                    product.setPrecioCompra(rs.getBigDecimal(8));
                    product.setPrecioVenta(rs.getBigDecimal(9));
                    product.setcAdditionalInformation(rs.getString(10));
                    product.setObservacionProducto(rs.getString(11));
                    product.setTipoRepresentacionImagen(rs.getByte(12));
                    product.setCodigoColor(rs.getString(13));
                    product.setCodigoForma(rs.getString(14));
                    product.setbImage(rs.getBytes(15));
                    product.setIdCategoria(rs.getInt(16));
                    product.setControlPeso(rs.getBoolean(17));
                    product.setControlStock(rs.getBoolean(18));
                    product.setEsFavorito(rs.getBoolean(19));
                    product.setEstadoVisible(rs.getString(20));
                    product.setEstadoActivo(rs.getString(21));
                    product.setIdProductImage(rs.getInt(22));
                    product.setEstadoVariante(rs.getBoolean(23));
                    product.setTipoPack(rs.getBoolean(24));
                    product.setUnidadMedida(rs.getString(25));
                    product.setMultiplePVenta(rs.getBoolean(26));
                    product.setIdSubCategoria(rs.getInt(27));
                    product.setpVentaLibre(rs.getBoolean(28));
                    product.setIdUnidadMedida(rs.getInt(29));
                    product.setbControlTiempo(rs.getBoolean(30));
                    product.setIdAreaProduccion(rs.getInt(31));
                    product.setbVisibleWeb(rs.getBoolean((32)));
                    product.setDCantidadMaximaPedido(rs.getBigDecimal(33));
                }
                ps.clearParameters();
                rs.close();
                if (product.isMultiplePVenta()) {
                    product.setPriceProductList(ObtenerPreciosAdiccionales(idProduct));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                product = null;

            } finally {

                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            product = null;
        }
        return product;
    }

    public byte EliminarPreciosAdiccionales(int idProducto, List<AdditionalPriceProduct> lista) {

        byte respuesta = 100;
        PreparedStatement ps = null;
        String select = " select id_PVenta from PreciosVentaAdiccional where id_company=?  and idProducto=?  and id_PVenta not in(";
        String delete = "delete PreciosVentaAdiccional where id_company=?  and idProducto=? and id_PVenta in  ";
        String in = " ( ";
        String interior = "";
        String query = "";
        if (lista.size() == 0) {
            query = delete + "(" + select + "?))";
        } else if (lista.size() > 0) {
            for (AdditionalPriceProduct a : lista) {
                interior = interior + "?,";
            }
            interior = interior.substring(0, interior.length() - 1);

            query = delete + "(" + select + interior + "))";
        }

        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idProducto);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, idProducto);
                if (lista.size() == 0) {
                    ps.setInt(5, 0);
                } else if (lista.size() > 0) {
                    for (int i = 0; i < lista.size(); i++) {
                        ps.setInt(i + 5, lista.get(i).getId());
                    }
                }
                ps.execute();
                respuesta = 100;
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 98;
            }
        } else {
            respuesta = 98;
        }
        return respuesta;
    }

    public mVenta getCabeceraVenta(int idCabeceraVenta) {

        mVenta venta = new mVenta();

        PreparedStatement ps = null;
        final String query = "select iId_Cabecera_Venta,iId_Cliente," +
                "cNombre_Cliente,iId_Vendedor,cNombre_Vendedor," +
                "dTotal_Bruto_Venta,dDescuento_Venta," +
                "dTotal_Neto_Venta,cEstadoVenta,dCambio_Venta" +
                ",FechaVentaRealizada-'5:00'," +
                "mc.cRazonSocial,mc.cNumeroRuc, " +
                "isnull(mv.cPrimerNombre+' '+mc.cApellidoPaterno+' '+mc.cApellidoPaterno,'') " +
                ",format(cv.FechaVentaRealizada-'5:00','dd/MM/yyyy hh:mm tt') " +
                ",cv.id_TipoDocumentoPago,cv.cNumeroSerieDocumentoSunat, " +
                "isnull(cv.cNumero_Correlativo,''),cv.dTotal_Gravado,cv.dTotal_Igv" +
                " from Cabecera_Venta as cv (nolock) " +
                " left join maestrocliente as mc (nolock) " +
                " on mc.iIdCliente=cv.iId_Cliente " +
                " left join maestrovendedor as mv (nolock) " +
                "on mv.iIdVendedor=cv.iId_vendedor where iId_Cabecera_Venta=? and iId_Company=?";


        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idCabeceraVenta);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                venta = new mVenta();
                venta.setIdCabeceraVenta(rs.getInt(1));
                venta.setIdCliente(rs.getInt(2));
                venta.setNombreCliente(rs.getString(3));
                venta.setIdVendedor(rs.getInt(4));
                venta.setNombreVendedor(rs.getString(5));
                venta.setTotalBruto(rs.getBigDecimal(6));
                venta.setDescuento(rs.getBigDecimal(7));
                venta.setTotalNeto(rs.getBigDecimal(8));
                venta.setcEstadoVenta(rs.getString(9));
                venta.setCambio(rs.getBigDecimal(10));
                venta.setFechaVentaRealizada(rs.getTimestamp(11));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            venta = null;
        }
        return venta;
    }

    public List<mProduct> ObtenerProductosListaPrincipal(String descripcion) {

        List<mProduct> list = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement cs = null;
        try {

            cs = conn.prepareCall("call sp_busqueda_producto_lista_principal(" + ParamStoreProcedure(3) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setString(3, descripcion);
            cs.execute();

            rs = cs.getResultSet();
            if (rs != null) {

                while (rs.next()) {

                    mProduct product = new mProduct();
                    product.setIdProduct(rs.getInt(1));
                    product.setcKey(rs.getString(2));
                    product.setcProductName(rs.getString(3));
                    product.setPrecioVenta(rs.getBigDecimal(5));
                    product.setbImage(rs.getBytes(6));
                    product.setTipoRepresentacionImagen(rs.getByte(7));
                    product.setCodigoColor(rs.getString(8));
                    product.setCodigoForma(rs.getString(9));
                    product.setEstadoVariante(rs.getBoolean(10));
                    product.setTipoPack(rs.getBoolean(11));
                    product.setEsFavorito(rs.getBoolean(12));
                    product.setDescripcionCategoria(rs.getString(15));
                    product.setDescripcionSubCategoria(rs.getString(16));
                    product.setIdSubCategoria(rs.getInt(17));
                    product.setEstadoModificador(rs.getBoolean(19));
                    list.add(product);

                }

            }

        } catch (Exception e) {
            e.toString();
            list = null;
        } finally {

            try {
                if (cs != null)
                    cs.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        return list;

    }

    public List<mProduct> ObtenerProductosConfigAvanzada(String descripcion, byte metodo) {

        List<mProduct> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement cs = null;

        try {
            cs = conn.prepareCall("call sp_busqueda_producto_lista_configuracion_avanzada(" + ParamStoreProcedure(4) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setString(3, descripcion);
            cs.setByte(4, metodo);

            cs.execute();

            rs = cs.getResultSet();

            if (rs != null) {

                while (rs.next()) {
                    mProduct p = new mProduct();
                    p.setIdProduct(rs.getInt(1));
                    p.setcKey(rs.getString(2));
                    p.setcProductName(rs.getString(3));
                    p.setPrecioVenta(rs.getBigDecimal(4));
                    p.setbImage(rs.getBytes(5));
                    p.setTipoRepresentacionImagen(rs.getByte(6));
                    p.setCodigoColor(rs.getString(7));
                    p.setCodigoForma(rs.getString(8));
                    p.setEstadoVariante(rs.getBoolean(9));
                    p.setTipoPack(rs.getBoolean(10));
                    p.setEsFavorito(rs.getBoolean(11));
                    p.setEstadoModificador(rs.getBoolean(12));
                    p.setDescripcionCategoria(rs.getString(13));
                    p.setDescripcionSubCategoria(rs.getString(14));
                    lista.add(p);

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();


        } finally {

            try {
                if (rs != null)
                    rs.close();
                if (cs != null)
                    cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return lista;

    }

    public List<mProduct> ObtenerProductosConfiguracion(int idProducto, String parametroBusqueda, byte metodoBusqueda) {


        List<mProduct> list = new ArrayList<>();
        PreparedStatement ps = null;

        mProduct product;
        String cadena = "select top(100) ";
        final String camposSubCategoria = ",isnull( scp.c_Descripcion_SubCategoria,''),p.id_subcategoria ";
        final String campoFavorito = ",bEsFavorito";
        final String camposBasicos = "p.iIdProduct,p.cKey,p.cProductName,isnull(vs.nCantidad,0),p.dSalesPrice ";
        final String camposDetalle = ",p.dQuantityReserve,p.dPurcharsePrice,p.cAdditionalInformation ";
        final String nombreTabla = " from product as p(nolock) ";
        final String campoStockReserva = ", ISNULL(pr.cantidad, 0) AS cantidadPedido ";
        final String estadoVariante = ",bEstado_Variantes";
        final String multiplePv = " ,bPrecioVentaMulti ";
        final String descripcionCategoria = ",isnull(cp.cDescripcion_categoria,'') ";
        final String campoImagen = ",prI.imageFile,prI.iTipo_Imagen,cCodigo_Color,cCodigo_Imagen";
        final String unionImagen = " left outer JOIN  Categoria_Productos as cp" +
                " ON cp.id_categoria_producto = p.id_Categoria" +
                " and cp.id_company=p.iIdCompany  LEFT OUTER JOIN SubCategorias as scp" +
                " ON p.id_Subcategoria = scp.id_subcategoria and p.iIdCompany=scp.id_company " +
                "  inner join productImage as prI on" +
                " p.id_product_image=prI.id_product_image LEFT OUTER JOIN " +
                " Productos_Reserva_Pedido as pr ON " +
                " p.iIdCompany = pr.iIdCompany AND " +
                " pr.iIdTienda=? AND  p.iIdProduct = pr.iIdProducto  " +
                " left join  VStock_Almacen_Principal_Tienda as vs on" +
                " p.iIdProduct=vs.id_producto AND vs.id_tienda=? ";
        final String comparacionCompany = "p.iIdCompany=? ";
        final String busquedaParametro = " and (p.cKey like '%'+?+'%' or p.cProductName like '%'+?+'%' " +
                " or p.cCodigo_Barra like '%'+?+'%' " +
                " or  ((cp.cDescripcion_categoria+' '+rtrim(ltrim(isnull(scp.c_Descripcion_SubCategoria,'')))+' '+p.cProductName) like '%'+?+'%') or ((cp.cDescripcion_categoria+' '+p.cProductName) like '%'+?+'%') ) ";
        final String busquedaPorId = " and p.iIdProduct=? ";
        final String busquedaFavorita = " and bEsFavorito=1 ";
        final String ValidacionEliminado = " and p.cEliminado=' ' ";
        final String VerificacionNoVariante = " and    bEsVariante=0 ";
        final String busquedaCategoria = " and p.id_Categoria=? ";
        final String VerificarPack = ",bEsPack ";
        final String TieneModificadores = ",bEstadoModificador";
        final String ObtenerPack = "";
        final String ObtenerVariantes = "";
        final String campoControlStock = ",bControl_Stock";
        final String CantidadDisponible = ",isnull(vs.nCantidad,0)";
        //  final String selectTienda=" and p.id_tienda=? ";
        final String selectTienda = "  ";
        final String selectVariante = " and bEstado_Variantes=1 ";
        final String selectCombo = " and bEsPack=1 ";
        final String selectCodigo = ",p.ckey ";
        final String estadoProducto = " and p.cEstado_Producto='A' ";
        //  final String unionAlmacen=" left join stock_almacen as sa "
        //        +" on p.iIdProduct=sa.iIdProduct";
        final String ValidacionVisible = "     ";
        final String selectVenta = "select top(100) p.iIdProduct,p.cProductName,p.dSalesPrice,prI.imageFile,prI.iTipo_Imagen,"
                + "cCodigo_Color,cCodigo_Imagen,bEstado_Variantes,bEsPack,p.cAdditionalInformation" +
                TieneModificadores +
                campoControlStock + CantidadDisponible + campoStockReserva + multiplePv + descripcionCategoria + camposSubCategoria + selectCodigo +
                " from product as p (nolock) " + unionImagen + " where " +
                comparacionCompany +
                ValidacionEliminado + VerificacionNoVariante
                + selectTienda + estadoProducto + " order by cProductName asc";

        if (metodoBusqueda == 100) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante + VerificarPack +
                    campoFavorito + ",p.cAdditionalInformation" + TieneModificadores +
                    campoControlStock + CantidadDisponible + campoStockReserva + multiplePv +
                    descripcionCategoria + camposSubCategoria + selectCodigo + nombreTabla + unionImagen + " where " +
                    comparacionCompany + ValidacionEliminado + VerificacionNoVariante + selectTienda + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 106) {

            cadena = cadena + camposBasicos + campoImagen + estadoVariante +
                    VerificarPack + TieneModificadores +
                    campoControlStock + CantidadDisponible + campoStockReserva + multiplePv + descripcionCategoria + camposSubCategoria + selectCodigo + nombreTabla +
                    unionImagen + " where " + comparacionCompany + ValidacionEliminado + selectTienda + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 107) {

        } else if (metodoBusqueda == 101) {
            cadena = cadena + camposBasicos +
                    campoImagen + camposDetalle + estadoVariante + VerificarPack
                    + TieneModificadores +
                    campoControlStock + CantidadDisponible +
                    campoStockReserva + multiplePv + descripcionCategoria +
                    camposSubCategoria + selectCodigo + nombreTabla + unionImagen
                    + " where " + comparacionCompany + busquedaPorId + ValidacionEliminado
                    + VerificacionNoVariante + selectTienda + " order by cProductName asc";
        } else if (metodoBusqueda == 102) {
            cadena = cadena + camposBasicos + camposDetalle + estadoVariante
                    + VerificarPack + TieneModificadores +
                    campoControlStock + CantidadDisponible +
                    campoStockReserva + multiplePv + descripcionCategoria
                    + camposSubCategoria + selectCodigo + nombreTabla + " where "
                    + comparacionCompany + busquedaPorId + ValidacionEliminado + VerificacionNoVariante + selectTienda + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 103) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante
                    + VerificarPack + campoFavorito
                    + ",p.cAdditionalInformation" + TieneModificadores +
                    campoControlStock + CantidadDisponible + campoStockReserva +
                    multiplePv + descripcionCategoria + camposSubCategoria
                    + selectCodigo + nombreTabla + unionImagen + " where "
                    + comparacionCompany + busquedaParametro + ValidacionEliminado +
                    VerificacionNoVariante + selectTienda + estadoProducto + " order by p.cProductName asc";

        } else if (metodoBusqueda == 104) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante +
                    VerificarPack + TieneModificadores +
                    campoControlStock + CantidadDisponible
                    + campoStockReserva + multiplePv + descripcionCategoria + camposSubCategoria + selectCodigo +
                    nombreTabla + unionImagen
                    + " where " + comparacionCompany + busquedaFavorita + ValidacionEliminado +
                    VerificacionNoVariante + ValidacionVisible + selectTienda + estadoProducto + " order by p.cProductName asc";
        } else if (metodoBusqueda == 105) {
            cadena = cadena + camposBasicos + campoImagen +
                    estadoVariante + VerificarPack +
                    TieneModificadores +
                    campoControlStock + CantidadDisponible + campoStockReserva
                    + multiplePv + descripcionCategoria + camposSubCategoria + selectCodigo + nombreTabla + unionImagen
                    + " where " + comparacionCompany + busquedaCategoria + ValidacionEliminado +
                    VerificacionNoVariante + ValidacionVisible + selectTienda + estadoProducto +
                    " order by p.cProductName asc";

        } else if (metodoBusqueda == 108) {

            cadena = selectVenta;

        } else if (metodoBusqueda == 109) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante + VerificarPack +
                    campoFavorito + ",p.cAdditionalInformation" + TieneModificadores
                    + campoControlStock + CantidadDisponible + campoStockReserva +
                    multiplePv + descripcionCategoria +
                    camposSubCategoria + selectCodigo + nombreTabla + unionImagen +
                    " where " + comparacionCompany + ValidacionEliminado +
                    VerificacionNoVariante + selectVariante + selectTienda + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 110) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante +
                    VerificarPack + campoFavorito +
                    ",p.cAdditionalInformation" + TieneModificadores +
                    campoControlStock + CantidadDisponible +
                    campoStockReserva + multiplePv + descripcionCategoria +
                    camposSubCategoria + selectCodigo + nombreTabla + unionImagen + " where " +
                    comparacionCompany + ValidacionEliminado +
                    VerificacionNoVariante + selectCombo + selectTienda + estadoProducto + " order by cProductName asc";

        }
        if (conn != null) {
            try {

                ps = conn.prepareStatement(cadena);
                ps.setInt(1, Constantes.Tienda.idTienda);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, Constantes.Empresa.idEmpresa);

                if (metodoBusqueda == 100 || metodoBusqueda == 109 || metodoBusqueda == 110) {
                    //       ps.setInt(5,Constantes.Tienda.idTienda);

                } else if (metodoBusqueda == 101) {
                    ps.setInt(4, idProducto);
                    //      ps.setInt(6,Constantes.Tienda.idTienda);
                } else if (metodoBusqueda == 102) {
                    ps.setInt(4, idProducto);
                    //       ps.setInt(6,Constantes.Tienda.idTienda);
                } else if (metodoBusqueda == 103) {
                    ps.setString(4, parametroBusqueda);
                    ps.setString(5, parametroBusqueda);
                    ps.setString(6, parametroBusqueda);
                    ps.setString(7, parametroBusqueda);
                    ps.setString(8, parametroBusqueda);
                    //     ps.setInt(10,Constantes.Tienda.idTienda);
                } else if (metodoBusqueda == 104) {
                    //   ps.setInt(5,Constantes.Tienda.idTienda  );
                } else if (metodoBusqueda == 105) {
                    ps.setInt(5, idProducto);
                    //       ps.setInt(6,Constantes.Tienda.idTienda);

                } else if (metodoBusqueda == 108) {
                    //    ps.setInt(5,Constantes.Tienda.idTienda);
                }

                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    product = new mProduct();
                    if (metodoBusqueda == 108) {
                        product.setIdProduct(rs.getInt(1));
                        product.setcProductName(rs.getString(2));
                        product.setPrecioVenta(rs.getBigDecimal(3));
                        product.setbImage(rs.getBytes(4));
                        product.setTipoRepresentacionImagen(rs.getByte(5));
                        product.setCodigoColor(rs.getString(6));
                        product.setCodigoForma(rs.getString(7));
                        product.setEstadoVariante(rs.getBoolean(8));
                        product.setTipoPack(rs.getBoolean(9));
                        product.setcAdditionalInformation(rs.getString(10));
                        product.setEstadoModificador(rs.getBoolean(11));
                        product.setControlStock(rs.getBoolean(12));
                        product.setdQuantity(rs.getFloat(13));
                        product.setCantidadReserva(rs.getFloat(14));
                        product.setMultiplePVenta(rs.getBoolean(15));
                        product.setDescripcionCategoria(rs.getString(16));
                        product.setDescripcionSubCategoria(rs.getString(17));
                        product.setIdSubCategoria(rs.getInt(18));
                        product.setcKey(rs.getString(19));
                    } else {
                        product.setIdProduct(rs.getInt(1));
                        product.setcKey(rs.getString(2));
                        product.setcProductName(rs.getString(3));
                        product.setdQuantity(rs.getFloat(4));
                        product.setPrecioVenta(rs.getBigDecimal(5));
                        if (metodoBusqueda == 100 || metodoBusqueda == 109 || metodoBusqueda == 110) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setEstadoVariante(rs.getBoolean(10));
                            product.setTipoPack(rs.getBoolean(11));
                            product.setEsFavorito(rs.getBoolean(12));
                            product.setcAdditionalInformation(rs.getString(13));
                            product.setEstadoModificador(rs.getBoolean(14));
                            product.setControlStock(rs.getBoolean(15));
                            product.setdQuantity(rs.getFloat(16));
                            product.setCantidadReserva(rs.getFloat(17));
                            product.setMultiplePVenta(rs.getBoolean(18));
                            product.setDescripcionCategoria(rs.getString(19));
                            product.setDescripcionSubCategoria(rs.getString(20));
                            product.setIdSubCategoria(rs.getInt(21));
                            product.setcKey(rs.getString(22));
                        } else if (metodoBusqueda == 101) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setdQuantityReserve(rs.getFloat(10));
                            product.setPrecioCompra(rs.getBigDecimal(11));
                            product.setcAdditionalInformation(rs.getString(12));
                            product.setEstadoVariante(rs.getBoolean(13));
                            product.setTipoPack(rs.getBoolean(14));
                            product.setEstadoModificador(rs.getBoolean(15));
                            product.setControlStock(rs.getBoolean(16));
                            product.setdQuantity(rs.getFloat(17));
                            product.setCantidadReserva(rs.getFloat(18));
                            product.setMultiplePVenta(rs.getBoolean(19));
                            product.setDescripcionCategoria(rs.getString(20));
                            product.setDescripcionSubCategoria(rs.getString(21));
                            product.setIdSubCategoria(rs.getInt(22));
                            product.setcKey(rs.getString(23));

                        } else if (metodoBusqueda == 102) {
                            product.setdQuantityReserve(rs.getFloat(6));
                            product.setPrecioCompra(rs.getBigDecimal(7));
                            product.setcAdditionalInformation(rs.getString(8));
                            product.setEstadoVariante(rs.getBoolean(9));
                            product.setTipoPack(rs.getBoolean(10));
                            product.setEstadoModificador(rs.getBoolean(11));
                            product.setControlStock(rs.getBoolean(12));
                            product.setdQuantity(rs.getFloat(13));
                            product.setCantidadReserva(rs.getFloat(14));
                            product.setMultiplePVenta(rs.getBoolean(15));
                            product.setDescripcionCategoria(rs.getString(16));
                            product.setDescripcionSubCategoria(rs.getString(17));
                            product.setIdSubCategoria(rs.getInt(18));
                            product.setcKey(rs.getString(19));
                        } else if (metodoBusqueda == 103) {

                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setEstadoVariante(rs.getBoolean(10));
                            product.setTipoPack(rs.getBoolean(11));
                            product.setEsFavorito(rs.getBoolean(12));
                            product.setcAdditionalInformation(rs.getString(13));
                            product.setEstadoModificador(rs.getBoolean(14));
                            product.setControlStock(rs.getBoolean(15));
                            product.setdQuantity(rs.getFloat(16));
                            product.setCantidadReserva(rs.getFloat(17));
                            product.setMultiplePVenta(rs.getBoolean(18));
                            product.setDescripcionCategoria(rs.getString(19));
                            product.setDescripcionSubCategoria(rs.getString(20));
                            product.setIdSubCategoria(rs.getInt(21));
                            product.setcKey(rs.getString(22));

                        } else if (metodoBusqueda == 104) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setEstadoVariante(rs.getBoolean(10));
                            product.setTipoPack(rs.getBoolean(11));
                            product.setEstadoModificador(rs.getBoolean(12));
                            product.setControlStock(rs.getBoolean(13));
                            product.setdQuantity(rs.getFloat(14));
                            product.setCantidadReserva(rs.getFloat(15));
                            product.setMultiplePVenta(rs.getBoolean(16));
                            product.setDescripcionCategoria(rs.getString(17));
                            product.setDescripcionSubCategoria(rs.getString(18));
                            product.setIdSubCategoria(rs.getInt(19));
                            product.setcKey(rs.getString(20));

                        } else if (metodoBusqueda == 105) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setEstadoVariante(rs.getBoolean(10));
                            product.setTipoPack(rs.getBoolean(11));
                            product.setEstadoModificador(rs.getBoolean(12));
                            product.setControlStock(rs.getBoolean(13));
                            product.setdQuantity(rs.getFloat(14));
                            product.setCantidadReserva(rs.getFloat(15));
                            product.setMultiplePVenta(rs.getBoolean(16));
                            product.setDescripcionCategoria(rs.getString(17));
                            product.setDescripcionSubCategoria(rs.getString(18));
                            product.setIdSubCategoria(rs.getInt(19));
                            product.setcKey(rs.getString(20));
                        }
                    }
                    list.add(product);
                }

                list.size();
            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            } finally {
                try {
                    ps.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            list = null;
        }
        return list;
    }

    public List<mProduct> ObtenerProductosVentasV2(int idCategoria, String parametro, byte metodoBusqueda) {


        List<mProduct> lista = new ArrayList<>();

        CallableStatement cs = null;

        try {
            cs = conn.prepareCall("call sp_obtener_productos_venta_V3(" + ParamStoreProcedure(5) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setByte(3, metodoBusqueda);
            cs.setInt(4, idCategoria);
            cs.setString(5, parametro);
            cs.execute();

            ResultSet rs = cs.getResultSet();
            if (rs != null) {

                while (rs.next()) {
                    mProduct product = new mProduct();
                    product.setIdProduct(rs.getInt(1));
                    product.setcProductName(rs.getString(2));
                    product.setdQuantity(rs.getFloat(13));
                    product.setPrecioVenta(rs.getBigDecimal(4));
                    product.setbImage(rs.getBytes(5));
                    product.setTipoRepresentacionImagen(rs.getByte(6));
                    product.setCodigoColor(rs.getString(7));
                    product.setCodigoForma(rs.getString(8));
                    product.setEstadoVariante(rs.getBoolean(9));
                    product.setTipoPack(rs.getBoolean(10));
                    // product.setcAdditionalInformation(rs.getString(11));
                    product.setEstadoModificador(rs.getBoolean(11));
                    product.setControlStock(rs.getBoolean(12));
                    product.setdQuantity(rs.getFloat(13));
                    product.setCantidadReserva(rs.getFloat(14));
                    product.setMultiplePVenta(rs.getBoolean(15));
                    product.setDescripcionCategoria(rs.getString(16));
                    product.setDescripcionSubCategoria(rs.getString(17));
                    product.setIdSubCategoria(rs.getInt(18));
                    product.setcKey(rs.getString(19));
                    product.setbControlPeso(rs.getBoolean(20));
                    product.setbControlTiempo(rs.getBoolean(21));
                    product.setComboSimple(rs.getBoolean(22));
                    lista.add(product);

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = null;
        }


        return lista;
    }

    @Deprecated
    public List<mProduct> ObtenerProductosVentas(int idProducto, String parametroBusqueda, byte metodoBusqueda) {

        /*int id,String parametro,int metodoBusqueda,int PermitirImagen*/
        // metodo busqueda : Todos los productos=100  Por Id detalle=101   Por Id sin Imagen =102  Por parametro =103
        // 104 =busquedaFavoritos      105=busquedaPorCategoria    106=Variantes    107 Combos/Pack
        //108 todos los producto para venta
        List<mProduct> list = new ArrayList<>();
        PreparedStatement ps = null;

        mProduct product;
        String cadena = "select top(100) ";
        final String estadoProducto = " and p.cEstado_Producto='A' ";

        final String cVisible = " and cEstado_Visible='V' ";
        final String camposSubCategoria = ",isnull( scp.c_Descripcion_SubCategoria,''),p.id_subcategoria ";
        final String campoFavorito = ",bEsFavorito";
        final String camposBasicos = "p.iIdProduct,p.cKey,p.cProductName,isnull(vs.nCantidad,0),p.dSalesPrice ";
        final String camposDetalle = ",p.dQuantityReserve,p.dPurcharsePrice,p.cAdditionalInformation ";
        final String nombreTabla = " from product as p(nolock) ";
        final String campoStockReserva = ", ISNULL(pr.cantidad, 0) AS cantidadPedido ";
        final String estadoVariante = ",bEstado_Variantes";
        final String multiplePv = " ,bPrecioVentaMulti ";
        final String descripcionCategoria = ",isnull(cp.cDescripcion_categoria,'') ";
        final String campoImagen = ",prI.imageFile,prI.iTipo_Imagen,cCodigo_Color,cCodigo_Imagen";
        final String unionImagen = " left outer JOIN  Categoria_Productos as cp" +
                " ON cp.id_categoria_producto = p.id_Categoria" +
                " and cp.id_company=p.iIdCompany  LEFT OUTER JOIN SubCategorias as scp" +
                " ON p.id_Subcategoria = scp.id_subcategoria and p.iIdCompany=scp.id_company " +
                "  inner join productImage as prI on" +
                " p.id_product_image=prI.id_product_image LEFT OUTER JOIN " +
                " Productos_Reserva_Pedido as pr ON " +
                " p.iIdCompany = pr.iIdCompany AND " +
                " pr.iIdTienda=? AND  p.iIdProduct = pr.iIdProducto  " +
                " left join VStock_Almacen_Principal_Tienda as vs on" +
                " p.iIdProduct=vs.id_producto AND vs.id_tienda=? ";
        final String comparacionCompany = "p.iIdCompany=? ";
        final String busquedaParametro = " and (p.cKey like '%'+?+'%' or p.cProductName like '%'+?+'%' " +
                " or p.cCodigo_Barra like '%'+?+'%' " +
                " or  ((cp.cDescripcion_categoria+' '+rtrim(ltrim(isnull(scp.c_Descripcion_SubCategoria,'')))+' '+p.cProductName) like '%'+?+'%') or ((cp.cDescripcion_categoria+' '+p.cProductName) like '%'+?+'%') ) ";
        final String busquedaPorId = " and p.iIdProduct=? ";
        final String busquedaFavorita = " and bEsFavorito=1 ";
        final String ValidacionEliminado = " and p.cEliminado=' ' ";
        final String VerificacionNoVariante = " and bEsVariante=0 ";
        final String busquedaCategoria = " and p.id_Categoria=? ";
        final String VerificarPack = ",bEsPack ";
        final String TieneModificadores = ",bEstadoModificador";
        final String ObtenerPack = "";
        final String ObtenerVariantes = "";
        final String campoControlStock = ",bControl_Stock";
        final String CantidadDisponible = ",isnull(vs.nCantidad,0)";
        final String selectTienda = " ";

//        final String selectTienda=" and p.id_tienda=? ";
        final String selectVariante = " and bEstado_Variantes=1 ";
        final String selectCombo = " and bEsPack=1 ";
        final String selectCodigo = ",p.ckey,isnull(bControl_Peso,0),bUsaTiempo,bComboSimple  ";

        //  final String unionAlmacen=" left join stock_almacen as sa "
        //        +" on p.iIdProduct=sa.iIdProduct";
        final String ValidacionVisible = " and p.cEstado_Visible='V' ";
        final String selectVenta = "select  top(100) p.iIdProduct,p.cProductName,p.dSalesPrice,prI.imageFile,prI.iTipo_Imagen,"
                + "cCodigo_Color,cCodigo_Imagen,bEstado_Variantes,bEsPack,p.cAdditionalInformation" +
                TieneModificadores +
                campoControlStock + CantidadDisponible + campoStockReserva
                + multiplePv + descripcionCategoria + camposSubCategoria + selectCodigo +
                " from product as p (nolock) " + unionImagen + " where " +
                comparacionCompany +
                ValidacionEliminado + VerificacionNoVariante
                + " and p.cEstado_Visible='V' " + selectTienda + estadoProducto + " order by cProductName asc";


        if (metodoBusqueda == 100) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante + VerificarPack +
                    campoFavorito + ",p.cAdditionalInformation" + TieneModificadores +
                    campoControlStock + CantidadDisponible + campoStockReserva + multiplePv +
                    descripcionCategoria + camposSubCategoria + selectCodigo + nombreTabla + unionImagen + " where " +
                    comparacionCompany + ValidacionEliminado + ValidacionVisible + VerificacionNoVariante + selectTienda + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 106) {

            cadena = cadena + camposBasicos + campoImagen + estadoVariante +
                    VerificarPack + TieneModificadores +
                    campoControlStock + CantidadDisponible + campoStockReserva + multiplePv + descripcionCategoria + camposSubCategoria + selectCodigo + nombreTabla +
                    unionImagen + " where " + comparacionCompany + ValidacionVisible
                    + ValidacionEliminado + selectTienda + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 107) {

        } else if (metodoBusqueda == 101) {
            cadena = cadena + camposBasicos +
                    campoImagen + camposDetalle + estadoVariante + VerificarPack
                    + TieneModificadores +
                    campoControlStock + CantidadDisponible +
                    campoStockReserva + multiplePv + descripcionCategoria +
                    camposSubCategoria + selectCodigo + nombreTabla + unionImagen
                    + " where " + comparacionCompany + busquedaPorId + ValidacionEliminado
                    + VerificacionNoVariante + selectTienda + ValidacionVisible + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 102) {
            cadena = cadena + camposBasicos + camposDetalle + estadoVariante
                    + VerificarPack + TieneModificadores +
                    campoControlStock + CantidadDisponible +
                    campoStockReserva + multiplePv + descripcionCategoria
                    + camposSubCategoria + selectCodigo + nombreTabla + " where "
                    + comparacionCompany + busquedaPorId + ValidacionEliminado + VerificacionNoVariante + selectTienda + ValidacionVisible + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 103) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante
                    + VerificarPack + campoFavorito
                    + ",p.cAdditionalInformation" + TieneModificadores +
                    campoControlStock + CantidadDisponible + campoStockReserva +
                    multiplePv + descripcionCategoria + camposSubCategoria
                    + selectCodigo + nombreTabla + unionImagen + " where "
                    + comparacionCompany + busquedaParametro + ValidacionEliminado +
                    VerificacionNoVariante + selectTienda + ValidacionVisible + estadoProducto + " order by p.cProductName asc";

        } else if (metodoBusqueda == 104) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante +
                    VerificarPack + TieneModificadores +
                    campoControlStock + CantidadDisponible
                    + campoStockReserva + multiplePv + descripcionCategoria + camposSubCategoria + selectCodigo +
                    nombreTabla + unionImagen
                    + " where " + comparacionCompany + busquedaFavorita + ValidacionEliminado +
                    VerificacionNoVariante + ValidacionVisible + selectTienda + ValidacionVisible + estadoProducto + " order by p.cProductName asc";
        } else if (metodoBusqueda == 105) {
            cadena = cadena + camposBasicos + campoImagen +
                    estadoVariante + VerificarPack +
                    TieneModificadores +
                    campoControlStock + CantidadDisponible + campoStockReserva
                    + multiplePv + descripcionCategoria + camposSubCategoria + selectCodigo + nombreTabla + unionImagen
                    + " where " + comparacionCompany + busquedaCategoria + ValidacionEliminado +
                    VerificacionNoVariante + ValidacionVisible + selectTienda + ValidacionVisible + estadoProducto +
                    " order by p.cProductName asc";

        } else if (metodoBusqueda == 108) {

            cadena = selectVenta;

        } else if (metodoBusqueda == 109) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante + VerificarPack +
                    campoFavorito + ",p.cAdditionalInformation" + TieneModificadores
                    + campoControlStock + CantidadDisponible + campoStockReserva +
                    multiplePv + descripcionCategoria +
                    camposSubCategoria + selectCodigo + nombreTabla + unionImagen +
                    " where " + comparacionCompany + ValidacionEliminado +
                    VerificacionNoVariante + selectVariante + selectTienda + ValidacionVisible + estadoProducto + " order by cProductName asc";
        } else if (metodoBusqueda == 110) {
            cadena = cadena + camposBasicos + campoImagen + estadoVariante +
                    VerificarPack + campoFavorito +
                    ",p.cAdditionalInformation" + TieneModificadores +
                    campoControlStock + CantidadDisponible +
                    campoStockReserva + multiplePv + descripcionCategoria +
                    camposSubCategoria + selectCodigo + nombreTabla + unionImagen + " where " +
                    comparacionCompany + ValidacionEliminado +
                    VerificacionNoVariante + selectCombo + selectTienda + ValidacionVisible + estadoProducto + " order by cProductName asc";

        }
        if (conn != null) {
            try {

                ps = conn.prepareStatement(cadena);
                ps.setInt(1, Constantes.Tienda.idTienda);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, Constantes.Empresa.idEmpresa);

                if (metodoBusqueda == 100 || metodoBusqueda == 109 || metodoBusqueda == 110) {
                    //     ps.setInt(2,Constantes.Tienda.idTienda);

                } else if (metodoBusqueda == 101) {
                    ps.setInt(4, idProducto);
                    //    ps.setInt(3,Constantes.Tienda.idTienda);
                } else if (metodoBusqueda == 102) {
                    ps.setInt(4, idProducto);
                    //       ps.setInt(3,Constantes.Tienda.idTienda);
                } else if (metodoBusqueda == 103) {
                    ps.setString(4, parametroBusqueda);
                    ps.setString(5, parametroBusqueda);
                    ps.setString(6, parametroBusqueda);
                    ps.setString(7, parametroBusqueda);
                    ps.setString(8, parametroBusqueda);
                    //      ps.setInt(10,Constantes.Tienda.idTienda);
                } else if (metodoBusqueda == 104) {
                    //       ps.setInt(2,Constantes.Tienda.idTienda  );
                } else if (metodoBusqueda == 105) {
                    ps.setInt(4, idProducto);
                    //     ps.setInt(3,Constantes.Tienda.idTienda);

                } else if (metodoBusqueda == 108) {
                    //                   ps.setInt(2,Constantes.Tienda.idTienda);
                }

                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    product = new mProduct();
                    if (metodoBusqueda == 108) {
                        product.setIdProduct(rs.getInt(1));
                        product.setcProductName(rs.getString(2));
                        product.setPrecioVenta(rs.getBigDecimal(3));
                        product.setbImage(rs.getBytes(4));
                        product.setTipoRepresentacionImagen(rs.getByte(5));
                        product.setCodigoColor(rs.getString(6));
                        product.setCodigoForma(rs.getString(7));
                        product.setEstadoVariante(rs.getBoolean(8));
                        product.setTipoPack(rs.getBoolean(9));
                        product.setcAdditionalInformation(rs.getString(10));
                        product.setEstadoModificador(rs.getBoolean(11));
                        product.setControlStock(rs.getBoolean(12));
                        product.setdQuantity(rs.getFloat(13));
                        product.setCantidadReserva(rs.getFloat(14));
                        product.setMultiplePVenta(rs.getBoolean(15));
                        product.setDescripcionCategoria(rs.getString(16));
                        product.setDescripcionSubCategoria(rs.getString(17));
                        product.setIdSubCategoria(rs.getInt(18));
                        product.setcKey(rs.getString(19));
                        product.setbControlPeso(rs.getBoolean(20));
                        product.setbControlTiempo(rs.getBoolean(21));
                        product.setComboSimple(rs.getBoolean(22));

                    } else {
                        product.setIdProduct(rs.getInt(1));
                        product.setcKey(rs.getString(2));
                        product.setcProductName(rs.getString(3));
                        product.setdQuantity(rs.getFloat(4));
                        product.setPrecioVenta(rs.getBigDecimal(5));
                        if (metodoBusqueda == 100 || metodoBusqueda == 109 || metodoBusqueda == 110) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setEstadoVariante(rs.getBoolean(10));
                            product.setTipoPack(rs.getBoolean(11));
                            product.setEsFavorito(rs.getBoolean(12));
                            product.setcAdditionalInformation(rs.getString(13));
                            product.setEstadoModificador(rs.getBoolean(14));
                            product.setControlStock(rs.getBoolean(15));
                            product.setdQuantity(rs.getFloat(16));
                            product.setCantidadReserva(rs.getFloat(17));
                            product.setMultiplePVenta(rs.getBoolean(18));
                            product.setDescripcionCategoria(rs.getString(19));
                            product.setDescripcionSubCategoria(rs.getString(20));
                            product.setIdSubCategoria(rs.getInt(21));
                            product.setcKey(rs.getString(22));
                            product.setbControlPeso(rs.getBoolean(23));
                            product.setbControlTiempo(rs.getBoolean(24));
                            product.setComboSimple(rs.getBoolean(25));
                        } else if (metodoBusqueda == 101) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setdQuantityReserve(rs.getFloat(10));
                            product.setPrecioCompra(rs.getBigDecimal(11));
                            product.setcAdditionalInformation(rs.getString(12));
                            product.setEstadoVariante(rs.getBoolean(13));
                            product.setTipoPack(rs.getBoolean(14));
                            product.setEstadoModificador(rs.getBoolean(15));
                            product.setControlStock(rs.getBoolean(16));
                            product.setdQuantity(rs.getFloat(17));
                            product.setCantidadReserva(rs.getFloat(18));
                            product.setMultiplePVenta(rs.getBoolean(19));
                            product.setDescripcionCategoria(rs.getString(20));
                            product.setDescripcionSubCategoria(rs.getString(21));
                            product.setIdSubCategoria(rs.getInt(22));
                            product.setcKey(rs.getString(23));
                            product.setbControlPeso(rs.getBoolean(24));
                            product.setbControlTiempo(rs.getBoolean(25));
                            product.setComboSimple(rs.getBoolean(26));
                        } else if (metodoBusqueda == 102) {
                            product.setdQuantityReserve(rs.getFloat(6));
                            product.setPrecioCompra(rs.getBigDecimal(7));
                            product.setcAdditionalInformation(rs.getString(8));
                            product.setEstadoVariante(rs.getBoolean(9));
                            product.setTipoPack(rs.getBoolean(10));
                            product.setEstadoModificador(rs.getBoolean(11));
                            product.setControlStock(rs.getBoolean(12));
                            product.setdQuantity(rs.getFloat(13));
                            product.setCantidadReserva(rs.getFloat(14));
                            product.setMultiplePVenta(rs.getBoolean(15));
                            product.setDescripcionCategoria(rs.getString(16));
                            product.setDescripcionSubCategoria(rs.getString(17));
                            product.setIdSubCategoria(rs.getInt(18));
                            product.setcKey(rs.getString(19));
                            product.setbControlPeso(rs.getBoolean(20));

                            product.setbControlTiempo(rs.getBoolean(21));
                            product.setComboSimple(rs.getBoolean(22));
                        } else if (metodoBusqueda == 103) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setEstadoVariante(rs.getBoolean(10));
                            product.setTipoPack(rs.getBoolean(11));
                            product.setEsFavorito(rs.getBoolean(12));
                            product.setcAdditionalInformation(rs.getString(13));
                            product.setEstadoModificador(rs.getBoolean(14));
                            product.setControlStock(rs.getBoolean(15));
                            product.setdQuantity(rs.getFloat(16));
                            product.setCantidadReserva(rs.getFloat(17));
                            product.setMultiplePVenta(rs.getBoolean(18));
                            product.setDescripcionCategoria(rs.getString(19));
                            product.setDescripcionSubCategoria(rs.getString(20));
                            product.setIdSubCategoria(rs.getInt(21));
                            product.setcKey(rs.getString(22));
                            product.setbControlPeso(rs.getBoolean(23));
                            product.setbControlTiempo(rs.getBoolean(24));
                            product.setComboSimple(rs.getBoolean(25));
                        } else if (metodoBusqueda == 104) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setEstadoVariante(rs.getBoolean(10));
                            product.setTipoPack(rs.getBoolean(11));
                            product.setEstadoModificador(rs.getBoolean(12));
                            product.setControlStock(rs.getBoolean(13));
                            product.setdQuantity(rs.getFloat(14));
                            product.setCantidadReserva(rs.getFloat(15));
                            product.setMultiplePVenta(rs.getBoolean(16));
                            product.setDescripcionCategoria(rs.getString(17));
                            product.setDescripcionSubCategoria(rs.getString(18));
                            product.setIdSubCategoria(rs.getInt(19));
                            product.setcKey(rs.getString(20));
                            product.setbControlPeso(rs.getBoolean(21));
                            product.setbControlTiempo(rs.getBoolean(22));
                            product.setComboSimple(rs.getBoolean(23));
                        } else if (metodoBusqueda == 105) {
                            product.setbImage(rs.getBytes(6));
                            product.setTipoRepresentacionImagen(rs.getByte(7));
                            product.setCodigoColor(rs.getString(8));
                            product.setCodigoForma(rs.getString(9));
                            product.setEstadoVariante(rs.getBoolean(10));
                            product.setTipoPack(rs.getBoolean(11));
                            product.setEstadoModificador(rs.getBoolean(12));
                            product.setControlStock(rs.getBoolean(13));
                            product.setdQuantity(rs.getFloat(14));
                            product.setCantidadReserva(rs.getFloat(15));
                            product.setMultiplePVenta(rs.getBoolean(16));
                            product.setDescripcionCategoria(rs.getString(17));
                            product.setDescripcionSubCategoria(rs.getString(18));
                            product.setIdSubCategoria(rs.getInt(19));
                            product.setcKey(rs.getString(20));
                            product.setbControlPeso(rs.getBoolean(21));
                            product.setbControlTiempo(rs.getBoolean(22));
                            product.setComboSimple(rs.getBoolean(23));
                        }
                    }
                    list.add(product);
                }

                list.size();
            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            } finally {
                try {
                    ps.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            list = null;
        }
        return list;
    }

    public ResultProcessData<List<ProductoEnVenta>> ModificarEstadoPedidoV2(int idCabeceraPedido, InfoGuardadoPedido info) {

        int codigoResult = 0;
        String mensajeResult = "";
        int num = 0;
        String estados = "T";
        CallableStatement cs = null;
        List<ProductoEnVenta> lista = new ArrayList<>();
        try {


            cs = conn.prepareCall("call sp_guardar_pedido_reserva_v8(" + ParamStoreProcedure(14) + ")");
            cs.setInt(1, idCabeceraPedido);
            cs.setString(2, info.getStr1());
            cs.setInt(3, Constantes.Empresa.idEmpresa);
            cs.setInt(4, Constantes.Terminal.idTerminal);
            cs.setInt(5, Constantes.Usuario.idUsuario);
            cs.setInt(6, Constantes.Tienda.idTienda);
            cs.setString(7, info.getStr2());
            cs.setInt(8, GenerarFecha());
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.setString(9, "T");
            cs.setString(10, info.getStr3());
            cs.setString(11, info.getFecha());
            cs.setBoolean(12, info.getBEsContrato());
            cs.registerOutParameter(13, Types.INTEGER);
            cs.registerOutParameter(14, Types.VARCHAR);
            cs.execute();

            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {

                    ProductoEnVenta p = new ProductoEnVenta();
                    p.setItemNum(rs.getInt(2));
                    p.setProductName(rs.getString(3));
                    p.setDescripcionModificador(rs.getString(4));
                    p.setDescripcionVariante(rs.getString(5));
                    p.setCantidad(rs.getFloat(6));
                    p.getAreaProduccion().setIdArea(rs.getInt(7));
                    try {
                        p.getAreaProduccion().getImpresora().setIP(rs.getString(8));

                        p.getAreaProduccion().getImpresora().setPuerto(rs.getInt(9));
                        p.getAreaProduccion().getImpresora().setMacAddress(rs.getString(10));
                        p.getAreaProduccion().getImpresora().setTipoImpresora(rs.getString(11));

                    } catch (Exception e) {
                        e.toString();
                    }
                    p.setEsDetallePack(rs.getBoolean(12));
                    p.setDescripcionCombo(rs.getString(13));
                    p.setObservacionProducto(rs.getString(14));
                    p.setCodigoProducto(rs.getString(15));
                    lista.add(p);
                }
                rs.close();
            }

            estados = cs.getString(9);
            codigoResult = cs.getInt(13);
            mensajeResult = cs.getString(14);
            if (estados.equals("R")) {
                num = 1;
            } else {
                num = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            num = 0;
            Log.d("Error ", "MODIFICAR ESTADO " + e.toString());
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return new ResultProcessData<List<ProductoEnVenta>>(codigoResult, mensajeResult, lista);
    }

    public int ModificarEstadoPedido(int idCabeceraPedido, String identificador,
                                     String observacion, String estado, String identificador2, String fechaEntrega) {

        int num = 0;
        String estados = "T";
        CallableStatement cs = null;
        List<ProductoEnVenta> lista = new ArrayList<>();
        try {


            cs = conn.prepareCall("call sp_guardar_pedido_reserva_v5(" + ParamStoreProcedure(11) + ")");
            cs.setInt(1, idCabeceraPedido);
            cs.setString(2, identificador);
            cs.setInt(3, Constantes.Empresa.idEmpresa);
            cs.setInt(4, Constantes.Terminal.idTerminal);
            cs.setInt(5, Constantes.Usuario.idUsuario);
            cs.setInt(6, Constantes.Tienda.idTienda);
            cs.setString(7, observacion);
            cs.setInt(8, GenerarFecha());
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.setString(9, "T");
            cs.setString(10, identificador2);
            cs.setString(11, fechaEntrega);
            cs.execute();

            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {

                    ProductoEnVenta p = new ProductoEnVenta();
                    p.setItemNum(rs.getInt(2));
                    p.setProductName(rs.getString(3));
                    p.setDescripcionModificador(rs.getString(4));
                    p.setDescripcionVariante(rs.getString(5));
                    p.setCantidad(rs.getFloat(6));
                    p.getAreaProduccion().setIdArea(rs.getInt(7));
                    try {
                        p.getAreaProduccion().getImpresora().setIP(rs.getString(8));

                        p.getAreaProduccion().getImpresora().setPuerto(rs.getInt(9));
                        p.getAreaProduccion().getImpresora().setMacAddress(rs.getString(10));
                        p.getAreaProduccion().getImpresora().setTipoImpresora(rs.getString(11));

                    } catch (Exception e) {
                        e.toString();
                    }
                    p.setEsDetallePack(rs.getBoolean(12));
                    p.setDescripcionCombo(rs.getString(13));
                    p.setObservacionProducto(rs.getString(14));
                    p.setCodigoProducto(rs.getString(15));
                    lista.add(p);
                }
                rs.close();
            }

            estados = cs.getString(9);

            if (estados.equals("R")) {
                num = 1;
            } else {
                num = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            num = 0;
            Log.d("Error ", "MODIFICAR ESTADO " + e.toString());
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return num;
    }

    public void ActualizarTerminalPedido(int idPedido) {

        PreparedStatement ps = null;
        final String query = "update Cabecera_Pedido set iId_Terminal=? ," +
                "iIdUser=? where iId_Cabecera_Pedido=? and iIdCompany=? and iIdTienda=?";

        if (conn != null) {

            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Terminal.idTerminal);
                ps.setInt(2, Constantes.Usuario.idUsuario);
                ps.setInt(3, idPedido);
                ps.setInt(4, Constantes.Empresa.idEmpresa);
                ps.setInt(5, Constantes.Tienda.idTienda);

                ps.execute();


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public int SuspenderPedido(int idCabeceraPedido) {


        CallableStatement cs = null;

        try {

            cs = conn.prepareCall("call sp_anular_pedido(" + ParamStoreProcedure(7) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Usuario.idUsuario);
            cs.setInt(4, Constantes.Terminal.idTerminal);
            cs.setInt(5, idCabeceraPedido);
            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.execute();

            return 1;

        } catch (Exception e) {

            e.toString();
            return 0;
        }

    }
    //


    public void CambioPedidoActualPorReservado(int idPedidoActual, int idPedidoRecuperar) {

        CallableStatement cs = null;

        try {
            cs = conn.prepareCall("call sp_cambiar_pedido_actual_x_reserva(" + ParamStoreProcedure(8) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Usuario.idUsuario);
            cs.setInt(4, Constantes.Terminal.idTerminal);
            cs.setInt(5, idPedidoActual);
            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.setInt(8, idPedidoRecuperar);
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int ModificarEstadoPedidoSuspendido(int idCabeceraPedido, String estado) {

        int num = 0;
        PreparedStatement ps = null;
        final String query = "update Cabecera_Pedido set cEstadoPermanencia=? ,dFechaGuardadoPedido=GETUTCDATE() " +
                "where  iIdCompany=? and iIdTienda=? and iId_Cabecera_Pedido=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, estado);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Tienda.idTienda);
            ps.setInt(4, idCabeceraPedido);
            num = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            num = 0;
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return num;
    }

    public ArrayList<String> ObtenerIdentificadorPedido(int idCabeceraPedido) {

        String id = "";
        ArrayList<String> lista = new ArrayList<>();
        PreparedStatement ps = null;
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call sp_obtener_identificador_pedido_impresion(" + ParamStoreProcedure(3) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idCabeceraPedido);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    lista.add(rs.getString(1));
                    lista.add(rs.getString(2));
                    lista.add(rs.getString(3));
                    lista.add(rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6));
                    lista.add(rs.getString(7));
                    lista.add(rs.getString(8));

                }
                rs.close();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("ERROR", "ERROR CABECERA PEDIDO " + e.toString());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    public List<mCabeceraPedido> ObtenerCabecerasPedidos(int fechaInicio, int fechaFinal, int idCliente, String tipo, String nroPedido) {


        List<mCabeceraPedido> list = new ArrayList<>();

        try {
            CallableStatement cs = conn.prepareCall("call sp_obtener_cabeceras_pedido_periodo_fecha_v5(" + ParamStoreProcedure(8) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idCliente);
            cs.setInt(4, fechaInicio);
            cs.setInt(5, fechaFinal);
            cs.setInt(6, Constantes.ConfigTienda.idTipoZonaServicio);
            cs.setString(7, tipo);
            cs.setString(8, nroPedido);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    mCabeceraPedido pedido = new mCabeceraPedido();
                    pedido.setIdCabecera(rs.getInt(1));
                    pedido.setIdentificadorPedido(rs.getString(2));
                    pedido.setDenominacionCliente(rs.getString(3));
                    pedido.setNombreVendedor(rs.getString(4));
                    pedido.setFechaCreacion(rs.getInt(5));
                    pedido.setTotalBruto(rs.getBigDecimal(6));
                    pedido.setDescuentoPrecio(rs.getBigDecimal(7));
                    pedido.setTotalNeto(rs.getBigDecimal(8));
                    pedido.setFechaReserva(rs.getString(9));
                    pedido.setDescripcionPedido(rs.getString(10));
                    pedido.setAnulado(rs.getBoolean(12));
                    pedido.setObservacionReserva(rs.getString(13));
                    pedido.setPermitirModificaciones(rs.getBoolean(14));
                    pedido.setNumPedido(rs.getString("cNumPedido"));
                    pedido.setcTipoPedido(rs.getString("ctipo_pedido"));
                    list.add(pedido);
                }
                cs.close();
                rs.close();
            }
        } catch (SQLException ex) {
            Log.e("error1", ex.toString());
            ex.printStackTrace();
            list = new ArrayList<>();
        } catch (Exception ex1) {
            Log.e("error2", ex1.toString());
        }
        return list;
    }

    @Deprecated
    public List<mCabeceraPedido> getListCabeceraPedidos(int fechaInicio, int fechaFinal, int idCliente) {

        List<mCabeceraPedido> list = new ArrayList<>();
        String query = "select iId_Cabecera_Pedido,cIdentificador_Pedido," +
                "cNombreCliente,cPrimerNombre,DateKey," +
                "dValorBrutoVenta,dDescuentoPedido,dTotalVenta," +
                "format(dFechaGuardadoPedido-'5:00','dd/MM/yyyy hh:mm tt')," +
                "dbo.DescripcionZonaServicioOperarioV2" +
                "(id_zona_servicio,iIdCompany,iIdTienda,?,iId_Cabecera_Pedido) " +
                "from Cabecera_Pedido where iIdCompany=? and iIdTienda=? " +
                " and cEstadoPermanencia='R'  and DateKey BETWEEN ? and ? ";

        final String filtroCliente = " and iIdCliente=? ";


        if (idCliente != 0) {
            query = query + filtroCliente;
        }
        try {

            PreparedStatement ps = conn.prepareStatement(query + " order by dFechaGuardadoPedido desc ");
            ps.setInt(1, Constantes.ConfigTienda.idTipoZonaServicio);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Tienda.idTienda);
            ps.setInt(4, fechaInicio);
            ps.setInt(5, fechaFinal);


            if (idCliente != 0) {
                ps.setInt(6, idCliente);
            }
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                list.add(new mCabeceraPedido(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getBigDecimal(6),
                        rs.getBigDecimal(7),
                        rs.getBigDecimal(8),
                        rs.getString(9), rs.getString(10))
                );
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<mMedioPago> getMPagos() {

        //Filtro 0 se usa query original -- 1 se filtra con los tipos de pago solo efectivo
        List<mMedioPago> list = new ArrayList<>();
        mMedioPago medioPago;
        Connection con = conn;

        final String query = "call sp_getMedios_Pago(?)";
        if (con != null) {
            try {
                CallableStatement ps = con.prepareCall(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);

                ps.execute();
                ResultSet rs = ps.getResultSet();

                while (rs.next()) {
                    medioPago = new mMedioPago();
                    medioPago.setiIdMedioPago(rs.getInt(1));
                    medioPago.setcDescripcionMedioPago(rs.getString(2));
                    medioPago.setiIdTipoPago(rs.getInt(3));
                    medioPago.setIdImagen(rs.getString(4));
                    medioPago.setPorCobrar(rs.getBoolean(5));
                    medioPago.setEstadoModificador(rs.getBoolean(6));
                    medioPago.setcCodigoMedioPago(rs.getString(7));
                    medioPago.setdValorMinimo(rs.getBigDecimal(8));
                    medioPago.setbEsEfectivo(rs.getBoolean(9));
                    medioPago.setbActivaraCamara(rs.getBoolean(10));
                    list.add(medioPago);

                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            }
        } else {
            list = null;
        }
        return list;
    }

    public String getSaldoCliente(int idCliente) {

        String saldo = " ";
        PreparedStatement ps = null;


        if (conn != null) {

            try {
                ps = conn.prepareStatement("select m_monto from saldo_Cliente where i_id_cliente=? and i_id_company=?");
                ps.setInt(1, idCliente);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.execute();

                ResultSet rs = ps.getResultSet();

                while (rs.next()) {
                    saldo = Constantes.DivisaPorDefecto.SimboloDivisa + String.format("%.2f", rs.getBigDecimal(1));
                }

                rs.close();
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
                saldo = null;
            }
        } else {
            saldo = null;
        }

        return saldo;

    }

    public mRespuestaVenta GenerarVenta(int idCabeceraPedido, BigDecimal cambio, int idTipoDoc, boolean GeneraFac,
                                        int idTipoAtencion, BigDecimal montoPromocion, String observacion) {
        mRespuestaVenta r = new mRespuestaVenta();
        mCabeceraVenta c = new mCabeceraVenta();
        int respuesta = 0;
        CallableStatement cs = null;
        r.getList().clear();
        // List<ProductoEnVenta> lista=new ArrayList<>();
        int i4 = idTipoDoc;
        if (conn != null) {
            try {

                cs = conn.prepareCall("{call sp_Generar_Venta_V16(" + ParamStoreProcedure(45) + ")}");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Terminal.idTerminal);
                cs.setInt(3, Constantes.Tienda.idTienda);
                cs.setInt(4, idCabeceraPedido);
                cs.setInt(5, Constantes.Usuario.idUsuario);
                cs.setBigDecimal(6, cambio);
                cs.setInt(7, GenerarFecha());
                cs.registerOutParameter(8, Types.INTEGER);
                cs.setInt(9, idTipoDoc);
                cs.registerOutParameter(10, Types.INTEGER);
                cs.registerOutParameter(11, Types.VARCHAR);
                cs.registerOutParameter(12, Types.VARCHAR);
                cs.registerOutParameter(13, Types.INTEGER);
                cs.registerOutParameter(14, Types.DECIMAL);
                cs.registerOutParameter(15, Types.DECIMAL);
                cs.registerOutParameter(16, Types.DECIMAL);
                cs.registerOutParameter(17, Types.DECIMAL);
                cs.registerOutParameter(18, Types.DECIMAL);
                cs.registerOutParameter(19, Types.DECIMAL);
                cs.registerOutParameter(20, Types.DECIMAL);
                cs.registerOutParameter(21, Types.DECIMAL);
                cs.registerOutParameter(22, Types.DECIMAL);
                cs.registerOutParameter(23, Types.INTEGER);
                cs.registerOutParameter(24, Types.DECIMAL);
                cs.registerOutParameter(25, Types.VARCHAR);
                cs.registerOutParameter(26, Types.VARCHAR);
                cs.registerOutParameter(27, Types.VARCHAR);
                cs.registerOutParameter(28, Types.VARCHAR);
                cs.registerOutParameter(29, Types.VARCHAR);
                cs.registerOutParameter(30, Types.VARCHAR);
                cs.registerOutParameter(31, Types.INTEGER);
                cs.registerOutParameter(32, Types.VARCHAR);
                cs.setInt(33, idTipoAtencion);
                cs.registerOutParameter(34, Types.VARCHAR);

                cs.registerOutParameter(35, 12);
                cs.setBigDecimal(36, montoPromocion);
                cs.registerOutParameter(37, 12);
                cs.registerOutParameter(38, 12);
                cs.registerOutParameter(39, Types.INTEGER);
                cs.registerOutParameter(40, Types.VARCHAR);
                cs.registerOutParameter(41, Types.INTEGER);
                cs.registerOutParameter(42, Types.INTEGER);
                cs.registerOutParameter(43, Types.VARCHAR);
                cs.setString(44, observacion);
                cs.registerOutParameter(45, Types.VARCHAR);

                ResultSet rs = cs.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        ProductoEnVenta p = new ProductoEnVenta();
                        p.setCodigoProducto(rs.getString(1));
                        p.setProductName(rs.getString(2));
                        p.setCodUnidSunat(rs.getString(3));
                        p.setCantidad(rs.getFloat(4));
                        p.setValorUnitario(rs.getBigDecimal(5));
                        p.setPrecioOriginal(rs.getBigDecimal(6));//precioItem 8590.40
                        p.setPrecioNeto(rs.getBigDecimal(7));
                        p.setMontoIgv(rs.getBigDecimal(8));
                        p.setPrecioVentaFinal(rs.getBigDecimal(9));//montoItem 6169.49
                        p.setMontoDescuento(rs.getBigDecimal(10));
                        p.setEsVariante(rs.getBoolean(11));
                        r.getList().add(p);
                        //   lista.add(p);
                    }

                    // r.setList(lista);
                }
                c.setRucEmisor(Constantes.Empresa.NumRuc);
                c.setIdVenta(cs.getInt(23));
                c.setIdComprobantePagoSunat(cs.getInt(10));
                c.setFechaEmision(cs.getString(11));
                c.setNumSerie(cs.getString(12));
                c.setNumCorrelativo(cs.getInt(13));
                c.setTotalIgv(cs.getBigDecimal(14));
                c.setTotalGravado(cs.getBigDecimal(15));
                c.setTotalAnticipo(cs.getBigDecimal(16));
                c.setTotalDescuento(cs.getBigDecimal(17));
                c.setTotalExonerada(cs.getBigDecimal(18));
                c.setTotalGratuita(cs.getBigDecimal(19));
                c.setTotalInafecta(cs.getBigDecimal(20));
                c.setTotalOtrosCargos(cs.getBigDecimal(21));
                c.setDescuentoGlobal(cs.getBigDecimal(22));
                c.setTotalPagado(cs.getBigDecimal(24));
                c.getVendedor().setPrimerNombre(cs.getString(25));
                c.setFechaVenta(cs.getString(26));
                c.getCliente().setRazonSocial(cs.getString(27));
                c.getCliente().setNumeroRuc(cs.getString(28));
                c.getCliente().setcTipoDocumento(cs.getString(29));
                c.getCliente().setcDireccion(cs.getString(30));
                c.getCliente().setIdTipoDocSunat(cs.getInt(31));
                c.getCliente().setcEmail(cs.getString(32));
                c.setObservacion(observacion);
                c.setIdTipoDocPago(i4);
                r.setCodeRespuesta(cs.getInt(34));
                r.setMensaje(cs.getString(35));
                r.setCabeceraVenta(c);
                c.setFechaV2(cs.getString(38));
                c.setCodDocPago(cs.getString(37));
                c.setIdTipoDocPago(idTipoDoc);
                r.setCabeceraVenta(c);
                respuesta = cs.getInt(8);
                r.setValorRespuesta(respuesta);

                r.getCliente().setiId(cs.getInt(39));
                r.getCliente().setRazonSocial(cs.getString(40));
                r.getCliente().setIdTipoDocumento(cs.getInt(41));
                r.getCliente().setIdTipoDocSunat(cs.getInt(42));
                r.getCliente().setNumeroRuc(cs.getString(43));
                r.setFechaEntregaTemp(cs.getString(45));

            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("Error venta", e.toString());
                respuesta = 0;

                r.setValorRespuesta(respuesta);
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } else {
            respuesta = 0;
            r.setValorRespuesta(respuesta);
        }


        return r;
    }

    /*
    public List<mVenta> getCabeceraVentaList(int FechaInicio, int FechaFinal, int idCliente) {
        List<mVenta> list = new ArrayList<>();
        mVenta venta;
        PreparedStatement ps = null;

        String query = "select cv.iId_Cabecera_Venta,cv.iId_Cliente,cv.cNombre_Cliente," +
                "cv.iId_Vendedor,cv.cNombre_Vendedor" +
                ",cv.cEstadoVenta,cv.dTotal_Neto_Venta,cv.FechaVentaRealizada-'5:00'" +
                ",tp.cDescripcion_Visible,cv.cNumeroSerieDocumentoSunat,isnull(cv.cNumero_Correlativo,'') " +
                " from Cabecera_Venta as cv " +
                "inner join Tipo_Documento_Pago as tp on cv.id_tipoDocumentoPago=tp.iIdTipoDocumento " +
                "where iId_Company=? and cv.iId_Tienda=? " +
                "and cv.cFechaEmision BETWEEN ? and ? ";

        final String filtroCliente = " and iId_Cliente=? ";
        if (idCliente != 0) {
            query = query + filtroCliente;
        }
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query + " order by FechaVentaRealizada desc");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, FechaInicio);
                ps.setInt(4, FechaFinal);
                if (idCliente != 0) {
                    ps.setInt(5, idCliente);
                }
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    venta = new mVenta();
                    venta.setIdCabeceraVenta(rs.getInt(1));
                    venta.setIdCliente(rs.getInt(2));
                    venta.setNombreCliente(rs.getString(3));
                    venta.setIdVendedor(rs.getInt(4));
                    venta.setNombreVendedor(rs.getString(5));
                    venta.setcEstadoVenta(rs.getString(6));
                    venta.setTotalNeto(rs.getBigDecimal(7));
                    venta.setFechaVentaRealizada(rs.getTimestamp(8));
                    venta.setDescripcionDocumento(rs.getString(9));
                    venta.setNumeroSerie(rs.getString(10));
                    venta.setNumeroCorrelativo(rs.getString(11));
                    list.add(venta);

                }

            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            }
        } else {
            list = null;
        }
        return list;
    }
*/
    public List<mVenta> getCabeceraVentaList(int FechaInicio, int FechaFinal, int idCliente) {
        List<mVenta> list = new ArrayList<>();
        mVenta venta;
        CallableStatement ps = null;

        if (conn != null) {
            try {
                ps = conn.prepareCall("call sp_obtener_cabeceras_ventas(" + ParamStoreProcedure(5) + ")");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, FechaInicio);
                ps.setInt(4, FechaFinal);
                ps.setInt(5, idCliente);

                ps.execute();
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    venta = new mVenta();
                    venta.setIdCabeceraVenta(rs.getInt(1));
                    venta.setIdCliente(rs.getInt(2));
                    venta.setNombreCliente(rs.getString(3));
                    venta.setIdVendedor(rs.getInt(4));
                    venta.setNombreVendedor(rs.getString(5));
                    venta.setcEstadoVenta(rs.getString(6));
                    venta.setTotalNeto(rs.getBigDecimal(7));
                    venta.setFechaVentaRealizada(rs.getTimestamp(8));
                    venta.setDescripcionDocumento(rs.getString(9));
                    venta.setNumeroSerie(rs.getString(10));
                    venta.setNumeroCorrelativo(rs.getString(11));
                    list.add(venta);

                }

            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            }
        } else {
            list = null;
        }
        return list;
    }

    public byte VerificarAperturaCaja() {

        byte estado = 0;
        //Consultar si la caja esta aperturada
        //2 si la caja esta abierta
        //1 no encuentra caja abierta
        //0 sin conexion

        PreparedStatement ps = null;
        Connection con = getConnection();
        final String query = "select id_cierre from cierres where id_Company=? and id_Tienda=? and cEstado_Cierre=? and cEliminado!=?";
        final String estadoEliminado = "E";
        final String estadoActivo = "A";
        if (con != null) {

            try {
                ps = con.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setString(3, estadoActivo);
                ps.setString(4, estadoEliminado);
                ps.execute();

                ResultSet rs = ps.getResultSet();

                if (!rs.next()) {

                    estado = 10;
                } else {
                    do {

                    } while (rs.next());
                    estado = 20;
                }

                ps.close();
                con.close();
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                estado = 0;

            }


        } else {

            estado = 0;
        }
        return estado;

    }

    public byte getEstadoVenta(int idCabeceraVenta) {
        byte estado = 0;
        String EstadoVenta = "";
        PreparedStatement ps = null;
        final String query = "select cEstadoVenta from Cabecera_Venta where iId_Cabecera_Venta=? and iId_Company=?";
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, idCabeceraVenta);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    EstadoVenta = rs.getString(1);
                }
                if (EstadoVenta.equals("N")) {
                    estado = 2;
                } else if (EstadoVenta.equals("C")) {
                    estado = 1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                estado = 0;
            }
        } else {

            estado = 0;
        }
        return estado;
    }

    public List<mDetalleVenta> getListDetalleVenta(int idCabeceraVenta) {

        PreparedStatement ps = null;
        List<mDetalleVenta> list = new ArrayList<>();
        mDetalleVenta detalleVenta;
        final String query = "select cProductName,iCantidad,dPrecio_Subtotal,cDescripcion_Variante,bEsVariante," +
                "bCabeceraPack,bDetallePack,bEs_Modificado,cDescripcion_Modificador,dPrecioVentaUnidad " +
                "from Detalle_Venta where iIdCabecera_Venta=? order by iNum_Item asc";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idCabeceraVenta);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                detalleVenta = new mDetalleVenta();
                detalleVenta.setProductName(rs.getString(1));
                detalleVenta.setCantidad(rs.getInt(2));
                detalleVenta.setPrecioSubtotal(rs.getBigDecimal(3));
                detalleVenta.setcDecripcionVariante(rs.getString(4));
                detalleVenta.setEsVariante(rs.getBoolean(5));
                detalleVenta.setEsCabeceraPack(rs.getBoolean(6));
                detalleVenta.setEsDetallePack(rs.getBoolean(7));
                //detalleVenta.setEsModificado(rs.getBoolean(8));
                detalleVenta.setPrecioVentaUnidad(rs.getBigDecimal(10));
                list.add(detalleVenta);

            }

            list.size();

        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    public mCabeceraVenta getCabeceraVentaID(int idCabeceraVenta) {

        mCabeceraVenta cv = new mCabeceraVenta();

        PreparedStatement ps = null;
        final String query = "select iId_Cabecera_Venta,iId_Cliente," +
                "cNombre_Cliente,iId_Vendedor,cNombre_Vendedor," +
                "isnull(dTotal_Bruto_Venta,0),isnull(dTotal_descuento,0)," +
                "isnull(dTotal_Neto_Venta,0),cEstadoVenta,isnull(dCambio_Venta,0)" +
                ",FechaVentaRealizada-'5:00'," +
                "isnull(mc.cRazonSocial,''),isnull(mc.cNumeroRuc,''), " +
                "isnull(mv.cPrimerNombre+' '+mc.cApellidoPaterno+' '+mc.cApellidoPaterno,'') " +
                ",format(cv.FechaVentaRealizada-'5:00','dd/MM/yyyy hh:mm tt') " +
                ",isnull(cv.id_TipoDocumentoPago,0),isnull(cv.cNumeroSerieDocumentoSunat,''), " +
                "isnull(cv.cNumero_Correlativo,''),isnull(cv.dTotal_Gravado,0),isnull(cv.dTotal_Igv,0)," +
                " isnull(cNumeroSerieDocumentoSunat,''),isnull(cNumero_Correlativo,'')," +
                " convert(int,isnull(cNumero_Correlativo,'0'))," +
                " tp.cDescripcion_Visible," +
                " isnull(cp.cIdentificador_Pedido,'' ),permitir_anular" +
                " from Cabecera_Venta as cv (nolock) " +
                " inner join cabecera_pedido as cp on" +
                " cv.iId_cabecera_pedido=cp.iId_cabecera_pedido " +
                " and cp.iIdCompany=cv.iId_company " +
                " inner join Tipo_Documento_Pago as tp on " +
                " cv.id_tipoDocumentoPago=tp.iIdTipoDocumento " +
                " left join maestrocliente as mc (nolock) " +
                " on mc.iIdCliente=cv.iId_Cliente " +
                " left join maestrovendedor as mv (nolock) " +
                "on mv.iIdVendedor=cv.iId_vendedor where iId_Cabecera_Venta=? and iId_Company=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idCabeceraVenta);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                cv.setIdVenta(rs.getInt(1));
                cv.getCliente().setiId(rs.getInt(2));
                cv.getVendedor().setIdVendedor(rs.getInt(4));
                cv.setTotalBruto(rs.getBigDecimal(6));
                cv.setTotalDescuento(rs.getBigDecimal(7));
                cv.setTotalPagado(rs.getBigDecimal(8));
                cv.setEstadoVenta(rs.getString(9));
                cv.setTotalCambio(rs.getBigDecimal(10));
                cv.getCliente().setNumeroRuc(rs.getString(13));
                cv.getCliente().setRazonSocial(rs.getString(12));
                cv.getVendedor().setPrimerNombre(rs.getString(14));
                cv.setFechaEmision(rs.getString(15));
                cv.setFechaVenta(rs.getString(15));
                cv.setIdTipoDocPago(rs.getInt(16));
                cv.setNumSerie(rs.getString(17));
                cv.setNumeroCorrelativo(rs.getString(18));
                cv.setTotalGravado(rs.getBigDecimal(19));
                cv.setTotalIgv(rs.getBigDecimal(20));
                cv.setNumSerie(rs.getString(21));
                cv.setNumeroCorrelativo(rs.getString(22));
                cv.setNumCorrelativo(rs.getInt(23));
                cv.setTipoDocumento(rs.getString(24));
                cv.setIdentificador(rs.getString(25));
                cv.setAnulado(rs.getBoolean(26));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cv;
    }


    public mCierre getCabeceraCierreCaja(int idCierre) {
        mCierre cierre = new mCierre();
        PreparedStatement ps = null;

        final String query = "select id_cierre,cEstado_cierre,dFecha_Apertura-'5:00',dFecha_Cierre-'5:00' from Cierres where ";
        String filtro = "";
        if (conn != null) {

            try {
                if (idCierre == 0) {
                    filtro = "id_company=? and id_tienda=? and cEstado_Cierre='A'";
                } else if (idCierre != 0) {
                    filtro = "id_cierre=?";
                }
                ps = conn.prepareStatement(query + filtro);
                if (idCierre == 0) {
                    ps.setInt(1, Constantes.Empresa.idEmpresa);
                    ps.setInt(2, Constantes.Tienda.idTienda);
                } else if (idCierre != 0) {
                    ps.setInt(1, idCierre);
                }
                ps.execute();

                ResultSet rs = ps.getResultSet();

                while (rs.next()) {

                    cierre.setIdCierre(rs.getInt(1));
                    cierre.setEstadoCierre(rs.getString(2));
                    cierre.setFechaApertura(rs.getTimestamp(3));
                    cierre.setFechaCierre(rs.getTimestamp(4));

                }


                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                cierre = null;
            }

        } else {
            cierre = null;
        }

        return cierre;
    }

    public RetornoCancelar cancelarVenta(int idCabeceraVenta) {

        byte respuesta = 0; // 0 sin conexion 1 error  2 exito
        RetornoCancelar retornoCancelar = new RetornoCancelar();
        CallableStatement cs = null;
        final String procedure = "call sp_cancelar_venta(?,?,?,?,?,?,?,?,?)";
        if (conn != null) {

            try {
                cs = conn.prepareCall(procedure);
                cs.setInt(1, idCabeceraVenta);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Usuario.idUsuario);
                cs.setInt(4, Constantes.Terminal.idTerminal);
                cs.registerOutParameter(5, Types.TINYINT);
                cs.setInt(6, Constantes.Tienda.idTienda);
                cs.registerOutParameter(7, Types.TIMESTAMP);
                cs.registerOutParameter(8, Types.TIMESTAMP);
                cs.setString(9, Constantes.Empresa.nombrePropietario);
                cs.execute();
                retornoCancelar.setRespuesta(cs.getByte(5));
                retornoCancelar.setFechaApertura(cs.getTimestamp(7));
                retornoCancelar.setFechaCierre(cs.getTimestamp(8));
                retornoCancelar.getRespuesta();


            } catch (SQLException e) {
                e.printStackTrace();
                retornoCancelar = new RetornoCancelar();
                retornoCancelar.setRespuesta((byte) 1);


            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } else {
            retornoCancelar = new RetornoCancelar();
            retornoCancelar.setRespuesta((byte) 0);

        }

        retornoCancelar.setIdCabeceraVenta(idCabeceraVenta);

        return retornoCancelar;
    }

    public RetornoPagoTemporal GuardarPagoTemporal(int idCabeceraPedido, mPagosEnVenta pagosEnVenta) {
        byte respuesta = 0;
        CallableStatement cs = null;
        final String query = "call sp_guardar_pagos_temporales(?,?,?,?,?,?,?,?)";
        RetornoPagoTemporal retornoPagoTemporal = new RetornoPagoTemporal();
        if (conn != null) {

            try {
                cs = conn.prepareCall(query);
                cs.setInt(1, idCabeceraPedido);
                cs.setInt(2, pagosEnVenta.getIdTipoPago());
                cs.setString(3, pagosEnVenta.getcTipoPago());
                cs.setString(4, pagosEnVenta.getTipoPago());
                cs.setBigDecimal(5, pagosEnVenta.getCantidadPagada());
                cs.setInt(6, Constantes.Empresa.idEmpresa);
                cs.registerOutParameter(7, Types.BIT);
                cs.registerOutParameter(8, Types.TINYINT);


                cs.execute();
                respuesta = cs.getByte(8);
                retornoPagoTemporal.setRespuesta(respuesta);
                retornoPagoTemporal.setEsEfectivo(cs.getBoolean(7));
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 0;
                retornoPagoTemporal.setRespuesta((byte) 0);
                retornoPagoTemporal.setEsEfectivo(false);
                Log.i("e-pago", e.toString());
            }

        } else {
            retornoPagoTemporal.setRespuesta((byte) 0);
        }


        return retornoPagoTemporal;


    }

    public List<mMotivo_Ingreso_Retiro> getMotivoIngresoRetiro(String tipo) {

        //tipo= 1 ingreso  2 retiro
        byte tipoRegistro = 0;

        if (tipo.equals("Entrada")) {
            tipoRegistro = 1;
        } else if (tipo.equals("Retiro")) {
            tipoRegistro = 2;
        }

        List<mMotivo_Ingreso_Retiro> list = new ArrayList<>();
        PreparedStatement ps = null;
        final String query = "select id_motivo,cDescripcion_Motivo from Motivo_Ingreso_Retiro where iTipo_Registro=?";


        if (conn != null) {

            try {
                ps = conn.prepareStatement(query);
                ps.setByte(1, tipoRegistro);
                ps.execute();

                ResultSet rs = ps.getResultSet();


                while (rs.next()) {

                    list.add(new mMotivo_Ingreso_Retiro(rs.getInt(1), rs.getString(2)));


                }

                rs.close();
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            }

        } else {

            list = null;
        }
        list.size();
        return list;
    }

    public List<mPagosEnVenta> getPagosRealizados(int idCabeceraPedido) {

        List<mPagosEnVenta> list = new ArrayList<>();
        mPagosEnVenta pagosEnVenta;
        PreparedStatement ps = null;
        // Connection con = getConnection();
        final String query = "select p.id_Pago_Temporal,p.id_Cabecera_Pedido,p.id_Metodo_Pago,mp.cCodigo_Medio_Pago" +
                " ,mp.cDescripcion_Medio_Pago,p.m_Cantidad_Pagada,tp.bEsEfectivo  " +
                " from Pagos_Temporales as p inner join  Medio_Pago as mp on " +
                " p.id_Metodo_Pago=mp.iId_Medio_Pago  inner join  Tipo_Pago as tp on" +
                " mp.iId_Tipo_Pago=tp.iId_Tipo_Pago where id_company=? and id_Cabecera_Pedido=? and  cEstado_Pago_Temporal=?";
        final String estadoNormal = "N";
        if (conn != null) {
            try {
                CallableStatement cs = conn.prepareCall("call sp_pagos_pedido_get(" + ParamStoreProcedure(2) + ")");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, idCabeceraPedido);
                cs.executeQuery();
/*
                ps = conn.prepareStatement(query);
                ps.setInt(2, idCabeceraPedido);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(3, estadoNormal);
                ps.execute();
*/
                ResultSet rs = cs.getResultSet();

                while (rs.next()) {
                    pagosEnVenta = new mPagosEnVenta();
                    pagosEnVenta.setIdTipoPago(rs.getInt(3));
                    pagosEnVenta.setcTipoPago(rs.getString(4));
                    pagosEnVenta.setTipoPago(rs.getString(5));
                    pagosEnVenta.setCantidadPagada(rs.getBigDecimal(6));
                    pagosEnVenta.setEsEfectivo(rs.getBoolean(7));
                    pagosEnVenta.setActivaPagoExterno(rs.getBoolean(8));
                    list.add(pagosEnVenta);

                }
                rs.close();
                cs.close();
                //    ps.close();


            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

        } else {
            list = null;
        }

        return list;
    }

    public List<mDetalleMovCaja> getMovimientoCaja(int idCierre) {

        //Obtener los movimientos de la caja segun el cierre requerido
        List<mDetalleMovCaja> list = new ArrayList<>();
        PreparedStatement ps = null;
        final String query = "select mc.iTipo_registro,mr.cDescripcion_Motivo," +
                "mp.cDescripcion_Medio_Pago,mc.cDescipcion_movimiento,mc.dFecha_Movimiento-'5:00'," +
                "mc.mMonto from Movimiento_Caja mc inner join Medio_Pago as mp on " +
                "mc.id_medio_pago=mp.iId_Medio_Pago inner join Motivo_Ingreso_Retiro as " +
                "mr on mc.id_motivo=mr.id_motivo where mc.id_cierre_caja=?";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idCierre);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            int count = 0;
            while (rs.next()) {

                list.add(new mDetalleMovCaja());
                list.get(count).setTipoRegistro(rs.getByte(1));
                list.get(count).setDescripcionMotivo(rs.getString(2));
                list.get(count).setNombreMedioPago(rs.getString(3));
                list.get(count).setDescripcion(rs.getString(4));
                list.get(count).setFechaTransaccion(rs.getTimestamp(5));
                list.get(count).setMonto(rs.getBigDecimal(6));
                count++;
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<mPagosEnVenta> getPagosRealizadosDetallePedido(int idCabeceraPedido) {

        List<mPagosEnVenta> list = new ArrayList<>();
        mPagosEnVenta pagosEnVenta;
        PreparedStatement ps = null;
        final String estadoNormal = "N";
        if (conn != null) {
            try {
                CallableStatement cs = conn.prepareCall("call sp_pagos_pedido_frm_detalle_get(" + ParamStoreProcedure(2) + ")");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, idCabeceraPedido);
                cs.executeQuery();
/*
                ps = conn.prepareStatement(query);
                ps.setInt(2, idCabeceraPedido);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(3, estadoNormal);
                ps.execute();
*/
                ResultSet rs = cs.getResultSet();

                if (rs != null) {

                    while (rs.next()) {
                        pagosEnVenta = new mPagosEnVenta();
                        pagosEnVenta.setIdTipoPago(rs.getInt(3));
                        pagosEnVenta.setcTipoPago(rs.getString(4));
                        pagosEnVenta.setTipoPago(rs.getString(5));
                        pagosEnVenta.setCantidadPagada(rs.getBigDecimal(6));
                        pagosEnVenta.setEsEfectivo(rs.getBoolean(7));
                        list.add(pagosEnVenta);
                    }
                }
                rs.close();
                cs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

        } else {
            list = null;
        }

        return list;
    }




    public byte EliminarPagoTemporalv2(int idCabeceraPedido, int idMetodoPago,int idPago) {

        PreparedStatement ps = null;

        byte resultado = 0;
        final String query = "delete Pagos_Temporales where id_Cabecera_Pedido=?  " +
                "and id_Metodo_Pago=? and id_company=? and id_Pago_Temporal=?";
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, idCabeceraPedido);
                ps.setInt(2, idMetodoPago);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, idPago);
                ps.executeUpdate();
                resultado = 1;
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 0;
            }
        } else {
            resultado = 0;
        }
        return resultado;
    }





    public byte EliminarPagoTemporal(int idCabeceraPedido, int idMetodoPago) {

        PreparedStatement ps = null;

        byte resultado = 0;
        final String query = "delete Pagos_Temporales where id_Cabecera_Pedido=?  and id_Metodo_Pago=? and id_company=?";
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, idCabeceraPedido);
                ps.setInt(2, idMetodoPago);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.executeUpdate();
                resultado = 1;
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 0;
            }
        } else {
            resultado = 0;
        }
        return resultado;
    }

    public List<mPagosEnVenta> getPagosVenta(int idCabeceraVenta) {

        List<mPagosEnVenta> list = new ArrayList<>();
        mPagosEnVenta pagosEnVenta;

        CallableStatement cs = null;

        if (conn != null) {

            try {

                cs = conn.prepareCall("call sp_obtener_pagos_venta(" + ParamStoreProcedure(2) + ")");
                cs.setInt(1, idCabeceraVenta);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                ResultSet rs = cs.executeQuery();
                while (rs.next()) {
                    pagosEnVenta = new mPagosEnVenta();
                    pagosEnVenta.setTipoPago(rs.getString(1));
                    pagosEnVenta.setCantidadPagada(rs.getBigDecimal(2));
                    list.add(pagosEnVenta);
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            list = null;

        }

        return list;
    }

    public byte EliminarPagosTemporales(int idCabeceraPedido) {

        PreparedStatement ps = null;

        byte resultado = 0;
        final String EstadoCancelado = "S";
        final String query = "update Pagos_Temporales set cEstado_Pago_Temporal=?" +
                "  where id_Cabecera_Pedido=? and id_company=?";
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setString(1, EstadoCancelado);
                ps.setInt(2, idCabeceraPedido);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.executeUpdate();
                resultado = 1;
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 0;
            }
        } else {
            resultado = 0;
        }
        return resultado;


    }

    public List<mSaldoCliente> getSaldosClientes(byte saldoCero, String nombreCliente) {

        List<mSaldoCliente> list = new ArrayList<>();


        CallableStatement cs = null;
        if (conn != null) {
            try {

                cs = conn.prepareCall("call sp_obtener_saldos_clientes(" + ParamStoreProcedure(3) + ")");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setByte(2, saldoCero);
                cs.setString(3, nombreCliente);

                ResultSet rs = cs.executeQuery();
                while (rs.next()) {
                    mSaldoCliente saldo = new mSaldoCliente(rs.getInt(1),
                            rs.getInt(2), rs.getString(7), rs.getString(4), rs.getBigDecimal(5));

                    list.add(saldo);

                }

                rs.close();


            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            } finally {

                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        } else {
            list = null;
        }

        return list;
    }

    public List<DetalleCuentaCorriente> getDetalleCuentaCorriente(int idCliente) {

        List<DetalleCuentaCorriente> list = new ArrayList<>();
        DetalleCuentaCorriente detalleCuentaCorriente;
        final String query = "select cta.id_CtaCte_Cliente," +
                "tp.c_Tipo_Transaccion,cta.dFecha_Creacion," +
                "cta.id_Cabecera_Venta,cta.mMonto," +
                "cta.id_Transaccion,tp.cod_Tipo_Transaccion,cta.cObservacion" +
                ",mp.cDescripcion_medio_pago,cta.cod_Estado_Cta_Cte from" +
                " Cuenta_Corriente_Cliente as cta" +
                " inner join Tipo_Transaccion as tp on cta.cod_Tipo_Transaccion=tp.cod_Tipo_Transaccion " +
                " left join Pagos as p on cta.id_Transaccion=p.id_Pago " +
                "left join Medio_Pago as mp on " +
                "cta.id_Medio_Pago=mp.iId_Medio_Pago where cta.id_company=? " +
                "and cta.id_cliente=?  order by cta.dFecha_Creacion desc";
        PreparedStatement ps = null;

        if (conn != null) {

            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idCliente);
                ps.execute();

                ResultSet rs = ps.getResultSet();

                while (rs.next()) {

                    detalleCuentaCorriente = new DetalleCuentaCorriente();
                    detalleCuentaCorriente.setIdCtaCteCliente(rs.getInt(1));
                    detalleCuentaCorriente.setTipoTransaccion(rs.getString(2));
                    detalleCuentaCorriente.setFechaOrigen(rs.getTimestamp(3));
                    detalleCuentaCorriente.setIdCabeceraVenta(rs.getInt(4));
                    detalleCuentaCorriente.setMonto(rs.getBigDecimal(5));
                    detalleCuentaCorriente.setIdTransaccion(rs.getInt(6));
                    detalleCuentaCorriente.setIdTipoTransaccion(rs.getString(7));
                    detalleCuentaCorriente.setcObservacion(rs.getString(8));
                    detalleCuentaCorriente.setMetodoPago(rs.getString(9));
                    detalleCuentaCorriente.setEstadoCtaCte(rs.getString(10));
                    list.add(detalleCuentaCorriente);


                }

                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            }

        } else {

            list = null;

        }

        return list;

    }

    private byte VerificarTerminalImei(String codImei, Connection conn, String marca, String modelo, String versionAndroid) {
        byte estado = 100;
        CallableStatement cs = null;
        int idTerminal;
        int id;
        final String query = "call sp_ValidarIngresoTerminal(?,?,?,?,?,?)";

        if (conn != null) {

            try {

                cs = conn.prepareCall(query);
                cs.setString(1, codImei);
                cs.registerOutParameter(2, Types.TINYINT);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.setString(4, marca);
                cs.setString(5, modelo);
                cs.setString(6, versionAndroid);
                cs.execute();
                Constantes.Terminal.idTerminal = cs.getInt(3);
                idTerminal = Constantes.Terminal.idTerminal;
                estado = cs.getByte(2);

                cs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                estado = 100;
            }

        } else {
            estado = 100;
        }

        return estado;
    }

    public byte ProcesarPagoCtaCte(BigDecimal monto, int idMetodoPago, String cObservacion, int idCliente) {

        byte resultado = 0;
        byte c = 0;
        //resultado 101 pago exitoso   100 pago error  99 medio pago Eliminado 0 sin conexion
        CallableStatement cs = null;
        final String query = "call sp_procesarPagoCtaCte(?,?,?,?,?,?,?,?,?)";
        if (conn != null) {
            try {
                cs = conn.prepareCall(query);
                cs.setInt(1, idMetodoPago);
                cs.setBigDecimal(2, monto);
                cs.setInt(3, idCliente);
                cs.setInt(4, Constantes.Empresa.idEmpresa);
                cs.setInt(5, Constantes.Tienda.idTienda);
                cs.setInt(6, Constantes.Usuario.idUsuario);
                cs.setInt(7, Constantes.Terminal.idTerminal);
                cs.setString(8, cObservacion);
                cs.registerOutParameter(9, Types.TINYINT);
                cs.execute();
                resultado = cs.getByte(9);
                c = resultado;

                cs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 100;
            }
        } else {
            resultado = 0;
        }


        return resultado;
    }

    public byte CancelarPagoCtaCte(int idCtaCte, int idCliente) {
        byte resultado = 100;
        CallableStatement cs = null;
        final String query = "call sp_cancelarPagoCtaCte(?,?,?,?,?,?,?)";
        if (conn != null) {
            try {
                cs = conn.prepareCall(query);
                cs.setInt(1, idCtaCte);
                cs.setInt(2, Constantes.Usuario.idUsuario);
                cs.setInt(3, Constantes.Empresa.idEmpresa);
                cs.setInt(4, Constantes.Tienda.idTienda);
                cs.setInt(5, Constantes.Terminal.idTerminal);
                cs.setInt(6, idCliente);
                cs.registerOutParameter(7, Types.TINYINT);

                cs.execute();

                resultado = cs.getByte(7);
                cs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 100;
            }
        } else {

            resultado = 99;

        }

        return resultado;
    }

    public mCustomer getClienteId(int idcliente) {

        mCustomer customer = new mCustomer();

        CallableStatement cs = null;

        if (conn != null) {

            try {

                cs = conn.prepareCall("call sp_obtener_cliente_id(" + ParamStoreProcedure(5) + ")");
                cs.setInt(1, idcliente);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.registerOutParameter(4, Types.VARCHAR);
                cs.registerOutParameter(5, Types.CHAR);
                cs.execute();

                customer.setcName(cs.getString(3));
                customer.setcEmail(cs.getString(4));
                customer.setEstadoEliminado(cs.getString(5));


            } catch (SQLException e) {
                e.printStackTrace();
                customer = null;
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        } else {
            customer = null;
        }

        return customer;

    }

    public CtaCteCliente ObtenerCtaCteCorriente(int idCliente) {


        CallableStatement cs = null;
        BigDecimal monto = new BigDecimal(0);
        List<DetalleCuentaCorriente> list = new ArrayList<>();
        try {
            cs = conn.prepareCall("call sp_obtener_info_cuenta_corriente_cliente(" + ParamStoreProcedure(3) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, idCliente);
            cs.registerOutParameter(3, Types.DECIMAL);
            ResultSet rs = cs.executeQuery();

            if (rs != null) {

                while (rs.next()) {
                    DetalleCuentaCorriente detalleCuentaCorriente = new DetalleCuentaCorriente();
                    detalleCuentaCorriente.setIdCtaCteCliente(rs.getInt(1));
                    detalleCuentaCorriente.setTipoTransaccion(rs.getString(2));
                    detalleCuentaCorriente.setFechaOrigen(rs.getTimestamp(3));
                    detalleCuentaCorriente.setIdCabeceraVenta(rs.getInt(4));
                    detalleCuentaCorriente.setMonto(rs.getBigDecimal(5));
                    detalleCuentaCorriente.setIdTransaccion(rs.getInt(6));
                    detalleCuentaCorriente.setIdTipoTransaccion(rs.getString(7));
                    detalleCuentaCorriente.setcObservacion(rs.getString(8));
                    detalleCuentaCorriente.setMetodoPago(rs.getString(9));
                    detalleCuentaCorriente.setEstadoCtaCte(rs.getString(10));
                    list.add(detalleCuentaCorriente);
                }

            }
            monto = cs.getBigDecimal(3);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return new CtaCteCliente(monto, new ArrayList(list));

    }

    public List<mCategoriaProductos> getCategorias(int idCategoria, String nombreCategoria) {

        List<mCategoriaProductos> list = new ArrayList<>();


        if (conn != null) {
            try {


                CallableStatement cs = conn.prepareCall("call sp_Obtener_Categorias(?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.execute();
                ResultSet rs = cs.getResultSet();

                while (rs.next()) {
                    mCategoriaProductos categoriaProductos = new mCategoriaProductos();
                    categoriaProductos.setIdCategoria(rs.getInt(1));
                    categoriaProductos.setDescripcionCategoria(rs.getString(2));
                    categoriaProductos.setEstadoMod(rs.getBoolean(3));
                    list.add(categoriaProductos);

                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                list = new ArrayList<>();
            }

        } else {

            list = new ArrayList<>();
        }

        return list;

    }

    public List<mCategoriaProductos> getCategoriasVenta(int idCategoria, String nombreCategoria) {

        List<mCategoriaProductos> list = new ArrayList<>();


        if (conn != null) {
            try {


                CallableStatement cs = conn.prepareCall("call sp_Obtener_Categorias_venta(?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.execute();
                ResultSet rs = cs.getResultSet();

                while (rs.next()) {
                    mCategoriaProductos categoriaProductos = new mCategoriaProductos();
                    categoriaProductos.setIdCategoria(rs.getInt(1));
                    categoriaProductos.setDescripcionCategoria(rs.getString(2));
                    categoriaProductos.setEstadoMod(rs.getBoolean(3));
                    list.add(categoriaProductos);

                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                list = new ArrayList<>();
            }

        } else {

            list = new ArrayList<>();
        }

        return list;

    }

    public RetornoApertura aperturarCaja(BigDecimal monto) {

        RetornoApertura retornoApertura;
        byte resultado = 0;
        byte a = 0;
        int idCierre = 0;
        final String query = "call sp_generarAperturaCierre(?,?,?,?,?,?,?)";
        CallableStatement cs = null;

        if (conn != null) {

            try {
                cs = conn.prepareCall(query);
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, Constantes.Usuario.idUsuario);
                cs.setInt(4, Constantes.Terminal.idTerminal);
                cs.setBigDecimal(5, monto);
                cs.setByte(6, (byte) 99);
                cs.registerOutParameter(6, Types.TINYINT);
                cs.registerOutParameter(7, Types.INTEGER);
                cs.execute();
                resultado = cs.getByte(6);
                idCierre = cs.getInt(7);
                resultado = resultado;
                cs.close();
                retornoApertura = new RetornoApertura(resultado, idCierre);

            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
                retornoApertura = new RetornoApertura((byte) 99, 0);
            }

        } else {

            resultado = 0;
            retornoApertura = new RetornoApertura((byte) 0, 0);

        }

        return new RetornoApertura(resultado, idCierre);

    }

    public List<mResumenFlujoCaja> getResumenFlujoCaja(int idCierreCaja) {

        List<mResumenFlujoCaja> list = new ArrayList<>();
        mResumenFlujoCaja resumenFlujoCaja;
        PreparedStatement ps = null;
        CallableStatement cs = null;
        final String query = "SELECT  VistaFlujoMedioPago.cdescripcion,Flujo_Caja.mMonto,VistaFlujoMedioPago.titulo, " +
                " Titulo_Pago_Caja.cTitulo_Pago,VistaFlujoMedioPago.color,VistaFlujoMedioPago.[Subtitulo caja], dbo.VistaFlujoMedioPago.cSimbolo " +
                "FROM dbo.Flujo_Caja INNER JOIN " +
                " dbo.VistaFlujoMedioPago ON dbo.Flujo_Caja.cTipo_Registro = dbo.VistaFlujoMedioPago.tipo_registro AND dbo.Flujo_Caja.id_concepto_caja = dbo.VistaFlujoMedioPago.[codigo Flujo] INNER JOIN " +
                " dbo.Titulo_Pago_Caja ON dbo.VistaFlujoMedioPago.titulo = dbo.Titulo_Pago_Caja.id_titulo where id_cierre=?" +
                "ORDER BY dbo.VistaFlujoMedioPago.titulo, dbo.VistaFlujoMedioPago.orden";
        if (conn != null) {
            try {

                cs = conn.prepareCall("call  sp_calcular_saldo_Caja(?)");
                cs.setInt(1, idCierreCaja);
                cs.execute();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idCierreCaja);
                ps.execute();
                ResultSet resultSet = ps.getResultSet();
                while (resultSet.next()) {
                    resumenFlujoCaja = new mResumenFlujoCaja();

                    resumenFlujoCaja.setDescripcionTitulo(resultSet.getString(1));
                    resumenFlujoCaja.setMonto(resultSet.getBigDecimal(2));
                    resumenFlujoCaja.setCodtitulo(resultSet.getString(3));
                    resumenFlujoCaja.setTitutloPago(resultSet.getString(4));
                    resumenFlujoCaja.setCodColor(resultSet.getString(5));
                    resumenFlujoCaja.setSubtituloCaja(resultSet.getString(6));
                    resumenFlujoCaja.setSimbolo(resultSet.getString(7));
                    list.add(resumenFlujoCaja);
                }


                resultSet.close();
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            }
        } else {
            list = null;
        }

        return list;
    }

    public BigDecimal ObtenerMontoAperturaCierre(int idCierre) {

        PreparedStatement ps = null;
        BigDecimal montoApertura = new BigDecimal(0);
        BigDecimal a;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("select isnull( SUM(mMonto),0) from Movimiento_Caja where id_company=? and \n" +
                    "iTipo_registro=1 and id_motivo=7 and id_cierre_caja=? ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, idCierre);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                montoApertura = rs.getBigDecimal(1);
                a = rs.getBigDecimal(1);
                a.floatValue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            montoApertura = new BigDecimal(-99);
        } finally {
            try {

                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return montoApertura;
    }

    public List<mCierre> getCierresHistorial(String fechaInicio, String fechaFinal) {

        PreparedStatement ps = null;
        List<mCierre> list = new ArrayList<>();
        mCierre cierre;
        final String query = "select id_cierre,dFecha_apertura-'5:00',dFecha_cierre-'5:00',cEstado_cierre," +
                "(select count(iId_Cabecera_Venta) \n" +
                "from Cabecera_Venta where iId_Company=c.id_company and id_cierre=c.id_cierre and \n" +
                "cEstadoVenta='N' and cEliminado=' ') \n" +
                "as numTransacciones " +
                " from" +
                " Cierres as c where  cEliminado!='E' and id_company=? and  id_tienda=? and  dFecha_apertura-'5:00' " +
                " between cast(? as datetime)-'5:00' and cast(? as datetime) order by dFecha_apertura desc ";


        if (conn != null) {

            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setString(3, fechaInicio);
                ps.setString(4, fechaFinal + " 23:59:59");
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {

                    cierre = new mCierre();
                    cierre.setIdCierre(rs.getInt(1));
                    cierre.setFechaApertura(rs.getTimestamp(2));
                    cierre.setFechaCierre(rs.getTimestamp(3));
                    cierre.setEstadoCierre(rs.getString(4));
                    cierre.setNumTransacciones(rs.getInt(5));

                    list.add(cierre);

                }
                rs.close();
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            }

        } else {
            list = null;

        }
        return list;
    }

    public List<mMedioPago> getMedioPagoEfectivo() {

        PreparedStatement ps = null;
        List<mMedioPago> list = new ArrayList<>();
        mMedioPago medioPago;
        final String query = "select iId_Medio_Pago,cDescripcion_Medio_Pago from Medio_Pago where (iIdCompany=0 or iIdCompany=?) and iId_Tipo_Pago=100";
        if (conn != null) {

            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.execute();

                ResultSet rs = ps.getResultSet();
                while (rs.next()) {

                    medioPago = new mMedioPago();
                    medioPago.setiIdMedioPago(rs.getInt(1));
                    medioPago.setcDescripcionMedioPago(rs.getString(2));
                    list.add(medioPago);
                }


                ps.close();
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else {

            list = null;

        }

        return list;
    }

    public List<mResumenMedioPago> getResumenMP(int idCierre) {

        List<mResumenMedioPago> list = new ArrayList<>();

        PreparedStatement ps = null;

        ResultSet rs = null;
        final String query = "select mp.iId_Medio_Pago,mp.cDescripcion_Medio_Pago ,rm.mMonto_Entrada,mMonto_Salida," +
                " mMonto_Saldo_Disponible from Resumen_Medio_Pago_Cierre as " +
                " rm inner join Medio_Pago as mp on rm.id_Medio_Pago=mp.iId_Medio_Pago where id_cierre=?";
        if (conn != null) {
            try {


                ps = conn.prepareStatement(query);
                ps.setInt(1, idCierre);
                ps.execute();

                rs = ps.getResultSet();
                while (rs.next()) {

                    list.add(new mResumenMedioPago(rs.getInt(1), rs.getString(2),
                            rs.getBigDecimal(3), rs.getBigDecimal(4), rs.getBigDecimal(5)));

                }

            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            } finally {
                try {

                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } else {

            list = null;
        }


        return list;
    }

    public List<mVentasPorHora> getVentasPorHora(int idCierre) {

        List<mVentasPorHora> list = new ArrayList<>();
        PreparedStatement ps = null;

        final String query = "select [iHora],[mMonto],[iNum_Ventas] from Ventas_Cierre_Hora where id_cierre=? and mMonto!=0";
        if (conn != null)
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, idCierre);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {

                    list.add(new mVentasPorHora(rs.getInt(1), rs.getBigDecimal(2)));
                }
                list.size();
                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                list = new ArrayList<>();
            }
        return list;
    }

    public mResumenTotalVentas ObtenerCabeceraResumen(int idCierre) {

        mResumenTotalVentas resumenTotalVentas = new mResumenTotalVentas();
        CallableStatement cs = null;
        final String procedure = "call sp_actualizar_total_ventas(?,?,?,?)";
        if (conn != null) {
            try {
                cs = conn.prepareCall(procedure);
                cs.setInt(1, idCierre);
                /*
                cs.setBigDecimal(2, new BigDecimal(0));
                cs.setInt(3, 0);
                cs.setBigDecimal(4, new BigDecimal(0));
                */
                cs.registerOutParameter(2, Types.DECIMAL);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.registerOutParameter(4, Types.DECIMAL);

                cs.execute();

                resumenTotalVentas = new mResumenTotalVentas(cs.getInt(3), cs.getBigDecimal(2), cs.getBigDecimal(4));
                resumenTotalVentas.getNum_Ventas();


                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                resumenTotalVentas = null;
            }


        } else {
            resumenTotalVentas = null;
        }


        return resumenTotalVentas;
    }

    public byte AgregarIngresoRetiro(byte tipoRegistro, BigDecimal monto, int idMotivo, int idMedioPago, String descripcion, int idCierre) {
        //tipo Registro 1 ingreso. 2 Retiro
        //Validacion de Ingreso de retiro  en base de datos
        //Registro de movimientos de caja

        byte respuesta = 0;
        CallableStatement cs = null;
        final String query = "call sp_AgregarIngresoRetiro(?,?,?,?,?,?,?,?,?,?)";
        if (conn != null) {

            try {
                cs = conn.prepareCall(query);
                cs.setByte(1, tipoRegistro);
                cs.setBigDecimal(2, monto);
                cs.setInt(3, idCierre);
                cs.setInt(4, Constantes.Empresa.idEmpresa);
                cs.setInt(5, Constantes.Usuario.idUsuario);
                cs.setInt(6, Constantes.Terminal.idTerminal);
                cs.setInt(7, idMedioPago);
                cs.setInt(8, idMotivo);
                cs.setString(9, descripcion);
                cs.setByte(10, (byte) 0);
                cs.registerOutParameter(10, Types.TINYINT);

                cs.execute();

                respuesta = cs.getByte(10); //1 se realizo la transaccion//
                // 0 fallo la conexion o error al ejecutar//
                // 2 se verifico que el monto ingresado es mayor al disponible

                cs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 0;
            }

        } else {
            respuesta = 0;
        }
        return respuesta;
    }

    public mCierre ObtenerCierrePorId(int idCierre) {

        mCierre cierre = new mCierre();
        CallableStatement cs = null;

        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_Obtener_Estado_Cierre(?,?,?,?)");
                cs.setInt(1, idCierre);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Tienda.idTienda);
                cs.registerOutParameter(4, Types.CHAR);
                cs.execute();
                cierre.setIdCierre(idCierre);
                cierre.setEstadoCierre(cs.getString(4));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cierre;
    }

    public mCierre ObtenerIdCierre() {

        mCierre cierre = new mCierre();
        int id = 0;
        int c = 2;
        PreparedStatement ps = null;
        final String query = "select top(1)id_cierre,cEstado_Cierre from cierres where " +
                "id_company=? and id_tienda=? order by dFecha_apertura desc";
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    cierre.setIdCierre(rs.getInt(1));
                    cierre.setEstadoCierre(rs.getString(2));
                }
                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                cierre.setIdCierre(-1);
            }
        } else {
            cierre.setIdCierre(-2);
        }
        return cierre;
    }

    public byte CerrarCaja(int idCierre) {
        byte Respuesta = 0;
        CallableStatement cs = null;
        final String procedure = "call sp_cerrar_caja(?,?,?,?)";
        if (conn != null) {
            try {
                cs = conn.prepareCall(procedure);
                cs.setInt(1, Constantes.Usuario.idUsuario);
                cs.setInt(2, Constantes.Terminal.idTerminal);
                cs.setInt(3, idCierre);
                cs.setByte(4, (byte) 0);
                cs.registerOutParameter(4, Types.TINYINT);
                cs.execute();

                Respuesta = cs.getByte(4);

                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Respuesta = 1;
            }
        } else {
            Respuesta = 0;
        }
        return Respuesta;
    }

    public mProduct ObtenerConfiguracionVariante(int idProduct) {
        mProduct product = new mProduct();
        byte respuesta = 0;
        List<OpcionVariante> listOpcion = new ArrayList<>();
        OpcionVariante opcionVariante = new OpcionVariante();
        List<ValorOpcionVariante> varianteList = new ArrayList<>();
        ValorOpcionVariante valorOpcionVariante = new ValorOpcionVariante();
        CallableStatement cs = null;
        product.setIdProduct(idProduct);
        final String query = "call sp_obtenerConfiguracionVarianteProducto(?,?,?,?)";
        if (conn != null) {

            try {
                cs = conn.prepareCall(query);
                cs.setInt(1, idProduct);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.registerOutParameter(3, Types.BIT);
                cs.registerOutParameter(4, Types.TINYINT);
                cs.execute();
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    if (rs.getInt(1) == 1) {
                        opcionVariante = new OpcionVariante();
                        opcionVariante.setIdOpcionVariante(rs.getInt(2));
                        opcionVariante.setiNumIntem(rs.getInt(4));
                        opcionVariante.setDescripcion(rs.getString(5));
                        listOpcion.add(opcionVariante);


                    } else if (rs.getInt(1) == 2) {
                        valorOpcionVariante = new ValorOpcionVariante();
                        valorOpcionVariante.setIdOpcionVariante(rs.getInt(2));
                        valorOpcionVariante.setIdValor(rs.getInt(3));
                        valorOpcionVariante.setiNumItem(rs.getInt(4));
                        valorOpcionVariante.setDescripcion(rs.getString(5));
                        varianteList.add(valorOpcionVariante);

                    }
                }
                product.setEstadoVariante(cs.getBoolean(3));
                respuesta = cs.getByte(4);


                String cadena = "";
                product.setOpcionVarianteList(listOpcion);
                try {
                    for (int i = 0; i < product.getOpcionVarianteList().size(); i++) {
                        for (int a = 0; a < varianteList.size(); a++) {
                            if (product.getOpcionVarianteList().get(i).getIdOpcionVariante() == varianteList.get(a).getIdOpcionVariante()) {
                                product.getOpcionVarianteList().get(i).getListValores().add(varianteList.get(a));
                            }

                        }

                        for (ValorOpcionVariante valor : product.getOpcionVarianteList().get(i).getListValores()) {
                            cadena = cadena + valor.getDescripcion() + ",";
                        }

                        int longitud = cadena.length();
                        if (longitud > 0) {
                            cadena = cadena.substring(0, longitud - 1);
                        }
                        product.getOpcionVarianteList().get(i).setValores(cadena);
                        cadena = "";
                    /*    product.setVarianteList(getVariantesProducto(product.getIdProduct(), con));
                        product.setNumVariantes(product.getVarianteList().size());
*/

                    }
                } catch (Exception e) {
                    e.toString();
                }


                product.setVarianteList(getVariantesProducto(product.getIdProduct(), conn));
                product.setNumVariantes(product.getVarianteList().size());
                product.setIdProduct(idProduct);
                product.toString();

                cs.close();
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                product = null;
            }

        } else {
            product = null;
        }

        return product;

    }

    public byte ActualizarVariantePorId(Variante variante) {
        byte resultado = 0;
        CallableStatement ps = null;

        if (conn != null) {
            try {
                ps = conn.prepareCall("call sp_actualizar_variante(" + ParamStoreProcedure(7) + ")");
                ps.setBigDecimal(1, variante.getPrecioCompra());
                ps.setBigDecimal(2, variante.getPrecioVenta());
                ps.setString(3, variante.getCodigoBarra());
                ps.setBoolean(4, variante.isPVMultiple());
                ps.setInt(5, variante.getIdVariante());
                ps.setInt(6, Constantes.Empresa.idEmpresa);
                ps.setInt(7, Constantes.Tienda.idTienda);

                ps.execute();
                if (variante.isPVMultiple()) {
                    EliminarPreciosAdiccionales(variante.getIdVariante(), variante.getLista());

                    AgregarPreciosVentaAdiccionales(variante.getIdVariante(), variante.getLista());
                    variante.setPrecioVenta(variante.getLista().get(0).getAdditionalPrice());
                } else {
                    EliminarPreciosAdiccionales(variante.getIdVariante(), variante.getLista());
                }
                resultado = 100;
            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
                Log.e("QUERY_E", e.toString());
            }
        } else {
            resultado = 98;
        }
        return resultado;
    }

    public void ActualizarVisibilidadProductoTienda(int idProductTienda, boolean Visible) {
        try {
            CallableStatement cs = conn.prepareCall("call p_api_actualizar_visibilidad_producto_tienda(" + ParamStoreProcedure(3) + ")");
            cs.setInt("pIdProductTienda", idProductTienda);
            cs.setBoolean("pVisible", Visible);
            cs.setInt("pIdCompany", Constantes.Empresa.idEmpresa);
            cs.execute();
        } catch (Exception ex) {

            ex.toString();
        }

    }

    public ProductVisibilidadEnTiendas GetVisibilidaEnTiendasProducto(int idProduct) {
        ProductVisibilidadEnTiendas productVisibilidadEnTiendas = new ProductVisibilidadEnTiendas();
        try {
            CallableStatement cs = conn.prepareCall("call p_api_get_visibilidad_producto_tienda_venta(" + ParamStoreProcedure(2) + ")");
            cs.setInt("pidProduct", idProduct);
            cs.setInt("pidCompany", Constantes.Empresa.idEmpresa);
            cs.execute();

            ResultSet resultSet = cs.getResultSet();

            while (resultSet.next()) {
                Tienda tienda = new Tienda();
                tienda.setIdTienda(resultSet.getInt("iIdTienda"));
                tienda.setDescripcionTienda(resultSet.getString("cDescripcion_Tienda"));
                productVisibilidadEnTiendas.getListadoVisibilidad().add(
                        new ProductVisibilidadTienda(
                                resultSet.getInt("iIdProductTienda"),
                                tienda,
                                resultSet.getBoolean("bVisibleTienda"),
                                resultSet.getInt("iIdProduct")
                        )
                );

            }

            resultSet.close();
        } catch (Exception ex) {
            productVisibilidadEnTiendas = new ProductVisibilidadEnTiendas();
        }
        return productVisibilidadEnTiendas;
    }

    public byte ActualizarProducto(mProduct product, boolean estadoConfigVaria) {

        byte respuesta = 0;
        final String sprocedure = "call sp_Editar_Producto_v5(" + ParamStoreProcedure(39) + ")";

        CallableStatement cs = null;
        if (conn != null) {
            try {
                cs = conn.prepareCall(sprocedure);
                cs.setInt(1, product.getIdProduct());
                cs.setString(2, product.getcKey());
                cs.setString(3, product.getcProductName());
                cs.setString(4, product.getCodigoBarra());
                cs.setString(5, " ");
                cs.setBigDecimal(6, product.getStockDisponible());
                cs.setBigDecimal(7, product.getStockReserva());
                cs.setBigDecimal(8, product.getPrecioCompra());
                cs.setBigDecimal(9, product.getPrecioVenta());
                cs.setString(10, product.getcAdditionalInformation());
                cs.setString(11, product.getObservacionProducto());
                cs.setString(12, product.getEstadoActivo());
                cs.setString(13, product.getEstadoVisible());
                cs.setBoolean(14, product.isEsFavorito());
                cs.setBoolean(15, product.isControlStock());
                cs.setBoolean(16, product.isControlPeso());
                cs.setInt(17, product.getIdCategoria());
                cs.setInt(18, Constantes.Empresa.idEmpresa);
                cs.setInt(19, Constantes.Terminal.idTerminal);
                cs.setInt(20, Constantes.Usuario.idUsuario);
                cs.setBytes(21, product.getbImage());
                cs.setInt(22, product.getTipoRepresentacionImagen());
                cs.setString(23, product.getCodigoColor());
                cs.setString(24, product.getCodigoForma());
                cs.setInt(25, product.getIdProductImage());
                cs.setByte(26, respuesta);
                cs.registerOutParameter(26, Types.TINYINT);
                cs.setBoolean(27, product.isEstadoVariante());
                cs.setBoolean(28, product.isTipoPack());
                cs.setString(29, product.getUnidadMedida());
                cs.setBoolean(30, product.isMultiplePVenta());
                cs.setInt(31, product.getIdSubCategoria());
                cs.setInt(32, Constantes.Tienda.idTienda);
                cs.setInt(33, product.getIdAreaProduccion());
                cs.setBoolean(34, product.ispVentaLibre());
                cs.setInt(35, product.getIdUnidadMedida());
                cs.setBoolean(36, product.isbControlTiempo());
                cs.setBoolean(37, product.isbSeModificoImagen());
                cs.setBoolean(38, product.isbVisibleWeb());
                cs.setBigDecimal(39, product.getDCantidadMaximaPedido());
                cs.execute();
                // ActualizarConfiguracionVariante(product,con,estadoConfigVaria);

                //   ActualizarEstadosProducto(product);

                if (product.isMultiplePVenta()) {
                    EliminarPreciosAdiccionales(product.getIdProduct(), product.getPriceProductList());
                    if (product.getPriceProductList().size() > 0) {
                        AgregarPreciosVentaAdiccionales(product.getIdProduct(), product.getPriceProductList());
                    }
                }

                respuesta = cs.getByte(26);
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i("info_test", e.toString());
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }

        return respuesta;

    }

    public byte EliminarVariante(int idVariante) {
        byte respuesta = 0;

        PreparedStatement ps = null;
        if (conn != null) {
            try {
                ps = conn.prepareStatement("update product set  cEliminado='E' where iIdCompany=? and iIdProduct=? and bEsVariante=1");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idVariante);
                ps.execute();

                ps.close();
                respuesta = 100;
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }


        return respuesta;
    }

    public List<Variante> getVariantesProducto(int idProducto, Connection con) {

        List<Variante> list = new ArrayList<>();
        Variante variante;
        final String getVariantes = " select iIdProduct,cCodigo_Barra,cKey,cDescripcion_Variante,dQuantity,dPurcharsePrice,dSalesPrice,bPrecioVentaMulti from product where cod_Variante=? and cEliminado=''and bEsVariante=1 order by cDescripcion_Variante1,cDescripcion_Variante2,cDescripcion_Variante3 ";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(getVariantes);
            ps.setInt(1, idProducto);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                try {
                    variante = new Variante();
                    variante.setIdVariante(rs.getInt(1));
                    variante.setCodigoBarra(rs.getString(2));
                    variante.setCodigoVariante(rs.getString(3));
                    variante.setNombreVariante(rs.getString(4));
                    variante.setStockProducto(rs.getFloat(5));
                    variante.setPrecioCompra(rs.getBigDecimal(6));
                    variante.setPrecioVenta(rs.getBigDecimal(7));
                    variante.setPVMultiple(rs.getBoolean(8));
                    list.add(variante);
                } catch (Exception e) {
                    e.toString();
                }

            }
            ps.close();
        } catch (SQLException e) {

            e.printStackTrace();
            list = null;
        }

        return list;
    }

    public OpcionVariante GuardarOpcionProductoVariante(int idProduct, String cDescripcion) {
        OpcionVariante opcionVariante = new OpcionVariante();
        CallableStatement cs = null;
        Connection con = conn;

        if (con != null) {
            try {
                cs = con.prepareCall("call sp_guardar_Opcion_Variante(?,?,?,?,?)");
                cs.setString(1, cDescripcion);
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, idProduct);
                cs.registerOutParameter(4, Types.INTEGER);
                cs.registerOutParameter(5, Types.INTEGER);

                cs.execute();

                opcionVariante.setDescripcion(cs.getString(1));
                opcionVariante.setIdOpcionVariante(cs.getInt(4));
                opcionVariante.setiNumIntem(cs.getInt(5));
                opcionVariante.setIdProduct(idProduct);
            } catch (SQLException e) {
                e.printStackTrace();
                opcionVariante = null;
            }
        } else {
            opcionVariante.setiNumIntem(-98);
        }

        return opcionVariante;
    }

    public byte EliminarOpcion(OpcionVariante opcionVariante) {

        byte respuesta = 0;

        Connection con = conn;
        CallableStatement cs = null;
        if (con != null) {
            try {
                cs = con.prepareCall("call  sp_Eliminar_Opcion(?,?,?,?)");
                cs.setInt(1, opcionVariante.getIdProduct());
                cs.setInt(2, opcionVariante.getIdOpcionVariante());
                cs.setInt(3, Constantes.Empresa.idEmpresa);
                cs.registerOutParameter(4, Types.TINYINT);
                cs.execute();

                respuesta = cs.getByte(4);

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }
        return respuesta;
    }

    public OpcionVariante GuardarValoresOpciones(OpcionVariante opcionVariante) {

        PreparedStatement ps = null;


        if (conn != null) {
            try {
                ps = conn.prepareStatement("update Valor_Opcion_Variante set cEstado_Eliminado='E' where id_company=? and id_opcion=? and id_product=?");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, opcionVariante.getIdOpcionVariante());
                ps.setInt(3, opcionVariante.getIdProduct());
                ps.execute();

                ps.clearParameters();
                ps = conn.prepareStatement("insert into Valor_Opcion_Variante (id_company,id_product,id_opcion,iNum_Item,cDescripcion_Valor) values(?,?,?,?,?)");
                for (int i = 0; i < opcionVariante.getListValores().size(); i++) {
                    ps.setInt(1, Constantes.Empresa.idEmpresa);
                    ps.setInt(2, opcionVariante.getIdProduct());
                    ps.setInt(3, opcionVariante.getIdOpcionVariante());
                    ps.setInt(4, opcionVariante.getListValores().get(i).getiNumItem());
                    ps.setString(5, opcionVariante.getListValores().get(i).getDescripcion());

                    ps.addBatch();
                }
                ps.executeBatch();
                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
                opcionVariante = null;
            }

        } else {
            opcionVariante = new OpcionVariante();
            opcionVariante.setIdProduct(-1);
        }
        return opcionVariante;

    }

    public int GuardarValorVariante(int idProduct, int opcion, String valor) {
        int result = 0;
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call sp_guardar_valor_opcion_variante(?,?,?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, idProduct);
            cs.setInt(3, opcion);
            cs.setString(4, valor);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.execute();
            result = cs.getInt(5);
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result = 99;
        }
        return result;
    }

    public List<Variante> GenerarVariantes(int idProduct) {
        List<Variante> list = new ArrayList<>();
        CallableStatement cs = null;

        final String generarVariante = "call sp_Generar_Variantes_v2(?,?,?)";

        try {
            cs = conn.prepareCall(generarVariante);
            cs.setInt(1, idProduct);
            cs.setInt(2, Constantes.Empresa.idEmpresa);
            cs.setInt(3, Constantes.Tienda.idTienda);
            cs.execute();
            list.addAll(getVariantesProducto(idProduct, conn));
            cs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }

        return list;
    }

    public ResultProcesoVerificarNombre VerificarExistenciaNombreArticulo(int idProduct, String nombre) {

        CallableStatement cs = null;
        ResultProcesoVerificarNombre r = new ResultProcesoVerificarNombre();

        try {
            cs = conn.prepareCall("call sp_verificar_existencia_nombre_producto(?,?,?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, idProduct);
            cs.setString(3, nombre);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();

            r.setCode(cs.getInt(4));
            r.setMensajeResult(cs.getString(5));

        } catch (SQLException e) {
            e.printStackTrace();
            r = new ResultProcesoVerificarNombre();
            r.setMensajeResult("Verificar su conexión a internet");
            r.setCode(99);
        }

        return r;
    }

    public List<OpcionVariante> getOpcionesValores(int idProduct) {

        OpcionVariante opcionVariante;
        ValorOpcionVariante valorOpcionVariante;
        List<OpcionVariante> list = new ArrayList<>();
        List<ValorOpcionVariante> listVariantes = new ArrayList<>();
        CallableStatement cs = null;
        final String ListOpciones = "select iNum_item,cDescripcion_Opcion from Opcion_Variante" +
                " where id_company=? and id_product=? and cEstado_Eliminado='' order by iNum_item";
        final String Valores = "call sp_obtenerVariantesProducto(?,?)";

        PreparedStatement ps = null;
        ResultSet rs = null;
        if (conn != null) {
            try {
                ps = conn.prepareStatement(ListOpciones);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idProduct);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    opcionVariante = new OpcionVariante();
                    opcionVariante.setiNumIntem(rs.getInt(1));
                    opcionVariante.setDescripcion(rs.getString(2));
                    list.add(opcionVariante);
                    opcionVariante = null;
                }
                if (list.size() == 1) {
                    list.add(new OpcionVariante());
                    list.add(new OpcionVariante());
                } else if (list.size() == 2) {
                    list.add(new OpcionVariante());
                }

                cs = conn.prepareCall(Valores);
                cs.setInt(1, idProduct);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.execute();
                rs = null;
                rs = cs.getResultSet();

                while (rs.next()) {
                    valorOpcionVariante = new ValorOpcionVariante();
                    valorOpcionVariante.setNumItemPadre(rs.getInt(1));
                    valorOpcionVariante.setDescripcion(rs.getString(2));
                    listVariantes.add(valorOpcionVariante);
                    valorOpcionVariante = null;
                }

                ps.close();
                cs.close();

                for (int i = 0; i < list.size(); i++) {

                    for (int a = 0; a < listVariantes.size(); a++) {

                        if (listVariantes.get(a).getNumItemPadre() == i + 1) {
                            list.get(i).getListValores().add(listVariantes.get(a));
                        }
                    }

                }

                try {
                    if (list.size() > 0) {
                        for (int i = list.size() - 1; 0 <= i; i--) {

                            if (list.get(i).getListValores().get(0).getDescripcion().equals("")) {
                                list.remove(i);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.toString();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            } finally {

            }
        } else {

            list = null;
        }

        return list;
    }

    public ProductoEnVenta GuardarProductoVarianteDetallePedido(VarianteBusqueda varianteBusqueda) {
        ProductoEnVenta productoEnVenta = new ProductoEnVenta();
        CallableStatement cs = null;
        final String procedure = "Call sp_Guardar_Producto_Variante_Detalle_Pedido_v2" +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        byte numeroResultado = 0;
        if (conn != null) {
            try {
                cs = conn.prepareCall(procedure);
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, varianteBusqueda.getIdCabeceraPedido());
                cs.setInt(3, varianteBusqueda.getIdProducto());
                cs.setFloat(4, varianteBusqueda.getCantidadSeleccionada());
                cs.setString(5, varianteBusqueda.getVariablesBusqueda().get(0));
                cs.setString(6, varianteBusqueda.getVariablesBusqueda().get(1));
                cs.setString(7, varianteBusqueda.getVariablesBusqueda().get(2));
                cs.registerOutParameter(8, Types.INTEGER);
                cs.registerOutParameter(9, Types.VARCHAR);
                cs.registerOutParameter(10, Types.DECIMAL);
                cs.registerOutParameter(11, Types.DECIMAL);
                cs.setByte(12, numeroResultado);
                cs.registerOutParameter(12, Types.TINYINT);
                cs.registerOutParameter(13, Types.INTEGER);
                cs.registerOutParameter(14, Types.VARCHAR);
                cs.setInt(15, Constantes.Terminal.idTerminal);
                cs.setInt(16, Constantes.Tienda.idTienda);
                cs.setInt(17, Constantes.Usuario.idUsuario);
                cs.registerOutParameter(18, Types.INTEGER);
                cs.setBoolean(19, varianteBusqueda.isMultiPv());
                cs.setBigDecimal(20, varianteBusqueda.getPv());
                cs.execute();

                productoEnVenta.setIdProducto(cs.getInt(8));
                productoEnVenta.setDescripcionVariante(cs.getString(9));
                productoEnVenta.setPrecioOriginal(cs.getBigDecimal(10));
                productoEnVenta.setPrecioVentaFinal(cs.getBigDecimal(11));
                productoEnVenta.setCantidad(varianteBusqueda.getCantidadSeleccionada());
                productoEnVenta.setItemNum(cs.getInt(13));
                productoEnVenta.setProductName(cs.getString(14));
                productoEnVenta.setEsVariante(true);
                productoEnVenta.setIdDetallePedido(cs.getInt(18));
                numeroResultado = cs.getByte(12);
                cs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                productoEnVenta = null;
            }

        } else {
            productoEnVenta = null;
        }


        return productoEnVenta;
    }

    public byte CambiarEstadoVariante(mProduct product) {

        byte respuesta = 99;
        PreparedStatement ps = null;
        int numAct = 0;
        if (conn != null) {
            try {
                ps = conn.prepareStatement("update Product set bEstado_Variantes=? where iIdCompany=? and iIdProduct=?");
                ps.setBoolean(1, product.isEstadoVariante());
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, product.getIdProduct());
                numAct = ps.executeUpdate();
                if (numAct > 0) {
                    respuesta = 100;
                    ps.clearParameters();
                    ps = conn.prepareStatement("update Product set cEliminado='E' where iIdCompany=? and cod_variante=? ");
                    ps.setInt(1, Constantes.Empresa.idEmpresa);
                    ps.setInt(2, product.getIdProduct());
                    ps.executeUpdate();
                    ps.close();
                } else {
                    respuesta = 99;
                }
            } catch (SQLException e) {
                respuesta = 99;
                e.printStackTrace();
            }

        } else {
            respuesta = 98;
        }
        return respuesta;
    }

    public List<ConfigPack> getProductosPack(int idProducto) {
        List<ConfigPack> configPacks = new ArrayList<>();
        ConfigPack configPack;
        CallableStatement cs = null;

        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_obtener_config_pack_v2(?,?,?)");
                cs.setInt(1, idProducto);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Tienda.idTienda);
                cs.execute();
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    configPack = new ConfigPack();
                    configPack.setIdTipo(rs.getInt(1));
                    configPack.setIdItem(rs.getInt(2));
                    configPack.setNumItem(rs.getInt(3));
                    configPack.setcDescripcion(rs.getString(4));
                    configPack.setCodigoProducto(rs.getString(5));
                    configPack.setPrecio(rs.getBigDecimal(6));
                    configPack.setCantidad(rs.getBigDecimal(7));
                    configPack.setIdPack(rs.getInt(8));
                    configPack.setIdTipoModifica(rs.getInt(9));
                    configPack.setMontoModifica(rs.getBigDecimal(10));
                    configPacks.add(configPack);
                    configPack = null;
                }
                rs.close();
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                configPacks = null;
            }

        } else {

            configPacks = null;
        }


        return configPacks;
    }

    public mProduct IngresarProductoPack(ConfigPack configPack) {

        mProduct product = new mProduct();
        CallableStatement cs = null;
        byte Resultado = 0;

        try {
            cs = conn.prepareCall("call sp_insertar_producto_pack_v2(" + ParamStoreProcedure(16) + ")");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(1, configPack.getIdItem());
            cs.setInt(2, configPack.getIdTipo());
            cs.setInt(3, Constantes.Empresa.idEmpresa);
            cs.setInt(4, Constantes.Usuario.idUsuario);
            cs.setInt(5, Constantes.Terminal.idTerminal);
            cs.setInt(6, Constantes.Tienda.idTienda);
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.registerOutParameter(9, Types.TINYINT);
            cs.registerOutParameter(10, Types.DECIMAL);
            cs.setBigDecimal(10, configPack.getCantidad());
            cs.registerOutParameter(11, Types.DECIMAL);
            cs.setBigDecimal(11, configPack.getPrecio());
            cs.setByte(12, configPack.getMetodoGuardado());
            cs.registerOutParameter(12, Types.TINYINT);
            cs.setInt(13, configPack.getIdPack());
            cs.registerOutParameter(13, Types.INTEGER);
            cs.setInt(14, configPack.getNumItem());
            cs.registerOutParameter(14, Types.INTEGER);
            cs.setInt(15, configPack.getIdTipoModifica());
            cs.registerOutParameter(15, Types.INTEGER);
            cs.setBigDecimal(16, configPack.getMontoModifica());
            cs.registerOutParameter(16, Types.DECIMAL);
            cs.execute();

            product.setIdProduct(cs.getInt(1));
            product.setcKey(cs.getString(7));
            product.setcProductName(cs.getString(8));
            product.setStockDisponible(cs.getBigDecimal(10));
            product.setPrecioVenta(cs.getBigDecimal(11));
            product.setMetodoGuardado(cs.getByte(12));
            product.setIdPack(cs.getInt(13));
            product.setNumItem(cs.getInt(14));
            product.setMetodoGuardado(configPack.getMetodoGuardado());
            product.setIdTipoModifica(cs.getInt(15));
            product.setMontoModifica(cs.getBigDecimal(16));
            Resultado = cs.getByte(9);

            if (Resultado == 99) {
                product = null;
            }
            cs.close();


        } catch (SQLException e) {
            e.printStackTrace();
            product = null;
        }

        return product;
    }

    public byte EliminarProductoPack(ConfigPack configPack) {

        byte resultado = 0;
        int count = 0;

        PreparedStatement ps = null;
        Connection con = getCon();

        if (con != null) {
            try {
                ps = con.prepareStatement("update Pack_Producto set cEstado_Eliminado='E'  where id_Company=? and id_product_padre=? and id_product_hijo=? and iNum_Item=? and cEstado_Eliminado='' ");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, configPack.getIdTipo());
                ps.setInt(3, configPack.getIdItem());
                ps.setInt(4, configPack.getNumItem());


                count = ps.executeUpdate();
                ps.close();
                con.close();

                if (count > 0) {
                    resultado = 100;
                } else {
                    resultado = 99;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
            }
        } else {
            resultado = 98;
        }
        return resultado;

    }

    public List<PackElemento> ObtenerPackProductos(int idProducto) {

        List<ResultadoPack> resultadoPacks = new ArrayList<>();
        ResultadoPack resultadoPack;
        List<PackElemento> packElementoList = new ArrayList<>();

        CallableStatement cs = null;
        mProduct product;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_Obtener_Pack_Producto_v2(?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, idProducto);
                cs.execute();
                ResultSet rs = cs.getResultSet();
                while (rs.next()) {
                    resultadoPack = new ResultadoPack();
                    resultadoPack.setIdCategoria(rs.getInt(1));
                    resultadoPack.setDescripcionCategoria(rs.getString(2));
                    resultadoPack.setIdProducto(rs.getInt(3));
                    resultadoPack.setNombreProducto(rs.getString(4));
                    resultadoPack.setPrecioVenta(rs.getBigDecimal(5));
                    resultadoPack.setImagen(rs.getBytes(6));
                    resultadoPack.setTipoImagen(rs.getByte(7));
                    resultadoPack.setCodColor(rs.getString(8));
                    resultadoPack.setCodImagen(rs.getString(9));
                    resultadoPack.setCantidad(rs.getBigDecimal(10));
                    resultadoPack.setEsModificado(rs.getBoolean(11));
                    resultadoPack.setFactorModificionPack(rs.getInt(12));
                    resultadoPack.setValorModifiacionPack(rs.getBigDecimal(13));
                    resultadoPack.setVisibleMontoModPack(rs.getBoolean(14));
                    resultadoPacks.add(resultadoPack);

                }

                int idCatTemp = 0;
                int cont = 0;

                if (!resultadoPacks.isEmpty()) {
                    PackElemento packElemento1 = new PackElemento();
                    packElemento1.setDescripcion(resultadoPacks.get(0).descripcionCategoria);
                    packElemento1.setIdCategoria(resultadoPacks.get(0).idCategoria);
                    idCatTemp = resultadoPacks.get(0).idCategoria;
                    while (cont < resultadoPacks.size()) {
                        if (idCatTemp == resultadoPacks.get(cont).idCategoria) {
                            product = new mProduct();
                            product.setcProductName(resultadoPacks.get(cont).getNombreProducto());
                            product.setIdProduct(resultadoPacks.get(cont).getIdProducto());
                            product.setPrecioVenta(resultadoPacks.get(cont).getPrecioVenta());
                            product.setbImage(resultadoPacks.get(cont).getImagen());
                            product.setTipoRepresentacionImagen(resultadoPacks.get(cont).getTipoImagen());
                            product.setCodigoColor(resultadoPacks.get(cont).getCodColor());
                            product.setCodigoForma(resultadoPacks.get(cont).getCodImagen());
                            product.setIdCategoria(resultadoPacks.get(cont).getIdCategoria());
                            product.setStockDisponible(resultadoPacks.get(cont).getCantidad());
                            product.setEstadoModificador(resultadoPacks.get(cont).isEsModificado());
                            product.setMontoModifica(resultadoPacks.get(cont).getValorModifiacionPack());
                            product.setFactorModificacion(resultadoPacks.get(cont).getFactorModificionPack());
                            product.setVisibleTipoModificacionPack(resultadoPacks.get(cont).isVisibleMontoModPack());
                            packElemento1.getProductList().add(product);
                            cont++;
                        } else {
                            packElementoList.add(packElemento1);
                            packElemento1 = new PackElemento();
                            packElemento1.setDescripcion(resultadoPacks.get(cont).descripcionCategoria);
                            packElemento1.setIdCategoria(resultadoPacks.get(cont).idCategoria);
                            idCatTemp = resultadoPacks.get(cont).idCategoria;
                        }

                    }

                    packElementoList.add(packElemento1);

                }


                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                packElementoList = null;
                try {
                    cs.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            packElementoList = null;
        }

        return packElementoList;
    }

    public List<mProduct> BusquedaProductoCongifPack(byte metodoBusqueda, String Parametro, int idCategoria) {

        //metodo busqueda->100 =parametro  // 101= categoria
        List<mProduct> productList = new ArrayList<>();
        mProduct product;

        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = conn.prepareCall(" call sp_listado_productos_pack(" + ParamStoreProcedure(4) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idCategoria);
            cs.setString(4, Parametro);
            cs.execute();

            rs = cs.getResultSet();
            if (rs != null) {
                while (rs.next()) {

                    product = new mProduct();
                    product.setIdProduct(rs.getInt(1));
                    product.setcProductName(rs.getString(2));
                    product.setcKey(rs.getString(3));
                    productList.add(product);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            productList = new ArrayList<>();
        } finally {

            try {
                if (rs != null)
                    rs.close();
                if (cs != null)
                    cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


      /*  PreparedStatement ps=null;

        final String busquedaPorNombre="select top(100)p.iIdProduct,p.cProductName, c.cDescripcion_categoria " +
                " from Product as p inner join Categoria_Productos as c on p.id_Categoria=c.id_categoria_producto " +
                "where p.iIdCompany=? and( p.cProductName like '%'+?+'%' or p.cKey like '%'+ ? +'%' ) " +
                "AND p.cEliminado='' and p.cEstado_Visible='V' and p.bEsPack=0 and p.bEstado_Variantes=0 and bEsVariante=0";
        final String busquedaPorCategoria="select p.iIdProduct,p.cProductName, c.cDescripcion_categoria" +
                "  from Product as p  inner join Categoria_Productos as c on p.id_Categoria=c.id_categoria_producto  " +
                "where p.iIdCompany=? and p.id_categoria=? AND p.cEliminado='' and p.cEstado_Visible='V' and p.bEsPack=0 " +
                "and p.bEstado_Variantes=0 and p.bEsVariante=0";
        try {
            if(metodoBusqueda==101) {
                ps = conn.prepareStatement(busquedaPorCategoria);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2,idCategoria);
            }
            else if(metodoBusqueda==100){
                if(Parametro.trim().equals("")){
                    ps = conn.prepareStatement("select p.iIdProduct,p.cProductName, c.cDescripcion_categoria " +
                            " from Product as p  inner join Categoria_Productos as c on p.id_Categoria=c.id_categoria_producto" +
                            "  where p.iIdCompany=? AND p.cEliminado='' and p.cEstado_Visible='V'" +
                            " and p.bEsPack=0 and p.bEstado_Variantes=0 and p.bEsVariante=0");
                    ps.setInt(1, Constantes.Empresa.idEmpresa);
                }
                else {
                    ps = conn.prepareStatement(busquedaPorNombre);
                    ps.setInt(1, Constantes.Empresa.idEmpresa);
                    ps.setString(2, Parametro);
                    ps.setString(3, Parametro);
                }
            }
            ps.execute();

            ResultSet rs=ps.getResultSet();

            while(rs.next()){

                product=new mProduct();
                product.setIdProduct(rs.getInt(1));
                product.setcProductName(rs.getString(2));
                product.setcKey(rs.getString(3));
                productList.add(product);
                product=null;

            }
            rs.close();
              ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            productList=null;
        }
*/
        return productList;
    }

    public PackElemento GuardarPackDetallePedido(int idCabeceraPedido, PackElemento productoPadre) {

        List<mProduct> productList = new ArrayList<>();
        boolean permitir = true;
        List<String> productosFalta = new ArrayList<>();
        String prefijo = "";
        mProduct product;
        PreparedStatement pps = null;
        String consulta = "select p.iIdProduct,isnull(vs.ncantidad,0) -isnull(pr.cantidad,0),p.bControl_Stock " +
                " from product as p left join " +
                " Productos_Reserva_Pedido as pr on " +
                " p.iIdProduct=pr.iIdProducto and pr.iIdCompany=? and pr.iIdTienda=? " +
                " left join VStock_Almacen_Principal_Tienda as vs  " +
                " on vs.id_Producto=p.iIdProduct and   vs.id_Company=? and vs.id_tienda=? " +
                "  where p.iIdCompany=?  and " +
                " p.cEstado_Visible='V' and p.cEliminado='' " +
                " and p.cEstado_Producto='A' and p.iIdProduct in(";
        String consultaIn = "  ";

        ResultSet rs = null;
        for (int i = 0; i < productoPadre.getProductList().size(); i++) {
            consultaIn = consultaIn + "?,";
        }
        consultaIn = consultaIn.substring(0, consultaIn.length() - 1);
        consulta = consulta + consultaIn + ")";
        try {
            pps = conn.prepareStatement(consulta);
            pps.setInt(1, Constantes.Empresa.idEmpresa);
            pps.setInt(2, Constantes.Tienda.idTienda);
            pps.setInt(3, Constantes.Empresa.idEmpresa);
            pps.setInt(4, Constantes.Tienda.idTienda);
            pps.setInt(5, Constantes.Empresa.idEmpresa);

            for (int i = 0; i < productoPadre.getProductList().size(); i++) {
                pps.setInt(i + 6, productoPadre.getProductList().get(i).getIdProduct());

            }
            pps.execute();
            rs = pps.getResultSet();

            while (rs.next()) {
                product = new mProduct();
                product.setIdProduct(rs.getInt(1));
                product.setdQuantity(rs.getFloat(2));
                product.setControlStock(rs.getBoolean(3));
                productList.add(product);
            }

            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).isControlStock()) {
                    for (int a = 0; a < productoPadre.getProductList().size(); a++) {
                        if (productList.get(i).getIdProduct() == productoPadre.getProductList().get(a).getIdProduct()) {
                            if (productList.get(i).getdQuantity() < productoPadre.getProductList().get(a).getStockDisponible().intValueExact() * productoPadre.getCantidad()) {
                                permitir = false;
                                productosFalta.add(productoPadre.getProductList().get(a).getcProductName());
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pps.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        byte resultado = 99;
        productoPadre.setListaFaltante(productosFalta);
        productoPadre.setPermitir(permitir);
        if (productoPadre.isPermitir()) {
            CallableStatement cs = null;
            int numItem = 0;
            if (conn != null) {
                try {
                    cs = conn.prepareCall("call sp_insertarCabeceraPackPedido_v2" +
                            "(?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    cs.setInt(1, productoPadre.getIdProducto());
                    cs.setInt(2, idCabeceraPedido);
                    cs.setInt(3, Constantes.Empresa.idEmpresa);
                    cs.setInt(4, Constantes.Terminal.idTerminal);
                    cs.setInt(5, Constantes.Usuario.idUsuario);
                    cs.setInt(6, productoPadre.getCantidad());
                    cs.registerOutParameter(7, Types.VARCHAR);
                    cs.registerOutParameter(8, Types.DECIMAL);
                    cs.setBigDecimal(8, productoPadre.getPrecioVenta());
                    cs.registerOutParameter(9, Types.DECIMAL);
                    cs.registerOutParameter(10, Types.INTEGER);
                    cs.registerOutParameter(11, Types.INTEGER);
                    cs.registerOutParameter(12, Types.TINYINT);
                    cs.setInt(13, Constantes.Tienda.idTienda);
                    cs.execute();

                    productoPadre.setNombreProducto(cs.getString(7));
                    productoPadre.setPrecioVenta(cs.getBigDecimal(8));
                    productoPadre.setPrecioVentaFinal(cs.getBigDecimal(9));
                    productoPadre.setNumItem(cs.getInt(10));
                    productoPadre.setIdDetallePedido(cs.getInt(11));
                    if (productoPadre.getNombreProducto().length() <= 10) {
                        prefijo = productoPadre.getNombreProducto();
                    } else {
                        prefijo = productoPadre.getNombreProducto().substring(0, 10);
                    }
                    resultado = cs.getByte(12);

                    if (resultado == 99) {
                        productoPadre = new PackElemento();
                        productoPadre.setNumItem(-10);
                    } else if (resultado == 100) {

                        PreparedStatement ps = conn.prepareStatement("insert into Detalle_Pedido(" +
                                "iId_Cabecera_Pedido," +
                                "id_producto_padre," +
                                "iNumItem," +
                                "iIdProducto," +
                                "bDetallePack," +
                                "cProductName," +
                                "iCantidad," +
                                "dPrecioUnidad," +
                                "dPrecioVentaFinal," +
                                "id_Company,id_Terminal,id_Usuario,id_Tienda,bEs_Modificado," +
                                "cDescripcion_Modificador,cDescripcion_Combo," +
                                "ifactor_mod_pack,nmonto_modificador_pack) values(" + ParamStoreProcedure(18) + ")");
                        numItem = productoPadre.getNumItem();
                        for (int i = 0; i < productoPadre.getProductList().size(); i++) {

                            numItem = numItem + 1;
                            try {
                                productoPadre.getProductList().get(i).setIdPack(productoPadre.getIdDetallePedido());
                                ps.setInt(1, idCabeceraPedido);
                                ps.setInt(2, productoPadre.getIdDetallePedido());
                                ps.setInt(3, numItem);
                                ps.setInt(4, productoPadre.getProductList().get(i).getIdProduct());
                                ps.setBoolean(5, productoPadre.getProductList().get(i).isbDetallePack());
                                ps.setString(6, productoPadre.getProductList().get(i).getcProductName());
                                ps.setInt(7, productoPadre.getProductList().get(i).getStockDisponible()
                                        .intValueExact()
                                        * productoPadre.getCantidad());
                                ps.setBigDecimal(8, productoPadre.getProductList().get(i).getPrecioUnitarioItemPack());
                                ps.setBigDecimal(9, productoPadre.getProductList().
                                        get(i).getPrecioUnitarioItemPack()
                                        .multiply(new BigDecimal(productoPadre.getCantidad())).
                                        multiply(productoPadre.getProductList().get(i).getStockDisponible()));
                                ps.setInt(10, Constantes.Empresa.idEmpresa);
                                ps.setInt(11, Constantes.Terminal.idTerminal);
                                ps.setInt(12, Constantes.Usuario.idUsuario);
                                ps.setInt(13, Constantes.Tienda.idTienda);
                                ps.setBoolean(14, productoPadre.getProductList().get(i).isEstadoModificador());
                                ps.setString(15, productoPadre.getProductList().get(i).getcAdditionalInformation());
                                ps.setString(16, prefijo);
                                ps.setInt(17, productoPadre.getProductList().get(i).getFactorModificacion());
                                ps.setBigDecimal(18, productoPadre.getProductList().get(i).getMontoModifica());
                                productoPadre.getProductList().get(i).setNumItem(numItem);
                                ps.addBatch();
                            } catch (SQLException e) {
                                e.toString();
                            }
                        }
                        ps.executeBatch();
                    }


                    cs.close();

                } catch (SQLException e) {

                    e.toString();
                    productoPadre = new PackElemento();
                    productoPadre.setNumItem(-10);
                }
            } else {

                productoPadre = new PackElemento();
                productoPadre.setNumItem(-5);
            }
        }

        return productoPadre;

    }

    public ProductoEnVenta eliminarProductoPack(ProductoEnVenta productoEnVenta) {
        CallableStatement cs = null;

        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_eliminar_pack_detalle_pedido(?,?,?)");
                cs.setInt(1, productoEnVenta.getIdDetallePedido());
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Terminal.idTerminal);
                cs.execute();
                cs.close();
            } catch (SQLException e) {
                productoEnVenta = null;
                e.printStackTrace();
            }
        } else {
            productoEnVenta = null;
        }
        return productoEnVenta;
    }

    public void closeConnection() {

        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ProductoEnVenta guardarProductoNDetalle(ProductoEnVenta productoEnVenta) {

        CallableStatement cs = null;

        try {
            if (conn.isClosed()) {
                conn = getConnection();
            }
            if (conn == null) {
                conn = getConnection();
            } else {

                if (conn != null) {

                    try {
                        cs = conn.prepareCall("call sp_insertar_producto_normal_detalle_pedido_v3" +
                                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        cs.setInt(1, productoEnVenta.getIdCabeceraPedido());
                        cs.registerOutParameter(2, Types.INTEGER);
                        cs.setInt(2, productoEnVenta.getIdDetallePedido());
                        cs.setInt(3, productoEnVenta.getIdProducto());
                        cs.registerOutParameter(4, Types.VARCHAR);
                        cs.registerOutParameter(5, Types.DECIMAL);
                        cs.registerOutParameter(6, Types.DECIMAL);
                        cs.setFloat(7, productoEnVenta.getCantidad());
                        cs.registerOutParameter(7, Types.FLOAT);
                        cs.registerOutParameter(8, Types.INTEGER);
                        cs.setInt(9, Constantes.Empresa.idEmpresa);
                        cs.setInt(10, Constantes.Terminal.idTerminal);
                        cs.setInt(11, Constantes.Usuario.idUsuario);
                        cs.setString(12, productoEnVenta.getMetodoGuardar());
                        cs.setInt(13, Constantes.Tienda.idTienda);
                        cs.registerOutParameter(14, Types.FLOAT);
                        cs.setFloat(14, 0);
                        cs.registerOutParameter(15, Types.FLOAT);
                        cs.setFloat(15, 0f);
                        cs.setInt(16, productoEnVenta.getCodPrecioAlterno());
                        cs.registerOutParameter(17, Types.BOOLEAN);
                        cs.execute();
                        productoEnVenta.setProductName(cs.getString(4));
                        productoEnVenta.setCantidad(cs.getFloat(7));
                        productoEnVenta.setIdDetallePedido(cs.getInt(2));
                        productoEnVenta.setPrecioOriginal(cs.getBigDecimal(5));
                        productoEnVenta.setPrecioVentaFinal(cs.getBigDecimal(6));
                        productoEnVenta.setItemNum(cs.getInt(8));
                        productoEnVenta.setCantidadReserva(cs.getFloat(14));
                        productoEnVenta.setStockActual(cs.getFloat(15));
                        productoEnVenta.setbPrecioVariable(cs.getBoolean(17));
                        productoEnVenta.toString();
                        cs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        productoEnVenta = new ProductoEnVenta();
                        productoEnVenta.setIdProducto(-99);
                    }
                } else {
                    productoEnVenta = new ProductoEnVenta();
                    productoEnVenta.setIdProducto(-100);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setIdProducto(-100);
        }
        return productoEnVenta;
    }

    public List<ProductoModificador> obtenerCongifModificadorProduct(int idProduct) {

        List<ProductoModificador> productoModificadors = new ArrayList<>();
        ProductoModificador productoModificador;
        CallableStatement cs = null;


        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_obtener_config_modificador_producto(?,?)");
                cs.setInt(1, idProduct);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.execute();

                ResultSet rs = cs.getResultSet();

                while (rs.next()) {

                    productoModificador = new ProductoModificador();
                    productoModificador.setTipo(rs.getInt(1));
                    productoModificador.setIdPri(rs.getInt(2));
                    productoModificador.setIdSec(rs.getInt(3));
                    productoModificador.setDescripcion(rs.getString(4));
                    productoModificador.setEstado(rs.getBoolean(5));
                    productoModificadors.add(productoModificador);
                    productoModificador = null;

                }
                cs.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                productoModificadors = null;
            }
        } else {
            productoModificadors = null;

        }
        return productoModificadors;

    }

    public Modificador insertarModificador(int idProducto, int idModificador) {

        CallableStatement cs = null;
        Modificador modificador = new Modificador();
        if (conn != null) {
            try {
                cs = conn.prepareCall("call  sp_agregar_modificador_producto(?,?,?,?,?,?,?,?,?)");
                cs.setInt(1, idModificador);
                cs.setInt(2, idProducto);
                cs.setInt(3, Constantes.Empresa.idEmpresa);
                cs.setInt(4, Constantes.Tienda.idTienda);
                cs.setInt(5, Constantes.Usuario.idUsuario);
                cs.setInt(6, Constantes.Terminal.idTerminal);
                cs.registerOutParameter(7, Types.INTEGER);
                cs.registerOutParameter(8, Types.INTEGER);
                cs.registerOutParameter(9, Types.VARCHAR);
                cs.execute();

                modificador.setDescripcion(cs.getString(9));
                modificador.setNumItem(cs.getInt(8));
                modificador.setIdModificadorProducto(cs.getInt(7));
                modificador.setIdModificador(cs.getInt(7));


                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                modificador = new Modificador();
                modificador.setIdModificador(-10);
            }

        } else {
            modificador = new Modificador();
            modificador.setIdModificador(-5);
        }

        return modificador;
    }

    public byte EliminarModificadorProducto(int idModificadorProducto, int idProducto) {
        int update = 0;
        byte resultado = 0;
        PreparedStatement ps = null;


        if (conn != null) {

            try {
                ps = conn.prepareStatement("update Modificador_Producto set cEstado_Eliminado='E' where id_company=? and id_product=? and id_modificador_prod=?");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idProducto);
                ps.setInt(3, idModificadorProducto);
                update = ps.executeUpdate();
                ps.close();
                if (update == 1) {
                    resultado = 100;
                } else {
                    resultado = 99;
                }


            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
            }

        } else {

            resultado = 98;

        }

        return resultado;

    }

    public byte ActualizarEstadoModProd(int idProduct, boolean estado) {

        byte resultado = 0;
        PreparedStatement ps = null;
        Connection con = conn;

        if (con != null) {
            try {
                ps = con.prepareStatement("update Product set bEstadoModificador=? where iIdCompany=? and iIdProduct=? and bEsVariante=?");
                ps.setBoolean(1, estado);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, idProduct);
                ps.setBoolean(4, false);
                ps.execute();
                resultado = 100;
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
            }

        } else {
            resultado = 98;
        }


        return resultado;
    }

    public List<Modificador> obtenerModificadores(String parametro) {

        List<Modificador> modificadorList = new ArrayList<>();
        Modificador modificador;
        PreparedStatement ps = null;

        if (conn != null) {
            try {
                ps = conn.prepareStatement("select id_Modificador,cDescripcion_Modificador from Maestro_Modificadores where id_Company=? and cEstado_Eliminado='' AND cDescripcion_Modificador LIKE '%'+?+'%'");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, parametro);
                ps.execute();

                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    modificador = new Modificador();
                    modificador.setIdModificador(rs.getInt(1));
                    modificador.setDescripcion(rs.getString(2));
                    modificadorList.add(modificador);
                    modificador = null;

                }

                rs.close();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
                modificador = null;
            }

        } else {
            modificador = null;
        }
        return modificadorList;

    }

    public List<Modificador> ObtenerModificadoresProductoVenta(int idProducto) {

        CallableStatement cs = null;

        List<ModificadoresItems> modificadoresItemsList = new ArrayList<>();
        ModificadoresItems modificadoresItems;
        Modificador modificador;
        DetalleModificador detalleModificador;
        List<DetalleModificador> detalleModificadorList = new ArrayList<>();
        List<Modificador> modificadorList = new ArrayList<>();

        if (conn != null) {

            try {
                cs = conn.prepareCall("call sp_obtener_modificadores_producto(?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, idProducto);
                cs.execute();

                ResultSet rs = cs.getResultSet();
                while (rs.next()) {

                    modificadoresItems = new ModificadoresItems();
                    modificadoresItems.setIdModificador(rs.getInt(1));
                    modificadoresItems.setDescripcionMod(rs.getString(2));
                    modificadoresItems.setIdDetalle(rs.getInt(3));
                    modificadoresItems.setDescDetalle(rs.getString(4));
                    modificadoresItemsList.add(modificadoresItems);
                    modificadoresItems = null;

                }

                for (ModificadoresItems modificadoresItems1 : modificadoresItemsList) {

                    modificador = new Modificador();
                    modificador.setIdModificador(modificadoresItems1.getIdModificador());
                    modificador.setDescripcion(modificadoresItems1.getDescripcionMod());
                    modificadorList.add(modificador);
                    modificador = null;

                    detalleModificador = new DetalleModificador();
                    detalleModificador.setIdModificador(modificadoresItems1.getIdModificador());
                    detalleModificador.setIdDetalleModificador(modificadoresItems1.getIdDetalle());
                    detalleModificador.setDescripcionModificador(modificadoresItems1.getDescDetalle());

                    detalleModificadorList.add(detalleModificador);
                    detalleModificador = null;

                }
                Map<Integer, Modificador> mapModificador = new HashMap<>();
                for (Modificador modificador1 : modificadorList) {
                    mapModificador.put(modificador1.getIdModificador(), modificador1);
                }
                modificadorList.clear();
                for (Map.Entry<Integer, Modificador> m : mapModificador.entrySet()) {
                    modificadorList.add(m.getValue());

                }


                for (int i = 0; i < modificadorList.size(); i++) {

                    for (int a = 0; a < detalleModificadorList.size(); a++) {

                        if (modificadorList.get(i).getIdModificador() == detalleModificadorList.get(a).getIdModificador()) {
                            modificadorList.get(i).getDetalleModificadorList().add(detalleModificadorList.get(a));
                        }

                    }

                }
                modificadorList.size();
                cs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                modificadorList = null;
            }

        } else {

            modificadorList = null;

        }
        return modificadorList;
    }

    public ProductoEnVenta GuardarProductoModificadorDetallePedido
            (int idProducto, int idCabeceraPedido, float cantidad, String Modificador, int idPventa) {

        ProductoEnVenta productoEnVenta = new ProductoEnVenta();
        CallableStatement cs = null;

        byte respuesta = 0;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_guardar_producto_modificador_detalle_pedido_v3" +
                        "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                cs.setInt(1, idCabeceraPedido);
                cs.setInt(2, idProducto);
                cs.setString(3, Modificador);
                cs.registerOutParameter(4, Types.VARCHAR);
                cs.registerOutParameter(5, Types.DECIMAL);
                cs.registerOutParameter(6, Types.DECIMAL);
                cs.setFloat(7, cantidad);
                cs.registerOutParameter(8, Types.INTEGER);
                cs.registerOutParameter(9, Types.INTEGER);
                cs.setInt(10, Constantes.Empresa.idEmpresa);
                cs.setInt(11, Constantes.Tienda.idTienda);
                cs.setInt(12, Constantes.Terminal.idTerminal);
                cs.setInt(13, Constantes.Usuario.idUsuario);
                cs.registerOutParameter(14, Types.TINYINT);
                cs.registerOutParameter(15, Types.FLOAT);
                cs.registerOutParameter(16, Types.FLOAT);
                cs.setInt(17, idPventa);
                cs.registerOutParameter(18, Types.BOOLEAN);

                cs.execute();
                productoEnVenta.setIdProducto(idProducto);
                productoEnVenta.setDescripcionModificador(Modificador);
                productoEnVenta.setCantidad(cantidad);
                productoEnVenta.setEsModificado(true);
                productoEnVenta.setPrecioOriginal(cs.getBigDecimal(5));
                productoEnVenta.setPrecioVentaFinal(cs.getBigDecimal(6));
                productoEnVenta.setItemNum(cs.getInt(9));
                productoEnVenta.setIdDetallePedido(cs.getInt(8));
                productoEnVenta.setProductName(cs.getString(4));
                productoEnVenta.setRespuestaGuardar(cs.getByte(14));
                productoEnVenta.setStockActual(cs.getFloat(15));
                productoEnVenta.setCantidadReserva(cs.getFloat(16));
                //     productoEnVenta.set
                productoEnVenta.setbPrecioVariable(cs.getBoolean(18));

                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                productoEnVenta = new ProductoEnVenta();
                productoEnVenta.setIdProducto(-5);
            }
        } else {
            productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setIdProducto(-10);
        }
        return productoEnVenta;
    }

    public List<Modificador> obtenerModificadoresCompany() {

        List<Modificador> modificadorList = new ArrayList<>();
        List<DetalleModificador> detalleModificadorList = new ArrayList<>();
        Modificador modificador;
        DetalleModificador detalleModificador;
        CallableStatement cs = null;


        if (conn != null) {

            try {
                cs = conn.prepareCall("call sp_obtener_modificadores_companyV2(?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.execute();
                ResultSet rs = cs.getResultSet();

                while (rs.next()) {

                    if (rs.getInt(1) == 1) {

                        modificador = new Modificador();
                        modificador.setIdModificador(rs.getInt(2));
                        modificador.setCodigo(rs.getString(4));
                        modificador.setDescripcion(rs.getString(5));
                        modificadorList.add(modificador);

                        modificador = null;
                    } else if (rs.getInt(1) == 2) {
                        detalleModificador = new DetalleModificador();
                        detalleModificador.setIdModificador(rs.getInt(2));
                        detalleModificador.setIdDetalleModificador(rs.getInt(3));
                        detalleModificador.setCodigoModificador(rs.getString(4));
                        detalleModificador.setDescripcionModificador(rs.getString(5));
                        detalleModificador.setMonto(rs.getBigDecimal(6));
                        detalleModificador.setFactorModificador(rs.getShort(7));
                        detalleModificadorList.add(detalleModificador);
                        detalleModificador = null;
                    }
                }
                for (int i = 0; i < modificadorList.size(); i++) {
                    for (int a = 0; a < detalleModificadorList.size(); a++) {
                        if (modificadorList.get(i).getIdModificador() == detalleModificadorList.get(a).getIdModificador()) {
                            modificadorList.get(i).getDetalleModificadorList().add(detalleModificadorList.get(a));
                        }

                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                modificadorList = null;
            } finally {
                try {
                    cs.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {

            modificadorList = null;
        }


        return modificadorList;
    }

    public mCustomer ObtenerCliente(int idCliente) {

        PreparedStatement ps = null;
        Connection con = conn;
        mCustomer customer = new mCustomer();
        ResultSet rs = null;
        if (con != null) {

            try {
                ps = con.prepareStatement("select mc.iIdCliente,mc.cPrimerNombre,mc.cApellidoPaterno," +
                        "mc.cApellidoMaterno,mc.cEmail,mc.cNumeroTelefono,mc.cDireccion,mc.iTipoCliente,mc.cRazonSocial,mc.cNumeroRuc,mc.id_Tipo_Documento " +
                        "  " +
                        "from maestroCliente as mc inner join TipodeDocumento as tp on mc.id_Tipo_Documento=tp.iIdTipoDocumento " +
                        "where iIdCompany=? and iIdCliente=?");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idCliente);
                ps.execute();


                rs = ps.getResultSet();

                while (rs.next()) {

                    customer.setiId(rs.getInt(1));
                    customer.setcName(rs.getString(2));
                    customer.setcApellidoPaterno(rs.getString(3));
                    customer.setcApellidoMaterno(rs.getString(4));
                    customer.setcEmail(rs.getString(5));
                    customer.setcNumberPhone(rs.getString(6));
                    customer.setcDireccion(rs.getString(7));
                    customer.setTipoCliente(rs.getInt(8));
                    customer.setRazonSocial(rs.getString(9));
                    customer.setNumeroRuc(rs.getString(10));
                    customer.setIdTipoDocumento(rs.getInt(11));

                }


            } catch (SQLException e) {
                e.printStackTrace();
                customer = new mCustomer();
                customer.setiId(-99);
            } finally {
                try {
                    rs.close();
                    ps.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            customer = new mCustomer();
            customer.setiId(-98);
            conn = getConnection();

        }

        return customer;

    }

    public Modificador InsertarModificador(String Descripcion) {

        Modificador modificador = new Modificador();
        CallableStatement cs = null;


        if (conn != null) {

            try {
                cs = conn.prepareCall("call sp_agregar_cabecera_modificador(?,?,?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, Constantes.Terminal.idTerminal);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setString(5, Descripcion);
                cs.registerOutParameter(6, Types.INTEGER);
                cs.registerOutParameter(7, Types.INTEGER);
                cs.execute();

                modificador.setDescripcion(Descripcion);

                modificador.setNumItem(cs.getInt(6));
                modificador.setIdModificador(cs.getInt(7));

            } catch (SQLException e) {
                e.printStackTrace();
                modificador = new Modificador();
                modificador.setIdModificador(-5);
            } finally {

                try {
                    cs.close();


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {

            modificador = new Modificador();
            modificador.setIdModificador(-10);

        }

        return modificador;
    }

    public DetalleModificador GuardarDetalleModificador(int idModificador, DetalleModificador a) {


        PreparedStatement ps = null;
        DetalleModificador detalleModificador = new DetalleModificador();
        CallableStatement cs = null;
        int numItem = 0;
        if (conn != null) {

            try {

                cs = conn.prepareCall("call sp_guardar_valor_detalle_ModificadorV2(?,?,?,?,?,?,?,?,?,?)");
                cs.setInt(1, idModificador);
                cs.setString(2, a.getDescripcionModificador());
                cs.registerOutParameter(3, Types.INTEGER);
                cs.registerOutParameter(4, Types.INTEGER);
                cs.setInt(5, Constantes.Empresa.idEmpresa);
                cs.setInt(6, Constantes.Terminal.idTerminal);
                cs.setInt(7, Constantes.Tienda.idTienda);
                cs.setInt(8, Constantes.Usuario.idUsuario);
                cs.setBigDecimal(9, a.getMonto());
                cs.setShort(10, (short) a.getFactorModificador());
                cs.execute();
                numItem = cs.getInt(4);
                detalleModificador.setIdDetalleModificador(cs.getInt(3));
                detalleModificador.setIdModificador(idModificador);
                detalleModificador.setDescripcionModificador(a.getDescripcionModificador());
                detalleModificador.setMonto(a.getMonto());
                detalleModificador.setFactorModificador(a.getFactorModificador());


            } catch (SQLException e) {
                e.printStackTrace();
                detalleModificador = new DetalleModificador();
                detalleModificador.setIdDetalleModificador(-5);
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            detalleModificador = new DetalleModificador();
            detalleModificador.setIdDetalleModificador(-10);
        }
        return detalleModificador;
    }

    public List<mVentasVendedor> obtenerVentasPorCierre(int idCierre) {
        List<mVentasVendedor> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        mVentasVendedor v = null;
        if (conn != null) {

            try {
                ps = conn.prepareStatement(" select count(cv.iId_Cabecera_venta) as[Numero Ventas],cv.iId_Vendedor," +
                        "isnull(mv.cPrimerNombre,'Sin vendedor asignado'),isnull(mv.cSegundoNombre,'') " +
                        ",isnull(mv.cApellidoPaterno,''),isnull(mv.cApellidoMaterno,''),sum(cv.dTotal_Neto_Venta) as [Total Ventas] " +
                        " from Cabecera_venta as cv left join MaestroVendedor as mv on " +
                        " cv.iId_Vendedor=mv.iIdVendedor and mv.iIdCompany=? and iIdTienda=?" +
                        " where cv.iId_Company=? and cv.iId_Tienda=? and cv.id_cierre=? group by iId_Vendedor" +
                        ",mv.cPrimerNombre,mv.cSegundoNombre,mv.cApellidoPaterno,mv.cApellidoMaterno");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, Constantes.Tienda.idTienda);
                ps.setInt(5, idCierre);
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();
                while (rs.next()) {
                    v = new mVentasVendedor();
                    v.setNumeroVentas(rs.getInt(1));
                    v.getVendedor().setPrimerNombre(rs.getString(3));
                    v.getVendedor().setApellidoPaterno(rs.getString(5));
                    v.getVendedor().setApellidoMaterno(rs.getString(6));
                    v.setMontoVentas(rs.getBigDecimal(7));
                    lista.add(v);
                    v = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            } finally {
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            lista = null;
        }


        return lista;
    }

    public Modificador EliminarModificador(int idModificador) {

        Modificador modificador = new Modificador();
        PreparedStatement ps = null;


        if (conn != null) {

            try {
                ps = conn.prepareStatement("update Maestro_Modificadores set cEstado_Eliminado=? where id_company=? and id_Modificador=?");
                ps.setString(1, "E");
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, idModificador);
                ps.execute();
                modificador.setIdModificador(0);

            } catch (SQLException e) {
                e.printStackTrace();
                modificador = new Modificador();
                modificador.setIdModificador(-5);
            } finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } else {

            modificador = new Modificador();
            modificador.setIdModificador(-10);

        }

        return modificador;
    }

    public Modificador EditarModificador(int idModificador, String descripcion) {

        PreparedStatement ps = null;
        Modificador modificador;
        if (conn != null) {
            try {
                ps = conn.prepareStatement("update Maestro_Modificadores set cDescripcion_Modificador=? where id_Company=? and id_Modificador=?");
                ps.setString(1, descripcion);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, idModificador);
                ps.execute();

                modificador = new Modificador();
                modificador.setIdModificador(idModificador);
                modificador.setDescripcion(descripcion);
            } catch (SQLException e) {
                e.printStackTrace();
                modificador = new Modificador();
                modificador.setIdModificador(-5);

            } finally {
                try {

                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            modificador = new Modificador();
            modificador.setIdModificador(-10);

        }
        return modificador;
    }

    public DetalleModificador EliminarDetallaModificador(int idModificador, int idDetalleModificador) {

        PreparedStatement ps = null;

        DetalleModificador detalleModificador = new DetalleModificador();

        if (conn != null) {
            try {
                ps = conn.prepareStatement("update Detalle_Modificador  set cEstado_Eliminado=? where id_Company=? and id_Modificador=? and id_Detalle_Modificador=?");
                ps.setString(1, "E");
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, idModificador);
                ps.setInt(4, idDetalleModificador);
                ps.execute();

                detalleModificador = new DetalleModificador();
                detalleModificador.setIdModificador(0);

            } catch (SQLException e) {
                e.printStackTrace();
                detalleModificador = new DetalleModificador();
                detalleModificador.setIdModificador(-5);

            } finally {
                try {
                    ps.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            detalleModificador = new DetalleModificador();
            detalleModificador.setIdModificador(-10);

        }
        return detalleModificador;
    }

    public DetalleModificador EditarDetalleModificador(int idModificador, int idDetalleModificador, DetalleModificador d) {

        PreparedStatement ps = null;

        DetalleModificador detalleModificador;
        if (conn != null) {
            try {
                ps = conn.prepareStatement("update Detalle_Modificador set cDescripcion_DModificador=?" +
                        ",dMonto_Modificar=?,cConstante_Modificar=?" +
                        " where id_Company=? and id_Modificador=? and id_Detalle_Modificador=? ");
                ps.setString(1, d.getDescripcionModificador());
                ps.setBigDecimal(2, d.getMonto());
                ps.setShort(3, (short) d.getFactorModificador());
                ps.setInt(4, Constantes.Empresa.idEmpresa);
                ps.setInt(5, idModificador);
                ps.setInt(6, idDetalleModificador);
                ps.execute();

                detalleModificador = new DetalleModificador();
                detalleModificador.setIdModificador(idModificador);
                detalleModificador.setDescripcionModificador(d.getDescripcionModificador());
                detalleModificador.setIdDetalleModificador(idDetalleModificador);
                detalleModificador.setFactorModificador(d.getFactorModificador());
                detalleModificador.setMonto(d.getMonto());


            } catch (SQLException e) {
                e.printStackTrace();
                detalleModificador = new DetalleModificador();
                detalleModificador.setIdDetalleModificador(-5);
            } finally {
                try {
                    ps.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            detalleModificador = new DetalleModificador();
            detalleModificador.setIdDetalleModificador(-10);
        }
        return detalleModificador;
    }

    public byte AgregarCategoria(String cDescripcionCategoria) {

        byte respuesta = 0;

        CallableStatement cs = null;


        if (conn != null) {

            try {
                cs = conn.prepareCall("call sp_agregar_categoria(?,?,?,?,?,?)");
                cs.setString(1, cDescripcionCategoria);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Terminal.idTerminal);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setInt(5, Constantes.Tienda.idTienda);
                cs.registerOutParameter(6, Types.TINYINT);
                cs.execute();

                respuesta = cs.getByte(6);


            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {

            respuesta = 98;
        }
        return respuesta;


    }

    public RespuestaEliminar EliminarCategoria(int idCategoria) {

        RespuestaEliminar respuestaEliminar = new RespuestaEliminar();

        CallableStatement cs = null;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_eliminar_categoria(?,?,?,?,?,?,?)");
                cs.setInt(1, idCategoria);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Terminal.idTerminal);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setInt(5, Constantes.Tienda.idTienda);
                cs.registerOutParameter(6, Types.INTEGER);
                cs.registerOutParameter(7, Types.TINYINT);
                cs.execute();

                respuestaEliminar.setCantidad(cs.getInt(6));
                respuestaEliminar.setRespuesta(cs.getByte(7));

            } catch (SQLException e) {
                e.printStackTrace();
                respuestaEliminar = new RespuestaEliminar();
                respuestaEliminar.setRespuesta((byte) 98);
            } finally {
                try {

                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        } else {

            respuestaEliminar = new RespuestaEliminar();
            respuestaEliminar.setRespuesta((byte) 97);
        }

        return respuestaEliminar;
    }

    public byte ModificarCategoria(int idCategoria, String cDescripcion) {

        byte respuesta = 98;

        CallableStatement cs = null;


        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_editar_categoria(?,?,?,?,?,?,?)");
                cs.setInt(1, idCategoria);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setString(3, cDescripcion);
                cs.setInt(4, Constantes.Terminal.idTerminal);
                cs.setInt(5, Constantes.Tienda.idTienda);
                cs.setInt(6, Constantes.Usuario.idUsuario);
                cs.setByte(7, (byte) 98);
                cs.registerOutParameter(7, Types.TINYINT);
                cs.execute();

                respuesta = cs.getByte(7);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    cs.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {

            respuesta = 98;

        }

        return respuesta;

    }

    public List<mTipo_Pago> getTipoPago() {

        List<mTipo_Pago> mTipo_pagoList = new ArrayList<>();
        mTipo_Pago tipoPago;
        CallableStatement cs = null;
        ResultSet rs = null;

        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_obtener_tipo_pago");
                cs.execute();

                rs = cs.getResultSet();

                while (rs.next()) {
                    tipoPago = new mTipo_Pago();
                    tipoPago.setIdTipoPago(rs.getInt(1));
                    tipoPago.setcDescripcion(rs.getString(2));
                    mTipo_pagoList.add(tipoPago);
                    tipoPago = null;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                mTipo_pagoList = null;
            } finally {
                try {
                    cs.close();
                    rs.close();

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        } else {

            mTipo_pagoList = null;

        }

        return mTipo_pagoList;
    }

    public byte guardarMedioPago(String codigo, String descripcion, int idTipo, BigDecimal valorMinimo, String nombreImagen) {
        byte respuesta = 99;

        CallableStatement cs = null;
        Connection con = getConnection();

        if (con != null) {
            try {
                cs = con.prepareCall("call sp_guardar_medio_pago(?,?,?,?,?,?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, Constantes.Terminal.idTerminal);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setString(5, codigo);
                cs.setString(6, descripcion);
                cs.setInt(7, idTipo);
                cs.setBigDecimal(8, valorMinimo);
                cs.setString(9, nombreImagen);
                cs.registerOutParameter(10, Types.TINYINT);
                cs.setByte(10, respuesta);
                cs.execute();

                respuesta = cs.getByte(10);


            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {

            respuesta = 98;
        }

        return respuesta;

    }

    public byte editarMedioPago(int idMedio, String codigo, String descripcion, int idTipo, BigDecimal valorMinimo, String nombreImagen) {

        byte respuesta = 98;
        CallableStatement cs = null;
        Connection con = getConnection();

        if (con != null) {

            try {
                cs = con.prepareCall("call sp_editar_medio_pago(?,?,?,?,?,?,?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, Constantes.Terminal.idTerminal);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setString(5, codigo);
                cs.setString(6, descripcion);
                cs.setInt(7, idTipo);
                cs.setBigDecimal(8, valorMinimo);
                cs.setString(9, nombreImagen);
                cs.registerOutParameter(10, Types.TINYINT);
                cs.setByte(10, respuesta);
                cs.setInt(11, idMedio);
                cs.execute();

                respuesta = cs.getByte(10);


            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            respuesta = 98;
        }

        return respuesta;
    }

    public byte eliminarMedioPago(int idMedioPago) {
        byte respuesta = 98;
        CallableStatement cs = null;
        Connection con = getConnection();

        if (con != null) {
            try {
                cs = con.prepareCall("");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, Constantes.Terminal.idTerminal);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setInt(5, idMedioPago);
                cs.registerOutParameter(6, Types.TINYINT);
                cs.setByte(6, respuesta);

                cs.execute();

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {

                    e.printStackTrace();
                }
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {

            respuesta = 98;
        }
        return respuesta;

    }

    public List<mTipoTienda> ObtenerTiposTienda() {

        List<mTipoTienda> tipoTiendas = new ArrayList<>();
        mTipoTienda tipoTienda;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConnectionCreate();

        if (con != null) {

            try {
                ps = con.prepareStatement("select idSegmento,cDescripcion_Segmento from Segmento (NOLOCK) where bVisible_Interfaz=1");
                ps.execute();
                rs = ps.getResultSet();

                while (rs.next()) {

                    tipoTienda = new mTipoTienda();
                    tipoTienda.setIdTipoTienda(rs.getInt(1));
                    tipoTienda.setDescripcionTienda(rs.getString(2));
                    tipoTiendas.add(tipoTienda);
                    tipoTienda = null;
                }

                tipoTiendas.size();
            } catch (SQLException e) {
                e.printStackTrace();
                tipoTiendas = null;
            } finally {
                try {
                    con.close();
                    ps.close();
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } else {
            tipoTiendas = null;
        }

        return tipoTiendas;
    }

    public mNumServicioConsulta GetNumeroSoporte() {
        Connection con = getConnectionCreate();
        CallableStatement cs = null;
        mNumServicioConsulta result = new mNumServicioConsulta();

        try {

            cs = con.prepareCall("call sp_get_info_soporte_tecnico_app");
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                result.setEstado(true);
                result.setResultado(rs.getString(1));
            }

        } catch (Exception ex) {


        } finally {
            try {
                con.close();
            } catch (Exception ex) {

            }

        }
        return result;
    }

    public ResultRegisterUser RegistrarUsuarioV2(mUsuario usuario) {

        Connection con = getConnectionCreate();
        CallableStatement cs = null;
        int CodeResult = 0;
        String mensaje = "";
        try {
            cs = con.prepareCall("call plicencia_v3(" + ParamStoreProcedure(11) + ")");
            cs.setString(1, usuario.getNombreUsuario());
            cs.setString(2, usuario.getNombreTienda());
            cs.setString(3, usuario.getEmail());
            cs.setString(4, usuario.getContrasena());
            cs.setString(5, usuario.getTelefono());
            cs.setInt(6, usuario.getIdSegmento());
            cs.setString(7, usuario.getcDireccion());//Direccion
            cs.setString(8, usuario.getcNumRuc());//NumRuc
            cs.registerOutParameter(9, Types.CHAR);
            cs.registerOutParameter(10, Types.INTEGER);
            cs.registerOutParameter(11, Types.VARCHAR);
            cs.execute();
            CodeResult = cs.getInt(10);
            mensaje = cs.getString(11);
            return new ResultRegisterUser(CodeResult, mensaje);
        } catch (Exception e) {

            return new ResultRegisterUser(0, "Error al conectarse para registrar");
        }

    }

    //Crear nueva empresa con email
    @Deprecated
    public String RegistrarUsuario(mUsuario usuario) {

        String respuesta = "n";
        CallableStatement cs = null;
        Connection con = getConnectionCreate();

        if (con != null) {
            try {

                cs = con.prepareCall("call sp_validar_email_existencia(?,?)");
                cs.setString(1, usuario.getEmail());
                cs.registerOutParameter(2, Types.CHAR);
                cs.execute();
                respuesta = cs.getString(2);
                if (respuesta.equals("i")) {
                    cs.clearParameters();
                    cs = con.prepareCall("call plicencia_v2(?,?,?,?,?,?,?)");
                    cs.setString(1, usuario.getNombreUsuario());
                    cs.setString(2, usuario.getNombreTienda());
                    cs.setString(3, usuario.getEmail());
                    cs.setString(4, usuario.getContrasena());
                    cs.setString(5, usuario.getTelefono());
                    cs.setInt(6, usuario.getIdSegmento());
                    cs.registerOutParameter(7, Types.CHAR);

                    cs.addBatch();

                    cs.execute();
                    respuesta = cs.getString(7);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = "1";
            } finally {
                try {
                    cs.close();
                    con.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            respuesta = "n";
        }
        return respuesta;
    }

    private boolean GenerarAreaPorDefecto() {

        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call sp_CrearAreaProduccionDefecto(?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setByte(2, (byte) 0);
            cs.execute();
            cs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public byte ObtenerRutaTokenApi(Connection con) throws SQLException {

        byte respuesta = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ps = con.prepareStatement("select cRutaApi,rtrim(ltrim(cToken)),rtrim(ltrim(cTokenP2)) from Tienda (NOLOCK) where iIdCompany=? and iIdTienda=? ");
        ps.setInt(1, Constantes.Empresa.idEmpresa);
        ps.setInt(2, Constantes.Tienda.idTienda);
        ps.execute();

        rs = ps.getResultSet();

        while (rs.next()) {

            Constantes.TokenFactura.tokenEnvio = rs.getString(2);
            Constantes.TokenFactura.rutaApi = rs.getString(1);
            Constantes.TokenFactura.tokenP2 = rs.getString(3);
        }
        Constantes.TokenFactura.tokenEnvio.length();
        Constantes.TokenFactura.rutaApi.length();
        if (rs != null) {
            rs.close();

        }
        if (ps != null) {
            ps.close();
        }

        return respuesta;
    }

    public void ObtenerServiciosUrl() {
        String respuesta = "";
        try {
            CallableStatement cs = connectionInfra.prepareCall("call ObtenerServicios(?)");
            cs.registerOutParameter(1, 12);
            cs.execute();
            respuesta = cs.getString(1);
            JSONParser parsearRsptaJson2 = new JSONParser();
            JSONArray jsonA = (JSONArray) parsearRsptaJson2.parse(respuesta);
            int a = jsonA.size();
            List<UrlAccess> lista = new ArrayList();
            String cad = "";
            for (int i = 0; i < a; i++) {
                JSONObject jsonO = (JSONObject) parsearRsptaJson2.parse(jsonA.get(i).toString());
                UrlAccess u = new UrlAccess();
                u.setCodeUrl(jsonO.get("Code").toString());
                u.setDescripcion(jsonO.get("Descripcion").toString());
                u.setUrl(jsonO.get("cURL").toString());
                lista.add(u);
            }
            Constantes.Links.links = lista;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
    }

    public byte ObtenerConfigTienda(int idTienda) {


        byte respuesta = 100;
        Connection con = getConnectionCreate();
        Constantes.Tienda.idTienda = idTienda;
        ObtenerPermisosUsuario(con);
        try {
            ObtenerRutaTokenApi(con);
            GenerarConfiguracionEmpresa();
            SimboloMonedaPorDefecto();
            ObtenerConfiguracionEmpresa();
            ObtenerIgv();
            ObtenerTiposDocPago();
            ObtenerTiposDocIdentificacion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return respuesta;
    }

    public String ObtenerNombreTienda(Connection con) {

        String nombre = "";

        PreparedStatement ps = null;
        boolean v = false;
        boolean c = false;
        try {
            ps = con.prepareStatement("select cDescripcion_Tienda,bPrincipal,bZonasServicio,cTipoNegocio" +
                    " from tienda (NOLOCK)  where iIdCompany=? and iIdTienda=?");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                Constantes.Tienda.nombreTienda = rs.getString(1);
                Constantes.Tienda.esPrincipal = rs.getBoolean(2);
                //    Constantes.Tienda.ZonasAtencion=rs.getBoolean(3);
                //    Constantes.Tienda.cTipoZonaServicio=rs.getString(4);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombre;
    }

    public void GetConfigGeneral() {

        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call sp_get_config_general (?,?,?,?,?,?,?,?,?,?)");
            cs.setInt(1, 0);
            cs.setString(2, "");
            cs.setString(3, "");
            cs.setString(4, "");
            cs.setInt(5, 0);
            cs.setInt(6, Constantes.Empresa.idEmpresa);
            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, Types.DECIMAL);
            cs.registerOutParameter(9, Types.INTEGER);
            cs.registerOutParameter(10, Types.VARCHAR);
            cs.execute();

            Constantes.IGV.valorIgv = cs.getBigDecimal(8);
            Constantes.DivisaPorDefecto.idDivisaSunat = cs.getInt(9);
            Constantes.DivisaPorDefecto.SimboloDivisa = cs.getString(10);


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private String ParamStoreProcedure(int numParam) {

        String c = "";
        for (int i = 0; i < numParam; i++) {
            c = c + "?,";
        }
        if (numParam > 0) {
            c = c.substring(0, c.length() - 1);
        }
        return c;
    }

    public void GetConfiguracionEmpresa() {

        CallableStatement cs = null;

        try {
            cs = conn.prepareCall("call sp_get_config_Empresa_v9_DEMO(" + ParamStoreProcedure(2) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.execute();

            ResultSet rs = cs.getResultSet();
            if (rs != null) {

                List<Control1Cliente> list1 = new ArrayList();
                List<Control2Cliente> list2 = new ArrayList();

                Control2Cliente b = new Control2Cliente();
                b.setIdControl1Cliente(0);
                b.setIdControl2Cliente(0);
                b.setDescripcicionControl2Cliente("Todos");
                list2.add(b);
                Control1Cliente a = new Control1Cliente();
                a.setDescripcionControl("Todos");
                a.setIdControl(0);
                list1.add(a);
                while (rs.next()) {
                    Constantes.IGV.valorIgv = rs.getBigDecimal("valIgv");
                    Constantes.DivisaPorDefecto.idDivisaSunat = rs.getInt("id_divisa_sunat");
                    Constantes.DivisaPorDefecto.SimboloDivisa = rs.getString("SimboloMoneda");
                    Constantes.ConfigTienda.tieneAreaDespacho = rs.getBoolean("bTiene_Despacho");
                    Constantes.ConfigTienda.nombreConCategoria = rs.getBoolean("bNombresCompuesto");
                    Constantes.ConfigTienda.precioConIgv = rs.getBoolean("bPrecioConIgv");
                    Constantes.ConfigTienda.bUsa_Facturacion = rs.getBoolean("bUsa_Facturacion");
                    Constantes.PieImpresion.pieNotaVenta = rs.getString("PieNotaVenta");
                    Constantes.PieImpresion.pieFactura = rs.getString("PieFactura");
                    Constantes.PieImpresion.pieBoleta = rs.getString("PieBoleta");
                    Constantes.Tienda.ZonasAtencion = rs.getBoolean("bUsaZonaServicio");
                    Constantes.ConfigTienda.idTipoZonaServicio = rs.getInt("idTipo_Zona_Servicio");
                    Constantes.Tienda.cTipoZonaServicio = rs.getString("cTipoZonaServicio");
                    Constantes.ConfigTienda.visibleNumDocumento = rs.getBoolean("bVisibleNumDocumento");
                    Constantes.ConfigTienda.visibleBusquedaAvanzadaCliente = rs.getBoolean("bVisibleBusquedaAvanzadaCliente");
                    Constantes.ConfigTienda.ObtenerControlClientes = rs.getBoolean("bObtenerControlClientes");
                    Constantes.ConfigTienda.pagoUnico = rs.getBoolean("bPagoUnicoActivo");
                    Constantes.ConfigTienda.usaPromocion = rs.getBoolean("bUsaPromocion");
                    Constantes.ConfigTienda.bUsaTipoAtencion = rs.getBoolean("bUsaTipoAtencion");
                    Constantes.ConfigTienda.CodeFacturacion = rs.getString("cCodigoFacturador");
                    Constantes.ConfigTienda.CabeceraPieTicketAdicional = rs.getBoolean("bCabeceraPedidoEnPieTicketPedido");
                    Constantes.ConfigTienda.ContenidoPieTicketAdicional = rs.getString("cContenidoPieTicketPedido");
                    Constantes.ConfigTienda.bPieTicketAdicional = rs.getBoolean("bAdiccionalPieTicketPedido");
                    Constantes.ConfigTienda.LinkTicket = rs.getString("LinkTicketImpresion");
                    Constantes.ConfigTienda.bCategoriaImpresion = rs.getBoolean("cCategoriaImpresion");
                    Constantes.ConfigTienda.bVentaCredito = rs.getBoolean("bVentaCredito");
                    Constantes.ConfigTienda.idDocumentoPagoDefecto = rs.getInt("idDocumentoPagoDefecto");
                    Constantes.ConfigTienda.bUsaFechaEntrega = rs.getBoolean("bUsa_FechaEntrega");
                    Constantes.ConfigTienda.bImprimePagosPrecuenta = rs.getBoolean("bImprimePagosPrecuenta");
                    Constantes.ConfigTienda.bImprimePrecuentaAutomatica = rs.getBoolean("bImprimePreCuentaGuardarPedido");
                    Constantes.ConfigTienda.cMensajePrecuenta = rs.getString("cMensajePrecuenta");
                    Constantes.ConfigTienda.bCategoriaDefecto = rs.getBoolean("bUsaCategoriaDefecto");
                    Constantes.ConfigTienda.idCategoriaDefecto = rs.getInt("iIdCategoriaDefecto");
                    Constantes.ConfigTienda.cLinkBaseWeb = rs.getString("cLink_Base");
                    Constantes.ConfigTienda.cLinkAddPedidoNuevo = rs.getString("urlNuevo");
                    Constantes.ConfigTienda.cLinkAddPedidoExistente = rs.getString("urlExistente");
                    Constantes.ConfigTienda.cLinkAddPedidoConsulta = rs.getString("urlConsulta");
                    Constantes.ConfigTienda.iTiempoLecturaPedido = rs.getInt("MinutosConsultaPedido");
                    Constantes.ConfigTienda.bUsaAdelantoPagoPedido = rs.getBoolean("bUsaAdelantoPagoPedido");
                    Constantes.ConfigTienda.cTipoPantallaPedido = rs.getString("ctipo_pantalla_pedido");
                    Constantes.ConfigTienda.cConfiguracionPantallaPedido = rs.getString("cCambioTipoPantalla");

                    Constantes.ConfigTienda.bVisibleBtnCambioPantalla = rs.getBoolean("bVisible_Btn_Cambio_Mod_Pedido");
                    Constantes.ConfigTienda.bUsaAforo = rs.getBoolean("bUsa_Aforo");

                }
                for (int i = 0; i < list1.size(); i++) {
                    for (int j = 0; j < list2.size(); j++) {
                        if (((Control1Cliente) list1.get(i)).getIdControl() == ((Control2Cliente) list2.get(j)).getIdControl1Cliente()) {
                            ((Control1Cliente) list1.get(i)).getListaControl2().add(list2.get(j));
                        }
                    }
                }
                Constantes.ControlCliente.control1Clientes = list1;
                Constantes.ControlCliente.control2Clientes = list2;
                rs.close();
            }
            cs.close();
        } catch (SQLException esq) {
            Log.i("Info_sql", esq.toString());
            esq.printStackTrace();
            esq.toString();
        } catch (Exception ex) {
            Log.i("Info_sql", ex.toString());
        }

    }

    public List<mProduct> GetProductsConTiempo() {

        List<mProduct> list = new ArrayList<>();
        try {
            CallableStatement cs = conn.prepareCall("call p_api_listaproductotiempo(" + ParamStoreProcedure(2) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.execute();

            ResultSet rs = cs.getResultSet();
            while (rs.next()) {

                mProduct product = new mProduct();
                product.setIdProduct(rs.getInt(1));
                product.setcKey(rs.getString(2));
                product.setcProductName(rs.getString(3));
                product.setbImage(rs.getBytes(4));
                product.setCodigoColor(rs.getString(5));
                product.setCodigoForma(rs.getString(6));
                product.setDescripcionCategoria(rs.getString(7));
                product.setTipoRepresentacionImagen(rs.getByte(8));
                product.setPrecioVenta(rs.getBigDecimal(9));
                product.setUnidadMedida(rs.getString(10));
                product.setIdCategoria(rs.getInt(11));
                product.setDescripcionCategoria(rs.getString(12));
                list.add(product);

            }

        } catch (Exception ex) {

        }

        return list;
    }

    public void GetConfigApp() {

        CallableStatement cs = null;

        try {

            cs = conn.prepareCall("call sp_config_app_interna");
            cs.execute();

            ResultSet rs = cs.getResultSet();


            List<mTipoDocumento> lista = new ArrayList<>();
            mTipoDocumento t = null;
            mDocPago doc = null;
            List<mDocPago> listaPagos = new ArrayList<>();
            while (rs.next()) {
                switch (rs.getInt(1)) {
                    case 1:
                        t = new mTipoDocumento();
                        t.setIdTipoDocumento(rs.getInt(2));
                        t.setIdDocSunat(rs.getInt(3));
                        t.setCDescripcionCorta(rs.getString(4));
                        t.setBVerificarDireccion(rs.getBoolean(5));
                        t.setLongitudNumeroDoc(rs.getInt(6));
                        t.setDenominacionNumero(rs.getString(7));
                        t.setDenominacionCliente(rs.getString(8));
                        t.setVerificaRuc(rs.getBoolean(9));
                        lista.add(t);
                        break;
                    case 2:
                        doc = new mDocPago();
                        doc.setIdDoc(rs.getInt(2));
                        doc.setIdEnvio(rs.getInt(3));
                        doc.setCDescripcion(rs.getString(4));
                        doc.setGeneraDocPago(rs.getBoolean(5));
                        listaPagos.add(doc);
                        break;
                }
            }
            Constantes.TiposDocumentoId.listadoDocumentos = lista;
            Constantes.TiposDocPago.listaTipoDocPago = listaPagos;
        } catch (Exception e) {
            e.toString();
        }
    }

    public LogPinResult LogPinUserV2(String pinUser, String imei, String marcaTerminal,
                                     String modeloTerminal, String versionAndroid,
                                     int idUser, boolean reingreso) {
        LogPinResult result = new LogPinResult();
        CallableStatement cs = null;
        if (!reingreso) {
            idUser = 0;
        }


        byte resultado = 0;
        try {
            cs = connectionInfra.prepareCall("call sp_login_pin_user_v5(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.registerOutParameter(2, Types.INTEGER);
            cs.setString(3, pinUser);
            cs.registerOutParameter(4, Types.TINYINT);
            cs.setInt(5, idUser);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.registerOutParameter(7, Types.BIT);
            cs.setString(8, imei);
            cs.setString(9, marcaTerminal);
            cs.setString(10, modeloTerminal);
            cs.setString(11, versionAndroid);
            cs.registerOutParameter(12, Types.INTEGER);
            cs.registerOutParameter(13, Types.TINYINT);
            cs.registerOutParameter(14, Types.VARCHAR);
            cs.registerOutParameter(15, Types.BIT);
            cs.registerOutParameter(16, Types.VARCHAR);
            cs.registerOutParameter(17, Types.VARCHAR);
            cs.registerOutParameter(18, Types.VARCHAR);
            cs.registerOutParameter(19, Types.VARCHAR);
            cs.registerOutParameter(20, Types.VARCHAR);
            cs.execute();

            resultado = cs.getByte(4);
            result.setCode(cs.getByte(4));
            result.setMessageResult(cs.getString(19));
            if (resultado == 100) {
                Constantes.Usuario.idUsuario = cs.getInt(5);
                Constantes.Empresa.nombrePropietario = cs.getString(6);
                Constantes.Usuario.esAdministrador = cs.getBoolean(7);
                Constantes.Terminal.idTerminal = cs.getInt(12);
                Constantes.TiposAtencion.lista = obtenerTiposAtencion();

                if (!reingreso) {
                    ObtenerPermisosUsuario(connectionInfra);
                }
                GetConfigApp();
                ObtenerServiciosUrlV2(cs.getString(20));
                // ObtenerServiciosUrl();

                if (!Constantes.Usuario.esAdministrador) {
                    Constantes.Tienda.idTienda = cs.getInt(2);
                    Constantes.Tienda.nombreTienda = cs.getString(14);
                    Constantes.Tienda.esPrincipal = cs.getBoolean(15);
                    Constantes.TokenFactura.tokenEnvio = cs.getString(17);
                    Constantes.TokenFactura.rutaApi = cs.getString(16);
                    Constantes.TokenFactura.tokenP2 = cs.getString(18);
                    GetConfiguracionEmpresa();
                    resultado = 100;
                } else {

                    if (Constantes.Tienda.NumTiendas == 1) {
                        Constantes.TokenFactura.tokenEnvio = cs.getString(17);
                        Constantes.TokenFactura.rutaApi = cs.getString(16);
                        Constantes.TokenFactura.tokenP2 = cs.getString(18);
                        result.setAccesoDirecto(true);
                        Constantes.Tienda.idTienda = Constantes.Tiendas.tiendaList.get(0).getIdTienda();
                        Constantes.Tienda.nombreTienda = Constantes.Tiendas.tiendaList.get(0).getNombreTienda();
                        Constantes.Tienda.esPrincipal = true;
                        GetConfiguracionEmpresa();
                    } else {
                        result.setAccesoDirecto(false);
                    }
                    resultado = 101;

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            resultado = 99;
            result.setMessageResult("Verifique su conexión a internet. Reinicie la aplicacion");
            result.setCode(resultado);
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public void GetConfigAddicTienda(Connection con) {

        CallableStatement cs = null;

        try {
            cs = con.prepareCall("call sp_get_config_tienda_infra(?,?,?,?,?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.BIT);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.execute();

            Constantes.Tienda.nombreTienda = cs.getString(3);
            Constantes.Tienda.esPrincipal = cs.getBoolean(4);
            Constantes.TokenFactura.rutaApi = cs.getString(5);
            Constantes.TokenFactura.tokenEnvio = cs.getString(6);
            Constantes.TokenFactura.tokenP2 = cs.getString(7);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Map<Integer, Object> GuardarUrlCompanyTienda(String url) {
        Map<Integer, Object> mapResult = new HashMap<Integer, Object>();

        try {
            CallableStatement cs = connectionInfra.prepareCall("call sp_guardar_codigo_web_company_tienda(" + ParamStoreProcedure(5) + ")");

            cs.setInt("@idCompany", Constantes.Empresa.idEmpresa);
            cs.setInt("@idTienda", Constantes.Tienda.idTienda);
            cs.setString("@cCodigoWeb", url);
            cs.registerOutParameter("@codeResult", Types.INTEGER);
            cs.registerOutParameter("@cMensajeResult", Types.VARCHAR);
            cs.execute();
            mapResult.put(1, cs.getInt("@codeResult"));
            mapResult.put(2, cs.getString("@cMensajeResult"));


        } catch (Exception e) {

            mapResult.put(1, 0);
            mapResult.put(2, "Existe un inconveniente al guardar la informacion.Verifique su conexión a internet.");
        }
        return mapResult;
    }

    public String GetCodeWeb() {

        try {
            CallableStatement cs = connectionInfra.prepareCall("call  sp_get_codigo_web_company_tienda(" + ParamStoreProcedure(3) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            String a = cs.getString(3);
            return a;
        } catch (Exception e) {
            return "";
        }

    }

    public boolean getConnectionBd() {
        boolean data = true;
        try {
            CallableStatement cs = conn.prepareCall("");
            cs.setQueryTimeout(10);
            cs.execute();

            long StartTime = System.currentTimeMillis();
            long EndTime = System.currentTimeMillis();
            long a = TimeUnit.MILLISECONDS.toSeconds(EndTime - StartTime);
        } catch (Exception e) {
            data = false;
        }
        return data;
    }

    public byte ObtenerConfigTiendaAdmin1(int idTienda) {
        byte respuesta = 0;
        Connection con = connectionInfra;
        Constantes.Tienda.idTienda = idTienda;
        try {
            GetConfiguracionEmpresa();
            GetConfigAddicTienda(con);
            /*
            ObtenerNombreTienda(con);
            ObtenerRutaTokenApi(con);*/
            // GenerarConfiguracionEmpresa();
            Constantes.TiposAtencion.lista = obtenerTiposAtencion();
            // ObtenerConfiguracionEmpresa();
            respuesta = 100;

        } catch (Exception e) {
            e.printStackTrace();
            respuesta = 99;
        }
        return respuesta;
    }


/*

    public byte GuardarIngresoAlmacenProcesarCompra(String fechaMov, String fechaGuia,
                                                    String fechaCompra, String numRemision,
                                                    String nombreProveedor, int idAlmacen,
                                                    List<mProduct>productList){


        byte respuesta=0;
        int factorConversion=0;
        int idTransaccion=0;
        CallableStatement cs=null;
        PreparedStatement ps=null;

        return respuesta;

    }

    */

    //login con pin para identificar a que tienda pertenece el usuario
    @Deprecated
    public byte loginPinUser(String pinUser, String imei, String marcaTerminal, String modeloTerminal, String versionAndroid) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        CallableStatement cs = null;
        byte resultado = 0;
        if (connectionInfra != null) {
            try {
                cs = connectionInfra.prepareCall("call sp_login_pin_user_v2(?,?,?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, 0);
                cs.setString(3, pinUser);
                cs.registerOutParameter(4, Types.TINYINT);
                cs.registerOutParameter(5, Types.INTEGER);
                cs.registerOutParameter(6, Types.VARCHAR);
                cs.registerOutParameter(7, Types.BIT);
                cs.execute();
                resultado = cs.getByte(4);
                if (resultado == 100) {
                    Constantes.Usuario.idUsuario = cs.getInt(5);
                    Constantes.Empresa.nombrePropietario = cs.getString(6);
                    Constantes.Usuario.esAdministrador = cs.getBoolean(7);
                    ObtenerIgv();
                    SimboloMonedaPorDefecto();

                    VerificarTerminalImei(imei, connectionInfra, marcaTerminal, modeloTerminal, versionAndroid);
                    ObtenerPermisosUsuario(connectionInfra);

                    ObtenerTiposDocIdentificacion();
                    ObtenerTiposDocPago();
                    ObtenerServiciosUrl();

                    //
                    if (!Constantes.Usuario.esAdministrador) {
                        ps = connectionInfra.prepareStatement("select ildTienda from Usuario where ildCompany=? and idUsuario=?");
                        ps.setInt(1, Constantes.Empresa.idEmpresa);
                        ps.setInt(2, Constantes.Usuario.idUsuario);
                        ps.execute();
                        rs = ps.getResultSet();
                        while (rs.next()) {
                            Constantes.Tienda.idTienda = rs.getInt(1);
                        }
                        ObtenerNombreTienda(connectionInfra);
                        ObtenerRutaTokenApi(connectionInfra);
                        GenerarConfiguracionEmpresa();
                        ObtenerConfiguracionEmpresa();
                        resultado = 100;
                    } else {
                        resultado = 101;
                    }
          /*           if(!Constantes.Usuario.esAdministrador) {
                     }
                     else{
                         resultado=101;
                     }
            */
                } else {
                }

            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
            }
        } else {
            resultado = 98;
        }

        return resultado;
    }

    public void ObtenerServiciosUrlV2(String servicios) {
        String respuesta = "";
        try {
            respuesta = servicios;
            JSONParser parsearRsptaJson2 = new JSONParser();
            JSONArray jsonA = (JSONArray) parsearRsptaJson2.parse(respuesta);
            int a = jsonA.size();
            List<UrlAccess> lista = new ArrayList();
            String cad = "";
            for (int i = 0; i < a; i++) {
                JSONObject jsonO = (JSONObject) parsearRsptaJson2.parse(jsonA.get(i).toString());
                UrlAccess u = new UrlAccess();
                u.setCodeUrl(jsonO.get("Code").toString());
                u.setDescripcion(jsonO.get("Descripcion").toString());
                u.setUrl(jsonO.get("cURL").toString());
                lista.add(u);
            }
            Constantes.Links.links = lista;
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
    }

    private byte CrearDivisaPorDefecto(Connection con) {
        byte respuesta = 0;
        CallableStatement cs = null;
        if (con != null) {
            try {
                cs = con.prepareCall("call sp_ingresar_moneda_defecto(?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.registerOutParameter(2, Types.TINYINT);
                cs.execute();
                respuesta = cs.getByte(2);
                Constantes.DivisaPorDefecto.SimboloDivisa = "S/";
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            respuesta = 98;
        }
        return respuesta;
    }

    //Obtener lista de acciones permitidas para el usuario
    private void ObtenerPermisosUsuario(Connection con) {

        CallableStatement cs = null;
        ResultSet rs = null;

        if (helper != null) {
            if (con != null) {
                try {
                    cs = con.prepareCall("call sp_obtener_usuario_proceso(?,?,?)");
                    cs.setInt(1, Constantes.Empresa.idEmpresa);
                    cs.setInt(2, 0);
                    cs.setInt(3, Constantes.Usuario.idUsuario);
                    cs.execute();

                    rs = cs.getResultSet();

                    helper.DeleteAccesoRolAcceso();
                    while (rs.next()) {

                        helper.InsertRolAcceso(rs.getInt(1), rs.getBoolean(2));

                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {

                    try {
                        rs.close();
                        cs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } else {
            }

        }
    }

    public mVendedor ObtenerVendedorPorId(int idVendedor) {

        mVendedor vendedor = null;
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (con != null) {

            try {
                ps = con.prepareStatement("select cPrimerNombre,cApellidoPaterno,cApellidoMaterno,cEmail,cNumeroTelefono from MaestroVendedor (NOLOCK) where iIdCompany=? and iIdTienda=? and iIdVendedor=?");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, idVendedor);
                ps.execute();


                rs = ps.getResultSet();
                vendedor = new mVendedor();
                while (rs.next()) {
                    vendedor.setIdVendedor(idVendedor);
                    vendedor.setPrimerNombre(rs.getString(1));
                    vendedor.setApellidoPaterno(rs.getString(2));
                    vendedor.setApellidoMaterno(rs.getString(3));
                    vendedor.setEmail(rs.getString(4));
                    vendedor.setNumeroTelefono(rs.getString(5));


                }


            } catch (SQLException e) {
                e.printStackTrace();
                vendedor = new mVendedor();
                vendedor.setIdVendedor(-99);
            }
        } else if (con == null) {

            vendedor = new mVendedor();
            vendedor.setIdVendedor(-98);

        }


        return vendedor;
    }

    public byte RegistroVendedor(mVendedor vendedor) {

        byte respuesta = 0;
        CallableStatement cs = null;
        Connection con = getConnection();


        if (con != null) {
            try {
                cs = con.prepareCall("call sp_insert_update_vendedor(?,?,?,?,?,?,?,?,?)");
                cs.setInt(1, vendedor.getIdVendedor());
                cs.setString(2, vendedor.getPrimerNombre());
                cs.setString(3, vendedor.getApellidoPaterno());
                cs.setString(4, vendedor.getApellidoMaterno());
                cs.setString(5, vendedor.getEmail());
                cs.setString(6, vendedor.getNumeroTelefono());
                cs.setInt(7, Constantes.Empresa.idEmpresa);
                cs.setInt(8, Constantes.Tienda.idTienda);
                cs.registerOutParameter(9, Types.TINYINT);
                cs.setByte(9, respuesta);

                cs.execute();
                respuesta = cs.getByte(9);

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }

        } else {
            respuesta = 98;
        }


        return respuesta;
    }

    //Buscar producto por medio del codigo de barra para agregarlo al detalle pedido
    public ProductoEnVenta BuscarProductoCodigoBarra(String codigoBarra, int idCabeceraPedido) {

        ProductoEnVenta productoEnVenta = new ProductoEnVenta();

        Connection con = conn;
        int r = 0;
        int respuesta = 0;
        CallableStatement cs = null;
        if (con != null) {
            try {
                cs = con.prepareCall("call sp_buscar_producto_codigo_barra(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Terminal.idTerminal);
                cs.setInt(3, Constantes.Tienda.idTienda);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setString(5, codigoBarra);
                cs.setInt(6, idCabeceraPedido);
                cs.setInt(7, respuesta);
                cs.registerOutParameter(7, Types.INTEGER);

                cs.registerOutParameter(8, Types.INTEGER);
                cs.registerOutParameter(9, Types.INTEGER);
                cs.registerOutParameter(10, Types.INTEGER);
                cs.registerOutParameter(11, Types.INTEGER);
                cs.registerOutParameter(12, Types.VARCHAR);
                cs.registerOutParameter(13, Types.DECIMAL);
                cs.registerOutParameter(14, Types.DECIMAL);
                cs.registerOutParameter(15, Types.VARCHAR);
                cs.setString(15, "");
                cs.registerOutParameter(16, Types.BIT);
                cs.setBoolean(16, false);
                cs.execute();

                productoEnVenta.setAccionGuardarCodeBar(cs.getInt(7));
                productoEnVenta.setItemNum(cs.getInt(8));
                productoEnVenta.setIdProducto(cs.getInt(9));
                productoEnVenta.setIdDetallePedido(cs.getInt(10));
                productoEnVenta.setCantidad(cs.getFloat(11));
                productoEnVenta.setProductName(cs.getString(12));
                productoEnVenta.setPrecioOriginal(cs.getBigDecimal(13));
                productoEnVenta.setPrecioVentaFinal(cs.getBigDecimal(14));
                productoEnVenta.setDescripcionVariante(cs.getString(15));
                productoEnVenta.setEsVariante(cs.getBoolean(16));


            } catch (SQLException e) {
                e.printStackTrace();
                productoEnVenta = new ProductoEnVenta();
                productoEnVenta.setIdProducto(-99);

            }
        } else {
            productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setIdProducto(-98);
        }
        return productoEnVenta;

    }

    public byte EliminarProductoDetallePedido(int idCabeceraPedido, int idDetallePedido) {

        byte resultado = 50;
        CallableStatement cs = null;
        Connection con = getConnection();

        if (con != null) {
            try {
                cs = con.prepareCall("call sp_Eliminar_producto_detalle_pedido(?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, idCabeceraPedido);
                cs.setInt(4, idDetallePedido);
                cs.setByte(5, resultado);
                cs.registerOutParameter(5, Types.TINYINT);
                cs.execute();

                resultado = cs.getByte(5);


            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
            }
        } else {
            resultado = 98;
        }

        return resultado;
    }

    public List<mAlmacen> ObtenerAlmacenes() {


        List<mAlmacen> almacenList = new ArrayList<>();
        mAlmacen almacen;
        PreparedStatement ps = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        if (conn != null) {

            try {
                cs = conn.prepareCall("call sp_obtener_almacenes_seleccion_movimientos(?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                 /*ps=conn.prepareStatement("select iId_Almacen,cDescripcion_Almacen,lTienda,iIdTiendaUbicacion" +
                        " from Almacenes (NOLOCK) where iIdCompany=?  and cEliminado='' order by iIdTiendaUbicacion ");
                ps.setInt(1,Constantes.Empresa.idEmpresa);
                cs.execute();*/
                rs = cs.executeQuery();

                while (rs.next()) {
                    almacen = new mAlmacen();
                    almacen.setIdAlmacen(rs.getInt(1));
                    almacen.setDescripcionAlmacen(rs.getString(2));
                    almacen.setTienda(rs.getBoolean(3));
                    almacen.setIdTienda(rs.getInt(4));
                    almacenList.add(almacen);

                }
            } catch (SQLException e) {
                e.printStackTrace();
                almacenList = null;
            } finally {
                try {
                    cs.close();
                    //   con.close();
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {

            almacenList = null;
        }

        return almacenList;
    }

    public List<mProduct> ProductosEnAlmacenSeleccionado(int idAlmacenOrigen, String descripcion) {

        List<mProduct> productList = new ArrayList<>();
        Connection con = conn;
        ResultSet rs = null;
        mProduct product = null;
        CallableStatement cs = null;
        //   PreparedStatement ps=null;
        if (con != null) {

            try {

                cs = con.prepareCall("call sp_obtener_productos_almacen_inventario_v2(?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, idAlmacenOrigen);
                cs.setString(3, descripcion);
                rs = cs.executeQuery();

                while (rs.next()) {

                    product = new mProduct();
                    product.setIdProduct(rs.getInt(1));
                    product.setCodigoBarra(rs.getString(2));
                    product.setcProductName(rs.getString(3));
                    product.setPrecioCompra(rs.getBigDecimal(4));
                    product.setdQuantity(rs.getFloat(5));
                    product.setcKey(rs.getString(6));
                    product.setDescripcionCategoria(rs.getString(7));
                    productList.add(product);
                    product = null;

                }
            } catch (SQLException e) {
                e.printStackTrace();
                productList = null;
            } finally {
                try {
                    cs.close();
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        } else {
            productList = null;
        }

        return productList;
    }

    public List<mProduct> ProductosEnAlmacenSeleccionadoPrincipal(int idAlmacenOrigen) {

        List<mProduct> productList = new ArrayList<>();
        Connection con = getConnection();
        ResultSet rs = null;
        mProduct product = null;
        PreparedStatement ps = null;
        if (con != null) {

            try {

                ps = con.prepareStatement("SELECT p.iIdProduct,p.cCodigo_Barra" +
                        ",p.cProductName + ' ' +p.cDescripcion_Variante ," +
                        " p.dPurcharsePrice,(sa.ncantidad - isnull(pd.Cantidad,0))" +
                        ",isnull(p.cKey,''),isnull(cp.cDescripcion_categoria,'' )" +
                        "FROM  Product as p inner join stock_almacen as sa (nolock) on" +
                        " p.iIdProduct=sa.iIdProduct left join productos_reserva_Pedido as pd (nolock) " +
                        " on p.iIdProduct=pd.iIdProducto " +
                        "  INNER JOIN Categoria_Productos AS cp (NOLOCK) " +
                        " on p.id_Categoria=cp.id_categoria_producto " +
                        "WHERE (p.bControl_Stock = 1) AND (p.cEstado_Visible = 'V') AND (p.bEstado_Variantes = 0)  AND " +
                        "(p.iIdCompany = ?) AND (p.cEliminado = '') AND (p.cEstado_Producto = 'A') and sa.iId_Almacen=? ");
                ps.setInt(1, Constantes.Empresa.idEmpresa);

                ps.setInt(2, idAlmacenOrigen);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {

                    product = new mProduct();
                    product.setIdProduct(rs.getInt(1));
                    product.setCodigoBarra(rs.getString(2));
                    product.setcProductName(rs.getString(3));
                    product.setPrecioCompra(rs.getBigDecimal(4));
                    product.setdQuantity(rs.getFloat(5));
                    product.setcKey(rs.getString(6));
                    product.setDescripcionCategoria(rs.getString(7));
                    productList.add(product);
                    product = null;

                }
            } catch (SQLException e) {
                e.printStackTrace();
                productList = null;
            } finally {
                try {
                    con.close();
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        } else {
            productList = null;
        }

        return productList;
    }

    public List<mProduct> ProductosParaCompra(String descripcion) {


        List<mProduct> productList = new ArrayList<>();
        Connection con = conn;
        ResultSet rs = null;
        mProduct product = null;
        CallableStatement cs = null;
        if (con != null) {

            try {

                cs = con.prepareCall("call sp_busqueda_productos_Ingreso_Inventario_v2(?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setString(2, descripcion);
                rs = cs.executeQuery();


                while (rs.next()) {

                    product = new mProduct();
                    product.setIdProduct(rs.getInt(1));
                    product.setCodigoBarra(rs.getString(2));
                    product.setcProductName(rs.getString(3));
                    product.setPrecioCompra(rs.getBigDecimal(4));
                    product.setDescripcionCategoria(rs.getString(8));
                    product.setcKey(rs.getString(9));
                    productList.add(product);
                    product = null;

                }
            } catch (SQLException e) {
                e.printStackTrace();
                productList = null;
            } finally {
                try {
                    //     con.close();
                    rs.close();
                    cs.close();
                    //     ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        } else {
            productList = null;
        }

        return productList;

    }

    public BigDecimal obtenerPrecioCompraProducto(int idProducto) {

        PreparedStatement ps = null;
        Connection con = conn;
        ResultSet rs = null;
        BigDecimal montoCompra = new BigDecimal(0);

        try {
            if (con != null) {
                ps = con.prepareStatement("SELECT        dPurcharsePrice\n" +
                        "FROM            dbo.Product\n" +
                        "WHERE        (iIdCompany = ?)  AND (iIdProduct = ?)");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idProducto);
                ps.execute();
                rs = ps.getResultSet();

                while (rs.next()) {
                    montoCompra = rs.getBigDecimal(1);
                }

            } else {


                montoCompra = new BigDecimal(-5);

            }
        } catch (Exception e) {
            e.toString();
            montoCompra = new BigDecimal(-10);
        }

        return montoCompra;
    }

    public List<mAlmacen> obtenerAlmacenesProducto(int idProducto) {


        List<mAlmacen> almacenList = new ArrayList<>();
        PreparedStatement ps = null;
        Connection con = conn;
        ResultSet rs = null;
        try {
            if (con != null) {
                try {
                    ps = con.prepareStatement("SELECT Stock_Almacen.iId_Almacen, Almacenes.cDescripcion_Almacen, Almacenes.lTienda " +
                            ", Stock_Almacen.ncantidad,Almacenes.iIdTiendaUbicacion FROM  Almacenes INNER JOIN " +
                            " Stock_Almacen ON Almacenes.iId_Almacen = Stock_Almacen.iId_Almacen " +
                            " WHERE (Almacenes.iIdCompany = ?) " +
                            " AND (Stock_Almacen.iIdCompany = ?) AND (Stock_Almacen.iIdProduct = ?)");
                    ps.setInt(1, Constantes.Empresa.idEmpresa);
                    ps.setInt(2, Constantes.Empresa.idEmpresa);
                    ps.setInt(3, idProducto);
                    ps.execute();

                    rs = ps.getResultSet();


                    while (rs.next()) {

                        almacenList.add(new mAlmacen(
                                rs.getInt(1),
                                rs.getString(2)
                                , rs.getBoolean(3),
                                rs.getFloat(4), rs.getInt(5)));

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    almacenList = null;
                }
            }
        } catch (Exception e) {
            toString();
            almacenList = null;
        }
        return almacenList;
    }

    public ProductoEnVenta EditarCantidadProducto(ProductoEnVenta p, int idCabeceraPedido) {

        CallableStatement cs = null;
        ProductoEnVenta productoEnVenta = new ProductoEnVenta();
        float cantidadOriginal = 0;
        float stockComprometido = 0;
        float stockFisico = 0;
        boolean agregar = true;
        int idt = 0;

        try {


            cs = conn.prepareCall("call sp_EditarCantidadProductoDetalleVenta_v2(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            cs.setFloat(1, p.getCantidad());
            cs.setInt(2, idCabeceraPedido);
            cs.setInt(3, p.getIdDetallePedido());
            cs.setInt(4, Constantes.Empresa.idEmpresa);
            cs.setInt(5, Constantes.Tienda.idTienda);
            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, Types.DECIMAL);
            cs.registerOutParameter(8, Types.DECIMAL);
            cs.registerOutParameter(9, Types.DECIMAL);
            cs.registerOutParameter(10, Types.BIT);
            cs.setBigDecimal(11, p.getMontoDescuento());
            cs.registerOutParameter(11, Types.DECIMAL);
            cs.setString(12, p.getObservacion());
            cs.registerOutParameter(12, Types.VARCHAR);
            cs.setBoolean(13, p.isUsaDescuento());
            cs.setBigDecimal(14, p.getPrecioOriginal());
            cs.registerOutParameter(14, Types.DECIMAL);
            cs.execute();

            productoEnVenta.setIdProducto(cs.getInt(6));
            productoEnVenta.setCantidad(p.getCantidad());
            stockFisico = cs.getFloat(7);
            stockComprometido = cs.getFloat(8);
            cantidadOriginal = cs.getFloat(9);
            agregar = cs.getBoolean(10);
            productoEnVenta.setPrecioOriginal(cs.getBigDecimal(14));
            productoEnVenta.setDisponibleStock(agregar);
            productoEnVenta.setStockActual(stockFisico);
            productoEnVenta.setCantidadReserva(stockComprometido);
            productoEnVenta.setMontoDescuento(cs.getBigDecimal(11));
            productoEnVenta.setObservacion(cs.getString(12));
            productoEnVenta.setUsaDescuento(p.isUsaDescuento());
            if (agregar) {
                productoEnVenta.setIdDetallePedido(p.getIdDetallePedido());
            } else {
                productoEnVenta.setIdDetallePedido(-98);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setIdDetallePedido(-99);
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return productoEnVenta;
    }

    public byte EditarMovAlmacenSinProcesar(int idMovCabecera, String fechaMov, String fechaGuia,
                                            String fechaCompra, String numRemision,
                                            String nombreProveedor, int idAlmacen, int idAlmacenDestino,
                                            List<mProduct> productList, String codTransaccion) {
        byte respuesta = 0;
        int factorConversion = 0;
        CallableStatement cs = null;
        PreparedStatement ps = null;

        try {
            if (conn != null) {

                try {
                    ps = conn.prepareStatement("delete Movimiento_Almacen_Detalle where iId_movimiento_almacen_cabecera=?");
                    ps.setInt(1, idMovCabecera);
                    ps.execute();
                    cs = conn.prepareCall("call sp_editar_cabecera_mov_almacen(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    cs.setInt(1, Constantes.Empresa.idEmpresa);
                    cs.setInt(2, Constantes.Tienda.idTienda);
                    cs.setInt(3, idAlmacen);
                    cs.setString(4, fechaMov);
                    cs.setString(5, fechaGuia);
                    cs.setString(6, fechaCompra);
                    cs.setString(7, numRemision);
                    cs.setString(8, nombreProveedor);
                    cs.registerOutParameter(9, Types.INTEGER);
                    cs.registerOutParameter(10, Types.INTEGER);
                    cs.setInt(11, Constantes.Usuario.idUsuario);
                    cs.setInt(12, Constantes.Terminal.idTerminal);
                    cs.setString(13, codTransaccion);
                    cs.setInt(14, idMovCabecera);
                    cs.setInt(15, idAlmacenDestino);
                    cs.execute();
                    factorConversion = cs.getInt(9);
                    ps.clearParameters();
                    ps.clearBatch();
                    ps = conn.prepareStatement("insert into Movimiento_Almacen_Detalle" +
                            "(iId_movimiento_almacen_cabecera,iIdProducto" +
                            ",cProductName,ncantidad_movimiento_detalle,nmonto_unitario_movimiento," +
                            "nmonto_total_movimiento) values (?,?,?,?,?,?)");
                    for (int i = 0; i < productList.size(); i++) {
                        ps.setInt(1, idMovCabecera);

                        ps.setInt(2, productList.get(i).getIdProduct());
                        ps.setString(3, productList.get(i).getcProductName());
                        ps.setFloat(4, productList.get(i).getdQuantity() * factorConversion);
                        ps.setBigDecimal(5, productList.get(i).getPrecioCompra().multiply(new BigDecimal(factorConversion)));
                        ps.setBigDecimal(6, productList.get(i).getPrecioCompra().multiply(new BigDecimal(productList.get(i).getdQuantity())).multiply(new BigDecimal(factorConversion)));
                        ps.addBatch();
                    }

                    ps.executeBatch();
                    respuesta = 101;

                } catch (SQLException e) {
                    e.printStackTrace();
                    respuesta = 99;
                } finally {
                    cs.close();
                    ps.close();
                }

            }
        } catch (Exception e) {
            respuesta = 98;
        }
        return respuesta;

    }

    public byte EditarMovAlmacenProcesar(int idMovCabecera, String fechaMov, String fechaGuia,
                                         String fechaCompra, String numRemision,
                                         String nombreProveedor, int idAlmacen, int idAlmacenDestino,
                                         List<mProduct> productList, String codTransaccion, boolean movSalida) {
        byte respuesta = 0;
        int factorConversion = 0;
        CallableStatement cs = null;
        PreparedStatement ps = null;

        try {
            if (conn != null) {
                try {
                    ps = conn.prepareStatement("delete Movimiento_Almacen_Detalle where iId_movimiento_almacen_cabecera=?");
                    ps.setInt(1, idMovCabecera);
                    ps.execute();
                    cs = conn.prepareCall("call sp_editar_cabecera_mov_almacen(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    cs.setInt(1, Constantes.Empresa.idEmpresa);
                    cs.setInt(2, Constantes.Tienda.idTienda);
                    cs.setInt(3, idAlmacen);
                    cs.setString(4, fechaMov);
                    cs.setString(5, fechaGuia);
                    cs.setString(6, fechaCompra);
                    cs.setString(7, numRemision);
                    cs.setString(8, nombreProveedor);
                    cs.registerOutParameter(9, Types.INTEGER);
                    cs.registerOutParameter(10, Types.INTEGER);
                    cs.setInt(11, Constantes.Usuario.idUsuario);
                    cs.setInt(12, Constantes.Terminal.idTerminal);
                    cs.setString(13, codTransaccion);
                    cs.setInt(14, idMovCabecera);
                    cs.setInt(15, idAlmacenDestino);
                    cs.execute();
                    factorConversion = cs.getInt(9);
                    ps.clearParameters();
                    ps = conn.prepareStatement("insert into Movimiento_Almacen_Detalle" +
                            "(iId_movimiento_almacen_cabecera,iIdProducto" +
                            ",cProductName,ncantidad_movimiento_detalle,nmonto_unitario_movimiento," +
                            "nmonto_total_movimiento) values (?,?,?,?,?,?)");
                    for (int i = 0; i < productList.size(); i++) {
                        ps.setInt(1, idMovCabecera);

                        ps.setInt(2, productList.get(i).getIdProduct());
                        ps.setString(3, productList.get(i).getcProductName());
                        ps.setFloat(4, productList.get(i).getdQuantity() * factorConversion);
                        ps.setBigDecimal(5, productList.get(i).getPrecioCompra().multiply(new BigDecimal(factorConversion)));
                        ps.setBigDecimal(6, productList.get(i).getPrecioCompra().multiply(new BigDecimal(productList.get(i).getdQuantity())).multiply(new BigDecimal(factorConversion)));
                        ps.addBatch();
                    }

                    ps.executeBatch();
                    if (codTransaccion.equals(Constantes.MovAlmacen.SalidaTransferencia)) {
                        cs.clearParameters();
                        cs.clearBatch();
                        cs = conn.prepareCall("call sp_generar_registro_salida_transferencia(?,?,?)");
                        cs.setInt(1, idMovCabecera);
                        cs.setInt(2, Constantes.Empresa.idEmpresa);
                        cs.setInt(3, Constantes.Tienda.idTienda);
                        cs.execute();

                    }


                    respuesta = 101;
                    if (!movSalida) {
                        cs = conn.prepareCall("call pActualiza_UltimoPrecioCompra(?)");
                        cs.setInt(1, idMovCabecera);
                        cs.execute();
                        cs.clearParameters();
                        cs.clearWarnings();
                    }
                    cs = conn.prepareCall("call pActualiza_Stock_AlamcenxVenta(?)");
                    cs.setInt(1, idMovCabecera);
                    cs.execute();
                    cs.clearWarnings();
                    cs.clearParameters();
                    respuesta = 102;

                } catch (SQLException e) {
                    e.printStackTrace();
                    respuesta = 99;
                } finally {
                    cs.close();
                    ps.close();
                }
            } else {
                respuesta = 98;
            }
        } catch (Exception e) {

            respuesta = 98;
        }


        return respuesta;

    }

    public byte GuardarMovAlmacenSinProcesar(String fechaMov, String fechaGuia,
                                             String fechaCompra, String numRemision,
                                             String nombreProveedor, int idAlmacen, int idAlmacenDestino,
                                             List<mProduct> productList, String codTransaccion) {

        byte respuesta = 0;
        int factorConversion = 0;
        int idTransaccion = 0;
        CallableStatement cs = null;
        PreparedStatement ps = null;

        try {
            if (conn != null) {
                try {
                    cs = conn.prepareCall("call sp_registrar_ingreso_compra_mov_alm_cabecera(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    cs.setInt(1, Constantes.Empresa.idEmpresa);
                    cs.setInt(2, Constantes.Tienda.idTienda);
                    cs.setInt(3, idAlmacen);
                    cs.setString(4, fechaMov);
                    cs.setString(5, fechaGuia);
                    cs.setString(6, fechaCompra);
                    cs.setString(7, numRemision);
                    cs.setString(8, nombreProveedor);
                    cs.setInt(9, factorConversion);
                    cs.registerOutParameter(9, Types.INTEGER);
                    cs.setInt(10, idTransaccion);
                    cs.registerOutParameter(10, Types.INTEGER);
                    cs.setInt(11, Constantes.Usuario.idUsuario);
                    cs.setInt(12, Constantes.Terminal.idTerminal);
                    cs.setString(13, codTransaccion);
                    cs.setInt(14, idAlmacenDestino);
                    cs.execute();

                    idTransaccion = cs.getInt(10);
                    factorConversion = cs.getInt(9);
                    ps = conn.prepareStatement("insert into Movimiento_Almacen_Detalle" +
                            "(iId_movimiento_almacen_cabecera,iIdProducto" +
                            ",cProductName,ncantidad_movimiento_detalle,nmonto_unitario_movimiento," +
                            "nmonto_total_movimiento) values (?,?,?,?,?,?)");
                    for (int i = 0; i < productList.size(); i++) {
                        ps.setInt(1, idTransaccion);

                        ps.setInt(2, productList.get(i).getIdProduct());
                        ps.setString(3, productList.get(i).getcProductName());
                        ps.setFloat(4, productList.get(i).getdQuantity() * factorConversion);
                        ps.setBigDecimal(5, productList.get(i).getPrecioCompra().multiply(new BigDecimal(factorConversion)));
                        ps.setBigDecimal(6, productList.get(i).getPrecioCompra().multiply(new BigDecimal(productList.get(i).getdQuantity())).multiply(new BigDecimal(factorConversion)));
                        ps.addBatch();
                    }
                    ps.executeBatch();

                    respuesta = 101;
                } catch (SQLException e) {
                    e.printStackTrace();
                    respuesta = 99;
                } finally {
                    cs.close();
                    ps.close();
                }
            } else {
                respuesta = 98;
            }
        } catch (Exception e) {
            respuesta = 98;
        }
        return respuesta;
    }

    public byte GuardarIngresoAlmacenProcesarCompra(String fechaMov, String fechaGuia,
                                                    String fechaCompra, String numRemision,
                                                    String nombreProveedor, int idAlmacen, int idAlmacenDestino,
                                                    List<mProduct> productList, String codTransaccion, boolean movSalida) {

        byte respuesta = 0;
        int factorConversion = 0;
        int idTransaccion = 0;
        CallableStatement cs = null;
        PreparedStatement ps = null;

        try {
            if (conn != null) {
                try {
                    cs = conn.prepareCall("call sp_registrar_ingreso_compra_mov_alm_cabecera(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    cs.setInt(1, Constantes.Empresa.idEmpresa);
                    cs.setInt(2, Constantes.Tienda.idTienda);
                    cs.setInt(3, idAlmacen);
                    cs.setString(4, fechaMov);
                    cs.setString(5, fechaGuia);
                    cs.setString(6, fechaCompra);
                    cs.setString(7, numRemision);
                    cs.setString(8, nombreProveedor);
                    cs.setInt(9, factorConversion);
                    cs.registerOutParameter(9, Types.INTEGER);
                    cs.setInt(10, idTransaccion);
                    cs.registerOutParameter(10, Types.INTEGER);
                    cs.setInt(11, Constantes.Usuario.idUsuario);
                    cs.setInt(12, Constantes.Terminal.idTerminal);
                    cs.setString(13, codTransaccion);
                    cs.setInt(14, idAlmacenDestino);
                    cs.execute();
                    factorConversion = cs.getInt(9);
                    idTransaccion = cs.getInt(10);
                    ps = conn.prepareStatement("insert into Movimiento_Almacen_Detalle" +
                            "(iId_movimiento_almacen_cabecera,iIdProducto" +
                            ",cProductName,ncantidad_movimiento_detalle,nmonto_unitario_movimiento," +
                            "nmonto_total_movimiento) values (?,?,?,?,?,?)");
                    for (int i = 0; i < productList.size(); i++) {
                        ps.setInt(1, idTransaccion);
                        ps.setInt(2, productList.get(i).getIdProduct());
                        ps.setString(3, productList.get(i).getcProductName());
                        ps.setFloat(4, productList.get(i).getdQuantity() * factorConversion);
                        ps.setBigDecimal(5, productList.get(i).getPrecioCompra().multiply(new BigDecimal(factorConversion)));
                        ps.setBigDecimal(6, productList.get(i).getPrecioCompra().multiply(new BigDecimal(productList.get(i).getdQuantity())).multiply(new BigDecimal(factorConversion)));
                        ps.addBatch();
                    }

                    ps.executeBatch();
                    if (codTransaccion.equals(Constantes.MovAlmacen.SalidaTransferencia)) {
                        cs.clearParameters();
                        cs.clearBatch();
                        cs = conn.prepareCall("call sp_generar_registro_salida_transferencia(?,?,?)");
                        cs.setInt(1, idTransaccion);
                        cs.setInt(2, Constantes.Empresa.idEmpresa);
                        cs.setInt(3, Constantes.Tienda.idTienda);
                        cs.execute();

                    }

                    respuesta = 101;

                    cs = conn.prepareCall("call pActualiza_Stock_AlamcenxVenta(?)");
                    cs.setInt(1, idTransaccion);
                    cs.execute();
                    cs.clearWarnings();
                    cs.clearParameters();


                    if (!movSalida) {
                        cs = conn.prepareCall("call pActualiza_UltimoPrecioCompra(?)");
                        cs.setInt(1, idTransaccion);
                        cs.execute();
                        cs.clearParameters();
                        cs.clearWarnings();
                    }
                    respuesta = 102;
                } catch (SQLException e) {
                    e.printStackTrace();
                    respuesta = 99;
                } finally {
                    cs.close();
                    ps.close();
                }
            } else {
                respuesta = 98;
            }
        } catch (Exception e) {
            respuesta = 98;
        }


        return respuesta;
    }

    public List<mMovAlmacen> ObtenerMovimientosAlmacen(String fechaOrigen, String fechaFinal) {

        CallableStatement ps = null;
        Connection con = conn;
        List<mMovAlmacen> movAlmacenList = new ArrayList<>();
        ResultSet rs = null;
        try {
            if (conn != null) {

                try {
                    ps = con.prepareCall("call sp_get_cabecera_movimientos_almacen(" + ParamStoreProcedure(3) + ")");

                    ps.setInt(1, Constantes.Empresa.idEmpresa);
                    ps.setString(2, fechaOrigen);
                    ps.setString(3, fechaFinal);
                    ps.execute();

                    rs = ps.getResultSet();

                    while (rs.next()) {

                        movAlmacenList.add(new mMovAlmacen(rs.getInt(1),
                                rs.getString(3), rs.getString(2),
                                rs.getString(4), rs.getString(5)
                                , rs.getString(6), rs.getString(7)));

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                movAlmacenList = null;
            }
        } catch (Exception e) {
            movAlmacenList = null;
        }

        return movAlmacenList;

    }

    public List<ProductoEnVenta> verificarStockProducto(int idCabeceraPedido) {

        CallableStatement cs = null;
        ResultSet rs = null;
        List<ProductoEnVenta> productoEnVentaList = new ArrayList<>();
        ProductoEnVenta productoEnVenta = null;
        try {
            if (conn != null) {
                try {
                    cs = conn.prepareCall("call sp_control_stock_venta_v2(?,?,?)");
                    cs.setInt(1, Constantes.Empresa.idEmpresa);
                    cs.setInt(2, Constantes.Tienda.idTienda);
                    cs.setInt(3, idCabeceraPedido);
                    cs.execute();
                    rs = cs.getResultSet();
                    while (rs.next()) {
                        productoEnVenta = new ProductoEnVenta();
                        productoEnVenta.setIdProducto(rs.getInt(2));
                        productoEnVenta.setProductName(rs.getString(3));
                        productoEnVenta.setDisponibleStock(rs.getBoolean(6));
                        productoEnVentaList.add(productoEnVenta);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    productoEnVenta = new ProductoEnVenta();
                    productoEnVenta.setIdProducto(-99);
                    productoEnVentaList = new ArrayList<>();
                    productoEnVentaList.add(productoEnVenta);
                } finally {
                    cs.close();
                    rs.close();
                }

            } else {
                productoEnVentaList = null;
                productoEnVenta = new ProductoEnVenta();
                productoEnVenta.setIdProducto(-98);
                productoEnVentaList = new ArrayList<>();
                productoEnVentaList.add(productoEnVenta);
            }

        } catch (Exception e) {
            productoEnVentaList = null;
            productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setIdProducto(-98);
            productoEnVentaList = new ArrayList<>();
            productoEnVentaList.add(productoEnVenta);
        }

        return productoEnVentaList;
    }

    public mMovAlmacen ObtenerCabeceraMovimiento(int idMov) {

        CallableStatement cs = null;
        mMovAlmacen movAlmacen = new mMovAlmacen();
        try {
            if (conn != null) {
                try {
                    cs = conn.prepareCall("call sp_obtener_cabecera_mov_almacen(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    cs.setInt(1, idMov);
                    cs.setInt(2, Constantes.Empresa.idEmpresa);
                    cs.setInt(3, Constantes.Tienda.idTienda);
                    cs.registerOutParameter(4, Types.VARCHAR);
                    cs.registerOutParameter(5, Types.VARCHAR);
                    cs.registerOutParameter(6, Types.VARCHAR);
                    cs.registerOutParameter(7, Types.VARCHAR);
                    cs.registerOutParameter(8, Types.CHAR);
                    cs.registerOutParameter(9, Types.VARCHAR);
                    cs.registerOutParameter(10, Types.INTEGER);
                    cs.registerOutParameter(11, Types.VARCHAR);
                    cs.registerOutParameter(12, Types.INTEGER);
                    cs.registerOutParameter(13, Types.VARCHAR);
                    cs.registerOutParameter(14, Types.VARCHAR);
                    cs.execute();

                    movAlmacen.setIdMovAlmacen(idMov);
                    movAlmacen.setFechaMov(cs.getString(4));
                    movAlmacen.setFechaGuia(cs.getString(5));
                    movAlmacen.setNroGuia(cs.getString(6));
                    movAlmacen.setFechaFactura(cs.getString(7));
                    movAlmacen.setcEstadoRegistro(cs.getString(8));
                    movAlmacen.setCodTransaccion(cs.getString(9));
                    movAlmacen.setIdAlmacenI(cs.getInt(10));
                    movAlmacen.setDescAlmacenI(cs.getString(11));
                    movAlmacen.setIdAlmacenD(cs.getInt(12));
                    movAlmacen.setDescAlmacenD(cs.getString(13));
                    movAlmacen.setDescripcionMov(cs.getString(14));
                    movAlmacen.setIdTiendaOrigen(obtenerAlmacen(movAlmacen.getIdAlmacenI()).getIdTienda());
                    movAlmacen.setIdTiendaD(obtenerAlmacen(movAlmacen.getIdAlmacenD()).getIdTienda());
                } catch (SQLException e) {
                    e.printStackTrace();
                    movAlmacen.setIdMovAlmacen(-99);
                }


            } else {

                movAlmacen.setIdMovAlmacen(-98);
            }
        } catch (Exception e) {
            e.toString();
            movAlmacen.setIdMovAlmacen(-98);
        }

        return movAlmacen;
    }

    public ArrayList<mProduct> productListMovAlmacen(int idMov) {

        ArrayList<mProduct> productList = new ArrayList<>();
        mProduct product = new mProduct();
        PreparedStatement ps = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        int[] values = {1, 2, 3};
        try {
            if (conn != null) {
                try {

                    cs = conn.prepareCall("call sp_obtener_detalle_movimiento_almacen_v2(?,?,?)");
                    cs.setInt(1, Constantes.Empresa.idEmpresa);
                    cs.setInt(2, Constantes.Tienda.idTienda);
                    cs.setInt(3, idMov);
                    rs = cs.executeQuery();
                    /*
                    ps = conn.prepareStatement("select iIdProducto,cProductName,ncantidad_movimiento_detalle,\n" +
                            "nmonto_unitario_movimiento,nmonto_total_movimiento\n" +
                            " from Movimiento_Almacen_Detalle where iId_movimiento_almacen_cabecera=?" +
                            " order by iId_movimiento_almacen_detalle");
                    ps.setInt(1, idMov);
                    ps.execute();
                    rs=ps.getResultSet();*/
                    while (rs.next()) {
                        product = new mProduct();
                        product.setIdProduct(rs.getInt(1));
                        product.setcProductName(rs.getString(2));
                        product.setdQuantity(rs.getFloat(3));
                        product.setPrecioCompra(rs.getBigDecimal(4));
                        product.setPrecioVenta(rs.getBigDecimal(5));
                        productList.add(product);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    product.setIdProduct(-99);
                    productList.add(product);
                }
            } else {
                product = new mProduct();
                product.setIdProduct(-98);
                productList.add(product);
            }
        } catch (Exception e) {
            product = new mProduct();
            product.setIdProduct(-98);
            productList.add(product);
        }
        return productList;
    }

    public List<mTransacciones_Almacen> ObtenerTransaccionesAlmacen() {
        List<mTransacciones_Almacen> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (conn != null) {
                try {
                    ps = conn.prepareStatement("select iId_Transacciones_Almacen,cDescripcion_Transacciones," +
                            "cCodigo_Transaccion from Transacciones_Almacen order by iId_Transacciones_Almacen");
                    ps.execute();
                    rs = ps.getResultSet();
                    while (rs.next()) {
                        lista.add(new mTransacciones_Almacen(rs.getInt(1), rs.getString(2), rs.getString(3)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    lista = new ArrayList<>();
                    lista.add(new mTransacciones_Almacen(-99, "", ""));
                }
            } else {
                lista = new ArrayList<>();
                lista.add(new mTransacciones_Almacen(-98, "", ""));
            }
        } catch (Exception e) {
            lista = new ArrayList<>();
            lista.add(new mTransacciones_Almacen(-98, "", ""));
        }

        return lista;

    }

    public byte CompletarTranferenciaAlmacen(int idMov, String cDescripcion, String fechaMovimiento, String fechaOrigen) {

        byte respuesta = 0;
        try {
            if (conn != null) {
                CallableStatement cs = null;

                try {
                    cs = conn.prepareCall("call sp_ingreso_transferencia_almacen(?,?,?,?,?,?,?,?)");
                    cs.setInt(1, idMov);
                    cs.setInt(2, Constantes.Empresa.idEmpresa);
                    cs.setInt(3, Constantes.Tienda.idTienda);
                    cs.setString(4, fechaMovimiento);
                    cs.setString(5, cDescripcion);
                    cs.registerOutParameter(6, Types.TINYINT);
                    cs.setByte(6, (byte) 0);
                    cs.setInt(7, Constantes.Usuario.idUsuario);
                    cs.setInt(8, Constantes.Terminal.idTerminal);
                    cs.execute();
                    respuesta = cs.getByte(6);
                    cs.clearParameters();
                    cs.clearWarnings();
                    if (respuesta == 100) {

                        cs = conn.prepareCall("call pActualiza_Stock_AlamcenxVenta(?)");
                        cs.setInt(1, idMov);
                        cs.execute();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    respuesta = 99;
                }
            }
        } catch (Exception e) {
            respuesta = 98;
        }
        return respuesta;
    }

    public List<mProduct> VerificarStockAlmacen(int idAlmacen, List<mProduct> productList) {
        List<mProduct> list = new ArrayList<>();
        mProduct product = null;
        String consulta = "select  iIdProduct,ncantidad from Stock_Almacen as sa where" +
                " iIdCompany=? and iId_Almacen=? and cEstado_Registro='A' and iIdProduct in(";
        String consultaIn = "";

        for (int i = 0; i < productList.size(); i++) {
            consultaIn = consultaIn + "?,";
        }
        consultaIn = consultaIn.substring(0, consultaIn.length() - 1);
        consulta = consulta + consultaIn + ")";


        if (conn != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                ps = conn.prepareStatement(consulta);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idAlmacen);
                for (int i = 0; i < productList.size(); i++) {
                    ps.setInt(i + 3, productList.get(i).getIdProduct());

                }
                ps.execute();
                rs = ps.getResultSet();

                while (rs.next()) {

                    product = new mProduct();
                    product.setIdProduct(rs.getInt(1));
                    product.setdQuantity(rs.getFloat(2));
                    list.add(product);
                    product = null;

                }


            } catch (SQLException e) {
                e.printStackTrace();
                list = new ArrayList<>();
            }


        }


        return list;
    }

    public List<mProduct>
    consultarProductosDisponibles(int idAlmacen, List<mProduct> productList) {

        List<mProduct> list = new ArrayList<>();
        mProduct product = null;
        if (conn != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {

                String consulta = "select p.iIdProduct,sa.ncantidad-isnull(pr.cantidad,0),p.bControl_Stock " +
                        "       from product as p left join " +
                        " Productos_Reserva_Pedido as pr on " +
                        "  p.iIdProduct=pr.iIdProducto " +
                        "  inner join Stock_Almacen as sa on " +
                        "  p.iIdProduct=sa.iIdProduct" +
                        "where p.iIdCompany=? and " +
                        " p.cEstado_Visible='V' and p.cEliminado='' " +
                        "  and p.cEstado_Producto='A' and iId_Almacen=? and p.iIdProduct in(";
                String consultaIn = "  ";

                for (int i = 0; i < productList.size(); i++) {
                    consultaIn = consultaIn + "?,";
                }
                consultaIn = consultaIn.substring(0, consultaIn.length() - 1);
                consulta = consulta + consultaIn + ")";

                ps = conn.prepareStatement(consulta);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idAlmacen);
                for (int i = 0; i < productList.size(); i++) {
                    ps.setInt(i + 3, productList.get(i).getIdProduct());

                }
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    product = new mProduct();
                    product.setIdProduct(rs.getInt(1));
                    product.setdQuantity(rs.getFloat(2));
                    list.add(product);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {

        }

        return list;

    }

    public List<mMovAlmacen> ObtenerTranferenciasAlmacen(int idAlmacen) {

        List<mMovAlmacen> lista = new ArrayList<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        mMovAlmacen movAlmacen = null;
        try {
            if (conn != null) {
                try {
                    cs = conn.prepareCall("call sp_obtener_transferencias_almacen(?,?)");
                    cs.setInt(1, Constantes.Empresa.idEmpresa);
                    cs.setInt(2, idAlmacen);
                    cs.execute();
                    rs = cs.getResultSet();
                    while (rs.next()) {

                        movAlmacen = new mMovAlmacen();
                        movAlmacen.setIdMovAlmacen(rs.getInt(1));
                        movAlmacen.setDescripcionMov(rs.getString(2));
                        movAlmacen.setIdMovOrigenTransf(rs.getInt(3));
                        movAlmacen.setDescAlmacenI(rs.getString(4));
                        movAlmacen.setFechaMov(rs.getString(5));
                        lista.add(movAlmacen);

                    }
                    lista.size();
                } catch (SQLException e) {
                    e.printStackTrace();
                    lista = null;
                } finally {
                    cs.close();
                    rs.close();
                }
            }
        } catch (Exception e) {
            e.toString();
        }
        return lista;
    }

    public byte VerificarTipoAlmacen(int idAlmacen) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        byte respuesta = 98;
        boolean esTienda = false;
        try {
            if (conn != null) {
                try {
                    ps = conn.prepareStatement("select lTienda from almacenes " +
                            "where iIdCompany=? and iId_Almacen=?");
                    ps.setInt(1, Constantes.Empresa.idEmpresa);
                    ps.setInt(2, idAlmacen);
                    ps.execute();

                    rs = ps.getResultSet();

                    while (rs.next()) {

                        esTienda = rs.getBoolean(1);

                    }
                    if (esTienda) {
                        respuesta = 50;
                    } else {
                        respuesta = 40;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                respuesta = 98;
            }
        } catch (Exception e) {
            respuesta = 98;
        }
        return respuesta;
    }

    public List<mTransaccionAlmacen> obtenerTipoTransacciones() {
        List<mTransaccionAlmacen> lista = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            if (conn != null) {
                try {
                    cs = conn.prepareCall("call  sp_obtener_transacciones ");
                    cs.execute();
                    rs = cs.getResultSet();
                    lista = new ArrayList<>();
                    while (rs.next()) {
                        lista.add(new mTransaccionAlmacen(1, rs.getString(1),
                                rs.getString(3), rs.getString(2), rs.getString(4)));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    lista = new ArrayList<>();
                    lista.add(new mTransaccionAlmacen(-99, "", "", "", ""));
                }

            }
        } catch (Exception e) {
            e.toString();
            lista = new ArrayList<>();
            lista.add(new mTransaccionAlmacen(-98, "", "", "", ""));
        }
        return lista;
    }

    public mProduct ObtenerCantidadDisponibleVariante(int idProducto, String d1, String d2, String d3) {

        mProduct product = new mProduct();


        try {
            if (conn != null) {
                CallableStatement cs = null;

                try {
                    cs = conn.prepareCall("call sp_obtener_cantidad_variante_v2(?,?,?,?,?,?,?,?,?,?,?)");
                    cs.setInt(1, idProducto);
                    cs.setString(2, d1);
                    cs.setString(3, d2);
                    cs.setString(4, d3);
                    cs.setInt(5, Constantes.Empresa.idEmpresa);
                    cs.setInt(6, Constantes.Tienda.idTienda);
                    cs.registerOutParameter(7, Types.DECIMAL);
                    cs.registerOutParameter(8, Types.DECIMAL);
                    cs.registerOutParameter(9, Types.DECIMAL);
                    cs.registerOutParameter(10, Types.INTEGER);
                    cs.registerOutParameter(11, Types.BOOLEAN);

                    cs.execute();

                    product.setdQuantity(cs.getFloat(7));
                    product.setStockReserva(cs.getBigDecimal(8));
                    product.setPrecioVenta(cs.getBigDecimal(9));
                    product.setIdProduct(cs.getInt(10));
                    product.setMultiplePVenta(cs.getBoolean(11));

                    if (product.isMultiplePVenta()) {
                        product.setPriceProductList(ObtenerPreciosAdiccionales(product.getIdProduct()));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    product = new mProduct();
                    product.setIdProduct(-99);
                }

            } else {
                product = new mProduct();
                product.setIdProduct(-99);
            }
        } catch (Exception e) {
            product = new mProduct();
            product.setIdProduct(-99);
        }
        return product;

    }

    // Obtener los roles del sistema para configuracion
    public List<mRol> ObtenerRoles() {

        List<mRol> list = new ArrayList<>();
        mRol mRol = new mRol();
        Connection con = getConnectionCreate();
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (con != null) {


            try {
                ps = con.prepareStatement("select idRol,cDescripcionRol,ladministrador from roles");
                ps.execute();

                rs = ps.getResultSet();

                while (rs.next()) {

                    mRol = new mRol();
                    mRol.setIdRol(rs.getInt(1));
                    mRol.setcDescripcion(rs.getString(2));
                    mRol.setbEsAdmistrador(rs.getBoolean(3));
                    list.add(mRol);
                }

            } catch (SQLException e) {

                e.printStackTrace();
                list = new ArrayList<>();
                mRol = new mRol();
                mRol.setIdRol(-99);
                list.add(mRol);
            }
        } else {

            list = new ArrayList<>();
            mRol = new mRol();
            mRol.setIdRol(-98);
            list.add(mRol);
        }
        return list;
    }

    public RetornoUsuario IngresarNuevoUsuario(mUsuario usuario) {

        byte respuesta = 0;
        CallableStatement cs = null;
        Connection con = getConnectionCreate();
        int numeroUsuario = 0;

        if (con != null) {
            try {
                cs = con.prepareCall("call sp_RegistrarUsuario(?,?,?,?,?,?,?)");
                cs.setString(1, usuario.getNombreUsuario());
                cs.setString(2, usuario.getContrasena());
                cs.setInt(3, usuario.getmRol().getIdRol());
                cs.setInt(4, Constantes.Empresa.idEmpresa);
                cs.setInt(5, usuario.getIdTienda());
                cs.registerOutParameter(6, Types.TINYINT);
                cs.registerOutParameter(7, Types.INTEGER);
                cs.setByte(6, respuesta);
                cs.execute();

                respuesta = cs.getByte(6);
                numeroUsuario = cs.getByte(7);


            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }

        return new RetornoUsuario(numeroUsuario, respuesta);
    }

    public mUsuario obtenerInformacionUsuario(int idUsuario) {

        mUsuario usuario = null;
        CallableStatement cs = null;
        Connection con = getConnectionCreate();
        ResultSet rs = null;
        if (con != null) {
            usuario = new mUsuario();
            try {
                cs = con.prepareCall("call sp_consulta_usuario(?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, idUsuario);
                cs.execute();
                rs = cs.getResultSet();
                while (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                    usuario.setNombreUsuario(rs.getString(2));
                    usuario.setContrasena(rs.getString(3));
                    usuario.getmRol().setIdRol(rs.getInt(4));
                    usuario.setIdTienda(rs.getInt(5));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                usuario = new mUsuario();
                usuario.setIdUsuario(-99);
            } finally {
                try {
                    cs.close();
                    rs.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            usuario = new mUsuario();
            usuario.setIdUsuario(-98);
        }
        return usuario;
    }

    public List<mUsuario> ObtenerUsuariosRegistrados() {

        CallableStatement cs = null;
        Connection con = getConnectionCreate();
        List<mUsuario> usuarioList = null;
        ResultSet rs = null;
        int count = 0;
        if (con != null) {
            usuarioList = new ArrayList<>();
            try {
                cs = con.prepareCall("call sp_obtener_usuarios_registrados(?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.execute();
                rs = cs.getResultSet();
                while (rs.next()) {

                    usuarioList.add(new mUsuario());
                    usuarioList.get(count).setIdUsuario(rs.getInt(1));
                    usuarioList.get(count).setNombreUsuario(rs.getString(2));
                    usuarioList.get(count).getmRol().setcDescripcion(rs.getString(3));
                    usuarioList.get(count).setcEstadoUsuario(rs.getString(4));
                    count++;

                }
            } catch (SQLException e) {
                e.printStackTrace();
                usuarioList = new ArrayList<>();
                usuarioList.add(new mUsuario());
                usuarioList.get(0).setIdUsuario(-99);
            }


        } else {
            usuarioList = new ArrayList<>();
            usuarioList.add(new mUsuario());
            usuarioList.get(0).setIdUsuario(-98);
        }

        return usuarioList;
    }

    public byte EditarUsuarioRegistrado(mUsuario usuario) {

        CallableStatement cs = null;
        Connection con = getConnectionCreate();
        byte respuesta = 0;
        if (con != null) {
            try {
                cs = con.prepareCall("call sp_editar_usuario_registrado(?,?,?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, usuario.getIdTienda());
                cs.setInt(3, usuario.getIdUsuario());
                cs.setString(4, usuario.getNombreUsuario());
                cs.setString(5, usuario.getContrasena());
                cs.setInt(6, usuario.getmRol().getIdRol());
                cs.registerOutParameter(7, Types.TINYINT);
                cs.execute();
                respuesta = cs.getByte(7);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    cs.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {

            respuesta = 98;

        }

        return respuesta;
    }

    public byte EliminarUsuario(int idUsuario) {

        CallableStatement cs = null;
        Connection con = getConnectionCreate();
        byte respuesta = 0;
        if (con != null) {

            try {
                cs = con.prepareCall("call sp_anular_usuario(?,?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, Constantes.Tienda.idTienda);
                cs.setInt(3, Constantes.Terminal.idTerminal);
                cs.setInt(4, idUsuario);
                cs.setInt(5, Constantes.Usuario.idUsuario);
                cs.registerOutParameter(6, Types.TINYINT);
                cs.execute();

                respuesta = cs.getByte(6);

            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }

        } else {
            respuesta = 98;
        }

        return respuesta;
    }

    public byte EliminarCliente(int idCliente) {

        byte respuesta = 0;
        CallableStatement cs = null;

        try {
            cs = conn.prepareCall("call sp_eliminar_cliente(?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, idCliente);
            cs.registerOutParameter(3, Types.TINYINT);
            cs.execute();
            respuesta = cs.getByte(3);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return respuesta;

    }


    public List<AdditionalPriceProduct> ObtenerPreciosAdiccionales(int idProducto) {

        List<AdditionalPriceProduct> lista = new ArrayList<>();

        AdditionalPriceProduct a;
        ResultSet rs = null;
        PreparedStatement ps = null;

        if (conn != null) {

            try {
                ps = conn.prepareStatement("select id_PVenta,dMonto,numItem from PreciosVentaAdiccional" +
                        " where id_company=? and idProducto=? order by numItem");
                ps.setInt(1, Constantes.Empresa.idEmpresa);

                ps.setInt(2, idProducto);

                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    a = new AdditionalPriceProduct();
                    a.setId(rs.getInt(1));
                    a.setAdditionalPrice(rs.getBigDecimal(2));
                    a.setNumItem(rs.getInt(3));
                    lista.add(a);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            }


        } else {
            lista = null;
        }


        return lista;

    }

    public byte EliminarVendedor(int idVendedor) {
        byte respuesta = 0;
        CallableStatement cs = null;

        try {
            cs = conn.prepareCall("call sp_eliminar_vendedor(?,?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, idVendedor);
            cs.setInt(3, Constantes.Tienda.idTienda);
            cs.registerOutParameter(4, Types.TINYINT);
            cs.execute();

            respuesta = cs.getByte(4);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return respuesta;
    }

    public List<mUnidadMedida> ObtenerUnidadesMedida() {

        CallableStatement cs = null;
        PreparedStatement ps = null;
        List<mUnidadMedida> listaUnidades = new ArrayList<>();
        mUnidadMedida unidadMedida;
        Connection con = conn;
        ResultSet rs = null;
        if (conn != null) {
            try {
                ps = conn.prepareStatement(" select iId_Unidad_Medida,Rtrim( ltrim(cDescripcion_unidad)) as q," +
                        " bEdiccion_Habilitada,bPorDefecto,cDescripcion_larga from Unidad_medida where iIdCompany=0 and cEstado_Eliminado='' order by num_orden");
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    unidadMedida = new mUnidadMedida();
                    unidadMedida.setIdUnidad(rs.getInt(1));
                    unidadMedida.setcDescripcion(rs.getString(2));
                    unidadMedida.setEdicionHabilitada(rs.getBoolean(3));
                    unidadMedida.setcDescripcionLarga(rs.getString(5));
                    listaUnidades.add(unidadMedida);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                listaUnidades = new ArrayList<>();
            }
        }
        return listaUnidades;
    }

    public byte GuardarAlmacen(mAlmacen almacen) {

        byte respuesta = 0;
        CallableStatement cs = null;

        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_guardar_nuevo_almacen(?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, almacen.getIdTienda());
                cs.setString(3, almacen.getDescripcionAlmacen());
                cs.setBoolean(4, almacen.isTienda());
                cs.registerOutParameter(5, Types.TINYINT);
                cs.execute();
                respuesta = cs.getByte(5);


            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }


        return respuesta;
    }

    public byte EditarAlmacen(mAlmacen almacen) {
        byte respuesta = 0;

        CallableStatement cs = null;
        if (conn != null) {

            try {
                cs = conn.prepareCall("call sp_editar_almacen(?,?,?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, almacen.getIdTienda());
                cs.setInt(3, almacen.getIdAlmacen());
                cs.setString(4, almacen.getDescripcionAlmacen());
                cs.setBoolean(5, almacen.isTienda());
                cs.registerOutParameter(6, Types.TINYINT);
                cs.execute();

                respuesta = cs.getByte(6);


            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }

        return respuesta;
    }

    public mAlmacen obtenerAlmacen(int idAlmacen) {

        mAlmacen almacen = new mAlmacen();
        PreparedStatement ps = null;

        if (conn != null) {
            try {

                ps = conn.prepareStatement("select cDescripcion_Almacen,lTienda,iIdTiendaUbicacion from Almacenes where iIdCompany=? and iId_Almacen=?");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idAlmacen);
                ps.execute();
                ResultSet rs = ps.getResultSet();

                while (rs.next()) {

                    almacen.setIdAlmacen(idAlmacen);
                    almacen.setDescripcionAlmacen(rs.getString(1));
                    almacen.setTienda(rs.getBoolean(2));
                    almacen.setIdTienda(rs.getInt(3));

                }
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                almacen.setIdAlmacen(-99);
            }
        } else {

            almacen.setIdAlmacen(-98);
        }
        return almacen;

    }

    public byte eliminarAlmacen(int idAlmacen, int idTienda) {

        byte respuesta = 0;

        CallableStatement cs = null;

        if (conn != null) {

            try {
                cs = conn.prepareCall("call sp_eliminar_almacen(?,?,?,?)");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, idTienda);
                cs.setInt(3, idAlmacen);
                cs.registerOutParameter(4, Types.TINYINT);
                cs.execute();
                respuesta = cs.getByte(4);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }
        } else {
            respuesta = 98;
        }
        return respuesta;
    }

    public List<mProcesoRol> obtenerProcesosRol(int idRol) {

        PreparedStatement ps = null;
        Connection con = getConnectionCreate();
        List<mProcesoRol> listaRol = new ArrayList<>();

        mProcesoRol procesoRol = null;

        ResultSet rs = null;
        if (con != null) {

            try {
                ps = con.prepareStatement("select rp.idRolProceso,p.cProceso,rp.lacceso from RolProceso as rp " +
                        " inner join Procesos as p on rp.idProcesos=p.idProcesos where " +
                        " iIdCompany=?  and idRol=?");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idRol);
                ps.execute();
                rs = ps.getResultSet();

                while (rs.next()) {

                    procesoRol = new mProcesoRol();
                    procesoRol.setIdProceso(rs.getInt(1));
                    procesoRol.setNombreProceso(rs.getString(2));
                    procesoRol.setbEstadoAcceso(rs.getBoolean(3));
                    listaRol.add(procesoRol);
                    procesoRol = null;

                }
            } catch (SQLException e) {
                e.printStackTrace();
                listaRol = null;
            }

        } else {
            listaRol = null;
        }
        return listaRol;
    }

    public byte GuardarCambiosRolProceso(int idRol, List<mProcesoRol> lista) {

        PreparedStatement ps = null;
        Connection con = getConnectionCreate();
        byte resultado = 0;
        if (con != null) {
            try {
                ps = con.prepareStatement(" update RolProceso set lacceso=? where iIdCompany=?  " +
                        " and idRol=? and idRolProceso=? ");

                for (mProcesoRol procesoRol : lista) {

                    ps.setBoolean(1, procesoRol.isbEstadoAcceso());
                    ps.setInt(2, Constantes.Empresa.idEmpresa);
                    ps.setInt(3, idRol);
                    ps.setInt(4, procesoRol.getIdProceso());
                    ps.addBatch();

                }
                ps.executeBatch();

                resultado = 100;
            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
            }


        } else {
            resultado = 98;
        }


        return resultado;
    }

    public List<mVendedorProducto> obtenerReporteVendedorVentaAcumulado(int idVendedor, String desde, String hasta, int idTienda) {
        String fechaInit = desde + " 00:00:00";
        String fechaEnd = hasta + " 23:59:59";
        List<mVendedorProducto> lista = null;
        mVendedorProducto vendedorProducto = new mVendedorProducto();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String select = "select convert(varchar(11),cast( cv.FechaVentaRealizada-'5:00' as date),103) as fecha," +
                " iIdProducto,dv.cProductName,dv.cDescripcion_Variante,cDescripcion_Modificador " +
                ",sum(iCantidad) as cantidad,sum(dv.dPrecio_Subtotal) as total ,cv.iId_vendedor, " +
                " case " +
                " when cv.iId_vendedor=0 " +
                " then 'Sin Vendedor' " +
                " else mv.cPrimerNombre" +
                " end AS cPrimerNombre, " +
                " mv.cSegundoNombre,mv.cApellidoPaterno,mv.cApellidoMaterno,cv.iId_Tienda,p.ckey " +
                " from  cabecera_venta as cv left  join MaestroVendedor as mv on mv.iIdVendedor=cv.iId_vendedor" +
                " inner join Detalle_Venta as dv " +
                " on dv.iIdCabecera_Venta=cv.iId_Cabecera_Venta " +
                " left join product as p on dv.iIdProducto=p.iIdProduct " +
                " where cv.iId_Company=? and cv.iId_Tienda=? AND dv.id_Company=? and dv.cEliminado='A' and cv.cEstadoVenta='N' " +
                " and cv.cEliminado='' and dv.cEstadoDetalleVenta='A' and  dv.bDetallePack=0 " +
                " and cv.FechaVentaRealizada-'5:00'  between ?  and ? ";
        String queryFinal = "";
        String fvendedor = " and cv.iId_Vendedor=? ";
        String groupby = " group by cv.iId_Tienda,cast(cv.FechaVentaRealizada-'5:00' as date)," +
                "dv.iIdProducto,cv.cNombre_Vendedor,cv.iId_Vendedor,mv.cPrimerNombre," +
                "mv.cSegundoNombre,mv.cApellidoPaterno,mv.cApellidoMaterno,p.ckey,dv.cProductName,dv.cDescripcion_Variante,cDescripcion_Modificador";
        String orden = " order by fecha desc,cv.iId_Vendedor desc,iIdProducto,p.ckey ";
        if (idVendedor == 0) {
            queryFinal = select + groupby + orden;
        } else {
            queryFinal = select + fvendedor + groupby + orden;
        }
        if (conn != null) {
            lista = new ArrayList<>();
            try {
                ps = conn.prepareStatement(queryFinal);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idTienda);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setString(4, fechaInit);
                ps.setString(5, fechaEnd);
                if (idVendedor != 0) {
                    ps.setInt(6, idVendedor);
                }
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    vendedorProducto = new mVendedorProducto();
                    vendedorProducto.setFechaProceso(rs.getString(1));
                    vendedorProducto.getProduct().setIdProduct(rs.getInt(2));
                    vendedorProducto.getProduct().setcProductName(rs.getString(3));
                    vendedorProducto.getProduct().setDescripcionVariante(rs.getString(4));
                    vendedorProducto.getProduct().setModificadores(rs.getString(5));
                    vendedorProducto.getProduct().setdQuantity(rs.getFloat(6));
                    vendedorProducto.getProduct().setPrecioVenta(rs.getBigDecimal(7));
                    vendedorProducto.getVendedor().setIdVendedor(rs.getInt(8));
                    if (vendedorProducto.getVendedor().getIdVendedor() == 0) {
                        vendedorProducto.getVendedor().setPrimerNombre("Sin vendedor asignado");
                    } else {
                        vendedorProducto.getVendedor().setPrimerNombre(rs.getString(9) + " " + rs.getString(10) + " " + rs.getString(11) + " " + rs.getString(12));
                    }
                    vendedorProducto.setIdTienda(rs.getInt(13));
                    vendedorProducto.getProduct().setcKey(rs.getString(14));
                    lista.add(vendedorProducto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            } finally {
                try {
                    ps.close();
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            lista = null;
        }

        return lista;
    }

    public List<mVendedorProducto> obtenerReporteVendedorVentaTodasTiendasAcumulado(String desde, String hasta) {
        String fechaInit = desde + " 00:00:00";
        String fechaEnd = hasta + " 23:59:59";
        List<mVendedorProducto> lista = null;
        mVendedorProducto vendedorProducto = new mVendedorProducto();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String select = "select convert(varchar(11),cast( cv.FechaVentaRealizada-'5:00' as date),103) as fecha," +
                " dv.iIdProducto,dv.cProductName,dv.cDescripcion_Variante,cDescripcion_Modificador " +
                ",sum(iCantidad) as cantidad,sum(dv.dPrecio_Subtotal) as total ,cv.iId_vendedor, " +
                " case " +
                " when cv.iId_vendedor=0 " +
                " then 'Sin Vendedor' " +
                " else mv.cPrimerNombre" +
                " end AS cPrimerNombre, " +
                " mv.cSegundoNombre,mv.cApellidoPaterno,mv.cApellidoMaterno,cv.iId_Tienda,p.ckey, " +
                "isnull(c.cDescripcion_categoria,'GENERAL')," +
                "isnull(sc.c_Descripcion_SubCategoria,'') " +
                " from  cabecera_venta as cv left  join MaestroVendedor as mv on mv.iIdVendedor=cv.iId_vendedor" +
                " inner join Detalle_Venta as dv " +
                " on dv.iIdCabecera_Venta=cv.iId_Cabecera_Venta " +
                " left join product as p on dv.iIdProducto=p.iIdProduct " +
                " left join Categoria_Productos as c on " +
                " p.id_Categoria=c.id_categoria_producto " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria " +
                " where cv.iId_Company=?  AND dv.id_Company=? and dv.cEliminado='A' and cv.cEstadoVenta='N' " +
                " and cv.cEliminado='' and dv.cEstadoDetalleVenta='A' and  dv.bDetallePack=0 " +
                " and cv.FechaVentaRealizada-'5:00'  between ?  and ?  and p.iIdCompany=? ";
        String queryFinal = "";
        String groupby = " group by cv.iId_Tienda,cast(cv.FechaVentaRealizada-'5:00' as date),iIdProducto,cv.cNombre_Vendedor,cv.iId_Vendedor,mv.cPrimerNombre," +
                "mv.cSegundoNombre,mv.cApellidoPaterno,mv.cApellidoMaterno" +
                ",p.ckey,isnull(c.cDescripcion_categoria,'GENERAL')," +
                "isnull(sc.c_Descripcion_SubCategoria,'')," +
                "dv.cProductName,dv.cDescripcion_Variante,cDescripcion_Modificador";
        String orden = " order by cv.iId_Tienda,fecha desc,cv.iId_Vendedor desc," +
                "isnull(c.cDescripcion_categoria,'GENERAL')," +
                "isnull(sc.c_Descripcion_SubCategoria,'') ";
        ;
        queryFinal = select + groupby + orden;

        if (conn != null) {
            lista = new ArrayList<>();
            try {
                ps = conn.prepareStatement(queryFinal);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setString(3, fechaInit);
                ps.setString(4, fechaEnd);
                ps.setInt(5, Constantes.Empresa.idEmpresa);

                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    vendedorProducto = new mVendedorProducto();
                    vendedorProducto.setFechaProceso(rs.getString(1));
                    vendedorProducto.getProduct().setIdProduct(rs.getInt(2));
                    vendedorProducto.getProduct().setcProductName(rs.getString(15) + " "
                            + rs.getString(16)
                            + " " + rs.getString(3) + Constantes.EHTML.Left);
                    vendedorProducto.getProduct().setDescripcionVariante(rs.getString(4));
                    vendedorProducto.getProduct().setModificadores(rs.getString(5));
                    vendedorProducto.getProduct().setdQuantity(rs.getFloat(6));
                    vendedorProducto.getProduct().setPrecioVenta(rs.getBigDecimal(7));
                    vendedorProducto.getVendedor().setIdVendedor(rs.getInt(8));
                    if (vendedorProducto.getVendedor().getIdVendedor() == 0) {
                        vendedorProducto.getVendedor().setPrimerNombre("Sin vendedor asignado");
                    } else {
                        vendedorProducto.getVendedor().setPrimerNombre(rs.getString(9) + " " + rs.getString(10) + " " + rs.getString(11) + " " + rs.getString(12));
                    }
                    vendedorProducto.setIdTienda(rs.getInt(13));
                    vendedorProducto.getProduct().setcKey(rs.getString(14));
                    lista.add(vendedorProducto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            } finally {
                try {
                    ps.close();
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            lista = null;
        }

        return lista;
    }

    public List<mVendedorProducto> ReporteDetalleTodasTiendas(String desde, String hasta) {
        String fechaInit = desde + " 00:00:00";
        String fechaEnd = hasta + " 23:59:59";
        List<mVendedorProducto> lista = null;
        ResultSet rs = null;

        String query = "select cv.iId_Vendedor,convert(varchar(11),cv.FechaVentaRealizada-'5:00',103),case " +
                " when cv.iId_vendedor=0 " +
                " then 'Sin Vendedor' " +
                " else mv.cPrimerNombre " +
                " end AS cPrimerNombre,dv.cProductName " +
                "        ,dv.cDescripcion_Variante,dv.cDescripcion_Modificador" +
                " ,dv.iCantidad,dv.dPrecio_Subtotal,cv.iId_Cabecera_Pedido " +
                "         ,isnull(mv.cPrimerNombre,''),isnull(mv.cSegundoNombre,'')," +
                " isnull(mv.cApellidoPaterno,''),isnull(mv.cApellidoMaterno,'')" +
                ",dv.iIdProducto,cv.iId_Tienda,p.ckey ," +
                "isnull(c.cDescripcion_categoria,'GENERAL')," +
                "isnull(sc.c_Descripcion_SubCategoria,'') " +
                "        from Cabecera_Venta as cv inner join Detalle_Venta as dv " +
                "on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                "         left join MaestroVendedor as mv on cv.iId_vendedor=mv.iIdVendedor " +
                "  left join product as p on dv.iIdProducto=p.iIdProduct " +
                " left join Categoria_Productos as c on " +
                " p.id_Categoria=c.id_categoria_producto " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria " +
                "        where cv.iId_Company=?  " +
                " AND dv.id_Company=? and dv.cEliminado='A' and cv.cEstadoVenta='N' " +
                "        and cv.cEliminado='' and dv.cEstadoDetalleVenta='A' and" +
                " dv. bDetallePack=0  and cv.FechaVentaRealizada-'5:00' between ?  and ? ";
        String order = "  order by cv.iId_Tienda," +
                "convert(date,(cv.FechaVentaRealizada)-'5:00')" +
                " desc,cv.iId_Vendedor desc, " +
                "isnull(c.cDescripcion_categoria,'GENERAL')," +
                "isnull(sc.c_Descripcion_SubCategoria,'') ";
        String queryFinal = "";
        queryFinal = query + order;
        mVendedorProducto vendedorProducto = new mVendedorProducto();
        PreparedStatement ps = null;
        lista = new ArrayList<>();
        try {
            ps = conn.prepareStatement(queryFinal);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setString(3, fechaInit);
            ps.setString(4, fechaEnd);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                vendedorProducto = new mVendedorProducto();
                vendedorProducto.getVendedor().setIdVendedor(rs.getInt(1));
                vendedorProducto.setFechaProceso(rs.getString(2));
                if (vendedorProducto.getVendedor().getIdVendedor() == 0) {
                    vendedorProducto.getVendedor().setPrimerNombre("Sin vendedor asignado");

                } else {
                    vendedorProducto.getVendedor().setPrimerNombre(rs.getString(10) + " " + rs.getString(11) + " " + rs.getString(12) + " " + rs.getString(13));

                }
                vendedorProducto.getProduct().setcProductName(
                        rs.getString(17) + " " + rs.getString(18)
                                + " " + rs.getString(4) + Constantes.EHTML.Left);
                vendedorProducto.getProduct().setDescripcionVariante(rs.getString(5));
                vendedorProducto.getProduct().setModificadores(rs.getString(6));
                vendedorProducto.getProduct().setdQuantity(rs.getFloat(7));
                vendedorProducto.getProduct().setPrecioVenta(rs.getBigDecimal(8));
                vendedorProducto.setIdCabeceraVenta(rs.getInt(9));
                vendedorProducto.getProduct().setIdProduct(rs.getInt(14));
                vendedorProducto.setIdTienda(rs.getInt(15));
                vendedorProducto.getProduct().setcKey(rs.getString(16));

                lista.add(vendedorProducto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lista = null;
        }
        return lista;

    }

    public List<mVendedorProducto> obtenerReporteVendedorVenta(int idVendedor, String desde, String hasta, int idTienda) {
        String fechaInit = desde + " 00:00:00";
        String fechaEnd = hasta + " 23:59:59";
        String query = "select cv.iId_Vendedor,convert(varchar(11),cv.FechaVentaRealizada-'5:00',103),case " +
                " when cv.iId_vendedor=0 " +
                " then 'Sin Vendedor' " +
                " else mv.cPrimerNombre " +
                " end AS cPrimerNombre,dv.cProductName " +
                "        ,dv.cDescripcion_Variante,dv.cDescripcion_Modificador " +
                "        ,dv.iCantidad,dv.dPrecio_Subtotal,cv.iId_Cabecera_Pedido " +
                "         ,mv.cPrimerNombre,mv.cSegundoNombre," +
                "  mv.cApellidoPaterno,mv.cApellidoMaterno,dv.iIdProducto" +
                " ,cv.iId_Tienda,p.ckey  " +
                "        from Cabecera_Venta as cv inner join Detalle_Venta as dv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                "         left join MaestroVendedor as mv on cv.iId_vendedor=mv.iIdVendedor" +
                "   left join product as p on dv.iIdProducto=p.iIdProduct  " +
                "        where cv.iId_Company=? and cv.iId_Tienda=? AND " +
                "dv.id_Company=? and dv.cEliminado='A' and cv.cEstadoVenta='N' " +
                "        and cv.cEliminado='' and dv.cEstadoDetalleVenta='A'" +
                " and dv. bDetallePack=0  and cv.FechaVentaRealizada-'5:00' between ?  and ? ";
        //    "    and mv.iIdCompany=? and mv.iIdTienda=? ";
        String todos = "  ";
        String porVendedor = "  and cv.iId_Vendedor=? ";
        String order = "  order by convert(date,(cv.FechaVentaRealizada)-'5:00') desc,cv.iId_Vendedor desc ";
        List<mVendedorProducto> lista = null;
        mVendedorProducto vendedorProducto = new mVendedorProducto();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String queryFinal = "";
        if (idVendedor == 0) {
            queryFinal = query + todos + order;
        } else if (idVendedor > 0) {
            queryFinal = query + porVendedor + order;
        }
        if (conn != null) {
            try {
                ps = conn.prepareStatement(queryFinal);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idTienda);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setString(4, fechaInit);
                ps.setString(5, fechaEnd);
                if (idVendedor != 0) {
                    ps.setInt(6, idVendedor);
                }
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();

                while (rs.next()) {
                    vendedorProducto = new mVendedorProducto();
                    vendedorProducto.getVendedor().setIdVendedor(rs.getInt(1));
                    vendedorProducto.setFechaProceso(rs.getString(2));
                    if (vendedorProducto.getVendedor().getIdVendedor() == 0) {
                        vendedorProducto.getVendedor().setPrimerNombre("Sin vendedor asignado");

                    } else {
                        vendedorProducto.getVendedor().setPrimerNombre(rs.getString(10) + " " + rs.getString(11) + " " + rs.getString(12) + " " + rs.getString(13));

                    }
                    vendedorProducto.getProduct().setcProductName(rs.getString(4));
                    vendedorProducto.getProduct().setDescripcionVariante(rs.getString(5));
                    vendedorProducto.getProduct().setModificadores(rs.getString(6));
                    vendedorProducto.getProduct().setdQuantity(rs.getFloat(7));
                    vendedorProducto.getProduct().setPrecioVenta(rs.getBigDecimal(8));
                    vendedorProducto.setIdCabeceraVenta(rs.getInt(9));
                    vendedorProducto.getProduct().setIdProduct(rs.getInt(14));
                    vendedorProducto.setIdTienda(rs.getInt(15));
                    vendedorProducto.getProduct().setcKey(rs.getString(16));
                    lista.add(vendedorProducto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            } finally {
                try {
                    ps.close();
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            lista = null;
        }
        return lista;
    }

    public List<mVentasVendedor> obtenerVentasPorVendedor(int idVendedor, String desde, String hasta) {
        desde = desde + " 00:00:00";
        hasta = hasta + " 23:59:59";
        List<mVentasVendedor> list = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String select = " select cv.iId_Vendedor,case cv.iId_Vendedor " +
                "  when 0 then 'Sin vendedor asignado' " +
                "  else Ltrim(mv.cPrimerNombre) " +
                "  end as NombreVendedor,isnull(mv.cSegundoNombre,''),isnull(mv.cApellidoPaterno,''),isnull(mv.cApellidoMaterno,'')," +
                " count(iId_Cabecera_Venta) as numeroVentas,sum(dTotal_Neto_Venta) AS TotalVentasVendedor" +
                " from cabecera_venta as cv LEFT join MaestroVendedor as mv on cv.iId_Vendedor=mv.iIdVendedor and mv.iIdCompany=? and mv.iIdTienda=? " +
                " where iId_Company=? and iId_Tienda=? and FechaVentaRealizada-'05:00' " +
                " between ? and ? and cv.cEstadoVenta='N' and cv.cEliminado='' ";
        String order = "  order by cv.iId_Vendedor desc,numeroVentas desc ";
        String filterVendedor = " and cv.iId_Vendedor=? ";
        String query = "";
        String groupby = " group by cv.iId_Vendedor, mv.cPrimerNombre,mv.cSegundoNombre,mv.cApellidoPaterno," +
                " mv.cApellidoMaterno,mv.cEstado_Eliminado ";
        if (idVendedor > 0) {
            select = select + " " + filterVendedor;
        }
        query = select + groupby + order;
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, Constantes.Tienda.idTienda);
                ps.setString(5, desde);
                ps.setString(6, hasta);

                if (idVendedor != 0) {
                    ps.setInt(7, idVendedor);
                }

                ps.execute();
                rs = ps.getResultSet();
                list = new ArrayList<>();
                while (rs.next()) {
                    mVentasVendedor v = new mVentasVendedor();
                    v.getVendedor().setIdVendedor(rs.getInt(1));
                    v.getVendedor().setPrimerNombre(rs.getString(2) + " " + rs.getString(3));
                    v.getVendedor().setApellidoPaterno(rs.getString(4));
                    v.getVendedor().setApellidoMaterno(rs.getString(5));
                    v.setNumeroVentas(rs.getInt(6));
                    v.setMontoVentas(rs.getBigDecimal(7));
                    list.add(v);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                list = null;
            }
        } else {
            list = null;
        }
        return list;
    }

    private byte AgregarPreciosVentaAdiccionales(int idProducto, List<AdditionalPriceProduct> lista) {
        byte respuesta = 100;
        boolean permitir = true;

        if (idProducto <= 0) {
            permitir = false;
        } else {
            permitir = true;
        }
        if (lista != null) {
            if (lista.size() == 0) {
                permitir = true;
            }
        } else {
            permitir = false;
        }

        for (AdditionalPriceProduct a : lista) {
            if (a.getId() == 0) {
                permitir = true;
            }
        }

        if (permitir) {
            int count = 0;

            if (lista.size() > 0) {
                int mayor = 0;
                mayor = lista.get(lista.size() - 1).getNumItem();
                for (int i = 0; i < lista.size() - 1; i++) {
                    if (lista.get(i).getNumItem() > mayor) {
                        mayor = lista.get(i).getNumItem();
                    }

                }

                count = mayor;
            }
            final String SqlInsertPrecio = "insert into PreciosVentaAdiccional" +
                    "(dMonto,idProducto,id_Company,id_Tienda,numItem)" +
                    " values(?,?,?,?,?)";
            PreparedStatement ps = null;
            if (conn != null) {
                try {
                    ps = conn.prepareStatement(SqlInsertPrecio);
                    for (AdditionalPriceProduct a : lista) {
                        if (a.getId() == 0) {
                            count++;
                            ps.setBigDecimal(1, a.getAdditionalPrice());
                            ps.setInt(2, idProducto);
                            ps.setInt(3, Constantes.Empresa.idEmpresa);
                            ps.setInt(4, Constantes.Tienda.idTienda);
                            ps.setInt(5, count);
                            ps.addBatch();
                        }
                    }
                    ps.executeBatch();
                    respuesta = 100;
                    ps.clearParameters();
                    ps.clearBatch();
                    if (respuesta == 100) {
                        ps = conn.prepareStatement("update product set dSalesPrice=? where iIdCompany=?  and iIdProduct=?");
                        ps.setBigDecimal(1, lista.get(0).getAdditionalPrice());
                        ps.setInt(2, Constantes.Empresa.idEmpresa);
                        ps.setInt(3, idProducto);
                        ps.execute();


                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    respuesta = 88;
                }
            } else {
                respuesta = 88;
            }
        } else {
            respuesta = 77;
        }

        return respuesta;
    }

    public List<mTipoDocumento> obtenerTiposDocumentoIndentificacion() {

        PreparedStatement ps = null;
        List<mTipoDocumento> lista = null;
        mTipoDocumento doc = null;
        ResultSet rs = null;
        if (conn != null) {

            try {
                ps = conn.prepareStatement("select iIdTipoDocumento," +
                        "cDescripcionCorta,id_doc_sunat," +
                        "iLongitud,rtrim(cDenominacionNumero)," +
                        "rtrim(cDenominacionCliente),b_Verificar_Direccion,bVerificarRuc " +
                        "from TipodeDocumento where b_visible_interfaz=1");
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();

                while (rs.next()) {

                    doc = new mTipoDocumento();
                    doc.setIdTipoDocumento(rs.getInt(1));
                    doc.setCDescripcionCorta(rs.getString(2));
                    doc.setIdDocSunat(rs.getInt(3));
                    doc.setLongitudNumeroDoc(rs.getInt(4));
                    doc.setDenominacionNumero(rs.getString(5));
                    doc.setDenominacionCliente(rs.getString(6));
                    doc.setBVerificarDireccion(rs.getBoolean(7));
                    doc.setVerificaRuc(rs.getBoolean(8));
                    lista.add(doc);

                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            }

        } else {
            lista = null;
        }
        return lista;
    }

    public List<mVendedorProducto> obtenerDetalleVentaCierre(int idCierre, int idVendedor) {
        List<mVendedorProducto> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        final String select = "  select cv.iId_Vendedor,cv.iId_Cabecera_Venta," +
                "isnull(mv.cPrimerNombre,'Sin vendedor asignado'),isnull(mv.cSegundoNombre,'') " +
                ",isnull(mv.cApellidoPaterno,''),isnull(mv.cApellidoMaterno,''),dv.iIdProducto, " +
                " dv.cProductName,dv.cDescripcion_variante,cDescripcion_Modificador, " +
                " (sum(dv.iCantidad)) as cantidad,sum(dv.dPrecio_Subtotal)" +
                " ,convert(varchar(11),cast( cv.FechaVentaRealizada-'5:00' as date),103) " +
                ",isnull(c.cDescripcion_categoria,'General')" +
                ",isnull(sc.c_Descripcion_SubCategoria,'') " +
                " from Cabecera_venta as cv left join MaestroVendedor as mv " +
                "on cv.iId_Vendedor=mv.iIdVendedor and mv.iIdCompany=? and iIdTienda=? " +
                " inner join Detalle_Venta as dv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                " inner join Product as p on p.iIdProduct=dv.iIdProducto " +
                " left join Categoria_Productos as c on " +
                " p.id_Categoria=c.id_categoria_producto " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria " +
                " where cv.iId_Company=? and cv.iId_Tienda=? " +
                "and cv.id_cierre=? and dv.bDetallePack=0 and " +
                " dv.cEliminado='A' and  cv.cEstadoVenta='N'" +
                " and cv.cEliminado='' and dv.cEstadoDetalleVenta='A'";
        final String whereVendedor = " and cv.iId_Vendedor=? ";
        final String group = "  group by cv.iId_Vendedor,cv.iId_Cabecera_Venta,dv.iIdProducto,mv.cPrimerNombre,mv.cSegundoNombre, " +
                "  mv.cApellidoPaterno,mv.cApellidoMaterno,isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,'')" +
                ",dv.cProductName,dv.cDescripcion_variante,cDescripcion_Modificador" +
                ",cast(cv.FechaVentaRealizada-'5:00' as date) order by  cast( cv.FechaVentaRealizada-'5:00' as date) desc,cv.iId_Vendedor, " +
                " isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''), dv.cProductName";
        String query = "";
        if (idVendedor != 0) {
            query = select + whereVendedor + group;
        } else {
            query = select + group;
        }
        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, Constantes.Tienda.idTienda);
                ps.setInt(5, idCierre);
                if (idVendedor != 0) {
                    ps.setInt(6, idVendedor);
                }
                ps.execute();
                mVendedorProducto v = null;
                rs = ps.getResultSet();
                lista = new ArrayList<>();
                while (rs.next()) {
                    v = new mVendedorProducto();
                    v.getVendedor().setIdVendedor(rs.getInt(1));
                    v.setIdCabeceraVenta(rs.getInt(2));
                    v.getVendedor().setPrimerNombre(rs.getString(3) + " " + rs.getString(4));
                    v.getVendedor().setApellidoPaterno(rs.getString(5));
                    v.getVendedor().setApellidoMaterno(rs.getString(6));
                    v.getProduct().setIdProduct(rs.getInt(7));
                    v.getProduct().setcProductName(rs.getString(14) + " " +
                            rs.getString(15) + " " + rs.getString(8) + Constantes.EHTML.Left);
                    v.getProduct().setDescripcionVariante(rs.getString(9));
                    v.getProduct().setModificadores(rs.getString(10));
                    v.getProduct().setdQuantity(rs.getFloat(11));
                    v.getProduct().setPrecioVenta(rs.getBigDecimal(12));
                    v.setFechaProceso(rs.getString(13));
                    lista.add(v);
                    v = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            }
        } else {
            lista = null;
        }
        return lista;

    }

    public List<mVendedorProducto> obtenerAcumuladoVentasCierre(int idCierre, int idVendedor) {

        List<mVendedorProducto> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        final String select = " select cv.iId_Vendedor,isnull(mv.cPrimerNombre,'Sin vendedor asignado'),isnull(mv.cSegundoNombre,'') " +
                "   ,isnull(mv.cApellidoPaterno,''),isnull(mv.cApellidoMaterno,''),dv.iIdProducto," +
                "   dv.cProductName,dv.cDescripcion_variante,cDescripcion_Modificador," +
                "(sum(dv.iCantidad)) as cantidad,sum(dv.dPrecio_Subtotal)," +
                "convert(varchar(11),cast( cv.FechaVentaRealizada-'5:00' as date),103) " +
                ",isnull(c.cDescripcion_categoria,'General')" +
                ",isnull(sc.c_Descripcion_SubCategoria,'') " +
                "   from Cabecera_venta as cv left join MaestroVendedor as mv on " +
                "cv.iId_Vendedor=mv.iIdVendedor and mv.iIdCompany=? and iIdTienda=? " +
                "   inner join Detalle_Venta as dv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                " inner join Product as p on p.iIdProduct=dv.iIdProducto " +
                " left join   Categoria_Productos as c on " +
                " p.id_Categoria=c.id_categoria_producto " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria " +
                "   where cv.iId_Company=? and cv.iId_Tienda=? and cv.id_cierre=? and dv.bDetallePack=0 and " +
                "   dv.cEliminado='A' and  cv.cEstadoVenta='N' and cv.cEliminado='' and dv.cEstadoDetalleVenta='A' ";
        final String where = " and cv.iId_Vendedor=? ";
        final String order = "   group by cv.iId_Vendedor,dv.iIdProducto,mv.cPrimerNombre,mv.cSegundoNombre" +
                ",mv.cApellidoPaterno,mv.cApellidoMaterno," +
                "isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,'')," +
                "dv.cProductName,dv.cDescripcion_variante," +
                "cDescripcion_Modificador,cast( cv.FechaVentaRealizada-'5:00' as date)" +
                " order by cast( cv.FechaVentaRealizada-'5:00' as date) desc, cv.iId_Vendedor ," +
                " isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''), dv.cProductName";
        String query = "";

        if (idVendedor != 0) {
            query = select + where + order;
        } else {
            query = select + order;
        }
        if (conn != null) {

            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, Constantes.Tienda.idTienda);
                ps.setInt(5, idCierre);
                if (idVendedor != 0) {
                    ps.setInt(6, idVendedor);
                }
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();
                mVendedorProducto v;
                while (rs.next()) {
                    v = new mVendedorProducto();
                    v.getVendedor().setIdVendedor(rs.getInt(1));
                    v.getVendedor().setPrimerNombre(rs.getString(2) + " " + rs.getString(3));
                    v.getVendedor().setApellidoPaterno(rs.getString(4));
                    v.getVendedor().setApellidoMaterno(rs.getString(5));
                    v.getProduct().setIdProduct(rs.getInt(6));
                    v.getProduct().setcProductName(rs.getString(13) + " " +
                            rs.getString(14) + " " + rs.getString(7) + Constantes.EHTML.Left);
                    v.getProduct().setDescripcionVariante(rs.getString(8));
                    v.getProduct().setModificadores(rs.getString(9));
                    v.getProduct().setdQuantity(rs.getFloat(10));
                    v.getProduct().setPrecioVenta(rs.getBigDecimal(11));
                    v.setFechaProceso(rs.getString(12));
                    lista.add(v);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            } finally {

                try {
                    rs.close();
                    ps.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            lista = null;
        }

        return lista;

    }

    public List<mAlmacenProducto> ObtenerProductosAlmacen(int idAlmacen) {

        List<mAlmacenProducto> lista = null;
        mAlmacenProducto m = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        final String select = "SELECT Product.iIdProduct," +
                "Product.cProductName,Product.cDescripcion_Variante, " +
                "Stock_Almacen.nUltimoPrecioCompra, Product.dSalesPrice, Stock_Almacen.ncantidad, " +
                " Almacenes.iId_Almacen, Almacenes.cDescripcion_Almacen, Almacenes.lTienda," +
                "isnull(c.cDescripcion_categoria,'GENERAL')," +
                "isnull(sc.c_Descripcion_SubCategoria,'') " +
                " FROM Almacenes INNER JOIN " +
                " Stock_Almacen ON Almacenes.iId_Almacen = Stock_Almacen.iId_Almacen INNER JOIN " +
                " Product ON Stock_Almacen.iIdProduct = Product.iIdProduct " +
                " left join Categoria_Productos as c on " +
                " Product.id_Categoria=c.id_categoria_producto " +
                " left join SubCategorias as sc on Product.id_Subcategoria=sc.id_subcategoria " +
                " WHERE (Product.cEliminado = ' ') AND " +
                " (Stock_Almacen.cEstado_Registro = 'A') and Product.bControl_Stock=1 AND " +
                " (Stock_Almacen.iIdCompany = ?) AND (Almacenes.iIdCompany = ?) ";
        final String where = " and Almacenes.iId_Almacen=? ";
        final String order = " ORDER BY Almacenes.lTienda DESC," +
                " Almacenes.iId_Almacen,isnull(c.cDescripcion_categoria,'GENERAL')" +
                " ,isnull(sc.c_Descripcion_SubCategoria,'')  ";

        String query = "";

        if (idAlmacen != 0) {
            query = select + where + order;
        } else {
            query = select + order;
        }

        if (conn != null) {
            try {
                ps = conn.prepareStatement(query);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                if (idAlmacen != 0) {
                    ps.setInt(3, idAlmacen);
                }
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();
                while (rs.next()) {
                    m = new mAlmacenProducto();
                    m.setIdProducto(rs.getInt(1));
                    m.setNombreProducto(rs.getString(10) + " " + rs.getString(11) + " " +
                            rs.getString(2));
                    m.setDescripcionVariante(rs.getString(3));
                    m.setPrecioCompra(rs.getBigDecimal(4));
                    m.setPrecioVenta(rs.getBigDecimal(5));
                    m.setCantidadDisponible(rs.getFloat(6));
                    m.setIdAlmacen(rs.getInt(7));
                    m.setDescripcionAlmacen(rs.getString(8));
                    m.setEsTienda(rs.getBoolean(9));
                    lista.add(m);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            }

        } else {
            lista = null;
        }
        return lista;

    }

    public byte AnularMovimientoAlmacen(int idMovAlmacen) {

        byte respuesta = 98;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "";
        if (conn != null) {
            try {
                ps = conn.prepareStatement("update Movimiento_Almacen_Cabecera set cEstado_Registro='E',id_usuario_anular=?,id_terminal_anular=? " +
                        " where iIdCompany=? and iIdTienda=? and cEstado_Registro='A' and iId_movimiento_almacen_cabecera=? ");
                ps.setInt(1, Constantes.Usuario.idUsuario);
                ps.setInt(2, Constantes.Terminal.idTerminal);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, Constantes.Tienda.idTienda);
                ps.setInt(5, idMovAlmacen);
                ps.execute();
                ps.clearParameters();
                ps = conn.prepareStatement("select cEstado_Registro from Movimiento_Almacen_Cabecera where iIdCompany=? and iIdTienda=? and iId_movimiento_almacen_cabecera=?");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.setInt(3, idMovAlmacen);
                ps.execute();
                rs = ps.getResultSet();
                while (rs.next()) {
                    resultado = rs.getString(1);
                }
                if (resultado.equals("E")) {
                    respuesta = 100;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 98;
            }
        } else {
            respuesta = 98;
        }

        return respuesta;


    }

    public List<mCabeceraPedido> ObtenerPedidosTimer(int idUltimaCabecera) {
        mCabeceraPedido c = null;
        List<mCabeceraPedido> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String select = "SELECT Cabecera_Pedido.iId_Cabecera_Pedido,Cabecera_Pedido.cObservacion,Cabecera_Pedido.iIdCliente," +
                " ISNULL(maestroCliente.iTipoCliente, 0) AS TipoCliente, ISNULL(maestroCliente.cRazonSocial, '') AS RazonSocialC, " +
                " ISNULL(maestroCliente.cPrimerNombre, 'Desconocido') AS PrimerNombreC, ISNULL(maestroCliente.cSegundoNombre, '') AS SegundoNombreC" +
                " ,ISNULL(maestroCliente.cApellidoPaterno, '') AS ApellidoPaternoC,  " +
                " ISNULL(maestroCliente.cApellidoMaterno, '') AS ApellidoMaternoC,Cabecera_Pedido.iIdVendedor," +
                " ISNULL(MaestroVendedor.cPrimerNombre, 'Desconocido') AS PNombreV,ISNULL(MaestroVendedor.cSegundoNombre, '') " +
                " AS SNombreV, ISNULL(MaestroVendedor.cApellidoPaterno, '') AS ApellidoPV, ISNULL(MaestroVendedor.cApellidoMaterno, '') " +
                " AS ApellidoMV, Cabecera_Pedido.dValorBrutoVenta,Cabecera_Pedido.dDescuentoPedido, Cabecera_Pedido.dTotalVenta," +
                " FORMAT(Cabecera_Pedido.dFechaGuardadoPedido-'5:00','dd/MM/yyyy  hh:mm tt') " +
                ",Cabecera_Pedido.dEstadoAtencion, " +
                " Cabecera_Pedido.cEstadoPermanencia,Cabecera_Pedido.cIdentificador_Pedido " +
                " FROM Cabecera_Pedido LEFT OUTER JOIN " +
                " maestroCliente ON Cabecera_Pedido.iIdCliente = maestroCliente.iIdCliente" +
                " AND maestroCliente.iIdCompany=? LEFT OUTER JOIN " +
                " MaestroVendedor ON Cabecera_Pedido.iIdVendedor = MaestroVendedor.iIdVendedor " +
                " AND MaestroVendedor.iIdCompany = ? AND MaestroVendedor.iIdTienda = ? " +
                " WHERE(Cabecera_Pedido.iIdCompany = ?) AND (Cabecera_Pedido.iIdTienda = ? ) " +
                " AND (Cabecera_Pedido.cEstadoPermanencia in ('R','P')) and Cabecera_Pedido.iId_Cabecera_Pedido>? " +
                " order by Cabecera_Pedido.dFechaGuardadoPedido desc";
        if (conn != null) {
            try {
                ps = conn.prepareStatement(select);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, Constantes.Tienda.idTienda);
                ps.setInt(4, Constantes.Empresa.idEmpresa);
                ps.setInt(5, Constantes.Tienda.idTienda);
                ps.setInt(6, idUltimaCabecera);
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();
                while (rs.next()) {
                    c = new mCabeceraPedido();
                    c.setIdCabecera(rs.getInt(1));
                    c.setObservacion(rs.getString(2));
                    c.setIdCliente(rs.getInt(3));
                    c.getCliente().setiId(rs.getInt(4));
                    c.getCliente().setRazonSocial(rs.getString(5));
                    c.getCliente().setcName(rs.getString(6) + " " + rs.getString(7));
                    c.getCliente().setcApellidoPaterno(rs.getString(8));
                    c.getCliente().setcApellidoMaterno(rs.getString(9));
                    c.getVendedor().setIdVendedor(rs.getInt(10));
                    c.getVendedor().setPrimerNombre(rs.getString(11) + " " + rs.getString(12));
                    c.getVendedor().setApellidoPaterno(rs.getString(13));
                    c.getVendedor().setApellidoMaterno(rs.getString(14));
                    c.setTotalBruto(rs.getBigDecimal(15));
                    c.setDescuentoPrecio(rs.getBigDecimal(16));
                    c.setTotalNeto(rs.getBigDecimal(17));
                    c.setFecha(rs.getString(18));
                    c.setcEstadoAtencion(rs.getString(19));
                    c.setEstadoPermanecia(rs.getString(20).charAt(0));
                    c.setIdentificadorPedido(rs.getString(21));
                    lista.add(c);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            }
        } else {
            lista = null;
        }
        return lista;
    }

    public List<mCabeceraPedido> ObtenerPedidos(String desde, String hasta, int idCliente) {

        desde = desde + " 00:00:00";
        hasta = hasta + " 23:59:59";
        mCabeceraPedido c = null;
        List<mCabeceraPedido> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String selectB = "SELECT Cabecera_Pedido.iId_Cabecera_Pedido,Cabecera_Pedido.cObservacion,Cabecera_Pedido.iIdCliente," +
                " ISNULL(maestroCliente.iTipoCliente, 0) AS TipoCliente, ISNULL(maestroCliente.cRazonSocial, '') AS RazonSocialC, " +
                " ISNULL(maestroCliente.cPrimerNombre, 'Desconocido') AS PrimerNombreC, ISNULL(maestroCliente.cSegundoNombre, '') AS SegundoNombreC" +
                " ,ISNULL(maestroCliente.cApellidoPaterno, '') AS ApellidoPaternoC,  " +
                " ISNULL(maestroCliente.cApellidoMaterno, '') AS ApellidoMaternoC,Cabecera_Pedido.iIdVendedor," +
                " ISNULL(MaestroVendedor.cPrimerNombre, 'Desconocido') AS PNombreV,ISNULL(MaestroVendedor.cSegundoNombre, '') " +
                " AS SNombreV, ISNULL(MaestroVendedor.cApellidoPaterno, '') AS ApellidoPV, ISNULL(MaestroVendedor.cApellidoMaterno, '') " +
                " AS ApellidoMV, Cabecera_Pedido.dValorBrutoVenta,Cabecera_Pedido.dDescuentoPedido, Cabecera_Pedido.dTotalVenta," +
                " FORMAT(Cabecera_Pedido.dFechaGuardadoPedido-'5:00','dd/MM/yyyy  hh:mm tt') " +
                ",Cabecera_Pedido.dEstadoAtencion, " +
                " Cabecera_Pedido.cEstadoPermanencia,Cabecera_Pedido.cIdentificador_Pedido " +
                " FROM Cabecera_Pedido LEFT OUTER JOIN " +
                " maestroCliente ON Cabecera_Pedido.iIdCliente = maestroCliente.iIdCliente" +
                " AND maestroCliente.iIdCompany=? LEFT OUTER JOIN " +
                " MaestroVendedor ON Cabecera_Pedido.iIdVendedor = MaestroVendedor.iIdVendedor " +
                " AND MaestroVendedor.iIdCompany = ? AND MaestroVendedor.iIdTienda = ? " +
                " WHERE(Cabecera_Pedido.iIdCompany = ?) AND (Cabecera_Pedido.iIdTienda = ? ) and (Cabecera_Pedido.dFechaGuardadoPedido between ? AND ?)" +
                " AND (Cabecera_Pedido.cEstadoPermanencia in ('R','P')) order by Cabecera_Pedido.dFechaGuardadoPedido desc";

        if (conn != null) {
            try {
                ps = conn.prepareStatement(selectB);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, Constantes.Tienda.idTienda);
                ps.setInt(4, Constantes.Empresa.idEmpresa);
                ps.setInt(5, Constantes.Tienda.idTienda);
                ps.setString(6, desde);
                ps.setString(7, hasta);
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();
                while (rs.next()) {

                    c = new mCabeceraPedido();
                    c.setIdCabecera(rs.getInt(1));
                    c.setObservacion(rs.getString(2));
                    c.setIdCliente(rs.getInt(3));
                    c.getCliente().setiId(rs.getInt(4));
                    c.getCliente().setRazonSocial(rs.getString(5));
                    c.getCliente().setcName(rs.getString(6) + " " + rs.getString(7));
                    c.getCliente().setcApellidoPaterno(rs.getString(8));
                    c.getCliente().setcApellidoMaterno(rs.getString(9));
                    c.getVendedor().setIdVendedor(rs.getInt(10));
                    c.getVendedor().setPrimerNombre(rs.getString(11) + " " + rs.getString(12));
                    c.getVendedor().setApellidoPaterno(rs.getString(13));
                    c.getVendedor().setApellidoMaterno(rs.getString(14));
                    c.setTotalBruto(rs.getBigDecimal(15));
                    c.setDescuentoPrecio(rs.getBigDecimal(16));
                    c.setTotalNeto(rs.getBigDecimal(17));
                    c.setFecha(rs.getString(18));
                    c.setcEstadoAtencion(rs.getString(19));
                    c.setEstadoPermanecia(rs.getString(20).charAt(0));
                    c.setIdentificadorPedido(rs.getString(21));
                    lista.add(c);

                }


            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            }
        } else {
            lista = null;

        }
        return lista;

    }

    public List<mAreaProduccion> getAreasProduccionListado() {
        List<mAreaProduccion> lista = null;
        mAreaProduccion a;
        ResultSet rs = null;

        PreparedStatement ps = null;


        if (conn != null) {

            try {
                ps = conn.prepareStatement("select id_Area_Produccion,c_Descripcion_Area " +
                        " from Areas_Produccion where id_Company=? and id_Tienda=? and c_Estado_Eliminado='' order by id_Company asc");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Tienda.idTienda);
                ps.execute();

                rs = ps.getResultSet();
                lista = new ArrayList<>();
                while (rs.next()) {

                    a = new mAreaProduccion();
                    a.setIdArea(rs.getInt(1));
                    a.setCDescripcionArea(rs.getString(2));

                    lista.add(a);
                }
                lista.size();
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            }


        } else {
            lista = null;
        }
        return lista;
    }

    public byte EliminarAreaProduccion(mAreaProduccion area) {
        CallableStatement cs = null;
        byte respuesta = 0;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_Eliminar_Area_Produccion(?,?,?,?,?,?)");
                cs.setInt(1, area.getIdArea());
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Tienda.idTienda);
                cs.setInt(4, Constantes.Terminal.idTerminal);
                cs.setInt(5, Constantes.Empresa.idEmpresa);
                cs.registerOutParameter(6, Types.TINYINT);
                cs.execute();
                respuesta = cs.getByte(6);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }

        } else {
            respuesta = 99;
        }

        return respuesta;
    }

    public byte EditarAreaProduccion(mAreaProduccion area) {

        CallableStatement cs = null;
        byte respuesta = 0;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_Editar_Area_Produccion(?,?,?,?,?,?,?)");
                cs.setInt(1, area.getIdArea());
                cs.setString(2, area.getCDescripcionArea());
                cs.setInt(3, Constantes.Empresa.idEmpresa);
                cs.setInt(4, Constantes.Tienda.idTienda);
                cs.setInt(5, Constantes.Terminal.idTerminal);
                cs.setInt(6, Constantes.Empresa.idEmpresa);
                cs.registerOutParameter(7, Types.TINYINT);
                cs.execute();
                respuesta = cs.getByte(7);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            }

        } else {
            respuesta = 99;
        }

        return respuesta;
    }

    public byte GuardarAreaProduccion(mAreaProduccion area) {

        PreparedStatement ps = null;
        byte resultado = 0;
        if (conn != null) {

            try {
                ps = conn.prepareStatement("insert into Areas_Produccion" +
                        "(c_Descripcion_Area,id_Company,id_Tienda,id_Terminal_c,id_Usuario_c,d_Fecha_c) " +
                        " values(?,?,?,?,?,GETUTCDATE())");
                ps.setString(1, area.getCDescripcionArea());
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, Constantes.Tienda.idTienda);
                ps.setInt(4, Constantes.Terminal.idTerminal);
                ps.setInt(5, Constantes.Usuario.idUsuario);
                ps.execute();
                resultado = 100;
            } catch (SQLException e) {
                e.printStackTrace();
                resultado = 99;
            } finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return resultado;

    }

    public mAreaProduccion getAreaPorId(int idAreaProduccion) {

        mAreaProduccion a = null;

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("select id_Area_Produccion,rtrim(ltrim(c_Descripcion_Area)) " +
                    "from Areas_Produccion where id_Company =? and  id_Area_Produccion=?  ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, idAreaProduccion);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                a = new mAreaProduccion();
                a.setIdArea(rs.getInt(1));
                a.setCDescripcionArea(rs.getString(2));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            a = null;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return a;

    }

    public double cantidadMaximaPedidoWeb() {

        double data = 0;

        try {
            PreparedStatement ps = conn.prepareStatement("select ISNULL(dCantidad_max_pedidoDefault,10) as CantidadMaximaConfiguracion from configuracion_generaltienda where iIdCompany=? and iIdTienda=?");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                data = rs.getInt("CantidadMaximaConfiguracion");
            }


        } catch (Exception ex) {


        }

        return data;
    }

    public List<mAreaProduccion> getAreasProduccion() {


        List<mAreaProduccion> lista = null;
        mAreaProduccion a;
        ResultSet rs = null;

        PreparedStatement ps = null;

        if (conn != null) {
            try {
                ps = conn.prepareStatement("select id_Area_Produccion,c_Descripcion_Area " +
                        " from Areas_Produccion where id_Company =? and c_Estado_Eliminado='' order by id_Area_Produccion asc");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();
                while (rs.next()) {

                    a = new mAreaProduccion();
                    a.setIdArea(rs.getInt(1));
                    a.setCDescripcionArea(rs.getString(2));

                    lista.add(a);
                }
                lista.size();
            } catch (SQLException e) {
                e.printStackTrace();
                lista = null;
            }
        } else {
            lista = null;
        }


        return lista;

    }

    public long VerificarVersion(String nameVersion, int idVersion, String idApp) {

        long respuesta = 0L;
        CallableStatement cs = null;
        Connection con = getConnectionStart();

        if (con != null) {
            try {
                cs = con.prepareCall("call sp_verificar_conexion_ves(?,?,?,?)");
                cs.setString(1, idApp);
                cs.setInt(2, idVersion);
                cs.setString(3, nameVersion);
                cs.registerOutParameter(4, Types.BIGINT);

                cs.execute();

                respuesta = cs.getLong(4);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    con.close();
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } else {
            respuesta = 98;
        }
        return respuesta;
    }

    public int VerificarPinReinicio(String pin) {

        int respuesta = 0;
        CallableStatement cs = null;
        Connection con = getConnectionStart();
        if (con != null) {
            try {
                cs = con.prepareCall("call sp_verificar_pin(?,?)");
                cs.setString(1, pin);
                cs.registerOutParameter(2, Types.INTEGER);
                cs.execute();
                respuesta = cs.getInt(2);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = 99;
            } finally {
                try {
                    cs.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            respuesta = 98;
        }

        return respuesta;
    }

    public List<mSubCategoria> ObtenerSubCategorias(int idCategoria) {
        List<mSubCategoria> lista = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if (conn != null) {
            try {
                ps = conn.prepareStatement("select id_subcategoria,c_Descripcion_SubCategoria " +
                        " from SubCategorias where id_company=?  and id_categoria=? " +
                        " and c_Estado_Eliminado='' order by c_Descripcion_SubCategoria asc");

                ps.setInt(1, Constantes.Empresa.idEmpresa);

                ps.setInt(2, idCategoria);
                ps.execute();
                rs = ps.getResultSet();
                lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new mSubCategoria(rs.getInt(1), rs.getString(2)));
                }
            } catch (SQLException e) {
                lista = null;
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            lista = null;
        }
        return lista;
    }

    public mSubCategoria AgregarSubCategoria(int idCategoria, String descripcionSubCategoria) {

        CallableStatement cs = null;
        mSubCategoria sc = null;
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_agregar_sub_categoria(?,?,?,?,?,?,?)");
                cs.setInt(1, idCategoria);
                cs.setString(2, descripcionSubCategoria);
                cs.setInt(3, Constantes.Empresa.idEmpresa);
                cs.setInt(4, Constantes.Tienda.idTienda);
                cs.setInt(5, Constantes.Terminal.idTerminal);
                cs.setInt(6, Constantes.Usuario.idUsuario);
                cs.registerOutParameter(7, Types.INTEGER);
                cs.execute();
                sc = new mSubCategoria(cs.getInt(7), descripcionSubCategoria);


            } catch (SQLException e) {
                e.printStackTrace();
                sc = new mSubCategoria(-99, "");
            } finally {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            sc = new mSubCategoria(-99, "");
        }
        return sc;
    }

    public mSubCategoria EditarSubCategoria(mSubCategoria subCategoria) {

        mSubCategoria sc = null;


        PreparedStatement ps = null;

        if (conn != null) {

            try {
                ps = conn.prepareStatement("update SubCategorias set c_Descripcion_SubCategoria=? where id_company=?  and id_categoria=? and id_subcategoria=? ");
                ps.setString(1, subCategoria.getDescripcionSubCategoria());
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, Constantes.Tienda.idTienda);
                ps.setInt(4, subCategoria.getIdCategoria());
                ps.setInt(5, subCategoria.getIdSubCategoria());
                ps.execute();
                sc = subCategoria;
            } catch (SQLException e) {
                e.printStackTrace();
                sc = new mSubCategoria(-99, "");
            } finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            sc = new mSubCategoria(-99, "");
        }
        return sc;
    }

    public void ObtenerTokenFacturacion() {


    }

    public String getHttpNewVersion(String packageName, String versionName, int versionCode) {

        String respuesta = "";
        CallableStatement cs = null;
        Connection con = getConnectionStart();
        if (con != null) {
            try {
                cs = con.prepareCall("call sp_get_url_package(?,?,?,?)");
                cs.setString(1, packageName);
                cs.setString(2, versionName);
                cs.setInt(3, versionCode);
                cs.registerOutParameter(4, Types.VARCHAR);
                cs.execute();
                respuesta = cs.getString(4);
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = "E.C.";
            } finally {
                try {
                    cs.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            respuesta = "N.I.";
        }
        return respuesta;
    }

    public boolean ObtenerTiposDocPago() {

        boolean respuesta = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        mDocPago doc = null;
        List<mDocPago> lista = new ArrayList<>();
        try {
            ps = conn.prepareStatement("SELECT [iIdTipoDocumento]" +
                    "      ,[id_doc_sunat]" +
                    "      ,[cDescripcion_Visible]," +
                    " bGeneraDocumento" +
                    "  FROM Tipo_Documento_Pago where bVisible_Venta=1 order by bOrdenVenta");
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                doc = new mDocPago();
                doc.setIdDoc(rs.getInt(1));
                doc.setIdEnvio(rs.getInt(2));
                doc.setCDescripcion(rs.getString(3));
                doc.setGeneraDocPago(rs.getBoolean(4));
                lista.add(doc);
            }
            Constantes.TiposDocPago.listaTipoDocPago = lista;
            respuesta = true;
        } catch (SQLException e) {
            respuesta = false;
            e.printStackTrace();
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return respuesta;
    }

    public boolean ObtenerTiposDocIdentificacion() {

        boolean respuesta = false;
        List<mTipoDocumento> lista = new ArrayList<>();
        mTipoDocumento t = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        if (conn != null) {

            try {
                ps = conn.prepareStatement("select iIdTipoDocumento,cDescripcionCorta,id_doc_sunat," +
                        "b_Verificar_Direccion,iLongitud,rtrim(cDenominacionNumero)," +
                        "rtrim(cDenominacionCliente),bVerificarRuc " +
                        "from TipodeDocumento where b_visible_interfaz=1");
                ps.execute();
                rs = ps.getResultSet();

                while (rs.next()) {

                    t = new mTipoDocumento();
                    t.setIdTipoDocumento(rs.getInt(1));
                    t.setCDescripcionCorta(rs.getString(2));
                    t.setIdDocSunat(rs.getInt(3));
                    t.setBVerificarDireccion(rs.getBoolean(4));
                    t.setLongitudNumeroDoc(rs.getInt(5));
                    t.setDenominacionNumero(rs.getString(6));
                    t.setDenominacionCliente(rs.getString(7));
                    t.setVerificaRuc(rs.getBoolean(8));
                    lista.add(t);
                }
                Constantes.TiposDocumentoId.listadoDocumentos = lista;
                Constantes.TiposDocumentoId.listadoDocumentos.size();
            } catch (SQLException e) {
                e.printStackTrace();
                respuesta = false;
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            respuesta = false;
        }

        return respuesta;
    }

    public Venta ObtenerVentaId(int idCabeceraVenta) {

        CallableStatement cs = null;
        Venta venta = new Venta();
        List<ProductoEnVenta> listaP = new ArrayList<>();
        ;

        try {
            cs = conn.prepareCall("call sp_obtener_venta_id_v5(" + ParamStoreProcedure(29) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idCabeceraVenta);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.VARCHAR);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.registerOutParameter(7, Types.VARCHAR);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.registerOutParameter(9, Types.INTEGER);
            cs.registerOutParameter(10, Types.INTEGER);
            cs.registerOutParameter(11, Types.VARCHAR);
            cs.registerOutParameter(12, Types.VARCHAR);
            cs.registerOutParameter(13, Types.VARCHAR);
            cs.registerOutParameter(14, Types.INTEGER);
            cs.registerOutParameter(15, Types.DECIMAL);
            cs.registerOutParameter(16, Types.DECIMAL);
            cs.registerOutParameter(17, Types.DECIMAL);
            cs.registerOutParameter(18, Types.DECIMAL);
            cs.registerOutParameter(19, Types.DECIMAL);
            cs.registerOutParameter(20, Types.DECIMAL);
            cs.registerOutParameter(21, Types.CHAR);
            cs.registerOutParameter(22, Types.VARCHAR);
            cs.registerOutParameter(23, Types.VARCHAR);
            cs.registerOutParameter(24, Types.VARCHAR);
            cs.registerOutParameter(25, Types.VARCHAR);
            cs.registerOutParameter(26, Types.VARCHAR);
            cs.registerOutParameter(27, Types.BIT);
            cs.registerOutParameter(28, Types.VARCHAR);
            cs.registerOutParameter(29, Types.VARCHAR);

            ResultSet set = cs.executeQuery();

            if (set != null) {

                while (set.next()) {

                    ProductoEnVenta p = new ProductoEnVenta();
                    p.setCodigoProducto(set.getString(1));
                    p.setProductName(set.getString(2));
                    p.setCodUnidSunat(set.getString(3));
                    p.setCantidad(set.getFloat(4));
                    p.setValorUnitario(set.getBigDecimal(5));
                    p.setPrecioOriginal(set.getBigDecimal(6));
                    p.setPrecioNeto(set.getBigDecimal(7));
                    p.setMontoIgv(set.getBigDecimal(8));
                    p.setPrecioVentaFinal(set.getBigDecimal(9));
                    p.setMontoDescuento(set.getBigDecimal(10));
                    p.setEsVariante(set.getBoolean(11));
                    p.setDetallePack(set.getString(12));

                    listaP.add(p);

                }
                set.close();
            }
            mCabeceraVenta cab = new mCabeceraVenta();
            cab.setIdVenta(cs.getInt(3));
            cab.getVendedor().setPrimerNombre(cs.getString(4));
            cab.getVendedor().setIdVendedor(cs.getInt(5));
            cab.getCliente().setRazonSocial(cs.getString(6));
            cab.getCliente().setNumeroRuc(cs.getString(7));
            cab.getCliente().setRazonSocial(cs.getString(8));
            cab.getCliente().setiId(cs.getInt(9));
            cab.setIdTipoDocPago(cs.getInt(10));
            cab.setTipoDocumento(cs.getString(11));
            cab.setNumSerie(cs.getString(12));
            cab.setNumeroCorrelativo(cs.getString(13));
            cab.setNumCorrelativo(cs.getInt(14));
            cab.setTotalBruto(cs.getBigDecimal(15));
            cab.setTotalDescuento(cs.getBigDecimal(16));
            cab.setTotalPagado(cs.getBigDecimal(17));
            cab.setTotalCambio(cs.getBigDecimal(18));
            cab.setTotalGravado(cs.getBigDecimal(19));
            cab.setTotalIgv(cs.getBigDecimal(20));
            cab.setEstadoVenta(cs.getString(21));
            cab.setFechaEmision(cs.getString(22));
            cab.setFechaVenta(cs.getString(22));
            cab.setIdentificador(cs.getString(23));
            cab.setAnulado(cs.getBoolean(24));
            cab.setObservacion(cs.getString(25));
            cab.getCliente().setcDireccion(cs.getString(26));
            cab.setUsaFacElectronica(cs.getBoolean(27));
            cab.setcEstadoCPE(cs.getString(28));
            cab.setCodeStatusCPE(cs.getString(29));
            venta.setCabeceraVenta(cab);
            venta.setProductosVenta(new ArrayList(listaP));

        } catch (SQLException e) {

            e.printStackTrace();
            venta = new Venta();
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                venta = new Venta();
            }
        }

        return venta;
    }

    public List<ProductoEnVenta> ObtenerDetalleVentaV2(int idCabeceraVenta) {
        List<ProductoEnVenta> lista = new ArrayList<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        ProductoEnVenta p;
        try {
            cs = conn.prepareCall("call sp_obtener_detalle_venta(?,?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, idCabeceraVenta);
            cs.setInt(3, Constantes.Tienda.idTienda);
            rs = cs.executeQuery();
            while (rs.next()) {
                p = new ProductoEnVenta();
                p.setCodigoProducto(rs.getString(1));
                p.setProductName(rs.getString(2));
                p.setCodUnidSunat(rs.getString(3));
                p.setCantidad(rs.getFloat(4));
                p.setValorUnitario(rs.getBigDecimal(5));
                p.setPrecioOriginal(rs.getBigDecimal(6));
                p.setPrecioNeto(rs.getBigDecimal(7));
                p.setMontoIgv(rs.getBigDecimal(8));
                p.setPrecioVentaFinal(rs.getBigDecimal(9));
                p.setMontoDescuento(rs.getBigDecimal(10));
                p.setEsVariante(rs.getBoolean(11));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
            p = new ProductoEnVenta();
            p.setIdProducto(-99);
            lista.add(p);

        }

        return lista;
    }

    public List<ProductoEnVenta> ObtenerDetalleVenta(int idCabeceraVenta) {
        List<ProductoEnVenta> lista = new ArrayList<>();
        PreparedStatement ps = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        final String query = "select p.cKey,dv.cProductName+' '+dv.cDescripcion_Variante+' '+dv.cObservacion,dv.cDescripcion_Unidad_Sunat" +
                ",dv.iCantidad,dv.dValor_Unitario,dv.dPrecioVentaUnidad," +
                "dv.dPrecio_Neto,dv.d_Igv,dv.dPrecio_Subtotal,dv.dMontoDescuento_NoIgv,dv.bEsVariante " +
                " from " +
                "Detalle_Venta as dv inner join " +
                "product as p on dv.iIdProducto=p.iIdProduct " +
                "left join " +
                "  Categoria_Productos AS cp (NOLOCK) " +
                " on p.id_Categoria=cp.id_categoria_producto" +
                " and cp.id_company=p.iIdCompany " +
                " where dv.id_Company=?" +
                " and dv.iIdCabecera_Venta=?" +
                " and dv.bDetallePack=0 and p.iIdCompany=?  order by iNum_Item asc";
        if (conn != null) {
            try {
                cs = conn.prepareCall("call sp_obtener_detalle_venta(" + ParamStoreProcedure(3) + ")");
                cs.setInt(1, Constantes.Empresa.idEmpresa);
                cs.setInt(2, idCabeceraVenta);
                cs.setInt(3, 3111);
                cs.execute();
                rs = cs.getResultSet();
                while (rs.next()) {
                    ProductoEnVenta p = new ProductoEnVenta();
                    p.setCodigoProducto(rs.getString(1));
                    p.setProductName(rs.getString(2));
                    p.setCodUnidSunat(rs.getString(3));
                    p.setCantidad(rs.getFloat(4));
                    p.setValorUnitario(rs.getBigDecimal(5));
                    p.setPrecioOriginal(rs.getBigDecimal(6));//precioItem 8590.40
                    p.setPrecioNeto(rs.getBigDecimal(7));
                    p.setMontoIgv(rs.getBigDecimal(8));
                    p.setPrecioVentaFinal(rs.getBigDecimal(9));//montoItem 6169.49
                    p.setMontoDescuento(rs.getBigDecimal(10));
                    p.setEsVariante(rs.getBoolean(11));
                    lista.add(p);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                lista = new ArrayList<>();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return lista;
    }

    public List<mProduct> ObtenerProductosVentasMonto(String fechaInicio, String fechaFinal) {
        String fInicio = fechaInicio + " 00:00:00 ";
        String fFinal = fechaFinal + " 23:59:59 ";
        List<mProduct> list = new ArrayList<>();
        mProduct product = null;
        ResultSet rs = null;
        final String select = " select isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''), " +
                " p.cProductName,dv.cDescripcion_Variante,sum(dv.dPrecio_Subtotal) as Total_Monto_Venta, " +
                "sum (dv.iCantidad) as unidad_ventas,p.cDescripcion_Unidad from Detalle_Venta as dv " +
                " inner join Product as p on dv.iIdProducto=p.iIdProduct " +
                " left join Categoria_Productos as c on p.id_Categoria=c.id_categoria_producto and c.id_company=? and c.id_tienda=? " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria and " +
                "  sc.id_Company=? and sc.id_tienda=? " +
                "  inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                " where dv.id_Company=? and dv.bCabeceraPack=0 and dv.cEstadoDetalleVenta='A' and " +
                " dv.cEliminado='A' and p.iIdCompany=? and " +
                "  p.id_tienda=? AND p.cEliminado='' and " +
                " cv.FechaVentaRealizada-'5:00' between ? and ? " +
                " group by dv.iIdProducto,p.cProductName,dv.cDescripcion_Variante, " +
                " isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''),p.cDescripcion_Unidad " +
                " order by sum(dv.dPrecio_Subtotal) desc ";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Tienda.idTienda);
            ps.setInt(5, Constantes.Empresa.idEmpresa);
            ps.setInt(6, Constantes.Empresa.idEmpresa);
            ps.setInt(7, Constantes.Tienda.idTienda);
            ps.setString(8, fInicio);
            ps.setString(9, fFinal);

            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                product = new mProduct();
                product.setDescripcionCategoria(rs.getString(1));
                product.setDescripcionSubCategoria(rs.getString(2));
                product.setcProductName(rs.getString(3));
                product.setDescripcionVariante(rs.getString(4));
                product.setPrecioVenta(rs.getBigDecimal(5));
                product.setdQuantity(rs.getFloat(6));
                product.setUnidadMedida(rs.getString(7));
                list.add(product);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    public List<mProduct> ObtenerProductosVentasCantidad(String fechaInicio, String fechaFinal) {
        String fInicio = fechaInicio + " 00:00:00 ";
        String fFinal = fechaFinal + " 23:59:59 ";
        List<mProduct> list = new ArrayList<>();
        mProduct product = null;
        ResultSet rs = null;
        final String select = " select isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''), " +
                " p.cProductName,dv.cDescripcion_Variante,sum(dv.dPrecio_Subtotal) as Total_Monto_Venta, " +
                "sum (dv.iCantidad) as unidad_ventas,p.cDescripcion_Unidad from Detalle_Venta as dv " +
                " inner join Product as p on dv.iIdProducto=p.iIdProduct " +
                " left join Categoria_Productos as c on " +
                " p.id_Categoria=c.id_categoria_producto and c.id_company=? and c.id_tienda=? " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria and " +
                "  sc.id_Company=? and sc.id_tienda=? " +
                "   inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                " where dv.id_Company=? and dv.bCabeceraPack=0 and dv.cEstadoDetalleVenta='A' and " +
                " dv.cEliminado='A' and p.iIdCompany=? and " +
                "  p.id_tienda=? AND p.cEliminado='' and " +
                "  cv.FechaVentaRealizada- '5:00' between ? and ? " +
                " group by cast(cv.FechaVentaRealizada-'5:00' as date),dv.iIdProducto,p.cProductName,dv.cDescripcion_Variante, " +
                " isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''),p.cDescripcion_Unidad " +
                " order by sum(dv.iCantidad) desc ";

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Tienda.idTienda);
            ps.setInt(5, Constantes.Empresa.idEmpresa);
            ps.setInt(6, Constantes.Empresa.idEmpresa);
            ps.setInt(7, Constantes.Tienda.idTienda);
            ps.setString(8, fInicio);
            ps.setString(9, fFinal);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                product = new mProduct();
                product.setDescripcionCategoria(rs.getString(1));
                product.setDescripcionSubCategoria(rs.getString(2));
                product.setcProductName(rs.getString(3));
                product.setDescripcionVariante(rs.getString(4));
                product.setPrecioVenta(rs.getBigDecimal(5));
                product.setdQuantity(rs.getFloat(6));
                product.setUnidadMedida(rs.getString(7));
                list.add(product);

            }
            list.size();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    public List<ProductoEnVenta> ObtenerAcumuladoVentasPorCierreMontoTop10(int idCierre) {
        List<ProductoEnVenta> list = new ArrayList<>();
        ProductoEnVenta product = null;
        ResultSet rs = null;
        final String select = " select Top(10) isnull(c.cDescripcion_categoria,''),isnull(sc.c_Descripcion_SubCategoria,''), " +
                " p.cProductName,dv.cDescripcion_Variante,sum(dv.dPrecio_Subtotal) as Total_Monto_Venta, " +
                "sum (dv.iCantidad) as unidad_ventas,sum(dMontoDescuento_Igv)" +
                ",sum(d_Igv),dv.cDescripcion_combo from Detalle_Venta as dv " +
                " inner join Product as p on dv.iIdProducto=p.iIdProduct " +
                " left join Categoria_Productos as c on " +
                " p.id_Categoria=c.id_categoria_producto and c.id_company=?  " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria and " +
                "  sc.id_Company=?  " +
                "   inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                " where dv.id_Company=? and dv.bCabeceraPack=0 and dv.cEstadoDetalleVenta='A' and " +
                " dv.cEliminado='A' and p.iIdCompany=? AND p.cEliminado='' and " +
                "  cv.id_cierre=? " +
                " group by  dv.iIdProducto,p.cProductName,dv.cDescripcion_combo,dv.cDescripcion_Variante, " +
                " isnull(c.cDescripcion_categoria,''),isnull(sc.c_Descripcion_SubCategoria,''),p.cDescripcion_Unidad " +
                " order by sum(dv.dPrecio_Subtotal) desc ";

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Empresa.idEmpresa);
            ps.setInt(5, idCierre);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                product = new ProductoEnVenta();
                product.setDescripcionCategoria(rs.getString(1));
                product.setDescripcionSubCategoria(rs.getString(2));
                product.setProductName(rs.getString(3));
                product.setDescripcionVariante(rs.getString(4));
                product.setPrecioVentaFinal(rs.getBigDecimal(5));
                product.setCantidad(rs.getFloat(6));
                product.setMontoDescuento(rs.getBigDecimal(7));
                product.setMontoIgv(rs.getBigDecimal(8));
                product.setDescripcionCombo(rs.getString(9));
                list.add(product);

            }
            list.size();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    public List<ProductoEnVenta> ObtenerAcumuladoVentasPorCierreMonto(int idCierre) {
        List<ProductoEnVenta> list = new ArrayList<>();
        ProductoEnVenta product = null;
        ResultSet rs = null;
        final String select = " select isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''), " +
                " p.cProductName,dv.cDescripcion_Variante,sum(dv.dPrecio_Subtotal) as Total_Monto_Venta, " +
                "sum (dv.iCantidad) as unidad_ventas,sum(dMontoDescuento_Igv),sum(d_Igv),dv.cDescripcion_combo from Detalle_Venta as dv " +
                " inner join Product as p on dv.iIdProducto=p.iIdProduct " +
                " left join Categoria_Productos as c on " +
                " p.id_Categoria=c.id_categoria_producto and c.id_company=? and c.id_tienda=? " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria and " +
                "  sc.id_Company=? and sc.id_tienda=? " +
                "   inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                " where dv.id_Company=? and dv.bCabeceraPack=0 and dv.cEstadoDetalleVenta='A' and " +
                " dv.cEliminado='A' and p.iIdCompany=? and " +
                "  p.id_tienda=? AND p.cEliminado='' and " +
                "  cv.id_cierre=? " +
                " group by  dv.iIdProducto,p.cProductName,dv.cDescripcion_combo,dv.cDescripcion_Variante, " +
                " isnull(c.cDescripcion_categoria,'General'),isnull(sc.c_Descripcion_SubCategoria,''),p.cDescripcion_Unidad " +
                " order by sum(dv.dPrecio_Subtotal) desc ";

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Tienda.idTienda);
            ps.setInt(5, Constantes.Empresa.idEmpresa);
            ps.setInt(6, Constantes.Empresa.idEmpresa);
            ps.setInt(7, Constantes.Tienda.idTienda);
            ps.setInt(8, idCierre);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                product = new ProductoEnVenta();
                product.setDescripcionCategoria(rs.getString(1));
                product.setDescripcionSubCategoria(rs.getString(2));
                product.setProductName(rs.getString(3));
                product.setDescripcionVariante(rs.getString(4));
                product.setPrecioVentaFinal(rs.getBigDecimal(5));
                product.setCantidad(rs.getFloat(6));
                product.setMontoDescuento(rs.getBigDecimal(7));
                product.setMontoIgv(rs.getBigDecimal(8));
                product.setDescripcionCombo(rs.getString(9));
                list.add(product);

            }
            list.size();
        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    //indexado
    public List<VentaDocumento> VentasPorDocumentoCierre(int idCierre) {

        List<VentaDocumento> lista = new ArrayList<>();
        final String select = "select tp.cDescripcion_Visible,count(cv.iId_Cabecera_Venta)," +
                "sum(cv.dTotal_Neto_Venta) from Cabecera_Venta as cv inner join " +
                "Tipo_Documento_Pago as tp on cv.id_TipoDocumentoPago=tp.iIdTipoDocumento " +
                "where iId_Company=? and iId_Tienda=? and id_cierre=? and cEstadoVenta='N' and cEliminado='' " +
                "group by tp.cDescripcion_Visible";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, idCierre);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {

                lista.add(new VentaDocumento(rs.getString(1), rs.getBigDecimal(3), rs.getInt(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return lista;
    }

    public List<mDetalleMovCaja> RetirosCajaPorCierre(int idCierre) {

        List<mDetalleMovCaja> list = new ArrayList<>();
        mDetalleMovCaja d = null;
        final String select = "select mi.cDescripcion_Motivo,count(id_mov_caja),sum(mv.mMonto) " +
                "from Movimiento_Caja as mv inner join Motivo_Ingreso_Retiro as mi on mv.id_motivo=mi.id_motivo " +
                "where id_company=? and mi.iTipo_Registro=2 and id_cierre_caja=? " +
                "GROUP BY mi.cDescripcion_Motivo";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, idCierre);
            ps.execute();

            rs = ps.getResultSet();

            while (rs.next()) {

                d = new mDetalleMovCaja();
                d.setDescripcion(rs.getString(1));
                d.setCantidadMov(rs.getInt(2));
                d.setMonto(rs.getBigDecimal(3));
                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            list = null;

        }

        return list;

    }

    public BigDecimal TotalDescuentoCierre(int idCierre) {

        final String select = " select isnull(sum(dMontoDescuento_Igv),0) from Detalle_Venta as dv " +
                " inner join Product as p on dv.iIdProducto=p.iIdProduct " +
                " left join Categoria_Productos as c on " +
                " p.id_Categoria=c.id_categoria_producto and c.id_company=? and c.id_tienda=? " +
                " left join SubCategorias as sc on p.id_Subcategoria=sc.id_subcategoria and " +
                "  sc.id_Company=? and sc.id_tienda=? " +
                "   inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                " where dv.id_Company=? and dv.bCabeceraPack=0 and dv.cEstadoDetalleVenta='A' and " +
                " dv.cEliminado='A' and p.iIdCompany=? and " +
                "  p.id_tienda=? AND p.cEliminado='' and " +
                "  cv.id_cierre=? ";

        BigDecimal totalDescuento = new BigDecimal(0);
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Tienda.idTienda);
            ps.setInt(5, Constantes.Empresa.idEmpresa);
            ps.setInt(6, Constantes.Empresa.idEmpresa);
            ps.setInt(7, Constantes.Tienda.idTienda);
            ps.setInt(8, idCierre);
            ps.execute();
            rs = ps.getResultSet();

            while (rs.next()) {

                totalDescuento = rs.getBigDecimal(1);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            totalDescuento = new BigDecimal(-99);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return totalDescuento;
    }

    public List<mCierre> ComparativoCierresUltimos10(int idCierre) {

        List<mCierre> list = new ArrayList<>();
        List<mCierre> lista = new ArrayList<>();
        final String select = " select  top(10) " +
                "fc.mMonto ," +
                "case c.cEstado_cierre " +
                " when 'C' THEN 'CERRADO' " +
                " when 'A' tHEN 'ABIERTO' end," +
                " isnull(FORMAT(c.dFecha_Apertura-'5:00','dd/MM'),'-') " +
                " ,isnull(FORMAT(c.dFecha_cierre-'5:00','dd/MM'),'Ahora' ) " +
                " from cierres  as c inner join  flujo_caja  as fc on c.id_cierre=fc.id_cierre " +
                "  INNER JOIN concepto_caja as cc on  " +
                " cc.id_concepto=fc.id_concepto_caja  where c.id_company=?  and c.id_tienda=? " +
                " and fc.id_company=? and fc.id_tienda=? and fc.cTipo_Registro=1 " +
                " and fc.id_concepto_caja=2 and c.id_cierre<=?  order by c.dFecha_Apertura desc ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        mCierre cierre;

        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Tienda.idTienda);
            ps.setInt(5, idCierre);
            ps.execute();
            rs = ps.getResultSet();

            while (rs.next()) {

                cierre = new mCierre();
                cierre.setTotalVentas(rs.getBigDecimal(1));
                cierre.setcFechaApertura(rs.getString(3));
                cierre.setcFechaCierre(rs.getString(4));
                list.add(cierre);

            }

            for (int i = list.size() - 1; i >= 0; i--) {
                lista.add(list.get(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = null;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return lista;

    }

    public List<mDetalleMovCaja> MovimientosCajaPorPeriodoFecha(String fInicio, String fFinal) {
        List<mDetalleMovCaja> list = new ArrayList<>();
        mDetalleMovCaja detalle;
        final String select = "select isnull(format(dFecha_Movimiento-'5:00','dd/MM/yyyy hh:mm tt'),'-'),mv.iTipo_registro, " +
                " mir.cDescripcion_Motivo,mv.cDescipcion_movimiento,mv.mMonto, " +
                " isnull(format(c.dFecha_apertura-'5:00','dd/MM hh:mm tt'),'-'), " +
                " isnull(format(c.dFecha_cierre-'5:00','dd/MM hh:mm tt'),'-') " +
                " from Movimiento_Caja as mv  " +
                " inner join Motivo_Ingreso_Retiro as mir on mv.id_motivo=mir.id_motivo " +
                " inner join cierres as c on c.id_cierre=mv.id_cierre_caja " +
                " where mv.id_company=? and c.id_company=? and c.id_tienda=? " +
                " and mv.dFecha_Movimiento-'5:00' between ? and ? " +
                " order by mv.iTipo_registro,dFecha_Movimiento desc";

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(select);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Tienda.idTienda);
            ps.setString(4, fInicio + " " + horaInicio);
            ps.setString(5, fFinal + " " + horaFinal);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                detalle = new mDetalleMovCaja();
                detalle.setcFechaTransaccion(rs.getString(1));
                detalle.setTipoRegistro(rs.getByte(2));
                detalle.setDescripcion(rs.getString(3));
                detalle.setDescripcionMotivo(rs.getString(4));
                detalle.setMonto(rs.getBigDecimal(5));
                detalle.getCierre().setcFechaApertura(rs.getString(6));
                detalle.getCierre().setcFechaCierre(rs.getString(7));
                list.add(detalle);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public mCierre CabeceraCierre(int idCierre) {
        mCierre c = new mCierre();
        final String cierre = " select case cEstado_cierre when 'C' THEN 'Caja cerrada' " +
                " when 'A' then 'Caja Abierta' end,isnull(format(dFecha_apertura-'5:00','dd/MM/yyyy hh:mm tt'),'-'), " +
                " case cEstado_cierre when 'C' THEN isnull(format(dFecha_cierre-'5:00','dd/MM/yyyy hh:mm tt'),'-') " +
                " when 'A' then 'Hasta ahora' end " +
                " from cierres where id_company=? AND id_tienda=? and  id_cierre=? ";

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(cierre);
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, idCierre);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                c.setEstadoCierre(rs.getString(1));
                c.setcFechaApertura(rs.getString(2));
                c.setcFechaCierre(rs.getString(3));

            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            c = new mCierre();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return c;
    }

    public List<ProductoEnVenta> ProductosVendidosPorFechaTienda(int idTienda, String fInicio, String fFinal) {
        PreparedStatement ps = null;
        String fI = fInicio + " 00:00:00";
        String fF = fFinal + " 23:59:59";
        List<ProductoEnVenta> lista = new ArrayList<>();
        ProductoEnVenta p;
        try {
            if (idTienda == 0) {
                ps = conn.prepareStatement(SelectPVFT + wherePVFT + groupByPVFT);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, Constantes.Empresa.idEmpresa);
                ps.setString(5, fI);
                ps.setString(6, fF);
                ps.execute();
            } else {
                ps = conn.prepareStatement(SelectPVFT + wherePVFT + whereTiendaPVFT + groupByPVFT);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.setInt(4, Constantes.Empresa.idEmpresa);
                ps.setString(5, fI);
                ps.setString(6, fF);
                ps.setInt(7, idTienda);
                ps.execute();
            }

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                p = new ProductoEnVenta();
                p.setIdTienda(rs.getInt(1));
                p.setFecha(rs.getString(2));
                p.setDescripcionCategoria(rs.getString(3));
                p.setDescripcionSubCategoria(rs.getString(4));
                p.setProductName(rs.getString(5));
                p.setDescripcionVariante(rs.getString(6));
                p.setPrecioVentaFinal(rs.getBigDecimal(7));
                p.setCantidad(rs.getFloat(8));
                p.setMontoDescuento(rs.getBigDecimal(9));
                p.setMontoIgv(rs.getBigDecimal(10));
                p.setDescripcionCombo(rs.getString(11));
                p.setCodigoProducto(rs.getString(12));
                p.setCostoTotal(rs.getBigDecimal(13));
                p.setUtilidad(rs.getBigDecimal(14));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<PagosDocPago> PagosPorDocumentoPago(int idTienda, String fInit, String fFin) {
        PagosDocPago a;
        fInit = fInit + " 00:00:00 ";
        fFin = fFin + " 23:59:59 ";

        PreparedStatement ps = null;

        List<PagosDocPago> lista = new ArrayList<>();

        try {
            if (idTienda == 0) {
                ps = conn.prepareStatement(selectPDP + wherePDP + groupPDP);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, fInit);
                ps.setString(3, fFin);
                ps.execute();

            } else {

                ps = conn.prepareStatement(selectPDP + wherePDP + whereTiendaPDP + groupPDP);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(4, idTienda);
                ps.setString(2, fInit);
                ps.setString(3, fFin);
                ps.execute();

            }
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                a = new PagosDocPago();
                a.setIdTienda(rs.getInt(1));
                a.setCDescripcion(rs.getString(2));
                a.setNumeroDocumentos(rs.getInt(3));
                a.setMonto(rs.getBigDecimal(4));
                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return lista;

    }

    public List<PagoPorTipoPago> PagosPorTiposPagoTiendas(int idTienda, String fInit, String fFinal) {

        fInit = fInit + " 00:00:00 ";
        fFinal = fFinal + " 23:59:59";
        List<PagoPorTipoPago> lista = new ArrayList<>();
        PagoPorTipoPago p;
        PreparedStatement ps = null;

        try {
            if (idTienda == 0) {
                ps = conn.prepareStatement(selectPPT + wherePPT + groupPPT);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, fInit);
                ps.setString(3, fFinal);
                ps.execute();
            } else {
                ps = conn.prepareStatement(selectPPT + wherePPT + whereTiendaPPT + groupPPT);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, fInit);
                ps.setString(3, fFinal);
                ps.setInt(4, idTienda);
                ps.execute();
            }
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                p = new PagoPorTipoPago();
                p.setIdTienda(rs.getInt(1));
                p.getTipoPago().setDescripcionTipoPago(rs.getString(2));
                p.setMonto(rs.getBigDecimal(3));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }

    //indexado
    public List<DescuentoPorTienda> DescuentosPorTienda(int idTienda, String fInicio, String fFinal) {

        List<DescuentoPorTienda> lista = new ArrayList<>();
        fInicio = fInicio + " 00:00:00 ";
        fFinal = fFinal + " 23:59:59 ";
        DescuentoPorTienda d;
        PreparedStatement ps = null;


        try {
            if (idTienda == 0) {
                ps = conn.prepareStatement("select iId_Tienda," +
                        "sum(isnull(dDescuento_Venta,0)) from cabecera_venta  \n" +
                        "as cv where cv.iId_Company=? " +
                        "" +
                        "and cv.cEstadoVenta='N' and cv.cEliminado='' and cv.FechaVentaRealizada " +
                        "between ? and ? group by iId_Tienda");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, fInicio);
                ps.setString(3, fFinal);
                ps.execute();

            } else {
                ps = conn.prepareStatement("select iId_Tienda," +
                        "sum(isnull(dDescuento_Venta,0)) from cabecera_venta  \n" +
                        "as cv where cv.iId_Company=? and cv.iId_tienda=? " +
                        "and cv.cEstadoVenta='N' and cv.cEliminado='' and cv.FechaVentaRealizada " +
                        "between ? and ? group by iId_Tienda");
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setInt(2, idTienda);
                ps.setString(3, fInicio);
                ps.setString(4, fFinal);

                ps.execute();

            }

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                d = new DescuentoPorTienda();
                d.setIdTienda(rs.getInt(1));
                d.setMonto(rs.getBigDecimal(2));
                lista.add(d);

            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return lista;

    }

    public List<MovCajaTienda> obtenerMovimientosCaja(int idTienda, String fInit, String fFinal) {

        List<MovCajaTienda> lista = new ArrayList<>();
        PreparedStatement ps = null;
        MovCajaTienda mov;

        try {
            if (idTienda == 0) {
                ps = conn.prepareStatement(selectMC + whereMC + groupMC);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, fInit);
                ps.setString(3, fFinal);
                ps.execute();
            } else {
                ps = conn.prepareStatement(selectMC + whereMC + whereTiendaMC + groupMC);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, fInit);
                ps.setString(3, fFinal);
                ps.setInt(4, idTienda);
                ps.execute();

            }

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                mov = new MovCajaTienda();
                mov.setIdTienda(rs.getInt(1));
                mov.getMotivo().setTipoMotivo(rs.getInt(2));
                mov.getMotivo().setDescripcionMotivo(rs.getString(3));
                mov.setMonto(rs.getBigDecimal(4));
                lista.add(mov);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return lista;
    }

    public List<TotalPorTienda> TotalesPorTienda(int idTienda, String fIni, String fFinal) {

        fIni = fIni + " 00:00:00 ";
        fFinal = fFinal + " 23:59:59 ";
        PreparedStatement ps = null;
        List<TotalPorTienda> lista = new ArrayList<>();
        TotalPorTienda t;
        try {

            if (idTienda == 0) {
                ps = conn.prepareStatement(selectTPT + whereTPT + groupTPT);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, fIni);
                ps.setString(3, fFinal);
                ps.execute();
            } else {


                ps = conn.prepareStatement(selectTPT + whereTPT + whereTiendaTPT + groupTPT);
                ps.setInt(1, Constantes.Empresa.idEmpresa);
                ps.setString(2, fIni);
                ps.setString(3, fFinal);
                ps.setInt(4, idTienda);
                ps.execute();
            }

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                t = new TotalPorTienda();
                t.setIdTienda(rs.getInt(1));
                t.setTotal(rs.getBigDecimal(2));
                t.setTotalIgv(rs.getBigDecimal(3));
                t.setTotalUtilidad(rs.getBigDecimal(4));
                t.setTotalCosto(rs.getBigDecimal(5));

                lista.add(t);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return lista;
    }

    public List<ProductoEnVenta> ObtenerAreasImpresionProductosPedido(int idCabeceraPedido) {

        List<ProductoEnVenta> lista = new ArrayList<>();
        ProductoEnVenta p;
        PreparedStatement ps = null;
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call sp_obtener_pedidos_area_trabajo(" + ParamStoreProcedure(3) + ")");

            cs.setInt(1, idCabeceraPedido);
            cs.setInt(2, Constantes.Empresa.idEmpresa);
            cs.setInt(3, Constantes.Tienda.idTienda);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {

                    p = new ProductoEnVenta();
                    p.setItemNum(rs.getInt(2));
                    p.setProductName(rs.getString(3));
                    p.setDescripcionModificador(rs.getString(4));
                    p.setDescripcionVariante(rs.getString(5));
                    p.setCantidad(rs.getFloat(6));
                    p.getAreaProduccion().setIdArea(rs.getInt(7));
                    try {
                        p.getAreaProduccion().getImpresora().setIP(rs.getString(8));

                        p.getAreaProduccion().getImpresora().setPuerto(rs.getInt(9));
                        p.getAreaProduccion().getImpresora().setMacAddress(rs.getString(10));
                        p.getAreaProduccion().getImpresora().setTipoImpresora(rs.getString(11));

                    } catch (Exception e) {
                        e.toString();
                    }
                    p.setEsDetallePack(rs.getBoolean(12));
                    p.setDescripcionCombo(rs.getString(13));
                    p.setObservacionProducto(rs.getString(14));
                    p.setCodigoProducto(rs.getString(15));
                    lista.add(p);
                }
                rs.close();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
            p = new ProductoEnVenta();
            p.setIdDetallePedido(-95);
            lista.add(p);
            Log.d("Error ", "OBTENER PEDIDOS IMPRESION -> " + e.toString()
            );
        } finally {

            if (ps != null) {
                try {

                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return lista;
    }

    public byte ActualizarEstadoImpresionPedido(List<ConfirmacionImpresion> lista, int idCabeceraPedido) {

        PreparedStatement ps = null;
        byte respuesta = 0;
        try {
            ps = conn.prepareStatement("update detalle_pedido set " +
                    "b_Visible_Pantalla=? where id_Company=? and" +
                    " iId_cabecera_pedido=?");

            for (ConfirmacionImpresion it : lista) {

                ps.setBoolean(1, it.getImprimio());
                ps.setInt(2, Constantes.Empresa.idEmpresa);
                ps.setInt(3, idCabeceraPedido);
                ps.addBatch();
            }

            ps.executeBatch();
            respuesta = 100;
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = 99;
        }
        return respuesta;
    }

    public List<ProductoEnVenta> ObtenerAreasImpresionProductosPedidoAnulacion(int idCabeceraPedido) {

        List<ProductoEnVenta> lista = new ArrayList<>();
        ProductoEnVenta p;
        PreparedStatement ps = null;
        String selectOAIPP = "SELECT  Detalle_Pedido.iId_Cabecera_Pedido," +
                "Detalle_Pedido.iNumItem,Detalle_Pedido.cProductName, " +
                "dbo.Detalle_Pedido.cDescripcion_Modificador, " +
                "dbo.Detalle_Pedido.cDescripcion_Variante, " +
                "dbo.Detalle_Pedido.iCantidad, isnull(dbo.Areas_Produccion.id_Area_Produccion,0)," +
                "isnull(i.cIp_Impresora,''),i.iPuerto,isnull(i.cMacAddress,null),isnull(i.cTipoImpresora,'')," +
                " Detalle_Pedido.bDetallePack,isnull( Detalle_Pedido.cDescripcion_Combo,'')," +
                " isnull(Detalle_Pedido.cObservacionProducto,'') " +
                "FROM Product INNER JOIN " +
                " Detalle_Pedido ON Product.iIdProduct = Detalle_Pedido.iIdProducto INNER JOIN " +
                " Areas_Produccion ON Product.id_Area_Produccion = Areas_Produccion.id_Area_Produccion INNER JOIN" +
                "  IMPRESORAS as i  ON Areas_Produccion.id_Area_Produccion=i.id_area " +
                " WHERE (Detalle_Pedido.bCabeceraPack = 0) AND " +
                "  (Detalle_Pedido.iId_Cabecera_Pedido = ?) AND" +
                " (Detalle_Pedido.id_Company = ?) and  " +
                " Detalle_Pedido.id_Tienda=? and " +
                " Areas_Produccion.id_Company=? and " +
                " i.id_company=? and  i.id_tienda=? and i.cEliminado='' " +
                " and Detalle_pedido.b_Visible_Pantalla=1 " +
                " order by " +
                " dbo.Areas_Produccion.id_Area_Produccion,Detalle_Pedido.iNumItem asc";
        try {
            ps = conn.prepareStatement(selectOAIPP);
            ps.setInt(1, idCabeceraPedido);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Tienda.idTienda);
            ps.setInt(4, Constantes.Empresa.idEmpresa);

            ps.setInt(5, Constantes.Empresa.idEmpresa);
            ps.setInt(6, Constantes.Tienda.idTienda);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                p = new ProductoEnVenta();
                p.setItemNum(rs.getInt(2));
                p.setProductName(rs.getString(3));
                p.setDescripcionModificador(rs.getString(4));
                p.setDescripcionVariante(rs.getString(5));
                p.setCantidad(rs.getFloat(6) * (-1));
                p.getAreaProduccion().setIdArea(rs.getInt(7));
                try {
                    p.getAreaProduccion().getImpresora().setIP(rs.getString(8));

                    p.getAreaProduccion().getImpresora().setPuerto(rs.getInt(9));
                    p.getAreaProduccion().getImpresora().setMacAddress(rs.getString(10));
                    p.getAreaProduccion().getImpresora().setTipoImpresora(rs.getString(11));

                } catch (Exception e) {
                    e.toString();
                }
                p.setEsDetallePack(rs.getBoolean(12));
                p.setDescripcionCombo(rs.getString(13));
                p.setObservacionProducto(rs.getString(14));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
            p = new ProductoEnVenta();
            p.setIdDetallePedido(-95);
            lista.add(p);
        } finally {

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return lista;
    }

    //INDEXADO
    public mImpresora ObtenerImpresora(int id) {

        PreparedStatement ps = null;
        mImpresora impresora = new mImpresora();

        try {
            ps = conn.prepareStatement(" select id_tienda,id_area,cDescripcionImpresora," +
                    "cIp_Impresora,iPuerto,cTipoImpresora " +
                    " from Impresoras where id_company=? and id_impresora=? ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, id);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                impresora.setIdImpresora(id);
                impresora.setIdTienda(rs.getInt(1));
                impresora.setIdArea(rs.getInt(2));
                impresora.setNombreImpresora(rs.getString(3));
                impresora.setIP(rs.getString(4));
                impresora.setPuerto(rs.getInt(5));


            }

        } catch (SQLException e) {
            e.printStackTrace();
            impresora.setIdImpresora(-99);
        }
        return impresora;
    }

    public byte GuardarNuevaImpresora(mImpresora impresora) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("insert into Impresoras(id_company,id_tienda," +
                    "id_area,cDescripcionImpresora,cIp_Impresora,iPuerto,cTipoImpresora) " +
                    "values (?,?,?,?,?,?,?) ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, impresora.getIdTienda());
            ps.setInt(3, impresora.getIdArea());
            ps.setString(4, impresora.getNombreImpresora());
            ps.setString(5, impresora.getIP());
            ps.setInt(6, impresora.getPuerto());
            ps.setString(7, "R");
            ps.execute();
            return 100;
        } catch (SQLException e) {
            e.printStackTrace();
            return 99;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public byte EditarImpresora(mImpresora impresora) {

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("update Impresoras set cDescripcionImpresora=? ," +
                    " cIp_Impresora=? ,iPuerto=?, id_tienda=?,id_area=? " +
                    " where id_company=? and id_impresora=? ");
            ps.setString(1, impresora.getNombreImpresora());
            ps.setString(2, impresora.getIP());
            ps.setInt(3, impresora.getPuerto());
            ps.setInt(4, impresora.getIdTienda());
            ps.setInt(5, impresora.getIdArea());
            ps.setInt(6, Constantes.Empresa.idEmpresa);
            ps.setInt(7, impresora.getIdImpresora());
            ps.execute();
            return 100;

        } catch (SQLException e) {
            e.printStackTrace();
            return 99;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }/*
    public byte EliminarImpresora(){


    }*/

    public List<mImpresora> ObtenerImpresoras() {

        List<mImpresora> lista = new ArrayList<>();

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(" select i.id_tienda,i.id_area," +
                    "isnull(i.cDescripcionImpresora,'')," +
                    "isnull(i.cIp_Impresora,''),i.iPuerto," +
                    " i.cTipoImpresora,isnull(i.id_impresora,''),a.c_descripcion_area  " +
                    "  from Impresoras as i inner join " +
                    " Areas_Produccion as a on i.id_area=a.id_area_produccion" +
                    " where i.id_company=? and i.cEliminado='' ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                mImpresora a = new mImpresora();

                a.setIdTienda(rs.getInt(1));
                a.setIdArea(rs.getInt(2));
                a.setNombreImpresora(rs.getString(3));
                a.setIP(rs.getString(4));
                a.setPuerto(rs.getInt(5));
                a.setIdImpresora(rs.getInt(7));
                a.setNombreArea(rs.getString(8));
                lista.add(a);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
            mImpresora a = new mImpresora();
            a.setIdImpresora(-99);

        }

        return lista;

    }

    public byte EliminarImpresora(int idImpresora) {

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("update impresoras set cEliminado='E' where id_company=? and id_impresora=? ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, idImpresora);
            ps.execute();
            return 100;

        } catch (SQLException e) {
            e.printStackTrace();
            return 99;
        } finally {

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public RespuestaExisVariante VeficarExistenciaVariante(int idProducto) {

        RespuestaExisVariante r = new RespuestaExisVariante();
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call  sp_verificar_mov_productos_variantes(?,?,?,?)");
            cs.setInt(1, idProducto);
            cs.setInt(2, Constantes.Empresa.idEmpresa);
            cs.registerOutParameter(3, Types.BIT);
            cs.registerOutParameter(4, Types.VARCHAR);
            cs.execute();

            r.setPermitir(cs.getBoolean(3));
            r.setMensaje(cs.getString(4));
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            r.setPermitir(false);
            r.setMensaje("Existe un problema al eliminar las variantes" +
                    ".Verifique su conexión a internet" +
                    ".Reinicie su aplicación");
        } finally {
            try {
                cs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return r;
    }

    public List<mProduct> ObtenerStockProductos() {

        List<mProduct> lista = new ArrayList<>();

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("select top(100)id_producto,CodProducto,DescCategoria," +
                    "NombreProducto,Descripcion_Variante,Cantidad from vstockTotalProductos where Id_Company=?");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                mProduct p = new mProduct();
                p.setIdProduct(rs.getInt(1));
                p.setcKey(rs.getString(2));
                p.setDescripcionCategoria(rs.getString(3));
                p.setcProductName(rs.getString(4));
                p.setDescripcionVariante(rs.getString(5));
                p.setdQuantity(rs.getFloat(6));
                lista.add(p);

            }


        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }


        return lista;

    }

    public List<mProduct> ObtenerStockProductosFiltroTexto(String texto) {

        List<mProduct> lista = new ArrayList<>();

        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("select top(100)id_producto,CodProducto,DescCategoria," +
                    "NombreProducto,Descripcion_Variante,Cantidad from vstockTotalProductos where Id_Company=?" +
                    " and CodigoBarra+''+codProducto+ DescCategoria+" +
                    "' '+NombreProducto+' '+VAR1+''+VAR2+''+VAR3 like '%'+?+'%' ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setString(2, texto);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                mProduct p = new mProduct();
                p.setIdProduct(rs.getInt(1));
                p.setcKey(rs.getString(2));
                p.setDescripcionCategoria(rs.getString(3));
                p.setcProductName(rs.getString(4));
                p.setDescripcionVariante(rs.getString(5));
                p.setdQuantity(rs.getFloat(6));
                lista.add(p);

            }


        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }
        return lista;

    }

    //indexado
    public List<AlmacenProducto> ObtenerCantidadPorAlmacen(int idProducto) {

        List<AlmacenProducto> lista = new ArrayList<>();


        PreparedStatement ps = null;


        try {
            ps = conn.prepareStatement(" select sa.iIdProduct,a.cDescripcion_Almacen," +
                    "a.iIdTiendaUbicacion,ncantidad " +
                    " from Stock_Almacen as sa inner join " +
                    " Almacenes as a  on sa.iId_Almacen=a.iId_Almacen " +
                    " where sa.cEstado_Registro='a' and a.cEliminado='' and a.iIdCompany=? and sa.iIdProduct=? " +
                    " order by a.iIdTiendaUbicacion");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, idProducto);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                AlmacenProducto a = new AlmacenProducto();
                a.getAlmacen().setDescripcionAlmacen(rs.getString(2));
                a.getAlmacen().setIdTienda(rs.getInt(3));
                a.getProducto().setdQuantity(rs.getFloat(4));
                lista.add(a);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }

        return lista;

    }

    public ResZonaServicio RegistrarZonaServicioPedido(mCabeceraPedido cabeceraPedido) {

        CallableStatement cs = null;
        ResZonaServicio r = new ResZonaServicio();
        byte respuesta = 0;
        try {
            cs = conn.prepareCall("call sp_registrar_zona_servicio_pedido_c_v3(?,?,?,?,?,?,?,?,?,?,?,?)");
            cs.setString(1, cabeceraPedido.getZonaServicio().getDescripcion());
            cs.setInt(2, Constantes.Empresa.idEmpresa);
            cs.setInt(3, Constantes.Tienda.idTienda);
            cs.setInt(4, Constantes.Terminal.idTerminal);
            cs.setInt(5, Constantes.Usuario.idUsuario);
            cs.setInt(6, cabeceraPedido.getIdCabecera());
            cs.registerOutParameter(7, Types.TINYINT);
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.registerOutParameter(10, Types.BOOLEAN);
            cs.registerOutParameter(11, Types.INTEGER);
            cs.registerOutParameter(12, Types.VARCHAR);
            cs.execute();

            respuesta = cs.getByte(7);
            r.setRespuesta(respuesta);
            r.setObservacion(cs.getString(9));
            r.getZonaServicio().setDescripcion(cabeceraPedido.getZonaServicio().getDescripcion());
            r.getZonaServicio().setIdZona(cs.getInt(8));
            r.setBTieneVentas(cs.getBoolean(10));
            r.getCliente().setcName(cs.getString(12));
            r.getCliente().setiId(cs.getInt(11));

        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = 99;
            r.setRespuesta(respuesta);
        }
        return r;
    }

    //INDEXADO
    public List<mOperario> ObtenerOperariosCompany() {

        List<mOperario> lista = new ArrayList<>();
        mOperario operario;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select cPrimerNombre,cSegundoNombre," +
                    "cApellidoPaterno,cApellidoMaterno,id_area_produccion,id_operario " +
                    "from operarios where id_company=? and cEstado='' order by cApellidoPaterno asc");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.execute();
            rs = ps.getResultSet();

            while (rs.next()) {

                operario = new mOperario();
                operario.setPrimerNombre(rs.getString(1));
                operario.setSegundoNombre(rs.getString(2));
                operario.setApellidoPaterno(rs.getString(3));
                operario.setApellidoMaterno(rs.getString(4));
                operario.setIdOperario(rs.getInt(6));
                lista.add(operario);

            }


        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    //indexado---> verificar
    public mCabeceraPedido ObtenerDatosPedidoCW(int idCabeceraPedido) {

        ResultSet rs;
        PreparedStatement ps = null;
        mCabeceraPedido cabeceraPedido = null;
        try {
            ps = conn.prepareStatement("select " +
                    "ISNULL(format(dHora_inicio,'hh:mm tt'),'NN')," +
                    "ISNULL(format(dHora_Final,'hh:mm tt'),'NN')," +
                    "isnull(zs.id_zona_servicio,0)," +
                    "ISNULL(zs.cDescripcion_Zona,'')," +
                    "isnull(zs.cColor,'')," +
                    "isnull(zs.idMarca,0)," +
                    "isnull(cMarca,'')," +
                    "isnull(zs.num_espacio,0)," +
                    "isnull(cp.cObservacion2,'' )," +
                    "isnull(format(cp.dHora_inicio,'dd-MM-yyyy'),'')," +
                    "isnull(format(cp.dHora_final,'dd-MM-yyyy'),'')," +
                    "isnull(zs.id_modelo,0) " +
                    " from cabecera_pedido as cp left join ZonaServicio as zs " +
                    " on cp.id_zona_servicio=zs.id_zona_servicio " +
                    " where " +
                    " cp.iId_Cabecera_pedido=? " +
                    " and cp.iIdCompany=? " +
                    " and cp.iIdTienda=? ");
            ps.setInt(1, idCabeceraPedido);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Tienda.idTienda);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {

                cabeceraPedido = new mCabeceraPedido();
                cabeceraPedido.setHoraInicio(rs.getString(1));
                cabeceraPedido.setHoraFinal(rs.getString(2));
                cabeceraPedido.setZonaServicio(new mZonaServicio());
                cabeceraPedido.getZonaServicio().setIdZona(rs.getInt(3));
                cabeceraPedido.getZonaServicio().setDescripcion(rs.getString(4));
                cabeceraPedido.getZonaServicio().setColor(rs.getString(5));
                cabeceraPedido.getZonaServicio().setIdMarca(rs.getInt(6));
                cabeceraPedido.getZonaServicio().setDescripcionMarca(rs.getString(7));
                cabeceraPedido.getZonaServicio().setNumEspacios(rs.getInt(8));
                cabeceraPedido.setObservacionPedido(rs.getString(9));
                cabeceraPedido.setFechaInicial(rs.getString(10));
                cabeceraPedido.setFechaFinal(rs.getString(11));
                cabeceraPedido.getZonaServicio().setIdModelo(rs.getInt(12));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            cabeceraPedido = new mCabeceraPedido();
        }
        return cabeceraPedido;
    }

    public List<mMarca> ObtenerMarcasAutos() {

        List<mMarca> lista = new ArrayList<>();
        mMarca marca;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select id_marca,cDescripcion_Marca " +
                    "from Marcas where cEliminado='' order by cDescripcion_Marca");
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {

                marca = new mMarca();
                marca.setIdMarca(rs.getInt(1));
                marca.setDescripcion(rs.getString(2));
                lista.add(marca);

            }


        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }

        return lista;
    }

    public List<mOperario> ObtenerOperariosPedido(int idCabeceraPedido) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<mOperario> lista = new ArrayList<>();
        mOperario operario;
        try {
            ps = conn.prepareStatement("select " +
                    "o.id_operario,o.cPrimerNombre," +
                    "o.cSegundoNombre,O.cApellidoPaterno," +
                    "o.cApellidoMaterno " +
                    "from Pedido_Operario as po " +
                    "inner join Operarios as o on  " +
                    "po.id_operario=o.id_operario " +
                    "where po.cEstado='' " +
                    "and po.id_cabecera_pedido=? " +
                    "and po.id_company=? ");
            ps.setInt(1, idCabeceraPedido);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                operario = new mOperario();
                operario.setIdOperario(rs.getInt(1));
                operario.setPrimerNombre(rs.getString(2));
                operario.setSegundoNombre(rs.getString(3));
                operario.setApellidoPaterno(rs.getString(4));
                operario.setApellidoMaterno(rs.getString(5));
                lista.add(operario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }
        return lista;
    }

    public Tiempo ObtenerHoraActual() {
        Tiempo t = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select FORMAT(getUTCdate()-'5:00','dd-MM-yyyy'),FORMAT(getUTCdate()-'5:00','hh:mm tt'),getutcDate()-'5:00'");
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                t = new Tiempo();
                t.setFecha(rs.getString(1));
                t.setHora(rs.getString(2));
                t.setTiempo(rs.getTime(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            t = new Tiempo();

        }

        return t;
    }

    public EstadoEntrega ActualizarEstadoEntrega(int idCabeceraPedido) {

        boolean r = false;
        try {
            CallableStatement cs = conn.prepareCall("call sp_actualiza_estado_entrega(" + ParamStoreProcedure(5) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idCabeceraPedido);
            cs.registerOutParameter(4, Types.BOOLEAN);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();
            r = cs.getBoolean(4);
            cs.close();
            return new EstadoEntrega(r, "");
        } catch (Exception e) {

            return new EstadoEntrega(false, "");

        }

    }

    public byte GuardarDetallePedido(mCabeceraPedido pedido) {
        byte respuesta = 100;
        CallableStatement cs = null;

        try {
            cs = conn.prepareCall("call sp_guardar_detalle_pedido_cw(?,?,?,?,?,?,?,?,?,?,?,?)");
            cs.setInt(1, pedido.getIdCabecera());
            cs.setInt(2, Constantes.Empresa.idEmpresa);
            cs.setInt(3, Constantes.Tienda.idTienda);
            cs.setInt(4, Constantes.Usuario.idUsuario);
            cs.setInt(5, Constantes.Terminal.idTerminal);
            cs.setString(6, pedido.getObservacionPedido());
            cs.setString(7, pedido.getHoraInicio());
            cs.setString(8, pedido.getHoraFinal());
            if (pedido.getOperarios().size() > 0) {
                cs.setInt(9, pedido.getOperarios().get(0).getIdOperario());
            } else {
                cs.setInt(9, 0);
            }
            cs.setInt(10, pedido.getZonaServicio().getIdZona());

            cs.setInt(11, pedido.getZonaServicio().getIdMarca());
            cs.setString(12, pedido.getZonaServicio().getColor());
            cs.execute();

            cs.clearParameters();
            cs.close();
            respuesta = 100;

        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = 99;
        }
        return respuesta;
    }

    public List<mCabeceraPedido> ObtenerPedidosTiendaV2(String fi, String ff, String cIdentificador) {
        fi = fi + " 00:00:00";
        ff = ff + " 23:59:59";
        List<mCabeceraPedido> lista = new ArrayList<>();
        mCabeceraPedido c;

        CallableStatement cs = null;

        try {
            cs = conn.prepareCall("call sp_obtener_pedidos_tienda(" + ParamStoreProcedure(6) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setString(3, fi);
            cs.setString(4, ff);
            cs.setString(5, cIdentificador);
            cs.setInt(6, Constantes.ConfigTienda.idTipoZonaServicio);
            cs.execute();

            ResultSet rs = cs.getResultSet();


            if (rs != null) {

                while (rs.next()) {
                    c = new mCabeceraPedido();
                    c.setIdCabecera(rs.getInt(1));
                    c.setIdentificadorPedido(rs.getString(2));
                    c.getCliente().setcName(rs.getString(3));
                    c.getVendedor().setPrimerNombre(rs.getString(4));
                    c.setFechaReserva(rs.getString(5));
                    c.setTotalNeto(rs.getBigDecimal(6));
                    c.setbEstadoPagado(rs.getBoolean(7));
                    c.setObservacionPedido(rs.getString(8));
                    c.setObservacionReserva(rs.getString(9));
                    lista.add(c);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();

        }


        return lista;
    }

    @Deprecated
    public List<mCabeceraPedido> ObtenerPedidosTienda(String fi, String ff, String cIdentificador) {
        fi = fi + " 00:00:00";
        ff = ff + " 23:59:59";
        List<mCabeceraPedido> lista = new ArrayList<>();
        mCabeceraPedido c;
        String select = "select id_pedido,cIdentificador,cNombreCliente,cNombreVendedor, " +
                "format(dFechaPedido-'5:00','dd/MM/yyyy hh:mm tt')" +
                ",dTotal_Pedido,bEstadoPagado,dbo.DescripcionZonaServicioOperarioV2 " +
                "(id_zona_servicio,id_company, " +
                "id_tienda,?,id_pedido) " +
                "from vpedidoscompany " +
                " where id_company=? and id_tienda=? and dFechaPedido-'5:00' " +
                " between ? and ?  ";
        String where = " and cIdentificador+' '+cNombreCliente like '%'+?+'%' ";

        if (cIdentificador.length() > 0) {
            select = select + where;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(select + " order by dFechaPedido desc");
            ps.setInt(1, Constantes.ConfigTienda.idTipoZonaServicio);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Tienda.idTienda);
            ps.setString(4, fi);
            ps.setString(5, ff);
            if (cIdentificador.length() > 0) {
                ps.setString(6, cIdentificador);
            }
            ps.execute();
            rs = ps.getResultSet();
            while (rs.next()) {
                c = new mCabeceraPedido();
                c.setIdCabecera(rs.getInt(1));
                c.setIdentificadorPedido(rs.getString(2));
                c.getCliente().setcName(rs.getString(3));
                c.getVendedor().setPrimerNombre(rs.getString(4));
                c.setFechaReserva(rs.getString(5));
                c.setTotalNeto(rs.getBigDecimal(6));
                c.setbEstadoPagado(rs.getBoolean(7));
                c.setObservacionPedido(rs.getString(8));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();

        }

        return lista;

    }

    public List<ProductoEnVenta> obtenerUltimosPedidoZonaServicio(int idZonaServicio) {

        List<ProductoEnVenta> lista = new ArrayList<>();

        PreparedStatement ps = null;


        try {
            ps = conn.prepareStatement("SELECT TOP (5) Product.iIdProduct, " +
                    "Cabecera_Pedido.iId_Cabecera_Pedido, Cabecera_Venta.iId_Cabecera_Venta," +
                    " LTRIM(ISNULL(Categoria_Productos.cDescripcion_categoria, '') " +
                    " + ' ' + ISNULL(ltrim(SubCategorias.c_Descripcion_SubCategoria), '')" +
                    " + ' ' + ISNULL(Detalle_Venta.cProductName, '') " +
                    " + ' ' + ISNULL(Detalle_Venta.cDescripcion_Variante, '')) , Detalle_Venta.idDetalle_Venta, " +
                    " format(Cabecera_Venta.FechaVentaRealizada,'dd/MM/yyyy hh:mm tt'),Cabecera_Venta.iId_Company, " +
                    "Cabecera_Venta.iId_Tienda, Cabecera_Pedido.id_zona_servicio,Detalle_Venta.iCantidad " +
                    "FROM SubCategorias RIGHT OUTER JOIN " +
                    "  Cabecera_Pedido INNER JOIN " +
                    " Cabecera_Venta ON Cabecera_Pedido.iId_Cabecera_Pedido = Cabecera_Venta.iId_Cabecera_Pedido INNER JOIN" +
                    " Detalle_Venta ON Cabecera_Venta.iId_Cabecera_Venta = Detalle_Venta.iIdCabecera_Venta INNER JOIN " +
                    "  Product ON Detalle_Venta.iIdProducto = Product.iIdProduct LEFT OUTER JOIN " +
                    "  Categoria_Productos ON Product.id_Categoria =" +
                    " Categoria_Productos.id_categoria_producto " +
                    " AND Product.iIdCompany = Categoria_Productos.id_company ON  " +
                    " SubCategorias.id_subcategoria = Product.id_Subcategoria " +
                    " where  cabecera_pedido.iIdCompany=? and " +
                    " cabecera_pedido.iIdTienda=? and Cabecera_pedido.id_zona_servicio=? " +
                    " and cabecera_venta.iId_Company=?  and  cabecera_venta.iId_tienda=? " +
                    " cabecera_venta.cEstadovENTA='N' and cabecera_venta.cEliminado=''" +
                    "ORDER BY dbo.Cabecera_Venta.FechaVentaRealizada DESC");

            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, idZonaServicio);
            ps.setInt(4, Constantes.Empresa.idEmpresa);
            ps.setInt(5, Constantes.Tienda.idTienda);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                ProductoEnVenta p = new ProductoEnVenta();
                p.setIdProducto(rs.getInt(1));
                p.setIdCabeceraPedido(rs.getInt(2));
                p.setProductName(rs.getString(4));
                p.setFecha(rs.getString(6));
                p.setCantidad(rs.getFloat(10));
                lista.add(p);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }


        return lista;
    }

    public List<mCabeceraVenta> UltimasVentasZonaServicio(int idZonaServicio, int ultimos) {

        PreparedStatement ps = null;
        List<mCabeceraVenta> lista = new ArrayList<>();
        try {
            ps = conn.prepareStatement("select top(" + String.valueOf(ultimos) + ")cp.id_zona_servicio,format" +
                    "(cabecera_venta.FechaVentaRealizada-'5:00','dd/MM/yyyy hh:mm tt'), " +
                    "( " +
                    "select  ' " + Constantes.Etiquetas.SaltoLinea
                    + " '+format(dv.iCantidad,'N', 'en-us')+' '+rtrim(ltrim(isnull(cp.cDescripcion_Categoria,'') \n" +
                    "        +'/ '+rtrim(ltrim(isnull(sc.c_Descripcion_SubCategoria,'')))+'/ '+cDescripcion_Combo+ ' '+dv.cProductName +' '\n" +
                    "        +dv.cDescripcion_Variante))   from detalle_venta as dv inner join Cabecera_Venta as cv  on\n" +
                    "         cv.iId_Cabecera_venta=dv.iIdCabecera_Venta inner join Product AS p on p.iIdCompany=dv.id_Company and p.cEliminado='' \n" +
                    "         and p.iIdProduct=dv.iIdProducto left join Categoria_Productos as cp on p.iIdCompany=cp.id_company and\n" +
                    "          p.id_categoria=cp.id_categoria_producto left join SubCategorias as sc  on p.id_subcategoria=sc.id_subcategoria \n" +
                    "          and p.iIdCompany=sc.id_company  where dv.iIdCabecera_Venta=Cabecera_Venta.iId_Cabecera_Venta for xml path('') ) as\n" +
                    "           Productos ,Cabecera_Venta.dTotal_Neto_Venta  from Cabecera_Venta inner join Cabecera_Pedido as cp on\n" +
                    "            Cabecera_Venta.iId_Cabecera_Pedido=cp.iId_Cabecera_Pedido where Cabecera_Venta.iId_Company=? and \n" +
                    "            Cabecera_Venta.iId_Tienda=? and cabecera_venta.cEstadoVenta='N'  and Cabecera_Venta.cEliminado='' \n" +
                    "            and cp.iIdCompany=? and cp.iIdTienda=? and cp.id_zona_servicio=? order by cabecera_venta.FechaVentaRealizada desc  ");

            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Tienda.idTienda);
            ps.setInt(5, idZonaServicio);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                mCabeceraVenta c = new mCabeceraVenta();
                c.setFechaVenta(rs.getString(2));
                c.setDescripcionVenta(rs.getString(3));
                c.setTotalPagado(rs.getBigDecimal(4));
                lista.add(c);
            }
            rs.close();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    public mZonaServicio DatosZonaServicio(int idZona) {

        mZonaServicio z = new mZonaServicio();

        PreparedStatement ps = null;


        try {
            ps = conn.prepareStatement("select id_zona_servicio,cDescripcion_Zona," +
                    "idMarca,id_modelo," +
                    "cColor from zonaservicio where " +
                    "id_company=? and id_zona_servicio=?");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, idZona);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                z.setIdZona(rs.getInt(1));
                z.setDescripcion(rs.getString(2));
                z.setIdMarca(rs.getInt(3));
                z.setIdModelo(rs.getInt(4));
                z.setColor(rs.getString(5));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return z;
    }

    public List<cModelo> ObtenerModelosCW() {

        PreparedStatement ps = null;
        cModelo m;
        List<cModelo> lista = new ArrayList<>();

        try {
            ps = conn.prepareStatement("select id_modelo,cDescripcion,cTipoMarca" +
                    " from modelo where cEliminado='' and cTipoMarca=? order by cDescripcion ");
            ps.setString(1, Constantes.Tienda.cTipoZonaServicio);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                m = new cModelo();
                m.setIdModelo(rs.getInt(1));
                m.setCDescripcion(rs.getString(2));
                m.setCTipo(rs.getString(3));
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }

        return lista;
    }

    public byte ActualizarZonaServicioCw(mZonaServicio zonaServicio) {

        PreparedStatement ps = null;
        byte respuesta = 100;
        try {
            ps = conn.prepareStatement("update zonaservicio set cColor=? ,id_modelo=?, " +
                    "idmarca=? where id_company=? and id_zona_servicio=?");
            ps.setString(1, zonaServicio.getColor());
            ps.setInt(2, zonaServicio.getIdModelo());
            ps.setInt(3, zonaServicio.getIdMarca());
            ps.setInt(4, Constantes.Empresa.idEmpresa);
            ps.setInt(5, zonaServicio.getIdZona());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = 99;
        }

        return respuesta;
    }

    public List<mZonaServicio> ObtenerZonasServiciosM() {


        List<mZonaServicio> lista = null;
        lista = new ArrayList<>();
        PreparedStatement ps = null;
        mZonaServicio s;
        try {
            ps = conn.prepareStatement("SELECT zs.id_zona_servicio,zs.cDescripcion_Zona,(select count(iId_Cabecera_Pedido)\n" +
                    " from Cabecera_Pedido  as cp  where cp.iIdCompany=? and cp.iIdTienda=?\n" +
                    "and cp.id_zona_servicio=zs.id_zona_servicio and cp.cEstadoPermanencia!='S' ),zs.bZonaLibre\n" +
                    " FROM ZonaServicio AS ZS where zs.id_company=? and zs.id_tienda=? and zs.cEstado=''\n" +
                    "  order by zs.cDescripcion_Zona");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Tienda.idTienda);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                s = new mZonaServicio();
                s.setIdZona(rs.getInt(1));
                s.setDescripcion(rs.getString(2));
                s.setNumReservas(rs.getInt(3));
                s.setBZonaLibre(rs.getBoolean(4));
                lista.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        }

        return lista;
    }

    public ResZonaServicio GuardarSeleccionZonaServicio(mZonaServicio zonaServicio, int idCabeceraPedido) {

        CallableStatement cs = null;
        ResZonaServicio r = new ResZonaServicio();

        try {
            cs = conn.prepareCall("call sp_ingresar_zona_servicio_pedido(?,?,?,?,?,?)");
            cs.setInt(1, idCabeceraPedido);
            cs.setInt(2, zonaServicio.getIdZona());
            cs.setInt(3, Constantes.Empresa.idEmpresa);
            cs.setInt(4, Constantes.Tienda.idTienda);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.registerOutParameter(6, Types.BOOLEAN);
            cs.execute();


        } catch (SQLException e) {
            e.printStackTrace();
            r = new ResZonaServicio();
            r.getZonaServicio().setIdZona(-99);

        }

        return r;
    }

    public byte EliminarZonaServicioPedido(int idCabeceraPedido) {


        PreparedStatement ps = null;
        byte respuesta = 100;

        try {
            ps = conn.prepareStatement("update Cabecera_Pedido set id_zona_servicio=0,cIdentificador_Pedido='' " +
                    " where iIdCompany=? and iIdTienda=? and iId_Cabecera_Pedido=?" +
                    "");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, idCabeceraPedido);
            ps.execute();
            respuesta = 100;
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = 99;
        }

        return respuesta;
    }

    public ResultProcessData<ProductoEnVenta> GuardarProductoTiempoPedido(mProduct p, String horaInicial, int idCabeceraPedido) {


        CallableStatement cs = null;
        ProductoEnVenta productoEnVenta;
        String mensaje = "";
        int code = 0;

        try {
            cs = conn.prepareCall("call sp_guardar_producto_tiempo_pedido_v3(" + ParamStoreProcedure(18) + ")");
            cs.setInt(1, p.getIdProduct());
            cs.setInt(2, idCabeceraPedido);
            cs.setInt(3, Constantes.Empresa.idEmpresa);
            cs.setInt(4, Constantes.Tienda.idTienda);
            cs.setInt(5, Constantes.Terminal.idTerminal);
            cs.setInt(6, Constantes.Usuario.idUsuario);
            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, Types.FLOAT);
            cs.registerOutParameter(10, Types.VARCHAR);
            cs.registerOutParameter(11, Types.VARCHAR);
            cs.setString(11, horaInicial);
            cs.registerOutParameter(12, Types.BOOLEAN);
            cs.registerOutParameter(13, Types.BOOLEAN);
            cs.registerOutParameter(14, Types.DECIMAL);
            cs.registerOutParameter(15, Types.DECIMAL);

            cs.setBoolean(16, p.isProductoUnico());
            cs.registerOutParameter(17, Types.INTEGER);
            cs.registerOutParameter(18, Types.VARCHAR);
            cs.execute();
            productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setItemNum(cs.getInt(7));
            productoEnVenta.setIdDetallePedido(cs.getInt(8));
            productoEnVenta.setCantidad(cs.getFloat(9));
            productoEnVenta.setProductName(cs.getString(10));
            productoEnVenta.setHoraInicio(cs.getString(11));
            productoEnVenta.setControlTiempo(cs.getBoolean(12));
            productoEnVenta.setPrecioOriginal(cs.getBigDecimal(14));
            productoEnVenta.setPrecioVentaFinal(cs.getBigDecimal(15));

            code = cs.getInt(17);
            mensaje = cs.getString(18);

            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setIdProducto(-99);
        }

        return new ResultProcessData<>(code, mensaje, productoEnVenta);
    }

    public ProductoEnVenta EditarProductoTiempoPedido(ProductoEnVenta p, int idCabeceraPedido, String horaFinal) {
        ProductoEnVenta a = new ProductoEnVenta();
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call sp_editar_producto_control_tiempo_pedido(?,?,?,?,?,?,?,?,?    )");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idCabeceraPedido);
            cs.setInt(4, p.getIdDetallePedido());
            cs.setInt(5, Constantes.Terminal.idTerminal);
            cs.setInt(6, Constantes.Usuario.idUsuario);
            cs.setString(7, horaFinal);
            cs.registerOutParameter(8, Types.FLOAT);
            cs.registerOutParameter(9, Types.DECIMAL);
            cs.execute();
            a.setCantidad(cs.getFloat(8));
            a.setIdDetallePedido(p.getIdDetallePedido());
            a.setPrecioOriginal(p.getPrecioOriginal());
            a.setPrecioOriginal(cs.getBigDecimal(9));
            a.setHoraFinal(horaFinal);
        } catch (SQLException e) {
            e.printStackTrace();
            a = new ProductoEnVenta();
            a.setIdDetallePedido(-99);
        }
        return a;
    }

    public ProductoEnVenta ObtenerFechaFinalProducto(String fechaInit) {


        CallableStatement cs = null;
        ProductoEnVenta p = new ProductoEnVenta();

        try {


            cs = conn.prepareCall("call sp_obtener_fechaFinal_horas(?,?,?,?,?)");
            cs.setString(1, fechaInit);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.setInt(4, Constantes.Empresa.idEmpresa);
            cs.setInt(5, Constantes.Tienda.idTienda);
            cs.execute();

            p.setHoraFinal(cs.getString(2));
            p.setCantidad(cs.getFloat(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public List<CategoriaPack> GetCategoriasPrecio(int id) {

        List<CategoriaPack> list = new ArrayList<>();
        CallableStatement cs = null;
        PreparedStatement ps = null;

        try {
            cs = conn.prepareCall("call sp_obtener_precios_categorias_pack(?,?)");
            cs.setInt(1, id);
            cs.setInt(2, Constantes.Empresa.idEmpresa);
            cs.execute();

            ps = conn.prepareStatement(" select pcp.id_config_precio,cp.cDescripcion_categoria,pcp.dPrecio_Categoria " +
                    " from pack_configuracion_precio as pcp inner join  categoria_productos as cp  " +
                    " on pcp.id_categoria=cp.id_categoria_producto where pcp.id_company=? and  pcp.id_product_padre=? and pcp.cEstado_Eliminado=''");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, id);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                CategoriaPack c = new CategoriaPack();

                c.setIdCategoriaPack(rs.getInt(1));
                c.setDescripcionCategoria(rs.getString(2));
                c.setPrecio(rs.getBigDecimal(3));
                list.add(c);
            }

        } catch (Exception e) {

            e.toString();
            list = new ArrayList<>();
        }


        return list;
    }

    public void FiltrosClientes() {
        List<Control1Cliente> list1 = new ArrayList();
        List<Control2Cliente> list2 = new ArrayList();
        Control1Cliente c1 = new Control1Cliente();
        c1.setIdControl(0);
        c1.setDescripcionControl("Todos");
        c1.AddControl2List();
        list1.add(c1);
        try {
            PreparedStatement ps = this.conn.prepareStatement("  \n\n select a.tipo,a.id_Control,a.idPadre,a.Descripcion from (select 1 as tipo,id_control1 as id_Control,0 \n             " +
                    "        as idPadre,cDescripcionControl1 as Descripcion,iNumItem  as num\n                   from control1cliente where id_company=?  \n   union all \n  " +
                    "  select 2 as tipo,id_control2 as id_Control,id_control1 as idPadre,\n           " +
                    "          cDescripcionControl2 as Descripcion ,iNumItem as num\n                  " +
                    "   from control2cliente where id_company=?) as a\n\t\t\t\t\t order by a.tipo,a.num ");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    c1 = new Control1Cliente();
                    c1.setIdControl(rs.getInt(2));
                    c1.setDescripcionControl(rs.getString(4));
                    c1.AddControl2List();
                    list1.add(c1);
                } else if (rs.getInt(1) == 2) {
                    Control2Cliente c2 = new Control2Cliente();
                    c2.setIdControl2Cliente(rs.getInt(2));
                    c2.setDescripcicionControl2Cliente(rs.getString(4));
                    c2.setIdControl1Cliente(rs.getInt(3));
                    list2.add(c2);
                }
            }
            for (int i = 0; i < list1.size(); i++) {
                for (int j = 0; j < list2.size(); j++) {
                    if (((Control1Cliente) list1.get(i)).getIdControl() == ((Control2Cliente) list2.get(j)).getIdControl1Cliente()) {
                        ((Control1Cliente) list1.get(i)).getListaControl2().add(list2.get(j));
                    }
                }
            }
            Constantes.ControlCliente.control1Clientes = list1;
            Constantes.ControlCliente.control2Clientes = list2;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int GuardarPrecioCategoria(List<CategoriaPack> list, int idPack) {
        try {
            PreparedStatement ps = this.conn.prepareStatement(
                    "update pack_configuracion_precio set dPrecio_Categoria=?  where id_config_precio=? and id_company=? ");
            for (CategoriaPack pack : list) {
                ps.setBigDecimal(1, pack.getPrecio());
                ps.setInt(2, pack.getIdCategoriaPack());
                ps.setInt(3, Constantes.Empresa.idEmpresa);
                ps.addBatch();
            }
            ps.executeBatch();
            return 100;
        } catch (SQLException e) {
            e.printStackTrace();
            return 99;
        }
    }

    public List<ProductoEnVenta> ObtenerDetalleNota(int idCabeceraNota) {
        List<ProductoEnVenta> list = new ArrayList();
        try {
            PreparedStatement ps = this.conn.prepareStatement("\nselect dn.cDescripcion_Unidad_Sunat," +
                    "p.cKey, ltrim(case cp.cDescripcion_Categoria when 'General' th" +
                    "en ''   else cp.cDescripcion_Categoria end +' '+dn.cProductName), " +
                    "dn.iCantidad,dn.dPrecio_Costo,dn.dValor_Unitario\n,dn.dPrecioUnitario," +
                    "dn.dDescuento_NoIgv, dn.dDescuento_Igv,dn.d_Igv,dn.dPrecio_Neto," +
                    "dn.dPrecio_Total,p.cCodigoSunat  from DetalleNota as dn left join product as p" +
                    "\n on dn.idProduct=p.iIdProduct\n left join categoria_productos as cp on " +
                    " p.id_categoria=cp.id_categoria_producto\nwhere idCompany=? and idTienda=? and idCabeceraNota=? and bDetallePack=0\n\n");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.setInt(3, idCabeceraNota);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                ProductoEnVenta p = new ProductoEnVenta();
                p.setCodUnidSunat(rs.getString(1));
                p.setIdProducto(rs.getInt(2));
                p.setProductName(rs.getString(3));
                p.setCantidad((float) rs.getInt(4));
                p.setValorUnitario(rs.getBigDecimal(6));
                p.setPrecioOriginal(rs.getBigDecimal(7));
                p.setMontoDescuento(rs.getBigDecimal(8));
                p.setMontoIgv(rs.getBigDecimal(10));
                p.setPrecioNeto(rs.getBigDecimal(11));
                p.setPrecioVentaFinal(rs.getBigDecimal(12));
                p.setCodigoSunat(rs.getString(13));
                list.add(p);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public EntregaPedidoInfo GetDatoEntregaPedido(int idPedido) {

        EntregaPedidoInfo EntregaPedidoInfo = new EntregaPedidoInfo();
        try {
            CallableStatement cs = conn.prepareCall("call p_api_get_cabecera_pedido_con_datos_entrega_app(" + ParamStoreProcedure(3) + ")");
            cs.setInt("@pidCompany", Constantes.Empresa.idEmpresa);
            cs.setInt("@pidTienda", Constantes.Tienda.idTienda);
            cs.setInt("@pidPedido", idPedido);
            cs.execute();

            ResultSet rs = cs.getResultSet();
            while (rs.next()) {
                EntregaPedidoInfo.setCNumeroPedido(rs.getString("cNumPedido"));
                EntregaPedidoInfo.setCFechaCreacion(rs.getString("FechaCracion"));
                mCustomer clienteEntrega = new mCustomer();
                clienteEntrega.setcName(rs.getString("cNombre_Cliente"));
                clienteEntrega.setcApellidoPaterno(rs.getString("cApellidos_cliente"));
                clienteEntrega.setcNumberPhone(rs.getString("ctelefono"));
                clienteEntrega.setcEmail(rs.getString("cemail_cliente"));
                EntregaPedidoInfo.setClienteEntrega(clienteEntrega);
                MedioPagoEntrega medioEntrega = new MedioPagoEntrega();
                medioEntrega.setCCodeMedioPago(rs.getInt("iid_Metodo_Pago_Entrega"));
                medioEntrega.setCDescripcionMedioPago(rs.getString("cDescrMetodoPago"));
                EntregaPedidoInfo.setMedioPagoEntrega(medioEntrega);
                InfoAdicionalEntregaPedido InfoAdicionalEntregaPedido = new InfoAdicionalEntregaPedido();
                InfoAdicionalEntregaPedido.setCComentario(rs.getString("cComentario"));
                EntregaPedidoInfo.setInfoAdicionalEntregaPedido(InfoAdicionalEntregaPedido);


                TiempoEntregaPedido TiempoEntregaPedido = new TiempoEntregaPedido();
                TiempoEntregaPedido.setCDescripcionTipoHora(rs.getString("cDescrTiempoEntrega"));
                TiempoEntregaPedido.setCFechaEntrega(rs.getString("cDiaEntrega"));
                TiempoEntregaPedido.setCHoraEntrega(rs.getString("cHoraEntrega"));
                TiempoEntregaPedido.setIdTiempoEntrega(rs.getInt("iId_Tiempo_Entrega"));
                TiempoEntregaPedido.setCDescripcionEntrega(rs.getString("cDescripcionEntrega"));
                EntregaPedidoInfo.setTiempoEntregaPedido(TiempoEntregaPedido);


                TipoEntregaPedido TipoEntregaPedido = new TipoEntregaPedido();
                TipoEntregaPedido.setCodeTipoEntrega(rs.getString("iId_Forma_Entrega_pedido"));
                TipoEntregaPedido.setCalleNumero(rs.getString("cEntrega_Calle"));
                TipoEntregaPedido.setCiudadLocalidad(rs.getString("cEntrega_Ciudad_Localidad"));
                TipoEntregaPedido.setReferencia(rs.getString("cEntrega_Referencia"));
                TipoEntregaPedido.setDescripcionTipoEntrega(rs.getString("cDescrFormaEntrega"));
                EntregaPedidoInfo.setTipoEntregaPedido(TipoEntregaPedido);


            }
        } catch (Exception e) {
            EntregaPedidoInfo = null;
        }

        return EntregaPedidoInfo;
    }

    public String FechaServer() {
        String r = "";
        try {
            PreparedStatement ps = conn.prepareStatement("select  FORMAT(GETUTCDATE()-'5:00','yyyy-MM-dd HH:mm:ss') as fecha");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                r = rs.getString("fecha");
            }
        } catch (Exception e) {
            return r;
        }
        return r;
    }
    ///////////**///////////////
    ///////////***//////////////
    ///////////****/////////////
    ///////////*****////////////
    ///////////******///////////
    ///////////**//***//////////
    ///////////**///////////////
    ///////////**///////////////
    ///////////**///////////////
    ///////////**///////////////


    /////////VERIFICAR FUNCION RECUPERADA////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////
    /////////////////////
    //
    //
    //
    // /
    ////////////////////////////
    ////////////////////////////
    ////////////////////////////
    ////////////////////////////
    ////////////////////////////
    ////////////////////////////
    ////////////////////////////
    ////////////////////////////
    ////////////////////////////
    public mProduct ConfigProduct(int idProduct) {
        mProduct p = null;
        try {
            PreparedStatement ps = this.conn.prepareStatement("select bComboSimple,dSalesPrice,cProductName from product where iIdProduct=?");
            ps.setInt(1, idProduct);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                p = new mProduct();
                p.setComboSimple(rs.getBoolean(1));
                p.setPrecioVenta(rs.getBigDecimal(2));
                p.setcProductName(rs.getString(3));
            }
            return p;
        } catch (SQLException e) {
            e.printStackTrace();
            return new mProduct();
        }
    }

    public boolean GuardaAccionFlujoPagoPedido(int idAccionFlujo, int idEstadoEntrega, String fechaAccion, int idPedido) {

        boolean exito = false;
        try {
            CallableStatement cs = conn.prepareCall("call p_api_Accion_pago_flujo(" + ParamStoreProcedure(5) + ")");
            cs.setInt("@paccion_flujo_entrega", idAccionFlujo);
            cs.setInt("@iId_estado_entrega_pedido", idEstadoEntrega);
            cs.setInt("@iId_Cabecera_Pedido", idPedido);
            cs.setString("@cusuario", "WEB");
            cs.setString("@dfecha_accion", fechaAccion);
            cs.execute();
            exito = true;

        } catch (Exception e) {

            exito = false;
        }

        return exito;
    }

    //PRUEBA API
    public List<EstadoPagoEnPedido> GetEstadoFlujoPagoPedido(int idPedido) {
        List<EstadoPagoEnPedido> list = new ArrayList<>();
        try {
            CallableStatement cs = conn.prepareCall("call p_api_estado_pedido_pago_app(" + ParamStoreProcedure(3) + ")");
            cs.setInt("@p_iIdCompany", Constantes.Empresa.idEmpresa);
            cs.setInt("@p_id_tienda", Constantes.Tienda.idTienda);
            cs.setInt("@iid_Cabecera_pedido", idPedido);
            ResultSet rs = cs.executeQuery();


            while (rs.next()) {

                EstadoPagoEnPedido estadoPagoEnPedido = new EstadoPagoEnPedido();
                estadoPagoEnPedido.setCDescripcionEstadoPago(rs.getString("cDescripcion"));
                estadoPagoEnPedido.setBMarcado(rs.getBoolean("iProcesado"));
                estadoPagoEnPedido.setIdEstadoPago(rs.getInt("iId_estado_pago_pedido"));
                list.add(estadoPagoEnPedido);
            }

        } catch (Exception e) {
            list = new ArrayList<>();
        }

        return list;
    }


    public byte ActualizarEstadoVentaRapida(int idProducto, boolean estado) {
        try {
            PreparedStatement ps = this.conn.prepareStatement("update product set bComboSimple=? where iIdProduct=? ");
            ps.setBoolean(1, estado);
            ps.setInt(2, idProducto);
            ps.execute();
            return (byte) 100;
        } catch (SQLException e) {
            e.printStackTrace();
            return Constantes.EstadosAppLoguin.EstadoErrorProc;
        }
    }

    public int VerificaConexionBd() {

        int code = 0;
        try {

            CallableStatement cs = conn.prepareCall("call sp_valid_connection(?)");
            cs.setQueryTimeout(6);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();
            code = cs.getInt(1);

        } catch (SQLTimeoutException timeEx) {
            code = 55;
        } catch (SQLException sqle) {
            code = 100;
        } catch (Exception e) {

            code = 99;

        }


        return code;
    }

    public boolean GuardaAccionFlujoPedido(int idAccionFlujo, int idEstadoEntrega, String fechaAccion, int idPedido, String mensaje, boolean marcado) {

        boolean exito = false;
        try {
            CallableStatement cs = conn.prepareCall("call p_api_Accion_flujo_v2(" + ParamStoreProcedure(7) + ")");
            cs.setInt("@paccion_flujo_entrega", idAccionFlujo);
            cs.setInt("@iId_estado_entrega_pedido", idEstadoEntrega);
            cs.setInt("@iId_Cabecera_Pedido", idPedido);
            cs.setString("@cusuario", "WEB");
            cs.setString("@dfecha_accion", fechaAccion);
            cs.setString("@tComentario", mensaje);
            cs.setBoolean("@bMarca", marcado);
            cs.execute();
            exito = true;

        } catch (Exception e) {

            exito = false;
        }

        return exito;
    }

    private List<mTipoAtencion> obtenerTiposAtencion() {
        List<mTipoAtencion> list = new ArrayList();
        try {
            PreparedStatement ps = this.conn.prepareStatement("select id_Tipo_Atencion,cDescripcion from tipo_atencion where cEstado_Eliminado=''");
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                mTipoAtencion a = new mTipoAtencion();
                a.setIdTipoAtencion(rs.getInt(1));
                a.setDescripcion(rs.getString(2));
                list.add(a);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<EstadoEntregaPedidoEnUso> GetEstadoFlujoPedido(int idPedido) {
        List<EstadoEntregaPedidoEnUso> list = new ArrayList<>();
        try {
            CallableStatement cs = conn.prepareCall("call p_api_estado_pedido_app(" + ParamStoreProcedure(3) + ")");
            cs.setInt("@p_iIdCompany", Constantes.Empresa.idEmpresa);
            cs.setInt("@p_id_tienda", Constantes.Tienda.idTienda);
            cs.setInt("@iid_Cabecera_pedido", idPedido);
            ResultSet rs = cs.executeQuery();


            while (rs.next()) {

                EstadoEntregaPedidoEnUso estadoEntregaPedidoEnUso = new EstadoEntregaPedidoEnUso();
                estadoEntregaPedidoEnUso.setCDescripcionEstado(rs.getString("cDescripcion"));
                estadoEntregaPedidoEnUso.setCDescripcionAdicionalEstado(rs.getString("tComentario"));
                estadoEntregaPedidoEnUso.setBMarcado(rs.getBoolean("iProcesado"));
                estadoEntregaPedidoEnUso.setIdEstadoEntrega(rs.getInt("iId_estado_entrega_pedido"));
                list.add(estadoEntregaPedidoEnUso);
            }

        } catch (Exception e) {
            list = new ArrayList<>();
        }

        return list;
    }

    ////////////////verificar parametros entrada
    public List<itemProductoProgramado> ObtenerDetalleProductosProgramados(String desde, String hasta) {

        List<itemProductoProgramado> lista = new ArrayList<>();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("select dv.iIdProducto," +
                    "isnull(cat.cDescripcion_categoria,'')+' '+  " +
                    "rtrim(ltrim(isnull(subP.c_Descripcion_SubCategoria,''))) +' '+ dv.cProductName+' '+dv.cDescripcion_Variante, " +
                    "sum(dv.iCantidad)," +
                    "isnull(cv.cNombre_Cliente,'') , " +
                    "isnull(mc.cControl1,'')+' '+ isnull(mc.cControl2,'')," +
                    "isnull(mc.iIdCliente,0)," +
                    " p.id_area_produccion," +
                    "isnull(ap.c_Descripcion_area,'') " +
                    "" +
                    "" +
                    "" +
                    " from Detalle_Venta as dv inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta " +
                    "and dv.id_Company=?  inner join Product as p on dv.iIdProducto=p.iIdProduct and p.iIdCompany=? " +
                    " left join Areas_Produccion as ap on p.id_area_produccion=ap.id_area_produccion " +
                    " left join Categoria_Productos as cat on p.id_categoria=cat.id_categoria_producto " +
                    "and cat.id_company=?  left join SubCategorias as subP on p.id_subcategoria=subP.id_Subcategoria" +
                    " and subP.id_company=? " +
                    " left join maestroCliente as mc on mc.iIdCliente=cv.iId_Cliente  where cv.iId_Company=? and cv.iId_Tienda=? " +
                    "and cv.cEliminado='' " +
                    "and cv.cEstadoVenta='N' and cv.cEstado_Atencion='I'  and cv.FechaVentaRealizada-'5:00' between ? and ?  " +
                    "and dv.bCabeceraPack=0  group by isnull(mc.iIdCliente,0),isnull(cv.cNombre_Cliente,'') " +
                    " ,isnull(mc.cControl1,'')+' '+isnull(mc.cControl2,'')," +
                    " dv.iIdProducto,isnull(cat.cDescripcion_categoria,'')+' '+" +
                    " rtrim(ltrim(isnull(subP.c_Descripcion_SubCategoria,'')))" +
                    " +' '+ dv.cProductName+' '+dv.cDescripcion_Variante,p.id_area_produccion,isnull(ap.c_Descripcion_area,'') " +
                    "order by p.id_area_produccion,isnull(ap.c_Descripcion_area,''),isnull(cv.cNombre_Cliente,'')");

            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Empresa.idEmpresa);
            ps.setInt(5, Constantes.Empresa.idEmpresa);
            ps.setInt(6, Constantes.Tienda.idTienda);
            ps.setString(7, desde);
            ps.setString(8, hasta);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {

                itemProductoProgramado a = new itemProductoProgramado();
                a.getProduct().setIdProducto(rs.getInt(1));
                a.getProduct().setProductName(rs.getString(2));
                a.getProduct().setCantidad(rs.getFloat(3));
                a.getCustomer().setcName(rs.getString(4));
                a.getCustomer().setControl1(rs.getString(5));
                a.getCustomer().setiId(rs.getInt(6));
                a.getAreaProduccion().setIdArea(rs.getInt(7));
                a.getAreaProduccion().setCDescripcionArea(rs.getString(8));
                lista.add(a);


            }

        } catch (SQLException e) {
            e.printStackTrace();
            lista = new ArrayList<>();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;

    }

    public List<itemProductoProgramado> ObtenerProductosProgramadosAcumulado(String desde, String hasta) {


        PreparedStatement ps = null;
        List<itemProductoProgramado> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement("select " +
                    "p.id_area_produccion," +
                    "isnull(ap.c_Descripcion_area,'')" +
                    ",isnull(mc.cControl1,'')" +
                    " ,dv.iIdProducto," +
                    "isnull(cat.cDescripcion_categoria,'')+' '+  rtrim(ltrim(isnull(subP.c_Descripcion_SubCategoria,'')))  +' '+ dv.cProductName+' '+dv.cDescripcion_Variante," +
                    "sum(dv.iCantidad)  " +
                    "from Detalle_Venta as  dv inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta  " +
                    "and dv.id_Company=? left join maestroCliente as mc  on mc.iIdCliente=cv.iId_Cliente  " +
                    "inner join Product as p on dv.iIdProducto=p.iIdProduct and p.iIdCompany=?  " +
                    "left join Categoria_Productos as cat on p.id_categoria=cat.id_categoria_producto  " +
                    "and cat.id_company=?  left join SubCategorias as subP on p.id_subcategoria=subP.id_Subcategoria " +
                    " and subP.id_company=?  left join Areas_Produccion as ap on p.id_area_produccion=ap.id_area_produccion " +
                    " where cv.iId_Company=? and cv.iId_Tienda=? and cv.cEliminado=''  and cv.cEstadoVenta='N' and cv.cEstado_Atencion='I' " +
                    " and cv.FechaVentaRealizada-'5:00' between ? and ? and dv.bCabeceraPack=0  group by isnull(mc.cControl1,'')," +
                    "dv.iIdProducto,isnull(cat.cDescripcion_categoria,'')+' '+  rtrim(ltrim(isnull(subP.c_Descripcion_SubCategoria,''))) +' '+" +
                    " dv.cProductName+' '+dv.cDescripcion_Variante ,p.id_Area_produccion, isnull(ap.c_Descripcion_area,'')  " +
                    "order by p.id_Area_Produccion,isnull(mc.cControl1,''),isnull(cat.cDescripcion_categoria,'')+' '+  " +
                    "rtrim(ltrim(isnull(subP.c_Descripcion_SubCategoria,''))) +' '+ dv.cProductName+' '+dv.cDescripcion_Variante ");

            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Empresa.idEmpresa);
            ps.setInt(3, Constantes.Empresa.idEmpresa);
            ps.setInt(4, Constantes.Empresa.idEmpresa);
            ps.setInt(5, Constantes.Empresa.idEmpresa);
            ps.setInt(6, Constantes.Tienda.idTienda);
            ps.setString(7, desde);
            ps.setString(8, hasta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                itemProductoProgramado a = new itemProductoProgramado();
                a.getAreaProduccion().setIdArea(rs.getInt(1));
                a.getAreaProduccion().setCDescripcionArea(rs.getString(2));
                a.getCustomer().setControl1(rs.getString(3));
                a.getProduct().setIdProducto(rs.getInt(4));
                a.getProduct().setProductName(rs.getString(5));
                a.getProduct().setCantidad(rs.getFloat(6));
                list.add(a);

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            list = new ArrayList<>();

        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<itemProductoProgramado> ObtenerProductosProgramadosAcumuladoSinReferencia(String desde, String hasta) {


        PreparedStatement ps = null;
        List<itemProductoProgramado> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement("select " +
                    "p.id_area_produccion," +
                    "rtrim(ltrim(isnull(ap.c_Descripcion_area,'')))," +
                    "dv.iIdProducto,isnull(cat.cDescripcion_categoria,'')+' '+  " +
                    "rtrim(ltrim(isnull(subP.c_Descripcion_SubCategoria,''))) +' '+ dv.cProductName+' '+dv.cDescripcion_Variante," +
                    "sum(dv.iCantidad) " +
                    " from Detalle_Venta as  dv inner join Cabecera_Venta as cv on cv.iId_Cabecera_Venta=dv.iIdCabecera_Venta  " +
                    "and dv.id_Company=? left join maestroCliente as mc  on mc.iIdCliente=cv.iId_Cliente " +
                    " inner join Product as p on dv.iIdProducto=p.iIdProduct and p.iIdCompany=? " +
                    " left join Categoria_Productos as cat on p.id_categoria=cat.id_categoria_producto " +
                    " and cat.id_company=?  left join SubCategorias as subP on p.id_subcategoria=subP.id_Subcategoria " +
                    " and subP.id_company=?  left join Areas_Produccion as ap on p.id_area_produccion=ap.id_area_produccion " +
                    " where cv.iId_Company=? and cv.iId_Tienda=? and cv.cEliminado=''  and cv.cEstadoVenta='N' and cv.cEstado_Atencion='I'" +
                    "  and cv.FechaVentaRealizada-'5:00' between ? and ? and dv.bCabeceraPack=0  " +
                    "group by dv.iIdProducto,isnull(cat.cDescripcion_categoria,'')+' '+  " +
                    "rtrim(ltrim(isnull(subP.c_Descripcion_SubCategoria,''))) +' '+ dv.cProductName+' '+dv.cDescripcion_Variante ," +
                    "p.id_Area_produccion, rtrim(ltrim(isnull(ap.c_Descripcion_area,'')))  order by p.id_Area_Produccion," +
                    "isnull(cat.cDescripcion_categoria,'')+' '+  rtrim(ltrim(isnull(subP.c_Descripcion_SubCategoria,''))" +
                    ") +' '+ dv.cProductName+' '+dv.cDescripcion_Variante ");


            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                itemProductoProgramado a = new itemProductoProgramado();
                a.getAreaProduccion().setIdArea(rs.getInt(1));
                a.getAreaProduccion().setCDescripcionArea(rs.getString(2));
                a.getProduct().setIdProducto(rs.getInt(3));
                a.getProduct().setDescripcionCategoria(rs.getString(4));
                a.getProduct().setProductName(rs.getString(5));
                a.getProduct().setCantidad(rs.getFloat(6));

            }


        } catch (SQLException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }

        return list;
    }

    public mCabeceraVenta GetCabeceraVenta(int id) {

        mCabeceraVenta c = new mCabeceraVenta();

        PreparedStatement ps = null;


        try {
            CallableStatement cs = conn.prepareCall("call sp_obtener_cabecera_venta_id(?,?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, id);
            ResultSet rs = cs.executeQuery();
  /*          ps=conn.prepareStatement("\n" +
                    "\tselect\t\t\tcNumeroSerieDocumentoSunat,cast(cv.cNumero_Correlativo as int),\t isnull(mv.cPrimerNombre,'') +' '+\n" +
                    "isnull(mv.cApellidoPaterno,'')+' '+isnull(mv.cApellidoMaterno,'') as vendedor,\n" +
                    "  format(cv.FechaVentaRealizada-'5:00','dd-MM-yyyy') as fecha\n" +
                    ",isnull(mc.cRazonSocial,'') as razonsocial,isnull(cNumeroRuc,'') as numdoc\n" +
                    ",isnull(td.cTipoDocumento,'') as tipodoc,isnull(mc.cDireccion,'') as direccion\n" +
                    ",isnull(td.id_doc_sunat,0) as tipodoc2,\n" +
                    " isnull(mc.cEmail,'') as email,isnull(tdp.id_doc_sunat,'') as tipoDocPago,\n" +
                    "isnull(format(cv.FechaVentaRealizada-'5:00','yyyy-MM-dd'),'') as fecha2,\n" +
                    " dDescuento_global,dTotal_Descuento,dTotal_Igv,dTotal_Gravado,dTotal_Neto_Venta\n" +
                    " from cabecera_venta as cv LEFT join MaestroVendedor as\n" +
                    " mv on cv.iId_Vendedor=mv.iIdvendedor \n" +
                    " LEFT JOIN maestrocliente as mc \n" +
                    " on cv.iId_Cliente=mc.iIdCliente and\n" +
                    " cv.iId_company=mc.iIdCompany \n" +
                    " left join tipodeDocumento as td \n" +
                    "on mc.id_tipo_documento=td.iIdTipoDocumento\n" +
                    " left join Tipo_Documento_Pago as tdp\n" +
                    " on cv.id_TipoDocumentoPago = tdp.iIdTipoDocumento\n" +
                    " where iId_Company=? and iId_cabecera_venta=?");

            ps.setInt(1,Constantes.Empresa.idEmpresa);
            ps.setInt(2,id);

            ps.execute();
*/
            while (rs.next()) {


                c.setNumSerie(rs.getString(1));
                c.setNumCorrelativo(rs.getInt(2));
                c.setFechaEmision("27-08-2019");
                c.getCliente().setRazonSocial(rs.getString(5));
                c.getCliente().setNumeroRuc(rs.getString(6));

                c.getCliente().setcDireccion(rs.getString(8));
                c.getCliente().setIdTipoDocSunat(rs.getInt(9));

                c.getCliente().setcEmail(rs.getString(10));
                c.setCodDocPago(rs.getString(11));
                c.setFechaV2(rs.getString(12));
                c.setDescuentoGlobal(rs.getBigDecimal(13));
                c.setTotalDescuento(rs.getBigDecimal(14));
                c.setTotalIgv(rs.getBigDecimal(15));
                c.setTotalGravado(rs.getBigDecimal(16));
                c.setTotalPagado(rs.getBigDecimal(17));
                c.setIdComprobantePagoSunat(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;

    }

    public List<Integer> ObtenerIdReenvio() {

        CallableStatement cs = null;
        List<Integer> listId = new ArrayList<>();
        try {
            cs = conn.prepareCall("call sp_reenvio_factura(?)");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            ResultSet rs = cs.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    listId.add(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listId;
    }

    public Version URL_New_Version(String pack) {

        Version a = new Version();

        CallableStatement cs = null;
        Connection co = getConnectionStart();
        String url = "";
        if (co != null) {

            try {
                cs = co.prepareCall("call obtener_url_descarga(?,?,?)");
                cs.setString(1, pack);
                cs.registerOutParameter(2, Types.VARCHAR);
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.execute();

                a.setLink(cs.getString(2));
                a.setName(cs.getString(3));

            } catch (SQLException e) {
                e.printStackTrace();
                url = "";
                a.setName("");
                a.setLink("");
            }
        }

        return a;

    }

    public RespuestaProductoVenta AgregarProductoCodigoBarra(String codigoBarra, int idCabecera, int DetallePedido, int idProducto, String Metodo) {
        SQLException e;
        int i;
        String str;
        int i2;
        int i3 = idCabecera;
        ProductoEnVenta p = new ProductoEnVenta();
        String m = "";
        String m2 = Metodo;
        RespuestaProductoVenta r = new RespuestaProductoVenta();
        try {
            CallableStatement cs = this.conn.prepareCall("call sp_agregar_producto_pedido_codigo_barra_V2(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            try {
                cs.setString(1, codigoBarra);
                cs.setInt(2, Constantes.Empresa.idEmpresa);
                cs.setInt(3, Constantes.Tienda.idTienda);
                cs.setInt(4, Constantes.Usuario.idUsuario);
                cs.setInt(5, Constantes.Terminal.idTerminal);
                cs.registerOutParameter(6, 4);
                cs.registerOutParameter(7, 12);
                try {
                    cs.setInt(8, DetallePedido);
                    cs.registerOutParameter(8, 4);
                    cs.setInt(9, i3);
                    cs.registerOutParameter(10, 12);
                    cs.registerOutParameter(11, 4);
                    cs.registerOutParameter(12, 6);
                    cs.registerOutParameter(13, 6);
                    cs.registerOutParameter(14, 6);
                    cs.registerOutParameter(15, 3);
                    cs.registerOutParameter(16, 3);
                    cs.registerOutParameter(17, 16);
                } catch (SQLException e2) {
                    e = e2;
                    i = idProducto;
                    str = Metodo;
                    e.printStackTrace();
                    p = new ProductoEnVenta();
                    p.setIdProducto(-99);
                    r.setCodeRespuesta(20);
                    r.setMensaje("Existe un problema en la busqueda de los codigos de barra.Verifique que los códigos no se repiten.Verifique su conexión a internet");
                    r.setProductoEnVenta(p);
                    return r;
                }
            } catch (SQLException e3) {
                e = e3;
                i2 = DetallePedido;
                i = idProducto;
                str = Metodo;
                e.printStackTrace();
                p = new ProductoEnVenta();
                p.setIdProducto(-99);
                r.setCodeRespuesta(20);
                r.setMensaje("Existe un problema en la busqueda de los codigos de barra.Verifique que los códigos no se repiten.Verifique su conexión a internet");
                r.setProductoEnVenta(p);
                return r;
            }
            try {
                cs.setString(18, Metodo);
                cs.registerOutParameter(18, 12);
                cs.registerOutParameter(19, 4);
                try {
                    cs.setInt(19, idProducto);
                    cs.registerOutParameter(20, 16);
                    cs.registerOutParameter(21, 16);
                    cs.execute();
                    r.setCodeRespuesta(cs.getInt(6));
                    r.setMensaje(cs.getString(7));
                    p.setMetodoGuardar(m2);
                    p.setIdDetallePedido(cs.getInt(8));
                    p.setIdCabeceraPedido(i3);
                    p.setCantidadReserva(cs.getFloat(12));
                    p.setStockActual(cs.getFloat(13));
                    p.setCantidad(cs.getFloat(14));
                    p.setProductName(cs.getString(10));
                    p.setPrecioOriginal(cs.getBigDecimal(15));
                    p.setPrecioVentaFinal(cs.getBigDecimal(16));
                    p.setItemNum(cs.getInt(11));
                    p.setbPrecioVariable(cs.getBoolean(17));
                    p.setMetodoGuardar(cs.getString(18));
                    p.setIdProducto(cs.getInt(19));
                    p.setEsPack(cs.getBoolean(20));
                    p.setComboSimple(cs.getBoolean(21));
                } catch (SQLException e4) {
                    e = e4;
                    e.printStackTrace();
                    p = new ProductoEnVenta();
                    p.setIdProducto(-99);
                    r.setCodeRespuesta(20);
                    r.setMensaje("Existe un problema en la busqueda de los codigos de barra.Verifique que los códigos no se repiten.Verifique su conexión a internet");
                    r.setProductoEnVenta(p);
                    return r;
                }
            } catch (SQLException e5) {
                e = e5;
                i = idProducto;
                e.printStackTrace();
                p = new ProductoEnVenta();
                p.setIdProducto(-99);
                r.setCodeRespuesta(20);
                r.setMensaje("Existe un problema en la busqueda de los codigos de barra.Verifique que los códigos no se repiten.Verifique su conexión a internet");
                r.setProductoEnVenta(p);
                return r;
            }
        } catch (SQLException e6) {
            e = e6;
            String str2 = codigoBarra;
            i2 = DetallePedido;
            i = idProducto;
            str = Metodo;
            e.printStackTrace();
            p = new ProductoEnVenta();
            p.setIdProducto(-99);
            r.setCodeRespuesta(20);
            r.setMensaje("Existe un problema en la busqueda de los codigos de barra.Verifique que los códigos no se repiten.Verifique su conexión a internet");
            r.setProductoEnVenta(p);
            return r;
        }
        r.setProductoEnVenta(p);
        return r;
    }


    public RespuestaProductoVenta AgregarComboRapidoDetallePedido(int idCabeceraVenta, ProductoEnVenta p) {
        RespuestaProductoVenta r = new RespuestaProductoVenta();
        try {
            CallableStatement cs = this.conn.prepareCall("call sp_agregar_combo_rapido_detalle_pedido(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            cs.setInt(1, idCabeceraVenta);
            cs.setInt(2, Constantes.Empresa.idEmpresa);
            cs.setInt(3, Constantes.Tienda.idTienda);
            cs.setInt(4, Constantes.Terminal.idTerminal);
            cs.setInt(5, Constantes.Usuario.idUsuario);
            cs.setInt(6, p.getIdProducto());
            cs.registerOutParameter(7, 4);
            cs.registerOutParameter(8, 12);
            cs.registerOutParameter(9, 12);
            cs.registerOutParameter(10, 16);
            cs.registerOutParameter(11, 3);
            cs.registerOutParameter(12, 3);
            cs.registerOutParameter(13, 6);
            cs.setInt(13, 1);
            cs.registerOutParameter(14, 12);
            cs.registerOutParameter(15, 4);
            cs.registerOutParameter(16, 4);
            cs.execute();
            r.getProductoEnVenta().setMetodoGuardar("N");
            r.getProductoEnVenta().setItemNum(cs.getInt(7));
            r.getProductoEnVenta().setIdDetallePedido(cs.getInt(16));
            r.getProductoEnVenta().setProductName(cs.getString(8));
            r.getProductoEnVenta().setEsPack(cs.getBoolean(10));
            r.getProductoEnVenta().setPrecioOriginal(cs.getBigDecimal(11));
            r.getProductoEnVenta().setPrecioVentaFinal(cs.getBigDecimal(12));
            r.getProductoEnVenta().setCantidad(cs.getFloat(13));
            r.setMensaje(cs.getString(14));
            r.setCodeRespuesta(cs.getInt(15));
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
            r = new RespuestaProductoVenta();
            r.setCodeRespuesta(20);
            r.setMensaje("Hubo un problema al guardar el producto.Verifique su conexión a internet o reinicie su aplicación.");
            return r;
        }
    }

    public void ObtenerDetalleProductosProgramados() {


    }

    public mDocVenta GenerarNotaFacturacion(int idCabeceraVenta) {

        mDocVenta doc = null;
        mCabeceraVenta cabeceraVenta = new mCabeceraVenta();
        List<ProductoEnVenta> list = new ArrayList<>();
        ResultSet rs = null;
        ResultSet rs1 = null;
        String fechaRef = "";
        String numeroSerieRef = "";
        int correlativoRef = 0;
        ProductoEnVenta p;

        try {
            PreparedStatement ps = conn.prepareStatement("select format(FechaVentaRealizada,'yyyy-MM-dd') as fechaRef,\n" +
                    " FORMAT(GETUTCDATE()-'5:00','yyyy-MM-dd') as fechaAct,id_tipoDocumentoPago,cNumeroSerieDocumentoSunat,\n" +
                    " CAST( cNumero_Correlativo as int) as serie,id_TipoDocSunat,cNumDocCliente,cDireccionCliente,\n" +
                    " cast(dTotal_Igv as decimal(12,2)),cast(dTotal_Gravado as decimal(12,2)),\n" +
                    " cast(dTotal_Bruto_Venta as decimal(12,2)),cast(dTotal_Neto_Venta as decimal(12,2)),cNombre_Cliente\n" +
                    " from cabecera_venta where iId_Cabecera_Venta=8052");
            ps.execute();
            rs = ps.getResultSet();

            while (rs.next()) {
                fechaRef = rs.getString(1);
                cabeceraVenta.setFechaV2(rs.getString(2));
                //  cabeceraVenta.setIdTipoDocPago("07");
                numeroSerieRef = rs.getString(4);
                cabeceraVenta.setIdVenta(8052);
                cabeceraVenta.setNumSerie("F401");
                correlativoRef = rs.getInt(5);
                cabeceraVenta.setNumCorrelativo(6);
                cabeceraVenta.getCliente().setIdTipoDocSunat(rs.getInt(6));
                cabeceraVenta.getCliente().setTipoCliente(rs.getInt(6));
                cabeceraVenta.getCliente().setNumeroRuc(rs.getString(7));
                cabeceraVenta.getCliente().setcDireccion(rs.getString(8));
                cabeceraVenta.setTotalIgv(rs.getBigDecimal(9));
                cabeceraVenta.setTotalGravado(rs.getBigDecimal(10));
                cabeceraVenta.setTotalPagado(rs.getBigDecimal(12));
                cabeceraVenta.setCodDocPago("07");
                cabeceraVenta.getCliente().setcEmail("gchavez@sjr.edu.pe");
                cabeceraVenta.getCliente().setRazonSocial(rs.getString(13));
            }
            rs.close();
            PreparedStatement ps1 = conn.prepareStatement("  select cProductName," +
                    " iCantidad,cDescripcion_Unidad_Sunat," +
                    "cast(dValor_Unitario as decimal(12,2)),\n" +
                    "  CAST(dPrecio_Neto AS decimal(12,2))," +
                    "cast(d_Igv as decimal(12,2)),\n" +
                    " cast( dPrecioVentaUnidad as decimal(12,2)) ," +
                    " cast(dPrecio_Subtotal as decimal(12,2)) from detalle_venta where iIdCabecera_Venta=8052 ");

            ps1.execute();
            rs1 = ps1.getResultSet();

            while (rs1.next()) {

                p = new ProductoEnVenta();
                p.setCodigoProducto("00523");

                p.setProductName(rs1.getString(1));
                p.setCantidad(rs1.getFloat(2));
                p.setCodUnidSunat(rs1.getString(3));
                p.setPrecioOriginal(rs1.getBigDecimal(4));
                p.setPrecioOriginal(rs1.getBigDecimal(5));
                p.setMontoIgv(rs1.getBigDecimal(6));
                p.setPrecioVentaFinal(rs1.getBigDecimal(7));
                list.add(p);
            }
            //      rs1.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        doc = new mDocVenta(cabeceraVenta, list);
        doc.setEsNota(true);
        doc.setFechaReferencia(fechaRef);
        doc.setCorrelativoReferencia(correlativoRef);
        doc.setSerieReferencia(numeroSerieRef);
        doc.setTipoDocReferenciaChar("01");
        return doc;
    }

    public ResultProcces PermitirVenta(int idCabeceraPedido, boolean ValidaPagos) {
        ResultProcces r = new ResultProcces();
        try {
            CallableStatement cs = conn.prepareCall("call sp_permitir_venta_pedido_v2(" + ParamStoreProcedure(8) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Terminal.idTerminal);
            cs.setInt(4, idCabeceraPedido);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, Types.INTEGER);
            cs.setBoolean(8, ValidaPagos);
            cs.execute();
            r.setCodeResult(cs.getInt(6));
            r.setMessageResult(cs.getString(5));
            r.setCodeProcess(cs.getInt(7));
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            r.setMessageResult("Hubo un inconveniente al realizar la operación.Verifique su conexion a internet.");
            r.setCodeResult(99);
        }
        return r;
    }

    public mDocVenta GenerarNota(int idCabeceraVenta, String Motivo) {

        mDocVenta docVenta = null;
        mCabeceraVenta cabeceraVenta = new mCabeceraVenta();
        List<ProductoEnVenta> list = new ArrayList<>();
        boolean permitir = false;
        String fechaRef = "";
        String numeroSerieRef = "";
        int correlativoRef = 0;
        String resultado;
        try {
            CallableStatement cs = conn.prepareCall("call sp_generar_nota(" + ParamStoreProcedure(8) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Terminal.idTerminal);
            cs.setInt(4, Constantes.Usuario.idUsuario);
            cs.setInt(5, idCabeceraVenta);
            cs.setInt(6, 1);
            cs.setString(7, Motivo);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.execute();
            resultado = cs.getString(8);
            JSONParser parser = new JSONParser();
            JSONObject json1 = (JSONObject) parser.parse(resultado);

            int code = Integer.parseInt(json1.get("codeResult").toString());
            /*          String correlativo= json.get("correlativo").toString();
            String serie= json.get("serie").toString();
*/
            if (code == 100) {

                String m = json1.get("Content").toString();
                JSONObject json = (JSONObject) parser.parse(m);
                fechaRef = json.get("fechaOrigen").toString();
                cabeceraVenta.setFechaEmision(json.get("fecha").toString());
                cabeceraVenta.setFechaV2(json.get("fecha").toString());
                numeroSerieRef = json.get("serieModifica").toString();
                cabeceraVenta.setIdVenta(idCabeceraVenta);
                cabeceraVenta.setNumSerie(json.get("serie").toString());
                correlativoRef = Integer.parseInt(json.get("correlativoModifica").toString());
                cabeceraVenta.setNumCorrelativo(Integer.parseInt(json.get("correlativo").toString()));
                cabeceraVenta.getCliente().setIdTipoDocSunat(Integer.parseInt(
                        json.get("tipoDocumentoCliente").toString()));
                cabeceraVenta.getCliente().setTipoCliente(Integer.parseInt(
                        json.get("tipoDocumentoCliente").toString()));
                cabeceraVenta.getCliente().setNumeroRuc(json.get("numeroDocumento").toString());
                cabeceraVenta.getCliente().setcDireccion(json.get("direccionCliente").toString());
                cabeceraVenta.setTotalIgv(new BigDecimal(json.get("igv").toString()));
                cabeceraVenta.setTotalGravado(new BigDecimal(json.get("totalgravado").toString()));
                cabeceraVenta.setTotalPagado(new BigDecimal(json.get("totalMovimiento").toString()));
                cabeceraVenta.setCodDocPago(json.get("tipoDocumentoChar").toString());
                cabeceraVenta.getCliente().setcEmail(json.get("clienteEmail").toString());
                cabeceraVenta.getCliente().setRazonSocial(json.get("clienteDenominacion").toString());
                cabeceraVenta.setIdComprobantePagoSunat(Integer.parseInt(json.get("tipoDocumentoNum").toString()));

                CallableStatement cs1 = conn.prepareCall("call sp_obtener_detalle_nota(" + ParamStoreProcedure(4) + ")");
                cs1.setInt(1, Constantes.Empresa.idEmpresa);
                cs1.setInt(2, Constantes.Tienda.idTienda);
                cs1.setInt(3, Constantes.Terminal.idTerminal);
                cs1.setInt(4, idCabeceraVenta);
                cs1.execute();
                ResultSet rs1 = cs1.getResultSet();
                if (rs1 != null)
                    while (rs1.next()) {
                        ProductoEnVenta p = new ProductoEnVenta();
                        p.setProductName(rs1.getString(1));
                        p.setCantidad(rs1.getFloat(2));
                        p.setCodUnidSunat(rs1.getString(3));
                        p.setValorUnitario(rs1.getBigDecimal(4));
                        p.setPrecioNeto(rs1.getBigDecimal(5));
                        p.setMontoIgv(rs1.getBigDecimal(6));
                        p.setPrecioOriginal(rs1.getBigDecimal(7));
                        p.setPrecioVentaFinal(rs1.getBigDecimal(8));
                        p.setCodigoProducto(rs1.getString(9));
                        p.setIdProducto(rs1.getInt(10));
                        list.add(p);
                    }
                docVenta = new mDocVenta(cabeceraVenta, list);
                docVenta.setSerieReferencia(numeroSerieRef);
                docVenta.setCorrelativoReferencia(correlativoRef);
                docVenta.setFechaReferencia(fechaRef);
                docVenta.setTipoDocReferenciaChar(json.get("tipoDocumentoModificaChar").toString());
                docVenta.setTipoDocReferenciaNum(Integer.parseInt(json.get("tipoDocumentoModificaNum").toString()));
                docVenta.setEsNota(true);

                cs1.close();
                cs.close();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ParseException e2) {
            e2.printStackTrace();
        } catch (Exception e) {
            e.toString();
        }
        return docVenta;
    }

    public ResultProcces VerificarConfigCorrelativosNota() {

        ResultProcces r = new ResultProcces();

        try {
            CallableStatement cs = conn.prepareCall("call sp_verificar_existencia_correlativo_nota(" + ParamStoreProcedure(6) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Terminal.idTerminal);
            cs.registerOutParameter(4, Types.VARCHAR);
            cs.registerOutParameter(5, Types.BOOLEAN);
            cs.registerOutParameter(6, Types.INTEGER);
            cs.execute();

            r.setMessageResult(cs.getString(4));
            r.setCodeProcess(cs.getInt(6));
            r.setPermitir(cs.getBoolean(5));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return r;
    }

    public void ActualizarEstadoNotaGenerada(ResultadoComprobante resultadoComprobante, int idCabeceraVenta) {


        try {
            CallableStatement cs = conn.prepareCall("call sp_actualizar_estado_nota(" + ParamStoreProcedure(10) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Usuario.idUsuario);
            cs.setInt(4, Constantes.Terminal.idTerminal);
            cs.setInt(5, idCabeceraVenta);
            cs.setInt(6, resultadoComprobante.getCodeSuccess());
            cs.setBoolean(7, resultadoComprobante.getEnviado());
            cs.setBoolean(8, resultadoComprobante.getRecibido());
            cs.setString(9, resultadoComprobante.getMensaje());
            cs.setString(10, resultadoComprobante.getEstadoRespuesta());
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TipoAnulacion> ObtenerTiposAnulacionDocumento(int idCabecera) {

        List<TipoAnulacion> list = new ArrayList<>();
        try {
            CallableStatement cs = conn.prepareCall("call sp_obtener_tipos_anulacion_documento(?,?,?)");
            cs.setInt(1, idCabecera);
            cs.setInt(2, Constantes.Empresa.idEmpresa);
            cs.setInt(3, Constantes.Tienda.idTienda);
            cs.execute();

            ResultSet rs = cs.executeQuery();

            if (rs != null) {

                while (rs.next()) {

                    TipoAnulacion ta = new TipoAnulacion();
                    ta.setIdAnulacion(rs.getInt(1));
                    ta.setDesAnulacion(rs.getString(2));
                    ta.setCodeAnulacion(rs.getInt(3));
                    list.add(ta);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public HashMap<String, Object> GetPedidosNotificacion() {

        HashMap<String, Object> result = new HashMap<>();
        CallableStatement cs;
        try {
            cs = conn.prepareCall("call sp_get_notificacion_pedido(" + ParamStoreProcedure(2) + ")");
            cs.setInt("@pidCompany", Constantes.Empresa.idEmpresa);
            cs.setInt("@pidTienda", Constantes.Tienda.idTienda);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while (rs.next()) {
                result.put(Constantes.ParamNameMap.MESSAGE_NOT, rs.getString("mensaje_notificacion"));
                result.put(Constantes.ParamNameMap.NRO_PEDIDOS, rs.getInt("nro_pedidos_nuevo"));
                result.put(Constantes.ParamNameMap.TIENE_PEDIDO, rs.getBoolean("tienePedidosNuevos"));
                result.put(Constantes.ParamNameMap.ID_PEDIDO, rs.getInt("idPedidoUnico"));
            }
        } catch (Exception ex) {
            result.clear();
            result.put(Constantes.ParamNameMap.MESSAGE_NOT, "");
            result.put(Constantes.ParamNameMap.NRO_PEDIDOS, 0);
            result.put(Constantes.ParamNameMap.TIENE_PEDIDO, false);
            result.put(Constantes.ParamNameMap.ID_PEDIDO, 0);

        } finally {

        }
        return result;
    }

    public ResultProcces ActualizaCategoriaDefecto(boolean bUsaCategoria, int idCategoria) {
        ResultProcces result = new ResultProcces();
        CallableStatement cs = null;
        try {
            cs = conn.prepareCall("call sp_modifica_categoria_defecto(" + ParamStoreProcedure(6) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setBoolean(3, bUsaCategoria);
            cs.setInt(4, idCategoria);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.execute();
            result.setCodeResult(cs.getInt(5));
            result.setMessageResult(cs.getString(6));


        } catch (Exception e) {
            result.setCodeResult(100);
            result.setMessageResult("No se logro modificar la categoria por defecto");
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public mDocVenta AnularDocumento(int idCabeceraVenta, String motivo) {

        mDocVenta doc = null;

        mCabeceraVenta cab = new mCabeceraVenta();
        doc = new mDocVenta(cab, new ArrayList<ProductoEnVenta>());
        try {
            CallableStatement cs = conn.prepareCall("call sp_generar_anulacion(" + ParamStoreProcedure(15) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Terminal.idTerminal);
            cs.setInt(4, Constantes.Usuario.idUsuario);
            cs.setInt(5, idCabeceraVenta);
            cs.setString(6, motivo);


            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.registerOutParameter(9, Types.INTEGER);
            cs.registerOutParameter(10, Types.VARCHAR);
            cs.registerOutParameter(11, Types.VARCHAR);
            cs.registerOutParameter(12, Types.INTEGER);
            cs.registerOutParameter(13, Types.VARCHAR);
            cs.registerOutParameter(14, Types.VARCHAR);
            cs.registerOutParameter(15, Types.INTEGER);
            cs.execute();

            doc.setFechaReferencia(cs.getString(14));
            doc.getCabeceraVenta().setTipoDocumento(cs.getString(13));
            doc.getCabeceraVenta().setNumCorrelativo(cs.getInt(12));
            doc.getCabeceraVenta().setNumSerie(cs.getString(11));
            doc.getCabeceraVenta().setFechaEmision(cs.getString(10));
            doc.setCodeResult(cs.getInt(9));
            doc.setMessageResult(cs.getString(8));
            doc.setIdResult(cs.getInt(7));
            doc.getCabeceraVenta().setIdTipoDocPago(cs.getInt(15));

        } catch (SQLException e) {
            e.printStackTrace();
            doc = null;
        }

        return doc;
    }

    public void ActualizarEstadoComunicacionBaja(int idCabeceraVenta, ResultadoComprobante resultadoComprobante) {

        CallableStatement cs = null;
        try {

            cs = conn.prepareCall("call sp_actualizar_estado_anulacion(" + ParamStoreProcedure(10) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, Constantes.Terminal.idTerminal);
            cs.setInt(4, Constantes.Usuario.idUsuario);
            cs.setInt(5, idCabeceraVenta);
            cs.setString(6, resultadoComprobante.getEstadoRespuesta());
            cs.setInt(7, resultadoComprobante.getCodeSuccess());
            cs.setBoolean(8, resultadoComprobante.getEnviado());
            cs.setBoolean(9, resultadoComprobante.getRecibido());
            cs.setString(10, resultadoComprobante.getMensaje());
            cs.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            e.toString();
        }

    }

    //Api-check
    public ResultProcces GuardarConfiguracionCelularWeb(ConfigWhatsappTienda configWhatsappTienda) {
        ResultProcces result = new ResultProcces();
        try {
            CallableStatement cs = conn.prepareCall("call sp_grabar_configuracion_celular_web(" + ParamStoreProcedure(7) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setBoolean(3, configWhatsappTienda.getBActivo());
            cs.setString(5, configWhatsappTienda.getCNumero());
            cs.setString(4, configWhatsappTienda.getCMensajeInicial());
            cs.registerOutParameter(6, Types.INTEGER);
            cs.registerOutParameter(7, Types.VARCHAR);

            cs.execute();

            result.setCodeResult(cs.getInt(6));
            result.setMessageResult(cs.getString(7));

        } catch (Exception ex) {

            ex.toString();
        }
        return result;
    }

    //Api-check
    public ConfigWhatsappTienda GetConfiguracionWhatsappWeb() {
        ConfigWhatsappTienda result = new ConfigWhatsappTienda();
        try {
            PreparedStatement ps = conn.prepareStatement("select cNumCelularWeb,bUsaCelularWeb,cMensajeWebEnvioUsuario from Configuracion_GeneralTienda WHERE iIdCompany=? and iIdTienda=?");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                result.setBActivo(rs.getBoolean("bUsaCelularWeb"));
                result.setCNumero(rs.getString("cNumCelularWeb"));
                result.setCMensajeInicial(rs.getString("cMensajeWebEnvioUsuario"));
            }

        } catch (Exception ex) {


        }
        return result;
    }

    //api-check
    public SemanaConfigWeb GetSemanaConfig() {
        SemanaConfigWeb result = new SemanaConfigWeb();
        try {
            CallableStatement cs = conn.prepareCall("call SP_GET_DIAS_SEMANA_WEB_CONFIG(" + ParamStoreProcedure(2) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            ArrayList<DiaSemanaConfig> list = new ArrayList<>();

            while (rs.next()) {

                DiaSemanaConfig d = new DiaSemanaConfig();
                d.setActivo(rs.getBoolean(3));
                d.setIdSemanaConfig(rs.getInt(1));
                d.setDescripcion(rs.getString(2));
                list.add(d);

            }

            result.setDiasSemana(list);
        } catch (Exception ex) {

        }
        return result;
    }

    public ResultProcces GuardarConfigDiaSemana(int idDiaSemanaConfig, boolean activo) {

        ResultProcces result = new ResultProcces();
        try {

            CallableStatement cs = conn.prepareCall("call sp_guardar_dia_semana_config(" + ParamStoreProcedure(6) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idDiaSemanaConfig);
            cs.setBoolean(4, activo);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.execute();

            result.setCodeResult(cs.getInt(5));
            result.setMessageResult(cs.getString(6));


        } catch (Exception ex) {

        }
        return result;
    }

    public List<TimeHour> GetIntervaloEntrega() {
        List<TimeHour> list = new ArrayList<>();
        try {
            CallableStatement cs = conn.prepareCall("call sp_getIntervaloHorasEntrega(" + ParamStoreProcedure(2) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while (rs.next()) {
                TimeHour t1 = new TimeHour();
                t1.setHour(rs.getInt("HOUR_INIT"));
                t1.setMinute(rs.getInt("MIN_INIT"));
                TimeHour t2 = new TimeHour();
                t2.setHour(rs.getInt("HOUR_END"));
                t2.setMinute(rs.getInt("MIN_END"));
                list.add(t1);
                list.add(t2);
            }
        } catch (Exception ex) {

        }
        return list;
    }

    public ResultProcces GuardarHorasIntervaloEntrega(TimeHour horaInit, TimeHour horaEnd) {
        ResultProcces result = new ResultProcces();
        try {
            CallableStatement cs = conn.prepareCall("call sp_actualiza_periodo_horas_entrega(" + ParamStoreProcedure(8) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, horaInit.getHour());
            cs.setInt(4, horaInit.getMinute());
            cs.setInt(5, horaEnd.getHour());
            cs.setInt(6, horaEnd.getMinute());
            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.execute();

            result.setCodeResult(cs.getInt(7));
            result.setMessageResult(cs.getString(8));

        } catch (Exception ex) {
            result.setMessageResult("Existe un inconveniente al guarda la informaciñon.Verifique su conexión a internet");
            result.setCodeResult(99);
        }
        return result;
    }

    public boolean UpdateLogoCompany(byte[] arrayImg) {

        boolean result = false;
        try {
            CallableStatement cs = connectionInfra.prepareCall("call sp_update_logo_company(" + ParamStoreProcedure(2) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setBytes(2, arrayImg);
            cs.execute();
            result = true;
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public byte[] GetLogoCompany() {
        byte[] array = null;
        try {
            CallableStatement cs = connectionInfra.prepareCall("call sp_get_logo_company(" + ParamStoreProcedure(1) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while (rs.next()) {

                array = rs.getBytes(1);

            }

        } catch (Exception ex) {

        }
        return array;
    }

    //API-CHECK
    public ListasPreciosActivas GetListaPreciosSelect() {
        ListasPreciosActivas listas = new ListasPreciosActivas();
        try {
            CallableStatement cs = conn.prepareCall("call sp_get_lista_precios_select(" + ParamStoreProcedure(2) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                ListaPrecios lista = new ListaPrecios();
                lista.setIdLista(rs.getInt(1));
                lista.setDescripcionLista(rs.getString(2));
                listas.getListas().add(lista);


            }

        } catch (Exception ex) {

        }
        return listas;
    }

    //API-CHECK
    public ResultProcces UpdateListaPrecioDetalle(ListaPrecioDetalle detalle) {

        ResultProcces result = new ResultProcces();
        try {
            CallableStatement cs = conn.prepareCall("call sp_actualiza_detalle_lista_precio(" + ParamStoreProcedure(8) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, detalle.getIdDetalleLista());
            cs.setInt(4, detalle.getProduct().getIdProduct());
            cs.setInt(5, detalle.getIdLista());
            cs.setBigDecimal(6, detalle.getPrecioUnitarioVenta());
            cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, Types.VARCHAR);
            cs.execute();
            result.setCodeResult(cs.getInt(7));
            result.setMessageResult(cs.getString(8));


            cs.execute();
        } catch (Exception ex) {
            ex.toString();
            result.setCodeResult(0);
            result.setMessageResult("Existe un inconveniente al guarda la información.Verifique su conexión a internet.");
        }
        return result;

    }

    //API-CHECK
    public List<ListaPrecioDetalle> GetListPrecioDetalle(int idListaPrecio, String parametro) {
        List<ListaPrecioDetalle> list = new ArrayList<>();
        try {
            CallableStatement cs = conn.prepareCall("call sp_get_detalle_lista_precio_id(" + ParamStoreProcedure(4) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idListaPrecio);
            cs.setString(4, parametro);
            cs.execute();

            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                ListaPrecioDetalle item = new ListaPrecioDetalle();
                item.setIdDetalleLista(rs.getInt(1));
                item.getProduct().setIdProduct(rs.getInt(2));
                item.getProduct().setcProductName(rs.getString(3));
                item.setPrecioUnitarioVenta(rs.getBigDecimal(4));
                item.getProduct().setcKey(rs.getString(5));
                item.getProduct().setDescripcionCategoria(rs.getString(6));
                item.getProduct().setDescripcionVariante(rs.getString(7));
                list.add(item);
            }

        } catch (Exception ex) {

        }
        return list;
    }

    public List<TipoModificadorPack> GetTipoModificadoresPack() {

        CallableStatement cs = null;
        final List<TipoModificadorPack> list = new ArrayList<>();
        try {
            cs = conn.prepareCall("call sp_get_tipo_modificadores_pack");
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while (rs.next()) {
                TipoModificadorPack t = new TipoModificadorPack();
                t.setIdTipoMod(rs.getInt(1));
                t.setDescripcionMod(rs.getString(2));
                t.setBVisibleMonto(rs.getBoolean(3));

                list.add(t);
            }

        } catch (Exception e) {

        }
        return list;
    }

    //API-DEMO
    public int GetIdZonaServicioReservado(String descripcionzona) {

        int idPedido = 0;
        try {
            CallableStatement cs = conn.prepareCall("call SP_GET_ID_PEDIDO_ZONA_SERVICIO_RESERVA(" + ParamStoreProcedure(4) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setString(3, descripcionzona);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();

            idPedido = cs.getInt(4);


        } catch (Exception ex) {
            idPedido = -1;
        }

        return idPedido;
    }

    public void GeneraPromocion(int idPedido, String nroPlaca) {

        try {

            CallableStatement cs = conn.prepareCall("call p_genera_promocion(?,?)");
            cs.setInt(1, idPedido);
            cs.setString(2, nroPlaca);
            cs.execute();

        } catch (Exception ex) {

        }

    }

    //avanzando api
    public int GetAforoDisponible() {

        int aforoDisponible = 0;

        try {

            PreparedStatement ps = conn.prepareStatement("select DBO.FUN_GET_AFORO_DISPONIBLE(?,?)");
            ps.setInt(1, Constantes.Empresa.idEmpresa);
            ps.setInt(2, Constantes.Tienda.idTienda);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {

                aforoDisponible = rs.getInt(1);

            }

        } catch (Exception ex) {
            aforoDisponible = 0;
        }

        return aforoDisponible;
    }

    //API-DEMO
    public List<ResumenPedido> GetResumenPedidoReserva() {


        CallableStatement cs = null;
        List<ResumenPedido> list = new ArrayList<>();
        try {


            cs = conn.prepareCall("call sp_get_resumen_pedidos_reserva(" + ParamStoreProcedure(2) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.execute();

            ResultSet rs = cs.getResultSet();
            while (rs.next()) {
                ResumenPedido obj = new ResumenPedido();
                obj.setIdPedido(rs.getInt(1));
                obj.setIdentificador(rs.getString(2));
                obj.setFecha(rs.getString(3));
                obj.setProductoResumen(rs.getString(4));
                obj.setMarcado(rs.getBoolean(5));
                list.add(obj);
            }

        } catch (Exception ex) {


        }

        return list;
    }

    public ResultProcces updateVerificadoPedido(int idPedido, boolean bVerificado) {
        String mensaje = "";
        int code = 0;
        try {

            CallableStatement cs = conn.prepareCall("call sp_actualiza_verifica_pedido(" + ParamStoreProcedure(6) + ")");
            cs.setInt(1, Constantes.Empresa.idEmpresa);
            cs.setInt(2, Constantes.Tienda.idTienda);
            cs.setInt(3, idPedido);
            cs.setBoolean(4, bVerificado);
            cs.registerOutParameter(5, Types.INTEGER);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.execute();

            mensaje = cs.getString(6);
            code = cs.getInt(5);

            cs.close();


        } catch (Exception ex) {

        }
        return new ResultProcces(code, mensaje);
    }

    public class RetornoCancelar {


        int idCabeceraVenta;
        private Timestamp fechaApertura;
        private Timestamp fechaCierre;
        byte respuesta;

        public RetornoCancelar() {
            java.util.Date utilDate = new Date(); //fecha actual
            long lnMilisegundos = utilDate.getTime();
            fechaApertura = new Timestamp(lnMilisegundos);
            fechaCierre = new Timestamp(lnMilisegundos);
            respuesta = 0;
        }

        public byte getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(byte respuesta) {
            this.respuesta = respuesta;
        }

        public int getIdCabeceraVenta() {
            return idCabeceraVenta;
        }

        public void setIdCabeceraVenta(int idCabeceraVenta) {
            this.idCabeceraVenta = idCabeceraVenta;
        }

        public Timestamp getFechaApertura() {
            return fechaApertura;
        }

        public void setFechaApertura(Timestamp fechaApertura) {
            this.fechaApertura = fechaApertura;
        }

        public Timestamp getFechaCierre() {
            return fechaCierre;
        }

        public void setFechaCierre(Timestamp fechaCierre) {
            this.fechaCierre = fechaCierre;
        }
    }

    class ResultadoPack {

        int idCategoria;
        String descripcionCategoria;
        int idProducto;
        String nombreProducto;
        BigDecimal precioVenta;
        byte[] imagen;
        byte tipoImagen;
        String codColor;
        String codImagen;
        BigDecimal cantidad;
        boolean esModificado;
        boolean controlStock;
        int factorModificionPack;
        BigDecimal valorModifiacionPack;
        boolean visibleMontoModPack;

        public ResultadoPack() {

            idCategoria = 0;
            descripcionCategoria = "";
            idProducto = 0;
            nombreProducto = "";
            precioVenta = new BigDecimal(0);
            imagen = null;
            tipoImagen = 0;
            codColor = "";
            codImagen = "";
            cantidad = new BigDecimal(0);
            esModificado = false;

        }

        public boolean isControlStock() {
            return controlStock;
        }

        public void setControlStock(boolean controlStock) {
            this.controlStock = controlStock;
        }

        public int getFactorModificionPack() {
            return factorModificionPack;
        }

        public void setFactorModificionPack(int factorModificionPack) {
            this.factorModificionPack = factorModificionPack;
        }

        public BigDecimal getValorModifiacionPack() {
            return valorModifiacionPack;
        }

        public void setValorModifiacionPack(BigDecimal valorModifiacionPack) {
            this.valorModifiacionPack = valorModifiacionPack;
        }

        public boolean isVisibleMontoModPack() {
            return visibleMontoModPack;
        }

        public void setVisibleMontoModPack(boolean visibleMontoModPack) {
            this.visibleMontoModPack = visibleMontoModPack;
        }

        public boolean isEsModificado() {
            return esModificado;
        }

        public void setEsModificado(boolean esModificado) {
            this.esModificado = esModificado;
        }

        public BigDecimal getCantidad() {
            return cantidad;
        }

        public void setCantidad(BigDecimal cantidad) {
            this.cantidad = cantidad;
        }

        public int getIdCategoria() {
            return idCategoria;
        }

        public void setIdCategoria(int idCategoria) {
            this.idCategoria = idCategoria;
        }

        public String getDescripcionCategoria() {
            return descripcionCategoria;
        }

        public void setDescripcionCategoria(String descripcionCategoria) {
            this.descripcionCategoria = descripcionCategoria;
        }

        public int getIdProducto() {
            return idProducto;
        }

        public void setIdProducto(int idProducto) {
            this.idProducto = idProducto;
        }

        public String getNombreProducto() {
            return nombreProducto;
        }

        public void setNombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
        }

        public BigDecimal getPrecioVenta() {
            return precioVenta;
        }

        public void setPrecioVenta(BigDecimal precioVenta) {
            this.precioVenta = precioVenta;
        }

        public byte[] getImagen() {
            return imagen;
        }

        public void setImagen(byte[] imagen) {
            this.imagen = imagen;
        }

        public byte getTipoImagen() {
            return tipoImagen;
        }

        public void setTipoImagen(byte tipoImagen) {
            this.tipoImagen = tipoImagen;
        }

        public String getCodColor() {
            return codColor;
        }

        public void setCodColor(String codColor) {
            this.codColor = codColor;
        }

        public String getCodImagen() {
            return codImagen;
        }

        public void setCodImagen(String codImagen) {
            this.codImagen = codImagen;
        }
    }

    public class ProductoModificador {
        int tipo;
        int idPri;
        int idSec;
        String descripcion;
        boolean Estado;

        public ProductoModificador() {
            tipo = 0;
            idPri = 0;
            idSec = 0;
            descripcion = "";
            Estado = false;
        }

        public int getTipo() {
            return tipo;
        }

        public void setTipo(int tipo) {
            this.tipo = tipo;
        }

        public int getIdPri() {
            return idPri;
        }

        public void setIdPri(int idPri) {
            this.idPri = idPri;
        }

        public int getIdSec() {
            return idSec;
        }

        public void setIdSec(int idSec) {
            this.idSec = idSec;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public boolean isEstado() {
            return Estado;
        }

        public void setEstado(boolean estado) {
            Estado = estado;
        }
    }

    class ModificadoresItems {
        int idModificador;
        String DescripcionMod;
        int idDetalle;
        String DescDetalle;

        public int getIdModificador() {
            return idModificador;
        }

        public void setIdModificador(int idModificador) {
            this.idModificador = idModificador;
        }

        public String getDescripcionMod() {
            return DescripcionMod;
        }

        public void setDescripcionMod(String descripcionMod) {
            DescripcionMod = descripcionMod;
        }

        public int getIdDetalle() {
            return idDetalle;
        }

        public void setIdDetalle(int idDetalle) {
            this.idDetalle = idDetalle;
        }

        public String getDescDetalle() {
            return DescDetalle;
        }

        public void setDescDetalle(String descDetalle) {
            DescDetalle = descDetalle;
        }
    }

    public class RespuestaEliminar {

        int cantidad;
        byte respuesta;

        public RespuestaEliminar() {
            cantidad = 0;
            respuesta = 0;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public byte getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(byte respuesta) {
            this.respuesta = respuesta;
        }
    }

    public class RetornoUsuario {

        int numeroUsuario;
        byte respuesta;

        public RetornoUsuario(int numeroUsuario, byte respuesta) {
            this.numeroUsuario = numeroUsuario;
            this.respuesta = respuesta;
        }

        public int getNumeroUsuario() {
            return numeroUsuario;
        }

        public byte getRespuesta() {
            return respuesta;
        }
    }
}



















