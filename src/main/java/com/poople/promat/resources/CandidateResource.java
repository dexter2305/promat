package com.poople.promat.resources;


import com.poople.promat.models.Candidate;
import com.poople.promat.persistence.IStore;
import com.poople.promat.persistence.InMemoryDatabase;
import com.poople.promat.persistence.ormlite.StoreException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;


@Path("candidates")
public class CandidateResource {


    @Context
    private UriInfo uriInfo;

    private IStore db = InMemoryDatabase.INSTANCE;
    private static final Log logger = LogFactory.getLog(CandidateResource.class);

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(@PathParam("id") long id) {
        logger.info("read: for candidate with id = " + id);
        Candidate candidate;
        Response response;
        try {
            candidate = db.read(id);

            if (candidate == null) {
                response = Response.noContent().build();
            } else {
                response = Response.ok(candidate).build();
            }
            logger.info("read: Candidate(id=" + id + ") returned " + response.getStatus());
        } catch (StoreException e) {
            response = Response.serverError().build();
            logger.error("Error while read: Candidate(id=" + id, e);
        }
        return response;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get() {
        logger.info("read: all candidates");
        Collection<Candidate> candidates;
        Response response;
        try {
            candidates = db.readAll();
            if (candidates.isEmpty()) {
                response = Response.noContent().build();
            } else {
                response = Response.ok(candidates).build();
            }
            logger.info("read: all candidates returned " + response.getStatus());
        } catch (StoreException e) {
            logger.error("Error while read: all candidates returned ", e);
            response = Response.serverError().build();
        }
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Candidate candidate) {
        logger.info("create: " + candidate);
        Response response;
        try {
            db.create(candidate);
            response = Response.created(URI.create(uriInfo.getAbsolutePath() + "/" + candidate.getId())).build();
            logger.info("create: " + candidate + " returned " + response.getStatus() + " -> " + response.getLocation().getPath());
        } catch (StoreException e) {
            logger.error("Error while create: " + candidate, e);
            response = Response.serverError().build();
        }
        return response;
    }

    @POST
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Collection<Candidate> apply(Filter filter){
        logger.debug("filter processing can happen here.... ");
        try {
            return db.apply(filter);
        } catch (StoreException e) {
            e.printStackTrace();
        } return null;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")long candidateId){
        logger.info("delete: " + candidateId);
        Response response;
        try {
            db.delete(candidateId);
            response = Response.ok().build();
            logger.info("delete: " + candidateId + " returned " + response.getStatus());
        } catch (StoreException e) {
            logger.error("Error while delete: " + candidateId, e);
            response = Response.serverError().build();
        }
        return response;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Candidate candidate){
        logger.info("update: " + candidate.getId());
        Response response;
        try {
            if (db.read(candidate.getId()) != null){
                db.update(candidate);
                response = Response.ok().build();
            }else{
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
            logger.info("update: " + candidate.getId() + " returned " + response.getStatus());
        } catch (StoreException e) {
            logger.info("Error while update: " + candidate.getId(), e);
            response = Response.serverError().build();
        }
        return response;
    }

}
