package com.poople.promat.persistence;

import com.poople.promat.models.Candidate;
import com.poople.promat.models.Contact;
import com.poople.promat.models.Education;
import com.poople.promat.models.Physique;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDatabase {

    public static final InMemoryDatabase INSTANCE = new InMemoryDatabase();

    private Map<Long, Candidate> store = new ConcurrentHashMap<>();

    private InMemoryDatabase() {
        Collection<Candidate> testCandidates = getTestCandidates();

        for (Candidate c : testCandidates) {
            store.put(c.getId(), c);
        }


    }

    public void create(Candidate person) {
        long id = IDGenerator.INSTANCE.getUUID();
        person.setId(id);
        store.put(person.getId(), person);
    }

    public Candidate read(long id) {
        return store.get(id);
    }

    public void update(Candidate person) {
        store.put(person.getId(), person);
    }

    public void delete(long id) {
        store.remove(id);
    }

    public Collection<Candidate> readAll() {
        return store.values();
    }

    private Collection<Candidate> getTestCandidates() {
        Collection<Candidate> candidates = new LinkedList<>();


        Candidate rob = getRobStark();
        Candidate yigrette = getYigrette();
        Candidate ned = getNedStark();

        candidates.add(rob);
        candidates.add(yigrette);
        candidates.add(ned);

        return candidates;
    }

    /* HELPER METHODS */

    private Candidate getNedStark() {
        Candidate ned = new Candidate();
        ned.setName("Ned Stark");
        long id = IDGenerator.INSTANCE.getUUID();
        ned.setId(id);
        ned.setGender(Candidate.Gender.MALE);
        return ned;
    }

    private Candidate getYigrette() {
        Candidate yigrette = new Candidate();
        yigrette.setName("Yigrette");
        yigrette.setGender(Candidate.Gender.FEMALE);
        long id = IDGenerator.INSTANCE.getUUID();
        yigrette.setId(id);
        Contact yContact = yigrette.getContact();
        yContact.setEmail("yiggy@got.com");
        yContact.setPhoneNumber("+91987912367");
        yigrette.setContact(yContact);
        return yigrette;
    }

    private Candidate getRobStark() {
        Candidate rob = new Candidate();
        rob.setGender(Candidate.Gender.MALE);
        rob.setName("Rob Stark");
        long id = IDGenerator.INSTANCE.getUUID();
        rob.setId(id);
        Contact robContact = rob.getContact();
        robContact.setEmail("robStark@got.com");
        robContact.setPhoneNumber("+919879689123");
        rob.setContact(robContact);
        Physique robPhysique = rob.getPhysique();
        robPhysique.setBloodGroup(Physique.Bloodgroup.ABPOSITIVE);
        robPhysique.setBodyType(Physique.BodyType.ATHLETIC);
        robPhysique.setHeight(170);
        robPhysique.setWeight(70);
        robPhysique.setSkinTone(Physique.SkinTone.FAIR);
        rob.setPhysique(robPhysique);
        Education ug = new Education();
        ug.setQualification("BE (Computer Science)");
        ug.setInstituteName("PSG, Coimbatore");
        ug.setYearOfGraduation(2003);
        rob.getEducations().add(ug);
        return rob;
    }
}
