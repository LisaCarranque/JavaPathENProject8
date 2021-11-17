package tourGuide.service;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceTest {

    @InjectMocks
    TourGuideService tourGuideService;
    @Mock
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
    public void getUserLocation() {
        UUID uuid = UUID.fromString("ef99a80f-f40f-49c9-98a9-7c97a5d8b4a6");
        rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);

        List<Attraction> attractionList = new ArrayList<>();
        Attraction attraction = new Attraction("Disney", "Disney", "Disney",1D, 1D);
        attractionList.add(attraction);

        User user = new User(uuid, "jon", "000", "jon@tourGuide.com");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocationOutput = new VisitedLocation(uuid, location, new Date());
        when(gpsUtilProxy.calculateUserLocation(user.getUserId().toString())).thenReturn(visitedLocationOutput);

        VisitedLocation visitedLocation = tourGuideService.calculateUserLocation(user);
        tourGuideService.gps.stopTracking();
        assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    @Test
    public void addUser() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        User retrivedUser = tourGuideService.getUser(user.getUserName());
        User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

        tourGuideService.gps.stopTracking();

        assertEquals(user, retrivedUser);
        assertEquals(user2, retrivedUser2);
    }

    @Test
    public void getAllUsers() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        List<User> allUsers = tourGuideService.getAllUsers();

        tourGuideService.gps.stopTracking();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    public void getLocationUser() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocationOutput = new VisitedLocation(user.getUserId(), location, new Date());

        when(gpsUtilProxy.calculateUserLocation(user.getUserId().toString())).thenReturn(visitedLocationOutput);
        VisitedLocation visitedLocation = tourGuideService.calculateUserLocation(user);

        tourGuideService.gps.stopTracking();

        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    @Test
    public void getNearbyAttractions() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        Attraction attraction1 = new Attraction("Disney1", "Disney", "Disney",1D, 1D);
        Attraction attraction2 = new Attraction("Disney2", "Disney", "Disney",2D, 1D);
        Attraction attraction3 = new Attraction("Disney3", "Disney", "Disney",3D, 1D);
        Attraction attraction4 = new Attraction("Disney4", "Disney", "Disney",4D, 1D);
        Attraction attraction5 = new Attraction("Disney5", "Disney", "Disney",5D, 1D);
        List<Attraction> attractionList = new ArrayList<>();
        attractionList.add(attraction1);
        attractionList.add(attraction2);
        attractionList.add(attraction3);
        attractionList.add(attraction4);
        attractionList.add(attraction5);

        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocationOutput = new VisitedLocation(user.getUserId(), location, new Date());

        when(gpsUtilProxy.getAttractions()).thenReturn(attractionList);
        when(gpsUtilProxy.calculateUserLocation(user.getUserId().toString())).thenReturn(visitedLocationOutput);

        VisitedLocation visitedLocation = tourGuideService.calculateUserLocation(user);

        List<NearByAttractionDto> attractions = tourGuideService.getNearByAttractions(gpsUtilProxy.getAttractions(), user, visitedLocation, 5);

        tourGuideService.gps.stopTracking();

        assertEquals(5, attractions.size());
    }

    @Test
    public void getTripDeals() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy);
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        User user = new User(uuid, "jon", "000", "jon@tourGuide.com");

        List<Provider> providerList = new ArrayList<>();
        Provider provider1 = new Provider(UUID.randomUUID(), "provider1",100);
        Provider provider2 = new Provider(UUID.randomUUID(), "provider2",200);
        Provider provider3 = new Provider(UUID.randomUUID(), "provider3",300);
        Provider provider4 = new Provider(UUID.randomUUID(), "provider4",400);
        Provider provider5 = new Provider(UUID.randomUUID(), "provider5",500);
        Provider provider6 = new Provider(UUID.randomUUID(), "provider6",600);
        Provider provider7 = new Provider(UUID.randomUUID(), "provider7",700);
        Provider provider8 = new Provider(UUID.randomUUID(), "provider8",800);
        Provider provider9 = new Provider(UUID.randomUUID(), "provider9",900);
        Provider provider10 = new Provider(UUID.randomUUID(), "provider10",1000);
        providerList.add(provider1);
        providerList.add(provider2);
        providerList.add(provider3);
        providerList.add(provider4);
        providerList.add(provider5);
        providerList.add(provider6);
        providerList.add(provider7);
        providerList.add(provider8);
        providerList.add(provider9);
        providerList.add(provider10);
        when(tripPricerProxy.getPrice( "test-server-api-key" ,uuid, 1, 0, 1, 0 )).thenReturn(providerList);
        List<Provider> providers = tourGuideService.getTripDeals(user, tripPricerProxy);

        tourGuideService.gps.stopTracking();

        assertEquals(10, providers.size());
    }

    @Test
    public void setUserPreferences() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy);

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
        tourGuideService.setUserPreferences(user.getUserName(),userPreferences);

        tourGuideService.gps.stopTracking();
        assertEquals(user.getUserPreferences().getNumberOfAdults(), 1);
        assertEquals(user.getUserPreferences().getNumberOfChildren(), 1);
        assertEquals(user.getUserPreferences().getAttractionProximity(), 1);
        assertEquals(user.getUserPreferences().getTicketQuantity(), 1);
        assertEquals(user.getUserPreferences().getTripDuration(), 1);
        assertEquals(user.getUserPreferences().getHighPricePoint(), Money.of(1, currency));
        assertEquals(user.getUserPreferences().getLowerPricePoint(), Money.of(0, currency));
    }

    @Test
    public void getAllCurrentLocations() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);

        InternalTestHelper.setInternalUserNumber(100);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy);

        HashMap<String, Location> visitedLocations = tourGuideService.getAllCurrentLocations();

        tourGuideService.gps.stopTracking();
        assertTrue(visitedLocations.size() == 100);
    }
}
