package raf.si.racunovodstvo.preduzece.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

public class RedisContainer extends GenericContainer<RedisContainer> {

    public RedisContainer(Network network, int port) {
        super("redis:6.2.5-alpine");
        withExposedPorts(port);
        withNetwork(network);
        withNetworkAliases("redis");
        waitingFor(new HostPortWaitStrategy());
        setStartupAttempts(3);
    }
}
