package com.omarchdev.smartqsale.smartqsaleventas.Controlador;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.util.Log;

import com.google.gson.Gson;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.EntregaPedidoInfo;
import com.omarchdev.smartqsale.smartqsaleventas.Model.EstadoEntregaPedidoEnUso;
import com.omarchdev.smartqsale.smartqsaleventas.Model.EstadoPagoEnPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Pedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.PedidoCambioEstado;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProcessResult;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ProductoEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCabeceraPedido;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mPagosEnVenta;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPagoRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IPedidoRespository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by OMAR CHH on 07/11/2017.
 */

public class ControladorVentas {
    final String codeCia = GetJsonCiaTiendaBase64x3();

    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IPedidoRespository iPedidoRespository = retro.create(IPedidoRespository.class);
    IPagoRepository iPagoRepository = retro.create(IPagoRepository.class);
    BdConnectionSql bdConnectionSql;

    public ControladorVentas() {
        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
                Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }).create();
            retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        }*/
        bdConnectionSql = BdConnectionSql.getSinglentonInstance();
    }

    public int verificarExistePedido() {
        return bdConnectionSql.VerificarExistePedido();
    }

    public Pedido getPedidoUltimoPedido() {
        return bdConnectionSql.GetUltimoPedido();
    }

    public mCabeceraPedido getCabeceraUltimoPedido(int id) {
        return bdConnectionSql.getCabeceraPedidoPorId(id);
    }

    public EntregaPedidoInfo GetEntregaPedidoInfo(int idPedido) {
        EntregaPedidoInfo entregaPedidoInfo = new EntregaPedidoInfo();
        try {
            Response<EntregaPedidoInfo> responseEntregaPedido = iPedidoRespository.GetDatoEntregaPedido(codeCia, TIPO_CONSULTA, idPedido).execute();
            if (responseEntregaPedido.code() == 200) {
                entregaPedidoInfo = responseEntregaPedido.body();
                //EntregaPedidoInfo entregaPedidoInfo=bdConnectionSql.GetDatoEntregaPedido(idPedido);
            } else {
                Log.d("Adver", "Error entrega pedido");
            }
        } catch (Exception ex) {
            ex.toString();
        }
        return entregaPedidoInfo;
    }

    public List<mPagosEnVenta> getPagosRealizadosDetallePedido(int idCabeceraPedido) {
        try{
           List<mPagosEnVenta> list= iPagoRepository.GetPagosPedido(codeCia,TIPO_CONSULTA,idCabeceraPedido).execute().body();
            return list;
        }catch (Exception ex){
            return new ArrayList<>();
        }
    }

    //Obtener pedido despues de dar click al boton recuperar pedido(Boton verde del item) del listado historial pedido
    public Pedido ObtenerPedidoId(int idPedido, boolean pagado) {
        try {
            Response<Pedido> responsePedido = iPedidoRespository.GetPedidoId(codeCia, TIPO_CONSULTA, idPedido, Constantes.ConfigTienda.idTipoZonaServicio, pagado).execute();
            Pedido pedido;
            if (responsePedido.code() == 200) {
                pedido = responsePedido.body();
            } else {
                pedido = new Pedido();
                pedido.getCabeceraPedido().setIdCabecera(0);
            }
            Response<List<mPagosEnVenta>> responsePagosVenta = iPagoRepository.GetPagosPedidoV2(codeCia, "2", idPedido).execute();
            List<mPagosEnVenta> pagosEnVentas = null;
            if (responsePagosVenta.code() == 200) {
                pagosEnVentas = responsePagosVenta.body();
            } else {

            }
            //      List<mPagosEnVenta> pagos= bdConnectionSql.getPagosRealizadosDetallePedido(idPedido);
            pedido.setPagosEnPedido((ArrayList<mPagosEnVenta>) pagosEnVentas);
            if (pedido.getIdEntregaPedido() != 0) {
                Response<EntregaPedidoInfo> responseEntregaPedido = iPedidoRespository.GetDatoEntregaPedido(codeCia, TIPO_CONSULTA, idPedido).execute();
                if (responseEntregaPedido.code() == 200) {
                    EntregaPedidoInfo entregaPedidoInfo = responseEntregaPedido.body();
                    //EntregaPedidoInfo entregaPedidoInfo=bdConnectionSql.GetDatoEntregaPedido(idPedido);
                    pedido.setEntregaPedidoInfo(entregaPedidoInfo);
                } else {
                    Log.d("Adver", "Error entrega pedido");
                }

                Response<List<EstadoPagoEnPedido>> responseEstadoFlujoPagoPedido = iPedidoRespository.GetEstadoFlujoPagoPedido(codeCia, "2", idPedido).execute();
                //   List<EstadoPagoEnPedido> listFlujoPagos=bdConnectionSql.GetEstadoFlujoPagoPedido(idPedido);
                List<EstadoPagoEnPedido> listFlujoPagos = new ArrayList<>();
                if (responseEstadoFlujoPagoPedido.code() == 200) {
                    listFlujoPagos = responseEstadoFlujoPagoPedido.body();
                } else {
                    Log.d("Adver", "Error obtener flujo pagos");
                }
                pedido.getFlujoPagoPedido().setListEstadoPago((ArrayList<EstadoPagoEnPedido>) listFlujoPagos);
                Response<List<EstadoEntregaPedidoEnUso>> responseEstadoEntregaPedidoEnUso = iPedidoRespository.GetEstadoFlujoPedido(codeCia, "2", idPedido).execute();
                //   List<EstadoEntregaPedidoEnUso> listEntrega=bdConnectionSql.GetEstadoFlujoPedido(idPedido);
                List<EstadoEntregaPedidoEnUso> listEntrega = new ArrayList<>();
                if (responseEstadoEntregaPedidoEnUso.code() == 200) {
                    listEntrega = responseEstadoEntregaPedidoEnUso.body();
                } else {
                    Log.d("Adver", "Error obtener flujo pedido");
                }
                pedido.getFlujoEntrega().setListEstadoEntregaPedido((ArrayList<EstadoEntregaPedidoEnUso>) listEntrega);
                // bdConnectionSql.GetDatoEntregaPedido(idPedido);

            }
            return pedido;
        } catch (IOException ex1) {
            ex1.printStackTrace();
            return new Pedido();
        } catch (Exception ex2) {
            return new Pedido();
        }

    }

    public List<ProductoEnVenta> getDetallePedidoId(int id) {
        try {
            List<ProductoEnVenta> list = iPedidoRespository.GetDetallePedidoId(codeCia, TIPO_CONSULTA, id).execute().body();
            for(int i=0;i<list.size();i++){
                list.get(i).ConvierteFechaJSON();
            }
            return list;


        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList();
        }
//        return bdConnectionSql.ObtenerDetallePedidoPorId(id);
    }

    /*  public int CambiarEstadoPedido(int idCabeceraPedido, String identificador, String observacion, String identificador2, String fechaEntrega) {

          return bdConnectionSql.ModificarEstadoPedido(idCabeceraPedido, identificador, observacion,
                  mCabeceraPedido.EstadoPedido.R.toString(), identificador2, fechaEntrega);

      }
  */
    public int CambiarEstadoPedidoSuspender(int idCabeceraPedido) {
        SolicitudEnvio<Integer> solicitudEnvio = new SolicitudEnvio<>(codeCia,
                "2", idCabeceraPedido,
                Constantes.Terminal.idTerminal,
                Constantes.Usuario.idUsuario);
        int code = 0;
        try {
            ProcessResult<Integer> result = iPedidoRespository.AnularPedido(solicitudEnvio).execute().body();
            code = result.getCodeResult();
        } catch (IOException e) {
            code = 0;
        } catch (Exception ex) {
            code = 0;
        }
        return code;

    }

    //PRUEBA API
    public void CambioPedidoActualPorReservado(int idPedidoActual, int idPedidoRecuperar) {
        PedidoCambioEstado pedidoCambioEstado = new PedidoCambioEstado(idPedidoActual, idPedidoRecuperar);

        SolicitudEnvio<PedidoCambioEstado> solicitudEnvio = new SolicitudEnvio<>(codeCia,
                "2", pedidoCambioEstado,
                Constantes.Terminal.idTerminal,
                Constantes.Usuario.idUsuario);

        Gson gson = new Gson();
        String JSONS = gson.toJson(solicitudEnvio);

        int code = 0;
        try {
            Response<Boolean> response = iPedidoRespository.CambioPedidoActualPorReservado(solicitudEnvio).execute();
            if (response.code() == 200) {

            } else {

            }

        } catch (IOException e) {
            code = 0;
        } catch (Exception ex) {
            code = 0;
        }
        //   bdConnectionSql.CambioPedidoActualPorReservado( idPedidoActual,idPedidoRecuperar);

    }
    @Deprecated
    public int CambiarEstadoPedidoTemporal(int idCabeceraPedido) {
        return bdConnectionSql.ModificarEstadoPedidoSuspendido(idCabeceraPedido, mCabeceraPedido.EstadoPedido.T.toString());
    }
    @Deprecated
    public mCabeceraPedido ObtenerNuevoPedido() {

        return bdConnectionSql.GenerarNuevoPedido();
    }

    @Deprecated
    public int GenerarNuevoPedido() {

        return bdConnectionSql.generarNuevoPedido();
    }

    public int GuardarValorVenta(int id, mCabeceraPedido cabeceraPedido) {

        return bdConnectionSql.GuardarTotalPagoCabeceraPedido(id, cabeceraPedido);
    }


    public void GuardarCabeceraPedido(int id, mVendedor vendedor, mCustomer customer) {
        bdConnectionSql.GuardarCabeceraPedido(id, vendedor, customer);
    }

    public void GuardarProductoDetallePedido(int idCabecera, ProductoEnVenta productoEnVenta, char c) {

        bdConnectionSql.GuardarDetallePedido(idCabecera, productoEnVenta, c);
    }

    public void EditarCantidadProducto(int idCabeceraDetella, int idDetallePedido, float cantidad) {
        //  bdConnectionSql.EditarCantidadProducto(idCabeceraDetella,idDetallePedido,cantidad);
    }


    public boolean GetEstadoPedido(int id) {
        try {
            return iPedidoRespository.GetEstadoPedido(codeCia, "2", id).execute().body();
        } catch (IOException e) {
            return false;
        }
    }

    public List<mCabeceraPedido> getListaPedidosReserva(int id, int fechaInicio, int fechaFinal, String tipo, String nroPedido) {

        try {
            return iPedidoRespository.
                    GetPedidosReserva(codeCia, "2", fechaInicio, fechaFinal, Constantes.ConfigTienda.idTipoZonaServicio, tipo, nroPedido, id).execute().body();
        } catch (IOException e) {
            return new ArrayList<>();
        }
        //      return bdConnectionSql.ObtenerCabecerasPedidos(fechaInicio, fechaFinal, id,tipo,nroPedido);
    }


}
