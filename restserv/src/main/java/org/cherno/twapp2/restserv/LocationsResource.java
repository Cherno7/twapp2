package org.cherno.twapp2.restserv;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
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
    @Metered
    @Path("locations/{name: [a-zA-Z][a-zA-Z_0-9]*}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response fullListOfLocationsResource(@DefaultValue("false") @QueryParam("skip_empty") boolean skipEmpty,
                                      @PathParam("name") String name) {

        TwappService twappService = (TwappService) config.getProperty("service");

        Locations locations = new Locations();
        LocationsModel locationsModel = twappService.getLocations(name, skipEmpty);
        locations.setLocations(locationsModel.getLocations());
        locations.setStatus(locationsModel.getStatus());
        locations.setLimits(twappService.getCurrentTwitterLimits());

        Response.ResponseBuilder response = Response.status(200);
        response.entity(locations);

        return response.build();
    }

     @GET
     @Metered
     @Path("slocation/{name: [a-zA-Z][a-zA-Z_0-9]*}")
     @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
     public Response suggestedLocationResource(@DefaultValue("false") @QueryParam("skip_empty") boolean skipEmpty,
                                               @PathParam("name") String name) {

        TwappService twappService = (TwappService) config.getProperty("service");

        SuggestedLocation location = new SuggestedLocation();
        SuggestedLocationModel suggestedLocationModel = twappService.getSuggestedLocation(name, true);
        location.setSuggestedLocation(suggestedLocationModel.getSuggestedLocation());
        location.setStatus(suggestedLocationModel.getStatus());
        location.setLimits(twappService.getCurrentTwitterLimits());

        Response.ResponseBuilder response = Response.status(200);
        response.entity(location);

        return response.build();
    }
    @GET
    @Path("status/")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public String status() {
        MetricRegistry metrics = (MetricRegistry) config.getProperty("metricregistry");
        String report = "";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Meter> map : metrics.getMeters().entrySet()) {
            sb.append("Resource: ");
            sb.append(map.getKey());
            sb.append("\n");
            sb.append("Hit counter: ");
            sb.append(map.getValue().getCount());
            sb.append("\n");
            sb.append("One minute rate: ");
            sb.append(String.format("%.3f", map.getValue().getOneMinuteRate()));
            sb.append("\n");
            sb.append("Five minute rate: ");
            sb.append(String.format("%.3f", map.getValue().getFiveMinuteRate()));
            sb.append("\n");
            sb.append("Fifteen minute rate: ");
            sb.append(String.format("%.3f", map.getValue().getFifteenMinuteRate()));
            sb.append("\n");
        }
        return sb.toString();
    }

}