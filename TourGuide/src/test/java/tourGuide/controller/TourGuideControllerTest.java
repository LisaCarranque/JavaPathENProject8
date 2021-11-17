package tourGuide.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.service.ITourGuideService;
import tourGuide.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourGuideControllerTest {

    @InjectMocks
    TourGuideController controller;

    @Mock
    ITourGuideService tourGuideService;

    @Mock
    GpsUtilProxy gpsUtilProxy;

    @Test
    public void testController() {
        assertNotNull(controller);
    }

    @Test
    public void get() {
        controller.index();
        verifyZeroInteractions(tourGuideService);
    }

    @Test
    public void getNearbyAttractionsTest()  {
        UUID uuid = UUID.randomUUID();
        User user =  new User(uuid, "001", "0", "@");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
        List<Attraction> attractionList = new ArrayList<>();
        Attraction attraction = new Attraction("Disney", "Disney", "Disney",1D, 1D);
        attractionList.add(attraction);
        List<NearByAttractionDto> attractionDtos = new ArrayList<>();
        NearByAttractionDto attractionDto =  NearByAttractionDto.builder().attraction(attraction).user(user).distance(2D).build();
        attractionDtos.add(attractionDto);

        when(tourGuideService.getUser(any())).thenReturn(user);
        when(tourGuideService.getUserLocation(any())).thenReturn(visitedLocation);
        when(gpsUtilProxy.getAttractions()).thenReturn(attractionList);
        controller.getNearbyAttractions(any());

        verify(tourGuideService, times(1)).getUserLocation(any());
        verify(gpsUtilProxy, times(1)).getAttractions();
    }

    @Test
    public void getAllCurrentLocationTest()  {
        controller.getAllCurrentLocations();
        verify(tourGuideService, times(1)).getAllCurrentLocations();
    }

    @Test
    public void getLocationTest()  {
        UUID uuid = UUID.randomUUID();
        User user =  new User(uuid, "001", "0", "@");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(uuid, location, new Date());
        when(tourGuideService.getUser(any())).thenReturn(user);
        when(tourGuideService.getUserLocation(any())).thenReturn(visitedLocation);
        controller.getLocation(any());
        verify(tourGuideService, times(1)).getUser(any());
        verify(tourGuideService, times(1)).getUserLocation(any());
    }

    @Test
    public void getRewardsTest() {
        controller.getRewards(anyString());
        verify(tourGuideService, times(1)).getUserRewards(any());
 }

    @Test
    public void getTripDealsTest() {
        controller.getTripDeals(anyString());
        verify(tourGuideService, times(1)).getTripDeals(any(), any());
    }

    @Test
    public void setUserPreferencesTest() {
        UUID uuid = UUID.randomUUID();
        User user =  new User(uuid, "0001", "00", "@mail");
        when(tourGuideService.setUserPreferences(any(), any())).thenReturn(user);
        controller.setUserPreferences(any(), any());
        verify(tourGuideService, times(1)).setUserPreferences(any(), any());
    }

    @Test
    public void getUserTest() {
        controller.getUser(any());
        verify(tourGuideService,times(1)).getUser(any());
    }
}
