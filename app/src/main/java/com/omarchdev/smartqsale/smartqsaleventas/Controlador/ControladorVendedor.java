package com.omarchdev.smartqsale.smartqsaleventas.Controlador;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mVendedor;
import com.omarchdev.smartqsale.smartqsaleventas.Repository.IVendedorRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.omarchdev.smartqsale.smartqsaleventas.Constantes.Constantes.BASECONN.BASE_URL_API;
import static com.omarchdev.smartqsale.smartqsaleventas.Model.CiaTiendaKt.GetJsonCiaTiendaBase64x3;

/**
 * Created by OMAR CHH on 07/12/2017.
 */

public class ControladorVendedor {


    final String codeCia=GetJsonCiaTiendaBase64x3();
    Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create()).build();
    IVendedorRepository iVendedorRepository= retro.create(IVendedorRepository.class);
    public mVendedor getVendedorPorId(int id) {
        return BdConnectionSql.getSinglentonInstance().getVendedor(id, "", Constantes.ParametrosVendedor.BusquedaPorId).get(0);
    }

    public List<mVendedor> getBusquedaNombreApellido(String parametro) {

        try {
            return iVendedorRepository.GetVendedores(codeCia,"2",parametro).execute().body();
        } catch (IOException e) {
            return new ArrayList<>();

        }
        //    return BdConnectionSql.getSinglentonInstance().getVendedor(0, parametro, Constantes.ParametrosVendedor.BusquedaPorNombre);
    }

    public List<mVendedor> getAllVendedor() {
        try {
            return iVendedorRepository.GetVendedores(codeCia,"2","").execute().body();
        } catch (IOException e) {
            return new ArrayList<>();
        }catch (Exception ex){
            return new ArrayList<>();
        }
     //   return BdConnectionSql.getSinglentonInstance().getVendedor(0, "", Constantes.ParametrosVendedor.TodosVendedores);
    }


}
