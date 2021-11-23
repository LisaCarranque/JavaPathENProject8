package tripPricer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TripPricerModule {

    /**
     * This method produces a bean TripPricer to be managed by the Spring container
     * @return the bean TripPricer
     */
    @Bean
    public TripPricer getTripPricer() {
        return new TripPricer();
    }

}
