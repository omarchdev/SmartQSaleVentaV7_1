package com.omarchdev.smartqsale.smartqsaleventas.AsyncTask;

import android.os.AsyncTask;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProcesoRol;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mRol;

import java.util.List;

public class AsyncRoles {

    BdConnectionSql bdConnectionSql=BdConnectionSql.getSinglentonInstance();

    ListenerRoles listenerRoles;

    public void setListenerRoles(ListenerRoles listenerRoles){
        this.listenerRoles=listenerRoles;
    }
    public interface ListenerRoles{

        public void RolesObtenidos(List<mRol> mRolList);
        public void ErrorObtenerRoles();

    }




    public void ObtenerRoles(){
            new ObtenerRoles().execute();
    }

    private class ObtenerRoles extends AsyncTask<Void,Void,List<mRol>>{

        @Override
        protected List<mRol> doInBackground(Void... voids) {
            return bdConnectionSql.ObtenerRoles();
        }

        @Override
        protected void onPostExecute(List<mRol> mRolList) {
            super.onPostExecute(mRolList);
            if(listenerRoles!=null){
                if(mRolList.size()>0){
                    listenerRoles.RolesObtenidos(mRolList);
                }else if(mRolList.size()==0){
                    listenerRoles.ErrorObtenerRoles();
                }

            }
        }
    }

    ListenerConfigProcesoRol listenerConfigProcesoRol;

    public void setListenerConfigProcesoRol(ListenerConfigProcesoRol listenerConfigProcesoRol){
        this.listenerConfigProcesoRol=listenerConfigProcesoRol;
    }

    public interface ListenerConfigProcesoRol{
        public void ProcesosRolObtenidos(List<mProcesoRol> procesoRols);
        public void ErrorObtenerProcesos();
    }

    public void ObtenerProcesosRol(int idRol){
        new ObtenerProcesosRol().execute(idRol);
    }
    private class ObtenerProcesosRol extends AsyncTask<Integer,Void,List<mProcesoRol>>{
        @Override
        protected List<mProcesoRol> doInBackground(Integer... integers) {
            return bdConnectionSql.obtenerProcesosRol(integers[0]);
        }
        @Override
        protected void onPostExecute(List<mProcesoRol> mProcesoRols) {
            super.onPostExecute(mProcesoRols);
            if(listenerConfigProcesoRol!=null){
                if(mProcesoRols!=null){
                    listenerConfigProcesoRol.ProcesosRolObtenidos(mProcesoRols);
                }else{
                    listenerConfigProcesoRol.ErrorObtenerProcesos();
                }
            }
        }
    }

    ListenerGuardarProcesos listenerGuardarProcesos;
    public void setListenerGuardarProcesos(ListenerGuardarProcesos listenerGuardarProcesos){
        this.listenerGuardarProcesos=listenerGuardarProcesos;
    }

    public interface ListenerGuardarProcesos{

        public void ExitoGuardar();
        public void ErrorConnection();
        public void ErrorGuardar();
        public void FinGuardar();

    }

    public void GuardarEstadosProcesoRol(int idRol,List<mProcesoRol> rolList){
            GuardarEstadosProcesoRol obj=new GuardarEstadosProcesoRol();
            obj.setRolList(rolList);
            obj.execute(idRol);
    }

    private class GuardarEstadosProcesoRol extends AsyncTask<Integer,Void,Byte>{

        List<mProcesoRol> rolList;

        public void setRolList(List<mProcesoRol> rolList) {
            this.rolList = rolList;
        }

        @Override
        protected Byte doInBackground(Integer... integers) {
            return bdConnectionSql.GuardarCambiosRolProceso(integers[0],rolList);
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if(listenerGuardarProcesos!=null){
                listenerGuardarProcesos.FinGuardar();
                if(aByte==100){
                    listenerGuardarProcesos.ExitoGuardar();
                }else if(aByte==99){
                    listenerGuardarProcesos.ErrorGuardar();
                }else if(aByte==98){
                    listenerGuardarProcesos.ErrorConnection();
                }
            }
        }
    }

}
