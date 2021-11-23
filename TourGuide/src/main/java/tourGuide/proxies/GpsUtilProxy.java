package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;

import java.util.List;

/**
 * This interface is used as proxy for Feign Client Discovery
 * which allows to use the GpsUtil microservice endpoints
 */
@FeignClient(name = "GpsUtil", url = "localhost:9003")
public interface GpsUtilProxy {

    @GetMapping("/getUserLocation")
    public VisitedLocation calculateUserLocation(@RequestParam("userId") String userId);

    @GetMapping(value = "/getAttractions")
    public List<Attraction> getAttractions();

}
