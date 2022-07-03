package raf.si.racunovodstvo.nabavka.integration.containers;

import org.testcontainers.containers.Network;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;

import java.time.Duration;

public class MySQLSlaveContainer extends MySQLContainer {

    public MySQLSlaveContainer(Network network, int port, String hostName) {
        super(network, port, hostName);;

        withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofMillis(20000)));
        addEnv("MYSQL_REPLICATION_MODE", "slave");
        addEnv("MYSQL_ROOT_PASSWORD", "test");
        addEnv("MYSQL_MASTER_HOST", "mysql_db");
        addEnv("MYSQL_MASTER_PORT_NUMBER", "3306");
        addEnv("MYSQL_MASTER_ROOT_PASSWORD", "test");
    }
}
