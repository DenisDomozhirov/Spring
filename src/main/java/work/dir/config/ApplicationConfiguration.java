package work.dir.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Scanner;

@Configuration
@PropertySource("classpath:application.properties")

public class ApplicationConfiguration {

    @Bean
    public Scanner scan(){
        return new Scanner(System.in);
    }
}
