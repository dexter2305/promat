package com.poople.promat.models;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class CandidateTest {

    private Candidate candidate;

    @Before
    public void init(){
        candidate = new Candidate();
    }

    @Test
    public void testContactMustBeNonNull(){
        Assert.assertNotNull(candidate.getContact());
    }

    @Test
    public void testPhysiqueMustBeNonNull(){
        Assert.assertNotNull(candidate.getPhysique());
    }

}