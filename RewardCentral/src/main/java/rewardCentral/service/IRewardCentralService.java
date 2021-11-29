package rewardCentral.service;

import java.util.UUID;

public interface IRewardCentralService {

    /**
     * This method calculates reward points of an attraction for one user
     *
     * @param attractionId the UUID of the targeted attraction
     * @param userId       the UUID of the targeted user
     * @return the result of the rewardCentral's method getAttractionRewardPoints
     * which is the integer value of the reward points for this tuple (attractionId, userId)
     */
    public int getAttractionRewardPoints(UUID attractionId, UUID userId);

}
