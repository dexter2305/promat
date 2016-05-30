package com.poople.promat.models;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Candidate {

    private String externalUserId;

    private long id;

    private String name;

    private Gender gender;

    private Contact contact;

    private Physique physique;

    private Set<Education> educations;

    private Set<Occupation> occupations;

    private Collection<Note> notes;

    private Dob dob;

    //private MaritalStatus maritalStatus; // not using as of as the data is not consistent
    private String maritalStatus;
    
    //TODO not clear about this field as of now..
    private String late;

    private String kulam;
    
    //Note: Need DOB in side horoscope as horoscope is generated using DOB
    private Horoscope horoscope;
    
    public Candidate() {
        contact = new Contact();
        physique = new Physique();
        educations = new LinkedHashSet<>();
        occupations = new LinkedHashSet<>();
        notes = new LinkedHashSet<>();
        dob  = new Dob();
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

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public Collection<Note> getNotes() {
        return notes;
    }

    public Dob getDob() {
        return dob;
    }

    public void setDob(Dob dob) {
        this.dob = dob;
    }

    public Integer age(){
        return dob.age();
    }

    public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getLate() {
		return late;
	}

	public void setLate(String late) {
		this.late = late;
	}

	public String getKulam() {
		return kulam;
	}

	public void setKulam(String kulam) {
		this.kulam = kulam;
	}

	public Horoscope getHoroscope() {
		return horoscope;
	}

	public void setHoroscope(Horoscope horoscope) {
		this.horoscope = horoscope;
	}

	public enum Gender {
        FEMALE, MALE;

        public static Gender fromString(String s) {
            if (s == null || s.isEmpty()) return null;
            Pattern femalePattern = Pattern.compile("female".toLowerCase(), Pattern.CASE_INSENSITIVE);
            Pattern malePattern = Pattern.compile("male".toLowerCase(), Pattern.CASE_INSENSITIVE);
            Matcher femaleMatcher = femalePattern.matcher(s);
            Matcher maleMatcher = malePattern.matcher(s);

            if (femaleMatcher.find()) {
                return Gender.FEMALE;
            } else if (maleMatcher.find()) {
                return Gender.MALE;
            }
            return null;
        }

    }

    @Override
    public String toString() {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder
                .append("Candidate:{")
                .append("id:").append("'").append(id).append("'").append(",")
                .append("xId:").append("'").append(externalUserId).append("'").append(",")
                .append("name:").append("'").append(name).append("'").append(",")
                .append("gender:").append("'").append(gender).append("'").append(",")
                .append("maritalStatus:").append("'").append(maritalStatus).append("'").append(",")
                .append("kulam:").append("'").append(kulam).append("'").append(",")
                .append("kulam:").append("'").append(horoscope).append("'").append(",")
                .append(physique.toString()).append(",")
                .append(contact.toString())
                .append("education:[");
        educations.forEach(education -> contentBuilder.append(education.toString()));
        contentBuilder
                .append("]")
                .append("occupation:[");
        occupations.forEach(occupation -> contentBuilder.append(occupation.toString()));
        contentBuilder
                .append("]")

                .append("notes:[");
        notes.forEach(note -> contentBuilder.append(note.toString()));
        contentBuilder
                .append("]")
                .append(dob.toString())
                .append("}");
        return contentBuilder.toString();
    }
}

