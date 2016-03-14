package com.poople.promat.models;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Created by innsh on 14-03-2016.
 */
public class DobTest {

    private Dob dob;

    @Before
    public void init(){
        dob = new Dob();
    }

    @Test
    public void testGetAge_WhenBirthDateNotSet(){
        Assert.assertNull(dob.age());
    }

    @Test
    public void testGetAge_WhenBirthdateSet(){
        final Integer age = 10;
        LocalDate now = LocalDate.now();
        dob.setBirthdate(LocalDate.of((now.getYear() - age),now.getMonth(), now.getDayOfMonth()));
        Assert.assertEquals(age, dob.age());
    }

    @Test
    public void getAgeWhenInvalidDOBSet(){
        LocalDate now = LocalDate.now();
        dob.setBirthdate(LocalDate.of((now.getYear() + 5),now.getMonth(), now.getDayOfMonth()));
        Assert.assertNull(dob.age());
    }
}