package com.example.android.hrm;

public class Employer_requirement_send {
    String ndays;
    String nlab;
    String job_desp;
    String job;

    public Employer_requirement_send(){
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

    public Employer_requirement_send(String ndays, String nlab, String job_desp, String job) {
        this.ndays = ndays;
        this.nlab = nlab;
        this.job_desp = job_desp;
        this.job = job;
    }
}
