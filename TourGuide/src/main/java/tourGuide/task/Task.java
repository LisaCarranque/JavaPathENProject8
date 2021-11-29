package tourGuide.task;

import lombok.extern.log4j.Log4j2;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.service.RewardsService;
import tourGuide.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * This task is used to improve calculateUserLocation with Thread and ExecutorService
 * This class implements the Callable interface which is similar to Runnable,
 * in that both are designed for classes whose instances are potentially executed by another thread.
 */
@Log4j2
public class Task implements Callable<List<VisitedLocation>> {

    List<User> users;
    GpsUtilProxy gpsUtilProxy;
    RewardsService rewardsService;

    public Task(List<User> users, GpsUtilProxy gpsUtilProxy, RewardsService rewardsService) {
        this.users = users;
        this.gpsUtilProxy = gpsUtilProxy;
        this.rewardsService = rewardsService;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception – if unable to compute a result
     */
    @Override
    public List<VisitedLocation> call() {
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        users.stream().forEach(user -> {
            VisitedLocation visitedLocation = gpsUtilProxy.calculateUserLocation(String.valueOf(user.getUserId()));
            user.addToVisitedLocations(visitedLocation);
            log.info(visitedLocation);
            rewardsService.calculateRewards(user);
            visitedLocations.add(visitedLocation);
        });
        return visitedLocations;
    }
}


