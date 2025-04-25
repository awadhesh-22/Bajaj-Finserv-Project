package com.bajaj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class
})
public class BajajAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(BajajAppApplication.class, args);
    }
}
