package raf.si.racunovodstvo.nabavka.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

public class RedisContainer extends GenericContainer<RedisContainer> {

    public RedisContainer(Network network, int port) {
        super("redis:6.2.5-alpine");
        withExposedPorts(port);
        withNetwork(network);
        //withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withHostName("redis"));
        withNetworkAliases("redis");
        waitingFor(new HostPortWaitStrategy());
        setStartupAttempts(3);
    }
}
