package com.tjazi.routing.router.client.utils;

/**
 * Created by Krzysztof Wasiak on 05/04/2016.
 */
public interface RouterQueuesNameGenerator {
    String generateRouterToRouterQueueName(String regionName);

    String generateEndpointToRouterQueueName(String regionName);
}
