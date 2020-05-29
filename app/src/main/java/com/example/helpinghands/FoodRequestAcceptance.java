package com.example.helpinghands;

public class FoodRequestAcceptance {
    String vname,rname,location,date,status,key;

    FoodRequestAcceptance(){

    }

    public FoodRequestAcceptance(String key,String vname, String rname, String location, String date, String status) {
        this.key = key;
        this.vname = vname;
        this.rname = rname;
        this.location = location;
        this.date = date;
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
