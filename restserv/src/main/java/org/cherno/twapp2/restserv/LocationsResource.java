package org.cherno.twapp2.restserv;

import org.cherno.twapp2.service.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created on 08.06.2015.
 */
@Path("/")
public class LocationsResource {

    @Context
    private javax.ws.rs.core.Configuration config;

    @GET
    @Path("locations/{name: [a-zA-Z][a-zA-Z_0-9]*}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response fullListOfLocationsResource(@DefaultValue("1000") @QueryParam("max_results") int limit,
                                      @DefaultValue("false") @QueryParam("skip_empty") boolean skipEmpty,
                                      @PathParam("name") String name) {

        TwappService twappService = (TwappService) config.getProperty("service");

        Locations locations = new Locations();
        LocationsModel locationsModel = twappService.getLocations(name, limit, skipEmpty);
        locations.setLocations(locationsModel.getLocations());


        Response.ResponseBuilder response = Response.status(200);
        response.entity(locations);

        for(Map.Entry<String, Integer> header : locationsModel.getHeaders().entrySet()){
            response.header(header.getKey(), header.getValue());
        }
        return response.build();
    }

    @GET
    @Path("slocation/{name: [a-zA-Z][a-zA-Z_0-9]*}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response suggestedLocationResource(@DefaultValue("1000") @QueryParam("max_results") int limit,
                                      @DefaultValue("false") @QueryParam("skip_empty") boolean skipEmpty,
                                      @PathParam("name") String name) {

        TwappService twappService = (TwappService) config.getProperty("service");

        SuggestedLocation location = new SuggestedLocation();
        SuggestedLocationModel suggestedLocationModel = twappService.getSuggestedLocation(name, limit, true);
        location.setSuggestedLocation(suggestedLocationModel.getSuggestedLocation());
        location.setOptionalLocation(suggestedLocationModel.getOptionalLocation());

        Response.ResponseBuilder response = Response.status(200);
        response.entity(location);

        for(Map.Entry<String, Integer> header : suggestedLocationModel.getHeaders().entrySet()){
            response.header(header.getKey(), header.getValue());
        }
        return response.build();
    }

}