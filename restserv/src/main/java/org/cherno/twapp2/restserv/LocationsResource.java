package org.cherno.twapp2.restserv;

import org.cherno.twapp2.service.TwappService;
import org.cherno.twapp2.service.TwappServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
@Path("/")
public class LocationsResource {

    @GET
    @Path("locations/{name: [a-zA-Z][a-zA-Z_0-9]*}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response LocationsResource(@DefaultValue("1000") @QueryParam("max_results") int limit,
                                      @DefaultValue("false") @QueryParam("skip_empty") boolean skipEmpty,
                                      @PathParam("name") String name) {

        TwappService twappService = new TwappServiceImpl();
        Locations locations = new Locations();
        locations.setLocations(twappService.getLocations(name, limit, skipEmpty));

        Response response = Response.status(200).entity(locations).header("test-header", "test").build();
        return response;
    }

    @GET
    @Path("slocation/{name: [a-zA-Z][a-zA-Z_0-9]*}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response SuggestedLocationsResource(@DefaultValue("1000") @QueryParam("max_results") int limit,
                                      @DefaultValue("false") @QueryParam("skip_empty") boolean skipEmpty,
                                      @PathParam("name") String name) {

        TwappService twappService = new TwappServiceImpl();
        Locations locations = new Locations();
        SuggestedLocation location = new SuggestedLocation();
        location.setSuggestedLocation(twappService.getSuggestedLocation(name, limit, skipEmpty));

        Response response = Response.status(200).entity(location).header("test-header", "test").build();
        return response;
    }

}