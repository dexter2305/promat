package com.poople.promat.models;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.poople.promat.models.HoroscopeConstants.MatchType;

public class Occupation {
    private String company;
    private String companyLocation;
    private int yearOfLeavingCompany;
    private Double salary;
    private String title;
    private boolean isCurrent = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

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

    public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        //sb.append("company='").append(company).append('\'').append(",");
        sb.append("companyLocation='").append(companyLocation).append('\'').append(",");
        //sb.append(", yearOfLeavingCompany=").append(yearOfLeavingCompany).append(",");
        sb.append("salary=").append(salary).append(",");
        sb.append("title='").append(title).append('\'');
        sb.append('}');
        return sb.toString();
    }
    public static Occupation getCurrentOccupation(Set<Occupation> occupations) {
    	Optional<Occupation> currOcc = occupations.stream().filter(o -> o.isCurrent()).findFirst();
    	return currOcc.isPresent()?currOcc.get():null;
    }
}
