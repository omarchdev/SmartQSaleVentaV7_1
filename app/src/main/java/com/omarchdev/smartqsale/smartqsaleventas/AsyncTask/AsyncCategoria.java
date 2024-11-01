package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.TIPO_CONSULTA;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.CategoriaDefault;
import com.omarchdev.smartqsale.smartqsaleventas.Model.RepuestaEliminar;
import com.omarchdev.smartqsale.smartqsaleventas.Model.ResultProcces;
import com.omarchdev.smartqsale.smartqsaleventas.Model.SolicitudEnvio;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mAreaProduccion;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCategoriaProductos;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUnidadMedida;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IAreasProduccionRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.ICategoriaRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IProductoRepository;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.UnidadMedidaRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by OMAR CHH on 20/02/2018.
 */

public class AsyncCategoria {

    public List<mCategoriaProductos> listCategoria;
    //  BdConnectionSql bdConnectionSql;
    ListenerCategoria listenerCategoria;
    ListenerProcesoCategoria listenerProcesoCategoria;
    GuardarCategoria guardarCategoria;
    EliminarCategoria eliminarCategoria;
    EditarCategoria editarCategoria;
    Context context;
    AsyncAreasProduccion.ListenerAreasProduccion listenerAreasProduccion;
    GetCategoriaProducto getCategoriaProducto;
    boolean descargarUnidadesMedida;
    ListenerCantidadMaximaPedido listenerCantidadMaximaPedido;
    private ListenerResultCambioCategoriaDefecto listenerResultCambioCategoriaDefecto;
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    ICategoriaRepository iCategoriaRepository = retro.create(ICategoriaRepository.class);
    UnidadMedidaRepository iUnidadMedidaRepository = retro.create(UnidadMedidaRepository.class);
    IAreasProduccionRepository iAreasProduccionRepository = retro.create(IAreasProduccionRepository.class);
    IProductoRepository iProductoRepository = retro.create(IProductoRepository.class);
    String ciaCode = GetJsonCiaTiendaBase64x3();

    public void setListenerCantidadMaximaPedido(ListenerCantidadMaximaPedido listenerCantidadMaximaPedido) {
        this.listenerCantidadMaximaPedido = listenerCantidadMaximaPedido;
    }

    public void setListenerAreasProduccion(AsyncAreasProduccion.ListenerAreasProduccion listenerAreasProduccion) {
        this.listenerAreasProduccion = listenerAreasProduccion;
    }

    public void setListenerResultCambioCategoriaDefecto(ListenerResultCambioCategoriaDefecto listenerResultCambioCategoriaDefecto) {
        this.listenerResultCambioCategoriaDefecto = listenerResultCambioCategoriaDefecto;
    }

    public void setListenerProcesoCategoria(ListenerProcesoCategoria listenerProcesoCategoria) {

        this.listenerProcesoCategoria = listenerProcesoCategoria;

    }

    public void getCategorias() {

        getCategoriaProducto = new GetCategoriaProducto();
        getCategoriaProducto.execute();

    }

    public void setListenerCategoria(ListenerCategoria listenerCategoria) {
        this.listenerCategoria = listenerCategoria;
    }


    public AsyncCategoria() {
    /*    descargarUnidadesMedida=false;
        bdConnectionSql=BdConnectionSql.getSinglentonInstance();
        iCategoriaRepository= retro.create(ICategoriaRepository.class);
*/
    }

    public void setDescargarUnidadesMedida(boolean descargarUnidadesMedida) {
        this.descargarUnidadesMedida = descargarUnidadesMedida;
    }

    public void cancelObtenerCategorias() {
        if (getCategoriaProducto != null) {

            getCategoriaProducto.cancel(false);

        }
    }

    public void GuardarCategoria(String descripcion) {

        guardarCategoria = new GuardarCategoria();
        guardarCategoria.execute(descripcion);

    }

    public void ModificaCategoriaDefecto(int idCategoria, boolean usoCategoria) {
        ModCategoriaDefecto mod = new ModCategoriaDefecto();
        mod.setIdCategoria(idCategoria);
        mod.setUsaCategoria(usoCategoria);
        mod.execute();
    }

    public void CancelarEditar() {
        if (editarCategoria != null) {

            editarCategoria.cancel(true);
        }
    }

    public void EditarCategoriaId(int idCategoria, String descripcion) {

        try {
            editarCategoria = new EditarCategoria();
            editarCategoria.setIdCategoria(idCategoria);
            editarCategoria.setDescripcionCategoria(descripcion);
            editarCategoria.execute();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();


        }
    }

    public interface ListenerProcesoCategoria {

        //respuesta Guardar  100-> exito   99->error  98->error conexion
        public void RespuestaGuardarCategoria(byte respuestaGuardar);

        public void RespuestaEliminarCategoria(RepuestaEliminar respuestaEliminar);

        public void RespuestaEditarCategoria(byte respuesta);

    }

    public interface ListenerCantidadMaximaPedido {
        public void CantidadMaxima(double cantidadMaxima);
    }

    public interface ListenerCategoria {

        public void CategoriasObtenidas(List<mCategoriaProductos> categoriaProductosList);

        public void ObtenerUnidadesMedidad(List<mUnidadMedida> listaUnidades);


    }

    public interface ListenerResultCambioCategoriaDefecto {
        public void ActualizoCategoriaDefecto(String mensajeResultado);

        public void ErrorActualizar(String mensajeResultado);
    }

    protected class GuardarCategoria extends AsyncTask<String, Void, Byte> {

        @Override
        protected Byte doInBackground(String... strings) {
            try {

                SolicitudEnvio<String> sol = new SolicitudEnvio<>(
                        ciaCode, TIPO_CONSULTA,
                        strings[0], Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario);
                return iCategoriaRepository.AgregarCategoria(sol).execute().body();

            } catch (Exception ex) {
                return 0;
            }
            // return bdConnectionSql.AgregarCategoria(strings[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (listenerProcesoCategoria != null) {
                listenerProcesoCategoria.RespuestaGuardarCategoria(aByte);
            }

        }
    }

    /*
        private class GetCategoriaProductoVenta extends AsyncTask<Void, Void, List<mCategoriaProductos>> {
            List<mUnidadMedida> listaUnidad;
            List<mAreaProduccion> listaAreas;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                listCategoria = new ArrayList<>();
            }

            @Override
            protected List<mCategoriaProductos> doInBackground(Void... voids) {
                if (descargarUnidadesMedida) {
                    listaUnidad = bdConnectionSql.ObtenerUnidadesMedida();
                    listaAreas = bdConnectionSql.getAreasProduccion();
                }
                return bdConnectionSql.getCategoriasVenta(0, "");
            }

            @Override
            protected void onPostExecute(List<mCategoriaProductos> mCategoriaProductos) {
                super.onPostExecute(mCategoriaProductos);
                if (listenerCategoria != null) {
                    if (descargarUnidadesMedida) {
                        listenerCategoria.ObtenerUnidadesMedidad(listaUnidad);
                    }
                    if (listenerAreasProduccion != null) {
                        if (listaAreas != null) {
                            listenerAreasProduccion.ResultadosBusquedaArea(listaAreas);
                        } else {
                            listenerAreasProduccion.ErrorConsultaAreas();
                        }
                    }
                    listenerCategoria.CategoriasObtenidas(mCategoriaProductos);
                }
            }


        }
    */
    private class GetCategoriaProducto extends AsyncTask<Void, Void, List<mCategoriaProductos>> {
        List<mUnidadMedida> listaUnidad;
        List<mAreaProduccion> listaAreas;
        double cantidadMaxima;

        public GetCategoriaProducto() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listCategoria = new ArrayList<>();
        }

        @Override
        protected List<mCategoriaProductos> doInBackground(Void... voids) {
            if (descargarUnidadesMedida) {
                try {
                    listaUnidad = iUnidadMedidaRepository.ObtenerUnidadesMedida(ciaCode, TIPO_CONSULTA).execute().body();
                    listaAreas = iAreasProduccionRepository.GetAreasProduccion(TIPO_CONSULTA, ciaCode).execute().body();
                    cantidadMaxima = iProductoRepository.CantidadMaximaPedidoWeb(ciaCode, TIPO_CONSULTA).execute().body().doubleValue();
                } catch (Exception ex) {
                    listaUnidad = new ArrayList<>();
                    listaAreas = new ArrayList<>();
                    cantidadMaxima = 0;

                }

            }

            ICategoriaRepository iCategoriaRepository = retro.create(ICategoriaRepository.class);
            String temp = GetJsonCiaTiendaBase64x3();
            Call<List<mCategoriaProductos>> result = iCategoriaRepository.GetCategoriaVentas("2", temp);
            List<mCategoriaProductos> categorias = new ArrayList<>();
            try {
                Response<List<mCategoriaProductos>> responseCategorias = result.execute();
                if (responseCategorias.isSuccessful()) {
                    List<mCategoriaProductos> list = responseCategorias.body();
                    list.size();
                    categorias.addAll(list);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.toString();
            }
            return categorias;
            //          return bdConnectionSql.getCategorias(0, "");
        }

        @Override
        protected void onPostExecute(List<mCategoriaProductos> mCategoriaProductos) {
            super.onPostExecute(mCategoriaProductos);
            if (listenerCantidadMaximaPedido != null) {
                listenerCantidadMaximaPedido.CantidadMaxima(cantidadMaxima);
            }
            if (listenerCategoria != null) {
                if (descargarUnidadesMedida) {
                    listenerCategoria.ObtenerUnidadesMedidad(listaUnidad);
                }
                if (listenerAreasProduccion != null) {
                    if (listaAreas != null) {
                        listenerAreasProduccion.ResultadosBusquedaArea(listaAreas);
                    } else {
                        listenerAreasProduccion.ErrorConsultaAreas();
                    }
                }
                listenerCategoria.CategoriasObtenidas(mCategoriaProductos);
            }
        }


    }

    private class ModCategoriaDefecto extends AsyncTask<Void, Void, ResultProcces> {
        private int idCategoria;
        private boolean usaCategoria;

        ModCategoriaDefecto() {
            idCategoria = 0;
            usaCategoria = false;
        }

        public void setIdCategoria(int idCategoria) {
            this.idCategoria = idCategoria;
        }

        public void setUsaCategoria(boolean usaCategoria) {
            this.usaCategoria = usaCategoria;
        }

        @Override
        protected ResultProcces doInBackground(Void... voids) {
            try {
                CategoriaDefault cat = new CategoriaDefault(idCategoria, usaCategoria);
                String ciaCode = GetJsonCiaTiendaBase64x3();
                SolicitudEnvio<CategoriaDefault> sol = new SolicitudEnvio(
                        ciaCode, TIPO_CONSULTA, cat, Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario
                );
                return iCategoriaRepository.ActualizaCategoriaDefecto(sol).execute().body();
            } catch (Exception ex) {
                return new ResultProcces();
            }

            //   return bdConnectionSql.ActualizaCategoriaDefecto(usaCategoria, idCategoria);
        }

        @Override
        protected void onPostExecute(ResultProcces resultProcces) {
            super.onPostExecute(resultProcces);
            if (listenerResultCambioCategoriaDefecto != null) {
                if (resultProcces.getCodeResult() == 200) {
                    listenerResultCambioCategoriaDefecto.ActualizoCategoriaDefecto(resultProcces.getMessageResult());
                } else {
                    listenerResultCambioCategoriaDefecto.ErrorActualizar(resultProcces.getMessageResult());
                }
            }


        }
    }

    public void setContext(Context context) {

        this.context = context;

    }

    private class EditarCategoria extends AsyncTask<Void, Void, Byte> {

        int idCategoria;
        String descripcionCategoria;

        public void setIdCategoria(int idCategoria) {
            this.idCategoria = idCategoria;
        }

        public void setDescripcionCategoria(String descripcionCategoria) {
            this.descripcionCategoria = descripcionCategoria;
        }

        @Override
        protected Byte doInBackground(Void... voids) {

            try {
                String ciaCode = GetJsonCiaTiendaBase64x3();
                mCategoriaProductos categoriaProductos = new mCategoriaProductos();
                categoriaProductos.setIdCategoria(idCategoria);
                categoriaProductos.setDescripcionCategoria(descripcionCategoria);
                SolicitudEnvio<mCategoriaProductos> sol = new SolicitudEnvio(
                        ciaCode, TIPO_CONSULTA, categoriaProductos, Constantes.Terminal.idTerminal, Constantes.Usuario.idUsuario
                );
                Log.d("cat_print", new Gson().toJson(sol));
                return iCategoriaRepository.EditarCategoria(sol).execute().body().byteValue();
            } catch (Exception ex) {
                return 1;
            }

            //  return bdConnectionSql.ModificarCategoria(idCategoria,descripcionCategoria);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (listenerProcesoCategoria != null) {
                listenerProcesoCategoria.RespuestaEditarCategoria(aByte);
            }
        }
    }


    public void EliminarCategoriaPorId(int idCategoria) {

        eliminarCategoria = new EliminarCategoria();
        eliminarCategoria.execute(idCategoria);


    }

    private class EliminarCategoria extends AsyncTask<Integer, Void, RepuestaEliminar> {

        @Override
        protected RepuestaEliminar doInBackground(Integer... integers) {
            try{
                return  iCategoriaRepository.EliminarCategoria(TIPO_CONSULTA,ciaCode,integers[0],Constantes.Terminal.idTerminal,Constantes.Usuario.idUsuario).execute().body();

            }catch (Exception ex){
                RepuestaEliminar res=new RepuestaEliminar();
                res.setRespuesta((byte)98);
                res.setCantidad(0);
                return  res;
            }
        }

        @Override
        protected void onPostExecute(RepuestaEliminar respuestaEliminar) {
            super.onPostExecute(respuestaEliminar);
                if (listenerProcesoCategoria != null) {
                listenerProcesoCategoria.RespuestaEliminarCategoria(respuestaEliminar);
            }
        }
    }

}
