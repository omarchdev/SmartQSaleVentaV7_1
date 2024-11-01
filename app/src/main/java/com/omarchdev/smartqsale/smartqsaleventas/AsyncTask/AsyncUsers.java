package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Controlador.ControladorProcesoCargar;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRol;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 08/05/2018.
 */

public class AsyncUsers  {

    UserLoguinTask userLoguinTask;
    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();
    ListenerVerificarResultadoLogin listenerVerificarResultadoLogin;
    ListenerVerificarResultadoPinLog listenerVerificarResultadoPinLog;
    AccesoPinUsuario accesoPinUsuario;
    Context context;
    ListenerConfigInit listenerConfigInit;
    ObtenerRoles obtenerRoles;
    RegistrarNuevoUsuario registrarNuevoUsuario;
    ObtenerUsuarios obtenerUsuarios;
    ObtenerUsuarioId obtenerUsuarioId;
    EditarUsuarioId editarUsuarioId;
    ControladorProcesoCargar controladorProcesoCargar;
    EliminarUsuario eliminarUsuario;

    public void setContext(Context context){
        if(context!=null) {
            this.context = context;
            bdConnectionSql.setContext(context);
            controladorProcesoCargar = new ControladorProcesoCargar(context);
        }
    }
    public AsyncUsers(){

    }
    public void setListenerVerificarResultadoLogin(ListenerVerificarResultadoLogin listenerVerificarResultadoLogin){
        this.listenerVerificarResultadoLogin = listenerVerificarResultadoLogin;
    }

    public void setListenerVerificarResultadoPinLog(ListenerVerificarResultadoPinLog listenerVerificarResultadoPinLog){
        this.listenerVerificarResultadoPinLog=listenerVerificarResultadoPinLog;
    }



    public void setListenerConfigInit(ListenerConfigInit listenerConfigInit){

        this.listenerConfigInit=listenerConfigInit;
    }
    public interface ListenerConfigInit{

        public void ConfigInit();
        public void ConfigInitError();
        public void ConfigInitErrorInternet();
    }

    public interface ListenerVerificarResultadoPinLog{
        public void EsAdministrador(byte respuesta);
        public void respuestaVerificarUsuarioPin(byte respuesta);
        public void ErrorInternetPin();
        public void ErrorProcedurePin();
        public void UserNoExistsPin();
    }

    public interface ListenerVerificarResultadoLogin {

        public void respuestaVerificarUsuario(byte respuesta);
        public void ErrorInternet();
        public void ErrorProcedure();
        public void UserNoExists();
        public void FinDemo();
    }

    public void VerificarCredencialesUsuario(String usuario,String password){

        userLoguinTask=new UserLoguinTask();
        userLoguinTask.setUsuario(usuario);
        userLoguinTask.setPassword(password);
        userLoguinTask.execute();

    }

    public void VerificarPinUsuario (String pinUsuario,String imei,
                                     String marcaTerminal,String modeloTerminal,
                                     String versionAndroid){

        accesoPinUsuario=new AccesoPinUsuario();
        accesoPinUsuario.execute(pinUsuario,imei,marcaTerminal,modeloTerminal,versionAndroid);
    }


    private class AccesoPinUsuario extends AsyncTask<String,Void,Byte>{

        @Override
        protected Byte doInBackground(String... strings) {
            return bdConnectionSql.loginPinUser(strings[0],strings[1],strings[2],strings[3],strings[4]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(listenerVerificarResultadoPinLog!=null){
                if(aByte==100){
                    listenerVerificarResultadoPinLog.respuestaVerificarUsuarioPin(aByte);
                }
                else if(aByte==99){
                    listenerVerificarResultadoPinLog.ErrorProcedurePin();
                }
                else if(aByte==98){
                    listenerVerificarResultadoPinLog.ErrorInternetPin();
                }
                else if(aByte==0){
                    listenerVerificarResultadoPinLog.UserNoExistsPin();
                }else if(aByte==101){
                    listenerVerificarResultadoPinLog.EsAdministrador(aByte);
                }

            }
        }
    }



    public void RegistrarDivisaDefecto(){


        new RegistroMonedaDefecto().execute();

    }

    private class RegistroMonedaDefecto extends AsyncTask<Void,Void,Byte>{


        @Override
        protected Byte doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(listenerConfigInit!=null){

                if(aByte==100){
                    listenerConfigInit.ConfigInit();
                }
                else if(aByte==99){
                    listenerConfigInit.ConfigInitError();
                }
                else if(aByte==98){
                    listenerConfigInit.ConfigInitErrorInternet();
                }
                else if(aByte==0){
                    listenerConfigInit.ConfigInitError();
                }

            }
        }
    }



    private class UserLoguinTask extends AsyncTask<Void,Void,Byte> {

        String usuario;
        String password;
        byte respuesta;

        public UserLoguinTask() {
            respuesta=0;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if(context!=null){
                controladorProcesoCargar.IniciarDialogCarga("Ingresando a su cuenta");
            }

        }

        @Override
        protected Byte doInBackground(Void... voids) {
            respuesta= bdConnectionSql.ValidarUsuarioConnection(usuario,password);

            return respuesta;
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(context!=null){
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if(listenerVerificarResultadoLogin!=null) {
                if (aByte == Constantes.EstadosAppLoguin.EstadoD || aByte == Constantes.EstadosAppLoguin.EstadoL) {
                    listenerVerificarResultadoLogin.respuestaVerificarUsuario(aByte);
                } else if (aByte == Constantes.EstadosAppLoguin.EstadoNoConnect) {
                    listenerVerificarResultadoLogin.ErrorInternet();
                } else if (aByte == Constantes.EstadosAppLoguin.EstadoErrorProc) {
                    listenerVerificarResultadoLogin.ErrorProcedure();
                } else if (aByte == Constantes.EstadosAppLoguin.EstadoNC) {
                    listenerVerificarResultadoLogin.UserNoExists();
                }else if(aByte== Constantes.EstadosAppLoguin.EstadoDN){
                    listenerVerificarResultadoLogin.FinDemo();
                }
            }
        }

    }


    ListenerRoles listenerRoles;

    public void setListenerRoles(ListenerRoles listenerRoles){
        this.listenerRoles=listenerRoles;
    }

    public interface ListenerRoles {

        public void ObtenerRoles(List<mRol> mRolList, List<String> listaDescripciones);
        public void ErrorObtener();
    }


    public void ObtenerRoles(){

        obtenerRoles=new ObtenerRoles();
        obtenerRoles.execute();

    }

    private class ObtenerRoles extends  AsyncTask<Void,Void,List<mRol>>{

        List<String> listaDescripciones=new ArrayList<>();
        List<mRol> mRolList;

        @Override
        protected void onPreExecute() {
            listaDescripciones.clear();
            listaDescripciones.add("Elige el tipo de usuario");
            super.onPreExecute();
        }

        @Override
        protected List<mRol> doInBackground(Void... voids) {
            mRolList =bdConnectionSql.ObtenerRoles();
            for(mRol mRol : mRolList){
                listaDescripciones.add(mRol.getcDescripcion());
            }
            return mRolList;
        }

        @Override
        protected void onPostExecute(List<mRol> mRols) {
            super.onPostExecute(mRols);

            if(listenerRoles!=null){
                if(mRols.get(0).getIdRol()>0) {
                    listenerRoles.ObtenerRoles(mRols,listaDescripciones);
                }
                else if(mRols.get(0).getIdRol()==-98 || mRols.get(0).getIdRol()==-99){
                    listenerRoles.ErrorObtener();
                }
            }

        }
    }


    ListenerRegistroUsuario listenerRegistroUsuario;

    public void setListenerRegistroUsuario(ListenerRegistroUsuario listenerRegistroUsuario){
        this.listenerRegistroUsuario=listenerRegistroUsuario;
    }

    public void RegistrarNuevoUsuario(mUsuario usuario){

        registrarNuevoUsuario=new RegistrarNuevoUsuario();
        registrarNuevoUsuario.execute(usuario);
    }

    public interface ListenerRegistroUsuario{


        public void RegistroExitoso();
        public void ErrorRegistro();
        public void PinExiste();
        public void NombreUsuarioExiste();
        public void ErrorRol();
        public void NumeroExcedido(int numeroUsuario);

    }

    private class RegistrarNuevoUsuario extends AsyncTask<mUsuario,Void,BdConnectionSql.RetornoUsuario>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(context!=null){
                controladorProcesoCargar.IniciarDialogCarga("Guardando datos del usuario");
            }

        }

        @Override
        protected BdConnectionSql.RetornoUsuario doInBackground(mUsuario... mUsuarios) {
            return bdConnectionSql.IngresarNuevoUsuario(mUsuarios[0]);
        }

        @Override
        protected void onPostExecute(BdConnectionSql.RetornoUsuario respuesta) {
            super.onPostExecute(respuesta);
            if(context!=null){
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if(listenerRegistroUsuario!=null){

                if(respuesta.getRespuesta()==100){
                    listenerRegistroUsuario.RegistroExitoso();
                }else if(respuesta.getRespuesta()==98 || respuesta.getRespuesta()==99){
                    listenerRegistroUsuario.ErrorRegistro();
                }else if(respuesta.getRespuesta()==90){
                    listenerRegistroUsuario.NombreUsuarioExiste();
                }else if(respuesta.getRespuesta()==80){
                    listenerRegistroUsuario.PinExiste();
                }else if(respuesta.getRespuesta()==50){
                    listenerRegistroUsuario.ErrorRol();
                }else if(respuesta.getRespuesta()==40){
                    listenerRegistroUsuario.NumeroExcedido(respuesta.getNumeroUsuario());
                }

            }
        }
    }

    public void ObtenerUsuarios(){

        obtenerUsuarios=new ObtenerUsuarios();
        obtenerUsuarios.execute();

    }

    public interface ListenerObtenerUsuarios{

        public void UsuariosObtenidos(List<mUsuario> usuarioList);
        public void ErrorConnection();
        public void ErrorConsulta();
    }
    ListenerObtenerUsuarios listenerObtenerUsuarios;

    public void setListenerObtenerUsuarios(ListenerObtenerUsuarios listenerObtenerUsuarios){

        this.listenerObtenerUsuarios=listenerObtenerUsuarios;

    }


    private class ObtenerUsuarios extends AsyncTask<Void,Void,List<mUsuario>>{

        @Override
        protected List<mUsuario> doInBackground(Void... voids) {
            return bdConnectionSql.ObtenerUsuariosRegistrados();
        }

        @Override
        protected void onPostExecute(List<mUsuario> mUsuarios) {
            super.onPostExecute(mUsuarios);
            if(listenerObtenerUsuarios!=null){

                if(mUsuarios.get(0).getIdUsuario()==-99){
                    listenerObtenerUsuarios.ErrorConsulta();
                }
                else if(mUsuarios.get(0).getIdUsuario()==-98){
                    listenerObtenerUsuarios.ErrorConnection();
                }else if(mUsuarios.get(0).getIdUsuario()>0){
                    listenerObtenerUsuarios.UsuariosObtenidos(mUsuarios);
                }
            }
        }
    }


    ListenerResultadoUsuarioSolicitado listenerResultadoUsuarioSolicitado;

    public void setListenerResultadoUsuarioSolicitado(ListenerResultadoUsuarioSolicitado listenerResultadoUsuarioSolicitado){

        this.listenerResultadoUsuarioSolicitado=listenerResultadoUsuarioSolicitado;

    }

    public interface ListenerResultadoUsuarioSolicitado{

        public void UsuarioObtenido(mUsuario usuario);
        public void ErrorObtener();
        public void ErrorConnection();

    }


    public void ObtenerUsuarioId(int idUsuario){
        obtenerUsuarioId=new ObtenerUsuarioId();
        obtenerUsuarioId.execute(idUsuario);
    }

    private class ObtenerUsuarioId extends AsyncTask<Integer,Void,mUsuario>{

        @Override
        protected mUsuario doInBackground(Integer... integers) {
            return bdConnectionSql.obtenerInformacionUsuario(integers[0]);
        }

        @Override
        protected void onPostExecute(mUsuario usuario) {
            super.onPostExecute(usuario);
            if(listenerResultadoUsuarioSolicitado!=null){
                listenerResultadoUsuarioSolicitado.UsuarioObtenido(usuario);

            }
        }
    }



    public void EditarUsuario(mUsuario usuario){

        editarUsuarioId=new EditarUsuarioId();
        editarUsuarioId.execute(usuario);

    }

    ListenerEditarUsuario listenerEditarUsuario;
    public void setListenerEditarUsuario(ListenerEditarUsuario listenerEditarUsuario){
        this.listenerEditarUsuario=listenerEditarUsuario;
    }

    public interface ListenerEditarUsuario{

        public void EditarExito();
        public void ErrorEditar();
        public void ErrorEditarConexion();
        public void ErrorNombreUsuario();
        public void ErrorPin();

    }

    private class EditarUsuarioId extends AsyncTask<mUsuario,Void,Byte>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(context!=null){
                controladorProcesoCargar.IniciarDialogCarga("Guardando datos del usuario");
            }
        }

        @Override
        protected Byte doInBackground(mUsuario... mUsuarios) {
            return bdConnectionSql.EditarUsuarioRegistrado(mUsuarios[0]);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(context!=null){
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if(listenerEditarUsuario!=null){

                if(aByte==100){
                    listenerEditarUsuario.EditarExito();
                }else if(aByte==98){
                    listenerEditarUsuario.ErrorEditarConexion();
                }else if(aByte==99){
                    listenerEditarUsuario.ErrorEditar();
                }else if(aByte==80){
                    listenerEditarUsuario.ErrorPin();
                }else if(aByte==60){
                    listenerEditarUsuario.ErrorNombreUsuario();
                }

            }
        }
    }

    ListenerEliminarUsuario listenerEliminarUsuario;
    public void setListenerEliminarUsuario(ListenerEliminarUsuario listenerEliminarUsuario){
        this.listenerEliminarUsuario=listenerEliminarUsuario;
    }

    public interface ListenerEliminarUsuario{

        public void ErrorEliminar();
        public void EliminarExito();
        public void ErrorConexion();

    }

    public boolean EliminarUsuario(int idUsuario){

        if(idUsuario!=Constantes.Usuario.idUsuario){

            eliminarUsuario=new EliminarUsuario();
            eliminarUsuario.execute(idUsuario);

            return true;
        }
        else {
            return false;
        }

    }



    private class EliminarUsuario extends AsyncTask<Integer,Void,Byte>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(context!=null){

                controladorProcesoCargar.IniciarDialogCarga("Eliminando Usuario");

            }
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.EliminarUsuario(integers[0]);

        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(context!=null) {
                controladorProcesoCargar.FinalizarDialogCarga();
            }
            if(listenerEliminarUsuario!=null){

                if(aByte==100) {
                    listenerEliminarUsuario.EliminarExito();
                }else if(aByte==98){
                    listenerEliminarUsuario.ErrorConexion();
                }else if(aByte==99 || aByte==50){
                    listenerEliminarUsuario.ErrorEliminar();
                }
            }

        }
    }

}
