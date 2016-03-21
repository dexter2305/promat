package com.poople.promat.persistence;

import com.poople.promat.models.Candidate;
import com.poople.promat.resources.Filter;

import java.util.Collection;

/**
 * Created by innsh on 21-03-2016.
 */
public interface IStore {
    void create(Candidate candidate);
    void update(Candidate candidate);
    void delete(long candidateId);
    Candidate read(long candidateId);
    Collection<Candidate> readAll();
    Collection<Candidate> apply(Filter filter);
}
