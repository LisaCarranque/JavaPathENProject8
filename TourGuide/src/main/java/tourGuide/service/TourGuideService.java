package tourGuide.service;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.task.Task;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@EnableFeignClients(clients = {GpsUtilProxy.class, RewardCentralProxy.class, TripPricerProxy.class})
public class TourGuideService implements ITourGuideService {
    private final RewardsService rewardsService;
    private final GpsUtilProxy gpsUtilProxy;
    private final TripPricerProxy tripPricerProxy;
    public final Tracker tracker;
    boolean testMode = true;


    public TourGuideService(RewardsService rewardsService, GpsUtilProxy gpsUtilProxy, TripPricerProxy tripPricerProxy) {
        this.rewardsService = rewardsService;
        this.gpsUtilProxy = gpsUtilProxy;
        this.tripPricerProxy = tripPricerProxy;

        if (testMode) {
            log.info("TestMode enabled");
            log.debug("Initializing users");
            InternalTestHelper.initializeInternalUsers();
            log.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    /**
     * This methods gets the user rewards
     *
     * @param user the selected user
     * @return the user rewards of the targeted user
     */
    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    /**
     * This method gets user location
     *
     * @param user the targeted user
     * @return the user location
     */
    public VisitedLocation getUserLocation(User user) {
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
        return visitedLocation;
    }

    /**
     * This method gets a user by username
     *
     * @param userName the username of the targeted user
     * @return the targeted user
     */
    public User getUser(String userName) {
        return InternalTestHelper.internalUserMap.get(userName);
    }

    /**
     * This methods returns a list of all users
     *
     * @return the list of all users
     */
    public List<User> getAllUsers() {
        return InternalTestHelper.internalUserMap.values().stream().collect(Collectors.toList());
    }

    /**
     * This method adds a new user
     *
     * @param user the targeted user
     */
    public void addUser(User user) {
        if (!InternalTestHelper.internalUserMap.containsKey(user.getUserName())) {
            InternalTestHelper.internalUserMap.put(user.getUserName(), user);
        }
    }

    /**
     * This method gets a list of provider for user's trip according to the user preferences
     *
     * @param user the targeted user
     * @return the list of providers with the id of the trip, the price of the trip, and the name of the provider
     */
    public List<Provider> getTripDeals(User user) {
        log.info("Getting trip deals");
        int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricerProxy.getPrice(InternalTestHelper.tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    public VisitedLocation trackUserLocation(User user) {
        log.info("Tracking user location for user {}", user.getUserName());
        VisitedLocation visitedLocation = gpsUtilProxy.calculateUserLocation(String.valueOf(user.getUserId()));
        user.addToVisitedLocations(visitedLocation);
        log.info(visitedLocation);
        rewardsService.calculateRewards(user);
        return visitedLocation;
    }

    /**
     * This method calculates user location for a list of users with better performances with ExecutorService and Callable Tasks
     *
     * @param users the list of users
     * @return the list of the locations for these users
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    public List<VisitedLocation> trackUsersLocation(List<User> users) throws InterruptedException {
        log.info("Tracking users location");
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<List<User>> list = Lists.partition(users, 2);
        Set<Task> callables = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            int finalI = i;
            Task callable =
                    new Task(list.get(finalI), gpsUtilProxy, rewardsService);
            callables.add(callable);
        }
        List<Future<List<VisitedLocation>>> visitedLocationOutput = executor.invokeAll(callables);
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        visitedLocationOutput.stream().forEach(visitedLocation -> {
            try {
                visitedLocations.addAll(visitedLocation.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
        executor.awaitTermination(1000, TimeUnit.SECONDS);
        log.info("End of main thread");
        return visitedLocations;
    }

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
                                                          int maxToGrab) {
        List<NearByAttractionDto> nearByAttractions = new ArrayList<>();
        if (!attractions.isEmpty()) {
            for (NearByAttractionDto nearByDto : getClosestAttractions(user, currentUserLocation.location, attractions, maxToGrab)) {
                nearByAttractions.add(nearByDto);
            }
        }
        return nearByAttractions;
    }

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
            int maxToGrab) {
        log.info("Getting {} closest attraction from user location with latitude {}" +
                "and longitude {}", maxToGrab, source.latitude, source.longitude);
        final List<NearByAttractionDto> distances = new ArrayList<>();
        for (final Attraction attraction : others) {
            distances.add(NearByAttractionDto.builder().attraction(attraction)
                    .distance(rewardsService.getDistance(attraction, source))
                    .user(userDto).build());
        }
        Collections.sort(distances);
        return distances.subList(0, Math.min(maxToGrab, distances.size()));
    }

    /**
     * This method sets user preferences for one user targeted by username
     *
     * @param userName        the username of the targeted user
     * @param userPreferences the user preferences of the targeted user
     * @return the user with updated user preferences
     */
    public User setUserPreferences(String userName, UserPreferences userPreferences) {
        log.info("Setting user preferences with high price point {}, low price point {}, attraction proximity {}," +
                        " number of adults {}, number of children {}" +
                        "and trip duration {} and ticket quantity {} for user {}", userPreferences.getHighPricePoint(), userPreferences.getLowerPricePoint(),
                userPreferences.getAttractionProximity(), userPreferences.getNumberOfAdults(), userPreferences.getNumberOfChildren(), userPreferences.getTripDuration(),
                userPreferences.getTicketQuantity(), userName);
        User user = getUser(userName);
        user.setUserPreferences(userPreferences);
        return user;
    }

    /**
     * This method gets all current locations for all users
     *
     * @return the HashMap containing the UUID of the user as a String key and
     * the current location of the user as a Location value
     */
    public HashMap<String, Location> getAllCurrentLocations() {
        log.info("Getting all current location for all users");
        HashMap<String, Location> visitedLocations = new HashMap<>();
        List<User> users = getAllUsers();
        for (User user : users) {
            VisitedLocation visitedLocation = user.getLastVisitedLocation();
            visitedLocations.put(user.getUserId().toString(), new Location(visitedLocation.location.latitude,
                    visitedLocation.location.longitude));
        }
        return visitedLocations;
    }

    /**
     * Registers a new virtual-machine shutdown hook.
     * The Java virtual machine shuts down in response to two kinds of events:
     * The program exits normally, when the last non-daemon thread exits or when the exit (equivalently, System.exit) method is invoked, or
     * The virtual machine is terminated in response to a user interrupt, such as typing ^C, or a system-wide event, such as user logoff or system shutdown.
     */
    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }
}
