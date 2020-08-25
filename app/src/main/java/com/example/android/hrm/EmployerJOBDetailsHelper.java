package com.example.android.hrm;

public class EmployerJOBDetailsHelper {
    String ndays;
    String nlab;
    String desp;
    String name;
    String phn;
    String date;
    public EmployerJOBDetailsHelper(){

    }
    public String getNdays() {
        return ndays;
    }

    public String getNlab() {
        return nlab;
    }

    public String getDesp() {
        return desp;
    }

    public String getName() {
        return name;
    }

    public String getPhn() {
        return phn;
    }

    public String getDate() {
        return date;
    }

    public void setNdays(String ndays) {
        this.ndays = ndays;
    }

    public void setNlab(String nlab) {
        this.nlab = nlab;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EmployerJOBDetailsHelper(String ndays, String nlab, String desp, String name, String phn, String date) {
        this.ndays = ndays;
        this.nlab = nlab;
        this.desp = desp;
        this.name=name;
        this.phn=phn;
        this.date=date;
    }
}

