package com.poople.promat.persistence;

import com.poople.promat.models.Candidate;
import com.poople.promat.persistence.ormlite.StoreException;
import com.poople.promat.resources.Filter;

import java.util.Collection;

/**
 * Created by innsh on 21-03-2016.
 */
public interface IStore {
    void setup() throws StoreException;
    void cleanUp() throws StoreException;

    void create(Candidate candidate) throws StoreException;
    void update(Candidate candidate) throws StoreException;
    void delete(long candidateId) throws StoreException;
    Candidate read(long candidateId) throws StoreException;
    Collection<Candidate> readAll() throws StoreException;
    Collection<Candidate> apply(Filter filter) throws StoreException;
}
