package com.poople.promat.persistence;

import com.poople.promat.models.Candidate;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDatabase {

    private Map<Long, Candidate> store = new ConcurrentHashMap<Long, Candidate>();

    public InMemoryDatabase() {
        Collection<Candidate> testCandidates = getTestCandidates();

        Iterator<Candidate> iterator = testCandidates.iterator();
        while(iterator.hasNext()){
            Candidate c = iterator.next();
            store.put(c.getId(), c);
        }
    }

    public void create(Candidate person) {
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

        Candidate anand = new Candidate();
        anand.setGender(Candidate.Gender.MALE);
        anand.setName("Rob Stark");
        anand.setId(90123);

        Candidate rekha = new Candidate();
        rekha.setName("Yigrette");
        rekha.setGender(Candidate.Gender.FEMALE);
        rekha.setId(80967);

        Candidate ned = new Candidate();
        ned.setName("Ned Stark");
        ned.setId(78907);
        ned.setGender(Candidate.Gender.MALE);

        candidates.add(anand);
        candidates.add(rekha);
        candidates.add(ned);

        return candidates;
    }

}
