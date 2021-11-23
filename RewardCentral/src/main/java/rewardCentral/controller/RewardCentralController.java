package rewardCentral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardCentral.service.RewardCentralService;

import java.util.UUID;

/**
 * This controller is responsible for calculating reward points
 */
@RestController
public class RewardCentralController {

    @Autowired
    RewardCentralService rewardsCentralService;

    /**
     * This endpoint calculates the reward points of an attraction based on the user UUID and the attraction UUID
     * @param attractionId the UUID of the targeted attraction
     * @param userId the UUID of the targeted user
     * @return
     */
    @RequestMapping("/getAttractionRewardPoints")
    public int getAttractionRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId) {
        return rewardsCentralService.getAttractionRewardPoints(attractionId, userId);
    }
}
