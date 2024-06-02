package io.arrogantprogrammer;

import java.net.URI;
import java.util.HashMap;

import io.quarkus.logging.Log;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    private static HashMap<Integer,String> greetings = new HashMap<Integer,String>();

    @GET
    public Response hello() {
        Log.debug("hello");
        return Response.ok().entity("Hello from Quarkus REST").build();
    }

    @POST
    public Response addHello(@QueryParam("hello") final String helloString){
        Log.debugf("adding a hello, %s", helloString);
        Integer key = greetings.size() + 1;
        greetings.put(key, helloString);
        return Response.created(URI.create("/" + key)).entity(greetings.get(key)).build();
    }

    @GET
    @Path("/{key}")
    public Response getHello(@PathParam("key") Integer key){
        Log.debugf("Retrieving greeting for %s", key);
        if(greetings.containsKey(key)){
            return Response.ok().entity(greetings.get(key)).build();
        } else {
            return Response.noContent().build();
        }
    }
}
