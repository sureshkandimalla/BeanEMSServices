package com.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SpringbootBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringbootBackendApplication.class, args);
  }
}
