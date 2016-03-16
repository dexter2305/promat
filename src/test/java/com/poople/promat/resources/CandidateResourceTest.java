package com.poople.promat.resources;

import com.poople.promat.models.Candidate;
import com.poople.promat.models.Dob;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

/**
 * Created by innsh on 10/21/2015.
 */
public class CandidateResourceTest extends JerseyTest {


    @Override
    protected Application configure() {
        return new ResourceConfig(CandidateResource.class);
    }

    @Test
    public void testForAllCandidates() {
        Response response = target().path("candidates").request().get();
        Collection collection = response.readEntity(Collection.class);
        Assert.assertEquals(3, collection.size());
        Assert.assertEquals(200, response.getStatus());
        Collection<Candidate> candidates = target().path("candidates").request().get(Collection.class);
        Assert.assertEquals(3, candidates.size());
    }

    @Test
    public void testUpdateExistingCandidate() {
        GenericType<Collection<Candidate>> candidateType = new GenericType<Collection<Candidate>>(){};
        Invocation.Builder invocation = target().path("candidates").request();
        Collection<Candidate> allCandidates = invocation.get(candidateType);
        Candidate selectedCandidate = allCandidates.stream().findAny().get();
        selectedCandidate.setName("updated name");
        Response response = target().path("/candidates").path(String.valueOf(selectedCandidate.getId())).request().put(Entity.entity(selectedCandidate, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals(200, response.getStatus());
        Candidate updatedCandidate = target().path("/candidates").path(String.valueOf(selectedCandidate.getId())).request().get(Candidate.class);
        Assert.assertEquals(updatedCandidate.getName(), selectedCandidate.getName());
    }

    @Test
    public void testAddNewCandidate(){
        Candidate newCandidate = new Candidate();
        newCandidate.setName("Tyrion Lannister");
        newCandidate.setGender(Candidate.Gender.MALE);
        newCandidate.getContact().addEmailAddress("tyrion@got.com");
        newCandidate.getContact().addPhoneNumber("123-456-789");
        Response response = target().path("candidates").request().post(Entity.entity(newCandidate, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testAddNewCandidateWithDOB(){
        Candidate newCandidate = new Candidate();
        newCandidate.setName("Cersei Lannister");
        newCandidate.setGender(Candidate.Gender.FEMALE);
        Dob dob = new Dob();
        dob.setBirthdate(LocalDate.now());
        dob.setBirthtime(LocalTime.now());
        newCandidate.setDob(dob);
        Response response = target().path("candidates").request().post(Entity.entity(newCandidate, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testDeleteExistingCandidate() {
        GenericType<Collection<Candidate>> candidateType = new GenericType<Collection<Candidate>>(){};
        Invocation.Builder invocation = target().path("candidates").request();
        Collection<Candidate> allCandidates = invocation.get(candidateType);
        Candidate selectedCandidate = allCandidates.stream().findAny().get();
        Response response = target().path("/candidates").path(String.valueOf(selectedCandidate.getId())).request().delete();
        Assert.assertEquals(200, response.getStatus());
        response = target().path("/candidates").path(String.valueOf(selectedCandidate.getId())).request().get();
        Assert.assertEquals(204, response.getStatus());
        Candidate deletedCandidate = response.readEntity(Candidate.class);
        Assert.assertNull(deletedCandidate);
    }

}