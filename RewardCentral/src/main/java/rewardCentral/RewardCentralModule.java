package rewardCentral;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RewardCentralModule {

    /**
     * This method produces a bean RewardCentral to be managed by the Spring container
     * @return the bean RewardCentral
     */
    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }

}
