package org.cherno.twapp2.restserv;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Metered;
import org.cherno.twapp2.restserv.rm.Locations;
import org.cherno.twapp2.restserv.rm.ResourcesStatuses;
import org.cherno.twapp2.restserv.rm.Status;
import org.cherno.twapp2.restserv.rm.SuggestedLocation;
import org.cherno.twapp2.service.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
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
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response status() {
        MetricRegistry metrics = (MetricRegistry) config.getProperty("metricregistry");
        ResourcesStatuses rs = new ResourcesStatuses();
        List<Status> list = new ArrayList<>();
        for (Map.Entry<String, Meter> map : metrics.getMeters().entrySet()) {
            Status status = new Status();
            status.setResource(map.getKey());
            status.setCounter(map.getValue().getCount());
            status.setOneMinuteRate(map.getValue().getOneMinuteRate());
            status.setFiveMinuteRate(map.getValue().getFiveMinuteRate());
            status.setFifteenMinuteRate(map.getValue().getFifteenMinuteRate());
            list.add(status);
        }
        rs.setStatuses(list);

        Response.ResponseBuilder response = Response.status(200);
        response.entity(rs);

        return response.build();
    }

}