package raf.si.racunovodstvo.preduzece.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

public class EurekaContainer extends GenericContainer<EurekaContainer> {

    public EurekaContainer(Network network, int port) {
        super("ghcr.io/raf-si-2021/racunovodstvo-eureka:dev");
        withExposedPorts(port);
        withNetwork(network);
        //withCreateContainerCmdModifier(createContainerCmd -> createContainerCmd.withHostName("eureka"));
        withNetworkAliases("eureka");
        waitingFor(new HostPortWaitStrategy());

        addEnv("SERVER_PORT", port + "");
    }
}
