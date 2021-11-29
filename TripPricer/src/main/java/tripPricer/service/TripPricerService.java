package tripPricer.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class TripPricerService implements ITripPricerService {

    @Autowired
    TripPricer tripPricer;

    /**
     * This method calculates the prices of the trip of one user according to the user preferences
     *
     * @param apiKey
     * @param attractionId  the UUID of the attraction targeted for this trip
     * @param adults        the number of adults to travel in this trip
     * @param children      the number of children to travel in this trip
     * @param nightsStay    the number of nights for this trip
     * @param rewardsPoints the reward points calculted for this trip
     * @return the result of the library TripPricer's method getPrice which is a list of Provider
     * containing the id of the trip, the name of the provider, and the cost of this trip.
     */
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults,
                                   int children, int nightsStay, int rewardsPoints) {
        log.info("Getting price for apiKey {}, attraction {}, number of adults {}, number of children {}," +
                "number of night stays {}, number of rewards points {}", apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
        return tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }
}
