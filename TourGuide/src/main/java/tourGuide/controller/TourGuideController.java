package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.service.IRewardsService;
import tourGuide.service.ITourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import java.util.HashMap;
import java.util.List;

@RestController
@EnableFeignClients(clients = {GpsUtilProxy.class, RewardCentralProxy.class, TripPricerProxy.class})
public class TourGuideController {

    @Autowired
    ITourGuideService tourGuideService;

    @Autowired
    IRewardsService rewardsService;

    @Autowired
    private GpsUtilProxy gpsUtilProxy;

    @Autowired
    RewardCentralProxy rewardCentralProxy;

    @Autowired
    TripPricerProxy tripPricerProxy;

    /**
     * This endpoint returns TourGuide welcome information
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * This endpoint returns the location for a user targeted by username
     *
     * @param userName the username of the targeted user
     * @return the current location of the targeted user
     */
    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }


    /**
     * This endpoint gets the closest five tourist attractions to the user no matter how far away they are
     *
     * @param userName the username of the targeted user
     * @return the list of the closest five attractions to the user
     */
    @RequestMapping("/getNearbyAttractions/{userName}")
    public List<NearByAttractionDto> getNearbyAttractions(@PathVariable String userName) {
        User user = getUser(userName);
        VisitedLocation currentUserLocation = tourGuideService.getUserLocation(user);
        List<Attraction> attractions = gpsUtilProxy.getAttractions();
        return tourGuideService.getNearByAttractions(attractions, user, currentUserLocation, 5);
    }

    /**
     * This endpoint returns the rewards of the user targeted by username
     *
     * @param userName the username of the targeted user
     * @return the rewards for this user
     */
    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    /**
     * This endpoint returns the current locations for all users
     *
     * @return the list of the current locations for all users
     */
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        HashMap<String, Location> locationHashMap = tourGuideService.getAllCurrentLocations();
        return JsonStream.serialize(locationHashMap);
    }

    /**
     * This endpoint returns a list of trip deals for the targeted user
     *
     * @param userName the username of the targeted user
     * @return the list of trip deals for this user
     */
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return providers;
    }

    /**
     * This endpoint allows setting new user preferences to the targeted user
     *
     * @param userPreferences the user preferences to set to this user
     * @param userName        the username of the targeted user
     * @return the user with updated user preferences
     */
    @RequestMapping("/setUserPreferences/{userName}")
    public User setUserPreferences(@RequestBody UserPreferences userPreferences, @PathVariable String userName) {
        User user = tourGuideService.setUserPreferences(userName, userPreferences);
        return user;
    }

    /**
     * This method gets a targeted user from username
     *
     * @param userName the username of the targeted user
     * @return the user targeted by username
     */
    public User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }


}