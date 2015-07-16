# REST service in Java for determining [Twitter](https://twitter.com) user suggested location.

## Build and run
```
$ git clone https://github.com/Cherno7/twapp2.git
$ mvn package
$ cd restserv\target
$ java -jar restserv-1.0 --config config.properties
```

## Configuration
##### All of the following properties must be specified in the .properties file:

* `twitterdao.consumerKey`, `twitterdao.consumerSecret`, `twitterdao.accessToken`, `twitterdao.accessTokenSecret` : twitter REST API credentials. Use https://apps.twitter.com/app/new to register an application and get the credentials.

##### Optional properties (if not specified then default values will be used)
* `restserv.URI` : service endpoint. Example : `restserv.URI = http://localhost:8080/twapp/`
* `twitterdao.followerListURL`, `twitterdao.friendsListURL` : twitter API URI for getting friends and followers lists. Example: `twitterdao.followerListURL = https://api.twitter.com/1.1/followers/list.json` You can specify additional parameters (such as `count`, `skip_status` or `include_user_entities`) for managing a size of the twitter response.
* `twitterdao.querylimit` : Number of the friends and followers records to be processed in a single query.
* `service.geofile` : File containing the cities in [Geonames](http://download.geonames.org/export/dump/) format.
* `twitterdao.cache` : Caching class that implements `org.cherno.twapp2.twitterDAO.cache.TwappCache` interface. (Example :You can use the `org.cherno.twapp2.twitterDAO.cache.TwappCacheJCSImpl` which uses JCS)
* `twitterdao.cachedir` : path to the cache files.
* `twitterdao.cache.eternal` and `twitterdao.cache.ttl` : determining TTL of the cache records. 

## Resources

For all of the following URLs `http://localhost:8080/twapp/` was used as value  for `restserv.URI` property.

* `http://localhost:8080/twapp/locations/{username}`

Returns collection of all friends' and followers' locations for the twitter user specified by {username}.
You can use an additional query parameter `skip_empty=true` to skip the users with empty location fields.

* `http://localhost:8080/twapp/slocation/{username}`

Returns a suggested location for the twitter user specified by {username} in the "{ISO 3166 country code, city name" format. 

* `http://localhost:8080/twapp/status`

Provides server resource statuses (Number of hits, load averages)
