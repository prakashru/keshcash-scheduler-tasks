package org.acme.rest.client.multipart.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.rest.client.multipart.CounterBean;

public class CountResource {
	
	@Inject
    CounterBean counter;            


    @GET
    @Path("/counter")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "count: " + counter.get();  
    }

}
