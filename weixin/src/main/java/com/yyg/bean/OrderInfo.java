package com.yyg.bean;

import java.io.Serializable;

public class OrderInfo extends Order implements Serializable {

    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}