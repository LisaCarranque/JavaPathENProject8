package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.service.RewardsService;

@Configuration
public class TourGuideModule {

    @Autowired
    public GpsUtilProxy gpsUtilProxy;

    @Autowired
    public RewardCentralProxy rewardCentralProxy;

    /**
     * This method produces a bean RewardsService to be managed by the Spring container
     * @return the bean RewardsService
     */
    @Bean
    public RewardsService getRewardsService() {
        return new RewardsService(gpsUtilProxy, rewardCentralProxy);
    }

}
