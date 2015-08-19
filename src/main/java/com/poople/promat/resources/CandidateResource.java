package com.poople.promat.resources;


import com.poople.promat.models.Candidate;
import com.poople.promat.persistence.InMemoryDatabase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("candidates")
public class CandidateResource {
    private InMemoryDatabase db = InMemoryDatabase.INSTANCE;

    private Logger logger = LogManager.getLogger("CandidateResource");

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@PathParam("id") long id) {
        logger.info("read: for candidate with id = " + id);
        Candidate candidate = db.read(id);
        Response response;
        if (candidate == null) {
            response = Response.noContent().build();
        } else {
            response = Response.ok(candidate).build();
        }
        logger.info("read: Candidate(id=" + id + ") returned " + response.getStatus());
        return response;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get() {
        logger.info("read: all candidates");
        Collection<Candidate> candidates = db.readAll();
        Response response;
        if (candidates.isEmpty()) {
            response = Response.noContent().build();
        } else {
            response = Response.ok(candidates).build();
        }
        logger.info("read: all candidates returned " + response.getStatus());
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Candidate candidate) {
        logger.info("create: " + candidate);
        Response response;
        db.create(candidate);
        response = Response.ok().build();
        logger.info("create: " + candidate + " returned " + response.getStatus());
        return response;
    }


}
