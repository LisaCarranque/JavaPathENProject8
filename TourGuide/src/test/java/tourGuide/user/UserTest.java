package tourGuide.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @InjectMocks
    TourGuideService tourGuideService;

    @InjectMocks
    RewardsService rewardsService;

    List<User> users;

    @BeforeEach
    public void setBefore() {
        users = tourGuideService.getAllUsers();
    }

    @BeforeEach
    public void setAfter() {
        users = new ArrayList<>();
    }

    @Test
    public void testUser() {
        assertNotNull(users);
    }

    @Test
    public void setAndGetUser() {
        User user = new User(UUID.randomUUID(), "name", "phone", "email");
        user.setEmailAddress("email1");
        user.setPhoneNumber("phone1");
        user.setTripDeals(new ArrayList<>());
        user.setUserPreferences(new UserPreferences());
        user.setLatestLocationTimestamp(new Date());
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), location, new Date());
        Attraction attraction = new Attraction("attraction", "city",
                "state", 1D, 1D);
        rewardsService.addIfNotInUserRewards(user, new UserReward(visitedLocation, attraction, 1));
        user.addToVisitedLocations(visitedLocation);
        assertNotNull(user.getUserId());
        assertNotNull(user.getUserPreferences());
        assertNotNull(user.getUserName());
        assertNotNull(user.getEmailAddress());
        assertNotNull(user.getLatestLocationTimestamp());
        assertNotNull(user.getPhoneNumber());
        assertNotNull(user.getTripDeals());
        assertNotNull(user.getUserRewards());
        assertNotNull(user.getVisitedLocations());
        assertNotNull(user.getLastVisitedLocation());
        user.clearVisitedLocations();
        assert (user.getVisitedLocations().isEmpty());


    }
}
