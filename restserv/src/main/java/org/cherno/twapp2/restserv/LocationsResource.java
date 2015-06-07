package org.cherno.twapp2.restserv;

import org.cherno.twapp2.service.TwappService;
import org.cherno.twapp2.service.TwappServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
@Path("locations/{name}")
public class LocationsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response LocationsResource(@PathParam("name") String name) {

        TwappService twappService = new TwappServiceImpl();
        Locations locations = new Locations();
        locations.setLocations(twappService.getLocations(name));

        Response response = Response.status(200).entity(locations).header("test-header", "test").build();
        return response;
    }
}