package gpsUtil.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Log4j2
@Service
public class GpsUtilService {

    @Autowired
    GpsUtil gpsUtil;

    /**
     * This method calculates the current user location
     * @param userId the UUID of the targeted user
     * @return the current user location
     */
    public VisitedLocation getUserLocation(String userId) {
        Locale.setDefault(Locale.US);
        return gpsUtil.getUserLocation(UUID.fromString(userId));
    }

    /**
     * This method gets the list of attractions
     * @return the list of attractions in TourGuide
     */
    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }

}
