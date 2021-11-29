package tripPricer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;
import tripPricer.service.TripPricerService;

import java.util.List;
import java.util.UUID;

@RestController
public class TripPricerController {

    @Autowired
    TripPricerService tripPricerService;

    /**
     * This endpoints gets a list of providers for a trip to a targeted attraction
     *
     * @param apiKey
     * @param attractionId  the UUID of the attraction targeted for this trip
     * @param adults        the number of adults to travel in this trip
     * @param children      the number of children to travel in this trip
     * @param nightsStay    the number of nights for this trip
     * @param rewardsPoints the reward points calculated for this trip
     * @return the result of the tripPricerService method getPrice which is a list of Provider
     * containing the id of the trip, the name of the provider, and the cost of this trip.
     */
    @RequestMapping("/getPrice")
    public List<Provider> getPrice(@RequestParam("apiKey") String apiKey, @RequestParam("attractionId") UUID attractionId,
                                   @RequestParam("adults") int adults, @RequestParam("children") int children,
                                   @RequestParam("nightsStay") int nightsStay, @RequestParam("rewardsPoints") int rewardsPoints) {
        return tripPricerService.getPrice(apiKey, attractionId, adults,
                children, nightsStay, rewardsPoints);
    }


}
