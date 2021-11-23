package tourGuide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.proxies.TripPricerProxy;

import java.util.Locale;

@EnableAutoConfiguration
@SpringBootApplication
@EnableFeignClients(clients = {GpsUtilProxy.class, RewardCentralProxy.class, TripPricerProxy.class})
@ComponentScan(basePackages = {"tourGuide"})
public class Application {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        SpringApplication.run(Application.class, args);
    }

}
