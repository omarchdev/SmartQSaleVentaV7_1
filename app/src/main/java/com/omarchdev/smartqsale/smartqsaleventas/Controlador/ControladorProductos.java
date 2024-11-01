package com.omarchdev.smartqsale.smartqsaleventas.Controlador;

import android.content.Context;

import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.BdConnectionSql;
import com.omarchdev.smartqsale.smartqsaleventas.Model.mProduct;

import java.util.List;

/**
 * Created by OMAR CHH on 05/10/2017.
 */

public class ControladorProductos   {

    BdConnectionSql bdConnectionSql;
    Context context;
    public ControladorProductos(Context context){
        bdConnectionSql = BdConnectionSql.getSinglentonInstance();
        this.context=context;
    }


    public List<mProduct> getListaProductos() {
        // metodo busqueda : Todos los productos=100  Por Id detalle=101   Por Id sin Imagen =102  Por parametro =103

        byte metodo = 100;
        return bdConnectionSql.ObtenerProductosVentas(0, "", metodo);
    }

    public List<mProduct> getListaProductosPorParametro(String parametroBusqueda) {
        // metodo busqueda : Todos los productos=100  Por Id detalle=101   Por Id sin Imagen =102  Por parametro =103

        byte metodo = 103;
        return bdConnectionSql.ObtenerProductosVentas(0, parametroBusqueda, metodo);
    }

    public mProduct getProductIdSinImagen(int idProducto) {
        byte metodo = 102;
        return bdConnectionSql.ObtenerProductosVentas(idProducto, "", metodo).get(0);
    }

    public mProduct getProductoPorIdImagen(int idProducto) {
        byte metodo = 101;
        return bdConnectionSql.ObtenerProductosVentas(idProducto, "", metodo).get(0);
    }





}
