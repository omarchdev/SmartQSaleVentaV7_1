package com.omarchdev.smartqsale.smartqsaleventas.Model;

/**
 * Created by OMAR CHH on 25/09/2017.
 */

public class mCompany {


    private String cKeyCompany;

    public mCompany(){

        cKeyCompany="C0001";

    }

    public String getcKeyCompany() {
        return cKeyCompany;
    }

    public void setcKeyCompany(String cKeyCompany) {
        this.cKeyCompany = cKeyCompany;
    }

    public mCompany(String cKeyCompany) {
        this.cKeyCompany = cKeyCompany;
    }

}
