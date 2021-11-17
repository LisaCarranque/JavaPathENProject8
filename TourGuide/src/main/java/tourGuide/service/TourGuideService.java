package tourGuide.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.gps.Gps;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.TripPricerProxy;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
public class TourGuideService implements  ITourGuideService {
	private final RewardsService rewardsService;
	private final GpsUtilProxy gpsUtilProxy;
	public final Gps gps;
	boolean testMode = true;

	@Autowired
	TripPricerProxy tripPricerProxy;

	public TourGuideService(RewardsService rewardsService, GpsUtilProxy gpsUtilProxy) {
		this.rewardsService = rewardsService;
		this.gpsUtilProxy = gpsUtilProxy;

		if(testMode) {
			log.info("TestMode enabled");
			log.debug("Initializing users");
			initializeInternalUsers();
			log.debug("Finished initializing users");
		}
		gps = new Gps(this);
		addShutDownHook();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			calculateUserLocation(user);
		return visitedLocation;
	}
	
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}
	
	public List<Provider> getTripDeals(User user, TripPricerProxy tripPricerProxy) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricerProxy.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}
	
	public VisitedLocation calculateUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtilProxy.calculateUserLocation(String.valueOf(user.getUserId()));
		user.addToVisitedLocations(visitedLocation);
		log.info(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	public List<NearByAttractionDto> getNearByAttractions(List<Attraction> attractions,
														  User user, VisitedLocation currentUserLocation,
														  int maxToGrab) {
		List<NearByAttractionDto> nearByAttractions = new ArrayList<>();
		if (!attractions.isEmpty()) {
			for (NearByAttractionDto nearByDto : getClosestAttraction(user, currentUserLocation.location, attractions, maxToGrab)) {
				nearByAttractions.add(nearByDto);
			}
		}
		return nearByAttractions;
	}

	public Iterable<NearByAttractionDto> getClosestAttraction(
			User userDto,
			final Location source,
			final Iterable<Attraction> others,
			int maxToGrab) {
		final List<NearByAttractionDto> distances = new ArrayList<>();
		for (final Attraction attraction : others) {
			distances.add(NearByAttractionDto.builder().attraction(attraction)
					.distance(rewardsService.getDistance(attraction, source))
					.user(userDto).build());
		}
		Collections.sort(distances);
		return distances.subList(0, Math.min(maxToGrab, distances.size()));
	}

	public User setUserPreferences(String userName, UserPreferences userPreferences) {
		User user = getUser(userName);
		user.setUserPreferences(userPreferences);
		return user;
	}

	public HashMap<String, Location> getAllCurrentLocations() {
		HashMap<String, Location> visitedLocations = new HashMap<>();
		List<User> users = getAllUsers();
		for (User user : users) {
			VisitedLocation visitedLocation = user.getLastVisitedLocation();
			visitedLocations.put(user.getUserId().toString(), new Location(visitedLocation.location.latitude,
					visitedLocation.location.longitude));
		}
		return visitedLocations;
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        gps.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		log.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}
	
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
	
}
