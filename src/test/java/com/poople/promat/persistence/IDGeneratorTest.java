package com.poople.promat.persistence;


import org.junit.Assert;
import org.junit.Test;

public class IDGeneratorTest {


    @Test
    public void testForGreaterThanZero() {
        long uuid = IDGenerator.INSTANCE.getUUID();
        Assert.assertTrue("Unique ID should be greater than ZERO", uuid > 0 );
    }

    @Test
    public void testForTwoSuccessiveCalls(){
        long result1 = IDGenerator.INSTANCE.getUUID();
        long result2 = IDGenerator.INSTANCE.getUUID();
        Assert.assertNotEquals(result1, result2);
    }
}