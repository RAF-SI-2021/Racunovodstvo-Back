package raf.si.racunovodstvo.user.integration;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import raf.si.racunovodstvo.user.integration.containers.MySQLMasterContainer;
import raf.si.racunovodstvo.user.integration.containers.RedisContainer;
import raf.si.racunovodstvo.user.integration.network.NetworkHolder;

@Testcontainers
@ActiveProfiles("test")
class BaseIT {

    protected static final RedisContainer redisContainer = new RedisContainer(NetworkHolder.network(), 6379);
    protected static final MySQLMasterContainer mySQLMasterContainer = new MySQLMasterContainer(NetworkHolder.network(), 3306);
    protected static final MySQLContainer mysqlContainer = new MySQLContainer("mysql:8.0.28");

    static {
        mysqlContainer.withReuse(true);

        mysqlContainer.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        String mysqlHost =
            "jdbc:mysql://" + mysqlContainer.getHost() + ":" + mysqlContainer.getMappedPort(3306) + "/" + mysqlContainer.getDatabaseName()
                + "?serverTimezone=UTC";

        registry.add("spring.datasource.url", () -> mysqlHost);
        registry.add("spring.datasource.password", () -> "test");
        registry.add("eureka.client.enabled", () -> "false");
        registry.add("spring.cloud.loadbalancer.ribbon.enabled", () -> "false");
    }
}
