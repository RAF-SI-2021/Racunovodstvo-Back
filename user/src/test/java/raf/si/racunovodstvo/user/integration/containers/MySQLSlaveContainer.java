package raf.si.racunovodstvo.user.integration.containers;

import org.testcontainers.containers.Network;

public class MySQLSlaveContainer extends MySQLContainer {

    public MySQLSlaveContainer(Network network, int port, String hostName) {
        super(network, port, hostName);;

        addEnv("MYSQL_REPLICATION_MODE", "slave");
        addEnv("MYSQL_ROOT_PASSWORD", "test");
        addEnv("MYSQL_MASTER_HOST", "mysql_db");
        addEnv("MYSQL_MASTER_PORT_NUMBER", "3306");
        addEnv("MYSQL_MASTER_ROOT_PASSWORD", "test");
    }
}
