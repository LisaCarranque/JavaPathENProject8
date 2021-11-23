package tourGuide.IT;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tourGuide.TourGuideModule;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TourGuideModule.class})
@EnableAutoConfiguration
@ComponentScan("tourGuide")
@EnableFeignClients(clients = {GpsUtilProxy.class, RewardCentralProxy.class, TripPricerProxy.class})
@Log4j2
public class TestPerformance {

	@Autowired
	TourGuideService tourGuideService;

	@Autowired
	RewardsService rewardsService;

	@Autowired
	private GpsUtilProxy gpsUtilProxy;

	@Autowired
	private RewardCentralProxy rewardCentralProxy;

	@Autowired
	private TripPricerProxy tripPricerProxy;



	@BeforeEach
	public void set() {
		Locale.setDefault(Locale.US);
	}

	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */

	@Test
	public void highVolumeTrackLocation() throws InterruptedException {
		Locale.setDefault(Locale.US);
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		log.info("Number of users :" + InternalTestHelper.getInternalUserNumber());
		rewardsService.setProximityBuffer(10);
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		tourGuideService.calculateUserLocationWithStreamAndExecutorAndTask(allUsers);
		stopWatch.stop();
		tourGuideService.gps.stopTracking();

		for(User user : allUsers) {
			assertTrue(user.getVisitedLocations().size() == 4);
		}

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		log.info("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() throws InterruptedException, ExecutionException {
		rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		log.info("Number of user : "+ InternalTestHelper.getInternalUserNumber());
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		tourGuideService = new TourGuideService(rewardsService, gpsUtilProxy, tripPricerProxy);

	    Attraction attraction = gpsUtilProxy.getAttractions().get(0);
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

		rewardsService.calculateRewardsWithStreamAndExecutor(allUsers);

		List<User> users = new ArrayList<>();
		users = tourGuideService.getAllUsers();
		for(User user : users) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();
		tourGuideService.gps.stopTracking();
		log.info("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	
}
