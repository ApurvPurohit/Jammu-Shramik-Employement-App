package com.example.android.hrm;
public class EmployeeHelperClass {
    String Name, Phone, gen, occ, exp, status;

    public EmployeeHelperClass() {
    }
    public EmployeeHelperClass(String name, String phone, String gen, String occ, String exp, String s) {
        Name = name;
        Phone = phone;
        this.gen = gen;
        this.occ = occ;
        this.exp = exp;
        this.status = s;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getOcc() {
        return occ;
    }

    public void setOcc(String occ) {
        this.occ = occ;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}
