package com.example.finaldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableAspectJAutoProxy
public class FinaldemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinaldemoApplication.class, args);
	}

}
