package tripPricer.service;

import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

public interface ITripPricerService {

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
                                   int children, int nightsStay, int rewardsPoints);

}
