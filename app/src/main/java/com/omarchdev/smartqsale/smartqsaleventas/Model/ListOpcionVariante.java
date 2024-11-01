package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 02/03/2018.
 */

public class  ListOpcionVariante {

    private List<OpcionVariante> opcionVarianteList;
    private OpcionVariante opcionVariante;

    public ListOpcionVariante() {
        this.opcionVarianteList =new ArrayList<>();
    }

    public List<OpcionVariante> getOpcionVarianteList() {
        return opcionVarianteList;
    }

    public void setOpcionVarianteList(List<OpcionVariante> opcionVarianteList) {
        this.opcionVarianteList = opcionVarianteList;
    }

    public int LongitudOpcionesVariante(){

        return opcionVarianteList.size();
    }
    public void setItemOpcionVariante(OpcionVariante opcionVariante){


    }

    public List<OpcionVariante> getList(){
        return opcionVarianteList;
    }

    public void ChangeNumItem(){

        for(int i=0;i<opcionVarianteList.size();i++){
            opcionVarianteList.get(i).setiNumIntem(i+1);
        }

        changeNumItemChild();
    }

    private void changeNumItemChild(){


        for(int i=0;i<opcionVarianteList.size();i++){
            for(int x=0;x<opcionVarianteList.get(i).listValores.size();x++){

                opcionVarianteList.get(i).listValores.get(x).setNumItemPadre(i+1);

            }

        }
    }

    public void Insertar(OpcionVariante opcionVariante){
            opcionVarianteList.add(opcionVariante);
    }

    public void InsertarOpcion(String Descripcion){
        opcionVariante=new OpcionVariante();
        opcionVariante.setDescripcion(Descripcion);
        opcionVariante.setiNumIntem(opcionVarianteList.size()+1);
        opcionVarianteList.add(opcionVariante);
    }

}
