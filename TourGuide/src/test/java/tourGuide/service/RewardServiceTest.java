package tourGuide.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @InjectMocks
    RewardsService rewardsService;
    @Mock
    GpsUtilProxy gpsUtilProxy;
    @Mock
    RewardCentralProxy rewardCentralProxy;
    @Mock
    TripPricerProxy tripPricerProxy;

    @BeforeEach
    public void set() {
        Locale.setDefault(Locale.US);
    }

    @Test
    public void userGetRewards() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction1 = new Attraction("Disney1", "Disney", "Disney", 1D, 1D);
        List<Attraction> attractionList = new ArrayList<>();
        attractionList.add(attraction1);
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocationOutput = new VisitedLocation(user.getUserId(), location, new Date());

        when(gpsUtilProxy.getAttractions()).thenReturn(attractionList);
        when(gpsUtilProxy.calculateUserLocation(user.getUserId().toString())).thenReturn(visitedLocationOutput);

        Attraction attraction = gpsUtilProxy.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.calculateUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.gps.stopTracking();
        assertTrue(userRewards.size() == 1);
    }

/*    @Test
    public void isWithinAttractionProximity() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        Attraction attraction1 = new Attraction("Disney1", "Disney", "Disney", 1D, 1D);
        List<Attraction> attractionList = new ArrayList<>();
        attractionList.add(attraction1);
        Location location = new Location(1D, 1D);
        when(gpsUtilProxy.getAttractions()).thenReturn(attractionList);
        Attraction attraction = gpsUtilProxy.getAttractions().get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }*/

    @Test
    public void nearAllAttractions() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

        Attraction attraction1 = new Attraction("Disney1", "Disney", "Disney", 1D, 1D);
        List<Attraction> attractionList = new ArrayList<>();
        attractionList.add(attraction1);
        Location location = new Location(1D, 1D);

        when(gpsUtilProxy.getAttractions()).thenReturn(attractionList);

        rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
        List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
        tourGuideService.gps.stopTracking();

        assertEquals(gpsUtilProxy.getAttractions().size(), userRewards.size());
    }

    @Test
    public void nearAllAttractionsWithAlreadyOneReward() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(2);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);
        User user = tourGuideService.getAllUsers().get(1);

        double longitude = ThreadLocalRandom.current().nextDouble(-180.0D, 180.0D);
        longitude = Double.parseDouble(String.format("%.6f", longitude));
        double latitude = ThreadLocalRandom.current().nextDouble(-85.05112878D, 85.05112878D);

        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(latitude, longitude), new Date());
        Attraction attraction = new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);

        user.getUserRewards().add(new UserReward(visitedLocation, attraction, 1));

        Attraction attraction1 = new Attraction("Disney1", "Disney", "Disney", 1D, 1D);
        List<Attraction> attractionList = new ArrayList<>();
        attractionList.add(attraction1);
        Location location = new Location(1D, 1D);

        when(gpsUtilProxy.getAttractions()).thenReturn(attractionList);

        rewardsService.calculateRewards(user);
        List<UserReward> userRewards = tourGuideService.getUserRewards(user);
        tourGuideService.gps.stopTracking();

        assertEquals(2, userRewards.size());
    }

}
