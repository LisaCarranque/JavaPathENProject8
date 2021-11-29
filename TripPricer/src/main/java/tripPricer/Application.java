package tripPricer;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Log4j2
@SpringBootApplication
@EnableFeignClients("tripPricer")
public class Application {

    public static void main(String[] args) {
        log.info("Launch TripPricer module");
        SpringApplication.run(Application.class, args);
    }

}
