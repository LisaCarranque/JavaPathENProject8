package rewardCentral.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Log4j2
@Service
public class RewardCentralService implements IRewardCentralService {

    @Autowired
    RewardCentral rewardCentral;

    /**
     * This method calculates reward points of an attraction for one user
     *
     * @param attractionId the UUID of the targeted attraction
     * @param userId       the UUID of the targeted user
     * @return the result of the rewardCentral's method getAttractionRewardPoints
     * which is the integer value of the reward points for this tuple (attractionId, userId)
     */
    public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
        log.info("Getting attraction reward points for attraction {} and user {}", attractionId, userId);
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

}
