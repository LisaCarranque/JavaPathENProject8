package tourGuide.service;

import tourGuide.dto.NearByAttractionDto;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;

import java.util.HashMap;
import java.util.List;

public interface ITourGuideService {

    /**
     * This methods gets the user rewards
     *
     * @param user the selected user
     * @return the user rewards of the targeted user
     */
    public List<UserReward> getUserRewards(User user);

    /**
     * This method gets user location
     *
     * @param user the targeted user
     * @return the user location
     */
    public VisitedLocation getUserLocation(User user);

    /**
     * This method gets users location
     *
     * @param users the targeted users
     * @return the user location
     */
    public List<VisitedLocation> getUsersLocation(List<User> users) throws InterruptedException;

    /**
     * This method gets a user by username
     *
     * @param userName the username of the targeted user
     * @return the targeted user
     */
    public User getUser(String userName);

    /**
     * This methods returns a list of all users
     *
     * @return the list of all users
     */
    public List<User> getAllUsers();
    /**
     * This method adds a new user
     *
     * @param user the targeted user
     */
    public void addUser(User user);

    /**
     * This method gets a list of provider for user's trip according to the user preferences
     *
     * @param user the targeted user
     * @return the list of providers with the id of the trip, the price of the trip, and the name of the provider
     */
    public List<Provider> getTripDeals(User user);

    public VisitedLocation trackUserLocation(User user);

    /**
     * This method calculates user location for a list of users with better performances with ExecutorService and Callable Tasks
     *
     * @param users the list of users
     * @return the list of the locations for these users
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    public List<VisitedLocation> trackUsersLocation(List<User> users) throws InterruptedException;

    /**
     * This method gets the closest attractions to user location
     *
     * @param attractions         the list of attractions to compare
     * @param user                the targeted user
     * @param currentUserLocation the current user location
     * @param maxToGrab           the number of attractions to get
     * @return the list of the closest attractions to the user location
     */
    public List<NearByAttractionDto> getNearByAttractions(List<Attraction> attractions,
                                                          User user, VisitedLocation currentUserLocation,
                                                          int maxToGrab);

    /**
     * This method gets the closest attraction to the user location
     *
     * @param userDto   the targeted user
     * @param source    the location of the user
     * @param others    the list of attractions
     * @param maxToGrab the number of attractions to select
     * @return the list of the five closest attraction to the user location
     * with the targeted user and the value of the distance
     */
    public Iterable<NearByAttractionDto> getClosestAttractions(
            User userDto,
            final Location source,
            final Iterable<Attraction> others,
            int maxToGrab);

    /**
     * This method sets user preferences for one user targeted by username
     *
     * @param userName        the username of the targeted user
     * @param userPreferences the user preferences of the targeted user
     * @return the user with updated user preferences
     */
    public User setUserPreferences(String userName, UserPreferences userPreferences);

    /**
     * This method gets all current locations for all users
     *
     * @return the HashMap containing the UUID of the user as a String key and
     * the current location of the user as a Location value
     */
    public HashMap<String, Location> getAllCurrentLocations();

}
