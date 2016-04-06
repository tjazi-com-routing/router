package com.tjazi.routing.router.client;

import com.tjazi.routing.router.messages.endpoint.EndpointToRouterPayloadMessageType;

import java.util.UUID;

/**
 * Created by Krzysztof Wasiak on 30/03/2016.
 */
public interface EndpointToRouterClient {

    void routeMessage(EndpointToRouterPayloadMessageType messageType, UUID targetId, String payload);
}
