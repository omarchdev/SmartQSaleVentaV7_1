package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import android.os.AsyncTask;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.OpcionVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.RespuestaExisVariante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.Variante;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;

import java.util.List;

/**
 * Created by OMAR CHH on 21/03/2018.
 */

public class AsyncVariantes {

    VariantesProduct variantesProduct;
    ListenerVariante listenerVariante;
    ConfigVariantes configVariantes;
    ListenerVerificarExisteVariante listenerVerificarExisteVariante;
    ControladorVariante controladorVariante;



    public void setListenerVerificarExisteVariante(ListenerVerificarExisteVariante listenerVerificarExisteVariante) {
        this.listenerVerificarExisteVariante = listenerVerificarExisteVariante;
    }

    public void setListenerVariante(ListenerVariante listenerVariante) {
        this.listenerVariante = listenerVariante;
    }

    public void setConfigVariantes(ConfigVariantes configVariantes) {
        this.configVariantes = configVariantes;
    }

    public void setVariantesProduct(VariantesProduct variantesProduct) {

        this.variantesProduct = variantesProduct;

    }

    public interface ListenerVariante {
        public void VariantesProducto(List<Variante> varianteList);

    }

    public interface ListenerVerificarExisteVariante {

        public void PermitirEliminar();

        public void NoPermitirEliminar(String mensaje);

    }

    public interface ConfigVariantes {

        public void ObtenerConfiguracionVariantes(mProduct product);

        public void ResultadoActualizarVariante(byte resultado);

        public void ResultadoEliminarVariante(byte resultado);

        public void GenerarVariantes(List<Variante> varianteList);

        public void ResultadoGuardarOpcion(OpcionVariante opcionVariante);

        public void ResultadoEliminarOpcion(byte respuesta);

        public void ResultadoGuardarValores(OpcionVariante opcionVariante);

        public void ActualizarEstadoVariante(byte respuesta);
    }

    public interface VariantesProduct {

        public void ValoresObtenidos(List<OpcionVariante> opcionVarianteList);
    }

    BdConnectionSql bdConnectionSql = BdConnectionSql.getSinglentonInstance();

    public AsyncVariantes() {

        controladorVariante=new ControladorVariante();
    }

    public void GenerarVariantes(int idProduct) {

        new GenerarVariantes().execute(idProduct);
    }

    public void ActualizarVariantePorId(Variante variante) {
        new ActualizarVarianteId().execute(variante);
    }

    public void ObtenerConfigVariantes(int idProduct) {
        new GetConfigVariantes().execute(idProduct);
    }

    public void getVariantesProduct(int idProduct) {
        new GetVariantesProducto().execute(idProduct);
    }

    public void EliminarOpcion(OpcionVariante opcionVariante) {
        new EliminarOpcion().execute(opcionVariante);
    }

    public void ObtenerValoresVariante(int idProduct) {

        new GetOpcionesValores().execute(idProduct);

    }

    public void EliminarVariante(int idProduct) {

        new EliminarVariante().execute(idProduct);
    }

    public void GuardarOpcion(OpcionVariante opcionVariante) {

        new GuardarOpcion().execute(opcionVariante);
    }

    public void GuardarValorOpcion(OpcionVariante opcionVariante) {

        new GuardarValoresOpciones().execute(opcionVariante);
    }

    public void VerificarExisteVariante(int idProducto) {
        new VerificarExistenciaVariante().execute(idProducto);
    }

    public class VerificarExistenciaVariante extends AsyncTask<Integer, Void, RespuestaExisVariante> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected RespuestaExisVariante doInBackground(Integer... integers) {
            return bdConnectionSql.VeficarExistenciaVariante(integers[0]);
        }

        @Override
        protected void onPostExecute(RespuestaExisVariante respuestaExisVariante) {
            super.onPostExecute(respuestaExisVariante);

            if (respuestaExisVariante.getPermitir()) {
                listenerVerificarExisteVariante.PermitirEliminar();
            } else {
                listenerVerificarExisteVariante.NoPermitirEliminar(respuestaExisVariante.getMensaje());
            }
        }
    }


    public void ActualizarEstadoVariante(mProduct product) {

        new ActualizarEstadoVariante().execute(product);
    }

    public class ActualizarEstadoVariante extends AsyncTask<mProduct, Void, Byte> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Byte doInBackground(mProduct... mProducts) {

            return bdConnectionSql.CambiarEstadoVariante(mProducts[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (configVariantes != null) {
                configVariantes.ActualizarEstadoVariante(aByte);
            }

        }
    }

    public interface IActualizaValorOpcion {
        public void ActualizoValorOpcionExito();

        public void ErrorActulizoValorOpcion();

        public void ResultadoListaVariantes(List<Variante> resultado);
    }

    public IActualizaValorOpcion iActualizaValorOpcion;

    public void setiActualizaValorOpcion(IActualizaValorOpcion iActualizaValorOpcion) {
        this.iActualizaValorOpcion = iActualizaValorOpcion;
    }

    public void GuardarNuevoValorVariante(String valor, int idItem, int IdProduct) {
        ActualizarVariantes act = new ActualizarVariantes();
        act.setIdProduct(IdProduct);
        act.setValor(valor);
        act.setIdItem(idItem);
        act.execute();
    }

    private class ActualizarVariantes extends AsyncTask<Void, Void, List<Variante>> {

        public void setValor(String valor) {
            this.valor = valor;
        }

        public void setIdItem(int idItem) {
            this.idItem = idItem;
        }

        public void setNumItem(int numItem) {
            this.numItem = numItem;
        }

        public void setIdProduct(int idProduct) {
            IdProduct = idProduct;
        }

        String valor;
        int idItem;
        int numItem;
        int IdProduct;
        private int result;

        @Override
        protected List<Variante> doInBackground(Void... voids) {

            result = bdConnectionSql.GuardarValorVariante(IdProduct, idItem, valor);
            if (result == 100) {
                return bdConnectionSql.GenerarVariantes(IdProduct);
            } else {
                return null;
            }


        }

        @Override
        protected void onPostExecute(List<Variante> variantes) {
            super.onPostExecute(variantes);
            if (result == 100) {
                iActualizaValorOpcion.ActualizoValorOpcionExito();
                iActualizaValorOpcion.ResultadoListaVariantes(variantes);
            } else {
                iActualizaValorOpcion.ErrorActulizoValorOpcion();
            }
        }
    }

    private class GetConfigVariantes extends AsyncTask<Integer, Void, mProduct> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected mProduct doInBackground(Integer... integers) {
            return bdConnectionSql.ObtenerConfiguracionVariante(integers[0]);
        }

        @Override
        protected void onPostExecute(mProduct product) {
            super.onPostExecute(product);
            if (configVariantes != null) {
                configVariantes.ObtenerConfiguracionVariantes(product);
            }
        }
    }

    private class EliminarVariante extends AsyncTask<Integer, Void, Byte> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarVariante(integers[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (configVariantes != null) {
                configVariantes.ResultadoEliminarVariante(aByte);
            }
        }
    }


    public class GuardarValoresOpciones extends AsyncTask<OpcionVariante, Void, OpcionVariante> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected OpcionVariante doInBackground(OpcionVariante... opcionVariantes) {
            return bdConnectionSql.GuardarValoresOpciones(opcionVariantes[0]);
        }

        @Override
        protected void onPostExecute(OpcionVariante opcionVariante) {
            super.onPostExecute(opcionVariante);
            configVariantes.ResultadoGuardarValores(opcionVariante);
        }
    }

    public class EliminarOpcion extends AsyncTask<OpcionVariante, Void, Byte> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Byte doInBackground(OpcionVariante... opcionVariantes) {
            return bdConnectionSql.EliminarOpcion(opcionVariantes[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (configVariantes != null) {
                configVariantes.ResultadoEliminarOpcion(aByte);
            }
        }
    }

    public class GuardarOpcion extends AsyncTask<OpcionVariante, Void, OpcionVariante> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected OpcionVariante doInBackground(OpcionVariante... opcionVariantes) {
            return bdConnectionSql.GuardarOpcionProductoVariante(opcionVariantes[0].getIdProduct(), opcionVariantes[0].getDescripcion());
        }

        @Override
        protected void onPostExecute(OpcionVariante opcionVariante) {
            super.onPostExecute(opcionVariante);
            if (configVariantes != null) {
                configVariantes.ResultadoGuardarOpcion(opcionVariante);
            }
        }
    }

    public class GenerarVariantes extends AsyncTask<Integer, Void, List<Variante>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Variante> doInBackground(Integer... integers) {
            return bdConnectionSql.GenerarVariantes(integers[0]);
        }

        @Override
        protected void onPostExecute(List<Variante> varianteList) {
            super.onPostExecute(varianteList);
            if (configVariantes != null) {
                configVariantes.GenerarVariantes(varianteList);

            }
        }
    }

    private class GetVariantesProducto extends AsyncTask<Integer, Void, List<Variante>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Variante> doInBackground(Integer... integers) {
            return bdConnectionSql.getVariantesProducto(integers[0], null);
        }

        @Override
        protected void onPostExecute(List<Variante> varianteList) {
            super.onPostExecute(varianteList);
            if (listenerVariante != null) {

                listenerVariante.VariantesProducto(varianteList);
            }
        }


    }

    private class GetOpcionesValores extends AsyncTask<Integer, Void, List<OpcionVariante>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<OpcionVariante> doInBackground(Integer... integers) {
            return  controladorVariante.getOpcionesValores(integers[0]);
          //  return bdConnectionSql.getOpcionesValores(integers[0]);
        }

        @Override
        protected void onPostExecute(List<OpcionVariante> opcionVarianteList) {
            super.onPostExecute(opcionVarianteList);
            if (variantesProduct != null) {
                variantesProduct.ValoresObtenidos(opcionVarianteList);
            }
        }
    }

    private class ActualizarVarianteId extends AsyncTask<Variante, Void, Byte> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Byte doInBackground(Variante... variantes) {
            return bdConnectionSql.ActualizarVariantePorId(variantes[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (configVariantes != null) {
                configVariantes.ResultadoActualizarVariante(aByte);
            }

        }
    }
}










