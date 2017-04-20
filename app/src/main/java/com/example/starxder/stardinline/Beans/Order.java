package com.example.starxder.stardinline.Beans;

/**
 * Created by Administrator on 2017/4/17.
 */

public class Order {
    private String ordercode;

    private String ordertype;

    private String ordertime;

    private Integer ordername;

    private String dateflag;

    private String delflag;

    private String dealtype;

    public Order(String ordercode, String ordertype, String ordertime, Integer ordername, String dateflag, String delflag, String dealtype) {
        this.ordercode = ordercode;
        this.ordertype = ordertype;
        this.ordertime = ordertime;
        this.ordername = ordername;
        this.dateflag = dateflag;
        this.delflag = delflag;
        this.dealtype = dealtype;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public Integer getOrdername() {
        return ordername;
    }

    public void setOrdername(Integer ordername) {
        this.ordername = ordername;
    }

    public String getDateflag() {
        return dateflag;
    }

    public void setDateflag(String dateflag) {
        this.dateflag = dateflag;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getDealtype() {
        return dealtype;
    }

    public void setDealtype(String dealtype) {
        this.dealtype = dealtype;
    }


}