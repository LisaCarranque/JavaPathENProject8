package tourGuide.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserRewardTest {

    @Test
    public void setAndGetUserReward() {
        User user = new User(UUID.randomUUID(), "name", "phone", "email");
        Location location = new Location(1D, 1D);
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), location, new Date());
        Attraction attraction = new Attraction("attraction", "city",
                "state", 1D, 1D);
        UserReward userReward = new UserReward(visitedLocation, attraction, 0);
        userReward.setRewardPoints(1);
        assertEquals(1, userReward.getRewardPoints());
    }
}
