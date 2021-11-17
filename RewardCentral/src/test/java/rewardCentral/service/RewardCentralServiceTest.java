package rewardCentral.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rewardCentral.RewardCentral;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RewardCentralServiceTest {

    @InjectMocks
    RewardCentralService rewardCentralService;

    @Mock
    RewardCentral rewardCentral;

    @Test
    public void getAttractionRewardPointsTest() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        rewardCentralService.getAttractionRewardPoints(uuid1, uuid2);
        verify(rewardCentral).getAttractionRewardPoints(uuid1, uuid2);
    }

}
