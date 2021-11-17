package tourGuide.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 1000000000;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;

    @Autowired
    GpsUtilProxy gpsUtilProxy;

    @Autowired
    RewardCentralProxy rewardCentralProxy;

    public RewardsService(GpsUtilProxy gpsUtilProxy, RewardCentralProxy rewardCentralProxy) {
        this.gpsUtilProxy = gpsUtilProxy;
        this.rewardCentralProxy= rewardCentralProxy;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtilProxy.getAttractions();
		List<VisitedLocation> visitedLocations = new ArrayList<>(userLocations);
		visitedLocations.forEach(location -> {
			attractions.stream()
					.filter(attraction -> nearAttraction(location, attraction))
					.forEach(attraction -> addIfNotInUserRewards(user, new UserReward(location, attraction, getRewardPoints(attraction, user))));

		});
	}

    public void calculateRewardsWithPerformance(List<User> users) {

        users.stream().parallel().forEach(user -> {
                    List<VisitedLocation> userLocations = user.getVisitedLocations();
                    List<Attraction> attractions = gpsUtilProxy.getAttractions();
                    List<VisitedLocation> visitedLocations = new ArrayList<>(userLocations);
                    visitedLocations.forEach(location -> {
                        attractions.stream()
                                .filter(attraction -> nearAttraction(location, attraction))
                                .forEach(attraction -> addIfNotInUserRewards(user, new UserReward(location, attraction, getRewardPoints(attraction, user))));

                    });
                }
        );

    }

    public boolean addIfNotInUserRewards(User user, UserReward userReward) {
        List<UserReward> userRewards = user.getUserRewards();
        List<UserReward> userRewardsCopy = new ArrayList<>(userRewards);
        for (UserReward userReward1 : userRewardsCopy) {
            if (userReward1.attraction.attractionName.equals(userReward.attraction.attractionName)) {
                return false;
            }
        }
        userRewards.add(userReward);
        return true;
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    private int getRewardPoints(Attraction attraction, User user) {
        return rewardCentralProxy.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

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

}
