package org.acme;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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

    private AtomicInteger counter = new AtomicInteger(0);

    private boolean misbehave = false;

    private Map<Long, Integer> ratings = new HashMap<>();

    private Random r = new Random(1L);

    public RatingResource() {
        ratings.put(1L, r.nextInt(6));
        ratings.put(2L, r.nextInt(6));
        ratings.put(3L, r.nextInt(6));
        ratings.put(4L, r.nextInt(6));
        ratings.put(5L, r.nextInt(6));
    }

    @GET
    @Path("/misbehave")
    @Produces(MediaType.TEXT_PLAIN)
    public Response misbehave() {
        misbehave = true;
        return Response.ok("Ratings endpoint returns 503 error").build();
    }

    @GET
    @Path("/behave")
    @Produces(MediaType.TEXT_PLAIN)
    public Response behave() {
        misbehave = false;
        return Response.ok("Back to normal").build();
    }

    @GET
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rate(@PathParam("bookId") Long bookId) {
        System.out.println("Request " + counter.incrementAndGet());
        if (!misbehave) {
        Integer rating = ratings.getOrDefault(bookId, 0);
            return Response.ok(new Rate(bookId, rating)).build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/{bookId}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createRate(@PathParam("bookId") Long bookId, Integer rating) {
        ratings.put(bookId, rating);
        return Response.ok().build();
    }
}