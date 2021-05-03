package org.acme;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rate")
public class RatingResource {

    private Map<Long, Integer> ratings = new HashMap<>();

    private Random r = new Random(1);

    public RatingResource() {
        ratings.put(1L, r.nextInt(5));
        ratings.put(2L, r.nextInt(5));
        ratings.put(3L, r.nextInt(5));
        ratings.put(4L, r.nextInt(5));
        ratings.put(5L, r.nextInt(5));
    }

    @GET
    @Path("/{bookId}")
    @RolesAllowed("Echoer")
    @Produces(MediaType.APPLICATION_JSON)
    public Rate rate(@PathParam("bookId") Long bookId) {
        Integer rating = ratings.getOrDefault(bookId, 0);
        return new Rate(bookId, rating);
    }

    @POST
    @Path("/{bookId}")
    @RolesAllowed("Echoer")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createRate(@PathParam("bookId") Long bookId, Integer rating) {
        ratings.put(bookId, rating);
        return Response.ok().build();
    }
}