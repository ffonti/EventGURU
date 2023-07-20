package it.polimi.iswpf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class EventGURUApplication {

    private static Environment env; //ok

    public EventGURUApplication(Environment env) {
        EventGURUApplication.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(EventGURUApplication.class, args);
        System.out.println("Server listening on port " + env.getProperty("server.port"));
    }

}
