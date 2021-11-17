package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.Provider;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "TripPricer", url = "localhost:9001")
public interface TripPricerProxy {

    @GetMapping("/getPrice")
    public List<Provider> getPrice(@RequestParam String apiKey, @RequestParam UUID attractionId,
                                   @RequestParam int adults, @RequestParam int children,
                                   @RequestParam int nightsStay, @RequestParam int rewardsPoints);

}
