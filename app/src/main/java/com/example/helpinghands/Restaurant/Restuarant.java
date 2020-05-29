package com.example.helpinghands.Restaurant;

public class Restuarant {
    String key,resname, mail, pswd, phone, add, licno,owner,imageid;

    Restuarant(){

    }

    public Restuarant(String key, String resname, String mail, String pswd, String phone, String add, String licno, String owner,String imageid) {
        this.key = key;
        this.resname = resname;
        this.mail = mail;
        this.pswd = pswd;
        this.phone = phone;
        this.add = add;
        this.licno = licno;
        this.owner = owner;
        this.imageid = imageid;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getLicno() {
        return licno;
    }

    public void setLicno(String licno) {
        this.licno = licno;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
