package com.omarchdev.smartqsale.smartqsaleventas.Controlador;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IClienteRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

/**
 * Created by OMAR CHH on 06/12/2017.
 */

public class ControladorCliente {

    final String codeCia=GetJsonCiaTiendaBase64x3();
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IClienteRepository iClienteRepository= retro.create(IClienteRepository.class);
    public byte InsertarCliente(mCustomer cliente) {

       return  BdConnectionSql.getSinglentonInstance().ClienteInsertEdit(cliente, Constantes.ParametrosCliente.nuevoCliente);


    }


    public byte EditarClienteId(mCustomer cliente){
        return BdConnectionSql.getSinglentonInstance().EditarCliente(cliente);
    }

    public void EditarCliente(mCustomer cliente) {

        BdConnectionSql.getSinglentonInstance().ClienteInsertEdit(cliente, Constantes.ParametrosCliente.editarCliente);


    }


    public List<mCustomer> getClienteNombreApellido(String Parametro, String Control1, String Control2) {

        List<mCustomer> listResult=new ArrayList<>();
        try {
            listResult=iClienteRepository.GetClientePedido("2",codeCia,Parametro).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listResult;
    }


    public List<mCustomer> getAllCliente() {

        return BdConnectionSql.getSinglentonInstance().getClientes(0, "", Constantes.ParametrosCliente.TodosLosClientes,"","");
    }


}
