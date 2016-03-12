package com.poople.promat.models;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class Contact {

    private Collection<String> phoneNumbers;
    private Collection<String> emailAddresses;

    Contact(){
        phoneNumbers = new LinkedHashSet<>();
        emailAddresses = new LinkedHashSet<>();
    }

    public void addPhoneNumber(String phoneNumber){
        phoneNumbers.add(phoneNumber);
    }

    public void addPhoneNumbers(Collection<String> phoneNumbers){
        if (phoneNumbers == null) return;
        phoneNumbers.forEach(phoneNumber ->  addPhoneNumber(phoneNumber));
    }

    public boolean removePhoneNumber(String phoneNumber){
        return phoneNumbers.remove(phoneNumber);
    }

    public Collection<String> getAllPhoneNumbers(){
        return new LinkedHashSet<>(phoneNumbers);
    }

    public boolean hasPhoneNumbers(){
        return !phoneNumbers.isEmpty();
    }

    public void addEmailAddress(String emailAddress){
        emailAddresses.add(emailAddress);
    }

    public boolean removeEmailAddress(String emailAddress){
        return emailAddresses.remove(emailAddress);
    }

    public Collection<String> getAllEmailAddresses(){
        return new LinkedHashSet<>(emailAddresses);
    }

    public boolean hasEmailAddresses(){
        return !emailAddresses.isEmpty();
    }

    @Override
    public String toString() {

        final StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("contact:")
                .append("{")
                .append("phoneNumbers:[");
        phoneNumbers.forEach(s -> contentBuilder.append(s).append(","));
        contentBuilder.append("]")
                .append(",")
                .append("emailAddresses:[");
        emailAddresses.forEach(s -> contentBuilder.append(s).append(","));
        contentBuilder.append("]")
                .append("}");

        return contentBuilder.toString();
    }

}
