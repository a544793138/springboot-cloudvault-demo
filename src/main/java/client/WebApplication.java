package client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        final SpringApplication springApplication = new SpringApplication(WebApplication.class);
        springApplication.addListeners(new ApplicationPreparedListener());
        springApplication.run(args);
    }
}
