package com.example.helpinghands;

import java.util.ArrayList;

public class Volunteer {
    private String key,name,email,password,address,dob,license,gender,phone,imageid;

    Volunteer(){

    }

    public Volunteer(String key, String fname, String mail, String password, String mobile, String add, String licno, String gen, String bday,String imageid) {
        this.key = key;
        this.name = fname;
        this.email = mail;
        this.password = password;
        this.phone = mobile;
        this.address = add;
        this.license = licno;
        this.gender = gen;
        this.dob = bday;
        this.imageid = imageid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getLicense() {
        return license;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getKey(){
        return key;
    }

}
