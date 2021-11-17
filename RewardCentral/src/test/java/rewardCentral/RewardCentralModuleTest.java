package rewardCentral;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RewardCentralModuleTest {

    @InjectMocks
    RewardCentralModule rewardCentralModule;

    @Test
    public void loadRewardCentralModule() {
        assertNotNull(rewardCentralModule);
    }

    @Test
    public void loadRewardCentral() {
        assertNotNull(rewardCentralModule.getRewardCentral());
    }

}
