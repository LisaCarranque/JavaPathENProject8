package gpsUtil.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import java.util.List;

public interface IGpsUtilService {

    /**
     * This method calculates the current user location
     *
     * @param userId the UUID of the targeted user
     * @return the current user location
     */
    public VisitedLocation getUserLocation(String userId);

    /**
     * This method gets the list of attractions
     *
     * @return the list of attractions in TourGuide
     */
    public List<Attraction> getAttractions();

}
