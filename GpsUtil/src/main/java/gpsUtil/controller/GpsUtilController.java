package gpsUtil.controller;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import gpsUtil.service.GpsUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This controller is responsible for getting locations
 */
@RestController
public class GpsUtilController {

    @Autowired
    GpsUtilService gpsUtilService;

    /**
     * This endpoint gets the location of one user targeted by userId
     * @param userId the UUID of the targeted user
     * @return the calculated location for this user
     */
    @RequestMapping("/getUserLocation")
    public VisitedLocation getUserLocation(@RequestParam String userId) {
        return gpsUtilService.getUserLocation(userId);
    }

    /**
     * This endpoint gets the list of the attractions recorded in TourGuide
     * @return the list of the attractions in TourGuide
     */
    @RequestMapping("/getAttractions")
    public List<Attraction> getAttractions() {
        return gpsUtilService.getAttractions();
    }
}
