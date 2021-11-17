package gpsUtil.controller;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import gpsUtil.service.GpsUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GpsUtilController {

    @Autowired
    GpsUtilService gpsUtilService;

    @RequestMapping("/getUserLocation")
    public VisitedLocation getUserLocation(@RequestParam String userId) {
        return gpsUtilService.getUserLocation(userId);
    }

    @RequestMapping("/getAttractions")
    public List<Attraction> getAttractions() {
       return gpsUtilService.getAttractions();
    }
}
