package gpsUtil;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Log4j2
@SpringBootApplication
@EnableFeignClients("GpsUtil")
public class Application {

    public static void main(String[] args) {
        log.info("launch GpsUtil module");
        SpringApplication.run(Application.class, args);
    }

}
