package com.bijana.doms.apps.Adapter;

public class GetData {

    public GetData(String date, String nominal, String type){

        this.date = date;
        this.nominal = nominal;
        this.type = type;

    }
    String date="", nominal="", type;

    public String getDate() {
        return date;
    }

    public String getNominal() {
        return nominal;
    }

    public String getType() {
        return type;
    }

}
