package com.tjazi.routing.router.client.utils;

/**
 * Created by Krzysztof Wasiak on 05/04/2016.
 */
public class RouterQueuesNameGeneratorImpl implements RouterQueuesNameGenerator {

    private final static String routerQueueNameFormat = "router_%s_%s_queue";

    @Override
    public String generateRouterToRouterQueueName(String regionName) {

        if (regionName == null || regionName.isEmpty()) {
            throw new IllegalArgumentException("regionName is null or empty");
        }

        return String.format(routerQueueNameFormat, regionName, "r2r");
    }

    @Override
    public String generateEndpointToRouterQueueName(String regionName) {

        if (regionName == null || regionName.isEmpty()) {
            throw new IllegalArgumentException("regionName is null or empty");
        }

        return String.format(routerQueueNameFormat, regionName, "e2r");
    }
}
