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

    @Test
    public void testEducationsMustBeNonNull(){
        Assert.assertNotNull(candidate.getEducations());
    }

    @Test
    public void testEducationsSizeMustBeZeroInitially(){
        Assert.assertEquals(0, candidate.getEducations().size());
    }

    @Test
    public void testContactEmailEmptyInitially(){
        Assert.assertFalse(candidate.getContact().hasEmailAddresses());
    }

    @Test
    public void testContactPhoneNumberMustBeEmptyInitially(){
        Assert.assertFalse(candidate.getContact().hasPhoneNumbers());
    }

    @Test
    public void testNotesMustBeNonNull(){
        Assert.assertNotNull(candidate.getNotes());
    }

    @Test
    public void testNotesMustBeEmpty(){
        Assert.assertEquals(0, candidate.getNotes().size());
    }
}