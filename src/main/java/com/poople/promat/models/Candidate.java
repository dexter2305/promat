package com.poople.promat.models;

import java.util.LinkedHashSet;
import java.util.Set;

public class Candidate {

    private long id;

    private String name;

    private Gender gender;

    private Contact contact;

    private Physique physique;

    private Set<Education> educations;

    private Set<Occupation> occupations;

    public Candidate() {
        contact = new Contact();
        physique = new Physique();
        educations = new LinkedHashSet<>();
        occupations = new LinkedHashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Physique getPhysique() {
        return physique;
    }

    public void setPhysique(Physique physique) {
        this.physique = physique;
    }

    public Set<Education> getEducations() {
        return educations;
    }

    public Set<Occupation> getOccupations() {
        return occupations;
    }

    public enum Gender {
        FEMALE, MALE
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "name='" + name + '\'' +
                ", id=" + id + '\'' +
                ", gender=" + gender +
                '}';
    }
}
