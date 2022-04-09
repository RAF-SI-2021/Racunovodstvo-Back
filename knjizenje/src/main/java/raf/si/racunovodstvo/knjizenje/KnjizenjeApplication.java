package raf.si.racunovodstvo.knjizenje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// For testing purpose
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableEurekaClient
public class KnjizenjeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnjizenjeApplication.class, args);
	}

}
