package com.poople.promat.resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

/**
 * Created by innsh on 10/21/2015.
 */
public class CandidateResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(CandidateResource.class);
    }

    @Test
    public void testForAllCandidates(){
        Response response = target().path("candidates").request().get();
        Assert.assertEquals(200, response.getStatus());
    }

}