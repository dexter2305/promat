package com.poople.promat.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.poople.promat.adapters.DTFormatter;
import com.poople.promat.adapters.LocalDateAdapter;
import com.poople.promat.adapters.LocalTimeAdapter;

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
    
    public static void main(String[] args) {
        Dob dob = new Dob();
        dob.setBirthdate(DTFormatter.INSTANCE.getLocalDateFrom("30/03/1981"));
        dob.setBirthtime(DTFormatter.INSTANCE.getLocalTimeFrom("09:16:08 pm"));
		System.out.println(dob.toString());
		

        dob.setBirthdate(DTFormatter.INSTANCE.getLocalDateFrom("30/03/1981"));
        dob.setBirthtime(DTFormatter.INSTANCE.getLocalTimeFrom("09:16 pm"));
		System.out.println(dob.toString());

        dob.setBirthdate(DTFormatter.INSTANCE.getLocalDateFrom("30/3/1981"));
        dob.setBirthtime(DTFormatter.INSTANCE.getLocalTimeFrom("9:16 pm"));
		System.out.println(dob.toString());
		}
}
