package com.jobnest.reviewsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ReviewsMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewsMsApplication.class, args);
	}

}