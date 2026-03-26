package dev.modzelewski.iggy.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(IggyProperties.class)
public class Application {

  static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
