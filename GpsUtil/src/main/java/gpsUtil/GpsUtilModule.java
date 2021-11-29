package gpsUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GpsUtilModule {

    /**
     * This method produces a bean GpsUtil to be managed by the Spring container
     *
     * @return the bean GpsUtil
     */
    @Bean
    public GpsUtil getGpsUtil() {
        return new GpsUtil();
    }

}
