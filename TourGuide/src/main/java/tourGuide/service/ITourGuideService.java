package tourGuide.service;

import tourGuide.dto.NearByAttractionDto;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;

import java.util.HashMap;
import java.util.List;

public interface ITourGuideService {

    public List<UserReward> getUserRewards(User user);

    public VisitedLocation getUserLocation(User user);

    public User getUser(String userName);

    public List<User> getAllUsers();

    public void addUser(User user);

    public List<Provider> getTripDeals(User user, TripPricerProxy tripPricerProxy);

    public VisitedLocation calculateUserLocation(User user);

    public List<NearByAttractionDto> getNearByAttractions(List<Attraction> attractions,
                                                          User user, VisitedLocation currentUserLocation,
                                                          int maxToGrab);

    public Iterable<NearByAttractionDto> getClosestAttraction(
            User userDto,
            final Location source,
            final Iterable<Attraction> others,
            int maxToGrab);

    public User setUserPreferences(String userName, UserPreferences userPreferences);

    public HashMap<String, Location> getAllCurrentLocations();


}
