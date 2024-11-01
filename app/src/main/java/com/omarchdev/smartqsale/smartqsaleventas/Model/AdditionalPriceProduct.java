package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by OMAR CHH on 01/10/2017.
 */

public class AdditionalPriceProduct {

    @SerializedName("id")
    private int id;
    @SerializedName("cKey")
    private String cKey;
    @SerializedName("additionalPrice")
    private BigDecimal additionalPrice;
    @SerializedName("numItem")
    private int numItem;

    public String getcKey() {
        return cKey;
    }

    public void setcKey(String cKey) {
        this.cKey = cKey;
    }

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public AdditionalPriceProduct() {
        this.numItem=0;
    }

    public AdditionalPriceProduct(int id, BigDecimal additionalPrice,int numtItem) {
        this.id=id;
        this.additionalPrice = additionalPrice;
        this.numItem=numtItem;

    }

    public int getNumItem() {
        return numItem;
    }

    public void setNumItem(int numItem) {
        this.numItem = numItem;
    }
}
