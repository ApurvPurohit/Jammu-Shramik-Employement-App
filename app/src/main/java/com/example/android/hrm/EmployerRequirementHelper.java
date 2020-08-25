package com.example.android.hrm;

public class EmployerRequirementHelper {
    String ndays;
    String nlab;
    String job_desp;
    String job;
    String date;

    public EmployerRequirementHelper(){
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public EmployerRequirementHelper(String ndays, String nlab, String job_desp, String job,String date) {
        this.ndays = ndays;
        this.nlab = nlab;
        this.job_desp = job_desp;
        this.job = job;
        this.date=date;
    }
}
