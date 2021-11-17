package tourGuide;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TourGuideModuleTest {

    @InjectMocks
    TourGuideModule tourGuideModule;

    @Test
    public void loadTourGuideModule() {
        assertNotNull(tourGuideModule);
    }

    @Test
    public void loadRewardService() {
        assertNotNull(tourGuideModule.getRewardsService());
    }
}
