package tourGuide;

import java.util.List;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
	
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
        GpsUtil gpsUtil = new GpsUtil();
        User user = getUser(userName);
        VisitedLocation currentUserLocation = tourGuideService.getUserLocation(user);
        List<Attraction> attractions = gpsUtil.getAttractions();
        return tourGuideService.getNearByAttractions(attractions, user, currentUserLocation, 5);
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        return JsonStream.serialize(tourGuideService.getAllCurrentLocations());
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
    	return JsonStream.serialize(providers);
    }

    /**
     * Endpoint UserPrefences : this endpoint allows setting new UserPreferences to the targeted User
     */
    @RequestMapping("/setUserPreferences/{userName}")
    private String setUserPreferences(@RequestBody UserPreferences userPreferences, @PathVariable String userName) {
        User user = tourGuideService.setUserPreferences(userName, userPreferences);
        return JsonStream.serialize(user);
    }

    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}