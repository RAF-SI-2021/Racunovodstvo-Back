package raf.si.racunovodstvo.knjizenje.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"test"})
@Configuration
@LoadBalancerClient(name = "knjizenje")
public class FeignConfig {
}
