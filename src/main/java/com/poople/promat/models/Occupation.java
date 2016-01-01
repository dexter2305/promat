package com.poople.promat.models;

public class Occupation {
    private String company;
    private String companyLocation;
    private int yearOfLeavingCompany;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public int getYearOfLeavingCompany() {
        return yearOfLeavingCompany;
    }

    public void setYearOfLeavingCompany(int yearOfLeavingCompany) {
        this.yearOfLeavingCompany = yearOfLeavingCompany;
    }
}
