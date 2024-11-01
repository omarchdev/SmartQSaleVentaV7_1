package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 11/11/2017.
 */

public class ListProduct {

    List<mProduct> mProducts;
    int longitud;
    public ListProduct(){

        mProducts=new ArrayList<>();
        longitud=0;
    }
     public void agregarProducto(int i,mProduct product){
        mProducts.add(i,product);
        longitud++;
    }
    public void removeList(int position){
        if(longitud>0) {
            mProducts.remove(position);
            longitud--;
        }
    }

    public void setPrecio(int position,float precio){
      mProducts.get(position).setdSalesPrice(precio);
    }

    public void setCantidad(int position,float cantidad){
        mProducts.get(position).setdQuantity(cantidad);
    }
    public void MoverProducto(int newPos,mProduct product){

        mProducts.set(newPos,product);
    }


    private void setLongitud(int longitud){
        this.longitud=longitud;
    }
    public List<mProduct> getmProducts(){
            return mProducts;
    }

    public void addProductList(mProduct product){
        mProducts.add(product);
        longitud++;

    }
    public void setmProducts(List<mProduct> list){
        mProducts=list;
        longitud=mProducts.size();
    }
    public mProduct getProductPosition(int Position){

        return mProducts.get(Position);
    }
    public void clearList(){
        if(getLongitud()>0){
        mProducts.clear();
        setLongitud(0);
        }

     }

     public int getProductId(int position){
        return mProducts.get(position).getIdProduct();
     }

     public int getLongitud(){

        return longitud;
     }
}
