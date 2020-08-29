package com.example.android.hrm;

public class EmployerRequirementHelper {
    String ndays;
    String nlab;
    String job_desp;
    String job;
    String t_date;
    String f_date;

    public EmployerRequirementHelper(){
    }

    public void setT_date(String t_date) {
        this.t_date = t_date;
    }

    public void setF_date(String f_date) {
        this.f_date = f_date;
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

    public String getJob_desp() {
        return job_desp;
    }

    public String getJob() {
        return job;
    }

    public EmployerRequirementHelper(String ndays, String nlab, String job_desp, String job,String t_date,String f_date) {
        this.ndays = ndays;
        this.nlab = nlab;
        this.job_desp = job_desp;
        this.job = job;
        this.t_date=t_date;
        this.f_date=f_date;
    }
}
