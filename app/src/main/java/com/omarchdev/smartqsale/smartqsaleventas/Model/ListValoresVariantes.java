package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 05/03/2018.
 */

public class ListValoresVariantes {

    List<ValorOpcionVariante> valorOpcionVarianteList;
    ValorOpcionVariante valorOpcionVariante;

    public ListValoresVariantes() {
        valorOpcionVarianteList=new ArrayList<>();

    }

    public void setListValores(ValorOpcionVariante valores){
        this.valorOpcionVarianteList.add(valores);
    }

    public void AgregarValor(List<String> labels){
        valorOpcionVarianteList.clear();
        for(int i=0;i<labels.size();i++){
                valorOpcionVariante=new ValorOpcionVariante();
                valorOpcionVariante.setiNumItem(i+1);
                valorOpcionVariante.setDescripcion(labels.get(i));
                valorOpcionVarianteList.add(valorOpcionVariante);
        }

    }

    public List<ValorOpcionVariante> getList(){

        return valorOpcionVarianteList;
    }
    public void setList(List<ValorOpcionVariante> list){
        this.valorOpcionVarianteList.addAll(list);

    }
    public void setNumItemPadre(int numPadre){

        for(int i=0;i<valorOpcionVarianteList.size();i++){

            valorOpcionVarianteList.get(i).setNumItemPadre(numPadre);

        }
    }
    public int getLongitud(){
        return valorOpcionVarianteList.size();
    }
    public void AsignarNuevosNumeroItem(){


    }

}
