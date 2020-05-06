package stacs.eden;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class EdenAPI {
    @Autowired
    private ApplicationContext appContext;

    public static void main(String[] args) {
        SpringApplication.run(EdenAPI.class, args);
    }
}
