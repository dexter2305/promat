package com.poople.promat.resources;


import com.poople.promat.models.Candidate;
import com.poople.promat.persistence.InMemoryDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;


@Path("candidates")
public class CandidateResource {


    @Context
    private UriInfo uriInfo;

    private InMemoryDatabase db = InMemoryDatabase.INSTANCE;
    private static final Log logger = LogFactory.getLog(CandidateResource.class);

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
        response = Response.created(URI.create(uriInfo.getAbsolutePath() + "/" + candidate.getId())).build();
        logger.info("create: " + candidate + " returned " + response.getStatus() + " -> " + response.getLocation().getPath());
        return response;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")long candidateId){
        logger.info("delete: " + candidateId);
        db.delete(candidateId);
        Response response = Response.ok().build();
        logger.info("delete: " + candidateId + " returned " + response.getStatus());
        return response;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Candidate candidate){
        logger.info("update: " + candidate.getId());
        Response response;
        if (db.read(candidate.getId()) != null){
            db.update(candidate);
            response = Response.ok().build();
        }else{
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        logger.info("update: " + candidate.getId() + " returned " + response.getStatus());
        return response;
    }

}
