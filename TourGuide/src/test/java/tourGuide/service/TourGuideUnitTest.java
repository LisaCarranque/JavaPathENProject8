package tourGuide.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourGuideUnitTest {

    @InjectMocks
    TourGuideService tourGuideService;

    @Mock
    GpsUtilProxy gpsUtilProxy;

    @Mock
    RewardCentralProxy rewardCentralProxy;

    @Mock
    RewardsService rewardsService;

    @Mock
    TripPricerProxy tripPricerProxy;

    @Test
    public void setServiceTest() {
        assertNotNull(tourGuideService);
    }

    @Test
    public void getUserRewards() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "0", "0", "0");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
        Attraction attraction = new Attraction("Disney", "Disney", "Disney", 1D, 1D);
        UserReward userReward = new UserReward(visitedLocation, attraction);
        rewardsService.addIfNotInUserRewards(user, userReward);
        assertNotNull(tourGuideService.getUserRewards(user));
    }

    @Test
    public void getUserLocationWhenHasVisitedLocationTest() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "0", "0", "0");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
        user.addToVisitedLocations(visitedLocation);
        assertNotNull(tourGuideService.getUserLocation(user));
    }


    @Test
    public void getUserLocationWhenHasNotVisitedLocationTest() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "0", "0", "0");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
        when(gpsUtilProxy.calculateUserLocation(user.getUserId().toString())).thenReturn(visitedLocation);
        doNothing().when(rewardsService).calculateRewards(user);
        assertNotNull(tourGuideService.getUserLocation(user));
        tourGuideService.getUserLocation(user);
        verify(gpsUtilProxy, times(1)).calculateUserLocation(user.getUserId().toString());
        verify(rewardsService, times(1)).calculateRewards(user);
    }


    @Test
    public void getUserTest() {
        InternalTestHelper.setInternalUserNumber(0);
        tourGuideService.getAllUsers();
        assertNotNull(tourGuideService.getUser("internalUser0"));
    }

    @Test
    public void getAllUsersTest() {
        assertNotNull(tourGuideService.getAllUsers());
    }

    @Test
    public void addUserTest() {
        InternalTestHelper.setInternalUserNumber(100);
        List<User> users = tourGuideService.getAllUsers();
        // assertEquals(users.size()+1, 101);
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "0", "0", "0");
        tourGuideService.addUser(user);
        assertEquals(users.size() + 1, tourGuideService.getAllUsers().size());
    }

    @Test
    public void getTripDeals() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "internalUser0", "0", "0");
        when(tripPricerProxy.getPrice("test-server-api-key", uuid, 1, 0, 1, 0)).thenReturn(new ArrayList<>());
        tourGuideService.getTripDeals(user);
        verify(tripPricerProxy, times(1)).getPrice("test-server-api-key", uuid, 1, 0, 1, 0);
    }

    @Test
    public void calculateUserLocation() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "0", "0", "0");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
        when(gpsUtilProxy.calculateUserLocation(any())).thenReturn(visitedLocation);
//        when(gpsUtilProxy.getAttractions()).thenReturn(new ArrayList<>());
        doNothing().when(rewardsService).calculateRewards(any());
        tourGuideService.calculateUserLocation(user);
    }

    @Test
    public void getNearByAttractions() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "internalUser0", "0", "0");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
        Attraction attraction1 = new Attraction("Disney1", "Disney", "Disney", 1D, 1D);
        Attraction attraction2 = new Attraction("Disney2", "Disney", "Disney", 2D, 1D);
        Attraction attraction3 = new Attraction("Disney3", "Disney", "Disney", 3D, 1D);
        Attraction attraction4 = new Attraction("Disney4", "Disney", "Disney", 4D, 1D);
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(attraction1);
        attractions.add(attraction2);
        attractions.add(attraction3);
        attractions.add(attraction4);

        assertNotNull(tourGuideService.getNearByAttractions(attractions, user, visitedLocation, 2));
    }

    @Test
    public void getClosestAttractionTest() {
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "internalUser0", "0", "0");
        Location location = new Location(10D, 10D);
        Attraction attraction1 = new Attraction("Disney1", "Disney", "Disney", 1D, 1D);
        Attraction attraction2 = new Attraction("Disney2", "Disney", "Disney", 2D, 1D);
        Attraction attraction3 = new Attraction("Disney3", "Disney", "Disney", 3D, 1D);
        Attraction attraction4 = new Attraction("Disney4", "Disney", "Disney", 4D, 1D);
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(attraction1);
        attractions.add(attraction2);
        attractions.add(attraction3);
        attractions.add(attraction4);
        when(rewardsService.getDistance(any(), any())).thenReturn(10D);
        Iterable<NearByAttractionDto> attractionList = tourGuideService.getClosestAttractions(user, location, attractions, 4);
        assertNotNull(attractionList);
    }

    @Test
    public void setUserPreferencesTest() {
        InternalTestHelper.setInternalUserNumber(100);
        List<User> users = tourGuideService.getAllUsers();
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "internalUser0", "0", "0");
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setTripDuration(1);
        tourGuideService.setUserPreferences(user.getUserName(), userPreferences);
        assertNotNull(user.getUserPreferences());
        assertNotNull(user.getUserPreferences().getTripDuration());
    }

    @Test
    public void getAllCurrentLocations() {
        assertNotNull(tourGuideService.getAllCurrentLocations());
    }
}
