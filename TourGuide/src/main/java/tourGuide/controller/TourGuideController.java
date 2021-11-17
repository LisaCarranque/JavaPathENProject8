package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.service.ITourGuideService;
import tourGuide.service.RewardsService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import java.util.HashMap;
import java.util.List;

@RestController
public class TourGuideController {

    @Autowired
    ITourGuideService tourGuideService;

    @Autowired
    private GpsUtilProxy gpsUtilProxy;

    @Autowired
    RewardsService rewardsService;

    @Autowired
    RewardCentralProxy rewardCentralProxy;

    @Autowired
    TripPricerProxy tripPricerProxy;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }

    //Get the closest five tourist attractions to the user - no matter how far away they are.
    @RequestMapping("/getNearbyAttractions/{userName}")
    public List<NearByAttractionDto> getNearbyAttractions(@PathVariable String userName) {
        User user = getUser(userName);
        VisitedLocation currentUserLocation = tourGuideService.getUserLocation(user);
        List<Attraction> attractions = gpsUtilProxy.getAttractions();
        return tourGuideService.getNearByAttractions(attractions, user, currentUserLocation, 5);
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        HashMap<String, Location>  locationHashMap = tourGuideService.getAllCurrentLocations();
        return JsonStream.serialize(locationHashMap);
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName), tripPricerProxy);
    	return JsonStream.serialize(providers);
    }

    /**
     * Endpoint UserPrefences : this endpoint allows setting new UserPreferences to the targeted User
     */
    @RequestMapping("/setUserPreferences/{userName}")
    public User setUserPreferences(@RequestBody UserPreferences userPreferences, @PathVariable String userName) {
        User user = tourGuideService.setUserPreferences(userName, userPreferences);
        return user;
    }

    public User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}