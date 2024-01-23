package com.merge.doongG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class DoongGApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoongGApplication.class, args);
	}

}
