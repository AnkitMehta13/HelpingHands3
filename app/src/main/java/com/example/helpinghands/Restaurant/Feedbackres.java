package com.example.helpinghands.Restaurant;

class Feedbackres {
    String key,mail,rname,feedback;

    public Feedbackres(String key, String mail, String rname, String feedback) {
        this.key = key;
        this.mail = mail;
        this.rname = rname;
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

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
