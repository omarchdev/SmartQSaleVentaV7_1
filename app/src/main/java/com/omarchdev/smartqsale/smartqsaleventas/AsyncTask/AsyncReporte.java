package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAlmacenProducto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCierre;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedorProducto;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVentasVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IAlmacenesRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICierreRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVendedorRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsyncReporte {

    private final String codeCia = GetJsonCiaTiendaBase64x3();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constantes.BASECONN.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final IVendedorRepository vendedorRepository = retrofit.create(IVendedorRepository.class);
    private final IAlmacenesRepository almacenesRepository = retrofit.create(IAlmacenesRepository.class);
    private final ICierreRepository cierreRepository = retrofit.create(ICierreRepository.class);

    // ListenerReportePVendedor
    ListenerReportePVendedor listenerReportePVendedor;
    public void setListenerReportePVendedor(ListenerReportePVendedor listenerReportePVendedor){
        this.listenerReportePVendedor=listenerReportePVendedor;
    }
    public interface ListenerReportePVendedor{
        void ErrorResultado();
        void ResultadoReporte(List<mVendedorProducto> mVendedorProductoList);
    }
    public void ObtenerReporteVendedor(int idVendedor, String fechaIni, String fechaFin, int tipoReporte){
        // Método deprecado/comentado originalmente, no realiza operaciones.
    }

    // ResultadoVentasVendedor
    ResultadoVentasVendedor resultadoVentasVendedor;
    public void setResultadoVentasVendedor(ResultadoVentasVendedor resultadoVentasVendedor){
        this.resultadoVentasVendedor=resultadoVentasVendedor;
    }
    public interface ResultadoVentasVendedor{
        void ErrorConsulta();
        void ResultadosConsulta(List<mVentasVendedor> ventasVendedors);
    }
    public void ObtenerVentasVendedor(int idVendedor, String desde, String hasta){
        vendedorRepository.ObtenerVentasPorVendedor(idVendedor, desde, hasta, Constantes.BASECONN.TIPO_CONSULTA, codeCia)
                .enqueue(new Callback<List<mVentasVendedor>>() {
                    @Override
                    public void onResponse(Call<List<mVentasVendedor>> call, Response<List<mVentasVendedor>> response) {
                        if (resultadoVentasVendedor != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                resultadoVentasVendedor.ResultadosConsulta(response.body());
                            } else {
                                resultadoVentasVendedor.ErrorConsulta();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<mVentasVendedor>> call, Throwable t) {
                        if (resultadoVentasVendedor != null) {
                            resultadoVentasVendedor.ErrorConsulta();
                        }
                    }
                });
    }

    // ListenerVentasPorCierre
    ListenerVentasPorCierre listenerVentasPorCierre;
    public void setListenerVentasPorCierre(ListenerVentasPorCierre listenerVentasPorCierre){
        this.listenerVentasPorCierre=listenerVentasPorCierre;
    }
    public interface ListenerVentasPorCierre{
        void ErrorConsulta();
        void ResultadoVentasPorCierre(List<mVentasVendedor> listaResultado, mCierre cierre);
    }
    public void ObtenerVentasPorCierre(int idCierre){
        cierreRepository.getCabeceraCierreCaja(idCierre, Constantes.BASECONN.TIPO_CONSULTA, codeCia)
                .enqueue(new Callback<mCierre>() {
                    @Override
                    public void onResponse(Call<mCierre> call, Response<mCierre> responseCabecera) {
                        if (responseCabecera.isSuccessful() && responseCabecera.body() != null) {
                            final mCierre cierre = responseCabecera.body();
                            vendedorRepository.ObtenerVentasPorCierre(idCierre, Constantes.BASECONN.TIPO_CONSULTA, codeCia)
                                    .enqueue(new Callback<List<mVentasVendedor>>() {
                                        @Override
                                        public void onResponse(Call<List<mVentasVendedor>> call, Response<List<mVentasVendedor>> responseVentas) {
                                            if (listenerVentasPorCierre != null) {
                                                if (responseVentas.isSuccessful() && responseVentas.body() != null) {
                                                    listenerVentasPorCierre.ResultadoVentasPorCierre(responseVentas.body(), cierre);
                                                } else {
                                                    listenerVentasPorCierre.ErrorConsulta();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<mVentasVendedor>> call, Throwable t) {
                                            if (listenerVentasPorCierre != null) {
                                                listenerVentasPorCierre.ErrorConsulta();
                                            }
                                        }
                                    });
                        } else {
                            if (listenerVentasPorCierre != null) {
                                listenerVentasPorCierre.ErrorConsulta();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<mCierre> call, Throwable t) {
                        if (listenerVentasPorCierre != null) {
                            listenerVentasPorCierre.ErrorConsulta();
                        }
                    }
                });
    }
    public void ObtenerCabeceraCierre(int idCierre){
        cierreRepository.getCabeceraCierreCaja(idCierre, Constantes.BASECONN.TIPO_CONSULTA, codeCia)
                .enqueue(new Callback<mCierre>() {
                    @Override
                    public void onResponse(Call<mCierre> call, Response<mCierre> response) {
                        if (listenerVentasPorCierre != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                listenerVentasPorCierre.ResultadoVentasPorCierre(null, response.body());
                            } else {
                                listenerVentasPorCierre.ErrorConsulta();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<mCierre> call, Throwable t) {
                        if (listenerVentasPorCierre != null) {
                            listenerVentasPorCierre.ErrorConsulta();
                        }
                    }
                });
    }

    // ListenerReporteDetalleVentaCierre
    ListenerReporteDetalleVentaCierre listenerReporteDetalleVentaCierre;
    public void setListenerReporteDetalleVentaCierre(ListenerReporteDetalleVentaCierre listenerReporteDetalleVentaCierre){
        this.listenerReporteDetalleVentaCierre=listenerReporteDetalleVentaCierre;
    }
    public interface ListenerReporteDetalleVentaCierre{
        void ResultadoReporteCierre(List<mVendedorProducto> listaReporte);
        void ErrorConsultaReporte();
    }
    public void ObtenerAcumuladoVentasCierre(int idCierre, int idVendedor){
        vendedorRepository.ObtenerAcumuladoVentasCierre(idCierre, idVendedor, Constantes.BASECONN.TIPO_CONSULTA, codeCia)
                .enqueue(new Callback<List<mVendedorProducto>>() {
                    @Override
                    public void onResponse(Call<List<mVendedorProducto>> call, Response<List<mVendedorProducto>> response) {
                        if (listenerReporteDetalleVentaCierre != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                listenerReporteDetalleVentaCierre.ResultadoReporteCierre(response.body());
                            } else {
                                listenerReporteDetalleVentaCierre.ErrorConsultaReporte();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<mVendedorProducto>> call, Throwable t) {
                        if (listenerReporteDetalleVentaCierre != null) {
                            listenerReporteDetalleVentaCierre.ErrorConsultaReporte();
                        }
                    }
                });
    }
    public void ObtenerDetalleVentasCierre(int idCierre, int idVendedor){
        vendedorRepository.ObtenerDetalleVentasCierre(idCierre, idVendedor, Constantes.BASECONN.TIPO_CONSULTA, codeCia)
                .enqueue(new Callback<List<mVendedorProducto>>() {
                    @Override
                    public void onResponse(Call<List<mVendedorProducto>> call, Response<List<mVendedorProducto>> response) {
                        if (listenerReporteDetalleVentaCierre != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                listenerReporteDetalleVentaCierre.ResultadoReporteCierre(response.body());
                            } else {
                                listenerReporteDetalleVentaCierre.ErrorConsultaReporte();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<mVendedorProducto>> call, Throwable t) {
                        if (listenerReporteDetalleVentaCierre != null) {
                            listenerReporteDetalleVentaCierre.ErrorConsultaReporte();
                        }
                    }
                });
    }

    // ReporteAlmacen
    ReporteAlmacen reporteAlmacen;
    public void setReporteAlmacen(ReporteAlmacen reporteAlmacen){
        this.reporteAlmacen=reporteAlmacen;
    }
    public interface ReporteAlmacen{
        void ObtenerReporteAlmacen(List<mAlmacenProducto> listaReporte);
        void ErrorObtenerAlmacen();
    }
    public void ObtenerReporteProductosAlmacen(int idAlmacen){
        almacenesRepository.ObtenerReporteProductosAlmacen(idAlmacen, Constantes.BASECONN.TIPO_CONSULTA, codeCia)
                .enqueue(new Callback<List<mAlmacenProducto>>() {
                    @Override
                    public void onResponse(Call<List<mAlmacenProducto>> call, Response<List<mAlmacenProducto>> response) {
                        if (reporteAlmacen != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                reporteAlmacen.ObtenerReporteAlmacen(response.body());
                            } else {
                                reporteAlmacen.ErrorObtenerAlmacen();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<mAlmacenProducto>> call, Throwable t) {
                        if (reporteAlmacen != null) {
                            reporteAlmacen.ErrorObtenerAlmacen();
                        }
                    }
                });
    }
}
