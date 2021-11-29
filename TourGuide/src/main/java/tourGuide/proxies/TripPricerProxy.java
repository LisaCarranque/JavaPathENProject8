package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.Provider;

import java.util.List;
import java.util.UUID;

/**
 * This interface is used as proxy for Feign Client Discovery
 * which allows to use the TripPricer microservice endpoints
 */
@FeignClient(name = "TripPricer", url = "172.22.0.4:9001")
public interface TripPricerProxy {

    @GetMapping("/getPrice")
    public List<Provider> getPrice(@RequestParam("apiKey") String apiKey, @RequestParam("attractionId") UUID attractionId,
                                   @RequestParam("adults") int adults, @RequestParam("children") int children,
                                   @RequestParam("nightsStay") int nightsStay, @RequestParam("rewardsPoints") int rewardsPoints);

}
