package raf.si.racunovodstvo.knjizenje.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

import java.time.Duration;

public class UserContainer extends GenericContainer<UserContainer> {

    public UserContainer(Network network, int port) {
        super("ghcr.io/raf-si-2021/racunovodstvo-user:dev");
        withNetwork(network);
        withExposedPorts(port);
        withNetworkAliases("user");
        waitingFor(new HostPortWaitStrategy());
        withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofMillis(25000)));
        setStartupAttempts(3);

        addEnv("SERVER_PORT", port + "");
        addEnv("SPRING_PROFILES_ACTIVE", "prod");
        addEnv("MYSQL_MASTER_HOST", "jdbc:mysql://mysql_db:3306/si?serverTimezone=UTC");
        addEnv("MYSQL_SLAVE0_HOST", "jdbc:mysql://mysql_slave:3306/si?serverTimezone=UTC");
        addEnv("MYSQL_SLAVE1_HOST", "jdbc:mysql://mysql_slave:3306/si?serverTimezone=UTC");
        addEnv("MYSQL_MASTER_ROOT_PASSWORD", "test");
        addEnv("MYSQL_SLAVE_ROOT_PASSWORD", "test");
        addEnv("REDIS_HOST", "redis");
        addEnv("REDIS_PORT", "6379");
        addEnv("REGISTRY_HOST", "eureka");
    }
}
