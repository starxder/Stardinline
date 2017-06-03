package com.example.starxder.stardinline.Beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2017/6/1.
 */
@DatabaseTable(tableName = "category")
public class Category {

    @DatabaseField(columnName = "category")
    private String category;

    @DatabaseField(columnName = "id")
    private int id;

    @DatabaseField(columnName = "value1")
    private String value1;

    @DatabaseField(columnName = "value2")
    private String value2;

    @DatabaseField(columnName = "value3")
    private String value3;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }
}
