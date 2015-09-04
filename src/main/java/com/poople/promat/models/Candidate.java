package com.poople.promat.models;

public class Candidate {

    private long id;

    private String name;

    private Gender gender;

    private Contact contact;

    public Candidate(){
        contact = new Contact();
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
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

    public enum Gender{
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
