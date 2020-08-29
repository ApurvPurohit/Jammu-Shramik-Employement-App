package com.example.android.hrm;

public class EmployerJOBDetailsHelper {
    String ndays;
    String nlab;
    String desp;
    String name;
    String phn;
    String t_date;
    String f_date;
    public EmployerJOBDetailsHelper(){

    }

    public String getT_date() {
        return t_date;
    }

    public String getF_date() {
        return f_date;
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


    public void setT_date(String t_date) {
        this.t_date = t_date;
    }

    public void setF_date(String f_date) {
        this.f_date = f_date;
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



    public EmployerJOBDetailsHelper(String ndays, String nlab, String desp, String name, String phn, String t_date,String f_date) {
        this.ndays = ndays;
        this.nlab = nlab;
        this.desp = desp;
        this.name=name;
        this.phn=phn;
        this.t_date=t_date;
        this.f_date=f_date;
    }
}

