package com.example.helpinghands;

class Feedbackvol {
    String key,mail,vname,feedback;

    public Feedbackvol(String key, String mail, String vname, String feedback) {
        this.key = key;
        this.mail = mail;
        this.vname = vname;
        this.feedback = feedback;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
