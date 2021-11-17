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
     *
     * Get user location from gpsUtil
     */
    public VisitedLocation getUserLocation(String userId) {
        Locale.setDefault(Locale.US);
        return gpsUtil.getUserLocation(UUID.fromString(userId));
    }

    /**
     * Get list of attractions from gpsUtil
     * @return
     */
    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }

}
