package raf.si.racunovodstvo.nabavka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableEurekaClient
@EnableFeignClients
@EnableJpaAuditing
public class NabavkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NabavkaApplication.class, args);
	}

}
