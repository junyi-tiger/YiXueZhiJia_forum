package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication注解：
 *   相当于以下3个注解：
 *     - @Configuration：将这个类标记为配置源
 *     - @EnableAutoConfiguration： tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 *     - @ComponentScan： tells Spring to look for other components, configurations, and services in the package(com.example.demo), allowing it to find the controllers.
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
