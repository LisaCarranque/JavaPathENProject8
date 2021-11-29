package tourGuide.service;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
@EnableFeignClients(clients = {GpsUtilProxy.class, RewardCentralProxy.class, TripPricerProxy.class})
public class RewardsService implements IRewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;

    @Autowired
    GpsUtilProxy gpsUtilProxy;

    @Autowired
    RewardCentralProxy rewardCentralProxy;

    public RewardsService(GpsUtilProxy gpsUtilProxy, RewardCentralProxy rewardCentralProxy) {
        this.gpsUtilProxy = gpsUtilProxy;
        this.rewardCentralProxy = rewardCentralProxy;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    /**
     * This method is used to calculate the rewards for a targeted user
     *
     * @param user the targeted user
     */
    public void calculateRewards(User user) {
        log.info("calculating user rewards for user {}", user.getUserName());
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = gpsUtilProxy.getAttractions();
        List<VisitedLocation> visitedLocations = new ArrayList<>(userLocations);
        visitedLocations.forEach(location -> {
            attractions.stream()
                    .filter(attraction -> nearAttraction(location, attraction))
                    .forEach(attraction -> addIfNotInUserRewards(user, new UserReward(location, attraction, getRewardPoints(attraction, user))));

        });
    }

    /**
     * This method is used to calculate the user rewards with better performance thanks to stream, threads, and ExecutorService
     *
     * @param users the list of the targeted users
     * @throws InterruptedException the exception thrown when a thread is waiting, sleeping, or otherwise occupied,
     *                              and the thread is interrupted, either before or during the activity.
     */
    public void calculateUsersRewards(List<User> users) throws InterruptedException {
        log.info("Calculating user rewards for list of users");
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<List<User>> list = Lists.partition(users, 2);
        Set<Callable<String>> callables = new HashSet<Callable<String>>();
        for (int i = 0; i < list.size(); i++) {
            int finalI = i;
            Callable<String> callable = () -> {
                {
                    list.get(finalI).stream().forEach(user -> {
                        List<VisitedLocation> userLocations = user.getVisitedLocations();
                        List<Attraction> attractions = gpsUtilProxy.getAttractions();
                        List<VisitedLocation> visitedLocations = new ArrayList<>(userLocations);
                        visitedLocations.forEach(location -> {
                            attractions.stream()
                                    .filter(attraction -> nearAttraction(location, attraction))
                                    .forEach(attraction -> addIfNotInUserRewards(user, new UserReward(location, attraction, getRewardPoints(attraction, user))));
                        });
                    });
                }
                return null;
            };
            callables.add(callable);
        }
        executor.invokeAll(callables);
        executor.shutdown();
        executor.awaitTermination(1200, TimeUnit.SECONDS);
        log.info("end of main thread");
    }

    /**
     * This method adds a reward in the user rewards if this reward is not already in this list
     *
     * @param user       the targeted user
     * @param userReward the former list of user rewards
     * @return the updated list of the user rewards
     */
    public boolean addIfNotInUserRewards(User user, UserReward userReward) {
        log.info("Adding reward {} for user {}", userReward.getRewardPoints(), user.getUserName());
        List<UserReward> userRewards = user.getUserRewards();
        List<UserReward> userRewardsCopy = new ArrayList<>(userRewards);
        for (UserReward userReward1 : userRewardsCopy) {
            if (userReward1.attraction.attractionName.equals(userReward.attraction.attractionName)) {
                log.info("Reward {} already in user rewards, this reward is not added", userReward.getRewardPoints());
                return false;
            }
        }
        log.info("Reward {} successfully added in user rewards", userReward.getRewardPoints());
        userRewards.add(userReward);
        return true;
    }

    /**
     * This method calculates the distance between two locations
     *
     * @param loc1 the first location
     * @param loc2 the second location
     * @return the distance between the two locations
     */
    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

    /**
     * This method determines if a visitedLocation is near to an attraction
     * according to the proximityBuffer value
     *
     * @param visitedLocation the visitedLocation to evaluate
     * @param attraction      the attraction which is used as reference for this evaluation of distance
     * @return a boolean which indicates if this visitedLocation is near enough from the attraction
     */
    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    /**
     * This method calculates the user's reward points for an attraction
     *
     * @param attraction the UUID of the targeted attraction
     * @param user       the UUID of the user
     * @return the integer value of the reward points for this tuple (attraction, user)
     */
    private int getRewardPoints(Attraction attraction, User user) {
        return rewardCentralProxy.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }


}
