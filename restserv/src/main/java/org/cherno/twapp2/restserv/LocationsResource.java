package org.cherno.twapp2.restserv;

import org.cherno.twapp2.service.TwappService;
import org.cherno.twapp2.service.TwappServiceImpl;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> data = twappService.getLocations(name, limit, skipEmpty);
        locations.setLocations((List<String>)data.get("Locations"));

        int friendsStatus = (int)data.get("FriendsStatus");
        int followersStatus = (int)data.get("FollowersStatus");
        int followersRemainingLimit = (int)data.get("FollowersLimit");
        int friendsRemainingLimit = (int)data.get("FriendsLimit");

        Response response = Response.status(200).entity(locations)
                .header("twitter-friends-status", friendsStatus)
                .header("twitter-followers-status", followersStatus)
                .header("twitter-friends-limit", friendsRemainingLimit)
                .header("twitter-followers-limit", followersRemainingLimit)
                .build();
        return response;
    }

    @GET
    @Path("slocation/{name: [a-zA-Z][a-zA-Z_0-9]*}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response SuggestedLocationsResource(@DefaultValue("1000") @QueryParam("max_results") int limit,
                                      @DefaultValue("false") @QueryParam("skip_empty") boolean skipEmpty,
                                      @PathParam("name") String name) {

        TwappService twappService = new TwappServiceImpl();
        SuggestedLocation location = new SuggestedLocation();
        Map<String, Object> data = twappService.getSuggestedLocation(name, limit, skipEmpty);
        location.setSuggestedLocation((String)data.get("Location"));
        location.setOptionalLocation((String)data.get("Optional"));

        int friendsStatus = (int)data.get("FriendsStatus");
        int followersStatus = (int)data.get("FollowersStatus");
        int followersRemainingLimit = (int)data.get("FollowersLimit");
        int friendsRemainingLimit = (int)data.get("FriendsLimit");

        Response response = Response.status(200).entity(location)
                .header("twitter-friends-status", friendsStatus)
                .header("twitter-followers-status", followersStatus)
                .header("twitter-friends-limit", friendsRemainingLimit)
                .header("twitter-followers-limit", followersRemainingLimit)
                .build();
        return response;
    }

}