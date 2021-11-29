package tourGuide.IT;

import lombok.extern.log4j.Log4j2;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableAutoConfiguration
@ComponentScan("tourGuide")
@EnableFeignClients(clients = {GpsUtilProxy.class, RewardCentralProxy.class, TripPricerProxy.class})
@Log4j2
public class TestTourGuideServiceIT {

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    RewardsService rewardsService;

    @Autowired
    private GpsUtilProxy gpsUtilProxy;

    @Autowired
    private RewardCentralProxy rewardCentralProxy;

    @Autowired
    private TripPricerProxy tripPricerProxy;


    @Test
    public void getUserLocation() {
        UUID uuid = UUID.fromString("ef99a80f-f40f-49c9-98a9-7c97a5d8b4a6");
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);

        List<Attraction> attractionList = new ArrayList<>();
        Attraction attraction = new Attraction("Disney", "Disney", "Disney", 1D, 1D);
        attractionList.add(attraction);

        User user = new User(uuid, "jon", "000", "jon@tourGuide.com");

        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();
        Assertions.assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    @Test
    public void addUser() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "mat", "000", "mat@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "mat2", "000", "mat2@tourGuide.com");

        InternalTestHelper.generateUserLocationHistory(user);
        InternalTestHelper.generateUserLocationHistory(user2);

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        User retrivedUser = tourGuideService.getUser(user.getUserName());
        User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

        tourGuideService.tracker.stopTracking();

        Assertions.assertEquals(user, retrivedUser);
        Assertions.assertEquals(user2, retrivedUser2);
    }

    @Test
    public void getAllUsers() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        // InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "leon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "leon2", "000", "jon2@tourGuide.com");

        InternalTestHelper.generateUserLocationHistory(user);
        InternalTestHelper.generateUserLocationHistory(user2);

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        List<User> allUsers = tourGuideService.getAllUsers();

        tourGuideService.tracker.stopTracking();

        Assertions.assertTrue(allUsers.contains(user));
        Assertions.assertTrue(allUsers.contains(user2));
    }

    @Test
    public void getLocationUser() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        //   InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "tom", "000", "jon@tourGuide.com");

        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        tourGuideService.tracker.stopTracking();

        Assertions.assertEquals(user.getUserId(), visitedLocation.userId);
    }

    @Test
    public void getNearbyAttractions() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        //     InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        Attraction attraction1 = new Attraction("Disney1", "Disney", "Disney", 1D, 1D);
        Attraction attraction2 = new Attraction("Disney2", "Disney", "Disney", 2D, 1D);
        Attraction attraction3 = new Attraction("Disney3", "Disney", "Disney", 3D, 1D);
        Attraction attraction4 = new Attraction("Disney4", "Disney", "Disney", 4D, 1D);
        Attraction attraction5 = new Attraction("Disney5", "Disney", "Disney", 5D, 1D);
        List<Attraction> attractionList = new ArrayList<>();
        attractionList.add(attraction1);
        attractionList.add(attraction2);
        attractionList.add(attraction3);
        attractionList.add(attraction4);
        attractionList.add(attraction5);

        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        List<NearByAttractionDto> attractions = tourGuideService.getNearByAttractions(gpsUtilProxy.getAttractions(), user, visitedLocation, 5);

        tourGuideService.tracker.stopTracking();

        Assertions.assertEquals(5, attractions.size());
    }

    @Test
    public void getTripDeals() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        //    InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);
        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, "jon", "000", "jon@tourGuide.com");

        List<Provider> providers = tourGuideService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();

        Assertions.assertEquals(5, providers.size());
    }

    @Test
    public void setUserPreferences() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);

        //    InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

        CurrencyUnit currency = Monetary.getCurrency("USD");
        User user = tourGuideService.getAllUsers().get(0);
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setAttractionProximity(1);
        userPreferences.setNumberOfAdults(1);
        userPreferences.setNumberOfChildren(1);
        userPreferences.setTicketQuantity(1);
        userPreferences.setTripDuration(1);
        userPreferences.setHighPricePoint(Money.of(1, currency));
        userPreferences.setLowerPricePoint(Money.of(0, currency));
        tourGuideService.setUserPreferences(user.getUserName(), userPreferences);

        tourGuideService.tracker.stopTracking();
        Assertions.assertEquals(user.getUserPreferences().getNumberOfAdults(), 1);
        Assertions.assertEquals(user.getUserPreferences().getNumberOfChildren(), 1);
        Assertions.assertEquals(user.getUserPreferences().getAttractionProximity(), 1);
        Assertions.assertEquals(user.getUserPreferences().getTicketQuantity(), 1);
        Assertions.assertEquals(user.getUserPreferences().getTripDuration(), 1);
        Assertions.assertEquals(user.getUserPreferences().getHighPricePoint(), Money.of(1, currency));
        Assertions.assertEquals(user.getUserPreferences().getLowerPricePoint(), Money.of(0, currency));
    }

    @Test
    public void getAllCurrentLocations() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);

        InternalTestHelper.setInternalUserNumber(100);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

        HashMap<String, Location> visitedLocations = tourGuideService.getAllCurrentLocations();
        tourGuideService.tracker.stopTracking();
        Assertions.assertTrue(visitedLocations.size() == tourGuideService.getAllUsers().size());
    }


}
