package com.poople.promat.models;

import com.poople.promat.adapters.LocalDateAdapter;
import com.poople.promat.adapters.LocalTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

public class Dob {

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate birthdate;

    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime birthtime;

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public LocalTime getBirthtime() {
        return birthtime;
    }

    public void setBirthtime(LocalTime birthtime) {
        this.birthtime = birthtime;
    }

    public Integer age() {
        if (birthdate == null) return null;
        Period period = Period.between(birthdate, LocalDate.now());
        return period.getYears() > 0 ? period.getYears() : null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Dob{");
        sb.append("birthdate=").append(birthdate);
        sb.append(", birthtime=").append(birthtime);
        sb.append('}');
        return sb.toString();
    }
}