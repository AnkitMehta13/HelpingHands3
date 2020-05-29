package com.example.helpinghands;

public class FoodRequest {
    String key,remail,rname,rphone,senddata,radd;

    FoodRequest(){

    }

    public FoodRequest(String key, String remail, String rname, String rphone, String radd, String senddata) {
        this.key = key;
        this.remail = remail;
        this.rname = rname;
        this.rphone = rphone;
        this.radd = radd;
        this.senddata = senddata;
    }

    public String getRadd() {
        return radd;
    }

    public void setRadd(String radd) {
        this.radd = radd;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemail() {
        return remail;
    }

    public void setRemail(String remail) {
        this.remail = remail;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRphone() {
        return rphone;
    }

    public void setRphone(String rphone) {
        this.rphone = rphone;
    }

    public String getSenddata() {
        return senddata;
    }

    public void setSenddata(String senddata) {
        this.senddata = senddata;
    }
}
