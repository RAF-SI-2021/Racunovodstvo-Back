package raf.si.racunovodstvo.knjizenje.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

public class EurekaContainer extends GenericContainer<EurekaContainer> {

    public EurekaContainer(Network network, int port) {
        super("ghcr.io/raf-si-2021/racunovodstvo-eureka:dev");
        withExposedPorts(port);
        withNetwork(network);
        withNetworkAliases("eureka");
        waitingFor(new HostPortWaitStrategy());
        setStartupAttempts(3);

        addEnv("SERVER_PORT", port + "");
    }
}
