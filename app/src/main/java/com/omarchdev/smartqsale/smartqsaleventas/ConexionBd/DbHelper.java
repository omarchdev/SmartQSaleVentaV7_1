package com.omarchdev.smartqsale.smartqsaleventas.ConexionBd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mImpresora;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mUsuario;

/**
 * Created by OMAR CHH on 04/10/2017.
 */

public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context) {
        super(context, Constantes.DBSQLITE_Database.DATABASE_NAME, null, Constantes.DBSQLITE_Database.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constantes.TransactionDbSqlLite.Create_Table_Rol_Acceso);
        db.execSQL(Constantes.TransactionDbSqlLite.Create_Table_User);
        db.execSQL(Constantes.TransactionDbSqlLite.Create_Table_Device);
        db.execSQL(Constantes.TransactionDbSqlLite.Create_Table_Print_Default);
        db.execSQL(Constantes.TransactionDbSqlLite.Create_Table_User_Register);
        db.execSQL("Create table ImpresoraRed (IpImpresora text,Puerto integer not null default 9100) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion){
            case 3:
                db.execSQL("Create table ImpresoraRed (IpImpresora text,Puerto integer not null default 9100) ");
                break;
        }

    }


    public Cursor SelectImpresoraRed(){

        SQLiteDatabase db = getReadableDatabase();
        String[] campos = new String[]{"IpImpresora,Puerto"};
        return db.query("ImpresoraRed", campos, null, null, null, null, null);

    }

    public mImpresora ObtenerImpresoraRed(){
        mImpresora i=new mImpresora();
        Cursor a=SelectImpresoraRed();
        if(a.getCount()>0) {
            while(a.moveToNext()){
                i.setIP(a.getString(0));
                i.setPuerto(a.getInt(1));
            }
        }else{
            i.setPuerto(0);
            i.setIP("");
        }

       return i;
    }

    public Cursor SelectOptionPrint() {
        SQLiteDatabase db = getReadableDatabase();
        String[] campos = new String[]{"cNamePrint"};
        return db.query(Constantes.DBSQLITE_Usuario.TABLE_OPTION_PRINT, campos, null, null, null, null, null);
    }

    public String MetodoImpresioSeleccionada(){

        String metodo="";
        Cursor c=SelectOptionPrint();
        if (c.getCount() > 0) {

            while (c.moveToNext()) {
                metodo=c.getString(0);
            }
        }else{
            metodo="N";
        }
        return metodo;
    }

    public long InsertOptionUserRegister(String user,String password){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constantes.Usuario_Registro.Email,user);
        values.put(Constantes.Usuario_Registro.Contrasena,password);
        return db.insert(Constantes.Usuario_Registro.TablaUsuarioRegistrado,null,values);

    }

    public long InsertImpresoraRed(String ip,int puerto){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("IpImpresora",ip);
        values.put("Puerto",puerto);
        return db.insert("ImpresoraRed",null,values);
    }

    public void DeleteUserRegister(){

        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("Delete from "+Constantes.Usuario_Registro.TablaUsuarioRegistrado);

    }
        public long InsertOptionPrint(String option) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cNamePrint", option);
        return db.insert(Constantes.DBSQLITE_Usuario.TABLE_OPTION_PRINT, null, values);
    }
    public Cursor SelectDevice() {
        SQLiteDatabase db = getReadableDatabase();
        String[] campos = new String[]{"cDeviceAddress"};

        return db.query(Constantes.DBSQLITE_Usuario.TABLE_DEVICE_BLUETOOTH, campos, null, null, null, null, null);
    }

    public String AddressBT(){
        String a="";
        Cursor cc=SelectDevice();
        if (cc.getCount() > 0) {
            while (cc.moveToNext()) {
                a = cc.getString(0);
            }
        }else{
            a="N";
        }
        return a;
    }
    public long InsertRolAcceso(int idProceso,boolean EstadoAcceso) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long a = 0;
        int v = 0;
        values.clear();
        values.put(Constantes.TABLE_Acceso_Usuario.Proceso_ID, idProceso);
        values.put(Constantes.TABLE_Acceso_Usuario.EstadoAcceso, EstadoAcceso);
        a = db.insert(Constantes.TABLE_Acceso_Usuario.Tabla_Acceso_Usuario, null, values);

        return a;

    }

    public boolean ObtenerPermiso(int idProceso){

        boolean estado=false;
        SQLiteDatabase db=getReadableDatabase();
        int id=0;
        String[]campos=new String[]{Constantes.TABLE_Acceso_Usuario.EstadoAcceso};
        String[]arg=new String[]{String.valueOf(idProceso)};
       Cursor c=db.query(Constantes.TABLE_Acceso_Usuario.Tabla_Acceso_Usuario,campos,Constantes.TABLE_Acceso_Usuario.Proceso_ID+"=?",arg,null,null,null);
       try {
           if (c.getCount() > 0) {
               while (c.moveToNext()) {
                    estado = c.getInt(0) > 0;
               }
           }else {
               estado=true;
           }
       }catch (Exception e){
           e.toString();
       }
        return estado;
    }

    //Eliminar todos los accesos anteriores
    public void DeleteAccesoRolAcceso(){

        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("Delete from "+Constantes.TABLE_Acceso_Usuario.Tabla_Acceso_Usuario);

    }
    public void DeleteImpresoraRed(){

        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("Delete from ImpresoraRed");

    }
    public mUsuario SelectUsuario(){
        mUsuario usuario=new mUsuario();
        SQLiteDatabase db=getReadableDatabase();
        String[]campos=new String[]{Constantes.Usuario_Registro.Email,Constantes.Usuario_Registro.Contrasena};

        Cursor c =db.query(Constantes.Usuario_Registro.TablaUsuarioRegistrado,campos,null,null,null,null,null);
        if (c.getCount() > 0) {

            while (c.moveToNext()) {
                usuario.setEmail(c.getString(0));
                usuario.setContrasena(c.getString(1));
            }
        }


        return  usuario;
    }

    public void DeletePrint() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constantes.DBSQLITE_Usuario.TABLE_OPTION_PRINT);
    }

    public long InsertDevice(String deviceAddress) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cDeviceAddress", deviceAddress);
        return db.insert(Constantes.DBSQLITE_Usuario.TABLE_DEVICE_BLUETOOTH, null, values);
    }

    public void DeleteBluetooth() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM  " + Constantes.DBSQLITE_Usuario.TABLE_DEVICE_BLUETOOTH);
    }
    public long insertUser(String userName,String userPassword){

        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constantes.DBSQLITE_Usuario.User_Name,userName);
        values.put(Constantes.DBSQLITE_Usuario.User_Password,userPassword);
        return db.insert(Constantes.DBSQLITE_Usuario.TABLE_NAME,null,values);
    }
    public Cursor checkExistUserInDb(){
        SQLiteDatabase db=getReadableDatabase();
        String[]campos=new String []{Constantes.DBSQLITE_Usuario.User_Name,Constantes.DBSQLITE_Usuario.User_Password};

        return db.query(Constantes.DBSQLITE_Usuario.TABLE_NAME,campos,null,null,null,null,null);

    }
    public void deleteUser(){

        SQLiteDatabase db=getWritableDatabase();
        db.delete(Constantes.DBSQLITE_Usuario.TABLE_NAME,null,null);

    }
}
