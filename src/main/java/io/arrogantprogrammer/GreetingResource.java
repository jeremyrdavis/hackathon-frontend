package io.arrogantprogrammer;

import java.net.URI;
import java.util.HashMap;

import com.google.j2objc.annotations.Property;
import io.dapr.client.DaprClient;
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

    private DaprClient daprClient;

    @Property("dapr.pubsub")
    private String PUBSUB_NAME;
    @Property("dapr.kvstore")
    private String KVSTORE_NAME;
    @Property("dapr.http-endpoint")
    private String DAPR_HTTP_ENDPOINT;
    @Property("dapr.api-token")
    private String DAPR_API_TOKEN;
    @Property("dapr.target-appid")
    private String INVOKE_TARGET_APPID;

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
        daprClient.publishEvent(PUBSUB_NAME, helloString, key.toString());
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
