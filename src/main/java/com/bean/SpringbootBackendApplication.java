package com.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.bean.config.CompanyIdRangeConfig;

@SpringBootApplication
@EnableConfigurationProperties(CompanyIdRangeConfig.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SpringbootBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringbootBackendApplication.class, args);
  }
}
