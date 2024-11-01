package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 18/02/2018.
 */

public class ListColorItem {

        List<String> listColor;

        public ListColorItem(){

            listColor=new ArrayList<>();
            listColor.add("#FF0000");
            listColor.add("#FF7A09");
            listColor.add("#3E0CE8");
            listColor.add("#10FF0F");
            listColor.add("#E85DC0");
            listColor.add("#E8DF1B");
             listColor.add("#999797");


        }

        public List<String>getListColor(){

            return listColor;
        }
        

    

}
