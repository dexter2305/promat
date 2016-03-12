package com.poople.promat.models;

public class Education {
    private String qualification;
    private String instituteName;
    private int yearOfGraduation;

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public int getYearOfGraduation() {
        return yearOfGraduation;
    }

    public void setYearOfGraduation(int yearOfGraduation) {
        this.yearOfGraduation = yearOfGraduation;
    }

    @Override
    public String toString() {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder
                .append("{")
                .append("qualification:").append(qualification)
                .append("}");
        return contentBuilder.toString();
    }
}
