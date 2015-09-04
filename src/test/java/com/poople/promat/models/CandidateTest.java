package com.poople.promat.models;

import junit.framework.Assert;
import org.junit.Test;

public class CandidateTest {

    @Test
    public void testContactMustBeNonNull(){
        Candidate candidate = new Candidate();
        Assert.assertNotNull(candidate.getContact());
    }

}