package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * This interface is used as proxy for Feign Client Discovery
 * which allows to use the RewardCentral microservice endpoints
 */
@FeignClient(name = "RewardCentral", url = "localhost:9002")
public interface RewardCentralProxy {

    @GetMapping("/getAttractionRewardPoints")
    public int getAttractionRewardPoints(@RequestParam("attractionId") UUID attractionId
            , @RequestParam("userId") UUID userId);

}
