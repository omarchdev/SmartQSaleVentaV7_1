package com.omarchdev.smartqsale.smartqsaleventas.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 06/12/2017.
 */

public class ListClientes {

    List<mCustomer> list;

    public ListClientes() {
        list = new ArrayList<>();
    }

    public List<mCustomer> getListCliente() {

        return list;
    }

    public void setListCliente(List<mCustomer> list) {
        this.list = list;
    }



}
