package com.android.budget.entity;

/**
 * Created by dimal on 10.10.2017.
 */

public class Currency {

    private Integer id_currency;
    private String name_currency;
    private String iso_name_currency;

    public Currency() {}

    public Currency(Integer id_currency, String name_currency, String iso_name_currency) {
        this.id_currency = id_currency;
        this.name_currency = name_currency;
        this.iso_name_currency = iso_name_currency;
    }

    public Integer getId_currency() {
        return id_currency;
    }

    public void setId_currency(Integer id_currency) {
        this.id_currency = id_currency;
    }

    public String getName_currency() {
        return name_currency;
    }

    public void setName_currency(String name_currency) {
        this.name_currency = name_currency;
    }

    public String getIso_name_currency() {
        return iso_name_currency;
    }

    public void setIso_name_currency(String iso_name_currency) {
        this.iso_name_currency = iso_name_currency;
    }
}
