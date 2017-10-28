package com.android.budget.entity;

/**
 * Created by dimal on 10.10.2017.
 */

public class Category {

    private Integer id_category;
    private String name_category;
    private Integer src_image;
    private Integer id_account;

    public Category() {
    }

    public Category(Integer id_category, String name_category, Integer src_image, Integer id_account) {
        this.id_category = id_category;
        this.name_category = name_category;
        this.src_image = src_image;
        this.id_account = id_account;
    }

    public Integer getId_category() {
        return id_category;
    }

    public void setId_category(Integer id_category) {
        this.id_category = id_category;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public Integer getSrc_image() {
        return src_image;
    }

    public void setSrc_image(Integer src_image){
        this.src_image = src_image;
    }

    public Integer getId_account() {
        return id_account;
    }

    public void setId_account(Integer id_account) {
        this.id_account = id_account;
    }
}
