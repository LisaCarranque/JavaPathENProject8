package rewardCentral.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rewardCentral.service.RewardCentralService;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RewardCentralControllerTest {

    @InjectMocks
    RewardCentralController rewardCentralController;

    @Mock
    RewardCentralService rewardCentralService;

    @Test
    public void getAttractionRewardPointsTest() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        rewardCentralController.getAttractionRewardPoints(uuid1, uuid2);
        verify(rewardCentralService).getAttractionRewardPoints(uuid1, uuid2);
    }

}
